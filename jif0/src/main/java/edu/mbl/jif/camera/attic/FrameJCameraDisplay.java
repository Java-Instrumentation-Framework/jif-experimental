package edu.mbl.jif.camera.attic;

import edu.mbl.jif.camera.*;
import edu.mbl.jif.utils.prefs.Prefs;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;




//import psj.PSj;

// PSj: VideoPanel
// JPanel for Asynchronous Video Camera image (real-time) Stream
//
public class FrameJCameraDisplay
      extends JFrame implements ComponentListener, FocusListener, MouseWheelListener
{
   //
   private static int iWidth; // Image dimensions (pixels)
   private static int iHeight; // Image dimensions (pixels)
   private static int sWidth;
   private static int sHeight;
   private int hInsets = 18;
   private int vInsets = 40;
   private float displayScale = 1.0f;

   public static VideoPanel2 vPanel;
   public static SelectionPanelArea selArea;

   // Insets: java.awt.Insets[top=23,left=4,bottom=4,right=4]
   public FrameJCameraDisplay (float _displayScale) {
      //super("CameraDisplay", false, false, true, false);
      displayScale = _displayScale;
      try {
         jbInit();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
//      try {
//         //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//         UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.
//               SyntheticaBlueIceLookAndFeel());
//      }
//      catch (Exception e) {
//         e.printStackTrace();
//      }

      this.setVisible(false);
      try {
         this.setIconImage((new ImageIcon(this.getClass().getResource(
               "icons/camera16.gif"))).getImage());
      }
      catch (Exception ex) {
      }

      setTitle("Camera:  " + Camera.getCameraState());
      //getRootPane().setWindowDecorationStyle(JRootPane.NONE);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      // Add the VideoPanel with selection area (for ROI)
      vPanel = new VideoPanel2(displayScale);
      selArea = new SelectionPanelArea(vPanel);
      vPanel.setRoiBox(Camera.selectedROI); // restore a selected ROI
      setScale(displayScale);
      //
      this.getContentPane().setLayout(null);
      this.getContentPane().add(vPanel);
//    this.getContentPane().setBackground(PSj.COLOR_CAMERA[1]);
      this.getContentPane().add(selArea);
      addComponentListener(this); // for resizing
//    if (PSj.stage != null)  // mousewheel control of focus
//       if (PSj.stage.isFunctional)
//         addMouseWheelListener(this);

      //setSize(new Dimension(508, 204));
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = getSize();
      if (frameSize.height > screenSize.height) {
         frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
         frameSize.width = screenSize.width;
      }
      setLocation((screenSize.width - frameSize.width - 10), 150);
      setVisible(true);

   }


   //-----------------------------------------------------------
   //

   public void addFocus () {
      addFocusListener(this);
   }


   public void mouseWheelMoved (MouseWheelEvent e) {
      //System.out.println("Mouse wheel clicks: " + e.getWheelRotation());
      int i = e.getWheelRotation();
//     if(PSj.deskTopFrame != null && PSj.stagePanel != null) {
//        if (i < 0)
//           PSj.stagePanel.moveUp(PSj.stagePanel.getMoveIncrement());
//        else
//           PSj.stagePanel.moveDown(PSj.stagePanel.getMoveIncrement());
//     }
   }


   public void setAntiAliasing (boolean t) {
//    if (Prefs.usr.getBoolean("display.antiAliasing", false))
//      vPanel.antiAliasing.set_true();
//    else
//      vPanel.antiAliasing.set_false();
   }


   public void setBorderColor (Color c) {
      this.getContentPane().setBackground(c);
   }


   public void setScale (float scale) {
      displayScale = scale;
      Prefs.usr.putFloat("displayScale", scale);
      setFrameSizes();
      vPanel.setRoiBox(Camera.selectedROI); // restore a selected ROI
   }


   public void setFrameSizes () {
      this.setVisible(false);
      iWidth = (int) Camera.width;
      iHeight = (int) Camera.height;
      // scaled image size
      sWidth = (int) (iWidth * displayScale);
      sHeight = (int) (iHeight * displayScale);
      vPanel.reScale(displayScale, displayScale);
      vPanel.setBounds(4, 4, sWidth, sHeight);
      selArea.setBounds(4, 4, sWidth, sHeight);
      //selArea.reScale(displayScale, displayScale);
      setSize(sWidth + hInsets, sHeight + vInsets);
//    if(PSj.deskTopFrame != null) {
//       setLocation(PSj.deskTopFrame.getWidth() - this.getWidth() - 10, 5);
//    }
      this.validate();
      //this.updateUI();
      this.setVisible(true);
      //this.repaint();
   }


   // ------------------------------------------------------------
   // If the frame/window is resized, the image is scaled to fit
   public void componentResized (ComponentEvent e) {
      // resize the frame and the videoPanel
      // setFrameSizes();

      float xScale = (float) (getWidth() - hInsets) / (float) (iWidth);
      float yScale = (float) (getHeight() - vInsets) / (float) (iHeight);
      vPanel.reScale(xScale, yScale);
      vPanel.setBounds(4, 4, getWidth() - hInsets, getHeight() - vInsets);
      selArea.reScale(xScale, yScale);
      selArea.setBounds(4, 4, getWidth() - hInsets, getHeight() - vInsets);
      this.validate();
      repaint();

   }


   public void componentHidden (ComponentEvent e) {}


   public void componentMoved (ComponentEvent e) {}


   public void componentShown (ComponentEvent e) {
      //    this.revalidate();
      //    repaint();
   }


   //----------------------------------------------------------------
   //
   public void focusGained (FocusEvent e) {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            System.out.println("Display-FocusGained");
            Camera.displayResume();
            //        if (Prefs.usr.getBoolean("stopDisplayNotFocused", false)) {
            //          Camera.setDisplayOn();
            //        }
         }
      });
   }


   public void focusLost (FocusEvent e) {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            System.out.println("Display-FocusLost");
            Camera.displaySuspend();
            //        if (Prefs.usr.getBoolean("stopDisplayNotFocused", false)) {
            //          Camera.setTriggerSoft();
            //        }
         }
      });
   }
}
