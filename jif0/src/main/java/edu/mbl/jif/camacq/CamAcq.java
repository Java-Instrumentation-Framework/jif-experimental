package edu.mbl.jif.camacq;

import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.acq.AcqModel;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.acq.Acquisitioner_Lu;
import edu.mbl.jif.camera.CamUtils;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.DataRecorder;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.gui.imaging.DisplayLiveInterface;
import edu.mbl.jif.camera.Utils;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.fabric.ApplicationListener;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.action.ActionManager;

import org.pf.joi.Inspector;
import com.holub.asynch.Condition;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.File;

import java.text.DecimalFormat;

import javax.swing.ProgressMonitor;

/**
 * <PRE>edu.mbl.jig.camera.CamAcq
 *
 * CamAcq:  Creates Camera, Display, Acquisitioner, DataRecorder, objects and
 * wraps them in a set of methods for easy access - eg. for access by ImageJ plugins....
 *
 * Calling getInstance() creates the singleton instance of CamAcq,
 * starts the Application, sets itself as an ApplicationListener,
 * and waits for it to complete (when applicationDidInit is called).
 * It also initializes the camera and opens the CamAcqJ user interface.
 * </PRE>
 */
public class CamAcq implements ApplicationListener {

  static {
    System.out.println("Classpath: " +
            System.getProperty("java.class.path"));
  }
  private static CamAcq _instance; // Singleton instance, defaults to null
  //static AppFrame aFrame; // only one!
  static ApplicationFrameTabbed aFrame;
  static boolean hasBeenInitialized = false;
  static boolean deBug = false;    // ImageJ Objects
  private static InstrumentController instrumentCtrl;    // Camera
  private static CameraModel camModel;
  // private static CameraInterface cam;
  private static DisplayLiveInterface display;    // Acquisitioner
  private static AcqModel acqModel;
  private static Acquisitioner_Lu acq;
  private static AcquisitionController acqCtrl;    // Data
  private static DataModel dataModel;
  //
  static final DecimalFormat format = new DecimalFormat();


  static {
    format.setMinimumFractionDigits(1);
    format.setMaximumFractionDigits(1);
  }
  private static Condition initNotComplete = new Condition(true);
  public static final int DEPTH_BYTE = 8;
  public static final int DEPTH_SHORT = 12;    //private String camType = "LuCam";
  private String camType = "QCam";
  boolean cameraOpen = false;
  private boolean displayOn = false;
  DataRecorder dRec;
  String recordToFile = null;
  String stackName = "NewStack";
  int currentSlice = 0;
  ProgressMonitor pm = null;
  //
  // <editor-fold defaultstate="collapsed" desc="<<< Instantiate / Initialize >>>">

  private CamAcq() {
    // System.out.println("IsEDT? " + SwingUtilities.isEventDispatchThread());

    if (CamAcqJ.getInstance() == null) {
      CamAcqJ camAcqJ = new CamAcqJ();
      camAcqJ.addApplicationListener(this);
      camAcqJ.start();
      int tries = 30;
      while (initNotComplete.is_true()) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
        tries--;
        if (tries == 0) {
          Application.getInstance().error("Timed out on initialization");
        //return null;
        }
      }
    } else {
      setupModels();
    }
  }

  /**
   * Returns the instance of CamAcq.
   * CamAcq is a Singleton... do not try to instantiate.
   * Instead, use CamAcq.getInstance().
   * @return The singleton instance of CamAcq
   */
  public static CamAcq getInstance() {
    if (_instance == null) {
      synchronized (CamAcq.class) {
        if (_instance == null) {
          _instance = new CamAcq();
        }
      }
    }
    return _instance;
  }

  /**
   * Notification from Application that initialization has completed.
   * For implementation of ApplicationListener.
   */
  public void applicationDidInit() {
    System.out.println("applicationDidInit");
    setupModels();
    initNotComplete.set_false();
  }

  public void setupModels() {
    instrumentCtrl = (InstrumentController) CamAcqJ.getInstance().getController();
    if (instrumentCtrl != null) {
      camModel = ((CameraModel) instrumentCtrl.getModel("camera"));
      //camModel = instrumentCtrl.cameraModel;
      acqModel = ((AcqModel) instrumentCtrl.getModel("acq"));
      dataModel = ((DataModel) instrumentCtrl.getModel("data"));
      acqCtrl = ((AcquisitionController) instrumentCtrl.getController("acq"));
    } else {
      Application.getInstance().error("Instrument Controller failed to initialize");
    }
  }

  public Object getController(String ctrlName) {
    return instrumentCtrl.getController(ctrlName);
  }

  public Object getModel(String modelName) {
    return instrumentCtrl.getModel(modelName);
  }

  /**
   * Called by Application when exiting to assure complete shutdown.
   * For implementation of ApplicationListener.
   * @return Gives the Application permission to exit.
   */
  public boolean canApplicationExit() {
    return true;
  }

  /**
   * Notification from the Application that it is exiting.
   * For implementation of ApplicationListener.
   */
  public void applicationExiting() {
  }
  // </editor-fold>
  //
  // <editor-fold defaultstate="collapsed" desc="<<< Camera >>>">
  // --- Camera ------------------------------------------------------
    /*
  private boolean createCamera() {
  if (camType == "LuCam") {
  return createLuCamera();
  } else if (camType == "QCam") {
  return false; //createQCamera();
  } else {
  return false;
  }
  }

  private boolean createLuCamera() {
  try {
  int numCams = LuCamJNI.getNumCameras();
  if (numCams < 1) {
  return false;
  }
  cam = new LuCamera(1);
  //cam.open();
  cam.getCameraState();
  cam.initialize();
  //?? was for QCam --  cam.initializeCamera();
  } catch (Exception ex) {
  ex.printStackTrace();
  return false;
  }
  if (cam == null) {
  return false;
  }
  // Exposure
  //spinExpos = new SpinnerExposure(_instance);
  setExposureAcq((float)Prefs.usr.getInt("camera.exposure", 10000) / 1000f);
  // Gain
  float initGain = Prefs.usr.getFloat("cameraGain", 1.0f);
  if (initGain < 0) {
  initGain = 0;
  }
  //model_Gain = new SpinnerNumberModel(initGain, 1.0, 25.0, 0.1);
  setGainAcq(initGain);
  setGainSteam(initGain);

  // Offset
  int offsetMax = 4095;
  int offsetMin = 0;
  int initOffset = Prefs.usr.getInt("cameraOffset", 1500);
  if (initOffset < 0) {
  initOffset = 0;
  }
  //model_Offset = new SpinnerNumberModel(initOffset, 0, 4095, 100);
  cameraOpen = true;
  return true;
  }
   */

  /**
   * Is the camera open?
   * @return Is camera open
   */
  public boolean isCameraOpen() {
    return cameraOpen;
  }

  /**
   * Get the camera.
   * @return Camera attached to CamAcq
   */
//    public CameraInterface getCamera() {
//        return cam;
//    }
  /**
   * Close the camera and driver.
   */
  public void closeCamera() {
    if (camModel != null) {
      camModel.close();
    }
    cameraOpen = false;
  }

  /**
   * Get current width of image returned from camera.
   * @return image width
   */
  public int getWidth() {
    return (int) camModel.getWidth();
  }

  /**
   * Get current height of image returned from camera.
   * @return image height
   */
  public int getHeight() {
    return (int) camModel.getHeight();
  }

  /**
   * Get current camera exposure setting
   * @return exposure
   */
  public double getExposure() {
    return camModel.getExposureAcq();
  }

  /**
   * Set camera exposure time (in microseconds) for the camera when streaming
   * @param _exposure exposure
   */
  public void setExposureStream(double _exposure) {
    camModel.setExposureStream(_exposure);
  }

  /**
   * Set the exposure time (milliseconds) for the camera for image acquisition
   * @param _exposure exposure
   */
  public void setExposureAcq(double _exposure) {
    camModel.setExposureAcq(_exposure);
  }

  /**
   * Set the exposure time (milliseconds) for the camera
   * @param _exposure exposure time
   */
  public void setExposure(double _exposure) {
    setExposureAcq(_exposure);
  }

  /**
   * Get minimum allowable camera exposure setting
   * @return minimum exposure
   */
  public double getExposureMin() {
    return camModel.getExposureAcqMin();
  }

  /**
   * Get minimum allowable camera exposure setting
   * @return maximum exposure
   */
  public double getExposureMax() {
    return camModel.getExposureAcqMax();
  }

  /**
   * Get the current camera gain.
   * @return Normalized Gain. Expressed in micro units
   */
  public double getGain() {
    return camModel.getGainAcq();
  }

  /**
   * Set the current camera gain for streaming.
   * @param _gain Normalized gain
   */
  public void setGainSteam(float _gain) {
    camModel.setGainStream(_gain);
  }

  /**
   * Set the current camera gain for acquisition.
   * @param _gain Normalized gain
   */
  public void setGainAcq(float _gain) {
    camModel.setGainAcq(_gain);
  }

  /**
   * Set camera gain, normalized (for acq.)
   * @param _gain Normalized gain >=1.0
   */
  public void setGain(float _gain) {
    camModel.setGainAcq(_gain);
  }

  /**
   * Get the current camera offset (absolute)
   * @return Absolute offset
   */
  public int getOffset() {
    return (int) camModel.getOffsetAcq();
  }

  /**
   *
   * Set camera absolute Offset. Default 0.
   * @param _offset Absolute Offset, default 0
   */
  public void setOffset(int _offset) {
    camModel.setOffsetAcq(_offset);
  }

  /**
   * Get the current camera binning.
   * @return binning
   */
  public int getBinning() {
    return camModel.getBinning();
  }

  /**
   * Set the camera binning
   * 1x1, 2x2, or 4x4
   * @param _binning Binning: 1, 2 or 4.
   */
  public void setBinning(int _binning) {
    camModel.setBinning(_binning);
  }

  /**
   * Get the current camera readout speed
   * @return readout speed
   */
  public double getSpeed() {
    return camModel.getSpeed();
  }

  /**
   * Set camera readout speed, in megaHertz
   * @param _speed Readout speed, MHz: 20, 10, 5 or 2.5
   */
  public void setSpeed(double _speed) {
    camModel.setSpeed(_speed);
  }
  //---------------------------------------------------------------------------
  // ROI parameters

  /**
   * Get the current camera Region of Interest (ROI)
   * @return ROI rectangle
   */
  public Rectangle getCameraROI() {
    return new Rectangle(
            camModel.getRoiX(),
            camModel.getRoiY(),
            camModel.getRoiW(),
            camModel.getRoiH());
  }

  /**
   * Set camera Region of Interest, ROI
   * @param roi ROI rectangle
   */
  public void setCameraROI(Rectangle roi) {
    camModel.setROI(roi);
  }

  /**
   * Clear the camera Region of Interest, ROI
   */
  public void clearCameraROI() {
    camModel.clearRoi();
  }

  /**
   * Is camera cooler active?
   * @return Active or not.
   */
  public boolean isCoolerActive() {
    return camModel.isCoolerActive();
  }

  /**
   * Set camera cooler active
   * @param _coolerActive true or false
   */
  public void setCoolerActive(boolean _coolerActive) {
    camModel.setCoolerActive(_coolerActive);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< Display >>>">
  //======================= Display ==================================

  /**
   * Gets the live camera display (a DisplayLiveCamera, which extends JFrame)
   * <I>Display control</I>
   * @return DisplayLiveCamera
   */
  public DisplayLiveCamera getDisplay() {
    Object ctrl = CamAcqJ.getInstance().getController();
    if (ctrl == null) {
      return null;
    } else {
      return ((InstrumentController) ctrl).getDisplayLive();
    }
  }

  /**
   * Open the live camera display.
   * <I>Display control</I>
   */
  public void displayOpen() {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        ((AbstractAction) ActionManager.getInstance().getAction("openLiveDisplay")).actionPerformed(
                null);
      }
    });
  }

  /**
   * Suspend streaming to the the live display.
   * <I>Display control</I>
   */
  public void displaySuspend() {
    if (getDisplay() != null) {
      getDisplay().suspend();
    }
    displayOn = false;
  }

  /**
   * Resume streaming to the the live display.
   * <I>Display control</I>
   */
  public void displayResume() {
    if (getDisplay() != null) {
      getDisplay().resume();
    }
    displayOn = true;
  }

  /**
   * Close and dispose the live camera display frame.
   * <I>Display control</I>
   */
  public void displayClose() {
    if (getDisplay() != null) {
      getDisplay().close();
    }
    displayOn = false;
  }

  /**
   * Is the live camera display open and streaming?
   * <I>Display control</I>
   * @return Is on.
   */
  public boolean displayIsOn() {
    return displayOn;
  }

  /**
   * ROI currently selected in display.
   * Returns (0,0,0,0) in no selection or no display open.
   * <I>Display control</I>
   * @return ROI rectangle
   */
  public Rectangle getSelectedROI() {
    if (getDisplay() != null) {
      return getDisplay().getSelectedROI();
    } else {
      return new Rectangle(0, 0, 0, 0);
    }
  }

  /**
   * Sets the ROI in the live display.
   * <I>Display control</I>
   * @param roi ROI rectangle
   */
  public void setSelectedROI(Rectangle roi) {
    if (getDisplay() != null) {
      getDisplay().setSelectedROI(roi);
    }
  }

  /**
   * Is a ROI selected in the live display.
   * <I>Display control</I>
   * @return true or false
   */
  public boolean isROISelected() {
    if (getDisplay() != null) {
      Rectangle roi = getDisplay().getSelectedROI();
      return (roi.width > 0);
    } else {
      return false;
    }
  }

  /*
  public void displaySetMode(int mode) {
  if (getDisplay() != null) {
  }

  // @todo displaySetMode
  //     display.setLUT(mode);
  //     switch mode:
  //      display.setBorderColor(new Color(0, 100, 200));
  //      setBorderColor(new Color(226, 215, 123));
  }
   */    // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< Acquisition Setup >>>">
  /**
   * Enable mirroring of image both for display and acquisition.
   * @param t Enable (true) or disable (false) mirroring
   */
  public void setMirrorImage(boolean t) {
    camModel.setMirrorImage(t);
  }

  /**
   * Is Mirroring enabled?
   * @return Is mirroring on
   */
  public boolean isMirrorImage() {
    return camModel.isMirrorImage();
  }
  //-----------------------------------------------------------
  // Frame Averaging and Resulting image depth

  /**
   * Set number of exposures for Multi-frame acquisition
   * (May or may not be averaged, see setAveraging())
   * <I>Acquisition control</I>
   * @param n Number of frames/exposures
   */
  public void setMultiFrame(int n) {
    acqModel.setMultiFrame(n);
  }

  /**
   * Get number of exposures for Multi-frame acquisition
   * <I>Acquisition control</I>
   * @return Number of frames/exposures to average or integrate
   */
  public int getMultiFrame() {
    return acqModel.getMultiFrame();
  }

  /**
   * Set averaging or integration for multi-frame acquisition.
   * <I>Acquisition control</I>
   * @param b true: average;  false: integrate
   */
  public void setAveraging(boolean b) {
    acqModel.setDiv(b);
  }

  /**
   * Is averaging set for multi-frame acquisition.
   * <I>Acquisition control</I>
   * @return true: averaging;  false: integrating
   */
  public boolean isAveraging() {
    return acqModel.isDiv();
  }

  /**
   * Get image bit-depth for acquistion
   * <I>Acquisition control</I>
   * @return 8-bit or 12-bit
   */
  public int getDepth() {
    return acqModel.getDepth();
  }

  /**
   * Set image bit-depth for acquistion
   * <I>Acquisition control</I>
   * @param _depth 8 or 12
   */
  public void setDepth(int _depth) { // this is Acquisition depth
    if (_depth == 16 || _depth == 12) {
      _depth = DEPTH_SHORT;
    }
    if ((_depth != DEPTH_BYTE) && (_depth != DEPTH_SHORT)) {
      System.err.println("Attempt to set depth to illegal value (" + _depth + ")");
      return;
    }
    acqModel.setDepth(_depth);
  }
  //-----------------------------------------------------------
  // Parameters on Image
  //   public void setAddParms(boolean t) {
  //        acqModel.g
  //      Prefs.usr.putBoolean("parmsInFilename", t);
  //   }
  //
  //

  public boolean getAddParms() {
    return true; // Prefs.usr.getBoolean("parmsInFilename", false);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< Data >>>">
  //----------------------------------------------------------------
  // Save-Image-to-File parameters

  /**
   * Set Auto-saving of images as they are acquired
   * @param t true or false
   */
  public void setAutoSave(boolean t) {
    dataModel.setAutoSave(t);
  }

  public boolean isAutoSave() {
    return dataModel.isAutoSave();
  }

  /**
   * Put the acquired image in a window on the desktop
   * @param t
   */
  public void setPutOnDesk(boolean t) {
    dataModel.setIjPutOnDesk(t);
  }

  public boolean isPutOnDesk() {
    return dataModel.isIjPutOnDesk();
  }

  /**
   * Set the directory/folder for saving image data.
   * @param _dir Folder in which to save image data files
   */
  public void setImageDirectory(String _dir) {
    /** @todo  Check if exists... */
    if (checkDirOK(_dir)) {
      dataModel.setImageDirectory(_dir);
    } else {
      System.out.println("Directory does not exist");
    }
  }

  /**
   * Get image date path/directory
   * @return Image data path
   */
  public String getImageDirectory() {
    return dataModel.getImageDirectory();
  }

  /**
   * Sets the file prefix, used to label image files.
   * File name will be the file prefix followed by the file counter and
   * the file will have a �tif� extension.
   * The whole file name will be �file_PrefixfileCounter.tif�
   * @param _prefix filename prefix
   */
  public void setFilePrefix(String _prefix) {
    dataModel.setFilePrefix(_prefix);
  }

  /**
   * Get the prefix string used for auto-naming files
   * @return File name prefix
   */
  public String getFilePrefix() {
    return dataModel.getFilePrefix();
  }

  /**
   * Sets the value of the file counter for file naming using when prefix.
   * Typically reset to zero.
   * @param _fileCount serial number counter value
   */
  public void setFileCounter(int _fileCount) {
    dataModel.setFileCounter(_fileCount);
  }

  /**
   * Gets the value of the file counter for file naming
   * @see setFilePrefix
   * @return current number of the file counter (serial number)
   */
  public int getFileCounter() {
    return dataModel.getFileCounter();
  }

  public String nextFileName() {
    String s = null;
    int count = getFileCounter();
    s = getFilePrefix() + padWithZero(String.valueOf(count), 4);
    count++;
    setFileCounter(count);
    return s;
  }

  /**
   * Creates a new filename for an image based on currently selected naming scheme.
   * @return New filename
   */
  public String makeFilename() {
    String filename = "";
    if (dataModel.isUseFilenamePrefix()) {
      filename = nextFileName();
    } else {
      filename = Utils.timeStamp();
    }
    return filename;
  }

  String padWithZero(String s, int len) {
    while (s.length() < len) {
      s = "0" + s;
    }
    return s;
  }

  /**
   * Check to see that a directory exists
   * @param path Path to check
   * @return true if path exists, else false.
   */
  public boolean checkDirOK(String path) {
    File dir = new File(path);
    if (dir.exists() == false) {
      return false;
    }
    if (dir.isDirectory() == false) {
      return false;
    }
    return true;
  }
  //-----------------------------------------------------------------------

  public void recordMsg(String s) {
    System.out.println(s);
    if (recordToFile != null) {
      dRec.add("");
    }
  }

  public void setRecordToFile(String file) {
    recordToFile = file;
    dRec = new DataRecorder("filename.txt");
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< Setup for Series Acq. >>>">
    /*
   * Simple series image acquisition of _numImages at _iterval time intervals
   * Sets up the environment for a series acquisition.
   * �num� of images in the series,
   * �interval� is the time between the images in the sequence and
   * �initial_interval� is the initial time interval before taking the first image.
   */

  /**
   * Set the number of images (intervals) when acquiring a sequence of images.
   * @param n Number of images to acquire in sequence
   */
  public void setImagesInSequence(int n) {
    acqModel.setImagesInSequence(n);
  }

  /**
   * Set the number of images (intervals) when acquiring a sequence of images.
   * @return Number of images in sequence
   */
  public int getImagesInSequence() {
    return acqModel.getImagesInSequence();
  }

  /**
   * Set the time interval for acquiring a sequence of images.
   * @param i Time interval in seconds
   */
  public void setInterval(double i) {
    acqModel.setInterval(i);
  }

  /**
   * Get time interval for sequence
   */
  public double getInterval() {
    return acqModel.getInterval();
  }

  /**
   * Set the initial delay when acquiring a sequence of images.
   * @param i Initial delay in seconds
   */
  public void setInitDelay(double i) {
    acqModel.setInitDelay(i);
  }

  /**
   * Get the initial delay for acquiring a sequence of images.
   * @return initial delay in seconds
   */
  public double getInitDelay() {
    return acqModel.getInitDelay();
  }

  public void setupSequence(int _numImages, double _interval) {
    setupSequence(_numImages, _interval, 0);
  }

  public void setupSequence(int _numImages, double _interval, int _initDelay) {
    acqModel.setImagesInSequence(_numImages);
    acqModel.setInterval(_interval);
    acqModel.setInitDelay(_initDelay);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< Acquisition >>>">
  //-----------------------------------------------------------

  public void startAcq() {
    startAcq(false);
  }

  public void startAcq(boolean flushFirst) {
    acqCtrl.start(flushFirst);
  }

  /**
   * returns acquisition time
   */
  public long acquireImage(Object imageArray) {
    return acqCtrl.acquireImage(imageArray);
  }

  public void finishAcq() {
    acqCtrl.finish();
  }

  public void captureSingleImage(Object imageArray) {
    acqCtrl.captureSingleImage(imageArray);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="<<< ImageJ Image Capture >>>">
  static ImagePlus iPlus;
  static ImageStack stack;
  //-----------------------------------------------------------
  // captureToImagePlus
  // Acquires single image to an ij.ImagePlus

  public void captureToImagePlus() {
    // Initialize the ImageJ ImagePlus stack & allocate image memory
    Application.getInstance().statusBarMessage("Acquiring to ImagePlus...");
    ImageProcessor ip = null;
    ImagePlus imgPlus = null;

    try {
      if (getDepth() == DEPTH_SHORT) {
        ip = new ShortProcessor((int) camModel.getWidth(), (int) camModel.getHeight());
        ip.getCurrentColorModel();
      //System.out.println("isLUTInverted: " + ip.isInvertedLut());
      //ip.invertLut();
      } else {
        ip = new ByteProcessor((int) camModel.getWidth(), (int) camModel.getHeight());
      }
      imgPlus = new ImagePlus("title", ip);
    } catch (OutOfMemoryError e) {
      Application.getInstance().error("OutOfMemory Error in captureToImage");
      return;
    }
    if (deBug) {
      Inspector.inspectWait(imgPlus);
      listAllParameters();
      System.out.println("acqModel.getDepth() = " + acqModel.getDepth());
    }
    //
    long acqTime = captureToImagePlus(imgPlus);

    //
    String name = makeFilename();
    imgPlus.setTitle(name);
    setScale(imgPlus);
    if (this.getAddParms()) {
      addParms(imgPlus.getProcessor(), String.valueOf(acqTime));
      imgPlus.updateImage();
    }
    if (isPutOnDesk()) {
      imgPlus.show(); // display image in ImageJ window
      imgPlus.updateAndRepaintWindow();
    }
    if (isAutoSave()) {
      FileSaver fs = new FileSaver(imgPlus);
      if (!fs.saveAsTiff(getImageDirectory() + "\\" + name + ".tif")) {
        System.err.println("Error in saveAsTiff, " + getImageDirectory() + "\\" + name +
                ".tif");
      }
    } else {
      imgPlus.changes = true;
    }
    if (!isPutOnDesk()) {
      imgPlus.flush();
      imgPlus = null;
    }
    Application.getInstance().statusBarMessage(
            "Image Captured: " + name + ", " + getMultiFrame() + "  frameAvg");
  }

  public long captureToImagePlus(ImagePlus iPlus) {
    // Acquire the image
    /** @todo check that iPlus has compatible type */
    long acqTime = 0;
    acqCtrl.start();
    if (iPlus.getType() == ImagePlus.GRAY8) {
      byte[] pixelsByte = (byte[]) iPlus.getProcessor().getPixels();
      pixelsByte[0] = 0;
      acqTime = acqCtrl.acquireImage(pixelsByte);
    } else if (iPlus.getType() == ImagePlus.GRAY16) {
      short[] pixelsShort = (short[]) iPlus.getProcessor().getPixels();
      pixelsShort[0] = 0;
      acqTime = acqCtrl.acquireImage(pixelsShort);
    }
    acqCtrl.finish();
    return acqTime;
  }
  //-----------------------------------------------------------
  // putInImagePlus
  // Acquires single image to an ij.ImagePlus

  public void putInImagePlus(Object imageArray, long acqTime) {
    // Initialize the ImageJ ImagePlus stack & allocate image memory
    Application.getInstance().statusBarMessage("Acquiring to ImagePlus...");
    ImageProcessor ip = null;
    ImagePlus imgPlus = null;
    try {
      if (imageArray instanceof short[]) {
        ip = new ShortProcessor((int) camModel.getWidth(), (int) camModel.getHeight());
        ip.getCurrentColorModel();
      //System.out.println("isLUTInverted: " + ip.isInvertedLut());
      //ip.invertLut();
      } else {
        ip = new ByteProcessor((int) camModel.getWidth(), (int) camModel.getHeight());
      }
      imgPlus = new ImagePlus("title", ip);
    } catch (OutOfMemoryError e) {
      Application.getInstance().error("OutOfMemory Error in captureToImage");
      return;
    }
    imgPlus.getProcessor().setPixels(imageArray);
    String name = makeFilename();
    imgPlus.setTitle(name);
    setScale(imgPlus);
    if (this.getAddParms()) {
      addParms(imgPlus.getProcessor(), String.valueOf(acqTime));
      imgPlus.updateImage();
    }
    if (isPutOnDesk()) {
      imgPlus.show(); // display image in ImageJ window
      imgPlus.updateAndRepaintWindow();
    }
    if (isAutoSave()) {
      FileSaver fs = new FileSaver(imgPlus);
      String imgDir = getImageDirectory();
      if (!fs.saveAsTiff(getImageDirectory() + "\\" + name + ".tif")) {
        System.err.println("Error in saveAsTiff, " + getImageDirectory() + "\\" + name +
                ".tif");
      }
    } else {
      imgPlus.changes = true;
    }
    if (!isPutOnDesk()) {
      imgPlus.flush();
      imgPlus = null;
    }
    Application.getInstance().statusBarMessage(
            "Image Captured: " + name + ", " + getMultiFrame() + "  frameAvg");
  }
// </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=" IJ Stack Capture ">
  //---------------------------------------------------------------------------
  // newCaptureStack - Creates a new ImageJ Stack for capture

  /**
   * Creates a new ImagePlus with 2 slices in preparation for image capture.  It is named using the currently specified filenaming scheme.
   */
  public ImagePlus newCaptureStackByte() {
    return newCaptureStack(8, 2);
  }

  /**
   * Creates a new ImagePlus with numberOfSlices in preparation for image capture.  It is named using the currently specified filenaming scheme.
   * @param numberOfSlices Number of slices in new ImagePlus
   * @return ImagePlus with specified number of slices
   */
  public ImagePlus newCaptureStackByte(int numberOfSlices) {
    return newCaptureStack(8, numberOfSlices);
  }

  /**
   * Creates a new ImagePlus with numberOfSlices in preparation for image capture.  It is named using the currently specified filenaming scheme.
   * @return ImagePlus with 2 slices
   */
  public ImagePlus newCaptureStackShort() {
    return newCaptureStack(12, 2);
  }

  /**
   * Creates a new ImagePlus with numberOfSlices in preparation for image capture.  It is named using the currently specified filenaming scheme.
   * @param numberOfSlices Number of slices
   * @return ImagePlus with stack
   */
  public ImagePlus newCaptureStackShort(int numberOfSlices) {
    return newCaptureStack(12, numberOfSlices);
  }

  ImagePlus newCaptureStack(int depth, int numSlices) {
    // Initialize the ImageJ ImagePlus stack & allocate image memory
    if (acqModel.getDepth() == 8) { // result = byte []
      return newCaptureStackByte(numSlices, (int) camModel.getWidth(), (int) camModel.getHeight());
    //iPlus = NewImage.createByteImage("Title", (int) camModel.getWidth(),(int) camModel.getHeight(), numSlices, 0);
    } else {
      return newCaptureStackShort(numSlices, (int) camModel.getWidth(), (int) camModel.getHeight());
    // iPlus = NewImage.createShortImage("Title", (int) camModel.getWidth(), (int) camModel.getHeight(), numSlices, 0);
    // iPlus.getProcessor().invertLut();
    }

  }

  public ImagePlus newCaptureStackByte(int numSlices, int width, int height) {
    return newCaptureStack(8, numSlices, width, height);
  }

  public ImagePlus newCaptureStackShort(int numSlices, int width, int height) {
    return newCaptureStack(16, numSlices, width, height);
  }

  public ImagePlus newCaptureStack(int depth, int numSlices, int width, int height) {
    ImagePlus iPlus;
    try {
      // Initialize the ImageJ ImagePlus stack & allocate image memory
      if (depth == 8) { // result = byte []
        iPlus = NewImage.createByteImage("Title", width, height, numSlices, 0);
      } else {
        iPlus = NewImage.createShortImage("Title", width, height, numSlices, 0);
        iPlus.getProcessor().invertLut();
      }
    } catch (OutOfMemoryError e) {
      Application.getInstance().error("OutOfMemoryError in captureSeriesToStack");
      return null;
    }
    if (iPlus == null) {
      Application.getInstance().error("imp == null");
      return null;
    }
    ImageStack stack = iPlus.getStack();
    if (stack.getSize() == 0) {
      Application.getInstance().error("stack size = 0");
      return null;
    }
    String name = makeFilename();
    iPlus.setStack(name, stack);
    return iPlus; // return new stack
  }
  //---------------------------------------------------------------------------

  /**
   * Make the specified ImagePlus the destination for acquired image.
   * @param _imgPlus ImagePlus for images
   */
  public void setImagePlusForCapture(ImagePlus _imgPlus) {
    int type = _imgPlus.getType();
    if ((type != ImagePlus.GRAY8) && (type != ImagePlus.GRAY16)) {
      Application.getInstance().error("Wrong image type");
      return;
    } // ok then...
    iPlus = _imgPlus;
    stack = iPlus.getStack();
    currentSlice = 0;
  }
  //---------------------------------------------------------------------------

  /**
   * Acquire an image and append it to the current ImagePlus stack.
   */
  public void captureImageToStack() {
    // acquire image and append it to the stack
    addSliceIfNecessary(this.stack, currentSlice);
    captureImageToStack(this.stack, currentSlice);
    currentSlice++;
  }

  /**
   * Acquire an image and append it to the specified ImagePlus stack at the specified slice.
   * @param _stack The stack
   * @param _slice The slice
   */
  public void captureImageToStack(ImageStack _stack, int _slice) {
    if (acqCtrl == null) {
      /** @todo do start() & finish() automatically */
      System.err.println("Attempted to captureImageToStack() without start()");
      return;
    }
    acqCtrl.start();
    if ((_slice + 1) > _stack.getSize()) {
      System.err.println("Attempted to acquire beyond end of stack");
      return;
    }
    Application.getInstance().statusBarMessage("Acquiring image...");
    long acqTime = 0;
    if (_stack.getPixels(1) instanceof byte[]) {
      byte[] pixelsByte = (byte[]) _stack.getPixels(_slice + 1);
      pixelsByte[0] = 0;
      acqTime = acqCtrl.acquireImage(pixelsByte);
    } else if (_stack.getPixels(1) instanceof short[]) {
      short[] pixelsShort = (short[]) _stack.getPixels(_slice + 1);
      pixelsShort[0] = 0;
      acqTime = acqCtrl.acquireImage(pixelsShort);
    }
    acqCtrl.finish();
    Application.getInstance().statusBarMessage("");
    _stack.setSliceLabel(String.valueOf(acqTime), _slice + 1);
    // annotations
    if (getAddParms()) {
      addTimemark(_stack.getProcessor(_slice + 1), acqTime);
    }
  }

  /**
   * Acquire an image and append/insert it to the current ImagePlus stack at the specified slice.
   * @param _slice The slice
   */
  public void captureImageToStack(int _slice) {
    // acquire image and put in slice N of the stack
    if (_slice < 0) {
      return;
    }
    /** @todo create next slice while current acq. is in process. */
    captureImageToStack(stack, _slice - 1);
  }

  /**
   * Adds a slice to an ImageStack if currently has < n slices
   * @param _stack ij.ImageStack
   * @param n number of slices necessary
   */
  public void addSliceIfNecessary(ImageStack _stack, int n) {
    addSlice(_stack, n);
  }

  public void addSlice(ImagePlus iPlus, int n) {
    ImageStack _stack = iPlus.getStack();
    addSlice(_stack, n);
  }

  public void addSlice(ImageStack _stack, int n) {
    while (_stack.getSize() < (n + 1)) {
      if (_stack.getPixels(0) instanceof byte[]) {
        byte[] p = new byte[_stack.getWidth() * _stack.getHeight()];
        _stack.addSlice("slice", p);
      } else if (_stack.getPixels(0) instanceof short[]) {
        short[] p = new short[_stack.getWidth() * _stack.getHeight()];
        _stack.addSlice("slice", p);
      } else {
        System.err.println("Couldn't add slice - not byte or short type");
        return;
      }
      System.out.println("added slice, stackSize: " + _stack.getSize());
    }
  }

  public void captureImageToSlice(ImagePlus iPlus, int _slice) {
    captureImageToSlice(iPlus, _slice, null);
  }

  public void captureImageToSlice(ImagePlus iPlus, int _slice, String annotation) {
    ImageStack _stack = iPlus.getStack();
    if (acqCtrl == null) {
      /** @todo do start() & finish() automatically */
      System.err.println("Attempted to captureImageToStack() without start()");
      return;
    }
    if ((_slice + 1) > _stack.getSize()) {
      System.err.println("Attempted to acquire beyond end of stack");
      return;
    }
    long acqTime = 0;
    if (_stack.getPixels(1) instanceof byte[]) {
      byte[] pixelsByte = (byte[]) _stack.getPixels(_slice + 1);
      pixelsByte[0] = 0;
      acqTime = acqCtrl.acquireImage(pixelsByte);
    } else if (_stack.getPixels(1) instanceof short[]) {
      short[] pixelsShort = (short[]) _stack.getPixels(_slice + 1);
      pixelsShort[0] = 0;
      acqTime = acqCtrl.acquireImage(pixelsShort);
    }
    _stack.setSliceLabel(String.valueOf(acqTime), _slice + 1);
    if (annotation != null) {
      addAnnotation(_stack.getProcessor(_slice + 1), annotation);
    } else if (getAddParms()) {
      addTimemark(_stack.getProcessor(_slice + 1), acqTime);
    }
  }

  /**
   * Finish the capture of an ImageStack
   * Saves the file, updates the scale, and displays the window.
   * @param name
   */
  public void captureStackFinish(String name) {
    stackName = name;
    captureStackFinish(iPlus, stackName);
  }

  public void captureStackFinish() {
    stackName = iPlus.getShortTitle();
    captureStackFinish(iPlus, stackName);
  }

  public void captureStackFinish(ImagePlus _iPlus, String filename) {
    finishAcq();
    wait(100);
    setScale(_iPlus);
    _iPlus.updateImage();
    //if (Prefs.usr.getBoolean("parmsInFilename", true)) {
    //            addParms(iPlus.getStack().getProcessor(1), lastTimeStamp);
    //            iPlus.updateImage();
    //        }
    if (dataModel.isIjPutOnDesk()) {
      _iPlus.setSlice(1);
      _iPlus.show(); // display image in ImageJ window
      _iPlus.updateAndRepaintWindow();
    }
    /** @todo Need a mechanism to cancel... */
    //      if (pm.isCanceled()) { // was cancelled
    //         Application.getInstance().error("Series Capture Stopped.  Data not saved to file.");
    //      }
    //      else { // otherwise, save...
    if (this.isAutoSave()) {
      FileSaver fs = new FileSaver(_iPlus);
      if (_iPlus.getStackSize() == 1) {
        fs.saveAsTiff(getImageDirectory() + "\\" + filename + ".tif");
      } else {
        fs.saveAsTiffStack(getImageDirectory() + "\\" + filename + ".tif");
      }
    } else {
      _iPlus.changes = true;
    }
    //}
    if (!dataModel.isIjPutOnDesk()) {
      _iPlus.flush();
      _iPlus = null;
    }
    if (Application.isDebug()) {
      //TimerHR.mark("all done");
    }
    Application.getInstance().statusBarMessage("captureStackFinish done: " + filename);
  }
  public void captureStackComplete(ImagePlus _iPlus, String filename) {
    wait(100);
    setScale(_iPlus);
    _iPlus.updateImage();
    //if (Prefs.usr.getBoolean("parmsInFilename", true)) {
    //            addParms(iPlus.getStack().getProcessor(1), lastTimeStamp);
    //            iPlus.updateImage();
    //        }
    if (dataModel.isIjPutOnDesk()) {
      _iPlus.setSlice(1);
      _iPlus.show(); // display image in ImageJ window
      _iPlus.updateAndRepaintWindow();
    }
//    if (this.isAutoSave()) {
//      FileSaver fs = new FileSaver(_iPlus);
//      if (_iPlus.getStackSize() == 1) {
//        fs.saveAsTiff(getImageDirectory() + "\\" + filename + ".tif");
//      } else {
//        fs.saveAsTiffStack(getImageDirectory() + "\\" + filename + ".tif");
//      }
//    } else {
//      _iPlus.changes = true;
//    }
    //}
    if (!dataModel.isIjPutOnDesk()) {
      _iPlus.flush();
      _iPlus = null;
    }
    Application.getInstance().statusBarMessage("captureStackFinish done: " + filename);
  }

  /**
   * Capture a series into an ImageJ Stack using the current settings.
   */
  public void captureSequenceToStack() {
    // Initialize the ImageJ ImagePlus stack
    // allocate image memory
    if (getDepth() == DEPTH_SHORT) {
      iPlus = newCaptureStackShort(acqModel.getImagesInSequence());
    } else {
      iPlus = newCaptureStackByte(acqModel.getImagesInSequence());
    }
    if (iPlus == null) {
      Application.getInstance().error("imp == null");
      return;
    }
    stack = iPlus.getStack();
    if (stack.getSize() == 0) {
      Application.getInstance().error("stack size = 0");
      return;
    }

    // set filename
    String stackName = makeFilename();
    iPlus.setStack(stackName, stack);
    pm = new ProgressMonitor(null, "Series Acquisition Running", "Acquireing Series...", 0,
            acqModel.getImagesInSequence());
    pm.setMillisToPopup(200);
    Application.getInstance().statusBarMessage("Acquiring series...");
    // +++ flush first frame here...
    acqCtrl.start();
    int i = 0;
    while (i < acqModel.getImagesInSequence()) {
      Application.getInstance().statusBarMessage("Acquiring image # " + String.valueOf(i + 1) + " of " +
              String.valueOf(acqModel.getImagesInSequence()));
      long startTime = System.currentTimeMillis();

      captureImageToStack(stack, i);

      //stack.setSliceLabel(String.valueOf(acqTime), i + 1);
      //         if (getAddParms()) {
      //            addTimemark(stack.getProcessor(i + 1));
      //         }
      updateProgress(i);

      long nextTime = startTime + (long) (getInterval() * 1000);
      i++;
      // Wait until Interval passes
      long toWait = nextTime - System.currentTimeMillis();
      int q = 0;
      while (toWait > 0) {
        toWait = nextTime - System.currentTimeMillis();
        if ((toWait > 500) && (q > 200)) {
          pm.setNote("Acquired " + (i + 1) + " of " + acqModel.getImagesInSequence() +
                  ", next in " + (toWait / 1000) + " sec.");
          q = 0;
        }
        if (i < acqModel.getImagesInSequence()) {
          CamUtils.waitFor(5);
        }
        if (pm.isCanceled()) {
          break;
        }
        q++;
      }
      if (pm.isCanceled()) {
        break;
      }
    }
    wait(100);
    acqCtrl.finish();
    setScale(iPlus);
    //        if (Prefs.usr.getBoolean("parmsInFilename", true)) {
    //            addParms(iPlus.getStack().getProcessor(1), lastTimeStamp);
    //            iPlus.updateImage();
    //        }
    if (dataModel.isIjPutOnDesk()) {
      iPlus.show(); // display image in ImageJ window
      iPlus.updateAndRepaintWindow();
    }
    if (pm.isCanceled()) { // was cancelled
      Application.getInstance().error("Series Capture Stopped.  Data not saved to file.");
    } else { // otherwise, save...
      if (dataModel.isAutoSave()) {
        FileSaver fs = new FileSaver(iPlus);
        if (iPlus.getStackSize() == 1) {
          fs.saveAsTiff(getImageDirectory() + "\\" + stackName + ".tif");
        } else {
          fs.saveAsTiffStack(getImageDirectory() + "\\" + stackName + ".tif");
        }
      } else {
        iPlus.changes = true;
      }
    }
    if (!dataModel.isIjPutOnDesk()) {
      iPlus.flush();
      iPlus = null;
    }
    Application.getInstance().statusBarMessage("Series Captured to Stack: " + stackName + ", " +
            acqModel.getImagesInSequence() + " images, " + acqModel.getMultiFrame() + "  frameAvg");
  }

  /**
   * setScale -- Scales the ImageJ image for display
   * 0-255 for byte type
   * 0-4095 for short type
   * @param _imp ImagePlus to scale
   */
  public void setScale(ImagePlus _imp) {
    int max = 0;
    if (_imp.getStack().getProcessor(1).getPixels() instanceof short[]) {
      if ((this.getDepth() == DEPTH_BYTE) && (this.getMultiFrame() > 1) &&
              !this.isAveraging()) {
        max = 255 * getMultiFrame(); // acquired at 8-bits
        if (_imp.getStackSize() > 1) {
          for (int i = 1; i <= _imp.getStackSize(); i++) {
            _imp.setSlice(i);
            ImageProcessor ip = _imp.getProcessor();
            ip.setMinAndMax(0, max);
          }
        } else {
          _imp.getProcessor().setMinAndMax(0, max);
        }
      } else {
        if (getDepth() == DEPTH_SHORT) {
          if (isAveraging()) {
            max = 4095;
          } else {
            max = 4095 * getMultiFrame(); // acquired at 12-bits
          }
          if (_imp.getStackSize() > 1) {
            for (int i = 1; i <= _imp.getStackSize(); i++) {
              _imp.setSlice(i);
              ImageProcessor ip = _imp.getProcessor();
              ip.setMinAndMax(0, max);
            }
          } else {
            _imp.getProcessor().setMinAndMax(0, max);
          }
        }
      }
    }
  }
// </editor-fold>

  private void updateProgress(final int i) {
    if (pm != null) {
      Utils.dispatchToEDT(new Runnable() {

        public void run() {
          pm.setProgress(i + 1);
          pm.setNote("Acquired " + (i + 1) + " of " + acqModel.getImagesInSequence());
        }
      });
    }
  }


  // <editor-fold defaultstate="collapsed" desc="<<< ImageAnnotations >>>">
  /**
   *
   * @return
   */
  public String getAcqParms() {
    return "not implemented";
  }

  /**
   *
   * @param img
   * @param timeDate
   */
  public void addParms(BufferedImage img, String timeDate) {
  }

  /**
   *
   * @param ip
   * @param timeDate
   */
  public void addParms(ImageProcessor ip, String timeDate) {
    String frames = "";
    if (getMultiFrame() > 1) {
      frames = String.valueOf(this.getMultiFrame());
      if (this.isAveraging()) {
        frames = frames + "-FrmsAvg  ";
      } else {
        frames = frames + "-FrmsInt ";
      }
    }
    // Acq Parameters: exposure, gain, offset, binning, depth, frames
    String parms = format.format(
            getExposure()) + "ms, " +
            format.format(getGain()) + ", " +
            format.format(getOffset()) + ";  " +
            String.valueOf(getBinning()) + ", " +
            getDepth() + "-bit;  " +
            frames + timeDate;

    ip.setFont(new Font("SansSerif", Font.PLAIN, 10));
    ip.setColor(Color.black);
    ip.drawString(parms, 8, ip.getHeight() - 1);
    ip.setColor(Color.white);
    ip.drawString(parms, 9, ip.getHeight() - 2);
  }

  private void addAnnotation(ImageProcessor ip, String annot) {
    ip.setFont(new Font("SansSerif", Font.PLAIN, 10));
    ip.setColor(Color.black);
    ip.drawString(annot, 8, 14);
    ip.setColor(Color.white);
    ip.drawString(annot, 9, 15);
  }

  private void addTimemark(ImageProcessor ip, long time) {
    String t = String.valueOf(time);
    ip.setFont(new Font("SansSerif", Font.PLAIN, 10));
    ip.setColor(Color.black);
    ip.drawString(t, 8, 14);
    ip.setColor(Color.white);
    ip.drawString(t, 9, 15);
  }
  //-------------------------------------------------------------------

  /**
   *
   * @param bImage
   * @param data
   */
  public static void dataStripe(BufferedImage bImage, String data) {
    Graphics bG = bImage.getGraphics();
    int w = bImage.getWidth();
    int h = bImage.getHeight();
    // adjust font size to match stripe size
    // ###########
    bG.setFont(new Font("SansSerif", Font.PLAIN, 11));
    bG.setColor(Color.black);
    //bG.fillRect(0, (int) ((h * 31) / 32), w, h - (int) ((h * 31) / 32));
    bG.fillRect(0, h - 14, w, h);
    bG.setColor(Color.white);
    bG.drawString(data, 10, h - 3);
    bG.dispose();
  }
  // </editor-fold>

  //-----------------------------------------------------------
  // wait
  /**
   * Wait for n milliseconds
   */
  public void wait(int msecs) {
    System.out.println("wait (" + msecs + ")");
    try {
      Thread.sleep(msecs);
    } catch (InterruptedException e) {
    }
  }
  //---------------------------------------------------------------------------

  void waitForInterval() {
    Application.getInstance().statusBarMessage("");
    wait(100);

  /** @todo make interuptable */
  /*  pm.setProgress(i + 1);
  pm.setNote("Acquired " + (i + 1) + " of " + getImagesInStack());

  long nextTime = startTime + (long) (getInterval() * 1000);
  i++;
  // Wait until Interval passes
  long toWait = nextTime - System.currentTimeMillis();
  int q = 0;
  while (toWait > 0) {
  toWait = nextTime - System.currentTimeMillis();
  if (toWait > 500 && q > 200) {
  pm.setNote("Acquired " + (i + 1) + " of  " + getImagesInStack() +
  ", next in " + (toWait / 1000) + " sec.");
  q = 0;
  }
  if (i < getImagesInStack()) {
  CamUtils.waitFor(5);
  }
  if (pm.isCanceled()) {
  break;
  }
  q++;
  }
  if (pm.isCanceled()) {
  break;
  }
   */
  }

  //-----------------------------------------------------------------------------------------
  /**
   * List all parameters
   */
  public void listAllParameters() {
    System.out.println(">>> Camera and Image Parameters " +
            "---------------------------------------------------------------");
    System.out.print("Width: ");
    System.out.println(getWidth());
    System.out.print("Height: ");
    System.out.println(getHeight());
    System.out.print("Exposure: ");
    System.out.println(getExposure());
    System.out.print("ExposureMin: ");
    System.out.println(getExposureMin());
    System.out.print("ExposureMax: ");
    System.out.println(getExposureMax());
    System.out.print("Gain: ");
    System.out.println(getGain());
    System.out.print("Offset: ");
    System.out.println(getOffset());
    System.out.print("Depth: ");
    System.out.println(getDepth());
    System.out.print("Binning: ");
    System.out.println(getBinning());
    System.out.print("Speed: ");
    System.out.println(getSpeed());
    System.out.print("isMirrorImage: ");
    System.out.println(isMirrorImage());

    //   System.out.print("SelectedROI: ");
    //   System.out.println(Rectangle getSelectedROI());
    //   System.out.print("CameraROI: ");
    //   System.out.println(Rectangle getCameraROI());
    System.out.print("CoolerActive: ");
    System.out.println(isCoolerActive());

    System.out.println(">>> Acquisition Parameters");
    System.out.print("FramesToAverage: ");
    System.out.println(getMultiFrame());
    System.out.print("isDivideResult: ");
    System.out.println(isAveraging());
    //      System.out.print("Result: ");
    //      System.out.println(getResult());
    System.out.print("AddParms: ");
    System.out.println(getAddParms());

    System.out.println(">>> Series/Stack Acq Parameters");
    System.out.print("ImagesInStack: ");
    System.out.println(this.getImagesInSequence());
    //System.out.print("setSaveRaw: ");
    //System.out.println(void setSaveRaw (boolean t));
    System.out.print("PutOnDesk: ");
    System.out.println(isPutOnDesk());
    System.out.print("ImageDirectory: ");
    System.out.println(getImageDirectory());
    System.out.print("FilePrefix: ");
    System.out.println(getFilePrefix());
    System.out.print("FileCounter: ");
    System.out.println(getFileCounter());
    System.out.print("Interval: ");
    System.out.println(getInterval());
    System.out.print("InitDelay: ");
    System.out.println(this.getInitDelay());
    System.out.println("------------------------");
  }

  /**
   * Show  parameters...
   */
  public void showParameters() {
  }

  /**
   * Called to shutdown the camera and display when exiting.
   */
  public void terminate() {
    displayClose();
    closeCamera();
    hasBeenInitialized = false;
  }

  /**
   * Cancel any pending operations
   */
  public void cancel() {
    /** @todo  Cancel Series mechanism */
  }

  /**
   * Reset everything to its default values.
   */
  public void reset() {
    /** @todo ?? Set everything back to defaults
     * numberFrames, ...
     */
    this.setMultiFrame(1);
    this.setDepth(8);
  }

  /**
   * Enable debugging output.
   * @param t enable
   */
  public void setDeBug(boolean t) {
    deBug = t;
  }
}
