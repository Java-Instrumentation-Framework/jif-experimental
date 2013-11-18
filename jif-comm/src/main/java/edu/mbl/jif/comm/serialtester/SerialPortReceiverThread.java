package edu.mbl.jif.comm.serialtester;

import java.io.*;
import java.util.*;
import gnu.io.*;

/**
 * <p>Title: SerialPortReceiverThread</p>
 * <p>Description: Implments the send thread for the Echo test.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public class SerialPortReceiverThread
    implements Runnable, SerialPortEventListener {

  private InputStream _inputStream;
  private ByteArrayOutputStream _savedStream; //save the received data
  private SerialPort _serialPort;
  private Thread _readThread;
  private SerialParameters _serialParams;
  private boolean _keepRunning;
  private SerialTestResultsDisplay _results;

  /**
   * If the serial port can not be openned for receive throws SerialConnectionException
   * @param serialPort which port to use.
   * @param serialParams Parameters for the port.
   * @param results Object to call for status updates, usually a GUI.
   * @throws SerialConnectionException
   */
  public SerialPortReceiverThread(SerialPort serialPort,
                                  SerialParameters serialParams,
                                  SerialTestResultsDisplay results) throws
      SerialConnectionException {

    this._serialPort = serialPort;
    this._serialParams = serialParams;
    _results = results;

    try {
      _inputStream = _serialPort.getInputStream();
    }
    catch (IOException e) {
      _serialPort.close();
      throw new SerialConnectionException("Error opening i/o streams");
    }
    // Add this object as an event listener for the serial port.
    try {
      _serialPort.addEventListener(this);
    }
    catch (TooManyListenersException e) {
      _serialPort.close();
      throw new SerialConnectionException("too many listeners added");
    }

    // Set receive timeout to allow breaking out of polling loop during
    // input handling.
    /*try {
        _serialPort.enableReceiveTimeout(30);
         } catch (UnsupportedCommOperationException e) {
         }*/

    // Set notifyOnDataAvailable to true to allow event driven input.
    _serialPort.notifyOnDataAvailable(true);

    // Set notifyOnBreakInterrup to allow event driven break handling.
    _serialPort.notifyOnBreakInterrupt(true);
    _serialPort.notifyOnFramingError(true);
    _serialPort.notifyOnOverrunError(true);
    _serialPort.notifyOnParityError(true);

    _savedStream = new ByteArrayOutputStream();

    _readThread = new Thread(this);
    _keepRunning = true;
    _readThread.start();
  }

  public void serialEvent(SerialPortEvent event) {
    int ev = event.getEventType();
    switch (ev) {
      case SerialPortEvent.BI:
      case SerialPortEvent.OE:
      case SerialPortEvent.FE:
      case SerialPortEvent.PE:
        _results.updateEvent(ev);
        break;
      case SerialPortEvent.CD:
      case SerialPortEvent.CTS:
      case SerialPortEvent.DSR:
      case SerialPortEvent.RI:
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
        break;
      case SerialPortEvent.DATA_AVAILABLE:
        byte[] readBuffer = new byte[1024];

        try {
          while (_inputStream.available() > 0) {
            int numBytes = _inputStream.read(readBuffer);
            _savedStream.write(readBuffer, 0, numBytes);
            if ( (_savedStream.size() % 4096) == 0) {
              _results.updateBytesReceived(_savedStream.size());
            }
          }
          //System.out.print(new String(readBuffer));
        }
        catch (IOException e) {}
        break;
    }
  }

  public void run() {
    try {
      for (; ; ) {
        Thread.sleep(5000);
        if (!_keepRunning) {
          _serialPort.removeEventListener(); //so we dont' get anymore messages
          return;
        }
      }
    }
    catch (InterruptedException e) {}
    finally {
      _results.threadDone(EchoTestResultsDisplay.RECEIVE_DONE, 0);
    }
  }

  /**
   * Stop receiving on the port.
   */
  public void stopReceiving() {
    _keepRunning = false;
    try {
      _readThread.join();
    }
    catch (InterruptedException e) {}
  }

  /**
   * closes the serial input stream.
   * */
  public void closeio() throws IOException {
    _inputStream.close();
  }

  /**
   * Checks if the data received is equal to the passed in data.
   * @param data to check.
   * */
  public boolean IsReceivedEqual(byte[] sentData) {
    if ( (sentData == null) || (_savedStream == null)) {
      return false;
    }
    _results.updateBytesReceived(_savedStream.size());
    return Arrays.equals(_savedStream.toByteArray(), sentData);
  }

  /**
   * get the number of bytes received.
   * */
  public int getReceived() {
    return _savedStream.size();
  }
}
