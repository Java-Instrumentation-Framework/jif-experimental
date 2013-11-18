package edu.mbl.jif.comm;

import edu.mbl.jif.utils.string.StringUtil;
import java.io.*;
import java.util.TooManyListenersException;
import javax.comm.*;
//import javax.comm.*;

import java.awt.*;
import javax.swing.*;


/**
 * A class to handle communications with a serial connection
 * based on Vern Raben's Telescope code
 */// Defaults:
// "COM1", 9600baud, DATABITS_8, STOPBITS_1, PARITY_NONE, FLOWCONTROL_NONE
public class SerialPortConnection
        implements SerialPortEventListener, CommPortOwnershipListener {
  // Serial port parameters

  SerialParameters parms;
  /* Terminator character/s */
  public static char CHAR_LF = 0xA; // '\n' = LF = 10 = 0xA
  public static char CHAR_CR = 0xD; // '\r' = CR = 13 = 0xD
  private char[] terminatorRecv = {CHAR_CR};
  private char[] terminatorSend = {CHAR_CR};
  /** Output stream for communications */
  private OutputStream os;
  /** Input stream for communications */
  private InputStream is;
  /** Communications port id */
  private CommPortIdentifier portId;
  /** Serial port (Java CommAPI) */
  private SerialPort serialPort;
  /** the Port Owner (e.g. "variLC") */
  private String portOwner;
  /** flag to indicate whehter comm port is open */
  private boolean open = false;
  /** Queue for receiving characters from comm port */
  public MessageQueue messageQueue = new MessageQueue();
  /** Buffer for receiving chars from comm port */
  private StringBuffer inputBuffer = new StringBuffer();
  /** Check every millisecs, while waiting for response */
  private long checkEvery = 10;
  /** Retry, while waiting for response */
  private int retry = 50;
  /** Debug flag */
  private boolean debug = false;
  /** Monitor */
  private SerialPortMonitor monitor = null;
  JFrame monitorFrame;

  /**
   * Create a SerialConnection using DEFAULT settings of
   * 8 bits, 9600 baud, no parity, no flow control, and 1 stop bit on
   * port named COM1 .
   * @todo ???????
   */
  public SerialPortConnection() {
    parms = new SerialParameters();
  }
//    public SerialPortConnection(String port) {
//        parms = new SerialParameters();
//        setPortName(port);
//    }
  // <editor-fold defaultstate="collapsed" desc="<<< Open & Close >>>">
  boolean setParameters = true;

  /**
   * Open a serial connection and IO streams using the current parameters
   * @exception IOException may occur
   */
  public void openConnection(String _portOwner, boolean setParameters) throws IOException {
    this.setParameters = setParameters;
    openConnection(_portOwner);
  }

  public void openConnection(String _portOwner) throws IOException {
    this.portOwner = _portOwner;
    openConnection();
  }

  public void openConnection() throws IOException {
    if (parms.getPortName() == null) {
      System.err.println("Cannot openConnection; No portName set.");
      return;
    }
    try {
      portId = CommPortIdentifier.getPortIdentifier(parms.getPortName());
    } catch (NoSuchPortException e) {
      throw new IOException(e.getMessage());
    }
    try {  // Open the port
      serialPort = (SerialPort) portId.open(portOwner, 3000);
    } catch (PortInUseException e) {
      e.printStackTrace();
      System.err.println("PortInUseException: " + e.currentOwner);
      //e.currentOwner
      throw new IOException(e.getMessage());
    } catch (Exception x) {
      x.printStackTrace();

    }
    try {
      serialPort.addEventListener(this);
    } catch (TooManyListenersException e) {
      serialPort.close();
      throw new IOException("Too many listeners:" + e.getMessage());
    }
    if (setParameters) {
      try {
        setConnectionParameters();
      } catch (SerialConnectionException ex) {
        //ex.printStackTrace();
        throw new IOException("Unsupported parameter:" + ex.getMessage());
      }
    }
    try {
      os = serialPort.getOutputStream();
      is = serialPort.getInputStream();
      os.flush();
    } catch (IOException e) {
      serialPort.close();
      throw e;
    }
    serialPort.notifyOnDataAvailable(true);
    try {
      serialPort.enableReceiveTimeout(30);
    } catch (UnsupportedCommOperationException e) {
      // Ignore
    }
    // Add ownership listener to close connection (to prevent lockout if program hangs)
    portId.addPortOwnershipListener(this);
    open = true;
  }

  /**
   * Close the port and clean up associated elements.
   */
  public void closeConnection() {
    // If port is already closed just return.
    if (!open) {
      return;
    }
    // Close streams if serialPort not null
    if (serialPort != null) {
      try {
        os.close();
        is.close();
      } catch (IOException e) {
        System.err.println(e);
      }
      portId.removePortOwnershipListener(this);
      serialPort.removeEventListener();
      // Close the port.
      serialPort.close();
      System.out.println(">> Port closed");
    }
    open = false;
  }

  /**
   * Check if the serial port is open
   * @return true if port is open, false if port is closed.
   */
  public boolean isOpen() {
    return open;
  }

  /**
   * If the connection is not open, re-open it.
   */
  public boolean reOpenConnection() {
    if (!isOpen()) {
      try {
        this.openConnection(portOwner);
        return true;
      } catch (IOException ex) {
        ex.printStackTrace();
        return false;
      }
    } else {
      return true;
    }
  }
  // </editor-fold>

  public SerialPort getSerialPort() throws SerialConnectionException {
    if (serialPort == null) {
      throw new SerialConnectionException("Port Not Open");
    }
    return serialPort;
  }
  // <editor-fold defaultstate="collapsed" desc="<<< Get/Set Parameters, etc. >>>">

  public SerialParameters getParameters() throws SerialConnectionException {
    if (parms == null) {
      throw new SerialConnectionException("Parameters Not Set");
    }
    return parms;
  }

  /**
   * Sets the connection parameters to the setting in the parameters object.
   * If set fails return the parameters object to origional settings and throws exception.
   */
  public void setConnectionParameters() throws SerialConnectionException {
    // Save state of parameters before trying a set.
    if (serialPort != null) {
      int oldBaudRate = serialPort.getBaudRate();
      int oldDatabits = serialPort.getDataBits();
      int oldStopbits = serialPort.getStopBits();
      int oldParity = serialPort.getParity();
      int oldFlowControl = serialPort.getFlowControlMode();
      // Set connection parameters, if set fails return parameters object
      // to original state.
      try {
        serialPort.setSerialPortParams(
                parms.getBaudRate(), parms.getDatabits(),
                parms.getStopbits(), parms.getParity());
      } catch (UnsupportedCommOperationException e) {
        parms.setBaudRate(oldBaudRate);
        parms.setDatabits(oldDatabits);
        parms.setStopbits(oldStopbits);
        parms.setParity(oldParity);
        System.err.println("UnsupportedCommOperationException:" + e.getMessage());
        throw new SerialConnectionException("Unsupported parameter");
      }
      // Set flow control.
      try {
        serialPort.setFlowControlMode(parms.getFlowControlIn() | parms.getFlowControlOut());
      } catch (UnsupportedCommOperationException e) {
        throw new SerialConnectionException("Unsupported flow control");
      }
    }
  }

  /** --------------------------------------------------------------------------
   * Set name of port to the one specified or else the first one avail. as default.
   * @param portName (example for MS Windows: COM1 or COM2; for Mac: )
   */
  public String setPortName(String portName) {
    String port = SerialUtils.checkPortAvailable(portName);
    if (parms != null) {
      parms.setPortName(port);
      try {
        parms.recall();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return port;
  }

  /**
   * Get the port name being used by this connection
   * @return Current name of the port
   */
  public String getPortName() {
    if (parms != null) {
      return parms.getPortName();
    } else {
      return null;
    }
  }

  /**
   * Set stop bits.
   * @param stopbits New stop bits setting.
   */
  public void setStopbits(int stopbits) {
    if (parms != null) {
      parms.setStopbits(stopbits);
    }
  }

  /**
   * Get stop bits
   * @return Current stop bits.
   */
  public int getStopbits() {
    return parms.getStopbits();
  }

  /**
   * Set parity
   * @param parity New parity setting.
   */
  public void setParity(int parity) {
    if (parms != null) {
      parms.setParity(parity);
    }
  }

  /**
   * Get current parity setting
   * @return parity settting
   */
  public int getParity() {
    return parms.getParity();
  }

  /**
   * Get baud rate
   * @return Current baud rate.
   */
  public int getBaudRate() {
    return parms.getBaudRate();
  }

  /**
   * Set baud rate.
   * @param baudRate New baud rate (9600 is default)
   */
  public void setBaudRate(int baudRate) {
    if (parms != null) {
      parms.setBaudRate(baudRate);
    }

  }

  /**
   * Get data bits
   * @return Current data bits setting.
   */
  public int getDatabits() {
    return parms.getDatabits();
  }

  /**
   * Set new data bits.
   * @param databits New data bits setting.
   */
  public void setDatabits(int databits) {
    if (parms != null) {
      parms.setDatabits(databits);
    }

  }

  /**
   * Set flow control
   * Formed by "or-ing" both "in" and "out" flow control settings.
   * The default is SerialPort.FLOWCONTROL_NONE | SerialPort.FLOWCONTROL_NONE.
   * @see javax.comm.SerialPort for flow control definitions
   * @param flowControl Flow control settings setting
   */
  public void setFlowControl(int flowControl) {
    if (parms != null) {
      parms.setFlowControlIn(flowControl);
    }

  }

  /**
   * Get flow control
   * @return Current flow control setting.
   */
  public int getFlowControl() {
    return parms.getFlowControlIn();
  /** @todo  */
  }
//   /**
//    * Set the serial port connection parameters
//    * @throws IOException may occur
//    */
//   private void setSerialPortConnectionParameters () throws IOException {
//      // Set serial connection parameters
//      try {
//         serialPort.setSerialPortParams(getBaudRate(), getDatabits(),
//                                        getStopbits(), getParity());
//      }
//      catch (UnsupportedCommOperationException e) {
//         throw new IOException("Unsupported serial operation:"
//                               + e.getMessage());
//      }
//      // Set flow control.
//      try {
//         serialPort.setFlowControlMode(getFlowControl());
//      }
//      catch (UnsupportedCommOperationException e) {
//         throw new IOException("Unsupported flow control mode:"
//                               + e.getMessage());
//      }
//   }

  /** --------------------------------------------------------------------------
   * Some special handling setup stuff...
   */
  public void setWait(long _checkEvery, int _retry) {
    checkEvery = _checkEvery;
    retry = _retry;
  }

  public void setTerminatorRecv(char[] _terminator) {
    terminatorRecv = _terminator;
  }

  public void setTerminatorSend(char[] _terminator) {
    terminatorSend = _terminator;
  }

  /** --------------------------------------------------------------------------
   * Debug flag
   * @param debug Set to true to list cmds sent, responses received
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isDebug() {
    return this.debug;
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< serialEvent Handler >>>">

  /**
   * Handle Serial Port Events.
   * @param evt SerialPortEvent
   */
  public void serialEvent(SerialPortEvent evt) {
    int newData = 0;
    // Determine type of event.
    switch (evt.getEventType()) {
      case SerialPortEvent.BI:
      case SerialPortEvent.OE:
      case SerialPortEvent.FE:
      case SerialPortEvent.PE:
      case SerialPortEvent.CD:
      case SerialPortEvent.CTS:
      case SerialPortEvent.DSR:
      case SerialPortEvent.RI:
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
        break;

      case SerialPortEvent.DATA_AVAILABLE:
        while (newData != -1) { // Read data until -1 is returned
          try {
            // +?+?
            if (is.available() > 0) {
            }
            newData = is.read();
            // read() blocks until data avail or end of stream detected
            if (newData == -1) {
              break;
            }
            inputBuffer.append((char) newData);
          //System.out.println(">" + (char) newData);

//            if (newData == this.terminatorRecv) {
//              char[] chars = new char[inputBuffer.length()];
//              inputBuffer.getChars(0, inputBuffer.length(),
//                  chars, 0);
//              messageQueue.addChars(chars);
//              inputBuffer.delete(0, inputBuffer.length());
//            }
          } catch (IOException e) {
            System.err.println(e);
            return;
          }
        }

        // put buffered chars into MessageQueue
        char[] chars = new char[inputBuffer.length()];
        inputBuffer.getChars(0, inputBuffer.length(), chars, 0);
        messageQueue.addChars(chars);
        if (monitor != null) {
          monitor.received(inputBuffer.toString());
        }

        if (chars != null) {
          //System.out.println(">>" + new String(chars));
        }
        inputBuffer.delete(0, inputBuffer.length());
        break;

      /*        case SerialPortEvent.DATA_AVAILABLE:
      byte[] readBuffer = new byte[1024];

      // System.out.println("DATA_AVAILABLE");
      try {
      while (is.available() > 0) {
      // available() does not block
      int numBytes = is.read(readBuffer);
      if (monitor != null) {
      monitor.received(readBuffer, 0, numBytes);
      }

      try {
      //String ascii = new String(data, 0, len, "ANSI_X3.4-1968");
      String ascii = new String(readBuffer, 0, numBytes); //,"ASCII");
      messageQueue.addMessage(ascii);
      // System.out.println("recvd: " + ascii);
      }
      catch (Exception eio) {
      eio.printStackTrace();
      }

      //            System.out.println("bytes: " + numBytes + " - " +
      //                new String(readBuffer));
      }
      //System.out.print(new String(readBuffer));
      }
      catch (IOException e) {
      System.out.println("\n" + e.toString() + "\n");
      }
      break;
       */
    }
  }
  // </editor-fold>

  /**
   * Handle ownershipChange event.
   * When PORT_OWNERSHIP_REQUESTED event is received, close the connection.
   * @param type Type of owernership event
   */
  public void ownershipChange(int type) {
    System.out.print("ownershipChange type: " + type);
    if (type == CommPortOwnershipListener.PORT_OWNED) {
      System.out.println(" (open port)");
    } else if (type == CommPortOwnershipListener.PORT_UNOWNED) {
      System.out.println(" (PORT_UNOWNED, close port)");
    } else if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
      System.out.println(" (PORT_OWNERSHIP_REQUESTED, port is open, in use... closing)");
      // relinquish it
      closeConnection();
    }
  }
  // <editor-fold defaultstate="collapsed" desc="<<< Send & Recieve >>>">

  /** --------------------------------------------------------------------------
   * Send an array of chars to the serial connection
   * @param chars Array of chars
   * @exception IOException may occur
   * *** NO termination char appended
   */
  public void sendChars(char[] chars) throws IOException {

    if ((chars != null) && reOpenConnection()) {
      byte[] bytes = new byte[chars.length];
      for (int i = 0; i < chars.length; i++) {
        bytes[i] = (byte) chars[i];
      }
      if (monitor != null) {
        char[] charArray;
        String s = new String(chars);
        //System.out.println("chars:" + s);
        monitor.sent(s + " (" + displayCharArrayAsHexString(chars) + ")");
      }
      if (isDebug()) {
        System.out.println(parms.getPortName() + ".sendChars:" + displayCharArrayAsHexString(
                chars));
      }
      os.write(bytes);
      os.flush();
    }
  }

  /**
   * Send string to the serial connection
   * @param str The string to send
   * @throws IOException may occur
   * *** Termination char appended
   */
  public void sendString(String str) throws IOException {
    if ((str != null) && (str.length() > 0) && reOpenConnection()) {
      StringBuffer buff = new StringBuffer(str);
      buff.append(new String(terminatorSend));
      //char[] chars = new char[80];
      String str2 = buff.toString();
      //str2.getChars(0, str.length(), chars, 0);
      if (monitor != null) {
        monitor.sent(str);
      }
      if (isDebug()) {
        System.out.println(parms.getPortName() + ".sendString=" + str);
      }
      // System.out.println("chars: " + chars);
      // System.out.println("str.getBytes(): " + str.getBytes());
      try {
        os.write(str2.getBytes());
        os.flush();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void send(String s) {
    try {
      sendString(s);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /** @todo If receiveString is not called, need to dequeue the reply !! *///---------------------------------------------------------------------------
  /**
   * Receive String from serial connection
   * @return String
   */
  public String receiveString() {
    return receiveString((int) (checkEvery * retry));
  }

  public String receiveString(int wait) {
    if (isOpen()) {
      int maxTries = (int) (wait / checkEvery);
      int count = 0;
      while ((count < maxTries) && (messageQueue.isEmpty())) {
        //if (count > 0) {
        //if (isDebug()) {
        //System.out.println(getPortName() + ": waiting:" + count);
        //}
        //}
        count++;
        try {
          Thread.sleep(checkEvery);
        } catch (InterruptedException e) {
        }
      }
    }
    String response = messageQueue.getMessage();
    if (isDebug()) {
      System.out.println(parms.getPortName() + ".receiveString=" + response);
    }
    return response;
  }

  /**
   * Receive character array from serial connection
   * @return char[]
   */
  public char[] receiveChars() {
    if (isOpen()) {
      int count = 0;
      while ((count < retry) && (messageQueue.isEmpty())) {
        //        if (count > 0) {
        //          System.out.print(getPortName() + ": waiting:" + count);
        //        }
        count++;
        try {
          Thread.sleep(checkEvery);
        } catch (InterruptedException e) {
        }
      }
    }
    char[] response = messageQueue.getChars();
    return response;
  }

  public char[] receiveChars(int wait) {
    if (isOpen()) {
      int maxTries = (int) (wait / checkEvery);
      int count = 0;
      while ((count < maxTries) && (messageQueue.isEmpty())) {
        //        if (count > 0) {
        //          System.out.print(getPortName() + ": waiting:" + count);
        //        }
        count++;
        try {
          Thread.sleep(checkEvery);
        } catch (InterruptedException e) {
        }
      }
    }
    char[] response = messageQueue.getChars();
    return response;
  }

  public String recv() {
    return receiveString(500);
  }

  public void clearQueue() {
    messageQueue.clearMessages();
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< Monitor >>>">

  public void setMonitor(SerialPortMonitor _monitor) {
    monitor = _monitor;
  }

  public SerialPortMonitor createMonitorFrame() {
    monitor = new SerialPortMonitor(this);
    monitorFrame = new JFrame();
    monitorFrame.setLayout(new BorderLayout());
    JLabel p = new JLabel();
    p.setText(parms.getPortName() + ": " + parms.getAllString());
    p.setFont(new java.awt.Font("Dialog", Font.PLAIN, 11));
    //panelButton.add(p);
    monitorFrame.add(p, BorderLayout.NORTH);
    monitorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    monitorFrame.add(monitor, BorderLayout.CENTER);
    monitorFrame.pack();
    monitorFrame.setVisible(true);
    return monitor;
  }
  // </editor-fold>

  /** --------------------------------------------------------------------------
   * Convert array of bytes to space separated hex string for debug display
   * @param chars Array of chars
   * @return String for display
   */
  public static String displayCharArrayAsHexString(char[] chars) {
    StringBuffer buf = new StringBuffer();
    if (chars != null) {
      for (int i = 0; i < chars.length; i++) {
        if (i != 0) {
          buf.append(" ");
        }
        buf.append("char[");
        buf.append(i);
        buf.append("]=0x");
        buf.append(Integer.toString(chars[i], 16));
      }
    } else {
      buf.append("chars is null");
    }
    return buf.toString();
  }

  /** --------------------------------------------------------------------------
   *  main, for testing
   */
  public static void main(String[] args) {
    System.out.println("Available Serial Ports: ");
    String[] ps = SerialUtils.getAvailablePorts();
    System.out.println("Ports: " + StringUtil.current().asString(ps));
    //SerialPortConnection serialConnection = new SerialPortConnection();
    SerialPortConnection serialConnection = new SerialPortConnection();
    serialConnection.setPortName("COM3");
    serialConnection.setDebug(true);
    serialConnection.setBaudRate(9600);
    //
    try {
      System.out.println("Test: opening connection.");
      serialConnection.openConnection("Test Instrument");
    } catch (IOException ex1) {
      ex1.printStackTrace();
    }
    if ((serialConnection != null) && (serialConnection.isOpen())) {
      try {
        System.out.println("Test: Sending \"RZ-1000\" ");
        serialConnection.sendString("RZ-1000");
        String response = serialConnection.receiveString();
        System.out.println("Test: Response: " + response);
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
      } finally {
        System.out.println("Test: close connection.");
        serialConnection.closeConnection();
      }
    }
  }
}
