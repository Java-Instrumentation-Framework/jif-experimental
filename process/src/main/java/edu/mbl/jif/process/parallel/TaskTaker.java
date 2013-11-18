package edu.mbl.jif.process.parallel;

import edu.mbl.jif.process.parallel.listen.CompletionEvent;
import edu.mbl.jif.process.parallel.listen.CompletionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 *
 * @author GBH
 */
public class TaskTaker extends Thread {

   private TaskSubmitter submitter;
   private CompletionService< OrderedResult> cservice;
   OrderedResultsProcessor orderer;
   private int taken = 0;
   private boolean cancelled = false;

   public TaskTaker(TaskSubmitter submitter,
           CompletionService<OrderedResult> cservice,
           OrderedResultsProcessor orderer) {
      this.submitter = submitter;
      this.cservice = cservice;
      this.orderer = orderer;
   }

   public int getTaken() {
      return taken;
   }

   public void cancel() {
      cancelled = true;
   }

   public void run() {
      while (true) {
         OrderedResult result;
         try {
            Future<OrderedResult> f = cservice.take();
            //System.out.println("polled");
            if (f != null) {
               result = f.get();
               taken++;
               orderer.addToWaitingResults(result);
               //submitter.setTaken(taken++);
               System.out.println("             Added " + result.getSeq() + " to waiting");
               System.out.println("> submitted: " + submitter.getSubmitted() + "   taken: " + taken);
            } else {
               System.err.println(" (f != null) ");
            }
            if ((submitter.getSubmitted() == taken && submitter.isComplete()) || cancelled) {
               orderer.setStillProcessing(false);
               break;
            }
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
         } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception at submitted: " + submitter.getSubmitted() + "   taken: "
                    + taken);
         }
      }
      System.out.println(" ---------------------------------------------------- TaskTaker stopped.");
      if (this.cancelled) {
         fireEvent(1);
      } else {
         fireEvent(0);
      }

   }

   private List _listeners = new ArrayList();

   public synchronized void addCompletionListener(CompletionListener listener) {
      _listeners.add(listener);
   }

   public synchronized void removeCompletionListener(CompletionListener listener) {
      _listeners.remove(listener);
   }

   // call this method whenever you want to notify
   //the event listeners of the particular event
   private synchronized void fireEvent(int status) {
      CompletionEvent event = new CompletionEvent(status);
      Iterator i = _listeners.iterator();
      while (i.hasNext()) {
         ((CompletionListener) i.next()).handleCompletionEvent(event);
      }
   }
}
