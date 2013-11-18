/*
 * SwingThread.java
 */
package edu.mbl.jif.gui.swingthread;

import java.awt.Cursor;

import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Swing Threading Utilities
 *
 * @author GBH
 */
public class SwingThread {
   public SwingThread() {
   }

   // Example ...
   /*
    public void actionPerformed(ActionEvent event) {
    if (event.getSource() == loginButton) {
       gui.startWorker(new Runnable() {
          public void run() {
             loginActionPerformed();
          }
       });
    }
   }   */

   // --- dispatchToEDT ----------------------------------------
   // Usage:
   //      dispatchToEDT(new Runnable()
   //      {
   //         public void run () {
   //            //...
   //         }
   //      });
   public static void dispatchToEDT(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(runnable);
      } else {
         runnable.run();
      }
   }

   // -----------------------------------------------------------------
   public void startWorker(final Runnable runnable) {
      SwingWorker3 swingWorker =
         new SwingWorker3() {
            public Object construct() {
               try {
                  //setWaitCursor(true);
                  runnable.run();
               } catch (Exception e) {
                  //showExceptionDialog(e);
               } finally {
                  //setWaitCursor(false);
               }
               return null;
            }
         };
      swingWorker.start();
   }

   public void startWorkerWithCursor(final Runnable runnable, final JFrame frame) {
      SwingWorker3 swingWorker =
         new SwingWorker3() {
            public Object construct() {
               try {
                  setWaitCursor(true, frame);
                  runnable.run();
               } catch (Exception e) {
                  // showExceptionDialog(e);
               } finally {
                  setWaitCursor(false, frame);
               }
               return null;
            }
         };
      swingWorker.start();
   }

   // Disable button during run...
   
    public void startWorker(final Runnable runnable, final JButton button) {
        SwingWorker3 swingWorker = new SwingWorker3() {
            public Object construct() {
                setEnabled(button, false);
                //setWaitCursor(true);
                try { 
                   runnable.run();
                } catch (Exception e) {
                  // showExceptionDialog(e);
                } finally {
                   //setWaitCursor(false);
                   setEnabled(button, true);
                }
                return null;
            }
        };
        swingWorker.start();
    }
   
    
    public void setEnabled(final JButton button, final boolean enabled) {
      startWorker(new Runnable() {
         public void run() {
            button.setEnabled(enabled);
         }
      });
   }
    
    
   public void setWaitCursor(boolean waitCursor, JFrame frame) {
      if (!waitCursor) {
         frame.setCursor(null);
      } else {
         frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      }
      frame.getGlassPane().setVisible(waitCursor);
   }
}
