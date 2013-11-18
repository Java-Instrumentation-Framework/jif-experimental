package edu.mbl.jif.varilc.sap;

import edu.mbl.jif.gui.bean.MemorableBean;

/**
 *
 * @author GBH
 */
public class SLMModel extends MemorableBean {

//    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
//
//
//    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
//        propertyChangeSupport.addPropertyChangeListener(listener);
//    }
//
//
//    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
//        propertyChangeSupport.removePropertyChangeListener(listener);
//    }

    public SLMModel() {
    }
    public SLMModel(String filename) {
        super.autoSaveFilename = filename;
    }
    
    // Default starting values
    public final static int circMin = 1650;
    public final static int circMax = 850;
    public final static int ellipMin = 1650;
    public final static int ellipMax = 850;
    public final static int retardAMax = 1650;
    public final static int retardBMax = 850;
    public final static int extA = 1559;
    public final static int extB = 1586;

    // for characterization/response curves
    public final static double startMin = 1300;
    public final static double startMax = 1800;
    public final static int numPoints = 340;

        // for characterization/response curves
//    public final static double startMin = 600;
//    public final static double startMax = 4000;
//    public final static int numPoints = 340;
    // Operating Ranges for calibration limits
    // Range for Min Transmission
    public final static int HuntRangeMasksMin_a = 1100;
    public final static int HuntRangeMasksMin_b = 2000;
    // Range for Max Transmission
    public final static int HuntRangeMasksMax_a = 600;
    public final static int HuntRangeMasksMax_b = 1100;
    // Range for Retarders
    public final static int HuntRangeRetarderA_a = 900;
    public final static int HuntRangeRetarderA_b = 2400;
    public final static int HuntRangeRetarderB_a = 900;
    public final static int HuntRangeRetarderB_b = 2400;
    
    public final static boolean useMockRetarders = false;
    
    // -----------------------------------------------------------------
  //  public final static double swingFactor = 20.5;
    // @todo swingEquivalent
//    public final static int swingEquivalent = //(int) (swing * swingFactor);
//            50;
    static double deltaExtinct = 50;    
// -----------------------------------------------------------------
    private double wavelength = 548;
    public static final String PROP_WAVELENGTH = "wavelength";

    
    public double getWavelength() {
        return this.wavelength;
    }


    public void setWavelength(double newwavelength) {
        double oldwavelength = wavelength;
        this.wavelength = newwavelength;
        firePropertyChange(PROP_WAVELENGTH, oldwavelength,
                newwavelength);
    }

// -----------------------------------------------------------------
    private double swing = 0.10;
    public static final String PROP_SWING = "swing";


    public double getSwing() {
        return this.swing;
    }


    public void setSwing(double newswing) {
        double oldswing = swing;
        this.swing = newswing;
        firePropertyChange(PROP_SWING, oldswing, newswing);
    }

// -----------------------------------------------------------------
    private int settlingtime = 50;
    public static final String PROP_SETTLINGTIME = "settlingtime";


    public int getSettlingtime() {
        return this.settlingtime;
    }


    public void setSettlingtime(int newsettlingtime) {
        int oldsettlingtime = settlingtime;
        this.settlingtime = newsettlingtime;
        firePropertyChange(PROP_SETTLINGTIME,
                oldsettlingtime, newsettlingtime);
    }

// -----------------------------------------------------------------
    private double zeroIntensity = 0;
    public static final String PROP_ZEROINTENSITY = "zeroIntensity";


    public double getZeroIntensity() {
        return this.zeroIntensity;
    }


    public void setZeroIntensity(double newzeroIntensity) {
        double oldzeroIntensity = zeroIntensity;
        this.zeroIntensity = newzeroIntensity;
        firePropertyChange(PROP_ZEROINTENSITY,
                oldzeroIntensity, newzeroIntensity);
    }

// -----------------------------------------------------------------
    private double tolCoarse = 4.0;
    public static final String PROP_TOLCOARSE = "tolCoarse";


    public double getTolCoarse() {
        return this.tolCoarse;
    }


    public void setTolCoarse(double newtolCoarse) {
        double oldtolCoarse = tolCoarse;
        this.tolCoarse = newtolCoarse;
        firePropertyChange(PROP_TOLCOARSE, oldtolCoarse,
                newtolCoarse);
    }
    
// -----------------------------------------------------------------
    private double tolFine = 1.0;
    public static final String PROP_TOLFINE = "tolFine";


    public double getTolFine() {
        return this.tolFine;
    }


    public void setTolFine(double newtolFine) {
        double oldtolFine = tolFine;
        this.tolFine = newtolFine;
        firePropertyChange(PROP_TOLFINE, oldtolFine,
                newtolFine);
    }

    /* ----- huntRangeA_a ----------------------------------------------*/

    private int huntRangeA_a = 800;
    public static final String PROP_HUNTRANGEA_A = "huntRangeA_a";


    public int getHuntRangeA_a() {
        return this.huntRangeA_a;
    }


    public void setHuntRangeA_a(int newhuntRangeA_a) {
        int oldhuntRangeA_a = huntRangeA_a;
        this.huntRangeA_a = newhuntRangeA_a;
        firePropertyChange(PROP_HUNTRANGEA_A,
                oldhuntRangeA_a, newhuntRangeA_a);
    }




    // -----------------------------------------------------------------


//    public void save(String filename) {
//        try {
//            ObjectStoreXML.write(this, filename);
//        } catch (Throwable ex) {
//            Logger.getLogger(SLMModel.class.getName()).log(Level.SEVERE, null,
//                    ex);
//        }
//    }
//
//
//    public static SLMModel restore(String filename) {
//        SLMModel model = new SLMModel();
//        try {
//            model = (SLMModel) ObjectStoreXML.read(filename);
//        } catch (Throwable ex) {
//            Logger.getLogger(SLMModel.class.getName()).log(Level.SEVERE, null,
//                    ex);
//        }
//        return model;
//    }
}
