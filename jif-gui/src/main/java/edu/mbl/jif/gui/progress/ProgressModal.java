package edu.mbl.jif.gui.progress;

import java.awt.*;
import java.awt.event.*;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;


/** @todo these classes need to be make public... */

//Following is the sample application to show the usage of ProgressMonitor:
public class ProgressModal {
   static JFrame frame;
   static Runnable heavyRunnable =
      new Runnable() {
         public void run() {
            ProgressMonitor monitor =
               ProgressUtil.createModalProgressMonitor(frame, 100, false, 1000);
            monitor.start("Fetching 1 of 10 records from database...");
            try {
               for (int i = 0; i < 10; i += 1) {
                  fetchRecord(i);
                  monitor.setCurrent("Fetching " + (i + 1) + " of 10 records from database",
                     (i + 1) * 10);
               }
            } finally {
               // to ensure that progress dlg is closed in case of any exception
               if (monitor.getCurrent() != monitor.getTotal()) {
                  monitor.setCurrent(null, monitor.getTotal());
               }
            }
            heavyAction.setEnabled(true);
         }

         private void fetchRecord(int index) {
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      };
   static Action heavyAction =
      new AbstractAction("Databse Query") {
         public void actionPerformed(ActionEvent e) {
            setEnabled(false);
            new Thread(heavyRunnable).start();
         }
      };

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
         e.printStackTrace();
      }
      frame = new JFrame("Modal Progress Dialog - santhosh@in.fiorano.com");
      frame.getContentPane().setLayout(new FlowLayout());
      frame.getContentPane().add(new JButton(heavyAction));
      frame.setSize(300, 200);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}


/*
 You must ensure that, ProgressMonitor's value always hits its total value, otherwise ProgressDialog2 will never get disposed. We ensure this using try...finally block.
 */

// model for progress of task.

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
class ProgressMonitor {
   int total;
   int current = -1;
   boolean indeterminate;
   int milliSecondsToWait = 500; // half second
   String status;

   /*--------------------------------[ ListenerSupport ]--------------------------------*/
   private Vector listeners = new Vector();
   private ChangeEvent ce = new ChangeEvent(this);

   public ProgressMonitor(int total, boolean indeterminate, int milliSecondsToWait) {
      this.total = total;
      this.indeterminate = indeterminate;
      this.milliSecondsToWait = milliSecondsToWait;
   }

   public ProgressMonitor(int total, boolean indeterminate) {
      this.total = total;
      this.indeterminate = indeterminate;
   }

   public int getTotal() {
      return total;
   }

   public void start(String status) {
      if (current != -1) {
         throw new IllegalStateException("not started yet");
      }
      this.status = status;
      current = 0;
      fireChangeEvent();
   }

   public int getMilliSecondsToWait() {
      return milliSecondsToWait;
   }

   public int getCurrent() {
      return current;
   }

   public String getStatus() {
      return status;
   }

   public boolean isIndeterminate() {
      return indeterminate;
   }

   public void setCurrent(String status, int current) {
      if (current == -1) {
         throw new IllegalStateException("not started yet");
      }
      this.current = current;
      if (status != null) {
         this.status = status;
      }
      fireChangeEvent();
   }

   public void addChangeListener(ChangeListener listener) {
      listeners.add(listener);
   }

   public void removeChangeListener(ChangeListener listener) {
      listeners.remove(listener);
   }

   private void fireChangeEvent() {
      Iterator iter = listeners.iterator();
      while (iter.hasNext()) {
         ((ChangeListener)iter.next()).stateChanged(ce);
      }
   }
}


/*
 progress always starts from zero. user specified the total units and whether it is indeterminate or not. The arguments milliSecondsToWait tells, after how much time after the task started, the progress dialog should be shown. This value defaults to half second. Because if GUI doesn't respond within half second, user will clearly fees that GUI is slow. If the task completes before the milliSecondsToWait is elapsed, then progress dialog is not shown.

 ProgressMonitor fires ChangeEvent which can be listened by some other class to show progress dialog. Thus GUI is decoupled from model.

 Now create a ProgressDialog2 class:
 */

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
class ProgressDialog extends JDialog implements ChangeListener {
   JLabel statusLabel = new JLabel();
   JProgressBar progressBar = new JProgressBar();
   ProgressMonitor monitor;

   public ProgressDialog(Frame owner, ProgressMonitor monitor)
      throws HeadlessException {
      super(owner, "Progress", true);
      init(monitor);
   }

   public ProgressDialog(Dialog owner, ProgressMonitor monitor)
      throws HeadlessException {
      super(owner);
      init(monitor);
   }

   private void init(ProgressMonitor monitor) {
      this.monitor = monitor;

      progressBar = new JProgressBar(0, monitor.getTotal());
      if (monitor.isIndeterminate()) {
         progressBar.setIndeterminate(true);
      } else {
         progressBar.setValue(monitor.getCurrent());
      }
      statusLabel.setText(monitor.getStatus());

      JPanel contents = (JPanel)getContentPane();
      contents.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      contents.add(statusLabel, BorderLayout.NORTH);
      contents.add(progressBar);

      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      monitor.addChangeListener(this);
   }

   public void stateChanged(final ChangeEvent ce) {
      // to ensure EDT thread
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  stateChanged(ce);
               }
            });
         return;
      }

      if (monitor.getCurrent() != monitor.getTotal()) {
         statusLabel.setText(monitor.getStatus());
         if (!monitor.isIndeterminate()) {
            progressBar.setValue(monitor.getCurrent());
         }
      } else {
         dispose();
      }
   }
}


/*
 ProgressDialog2 has a JLabel which shows the status message and JProgressBar
  to show amount of progress done. It listens to ChangeEvents from
 ProgressMonitor and updates the JLabel and JProgressBar.
 When ProgressMonitor's value hits its total value, dialog is disposed.
 It ensures that GUI is updated in EDT thread, because ChangeEvents
 from ProgressMonitor are always fired in Non-EDT thread (see stateChanged method).

 ProgressMonitor can be used to show a modal progress dialog or event show the
 progress in status bar of the application, because we decoupled how progress
 is shown from its model. Now let us see how to create a ProgressMonitor which shows
 modal progress dialog:
 */

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
class ProgressUtil {
   public static ProgressMonitor createModalProgressMonitor(Component owner, int total,
      boolean indeterminate, int milliSecondsToWait) {
      ProgressMonitor monitor = new ProgressMonitor(total, indeterminate, milliSecondsToWait);
      Window window =
         (owner instanceof Window) ? (Window)owner : SwingUtilities.getWindowAncestor(owner);
      monitor.addChangeListener(new MonitorListener(window, monitor));
      return monitor;
   }

   static class MonitorListener implements ChangeListener, ActionListener {
      ProgressMonitor monitor;
      Window owner;
      Timer timer;

      public MonitorListener(Window owner, ProgressMonitor monitor) {
         this.owner = owner;
         this.monitor = monitor;
      }

      public void stateChanged(ChangeEvent ce) {
         ProgressMonitor monitor = (ProgressMonitor)ce.getSource();
         if (monitor.getCurrent() != monitor.getTotal()) {
            if (timer == null) {
               timer = new Timer(monitor.getMilliSecondsToWait(), this);
               timer.setRepeats(false);
               timer.start();
            }
         } else {
            if ((timer != null) && timer.isRunning()) {
               timer.stop();
            }
            monitor.removeChangeListener(this);
         }
      }

      public void actionPerformed(ActionEvent e) {
         monitor.removeChangeListener(this);
         ProgressDialog dlg =
            (owner instanceof Frame) ? new ProgressDialog((Frame)owner, monitor)
                                     : new ProgressDialog((Dialog)owner, monitor);
         dlg.pack();
         dlg.setLocationRelativeTo(null);
         dlg.setVisible(true);
      }
   }
}
/*
 User creates a ProgressMonitor using createModalProgressMonitor(...) method.
 The first argument owner is used to find the owner window for ProgressDialog2.
 We create a MonitorListener and register with ProgressMonitor.
 When ProgressMonitor.start(...) is called, MonitorListener starts a
 timer with milliSecondsToWait time. When timer hits its finish time,
 we show ProgressDialog2, We also ensure that MonitorListener is unregistered
 from ProgressMonitor when its job is complete.
 */
