package tests.gui.keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class KeystrokeSample
{
   public static void main (String args[]) {
      JFrame frame = new JFrame("KeyStroke Sample");
      JButton buttonA = new JButton("<html><center>FOCUSED<br>control alt 7");
      JButton buttonB =
            new JButton("<html><center>FOCUS/RELEASE<br>VK_ENTER");
      JButton buttonC =
            new JButton("<html><center>ANCESTOR<br>VK_F4+SHIFT_MASK");
      JButton buttonD =
            new JButton("<html><center>WINDOW<br>' '");

      // Define ActionListener

      ActionListener actionListener =
            new ActionListener()
      {
         public void actionPerformed (ActionEvent actionEvent) {
            System.out.println("Activated, " + actionEvent.getActionCommand());
         }
      };

      KeyStroke controlAlt7 = KeyStroke.getKeyStroke("control alt 7");
      buttonA.registerKeyboardAction(
            actionListener, controlAlt7, JComponent.WHEN_FOCUSED);

      KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
      buttonB.registerKeyboardAction(
            actionListener, enter, JComponent.WHEN_FOCUSED);

      KeyStroke shiftF4 =
            KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_MASK);
      buttonC.registerKeyboardAction(
            actionListener, shiftF4,
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

      KeyStroke space = KeyStroke.getKeyStroke(' ');
      buttonD.registerKeyboardAction(
            actionListener, space,
            JComponent.WHEN_IN_FOCUSED_WINDOW);

      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new GridLayout(2, 2));
      contentPane.add(buttonA);
      contentPane.add(buttonB);
      contentPane.add(buttonC);
      contentPane.add(buttonD);
      frame.setSize(400, 200);
      frame.setVisible(true);
   }
}
