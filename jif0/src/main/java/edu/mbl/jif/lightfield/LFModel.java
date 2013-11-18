package edu.mbl.jif.lightfield;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;               
import com.jgoodies.binding.list.ArrayListModel;       
import com.jgoodies.binding.list.ObservableList;       
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import java.util.Arrays;                               
import java.util.List;                                 
import java.util.prefs.Preferences;                    
import javax.swing.ListModel;                          


public class LFModel extends Model {

    Preferences prefs = CamAcqJ.getInstance().getPreferences();

    // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">

    // --- pitchInt ---
    public static final String PROPERTYNAME_PITCHINT = "pitchInt";
    private int pitchInt;
    static final int PITCHINT_DEFAULT = 17;
    PreferencesAdapter pitchIntPrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_PITCHINT, PITCHINT_DEFAULT);

    // --- pitch ---
    public static final String PROPERTYNAME_PITCH = "pitch";
    private double pitch;
    static final double PITCH_DEFAULT = 16.95;
    PreferencesAdapter pitchPrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_PITCH, PITCH_DEFAULT);

    // --- offsetX ---
    public static final String PROPERTYNAME_OFFSETX = "offsetX";
    private int offsetX;
    static final int OFFSETX_DEFAULT = 7;
    PreferencesAdapter offsetXPrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_OFFSETX, OFFSETX_DEFAULT);

    // --- offsetY ---
    public static final String PROPERTYNAME_OFFSETY = "offsetY";
    private int offsetY;
    static final int OFFSETY_DEFAULT = 7;
    PreferencesAdapter offsetYPrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_OFFSETY, OFFSETY_DEFAULT);

    // --- displacementX ---
    public static final String PROPERTYNAME_DISPLACEMENTX = "displacementX";
    private int displacementX;
    static final int DISPLACEMENTX_DEFAULT = 0;
    PreferencesAdapter displacementXPrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_DISPLACEMENTX, DISPLACEMENTX_DEFAULT);

    // --- displacementY ---
    public static final String PROPERTYNAME_DISPLACEMENTY = "displacementY";
    private int displacementY;
    static final int DISPLACEMENTY_DEFAULT = 0;
    PreferencesAdapter displacementYPrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_DISPLACEMENTY, DISPLACEMENTY_DEFAULT);

    // --- interpolate ---
    public static final String PROPERTYNAME_INTERPOLATE = "interpolate";
    private boolean interpolate;
    static final boolean INTERPOLATE_DEFAULT = true;
    PreferencesAdapter interpolatePrefAdapter = 
        new PreferencesAdapter(prefs, LFModel.PROPERTYNAME_INTERPOLATE, INTERPOLATE_DEFAULT);
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
    InstrumentController instrumentCtrl;

  /**
     * Creates a new instance of LF
     */
    public LFModel(InstrumentController instrumentCtr) {

        this.instrumentCtrl = instrumentCtrl;

    }
    // </editor-fold>

    /**
     * Gets the current value of pitchInt from preferences
     * @return Current value of pitchInt
     */
    public int getPitchInt() {
        pitchInt = pitchIntPrefAdapter.getInt();
         return pitchInt;
    }

    /**
     * Sets the value of pitchInt to preferences
     * @param pitchInt New value for pitchInt
     */
    public void setPitchInt(int pitchInt) {
        int oldPitchInt = getPitchInt();
        this.pitchInt = pitchInt;
        pitchIntPrefAdapter.setInt(pitchInt);
        firePropertyChange(PROPERTYNAME_PITCHINT, oldPitchInt, pitchInt);
    }

    /**
     * Gets the current value of pitch from preferences
     * @return Current value of pitch
     */
    public double getPitch() {
        pitch = pitchPrefAdapter.getDouble();
         return pitch;
    }

    /**
     * Sets the value of pitch to preferences
     * @param pitch New value for pitch
     */
    public void setPitch(double pitch) {
        double oldPitch = getPitch();
        this.pitch = pitch;
        pitchPrefAdapter.setDouble(pitch);
        firePropertyChange(PROPERTYNAME_PITCH, oldPitch, pitch);
    }

    /**
     * Gets the current value of offsetX from preferences
     * @return Current value of offsetX
     */
    public int getOffsetX() {
        offsetX = offsetXPrefAdapter.getInt();
         return offsetX;
    }

    /**
     * Sets the value of offsetX to preferences
     * @param offsetX New value for offsetX
     */
    public void setOffsetX(int offsetX) {
        int oldOffsetX = getOffsetX();
        this.offsetX = offsetX;
        offsetXPrefAdapter.setInt(offsetX);
        firePropertyChange(PROPERTYNAME_OFFSETX, oldOffsetX, offsetX);
    }

    /**
     * Gets the current value of offsetY from preferences
     * @return Current value of offsetY
     */
    public int getOffsetY() {
        offsetY = offsetYPrefAdapter.getInt();
         return offsetY;
    }

    /**
     * Sets the value of offsetY to preferences
     * @param offsetY New value for offsetY
     */
    public void setOffsetY(int offsetY) {
        int oldOffsetY = getOffsetY();
        this.offsetY = offsetY;
        offsetYPrefAdapter.setInt(offsetY);
        firePropertyChange(PROPERTYNAME_OFFSETY, oldOffsetY, offsetY);
    }

    /**
     * Gets the current value of displacementX from preferences
     * @return Current value of displacementX
     */
    public int getDisplacementX() {
        displacementX = displacementXPrefAdapter.getInt();
         return displacementX;
    }

    /**
     * Sets the value of displacementX to preferences
     * @param displacementX New value for displacementX
     */
    public void setDisplacementX(int displacementX) {
        int oldDisplacementX = getDisplacementX();
        this.displacementX = displacementX;
        displacementXPrefAdapter.setInt(displacementX);
        firePropertyChange(PROPERTYNAME_DISPLACEMENTX, oldDisplacementX, displacementX);
    }

    /**
     * Gets the current value of displacementY from preferences
     * @return Current value of displacementY
     */
    public int getDisplacementY() {
        displacementY = displacementYPrefAdapter.getInt();
         return displacementY;
    }

    /**
     * Sets the value of displacementY to preferences
     * @param displacementY New value for displacementY
     */
    public void setDisplacementY(int displacementY) {
        int oldDisplacementY = getDisplacementY();
        this.displacementY = displacementY;
        displacementYPrefAdapter.setInt(displacementY);
        firePropertyChange(PROPERTYNAME_DISPLACEMENTY, oldDisplacementY, displacementY);
    }

    /**
     * Gets the current value of interpolate from preferences
     * @return Current value of interpolate
     */
    public boolean isInterpolate() {
        interpolate = interpolatePrefAdapter.getBoolean();
         return interpolate;
    }

    /**
     * Sets the value of interpolate to preferences
     * @param interpolate New value for interpolate
     */
    public void setInterpolate(boolean interpolate) {
        boolean oldInterpolate = isInterpolate();
        this.interpolate = interpolate;
        interpolatePrefAdapter.setBoolean(interpolate);
        firePropertyChange(PROPERTYNAME_INTERPOLATE, oldInterpolate, interpolate);
    }

}