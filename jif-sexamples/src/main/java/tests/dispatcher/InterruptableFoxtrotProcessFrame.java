package tests.dispatcher;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import foxtrot.*;

public class InterruptableFoxtrotProcessFrame
        extends JFrame {

   private JButton button;
   private JProgressBar bar;
   private boolean running;
   private boolean taskInterrupted;
   private boolean done = false;
   private String queueStart = "Start";
   private String operation = "Process";
   JLabel labelOperation = new JLabel();

   public InterruptableFoxtrotProcessFrame() {
      //super("Working...", true, true, true, true);
      super();
      try {
         jbInit();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void jbInit() throws Exception {
      this.setTitle(operation);
      this.getContentPane().setLayout(null);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      // remove titlebar
      button = new JButton(queueStart);
      button.setMargin(new Insets(2, 4, 2, 4));
      button.setBounds(new Rectangle(79, 80, 95, 30));
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (!done) {
               if (running) {
                  onCancelClicked();
               } else {
                  onRunClicked();
               }
            } else {
               onExitClicked();
            }
         }
      });
      bar = new JProgressBar();
      bar.setStringPainted(true);
      bar.setBounds(new Rectangle(10, 44, 234, 23));
      labelOperation.setFont(new java.awt.Font("Dialog", 1, 12));
      labelOperation.setHorizontalAlignment(SwingConstants.CENTER);
      labelOperation.setText(operation);
      labelOperation.setBounds(new Rectangle(5, 11, 244, 21));
      JPanel main = new JPanel();
      main.setLayout(null);
      main.setBounds(new Rectangle(2, 1, 253, 124));
      main.add(button, null);
      main.add(bar, null);
      main.add(labelOperation, null);
      this.getContentPane().add(main, null);
   }

   public void setPrompts(String _operation, String _queueStart) {
      queueStart = _queueStart;
      button.setText(queueStart);
      operation = _operation;
      this.setTitle(operation);
   }

   private void onRunClicked() { // if button here starts work.
      //  public void runProcess() {
      running = true;
      setTaskInterrupted(false);
      button.setText("Cancel");
      doTheWork(); // <<<========================
      button.setText("Done");
      button.setEnabled(false);
      StringBuffer buffer = new StringBuffer();
      buffer.setLength(0);
      if (isTaskInterrupted()) {
         buffer.append("Cancelled ");
      } else {
         buffer.append("Completed ");
      }
      update(100, 100, buffer.toString());
      running = false;
      if (!isTaskInterrupted()) {
         //========================================
         // when done...
         //========================================
      } else { // Task was Interrupted
         //========================================
         // if interrupted...
         System.out.println("Process Interrupted.");
         // close the file.
         //========================================
      }
   }

   private void doTheWork() {
      Worker.post(new Job() {
         public Object run() {
            StringBuffer buffer = new StringBuffer();
            boolean more = true;
            // A repetitive operation that updates a progress bar.
            int max = 20;
            for (int i = 1; i <= max; ++i) {
               //==============================================
               // Simulate a heavy operation to retrieve data
               //==============================================
               try {
                  Thread.sleep(300);
               } catch (InterruptedException ex) {
               }
               // Prepare the progress bar string
               buffer.setLength(0);
               buffer.append("Step ").append(i).append(" of ").append(max);
               if (isTaskInterrupted()) {
                  buffer.append(" - Interrupted !");
                  update(i, max, buffer.toString());
                  break;
               } else {
                  update(i, max, buffer.toString()); // Update the progress bar
               }
            }
            // Finished...
            if (isTaskInterrupted()) {
               //=========================
               // Clean up if Interrupted
               System.out.println("Interrupted.");
               return null;
            } else {
               return (Object) new Integer(1);
            }
         }
      });
   }

   private void onCancelClicked() {
      setTaskInterrupted(true);
   }

   private void onExitClicked() {
   }

   private void update(final int index, final int max, final String string) {
      // This method is called by the Foxtrot Worker thread, but I want to
      // update the GUI, so I use SwingUtilities.invokeLater, as the Task
      // is not finished yet.

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            bar.setMaximum(max);
            bar.setValue(index);
            bar.setString(string);
            repaint();
         }
      });
   }

   private synchronized boolean isTaskInterrupted() {
      // Called from the Foxtrot Worker Thread.
      // Must be synchronized, since the variable taskInterrupted is accessed from 2 threads.
      // While it is easier just to change the variable value without synchronizing, it is possible
      // that the Foxtrot worker thread doesn't see the change (it may cache the value of the variable
      // in a registry).
      return taskInterrupted;
   }

   private synchronized void setTaskInterrupted(boolean value) {
      // Called from the AWT Event Dispatch Thread.
      // See comments above on why it must be synchronized.
      taskInterrupted = value;
   }
}
