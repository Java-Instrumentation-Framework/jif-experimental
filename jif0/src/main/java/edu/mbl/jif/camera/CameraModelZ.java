/*
 * CameraModelZ.java
 * Created on July 10, 2006, 1:11 PM
 */
package edu.mbl.jif.camera;

import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.utils.FileUtil;


import java.awt.Rectangle;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ListModel;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import edu.mbl.jif.utils.prefs.Prefs;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;


/**
 * This is unused, except in test with PropertySheet  (2012)
 * @author GBH
 * Made with jif.beans.BeanMaker.java
 */
public class CameraModelZ extends Model { //implements CameraInterface {
    // loadDriver();
    //
//   public static final String PROPERTYNAME_DRIVERLOADED = "driverLoaded";
    public static final String PROPERTYNAME_OPEN = "open";
    public static final String PROPERTYNAME_BUSY = "busy";
    public static final String PROPERTYNAME_WIDTH = "width";
    public static final String PROPERTYNAME_HEIGHT = "height";
    public static final String PROPERTYNAME_FORMAT = "format";
    public static final String PROPERTYNAME_DEPTH = "depth";
    public static final String PROPERTYNAME_BINNINGX = "binningX";
    public static final String PROPERTYNAME_BINNINGY = "binningY";
    public static final String PROPERTYNAME_SAMEXYBINNING = "sameXYBinning";
    public static final String PROPERTYNAME_SPEED = "speed";
    public static final String PROPERTYNAME_ROISET = "roiSet";
    public static final String PROPERTYNAME_ROIX = "roiX";
    public static final String PROPERTYNAME_ROIY = "roiY";
    public static final String PROPERTYNAME_ROIW = "roiW";
    public static final String PROPERTYNAME_ROIH = "roiH";
    public static final String PROPERTYNAME_SAMESETACQSTREAM = "sameSetAcqStream";
    public static final String PROPERTYNAME_EXPOSURESTREAM = "exposureStream";
    public static final String PROPERTYNAME_EXPOSURESTREAMMIN = "exposureStreamMin";
    public static final String PROPERTYNAME_EXPOSURESTREAMMAX = "exposureStreamMax";
    public static final String PROPERTYNAME_EXPOSUREACQ = "exposureAcq";
    public static final String PROPERTYNAME_GAINACQ = "gainAcq";
    public static final String PROPERTYNAME_OFFSETACQ = "offsetAcq";
    public static final String PROPERTYNAME_MIRRORIMAGE = "mirrorImage"; // display
    public static final String PROPERTYNAME_EXPOSUREACQMIN = "exposureAcqMin";
    public static final String PROPERTYNAME_EXPOSUREACQMAX = "exposureAcqMax";
    public static final String PROPERTYNAME_GAINSTREAM = "gainStream";
    public static final String PROPERTYNAME_OFFSETSTREAM = "offsetStream";
    public static final String PROPERTYNAME_ADJUSTEXPOSUREFORBINNING = "adjustExposureForBinning";
    
    // @todo add
    public static final String PROPERTYNAME_COOLERACTIVE = "coolerActive";
    public static final String PROPERTYNAME_TEMPERATURE = "temperature";
    public static final String PROPERTYNAME_TRIGGERTYPE = "triggerType";
    
    //public static final String PROPERTYNAME_STREAMSOURCE = "streamSource";
    public static final String PROPERTYNAME_STREAMINGENABLED = "streamingEnabled";
    public static final String PROPERTYNAME_STREAMING = "streaming";
    public static final String PROPERTYNAME_CHANNEL = "channel";
    public static final String PROPERTYNAME_CURRENTFPS = "currentFPS";
    public static final String PROPERTYNAME_ACTIVECAMERASETTINGS = "activeCameraSettings";
    
    // An int based enumeration for binning options
    public static final Integer BIN_1_INTEGER = new Integer(1);
    public static final Integer BIN_2_INTEGER = new Integer(2);
    public static final Integer BIN_3_INTEGER = new Integer(3);
    public static final List    BINNING_OPTIONS = Arrays.asList(new Integer[] {
        BIN_1_INTEGER, BIN_2_INTEGER, BIN_3_INTEGER
    });
    public static final Double  SPEED_20 = new Double(20.0);
    public static final Double  SPEED_10 = new Double(10.0);
    public static final Double  SPEED_5 = new Double(5.0);
    public static final Double  SPEED_2_5 = new Double(2.5);
    public static final List    SPEED_OPTIONS = Arrays.asList(new Double[] {
        SPEED_20, SPEED_10, SPEED_5, SPEED_2_5
    });
    
    //new String[] { "20 MHz", "10 MHz", "5 MHz", "2.5 MHz" });
    //private boolean        driverLoaded;
    private boolean        open;
    private boolean        busy;
    private int            width;
    private int            height;
    private int            format;
    private int            depth;
    private int            binningX;
    private int            binningY;
    private boolean        sameXYBinning; //
    private double         speed;
    private boolean        roiSet;
    private int            roiX;
    private int            roiY;
    private int            roiW;
    private int            roiH;
    private boolean        sameSetAcqStream;
    private double         exposureStream;
    private double         exposureStreamMin;
    private double         exposureStreamMax;
    private double         exposureAcq;
    private double         exposureAcqMin;
    private double         exposureAcqMax;
    private double         gainStream;
    private double         gainAcq;
    private double         offsetStream;
    private double         offsetAcq;
    private boolean        adjustExposureForBinning;
    private boolean        coolerActive;
    private double         temperature;
    private int            triggerType;
    private StreamSource   streamSource;
    private boolean        streamingEnabled;
    private boolean        streaming;
    private int            channel;
    private double         currentFPS;
    private boolean        mirrorImage;
    private String         activeCameraSettings;
    private ObservableList binningListModel;
    private Object         binningListSelection;
    private ObservableList speedListModel;
    private Object         speedListSelection;
    
    //
    private CameraInterface camera;

    private boolean initialized;
    
    /** Creates a new instance of CameraModelZ */
    public CameraModelZ(CameraInterface camera) {
        this.camera = camera;
        //initializeValues();
        // Initialize lists
        binningListModel = new ArrayListModel();
        binningListModel.addAll(BINNING_OPTIONS);
        binningListSelection    = binningListModel.get(0);
        
        speedListModel = new ArrayListModel();
        speedListModel.addAll(SPEED_OPTIONS);
        speedListSelection = speedListModel.get(0);
    }
    
    
    public CameraModelZ() {
        this.camera = null;
    }
    // <editor-fold defaultstate="collapsed" desc=" < < *  Lists  * > >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ">
    // List stuff...
    public ListModel getBinningListModel() {
        return binningListModel;
    }
    
    
    public Object getBinningListSelection() {
        return binningListSelection;
    }
    
    
    public ListModel getSpeedListModel() {
        return speedListModel;
    }
    
    
    public Object getSpeedListSelection() {
        return speedListSelection;
    }
    
// </editor-fold>
    //----------------------------------------------------------------------
    /**
     * Initialize the camera
     */
    public boolean initialize() {
        if(!camera.initialize()) return false;
        initializeValues();
        setOpen(true);
        initialized = true;
        return true;
    }
    
    public void initializeValues() {
        //---------------
        open                        = false;
        streaming                   = false;
        busy                        = false;
        width                       = -1;
        height                      = -1;
        format                      = -1;
        depth                       = -1;
        binningX                    = 1;
        binningY                    = 1;
        sameXYBinning               = true;
        speed                       = 20.0;
        roiSet                      = false;
        roiX                        = 0;
        roiY                        = 0;
        roiW                        = 0;
        roiH                        = 0;
        sameSetAcqStream            = false;
        exposureStream              = 10.0f;
        exposureStreamMin           = 0.040f;
        exposureStreamMax           = 999999999;
        exposureAcq                 = 25.0f;
        exposureAcqMin              = 0.009f;
        exposureAcqMax              = 999999999;
        gainStream                  = 1.0f;
        gainAcq                     = 1.0f;
        offsetStream                = 0;
        offsetAcq                   = 0;
        adjustExposureForBinning    = true;
        coolerActive                = true;
        temperature                 = -1;
        triggerType                 = 0;
        streamSource                = null;
        streamingEnabled            = false;
        channel                     = 0;
        currentFPS                  = -1;
        mirrorImage                 = false;
        activeCameraSettings        = "setA.xml";
        //------------------
        // Get init. values from Camera implementation...
        camera.getOffsetStream();
        setDepth(Prefs.usr.getInt("camera.depth", 8));
        setBinning(Prefs.usr.getInt("camera.binning", 2));
        setSpeed( Prefs.usr.getDouble("camera.speed", 20.0));
        setGainStream(Prefs.usr.getDouble("camera.gain", 1.0));
        setOffsetStream(Prefs.usr.getInt("camera.offset", 400));
        setExposureStream(Prefs.usr.getDouble("camera.exposure", 25.0));
        setMirrorImage(Prefs.usr.getBoolean("camera.mirrorImage", false));
        setCoolerActive(Prefs.usr.getBoolean("camera.coolerActive", false));
        updateToCameraState();
    }
    
    void updateToCameraState() {
        camera.getCameraState();
        setWidth(camera.getWidth());
        setHeight(camera.getHeight());
    }
    
    /**
     * Gets the current value of open
     * @return Current value of open
     */
    public boolean isOpen() {
        return camera.isOpen();
    }
    
    /**
     * Sets the value of open
     * @param open New value for open
     */
    public void setOpen(boolean open) {
        boolean oldOpen = isOpen();
        this.open = open;
        System.out.println("Camera Opened");
        firePropertyChange(PROPERTYNAME_OPEN, oldOpen, open);
    }
    
    
    /**
     * Gets the current value of busy
     * @return Current value of busy
     */
    public boolean isBusy() {
        return busy;
    }
    
    /**
     * Sets the value of busy
     * @param busy New value for busy
     */
    public void setBusy(boolean busy) {
        boolean oldBusy = isBusy();
        this.busy = busy;
        firePropertyChange(PROPERTYNAME_BUSY, oldBusy, busy);
    }
    
    
    /**
     * Gets the current value of width
     * @return Current value of width
     */
    public int getWidth() {
        int w = camera.getWidth();
        return w;
    }
    
    
    /**
     * Sets the value of width
     * @param width New value for width
     */
    public void setWidth(int width) {
        int oldWidth = getWidth();
        this.width = width;
        firePropertyChange(PROPERTYNAME_WIDTH, oldWidth, width);
    }
    
    
    /**
     * Gets the current value of height
     * @return Current value of height
     */
    public int getHeight() {
        int h = camera.getHeight();
        return h;
    }
    
    
    /**
     * Sets the value of height
     * @param height New value for height
     */
    public void setHeight(int height) {
        int oldHeight = getHeight();
        this.height = height;
        firePropertyChange(PROPERTYNAME_HEIGHT, oldHeight, height);
    }
    
    
    /**
     * Gets the current value of format
     * @return Current value of format
     */
    public int getFormat() {
        return format;
    }
    
    
    /**
     * Sets the value of format
     * @param format New value for format
     */
    public void setFormat(int format) {
        int oldFormat = getFormat();
        this.format = format;
        camera.setFormat(format);
        firePropertyChange(PROPERTYNAME_FORMAT, oldFormat, format);
    }
    
    
    /**
     * Gets the current value of depth
     * @return Current value of depth
     */
    public int getDepth() {
        return depth;
    }
    
    
    /**
     * Sets the value of depth
     * @param depth New value for depth
     */
    public void setDepth(int depth) {
        int oldDepth = getDepth();
        this.depth = depth;
        camera.setDepth(depth);
        firePropertyChange(PROPERTYNAME_DEPTH, oldDepth, depth);
    }
    
    
    /**
     * Set binning (assumes sameXYBinning)
     * @param _binning Binning to set to.
     */
    public void setBinning(int _binning) {
        this.setBinningX(_binning);
        //this.setBinningY(_binning);
    }
    
    
    /**
     * get the current binning (acutally gets binningX, and we are assuming sameXYBinning)
     * @return Current binning (X)
     */
    public int getBinning() {
        return this.getBinningX();
    }
    
    
    /**
     * Gets the current value of binningX
     * @return Current value of binningX
     */
    public int getBinningX() {
        return binningX;
    }
    
    
    /**
     * Sets the value of binningX
     * @param binningX New value for binningX
     */
    public void setBinningX(int binningX) {
        int oldBinningX = getBinningX();
        this.binningX = binningX;
        
        camera.setBinning(binningX);
        // Adjust the exposure proportionately for new binning
        if (this.isAdjustExposureForBinning()) {
            float factor = (float)(oldBinningX * oldBinningX) / (float)(binningX * binningX);
            float exposure = (long)Math.round(this.getExposureStream() * factor);
            if (exposure > 0) {
                setExposureStream(exposure);
            }
        }
        Prefs.usr.putInt("camera.binning", (int)binningX);
        this.updateToCameraState();
//      selectedROI    = new Rectangle(0, 0, 0, 0);
//      isROISet       = false;
        firePropertyChange(PROPERTYNAME_BINNINGX, oldBinningX, binningX);
    }
    
    
    /**
     * Gets the current value of binningY
     * @return Current value of binningY
     */
    public int getBinningY() {
        return binningY;
    }
    
    
    /**
     * Sets the value of binningY
     * @param binningY New value for binningY
     */
    public void setBinningY(int binningY) {
        int oldBinningY = getBinningY();
        this.binningY = binningY;
        firePropertyChange(PROPERTYNAME_BINNINGY, oldBinningY, binningY);
    }
    
    
    /**
     * Gets the current value of sameXYBinning
     * @return Current value of sameXYBinning
     */
    public boolean isSameXYBinning() {
        return sameXYBinning;
    }
    
    
    /**
     * Sets the value of sameXYBinning
     * @param sameXYBinning New value for sameXYBinning
     */
    public void setSameXYBinning(boolean sameXYBinning) {
        boolean oldSameXYBinning = isSameXYBinning();
        this.sameXYBinning = sameXYBinning;
        firePropertyChange(PROPERTYNAME_SAMEXYBINNING, oldSameXYBinning, sameXYBinning);
    }
    
    
    /**
     * Gets the current value of speed
     * @return Current value of speed
     */
    public double getSpeed() {
        return speed;
    }
    
    
    /**
     * Sets the value of speed
     * @param speed New value for speed
     */
    public void setSpeed(double speed) {
        double oldSpeed = getSpeed();
        this.speed = speed;
        camera.setSpeed(speed);
        firePropertyChange(PROPERTYNAME_SPEED, oldSpeed, speed);
    }
    
    // <editor-fold defaultstate="collapsed" desc="<<< ROI >>>">
       public void setROI(Rectangle roi) {
        roiX    = (int)roi.getX();
        roiX    = (int)roi.getY();
        roiW    = (int)roi.getWidth();
        roiH    = (int)roi.getHeight();
    }
    
    
    public Rectangle getROI() {
        return new Rectangle(this.roiX, roiY, roiW, roiH);
    }
    
    
    public void clearROI() {
        setROI(new Rectangle(0, 0, 0, 0));
    }
    
    
    /**
     * Gets the current value of roiSet
     * @return Current value of roiSet
     */
    public boolean isRoiSet() {
        return roiSet;
    }
    
    
    /**
     * Sets the value of roiSet
     * @param roiSet New value for roiSet
     */
    public void setRoiSet(boolean roiSet) {
        boolean oldRoiSet = isRoiSet();
        this.roiSet = roiSet;
        firePropertyChange(PROPERTYNAME_ROISET, oldRoiSet, roiSet);
    }
    
    
    /**
     * Gets the current value of roiX
     * @return Current value of roiX
     */
    public int getRoiX() {
        return roiX;
    }
    
    
    /**
     * Sets the value of roiX
     * @param roiX New value for roiX
     */
    public void setRoiX(int roiX) {
        int oldRoiX = getRoiX();
        this.roiX = roiX;
        firePropertyChange(PROPERTYNAME_ROIX, oldRoiX, roiX);
    }
    
    
    /**
     * Gets the current value of roiY
     * @return Current value of roiY
     */
    public int getRoiY() {
        return roiY;
    }
    
    
    /**
     * Sets the value of roiY
     * @param roiY New value for roiY
     */
    public void setRoiY(int roiY) {
        int oldRoiY = getRoiY();
        this.roiY = roiY;
        firePropertyChange(PROPERTYNAME_ROIY, oldRoiY, roiY);
    }
    
    
    /**
     * Gets the current value of roiW
     * @return Current value of roiW
     */
    public int getRoiW() {
        return roiW;
    }
    
    
    /**
     * Sets the value of roiW
     * @param roiW New value for roiW
     */
    public void setRoiW(int roiW) {
        int oldRoiW = getRoiW();
        this.roiW = roiW;
        firePropertyChange(PROPERTYNAME_ROIW, oldRoiW, roiW);
    }
    
    
    /**
     * Gets the current value of roiH
     * @return Current value of roiH
     */
    public int getRoiH() {
        return roiH;
    }
    
    
    /**
     * Sets the value of roiH
     * @param roiH New value for roiH
     */
    public void setRoiH(int roiH) {
        int oldRoiH = getRoiH();
        this.roiH = roiH;
        firePropertyChange(PROPERTYNAME_ROIH, oldRoiH, roiH);
    }
    // </editor-fold>
    
    /**
     * Gets the current value of sameSetAcqStream
     * @return Current value of sameSetAcqStream
     */
    public boolean isSameSetAcqStream() {
        return sameSetAcqStream;
    }
    
    
    /**
     * Sets the value of sameSetAcqStream
     * @param sameSetAcqStream New value for sameSetAcqStream
     */
    public void setSameSetAcqStream(boolean sameSetAcqStream) {
        boolean oldSameSetAcqStream = isSameSetAcqStream();
        this.sameSetAcqStream = sameSetAcqStream;
        firePropertyChange(PROPERTYNAME_SAMESETACQSTREAM, oldSameSetAcqStream,
                sameSetAcqStream);
    }
    
    
    /**
     * Gets the current value of exposureStream
     * @return Current value of exposureStream
     */
    public double getExposureStream() {
        return exposureStream;
    }
    
    
    /**
     * Sets the value of exposureStream
     * @param exposureStream New value for exposureStream
     */
    public void setExposureStream(double exposureStream) {
        double oldExposureStream = getExposureStream();
        this.exposureStream = exposureStream;
        camera.setExposureStream(exposureStream);
        firePropertyChange(PROPERTYNAME_EXPOSURESTREAM, oldExposureStream,
                exposureStream);
    }
    
    /**
     * Gets the current value of exposureStreamMin
     * @return Current value of exposureStreamMin
     */
    public double getExposureStreamMin() {
        return exposureStreamMin;
    }
    
    
    /**
     * Sets the value of exposureStreamMin
     * @param exposureStreamMin New value for exposureStreamMin
     */
    public void setExposureStreamMin(double exposureStreamMin) {
        double oldExposureStreamMin = getExposureStreamMin();
        this.exposureStreamMin = exposureStreamMin;
        firePropertyChange(PROPERTYNAME_EXPOSURESTREAMMIN, oldExposureStreamMin,
                exposureStreamMin);
    }
    
    
    /**
     * Gets the current value of exposureStreamMax
     * @return Current value of exposureStreamMax
     */
    public double getExposureStreamMax() {
        return exposureStreamMax;
    }
    
    
    /**
     * Sets the value of exposureStreamMax
     * @param exposureStreamMax New value for exposureStreamMax
     */
    public void setExposureStreamMax(double exposureStreamMax) {
        double oldExposureStreamMax = getExposureStreamMax();
        this.exposureStreamMax = exposureStreamMax;
        firePropertyChange(PROPERTYNAME_EXPOSURESTREAMMAX, oldExposureStreamMax,
                exposureStreamMax);
    }
    
    
    /**
     * Gets the current value of exposureAcq
     * @return Current value of exposureAcq
     */
    public double getExposureAcq() {
        return exposureAcq;
    }
    
    
    /**
     * Sets the value of exposureAcq
     * @param exposureAcq New value for exposureAcq
     */
    public void setExposureAcq(double exposureAcq) {
        double oldExposureAcq = getExposureAcq();
        this.exposureAcq = exposureAcq;
        camera.setExposureAcq(exposureAcq);
        firePropertyChange(PROPERTYNAME_EXPOSUREACQ, oldExposureAcq, exposureAcq);
    }
    
    
    /**
     * Gets the current value of exposureAcqMin
     * @return Current value of exposureAcqMin
     */
    public double getExposureAcqMin() {
        return exposureAcqMin;
    }
    
    
    /**
     * Sets the value of exposureAcqMin
     * @param exposureAcqMin New value for exposureAcqMin
     */
    public void setExposureAcqMin(double exposureAcqMin) {
        double oldExposureAcqMin = getExposureAcqMin();
        this.exposureAcqMin = exposureAcqMin;
        firePropertyChange(PROPERTYNAME_EXPOSUREACQMIN, oldExposureAcqMin,
                exposureAcqMin);
    }
    
    
    /**
     * Gets the current value of exposureAcqMax
     * @return Current value of exposureAcqMax
     */
    public double getExposureAcqMax() {
        return exposureAcqMax;
    }
    
    
    /**
     * Sets the value of exposureAcqMax
     * @param exposureAcqMax New value for exposureAcqMax
     */
    public void setExposureAcqMax(double exposureAcqMax) {
        double oldExposureAcqMax = getExposureAcqMax();
        this.exposureAcqMax = exposureAcqMax;
        firePropertyChange(PROPERTYNAME_EXPOSUREACQMAX, oldExposureAcqMax,
                exposureAcqMax);
    }
    
    
    /**
     * Gets the current value of gainStream
     * @return Current value of gainStream
     */
    public double getGainStream() {
        return gainStream;
    }
    
    
    /**
     * Sets the value of gainStream
     * @param gainStream New value for gainStream
     */
    public void setGainStream(double gainStream) {
        double oldGainStream = getGainStream();
        this.gainStream = gainStream;
        camera.setGainStream(gainStream);
        firePropertyChange(PROPERTYNAME_GAINSTREAM, oldGainStream, gainStream);
    }
    
    
    /**
     * Gets the current value of gainAcq
     * @return Current value of gainAcq
     */
    public double getGainAcq() {
        return gainAcq;
    }
    
    
    /**
     * Sets the value of gainAcq
     * @param gainAcq New value for gainAcq
     */
    public void setGainAcq(double gainAcq) {
        double oldGainAcq = getGainAcq();
        this.gainAcq = gainAcq;
        camera.setGainAcq(gainAcq);
        firePropertyChange(PROPERTYNAME_GAINACQ, oldGainAcq, gainAcq);
    }
    
    
    /**
     * Gets the current value of offsetStream
     * @return Current value of offsetStream
     */
    public double getOffsetStream() {
        return offsetStream;
    }
    
    
    /**
     * Sets the value of offsetStream
     * @param offsetStream New value for offsetStream
     */
    public void setOffsetStream(double offsetStream) {
        double oldOffsetStream = getOffsetStream();
        this.offsetStream = offsetStream;
        camera.setOffsetStream(offsetStream);
        firePropertyChange(PROPERTYNAME_OFFSETSTREAM, oldOffsetStream,
                offsetStream);
    }
    
    
    /**
     * Gets the current value of offsetAcq
     * @return Current value of offsetAcq
     */
    public double getOffsetAcq() {
        return offsetAcq;
    }
    
    
    /**
     * Sets the value of offsetAcq
     * @param offsetAcq New value for offsetAcq
     */
    public void setOffsetAcq(double offsetAcq) {
        double oldOffsetAcq = getOffsetAcq();
        this.offsetAcq = offsetAcq;
        firePropertyChange(PROPERTYNAME_OFFSETACQ, oldOffsetAcq, offsetAcq);
    }
    
    
    /**
     * Gets the current value of adjustExposureForBinning
     * @return Current value of adjustExposureForBinning
     */
    public boolean isAdjustExposureForBinning() {
        return adjustExposureForBinning;
    }
    
    
    /**
     * Sets the value of adjustExposureForBinning
     * @param adjustExposureForBinning New value for adjustExposureForBinning
     */
    public void setAdjustExposureForBinning(boolean adjustExposureForBinning) {
        boolean oldAdjustExposureForBinning = isAdjustExposureForBinning();
        this.adjustExposureForBinning = adjustExposureForBinning;
        firePropertyChange(PROPERTYNAME_ADJUSTEXPOSUREFORBINNING,
                oldAdjustExposureForBinning, adjustExposureForBinning);
    }
    
    
    /**
     * Gets the current value of coolerActive
     * @return Current value of coolerActive
     */
    public boolean isCoolerActive() {
        return coolerActive;
    }
    
    
    /**
     * Sets the value of coolerActive
     * @param coolerActive New value for coolerActive
     */
    public void setCoolerActive(boolean coolerActive) {
        boolean oldCoolerActive = isCoolerActive();
        this.coolerActive = coolerActive;
        firePropertyChange(PROPERTYNAME_COOLERACTIVE, oldCoolerActive,
                coolerActive);
    }
    
    
    /**
     * Gets the current value of temperature
     * @return Current value of temperature
     */
    public double getTemperature() {
        return temperature;
    }
    
    
    /**
     * Sets the value of temperature
     * @param temperature New value for temperature
     */
    public void setTemperature(double temperature) {
        double oldTemperature = getTemperature();
        this.temperature = temperature;
        firePropertyChange(PROPERTYNAME_TEMPERATURE, oldTemperature, temperature);
    }
    
    
    /**
     * Gets the current value of triggerType
     * @return Current value of triggerType
     */
    public int getTriggerType() {
        return triggerType;
    }
    
    
    /**
     * Sets the value of triggerType
     * @param triggerType New value for triggerType
     */
    public void setTriggerType(int triggerType) {
        camera.enableFastAcq(getDepth());
        int oldTriggerType = getTriggerType();
        this.triggerType = triggerType;
        firePropertyChange(PROPERTYNAME_TRIGGERTYPE, oldTriggerType, triggerType);
    }
    
    // <editor-fold defaultstate="collapsed" desc=" < Streaming  >>>>>>>>>>>>>>>>>>>>>>>>>>>">
    
    
    /**
     * Gets the current value of streamSource
     * @return Current value of streamSource
     */
    public StreamSource getStreamSource() {
        StreamSource s = ((StreamGenerator)camera).getStreamSource();
        if (s != null) {
            setStreamingEnabled(true);
        }
        return s;
    }
    
    
    /**
     * Sets the value of streamSource
     * @param streamSource New value for streamSource
     */
    //    public void setStreamSource(StreamSource streamSource) {
    //        StreamSource oldStreamSource = getStreamSource();
    //        this.streamSource = streamSource;
    //        firePropertyChange(PROPERTYNAME_STREAMSOURCE, oldStreamSource, streamSource);
    //    }
    
    /**
     * Gets the current value of streamingEnabled
     * @return Current value of streamingEnabled
     */
    public boolean isStreamingEnabled() {
        return streamingEnabled;
    }
    
    
    /**
     * Sets the value of streamingEnabled
     * @param streamingEnabled New value for streamingEnabled
     */
    public void setStreamingEnabled(boolean streamingEnabled) {
        boolean oldStreamingEnabled = isStreamingEnabled();
        this.streamingEnabled = streamingEnabled;
        firePropertyChange(PROPERTYNAME_STREAMINGENABLED, oldStreamingEnabled,
                streamingEnabled);
    }
    
    
    /**
     * Gets the current value of streaming
     * @return Current value of streaming
     */
    public boolean isStreaming() {
        return streaming;
    }
    
    
    /**
     * Sets the value of streaming
     * @param streaming New value for streaming
     */
    public void setStreaming(boolean streaming) {
        boolean oldStreaming = isStreaming();
        this.streaming = streaming;
        firePropertyChange(PROPERTYNAME_STREAMING, oldStreaming, streaming);
    }
    
    
    /**
     * Gets the current value of channel
     * @return Current value of channel
     */
    public int getChannel() {
        return channel;
    }
    
    
    /**
     * Sets the value of channel
     * @param channel New value for channel
     */
    public void setChannel(int channel) {
        int oldChannel = getChannel();
        this.channel = channel;
        firePropertyChange(PROPERTYNAME_CHANNEL, oldChannel, channel);
    }
    
    
    /**
     * Gets the current value of currentFPS
     * @return Current value of currentFPS
     */
    public double getCurrentFPS() {
        return currentFPS;
    }
    
    
    /**
     * Sets the value of currentFPS
     * @param currentFPS New value for currentFPS
     */
    public void setCurrentFPS(double currentFPS) {
        double oldCurrentFPS = getCurrentFPS();
        this.currentFPS = currentFPS;
        firePropertyChange(PROPERTYNAME_CURRENTFPS, oldCurrentFPS, currentFPS);
    }
    
// </editor-fold>
    
    /**
     * Gets the current value of mirrorImage
     * @return Current value of mirrorImage
     */
    public boolean isMirrorImage() {
        return mirrorImage;
    }
    
    
    /**
     * Sets the value of mirrorImage
     * @param mirrorImage New value for mirrorImage
     */
    public void setMirrorImage(boolean mirrorImage) {
        boolean oldMirrorImage = isMirrorImage();
        this.mirrorImage = mirrorImage;
        firePropertyChange(PROPERTYNAME_MIRRORIMAGE, oldMirrorImage, mirrorImage);
    }
    
    
    /**
     * Gets the current value of activeCameraSettings
     * @return Current value of activeCameraSettings
     */
    public String getActiveCameraSettings() {
        return activeCameraSettings;
    }
    
    
    //????
    /**
     * Sets the value of activeCameraSettings
     * @param activeCameraSettings New value for activeCameraSettings
     */
    public void setActiveCameraSettings(String activeCameraSettings) {
        String oldActiveCameraSettings = getActiveCameraSettings();
        this.activeCameraSettings = activeCameraSettings;
        firePropertyChange(PROPERTYNAME_ACTIVECAMERASETTINGS,
                oldActiveCameraSettings, activeCameraSettings);
    }
    
    
    // Non-bean things...  ---------------------------------------------------
    public String getCameraState() {
        return "not implemented";
    }
    
    
    public int pushSettings() {
        return 0;
    }
    
    
    public ArrayList getAvailFormats() {
        return null;
    }
    
    
    public byte[] takeSnapshot8() {
        return null;
    }
    
    
    public short[] takeSnapshot16() {
        return null;
    }
    
    
    public void enableFastAcq(int depth) {
    }
    
    
    public byte[] acqFast8() {
        return null;
    }
    
    
    public int acqFast8(byte[] imgArray) {
        return 0;
    }
    
    
    public short[] acqFast16() {
        return null;
    }
    
    
    public int acqFast16(short[] imgArray) {
        return 0;
    }
    
    
    public void disableFastAcq() {
    }
    
    
    public void startStream() {
        dispatchToEDT(new Runnable() {
            public void run() {
                ((StreamGenerator)camera).startStream();
            }
        });
        
        // this.setStreaming(true);
    }
    
    
    public void stopStream() {
        ((StreamGenerator)camera).stopStream();
        this.setStreaming(false);
    }
    
    
    public void closeStreamSource() {
    }
    
    
    public String listAllParms() {
        return null;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Save - Load >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>">
    
    // Save / Load XML -----------------------------------------------------
    
    // How to identify restorables...?
    // Save to XML using XStream
    // ** added by GBH
    public void save(String file) {
        XStream xstream = new XStream(new DomDriver());
        String  xml = xstream.toXML(this);
        System.out.println(xml);
        try {
            FileUtil.saveTxtFile(file, xml, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void load(String file) {
        XStream xstream = new XStream(new DomDriver());
        String  xml = xstream.toXML(this);
        System.out.println(xml);
        try {
            FileUtil.saveTxtFile(file, xml, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    void loadPrefs() {
        Preferences prefs = Application.getInstance().getPreferences();
        format                      = prefs.getInt("camera.format", -1);
        depth                       = prefs.getInt("camera.depth", -1);
        binningX                    = prefs.getInt("camera.binningX", 2);
        binningY                    = prefs.getInt("camera.binningY", 2);
        speed                       = prefs.getInt("camera.speed", -1);
        roiSet                      = prefs.getBoolean("camera.roiSet", false);
        roiX                        = prefs.getInt("camera.roiX", 0);
        roiY                        = prefs.getInt("camera.roiY", 0);
        roiW                        = prefs.getInt("camera.roiW", 0);
        roiH                        = prefs.getInt("camera.roiH", 0);
        sameSetAcqStream            = prefs.getBoolean("camera.sameSetAcqStream", false);
        exposureStream              = prefs.getFloat("camera.exposureStream",10.0f);
        exposureAcq                 = prefs.getFloat("camera.exposureAcq", 25.0f);
        gainStream                  = prefs.getFloat("camera.gainStream", 1.0f);
        gainAcq                     = prefs.getFloat("camera.gainAcq", 1.0f);
        offsetStream                = prefs.getInt("camera.offsetStream", 0);
        offsetAcq                   = prefs.getInt("camera.offsetAcq", 0);
        adjustExposureForBinning    = prefs.getBoolean("camera.adjustExposureForBin", true);
        coolerActive                = prefs.getBoolean("camera.coolerActive", true);
        mirrorImage                 = prefs.getBoolean("camera.mirrorImage", false);
    }
// </editor-fold>
    
// --- dispatchToEDT ----------------------------------------
    // Usage:
    //      dispatchToEDT(new Runnable()
    //      {
    //         public void run () {
    //            //...
    //         }
    //      });
    public static void dispatchToEDT(Runnable runnable) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            runnable.run();
        }
    }

    
}
