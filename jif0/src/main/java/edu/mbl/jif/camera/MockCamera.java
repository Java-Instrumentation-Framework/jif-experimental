package edu.mbl.jif.camera;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Random;


/*
Produces test images/stacks/streams
images
stacks
streams

 */
/** @todo see ThreadSpawner */
public class MockCamera
        implements CameraInterface, StreamGenerator {

  static final int CHANNEL_A = 1;
  int camNum = -1;
  long jniHandle = -1;
  StreamSource sourceStream = null;
  // PolStack bkgdStack;
  // PolStack pStack;
  private float brightness;
  private float contrast;
  private float hue;
  private float saturation;
  private float sharpness;
  private float gamma;
  private float exposureStream;
  private float exposureAcq;
  private float gainStream;
  private float gainAcq;
  private float color_format;
  private float auto_exp_target;
  private float auto_exp_maximum;
  private float timestamps;
  private float snapshot_clock_speed;
  private float temperature;
  private float trigger;
  private float frame_gate;
  private float exposure_interval;
  private float pwm;
  // ROI
  private long xOffset; // x coordinate on imager of top left corner of subwindow in pixels
  private long yOffset; // y coordinate on imager of top left corner of subwindow in pixels
  //private long width; // width in pixels of subwindow
  //private long height; // height in pixels of subwindow
  long width = 640;
  long height = 480;
  private long pixelFormat; // pixel format for data
  private int depth; // 8 or 16

  // Binning
  private short binningX; // binning ratio in x direction in pixels (x:1)
  private short flagsX; // LUCAM_FRAME_FORMAT_FLAGS_*
  private short binningY; // binning ratio in y direction in pixels (y:1)
  private short flagsY; // LUCAM_FRAME_FORMAT_FLAGS_*
  private boolean useStrobe; // for backward compatibility
  private long strobeFlags; // use LUCAM_PROP_FLAG_USE and/or LUCAM_PROP_FLAG_STROBE_FROM_START_OF_EXPOSURE
  private float strobeDelay; // time interval from when exposure starts to time the flash is fired in milliseconds
  private boolean useHwTrigger; // wait for hardware trigger
  private float timeout; // maximum time to wait for hardware trigger prior to returning from function in milliseconds
  private long shutterType;
  private float exposureDelay;
  private boolean bufferlastframe; // set to TRUE if you want TakeFastFrame to return an already received frame.
  int channel = 0;
  long wait = 50;
  InstrumentController instCtrl;
  //   public StreamSource getStreamSource() {
  //      sourceStream = new StreamSource((int)width, (int)height, this);
  //      if (sourceStream.imageArrayByte == null) {
  //         return null;
  //      }
  //      LuCamJNI.setDisplayArray8A(sourceStream.imageArrayByte);
  //      LuCamJNI.callBackSetupA(sourceStream);
  //      LuCamJNI.enableStreamingA(jniHandle);
  //      return sourceStream;
  //   }
  StreamSource source = null;
  boolean streaming = false;
  RandomStreamWorker worker;
  //XYStreamWorker worker;
  private int posX = 0;
  private int posY = 0;
  // Load the bigImage
  static BufferedImage bigImage = null;

  /**
   * Creates a new instance of Camera
   */
  public MockCamera() throws Exception {
//        try {
//            bigImage = ImageIO.read(new File(".\\bigStageImage.tif"));
//        } catch (Exception e) {
//            System.out.println("Exception loading: bigStageImage");
//            e.printStackTrace();
//        }
    if (bigImage == null) {
      bigImage = ImageFactoryGrayScale.testImageByte((int) width, (int) height);
    }
    if (false) {
      throw new Exception("Unable to intialize MockCamera");

    }
//        pStack = new PolStack(edu.mbl.jif.Constants.testDataPath +
//                "ps\\PS_Sythetic_15_2_3\\_PS_SynMagn15Swing_03Noise1_5-frameRaw.tif",
//                true);
//        bkgdStack = new PolStack(edu.mbl.jif.Constants.testDataPath +
//                "ps\\PS_Sythetic_15_2_3\\_BG_SynMagn15Swing_03Noise1_5-frameRaw.tif",
//                true);
  }

  //    public void run() {
  //        while (true) {
  //            QCamJNI.pixels8 = (byte[]) pStack.slice0;
  //            Camera.display.vPanel.callBack();
  //
  //            try {
  //                Thread.sleep(wait);
  //            } catch (InterruptedException ex) {
  //            }
  //
  //            QCamJNI.pixels8 = (byte[]) pStack.slice1;
  //            Camera.display.vPanel.callBack();
  //
  //            try {
  //                Thread.sleep(wait);
  //            } catch (InterruptedException ex) {
  //            }
  //
  //            QCamJNI.pixels8 = (byte[]) pStack.slice2;
  //            Camera.display.vPanel.callBack();
  //
  //            try {
  //                Thread.sleep(wait);
  //            } catch (InterruptedException ex) {
  //            }
  //
  //            QCamJNI.pixels8 = (byte[]) pStack.slice3;
  //            Camera.display.vPanel.callBack();
  //
  //            try {
  //                Thread.sleep(wait);
  //            } catch (InterruptedException ex) {
  //            }
  //
  //            QCamJNI.pixels8 = (byte[]) pStack.slice4;
  //            Camera.display.vPanel.callBack();
  //
  //            try {
  //                Thread.sleep(wait);
  //            } catch (InterruptedException ex) {
  //            }
  //        }
  //    }

  //    public static void main(String[] args) {
  //        int w = 201;
  //        int h = 181;
  //        Camera.initialize();
  //        Camera.width = w;
  //        Camera.height = h;
  //        QCamJNI.pixels8 = new byte[w * h];
  //        Camera.display = new FrameJCameraDisplay(1.0f);
  //        Camera.display.setVisible(true);
  //
  //        (new MockCamera(new InstrumentController())).run();
  //    }
  public boolean open() {
    return true;
  }

  public void setChannel(int channel) {
  }

  public int getChannel() {
    return -1;
  }

  public ArrayList getAvailFormats() {
    return null;
  }

  public void setDepth(int _depth) {
  }

  public int getDepth() {
    return 8;
  }

  public void setBinning(int _binning) {
  }

  public int getBinning() {
    return 1;
  }

  public int getWidth() {
    return (int) this.width;
  }

  public int getHeight() {
    return (int) this.height;
  }

  public void setExposureAuto(float target, float max) {
  }

  public void setROI(Rectangle roi) {
  }

  public Rectangle getROI() {
    return new Rectangle(0, 0, 0, 0);
  }

  public void clearROI() {
  }

  public boolean isRoiSet() {
    return true;
  }

  public double getCurrentFPS() {
    return -1.0;
  }

  public double getSpeed() {
    return -1.0;
  }

  public double getTemperature() {
    return -1.0;
  }

  public byte[] takeSnapshot8() {
    return null;
  }

  public short[] takeSnapshot16() {
    return null;
  }

  // Fast Acq -----------------------------------------------------
  public byte[] acqFast8() {
    return null;
  }

  public short[] acqFast16() {
    return null;
  }

  public void disableFastAcq() {
  }

  public boolean isBusy() {
    return true;
  }

  public void reset() {
  }

  public void abort() {
  }

  public void close() {
    this.closeStreamSource();
  }

  public void updateValues() {
  }

  public String getCameraState() {
    return null;
  }

  public String listAllParms() {
    return null;
  }

  public int pushSettings() {
    return 0;
  }

  public void enableFastAcq(int depth) {
  }

  public int acqFast8(byte[] imgArray) {
    int[] iArray = new int[(int) width * (int) height];
    ;
    bigImage.getData().getPixels(0, 0, (int) width, (int) height, iArray);//.getSubimage(0,0,width, height);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int offset = (y * (int) width) + x;
        //if (y == h) {
        imgArray[offset] = source.imageArrayByte[offset]; //(byte) iArray[offset];
//                        } else {
//                            pixels[(y * width) + x] = 0;
//                        }
      }
    }
    return 0;
  }

  public int acqFast16(short[] imgArray) {
    return 0;
  }

  public boolean isSameSetAcqStream() {
    return false;
  }

  public boolean isOpen() {
    return true;
  }

  public void setFormat(int fmt) {
  }

  public void setExposureStream(double _exposure) {
  }

  public double getExposureStream() {
    return 10.0;
  }

  public double getExposureStreamMax() {
    return 99999.0;
  }

  public double getExposureStreamMin() {
    return 0.001;
  }

  public void setExposureAcq(double _exposure) {
    this.exposureAcq = (float) _exposure;
  }

  public double getExposureAcq() {
    return exposureAcq;
  }

  public double getExposureAcqMax() {
    return 99999.9;
  }

  public double getExposureAcqMin() {
    return 0.0009;
  }

  public void setGainStream(double gain) {
  }

  public double getGainStream() {
    return 1.0;
  }

  public void setGainAcq(double gain) {
  }

  public double getGainAcq() {
    return 1.0;
  }

  public void setOffsetStream(double _offset) {
  }

  public double getOffsetStream() {
    return 111.0;
  }

  public void setOffsetAcq(double _offset) {
  }

  public double getOffsetAcq() {
    return 112.0;
  }

  public void setSpeed(double _speed) {
  }

  public void setCoolerActive(boolean onOff) {
  }

  public boolean isCoolerActive() {
    return false;
  }

  // Streaming ----------------------------------------------------
  public void openStreamSource() {
  }

  public boolean isStreaming() {
    return true;
  }

  public StreamSource getStreamSource() {
    source = new StreamSource((int) width, (int) height, this);
    if (source.imageArrayByte == null) {
      System.err.println("source.imageArrayByte == null");
      return null;
    }
    //worker = new XYStreamWorker((int) width, (int) height, source.imageArrayByte);
    //bigImage.getWidth(), bigImage.getHeight(),
    worker = new RandomStreamWorker((int) width, (int) height, source.imageArrayByte);
    worker.setPriority(Thread.currentThread().getPriority() - 1);
    worker.start();
    int ret = 0;
    //streamingEnabled = true;
    return source;
  }

  public void startStream() {
    if (streaming) {
      worker.safeResume();
      streaming = true;
    }
  }

  public void stopStream() {
    if (streaming) {
      worker.safeSuspend();
      streaming = false;
    }
  }

  public void closeStreamSource() {
    if (worker != null) {
      worker.safeStop();
    }
  }

  public String getDeviceName() {
    return "MockCamera";
  }

  public String getDeviceType() {
    return "deviceType";
  }

  public boolean isConnected() {
    return true;
  }

  public boolean isInitialized() {
    return true;
  }

  public void terminate() {
  }

  public boolean initialize() {
    return true;
  }

  public double getGainMultiplier() {
    return 1.0;
  }

  public double getExposureMultiplier() {
    return 1.0;
  }

  //--------------------------------------------------------------------------
  // Mock image stream generator...
  class RandomStreamWorker
          extends Thread {

    private int count;
    private volatile boolean done = false;
    private boolean suspended = false;
    int arrayLength;
    byte[] pixels;
    int[] iArray;
    int width;
    int height;
    long frames = 0;

//        int fps = 50;
//        private long delayMillis = 1000 / fps;
    public RandomStreamWorker(int width, int height, byte[] pixels) {
      super("MockCamera-Random");
      this.width = width;
      this.height = height;
      arrayLength = width * height;
      this.pixels = pixels;
      iArray = new int[width * height];
    }

    synchronized void safeStop() {
      System.out.println("MockCamera Worker stopped");
      done = true;
    }

    synchronized void safeSuspend() {
      System.out.println("suspended");
      suspended = true;
    }

    synchronized void safeResume() {
      System.out.println("resumed");
      suspended = false;
      notify();
    }

    synchronized boolean ok() {
      return (!done);
    }
    int h = 0;

    synchronized void doWork() {
      if (!suspended) {
        try {
          Thread.sleep((int) getExposureAcq());
        } catch (InterruptedException e) { /* die */

        }
        applyTransform(width, height, pixels);
        // Push out the new data
        frames++;
        source.callBack();
      } else {
        while (suspended) {
        }
      }
    }

    public void run() {
      while (ok()) {
        doWork();
      }
    }
    Random r = new Random();
    private double percent = 0.1;

    public int rand(int min, int max) {
      return min + (int) (r.nextDouble() * (max - min));
    }

    public byte[] applyTransform(int width, int height, byte[] pixels) {
      int n = (int) (percent * width * height);
      byte[] output = pixels;
      int rx, ry;
      int xmin = 0;
      int xmax = width;
      int ymin = 0;
      int ymax = height;
      byte value = (byte) rand(0, 255);
      for (int i = 0; i < n / 2; i++) {
        rx = rand(xmin, xmax);
        ry = rand(ymin, ymax);
        output[ry * width + rx] = value; //(byte) 254;
        rx = rand(xmin, xmax);
        ry = rand(ymin, ymax);
        output[ry * width + rx] = value; //(byte) 1;
      }
      return output;
    }
  }
  //
  // <editor-fold defaultstate="collapsed" desc="<<<  XYStage movement simulation... >>>">
  //--------------------------------------------------------------------------
  // Mock image stream generator...

  class XYStreamWorker
          extends Thread {

    private int count;
    private volatile boolean done = false;
    private boolean suspended = false;
    int arrayLength;
    byte[] pixels;
    int[] iArray;
    int width;
    int height;
    long frames = 0;

//        int fps = 50;
//        private long delayMillis = 1000 / fps;
    public XYStreamWorker(int width, int height, byte[] pixels) {
      super("MockCamera-XYStream");
      this.width = width;
      this.height = height;
      arrayLength = width * height;
      this.pixels = pixels;
      iArray = new int[width * height];
    }

    synchronized void safeStop() {
      System.out.println("MockCamera Worker stopped");
      done = true;
    }

    synchronized void safeSuspend() {
      System.out.println("suspended");
      suspended = true;
    }

    synchronized void safeResume() {
      System.out.println("resumed");
      suspended = false;
      notify();
    }

    synchronized boolean ok() {
      return (!done);
    }
    int h = 0;

    synchronized void doWork() {
      if (!suspended) {
        try {
          Thread.sleep((int) getExposureAcq());
        } catch (InterruptedException e) { /* die */

        }

        //Rectangle rect = new Rectangle(0,h,width, height);
        Graphics2D g = (Graphics2D) bigImage.getGraphics();
        g.drawImage(bigImage, width, height, null);
        g.drawString(String.valueOf(frames), 100, 100);
        bigImage.getData().getPixels(0, h, width, height, iArray);
        //bigImage.getSubimage(0,0,width, height);
        //source.imageArrayByte[]
        // applyTransform(width, height, pixels);
//                for (int x = 0; x < width; x++) {
//                    for (int y = 0; y < height; y++) {
//                        int offset = (y * width) + x;
//                        //if (y == h) {
//                        pixels[offset] = (byte) iArray[offset];
////                        } else {
////                            pixels[(y * width) + x] = 0;
////                        }
//                    }
//                }
//                h = h + 1;
//                if (h > bigImage.getHeight() - height) {
//                    h = 0;
//                }
        // Push out the new data
        frames++;

        source.callBack();
      } else {
        while (suspended) {
        }
      }
    }

    public void run() {
      while (ok()) {
        doWork();
      }

    }
  }

  public void setPosition(double x, double y) {
    double scale = 0.0001; // nm/
    posX = (int) (x * scale);
    posY = (int) (y * scale);
  // center of image
  // range of image
  // center of stage (8.0, 8.0)
  // get image from (posX, posY)
  }// </editor-fold>
}
