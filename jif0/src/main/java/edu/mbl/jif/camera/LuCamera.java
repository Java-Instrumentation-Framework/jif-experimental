package edu.mbl.jif.camera;

import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JFrame;

/* Lumenera camera implementation of CameraInterface
 *
 */
public class LuCamera implements CameraInterface, StreamGenerator {

    static final int CHANNEL_A = 1;
    static final int CHANNEL_B = 2; // @todo Add B...
    long camNum = -1;
    long jniHandle = -1;
    boolean busy = false;    //=======
    private boolean sameSetAcqStream = false; // Streaming and Acq exposure are the same thing
    private float exposureStream = 10.0f;
    private float exposureAcq = 25.0f;
    private float exposureStreamMin;
    private float exposureStreamMax;
    private float exposureAcqMin;
    private float exposureAcqMax;
    private float gainStream = 1.0f;
    private float gainAcq = 1.0f;
    private float offsetStream;
    private float offsetAcq;    // --- Format ---
    private float color_format;    // ROI
    private long xOffset; // x coordinate on imager of top left corner of subwindow in pixels
    private long yOffset; // y coordinate on imager of top left corner of subwindow in pixels
    private long width; // width in pixels of subwindow
    private long height; // height in pixels of subwindow
    private long pixelFormat; // pixel format for data
    private long depth; // Mine... 8 or 16
    // Binning
    private short binningX; // binning ratio in x direction in pixels (x:1)
    private short flagsX; // LUCAM_FRAME_FORMAT_FLAGS_*
    private short binningY; // binning ratio in y direction in pixels (y:1)
    private short flagsY; // LUCAM_FRAME_FORMAT_FLAGS_*
    // --- Snapshot settings ---
    private boolean useStrobe; // for backward compatibility
    private long strobeFlags; // use LUCAM_PROP_FLAG_USE and/or LUCAM_PROP_FLAG_STROBE_FROM_START_OF_EXPOSURE
    private float strobeDelay; // time interval from when exposure starts to time the flash is fired in milliseconds
    private boolean useHwTrigger; // wait for hardware trigger
    private float timeout; // maximum time to wait for hardware trigger prior to returning from function in milliseconds
    private long shutterType;
    private float exposureDelay;
    private boolean bufferlastframe; // set to TRUE if you want TakeFastFrame to return an already received frame.
    private float temperature;    //   Unused
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
    int channel = 0;
    StreamSource sourceA = null;
    private boolean streamingEnabledA = false;
    private boolean streamingA = false;
    StreamingDisplayPanel displayA = null;
    JFrame frameA = null;
    private boolean initialized = false;

    //---------------------------------------------------------------
    // For up to 2 cameras, must specify channel
    public LuCamera(int camNum, int channel) throws Exception {
        this(camNum);
        setChannel(channel);
    }

    // For single camera
    public LuCamera(int camNum) throws Exception {
        this.camNum = (long) camNum;
        setChannel(CHANNEL_A);
    }

    public boolean isOpen() {
        return jniHandle > 0;
    }
    //==========================================================
    public boolean open() {
        return true;
    }

    public boolean initialize() {
        if (!LuCamJNI.loadDLL()) {
            System.err.println("Camera load dll failed");
            return false;
        }
        jniHandle = LuCamJNI.openCamera(camNum);
        if (jniHandle == -1) {
            System.out.println("Could not open camera: " + camNum);
            return false;
        }
        System.out.println("Opened Camera: " + camNum + "(jniHandle: " + jniHandle + ")");
        getCameraState();
        setExposureStream(exposureStream);
        setGainStream(gainStream);
        setDepth(8);
        initialized = true;
        return true;
    }

    //   public int initSettings(int depth, float exposure, float gain) {
    //      if (jniHandle != -1) {
    //         this.depth = depth;
    //         this.exposureAcq = exposure;
    //         this.gainAcq = gain;
    //         return LuCamJNI.setSnapshotSettings(jniHandle, depth, exposureAcq, gainAcq);
    //      } else {
    //         return -1;
    //      }
    //   }
    public void close() {
        System.out.println("closing (LuCamera)");
        closeStreamSourceA();
        //closeStreamSourceB();
        LuCamJNI.closeCamera(jniHandle);
    }

    void updateSnapshotSettings() {
        LuCamJNI.setSnapshotSettings(jniHandle, depth, exposureAcq, gainAcq);
    }

//    void updateStreamSettings() {
//        LuCamJNI.setExposureGain(jniHandle, (long) depth, exposureStream,
//            gainStream, 0);
//    }

    public ArrayList getAvailFormats() {
        return null;
    }

    public int getWidth() {
        return (int) width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setFormat(int fmt) {
        System.err.println("setFormat() not implemented");
    }

    public void setDepth(int _depth) {
        if ((_depth != 8) && (_depth != 16)) {
            System.err.println("Attempt to set depth to other than 8 or 16");
            return;
        }
        this.depth = _depth;
        updateSnapshotSettings();
    }

    public int getDepth() {
        return (int) depth;
    }

    public void setBinning(int _binning) {
        // not implemented
    }

    public int getBinning() {
        return 1;
    }

    public boolean isSameSetAcqStream() {
        return sameSetAcqStream;
    }

    public void setExposureStream(double _exposure) {
        if (_exposure < this.exposureStreamMax) {
            exposureStream = (float) _exposure;
            float target = 1.0f;
            int result = LuCamJNI.setExposureGain(jniHandle, (long) depth,
                exposureStream, gainStream, target);
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
        exposureAcq = (float) _exposure;
        updateSnapshotSettings();
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
        gainStream = (float) gain;
        float target = 1.0f;
        int result = LuCamJNI.setExposureGain(jniHandle, (long) depth,
            exposureStream, gainStream, target);
        //updateStreamSettings();
    }

    public double getGainStream() {
        return gainStream;
    }

    public void setGainAcq(double gain) {
        gainAcq = (float) gain;
        updateSnapshotSettings();
    }

    public double getGainAcq() {
        return gainAcq;
    }

    public void setOffsetStream(double _offset) {
        offsetStream = (float) _offset;
    }

    public double getOffsetStream() {
        return offsetStream;
    }

    public void setOffsetAcq(double _offset) {
        offsetAcq = (float) _offset;
    }

    public double getOffsetAcq() {
        return offsetAcq;
    }

    // --- ROI ---
    public void setROI(Rectangle roi) {
        // not implemented
    }

    public Rectangle getROI() {
        return null;
    }

    public void clearROI() {
    }

    public boolean isRoiSet() {
        return true;
    }

    public void setSpeed(double _speed) {
    }

    public double getSpeed() {
        return -1f;
    }

    public void setCooler(boolean onOff) {
    }

    public boolean getCooler() {
        return false;
    }

    public double getTemperature() {
        return -1f;
    }

    public void setCoolerActive(boolean onOff) {
        return;
    }

    public boolean isCoolerActive() {
        return false;
    }

    // Else -------------------------------------------------------------------
    public boolean isBusy() {
        return false;
    }

    public void reset() {
    }

    public void abort() {
    }

    public int pushSettings() {
        return 0;
    }

    public String getCameraState() {
        System.out.println("Getting camera state");
        int ret = LuCamJNI.getProperties(jniHandle, this);
        exposureAcqMin = exposureStreamMin;
        exposureAcqMax = exposureStreamMax;

        System.out.println("LuCam Settings: ");
        System.out.println("Acq Settings\nexposure, gain, width, height, depth");
        System.out.println(exposureAcq + ",   " + gainAcq + ",  " + width +
            ", " + height + ", " + depth);
        System.out.println("Streaming Settings\nexposure, gain, width, height, depth");
        System.out.println(exposureStream + ",   " + gainStream + ",  " +
            width + ", " + height + ", " + depth);
        System.out.print("Min/Max Exposure: ");
        System.out.println(this.exposureStreamMin + " / " + this.exposureStreamMax);
        return null;
    }

    public String listAllParms() {
        return null;
    }

    // Snapshot --------------------------------------------------------
    public byte[] takeSnapshot8() {
        // if (depth == 8) {
        byte[] ret = LuCamJNI.takeSnapshot8(jniHandle);
        return ret;
    //}
    //return null;
    }

    public short[] takeSnapshot16() {
        final byte[] imageArrayB = LuCamJNI.takeSnapshot16b(jniHandle);
        short[] imageArray16 = new short[imageArrayB.length / 2];
        for (int i = 0; i < imageArrayB.length; i += 2) {
            imageArray16[i / 2] = (short) (((imageArrayB[i] & 0xff) << 8) |
                (imageArrayB[i + 1] & 0xff));
        }
        return imageArray16;
    }

    // Fast Acquistition ---------------------------------------------------
    public void enableFastAcq(int depth) {
        // if(isStreaming) disableStreaming();
        LuCamJNI.enableFastSnapshots(jniHandle);
    }

    public byte[] acqFast8() {
        //if (depth == 8) {
        byte[] ret = LuCamJNI.takeFastFrame8(jniHandle);
        return ret;
    //}
    //return null;
    }

    public int acqFast8(byte[] imgArray) {
        LuCamJNI.takeFastFrame8j(jniHandle, imgArray);
        return 0;
    }

    public short[] acqFast16() {
        final byte[] imageArrayB = LuCamJNI.takeFastFrame16(jniHandle);
        short[] imageArray16 = new short[imageArrayB.length / 2];
        for (int i = 0; i < imageArrayB.length; i += 2) {
            imageArray16[i / 2] = (short) (((imageArrayB[i] & 0xff) << 8) |
                (imageArrayB[i + 1] & 0xff));
        }
        return imageArray16;
    }

    public int acqFast16(short[] imageArray16) {
        final byte[] imageArrayB = LuCamJNI.takeFastFrame16(jniHandle);
        if (imageArray16.length != (imageArrayB.length / 2)) {
            System.err.println("Houston, we have a problem.");
            return -1;
        }
        for (int i = 0; i < imageArrayB.length; i += 2) {
            imageArray16[i / 2] = (short) (((imageArrayB[i] & 0xff) << 8) |
                (imageArrayB[i + 1] & 0xff));
        }
        return 0;
    }

    public void disableFastAcq() {
        LuCamJNI.disableFastSnapshots(jniHandle);
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
        sourceA = new StreamSource((int) width, (int) height, this);

        if (sourceA.imageArrayByte == null) {
            System.err.println("sourceA.imageArrayByte == null");
            return null;
        }
        int ret = 0;
        LuCamJNI.setDisplayArray8A(sourceA.imageArrayByte);
        LuCamJNI.callBackSetupA(sourceA);
        ret = LuCamJNI.enableStreamingA(jniHandle);
        //System.out.println("enableStreamingA: " + ret);
        streamingEnabledA = true;
        return sourceA;
    }

    public void startStreamA() {
        LuCamJNI.startStreamingA(jniHandle);
        streamingA = true;
    }

    public void stopStreamA() {
        if (isStreamingA()) {
            LuCamJNI.stopStreamingA(jniHandle);
            streamingA = false;
        }
    }

    public void closeStreamSourceA() {
        // close the frame
        if (isStreamingA()) {
            stopStreamA();
        }
        if (isStreamingEnabledA()) {
            LuCamJNI.disableStreamingA(jniHandle);
            streamingEnabledA = false;
            LuCamJNI.disposeArrays8A();
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
            displayA = new StreamingDisplayPanel(sourceA, (int) width,
                (int) height);
            frameA = new JFrame();
            frameA.add(displayA);
            frameA.setPreferredSize(new Dimension((int) width + 10,
                (int) height + 30));
            frameA.setLocation(200, 100);
            frameA.setTitle("Camera A (# " + camNum + ")");
            frameA.pack();
            frameA.setVisible(true);
        } else {
            System.err.println(
                "Tried to open DisplayA without StreamingEnabled.");
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
        //      LuCamJNI.setDisplayArray8B(displayPanelB.imageArrayByte);
        //      LuCamJNI.callBackSetupB(displayPanelB);
        //      LuCamJNI.enableStreamingB(jniHandle);
        //      LuCamJNI.startStreamingB(jniHandle);
    }

    private StreamSource getStreamSourceB() {
        return null;
    }

    public String getDeviceType() { // Camera Type
        return "Camera - Lumenera";
    }

    @Override
    public String getDeviceName() {
        return "LuCamera";
    }

    public boolean isConnected() {
        return true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void terminate() {
        close();
    }

    public double getGainMultiplier() {
        return 1.0;
    }

    public double getExposureMultiplier() {
        return 1.0;
    }

}
