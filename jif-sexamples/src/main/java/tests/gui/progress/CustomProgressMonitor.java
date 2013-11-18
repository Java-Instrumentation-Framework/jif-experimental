 package tests.gui.progress;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;




public class CustomProgressMonitor extends Object {
   private CustomProgressMonitor root;
   private JDialog dialog;
   private JOptionPane pane;
   private JProgressBar myBar;
   private JLabel noteLabel;
   private Component parentComponent;
   private String note;
   private Object message;
   private long T0;
   private int millisToDecideToPopup = 500;
   private int millisToPopup = 2000;
   private int min;
   private int max;
   private int lastDisp;
   private int reportDelta;


   public CustomProgressMonitor(Component parentComponent, Object message, String note, int min, int max) {
      this(parentComponent, message, note, min, max, null);
   }

   private CustomProgressMonitor(Component parentComponent, Object message, String note, int min, int max, CustomProgressMonitor group) {
      this.min = min;
      this.max = max;
      this.parentComponent = parentComponent;

      reportDelta = (max - min) / 100;
      if (reportDelta < 1) reportDelta = 1;
      this.message = message;
      this.note = note;
      if (group != null) {
         root = (group.root != null) ? group.root : group;
         T0 = root.T0;
         dialog = root.dialog;
      } else {
         T0 = System.currentTimeMillis();
      }
   }

   private class ProgressOptionPane extends JOptionPane {
      ProgressOptionPane(Object messageList) {
         super(messageList, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
      }

      public int getMaxCharactersPerLineCount() {
         return 60;
      }

      public JDialog createDialog(Component parentComponent, String title) {
         Frame frame = JOptionPane.getFrameForComponent(parentComponent);
         final JDialog dialog = new JDialog(frame, title, false);
         Container contentPane = dialog.getContentPane();

         contentPane.setLayout(new BorderLayout());
         contentPane.add(this, BorderLayout.CENTER);
         dialog.pack();
         dialog.setLocationRelativeTo(parentComponent);
         dialog.addWindowListener(new WindowAdapter() {
            boolean gotFocus = false;

            public void windowActivated(WindowEvent we) {
               // Once window gets focus, set initial focus
               if (!gotFocus) {
                  selectInitialValue();
                  gotFocus = true;
               }
            }
         });

         addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
               if (dialog.isVisible() && event.getSource() == ProgressOptionPane.this && (event.getPropertyName().equals(VALUE_PROPERTY) || event.getPropertyName().equals(INPUT_VALUE_PROPERTY))) {
                  dialog.setVisible(false);
                  dialog.dispose();
               }
            }
         });
         return dialog;
      }
   }

   public void setProgress(int nv) {
      if (nv >= max) {
         close();
      } else if (nv >= lastDisp + reportDelta) {
         lastDisp = nv;
         if (myBar != null) {
            myBar.setValue(nv);
         } else {
            long T = System.currentTimeMillis();
            long dT = (int)(T - T0);
            if (dT >= millisToDecideToPopup) {
               int predictedCompletionTime;
               if (nv > min) {
                  predictedCompletionTime = (int)((long)dT * (max - min) / (nv - min));
               } else {
                  predictedCompletionTime = millisToPopup;
               }
               if (predictedCompletionTime >= millisToPopup) {
                  myBar = new JProgressBar();
                  myBar.setMinimum(min);
                  myBar.setMaximum(max);
                  myBar.setValue(nv);
                  if (note != null) noteLabel = new JLabel(note);
                  pane = new ProgressOptionPane(new Object[]{message, noteLabel, myBar});
                  dialog = pane.createDialog(parentComponent, UIManager.getString("ProgressMonitor.progressText"));
                  dialog.show();
               }
            }
         }
      }
   }

   public void close() {
      if (dialog != null) {
         dialog.setVisible(false);
         dialog.dispose();
         dialog = null;
         pane = null;
         myBar = null;
      }
   }

   public int getMinimum() {
      return min;
   }

   public void setMinimum(int m) {
      min = m;
   }

   public int getMaximum() {
      return max;
   }

   public void setMaximum(int m) {
      max = m;
   }

   public boolean isCanceled() {
      return false;
   }

   public void setMillisToDecideToPopup(int millisToDecideToPopup) {
      this.millisToDecideToPopup = millisToDecideToPopup;
   }

   public int getMillisToDecideToPopup() {
      return millisToDecideToPopup;
   }

   public void setMillisToPopup(int millisToPopup) {
      this.millisToPopup = millisToPopup;
   }

   public int getMillisToPopup() {
      return millisToPopup;
   }

   public void setNote(String note) {
      this.note = note;
      if (noteLabel != null) {
         noteLabel.setText(note);
      }
   }

   public String getNote() {
      return note;
   }

   public static void main(String[] args) {
      final JFrame frame = new JFrame("test custom progress monitor");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      final CustomProgressMonitor pm = new CustomProgressMonitor(frame, "Please wait...", "", 0, 10);
      frame.setContentPane(new JButton(new AbstractAction("lanchProcess") {
         public void actionPerformed(ActionEvent e) {
            Runnable runner = new Runnable() {
               public void run() {
                  for (int i = 0; i <= 10; i++) {
                     final int index = i;
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                           pm.setProgress(index);
                           pm.setNote("step " + index);
                        }
                     });
                     try {
                        Thread.sleep(1000);
                     } catch (InterruptedException e1) {
                        e1.printStackTrace();
                     }
                  }
               }
            };
            new Thread(runner).start();
         }
      }));

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            frame.pack();
            frame.show();
         }
      });

   }
}
