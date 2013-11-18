package edu.mbl.jif.gui.swingthread;

import edu.mbl.jif.gui.*;

/**
 A task that takes long enough to require a progress monitor and the ability
 to cancel it.
 */

public class LongTaskTest
{
   private int lengthOfTask;
   private int current = 0;
   private boolean done = false;
   private boolean canceled = false;
   private String statMessage;

   public LongTaskTest () {
      //Compute length of task...
      //In a real program, this would figure out
      //the number of bytes to read or whatever.
      lengthOfTask = 1000;
   }


// Called from Config/Start panel to start the task.
   public void go () {
//      final SwingWorker3 worker = new SwingWorker3()
//      {
//         public Object construct () {
//            current = 0;
//            done = false;
//            canceled = false;
//            statMessage = null;
//            return new ActualTask();
//         }
//      };
//      worker.start();
   }


   /**
    * Called from ProgressBarDemo to find out how much work needs
    * to be done.
    */
   public int getLengthOfTask () {
      return lengthOfTask;
   }


   /**
    * Called from ProgressBarDemo to find out how much has been done.
    */
   public int getCurrent () {
      return current;
   }


   public void stop () {
      canceled = true;
      statMessage = null;
   }


   /**
    * Called from ProgressBarDemo to find out if the task has completed.
    */
   public boolean isDone () {
      return done;
   }


   /**
    * Returns the most recent status message, or null
    * if there is no current status message.
    */
   public String getMessage () {
      return statMessage;
   }


   public void cleanup () {

   }


   // The actual long running task.  This runs in a SwingWorker thread.
   class ActualTask
   {
      ActualTask () {
         while (!canceled && !done) {
            try {
               Thread.sleep(1000); //sleep for a second
               current += Math.random() * 100; //make some progress
               if (current >= lengthOfTask) {
                  done = true;
                  current = lengthOfTask;
               }
               statMessage = "Completed " + current +
                     " of " + lengthOfTask + ".";
            }
            catch (InterruptedException e) {
               System.out.println("Task interrupted");
            }
         }
         if (canceled) {
            cleanup();
         }
      }
   }
}
