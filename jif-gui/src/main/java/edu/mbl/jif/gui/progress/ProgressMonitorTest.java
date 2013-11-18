package edu.mbl.jif.gui.progress;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
   A program to test a progress monitor dialog.
 */
public class ProgressMonitorTest
{
   /*public static void main (String[] args) {
      JFrame frame = new ProgressMonitorFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
*/
}


/**
   A frame that contains a button to launch a simulated activity
   and a text area for the activity output.
 */
/*
class ProgressMonitorFrame
      extends JFrame
{


   private Timer activityMonitor;
   private JButton startButton;
   private ProgressMonitor progressDialog;

   private JTextArea textArea;
   private SimulatedActivity activity;

   public static final int WIDTH = 300;
   public static final int HEIGHT = 200;

   public ProgressMonitorFrame () {
      setTitle("ProgressMonitorTest");
      setSize(WIDTH, HEIGHT);

      Container contentPane = getContentPane();
      // this text area holds the activity output
      textArea = new JTextArea();
      // set up a button panel
      JPanel panel = new JPanel();
      startButton = new JButton("Start");
      panel.add(startButton);
      contentPane.add(new JScrollPane(textArea),
            BorderLayout.CENTER);
      contentPane.add(panel, BorderLayout.SOUTH);

      // set up the button action
      startButton.addActionListener(new
            ActionListener()
      {
         public void actionPerformed (ActionEvent event) {
            // start activity
            activity = new SimulatedActivity(1000);
            activity.start();

            // launch progress dialog
            progressDialog = new ProgressMonitor(
                  ProgressMonitorFrame.this,
                  "Waiting for Simulated Activity",
                  null, 0, activity.getTarget());

            // start timer
            activityMonitor.start();

            startButton.setEnabled(false);
         }
      });

      // set up the timer action
      activityMonitor = new Timer(500, new ActionListener()
      {
         public void actionPerformed (ActionEvent event) {
            int current = activity.getCurrent();
            // show progress
            textArea.append(current + "\n");
            progressDialog.setProgress(current);
            // check if task is completed or canceled
            if (current == activity.getTarget()
                  || progressDialog.isCanceled()) {
               activityMonitor.stop();
               progressDialog.close();
               activity.interrupt();
               startButton.setEnabled(true);
            }
         }
      });
   }


}
*/


/**
   A similated activity thread.
 */
class SimulatedActivity
      extends Thread
{
   /**
      Constructs the simulated activity thread object. The
      thread increments a counter from 0 to a given target.
      @param t the target value of the counter.
    */
   public SimulatedActivity (int t) {
      current = 0;
      target = t;
   }


   public int getTarget () {
      return target;
   }


   public int getCurrent () {
      return current;
   }


   public void run () {
      try {
         while (current < target && !interrupted()) {
            sleep(100);
            current++;
         }
      }
      catch (InterruptedException e) {
      }
   }


   private int current;
   private int target;
}
