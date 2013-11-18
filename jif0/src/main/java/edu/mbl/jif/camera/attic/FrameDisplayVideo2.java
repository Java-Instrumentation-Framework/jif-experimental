package edu.mbl.jif.camera.attic;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import edu.mbl.jif.camera.*;


// PSj: VideoPanel
// JPanel for Asynchronous Video Camera image (real-time) Stream

public class FrameDisplayVideo2
      extends JFrame implements ComponentListener
{
   private static int iWidth; // Image dimensions (pixels)
   private static int iHeight; // Image dimensions (pixels)
   private static int frames = 0;
   private static float framesPerSec = 0;
   private float sizeOfDisplay = 1.0f;

   int vInsets = 38;
   int hInsets = 16;
   // Insets: java.awt.Insets[top=23,left=4,bottom=4,right=4]

   //
   public static VideoPanel2 vPanel;
   public static SelectionPanelArea selArea;

   public FrameDisplayVideo2 (float _sizeOfDisplay) {
      super();
      sizeOfDisplay = _sizeOfDisplay;
      try {
         jbInit();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      this.setVisible(false);
      this.setIconImage((new ImageIcon(this.getClass().getResource(
            "icons/camera16.gif"))).getImage());
//**      setTitle("Camera:  " + Camera.getCameraState());
      this.setUndecorated(true);
       setResizable(true);
      setBackground(Color.gray);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//    this.addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent ev) {
//        CameraInterface.closeDisplayFrame();
//      }
//      public void windowActivated(WindowEvent ev) {
//        CameraInterface.setDisplayOn();
//      }
//      public void windowLostFocus(WindowEvent ev) {
//        CameraInterface.setTriggerSoft();
//      }
//      public void windowGainedFocus(WindowEvent ev) {
//        CameraInterface.setDisplayOn();
//      }
//    });
      // image size (in pixels)
      iWidth = (int) Camera.width;
      iHeight = (int) Camera.height;
      // scaled image size
      int sWidth = (int) (iWidth * sizeOfDisplay);
      int sHeight = (int) (iHeight * sizeOfDisplay);

      // Add the VideoPanel
      vPanel = new VideoPanel2(sizeOfDisplay);
      vPanel.reScale(sizeOfDisplay, sizeOfDisplay);
      vPanel.setRoiBox(Camera.selectedROI); // restore a selected ROI
      vPanel.setBounds(4, 4, sWidth, sHeight);
      //
      selArea = new SelectionPanelArea(vPanel);
      selArea.setBounds(4, 4, sWidth, sHeight);
      this.getContentPane().setLayout(null);
      this.getContentPane().add(vPanel);
      this.getContentPane().setBackground(new Color(0, 165, 255));
      this.getContentPane().add(selArea);

      // Add camera control panel at the bottom of the display
      //contentPane.add(ctrlPanel, BorderLayout.SOUTH);
      //    vPanel.reScale((float) (getWidth() - insets.left - insets.right) / (float) (iWidth),
      //      (float) (getHeight() - 20 - insets.top - insets.bottom) / (float) (iHeight));
      // in order to Resize
      setSize(sWidth + hInsets, sHeight + vInsets);
      // Center the display
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = getSize();
      if (frameSize.height > screenSize.height) {
         frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
         frameSize.width = screenSize.width;
      }
      setLocation((screenSize.width - frameSize.width) / 2,
                  (screenSize.height - frameSize.height) / 2);
      addComponentListener(this); // for resizing
   }


   public void setBorderColor (Color c) {
      this.getContentPane().setBackground(c);
   }


   // ------------------------------------------------------------
   // If the frame/window is resized, the image is scaled to fit
   public void componentResized (ComponentEvent e) {
      // resize the frame and the videoPanel
      float xScale = (float) (getWidth() - hInsets) / (float) (iWidth);
      float yScale = (float) (getHeight() - vInsets) / (float) (iHeight);
      vPanel.reScale(xScale, yScale);
      vPanel.setBounds(4, 4, getWidth() - hInsets, getHeight() - vInsets);
      //selArea.reScale(xScale, yScale);
      selArea.setBounds(4, 4, getWidth() - hInsets, getHeight() - vInsets);
      this.validate();
      repaint();
   }


   public void componentHidden (ComponentEvent e) {}


   public void componentMoved (ComponentEvent e) {}


   public void componentShown (ComponentEvent e) {}

   // FrameDisplayVideo
}
