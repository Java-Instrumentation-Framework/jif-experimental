package tests.gui.keyboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class KeyBdTest
{
   public KeyBdTest () {
   }


   public static void main (String[] args) {

      JFrame f = new JFrame("X");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JPanel contentPane = new JPanel(new GridBagLayout());
      f.setContentPane(contentPane);
      //some quick contents
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      for (int i = 0; i < 10; ++i) {
         contentPane.add(new JTextField(40), gbc);
      }

      // Add CTRL-E hotkey  when focus in within window containing root/contentPane!

      Action action = new AbstractAction("qwerty")
      {
         public void actionPerformed (ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "action invoked");
         }
      };
      //  KeyStroke ctrlE = KeyStroke.getKeyStroke(KeyEvent.VK_E,  InputEvent.CTRL_DOWN_MASK);
      //  Object key = action.getValue(Action.NAME);
      //  contentPane.getActionMap().put(key, action);
      //or use WHEN_ANCESTOR_OF_FOCUSED_COMPONENT:
      //  contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlE, key);

      //It is better to use the rootpane instead of the contentpane :
      InputMap im = f.getRootPane().getInputMap(JComponent.
                                                WHEN_IN_FOCUSED_WINDOW);
      ActionMap am = f.getRootPane().getActionMap();
      im.put(
            KeyStroke.getKeyStroke(KeyEvent.VK_E,
                                   InputEvent.CTRL_DOWN_MASK),
            "myAction");
      am.put("myAction", new AbstractAction("qwerty")
      {
         public void actionPerformed (ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "action invoked");
         }
      });

      //launch that pup
      f.pack();
      f.setVisible(true);
   }
}
