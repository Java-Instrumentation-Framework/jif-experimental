package edu.mbl.jif.utils.thread;

//import edu.mbl.jif.imaging.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ThreadSpawner extends Thread {
   private int     count;
   private boolean done = false;

   public ThreadSpawner() {
   }

   public synchronized void safeStop() {
      done = true;
   }

   synchronized boolean ok() {
      return (!done);
   }

   public void run() {
      while (ok()) {
         doWork();
      }
   }

   public void doWork() {
      System.out.println("Doing work");
      ShortThread st = new ShortThread();
      st.start();
      try {
         Thread.sleep(100);
      } catch (InterruptedException ex) {
      }
   }

   public static void main(String[] args) {
      // for (int i=0; i<5; i++) {
      ThreadSpawner framer = new ThreadSpawner();
      framer.setPriority(Thread.currentThread().getPriority() - 1);

      try {
         System.out.println("Starting thread:" + framer);
         framer.start();
         Thread.sleep(5000); // Wait 5 seconds.

         System.out.println("Stopping thread: " + framer);

         framer.safeStop(); // Stop the thread.
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      //  }
   }
}


class ShortThread extends Thread {
   public void run() {
    //  edu.mbl.jif.imaging.ImageFactoryGrayScale.testImageByte();
   }
}
