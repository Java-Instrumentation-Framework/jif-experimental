package edu.mbl.jif.comm.serialtester;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: Serial Port Tester</p>
 * <p>Description: Verify Serial Port Operation Of Remote Hosts</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public interface EchoTestResultsDisplay {
  static final int SEND_DONE = 1;
  static final int RECEIVE_DONE = 2;
  static final int RESULTS_DONE = 3;

  public void updateBytesSent(int bytesSent);
  public void updateBytesReceived(int bytesReceived);
  public void updateResults(String result);
  public void threadDone(int thread);
}