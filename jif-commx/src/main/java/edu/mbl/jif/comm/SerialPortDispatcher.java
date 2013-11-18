/*
 * SerialPortDispatcher.java
 * Created on February 11, 2007, 10:34 AM
 */
package edu.mbl.jif.comm;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SerialPortDispatcher
 * Enables non-blocking sending of command and then waiting for completion...
 * @author GBH
 */
public class SerialPortDispatcher {

  ExecutorService executor;
  private int timeOut;
  SerialPortConnection port;

  public SerialPortDispatcher(SerialPortConnection port) {
    this.port = port;
    executor = Executors.newSingleThreadExecutor();
  }

  public void setTimeOut(int timeOut) {
    this.timeOut = timeOut;
  }

  public SerialIoTask sendCommand(String cmd) {
    SerialIoTask task = new SerialIoTask(new SendCommand(cmd));
    executor.submit(task);
    return task;
  }

  public String waitFor(SerialIoTask task) {
    String response = "";
    try {
      response = (String) task.get(timeOut, TimeUnit.MILLISECONDS);
      return response;
    } catch (ExecutionException ex) {
      ex.printStackTrace();
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    } catch (TimeoutException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public void shutDown() {
    executor.shutdown();
  }

  //====================================
  class SendCommand implements Callable<String> {

    String cmd = "";

    public SendCommand(String cmd) {
      this.cmd = cmd;
    }

    public String call() {
      System.out.println("Sending command:" + cmd);
      port.send(cmd);
      return port.recv();
    }
  }

  class SerialIoTask extends FutureTask {

    public SerialIoTask(Callable call) {
      super(call);
    }
  }


  public static void main(String[] args) {
    SerialPortDispatcher sd = new SerialPortDispatcher(new SerialPortConnection());
    sd.setTimeOut(2000);
    //
    SerialIoTask sioTask = sd.sendCommand("somethin");
    System.out.println("Do some other stuff...");
    try {
      sioTask.get();
    } catch (InterruptedException ex) {
      Logger.getLogger(SerialPortDispatcher.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ExecutionException ex) {
      Logger.getLogger(SerialPortDispatcher.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println(sd.waitFor(sioTask));
    sd.shutDown();
  }
}
