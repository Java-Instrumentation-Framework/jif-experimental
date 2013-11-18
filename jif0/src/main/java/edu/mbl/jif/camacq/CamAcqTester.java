package edu.mbl.jif.camacq;

import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.utils.diag.edt.ThreadCheckingRepaintManager;
import edu.mbl.jif.utils.prefs.Prefs;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import java.awt.Button;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;




//import javax.swing.ProgressMonitor;

/**
 * CamAcq Tests
 */
public class CamAcqTester {
   JPanel                  buttons = new JPanel();
   public static CamAcq    camAcq = null;
   
    // <editor-fold defaultstate="collapsed" desc="<<< Setup buttons >>>">
   public CamAcqTester() {
      FrameForTest f = new FrameForTest();
      buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

      

      addButton("CamAcq Init",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               SwingUtilities.invokeLater(new Runnable() {
                     public void run() {
                        camAcq = CamAcq.getInstance();
                     }
                  });
            }
         });
      addButton("CaptureToImagePlus",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               testCaptureToImagePlus();
            }
         });
      addButton("CaptureToImagePlusMultiple",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               testCaptureToImagePlusMultiple();
            }
         });
      addButton("CaptureShort",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               simpleCaptureToImageShort();
            }
         });
      addButton("CaptureByte",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               simpleCaptureToImageByte();
            }
         });
      addButton("SeriesToStack",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               captureSeriesToStack();
            }
         });

      addButton("TestMethods",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               testCamAcq("test");
            }
         });
      addButton("ImageJ",
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // TODO edu.mbl.jif.imagej.IJMaker.openImageJ();
            }
         });

      // debugging helpers...
      // org.pf.joi.Inspector.inspectWait(new Object());
      //camAcq.setDeBug(true);
      //CamAcq.getInstance().listAllParameters();
      //testCapStack();
      //testCamAcq("");
      //calibrateTest();

      //--------
      f.add(buttons);
      f.setLocation(50, 50);
      f.pack();
      f.setVisible(true);
      //
      System.out.println("classpath: " + System.getProperty("java.class.path"));
      RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));
   }

   private void addButton(String label, ActionListener action) {
      Button b = new Button(label);
      if (action != null) {
         b.addActionListener(action);
      }
      buttons.add(b);
   }
    // </editor-fold>

   public static void main(String[] args) {
       if (!SwingUtilities.isEventDispatchThread()) {
           SwingUtilities.invokeLater(new Runnable() {

               public void run() {
                   new CamAcqTester();
               }
           });
       }
   }


   //---------------------------------------------------------------------------
   // Tests...

   //  acquires single image, saves, displays, etc, using defaults
   // Put in IJPlugins   DONE
   public static void testCaptureToImagePlus() {
      final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               camAcq.setDepth(8);
               camAcq.captureToImagePlus();
               camAcq.displayResume();
            }
         });
   }


   //-------------------------------
   public static void testCaptureToImagePlusMultiple() {
      final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
      for (int i = 0; i < 5; i++) {
         camAcq.captureToImagePlus();
      }
      camAcq.displayResume();
   }


   //---------------------------------------------------------------------------
   // Acquire 8-bit Image
   // acquires single image - into an array byte[]
   // *** IJPlugins DONE
   public static void simpleCaptureToImageByte() {
      final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
      CamAcqJ.getInstance().statusBarMessage("Acquiring image...");
      camAcq.setExposureAcq(20.0f);
      camAcq.setDepth(8);
      camAcq.setMultiFrame(3);
      ImagePlus iPlus = NewImage.createByteImage("-", camAcq.getWidth(),
            camAcq.getHeight(), 1, 0);

      byte[]    pixelsByte = (byte[])iPlus.getProcessor().getPixels();
      pixelsByte[0] = 0;
      camAcq.captureSingleImage(pixelsByte);
      iPlus.setTitle(String.valueOf(System.currentTimeMillis()));
      camAcq.setScale(iPlus);
      iPlus.show();
      camAcq.displayResume();
   }

   //---------------------------------------------------------------------------
   // Acquire 12-bit Image
   //  acquires single image - into an array short[]
   // *** IJPlugins DONE
   final short[] pixels = null;

   public static void simpleCaptureToImageShort() {
      final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               CamAcqJ.getInstance().statusBarMessage("Acquiring image...");
               ImagePlus iPlus = NewImage.createShortImage("-",
                     camAcq.getWidth(), camAcq.getHeight(), 1, 0);
               iPlus.getProcessor().getCurrentColorModel();
               iPlus.getProcessor().invertLut();
               short[] pixels = (short[])iPlus.getProcessor().getPixels();
               pixels[0] = 0;
               camAcq.setExposureAcq(20);
               camAcq.setDepth(16);
               camAcq.setMultiFrame(1);
               camAcq.captureSingleImage(pixels);
               //pixels  = (short[]) camAcq.captureArray();
               iPlus.getProcessor().setPixels(pixels);
               iPlus.setTitle(String.valueOf(System.currentTimeMillis()));
               camAcq.setScale(iPlus);
               iPlus.draw();
               iPlus.show();
               camAcq.displayResume();
            }
         });

      //    org.pf.joi.Inspector.inspect(pixels);
   }


   //---------------------------------------------------------------------------
   //
   // *** IJPlugins DONE
   public static void testCapStack() {
             final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
      int       nSlices = 5;
      ImagePlus imgPlus = camAcq.newCaptureStackByte();
      // camAcq.newCaptureStack(8, nSlices); // pre-allocate slices
      camAcq.setImagePlusForCapture(imgPlus); // sets default stack for capture
      camAcq.startAcq();
      for (int i = 0; i < nSlices; i++) {
         // change LC...
         camAcq.captureImageToStack();
      }
      camAcq.captureStackFinish();
      camAcq.finishAcq();
      camAcq.displayResume();
   }


   //---------------------------------------------------------------------------
   // CaptureImageStack
   public static void testCaptureImageStack() {
       final CamAcq camAcq = CamAcq.getInstance();
       if (camAcq == null) {
           return;
       }
       
       // put acquired image(s) in currently selected ImagePlus
       ImagePlus imp = IJ.getImage();
       if (imp == null) {
           return;
       }
       if (imp.getStackSize() == 1) {
           IJ.showMessage("This plugin requires a stack");
           return;
       }
       int       nSlices = 5;
       ImagePlus imgPlus = camAcq.newCaptureStackByte();
       //ImagePlus imgPlus = newCaptureStack(8, nSlices); // pre-allocates slices
       camAcq.setImagePlusForCapture(imgPlus);
       camAcq.startAcq();
       int slice = 3;
       camAcq.captureImageToStack();
       camAcq.captureStackFinish();
       camAcq.displayResume();
   }


   //---------------------------------------------------------------------------
   // CaptureImageSlice
   // Replace the third slice in the current stack with acquired image
   public static void testCaptureImageSlice() {
             final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }

      // put acquired image(s) in currently selected ImagePlus
      ImagePlus iPlus = IJ.getImage();
      if (iPlus == null) {
         return;
      }
      if (iPlus.getStackSize() < 3) {
         IJ.showMessage("Stack not large enough.");
         return;
      }
      camAcq.setImagePlusForCapture(iPlus);
      camAcq.startAcq();
      int slice = 3;

      //camAcq.captureImageToStack(slice);
      ij.io.FileSaver fs = new ij.io.FileSaver(iPlus);
      fs.saveAsTiff("Stack00.tiff");
      camAcq.finishAcq();
      camAcq.displayResume();
   }


   //---------------------------------------------------------------------
   // captureSequenceToStack
   // Using the current settings, capture a series into an ImageJ Stack.
      public static void captureSeriesToStack() {
             final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
             camAcq.captureSequenceToStack();
      }
      
   public static void captureSeriesToStack_NOT() {
             final CamAcq camAcq = CamAcq.getInstance();
      if (camAcq == null) {
         return;
      }
             camAcq.captureSequenceToStack();
      ImagePlus  iPlus;
      ImageStack stack;
      if (CamAcqJ.getInstance().isDebug()) {
         //startTimer("start captureSequenceToStack");
         System.out.println("Sys: " + System.currentTimeMillis());
      }
      int imagesInStack = 10;
      int interval = 500; //msec.
      if (camAcq.getDepth() == camAcq.DEPTH_SHORT) {
         iPlus = camAcq.newCaptureStackShort(imagesInStack);
      } else {
         iPlus = camAcq.newCaptureStackByte(imagesInStack);
      }
      if (iPlus == null) {
         IJ.error("imp == null");
         return;
      }
      stack = iPlus.getStack();
      if (stack.getSize() == 0) {
         IJ.error("stack size = 0");
         return;
      }

      // set filename
      String stackName = camAcq.makeFilename();
      iPlus.setStack(stackName, stack);

      camAcq.startAcq();
      ProgressMonitor pm = new ProgressMonitor(null,
            "Series Acquisition Running", "Acquireing Series...", 0,
            imagesInStack);
      CamAcqJ.getInstance().statusBarMessage("Acquiring series...");
      // +++ flush first frame here...
      int i = 0;
      while (i < imagesInStack) {
         long startTime = System.currentTimeMillis();
         camAcq.captureImageToStack(stack, i);

         //stack.setSliceLabel(camAcq.getAcqTime(), i + 1);
         //         if (Prefs.usr.getBoolean("parmsInFilename", true)) {
         //            camAcq.addTimemark(stack.getProcessor(i + 1));
         //         }
         CamAcqJ.getInstance().statusBarMessage("");
         pm.setProgress(i + 1);
         pm.setNote("Acquired " + (i + 1) + " of " + imagesInStack);
         long nextTime = startTime + (long)(interval * 1000);
         i++;
         // Wait until Interval passes
         long toWait = nextTime - System.currentTimeMillis();

         //CamUtils.waitFor((int)toWait);
         //         int q = 0;
         //         while (toWait > 0) {
         //            toWait = nextTime - System.currentTimeMillis();
         //            if ((toWait > 500) && (q > 200)) {
         //               pm.setNote("Acquired " + (i + 1) + " of " + imagesInStack
         //                     + ", next in " + (toWait / 1000) + " sec.");
         //               q = 0;
         //            }
         //            if (i < imagesInStack) {
         //               CamUtils.waitFor(5);
         //            }
         //            if (pm.isCanceled()) {
         //               break;
         //            }
         //            q++;
         //         }
         //
         //         if (pm.isCanceled()) {
         //            break;
         //         }
      }
      camAcq.wait(100);
      camAcq.finishAcq();
      camAcq.setScale(iPlus);
      if (Prefs.usr.getBoolean("parmsInFilename", true)) {
         camAcq.addParms(iPlus.getStack().getProcessor(1),
            stackName);
         iPlus.updateImage();
      }

      if (Prefs.usr.getBoolean("putOnDesk", true)) {
         iPlus.show(); // display image in ImageJ window
         iPlus.updateAndRepaintWindow();
      }

      //      if (pm.isCanceled()) { // was cancelled
      //         IJ.error("Series Capture Stopped.  Data not saved to file.");
      //      } else { // otherwise, save...
      if (Prefs.usr.getBoolean("autoSave", true)) {
         ij.io.FileSaver fs = new ij.io.FileSaver(iPlus);
         if (iPlus.getStackSize() == 1) {
            fs.saveAsTiff(camAcq.getImageDirectory() + "\\" + stackName
               + ".tiff");
         } else { 
            fs.saveAsTiffStack(camAcq.getImageDirectory() + "\\" + stackName
               + ".tiff");
         }
      } else {
         iPlus.changes = true;
      }

      //}
      if (!Prefs.usr.getBoolean("putOnDesk", true)) {
         iPlus.flush();
         iPlus = null;
      }
      IJ.showMessage("Series Captured to Stack: " + stackName + ", "
         + imagesInStack + " images, " + camAcq.getMultiFrame()
         + "  frameAvg");
   }

   //---------------------------------------------------------------------------
   //  acquires single image - custom
   /*
         public static void customCaptureToImage () {
         // Initialize the ImageJ ImagePlus stack & allocate image memory
         byte[] pixelsByte = null;
         short[] pixelsShort = null;
         //CameraInterface.display.vPanel.showMessage("Acquiring image...");
         CamCamAcqJ.getInstance().statusBarMessage("Acquiring image...");
         try {
            if (Prefs.usr.getInt("resultAvg", 8) == 8) { // result = byte []
               iPlus = NewImage.createByteImage("Title", (int) Camera.width,
                     (int) Camera.height, 1, 0);
            } else { // result = short[]
               iPlus = NewImage.createShortImage("Title", (int) Camera.width,
                     (int) Camera.height, 1, 0);
            }
         }
         catch (OutOfMemoryError e) {
            error("OutOfMemoryError in captureToImage");
            return;
         }
         iGet = new ImageGetter();
         Camera.setCallbackToImageGetter(iGet);
         // Acquire the images, putting them into the stack
         if (Prefs.usr.getInt("resultAvg", 8) == 8) {
            pixelsByte = (byte[]) iPlus.getProcessor().getPixels();
            pixelsByte[0] = 0;
            acquireImageNew(pixelsByte);
         } else {
            pixelsShort = (short[]) iPlus.getProcessor().getPixels();
            pixelsShort[0] = 0;
            acquireImageNew(pixelsShort);
         }
         wait(25);
         setBackToDisplay();
         String name = makeFilename();
         iPlus.setTitle(name);
         setScale(iPlus);
         if (Prefs.usr.getBoolean("parmsInFilename", true)) {
            addParms(iPlus.getProcessor());
            iPlus.updateImage();
         }
         if (Prefs.usr.getBoolean("putOnDesk", true)) {
            iPlus.show();
         }
         if (Prefs.usr.getBoolean("autoSave", true)) {
            ij.io.FileSaver fs = new ij.io.FileSaver(iPlus);
            fs.saveAsTiff(getImageDirectory() + "\\" + name + ".tiff");
         } else {
            iPlus.changes = true;
         }
         if (!Prefs.usr.getBoolean("putOnDesk", true)) {
            iPlus.flush();
            iPlus = null;
         }
         iGet = null;
         CamAcqJ.getInstance().statusBarMessage("Image Captured: " + name + ", " + getMultiFrame()
                         + "  frameAvg");
      }
    */
   public static void testCamAcq(String s) {
      // Test
      CamAcq camAcq = CamAcq.getInstance();
      //-------------------------------------------------------
      camAcq.setMirrorImage(true);
      System.out.println("isMirrorImage: " + camAcq.isMirrorImage());
      //-----------------------------------------------------------
      // Frame Averaging
      camAcq.setMultiFrame(4);
      System.out.println("FramesToAverage: " + camAcq.getMultiFrame());
      //-----------------------------------------------------------
      // Series Acq Parms
      camAcq.setImagesInSequence(5);
      //-----------------------------------------------------------
      // Image Parameters
      System.out.println("ImageWidth: " + camAcq.getWidth());
      System.out.println("ImageHeight: " + camAcq.getHeight());
      // ----------------------------------------------------------------
      System.out.println("Exposure: " + camAcq.getExposure());
      camAcq.setExposure(2000);
      System.out.println("ExposureMin: " + camAcq.getExposureMin());
      System.out.println("ExposureMax: " + camAcq.getExposureMax());
      camAcq.setGain(1.5f);
      System.out.println("Gain: " + camAcq.getGain());
      //      camAcq.setOffset(1500);
      //      System.out.println("Offset: " + camAcq.getOffset());
      camAcq.setDepth(8);
      System.out.println("Depth: " + camAcq.getDepth());
      //      camAcq.setBinning(3);
      //      System.out.println("Binning: " + camAcq.getBinning());
      //camAcq.getSpeed();
      //camAcq.setSpeed();
      // ROI -----------------------------------------------------------
      System.out.println("Selected ROI: " + camAcq.getSelectedROI());
      //      Rectangle roi = new Rectangle(100, 100, 400, 400);
      //      camAcq.setCameraROI(roi);
      //      camAcq.clearCameraROI();
      //      roi = camAcq.getCameraROI();
      //      System.out.println("CameraROI: " + roi);
      //--------------------------------------------------------------------
      //      System.out.println("CoolerActive: " + camAcq.getCoolerActive());
      //      camAcq.setCoolerActive(true);
      // -------------------------------------------------------
      camAcq.setImageDirectory(".");
      camAcq.setFilePrefix("TESTPrefix");
      camAcq.setFileCounter(0);

      // String nextFileName();
      // -------------------------------------------------------
      // camAcq.reset();
      camAcq.setupSequence(5, 10000);
      camAcq.setupSequence(5, 10000, 5000);
      camAcq.captureSequenceToStack();
      camAcq.captureToImagePlus();

      camAcq.displayResume();

      camAcq.wait(4000);
      camAcq.setRecordToFile("record0.dat");
      camAcq.recordMsg("Record me");
   }


   public static void calibrateTest() {
      CamAcq camAcq = CamAcq.getInstance();
      camAcq.listAllParameters();
      Rectangle roi = camAcq.getSelectedROI();
      camAcq.setCameraROI(roi);
      //      byte[] pixelsByte = new byte[(int) Camera.width * (int) Camera.height];
      //      camAcq.start();
      //      int i = 0;
      //      int max = 10;
      //      while (i < max) {
      //         pixelsByte[0] = 0;
      //         camAcq.acquireImage(pixelsByte);
      //         ImageStatistics iStats = ImageAnalyzer.getStats(pixelsByte,
      //               new Dimension((int) Camera.width, (int) Camera.height));
      //         float mean = iStats.mean;
      //         System.out.println("mean = " + mean);
      //         try {
      //            Thread.sleep(1000);
      //         }
      //         catch (InterruptedException ex) {
      //         }
      //         i++;
      //      }
      //      camAcq.finish();
      //      camAcq.clearCameraROI();
   }
}
