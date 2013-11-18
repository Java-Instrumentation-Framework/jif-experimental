
package edu.mbl.jif.camera;

import edu.mbl.jif.camera.display.DisplayImage;
import javax.swing.JFrame;
import javax.swing.event.*;
import javax.swing.SwingUtilities;


public class TestLuCamSync
{

   public TestLuCamSync () {

      final byte[] img1 = new byte[640 * 480];
      final byte[] img2 = new byte[640 * 480];
      for (int i = 0; i < img1.length; i++) {
         img1[i] = 127;
      }
      for (int i = 0; i < img2.length; i++) {
         img2[i] = 64;
      }
      int size = (640 * 480);
      byte[] ret;
      int numCams = LuCamJNI.getNumCameras();
      System.out.println("Number of Cameras: " + numCams);
      if (LuCamJNI.initializeSynchronous() >= 0) {
         ret = LuCamJNI.snapshotSynchronous();
         if (ret != null) {

            System.out.println("ret.length = " + ret.length);

            System.arraycopy(ret, 0, img1, 0, size);
            System.arraycopy(ret, size, img2, 0, size);

            SwingUtilities.invokeLater(new Runnable()
            {
               public void run () {
                  DisplayImage ip1 = new DisplayImage(640, 480);
                  ip1.updateImage(img1);
                  JFrame ft1 = new JFrame();
                  ft1.add(ip1);
                  ft1.pack();
                  ft1.setVisible(true);
               }
            });

            SwingUtilities.invokeLater(new Runnable()
            {
               public void run () {
                  DisplayImage ip2 = new DisplayImage(640, 480);
                  ip2.updateImage(img2);
                  JFrame ft2 = new JFrame();
                  ft2.add(ip2);
                  ft2.pack();
                  ft2.setVisible(true);
               }
            });
         }
         LuCamJNI.terminateSynchronous();
      }
   }

// Test Main....................................

   public static void main (String[] args) {
      TestLuCamSync testlucam = new TestLuCamSync();
   }
}
