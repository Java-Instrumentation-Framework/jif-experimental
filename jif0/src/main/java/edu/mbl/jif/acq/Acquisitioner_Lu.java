package edu.mbl.jif.acq;

import edu.mbl.jif.camera.*;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.utils.time.NanoTimer;

import java.text.DecimalFormat;


/**
 * <p>Acquisitioner</p>
 * <p>Description: Acquisitioner for LuCam </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author gharris at mbl.edu
 * @version 1.0
 */
public class Acquisitioner_Lu implements ImageAcquisitioner {
    
   // Acquisitioner for LuCam
   // Uses ImageGetter_Lu
    
   static final DecimalFormat format = new DecimalFormat();

   static {
      format.setMinimumFractionDigits(3);
      format.setMaximumFractionDigits(3);
   }
   
   CameraInterface cam;
   ImageGetter_Lu   iGet;
   NanoTimer       nt = new NanoTimer();
   int             acqDepth = 8; // default to 8-bit
   boolean         mirrorImage = false;
   int             multiFrame = 1;
   boolean         div = true;
   private boolean wasStreaming = false;
   boolean         started = false;
   //------------------------------------------------
   // create Acquisitioner with Camera, cam, attached
   public Acquisitioner_Lu(CameraInterface cam) {
      this.cam = cam;
   }

   public Acquisitioner_Lu(CameraInterface cam, int acqDepth, int frames, boolean divide) {
      this(cam, acqDepth, frames, divide, false);
   }

   public Acquisitioner_Lu(CameraInterface cam, int acqDepth, int frames, boolean divide,
      boolean mirror) {
      this.cam = cam;
      setAcqDepth(acqDepth);
      setMultiFrame(frames, divide);
      setMirrorImage(mirror);
   }

   
   public void setAcqDepth(int acqDepth) {
      this.acqDepth = acqDepth;
      if (cam.getDepth() != acqDepth) {
         System.err.println("cam.getDepth() != acqDepth");
         //cam.setDepth(acqDepth);
      }
   }

   public void setMultiFrame(int frames, boolean divide) {
      multiFrame    = frames;
      div           = divide;
   }
   
    public void setMirrorImage(boolean t) {
      this.mirrorImage = t;
   }

   public boolean isMirrorImage() {
      return this.mirrorImage;
   }


   
   // Start -----------------------------------------------------------------
   public void start(boolean flushFirst) {
      start();
   }

   public void start() {
      // @todo Check that not streaming...
      if(wasStreaming = ((StreamGenerator)cam).isStreaming()) 
         ((StreamGenerator)cam).stopStream();     
      iGet = new ImageGetter_Lu(acqDepth, (int)cam.getWidth(), (int)cam.getHeight());
      iGet.setMultiFrame(multiFrame, div);
      iGet.setMirrorImage(mirrorImage);
      cam.setDepth(this.getDepth());
      cam.enableFastAcq(this.getDepth());
      started = true;
   }

   public void flushFirstFrame() {
      flushFirstFrame(iGet);
   }

   void flushFirstFrame(ImageGetter_Lu _iGet) {
      if (!started) {
         System.err.println("Acquisitoiner not started before flushFirstFrame()");
         return;
      }
      _iGet.setMultiFrame(1, false);
      _iGet.resetFrameDone();
      if (this.acqDepth == 8) {
         cam.acqFast8();
      }
      if (this.acqDepth == 16) {
         cam.acqFast16();
      }
      cam.acqFast8();
      while (!_iGet.isFrameDone()) {
         try { // wait for FrameDone
            Thread.sleep(5);
         } catch (InterruptedException e) {
         }
      }
   }

   // Acquire Image...
   public long acquireImage(Object imageArray) {
      if (!started) {
         System.err.println("Acquisitioner not started before acquireImage()");
         return -1;
      }
      nt.reset();
      nt.start();
      long t = System.nanoTime();
      // Take each exposure...
      if (acquireFrames(iGet)) {
         // and wait for ProcessDone...
         while (!iGet.isProcessDone()) {
            try {
               Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
            }
         }
         try {
            iGet.putImageInto(imageArray); // put it into the destination
            return t;
         } catch (Exception ex) {
         }
      }
      return nt.elapsedNanos();
   }

   // acquireFrames --------------------------------------------------------
   private synchronized boolean acquireFrames(ImageGetter_Lu iGet) {
      if (!started) {
         System.err.println("Acquisitoiner not started before acquireFrames()");
         return false;
      }
      int frames = iGet.getNumFrames();
      for (int i = 0; i < frames; i++) {
         //         iGet.resetFrameDone();
         if (this.acqDepth == 8) {
            iGet.processFrame(cam.acqFast8());
         }
         if (this.acqDepth == 16) {
            iGet.processFrame(cam.acqFast16());
         }
         //         while (!iGet.isFrameDone()) { // wait for FrameDone...
         //            try {
         //               Thread.currentThread().sleep(1);
         //            } catch (InterruptedException e) {
         //            }
         //         }
      }
      return true;
   }

   //--------------------------------------------------------------------
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
      System.gc();
      cam.disableFastAcq();
      cam.setDepth(8);
      if(wasStreaming) 
         ((StreamGenerator)cam).startStream();
   }

   //   public String getAcqTime() {
   //      return acqTime;
   //   }
   public void setDepth(int depth) {
      this.acqDepth = depth;
   }

   public int getDepth() {
      return this.acqDepth;
   }

  
   public boolean isDivideResult() {
      return this.div;
   }

}
