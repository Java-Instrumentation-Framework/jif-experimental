/*
 * AcquisitionController.java
 *
 * Created on August 22, 2006, 10:31 AM
 */
package edu.mbl.jif.acq;

import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camera.StreamingVideoRecorder;
import edu.mbl.jif.camera.Utils;
import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.action.AbstractActionExt;
import edu.mbl.jif.gui.action.ActionManager;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;

import edu.mbl.jif.imaging.stream.StreamSource;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.microscope.illum.IllumModel;

import ij.ImagePlus;
import ij.gui.NewImage;

import java.awt.Dimension;



import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;



/**
 *
 * @author GBH
 */

/*
 * @todo add systemState = capturePreAcquisitionState();
 *  and restorePreAcquisitionState(systemState);
 */

public class AcquisitionController {

  InstrumentController instrumentCtrl;
  //CameraInterface cam;
  CameraModel cameraModel;
  AcqModel acqModel;
  ImageAcquisitioner acq;
  DataModel dataModel;
  String startTime = null;
  long acqTime;
  SequenceController seqCtrl;
  boolean doSequence = false;
  boolean doZseries = false;
  boolean recording = false;
  StreamingVideoRecorder vidRecA = null;
  IllumModel illum;
  private boolean wasEpiShutterOpen;
  private boolean wasXmisShutterOpen;

  /** Creates a new instance of AcquisitionController */
  public AcquisitionController(InstrumentController instrumentCtrl) {
    this.instrumentCtrl = instrumentCtrl;
    this.acqModel = (AcqModel) instrumentCtrl.getModel("acq");
    if (acqModel == null) {
      Application.getInstance().error("acqModel = null");
    }
    this.cameraModel = (CameraModel) instrumentCtrl.getModel("camera");
    this.dataModel = (DataModel) instrumentCtrl.getModel("data");
    this.illum = (IllumModel) instrumentCtrl.getModel("illum");
    //

  }

  // <editor-fold defaultstate="collapsed" desc="<<<  Action Methods >>>">
  public void doAcqImageAction() {
    // check if to do Zseries
    if (false /* @todo ZseriesCapable */) {
      if (isZseriesSelected()) {
        doZseries = true;
      } else {
        doZseries = false;
      }
    }
    if (dataModel.isIjPutOnDesk()) {
      if (isSequenceSelected()) {
        acquireSequenceIJ();
      } else {
        acquireAnImageIJ();
      }
    } else {
      // check if to do Sequence or single
      if (isSequenceSelected()) {
        acquireSequence();
      } else {
        acquireAnImage();
      }
    }
  }

  public void doAcqSequenceAction() {
    // check if to do Zseries
    if (false /* @todo ZseriesCapable */) {
      if (isZseriesSelected()) {
        doZseries = true;
      } else {
        doZseries = false;
      }
    }
    if (dataModel.isIjPutOnDesk()) {
      acquireSequenceIJ();
    } else {
      acquireSequence();
    }
  }

  // New June 2009
  public void doAcqCustom() {

  }

  boolean isZseriesSelected() {
    return ((AbstractActionExt) ActionManager.getInstance().getAction("toggleZseries")).isSelected();
  }

  boolean isSequenceSelected() {
    return ((AbstractActionExt) ActionManager.getInstance().getAction("toggleSequence")).isSelected();
  }

  // Called by recordVideo action
  public void recordVideo() {
    //  if (!recording) {
    //displaySuspend();
    String filename = dataModel.getImageDirectory() + "\\" + Utils.timeStamp() + ".vid.tif";
    if (startVideoRecorderA(
            (StreamGenerator) ((CameraModel) instrumentCtrl.getModel("camera")).getCamera(),
            filename)) {
      Application.getInstance().statusBarMessage("Recording video ...");
    } else {
      Application.getInstance().error("Failed to start video recording.");
    //displayResume();
    }
  //  } else {
//            stopRecordVideo();
//            CamAcqJ.getInstance().statusBarMessage("Video recording stopped.");
//            displayResume();
//        }
  }

  public void toggleSequence() {
    System.out.println("toggleSequence()");
  }

  public void toggleZseries() {
    System.out.println("toggleZseries()");
    doZseries = !doZseries;
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=" Acquisition of Images ">
  public void acquireAnImageIJ() {
    //        SwingUtilities.invokeLater(new Runnable() {
    //            public void run() {
    CamAcq.getInstance().captureToImagePlus();
  //            }
  //        });
  }

  // Acquires a single image, appending to tiff file
  public void acquireAnImage() {
    acquireAnImage(CamAcq.getInstance().makeFilename());
  }

  public void doPreImageAcq() {
  }

  public BufferedImage acquireImage() {
    Object imageArray;
    BufferedImage img;
    if (acqModel.getDepth() == 8) {
      imageArray = new byte[cameraModel.getWidth() * cameraModel.getHeight()];
    } else {
      imageArray = new short[cameraModel.getWidth() * cameraModel.getHeight()];
    }
    //
    start();
    acqTime = acquireImage(imageArray);
    finish();
    //
    if (imageArray instanceof byte[]) {
      // img = byteArrayToBufferedImage((byte[])imageArray, cam.getWidth(), cam.getHeight());
      img = createBufferedImage(cameraModel.getWidth(), cameraModel.getHeight(), (byte[]) imageArray);
    // img = ImageFactoryGrayScale.createImage(cameraModel.getWidth(), cameraModel.getHeight(), 8, (byte[]) imageArray);
    } else {
      img = createBufferedImage(cameraModel.getWidth(), cameraModel.getHeight(), (short[]) imageArray);
    // img = ImageFactoryGrayScale.createImage(cameraModel.getWidth(), cameraModel.getHeight(), 16,(short[]) imageArray);
    }
    return img;
  }

  public void acquireAnImage(String filename) {
    BufferedImage img = acquireImage();
    MultipageTiffFile.appendImageToTiffFile(img,
            dataModel.getImageDirectory() + "\\" + filename);
    edu.mbl.jif.gui.imaging.FrameImageDisplay id =
            new edu.mbl.jif.gui.imaging.FrameImageDisplay(img, filename);
    //id.setLocationByPlatform(true);
    id.setSize(StaticSwingUtils.sizeFrameForDefaultScreen(
            new Dimension(img.getWidth(), img.getHeight())));
    id.setVisible(true);
  }

  public byte[] acquireSampleImage() {
    int wasDepth = acqModel.getDepth();
    int wasMultiFrame = acqModel.getMultiFrame();
    acqModel.setDepth(8);
    acqModel.setMultiFrame(1);
    byte[] imageArray = new byte[cameraModel.getWidth() * cameraModel.getHeight()];
    start();
    acqTime = acquireImage(imageArray);
    finish();
    acqModel.setDepth(wasDepth);
    acqModel.setMultiFrame(wasMultiFrame);
    return imageArray;
  }
// </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< Sequence Acq.>>>">
  public void acquireSequence() {
    System.out.println("acquiring Sequence...");
    if (dataModel.isAutoSave()) {
      if (dataModel.isIjPutOnDesk()) {
        if (dataModel.isSeqInStack()) {
          // put an ImageJ sequence into an ImagePlus stack...
          // create ImagePlus and pass to SeqCtrl
        }
      }
      String baseFilename = dataModel.getImageDirectory() + "\\" + CamAcq.getInstance().makeFilename();
      seqCtrl = new SequenceController(instrumentCtrl,
              (long) (acqModel.getInitDelay() * 1000),
              (long) (acqModel.getInterval() * 1000),
              acqModel.getImagesInSequence(),
              baseFilename);
    }
  }

  // Capture Series to ImageJ Stack
  public void acquireSequenceIJ() {
    CamAcq camAcq = CamAcq.getInstance();
    // Initialize the ImageJ ImagePlus stack
    // allocate image memory

    ij.ImagePlus iPlus = createImagePlusStack(camAcq.getDepth(), acqModel.getImagesInSequence());
    if (iPlus == null) {
      Application.getInstance().error("imp == null");
      return;
    }
    ij.ImageStack stack = iPlus.getStack();
    if (stack.getSize() == 0) {
      Application.getInstance().error("stack size = 0");
      return;
    }
    // set filename
    String baseFilename = CamAcq.getInstance().makeFilename();
    iPlus.setStack(baseFilename, stack);
    CamAcqJ.getInstance().statusBarMessage("Acquiring series...");
    // +++ flush first frame here...
    seqCtrl = new SequenceController(instrumentCtrl,
            (long) (acqModel.getInitDelay() * 1000),
            (long) (acqModel.getInterval() * 1000),
            acqModel.getImagesInSequence(),
            baseFilename,
            iPlus);
  }

  public ij.ImagePlus createImagePlusStack(int depth, int slices) {
    CamAcq camAcq = CamAcq.getInstance();
    ij.ImagePlus iPlus;
    if (depth == 16) {
      iPlus = camAcq.newCaptureStackShort(slices);
    } else {
      iPlus = camAcq.newCaptureStackByte(slices);
    }
    return iPlus;
  }

// </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="<<< Video Recording >>>">
  public void stopRecordVideo() {
    stopVideoRecorderA();
  }

  public boolean startVideoRecorderA(StreamGenerator cam,
          final String filename) {
    final StreamSource source = cam.getStreamSource();
    final VideoRecordDialog dvr = new VideoRecordDialog(null, true,
            instrumentCtrl);
    if (source == null) {
      return false;
    }
    vidRecA = new StreamingVideoRecorder(cam, filename, dvr);
    if (vidRecA == null) {
      return false;
    }
    java.awt.EventQueue.invokeLater(new Runnable() {

      public void run() {
        dvr.setVisible(true);
      }
    });
    cam.startStream();
    vidRecA.record();
    recording = true;
    return true;
  }

  public void stopVideoRecorderA() {
    if (vidRecA != null) {
      vidRecA.finish();
    }
    recording = false;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< Acquistion Start/Stop >>>">
  // Single image acquisition - start/finish are done for you.
  public void captureSingleImage(Object imageArray) {
    start();
    acquireImage(imageArray);
    finish();
  //Utils.log("Image acquired at " + startTime);
  }

  // For a fast series acquisition, use:
  //   start();
  //   loop doing {
  //     acquireImage();
  //   }
  //   finish();
  //
  public void start() {
    start(false);
  }

//  OLD
//  public void start(boolean flushFirst) {
//    if (cameraModel == null) {
//      System.err.println("Attempted to start Acq without camera.");
//      return;
//    }
//    displaySuspend();
//    // Enter AcqMode(X)
//    openShutter();
//
//    if (!cameraModel.isSameSetAcqStream()) {
//      cameraModel.setExposureAcq(cameraModel.getExposureAcq());
//      cameraModel.setGainAcq(cameraModel.getGainAcq());
//    }
//    String type = cameraModel.getCameraType();
//    if (type.equalsIgnoreCase(CameraModel.TYPE_MOCK)) {
//      acq = new Acquisitioner_Mock(cameraModel.getCamera());
//    } else if (type.equalsIgnoreCase(CameraModel.TYPE_QCAM)) {
//      acq = new Acquisitioner_Q(cameraModel.getCamera());
//    } else if (type.equalsIgnoreCase(CameraModel.TYPE_LUCAM)) {
//      acq = new Acquisitioner_Lu(cameraModel.getCamera());
//    } else {
//      return;
//    }
//    acq.setDepth(acqModel.getDepth());
//    acq.setMultiFrame(acqModel.getMultiFrame(), acqModel.isDiv());
//    acq.setMirrorImage(acqModel.isMirrorImage());
//    if (flushFirst) {
//      acq.start(true);
//    } else {
//      acq.start();
//    }
//    startTime = Utils.timeStamp();
//    return;
//  }
  public void start(boolean flushFirst) {
    if (cameraModel == null) {
      System.err.println("Attempted to start Acq without camera.");
      return;
    }
    if (!cameraModel.isSameSetAcqStream()) {
      cameraModel.setExposureAcq(cameraModel.getExposureAcq());
      cameraModel.setGainAcq(cameraModel.getGainAcq());
    }
    start(flushFirst,
            cameraModel.getExposureAcq(), cameraModel.getGainAcq(), acqModel.getDepth(),
            acqModel.getMultiFrame(), acqModel.isMirrorImage());
  }

  public void start(double exposure, double gain, int depth, int multiFrame, boolean mirror) {
    start(false, exposure, gain, depth, multiFrame, mirror);
  }

  public void start(boolean flushFirst, double exposure, double gain, int depth, int multiFrame, boolean mirror) {
    if (cameraModel == null) {
      System.err.println("Attempted to start Acq without camera.");
      return;
    }
        String type = cameraModel.getCameraType();
    if (type.equalsIgnoreCase(CameraModel.TYPE_MOCK)) {
      acq = new Acquisitioner_Mock(cameraModel.getCamera());
    } else if (type.equalsIgnoreCase(CameraModel.TYPE_QCAM)) {
      acq = new Acquisitioner_Q(cameraModel.getCamera());
    } else if (type.equalsIgnoreCase(CameraModel.TYPE_LUCAM)) {
      acq = new Acquisitioner_Lu(cameraModel.getCamera());
    } else {
      return;
    }
    if(acq==null) {
      System.err.println("acq==null in start");
      return;
    }
    displaySuspend();
    cameraModel.setExposureAcq(exposure);
    cameraModel.setGainAcq(gain);
    acq.setDepth(depth);
    acq.setMultiFrame(multiFrame, true);
    acq.setMirrorImage(mirror);
    if (flushFirst) {
      acq.start(true);
    } else {
      acq.start();
    }
    startTime = Utils.timeStamp();
    openShutter();
    return;
  }

  public long acquireImage(Object imageArray) {
    long acqTime = -1;
    if (acq != null) {
      acqTime = acq.acquireImage(imageArray);
    } else {
      System.err.println("Attempted to acquireImage() without start()");
    }
    //System.out.println("acquireImage() done.");
    this.acqTime = acqTime;
    return acqTime;
  }

  public void finish() {
    if (acq != null) {
      acq.finish();
      acq = null;
    //System.out.println("finished.");
    }
    closeShutter();
    displayResume();
  }


  private void openShutter() {
    if (illum != null) {
      wasEpiShutterOpen = illum.isOpenEpi();
      wasXmisShutterOpen = illum.isOpenXmis();
      if (acqModel.isOpenShutterEpi()) {
        if (!wasEpiShutterOpen) {
          illum.setOpenEpi(true);
        }
      }
      if (acqModel.isOpenShutterXmis()) {
        if (!wasXmisShutterOpen) {
          illum.setOpenXmis(true);
        }
      }
    }
  }

  private void closeShutter() {
    if (illum != null) {
      if (!wasEpiShutterOpen) {
        illum.setOpenEpi(false);
      }
      if (!wasXmisShutterOpen) {
        illum.setOpenXmis(false);
      }
    }
  }
  public void displaySuspend() {
    DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
    if (disp != null) {
      disp.suspend();
    }
  }

  public void displayResume() {
    DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
    if (disp != null) {
      disp.restart();
    }
  }

  //---------------------------------------------------------------------------------
  // A R R A Y --- captureArray - acquires an array
  // Acquire a frame from the camera and return a byte[] or short[] array
  public Object captureArray() {
    start();
    if (acqModel.getDepth() == 12) {
      short[] pixelsShort = new short[getImageWidth() * getImageHeight()];
      pixelsShort[0] = 0;
      acquireImage(pixelsShort);
      finish();
      return pixelsShort;
    } else {
      byte[] pixelsByte = new byte[getImageWidth() * getImageHeight()];
      pixelsByte[0] = 0;
      acquireImage(pixelsByte);
      finish();
      return pixelsByte;
    }
  }

  public int getImageWidth() {
    return cameraModel.getWidth();
  }

  public int getImageHeight() {
    return cameraModel.getHeight();
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< Discrete Measurement >>>">
    /*
   * "Discrete Measurement" means taking a series of images for measurements, such as calibration.
   *  The camera stream is suspended and discrete images are captured.  This means that one must first
   * setupForDiscreetMeasurement(), acquire and measure using doDiscreetMeasurement(),
   * and then teardownForDiscreetMeasurement();
   */
  Object imageArrayMeasure;
  BufferedImage imgMeasure;

  public void testMeasurement() {
    Thread t = new Thread(new Runnable() {

      public void run() {
        setupForDiscreetMeasurement();
        for (int i = 0; i < 10; i++) {
          doDiscreetMeasurement();
          CamAcq.getInstance().wait(250);
        }
        teardownForDiscreetMeasurement();
      }
    });
    t.start();
  }

  public void setupForDiscreetMeasurement() {
    if (acqModel.getDepth() == 8) {
      imageArrayMeasure = new byte[getImageWidth() * getImageHeight()];
    } else {
      imageArrayMeasure = new short[getImageWidth() * getImageHeight()];
    }
    if (imageArrayMeasure instanceof byte[]) {
      imgMeasure = createBufferedImage(getImageWidth(), getImageHeight(), (byte[]) imageArrayMeasure);
    } else {
      imgMeasure = ImageFactoryGrayScale.createImage(cameraModel.getWidth(), cameraModel.getHeight(), 16,
              (short[]) imageArrayMeasure);
    }
    start();
  }

  public void doDiscreetMeasurement() {
    long acqTime = 0;
    acqTime = acquireImage(imageArrayMeasure);
    StaticSwingUtils.dispatchToEDT(new Runnable() {

      public void run() {
        DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
        if (disp != null) {
          disp.getImageDisplayPanel().showImage(imgMeasure);
        }
      }
    });
  }

  public void teardownForDiscreetMeasurement() {
    finish();
  }
  // </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc=" <<< Create BufferedImage >>>">

  public BufferedImage createBufferedImage(int width, int height, byte[] pixels) {
    SampleModel sampleModel;
    IndexColorModel icm = getDefaultColorModel();
    WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
    sampleModel = wr.getSampleModel();
    sampleModel = sampleModel.createCompatibleSampleModel(width, height);
    DataBuffer db = new DataBufferByte(pixels, width * height, 0);
    WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);
    BufferedImage image = new BufferedImage(icm, raster, false, null);
    return image;
  }

  public BufferedImage createBufferedImage(int width, int height, short[] pixels) {
    SampleModel sampleModel;
    IndexColorModel icm = getDefaultColorModel();
    WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
    sampleModel = wr.getSampleModel();
    sampleModel = sampleModel.createCompatibleSampleModel(width, height);
    DataBuffer db = new DataBufferUShort(pixels, width * height, 0);
    WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);
    BufferedImage image = new BufferedImage(icm, raster, false, null);
    return image;
  }

  /** Returns the default grayscale IndexColorModel. */
  public IndexColorModel getDefaultColorModel() {
    byte[] r = new byte[256];
    byte[] g = new byte[256];
    byte[] b = new byte[256];
    for (int i = 0; i < 256; i++) {
      r[i] = (byte) i;
      g[i] = (byte) i;
      b[i] = (byte) i;
    }
    IndexColorModel defaultColorModel = new IndexColorModel(8, 256, r, g, b);
    return defaultColorModel;
  }

  public BufferedImage byteArrayToBufferedImage(byte[] imgArray) {
    return byteArrayToBufferedImage(imgArray, this.getImageWidth(), this.getImageHeight());
  }

  public static BufferedImage byteArrayToBufferedImage(byte[] imgArray,
          int width, int height) {
    int size = width * height;
    BufferedImage image = null;
    byte[] image_data = new byte[size];
    image_data = imgArray;
    DataBuffer db = new DataBufferByte(image_data, image_data.length);
    try {
      image = new BufferedImage(width, height,
              BufferedImage.TYPE_BYTE_GRAY);
      WritableRaster wr = image.getRaster();
      wr.setDataElements(0, 0, width, height, image_data);
    } catch (Exception e) {
    }
    return image;
  // If you need a planar image
  // PlanarImage pi = PlanarImage.wrapRenderedImage(ri)
  }
// </editor-fold>
//
  // <editor-fold defaultstate="collapsed" desc="<<< _not used >>>">

  public void acquireAnImageNOT2() {
    ImageAcqTask task = new ImageAcqTask(10000);
    //      final JProgressBar progressBar = new JProgressBar(0, 100);
    //      progressBar.setIndeterminate(true);
    //      //        frame.add(progressBar, BorderLayout.NORTH);
    //      //        frame.setSize(500, 500);
    //      //        frame.setVisible(true);
    //      task.addPropertyChangeListener(new PropertyChangeListener() {
    //            public void propertyChange(PropertyChangeEvent evt) {
    //               if ("progress".equals(evt.getPropertyName())) {
    //                  progressBar.setIndeterminate(false);
    //                  progressBar.setValue((Integer)evt.getNewValue());
    //               }
    //            }
    //         });
    task.execute();
  }

  public BufferedImage acquireAnImageNOT() {
    Object imageArray;
    BufferedImage img;
    if (acqModel.getDepth() == 8) {
      imageArray = new byte[cameraModel.getWidth() * cameraModel.getHeight()];
    } else {
      imageArray = new short[cameraModel.getWidth() * cameraModel.getHeight()];
    }
    long acqTime = 0;
    start();
    acqTime = acquireImage(imageArray);
    finish();
    //  final byte[] imageArray = cam.takeSnapshot8();
    if (imageArray != null) {
      if (imageArray instanceof byte[]) {
        //                img = byteArrayToBufferedImage((byte[])imageArray, cam.getWidth(),
        //                        cam.getHeight());
        img = ImageFactoryGrayScale.createImage(cameraModel.getWidth(),
                cameraModel.getHeight(), 8,
                (byte[]) imageArray);
      } else {
        img = ImageFactoryGrayScale.createImage(cameraModel.getWidth(),
                cameraModel.getHeight(), 16,
                (short[]) imageArray);
      }
      String name = Utils.timeStamp();
      MultipageTiffFile.appendImageToTiffFile(img, dataModel.getImageDirectory() + "\\" +
              name);
      return img;
    } else {
      System.out.println("doAcqImage failed.");
      return null;
    }
  }

  //=================================================================================
  class ImageAcqTask
          extends SwingWorker<List<Integer>, Integer> {

    final int numbersToFind;
    //sorted list of consequent prime numbers
    private final List<Integer> primeNumbers;

    ImageAcqTask(int numbersToFind) {
      this.numbersToFind = numbersToFind;
      this.primeNumbers = new ArrayList<Integer>(numbersToFind);
    }

    @Override
    public List<Integer> doInBackground() {
      Object imageArray;
      BufferedImage img;
      if (acqModel.getDepth() == 8) {
        imageArray = new byte[cameraModel.getWidth() * cameraModel.getHeight()];
      } else {
        imageArray = new short[cameraModel.getWidth() * cameraModel.getHeight()];
      }
      long acqTime = 0;
      start();
      acqTime = acquireImage(imageArray);
      finish();
      if (imageArray != null) {
        if (dataModel.isIjPutOnDesk()) {
          ImagePlus iPlus = null;

          // CamAcq.getInstance().putInImagePlus(imageArray, acqTime);
          if (imageArray instanceof byte[]) {
            iPlus = NewImage.createByteImage("Title",
                    (int) cameraModel.getWidth(),
                    (int) cameraModel.getHeight(), 1, 0);
          } else {
            iPlus = NewImage.createShortImage("Title",
                    (int) cameraModel.getWidth(),
                    (int) cameraModel.getHeight(), 1, 0);
            iPlus.getProcessor().invertLut();
          }
          iPlus.getProcessor().setPixels(imageArray);
          iPlus.show(); // display image in ImageJ window
          iPlus.updateAndRepaintWindow();
        } else {
          if (imageArray instanceof byte[]) {
            img = byteArrayToBufferedImage((byte[]) imageArray,
                    cameraModel.getWidth(),
                    cameraModel.getHeight());
          //                    img = ImageFactoryGrayScale.createImage(cameraModel.getWidth(),
          //                            cameraModel.getHeight(), 8, (byte[])imageArray);
          } else {
            img = ImageFactoryGrayScale.createImage(cameraModel.getWidth(),
                    cameraModel.getHeight(),
                    16, (short[]) imageArray);
          }

          MultipageTiffFile.appendImageToTiffFile(img, "test001.tif");
          edu.mbl.jif.gui.imaging.FrameImageDisplay id = new edu.mbl.jif.gui.imaging.FrameImageDisplay(
                  img,
                  String.valueOf(acqTime));
          id.setVisible(true);
        }
      } else {
        System.out.println("doAcqImage failed.");
      }

      //            int number = 2;
      //            while(primeNumbers.size() < numbersToFind
      //                    && !isCancelled()) {
      //                if (isPrime(number)) {
      //                    primeNumbers.add(number);
      //                    setProgress(100 * primeNumbers.size() / numbersToFind);
      //                    publish(number);
      //                }
      //                number++;
      return primeNumbers;
    }

    @Override
    protected void process(List<Integer> chunks) {
      for (int number : chunks) {
        //textArea.append(number + "\n" );
      }
    }
  }
  // </editor-fold>
}
