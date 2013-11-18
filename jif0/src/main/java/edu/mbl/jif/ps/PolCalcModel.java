package edu.mbl.jif.ps;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ListModel;

public class PolCalcModel extends Model {

    Preferences prefs;
    static final String DEFAULT_PREFS_KEY = "PolCalcModel";
    // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">

    // --- open ---
    public static final String PROPERTYNAME_OPEN = "open";
    private boolean open;
    static final boolean OPEN_DEFAULT = false;
    PreferencesAdapter openPrefAdapter;

    // --- retardanceMax ---
    public static final String PROPERTYNAME_RETARDANCEMAX = "retardanceMax";
    private float retardanceMax;
    static final float RETARDANCEMAX_DEFAULT = 0;
    PreferencesAdapter retardanceMaxPrefAdapter;

    // --- swingFraction ---
    public static final String PROPERTYNAME_SWINGFRACTION = "swingFraction";
    private float swingFraction;
    static final float SWINGFRACTION_DEFAULT = 0.03f;
    PreferencesAdapter swingFractionPrefAdapter;

    // --- wavelength ---
    public static final String PROPERTYNAME_WAVELENGTH = "wavelength";
    private float wavelength;
    static final float WAVELENGTH_DEFAULT = 546f;
    PreferencesAdapter wavelengthPrefAdapter;

    // --- zeroIntensity ---
    public static final String PROPERTYNAME_ZEROINTENSITY = "zeroIntensity";
    private float zeroIntensity;
    static final float ZEROINTENSITY_DEFAULT = 0.0f;
    PreferencesAdapter zeroIntensityPrefAdapter;

    // --- retCeiling ---
    public static final String PROPERTYNAME_RETCEILING = "retCeiling";
    private int retCeiling;
    static final int RETCEILING_DEFAULT = 10;
    PreferencesAdapter retCeilingPrefAdapter;

    // --- algorithm ---
    public static final String PROPERTYNAME_ALGORITHM = "algorithm";
    private int algorithm;
    static final String ALGORITHM_DEFAULT = "5-frame";
    PreferencesAdapter algorithmPrefAdapter;
    public static final String ALGORITHM_ENUM_1 = new String("5-frame");
    public static final String ALGORITHM_ENUM_2 = new String("4-frame");
    public static final String ALGORITHM_ENUM_3 = new String("4-frame NoExt");
    public static final String ALGORITHM_ENUM_4 = new String("3-frame");
    public static final String ALGORITHM_ENUM_5 = new String("3-frame NoExt");
    public static final String ALGORITHM_ENUM_6 = new String("2-frame");
    public static final List ALGORITHM_OPTIONS = Arrays.asList(new String[]{
                ALGORITHM_ENUM_1,
                ALGORITHM_ENUM_2,
                ALGORITHM_ENUM_3,
                ALGORITHM_ENUM_4,
                ALGORITHM_ENUM_5,
                ALGORITHM_ENUM_6
            });
    private ObservableList algorithmListModel;
    private Object algorithmListSelection;

    // --- orientationRef ---
    public static final String PROPERTYNAME_ORIENTATIONREF = "orientationRef";
    private int orientationRef;
    static final int ORIENTATIONREF_DEFAULT = 0;
    PreferencesAdapter orientationRefPrefAdapter;

    // --- dynamicRange ---
    public static final String PROPERTYNAME_DYNAMICRANGE = "dynamicRange";
    private float dynamicRange;
    static final float DYNAMICRANGE_DEFAULT = 1.0f;
    PreferencesAdapter dynamicRangePrefAdapter;

    // --- backGroundStack ---
    public static final String PROPERTYNAME_BACKGROUNDSTACK = "backGroundStack";
    private String backGroundStack;
    static final String BACKGROUNDSTACK_DEFAULT = null;
    PreferencesAdapter backGroundStackPrefAdapter;

    // --- doBkgdCorrection ---
    public static final String PROPERTYNAME_DOBKGDCORRECTION = "doBkgdCorrection";
    private boolean doBkgdCorrection;
    static final boolean DOBKGDCORRECTION_DEFAULT = false;
    PreferencesAdapter doBkgdCorrectionPrefAdapter;

    // --- roiRatioing ---
    public static final String PROPERTYNAME_ROIRATIOING = "roiRatioing";
    private Rectangle roiRatioing;
    static final Rectangle ROIRATIOING_DEFAULT = new Rectangle(0, 0, 0, 0);
    PreferencesAdapter roiRatioingPrefAdapter;

    // --- ratioingAvg ---
//    public static final String PROPERTYNAME_RATIOINGAVG = "ratioingAvg";
//    private double[] ratioingAvg;
//    static final double[] RATIOINGAVG_DEFAULT = new double[5];
//    PreferencesAdapter ratioingAvgPrefAdapter;
     // A way to put/get a rectangle
    // prefs.put("main", String.format("%d,%d,%d,%d", getLocation().x, getLocation().y, getWidth(), getHeight()));
    // Get
//    int lx = 300, ly = 300, x0 = 0, y0 = 0;
//        String geom = PrefsEditor.prefs.get("main", null);
//        if (geom != null) {
//            try {
//                String[] wh = geom.split(",");
//                x0 = Integer.parseInt(wh[0]);
//                y0 = Integer.parseInt(wh[1]);
//                lx = Integer.parseInt(wh[2]);
//                ly = Integer.parseInt(wh[3]);
//            } catch (Exception ex) {
//                // ignore
//            }
//        }
    // --- doRatioing ---
    public static final String PROPERTYNAME_DORATIOING = "doRatioing";
    private boolean doRatioing;
    static final boolean DORATIOING_DEFAULT = false;
    PreferencesAdapter doRatioingPrefAdapter;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
    InstrumentController instrumentCtrl;

    /**
     * Creates a new instance of PolCalcModel
     */
    public PolCalcModel(InstrumentController instrumentCtr) {
        this(instrumentCtr, DEFAULT_PREFS_KEY);
    }

    public PolCalcModel(InstrumentController instrumentCtr, String key) {
        this.instrumentCtrl = instrumentCtr;
        initializePreferencesAdapters(key);

        // Initialize enumerated list for algorithm
        algorithmListModel = new ArrayListModel();
        algorithmListModel.addAll(ALGORITHM_OPTIONS);
        algorithmListSelection = algorithmListModel.get(0);

    }
    // </editor-fold>

    public void initializePreferencesAdapters(String key) {
        prefs = Application.getInstance().getPreferences().node(key);
        openPrefAdapter = 
						new PreferencesAdapter(prefs, 
						PolCalcModel.PROPERTYNAME_OPEN, OPEN_DEFAULT);
        retardanceMaxPrefAdapter = new PreferencesAdapter(prefs, 
						PolCalcModel.PROPERTYNAME_RETARDANCEMAX, RETARDANCEMAX_DEFAULT);
        swingFractionPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_SWINGFRACTION, SWINGFRACTION_DEFAULT);
        wavelengthPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_WAVELENGTH, WAVELENGTH_DEFAULT);
        zeroIntensityPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_ZEROINTENSITY, ZEROINTENSITY_DEFAULT);
        retCeilingPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_RETCEILING, RETCEILING_DEFAULT);
        algorithmPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_ALGORITHM, ALGORITHM_DEFAULT);
        orientationRefPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_ORIENTATIONREF, ORIENTATIONREF_DEFAULT);
        dynamicRangePrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_DYNAMICRANGE, DYNAMICRANGE_DEFAULT);
        backGroundStackPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_BACKGROUNDSTACK, BACKGROUNDSTACK_DEFAULT);
        doBkgdCorrectionPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_DOBKGDCORRECTION, DOBKGDCORRECTION_DEFAULT);
        roiRatioingPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_ROIRATIOING, ROIRATIOING_DEFAULT);
        //ratioingAvgPrefAdapter =
        //    new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_RATIOINGAVG, RATIOINGAVG_DEFAULT);
        doRatioingPrefAdapter =
                new PreferencesAdapter(prefs, PolCalcModel.PROPERTYNAME_DORATIOING, DORATIOING_DEFAULT);
    }
    // <editor-fold defaultstate="collapsed" desc="<<< Accessors >>>">

    /**
     * Gets the current value of open from preferences
     * @return Current value of open
     */
    public boolean isOpen() {
        open = openPrefAdapter.getBoolean();
        return open;
    }

    /**
     * Sets the value of open to preferences
     * @param open New value for open
     */
    public void setOpen(boolean open) {
        boolean oldOpen = isOpen();
        this.open = open;
        openPrefAdapter.setBoolean(open);
        firePropertyChange(PROPERTYNAME_OPEN, oldOpen, open);
    }

    /**
     * Gets the current value of retardanceMax from preferences
     * @return Current value of retardanceMax
     */
    public float getRetardanceMax() {
        retardanceMax = retardanceMaxPrefAdapter.getFloat();
        return retardanceMax;
    }

    /**
     * Sets the value of retardanceMax to preferences
     * @param retardanceMax New value for retardanceMax
     */
    public void setRetardanceMax(float retardanceMax) {
        float oldRetardanceMax = getRetardanceMax();
        this.retardanceMax = retardanceMax;
        retardanceMaxPrefAdapter.setFloat(retardanceMax);
        firePropertyChange(PROPERTYNAME_RETARDANCEMAX, oldRetardanceMax, retardanceMax);
    }

    /**
     * Gets the current value of swingFraction from preferences
     * @return Current value of swingFraction
     */
    public float getSwingFraction() {
        swingFraction = swingFractionPrefAdapter.getFloat();
        return swingFraction;
    }

    /**
     * Sets the value of swingFraction to preferences
     * @param swingFraction New value for swingFraction
     */
    public void setSwingFraction(float swingFraction) {
        float oldSwingFraction = getSwingFraction();
        this.swingFraction = swingFraction;
        swingFractionPrefAdapter.setFloat(swingFraction);
        firePropertyChange(PROPERTYNAME_SWINGFRACTION, oldSwingFraction, swingFraction);
    }

    /**
     * Gets the current value of wavelength from preferences
     * @return Current value of wavelength
     */
    public float getWavelength() {
        wavelength = wavelengthPrefAdapter.getFloat();
        return wavelength;
    }

    /**
     * Sets the value of wavelength to preferences
     * @param wavelength New value for wavelength
     */
    public void setWavelength(float wavelength) {
        float oldWavelength = getWavelength();
        this.wavelength = wavelength;
        wavelengthPrefAdapter.setFloat(wavelength);
        firePropertyChange(PROPERTYNAME_WAVELENGTH, oldWavelength, wavelength);
    }

    /**
     * Gets the current value of zeroIntensity from preferences
     * @return Current value of zeroIntensity
     */
    public float getZeroIntensity() {
        zeroIntensity = zeroIntensityPrefAdapter.getFloat();
        return zeroIntensity;
    }

    /**
     * Sets the value of zeroIntensity to preferences
     * @param zeroIntensity New value for zeroIntensity
     */
    public void setZeroIntensity(float zeroIntensity) {
        float oldZeroIntensity = getZeroIntensity();
        this.zeroIntensity = zeroIntensity;
        zeroIntensityPrefAdapter.setFloat(zeroIntensity);
        firePropertyChange(PROPERTYNAME_ZEROINTENSITY, oldZeroIntensity, zeroIntensity);
    }

    /**
     * Gets the current value of retCeiling from preferences
     * @return Current value of retCeiling
     */
    public int getRetCeiling() {
        retCeiling = retCeilingPrefAdapter.getInt();
        return retCeiling;
    }

    /**
     * Sets the value of retCeiling to preferences
     * @param retCeiling New value for retCeiling
     */
    public void setRetCeiling(int retCeiling) {
        int oldRetCeiling = getRetCeiling();
        this.retCeiling = retCeiling;
        retCeilingPrefAdapter.setInt(retCeiling);
        firePropertyChange(PROPERTYNAME_RETCEILING, oldRetCeiling, retCeiling);
    }

    /**
     * Gets the current value of algorithm from preferences
     * @return Current value of algorithm
     */
    public int getAlgorithm() {
        algorithm = algorithmPrefAdapter.getInt();
        return algorithm;
    }

    /**
     * Sets the value of algorithm to preferences
     * @param algorithm New value for algorithm
     */
    public void setAlgorithm(int algorithm) {
        int oldAlgorithm = getAlgorithm();
        this.algorithm = algorithm;
        algorithmPrefAdapter.setInt(algorithm);
        firePropertyChange(PROPERTYNAME_ALGORITHM, oldAlgorithm, algorithm);
    }

    /**
     * ListModel accessor for algorithm
     * @return ListModel of algorithm
     */
    public ListModel getAlgorithmListModel() {
        return algorithmListModel;
    }

    /**
     * ListSelection accessor for algorithm
     * @return ListSelection of algorithm
     */
    public Object getAlgorithmListSelection() {
        return algorithmListSelection;
    }

    /**
     * Gets the current value of orientationRef from preferences
     * @return Current value of orientationRef
     */
    public int getOrientationRef() {
        orientationRef = orientationRefPrefAdapter.getInt();
        return orientationRef;
    }

    /**
     * Sets the value of orientationRef to preferences
     * @param orientationRef New value for orientationRef
     */
    public void setOrientationRef(int orientationRef) {
        int oldOrientationRef = getOrientationRef();
        this.orientationRef = orientationRef;
        orientationRefPrefAdapter.setInt(orientationRef);
        firePropertyChange(PROPERTYNAME_ORIENTATIONREF, oldOrientationRef, orientationRef);
    }

    /**
     * Gets the current value of dynamicRange from preferences
     * @return Current value of dynamicRange
     */
    public float getDynamicRange() {
        dynamicRange = dynamicRangePrefAdapter.getFloat();
        return dynamicRange;
    }

    /**
     * Sets the value of dynamicRange to preferences
     * @param dynamicRange New value for dynamicRange
     */
    public void setDynamicRange(float dynamicRange) {
        float oldDynamicRange = getDynamicRange();
        this.dynamicRange = dynamicRange;
        dynamicRangePrefAdapter.setFloat(dynamicRange);
        firePropertyChange(PROPERTYNAME_DYNAMICRANGE, oldDynamicRange, dynamicRange);
    }

    /**
     * Gets the current value of backGroundStack from preferences
     * @return Current value of backGroundStack
     */
    public String getBackGroundStack() {
        backGroundStack = backGroundStackPrefAdapter.getString();
        return backGroundStack;
    }

    /**
     * Sets the value of backGroundStack to preferences
     * @param backGroundStack New value for backGroundStack
     */
    public void setBackGroundStack(String backGroundStack) {
        String oldBackGroundStack = getBackGroundStack();
        this.backGroundStack = backGroundStack;
        backGroundStackPrefAdapter.setString(backGroundStack);
        firePropertyChange(PROPERTYNAME_BACKGROUNDSTACK, oldBackGroundStack, backGroundStack);
    }

    /**
     * Gets the current value of doBkgdCorrection from preferences
     * @return Current value of doBkgdCorrection
     */
    public boolean isDoBkgdCorrection() {
        doBkgdCorrection = doBkgdCorrectionPrefAdapter.getBoolean();
        return doBkgdCorrection;
    }

    /**
     * Sets the value of doBkgdCorrection to preferences
     * @param doBkgdCorrection New value for doBkgdCorrection
     */
    public void setDoBkgdCorrection(boolean doBkgdCorrection) {
        boolean oldDoBkgdCorrection = isDoBkgdCorrection();
        this.doBkgdCorrection = doBkgdCorrection;
        doBkgdCorrectionPrefAdapter.setBoolean(doBkgdCorrection);
        firePropertyChange(PROPERTYNAME_DOBKGDCORRECTION, oldDoBkgdCorrection, doBkgdCorrection);
    }

    /**
     * Gets the current value of roiRatioing from preferences
     * @return Current value of roiRatioing
     */
//    public Rectangle getRoiRatioing() {
//        roiRatioing = roiRatioingPrefAdapter.getRectangle();
//         return roiRatioing;
//    }
    /**
     * Sets the value of roiRatioing to preferences
     * @param roiRatioing New value for roiRatioing
     */
//    public void setRoiRatioing(Rectangle roiRatioing) {
//        Rectangle oldRoiRatioing = getRoiRatioing();
//        this.roiRatioing = roiRatioing;
//        roiRatioingPrefAdapter.setRectangle(roiRatioing);
//        firePropertyChange(PROPERTYNAME_ROIRATIOING, oldRoiRatioing, roiRatioing);
//    }
    /**
     * Gets the current value of doRatioing from preferences
     * @return Current value of doRatioing
     */
    public boolean isDoRatioing() {
        doRatioing = doRatioingPrefAdapter.getBoolean();
        return doRatioing;
    }

    /**
     * Sets the value of doRatioing to preferences
     * @param doRatioing New value for doRatioing
     */
    public void setDoRatioing(boolean doRatioing) {
        boolean oldDoRatioing = isDoRatioing();
        this.doRatioing = doRatioing;
        doRatioingPrefAdapter.setBoolean(doRatioing);
        firePropertyChange(PROPERTYNAME_DORATIOING, oldDoRatioing, doRatioing);
    }

    // </editor-fold>
    //
    public static class PolCalcPresentation extends PresentationModel {

        // SelectionInList's holds the bean's list model plus a selection

        // --- algorithm
        private final SelectionInList selectionInAlgorithmList;

        /**
         * Creates a new instance of PolCalcModelPresentation
         */
        public PolCalcPresentation(PolCalcModel polCalcModel) {
            super(polCalcModel);

            // --- algorithm
            selectionInAlgorithmList = new SelectionInList(polCalcModel.getAlgorithmListModel(),
                    getModel(PolCalcModel.PROPERTYNAME_ALGORITHM));
        }


        // --- algorithm
        public SelectionInList getSelectionInAlgorithmList() {
            return selectionInAlgorithmList;
        }
    }
}