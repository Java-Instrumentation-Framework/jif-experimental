/*
 * IllumModel.java
 *
 * Created on April 10, 2007, 3:12 PM
 */
package edu.mbl.jif.microscope.illum;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.config.DeviceManager;
import edu.mbl.jif.microscope.ZeissAxiovert200M;
import java.util.prefs.Preferences;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionManager;

public class IllumModel
    extends Model {
    // use InstrumentController.getModel("illum")
    Preferences prefs = CamAcqJ.getInstance().getPreferences();
    //
    // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">
    //
    // --- openXmis ---
    public static final String PROPERTYNAME_OPENXMIS = "openXmis";
    private boolean openXmis;
    static final boolean OPENXMIS_DEFAULT = false;
    PreferencesAdapter openXmisPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_OPENXMIS, OPENXMIS_DEFAULT);
    // --- openEpi ---
    public static final String PROPERTYNAME_OPENEPI = "openEpi";
    private boolean openEpi;
    static final boolean OPENEPI_DEFAULT = false;
    PreferencesAdapter openEpiPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_OPENEPI, OPENEPI_DEFAULT);
    // --- levelXmis ---
    public static final String PROPERTYNAME_LEVELXMIS = "levelXmis";
    private double levelXmis;
    static final double LEVELXMIS_DEFAULT = 1;
    PreferencesAdapter levelXmisPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_LEVELXMIS, LEVELXMIS_DEFAULT);
    // --- levelEpi ---
    public static final String PROPERTYNAME_LEVELEPI = "levelEpi";
    private double levelEpi;
    static final double LEVELEPI_DEFAULT = 1;
    PreferencesAdapter levelEpiPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_LEVELEPI, LEVELEPI_DEFAULT);
    // --- commPortXmis ---
    public static final String PROPERTYNAME_COMMPORTXMIS = "commPortXmis";
    private String commPortXmis;
    static final String COMMPORTXMIS_DEFAULT = "COM1";
    PreferencesAdapter commPortXmisPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_COMMPORTXMIS, COMMPORTXMIS_DEFAULT);
    // --- commPortEpi ---
    public static final String PROPERTYNAME_COMMPORTEPI = "commPortEpi";
    private String commPortEpi;
    static final String COMMPORTEPI_DEFAULT = "COM2";
    PreferencesAdapter commPortEpiPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_COMMPORTEPI, COMMPORTEPI_DEFAULT);
    // --- shutterTypeEpi ---
    public static final String PROPERTYNAME_SHUTTERTYPEEPI = "shutterTypeEpi";
    private String shutterTypeEpi;
    static final String SHUTTERTYPEEPI_DEFAULT = "ZEISS"; //UNIBLITZ_1" or "UNIBLITZ_2" or "ZEISS"
    PreferencesAdapter shutterTypeEpiPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_SHUTTERTYPEEPI, SHUTTERTYPEEPI_DEFAULT);
    // --- shutterTypeXmis ---
    public static final String PROPERTYNAME_SHUTTERTYPEXMIS = "shutterTypeXmis";
    private String shutterTypeXmis;
    static final String SHUTTERTYPEXMIS_DEFAULT = "UNIBLITZ_1"; // "UNIBLITZ_2" or "ZEISS"
    PreferencesAdapter shutterTypeXmisPrefAdapter =
        new PreferencesAdapter(prefs, IllumModel.PROPERTYNAME_SHUTTERTYPEXMIS,
        SHUTTERTYPEXMIS_DEFAULT);
    // </editor-fold>
    //
    //
    // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
    //
    InstrumentController instrumentCtrl;
//    public SerialPortConnection portXmis;
//    public SerialPortConnection portEpi;
    Shutter shutterXmis;
    Shutter shutterEpi;

    /**
     * Creates a new instance of Illum
     */
    public IllumModel(InstrumentController instrumentCtrl, Shutter epiDevice, Shutter xmisDevice) {
        this.instrumentCtrl = instrumentCtrl;
        if (epiDevice != null) {
            this.shutterEpi = epiDevice;
        }
        if (xmisDevice != null) {
            this.shutterXmis = xmisDevice;
        }
    }
    // </editor-fold>
    /**
     * Gets the current value of openXmis from preferences
     * @return Current value of openXmis
     */
    public boolean isOpenXmis() {
        openXmis = openXmisPrefAdapter.getBoolean();
        return openXmis;
    }

    /**
     * Sets the value of openXmis to preferences
     * @param openXmis New value for openXmis
     */
    public void setOpenXmis(boolean openXmis) {
        boolean oldOpenXmis = isOpenXmis();
        this.openXmis = openXmis;
        openXmisPrefAdapter.setBoolean(openXmis);
        if (openXmis) {
            shutterXmis.setOpen(true);
        } else {
            shutterXmis.setOpen(false);
        }
        ((AbstractActionExt) ActionManager.getInstance().getAction("toggleXmisShutter")).setSelected(
            !openXmis);
        firePropertyChange(PROPERTYNAME_OPENXMIS, oldOpenXmis, openXmis);
    }

    /**
     * Gets the current value of openEpi from preferences
     * @return Current value of openEpi
     */
    public boolean isOpenEpi() {
        openEpi = openEpiPrefAdapter.getBoolean();
        return openEpi;
    }

    /**
     * Sets the value of openEpi to preferences
     *
     *
     * @param openEpi New value for openEpi
     */
    public void setOpenEpi(boolean openEpi) {
        boolean oldOpenEpi = isOpenEpi();
        this.openEpi = openEpi;
        openEpiPrefAdapter.setBoolean(openEpi);
        if (openEpi) {
            shutterEpi.setOpen(true);
        } else {
            shutterEpi.setOpen(false);
        }
        ((AbstractActionExt) ActionManager.getInstance().getAction("toggleEpiShutter")).setSelected(
            !openEpi);
        firePropertyChange(PROPERTYNAME_OPENEPI, oldOpenEpi, openEpi);
    }

    /**
     * Gets the current value of levelXmis from preferences
     * @return Current value of levelXmis
     */
    public double getLevelXmis() {
        levelXmis = levelXmisPrefAdapter.getDouble();
        return levelXmis;
    }

    /**
     * Sets the value of levelXmis to preferences
     * @param levelXmis New value for levelXmis
     */
    public void setLevelXmis(double levelXmis) {
        double oldLevelXmis = getLevelXmis();
        this.levelXmis = levelXmis;
        levelXmisPrefAdapter.setDouble(levelXmis);
        firePropertyChange(PROPERTYNAME_LEVELXMIS, oldLevelXmis, levelXmis);
    }

    /**
     * Gets the current value of levelEpi from preferences
     * @return Current value of levelEpi
     */
    public double getLevelEpi() {
        levelEpi = levelEpiPrefAdapter.getDouble();
        return levelEpi;
    }

    /**
     * Sets the value of levelEpi to preferences
     * @param levelEpi New value for levelEpi
     */
    public void setLevelEpi(double levelEpi) {
        double oldLevelEpi = getLevelEpi();
        this.levelEpi = levelEpi;
        levelEpiPrefAdapter.setDouble(levelEpi);
        firePropertyChange(PROPERTYNAME_LEVELEPI, oldLevelEpi, levelEpi);
    }

    /**
     * Gets the current value of commPortXmis from preferences
     * @return Current value of commPortXmis
     */
    public String getCommPortXmis() {
        commPortXmis = commPortXmisPrefAdapter.getString();
        return commPortXmis;
    }

    /**
     * Sets the value of commPortXmis to preferences
     * @param commPortXmis New value for commPortXmis
     */
    public void setCommPortXmis(String commPortXmis) {
        String oldCommPortXmis = getCommPortXmis();
        this.commPortXmis = commPortXmis;
        commPortXmisPrefAdapter.setString(commPortXmis);
        firePropertyChange(PROPERTYNAME_COMMPORTXMIS, oldCommPortXmis, commPortXmis);
    }

    /**
     * Gets the current value of commPortEpi from preferences
     * @return Current value of commPortEpi
     */
    public String getCommPortEpi() {
        commPortEpi = commPortEpiPrefAdapter.getString();
        return commPortEpi;
    }

    /**
     * Sets the value of commPortEpi to preferences
     * @param commPortEpi New value for commPortEpi
     */
    public void setCommPortEpi(String commPortEpi) {
        String oldCommPortEpi = getCommPortEpi();
        this.commPortEpi = commPortEpi;
        commPortEpiPrefAdapter.setString(commPortEpi);
        firePropertyChange(PROPERTYNAME_COMMPORTEPI, oldCommPortEpi, commPortEpi);
    }

    /**
     * Gets the current value of shutterTypeEpi from preferences
     * @return Current value of shutterTypeEpi
     */
    public String getShutterTypeEpi() {
        shutterTypeEpi = shutterTypeEpiPrefAdapter.getString();
        return shutterTypeEpi;
    }

    /**
     * Gets the current value of shutterTypeEpi from preferences
     * @return Current value of shutterTypeEpi
     */
    public String getShutterTypeXmis() {
        shutterTypeXmis = shutterTypeXmisPrefAdapter.getString();
        return shutterTypeXmis;
    }

    public void toggleEpiShutter() {
        setOpenEpi(
            !((AbstractActionExt) ActionManager.getInstance().getAction("toggleEpiShutter")).isSelected());
    }
    
    public void toggleXmisShutter() {
        setOpenXmis(
            !((AbstractActionExt) ActionManager.getInstance().getAction("toggleXmisShutter")).isSelected());
    }
//======================================================================================
// @todo TO DELETE ...
//    public boolean openPortEpiSettingsDialog(boolean onInitialize) {
//        getPortEpi().closeConnection();
//        String message;
//        String title;
//        if (onInitialize) {
//            title = "Epi Shutter Port Not Found";
//            message = "Select alternative port and OK, or Cancel";
//        } else {
//            title = "Epi Shutter Serial Port Settings";
//            message = "Change Serial Port Settings";
//        }
//        if (SerialUtils.openPortSettingsDialog(getPortEpi(), title, message)) {
//            try {
//                setCommPortEpi(getPortEpi().getParameters().getPortName());
//            } catch (SerialConnectionException ex) {
//                ex.printStackTrace();
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean openPortXmisSettingsDialog(boolean onInitialize) {
//        getPortXmis().closeConnection();
//        String message;
//        String title;
//        if (onInitialize) {
//            title = "Xmiss Shutter Port Not Found";
//            message = "Select alternative port and OK, or Cancel";
//        } else {
//            title = "Xmiss Shutter Serial Port Settings";
//            message = "Change Serial Port Settings";
//        }
//        if (SerialUtils.openPortSettingsDialog(getPortXmis(), title, message)) {
//            try {
//                setCommPortXmis(getPortXmis().getParameters().getPortName());
//            } catch (SerialConnectionException ex) {
//                ex.printStackTrace();
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

//    public SerialPortConnection getPortEpi() {
//        return portEpi;
//    }
//
//    public SerialPortConnection getPortXmis() {
//        return portXmis;
//    }

//    public void openMonitor(String title, SerialPortConnection port) {
//        SerialPortMonitor monitor = new SerialPortMonitor(port);
//        JFrame monFrame = new JFrame(title);
//        monFrame.setSize(new Dimension(200, 400));
//        monFrame.setLocation(100, 20);
//        //setMonitor(monitor);
//        monFrame.add(monitor, BorderLayout.CENTER);
//        monFrame.setVisible(true);
//    }
//
//    public void setMonitor(SerialPortMonitor _monitor) {
////        monitor = _monitor;
////        port.setMonitor(monitor);
//    }
//
//    public void clearMonitor() {
//        //  port.setMonitor(null);
//    }
}