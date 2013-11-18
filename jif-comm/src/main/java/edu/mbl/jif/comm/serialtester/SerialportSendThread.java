package edu.mbl.jif.comm.serialtester;
import java.io.*;
import java.util.*;
import gnu.io.*;

/**
 * <p>Title: SerialportSendThread</p>
 * <p>Description: Implments the send thread for the echo test.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public class SerialportSendThread implements Runnable{
  private boolean _keepRunning = true;
  private SerialParameters _parameters;
  private CommPortIdentifier _portId;
  private SerialPort _sPort;
  public Thread _sendThread;
  private byte[] _data;
  private FileInputStream _fdata;
  ByteArrayOutputStream _savedStream;
  private OutputStream _os;
  private int _sent;
  private SerialTestResultsDisplay _results;
  private int _chunkSize;

  /**
   *
   * Creates thread to send data, and immediately starts the thread and sends data.
   *
   * @param serialPort
   * @param serialParams
   * @param sendData
   * @throws SerialConnectionException
   *
   */
  public SerialportSendThread(SerialPort serialPort, SerialParameters serialParams, byte[] sendData, SerialTestResultsDisplay results) throws SerialConnectionException {
    _data = sendData;
    init(serialPort, serialParams, results);
    _keepRunning = true;
    _sendThread.start();

  }
  /**
   * Creates thread to send data, thread does not start, call sendDataStart() to begin sending data.
   * @param serialPort
   * @param serialParams
   * @throws SerialConnectionException
   *
   */
  public SerialportSendThread(SerialPort serialPort, SerialParameters serialParams, SerialTestResultsDisplay results) throws SerialConnectionException {
    _data = null;_fdata = null;
    init(serialPort, serialParams, results);
  }

  private void init(SerialPort serialPort, SerialParameters serialParams, SerialTestResultsDisplay results) throws SerialConnectionException {
    _parameters = serialParams;
    _sPort = serialPort;
    _results = results;
    _sendThread = new Thread(this);
    _chunkSize = 4096;
    _savedStream = new ByteArrayOutputStream();
    try {
        _os = _sPort.getOutputStream();
    } catch (IOException e) {
      _sPort.close();
      throw new SerialConnectionException("Error opening i/o streams");
    }

  }
  /**
   *
   * stopSending attempts to stop the thread, then waits for thread to stop.
   *
   */
  public void stopSending() {
    _keepRunning = false;
    try {
      _sendThread.join();
      } catch (InterruptedException e) {}
  }
  /**
   *
   * close the output io stream associated with the send comm port.
   * @throws IOException
   * */
   public void closeio() throws IOException {
    if (_os != null) _os.close();
    if (_fdata != null) _fdata.close();
  }

  public void run() {
    try {
      if (_data != null) {
        sendData(_data);
      }
      else {
        sendData(_fdata);
      }
    } catch (SerialConnectionException e) {
      //display error in gui
    }
    finally {
      _results.threadDone(EchoTestResultsDisplay.SEND_DONE,0);
    }
 }

 /**
  * start to send the data, starts the thread.
  * @param data byte array of data to send.
  */
 public void sendDataStart(byte[] data) {
   _data = data;_fdata = null;
   _keepRunning = true;
   _sendThread.start();
 }
 /**
  * start to send the data, starts the thread.
  * @param data FileInputStream of data to send.
  * */
  public void sendDataStart(FileInputStream data) {
   _fdata = data;
   _data = null;
   _keepRunning = true;
   _sendThread.start();
 }
 private void sendData(FileInputStream data) throws SerialConnectionException{
   int tosend = 0;
   byte[] databuff = new byte[_chunkSize];

   _sent = 0;
   try {
     tosend = data.read(databuff);
     while (tosend != -1) {
       if (!_keepRunning) return;
       _os.write(databuff, 0, tosend);
       _savedStream.write(databuff, 0, tosend);
       _sent += tosend;
       //update GUI
       _results.updateBytesSent(_sent);
       tosend = data.read(databuff);
     }
   }catch(IOException e){
     throw new SerialConnectionException("Error writing to i/o streams");
   }
 }
 private void sendData(byte[] data) throws SerialConnectionException{
   try {
     int size = data.length;
     int sent = 0;
     _sent = 0;
     long starttime = System.currentTimeMillis();
     while (sent < size) {
       int tosend;
       if (!_keepRunning) return;
       if ((sent + _chunkSize)>size) {
         tosend=size - sent;
       }
       else {
         tosend = _chunkSize;
       }
       _os.write(data, sent,tosend);
       sent += tosend;
       _sent = sent;
       //update GUI
       _results.updateBytesSent(sent);
     }
   }
   catch (IOException e){
     throw new SerialConnectionException("Error writing to i/o streams");
   }
 }

 /**
  * Get the number of bytes sent.
  * @return returns the number of bytes sent.
  */
 public int getSent(){
   return _sent;
 }
 /**
  * returns a byte array of the data sent.
  * @return byte array of data sent.
  * */
  public byte[] getData() {
  if (_data != null) return _data;
  return _savedStream.toByteArray();
}
}
