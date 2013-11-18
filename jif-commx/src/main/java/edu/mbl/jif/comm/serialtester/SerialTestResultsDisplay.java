package edu.mbl.jif.comm.serialtester;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: SerialTestResultsDisplay Interface</p>
 * <p>Description: An abstract class for representing results for the echo test.  Keeps the GUI
 * separate from the serial send/receive/monitor threads.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public interface SerialTestResultsDisplay {
  static final int SEND_DONE = 1;
  static final int RECEIVE_DONE = 2;
  static final int RESULTS_DONE = 3;

  /**
   * Called by the thread sending data, when implemented should display the bytesSent to
   * the user.
   * @param bytesSent bytes sent
   */
  public void updateBytesSent(int bytesSent);
  /**
   * Called by the thread receiving data, when implemented should display the bytesReceived to
   * the user.
   * @param bytesReceived bytes received.
   * */
   public void updateBytesReceived(int bytesReceived);
   /**
    * Called by the thread monitoring the echo test, when implemented should display the results to
    * the user.
    * @param result string Pass or Fail.
    * */
  public void updateResults(String result);
  /**
   * Called by each echo test thread when it has completed, when implemented should update any state
   * related to each of the threads finishing.
   * @param thread SEND_DONE, RECEIVE_DONE, or RESULTS_DONE
   * @param bytespersec when thread is RESULTS_DONE bytespersec is the calculated throughput.
   * */
   public void threadDone(int thread, int bytespersec);
  /**
   * called by the Command Test to update the GUI of bytes received.
   * @param receivedBytes
   * */
   public void updateResponse(String receivedBytes);
  /**
   * called by the Command Test to update the GUI of bytes received.
   * @param data byte array of data
   * @param offset into data array
   * @param len length of data in arrary
   * */
   public void updateResponse(byte[] data, int offset, int len);
   /**
    * update the GUI of the error event type.
    * @param eventType serial event type.
    * */
  public void updateEvent(int eventType);
}