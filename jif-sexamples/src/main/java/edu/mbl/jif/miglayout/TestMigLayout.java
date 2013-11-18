/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.miglayout;

/**
 *
 * @author GBH
 */
import edu.mbl.jif.miglayout.Mig;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

public class TestMigLayout extends JFrame {

//   public TestMigLayout() {
//      super("MigLayout Basic");
//      setLayout(new MigLayout("", "[grow]", "[grow]"));
//      add(new JLabel("Username:"), "right");
//      add(new JTextField(), "growx, left, wrap, w 100");
//      add(new JLabel("Password:"), "right");
//      add(new JPasswordField(), "growx, left, wrap, w 100");
//      add(new JCheckBox("Remember Me"), "center, wrap, span");
//      add(new JButton("Login"), "split 3, span 3, center");
//      add(new JButton("Close"));
//      add(new JButton("Another"));
//      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      pack();
//      setLocationRelativeTo(null);
//      setVisible(true);
//   }

      public TestMigLayout() {
      super("MigLayout Basic");
      setLayout(new MigLayout("", "[grow]", "[grow]"));
      JPanel p = new JPanel(new MigLayout());
      Mig.addSeparator(p, "ROI for Cropping");
       
      JLabel xLabel = new JLabel("X", SwingConstants.LEADING);
      p.add(xLabel, "gap para");
      
      p.add(Mig.createTextField(4));
      p.add(Mig.createLabel("Y"), "gap para");
      p.add(Mig.createTextField(4) );
      p.add(Mig.createLabel("W"), "gap para");
      p.add(Mig.createTextField(4) );
      p.add(Mig.createLabel("H"), "gap para");
      p.add(Mig.createTextField(4), "span, wrap para");
//      Mig.addSeparator(p, "Propeller");
//      p.add(Mig.createLabel("PTI/kW"), "gap para");
//      p.add(Mig.createTextField(10));
//      p.add(Mig.createLabel("Power/kW"), "gap para");
//      p.add(Mig.createTextField(10), "wrap");
//      p.add(Mig.createLabel("R/mm"), "gap para");
//      p.add(Mig.createTextField(10));
//      p.add(Mig.createLabel("D/mm"), "gap para");
//      p.add(Mig.createTextField(10));
      add(p);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);
      setVisible(true);
   }
      
//      public TestMigLayout() {
//      super("MigLayout Basic");
//      setLayout(new MigLayout("", "[grow]", "[grow]"));
//      JPanel p = Mig.createTabPanel(new MigLayout());
//
//      Mig.addSeparator(p, "General");
//      p.add(Mig.createLabel("Company"), "gap para");
//      p.add(Mig.createTextField(""), "span, growx, wrap");
//      p.add(Mig.createLabel("Contact"), "gap para");
//      p.add(Mig.createTextField(""), "span, growx, wrap para");
//      Mig.addSeparator(p, "Propeller");
//      p.add(Mig.createLabel("PTI/kW"), "gap para");
//      p.add(Mig.createTextField(10));
//      p.add(Mig.createLabel("Power/kW"), "gap para");
//      p.add(Mig.createTextField(10), "wrap");
//      p.add(Mig.createLabel("R/mm"), "gap para");
//      p.add(Mig.createTextField(10));
//      p.add(Mig.createLabel("D/mm"), "gap para");
//      p.add(Mig.createTextField(10));
//      add(p);
//      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      pack();
//      setLocationRelativeTo(null);
//      setVisible(true);
//   }

   public static void main(String args[]) {
      try {
         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception e) {
         // If Nimbus is not available, you can set the GUI to another look and feel.
      }
      new TestMigLayout();
   }
}
