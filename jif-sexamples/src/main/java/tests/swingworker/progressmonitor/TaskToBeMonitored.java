/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.swingworker.progressmonitor;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author GBH
 */
public class TaskToBeMonitored extends SwingWorker<Void, Integer> {

   /**
    * SwingWorker<T,V>
    * <T> - the result type returned by this SwingWorker's doInBackground and get methods
    * <V> - the type used for carrying out intermediate results by this SwingWorker's publish and
    * process methods More doc below...
    */
   private static final int PROGRESS_CHECKPOINT = 10000;
   private String someArg;
   int numSubTasks = 400;
   private final TaskCaller taskCaller;

   TaskToBeMonitored(TaskCaller taskCaller) {
      this.taskCaller = taskCaller;
   }

   // perform time-consuming copy task in the worker thread
   @Override
   public Void doInBackground() {

      // initialize bound property progress (inherited from SwingWorker)
      setProgress(0);

      while (!isCancelled()) {
         // do sub-tasks...

         for (int i = 0; i < numSubTasks; i++) {
            int current = i;
            try {
               Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            // set new value on bound property
            // progress and fire property change event
            int progress = (int) (100 * (current + 1) / numSubTasks);
            setProgress(progress);
            System.out.println("current = " + current);
            // publish current progress data for copy task
            publish(current);
            if (isCancelled()) {
               return null;
            }
         }
         break;
      }
      return null;
   }

   // process(List<V> chunks)
   // process copy task progress data in the event dispatch thread
   @Override
   public void process(java.util.List<Integer> progressList) {
      if (isCancelled()) {
         return;
      }
      Integer complete = progressList.get(progressList.size() - 1);
      int progress = (int) (100 * (complete + 1) / numSubTasks);
      //progressBar.setValue(percentComplete.intValue());
      setProgress(progress);
   }

   // perform final updates in the event dispatch thread
   @Override
   public void done() {
      try {
         // call get() to tell us whether the operation completed or 
         // was canceled; we don't do anything with this result
         Void result = get();
         taskCaller.onCompleted();
      } catch (InterruptedException e) {
      } catch (CancellationException e) {
         // get() throws CancellationException if background task was canceled
         taskCaller.onCancelled(e);
      } catch (ExecutionException e) {
         taskCaller.onException(e);
      }
      //progressMonitor.setProgress(0);
      setProgress(100);  // This is required to close the ProgressMonitor
   }
}
