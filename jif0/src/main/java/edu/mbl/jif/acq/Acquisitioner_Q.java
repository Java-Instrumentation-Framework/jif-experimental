package edu.mbl.jif.acq;

import edu.mbl.jif.camera.CameraInterface;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camera.ImageGetter;
import edu.mbl.jif.camera.QCamJNI;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import edu.mbl.jif.utils.time.NanoTimer;

import java.text.DecimalFormat;


/**
 * <p>Acquisitioner</p>
 * <p>Description: For QCam </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author gharris at mbl.edu
 * @version 1.0
 */
public class Acquisitioner_Q implements ImageAcquisitioner {
    
   static final DecimalFormat format = new DecimalFormat();
   static {
      format.setMinimumFractionDigits(3);
      format.setMaximumFractionDigits(3);
   }

   CameraInterface cam;
   ImageGetter iGet;
   NanoTimer nt = new NanoTimer();
   int acqDepth = 8; // default to 8-bit
   boolean mirrorImage = false;
   int multiFrame = 1;
   boolean div = true;
   private boolean wasStreaming = false;
   private boolean doRestoreDepth = false;
   boolean started = false;
   

   public Acquisitioner_Q(CameraInterface cam) {
      this.cam = cam;
   }

  @Override
   public void setMultiFrame(int frames, boolean divide) {
      this.multiFrame = frames;
      this.div = divide;
   }

  @Override
   public void setDepth(int depth) {
      acqDepth = depth;
   }

  @Override
   public void setMirrorImage(boolean t) {
      mirrorImage = t;
   }

  @Override
   public void start(boolean flushFirst) {
      start();
      flushFirstFrame();
      iGet.setMultiFrame(multiFrame, div);
   }


  @Override
   public void start() {
      // @todo Check that not streaming...
      if (wasStreaming = ((StreamGenerator)cam).isStreaming()) {
         System.out.println("Was streaming, stopping.");
         ((StreamGenerator)cam).stopStream();
      }
      iGet = new ImageGetter(acqDepth, (int)cam.getWidth(), (int)cam.getHeight());
      iGet.setMultiFrame(multiFrame, div);
      iGet.setMirrorImage(mirrorImage);
      if (cam.getDepth() != acqDepth) {
         cam.setDepth(acqDepth);
         doRestoreDepth = true;
      }
      setCallbackToImageGetter(iGet);
      //cam.enableFastAcq(this.getDepth());
      started = true;
   }


   // flushFirstFrame -------------------------------------------------------
   public void flushFirstFrame() {
      flushFirstFrame(iGet);
   }


   void flushFirstFrame(ImageGetter _iGet) {
      _iGet.setMultiFrame(1, false);
      _iGet.resetFrameDone();
      if (QCamJNI.doSoftTrigger() == 0) {
         while (!_iGet.isFrameDone()) {
            try { // wait for FrameDone
               Thread.sleep(5);
            } catch (InterruptedException e) {
            }
         }
      }
   }


  @Override
   public long acquireImage(Object imageArray) {
      if (!started) {
         System.err.println("Acquisitioner not started before acquireImage()");
         return -1;
      }
      nt.reset();
      nt.start();
      if (acquireFrames(iGet)) {
         // and wait for ProcessDone...
         while (!iGet.isProcessDone()) {
            try {
               Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
            }
         }

         // put it into the destination
         try {
            iGet.putImageInto(imageArray);
         } catch (Exception ex) {
         }
      }
      return nt.elapsedNanos();
   }


   private synchronized boolean acquireFrames(ImageGetter iGet) {
      int frames = iGet.getNumFrames();
      for (int i = 0; i < frames; i++) {
         iGet.resetFrameDone();
         if (QCamJNI.doSoftTrigger() == 0) {
            while (!iGet.isFrameDone()) { // wait for FrameDone...
               try {
                  Thread.currentThread().sleep(1);
               } catch (InterruptedException e) {
               }
            }
         } else {
            return false;
         }
      }
      return true;
   }


   //--------------------------------------------------------------------
  @Override
   public void finish() {
      if (!started) {
         System.err.println("Acquisitioner not started before finish()");
         return;
      }
      started = false;
      if (iGet != null) {
         iGet.nullOutArrays();
         iGet = null;
      }
      System.gc();  // ??
      if (doRestoreDepth) {
         cam.setDepth(8);
      }
      setCallBackToDisplay(wasStreaming);
      //cam.disableFastAcq();
   }


   //================================================================
   // Set Callbacks
   //----------------------------------------------------------------
   // set the callback to call the method callBack() in this object
   public static void setCallbackToImageGetter(ImageGetter imgGetter) {
      //Camera.setTriggerSoft();
      QCamJNI.setTriggerType(2); // !! redundant
      // @todo If image type and dimensions have not changed, these are redundant {...
      QCamJNI.setImageArray();
      QCamJNI.callBackSetup(imgGetter);
      //...}
      QCamJNI.setForAcquire();
   }


   // setCallBackToDisplay when done acquiring
   public static void setCallBackToDisplay(boolean restart) {
      DisplayLiveCamera display =
         ((InstrumentController)CamAcqJ.getInstance().getController()).getDisplayLive();
      if (display != null) {
         StreamSource source = display.getStreamSource();
         if (source != null) {
            QCamJNI.callBackSetup(source);
            if (restart) {
               display.resume();
            }
         }
      }
   }

//
//   public static int trigger() {
//      int err = QCamJNI.doSoftTrigger();
//      if (err != 0) {
//         System.out.println("Camera Error: Error in triggerSoft: " + String.valueOf(err));
//      }
//      return err;
//   }


}
