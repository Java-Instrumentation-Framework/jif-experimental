package edu.mbl.jif.camera.display;

import edu.mbl.jif.imaging.stats.Equalizer;
import edu.mbl.jif.camera.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.swing.*;

import com.holub.asynch.Condition;
import edu.mbl.jif.utils.prefs.Prefs;



public class DisplayPanel
      extends JPanel
{
   //Graphics2D      overGraphics;
   //  Graphics2D      buffGraphics;
   //  Graphics2D      msgGraphics;
   //
   // for Camera Image sample
   BasicStroke strokeA;
   BasicStroke strokeB;

   //
   BufferedImage bImage;
   BufferedImage overImage;
   Font font = new Font("Arial", Font.PLAIN, 18);

   //
   AffineTransform mirrorTx;
   LookupOp lookupOp = null;
   LookupTable lut = null;

   // for drawing ROI rectangle & displaying parameters
   Rectangle ROI;
   Rectangle box;
   Rectangle ratioingROI;
   RescaleOp rOp;
   String messageToDisplay = null;
   String parmROI = "";
   String parmROI2 = "";

   //
   VideoFileBinary videoFile = null; // for recording the video stream
   WritableRaster wr_bImage;
   boolean isSuspended = false;
   int framesRecorded = 0;
   Condition isRecording = new Condition(false);

   //
   boolean waitForNextFrame = false;
   float framesPerMsec = 0;

   // for resizing/scaling
   float xScale = 1;
   float yScale = 1;
   int cursorPosX = 0;
   int cursorPosY = 0;
   int frames = 0;
   int framesUpdate = 5;
   int iHeight; // Image dimensions (pixels)
   int iSize; // Image dimensions (pixels)
   int iWidth; // Image dimensions (pixels)
   int valueOfPixel = 0;
   long updateFreq = 250;
   long xTime = 0;

   //
   public Condition antiAliasing = new Condition(false);

   // for PolView------------

   private boolean polView;
   private int buffNum = 0;
   private int displayMode = 0;
   private int framesWaited = 0;
   boolean doUpdateDisplayValues = true; //  <<< -----

   //
   public DisplayPanel (float scaleDisplay) {
      super();
      setLayout(new BorderLayout());
      iWidth = (int) Camera.width;
      iHeight = (int) Camera.height;
      iSize = iWidth * iHeight;
      updateFreq = Prefs.usr.getLong("display.UpdateFreq", 250);
      //////////////////
      // PolView...
      //    bufferA = new byte[iSize];
      //    bufferB = new byte[iSize];
      //    polViewImg = new byte[iSize];
      //    PolStackParms psParms = new PolStackParms("polView");
      //    psParms.setToAcquisitionSettingsPS();
      //    PolCalculator.initialize2FrameLookup8(psParms);
      //////////////////
      //
      // zero the Roi selection rectangle
      ROI = Camera.selectedROI;
      box = new Rectangle((int) (ROI.x * xScale), (int) (ROI.y * yScale),
            (int) (ROI.width * xScale), (int) (ROI.height * yScale));
//      ratioingROI = Ratioing.retrieveAcqRoi();
      // +++
      if (scaleDisplay < 1.0f) {
         strokeA = new BasicStroke(1.0f);
         strokeB = new BasicStroke(1.0f);
      } else {
         strokeA = new BasicStroke(2.0f);
         strokeB = new BasicStroke(2.0f);
      }

      // AffineTransform for mirroring
      mirrorTx = new AffineTransform();
      mirrorTx.scale(1.0, -1.0);
      //
      // Create the BufferedImage, the WritableRaster and buffGraphics
      if (QCamJNI.wideDepth) { // for 16-bit
         //to create a grayscale BufferedImage object from array of shorts
         rOp = new RescaleOp(16f, 0f, null);
         bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_USHORT_GRAY);
         wr_bImage = bImage.getRaster();
         wr_bImage.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
      } else { // for 8-bit
         bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
         wr_bImage = bImage.getRaster();
         wr_bImage.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
      }

//      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//      GraphicsDevice device = env.getDefaultScreenDevice();
//      GraphicsConfiguration gc = device.getDefaultConfiguration();
//      bImage = gc.createCompatibleImage(iWidth, iHeight);
//      wr_bImage = bImage.getRaster();
//      wr_bImage.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);


      setOpaque(true);
      //
      overImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_ARGB);
      //overGraphics = (Graphics2D) overImage.createGraphics();
      setVisible(true);
   }


   //-----------------------------------------------------------
   // Display LUT
   //
   public void setDisplayLUT (int mode) {
      displayMode = mode;
      updateDisplayLUT();
   }


   public void updateDisplayLUT () { // for enhanced mode display
      Object lutArray = null;
      if (displayMode == 0) { // normal display, no change
         lookupOp = null;
         return;
      }
      if (displayMode == 1) { // "Enhanced" display w/ Histogram Equalization
         if (QCamJNI.wideDepth) {
            lutArray = Equalizer.equalize(QCamJNI.pixels16, iWidth, iHeight);
            lut = new ShortLookupTable(0, (short[]) lutArray);
         } else {
            lutArray = Equalizer.equalize(QCamJNI.pixels8, iWidth, iHeight);
            lut = new ByteLookupTable(0, (byte[]) lutArray);
         }
         lookupOp = new LookupOp(lut, null);
      }
   }


   //-----------------------------------------------------------
   // Suspend camera display
   public synchronized void suspend (boolean setTo) {
      isSuspended = setTo;
   }


   //-----------------------------------------------------------
   // Turn on/off PolView "realtime" retardance image
   public synchronized void setPolView (boolean t) {
      polView = t;
      System.out.println("PolView set: " + t);
      recordRawVideo = Prefs.usr.getBoolean("video.record.raw", false);
   }


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
                  System.arraycopy(QCamJNI.pixels16, 0, Camera.sampleImageShort, 0,
                        QCamJNI.pixels16.length);
               }
            }

         } else {
            synchronized (QCamJNI.pixels8) {
               if (Camera.sampleImageByte != null) {
                  System.arraycopy(QCamJNI.pixels8, 0, Camera.sampleImageByte, 0,
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

      // PolView...
      if (false) { //polView) {
         // wr_bImage.setDataElements(0, 0, iWidth, iHeight, PolView.polViewCalc8());
      } else {
         // put the frame in the display
         if (QCamJNI.wideDepth) {
            synchronized (QCamJNI.pixels16) {
               wr_bImage.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
            }
            rOp.filter(wr_bImage, wr_bImage);
         } else {
            synchronized (QCamJNI.pixels8) {
               wr_bImage.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
               //aTx.filter(wr, wr);
            }
         }
      }

      // Record frame to video file...
      if (isRecording.is_true()) {
         synchronized (videoFile) {
            synchronized (QCamJNI.pixels8) {
               videoFile.writeFrame((byte[]) wr_bImage.getDataElements(0, 0, iWidth,
                     iHeight, null), System.currentTimeMillis());
               if (recordRawVideo) {
                  videoFile.writeFrame(QCamJNI.pixels8,
                        System.currentTimeMillis());
               }
            }
            framesRecorded++;
         }
      }

      // periodically update status indicators
      if (true) {
         frames++;
         if (frames > framesUpdate) {
            updateFPS();
            //updateDisplayLUT();
         }
      }
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            repaint();
         }
      });

   }


   //=======================================================================
   // Paint Component
   //

   public synchronized void paintComponent (Graphics g) {
      //super.paintComponent(g);
      if (!isShowing()) {
         return;
      }
      Graphics2D g2 = (Graphics2D) g;

      //    if(antiAliasing.is_true())
      //      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      //        RenderingHints.VALUE_ANTIALIAS_ON);

      g2.setFont(font);
      if (lookupOp != null) { // apply LookupTableOp
         try {
            lookupOp.filter(bImage, bImage);
         }
         catch (Exception ex) {}
      }
      if (Camera.mirrorImage) {
         //Graphics2D g2d = (Graphics2D) g;
         AffineTransform oldAT = g2.getTransform();
         AffineTransform defaultAffineTransform = g2.getTransform();
         AffineTransform affineTransform = new AffineTransform();
         affineTransform.scale( -1.0, 1.0);
         // affineTransform.translate( -bImage.getWidth(null), 0);
         affineTransform.translate((int) ( -iWidth * xScale), 0);
         g2.transform(affineTransform);
         // ?? g2d.drawImage(bImage, affineTransform, null);
         g2.drawImage(bImage, 0, 0, getWidth(), getHeight(), 0, 0, iWidth, iHeight, null);
         g2.setTransform(oldAT);
      } else {
         g2.drawImage(bImage, 0, 0, getWidth(), getHeight(), 0, 0, iWidth, iHeight, null);
      }

      if (videoFile != null) { // if recording video
         messageToDisplay = "Recording to file (" + framesRecorded + " frames)";
      }

      if (messageToDisplay != null) {
         synchronized (messageToDisplay) {
            // Find the size of string s in font f in the current Graphics context g.
            FontMetrics fm = g.getFontMetrics(font);
            java.awt.geom.Rectangle2D rect = fm.getStringBounds(messageToDisplay, g);
            int textHeight = (int) (rect.getHeight());
            int textWidth = (int) (rect.getWidth());
            int panelHeight = this.getHeight();
            int panelWidth = this.getWidth();
            // Center text horizontally and vertically
            int x = (panelWidth - textWidth);
            int y = (panelHeight - textHeight) - fm.getAscent();

            g2.setColor(Color.black);
            g2.drawString(messageToDisplay, x, y);
            g2.setColor(Color.yellow);
            g2.drawString(messageToDisplay, x - 2, y - 2);
         }
      }

      if (box.width > 0) { // draw the selected ROI
         g2.setColor(Color.blue);
         g2.setStroke(strokeA);
         g2.drawRect(box.x, box.y, box.width, box.height);
         g2.setColor(Color.yellow);
         g2.setStroke(strokeB);
         g2.drawRect(box.x + 1, box.y + 1, box.width, box.height);
      }
//      if (ratioingROI.width > 0) { // draw the selected ROI
//         g2.setColor(Color.black);
//         g2.setStroke(strokeA);
//         g2.drawRect(ratioingROI.x, ratioingROI.y, ratioingROI.width,
//                     ratioingROI.height);
//         g2.setColor(Color.green);
//         g2.setStroke(strokeB);
//         g2.drawRect(ratioingROI.x + 1, ratioingROI.y + 1, ratioingROI.width,
//                     ratioingROI.height);
//      }
      g2.dispose();
      //
      //    if (Camera.mirrorImage) {
      //      //AffineTransform defaultAffineTransform = buffGraphics2.getTransform();
      //      AffineTransform affineTransform = new AffineTransform();
      //      affineTransform.scale(-1.0, 1.0);
      //      affineTransform.translate(-bImage.getWidth(null), 0);
      //      overGraphics.transform(affineTransform);
      //    }
      //    if (Camera.mirrorImage) {
      //      overGraphics.setTransform(oldATx);
      //    }
      //overGraphics.dispose();
      // g2.setTransform(origTx);
      // g2.drawImage(bImage, tx, this);
      // g2.dispose();
      // buffGraphics2.dispose();
   }


   //////////////////////////////////////////////////////////////////////////
   //
   public void updateFPS () {
      // Measure Frames Per Second & display in frame title
      framesPerMsec = ((float) frames)
            / (float) (System.currentTimeMillis() - xTime);
      framesUpdate = (int) (framesPerMsec * (float) updateFreq);
//      if (PSj.cameraPanel != null) {
//         PSj.cameraPanel.value_FPS.setText(
//               String.valueOf((int) (framesPerMsec * 1000f)));
//      }
      Camera.setCurrentFPS(framesPerMsec);
      //
      updateDisplayValues();
      // reset
      xTime = System.currentTimeMillis();
      frames = 0;
      // autoExposure
   }


   void updateDisplayValues () {
      if (doUpdateDisplayValues) {
         synchronized (QCamJNI.pixels8) {
            if (Camera.sampleImageByte != null) {
               System.arraycopy(QCamJNI.pixels8, 0, Camera.sampleImageByte, 0,
                     QCamJNI.pixels8.length);
            }
         }
         // now do this update on a new thread
//         if (PSj.camCtrl != null) {
//            PSj.camCtrl.updateImageValues();
//         }
      }
   }


   //
   public void enableUpdateDisplayValues (boolean t) {
      doUpdateDisplayValues = t;
   }


   //
   //-----------------------------------------------------------
   //
   // autoExposure()

   /*    if (Globals.ctrlPanel.toggleAutoExposure.isSelected()) {
         if (Averager.getMaxInROI() < 200 || Averager.getMaxInROI() > 254) {
           String inStr = String.valueOf(CameraInterface.spinExpos.getModel().
                                         getValue());
           float expos = 0;
           try {
             expos = Float.parseFloat(inStr);
           }
           catch (NumberFormatException nfe) {}
           if (Averager.getMaxInROI() < 200) {
             expos = expos + (0.1f * expos);
           }
           else if (Averager.getMaxInROI() > 254) {
             expos = expos - (0.1f * expos);
           }
           final float setExpos = expos;
           final SwingWorker worker = new SwingWorker() {
             public Object construct() {
        CameraInterface.spinExpos.model_Expos.setValue(new Double( (double)
                   setExpos));
               return null;
             }
             public void finished() {
               CameraInterface.setDisplayOn();
             }
           };
           worker.start();
         }
       }
    */

   //----------------------------------------------------------------
   // Video Recording...
   //
   boolean recordRawVideo = Prefs.usr.getBoolean("video.record.raw", false);

   public void setVideoFile (VideoFileBinary vf) {
      if (vf != null) {
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


   //------------------------------------------------------------------
   // Show/Clear message over image in the display
   //
   public synchronized void showMessage (String message) {
      messageToDisplay = message;
      repaint();
   }


   public synchronized void clearMessage () {
      //isSuspended = true;
      messageToDisplay = null;
      //isSuspended = false;
   }


   /////////////////////////////////////////////////////////////
   // Value Point - for displaying the value of the pixel at the
   // cursor location in the image
   //
   public void valuePoint (int x, int y) {
      if ((x == -1) || (y == -1)) {
         if (Camera.display != null) {
//            if (PSj.camCtrl != null) {
//               PSj.camCtrl.label_Exp.setText(" ");
//               PSj.camCtrl.label_Exp.repaint();
//               return;
//            }
         }
         cursorPosX = (int) (x / xScale);
         cursorPosY = (int) (y / yScale);
         int pixOffset;

         // if Mirror,
         if (Prefs.usr.getBoolean("mirrorImage", false)) {
            pixOffset = (cursorPosY + (1 * iWidth)) + (cursorPosX - iWidth);
         } else {
            pixOffset = (cursorPosY * iWidth) + cursorPosX;
         }
         if (pixOffset >= 0 && pixOffset < iSize) {
            if (QCamJNI.wideDepth) { // for 16-bit
               valueOfPixel = (int) (QCamJNI.pixels16[pixOffset]);
            } else { // for 8-bit
               valueOfPixel = (int) (QCamJNI.pixels8[pixOffset] & 0x000000FF);
            }
         } else {
            valueOfPixel = -1;
         }
         if (Camera.display != null) {
//            if (PSj.camCtrl != null) {
//               if (valueOfPixel == -1) {
//                  PSj.camCtrl.label_Exp.setText(" ");
//               } else {
//                  PSj.camCtrl.label_Exp.setText("(" + cursorPosX + ", "
//                        + cursorPosY +
//                        "): " + String.valueOf(valueOfPixel) + "  ");
//               }
//               PSj.camCtrl.label_Exp.repaint();
//            }
         }
      }
   }


   ///////////////////////////////////////////////////////////////////////////
   // For ROI selection retangle...
   //
   public Rectangle getRoiBox () {
      return box.getBounds();
   }


   public void setRoiBox (Rectangle r) {
      rectChanged(r);
      roiChanged();
   }


   public void roiChanged () {
      // Called by SelectArea when ROI is changed:
      Camera.selectedROI = ROI;
//      if (PSj.acqPanel != null) {
//         PSj.acqPanel.updateEnableCorrections();
//      }
      if (Camera.simulation) {
         repaint();
      }
   }


   // Called by SelectArea as the selection rectangle changes
   public void rectChanged (Rectangle rect) {
      ROI.x = (int) (rect.x / xScale);
      ROI.y = (int) (rect.y / yScale);
      ROI.width = (int) (rect.width / xScale);
      ROI.height = (int) (rect.height / yScale);
      box = rect;
      if (Camera.simulation) {
         System.out.println("ROI: " + ROI.x + ", " + ROI.y + ", " + ROI.width + ", "
               + ROI.height);
         System.out.println("box: " + box.x + ", " + box.y + ", " + box.width + ", "
               + box.height);
         repaint();
      }
   }


   public void setRatioingROI (Rectangle r) {
      ratioingROI = r;
      // clear camera roi
      Camera.setROIRectangle(new Rectangle(0, 0, 0, 0));
   }


   //-----------------------------------------------------------
   // reScale
   //
   public void reScale (float xS, float yS) {
      xScale = xS;
      yScale = yS;
      if (xScale >= 1) {
         strokeA = new BasicStroke(2.0f);
         strokeB = new BasicStroke(2.0f);
      } else {
         strokeA = new BasicStroke(1.0f);
         strokeB = new BasicStroke(1.0f);
      }
   }


   public BufferedImage getImage () {
      BufferedImage img = null;
      if (lookupOp != null) { // apply LookupTableOp
         try {
            img = lookupOp.filter(bImage, null);
         }
         catch (Exception ex) {}
         return img;
      } else {
         return bImage;
      }
   }
   //----------------------------------------------------------------
   // --- Blanks the display

   /*  public void blankDisplay(String message) {
       if (QCamJNI.wideDepth) {
         for (int i = 0; i < QCamJNI.pixels16.length; i++) {
           QCamJNI.pixels16[i] = 2048;
    wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
           rop.filter(wr, wr);
         }
       }
       else {
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
    */


   // VideoPanel
}
