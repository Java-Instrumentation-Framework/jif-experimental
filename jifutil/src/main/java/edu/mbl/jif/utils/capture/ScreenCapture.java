/*
 * ScreenCapture.java
 *
 * Created on May 30, 2006, 12:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.capture;

//import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/*
 * Save screenCaptures to .\_scrnCaps
 * Zip'em up and send with support email message.

/**
 *
 * @author GBH
 */
public class ScreenCapture {
   /** Creates a new instance of ScreenCapture */
   public ScreenCapture() {
   }

   public static BufferedImage capture() {
      BufferedImage bufferedImage = null;
      try {
         Robot     robot = new Robot();
         Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         bufferedImage = robot.createScreenCapture(captureSize);
      } catch (AWTException e) {
         System.err.println("Someone call a doctor!");
      }
      return bufferedImage;
   }
//   public static void main(String[] args) {
//       (new FrameImageDisplay(ScreenCapture.capture())).setVisible(true);
//       BufferedImage scrn = ScreenCapture.capture();
//       
//   }

}
