package edu.mbl.jif.camera.attic;

// OLD QCamJ video Panel

// JPanel for Asynchronous Video Camera image (real-time) Stream
// This is displayed inside of a FrameDisplayVideo object

import edu.mbl.jif.imaging.stats.Equalizer;
import edu.mbl.jif.camera.*;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;

import com.holub.asynch.Condition;
import edu.mbl.jif.gui.swingthread.SwingWorker3;


public class VideoPanelLucam
      extends JPanel
{
   int iWidth; // Image dimensions (pixels)
   int iHeight; // Image dimensions (pixels)
   int iSize; // Image dimensions (pixels)
   BufferedImage bImage;
   WritableRaster wr;

   RescaleOp rop;
   LookupTable lut = null;
   LookupOp lop = null;

   // for recording the video stream
   VideoFileTiff videoFile = null;
   Condition isRecording = new Condition(false);
   int framesRecorded = 0;

   boolean waitForNextFrame = false;
   private int framesWaited = 0;
   int framesUpdate = 5;

   // for resizing
   float xScale = 1;
   float yScale = 1;

   // for drawing ROI rectangle & displaying parameters
   Rectangle box;
   Rectangle ROI;
   BasicStroke strokeA;
   BasicStroke strokeB;
   Graphics2D buffGraphics;
   int frames = 0;
   long xTime = 0;
   float framesPerSec = 0;
   int valueOfPixel = 0;
   int cursorPosX = 0;
   int cursorPosY = 0;
   boolean showParms = false;
   boolean isSuspended = false;
   String messageToDisplay = null;
   long videoRecStart;

   //  String     parmExpFPS = "";
   //  String     parmSize = "";
   String parmROI = "";
   String parmROI2 = "";
   Font font = new Font("Arial", Font.PLAIN, 24);

   public VideoPanelLucam (float scaleDisplay) {
      super();
      iWidth = (int) Camera.width;
      iHeight = (int) Camera.height;
      iSize = iWidth * iHeight;
      setLayout(new BorderLayout());
      //setLocation(0, 0);
      //setSize( (int) (iWidth * scaleDisplay), (int) (iHeight * scaleDisplay));
      // this also displays the image size and the ROI selected
      //    parmSize =
      //      String.valueOf(iWidth) + "x" + String.valueOf(iHeight) + "pixels, "
      //      + ((QCamJNI.wideDepth) ? "16-bit" : "8-bit");
      //    parmExpFPS = (CameraInterface.exposure / 1000) + "ms";
      // zero the Roi selection rectangle
      box = new Rectangle(0, 0, 0, 0);
      ROI = new Rectangle(0, 0, 0, 0);
      setRoiFromCamera();
      if (scaleDisplay == 1.0f) {
         strokeA = new BasicStroke(1.0f);
         strokeB = new BasicStroke(1.0f);
      } else {
         strokeA = new BasicStroke(3.0f);
         strokeB = new BasicStroke(2.0f);
      }
      // Create the BufferedImage, the WritableRaster and buffGraphics
      if (QCamJNI.wideDepth) { // for 16-bit
//      Java 2D to create a BufferedImage object from array of shorts (grayscale image)
         rop = new RescaleOp(16f, 0f, null);
         bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_USHORT_GRAY);
         wr = bImage.getRaster();
         wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
         buffGraphics = (Graphics2D) bImage.createGraphics();
      } else { // for 8-bit
         bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
         wr = bImage.getRaster();
         wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
         buffGraphics = (Graphics2D) bImage.createGraphics();
      }
      buffGraphics.setFont(font);
      setVisible(true);
   }


   //============================================================
   public void setDisplayLUT (int mode) {
      Object lutArray = null;
      if (mode == 0) {
         lop = null;
         return;
      }
      if (mode == 1) {
         if (QCamJNI.wideDepth) {
            lutArray = Equalizer.equalize(QCamJNI.pixels16, iWidth,
                  iHeight);
            lut = new ShortLookupTable(0, (short[]) lutArray);
         } else {
            lutArray = Equalizer.equalize(QCamJNI.pixels8, iWidth,
                  iHeight);
            lut = new ByteLookupTable(0, (byte[]) lutArray);
         }
         lop = new LookupOp(lut, null);
      }
   }


   //============================================================

   void setRoiFromCamera () {
      box = Camera.selectedROI;
   }


   // DisplayUpdate ////////////////////////////////////////////////////////
   // This is the method that is called as a 'callback' from the C code
   // when the next frame in the stream is ready for display
   //
//   public synchronized void callBack () {
//      if (!isSuspended) {
//         if (QCamJNI.wideDepth) {
//            wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
//            rop.filter(wr, wr);
//         } else {
//            wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
//         }
//
//         //
//         if (!Averager.done) {
//            Averager.calcAndSave();
//
//         }
//         if (videoFile != null) { // if recording video
//            //videoFile.writeFrame(QCamJNI.pixels8);
//         }
//
//         repaint();
//
//         if (true) {
//            frames++;
//            if ((frames > 10)) {
//               updateFPS();
//            }
//         }
//
//      }
//   }


   //=======================================================================
   //      ****************   CallBack   *********************
   //=======================================================================
   // This is the method that is called as a 'callback' from the C code
   // when the next frame in the stream is ready for display from the camera
   //
   public void callBack () {
      if (isSuspended) {
         return;
      }
      // If grabbing a sample frame for statistics, wait for next frame
      if (waitForNextFrame) {
         if (QCamJNI.wideDepth) {
            synchronized (QCamJNI.pixels16) {
               if (Camera.sampleImageByte != null) {
                  System.arraycopy(QCamJNI.pixels16, 0,
                                   Camera.sampleImageShort, 0,
                                   QCamJNI.pixels16.length);
               }
            }

         } else {
            synchronized (QCamJNI.pixels8) {
               if (Camera.sampleImageByte != null) {
                  System.arraycopy(QCamJNI.pixels8, 0,
                                   Camera.sampleImageByte, 0,
                                   QCamJNI.pixels8.length);
               }
            }
         }
         Camera.grabDone.set_true();
         waitForNextFrame = false;
         framesWaited = 0;
         return;
      }
      if (!Camera.grabDone.is_true()) {
         if (framesWaited > 0) {
            waitForNextFrame = true;
            return;
         }
         framesWaited++;
      }
      // put the frame in the display
      if (QCamJNI.wideDepth) {
         synchronized (QCamJNI.pixels16) {
            wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
         }
         rop.filter(wr, wr);
      } else {
         synchronized (QCamJNI.pixels8) {
            wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
            //aTx.filter(wr, wr);
         }
      }
      // Record frame to video file...
      if (isRecording.is_true()) {
         synchronized (videoFile) {
            synchronized (QCamJNI.pixels8) {
               videoFile.writeFrame((byte[]) wr.getDataElements(0, 0, iWidth,
                     iHeight, null),
                                   System.currentTimeMillis());

            }
         }
         framesRecorded++;
      }
      // periodically update status indicators
      if (true) {
         frames++;
         if (frames > framesUpdate) {
            updateFPS();
            //updateDisplayLUT();
         }
      }
      synchronized (wr) {
         repaint();
      }
   } // <<< Callback


   public void suspend (boolean setTo) {
      isSuspended = setTo;
   }


//  public void update(Graphics g) {
//    paint(g);
//  }

   ////////////////////////////////////////////////////////////////////////////
   // Paint
   //
//  public synchronized void paint(Graphics g) {
   public synchronized void paintComponent (Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      Graphics2D buffGraphics2 = (Graphics2D) buffGraphics;
//    System.out.println("vPanel: " + getWidth() + ", " + getHeight()
//                       + "; ");

      //    if (false) { // if (showParms) {
      //      buffGraphics2.drawString(parmSize, 3, 25);
      //     parmExpFPS =
      //        "Exp: " + (CameraInterface.exposure / 1000) + "ms, "
      //        + String.valueOf((int) framesPerSec) + " fps";
      //      buffGraphics2.drawString(parmExpFPS, 3, 55);
      //      buffGraphics2.drawString(
      //        "Value: " + String.valueOf(valueOfPixel) + " @ (" + cursorPosX + ", "
      //        + cursorPosY + ")", 3, 85);
      //      if (box.width > 0) {
      //        buffGraphics2.drawString(
      //          parmROI + " Avg: " + String.valueOf((int) Averager.averageROI()), 3,
      //          115);
      //      }
      //    }
      if (videoFile != null) { // if recording video
         if (videoFile != null) { // if recording video
            messageToDisplay = "Recording to file (" + framesRecorded
                               + " frames)";
         }
      }
      if (messageToDisplay != null) {
         synchronized (messageToDisplay) {
            buffGraphics2.setColor(Color.black);
            buffGraphics2.drawString(messageToDisplay, 14, 30);
            buffGraphics2.setColor(Color.white);
            buffGraphics2.drawString(messageToDisplay, 12, 28);
         }
      }
      if (box.width > 0) { // draw the selected ROI
         buffGraphics2.setColor(Color.blue);
         buffGraphics2.setStroke(strokeA);
         buffGraphics2.drawRect(box.x, box.y, box.width, box.height);
         buffGraphics2.setColor(Color.yellow);
         buffGraphics2.setStroke(strokeB);
         buffGraphics2.drawRect(box.x + 1, box.y + 1, box.width, box.height);
      }
      if (lop != null) {
         try {
            lop.filter(bImage, bImage);
         }
         catch (Exception ex) {}
      }
      g2.drawImage(bImage, 0, 0, getWidth(), getHeight(), 0, 0, iWidth, iHeight, null);
      // g2.dispose();
      // buffGraphics2.dispose();
   }


   //////////////////////////////////////////////////////////////////////////
   //
   public void updateFPS () {
      // Measure Frames Per Second & display in frame title
      framesPerSec = (1000f * (float) frames)
                     / (float) (System.currentTimeMillis()
                                - xTime);
      xTime = System.currentTimeMillis();
      frames = 0;
//      if (Globals.ctrlPanel != null) {
//         Globals.ctrlPanel.value_FPS.setText(String.valueOf((int) framesPerSec));
//         updateROIAvg();
//
//         // Auto Exposure
//         if (Globals.ctrlPanel.toggleAutoExposure.isSelected()) {
//            if (Averager.getMaxInROI() < 200 || Averager.getMaxInROI() > 254) {
//               String inStr = String.valueOf(Camera.spinExpos.getModel().
//                     getValue());
//               float expos = 0;
//               try {
//                  expos = Float.parseFloat(inStr);
//               }
//               catch (NumberFormatException nfe) {}
//
//               if (Averager.getMaxInROI() < 200) {
//                  expos = expos + (0.1f * expos);
//               } else if (Averager.getMaxInROI() > 254) {
//                  expos = expos - (0.1f * expos);
//               }
//               final float setExpos = expos;
//
//               final SwingWorker worker = new SwingWorker()
//               {
//                  public Object construct () {
//                     Camera.spinExpos.model_Expos.setValue(new Double((double)
//                           setExpos));
//                     return null;
//                  }
//
//
//                  public void finished () {
//                     Camera.setDisplayOn();
//                  }
//               }; worker.start();
//            }
//         }
//      }
   }


   // --- For recording to file --------------------
   public void setVideoFile (VideoFileTiff vf) {
      if (vf != null) {
         videoRecStart = System.nanoTime();
         framesRecorded = 0;
         videoFile = vf;
         isRecording.set_true();
      } else {
         isRecording.set_false();
         messageToDisplay = "";
         synchronized (videoFile) {
            videoFile = vf;
         }
      }
   }


   // --- Blanks the display
   public void blankDisplay (String message) {
      if (QCamJNI.wideDepth) {
         for (int i = 0; i < QCamJNI.pixels16.length; i++) {
            QCamJNI.pixels16[i] = 2048;
            wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
            rop.filter(wr, wr);
         }
      } else {
         for (int i = 0; i < QCamJNI.pixels8.length; i++) {
            QCamJNI.pixels8[i] = (byte) 127;
         }
         wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
      }
      //repaint();
      Graphics g = getGraphics();
      if (message != null) {
         g.drawString(message, 10, iHeight / 2);
      }
   }


   //------------------------------------------------------------------
   // Show a message over frozen image in the display
   public synchronized void showMessage (String message) {
      messageToDisplay = message;
      repaint();
   }


   public synchronized void clearMessage () {
      isSuspended = true;
      messageToDisplay = null;
      isSuspended = false;
   }


   ///////////////////////////////////////////////////////////////////////////
   // For ROI selection retangle...
   public Rectangle getRoiBox () {
      return box.getBounds();
   }


   public void setRoiBox (Rectangle r) {
      box.setBounds(r.getBounds());
      //    box.x = r.x;
      //    box.y = r.y;
      //    box.width = r.width;
      //    box.height = r.height;
      roiChanged();
   }


   // Called by SelectArea when ROI is changed:
   public void roiChanged () {
//    CameraInterface.roiX = box.x;
//    CameraInterface.roiY = box.y;
//    CameraInterface.roiW = box.width;
//    CameraInterface.roiH = box.height;
      Camera.selectedROI = box;
      parmROI = "Avg: ";
      if (box.width == 0 || box.height == 0) {
         parmROI2 = " ";
      } else {
         parmROI2 = " [" + box.width + " x " + box.height + "] ";
      }
      updateROIAvg();
      if (Camera.simulation) {
         repaint();
      }
   }


   void updateROIAvg () {
//      if (Camera.display != null) {
//         float roiAvg = Averager.averageROI();
//         if (roiAvg != -1) {
//            if (Globals.ctrlPanel != null) {
//               Globals.ctrlPanel.label_Value.setText(parmROI + parmROI2
//                     + String.valueOf((int) roiAvg) + "  Max: "
//                     + String.valueOf(Averager.getMaxInROI()));
//            } else {
//               Globals.ctrlPanel.label_Value.setText(" ");
//            }
//            //Globals.ctrlPanel.repaint();
//         }
//      }
   }


   // Called by SelectArea as the selection rectangle changes
   public void rectChanged (Rectangle rect) {
//    box.x = (int) (rect.x);
//    box.y = (int) (rect.y);
//    box.width = (int) (rect.width);
//    box.height = (int) (rect.height);
      ROI.x = (int) (rect.x / xScale);
      ROI.y = (int) (rect.y / yScale);
      ROI.width = (int) (rect.width / xScale);
      ROI.height = (int) (rect.height / yScale);
      box = ROI;
//    box.x = (int) (rect.x * xScale);
//    box.y = (int) (rect.y * yScale);
//    box.width = (int) (rect.width * xScale);
//    box.height = (int) (rect.height * yScale);
//      Globals.ctrlPanel.label_Value.setText("ROI: " + ROI.x + ", " + ROI.y
//            + ", "
//            + ROI.width + ", " + ROI.height);
//      Globals.ctrlPanel.repaint();

      if (Camera.simulation) {
         System.out.println("ROI: " + ROI.x + ", " + ROI.y + ", " + ROI.width
                            + ", "
                            + ROI.height);
         System.out.println("box: " + box.x + ", " + box.y + ", " + box.width
                            + ", "
                            + box.height);
         repaint();
      }
   }


   public void reScale (float xS, float yS) {
      xScale = xS;
      yScale = yS;
   }


   // For displaying the value of the pixel at the cursor location on image
   // as well as the average value in the currently selected ROI
   //
   public void valuePoint (int x, int y) {
      if ((x == -1) || (y == -1)) {
         if (Camera.display != null) {
//            if (Globals.ctrlPanel != null) {
//               Globals.ctrlPanel.label_Exp.setText(" ");
//               Globals.ctrlPanel.label_Exp.repaint(); }
            return;
         }
      }
      cursorPosX = (int) (x / xScale);
      cursorPosY = (int) (y / yScale);
      int pixOffset = (cursorPosY * iWidth) + cursorPosX;
      if (pixOffset <= iSize) {
         if (QCamJNI.wideDepth) { // for 16-bit
            valueOfPixel = (int) (QCamJNI.pixels16[pixOffset]);
         } else { // for 8-bit
            valueOfPixel = (int) (QCamJNI.pixels8[pixOffset] & 0x000000FF);
         }
      } else {
         valueOfPixel = -1;
      }
      if (Camera.display != null) {
//         if (Globals.ctrlPanel != null) {
//            if (valueOfPixel == -1) {
//               Globals.ctrlPanel.label_Exp.setText(" ");
//            } else {
//
//               Globals.ctrlPanel.label_Exp.setText("(" + cursorPosX + ", "
//                     + cursorPosY
//                     + "): " + String.valueOf(valueOfPixel) + "  ");
//            }
//            Globals.ctrlPanel.label_Exp.repaint();
//         }
      }
   }

   /*
    import java.awt.color.*;
    LookupTable LUT;
// Create a look-up table for brightening pixels.
    public void brightenLUT() {
    short brighten[] = new short[256];
    for (int i=0; i<256; i++) {
    short pixelValue = (short) (i+10);
    if (pixelValue > 255)
    pixelValue = 255;
    else if (pixelValue < 0)
    pixelValue = 0;
    brighten = pixelValue;
    }
    LUT = new ShortLookupTable(0, brighten);
    }
// This method creates a look-up filter and applies it to
// the buffered image.
    public void applyFilter() {
    LookupOp lop = new LookupOp(LUT, null);
    lop.filter(bi, bi);
    }
// Create a look-up table to increase the contrast.
    public void contrastIncLUT() {
    short brighten[] = new short[256];
    for (int i=0; i<256; i++) {
    short pixelValue = (short) (i*1.2);
    if (pixelValue > 255)
    pixelValue = 255;
    else if (pixelValue < 0)
    pixelValue = 0;
    brighten = pixelValue;
    }
    LUT = new ShortLookupTable(0, brighten);
    }
//when you call theres methods you'll need to call the applyfilter()
//method after it and the repaint(), eg
    if(action.equals(B_INC))
    {
    brightenLUT();
    applyFilter();
    repaint();
    }
    */



   // VideoPanel

}
