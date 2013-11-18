package edu.mbl.jif.comm.serialtester;
import java.io.*;
import java.util.*;

/**
 * <p>Title: EchoTestThread</p>
 * <p>Description: Monitors the send/receive threads for the echo test.  Keeps track
 * of the time that the receive thread has run and calculates the baud rate. Verifies that
 * sent data is equal to received data.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter
 * @version 1.0
 */

public class EchoTestThread implements Runnable{

  private SerialportSendThread _sendThread;
  private SerialPortReceiverThread _readThread;
  private boolean _keepRunning;
  private Thread _thread;
  private SerialTestResultsDisplay _results;

  /**
   * Creates a thread object to monitor the send and receive threads for their completion.
   * @param sendThread the thread object that is sending data
   * @param readThread the thread object that is receiving data
   * @param results object to send results to (GUI).
   */
  public EchoTestThread(SerialportSendThread sendThread, SerialPortReceiverThread readThread, SerialTestResultsDisplay results) {
    _sendThread = sendThread;
    _readThread = readThread;
    _results = results;
    _keepRunning = true;
    _thread = new Thread(this);
  }
  /**
   * Starts the thread to monitor the tests.
   * */
   public void StartEchoTest() {
    _thread.start();
  }
  public void run() {
    long starttime = System.currentTimeMillis();
    int sent=0, recv=0;
      try {
          for (;;) {
            if (!_keepRunning) {
              return;
            }
            _sendThread._sendThread.join(1000);//either returns when sendthread is finished or 1000ms have passed
            if (_sendThread._sendThread.isAlive()) {//test if it has finished, if no go back and wait some more
              continue;
            }
            //send thread has finished, do throughput calculations
            sent = _sendThread.getSent(); recv = _readThread.getReceived();
            //if sent bytes not equal to recv bytes wait 5 seconds to see if we get the last few bytes
            //if we don't means we most likely wont so test fails.
            for(int i = 0; i < 5; i++) {
              if (sent <= recv) break;//if recv greater test failed so no use waiting more
              _thread.sleep(1000);//wait to see if we are still receiving bytes
              recv = _readThread.getReceived();
            }

            //update gui
            if (_readThread.IsReceivedEqual(_sendThread.getData())){
              _results.updateResults("Passed");
            }
            else {
              _results.updateResults("Failed");
            }
            return;
          }
      } catch (InterruptedException e) {}
      finally {
        long timereceived;
        timereceived = System.currentTimeMillis() - starttime;
        _results.threadDone(EchoTestResultsDisplay.RESULTS_DONE, (int)(recv/(timereceived/1000)));
      }
  }

  /**
   * Stops the test, stops any running send/receive threads.
   */
  public void stopTest() {
    _keepRunning = false;
    try {
        _thread.join();
      } catch (InterruptedException e) {}
    _sendThread.stopSending();
    _readThread.stopReceiving();
  }

}
