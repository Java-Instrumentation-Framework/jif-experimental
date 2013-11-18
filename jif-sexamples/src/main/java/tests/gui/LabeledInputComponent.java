package tests.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 *
 * @author GBH
 */
public class LabeledInputComponent extends JPanel {

   JLabel label;
   JComponent component;

   public LabeledInputComponent(String label, JComponent component) {
      super();
      this.label = new JLabel(label);
      this.component = component;
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      add(this.label);
      setMinimumSize(new Dimension(120,32));
      setMaximumSize(new Dimension(200,32));
      //this.setPreferredSize(new Dimension(32,120));
      add(Box.createHorizontalGlue());
      add(component);
      //add(Box.createRigidArea(new Dimension(10, 0)));
   }
   
   public static void main(String[] args) {
      JFrame frame = new JFrame("Labeled Component");
      frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.add(Box.createRigidArea(new Dimension(0, 10)));
      JSpinner spinner = new JSpinner();
      LabeledInputComponent lc =  new LabeledInputComponent("Spin Me", spinner );
      frame.getContentPane().add(lc);
      JSpinner spinner2 = new JSpinner();
      LabeledInputComponent lc2 =  new LabeledInputComponent("Spin Me2", spinner2 );
      frame.getContentPane().add(lc2);
      //frame.pack();
      frame.setBounds(100,100, 400,400);
      frame.setVisible(true);
   }
           
}
