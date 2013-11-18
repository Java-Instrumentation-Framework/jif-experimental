package edu.mbl.jif.comm.serialtester;

import java.io.*;
import java.util.*;
import gnu.io.*;

/**
 * <p>Title: CommandTest</p>
 * <p>Description: Sends commands strings in HEX as binary, and displays results.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public class CommandTest
    implements SerialPortEventListener {
  private SerialTestResultsDisplay _results;
  private SerialConnection _serialConnection;
  private InputStream _inStream;
  private OutputStream _outStream;

  /**
   * Creates a CommandTest object to be used to send HEX string commands to the serial port.
   * The HEX strings are converted to the binary data.
   * @param serialConnection serial port information.
   * @param results Where to send results to (GUI).
   */
  public CommandTest(SerialConnection serialConnection,
                     SerialTestResultsDisplay results) {
    _serialConnection = serialConnection;
    _results = results;
  }

  /**
   * Clean up the serial streams.
   * */
  public void CleanUp() {
    try {
      if (_inStream != null) {
        _inStream.close();
      }
      if (_outStream != null) {
        _outStream.close();
      }
    }
    catch (IOException e) {

    }
    try {
      _serialConnection.getPort().removeEventListener();
    }
    catch (SerialConnectionException e) {

    }
  }

  /**
   * Send the data and display the results.
   * @param command A string with data in format [0xff][0x01]..., the data
   * is sent as binary, ie [0xff] is sent as 0xff.
   * */
  public void StartTest(String command) throws SerialConnectionException {
    //clean up io
    CleanUp();
    //setup the port
    _serialConnection.openConnection(); //open connection
    _serialConnection.setConnectionParameters(); //reset the connection parameters
    // Open the input and output streams for the connection. If they won't
    // open, close the port before throwing an exception.
    try {
      _outStream = _serialConnection.getPort().getOutputStream();
      _inStream = _serialConnection.getPort().getInputStream();
    }
    catch (IOException e) {
      _serialConnection.getPort().close();
      throw new SerialConnectionException("Error opening i/o streams");
    }
    // Add this object as an event listener for the serial port.
    try {
      _serialConnection.getPort().addEventListener(this);
    }
    catch (TooManyListenersException e) {
      _serialConnection.getPort().close();
      throw new SerialConnectionException("too many listeners added");
    }
    // Set notifyOnDataAvailable to true to allow event driven input.
    _serialConnection.getPort().notifyOnDataAvailable(true);

    // Set notifyOnBreakInterrup to allow event driven break handling.
    _serialConnection.getPort().notifyOnBreakInterrupt(true);
    _serialConnection.getPort().notifyOnFramingError(true);
    _serialConnection.getPort().notifyOnOverrunError(true);
    _serialConnection.getPort().notifyOnParityError(true);

    HexString hexs = new HexString();

    try {
      _outStream.write(hexs.fromHexFormattedString(command));
      //_outStream.write(command.getBytes());
    }
    catch (IOException e) {
      throw new SerialConnectionException("Error writing to i/o streams");
    }
    catch (NumberFormatException ex) {
      _results.updateResponse("\n" + ex.toString() + "\n");
    }

  }


//---------------------------------------------------------------------------
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
          while (_inStream.available() > 0) {
            int numBytes = _inStream.read(readBuffer);
            _results.updateResponse(readBuffer, 0, numBytes);
          }
          //System.out.print(new String(readBuffer));
        }
        catch (IOException e) {
          _results.updateResponse("\n" + e.toString() + "\n");
        }
        break;
    }
  }

}
