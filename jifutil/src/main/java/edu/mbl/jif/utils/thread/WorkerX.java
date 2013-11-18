package edu.mbl.jif.utils.thread;

public class WorkerX
      extends Thread
{
   private int count;
   private boolean done = false;

   public synchronized void safeStop () {
      done = true;
   }


   synchronized boolean ok () {
      return (!done);
   }


   void doWork () {
      // do something useful here
      ++count;
   }


   public void run () {
      while (ok()) {
         doWork();
      }
   }
}
