package tests.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.GraphicsEnvironment;
import java.awt.Color;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FrameFullScreen
      extends JFrame
{
   BorderLayout borderLayout1 = new BorderLayout();

   public FrameFullScreen () {
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      getContentPane().setLayout(borderLayout1);
      setUndecorated(true);
      this.setBackground(Color.blue);



   }

   public static void main (String[] args) {
      FrameFullScreen fsf = new FrameFullScreen();
      GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fsf);
   }
}
