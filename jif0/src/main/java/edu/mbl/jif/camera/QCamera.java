package edu.mbl.jif.camera;


import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JFrame;


//
public class QCamera implements CameraInterface, StreamGenerator {
    public static int       err;
    public static boolean   driverLoaded       = false;
    public static final int CHANNEL_A          = 1;
    public static final int CHANNEL_B          = 2; // @todo Add B...
    private static String   state;
    long                    camNum             = -1;
    long                    jniHandle          = -1;
    boolean                 busy               = false;
    public static int       gainMultiplier     = 1000000;
    public static int       exposureMultiplier = 1000;
    
    //=======
    private boolean sameSetAcqStream  = false; // Streaming and Acq exposure are the same thing
    private float   exposureStream    = 10.0f;
    private float   exposureAcq       = 25.0f;
    private float   exposureStreamMin;
    private float   exposureStreamMax;
    private float   exposureAcqMin;
    private float   exposureAcqMax;
    private float   gainStream        = 100000.0f;
    private float   gainAcq           = 100000.0f;
    private float   offsetStream;
    private float   offsetAcq;
    
    // --- Format ---
    private float color_format;
    
    // ROI
    private long xOffset; // x coordinate on imager of top left corner of subwindow in pixels
    private long yOffset; // y coordinate on imager of top left corner of subwindow in pixels
    private long width; // width in pixels of subwindow
    private long height; // height in pixels of subwindow
    private long pixelFormat; // pixel format for data
    private long depth; // Mine... 8 or 16
    private long speed;
    
    // Binning
    private short   binningX; // binning ratio in x direction in pixels (x:1)
    private short   flagsX; // LUCAM_FRAME_FORMAT_FLAGS_*
    private short   binningY; // binning ratio in y direction in pixels (y:1)
    private short   flagsY; // LUCAM_FRAME_FORMAT_FLAGS_*
    // --- Snapshot settings ---
    private boolean useStrobe; // for backward compatibility
    private long    strobeFlags; // use LUCAM_PROP_FLAG_USE and/or LUCAM_PROP_FLAG_STROBE_FROM_START_OF_EXPOSURE
    private float   strobeDelay; // time interval from when exposure starts to time the flash is fired in milliseconds
    private boolean useHwTrigger; // wait for hardware trigger
    private float   timeout; // maximum time to wait for hardware trigger prior to returning from function in milliseconds
    private long    shutterType;
    private float   exposureDelay;
    private boolean bufferlastframe; // set to TRUE if you want TakeFastFrame to return an already received frame.
    private float   temperature;
    
    //   Unused
    //   private float trigger               ;
    //   private float frame_gate            ;
    //   private float exposure_interval     ;
    //   private float pwm                   ;
    //   private float brightness            ;
    //   private float contrast              ;
    //   private float hue                   ;
    //   private float saturation            ;
    //   private float sharpness             ;
    //   private float gamma                 ;
    //   private float auto_exp_target       ;
    //   private float auto_exp_maximum      ;
    //   private float timestamps            ;
    //   private float snapshot_clock_speed  ;
    
    //=====
    int                   channel           = 0;
    StreamSource          sourceA           = null;
    private boolean       streamingEnabledA = false;
    private boolean       streamingA        = false;
    StreamingDisplayPanel displayA          = null;
    JFrame                frameA            = null;
    private boolean       initialized       = false;
    private Rectangle     roi;
    
    //---------------------------------------------------------------
    // For up to 2 cameras, must specify channel
    public QCamera(int camNum, int channel) throws Exception {
        this.camNum = (long)camNum;
        setChannel(channel);
    }
    
    public boolean initialize() {
        // System.load(getClass().getResource("/QCamJNI.dll").toString());
        if(!QCamJNI.loadDLL()) {
            System.err.println("Camera load dll failed");
            return false;
        }
        if (!loadCameraDriver()) {
            return false;
        }
        boolean opened = QCamJNI.openCameraStreaming();
        if (!opened) {
            System.err.println("Camera open Failed");
            return false;
        }
        //setDefaults();
        getCameraState();
        return true;
    }
    
    
    // (default, persisted, min, max)
    public String getCameraState() {
        //int ret = QCamJNI.getProperties(jniHandle, this);
        QCamJNI.getParameters();
        QCamJNI.getInfo();
        QCamJNI.getExposureMinMax();
        System.out.println(QCamJNI.showCameraInfo());
        System.out.println(QCamJNI.showCameraState());
        //size           = QCamJNI.imageSize;
        width = QCamJNI.imageWidth;
        height = QCamJNI.imageHeight;
        // Camera Settings
        speed = QCamJNI.readOutSpeed;
//              if (QCamJNI.exposure >= 0) {
//                  exposureStream = QCamJNI.exposure;
//              }
//              if (QCamJNI.gain >= 0) {
//                  gainStream = QCamJNI.gain;
//              }
//              if (QCamJNI.offset >= 0) {
//                  offsetStream = QCamJNI.offset;
//              }
        exposureStreamMin = QCamJNI.exposureMin;
        exposureStreamMax = QCamJNI.exposureMax;
        exposureAcqMin = QCamJNI.exposureMin;
        exposureAcqMax = QCamJNI.exposureMax;
        
        // ?? format         = QCamJNI.imageFormat;
        //depth = QCamJNI.depth;
        //
        binningX = (short)QCamJNI.binning;
        binningY = binningX;
        
        // Gotta deal with this...
        roi = new Rectangle(
                (int)QCamJNI.roiX,
                (int)QCamJNI.roiY,
                (int)QCamJNI.roiWidth,
                (int)QCamJNI.roiHeight);
        return null;
    }
    
    
    //   public int initSettings(int depth, float exposure, float gain) {
    //      if (jniHandle != -1) {
    //         this.depth = depth;
    //         this.exposureAcq = exposure;
    //         this.gainAcq = gain;
    //         return QCamJNI.setSnapshotSettings(jniHandle, depth, exposureAcq, gainAcq);
    //      } else {
    //         return -1;
    //      }
    //   }
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
                System.out.println(
                        "Camera Error: Unable to load driver: " + String.valueOf(err));
                return false;
            }
        }
        return true;
    }
    
    
    public static void unLoadCameraDriver() {
//        if (state != "closed") {
//            QCamJNI.closeTheCamera();
//            state = "closed";
//        }
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
        return false;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    // Close the camera
    public static void closeCamera() {
        //closeDisplayFrame();
        if (state != "closed") {
            // if (binning != 2) { setBinning(2); }
            QCamJNI.closeTheCamera();

        }
        state = "closed";
        //isOpen    = false;
        //System.out.println("Camera closed.");
    }
    
    
    public boolean isOpen() {
        return jniHandle > 0;
    }
    
    
    public String getDeviceType() { // device Type
        return "Camera-RetigaEX";
    }
    
    
    //==========================================================
    public boolean open() {
        return true;
    }
    
    
    public void close() {
        System.out.println("closing (QCamera)");
        closeStreamSourceA();
        //closeStreamSourceB();
        QCamJNI.closeTheCamera();
        unLoadCameraDriver();
    }
    
    
    //   void updateSnapshotSettings() {
    //      QCamJNI.setSnapshotSettings(jniHandle, depth, exposureAcq, gainAcq);
    //   }
    //    void updateStreamSettings() {
    //        // if binning changed?
    //        // if roi changed?
    //        QCamJNI.setExposure((long)(exposureStream),
    //                (long)(gainStream),
    //                (long)(offsetStream));
    //    }
    public ArrayList getAvailFormats() {
        return null;
    }
    
    
    public int getWidth() {
        return (int)width;
    }
    
    
    public int getHeight() {
        return (int)height;
    }
    
    
    public void setFormat(int fmt) {
        System.err.println("setFormat() not implemented");
    }
    
    
    public void setDepth(int _depth) {
        if (_depth == 12) {
            _depth = 16;
        }
        if ((_depth != 8) && (_depth != 16)) {
            System.err.println("Attempt to set depth to other than 8 or 16");
            return;
        }
        this.depth = _depth;
        if (depth == 8) {
            QCamJNI.setFormat(2); // 2:8-bit
        } else {
            QCamJNI.setFormat(3); // 3:12-bit
        }
    }
    
    
    public int getDepth() {
        return (int)depth;
    }
    
    
    public void setBinning(int _binning) {
        QCamJNI.disposeImageArray();
        int err = QCamJNI.setBinning((long)_binning);
        QCamJNI.setImageArray();
    }
    
    
    public int getBinning() {
        return 1;
    }
    
    
    public boolean isSameSetAcqStream() {
        return sameSetAcqStream;
    }
    
    
    public double getGainMultiplier() {
        return gainMultiplier;
    }
    
    
    public double getExposureMultiplier() {
        return exposureMultiplier;
    }
    
    
    // nanoseconds
    public void setExposureStream(double _exposure) {
        if (_exposure < this.exposureStreamMax) {
            exposureStream = (float)_exposure;
            System.out.println("setExp: " +
                    (long)(exposureStream) + " " +
                    (long)(gainStream) + " " +
                    (long)offsetStream);
            if (!isStreaming()) {
                int err =
                        (int)QCamJNI.setExposure(
                        (long)(exposureStream),
                        (long)(gainStream),
                        (long)offsetStream);
            } else {
                int err =
                        (int)QCamJNI.queueExposureSet(
                        (long)(exposureStream),
                        (long)(gainStream),
                        (long)offsetStream);
                System.out.println("setExposure err: " + err);
            }
        }
    }
    
    
    public double getExposureStream() {
        return exposureStream;
    }
    
    
    public double getExposureStreamMax() {
        return exposureStreamMax;
    }
    
    
    public double getExposureStreamMin() {
        return exposureStreamMin;
    }
    
    
    public void setExposureAcq(double _exposure) {
        exposureAcq = (float)_exposure;
        //updateSnapshotSettings();
    }
    
    
    public double getExposureAcq() {
        return exposureAcq;
    }
    
    
    public double getExposureAcqMax() {
        return exposureAcqMax;
    }
    
    
    public double getExposureAcqMin() {
        return exposureAcqMin;
    }
    
    
    public void setGainStream(double gain) {
        gainStream = (float)gain;
        System.out.println("setGain: " +
                (long)(exposureStream) + " " +
                (long)(gainStream) + " " +
                (long)offsetStream);
        if (!isStreaming()) {
            int err =
                    (int)QCamJNI.setExposure(
                    (long)(exposureStream),
                    (long)(gainStream),
                    (long)offsetStream);
        } else {
            int err =
                    (int)QCamJNI.queueExposureSet(
                    (long)(exposureStream),
                    (long)(gainStream),
                    (long)offsetStream);
            System.out.println("setExposure err: " + err);
        }
    }
    
    
    public double getGainStream() {
        return gainStream;
    }
    
    
    public void setGainAcq(double gain) {
        gainAcq = (float)gain;
        // updateSnapshotSettings();
    }
    
    
    public double getGainAcq() {
        return gainAcq;
    }
    
    
    public void setOffsetStream(double _offset) {
        offsetStream = (float)_offset;
        if (!isStreaming()) {
            int err =
                    (int)QCamJNI.setExposure(
                    (long)(exposureStream),
                    (long)(gainStream),
                    (long)offsetStream);
        } else {
            int err =
                    (int)QCamJNI.queueExposureSet(
                    (long)(exposureStream),
                    (long)(gainStream),
                    (long)offsetStream);
            System.out.println("setExposure err: " + err);
        }
    }
    
    
    public double getOffsetStream() {
        return offsetStream;
    }
    
    
    public void setOffsetAcq(double _offset) {
        offsetAcq = (float)_offset;
    }
    
    
    public double getOffsetAcq() {
        return offsetAcq;
    }
    
    
    // --- ROI ---
    // roi(0,0,0,0) clears ROI setting
    public void setROI(Rectangle roi) {
        if (roi.getWidth() == 0) { // clears ROI setting
            setROIFull();
        } else {
            int err = -1;
            err =
                    (int)QCamJNI.setROI(
                    (long)roi.getX(),
                    (long)roi.getY(),
                    (long)roi.getWidth(),
                    (long)roi.getHeight());
        }
        getCameraState();
    }
    
    
    public Rectangle getROI() {
        return null;
    }
    
    
    public void clearROI() {
    }
    
    
    public boolean isRoiSet() {
        return true;
    }
    
    
    public static int setROIFull() {
        // Clear the CameraROI - reset to full frame
        int err = -1;
        err = (int)QCamJNI.setROIFull();
        System.out.println("Camera ROI set full [" + err + "]");
        return err;
    }
    
    
    
    public void setSpeed(double _speed) {
        int speed = 0;
        if (_speed == 20.0) {
            speed = 0; // 20MHz
        } else if (_speed == 10.0) {
            speed = 1; // 10MHz
        } else if (_speed == 5.0) {
            speed = 2; // 5MHz
        } else if (_speed == 2.5) {
            speed = 3; // 2.5MHz
        }
        QCamJNI.setReadoutSpeed(speed);
    }
    
    
    public double getSpeed() {
        QCamJNI.getParameters();
        long speed = QCamJNI.readOutSpeed;
        if (speed == 0) {
            return 20.0; // 20MHz
        }
        if (speed == 1) {
            return 10.0; // 10MHz
        }
        if (speed == 2) {
            return 5.0; // 5MHz
        }
        if (speed == 3) {
            return 2.5; // 2.5MHz
        }
        return 0.0;
    }
    
    
    public double getTemperature() {
        return -1f;
    }
    
    
    public void setCoolerActive(boolean onOff) {
        QCamJNI.setCooler(onOff);
        return;
    }
    
    
    public boolean isCoolerActive() {
        if (QCamJNI.coolerActive == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    
    // Else -------------------------------------------------------------------
    public int pushSettings() {
        return 0;
    }
    
    
    public String listAllParms() {
        getCameraState();
        String out =
                "CameraInterface Settings\n\n" + "driverLoaded: " + driverLoaded + "\n"
                + "width: " + width + "\n" + "height: " + height + "\n" + "depth: " + depth
                + "\n" + "speed: " + speed + "\n" + "exposureSteam: " + exposureStream + "\n"
                + "gainStream: " + gainStream + "\n" + "offsetStream: " + offsetStream + "\n"
                + "exposureAcq: " + exposureAcq + "\n" + "gainAcq: " + gainAcq + "\n"
                + "offsetAcq: " + offsetAcq + "\n" // "exposureMin: " + exposureMin + "\n" + "exposureMax: " + exposureMax + "\n" +
                //      "format: " +  format + "\n"
                + "binning: " + binningX + "\n" + "coolerActive: " + isCoolerActive() + "\n"
                + "roi: " + roi + "\n" + "\n";
        System.out.println(out);
        return out;
    }
    
    
    // Snapshot --------------------------------------------------------
    public byte[] takeSnapshot8() {
        // if (depth == 8) {
        byte[] ret = QCamJNI.takeSnapshot8(jniHandle);
        return ret;
        //}
        //return null;
    }
    
    
    public short[] takeSnapshot16() {
        final byte[] imageArrayB  = QCamJNI.takeSnapshot16b(jniHandle);
        short[]      imageArray16 = new short[imageArrayB.length / 2];
        for (int i = 0; i < imageArrayB.length; i += 2) {
            imageArray16[i / 2] = (short)(((imageArrayB[i] & 0xff) << 8)
            | (imageArrayB[i + 1] & 0xff));
        }
        return imageArray16;
    }
    
    
    // Fast Acquistition ---------------------------------------------------
    public void enableFastAcq(int depth) {
        // if(isStreaming) disableStreaming();
        QCamJNI.enableFastSnapshots(jniHandle);
    }
    
    
    public byte[] acqFast8() {
        //if (depth == 8) {
        byte[] ret = QCamJNI.takeFastFrame8(jniHandle);
        return ret;
        //}
        //return null;
    }
    
    
    public int acqFast8(byte[] imgArray) {
        QCamJNI.takeFastFrame8j(jniHandle, imgArray);
        return 0;
    }
    
    
    public short[] acqFast16() {
        final byte[] imageArrayB  = QCamJNI.takeFastFrame16(jniHandle);
        short[]      imageArray16 = new short[imageArrayB.length / 2];
        for (int i = 0; i < imageArrayB.length; i += 2) {
            imageArray16[i / 2] = (short)(((imageArrayB[i] & 0xff) << 8)
            | (imageArrayB[i + 1] & 0xff));
        }
        return imageArray16;
    }
    
    
    public int acqFast16(short[] imageArray16) {
        final byte[] imageArrayB = QCamJNI.takeFastFrame16(jniHandle);
        if (imageArray16.length != (imageArrayB.length / 2)) {
            System.err.println("Houston, we have a problem.");
            return -1;
        }
        for (int i = 0; i < imageArrayB.length; i += 2) {
            imageArray16[i / 2] = (short)(((imageArrayB[i] & 0xff) << 8)
            | (imageArrayB[i + 1] & 0xff));
        }
        return 0;
    }
    
    
    public void disableFastAcq() {
        QCamJNI.disableFastSnapshots(jniHandle);
    }
    
    
    // Streaming ------------------------------------------------------------------
    //  If only ONE Camera...
    public StreamSource getStreamSource() {
        if (channel == this.CHANNEL_A) {
            return getStreamSourceA();
        } else {
            return getStreamSourceB();
        }
    }
    
    
    public void startStream() {
        if (channel == this.CHANNEL_A) {
            startStreamA();
        } else {
            //startStreamA();
        }
    }
    
    
    public void stopStream() {
        if (channel == this.CHANNEL_A) {
            stopStreamA();
        }
    }
    
    
    public void closeStreamSource() {
        if (channel == this.CHANNEL_A) {
            closeStreamSourceA();
        }
    }
    
    
    public boolean isStreamingEnabled() {
        if (channel == this.CHANNEL_A) {
            return isStreamingEnabledA();
        } else {
            return false;
        }
    }
    
    
    public boolean isStreaming() {
        if (channel == this.CHANNEL_A) {
            return isStreamingA();
        } else {
            return false;
        }
    }
    
    
    //======================================
    // Channel-Specific
    // -------------------------------------
    public int getChannel() {
        return channel;
    }
    
    
    public void setChannel(int channel) {
        this.channel = channel;
    }
    
    
    // Channel A ------------------------
    public StreamSource getStreamSourceA() {
        //if(sourceA == null) {
            sourceA = new StreamSource((int)width, (int)height, this);
            if (sourceA.imageArrayByte == null) {
                System.err.println("sourceA.imageArrayByte == null");
                return null;
            }
        //}
        int ret = 0;
        QCamJNI.setImageArray8(sourceA.imageArrayByte);
        QCamJNI.callBackSetup(sourceA);
        
        ret = QCamJNI.enableStreaming(jniHandle);
        //System.out.println("enableStreamingA: " + ret);
        streamingEnabledA = true;
        return sourceA;
    }
    
    
    public void startStreamA() {
        // if(isStreamingEnabled()...)
        QCamJNI.startStreaming(jniHandle);
        streamingA = true;
    }
    
    
    public void stopStreamA() {
        if (isStreamingA()) {
            QCamJNI.stopStreaming(jniHandle);
            streamingA = false;
        }
    }
    
    
    public void closeStreamSourceA() {
        // close the frame
        if (isStreamingA()) {
            stopStreamA();
        }
        if (isStreamingEnabledA()) {
            QCamJNI.disableStreaming(jniHandle);
            streamingEnabledA = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            QCamJNI.disposeArrays8();
            // ... callback remains...
        }
    }
    
    
    public boolean isStreamingEnabledA() {
        return streamingEnabledA;
    }
    
    
    public boolean isStreamingA() {
        return streamingA;
    }
    
    
    //====================================================================
    // Testing...
    public void openDisplayA(StreamSource sourceA) {
        if (isStreamingEnabledA()) {
            displayA = new StreamingDisplayPanel(sourceA, (int)width, (int)height);
            frameA = new JFrame();
            frameA.add(displayA);
            frameA.setPreferredSize(new Dimension((int)width + 10, (int)height + 30));
            frameA.setLocation(200, 100);
            frameA.setTitle("Camera A (# " + camNum + ")");
            frameA.pack();
            frameA.setVisible(true);
        } else {
            System.err.println("Tried to open DisplayA without StreamingEnabled.");
        }
    }
    
    
    public void closeDisplayA() {
        if (isStreamingA()) {
            stopStreamA();
        }
        frameA.setVisible(false);
        displayA = null;
        frameA = null;
    }
    
    
    //=========================================================
    // Channel B ------------------------
    // @todo Need to repeat A for B...
    public void openDisplayB() {
        //      TryMemSrcThread displayPanelB = new TryMemSrcThread(640, 480);
        //      JFrame f = new JFrame();
        //      f.add(displayPanelB);
        //      f.setPreferredSize(new Dimension(670, 500));
        //      f.setLocation(400, 300);
        //      f.setTitle("Camera B (# " + camNum + ")");
        //      f.pack();
        //      f.setVisible(true);
        //      QCamJNI.setDisplayArray8B(displayPanelB.imageArrayByte);
        //      QCamJNI.callBackSetupB(displayPanelB);
        //      QCamJNI.enableStreamingB(jniHandle);
        //      QCamJNI.startStreamingB(jniHandle);
    }
    
    
    private StreamSource getStreamSourceB() {
        return null;
    }
    
    
    // ===  Device implementation  ==================================
    public boolean isBusy() {
        return false;
    }
    
    
    public String getDeviceName() {
        return "QCamera";
    }
    
    
    public boolean isConnected() {
        return true; // if this instance exists, must be connected
    }
    
    
    public boolean isInitialized() {
        return initialized;
    }
    
    
    public void reset() {
        // ??
    }
    
    
    public void terminate() {
        // shut down...
        close();
    }

}
