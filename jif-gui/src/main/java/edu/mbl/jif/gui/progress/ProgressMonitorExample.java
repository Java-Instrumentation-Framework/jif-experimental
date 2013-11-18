package edu.mbl.jif.gui.progress;

import javax.swing.*;

// ProgressMonitorExample.java
// A demonstration of the ProgressMonitor toolbar.  A timer is used to induce
// progress.  This example also shows how to use the UIManager properties
// associated with progress monitors.
//
import java.awt.*;
import java.awt.event.*;


public class ProgressMonitorExample
      extends JFrame //implements ActionListener
{
 
  //
   static ProgressMonitorD pbar;
   static int counter = 0;

   public ProgressMonitorExample () {
//      super("Progress Monitor Demo");
//      setSize(250, 100);
//      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      pbar = new ProgressMonitorD(
//            null, "Monitoring Progress", "Initializing . . .", 0, 100);
//      // Fire a timer every once in a while to update the progress.
//      Timer timer = new Timer(500, this);
//      timer.start();
//      setVisible(true);
   }



   //----------------------------------------------------------------
   // Invoked by the timer
   public void actionPerformed (ActionEvent e) {
      // Invoked by the timer every half second. Simply place
      // the progress monitor update on the event queue.
      SwingUtilities.invokeLater(new Update());
   }


   //----------------------------------------------------------------
   //
   public static void main (String[] args) {
      UIManager.put("ProgressMonitor.progressText", "This is progress?");
      UIManager.put("OptionPane.cancelButtonText", "Go Away");
      new ProgressMonitorExample();
   }


   class Update
         implements Runnable
   {
      public void run () {
         if (pbar.isCancelled()) {
          //  pbar.close();
            System.exit(1);
         }
         pbar.setProgress(counter);
         pbar.setNote("Operation is " + counter + "% complete");
         counter += 2;
      }
   }
 
}
