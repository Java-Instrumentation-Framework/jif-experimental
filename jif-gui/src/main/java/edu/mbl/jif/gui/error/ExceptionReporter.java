package edu.mbl.jif.gui.error;

/**
 * Derived from ImageViewer, @author Brian Risinger
 */

import java.awt.*;
import javax.swing.*;


public class ExceptionReporter
{
   // GBH
   //      Thread.UncaughtExceptionHandler handler =
   //            new StackWindow("Show Exception Stack", 400, 200);
   //      Thread.setDefaultUncaughtExceptionHandler(handler);
   
   public static final int EXCEPTION_INFORMATIONAL = 2;
   // user of informational warnings that you realy don't care about
   // shouldn't happen - eg a Thread.sleep(100) that you really don't
   // care about throwing an InterruptedExcaption
   public static final int EXCEPTION_DEBUG = 4;
   // for exceptions that you wat to be informed about durring debugging,
   // but not appear in the released product
   public static final int EXCEPTION_RELEASE = 8;
   // for exceptions that should appear in debug and release, ie file not found, etc
   public static final int EXCEPTION_SEVERE = 16;
   // for really severe exceptions that should be reported as 'loudly'
   // as possible. Always outputted to the console and a popup window if
   // posible, with a beep if posible.

   private static int exceptionLevel = EXCEPTION_RELEASE;
   private static boolean neverPopupError = false;

   public static void setExceptionReportingLevel (int level) {
      exceptionLevel = level;
   }


   public static int getExceptionReportingLevel () {
      return exceptionLevel;
   }


   public static void setPopupEnabled (boolean enabled) {
      neverPopupError = !enabled;
   }


   public static boolean getPopupEnabled () {
      return!neverPopupError;
   }


   /**
    * reportException Allows an easy method for reporting exceptions to users.
    * it will either pop up a dialog box or output to the console or both depending
    * on the parameters passed in to the method.
    *
    * @param thr - the throwable object that is to be reported
    * @param severity - the severity of this particular exception - should use
    *     the pre-defined constants defined by this class
    * @param cmp - can be null - a component, which if specified, will be used
    *     as the parent of the popup window.  If null only console output will
    *     occur.
    * @param discriptionOfActionOccuring - a discription of what was going on
    *     when the exception occuren, should start with a lowercase word as it
    *     will be used in the middle of a sentance. for example passing in
    *     'save a file' will cause the following to be printed "An Exception
    *     occured while attempting to save a file".  Can be null, in which case
    *     only "An Exception occured" will be printed.
    */
   public static void reportException (Throwable thr, int severity, Component cmp,
         String discriptionOfActionOccuring) {

      if (severity >= exceptionLevel) {
         String message = "An Exception occured" + ((discriptionOfActionOccuring != null)
               ? " while attempting to\n " + discriptionOfActionOccuring + ":\n "
               : ":\n ") + thr.getClass().getName() + "\n " + thr.getLocalizedMessage();
         boolean showPopup = true;
         boolean showConsole = true;
         if (cmp == null || neverPopupError) {
            showConsole = true;
         } else {
            showPopup = true;
         }
         if (severity >= EXCEPTION_SEVERE) {
            showConsole = true;
            try {
               Toolkit.getDefaultToolkit().beep();
            }
            catch (Throwable t) {}
         }
         if (showConsole) {
            System.out.println("\n");
            System.out.print(message);
            if (severity >= EXCEPTION_SEVERE
                  || (exceptionLevel < EXCEPTION_RELEASE && showPopup == false)) {
               thr.printStackTrace();
            }
            System.out.println();
         }
         if (showPopup) {
            // +++ Check if EventDispatchThread...

            if (exceptionLevel >= EXCEPTION_RELEASE) {
               JOptionPane.showMessageDialog(cmp, htmlize(message),
                     "An Exception Occured",
                     (severity > EXCEPTION_INFORMATIONAL) ? JOptionPane.ERROR_MESSAGE
                     : JOptionPane.WARNING_MESSAGE);
            } else {
               Object[] options = {"Print Stack Trace", "Close"};
               int sel = JOptionPane.showOptionDialog(cmp, htmlize(message),
                     "An Exception Occured", JOptionPane.DEFAULT_OPTION,
                     (severity > EXCEPTION_INFORMATIONAL) ? JOptionPane.ERROR_MESSAGE
                     : JOptionPane.WARNING_MESSAGE, null, options, options[1]);
               if (sel == 0) {
                  thr.printStackTrace();
               }
            }
         }
      }
   }


   public static String htmlize (String str) {
      StringBuffer sb = new StringBuffer("<HTML><HEAD></HEAD><BODY>");
      sb.append(str.replaceAll("\n", "<BR>"));
      sb.append("</BODY></HTML>");
      return sb.toString();
   }


   public static void main (String[] args) {
      setPopupEnabled(true);
      reportException(new Throwable(new String("Yikes")), EXCEPTION_SEVERE, null, null);
   }
}
