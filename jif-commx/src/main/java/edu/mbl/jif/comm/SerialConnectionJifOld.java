package edu.mbl.jif.comm;

import javax.comm.*;

import java.awt.TextArea;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.TooManyListenersException;
//import edu.mbl.jif.utils.*;

public class SerialConnectionJifOld
        implements SerialPortEventListener, CommPortOwnershipListener {
  Thread                      readThread;
  private OutputStream        os;
  private InputStream         is;
  private CommPortIdentifier  portId;
  static Enumeration          portList;
  private SerialPort          sPort;
  private boolean             open;
  private static StringBuffer recvBuffer;
  private static StringBuffer _recvBuffer;

  // Creates a SerialConnection object and initilizes variables
  public SerialConnectionJifOld() {
    open = false;
    recvBuffer = new StringBuffer();
    _recvBuffer = new StringBuffer();
  }

  ///////////////////////////////////////////////////////////////////////////
  public String getRecvBuffer() {
    int i = 5;
    while (recvBuffer.length() < 1) {
      try {
        Thread.sleep(100);  // wait a quarter of a sec.
      } catch (InterruptedException e) {}
      i--;
      if (i == 0)
        return "###";
    }
    return recvBuffer.toString();
  }

  public String waitToGetRecvBuffer(int t) {
    try {
      Thread.sleep(t);
    } catch (InterruptedException e) {}
    int i = 5;
    while (recvBuffer.length() < 1) {
      try {
        Thread.sleep(t / 2);  // wait a quarter of a sec.
      } catch (InterruptedException e) {}
      i--;
      if (i == 0)
        return "###";
    }
    return recvBuffer.toString();
  }

  public void flushRecvBuffer() {
    recvBuffer.setLength(0);
  }

  ////////////////////////////////////////////////////////////////////////////
  // openConnection
  //
  public void openConnection(String commPort, boolean flowCtrl)
    throws SerialConnectionException {
    // Obtain a CommPortIdentifier object for the port you want to open.
    portList = CommPortIdentifier.getPortIdentifiers();
    while (portList.hasMoreElements()) {
      portId = (CommPortIdentifier) portList.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        if (portId.getName()
                        .equals(commPort)) {
//          System.out.println("portId.getName(): " + portId.getName()
//            + " commPort: " + commPort);
          try {
            sPort = (SerialPort) portId.open("SerialDemo", 1000);
          } catch (PortInUseException e) {
            throw new SerialConnectionException(e.getMessage());
          }
          //System.out.println("Port opened, sPort = " + sPort);
        }
      }
    }

    // Set the parameters of the connection. If they won't set, close the
    // port before throwing an exception.
    if (sPort == null) {
      throw new SerialConnectionException("Error: sPort == null");
    }
    try {
      if (flowCtrl) {
        setConnectionParametersWithFlowCtrl();
      } else {
        setConnectionParametersNoFlowCtrl();
      }
    } catch (SerialConnectionException e) {
      sPort.close();
      throw e;
    }
    if (flowCtrl) {
      // Check to see if Clear-To-Send
      if (!sPort.isCTS()) {
        sPort.close();
        throw new SerialConnectionException("Not Clear-To-Send");
      }
    }

    // Open the input and output streams for the connection. If they won't
    // open, close the port before throwing an exception.
    try {
      os = sPort.getOutputStream();
      is = sPort.getInputStream();
    } catch (IOException e) {
      sPort.close();
      throw new SerialConnectionException("Error opening i/o streams");
    }

    // Add this object as an event listener for the serial port.
    try {
      sPort.addEventListener(this);
    } catch (TooManyListenersException e) {
      sPort.close();
      throw new SerialConnectionException("too many listeners added");
    }

    // Set notifyOnDataAvailable to true to allow event driven input.
    sPort.notifyOnDataAvailable(true);
    // Set notifyOnBreakInterrup to allow event driven break handling.
    sPort.notifyOnBreakInterrupt(true);
    //	sPort.notifyOnCarrierDetect(true);
    //	sPort.notifyOnCTS(true);
    //	sPort.notifyOnDSR(true);
    //	sPort.notifyOnFramingError(true);
    //	sPort.notifyOnOutputEmpty(true);
    //	sPort.notifyOnOverrunError(true);
    //	sPort.notifyOnParityError(true);
    //	sPort.notifyOnRingIndicator(true);
    // Set receive timeout to allow breaking out of polling loop during
    // input handling.
    try {
      sPort.enableReceiveTimeout(30);
    } catch (UnsupportedCommOperationException e) {
      System.out.println("enableReceiveTimeout occurred.");
    }

    // Add ownership listener to allow ownership event handling.
    //portId.addPortOwnershipListener(this);
    open = true;
  }

  /**
   Sets the connection parameters to the setting in the parameters object.
   If set fails return the parameters object to origional settings and
   throw exception.
   */

  //  For VariLC:
  //  setSerialPortParams(9600,
  //                      SerialPort.DATABITS_8,
  //                      SerialPort.STOPBITS_1,
  //                      SerialPort.PARITY_NONE);
  //  setFlowControlMode(SerialPort.FLOWCONTROL_NONE |
  //                     SerialPort.FLOWCONTROL_NONE);
  //
  //
  //  For Zeiss Axiovert 200M:
  //  setSerialPortParams(9600,
  //                      SerialPort.DATABITS_8,
  //                      SerialPort.STOPBITS_1,
  //                      SerialPort.PARITY_NONE);
  //  setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
  //                     SerialPort.FLOWCONTROL_RTSCTS_OUT);
  //
  public void setConnectionParametersNoFlowCtrl()
    throws SerialConnectionException {
    try {
      sPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    } catch (UnsupportedCommOperationException e) {}
    try {
      sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE
        | SerialPort.FLOWCONTROL_NONE);
    } catch (UnsupportedCommOperationException e) {
      throw new SerialConnectionException("Unsupported flow control");
    }
  }

  public void setConnectionParametersWithFlowCtrl()
    throws SerialConnectionException {
    try {
      sPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    } catch (UnsupportedCommOperationException e) {}
    try {
      sPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
        | SerialPort.FLOWCONTROL_RTSCTS_OUT);
      //      sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE
      //        | SerialPort.FLOWCONTROL_NONE);
    } catch (UnsupportedCommOperationException e) {
      throw new SerialConnectionException("Unsupported flow control");
    }

    //    sPort.setDTR(true);
    //    sPort.setRTS(true);
  }

  // Close the port and clean up associated elements
  public void closeConnection() {
    // If port is alread closed just return.
    if (!open) {
      return;
    }

    // Check to make sure sPort has reference to avoid a NPE.
    if (sPort != null) {
      try {
        // close the i/o streams.
        os.close();
        is.close();
      } catch (IOException e) {
        System.err.println(e);
      }

      // Close the port.
      sPort.removeEventListener();
      sPort.close();
      // Remove the ownership listener.
      portId.removePortOwnershipListener(this);
    }
    open = false;
  }

  /**
   Send a one second break signal.
   */
  public void sendBreak() {
    sPort.sendBreak(1000);
  }

  /**
   Reports the open status of the port.
   @return true if port is open, false if port is closed.
   */
  public boolean isOpen() {
    return open;
  }

  /**
   Handles SerialPortEvents... registered to listen for are DATA_AVAILABLE
   and BI. During DATA_AVAILABLE the port buffer is read until it is drained,
   when no more data is availble and 30ms has passed the method returns.
   When a BI event occurs the words BREAK RECEIVED are written.
   */
  public void serialEvent(SerialPortEvent e) {
    StringBuffer inputBuffer = new StringBuffer();
    inputBuffer.setLength(0);
    int newData = 0;
    switch (e.getEventType()) {
      // Read data until -1 is returned. If \r is received substitute
      // \n for correct newline handling.
      case SerialPortEvent.DATA_AVAILABLE :while (newData != -1) {
          try {
            newData = is.read();
            if (newData == -1) {
              break;
            }
            if ('\r' == (char) newData) {
              inputBuffer.append('\n');
            } else {
              inputBuffer.append((char) newData);
            }
          } catch (IOException ex) {
            System.err.println(ex);
            return;
          }
        }

        // System.out.println("Recvd:" + inputBuffer + ".");
        recvBuffer = inputBuffer;
        //messageAreaIn.append(new String(inputBuffer));
        break;
      case SerialPortEvent.BI :System.out.println("\n--- BREAK RECEIVED ---\n");
        break;
      default :System.out.println("\nSerialEvent: " + e.getEventType());
    }
  }

  public void ownershipChange(int type) {
    //		if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
    //			PortRequestedDialog prd = new PortRequestedDialog(parent);
    //		}
  }

  public void write(String outStr) {
    //    for (int i = 0; i < outStr.getBytes().length; i++) {
    //      System.out.println(outStr.getBytes()[i]);
    //    }
    //recvBuffer.setLength(0);
    try {
      os.write(outStr.getBytes());
      //os.flush();
    } catch (IOException e) {
      System.err.println("OutputStream write error: " + e);
      System.out.println("(out) OutputStream write error: " + e);
    }
  }

  public void writeChar(char outChar) {
    recvBuffer.setLength(0);
    try {
      os.write((byte) outChar);
      os.flush();
    } catch (IOException e) {
      System.err.println("OutputStream write error: " + e);
    }
  }

  ////////////////////////////////////////////////////////////////////////
  //
  public void checkControlState() {
    System.out.println("DSR: " + sPort.isDSR());
    System.out.println("DTR: " + sPort.isDTR());
    System.out.println("RTS: " + sPort.isRTS());
    System.out.println("CTS: " + sPort.isCTS());
  }
}
