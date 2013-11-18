/*
 * TestPanelButtons.java
 *
 * Created on April 27, 2006, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui;

import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.gui.command.CommandParsed;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class TestCommandlButtons extends JPanel {

   /** Creates a new instance of TestPanelButtons */
   public TestCommandlButtons() {
      super();
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
   }
   
   // ! Method passed must be public !
   public void addButton(String name, Object obj, String method) {
      JButton button = new JButton(name);
      try {
         button.addActionListener(CommandParsed.parse(obj, method));
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      this.add(button);
   }


   public static void main (String[] args) {
      FrameForTest f = new FrameForTest();
      TestCommandlButtons tb = new TestCommandlButtons();
      tb.addButton("Do Me", tb, "doMe();");
      f.add(tb);
      f.setVisible(true);
   }
}

