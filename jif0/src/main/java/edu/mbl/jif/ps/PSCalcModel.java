package edu.mbl.jif.ps;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *  This will be replaced by PolCalcModel which uses JGoodies Binding
 * @author GBH
 */
public class PSCalcModel {
    // PolStack parameters
/*
    private int retCeiling = 10; // maximum image retardance in nm
    private int algorithm = 5;;
    private int orientationRef = 0; // orientation reference in (whole) degrees
    
    // Background correction - filename of stack used for correction in magImage
    private String backGroundStack = null;
    private boolean doBkgdCorrection = false;
    
    // Ratioing Correction
    private Rectangle roiRatioing = new Rectangle(0, 0, 0, 0);
    private double[] ratioingAvg = null; // for each slice
    private boolean doRatioing = false;
*/
    
    protected double wavelength = 540; // wavelength in nm ~ 546 nm
    public static final String PROP_WAVELENGTH = "wavelength";

    public double getWavelength() {
        return wavelength;
    }

    public void setWavelength(double wavelength) {
        double oldWavelength = this.wavelength;
        this.wavelength = wavelength;
        propertyChangeSupport.firePropertyChange(PROP_WAVELENGTH, oldWavelength, wavelength);
    }
    
    // SwingFraction
    protected double swingFraction = 0.03; // =swing/wavelength ~ 0.03
    public static final String PROP_SWINGFRACTION = "swingFraction";

    public double getSwingFraction() {
        return swingFraction;
    }

    public void setSwingFraction(double swingFraction) {
        double oldSwingFraction = this.swingFraction;
        this.swingFraction = swingFraction;
        propertyChangeSupport.firePropertyChange(PROP_SWINGFRACTION, oldSwingFraction, swingFraction);
    }

    protected double retCeiling = 25;
    public static final String PROP_RETCEILING = "retCeiling";

    public double getRetCeiling() {
        return retCeiling;
    }

    public void setRetCeiling(double retCeiling) {
        double oldRetCeiling = this.retCeiling;
        this.retCeiling = retCeiling;
        propertyChangeSupport.firePropertyChange(PROP_RETCEILING, oldRetCeiling, retCeiling);
    }

    protected double refAngle = 0;  // orientation reference in (whole) degrees
    public static final String PROP_REFANGLE = "refAngle";

    public double getRefAngle() {
        return refAngle;
    }

    public void setRefAngle(double refAngle) {
        double oldRefAngle = this.refAngle;
        this.refAngle = refAngle;
        propertyChangeSupport.firePropertyChange(PROP_REFANGLE, oldRefAngle, refAngle);
    }

    protected double zeroIntensity = 4;
    public static final String PROP_ZEROINTENSITY = "zeroIntensity";

    public double getZeroIntensity() {
        return zeroIntensity;
    }

    public void setZeroIntensity(double zeroIntensity) {
        double oldZeroIntensity = this.zeroIntensity;
        this.zeroIntensity = zeroIntensity;
        propertyChangeSupport.firePropertyChange(PROP_ZEROINTENSITY, oldZeroIntensity, zeroIntensity);
    }

    protected boolean mirrorInPath = false;
    public static final String PROP_MIRRORINPATH = "mirrorInPath";

    public boolean isMirrorInPath() {
        return mirrorInPath;
    }

    public void setMirrorInPath(boolean mirrorInPath) {
        boolean oldMirrorInPath = this.mirrorInPath;
        this.mirrorInPath = mirrorInPath;
        propertyChangeSupport.firePropertyChange(PROP_MIRRORINPATH, oldMirrorInPath, mirrorInPath);
    }
    //-------------------------------------------
    protected boolean fastCalc = false;
    public static final String PROP_FASTCALC = "fastCalc";

    public boolean isFastCalc() {
        return fastCalc;
    }

    public void setFastCalc(boolean fastCalc) {
        boolean oldFastCalc = this.fastCalc;
        this.fastCalc = fastCalc;
        propertyChangeSupport.firePropertyChange(PROP_FASTCALC, oldFastCalc, fastCalc);
    }

    protected String calcPlugin = "Calc_5Fr";
    public static final String PROP_CALCPLUGIN = "calcPlugin";

    public String getCalcPlugin() {
        return calcPlugin;
    }

    public void setCalcPlugin(String calcPlugin) {
        String oldCalcPlugin = this.calcPlugin;
        this.calcPlugin = calcPlugin;
        propertyChangeSupport.firePropertyChange(PROP_CALCPLUGIN, oldCalcPlugin, calcPlugin);
    }

    protected boolean autoCalc = false;
    public static final String PROP_AUTOCALC = "autoCalc";

    public boolean isAutoCalc() {
        return autoCalc;
    }

    public void setAutoCalc(boolean autoCalc) {
        boolean oldAutoCalc = this.autoCalc;
        this.autoCalc = autoCalc;
        propertyChangeSupport.firePropertyChange(PROP_AUTOCALC, oldAutoCalc, autoCalc);
    }
    // PropertyChangeSupport =============================================================
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
