/*
 * VolatileImageTest.java
 */
package tests.gui;

import edu.mbl.jif.gui.test.FrameForTest;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.VolatileImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


/**
 *
 * @author GBH
 */
public class VolatileImageTest extends JPanel {
   
   VolatileImage volImage;
   Image origImage = null;

   public VolatileImageTest() {
      // Get image to move into accelerated image memory
      origImage = new ImageIcon("image.gif").getImage();
   }

   public void paint(Graphics g) {
      // Draw accelerated image
      int x = 0;
      int y = 0;
      volImage = drawVolatileImage((Graphics2D)g, volImage, x, y, origImage);
   }

   // Creating and Drawing an Accelerated Image
   // Images in accelerated memory are much faster to draw on the screen. 
   //   This example demonstrates how to take an image and make an accelerated 
   //         copy of it and then use it to draw on the screen.
   //  The problem with images in accelerated memory is that they can disappear 
   //         at any time. The system is free to free accelerated memory at 
   //         any time. Such images are called volatile images. 
   //         To deal with this issue, it is necessary to check whether a 
   //         volatile image is still valid immediately after drawing it. 
   //         If it is no longer valid, it is then necessary to reconstruct 
   //         the image and then attempt to draw with it again.

   // This example implements a convenient method for drawing volatile images. 
   // It automatically handles the task of reconstructing the volatile image 
   // when necessary.

   // This method draws a volatile image and returns it or possibly a
   // newly created volatile image object. Subsequent calls to this method
   // should always use the returned volatile image.
   // If the contents of the image is lost, it is recreated using orig.
   // img may be null, in which case a new volatile image is created.
   //
   
   public VolatileImage drawVolatileImage(Graphics2D g, VolatileImage img, int x, int y, Image orig) {
      final int MAX_TRIES = 100;
      for (int i = 0; i < MAX_TRIES; i++) {
         if (img != null) {
            // Draw the volatile image
            g.drawImage(img, x, y, null);

            // Check if it is still valid
            if (!img.contentsLost()) {
               return img;
            }
         } else {
            // Create the volatile image
            img = g.getDeviceConfiguration()
                   .createCompatibleVolatileImage(orig.getWidth(null), orig.getHeight(null));
         }

         // Determine how to fix the volatile image
         switch (img.validate(g.getDeviceConfiguration())) {
         case VolatileImage.IMAGE_OK:
            // This should not happen
            break;
         case VolatileImage.IMAGE_INCOMPATIBLE:
            // Create a new volatile image object;
            // this could happen if the component was moved to another device
            img.flush();
            img = g.getDeviceConfiguration()
                   .createCompatibleVolatileImage(orig.getWidth(null), orig.getHeight(null));
         case VolatileImage.IMAGE_RESTORED:
            // Copy the original image to accelerated image memory
            Graphics2D gc = (Graphics2D)img.createGraphics();
            gc.drawImage(orig, 0, 0, null);
            gc.dispose();
            break;
         }
      }

      // The image failed to be drawn after MAX_TRIES;
      // draw with the non-accelerated image
      g.drawImage(orig, x, y, null);
      return img;
   }
   
   public static void main(String[] args) {
      FrameForTest f = new FrameForTest();

   }
}
