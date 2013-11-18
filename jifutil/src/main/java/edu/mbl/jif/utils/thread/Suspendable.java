package edu.mbl.jif.utils.thread;

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
public class Suspendable
{

   boolean active = false;
   boolean suspended = false;

   public boolean isSuspended () {
      return suspended;
   }


   public void suspend () {
      if (active) {
         /* turn off whatever */
         suspended = true;
      }
   }


   public void resume () {
      if (suspended) {
         /* turn off whatever */
         suspended = false;
      }
   }


   public void activated () {
      active = true;
   }


   public void deActivated () {
      active = false;
   }
}

/*

// SuspendableThread

public class MyThread
      implements Runnable
{
   String name;
   Thread t;
   boolean suspended;
   MyThread () {
      t = new Thread(this, "Thread");
      suspended = false;
      t.start();
   }


   public void run () {
      try {
         for (int i = 0; i < 10; i++) {
            System.out.println("Thread: " + i);
            Thread.sleep(200);
            synchronized (this) {
               while (suspended) {
                  wait();
               }
            }
         }
      }
      catch (InterruptedException e) {
         System.out.println("Thread: interrupted.");
      }
      System.out.println("Thread exiting.");
   }


// Suspend
   void suspendThread () {
      suspended = true;
   }


// Resume
   synchronized void resumeThread () {
      suspended = false;
      notify();
   }
}



class Demo
{
   public static void main (String args[]) {
      MyThread t1 = new MyThread();
      try {
         Thread.sleep(1000);
         t1.suspendThread();
         System.out.println("Thread: Suspended");
         Thread.sleep(1000);
         t1.resumeThread();
         System.out.println("Thread: Resume");
      }
      catch (InterruptedException e) {
      }
      try {
         t1.t.join();
      }
      catch (InterruptedException e) {
         System.out.println(
               "Main Thread: interrupted");
      }
   }
}
*/
