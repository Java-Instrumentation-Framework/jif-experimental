/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.gui.help;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.fit.cssbox.swingbox.BrowserPane;

/**
 *
 * @author GBH
 */
public class TestSwingBox {
   public void test() {
      BrowserPane swingbox = new BrowserPane();
// add the component to your GUI
      JFrame jFrame = new JFrame();
jFrame.add(swingbox);
jFrame.setVisible(true);
      try {
         // display the page
         swingbox.setPage(new URL("http://cssbox.sourceforge.net"));
      } catch (IOException ex) {
         Logger.getLogger(TestSwingBox.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public static void main(String[] args) {
      new TestSwingBox().test();
   }
}
