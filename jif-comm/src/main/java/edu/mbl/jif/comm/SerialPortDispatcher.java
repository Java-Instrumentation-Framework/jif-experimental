/*
 * SerialPortDispatcher.java
 * Created on February 11, 2007, 10:34 AM
 */
package edu.mbl.jif.comm;

import java.util.concurrent.*;

/**
 * SerialPortDispatcher
 * Enables non-blocking sending of command and then waiting for completion...
 * @author GBH
 */

public class SerialPortDispatcher {
    ExecutorService executor;
    private int timeOut;
    
    public SerialPortDispatcher() {
        executor = Executors.newSingleThreadExecutor();
    }
    
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
    public SerialIoTask doCommand(String cmd) {
        SerialIoTask task = new SerialIoTask(new DoCommand(cmd));
        executor.submit(task);
        return task;
    }
    
    public String waitFor(SerialIoTask task) {
        String response = "";
        try {
            response = (String) task.get(3000, TimeUnit.MILLISECONDS);
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
    
    class DoCommand implements Callable {
        String cmd = "";
        
        public DoCommand(String cmd) {
            this.cmd = cmd;
        }
        
        public String call() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return "DoCommand: " + cmd;
        }
    }
    
    class SerialIoTask extends FutureTask {
        public SerialIoTask(Callable call) {
            super(call);
        }
    }
    
    public static void main(String []args) {
        SerialPortDispatcher sd = new SerialPortDispatcher();
        SerialIoTask sioTask = sd.doCommand("somethin");
        System.out.println("Do some other stuff...");
        System.out.println(sd.waitFor(sioTask));
        sd.shutDown();
    }
}
