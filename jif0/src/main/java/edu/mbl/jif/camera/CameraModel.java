package edu.mbl.jif.camera;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.action.ActionManager;

public class CameraModel
    extends Model {
    // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">
    //Preferences prefs = CamAcqJ.getInstance().getPreferences();
    static final String DEFAULT_PREFS_KEY = "camera";
    Preferences prefs;
    //
    public static final double GAIN_MIN = 0.451;
    public static final double GAIN_MAX = 21.0;    // --- open ---
    public static final String PROPERTYNAME_OPEN = "open";
    private boolean open = false;    // --- busy ---
    public static final String PROPERTYNAME_BUSY = "busy";
    private boolean busy = false;    // --- width ---
    public static final String PROPERTYNAME_WIDTH = "width";
    private int width = -1;    // --- height ---
    public static final String PROPERTYNAME_HEIGHT = "height";
    private int height = -1;    // --- format ---
    public static final String PROPERTYNAME_FORMAT = "format";
    private int format;
    static final int FORMAT_DEFAULT = -1;
    PreferencesAdapter formatPrefAdapter;
    public static final String PROPERTYNAME_DEPTH = "depth";
    private int depth;
    static final int DEPTH_DEFAULT = 8;
    PreferencesAdapter depthPrefAdapter;
    public static final Integer DEPTH_ENUM_1 = new Integer(8);
    public static final Integer DEPTH_ENUM_2 = new Integer(12);
    public static final List DEPTH_OPTIONS = Arrays.asList(new Integer[]{
            DEPTH_ENUM_1,
            DEPTH_ENUM_2
        });
    private ObservableList depthListModel;
    private Object depthListSelection;
    // --- binningX ---
    public static final String PROPERTYNAME_BINNINGX = "binningX";
    private int binningX;
    static final int BINNINGX_DEFAULT = 2;
    PreferencesAdapter binningXPrefAdapter;
    public static final Integer BINNINGX_ENUM_1 = new Integer(1);
    public static final Integer BINNINGX_ENUM_2 = new Integer(2);
    public static final Integer BINNINGX_ENUM_3 = new Integer(4);
    public static final List BINNINGX_OPTIONS = Arrays.asList(new Integer[]{
            BINNINGX_ENUM_1,
            BINNINGX_ENUM_2,
            BINNINGX_ENUM_3
        });
    private ObservableList binningXListModel;
    private Object binningXListSelection;
    // --- binningY ---
    public static final String PROPERTYNAME_BINNINGY = "binningY";
    private int binningY;
    static final int BINNINGY_DEFAULT = 2;
    PreferencesAdapter binningYPrefAdapter;
    public static final String PROPERTYNAME_SAMEXYBINNING = "sameXYBinning";
    private boolean sameXYBinning;
    static final boolean SAMEXYBINNING_DEFAULT = true;
    PreferencesAdapter sameXYBinningPrefAdapter;
    public static final String PROPERTYNAME_SPEED = "speed";
    private double speed;
    static final double SPEED_DEFAULT = 20.0;
    PreferencesAdapter speedPrefAdapter;
    public static final Double SPEED_ENUM_1 = new Double(20.0);
    public static final Double SPEED_ENUM_2 = new Double(10.0);
    public static final Double SPEED_ENUM_3 = new Double(5.0);
    public static final Double SPEED_ENUM_4 = new Double(2.5);
    public static final List SPEED_OPTIONS = Arrays.asList(new Double[]{
            SPEED_ENUM_1,
            SPEED_ENUM_2,
            SPEED_ENUM_3,
            SPEED_ENUM_4
        });
    private ObservableList speedListModel;
    private Object speedListSelection;    // --- roiSet ---
    public static final String PROPERTYNAME_ROISET = "roiSet";
    private boolean roiSet = false;    // --- roiX ---
    public static final String PROPERTYNAME_ROIX = "roiX";
    private int roiX = 0;    // --- roiY ---
    public static final String PROPERTYNAME_ROIY = "roiY";
    private int roiY = 0;    // --- roiW ---
    public static final String PROPERTYNAME_ROIW = "roiW";
    private int roiW = 0;    // --- roiH ---
    public static final String PROPERTYNAME_ROIH = "roiH";
    private int roiH = 0;    // --- sameSetAcqStream ---
    public static final String PROPERTYNAME_SAMESETACQSTREAM = "sameSetAcqStream";
    private boolean sameSetAcqStream;
    static final boolean SAMESETACQSTREAM_DEFAULT = true;
    PreferencesAdapter sameSetAcqStreamPrefAdapter;
    // --- exposureStream ---
    public static final String PROPERTYNAME_EXPOSURESTREAM = "exposureStream";
    private double exposureStream;
    static final double EXPOSURESTREAM_DEFAULT = 100;
    PreferencesAdapter exposureStreamPrefAdapter;
    // --- exposureStreamMin ---
    public static final String PROPERTYNAME_EXPOSURESTREAMMIN = "exposureStreamMin";
    private double exposureStreamMin = 0.040;    // --- exposureStreamMax ---
    public static final String PROPERTYNAME_EXPOSURESTREAMMAX = "exposureStreamMax";
    private double exposureStreamMax = 99999999;    // --- exposureAcq ---
    public static final String PROPERTYNAME_EXPOSUREACQ = "exposureAcq";
    private double exposureAcq;
    static final double EXPOSUREACQ_DEFAULT = 110;
    PreferencesAdapter exposureAcqPrefAdapter;
    public static final String PROPERTYNAME_EXPOSUREACQMIN = "exposureAcqMin";
    private double exposureAcqMin = 0.009;    // --- exposureAcqMax ---
    public static final String PROPERTYNAME_EXPOSUREACQMAX = "exposureAcqMax";
    private double exposureAcqMax = 999999999;    // --- gainStream ---
    public static final String PROPERTYNAME_GAINSTREAM = "gainStream";
    private double gainStream;
    static final double GAINSTREAM_DEFAULT = 1.0;
    PreferencesAdapter gainStreamPrefAdapter;
    public static final String PROPERTYNAME_GAINACQ = "gainAcq";
    private double gainAcq;
    static final double GAINACQ_DEFAULT = 1.0;
    PreferencesAdapter gainAcqPrefAdapter;
    public static final String PROPERTYNAME_OFFSETSTREAM = "offsetStream";
    private double offsetStream;
    static final double OFFSETSTREAM_DEFAULT = 1.0;
    PreferencesAdapter offsetStreamPrefAdapter;
    public static final String PROPERTYNAME_OFFSETACQ = "offsetAcq";
    private double offsetAcq;
    static final double OFFSETACQ_DEFAULT = 0;
    PreferencesAdapter offsetAcqPrefAdapter;
    public static final String PROPERTYNAME_ADJUSTEXPOSUREFORBINNING = "adjustExposureForBinning";
    private boolean adjustExposureForBinning;
    static final boolean ADJUSTEXPOSUREFORBINNING_DEFAULT = true;
    PreferencesAdapter adjustExposureForBinningPrefAdapter;
    public static final String PROPERTYNAME_COOLERACTIVE = "coolerActive";
    private boolean coolerActive;
    static final boolean COOLERACTIVE_DEFAULT = true;
    PreferencesAdapter coolerActivePrefAdapter;
    public static final String PROPERTYNAME_AUTOEXPOSURE = "autoExposure";
    private boolean autoExposure;
    static final boolean AUTOEXPOSURE_DEFAULT = false;
    PreferencesAdapter autoExposurePrefAdapter;
    public static final String PROPERTYNAME_TEMPERATURE = "temperature";
    private double temperature = 0.0;    // --- triggerType ---
    public static final String PROPERTYNAME_TRIGGERTYPE = "triggerType";
    private int triggerType = -1;    // --- streamSource ---
    public static final String PROPERTYNAME_STREAMSOURCE = "streamSource";
    private StreamSource streamSource = null;    // --- streamingEnabled ---
    public static final String PROPERTYNAME_STREAMINGENABLED = "streamingEnabled";
    private boolean streamingEnabled = false;    // --- streaming ---
    public static final String PROPERTYNAME_STREAMING = "streaming";
    private boolean streaming = false;    // --- channel ---
    public static final String PROPERTYNAME_CHANNEL = "channel";
    private int channel = 0;    // --- currentFPS ---
    public static final String PROPERTYNAME_CURRENTFPS = "currentFPS";
    private double currentFPS = -1;    // --- mirrorImage ---
    public static final String PROPERTYNAME_MIRRORIMAGE = "mirrorImage";
    private boolean mirrorImage;
    static final boolean MIRRORIMAGE_DEFAULT = false;
    PreferencesAdapter mirrorImagePrefAdapter;
    public static final String PROPERTYNAME_ACTIVECAMERASETTINGS = "activeCameraSettings";
    private String activeCameraSettings = "setA.xml";    // ---  ---zeroIntensity
    public static final String PROPERTYNAME_ZEROINTENSITY = "zeroIntensity";
    private int zeroIntensity;
    static final int ZEROINTENSITY_DEFAULT = 2;
    PreferencesAdapter zeroIntensityPrefAdapter;
    // Camera Types
    String cameraType;
    public static final String TYPE_QCAM = "QCam";
    public static final String TYPE_LUCAM = "LuCam";
    public static final String TYPE_MOCK = "Mock";
    //
    private CameraInterface camera;
    //
    InstrumentController instCtrl;
    // </editor-fold>
    /**
     * Creates a new instance of Camera
     */
    public CameraModel(InstrumentController instCtrl) throws Exception {
        this(instCtrl, DEFAULT_PREFS_KEY);
    }

    public CameraModel(InstrumentController instCtrl, String key) throws Exception {
        initializePreferenceAdapters(key);
        this.instCtrl = instCtrl;
        cameraType = prefs.get("cameraType", TYPE_MOCK);

        try {
            if (cameraType.equalsIgnoreCase(TYPE_MOCK)) {
                camera = new MockCamera();
            } else if (cameraType.equalsIgnoreCase(TYPE_QCAM)) {
                camera = new QCamera(1, QCamera.CHANNEL_A);
            } else if (cameraType.equalsIgnoreCase(TYPE_LUCAM)) {
                camera = new LuCamera(1, QCamera.CHANNEL_A);
            }
            if (camera == null) {
                return;
            }
            if (!initialize()) {
                Application.getInstance().error("No Camera Found\nor failed to initialize");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        // Initialize enumerated list for depth
        depthListModel = new ArrayListModel();
        depthListModel.addAll(DEPTH_OPTIONS);
        depthListSelection = depthListModel.get(0);

        // Initialize enumerated list for binningX
        binningXListModel = new ArrayListModel();
        binningXListModel.addAll(BINNINGX_OPTIONS);
        binningXListSelection = binningXListModel.get(0);

        // Initialize enumerated list for speed
        speedListModel = new ArrayListModel();
        speedListModel.addAll(SPEED_OPTIONS);
        speedListSelection = speedListModel.get(0);

    }

    public void initializePreferenceAdapters(String key) {
        prefs = CamAcqJ.getInstance().getPreferences().node(key);
        formatPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_FORMAT, FORMAT_DEFAULT);    // --- depth ---
        depthPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_DEPTH, DEPTH_DEFAULT);
        binningXPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_BINNINGX, BINNINGX_DEFAULT);
        binningYPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_BINNINGY, BINNINGY_DEFAULT);    // --- sameXYBinning ---
        sameXYBinningPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_SAMEXYBINNING, SAMEXYBINNING_DEFAULT);    // --- speed ---
        speedPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_SPEED, SPEED_DEFAULT);
        sameSetAcqStreamPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_SAMESETACQSTREAM, SAMESETACQSTREAM_DEFAULT);
        exposureStreamPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_EXPOSURESTREAM, EXPOSURESTREAM_DEFAULT);
        exposureAcqPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_EXPOSUREACQ, EXPOSUREACQ_DEFAULT);    // --- exposureAcqMin ---
        gainStreamPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_GAINSTREAM, GAINSTREAM_DEFAULT);    // --- gainAcq ---
        gainAcqPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_GAINACQ, GAINACQ_DEFAULT);    // --- offsetStream ---
        offsetStreamPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_OFFSETSTREAM, OFFSETSTREAM_DEFAULT);    // --- offsetAcq ---
        offsetAcqPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_OFFSETACQ, OFFSETACQ_DEFAULT);    // --- adjustExposureForBinning ---
        adjustExposureForBinningPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_ADJUSTEXPOSUREFORBINNING,
            ADJUSTEXPOSUREFORBINNING_DEFAULT);    // --- coolerActive ---
        coolerActivePrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_COOLERACTIVE, COOLERACTIVE_DEFAULT);    // --- adjustExposureForBinning ---
        autoExposurePrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_AUTOEXPOSURE, AUTOEXPOSURE_DEFAULT);    // --- temperature ---
        mirrorImagePrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_MIRRORIMAGE, MIRRORIMAGE_DEFAULT);    // --- activeCameraSettings ---
        zeroIntensityPrefAdapter =
            new PreferencesAdapter(prefs, CameraModel.PROPERTYNAME_ZEROINTENSITY, ZEROINTENSITY_DEFAULT);    // Camera Types... (this is bad!)
    }

    public boolean initialize() {
        if (!camera.initialize()) {
            return false;
        }
        setOpen(true);
        pushSettings();
        updateToCameraState();
        return true;
    }

    public int pushSettings() {
        setExposureStream(getExposureStream());
        setGainStream(getGainStream());
        setOffsetStream(getOffsetStream());
        camera.setDepth(8);
        camera.setBinning(getBinning());
        setSpeed(getSpeed());
        setCoolerActive(isCoolerActive());
        return 0;
    }

    void updateToCameraState() {
        camera.getCameraState();
        setWidth(camera.getWidth());
        setHeight(camera.getHeight());
    }

    public String getCameraState() {
        return "not implemented";
    }
    /////////////////////////////////////////////////////////////////
    public String getCameraType() {
        return cameraType;
    }

    public CameraInterface getCamera() {
        return camera;
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
        if (camera.open()) {
            this.open = open;
            firePropertyChange(PROPERTYNAME_OPEN, oldOpen, open);
        } else {
            System.err.println("camera.open() failed.");
        }
    }

    public void close() {
        camera.close();
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
     * Gets the current value of format from preferences
     * @return Current value of format
     */
    public int getFormat() {
        format = formatPrefAdapter.getInt();
        return format;
    }

    /**
     * Sets the value of format to preferences
     * @param format New value for format
     */
    public void setFormat(int format) {
        int oldFormat = getFormat();
        this.format = format;
        formatPrefAdapter.setInt(format);
        firePropertyChange(PROPERTYNAME_FORMAT, oldFormat, format);
    }

    /**
     * Gets the current value of depth from preferences
     * @return Current value of depth
     */
    public int getDepth() {
        depth = depthPrefAdapter.getInt();
        return depth;
    }

    /**
     * Sets the value of depth to preferences
     * @param depth New value for depth
     */
    public void setDepth(int depth) {
        int oldDepth = getDepth();
        this.depth = depth;
        if (depth == 12) {
            camera.setDepth(16);
        } else {
            camera.setDepth(depth);
        }
        depthPrefAdapter.setInt(depth);
        firePropertyChange(PROPERTYNAME_DEPTH, oldDepth, depth);
    }

    /**
     * ListModel accessor for depth
     * @return ListModel of depth
     */
    public ListModel getDepthListModel() {
        return depthListModel;
    }

    /**
     * ListSelection accessor for depth
     * @return ListSelection of depth
     */
    public Object getDepthListSelection() {
        return depthListSelection;
    }

    /**
     * Set binning (assumes sameXYBinning)
     * @param _binning Binning to set to.
     */
    public void setBinning(int _binning) {
        this.setBinningX(_binning);
    //this.setBinningY(_binning);
    }

    public int getBinning() {
        return getBinningX();
    }

    /**
     * Gets the current value of binningX from preferences
     * @return Current value of binningX
     */
    public int getBinningX() {
        binningX = binningXPrefAdapter.getInt();
        return binningX;
    }

    /**
     * Sets the value of binningX to preferences
     * @param binningX New value for binningX
     */
    public void setBinningX(final int binningX) {

        int oldBinningX = getBinningX();
        if (oldBinningX != binningX) {
            this.binningX = binningX;

            if (isSameXYBinning()) {
                setBinningY(binningX);
            }
            binningXPrefAdapter.setInt(binningX);
            firePropertyChange(PROPERTYNAME_BINNINGX, oldBinningX, binningX);
            setBinningAndRestartDisplay(oldBinningX);
        }

    }

    public void setBinningAndRestartDisplay(final int oldBinningX) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                boolean wasStreaming = false;
                DisplayLiveCamera display = instCtrl.getDisplayLive();
                if (display != null) {
                    wasStreaming = true;
                    ((StreamGenerator) camera).closeStreamSource();
                    display.close();
                    display = null;
                }
                camera.setBinning(binningX);
                // Adjust the exposure proportionately for new binning
                if (isAdjustExposureForBinning()) {
                    float factor = (float) (oldBinningX * oldBinningX) / (float) (binningX * binningX);
                    float exposure = (float) (getExposureStream() * factor);
                    if (exposure > 0) {
                        setExposureStream(exposure);
                    }
                }
                setGainStream(getGainStream());
                updateToCameraState();
                if (wasStreaming) {
                    ActionManager.getInstance().getAction("openLiveDisplay").actionPerformed(
                        new ActionEvent(this, 9090, "openLiveDisplay"));
                }
            }

        });
    }

    /**
     * ListModel accessor for binningX
     * @return ListModel of binningX
     */
    public ListModel getBinningXListModel() {
        return binningXListModel;
    }

    /**
     * ListSelection accessor for binningX
     * @return ListSelection of binningX
     */
    public Object getBinningXListSelection() {
        return binningXListSelection;
    }

    /**
     * Gets the current value of binningY from preferences
     * @return Current value of binningY
     */
    public int getBinningY() {
        binningY = binningYPrefAdapter.getInt();
        return binningY;
    }

    /**
     * Sets the value of binningY to preferences
     * @param binningY New value for binningY
     */
    public void setBinningY(int binningY) {
        int oldBinningY = getBinningY();
        this.binningY = binningY;
        binningYPrefAdapter.setInt(binningY);
        firePropertyChange(PROPERTYNAME_BINNINGY, oldBinningY, binningY);
    }

    /**
     * Gets the current value of sameXYBinning from preferences
     * @return Current value of sameXYBinning
     */
    public boolean isSameXYBinning() {
        sameXYBinning = sameXYBinningPrefAdapter.getBoolean();
        return sameXYBinning;
    }

    /**
     * Sets the value of sameXYBinning to preferences
     * @param sameXYBinning New value for sameXYBinning
     */
    public void setSameXYBinning(boolean sameXYBinning) {
        boolean oldSameXYBinning = isSameXYBinning();
        this.sameXYBinning = sameXYBinning;
        sameXYBinningPrefAdapter.setBoolean(sameXYBinning);
        firePropertyChange(PROPERTYNAME_SAMEXYBINNING, oldSameXYBinning, sameXYBinning);
    }

    /**
     * Gets the current value of speed from preferences
     * @return Current value of speed
     */
    public double getSpeed() {
        speed = speedPrefAdapter.getDouble();
        return speed;
    }

    /**
     * Sets the value of speed to preferences
     * @param speed New value for speed
     */
    public void setSpeed(double speed) {
        double oldSpeed = getSpeed();
        this.speed = speed;
        camera.setSpeed(speed);
        speedPrefAdapter.setDouble(speed);
        firePropertyChange(PROPERTYNAME_SPEED, oldSpeed, speed);
    }

    /**
     * ListModel accessor for speed
     * @return ListModel of speed
     */
    public ListModel getSpeedListModel() {
        return speedListModel;
    }

    /**
     * ListSelection accessor for speed
     * @return ListSelection of speed
     */
    public Object getSpeedListSelection() {
        return speedListSelection;
    }
    //#######################################################################
    public void setRoiToDisplayRoi() {
        if (!isRoiSet()) {
            DisplayLiveCamera display = instCtrl.getDisplayLive();
            if (display != null) {
                Rectangle roi = display.getSelectedROI();
                display.close();
                setROI(roi);
                setRoiSet(true);
            //  ActionManager.getInstance().getAction("openLiveDisplay").actionPerformed())
            }
        } else { // clear camera ROI
            clearRoi();
        }
    }

    public void clearRoi() {
        DisplayLiveCamera display = instCtrl.getDisplayLive();
        if (display != null) {
            display.close();
        }
        Rectangle roi = new Rectangle(0, 0, 0, 0);
        setROI(roi);
        setRoiSet(false);
    }

    public void setROI(Rectangle roi) {
        int newX = 0;
        if (isMirrorImage()) {
            newX = getWidth() - (int) roi.getX() - (int) roi.getWidth();
        } else {
            newX = (int) roi.getX();
        }
        setRoiX(newX);
        setRoiY((int) roi.getY());
        setRoiW((int) roi.getWidth());
        setRoiH((int) roi.getHeight());
        camera.setROI(new Rectangle(
            getRoiX(), getRoiY(),
            getRoiW(), getRoiH()));
        updateToCameraState();
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
    //#######################################################################
    /**
     * Gets the current value of sameSetAcqStream from preferences
     * @return Current value of sameSetAcqStream
     */
    public boolean isSameSetAcqStream() {
        sameSetAcqStream = sameSetAcqStreamPrefAdapter.getBoolean();
        return sameSetAcqStream;
    }

    /**
     * Sets the value of sameSetAcqStream to preferences
     * @param sameSetAcqStream New value for sameSetAcqStream
     */
    public void setSameSetAcqStream(boolean sameSetAcqStream) {
        boolean oldSameSetAcqStream = isSameSetAcqStream();
        this.sameSetAcqStream = sameSetAcqStream;
        sameSetAcqStreamPrefAdapter.setBoolean(sameSetAcqStream);
        firePropertyChange(PROPERTYNAME_SAMESETACQSTREAM, oldSameSetAcqStream, sameSetAcqStream);
    }

    /**
     * Gets the current value of exposureStream from preferences
     * @return Current value of exposureStream
     */
    public double getExposureStream() {
        exposureStream = exposureStreamPrefAdapter.getDouble();
        // Make sure the exposure is in range
        if (exposureStream < getExposureStreamMin()) {
            exposureStream = getExposureStreamMin();
        }
        if (exposureStream > getExposureStreamMax()) {
            exposureStream = getExposureStreamMax();
        }
        return exposureStream;
    }

    /**
     * Sets the value of exposureStream to preferences
     * exposure is in milliseconds;
     * @param exposureStream New value for exposureStream
     */
    public void setExposureStream(double exposureStream) {
        double oldExposureStream = getExposureStream();
        // Make sure the exposure is in range
        if (exposureStream < getExposureStreamMin()) {
            exposureStream = getExposureStreamMin();
        }
        if (exposureStream > getExposureStreamMax()) {
            exposureStream = getExposureStreamMax();
        }
        this.exposureStream = exposureStream;
        exposureStreamPrefAdapter.setDouble(exposureStream);
        camera.setExposureStream(exposureStream * camera.getExposureMultiplier());
        if (!setByOther) {
            if (isSameSetAcqStream()) {
                setByOther = true;
                setExposureAcq(exposureStream);
            }
            setByOther = false;
        }
        firePropertyChange(PROPERTYNAME_EXPOSURESTREAM, oldExposureStream, exposureStream);
    }

    boolean setByOther = false;

    /**
     * Gets the current value of exposureAcq from preferences
     * @return Current value of exposureAcq
     */
    public double getExposureAcq() {
        exposureAcq = exposureAcqPrefAdapter.getDouble();
        // Make sure the exposure is in range
        if (exposureAcq < getExposureAcqMin()) {
            exposureAcq = getExposureAcqMin();
        }
        if (exposureAcq > getExposureAcqMax()) {
            exposureAcq = getExposureAcqMax();
        }
        return exposureAcq;
    }

    /**
     * Sets the value of exposureAcq to preferences
     * @param exposureAcq New value for exposureAcq
     */
    public void setExposureAcq(double exposureAcq) {
        double oldExposureAcq = getExposureAcq();
        // Make sure the exposure is in range
        if (exposureAcq < getExposureAcqMin()) {
            exposureAcq = getExposureAcqMin();
        }
        if (exposureAcq > getExposureAcqMax()) {
            exposureAcq = getExposureAcqMax();
        }
        this.exposureAcq = exposureAcq;
        exposureAcqPrefAdapter.setDouble(exposureAcq);
        camera.setExposureAcq(exposureAcq * camera.getExposureMultiplier());
        if (!setByOther) {
            if (isSameSetAcqStream()) {
                setByOther = true;
                setExposureStream(exposureAcq);
            }
            setByOther = false;
        }
        firePropertyChange(PROPERTYNAME_EXPOSUREACQ, oldExposureAcq, exposureAcq);
    }

    /**
     * Gets the current value of exposureStreamMin
     * @return Current value of exposureStreamMin
     */
    public double getExposureStreamMin() {
        exposureStreamMin = camera.getExposureStreamMin() / camera.getExposureMultiplier();
        return exposureStreamMin;
    }

    /**
     * Sets the value of exposureStreamMin
     * @param exposureStreamMin New value for exposureStreamMin
     */
    public void setExposureStreamMin(double exposureStreamMin) {
        double oldExposureStreamMin = getExposureStreamMin();
        this.exposureStreamMin = exposureStreamMin;
        firePropertyChange(PROPERTYNAME_EXPOSURESTREAMMIN, oldExposureStreamMin, exposureStreamMin);
    }

    /**
     * Gets the current value of exposureStreamMax
     * @return Current value of exposureStreamMax
     */
    public double getExposureStreamMax() {
        exposureStreamMax = camera.getExposureStreamMax() / camera.getExposureMultiplier();
        return exposureStreamMax;
    }

    /**
     * Sets the value of exposureStreamMax
     * @param exposureStreamMax New value for exposureStreamMax
     */
    public void setExposureStreamMax(double exposureStreamMax) {
        double oldExposureStreamMax = getExposureStreamMax();
        this.exposureStreamMax = exposureStreamMax;
        firePropertyChange(PROPERTYNAME_EXPOSURESTREAMMAX, oldExposureStreamMax, exposureStreamMax);
    }

    /**
     * Gets the current value of exposureAcqMin
     * @return Current value of exposureAcqMin
     */
    public double getExposureAcqMin() {
        exposureAcqMin = camera.getExposureAcqMin() / camera.getExposureMultiplier();
        return exposureAcqMin;
    }

    /**
     * Sets the value of exposureAcqMin
     * @param exposureAcqMin New value for exposureAcqMin
     */
    public void setExposureAcqMin(double exposureAcqMin) {
        double oldExposureAcqMin = getExposureAcqMin();
        this.exposureAcqMin = exposureAcqMin;
        firePropertyChange(PROPERTYNAME_EXPOSUREACQMIN, oldExposureAcqMin, exposureAcqMin);
    }

    /**
     * Gets the current value of exposureAcqMax
     * @return Current value of exposureAcqMax
     */
    public double getExposureAcqMax() {
        exposureAcqMax = camera.getExposureAcqMax() / camera.getExposureMultiplier();
        return exposureAcqMax;
    }

    /**
     * Sets the value of exposureAcqMax
     * @param exposureAcqMax New value for exposureAcqMax
     */
    public void setExposureAcqMax(double exposureAcqMax) {
        double oldExposureAcqMax = getExposureAcqMax();
        this.exposureAcqMax = exposureAcqMax;
        firePropertyChange(PROPERTYNAME_EXPOSUREACQMAX, oldExposureAcqMax, exposureAcqMax);
    }

    /**
     * Gets the current value of gainStream from preferences
     * @return Current value of gainStream
     */
    public double getGainStream() {

        gainStream = gainStreamPrefAdapter.getDouble();
        // Make sure the gain is in range
        if (gainStream < GAIN_MIN) {
            gainStream = GAIN_MIN;
        }
        if (gainStream > GAIN_MAX) {
            gainStream = GAIN_MAX;
        }
        return gainStream;
    }

    /**
     * Sets the value of gainStream to preferences
     * @param gainStream New value for gainStream
     */
    public void setGainStream(double gainStream) {
        double oldGainStream = getGainStream();
        // Make sure the gain is in range
        if (gainStream < GAIN_MIN) {
            gainStream = GAIN_MIN;
        }
        if (gainStream > GAIN_MAX) {
            gainStream = GAIN_MAX;
        }
        this.gainStream = gainStream;
        gainStreamPrefAdapter.setDouble(gainStream);
        camera.setGainStream(gainStream * camera.getGainMultiplier());
        if (!setByOther) {
            if (isSameSetAcqStream()) {
                setByOther = true;
                setGainAcq(gainStream);
            }
            setByOther = false;
        }
        firePropertyChange(PROPERTYNAME_GAINSTREAM, oldGainStream, gainStream);
    }

    /**
     * Gets the current value of gainAcq from preferences
     * @return Current value of gainAcq
     */
    public double getGainAcq() {
        gainAcq = gainAcqPrefAdapter.getDouble();
        // Make sure the gain is in range
        if (gainAcq < GAIN_MIN) {
            gainAcq = GAIN_MIN;
        }
        if (gainAcq > GAIN_MAX) {
            gainAcq = GAIN_MAX;
        }
        return gainAcq;
    }

    /**
     * Sets the value of gainAcq to preferences
     * @param gainAcq New value for gainAcq
     */
    public void setGainAcq(double gainAcq) {
        double oldGainAcq = getGainAcq();
        if (gainAcq < GAIN_MIN) {
            gainAcq = GAIN_MIN;
        }
        if (gainAcq > GAIN_MAX) {
            gainAcq = GAIN_MAX;
        }
        this.gainAcq = gainAcq;
        gainAcqPrefAdapter.setDouble(gainAcq);
        camera.setGainAcq(gainAcq * camera.getGainMultiplier());
        if (!setByOther) {
            if (isSameSetAcqStream()) {
                setByOther = true;
                setGainStream(gainAcq);
            }
            setByOther = false;
        }
        firePropertyChange(PROPERTYNAME_GAINACQ, oldGainAcq, gainAcq);
    }

    /**
     * Gets the current value of offsetStream from preferences
     * @return Current value of offsetStream
     */
    public double getOffsetStream() {
        offsetStream = offsetStreamPrefAdapter.getDouble();
        return offsetStream;
    }

    /**
     * Sets the value of offsetStream to preferences
     * @param offsetStream New value for offsetStream
     */
    public void setOffsetStream(double offsetStream) {
        double oldOffsetStream = getOffsetStream();
        this.offsetStream = offsetStream;
        offsetStreamPrefAdapter.setDouble(offsetStream);
        camera.setOffsetStream(offsetStream);
        setOffsetAcq(offsetStream);
        firePropertyChange(PROPERTYNAME_OFFSETSTREAM, oldOffsetStream, offsetStream);
    }

    /**
     * Gets the current value of offsetAcq from preferences
     * @return Current value of offsetAcq
     */
    public double getOffsetAcq() {
        offsetAcq = offsetAcqPrefAdapter.getDouble();
        return offsetAcq;
    }

    /**
     * Sets the value of offsetAcq to preferences
     * @param offsetAcq New value for offsetAcq
     */
    public void setOffsetAcq(double offsetAcq) {
        double oldOffsetAcq = getOffsetAcq();
        this.offsetAcq = offsetAcq;
        offsetAcqPrefAdapter.setDouble(offsetAcq);
        camera.setOffsetAcq(offsetAcq);
        firePropertyChange(PROPERTYNAME_OFFSETACQ, oldOffsetAcq, offsetAcq);
    }

    /**
     * Gets the current value of adjustExposureForBinning from preferences
     * @return Current value of adjustExposureForBinning
     */
    public boolean isAdjustExposureForBinning() {
        adjustExposureForBinning = adjustExposureForBinningPrefAdapter.getBoolean();
        return adjustExposureForBinning;
    }

    /**
     * Sets the value of adjustExposureForBinning to preferences
     * @param adjustExposureForBinning New value for adjustExposureForBinning
     */
    public void setAdjustExposureForBinning(boolean adjustExposureForBinning) {
        boolean oldAdjustExposureForBinning = isAdjustExposureForBinning();
        this.adjustExposureForBinning = adjustExposureForBinning;
        adjustExposureForBinningPrefAdapter.setBoolean(adjustExposureForBinning);
        firePropertyChange(PROPERTYNAME_ADJUSTEXPOSUREFORBINNING, oldAdjustExposureForBinning,
            adjustExposureForBinning);
    }

    /**
     * Gets the current value of AutoExposure from preferences
     * @return Current value of AutoExposure
     */
    public boolean isAutoExposure() {
        autoExposure = autoExposurePrefAdapter.getBoolean();
        return autoExposure;
    }

    /**
     * Sets the value of autoExposure to preferences
     * @param autoExposure New value for autoExposure
     */
    public void setAutoExposure(boolean autoExposure) {
        boolean oldAutoExposure = isAutoExposure();
        this.autoExposure = autoExposure;
        autoExposurePrefAdapter.setBoolean(autoExposure);
        firePropertyChange(PROPERTYNAME_AUTOEXPOSURE, oldAutoExposure, autoExposure);
    }

    /**
     * Gets the current value of coolerActive from preferences
     * @return Current value of coolerActive
     */
    public boolean isCoolerActive() {
        coolerActive = coolerActivePrefAdapter.getBoolean();
        return coolerActive;
    }

    /**
     * Sets the value of coolerActive to preferences
     * @param coolerActive New value for coolerActive
     */
    public void setCoolerActive(boolean coolerActive) {
        boolean oldCoolerActive = isCoolerActive();
        this.coolerActive = coolerActive;
        coolerActivePrefAdapter.setBoolean(coolerActive);
        camera.setCoolerActive(coolerActive);
        firePropertyChange(PROPERTYNAME_COOLERACTIVE, oldCoolerActive, coolerActive);
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
        int oldTriggerType = getTriggerType();
        this.triggerType = triggerType;
        firePropertyChange(PROPERTYNAME_TRIGGERTYPE, oldTriggerType, triggerType);
    }

    /**
     * Gets the current value of streamSource
     * @return Current value of streamSource
     */
    public StreamSource getStreamSource() {
        StreamSource s = ((StreamGenerator) camera).getStreamSource();
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
    // @todo streamingEnabled doesn't appear to be utilized at all...
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
        firePropertyChange(PROPERTYNAME_STREAMINGENABLED, oldStreamingEnabled, streamingEnabled);
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

    /**
     * Gets the current value of mirrorImage from preferences
     * @return Current value of mirrorImage
     */
    public boolean isMirrorImage() {
        mirrorImage = mirrorImagePrefAdapter.getBoolean();
        return mirrorImage;
    }

    /**
     * Sets the value of mirrorImage to preferences
     * @param mirrorImage New value for mirrorImage
     */
    public void setMirrorImage(boolean mirrorImage) {
        boolean oldMirrorImage = isMirrorImage();
        this.mirrorImage = mirrorImage;
        mirrorImagePrefAdapter.setBoolean(mirrorImage);
        firePropertyChange(PROPERTYNAME_MIRRORIMAGE, oldMirrorImage, mirrorImage);
    }

    /**
     * Gets the current value of activeCameraSettings
     * @return Current value of activeCameraSettings
     */
    public String getActiveCameraSettings() {
        return activeCameraSettings;
    }

    /**
     * Sets the value of activeCameraSettings
     * @param activeCameraSettings New value for activeCameraSettings
     */
    public void setActiveCameraSettings(String activeCameraSettings) {
        String oldActiveCameraSettings = getActiveCameraSettings();
        this.activeCameraSettings = activeCameraSettings;
        firePropertyChange(PROPERTYNAME_ACTIVECAMERASETTINGS, oldActiveCameraSettings,
            activeCameraSettings);
    }

    /**
     * Gets the current value of binningY from preferences
     * @return Current value of binningY
     */
    public int getZeroIntensity() {
        zeroIntensity = zeroIntensityPrefAdapter.getInt();
        return zeroIntensity;
    }

    /**
     * Sets the value of binningY to preferences
     * @param binningY New value for binningY
     */
    public void setZeroIntensity(int zeroIntensity) {
        int oldZeroIntensity = getZeroIntensity();
        this.zeroIntensity = zeroIntensity;
        zeroIntensityPrefAdapter.setInt(zeroIntensity);
        firePropertyChange(PROPERTYNAME_ZEROINTENSITY, oldZeroIntensity, zeroIntensity);
    }
// <editor-fold defaultstate="collapsed" desc="<<< Non-Bean control methods >>>">
//============================================================================================
    public ArrayList getAvailFormats() {
        return null;
    }

    /* LuCam Specific....///////////////////////////////////////
     * 
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
    camera.startStream();
    }
    });
    
    // this.setStreaming(true);
    }
    
    
    public void stopStream() {
    camera.stopStream();
    this.setStreaming(false);
    }
    
    
    public void closeStreamSource() {
    }
    
     */
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
        String xml = xstream.toXML(this);
        System.out.println(xml);
        try {
            edu.mbl.jif.utils.FileUtil.saveTxtFile(file, xml, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void load(String file) {  // W R O N G  !!
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(this);
        System.out.println(xml);
        try {
            edu.mbl.jif.utils.FileUtil.saveTxtFile(file, xml, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
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
     */
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

    public void measureZeroIntensity() {
        // @todo measureZeroIntensity, implement 
        // prompt user to extinguish illumination
        // measure the zero intensity
        // check for reasonable value
        throw new UnsupportedOperationException("Not yet implemented");
    }
// </editor-fold>
    public static class CameraPresentation extends PresentationModel {

      // SelectionInList's holds the bean's list model plus a selection

      // --- depth
      private final SelectionInList selectionInDepthList;

      // --- binningX
      private final SelectionInList selectionInBinningXList;

      // --- speed
      private final SelectionInList selectionInSpeedList;

    /**
     * Creates a new instance of CameraPresentation
     */
    public CameraPresentation (CameraModel cameraModel) {
         super(cameraModel);

         // --- depth
         selectionInDepthList = new SelectionInList(cameraModel.getDepthListModel(),
               getModel(CameraModel.PROPERTYNAME_DEPTH));
         // --- binningX
         selectionInBinningXList = new SelectionInList(cameraModel.getBinningXListModel(),
               getModel(CameraModel.PROPERTYNAME_BINNINGX));
         // --- speed
         selectionInSpeedList = new SelectionInList(cameraModel.getSpeedListModel(),
               getModel(CameraModel.PROPERTYNAME_SPEED));
    }


    // --- depth
    public SelectionInList getSelectionInDepthList () {
         return selectionInDepthList;
    }

    // --- binningX
    public SelectionInList getSelectionInBinningXList () {
         return selectionInBinningXList;
    }

    // --- speed
    public SelectionInList getSelectionInSpeedList () {
         return selectionInSpeedList;
    }

}
}