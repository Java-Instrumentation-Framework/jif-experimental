package edu.mbl.jif.camera;

import ij.IJ;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

/**
 * Utilities
 */
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//import org.jdesktop.swingx.JXErrorDialog;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;


public class Utils {
   static String threadList = null;
   static boolean deBug = false;
   
   /**
    * 
    * @param msg 
    */
   public static void status(final String msg) {
      if (msg != "") {
         if (IJ.getInstance() != null) {
            IJ.showStatus(msg);
            // IJ.showMessage("Title", msg);
         } else {
            System.out.println(msg);
            if (Globals.ctrlPanel != null) {
               dispatchToEDT(new Runnable() {
                  public void run() {
       //              Globals.ctrlPanel.setProgress(msg);
                  }
               });
            }
         }
      }
   }
   
   public static void showProgress(final double percent) {
      if (IJ.getInstance() != null) {
         IJ.showProgress(percent);
      } else {
         System.out.println("Progress ... " + percent);
      }
      
   }
   
   
   /**
    * logs messages to: System.out or, if present, to ImageJ.log() window.
    * @param msg String to append to log
    */
   public static void log(String msg) {
      if (IJ.getInstance() != null) {
         IJ.log(msg);
      } else {
         System.out.println("Log: " + msg);
      }
   }
   
   
   /**
    * 
    * @return 
    */
   public static String timeStamp() {
      SimpleDateFormat formatter =
            new SimpleDateFormat("yy-MM-dd_HH-mm-ss_SS", Locale.getDefault());
      Date currentDate = new Date();
      String dateStr = formatter.format(currentDate);
      return dateStr;
   }
   
    public static String timeFormat(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        String minStr = formatter.format(time);
        return minStr;
    }
    
    static long MS_SECOND = 1000L;
    static long MS_MINUTE = 60L * MS_SECOND;
    static long MS_HOUR = 60L * MS_MINUTE;
    static long DAY = 24L;
    
    // Format for a time duration
    public static String durationHhMmSs(long milliseconds) {
        //DecimalFormat is used to display at least two digits
        DecimalFormat nf = new DecimalFormat("00");
        //calculate hours, minutes
        long remainder = 0;
        long hours = (milliseconds / MS_HOUR);
        remainder = milliseconds % MS_HOUR;
        long minutes = remainder / MS_MINUTE;
        remainder = remainder % MS_MINUTE;
        long seconds = remainder / MS_SECOND;
        //build "hh:mm:ss"
        StringBuffer buffer = new StringBuffer();
        buffer.append(nf.format(hours));
        buffer.append(":");
        buffer.append(nf.format(minutes));
        buffer.append(":");
        buffer.append(nf.format(seconds));
        return buffer.toString();
    }
    
   /**
    * Delays 'msecs' milliseconds.
    */
   public static void wait(int msecs) {
      try {Thread.sleep(msecs);} catch (InterruptedException e) { }
   }
   
   /** Emits an audio beep. */
   public static void beep() {
      java.awt.Toolkit.getDefaultToolkit().beep();
   }
   
   /**
    * record events (string messages) to a file
    */
   public static void setRecordToFile(String file) {
   }
   
   public static void recordMsg(String s) {
   }
   
   /** Error Handling
    * @param s 
    */
   public static void error(String s) {
//      if (IJ.getInstance() != null) {
//         IJ.error("CamAcqJ Error: " + s);
//      } else {
        JXErrorPane.showDialog(null, new ErrorInfo (null, "CamAcqJ Error:", s, null, null,null,null));
         System.err.println("CamJ Error: " + s);
//      }
   }
   
   
   public static void pipeErrorsTo( /*Stream*/ ) {
   }
   
   // Diagnostics ==========================================
   
   public static void consoleOpen() {
   }
   
   public static void consoleClose() {
   }
   
   public static void setDeBug(boolean t) {
      deBug = t;
   }
   
   
   // Threads --------------------------------------------
   public static void showThreads() {
      // Find the root thread group
      ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
      threadList = "Threads: \n";
      while (root.getParent() != null) {
         root = root.getParent();
      }
      // Visit each thread group
      visit(root, 0);
      listFrame("Threads", threadList);
   }
   
   // This method recursively visits all thread groups under `group'.
   public static void visit(ThreadGroup group, int level) {
      // Get threads in `group'
      int numThreads = group.activeCount();
      Thread[] threads = new Thread[numThreads * 2];
      numThreads = group.enumerate(threads, false);
      // Enumerate each thread in `group'
      for (int i = 0; i < numThreads; i++) {
         // Get thread
         Thread thread = threads[i];
         threadList = threadList + String.valueOf(threads[i]) + "\n";
         // System.out.println(threads[i]);
      }
      
      // Get thread subgroups of `group'
      int numGroups = group.activeGroupCount();
      ThreadGroup[] groups = new ThreadGroup[numGroups * 2];
      numGroups = group.enumerate(groups, false);
      // Recursively visit each subgroup
      for (int i = 0; i < numGroups; i++) {
         visit(groups[i], level + 1);
      }
   }
   
   public static void listFrame(String title, String list) {
      TextWindow tf = new TextWindow(title);
      tf.setSize(600, 800);
      tf.setLocation(200, 50);
      tf.setVisible(true);
      tf.set(list);
   }
   
   
   // --- dispatchToEDT ----------------------------------------
// Usage:
//
//      Runnable toRun = new Runnable() {
//         public void run() {
//            //...
//         }
//      };
//      dispatchToEDT(toRun);
//
   public static void dispatchToEDTAndWait(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      } else {
         runnable.run();
      }
   }
   
   public static void dispatchToEDT(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(runnable);
      } else {
         runnable.run();
      }
   }
}
