package edu.mbl.jif.camera;

import edu.mbl.jif.camera.attic.FrameJCameraDisplay;
import edu.mbl.jif.utils.JifUtils;

//
// CameraInterface for QImaging Retiga using QCamAPI.
//
// Java interface to the QImaging Retiga Firewire Camera -
// relies on C-code interface (QCamJNI.c) to QCamAPI library.
// (To be, also, an ImageJ Plug-In.)
// authored by Grant B. Harris, MBL & CRI, 2002 - 2003.
//

/* THIS IS DEFUNCT */


import java.awt.*;
import javax.swing.*;
import com.holub.asynch.*;
import edu.mbl.jif.utils.prefs.Prefs;


public class Camera {
   public static int     err;
   public static boolean driverLoaded = false;
   public static boolean isOpen = false;
   public static String  mode = "closed";
   public static boolean busy = false;
   public static boolean simulation = false;

   // Camera Settings
   public static long width = -1;
   public static long height = -1;
   public static long size = -1;
   public static long depth = -1;
   public static long speed = -1;
   public static long exposure = -1; // in microseconds
   public static long gain;
   public static long offset;
   public static long exposureMin;
   public static long exposureMax;
   public static long format = -1;
   public static long binning = -1;

   //   public static long format = 8;
   //   public static long binning = 2;
   public static boolean coolerActive = true;
   private static int    triggerType = 0;

   // ROI selection
   public static boolean   isROISet = false;
   public static long      roiX = 0;
   public static long      roiY = 0;
   public static long      roiW = 0;
   public static long      roiH = 0;
   public static Rectangle selectedROI = new Rectangle(0, 0, 0, 0);

   //
   //public static PanelCam camSetFrame;
   public static FrameJCameraDisplay display = null; // Realtime display
   public static VideoPanel2         vPanel = null; // contains Streaming callback handler
                                                    //
   public static SpinnerNumberModel  model_Gain;
   public static SpinnerNumberModel  model_Offset;
   public static SpinnerExposure     spinExpos;
   private static int                activeCameraSettings = 0;
   private static boolean            displayIsOn = false;
   private static float              currentFPS;
   private static boolean            AdjustExposureForBinning = true;

   // Grabber
   public static byte[]           sampleImageByte;
   public static short[]          sampleImageShort;
   public static Condition        grabDone = new Condition(false);
   public static int              framesToWait = 2;
   private static ImageStatistics imgStats;

   // Prefs.Usr
   public static boolean mirrorImage = Prefs.usr.getBoolean("mirrorImage", false);

   //#################### an experiment...
   // Preferences
   public static String prefsKey = "camera.";

   //  public static String _MIRROR_IMAGE = prefsKey + "mirrorImage";
   //  public static boolean _MIRROR_IMAGE_DFLT = false;
   //  public static boolean mirrorImage =
   //      Prefs.usr.getBoolean(_MIRROR_IMAGE, _MIRROR_IMAGE_DFLT);
   //
   //  public static String _EXPOSURE = prefsKey + "exposure";
   //  public static int _EXPOSURE_DFLT = 1000;
   //  public static int eXposure = Prefs.usr.getInt(_EXPOSURE, _EXPOSURE_DFLT);
   // use: Prefs.usr.getInt(Camera._MIRROR_IMAGE, Camera._MIRROR_IMAGE_DFLT);
   // use: Prefs.usr.getInt(Camera._EXPOSURE, Camera._EXPOSURE_DFLT);
   // exposure = new ActiveInt(this. "exposure", 1000 [, min, max]);
   // exp = Camera.exposure.get();
   // Camera.exposure.set(exp);
   public static String    _X = prefsKey + "x";
   public static boolean   _X_DFLT = false;
   public static boolean   X = Prefs.usr.getBoolean(_X, _X_DFLT);
   public static Condition frameDone = new Condition(false);

   //----------------------------------------------------------------
   // Dispatcher
   //  private static Active_object dispatcher = new Active_object();
   //  static {
   //    dispatcher.start();
   //  }

   //----------------------------------------------------------------
   // default display scale for live video for different binning-settings
   public static float[] displayScale = { 0.5f, 0.5f, 1.0f, 1.0f };

   //----------------------------------------------------------------
   // CameraSettings
   //setActiveCameraSettings(int n);
   //   e.g.:  Prefs.usr.putInt(String.valueOf(getActiveCameraSettings())
   //             + "_" +  "coolerActive", b);

   /*
       public static void saveCameraSettings(int n) {
       // 0th set is default
       String set = String.valueOf(n) + "_";
       CamUtils.changeProp(set + "exposure", exposure);
       CamUtils.changeProp(set + "gain", gain);
       CamUtils.changeProp(set + "offset", offset);
       CamUtils.changeProp(set + "format", format);
       CamUtils.changeProp(set + "binning", binning);
       CamUtils.changeProp(set + "coolerActive", coolerActive);
     }
     public static void loadCameraSettings(int n) {
       // 0th set is default
       String set = String.valueOf(n) + "_";
       CamUtils.changeProp(set + "exposure", exposure);
       CamUtils.changeProp(set + "gain", gain);
       CamUtils.changeProp(set + "offset", offset);
       CamUtils.changeProp(set + "format", format);
       CamUtils.changeProp(set + "binning", binning);
       CamUtils.changeProp(set + "coolerActive", coolerActive);
       setActiveCameraSettings(n);
     }
     public static int getActiveCameraSettings() {
       return activeCameraSettings;
     }
     public static void setActiveCameraSettings(int n) {
       activeCameraSettings = n;
     }
    */

   //----------------------------------------------------------------
   //
   static boolean displayWasOn = false;

   //-----------------------------------------------------------
   private Camera() {
   }

   // Prefs.Sys
   public static synchronized boolean isBusy() {
      return busy;
   }

   //-----------------------------------------------------------
   //
   public static void initialize() {
      // Exposure
      //***spinExpos = new SpinnerExposure();
      // Gain
      int initGain = Prefs.usr.getInt("cameraGain", 0);
      if (initGain < 0) {
         initGain = 0;
      }
      model_Gain = new SpinnerNumberModel(initGain, 0, 4095, 100);
      // Offset
      int initOffset = Prefs.usr.getInt("cameraOffset", 1500);
      if (initOffset < 0) {
         initOffset = 0;
      }
      model_Offset = new SpinnerNumberModel(initOffset, 0, 4095, 100);
   }

   public static void setMirrorImage() {
      mirrorImage = Prefs.usr.getBoolean("mirrorImage", false);
   }

   //-----------------------------------------------------------
   // Dummy Camera
   public static boolean initializeDummyCamera() {
      simulation    = true;
      isOpen        = true;
      mode          = "stream";
      format        = 2; // 2:8-bit  3:12-bit
      speed         = 0; // 0:20M 1:10M readout
      depth         = 8;
      binning       = 2; // 2x2 binning
      exposure      = 10000;
      width         = 600;
      height        = 500;
      size          = width * height;
      return true;
   }

   //-----------------------------------------------------------
   //
   public static boolean initializeCamera() {
      System.out.println("Initializing Camera...");
      try {
         if (openCameraStream()) {
            System.out.println("Camera opened.");
            return true;
         } else {
            System.out.println("Failed to Open Camera");
            return false;
         }
      } catch (Exception ex) {
      }
      return false;
      //    } else {  // close the camera display and release the driver
      //      System.out.println("Closing Camera...");
      //      closeDisplayFrame();
      //      closeCamera();
      //      return false;
   }

   //-----------------------------------------------------------
   //
   public static String getCameraState() {
      if (simulation) {
         return "SIMULATION";
      } else {
         return String.valueOf(width) + " x " + String.valueOf(height) + " pixels  (bin " +
         String.valueOf(binning) + ")  " + String.valueOf(depth) + "-bit";
      }
   }

   ///////////////////////////////////////////////////////////////////////////
   // Drivers - Load / Unload QCam Driver
   //
   public static boolean loadCameraDriver() {
      int err = -1;
      if (!driverLoaded) {
         try {
            err = QCamJNI.loadQCamDriver();
         } catch (Exception ex) {
            System.err.println("Exception in LoadCameraDriver\n" + ex.getMessage());
         }
         if (err == 0) {
            driverLoaded = true;
            return true;
         } else {
            System.out.println("Camera Error: Unable to load driver: " + String.valueOf(err));
            return false;
         }
      }
      return true;
   }

   public static void unLoadCameraDriver() {
      if (mode != "closed") {
         QCamJNI.closeTheCamera();
         mode = "closed";
      }
      if (driverLoaded) {
         QCamJNI.unLoadQCamDriver();
      }
      driverLoaded = false;
      System.out.println("Camera driver unloaded.");
   }

   ///////////////////////////////////////////////////////////////////////////
   // Open camera in Streaming (Asynchronous) Mode and
   // set to remembered settings
   //
   public static boolean openCameraStream() throws Exception {
      if (!loadCameraDriver()) {
         return false;
      }
      boolean result = QCamJNI.openCameraStreaming();
      if (result) {
         isOpen    = true;
         mode      = "stream";
         // initialize settings
         //
         depth     = 8;
         int d = Prefs.usr.getInt("camera.depth", 8);
         d         = 8; // force to 8-bit on start
         if (d == 8) {
            format = 2; // 2:8-bit
         } else {
            format = 3; // 3:12-bit
         }
         setFormat(format);
         //
         binning = Prefs.usr.getInt("camera.binning", 2);
         setBinning((int)binning);
         //
         speed = Prefs.usr.getInt("camera.speed", 0); // default to 20MHz, but...
         if (format > 1) {
            speed = 0; // 20MHz
         } else {
            speed = 1; // 10MHz
         }
         setReadoutSpeed((int)speed);

         /** @todo clear ROI ? */

         //
         int gain = Prefs.usr.getInt("camera.gain", 0);
         int offset = Prefs.usr.getInt("camera.offset", 1500);
         int expos = Prefs.usr.getInt("camera.exposure", 100000);
         expos = (expos > 500000) ? 500000 : expos;
         if (expos < 0) {
            expos = 99000;
         }
         setExposure(expos, gain, offset);
         //
         QCamJNI.setCooler(Prefs.usr.getBoolean("camera.coolerActive", false));
         //
         updateValues(); // synchronizes CameraInterface <== QCamJNI
      } else {
         isOpen    = false;
         mode      = "closed";
         throw new Exception("Failed to open camera.");
      }
      return result;
   }

   ///////////////////////////////////////////////////////////////////////////
   // Close the camera
   public static void closeCamera() {
      closeDisplayFrame();
      if (mode != "closed") {
         // if (binning != 2) { setBinning(2); }
         QCamJNI.closeTheCamera();
      }
      mode      = "closed";
      isOpen    = false;
      //System.out.println("Camera closed.");
   }

   ///////////////////////////////////////////////////////////////////////////
   // Open Display Frame
   //
   public static void openDisplayFrame() {
      if (!isOpen) {
         return;
      }
      if (displayIsOn) {
         return;
      }
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               closeDisplayFrame();
               updateValues();
               QCamJNI.setImageArray(); // pass array to C
               updateValues();
               if (display == null) {
                  //display = new FrameCameraDisplay(
                  // (float) (displayScale[ (int) binning - 1]));
                  display = new FrameJCameraDisplay(Prefs.usr.getFloat("displayScale", 1.0f));
               }
               QCamJNI.callBackSetup(display.vPanel); // pass new object to C
               display.setVisible(true);
               setStreaming();
               QCamJNI.displayOn();
               displayIsOn = true;
               // and then, turn it on with setDisplayOn()
            }
         });
   }

   public static boolean isDisplayOn() {
      return displayIsOn;
   }

   public static void setDisplayOn() {
      if (display != null) {
         //display.requestFocus();
         display.setVisible(true);
         setStreaming();
         QCamJNI.displayOn();
         //display.vPanel.setRoiFromCamera();
         displayIsOn = true;
      }
   }

   public static void setDisplayOff() {
      if (display != null) {
         display.setVisible(false);
         setTriggerSoft();
         displayIsOn = false;
      }
   }

   // Close (stop & dispose) of the display frame
   public static void closeDisplayFrame() {
      setTriggerSoft();
      if (display != null) {
         try {
            Thread.sleep(100);
         } catch (InterruptedException ex) {
         }
         display.dispose();
         display        = null;
         displayIsOn    = false;
         QCamJNI.disposeImageArray();
         System.gc();
      }
   }

   /////////////////////////////////////////////////////////////////////////////
   // Suspend / Resume streaming to display
   public static void displaySuspend() {
      if (displayIsOn) {
         displayWasOn = true;
         SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  setTriggerSoft();
                  displayIsOn = false;
               }
            });
      } else {
         displayWasOn = false;
      }
   }

   public static void displayResume() {
      if (displayWasOn) {
         SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  QCamJNI.displayOn();
                  displayIsOn = true;
               }
            });
      }
   }

   ///////////////////////////////////////////////////////////////////////////
   // Trigger
   //
   public static void setStreaming() {
      QCamJNI.setTriggerType(1); // freerun
      triggerType    = 1;
      displayIsOn    = true;
   }

   public static void setTriggerSoft() {
      try {
         QCamJNI.setTriggerType(2); // software trigger
      } catch (Exception ex) {
      }
      triggerType    = 2;
      displayIsOn    = false;
   }

   public static int trigger() {
      int err = QCamJNI.doSoftTrigger();
      if (err != 0) {
         System.out.println("Camera Error: Error in triggerSoft: " + String.valueOf(err));
      }
      return err;
   }

   //-----------------------------------------------------------
   // Set Display Scale
   //
   //  public static void setDisplayScale(int bin, float scale) {
   //    displayScale[bin - 1] = scale;
   //  }
   //
   //  public static float getDisplayScale(int bin) {
   //    return displayScale[bin - 1];
   //  }
   //
   //================================================================
   // Set Callbacks
   //----------------------------------------------------------------
   // set the callback to call the method callBack() in this object
   public static void setCallbackToImageGetter(ImageGetter imgGetter) {
      Camera.setTriggerSoft();
      QCamJNI.callBackSetup(imgGetter);
      QCamJNI.setForAcquire();
   }

   // setCallBackToDisplay when done acquiring
   public static void setCallBackToDisplay() {
      if (Camera.display != null) {
         if (Camera.display.vPanel != null) {
            QCamJNI.callBackSetup(Camera.display.vPanel);
         }
      }
   }

   //
   //
   ///////////////////////////////////////////////////////////////////////////
   // Set Exposure, Gain, and Offset (exposure is in microseconds.)
   //
   public static int setExposure(long _exposure, long _gain, long _offset) {
      if (_exposure >= 0) {
         exposure = _exposure;
         Prefs.usr.putInt("camera.exposure", (int)exposure);
      } else {
         exposure = (long)Prefs.usr.getInt("camera.exposure", 25);
      }
      if (_gain >= 0) {
         gain = _gain;
         Prefs.usr.putInt("camera.gain", (int)gain);
      } else {
         gain = (long)Prefs.usr.getInt("camera.gain", 0);
      }
      if (_offset >= 0) {
         offset = _offset;
         Prefs.usr.putInt("camera.offset", (int)offset);
      } else {
         offset = (long)Prefs.usr.getInt("camera.offset", 1500);
      }
      int err = (int)QCamJNI.setExposure(exposure, gain, offset);
      return 0;
   }

   public static int getExposureLimitsMinMax() throws Exception {
      int err = (int)QCamJNI.getExposureMinMax();
      exposureMin    = QCamJNI.exposureMin;
      exposureMax    = QCamJNI.exposureMax;
      System.out.println("getExposureMinMax [" + err + "] " + exposureMin + ", " + exposureMax);
      return err;
   }

   public static int setExposureOnly(long _exposure) {
      if (_exposure >= 0) {
         exposure = _exposure;
         Prefs.usr.putInt("camera.exposure", (int)exposure);
      } else {
         exposure = (long)Prefs.usr.getInt("camera.exposure", 25);
      }
      int err = (int)QCamJNI.queueExposureSet(exposure, gain, offset);
      return 0;
   }

   //
   //////////////////////////////////////////////////////////////////////////
   // Binning
   //
   public static int setBinning(int _bin) {
      //      if(binning == _bin)
      //         return 0;
      closeDisplayFrame();
      //    if ( (_bin == 1) && (speed < 1)) { // change the readout speed to 10M if 1x1 binning
      //      setReadoutSpeed(1);
      //    } else {
      //      setReadoutSpeed(0);
      //    }
      int err = QCamJNI.setBinning((long)_bin);
      if (err != 0) {
         System.out.println("Camera Error: Error setting binning: " + String.valueOf(err));
      } else {
         // Adjust the exposure proportionately for new binning
         if (AdjustExposureForBinning) {
            float factor = (float)(binning * binning) / (float)(_bin * _bin);
            exposure = (long)Math.round(exposure * factor);
            try {
               if (exposure > 0) {
                  setExposure(exposure, -1, -1);
               }
            } catch (Exception ex) {
            }
         }
         binning = _bin;
         Prefs.usr.putInt("camera.binning", (int)binning);
      }
      updateValues();
      selectedROI    = new Rectangle(0, 0, 0, 0);
      isROISet       = false;
      return err;
   }

   ////////////////////////////////////////////////////////////////////
   // Readout Speed
   //
   public static int setReadoutSpeed(int speed) {
      // ReadoutSpeed: 20M = 0, 10M = 1, 5M = 2, 2M5 = 3, _last = 4
      int err = QCamJNI.setReadoutSpeed((long)speed);
      Prefs.usr.putInt("camera.speed", (int)speed);
      if (err != 0) {
         System.out.println("Camera Error: Error setting readout speed: " + String.valueOf(err));
      }
      return err;
   }

   //////////////////////////////////////////////////////////////////////////
   // Set Format (Depth)
   //
   public static int setFormat(long fmt) throws Exception {
      //closeDisplayFrame();
      // fmt = (qfmtMono8 = 2 | qfmtMono16 = 3)
      int err = (int)QCamJNI.setFormat(fmt);
      if (fmt == 2) { // 8-bit
         depth                = 8;
         QCamJNI.wideDepth    = false;
         QCamJNI.setImageArray8();
      } else if (fmt == 3) { // 16-bit
         depth                = 12;
         QCamJNI.wideDepth    = true;
         QCamJNI.setImageArray16();
      } else {
         System.err.println("Camera Error: illegal format type");
         return -1;
      }
      Prefs.usr.putInt("camera.depth", (int)depth);
      System.out.println("Camera format set [" + err + "]" + fmt);
      return err;
   }

   //////////////////////////////////////////////////////////////////////
   // ROI
   //
   public static int setROI() {
      // Set the CameraROI (for sub-frame acquisition)
      int err = -1;
      if ((roiW > 0) & (roiH > 0)) {
         closeDisplayFrame();
         if (mirrorImage) {
            roiX = width - selectedROI.x - selectedROI.width;
         } else {
            roiX = selectedROI.x;
         }
         roiY    = selectedROI.y;
         roiW    = selectedROI.width;
         roiH    = selectedROI.height;
         err     = (int)QCamJNI.setROI(roiX, roiY, roiW, roiH);
         updateValues();
         if (Globals.isDeBug()) {
            System.out.println("Camera ROI set [" + err + "] " + roiX + ", " + roiY + ", " + roiW +
               ", " + roiH);
         }
         openDisplayFrame();
         //selectedROI = new Rectangle(0, 0, (int) width, (int) height);
         display.vPanel.setRoiBox(new Rectangle(0, 0, 0, 0));
         isROISet = true;
         //setDisplayOn();
      }
      return err;
   }

   public static int setROIFull() {
      // Clear the CameraROI - reset to full frame
      int err = -1;
      selectedROI = new Rectangle(0, 0, 0, 0);
      closeDisplayFrame();
      err = (int)QCamJNI.setROIFull();
      System.out.println("Camera ROI set full [" + err + "]");
      isROISet = false;
      openDisplayFrame();
      //setDisplayOn();
      return err;
   }

   //////////////////////////////////////////////////////////////////////////
   // Set measurement ROI (rather than CameraROI) in display panel
   //
   public static void setROIRectangle(Rectangle r) {
      display.vPanel.setRoiBox(r);
   }

   public static boolean isRoiSelected() {
      return ((selectedROI.width != 0) && (selectedROI.height != 0));
   }

   ////////////////////////////////////////////////////////////////////////
   public static void setCooler(boolean b) {
      QCamJNI.setCooler(b);
      Prefs.usr.putBoolean("camera.coolerActive", b);
      setDisplayOn();
   }

   ////////////////////////////////////////////////////////////////////////
   // Update the values in CameraInterface to reflect QCamJNI
   //
   public static void updateValues() {
      if (!driverLoaded) {
         return;
      }
      QCamJNI.getInfo();
      QCamJNI.getParameters();
      size           = QCamJNI.imageSize;
      width          = QCamJNI.imageWidth;
      height         = QCamJNI.imageHeight;
      // Camera Settings
      speed          = QCamJNI.readOutSpeed;
      if (QCamJNI.exposure >= 0) {
         exposure = QCamJNI.exposure;
      }
      if (QCamJNI.gain >= 0) {
         gain = QCamJNI.gain;
      }
      if (QCamJNI.offset >= 0) {
         offset = QCamJNI.offset;
      }
      exposureMin    = QCamJNI.exposureMin;
      exposureMax    = QCamJNI.exposureMax;
      //
      format         = QCamJNI.imageFormat;
      //depth = QCamJNI.depth;
      //
      binning        = QCamJNI.binning;
      roiX           = QCamJNI.roiX;
      roiY           = QCamJNI.roiY;
      roiW           = QCamJNI.roiWidth;
      roiH           = QCamJNI.roiHeight;

      //coolerActive = QCamJNI.coolerActive;
      // Camera state - from getParameters()
      //    long QCamJNI... triggerType, ,
      //    boolean wideDepth = false;
      //    // Camera Information - from getInfo()
      //    long cameraType, serialNumber, hardwareVersion, firmwareVersion,
      //      ccd, bitDepth /* max */ , cooled, imageWidth, imageHeight, imageSize,
      //      ccdType, ccdWidth, ccdHeight, firmwareBuild, uniqueId;

      // create arrays for measurements
      if (format == 2) {
         if ((sampleImageByte == null) || (sampleImageByte.length != (width * height))) {
            sampleImageByte     = new byte[(int)(width * height)];
            sampleImageShort    = null;
         }
      }
      //      else {
      //         if ((sampleImageShort == null) || (sampleImageShort.length != (width * height))) {
      //            sampleImageShort = new short[(int) (width * height)];
      //            sampleImageByte = null;
      //         }
      //      }
   }

   //-----------------------------------------------------------
   // FPS
   //
   public static void setCurrentFPS(float framesPerSec) {
      currentFPS = framesPerSec;
   }

   //********************************************************************************************
   //********************************************************************************************   

//----------------------------------------------------------------
   // Image analysis/stats
   //
   public static void testGrabSampleFrame() {
      for (int i = 0; i < 100; i++) {
         JifUtils.waitFor(500);
         Camera.grabSampleFrame();
         System.out.println(Camera.getSampleAverageROI());
      }
   }

   public static synchronized void grabSampleFrame() {
      if ((display == null) || !isDisplayOn()) {
         //DialogBoxes.boxError("Cannot sample camera image", "Please turn display on.");
         return;
      } else {
         // wait for one frame based on FPS
         grabDone.set_false(); //display.vPanel.setGrabSample(true);
         long maxWait = (2 * (exposure / 1000)) + 10000;

         //System.out.println("maxWait: " + maxWait);
         try {
            grabDone.wait_for_true(maxWait);
         } catch (InterruptedException ex) {
            System.err.println("InterruptedException in grabSampleFrame: " + ex.getMessage());
         } catch (Semaphore.Timed_out ex) {
            System.err.println("TimedOut in grabSampleFrame");
         }
         computeSample();
      }
   }

   public static void computeSample() {
      if (format == 2) {
         imgStats = ImageAnalyzer.getStats(getSampleFrameByte(), selectedROI,
               new Dimension((int)width, (int)height));
      } else {
         imgStats = ImageAnalyzer.getStats(getSampleFrameShort(), selectedROI,
               new Dimension((int)width, (int)height), (short)4095);
      }
   }

   //----------------------------------------------------------------
   // setFrameToWaitOnGrabSample
   //
   public static synchronized void setFramesToWaitOnGrabSample(int n) {
      framesToWait = n;
   }

   public static byte[] getSampleFrameByte() {
      return sampleImageByte;
   }

   public static short[] getSampleFrameShort() {
      return sampleImageShort;
   }

   public static float getSampleAverage() {
      return imgStats.mean;
   }

   public static float getSampleMax() {
      return imgStats.max;
   }

   public static float getSampleMin() {
      return imgStats.min;
   }

   public static float getSampleAverageROI() {
      return imgStats.meanInROI;
   }

   public static float getSampleMaxROI() {
      return imgStats.maxInROI;
   }

   public static float getSampleMinROI() {
      return imgStats.minInROI;
   }

   ////////////////////////////////////////////////////////////////////////
   // Synchronous functions
   //
   // Open camera in Synchronous Mode (not used much, if at all)
   public static int openCameraSynch() throws Exception {
      //int err = QCamJNI.openCameraSynch(3000);
      System.out.println("Camera openned in synchronous mode [" + err + "]");
      if (err == 0) {
         isOpen    = true;
         mode      = "synch";
      }
      if (err != 0) {
         throw new Exception("Failed to open camera.");
      }
      return err;
   }

   /*
      public static int snapImage() throws Exception {
      if (mode != "synch") {
         System.out.println("Cannot Snap image - Not in synchronous mode.");
         return -1;
      }
      long time0 = System.currentTimeMillis();
      // Snap the image...
      updateValues();
      QCamJNI.setImageArrays();
      int  err = (int) QCamJNI.snapImage(QCamJNI.capturePixels8_0);
      long time1 = System.currentTimeMillis();
      long dTime = time1 - time0;
      System.out.println("snapImage [" + err + "]" + size);
      System.out.println("dTime: " + dTime);
      // showImage();
      return err;
       }
    */

   //////////////////////////////////////////////////////////////////////////

   /*  public static void showImage () {
    int w = (int)QCamJNI.width;
    int h = (int)QCamJNI.height;
    BufferedImage bi = createFImage(w, h, (int)8, frameData);
    displayImage(bi, w, h);
    }
    public static void captureImage() {
    QCamJNI.queueFrameCapture(0);
    QCamJNI.doSoftTrigger();
    // wait...
    for (int i = 0; i < 30000000; i++) {}
    QCamJNI.getImageSize();
    DisplayImageFrame dFrame;
    dFrame = new DisplayImageFrame(QCamJNI.capturePixels8_0,
    String.valueOf(QCamJNI.time0));
    dFrame.setVisible(true);
    }
    public static void showCapturedImage () {
    int w = (int)QCamJNI.width;
    int h = (int)QCamJNI.height;
    BufferedImage bi = createFImage(w, h, (int)8, QCamJNI.capturePixels8_0);
    displayImage(bi, w, h);
    }
    // ====================================================================
    public static BufferedImage createFImage (int imageWidth, int imageHeight,
    int imageDepth, byte data[]) {
    ComponentColorModel ccm = new ComponentColorModel(
    ColorSpace.getInstance(ColorSpace.CS_GRAY),
    new int[] {imageDepth}, false, false, Transparency.OPAQUE,
    DataBuffer.TYPE_BYTE);
    ComponentSampleModel csm = new ComponentSampleModel(
    DataBuffer.TYPE_BYTE, imageWidth, imageHeight, 1,
    imageWidth, new int[] {0});
    DataBuffer dataBuf = new DataBufferByte((byte[])data, imageWidth);
    WritableRaster wr =
    Raster.createWritableRaster(csm, dataBuf, new Point(0, 0));
    Hashtable ht = new Hashtable();
    ht.put("owner", "rudolfo@mbl.edu");
    return  new BufferedImage(ccm, wr, true, ht);
    }
    //====================================================================
    public static void displayImage (BufferedImage img, int wid, int ht) {
    JFrame fr = new JFrame();
    ImagePanel pan = new ImagePanel(img);
    pan.setSize(wid, ht);
    fr.getContentPane().add(pan);
    fr.pack();
    fr.setSize(wid, ht);
    fr.setVisible(true);
    }
    //====================================================================
    static class ImagePanel extends JComponent {
    protected BufferedImage image;
    public ImagePanel () {
    }
    public ImagePanel (BufferedImage img) {
    image = img;
    }
    public void setImage (BufferedImage img) {
    image = img;
    }
    public void paintComponent (Graphics g) {
    Rectangle rect = this.getBounds();
    if (image != null) {
    g.drawImage(image, 0, 0, rect.width, rect.height, this);
    }
    }
    }
    */

   //////////////////////////////////////////////////////////////////////////
   // Test of Camera Timing
   //
   public static void testCameraTiming() {
      long   exposeTime = 0;
      long   LC_SettleTime = 0;
      long   delay = 0;
      long[] startTime = new long[5];
      long[] timeDone = new long[5];
      long   time0 = 0;
      long   time1 = 0;

      // how often to check if done
      long checkDoneTime = 1;
      QCamJNI.frameDone = true;
      setTriggerSoft();
      time0                = System.currentTimeMillis();
      QCamJNI.frameDone    = false;
      //QCamJNI.queueFrameCapture(0);
      QCamJNI.doSoftTrigger();
      while (!QCamJNI.frameDone) {
         try {
            Thread.currentThread().sleep(checkDoneTime);
         } catch (InterruptedException e) {
         }
      }
      time1 = System.currentTimeMillis();
      System.out.println("delay on first frame: " + (time1 - time0));
      time0                = System.currentTimeMillis();
      QCamJNI.frameDone    = false;
      //QCamJNI.queueFrameCapture(0);
      QCamJNI.doSoftTrigger();
      while (!QCamJNI.frameDone) {
         try {
            Thread.currentThread().sleep(checkDoneTime);
         } catch (InterruptedException e) {
         }
      }
      time1 = System.currentTimeMillis();
      System.out.println("delay on second frame: " + (time1 - time0));
   }

   public static String dumpParms() {
      String out =
         "CameraInterface Settings\n\n" + "driverLoaded: " + driverLoaded + "\n" + "isOpen: " +
         isOpen + "\n" + "mode: " + mode + "\n" + "width: " + width + "\n" + "height: " + height +
         "\n" + "size: " + size + "\n" + "depth: " + depth + "\n" + "speed: " + speed + "\n" +
         "exposure: " + exposure + "\n" + "gain: " + gain + "\n" + "offset: " + offset + "\n" +
         "exposureMin: " + exposureMin + "\n" + "exposureMax: " + exposureMax + "\n" + "format: " +
         format + "\n" + "binning: " + binning + "\n" + "coolerActive: " + coolerActive + "\n" +
         "isROISet: " + isROISet + "\n" + "roi: " + roiX + ", " + roiY + ", " + roiW + ", " + roiH +
         "\n" + "cameraSelROI: " + selectedROI.getX() + ", " + selectedROI.getY() + ", " +
         selectedROI.getWidth() + ", " + selectedROI.getHeight() + "\n";
      return out;
   }
}
