package edu.mbl.jif.laser;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.device.SerialDevice;

/**
 *
Sanger BurstGenerator
A -- Number of Trasitions in burst, 100 (NOT PULSES, MUST BE EVEN NUMBER)
B -- 1 = Invert (O)
C -- Mode (0, 1, 2)?
D -- Pulse width, 500 (in MICROSEC, Min 4; Freq= 2*Pulse width)
4 -- Start Single burst
6 -- Stop Burst
7 -- start continuous
9 -- stop continuous
* -- Save settings as startup default
# -- this Menu
 *
 */
public class BurstGenerator 
extends SerialDevice {

  private InstrumentController instrumentCtrl;

  public BurstGenerator(InstrumentController instrumentCtrl,
          String portOwnerName, String commPortName) {
    super(instrumentCtrl, portOwnerName, commPortName);
    getSerialPortConnection().setTerminatorSend(
            new char[]{SerialPortConnection.CHAR_CR});
    getSerialPortConnection().setTerminatorRecv(
            new char[]{SerialPortConnection.CHAR_CR, SerialPortConnection.CHAR_LF});
   initialize();
  // System.out.println("BurstGenerator setup on " + port.getPortName());
  }

  public void setPulses(long pulses) {
    sendCommand("A", 2 * pulses);
  }

  public void setPulseWidth(long pulseWidth) {
    sendCommand("D", pulseWidth);
  }

  public void start() {
    sendCommand("4");
  }

  public void stop() {
    sendCommand("6");
  }

  @Override
  public void sendCommand(String ss) {
    System.out.println("sendCommand: " + ss);
    char[] s = ss.toCharArray();
    sendChars(s);
  }

  private void sendCommand(char s) {
    System.out.println("sendCommand: " + s);
    sendChars(new char[]{s});
  }

  private void sendCommand(char s, long arg) {
    System.out.println("sendCommand: " + s + ", " + arg);
    sendChars(new char[]{s});
    receiveChars(300);
    try {
      Thread.sleep(250);
    } catch (InterruptedException ex) {
    }
    String a = new Long(arg).toString();
    sendString(a);
    receiveChars(300);
  }

  private void sendCommand(String ss, long arg) {
    System.out.println("sendCommand: " + ss + ", " + arg);
    char[] s = ss.toCharArray();
    sendChars(s);
    receiveChars(300);
    try {
      Thread.sleep(250);
    } catch (InterruptedException ex) {
    }
    String a = new Long(arg).toString();
    sendString(a);
    receiveChars(300);
  }

  public static void main(String[] args) {
    String portName = "COM18";
//        SerialPortConnection port = new SerialPortConnection();
//        port.setPortName(portName);
    BurstGenerator burstGen = new BurstGenerator(null, "BurstGenerator", portName);
    burstGen.getSerialPortConnection().createMonitorFrame();

//        burstGen.setPulseWidth(500);
//        try {
//            Thread.sleep(250);
//        } catch (InterruptedException ex) {
//        }
//        burstGen.setPulses(20);
//        burstGen.start();
  }
}
