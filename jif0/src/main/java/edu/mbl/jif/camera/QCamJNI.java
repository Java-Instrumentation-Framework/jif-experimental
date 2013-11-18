package edu.mbl.jif.camera;

///////////////////////////////////////////////////////////////////////////
import edu.mbl.jif.utils.string.StringUtil;

// QCamJNI: Interface to QImaging's Retiga EX Camera
// Relies on C code in QCamJNI.c which relies on QCamAPI.dll
// by Grant B. Harris, June 2006
// Notes on the Retiga EX Camera:

/*
"When readout ends, the next exposure or possibly the next readout
begins. (On model-B cameras, the next exposure will start while the
last exposure is reading out)."
 */
// From QCamAPI Nov 2005: Deprecated parameters:
// qprm64Exposure The exposure time in nanoseconds.
// qprmGain Deprecated. Use qprmNormalizedGain. "Expressed in micro units."
// qprmOffset Deprecated. Use qprmS32AbsoluteOffset., default 0
//////////////////////////////////////////////////////////////////////////
public class QCamJNI {
  // Camera state - from getParameters()

  public static long exposure;
  public static long gain;
  public static long offset;
  public static long exposureMin;
  public static long exposureMax;
  public static long binning;
  public static long readOutSpeed;
  public static long triggerType;
  public static long imageFormat;
  public static long depth;
  //public static long size; // this is in Bytes, not pixels
  public static long roiX;
  public static long roiY;
  public static long roiWidth;
  public static long roiHeight;
  public static long coolerActive;
  public static boolean wideDepth = false;
  //
  // Camera Information - from getInfo()
  public static long cameraType;
  public static long serialNumber;
  public static long hardwareVersion;
  public static long firmwareVersion;
  public static long ccd;
  public static long bitDepth; // maximum, 12
  public static long cooled;
  public static long imageWidth;
  public static long imageHeight;
  public static long imageSize;
  public static long ccdType;
  public static long ccdWidth;
  public static long ccdHeight;
  public static long firmwareBuild;
  public static long uniqueId;
  //
  // -------------------------------------------------------------------------
  // Image Frame Arrays
  //  QCam Pixels:
  //  qfmtMono8:  Monochrome 8-bit data. Each byte represents a pixel.
  //  qfmtMono16  Monochrome each 16-bit word (short) represents a pixel.
  //  Of course, '16-bit' really means a 12-bit image.
  //
  //  Full image: 1280 x 1024 =
  //     1,310,720 pixels x 2 bytes = 2,621,440 bytes for short
  //  2x2 binning: 640 x  512 =   327,680 pixels x 2 bytes =   655,360
  //
  /*
  RETIGA EX Frame Rates, per QImaging
  Exposure time: 1 ms
  8 BIT	12 BIT
  FULL RES. 	10.2 	 5.2
  2X2 BINNING	20.1	10.3
  3X3 BINNING	29.8	15.5
  4X4 BINNING	38.5	20.2
  50X50 ROI	38.5	19.9
  Combining binning and ROI, with Exposure time: 40 microseconds
  8 BIT	12 BIT
  2X2 BINNING + 50X50 ROI	86.2 	46.0
  3X3 BINNING + 50X50 ROI	92.6 	49.6
  4X4 BINNING +50X50 ROI	97.1 	52.0
   */
  // -------------------------------------------------------------------------
  public static short[] pixels16; // for display 16-bit
  public static byte[] pixels8; // for display  8-bit
  //
  public static boolean frameDone = false;

  public static synchronized void setFrameDone(boolean t) {
    frameDone = t;
  }

  // setImageArrays ---------------------------------------------------------
  // These array objects are filled with images by the C code
  // This needs to be (re)set when any of the following change:
  //   Binning, ROI, BitDepth(format)
  public static void setImageArray() {
    getInfo();
    getParameters();
    if (imageFormat == 2) {
      wideDepth = false;
    } else {
      wideDepth = true;
    }
    if (wideDepth) { // 16-bit
      pixels16 = new short[(int) imageSize];
      setImageArray16(pixels16);
    } else { // 8-bit
      pixels8 = new byte[(int) imageSize];
      setImageArray8(pixels8);
    }
  }

  public static void setImageArray8() {
    if (pixels8 != null) {
      setImageArray8(pixels8);
    }
  }

  public static void setImageArray16() {
    if (pixels16 != null) {
      setImageArray16(pixels16);
    }
  }

  public static void disposeImageArray() {
    pixels16 = null;
    disposeArrays16();
    pixels8 = null;
    disposeArrays8();
//   if (wideDepth) { // 16-bit
//      pixels16 = null;
//      disposeArrays16();
//   }
//   else { // 8-bit
//      pixels8 = null;
//      disposeArrays8();
//   }
    System.gc();  // ??
  }

  ////////////////////////////////////////////////////////////////////////////
  // Native calls into JNI to access the QCamJNI.dll
  //
  public static native void callBackSetup(Object callbackObj);

  // displayObj is the object containing a method named updateDisplay()
  // which, when in streaming (freerun trigger) mode,  is executed as
  // a 'callback' when the next frame is ready.


  public static native int loadQCamDriver();

  public static native void unLoadQCamDriver();

  public static native int getNumCameras();

  public native void getCameraList(long[][] cameraListArray);

  public static native boolean openCameraStreaming();

  // ----------------------------  Default settings
  //  m_state.mode	  = normal;
  //  m_state.format	  = mono;
  //  m_state.binning   = 2;
  //  m_state.wideDepth = false;
  //  m_state.exposure  = 2000;
  //  m_state.trigType  = software;
  //  m_state.readOut   = qcReadout20M;

  public static native void closeTheCamera();

  public static native int getInfo();

  public static native int getParameters();

  // setFormat - qfmtMono8 = 2 | qfmtMono16 = 3
  public static native int setFormat(long fmt);

  public static native int setBinning(long bin);

  // ReadoutSpeed: 20M = 0, 10M	= 1, 5M	= 2, 2M5 = 3, _last	= 4
  public static native int setReadoutSpeed(long speed);

  // sets the image arrays used for streaming, real-time display
  public static native void setImageArray8(byte[] byteArray);

  public static native void setImageArray16(short[] shortArray);

  public static synchronized native int displayOn();

  public static synchronized native int setForAcquire();

  public static native void disposeArrays8();

  public static native void disposeArrays16();

  public static native int setROI(long x, long y, long w, long h);

  public static native int setROIFull();

  // Exposure is in microseconds
  public static native int setExposure(long _exposure, long _gain, long _offset);

  public static native int queueExposureSet(long _exposure, long _gain, long _offset);

  public static native int getExposureMinMax();
  // tTriggerType: freerun=1, software=2, external=4
  public static final int TRIGGER_FREERUN = 1;
  public static final int TRIGGER_SOFTWARE = 2;
  public static final int TRIGGER_EXTERNAL = 4;

  public static native void setTriggerType(int tTriggerType);

  public static native int getTriggerType();

  // Queues the frame for the next capture DEFUNCT
  // public static synchronized native int queueAcqFrame(AcqFrame f);
  // Captures the next frame
  public static synchronized native int doSoftTrigger();

  public static native int setCooler(boolean onOff);

  //  public static native int openCameraSynch(long exposure);
  // synchronous image grabbing function (we are not using it)
  //public static synchronized native int snapImage(byte[] frameData); // Synchronous
  //////////////////////////////////////////////////////////////////////////
  // Load the C-code library (.DLL)
  public static boolean loadDLL() {
    try {
      System.loadLibrary("QCamJNI");  // assuming .dll is in java.library.path
      //System.load(".\\QCamJNI.dll");  // explicit path
      return true;
    } catch (Error e) {
      System.out.println(
              "Could not load QCamJNI.dll\njava.library.path= " + System.getProperty("java.library.path"));
      return false;
    }
  }

//----------------------------------------------------------------
  /////////////////////////////////////////////////////////////////////////
  // QCamJNI
  static byte[] takeSnapshot8(long jniHandle) {
    return null;
  }

  static byte[] takeSnapshot16b(long jniHandle) {
    return null;
  }

  static void enableFastSnapshots(long jniHandle) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  static byte[] takeFastFrame8(long jniHandle) {
    return null;
  }

  static void takeFastFrame8j(long jniHandle, byte[] imgArray) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  static byte[] takeFastFrame16(long jniHandle) {
    return null;
  }

  static void disableFastSnapshots(long jniHandle) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  // Steaming ----------
  static int enableStreaming(long jniHandle) {
    return 0;
  }

  static void startStreaming(long jniHandle) {
    displayOn();
  }

  static void stopStreaming(long jniHandle) {
    QCamJNI.setTriggerType(QCamJNI.TRIGGER_SOFTWARE);
  }

  static void disableStreaming(long jniHandle) {
    //throw new UnsupportedOperationException("Not yet implemented");
    }

  /////////////////////////////////////////////////////////////////////////
  public static String showCameraInfo() {
    String s =
            "Camera Information \n\n" + item("CameraType: ", String.valueOf(cameraType)) + item("SerialNumber: ", String.valueOf(serialNumber)) + item("HardwareVersion: ", String.valueOf(hardwareVersion)) + item("FirmwareVersion: ", String.valueOf(firmwareVersion)) + item("Ccd: ", String.valueOf(ccd)) + item("BitDepth(max): ", String.valueOf(bitDepth)) + item("Cooled: ", String.valueOf(cooled)) + item("ImageWidth: ", String.valueOf(imageWidth) + " pixels") + item("ImageHeight: ", String.valueOf(imageHeight) + " pixels") + item("ImageSize: ", String.valueOf(imageSize + " bytes")) + item("CcdType: ", String.valueOf(ccdType)) + item("CcdWidth: ", String.valueOf(ccdWidth)) + item("CcdHeight: ", String.valueOf(ccdHeight)) + item("FirmwareBuild: ", String.valueOf(firmwareBuild)) + item("UniqueId: ", String.valueOf(uniqueId));
    return s;
  }

  public static String showCameraState() {
    String s =
            "Camera State \n\n" + item("Exposure (microseconds): ", String.valueOf(exposure)) + item("Gain: ", String.valueOf(gain)) + item("Offset: ", String.valueOf(offset)) + item("Readout speed: ", String.valueOf(readOutSpeed)) + item("Trigger: ", String.valueOf(triggerType)) + item("CoolerActive: ", String.valueOf(coolerActive)) + item("ImgFormat(2=8bit,3=16bit): ", String.valueOf(imageFormat)) + item("Upper left X of ROI: ", String.valueOf(roiX)) + item("Upper left Y of ROI: ", String.valueOf(roiY)) + item("Width of ROI, pixels: ", String.valueOf(roiWidth)) + item("Height of ROI, pixels: ", String.valueOf(roiHeight));
    return s;
  }

  static String item(String s, String v) {
    return StringUtil.current().leftPad(s, 32) +
            StringUtil.current().leftPad(v, 16) + "\n";
  }

  //////////////////////////////////////////////////////////////////////////
  //  Utilities & testing
  //////////////////////////////////////////////////////////////////////////
  public static void setImageArraysFake(int w, int h) {
    int size = w * h;
    pixels8 = new byte[(int) size];
    fillTest8(pixels8, w, h, 64);
  }

  // fill the array with vertical bands of increasing brightness
  static void fillTest8(byte[] pix, int w, int h, int o) {
    int c = 0;
    int z = w / o;
    for (int i = 0; i < h; i++) {
      c = 0;
      for (int j = 0; j < w; j++) {
        pix[(i * w) + j] = (byte) c;
        if ((j % z) == 0) {
          c = c + z;
        }
      }
    }
  }

  static void fillTest16(short[] pix, int w, int h, int o) {
    int c = 0;
    int z = w / o;
    for (int i = 0; i < w; i++) {
      c = 0;
      for (int j = 0; j < h; j++) {
        pix[(i * w) + j] = (short) c;
        if ((w % i) == 0) {
          c = c + z;
        }
      }
    }
  }

  static void blankFrame8(byte[] pix) {
    for (int i = 0; i < pix.length; i++) {
      pix[i] = (byte) 127; // set to Gray
    }
  }

  static void blankFrame16(short[] pix) {
    for (int i = 0; i < pix.length; i++) {
      pix[i] = (short) 2048; // set to Gray
    }
  }
}
