package edu.mbl.jif.comm.serialtester;
import javax.comm.*;
import java.io.*;
import java.util.TooManyListenersException;

/**
 * <p>Title: SerialConnection</p>
 * <p>Description: Sets up serial port connection</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter, modified from Sun Microsystems example code.
 * @version 1.0
 */

public class SerialConnection implements CommPortOwnershipListener {

  private SerialParameters _parameters;
  private CommPortIdentifier _portId;
  private SerialPort _sPort;
  private boolean open;

  /**
   *
   * @param parameters
   */
  public SerialConnection(SerialParameters parameters) {
    	this._parameters = parameters;
        open = false;
  }

  public SerialPort getPort() throws SerialConnectionException {
    if (_sPort == null) {
      throw new SerialConnectionException("Port Not Open");
    }
    return _sPort;
  }

  public SerialParameters getParameters() throws SerialConnectionException {
    if (_parameters == null) {
      throw new SerialConnectionException("Parameters Not Set");
    }
    return _parameters;
  }
  /**
  Handles ownership events. If a PORT_OWNERSHIP_REQUESTED event is
  received a dialog box is created asking the user if they are
  willing to give up the port. No action is taken on other types
  of ownership events.
  */
  public void ownershipChange(int type) {
      if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {

      }
  }

  /**
  Attempts to open a serial connection and streams using the parameters
  in the SerialParameters object. If it is unsuccesfull at any step it
  returns the port to a closed state, throws a
  <code>SerialConnectionException</code>, and returns.

  Gives a timeout of 30 seconds on the portOpen to allow other applications
  to reliquish the port if have it open and no longer need it.
  */
  public void openConnection() throws SerialConnectionException {

    closeConnection();//clean up existing connection

       // Obtain a CommPortIdentifier object for the port you want to open.
       try {
           _portId =
                CommPortIdentifier.getPortIdentifier(_parameters.getPortName());
       } catch (NoSuchPortException e) {
           throw new SerialConnectionException(e.getMessage());
       }

       // Open the port represented by the CommPortIdentifier object. Give
       // the open call a relatively long timeout of 30 seconds to allow
       // a different application to reliquish the port if the user
       // wants to.
       try {
           _sPort = (SerialPort)_portId.open("SerialTester", 30000);
       } catch (PortInUseException e) {
           throw new SerialConnectionException(e.getMessage());
       }

       // Set the parameters of the connection. If they won't set, close the
       // port before throwing an exception.
       try {
           setConnectionParameters();
       } catch (SerialConnectionException e) {
           _sPort.close();
           throw e;
       }

       // Create a new KeyHandler to respond to key strokes in the
       // messageAreaOut. Add the KeyHandler as a keyListener to the
       // messageAreaOut.
       //keyHandler = new KeyHandler(os);
       //messageAreaOut.addKeyListener(keyHandler);

       // Add ownership listener to allow ownership event handling.
       _portId.addPortOwnershipListener(this);

       open = true;
   }

   /**
   Sets the connection parameters to the setting in the parameters object.
   If set fails return the parameters object to origional settings and
   throw exception.
   */
   public void setConnectionParameters() throws SerialConnectionException {

       // Save state of parameters before trying a set.
       int oldBaudRate = _sPort.getBaudRate();
       int oldDatabits = _sPort.getDataBits();
       int oldStopbits = _sPort.getStopBits();
       int oldParity   = _sPort.getParity();
       int oldFlowControl = _sPort.getFlowControlMode();

       // Set connection parameters, if set fails return parameters object
       // to original state.
       try {
           _sPort.setSerialPortParams(_parameters.getBaudRate(),
                                     _parameters.getDatabits(),
                                     _parameters.getStopbits(),
                                     _parameters.getParity());
       } catch (UnsupportedCommOperationException e) {
           _parameters.setBaudRate(oldBaudRate);
           _parameters.setDatabits(oldDatabits);
           _parameters.setStopbits(oldStopbits);
           _parameters.setParity(oldParity);
           throw new SerialConnectionException("Unsupported parameter");
       }

       // Set flow control.
       try {
           _sPort.setFlowControlMode(_parameters.getFlowControlIn()
                                  | _parameters.getFlowControlOut());
       } catch (UnsupportedCommOperationException e) {
           throw new SerialConnectionException("Unsupported flow control");
       }
   }

   /**
   Close the port and clean up associated elements.
   The IO streams must be closed prior to calling this function!
   */
   public void closeConnection() {
       // If port is alread closed just return.
       if (!open) {
           return;
       }

       // Check to make sure _sPort has reference to avoid a NPE.
       if (_sPort != null) {
           // Close the port.
           _sPort.close();
           // Remove the ownership listener.
           _portId.removePortOwnershipListener(this);
       }

       open = false;
   }

   /**
   Send a one second break signal.
   */
   public void sendBreak() {
       _sPort.sendBreak(1000);
   }

   /**
   Reports the open status of the port.
   @return true if port is open, false if port is closed.
   */
   public boolean isOpen() {
       return open;
   }
}
