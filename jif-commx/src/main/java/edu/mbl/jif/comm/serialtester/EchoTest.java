package edu.mbl.jif.comm.serialtester;
import javax.comm.*;
import java.io.*;

/**
 * <p>Title: EchoTest</p>
 * <p>Description: Handles setting up the send/receive/testmonitor threads used for the Echo test.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public class EchoTest {
  private SerialPortReceiverThread _recvThread;
  private SerialportSendThread _sendThread;
  private EchoTestThread _resultThread;
  private SerialTestResultsDisplay _results;
  private SerialConnection _serialConnection;

  /**
   * Pass the serial port setup and how to display the results for the echo test.
   * @param serialConnection serial port setup information.
   * @param results each thread will call this object so that the progress of the echo
   * test may be updated.
   */
  public EchoTest(SerialConnection serialConnection, SerialTestResultsDisplay results){
    _serialConnection = serialConnection;
    _recvThread = null;
    _sendThread = null;
    _resultThread = null;
    _results = results;
  }

  /**
   * Stops any running threads and closes the serial io streams.
   */
  public void CleanUp(){
    if (_sendThread != null) _sendThread.stopSending();
    if (_recvThread != null) _recvThread.stopReceiving();
    try {
      // close the i/o streams.
      if (_sendThread != null) _sendThread.closeio();
      if (_recvThread != null) _recvThread.closeio();
    } catch (IOException e) {
        System.err.println(e);
    }
  }

  private void StartTestInit() throws SerialConnectionException {
    //clean up io
    CleanUp();
    //setup the port
    _serialConnection.openConnection(); //open connection
    _serialConnection.setConnectionParameters();//reset the connection parameters
    // Open the input and output streams for the connection. If they won't
    // open, close the port before throwing an exception.
    // this is done in the constructors for the send/recv threads
    try {
        _sendThread = new SerialportSendThread(_serialConnection.getPort(), _serialConnection.getParameters(), _results);
        _recvThread = new SerialPortReceiverThread(_serialConnection.getPort(), _serialConnection.getParameters(), _results);
    } catch (SerialConnectionException e) {
        _serialConnection.getPort().close();
        throw e;
    }

  }
  private void StartTestPost() {
    //wait for the send thread to end and then check to see if recv thread has got back
    //everything we sent
    _resultThread = new EchoTestThread(_sendThread, _recvThread, _results);
    _resultThread.StartEchoTest();
  }
   /**
    * Starts the echo test.
    * @param data data to send for echo test.
    * */
    public void StartTest(FileInputStream data) throws SerialConnectionException {
    StartTestInit();
    _sendThread.sendDataStart(data);
    StartTestPost();
  }
  /**
   * Starts the test using a byte array as input.
   * @param data byte array of data to send.
   * */
  public void StartTest(byte[] data) throws SerialConnectionException {
    StartTestInit();
    _sendThread.sendDataStart(data);//start sending the data
    StartTestPost();
  }

  /**
   * Stops the test.
   */
  public void AbortTest() {
    if (_resultThread != null) {
      _resultThread.stopTest();
    }
  }
}
