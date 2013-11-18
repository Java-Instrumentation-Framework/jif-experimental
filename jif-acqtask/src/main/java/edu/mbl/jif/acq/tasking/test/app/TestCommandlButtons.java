/*
 * TestPanelButtons.java
 *
 * Created on April 27, 2006, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.tasking.test.app;

import edu.mbl.jif.acq.tasking.AcqRoutineExecutor;
import edu.mbl.jif.acq.tasking.AcquisitionRoutine;
import edu.mbl.jif.acq.tasking.test.TestAcqRoutine1;
import edu.mbl.jif.acq.tasking.test.TestAcqRoutineZ;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.gui.command.CommandParsed;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */
public class TestCommandlButtons extends JPanel {

   Box box;

   /**
    * Creates a new instance of TestPanelButtons
    */
   private TestCommandlButtons() {
      JFrame frame = new QuickFrame("Test Acq");
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      box = Box.createVerticalBox();

      add(box, BorderLayout.CENTER);
      JLabel label = new JLabel("---- Testing ----");
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      box.add(label);
      box.add(Box.createVerticalStrut(15));

      Action run1 = new AbstractAction("run1") {
         @Override
         public void actionPerformed(ActionEvent e) {
            runAcqRoutine(new TestAcqRoutine1("TestAcqRoutine1"));
         }
      };

      JButton b1 = new JButton(run1);
      addButton(b1);

      this.addCommands();
      frame.add(this, BorderLayout.CENTER);
      //setPreferredSize(new Dimension(300, 400));
      frame.setVisible(true);
   }

   public void addCommands() {
      addButton("Do Me", this, "doMe();");
      addButton("Acq1", this, "runAcq1();");
      addButton("AcqZ", this, "runAcqZ();");
   }

   //=====================================
   public void doMe() {
      System.out.println("Me done.");
   }

   public void runAcq1() {
      AcquisitionRoutine acqRoutine;
      acqRoutine = new TestAcqRoutine1("TestAcqRoutine1");
      runAcqRoutine(acqRoutine);
   }

   public void runAcqZ() {
      AcquisitionRoutine acqRoutine;
      acqRoutine = new TestAcqRoutineZ("TestAcqRoutineZ");
      runAcqRoutine(acqRoutine);
   }

   public void runAcqRoutine(AcquisitionRoutine acqRoutine) {
      AcqRoutineExecutor exec = new AcqRoutineExecutor(acqRoutine);
      exec.execute();
   }

   //=====================================
   // ! Method passed must be public !
   public void addButton(String name, Object obj, String method) {
      JButton button = new JButton(name);
      try {
         button.addActionListener(CommandParsed.parse(obj, method));
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      box.add(button);
      box.add(Box.createVerticalStrut(5));
   }

   public void addButton(JButton button) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      box.add(button);
      box.add(Box.createVerticalStrut(5));
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new TestCommandlButtons();
            //throw an UncaughtException
            //throw new IllegalArgumentException("foo");
         }
      });

   }
}

class QuickFrame extends JFrame {

   public QuickFrame(String title) {
      super(title);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(640, 480);
      setLocationByPlatform(true);
//      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//      setLocation(
//              Math.max(0, screenSize.width / 2 - getWidth() / 2),
//              Math.max(0, screenSize.height / 2 - getHeight() / 2));
   }
}
