/*
 * CompensatorModel.java
 *
 * Created on February 11, 2007, 5:08 PM
 *
 */

package edu.mbl.jif.varilc;

import com.jgoodies.binding.beans.Model;               
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import java.util.prefs.Preferences;                    

public class CompensatorModel extends Model {

    Preferences prefs = CamAcqJ.getInstance().getPreferences();
    // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">

    // --- choosen ---
    public static final String PROPERTYNAME_CHOOSEN = "choosen";
    private boolean choosen = false;

    // --- setA ---
    public static final String PROPERTYNAME_SETA = "setA";
    private double setA = 0.25;

    // --- setB ---
    public static final String PROPERTYNAME_SETB = "setB";
    private double setB = 0.5;

    // --- intensity ---
    public static final String PROPERTYNAME_INTENSITY = "intensity";
    private int intensity = 0;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
    VariLCModel vlcm;

  /**
     * Creates a new instance of _
     */
    public CompensatorModel(VariLCModel vlcm) {

        this.vlcm = vlcm;

    }
    // </editor-fold>

    /**
     * Gets the current value of choosen
     * @return Current value of choosen
     */
    public boolean isChoosen() {
        return choosen;
    }

    /**
     * Sets the value of choosen
     * @param choosen New value for choosen
     */
    public void setChoosen(boolean choosen) {
        boolean oldChoosen = isChoosen();
        this.choosen = choosen;
        firePropertyChange(PROPERTYNAME_CHOOSEN, oldChoosen, choosen);
    }

    /**
     * Gets the current value of setA
     * @return Current value of setA
     */
    public double getSetA() {
        return setA;
    }

    /**
     * Sets the value of setA
     * @param setA New value for setA
     */
    public void setSetA(double setA) {
        double oldSetA = getSetA();
        this.setA = setA;
        firePropertyChange(PROPERTYNAME_SETA, oldSetA, setA);
    }

    /**
     * Gets the current value of setB
     * @return Current value of setB
     */
    public double getSetB() {
        return setB;
    }

    /**
     * Sets the value of setB
     * @param setB New value for setB
     */
    public void setSetB(double setB) {
        double oldSetB = getSetB();
        this.setB = setB;
        firePropertyChange(PROPERTYNAME_SETB, oldSetB, setB);
    }

    /**
     * Gets the current value of intensity
     * @return Current value of intensity
     */
    public int getIntensity() {
        return intensity;
    }

    /**
     * Sets the value of intensity
     * @param intensity New value for intensity
     */
    public void setIntensity(int intensity) {
        int oldIntensity = getIntensity();
        this.intensity = intensity;
        firePropertyChange(PROPERTYNAME_INTENSITY, oldIntensity, intensity);
    }

}