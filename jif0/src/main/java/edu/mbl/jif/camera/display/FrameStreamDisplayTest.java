package edu.mbl.jif.camera.display;

import edu.mbl.jif.camera.*;
import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import edu.mbl.jif.imaging.stream.StreamSource;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;


/**
 * <p>Title: </p>
 * <p>Description:
 * Test of ImageDisplayPanel as Streaming Display
 * This uses ZoomControl16 with MouseSensitiveZSP
 * <p>Copyright: Copyright (c) 2006</p>
 * @author gbh at mbl.edu
 * @version 1.0
 */
public class FrameStreamDisplayTest extends JFrame {
   
   BorderLayout borderLayout1 = new BorderLayout();
   int w;
   int h;
   BufferedImage img = null;
   ImageDisplayPanel viewPanel;
   StreamSource source;

   public FrameStreamDisplayTest(int w, int h) {
      this.w = w;
      this.h = h;
      this.img = img;
      try {
         jbInit();
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   private void jbInit() throws Exception {
      try {
         com.jgoodies.looks.plastic.Plastic3DLookAndFeel lookFeel =
            new com.jgoodies.looks.plastic.Plastic3DLookAndFeel();
         com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
         // new com.jgoodies.looks.plastic.theme.Silver());
         //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
         new com.jgoodies.looks.plastic.theme.DesertBlue());
         com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
         UIManager.setLookAndFeel(lookFeel);
      } catch (Exception e) {
         e.printStackTrace();
      }
      getContentPane().setLayout(borderLayout1);

      // getWorkSpaceBounds
      //GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

      // Open the camera and get it's StreamSource
      LuCamera cam1;
      try {
         cam1 = new LuCamera(1);
         cam1.getDeviceType();
         //cam1.initSettings(8, 10.0f, 1.0f);
         h = cam1.getHeight();
         w = cam1.getWidth();
         source = cam1.getStreamSource();
      } catch (Exception ex) {
         ex.printStackTrace();
         return;
      }
      if (source != null) {
         Dimension imageDim = new Dimension(w, h);
         viewPanel = new ImageDisplayPanel(imageDim);
         viewPanel.setStreamingSource(source);
         this.add(viewPanel, BorderLayout.CENTER);
         cam1.startStreamA();
         pack();
      }
   }

   // +++ On maximizeFrame and restore, do FitToWindow

   //   public void changeImage(BufferedImage img) {
   //      viewPanel.changeImage(img);
   //   }
   public static void main(String[] args) {
      //      SwingUtilities.invokeLater(new Runnable()
      //      {
      //         public void run () {
      try {
         FrameStreamDisplayTest f = new FrameStreamDisplayTest(640, 480);
         f.setVisible(true);
      } catch (Exception ex) {
         return;
      }

      //         }
      //      });
   }
}
