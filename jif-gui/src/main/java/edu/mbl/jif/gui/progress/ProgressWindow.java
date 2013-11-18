package edu.mbl.jif.gui.progress;

import edu.mbl.jif.gui.swingthread.LongTaskTest;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import edu.mbl.jif.gui.*;


public class ProgressWindow extends JInternalFrame {
  public final static int ONE_SECOND = 1000;
  private ProgressMonitorD progressMonitor;
  private Timer timer;
  private JButton startButton;
  private JTextArea taskOutput;
  private String newline = "\n";
  private LongTaskTest task;


  public ProgressWindow(String msg, LongTaskTest _task) {
    task = _task;
    timer = new Timer(ONE_SECOND, new TimerListener());

    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception {
  }

  public void progressUpdate(int value) {
    //jProgressBar.setValue(value);
  }

  class TimerListener
    implements ActionListener {
  public void actionPerformed(ActionEvent evt) {
    progressMonitor.setProgress(task.getCurrent());
    String s = task.getMessage();
    if (s != null) {
      progressMonitor.setNote(s);
      taskOutput.append(s + newline);
      taskOutput.setCaretPosition(
          taskOutput.getDocument().getLength());
    }
    if (progressMonitor.isCancelled() || task.isDone()) {
      progressMonitor.destroy();
      task.stop();
      Toolkit.getDefaultToolkit().beep();
      timer.stop();
      if (task.isDone()) {
        taskOutput.append("Task completed." + newline);
      }
      else {
        taskOutput.append("Task canceled." + newline);

      }
      startButton.setEnabled(true);
    }
  }
}

}
