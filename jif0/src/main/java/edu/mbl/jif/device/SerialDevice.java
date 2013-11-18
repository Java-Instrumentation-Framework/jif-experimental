package edu.mbl.jif.device;

import com.jgoodies.binding.beans.Model;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.comm.SerialConnectionException;
import edu.mbl.jif.comm.SerialParameters;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.comm.SerialPortMonitor;
import edu.mbl.jif.comm.SerialUtils;
import edu.mbl.jif.fabric.Application;

import edu.mbl.jif.gui.util.StaticSwingUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * Serial Device used by all jif devices...
 * GBH 2009
 *
 */
public class SerialDevice 
extends Model {
  // ?? enabled ??
  //
  // @todo... instance specific prefs...
  //Preferences prefs = Application.getInstance().getPreferences();

  // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">
  // --- settleTime ---
  public static final String PROPERTYNAME_SETTLETIME = "settleTime";
  static final int SETTLETIME_DEFAULT = 50;    // --- definelatency ---
  public static final String PROPERTYNAME_DEFINELATENCY = "definelatency";
  static final double DEFINELATENCY_DEFAULT = 250;    // --- baudRate ---
  public static final String PROPERTYNAME_BAUDRATE = "baudRate";
  static final int BAUDRATE_DEFAULT = 9600;    // --- commPortName ---
  public static final String PROPERTYNAME_COMMPORTNAME = "commPortName";
//    static final String COMMPORTNAME_DEFAULT = "COM1";
  private int settleTime;
//    PreferencesAdapter settleTimePrefAdapter = new PreferencesAdapter(prefs,
//        SerialDevice.PROPERTYNAME_SETTLETIME, SETTLETIME_DEFAULT);
  private double definelatency;
//    PreferencesAdapter definelatencyPrefAdapter = new PreferencesAdapter(prefs,
//        SerialDevice.PROPERTYNAME_DEFINELATENCY, DEFINELATENCY_DEFAULT);
  private int baudRate;
//    PreferencesAdapter baudRatePrefAdapter = new PreferencesAdapter(prefs,
//        SerialDevice.PROPERTYNAME_BAUDRATE, BAUDRATE_DEFAULT);
  private String commPortName;
//    PreferencesAdapter commPortNamePrefAdapter = new PreferencesAdapter(prefs,
//        SerialDevice.PROPERTYNAME_COMMPORTNAME, COMMPORTNAME_DEFAULT);
  // </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
  private InstrumentController instrumentCtrl;
  private SerialPortConnection serialPortConnection;
  private SerialPortMonitor monitor;
  String portOwnerName = "unknown";
  private boolean initialized = false;
//    public SerialDevice() {
//    }
  boolean setParameters = false;

  public SerialDevice(InstrumentController instrumentCtrl, String portOwnerName,
          String commPortName) { //throws Exception
    this(instrumentCtrl, portOwnerName, commPortName, true);
  }

  public SerialDevice(InstrumentController instrumentCtrl, String portOwnerName,
          String commPortName, boolean setParameters) { //throws Exception
    this.setParameters = setParameters;
    this.instrumentCtrl = instrumentCtrl;
    this.portOwnerName = portOwnerName;
    this.setCommPortName(commPortName);

    Preferences prefs = CamAcqJ.getInstance().getPreferences().node("serialPorts");
//    try {
//      SerialParameters.SerialProperties props =
//              (SerialParameters.SerialProperties) PrefObj.getObject(prefs, "" + commPortName + "");
//    } catch (IOException ex) {
//      Logger.getLogger(SerialDevice.class.getName()).log(Level.SEVERE, null, ex);
//    } catch (BackingStoreException ex) {
//      Logger.getLogger(SerialDevice.class.getName()).log(Level.SEVERE, null, ex);
//    } catch (ClassNotFoundException ex) {
//      Logger.getLogger(SerialDevice.class.getName()).log(Level.SEVERE, null, ex);
//    }


    try {
      serialPortConnection = new SerialPortConnection();
      serialPortConnection.getParameters();
      //serialPortConnection.setConnectionParameters();
      serialPortConnection.setPortName(commPortName);
      serialPortConnection.setBaudRate(BAUDRATE_DEFAULT);
      serialPortConnection.setWait(10L, 50);
      serialPortConnection.setDebug(false);
    } catch (SerialConnectionException serialConnectionException) {
    }

    initialize();
    if (SerialUtils.isPortAvailable(getCommPortName())) {
      try {
        serialPortConnection.openConnection(portOwnerName, setParameters);
        System.out.println("Serial port " + getCommPortName() + " opened for " + portOwnerName);
      } catch (Exception ex1) {
        Application.getInstance().error(
                "Could not open comPort:" + this.commPortName + "for " + portOwnerName + ": " + ex1.getMessage());
      }
    }

  }
  // </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc=" Initialize & Open ">

  public void initialize() {
    boolean tryAgain = true;
    // Check that the serial port is available, if not, prompt to change it.
    while (tryAgain) {
      if (SerialUtils.isPortAvailable(getCommPortName())) {
        serialPortConnection.setPortName(getCommPortName());
        tryAgain = false;
      } else {
        //DialogBox.boxConfirm("Unable to find Serial Port", "Change serial port settings for " + portOwnerName + " ?")
        //JFrame host = Application.getInstance().getHostFrame();
        int result = JOptionPane.showConfirmDialog(
                null,
                "Unable to find Serial Port", "Change serial port settings for " + portOwnerName + " ?",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) {
          openPortSettingsDialog(true);
        } else {
          tryAgain = false;
        }

      }
    }
  }

  public boolean isOpen() {
    if (serialPortConnection == null) {
      return false;
    }
    return serialPortConnection.isOpen();
  }

  // </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc=" Port Setting ">
  public boolean assignCommPort(String commPort) {
    if (SerialUtils.isPortAvailable(commPort)) {
      return true;
    } else {
      // If it is not the serialPortConnection last used, ask user what to do...
      return openPortSettingsDialog(true);
    }
  }

  public SerialPortConnection getSerialPortConnection() {
    return serialPortConnection;
  }

  public String getPortOwnerName() {
    return portOwnerName;
  }

  public void setPortOwnerName(String portOwnerName) {
    this.portOwnerName = portOwnerName;
  }

  public boolean openPortSettingsDialog(boolean onInitialize) {
    getSerialPortConnection().closeConnection();
    String message;
    String title;
    if (onInitialize) {
      title = "Serial port for " + this.portOwnerName + " not found";
      message = "Select alternative port and OK, or Cancel";
    } else {
      title = "Serial Port Settings for " + this.portOwnerName;
      message = "Change Serial Port Settings";
    }
    if (SerialUtils.openPortSettingsDialog(serialPortConnection, title, message)) {
      try {
        String newPort = getSerialPortConnection().getParameters().getPortName();
        setCommPortName(newPort);
      } catch (SerialConnectionException ex) {
        ex.printStackTrace();
      }
      return true;
    } else {
      return false;
    }

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< Setters/Getters>>>">
  /**
   * Gets the current value of settleTime from preferences
   * @return Current value of settleTime
   */
  public int getSettleTime() {
    //settleTime = settleTimePrefAdapter.getInt();
    return settleTime;
  }

  /**
   * Sets the value of settleTime to preferences
   * @param settleTime New value for settleTime
   */
  public void setSettleTime(int settleTime) {
    int oldSettleTime = getSettleTime();
    this.settleTime = settleTime;
    //settleTimePrefAdapter.setInt(settleTime);
    firePropertyChange(PROPERTYNAME_SETTLETIME, oldSettleTime, settleTime);
  }

  /**
   * Gets the current value of definelatency from preferences
   * @return Current value of definelatency
   */
  public double getDefinelatency() {
    //definelatency = definelatencyPrefAdapter.getDouble();
    return definelatency;
  }

  /**
   * Sets the value of definelatency to preferences
   * @param definelatency New value for definelatency
   */
  public void setDefinelatency(double definelatency) {
    double oldDefinelatency = getDefinelatency();
    this.definelatency = definelatency;
    //definelatencyPrefAdapter.setDouble(definelatency);
    firePropertyChange(PROPERTYNAME_DEFINELATENCY, oldDefinelatency, definelatency);
  }

  /**
   * Gets the current value of baudRate from preferences
   * @return Current value of baudRate
   */
  public int getBaudRate() {
    // baudRate = baudRatePrefAdapter.getInt();
    return baudRate;
  }

  /**
   * Sets the value of baudRate to preferences
   * @param baudRate New value for baudRate
   */
  public void setBaudRate(int baudRate) {
    int oldBaudRate = getBaudRate();
    this.baudRate = baudRate;
    //baudRatePrefAdapter.setInt(baudRate);
    firePropertyChange(PROPERTYNAME_BAUDRATE, oldBaudRate, baudRate);
  }

  /**
   * Gets the current value of commPortName from preferences
   * @return Current value of commPortName
   */
  public String getCommPortName() {
    //commPortName = commPortNamePrefAdapter.getString();
    return commPortName;
  }

  /**
   * Sets the value of commPortName to preferences
   * @param commPortName New value for commPortName
   */
  public void setCommPortName(String commPort) {
    String oldCommPort = getCommPortName();
    this.commPortName = commPort;
    //commPortNamePrefAdapter.setString(commPort);
    firePropertyChange(PROPERTYNAME_COMMPORTNAME, oldCommPort, commPort);
  }
// </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc=">>>--- Low Level IO Functions ---<<<" >
  public void sendCommand(String cmd) {
    try {
      //System.out.println("SendCmd: " + cmd);
      serialPortConnection.sendString(cmd);
    } catch (IOException ex) {
      System.err.println("IOException in on SendCmd\n" + ex.getMessage());
    }
  }

  public String sendCommandAndWait(String cmd) {
    serialPortConnection.clearQueue();
    sendCommand(cmd);
    return waitForResponse();
  }

  public String sendCommandAndWait(String cmd, int time) {
    serialPortConnection.clearQueue();
    sendCommand(cmd);
    return waitForResponse(time);
  }

  public void sendString(String s) {
    try {
      serialPortConnection.sendString(s);
    } catch (IOException ex) {
      System.err.println("IOException in sentString\n" + ex.getMessage());
    }
  }

  public void sendChars(char[] ch) {
    try {
      serialPortConnection.sendChars(ch);
    } catch (IOException ex) {
      System.err.println("IOException in on SendCmd\n" + ex.getMessage());
    }
  }

  public char[] receiveChars() {
    return serialPortConnection.receiveChars();
  }

  public char[] receiveChars(int maxWait) {
    return serialPortConnection.receiveChars(maxWait);
  }

  public String waitForResponse(int time) {
    String r = serialPortConnection.receiveString(time);
    //String r = io.receiveString(50);
    //System.out.println("Receive: " + r.trim());
    return r; //r.trim();
  }

  public String waitForResponse() {
    String r = serialPortConnection.receiveString();
    //System.out.println("Receive: " + r.trim());
    return r; //r.trim();
  }
// </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc=" Monitor ">
  public void openMonitor() {
    StaticSwingUtils.dispatchToEDT(new Runnable() {
      public void run() {
        monitor = new SerialPortMonitor(serialPortConnection);
        JFrame monFrame = new JFrame("I/O Monitor for " + portOwnerName);
        monFrame.setSize(new Dimension(200, 400));
        monFrame.setLocation(100, 20);
        setMonitor(monitor);
        monFrame.add(monitor, BorderLayout.CENTER);
        monFrame.setVisible(true);
      }
    });
  }

  public void setMonitor(SerialPortMonitor _monitor) {
    monitor = _monitor;
    serialPortConnection.setMonitor(monitor);
  }

  public void clearMonitor() {
    serialPortConnection.setMonitor(null);
  }
  // </editor-fold>
  //

  public static void main(String[] args) {
    SerialDevice sd = new SerialDevice(null, "test", "COM3");
    sd.initialize();
  }
}
