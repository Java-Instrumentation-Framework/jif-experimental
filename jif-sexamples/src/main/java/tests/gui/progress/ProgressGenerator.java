package tests.gui.progress;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.event.MessageListener;
import org.jdesktop.swingx.event.MessageSource;
import org.jdesktop.swingx.event.MessageSourceSupport;
import org.jdesktop.swingx.event.ProgressListener;
import org.jdesktop.swingx.event.ProgressSource;



public class ProgressGenerator implements Runnable, ProgressSource, MessageSource {
   private MessageSourceSupport messageSupport = new MessageSourceSupport(this);

   public void run() {
      // process started...
      dispatchToEDT(new Runnable() {
         public void run() {
            messageSupport.fireProgressStarted(0, 19);
         }
         });

      for (int i = 0; i<20; i++) {
         final int currentPosition = i;
         // process on-going...
         dispatchToEDT(new Runnable() {
            public void run() {
               messageSupport.fireMessage("Progress Updated: " + currentPosition);
               messageSupport.fireProgressIncremented(currentPosition);
            }
            });
         try {
            Thread.sleep(500);
         } catch (InterruptedException e) { }
      }
      // process completed...
      dispatchToEDT(new Runnable() {
         public void run() {
            messageSupport.fireProgressEnded();
            messageSupport.fireMessage("");
         }
         });

   }

   private void dispatchToEDT(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      else {
         runnable.run();
      }
   }

   public void addMessageListener(MessageListener l) {
      messageSupport.addMessageListener(l);
   }
   public void addProgressListener(ProgressListener l) {
      messageSupport.addProgressListener(l);
   }
   public MessageListener[] getMessageListeners() {
      return messageSupport.getMessageListeners();
   }
   public ProgressListener[] getProgressListeners() {
      return messageSupport.getProgressListeners();
   }
   public void removeMessageListener(MessageListener l) {
      messageSupport.removeMessageListener(l);
   }
   public void removeProgressListener(ProgressListener l) {
      messageSupport.removeProgressListener(l);
   }


}