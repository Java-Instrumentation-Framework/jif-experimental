package edu.mbl.jif.oidic;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.ListModel;

public class ArcLCModel
        extends Model {

  // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">

  static final String DEFAULT_PREFS_KEY = "arcLC";
  Preferences prefs;
  //
  public static final double WAVELENGTH_MIN = 420;
  public static final double WAVELENGTH_MAX = 700;
  public static final double SWING_MIN = 0.001;
  public static final double SWING_MAX = 0.5;
  // --- type ---
  public static final String PROPERTYNAME_TYPE = "type";
  static final int TYPE_DEFAULT = 1;
  public static final Integer TYPE_ENUM_1 = new Integer(1);
  public static final Integer TYPE_ENUM_2 = new Integer(2);
  public static final Integer TYPE_ENUM_3 = new Integer(4);
  public static final List TYPE_OPTIONS = Arrays.asList(new Integer[]{
            TYPE_ENUM_1, TYPE_ENUM_2, TYPE_ENUM_3
          });
  // --- serialNumber ---
  public static final String PROPERTYNAME_SERIALNUMBER = "serialNumber";
  static final String SERIALNUMBER_DEFAULT = "none";
  // --- settleTime ---
  public static final String PROPERTYNAME_SETTLETIME = "settleTime";
  static final int SETTLETIME_DEFAULT = 50;
  // --- definelatency ---
  public static final String PROPERTYNAME_DEFINELATENCY = "definelatency";
  static final double DEFINELATENCY_DEFAULT = 250;
  // --- wavelength ---
  public static final String PROPERTYNAME_WAVELENGTH = "wavelength";
  static final double WAVELENGTH_DEFAULT = 540;
  // --- swing ---
  public static final String PROPERTYNAME_SWING = "swing";
  static final double SWING_DEFAULT = 0.01;
  // --- minA ---
  public static final String PROPERTYNAME_MINA = "minA";
  static final double MINA_DEFAULT = 0.0;
  // --- maxA ---
  public static final String PROPERTYNAME_MAXA = "maxA";
  static final double MAXA_DEFAULT = 10.0;
  // --- minB ---
  public static final String PROPERTYNAME_MINB = "minB";
  static final double MINB_DEFAULT = 0.0;
  // --- maxB ---
  public static final String PROPERTYNAME_MAXB = "maxB";
  static final double MAXB_DEFAULT = 10.0;
  // --- extA ---
  public static final String PROPERTYNAME_EXTINCTA = "extinctA";
  static final double EXTINCTA_DEFAULT = 0.25;
  // --- extB ---
  public static final String PROPERTYNAME_EXTINCTB = "extinctB";
  static final double EXTINCTB_DEFAULT = 0.5;
  // --- A0 ---
  public static final String PROPERTYNAME_A0 = "a0";
  static final double A0_DEFAULT = 0.25;
  // --- B0 ---
  public static final String PROPERTYNAME_B0 = "b0";
  static final double B0_DEFAULT = 0.5;
  // --- A1 ---
  public static final String PROPERTYNAME_A1 = "a1";
  static final double A1_DEFAULT = 0.25;
  // --- B1 ---
  public static final String PROPERTYNAME_B1 = "b1";
  static final double B1_DEFAULT = 0.5;
  // --- A2 ---
  public static final String PROPERTYNAME_A2 = "a2";
  static final double A2_DEFAULT = 0.25;
  // --- B2 ---
  public static final String PROPERTYNAME_B2 = "b2";
  static final double B2_DEFAULT = 0.5;
  // --- A3 ---
  public static final String PROPERTYNAME_A3 = "a3";
  static final double A3_DEFAULT = 0.25;
  // --- B3 ---
  public static final String PROPERTYNAME_B3 = "b3";
  static final double B3_DEFAULT = 0.5;
  // --- A4 ---
  public static final String PROPERTYNAME_A4 = "a4";
  static final double A4_DEFAULT = 0.25;
  // --- B4 ---
  public static final String PROPERTYNAME_B4 = "b4";
  static final double B4_DEFAULT = 0.5;
  // --- A5 ---
  public static final String PROPERTYNAME_A5 = "a5";
  static final double A5_DEFAULT = 0.25;
  // --- B5 ---
  public static final String PROPERTYNAME_B5 = "b5";
  static final double B5_DEFAULT = 0.5;
  // --- intensity0 ---
  public static final String PROPERTYNAME_INTENSITY0 = "intensity0";
  static final float INTENSITY0_DEFAULT = 0f;
  // --- intensity1 ---
  public static final String PROPERTYNAME_INTENSITY1 = "intensity1";
  static final float INTENSITY1_DEFAULT = 0f;
  // --- intensity2 ---
  public static final String PROPERTYNAME_INTENSITY2 = "intensity2";
  static final float INTENSITY2_DEFAULT = 0f;
  // --- intensity3 ---
  public static final String PROPERTYNAME_INTENSITY3 = "intensity3";
  static final float INTENSITY3_DEFAULT = 0f;
  // --- intensity4 ---
  public static final String PROPERTYNAME_INTENSITY4 = "intensity4";
  static final float INTENSITY4_DEFAULT = 0f;
  // --- intensity5 ---
  public static final String PROPERTYNAME_INTENSITY5 = "intensity5";
  static final float INTENSITY5_DEFAULT = 0f;
  // --- extinctionRatio ---
  public static final String PROPERTYNAME_EXTINCTIONRATIO = "extinctionRatio";
  static final float EXTINCTIONRATIO_DEFAULT = 0f;
  // --- selectedSetting ---
  public static final String PROPERTYNAME_SELECTEDSETTING = "selectedSetting";
  static final int SELECTEDSETTING_DEFAULT = 1;
  // --- lastCalibrated ---
  public static final String PROPERTYNAME_LASTCALIBRATED = "lastCalibrated";
  static final long LASTCALIBRATED_DEFAULT = 0;
  // --- toleranceGross ---
  public static final String PROPERTYNAME_TOLERANCEGROSS = "toleranceGross";
  static final double TOLERANCEGROSS_DEFAULT = 0.01;
  // --- toleranceFine ---
  public static final String PROPERTYNAME_TOLERANCEFINE = "toleranceFine";
  static final double TOLERANCEFINE_DEFAULT = 0.001;
  // --- toleranceA ---
  public static final String PROPERTYNAME_TOLERANCEA = "toleranceA";
  static final double TOLERANCEA_DEFAULT = 0.05;
  // --- toleranceB ---
  public static final String PROPERTYNAME_TOLERANCEB = "toleranceB";
  static final double TOLERANCEB_DEFAULT = 0.1;
  // --- maxIterations ---
  public static final String PROPERTYNAME_MAXITERATIONS = "maxIterations";
  static final int MAXITERATIONS_DEFAULT = 20;
  // --- stabilityLevel ---
  public static final String PROPERTYNAME_STABILITYLEVEL = "stabilityLevel";
  static final double STABILITYLEVEL_DEFAULT = 0.05;
  // --- roiSize ---
  public static final String PROPERTYNAME_ROISIZE = "roiSize";
  static final int ROISIZE_DEFAULT = 50;
  // --- commandFormat ---
  public static final String PROPERTYNAME_COMMANDFORMAT = "commandFormat";
  static final int COMMANDFORMAT_DEFAULT = 1;
  // --- baudRate ---
  public static final String PROPERTYNAME_BAUDRATE = "baudRate";
  static final int BAUDRATE_DEFAULT = 9600;
  // --- commPort ---
//    public static final String PROPERTYNAME_COMMPORT = "commPort";
//    static final String COMMPORT_DEFAULT = "COM10";
  private int type;
  PreferencesAdapter typePrefAdapter;
  private ObservableList typeListModel;
  private Object typeListSelection;
  private String serialNumber;
  PreferencesAdapter serialNumberPrefAdapter;
  private int settleTime;
  PreferencesAdapter settleTimePrefAdapter;
  private double definelatency;
  PreferencesAdapter definelatencyPrefAdapter;
  private double wavelength;
  PreferencesAdapter wavelengthPrefAdapter;
  private double swing;
  PreferencesAdapter swingPrefAdapter;
  private double minA;
  PreferencesAdapter minAPrefAdapter;
  private double maxA;
  PreferencesAdapter maxAPrefAdapter;
  private double minB;
  PreferencesAdapter minBPrefAdapter;
  private double maxB;
  PreferencesAdapter maxBPrefAdapter;
  private double extinctA;
  PreferencesAdapter extinctAPrefAdapter;
  private double extinctB;
  PreferencesAdapter extinctBPrefAdapter;
  private double a0;
  PreferencesAdapter a0PrefAdapter;
  private double b0;
  PreferencesAdapter b0PrefAdapter;
  private double a1;
  PreferencesAdapter a1PrefAdapter;
  private double b1;
  PreferencesAdapter b1PrefAdapter;
  private double a2;
  PreferencesAdapter a2PrefAdapter;
  private double b2;
  PreferencesAdapter b2PrefAdapter;
  private double a3;
  PreferencesAdapter a3PrefAdapter;
  private double b3;
  PreferencesAdapter b3PrefAdapter;
  private double a4;
  PreferencesAdapter a4PrefAdapter;
  private double b4;
  PreferencesAdapter b4PrefAdapter;
  private double a5;
  PreferencesAdapter a5PrefAdapter;
  private double b5;
  PreferencesAdapter b5PrefAdapter;
  private float intensity0;
  private float intensity1;
  private float intensity2;
  private float intensity3;
  private float intensity4;
  private float intensity5;
  private float extinctionRatio=1.0f;
  private int selectedSetting;
  PreferencesAdapter selectedSettingPrefAdapter;
  private long lastCalibrated;
  PreferencesAdapter lastCalibratedPrefAdapter;
  private double toleranceGross;
  PreferencesAdapter toleranceGrossPrefAdapter;
  private double toleranceFine;
  PreferencesAdapter toleranceFinePrefAdapter;
  private double toleranceA;
  PreferencesAdapter toleranceAPrefAdapter;
  private double toleranceB;
  PreferencesAdapter toleranceBPrefAdapter;
  private int maxIterations;
  PreferencesAdapter maxIterationsPrefAdapter;
  private int roiSize;
  PreferencesAdapter roiSizePrefAdapter;
  private int commandFormat;
  PreferencesAdapter commandFormatPrefAdapter;
  private int baudRate;
  PreferencesAdapter baudRatePrefAdapter;
  private String commPort;
  PreferencesAdapter commPortPrefAdapter;
  private double stabilityLevel;
  PreferencesAdapter stabilityLevelPrefAdapter;
  // </editor-fold>
  private InstrumentController instrumentCtrl;
  double[] intensity = new double[5];
  NumberFormat formatter = new DecimalFormat("###.#");
  private ArcLCController arcLC;

  /**
   * Creates a new instance of VariLC
   */
  public ArcLCModel(InstrumentController instrumentCtrl) throws Exception {
    this(instrumentCtrl, DEFAULT_PREFS_KEY);
  }

  public ArcLCModel(InstrumentController instrumentCtrl, String key) throws Exception {
    initializePrefs(key);

    this.instrumentCtrl = instrumentCtrl;
    this.arcLC = (ArcLCController) instrumentCtrl.getController("arcLC");
    // Initialize enumerated list for type
    typeListModel = new ArrayListModel();
    typeListModel.addAll(TYPE_OPTIONS);
    typeListSelection = typeListModel.get(0);

  // for test: vlcRT = new VariLC_RT(new SerialPortConnection());
  }

  public void initializePrefs(String key) {
    prefs = CamAcqJ.getInstance().getPreferences().node(key);
    typePrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_TYPE, TYPE_DEFAULT);
    serialNumberPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_SERIALNUMBER, SERIALNUMBER_DEFAULT);
    settleTimePrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_SETTLETIME, SETTLETIME_DEFAULT);
    definelatencyPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_DEFINELATENCY, DEFINELATENCY_DEFAULT);
    wavelengthPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_WAVELENGTH, WAVELENGTH_DEFAULT);
    swingPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_SWING, SWING_DEFAULT);
    minAPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_MINA, MINA_DEFAULT);
    maxAPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_MAXA, MAXA_DEFAULT);
    minBPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_MINB, MINB_DEFAULT);
    maxBPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_MAXB, MAXB_DEFAULT);
    extinctAPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_EXTINCTA, EXTINCTA_DEFAULT);
    extinctBPrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_EXTINCTB, EXTINCTB_DEFAULT);
    a0PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_A0, A0_DEFAULT);
    b0PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_B0, B0_DEFAULT);
    a1PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_A1, A1_DEFAULT);
    b1PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_B1, B1_DEFAULT);
    a2PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_A2, A2_DEFAULT);
    b2PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_B2, B2_DEFAULT);
    a3PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_A3, A3_DEFAULT);
    b3PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_B3, B3_DEFAULT);
    a4PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_A4, A4_DEFAULT);
    b4PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_B4, B4_DEFAULT);
    a5PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_A5, A5_DEFAULT);
    b5PrefAdapter = new PreferencesAdapter(prefs, ArcLCModel.PROPERTYNAME_B5, B5_DEFAULT);
    selectedSettingPrefAdapter =
            new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_SELECTEDSETTING, SELECTEDSETTING_DEFAULT);
    lastCalibratedPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_LASTCALIBRATED, LASTCALIBRATED_DEFAULT);
    toleranceGrossPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_TOLERANCEGROSS, TOLERANCEGROSS_DEFAULT);
    toleranceFinePrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_TOLERANCEFINE, TOLERANCEFINE_DEFAULT);
    toleranceAPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_TOLERANCEA, TOLERANCEA_DEFAULT);
    toleranceBPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_TOLERANCEB, TOLERANCEB_DEFAULT);
    maxIterationsPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_MAXITERATIONS, MAXITERATIONS_DEFAULT);
    roiSizePrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_ROISIZE, ROISIZE_DEFAULT);
    commandFormatPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_COMMANDFORMAT, COMMANDFORMAT_DEFAULT);
    baudRatePrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_BAUDRATE, BAUDRATE_DEFAULT);
//        commPortPrefAdapter = new PreferencesAdapter(prefs,
//                VariLCModel.PROPERTYNAME_COMMPORT, COMMPORT_DEFAULT);
    stabilityLevelPrefAdapter = new PreferencesAdapter(prefs,
            ArcLCModel.PROPERTYNAME_STABILITYLEVEL, STABILITYLEVEL_DEFAULT);
  }

  // <editor-fold defaultstate="collapsed" desc=">>>--- Setters/Getters  ---<<<" >
  /**
   * Gets the current value of type from preferences
   * @return Current value of type
   */
  public int getType() {
    type = typePrefAdapter.getInt();
    return type;
  }

  /**
   * Sets the value of type to preferences
   * @param type New value for type
   */
  public void setType(int type) {
    int oldType = getType();
    this.type = type;
    typePrefAdapter.setInt(type);
    firePropertyChange(PROPERTYNAME_TYPE, oldType, type);
  }

  /**
   * ListModel accessor for type
   * @return ListModel of type
   */
  public ListModel getTypeListModel() {
    return typeListModel;
  }

  /**
   * ListSelection accessor for type
   * @return ListSelection of type
   */
  public Object getTypeListSelection() {
    return typeListSelection;
  }

  /**
   * Gets the current value of serialNumber from preferences
   * @return Current value of serialNumber
   */
  public String getSerialNumber() {
    serialNumber = serialNumberPrefAdapter.getString();
    return serialNumber;
  }

  /**
   * Sets the value of serialNumber to preferences
   * @param serialNumber New value for serialNumber
   */
  public void setSerialNumber(String serialNumber) {
    String oldSerialNumber = getSerialNumber();
    this.serialNumber = serialNumber;
    serialNumberPrefAdapter.setString(serialNumber);
    firePropertyChange(PROPERTYNAME_SERIALNUMBER, oldSerialNumber,
            serialNumber);
  }

  /**
   * Gets the current value of settleTime from preferences
   * @return Current value of settleTime
   */
  public int getSettleTime() {
    settleTime = settleTimePrefAdapter.getInt();
    return settleTime;
  }

  /**
   * Sets the value of settleTime to preferences
   * @param settleTime New value for settleTime
   */
  public void setSettleTime(int settleTime) {
    int oldSettleTime = getSettleTime();
    this.settleTime = settleTime;
    settleTimePrefAdapter.setInt(settleTime);
    firePropertyChange(PROPERTYNAME_SETTLETIME, oldSettleTime, settleTime);
  }

  /**
   * Gets the current value of definelatency from preferences
   * @return Current value of definelatency
   */
  public double getDefinelatency() {
    definelatency = definelatencyPrefAdapter.getDouble();
    return definelatency;
  }

  /**
   * Sets the value of definelatency to preferences
   * @param definelatency New value for definelatency
   */
  public void setDefinelatency(double definelatency) {
    double oldDefinelatency = getDefinelatency();
    this.definelatency = definelatency;
    definelatencyPrefAdapter.setDouble(definelatency);
    firePropertyChange(PROPERTYNAME_DEFINELATENCY, oldDefinelatency,
            definelatency);
  }

  /**
   * Gets the current value of wavelength from preferences
   * @return Current value of wavelength
   */
  public double getWavelength() {
    wavelength = wavelengthPrefAdapter.getDouble();
    return wavelength;
  }

  /**
   * Sets the value of wavelength to preferences
   * @param wavelength New value for wavelength
   */
  public void setWavelength(double wavelength) {
    double oldWavelength = getWavelength();
    this.wavelength = wavelength;
    wavelengthPrefAdapter.setDouble(wavelength);
    firePropertyChange(PROPERTYNAME_WAVELENGTH, oldWavelength, wavelength);
  }

  /**
   * Gets the current value of swing from preferences
   * @return Current value of swing
   */
  public double getSwing() {
    swing = swingPrefAdapter.getDouble();
    return swing;
  }

  /**
   * Sets the value of swing to preferences
   * @param swing New value for swing
   */
  public void setSwing(double swing) {
    double oldSwing = getSwing();
    this.swing = swing;
    swingPrefAdapter.setDouble(swing);
    firePropertyChange(PROPERTYNAME_SWING, oldSwing, swing);
  }

  /**
   * Gets the current value of minA from preferences
   * @return Current value of minA
   */
  public double getMinA() {
    minA = minAPrefAdapter.getDouble();
    return minA;
  }

  /**
   * Sets the value of minA to preferences
   * @param minA New value for minA
   */
  public void setMinA(double minA) {
    double oldMinA = getMinA();
    this.minA = minA;
    minAPrefAdapter.setDouble(minA);
    firePropertyChange(PROPERTYNAME_MINA, oldMinA, minA);
  }

  /**
   * Gets the current value of maxA from preferences
   * @return Current value of maxA
   */
  public double getMaxA() {
    maxA = maxAPrefAdapter.getDouble();
    return maxA;
  }

  /**
   * Sets the value of maxA to preferences
   * @param maxA New value for maxA
   */
  public void setMaxA(double maxA) {
    double oldMaxA = getMaxA();
    this.maxA = maxA;
    maxAPrefAdapter.setDouble(maxA);
    firePropertyChange(PROPERTYNAME_MAXA, oldMaxA, maxA);
  }

  /**
   * Gets the current value of minB from preferences
   * @return Current value of minB
   */
  public double getMinB() {
    minB = minBPrefAdapter.getDouble();
    return minB;
  }

  /**
   * Sets the value of minB to preferences
   * @param minB New value for minB
   */
  public void setMinB(double minB) {
    double oldMinB = getMinB();
    this.minB = minB;
    minBPrefAdapter.setDouble(minB);
    firePropertyChange(PROPERTYNAME_MINB, oldMinB, minB);
  }

  /**
   * Gets the current value of maxB from preferences
   * @return Current value of maxB
   */
  public double getMaxB() {
    maxB = maxBPrefAdapter.getDouble();
    return maxB;
  }

  /**
   * Sets the value of maxB to preferences
   * @param maxB New value for maxB
   */
  public void setMaxB(double maxB) {
    double oldMaxB = getMaxB();
    this.maxB = maxB;
    maxBPrefAdapter.setDouble(maxB);
    firePropertyChange(PROPERTYNAME_MAXB, oldMaxB, maxB);
  }

  /**
   * Gets the current value of extA from preferences
   * @return Current value of extA
   */
  public double getExtinctA() {
    extinctA = extinctAPrefAdapter.getDouble();
    return extinctA;
  }

  /**
   * Sets the value of extA to preferences
   * @param extA New value for extA
   */
  public void setExtinctA(double extinctA) {
    double oldExtinctA = getExtinctA();
    this.extinctA = extinctA;
    extinctAPrefAdapter.setDouble(extinctA);
    firePropertyChange(PROPERTYNAME_EXTINCTA, oldExtinctA, extinctA);
  }

  /**
   * Gets the current value of extB from preferences
   * @return Current value of extB
   */
  public double getExtinctB() {
    extinctB = extinctBPrefAdapter.getDouble();
    return extinctB;
  }

  /**
   * Sets the value of extB to preferences
   * @param extB New value for extB
   */
  public void setExtinctB(double extinctB) {
    double oldExtinctB = getExtinctB();
    this.extinctB = extinctB;
    extinctBPrefAdapter.setDouble(extinctB);
    firePropertyChange(PROPERTYNAME_EXTINCTB, oldExtinctB, extinctB);
  }

  /**
   * Gets the current value of A0 from preferences
   * @return Current value of A0
   */
  public double getA0() {
    a0 = a0PrefAdapter.getDouble();
    return a0;
  }

  /**
   * Sets the value of A0 to preferences
   * @param A0 New value for A0
   */
  public void setA0(double A0) {
    double olda0 = getA0();
    this.a0 = A0;
    a0PrefAdapter.setDouble(a0);
    arcLC.defineElementAndMeasure(0);
    firePropertyChange(PROPERTYNAME_A0, olda0, a0);
  }

  /**
   * Gets the current value of B0 from preferences
   * @return Current value of B0
   */
  public double getB0() {
    b0 = b0PrefAdapter.getDouble();
    return b0;
  }

  /**
   * Sets the value of B0 to preferences
   * @param B0 New value for B0
   */
  public void setB0(double B0) {
    double oldb0 = getB0();
    this.b0 = B0;
    b0PrefAdapter.setDouble(b0);
    arcLC.defineElementAndMeasure(0);
    firePropertyChange(PROPERTYNAME_B0, oldb0, b0);
  }

  /**
   * Gets the current value of A1 from preferences
   * @return Current value of A1
   */
  public double getA1() {
    a1 = a1PrefAdapter.getDouble();
    return a1;
  }

  /**
   * Sets the value of A1 to preferences
   * @param A1 New value for A1
   */
  public void setA1(double A1) {
    double olda1 = getA1();
    this.a1 = A1;
    a1PrefAdapter.setDouble(a1);
    arcLC.defineElementAndMeasure(1);
    firePropertyChange(PROPERTYNAME_A1, olda1, a1);
  }

  /**
   * Gets the current value of B1 from preferences
   * @return Current value of B1
   */
  public double getB1() {
    b1 = b1PrefAdapter.getDouble();
    return b1;
  }

  /**
   * Sets the value of B1 to preferences
   * @param B1 New value for B1
   */
  public void setB1(double B1) {
    double oldb1 = getB1();
    this.b1 = B1;
    b1PrefAdapter.setDouble(b1);
    arcLC.defineElementAndMeasure(1);
    firePropertyChange(PROPERTYNAME_B1, oldb1, b1);
  }

  /**
   * Gets the current value of A2 from preferences
   * @return Current value of A2
   */
  public double getA2() {
    a2 = a2PrefAdapter.getDouble();
    return a2;
  }

  /**
   * Sets the value of A2 to preferences
   * @param A2 New value for A2
   */
  public void setA2(double A2) {
    double olda2 = getA2();
    this.a2 = A2;
    a2PrefAdapter.setDouble(a2);
    arcLC.defineElementAndMeasure(2);
    firePropertyChange(PROPERTYNAME_A2, olda2, a2);
  }

  /**
   * Gets the current value of B2 from preferences
   * @return Current value of B2
   */
  public double getB2() {
    b2 = b2PrefAdapter.getDouble();
    return b2;
  }

  /**
   * Sets the value of B2 to preferences
   * @param B2 New value for B2
   */
  public void setB2(double B2) {
    double oldB2 = getB2();
    this.b2 = B2;
    b2PrefAdapter.setDouble(b2);
    arcLC.defineElementAndMeasure(2);
    firePropertyChange(PROPERTYNAME_B2, oldB2, b2);
  }

  /**
   * Gets the current value of A3 from preferences
   * @return Current value of A3
   */
  public double getA3() {
    a3 = a3PrefAdapter.getDouble();
    return a3;
  }

  /**
   * Sets the value of A3 to preferences
   * @param A3 New value for A3
   */
  public void setA3(double A3) {
    double oldA3 = getA3();
    this.a3 = A3;
    a3PrefAdapter.setDouble(a3);
    arcLC.defineElementAndMeasure(3);
    firePropertyChange(PROPERTYNAME_A3, oldA3, a3);
  }

  /**
   * Gets the current value of B3 from preferences
   * @return Current value of B3
   */
  public double getB3() {
    b3 = b3PrefAdapter.getDouble();
    return b3;
  }

  /**
   * Sets the value of B3 to preferences
   * @param B3 New value for B3
   */
  public void setB3(double B3) {
    double oldB3 = getB3();
    this.b3 = B3;
    b3PrefAdapter.setDouble(b3);
    arcLC.defineElementAndMeasure(3);
    firePropertyChange(PROPERTYNAME_B3, oldB3, b3);
  }

  /**
   * Gets the current value of A4 from preferences
   * @return Current value of A4
   */
  public double getA4() {
    a4 = a4PrefAdapter.getDouble();
    return a4;
  }

  /**
   * Sets the value of A4 to preferences
   * @param A4 New value for A4
   */
  public void setA4(double A4) {
    double oldA4 = getA4();
    this.a4 = A4;
    a4PrefAdapter.setDouble(a4);
    arcLC.defineElementAndMeasure(4);
    firePropertyChange(PROPERTYNAME_A4, oldA4, a4);
  }

  /**
   * Gets the current value of B4 from preferences
   * @return Current value of B4
   */
  public double getB4() {
    b4 = b4PrefAdapter.getDouble();
    return b4;
  }

  /**
   * Sets the value of B4 to preferences
   * @param B4 New value for B4
   */
  public void setB4(double B4) {
    double oldB4 = getB4();
    this.b4 = B4;
    b4PrefAdapter.setDouble(b4);
    arcLC.defineElementAndMeasure(4);
    firePropertyChange(PROPERTYNAME_B4, oldB4, b4);
  }
  /**
   * Gets the current value of A5 from preferences
   * @return Current value of A5
   */
  public double getA5() {
    a5 = a5PrefAdapter.getDouble();
    return a5;
  }

  /**
   * Sets the value of A5 to preferences
   * @param A5 New value for A5
   */
  public void setA5(double A5) {
    double oldA5 = getA5();
    this.a5 = A5;
    a5PrefAdapter.setDouble(a5);
    arcLC.defineElementAndMeasure(5);
    firePropertyChange(PROPERTYNAME_A5, oldA5, a5);
  }

  /**
   * Gets the current value of B5 from preferences
   * @return Current value of B5
   */
  public double getB5() {
    b5 = b5PrefAdapter.getDouble();
    return b5;
  }

  /**
   * Sets the value of B5 to preferences
   * @param B5 New value for B5
   */
  public void setB5(double B5) {
    double oldB5 = getB5();
    this.b5 = B5;
    b5PrefAdapter.setDouble(b5);
    arcLC.defineElementAndMeasure(5);
    firePropertyChange(PROPERTYNAME_B5, oldB5, b5);
  }

  public float getIntensity0() {
    return intensity0;
  }

  public void setIntensity0(float I0) {
    float old = getIntensity0();
    this.intensity0 = I0;
    firePropertyChange(PROPERTYNAME_INTENSITY0, old, intensity0);
  }

  public float getIntensity1() {
    return intensity1;
  }

  public void setIntensity1(float I1) {
    float old = getIntensity1();
    this.intensity1 = I1;
    firePropertyChange(PROPERTYNAME_INTENSITY1, old, intensity1);
  }

  public float getIntensity2() {
    return intensity2;
  }

  public void setIntensity2(float I2) {
    float old = getIntensity2();
    this.intensity2 = I2;
    firePropertyChange(PROPERTYNAME_INTENSITY2, old, intensity2);
  }

  public float getIntensity3() {
    return intensity3;
  }

  public void setIntensity3(float I3) {
    float old = getIntensity3();
    this.intensity3 = I3;
    firePropertyChange(PROPERTYNAME_INTENSITY3, old, intensity3);
  }

  public float getIntensity4() {
    return intensity4;
  }

  public void setIntensity4(float I4) {
    float old = getIntensity4();
    this.intensity4 = I4;
    firePropertyChange(PROPERTYNAME_INTENSITY4, old, intensity4);
  }
  public float getIntensity5() {
    return intensity5;
  }

  public void setIntensity5(float I5) {
    float old = getIntensity5();
    this.intensity5 = I5;
    firePropertyChange(PROPERTYNAME_INTENSITY5, old, intensity5);
  }

  public float getExtinctionRatio() {
    return extinctionRatio;
  }

  public void setExtinctionRatio(float ratio) {
    float old = getExtinctionRatio();
    this.extinctionRatio = ratio;
    firePropertyChange(PROPERTYNAME_EXTINCTIONRATIO, old, extinctionRatio);
  }

  public int getSelectedSetting() {
    selectedSetting = selectedSettingPrefAdapter.getInt();
    return selectedSetting;
  }

  public void setSelectedSetting(int setting) {
    int old = getSelectedSetting();
    this.selectedSetting = setting;
    selectedSettingPrefAdapter.setInt(selectedSetting);
    firePropertyChange(PROPERTYNAME_SELECTEDSETTING, old, selectedSetting);
  }

  /**
   * Gets the current value of lastCalibrated from preferences
   * @return Current value of lastCalibrated
   */
  public long getLastCalibrated() {
    lastCalibrated = lastCalibratedPrefAdapter.getLong();
    return lastCalibrated;
  }

  /**
   * Sets the value of lastCalibrated to preferences
   * @param lastCalibrated New value for lastCalibrated
   */
  public void setLastCalibrated(long lastCalibrated) {
    long oldLastCalibrated = getLastCalibrated();
    this.lastCalibrated = lastCalibrated;
    lastCalibratedPrefAdapter.setLong(lastCalibrated);
    firePropertyChange(PROPERTYNAME_LASTCALIBRATED, oldLastCalibrated,
            lastCalibrated);
  }

  /**
   * Gets the current value of toleranceGross from preferences
   * @return Current value of toleranceGross
   */
  public double getToleranceGross() {
    toleranceGross = toleranceGrossPrefAdapter.getDouble();
    return toleranceGross;
  }

  /**
   * Sets the value of toleranceGross to preferences
   * @param toleranceGross New value for toleranceGross
   */
  public void setToleranceGross(double toleranceGross) {
    double oldToleranceGross = getToleranceGross();
    this.toleranceGross = toleranceGross;
    toleranceGrossPrefAdapter.setDouble(toleranceGross);
    firePropertyChange(PROPERTYNAME_TOLERANCEGROSS, oldToleranceGross,
            toleranceGross);
  }

  /**
   * Gets the current value of toleranceFine from preferences
   * @return Current value of toleranceFine
   */
  public double getToleranceFine() {
    toleranceFine = toleranceFinePrefAdapter.getDouble();
    return toleranceFine;
  }

  /**
   * Sets the value of toleranceFine to preferences
   * @param toleranceFine New value for toleranceFine
   */
  public void setToleranceFine(double toleranceFine) {
    double oldToleranceFine = getToleranceFine();
    this.toleranceFine = toleranceFine;
    toleranceFinePrefAdapter.setDouble(toleranceFine);
    firePropertyChange(PROPERTYNAME_TOLERANCEFINE, oldToleranceFine,
            toleranceFine);
  }

  /**
   * Gets the current value of toleranceA from preferences
   * @return Current value of toleranceA
   */
  public double getToleranceA() {
    toleranceA = toleranceAPrefAdapter.getDouble();
    return toleranceA;
  }

  /**
   * Sets the value of toleranceA to preferences
   * @param toleranceA New value for toleranceA
   */
  public void setToleranceA(double toleranceA) {
    double oldToleranceA = getToleranceA();
    this.toleranceA = toleranceA;
    toleranceAPrefAdapter.setDouble(toleranceA);
    firePropertyChange(PROPERTYNAME_TOLERANCEA, oldToleranceA, toleranceA);
  }

  /**
   * Gets the current value of toleranceB from preferences
   * @return Current value of toleranceB
   */
  public double getToleranceB() {
    toleranceB = toleranceBPrefAdapter.getDouble();
    return toleranceB;
  }

  /**
   * Sets the value of toleranceB to preferences
   * @param toleranceB New value for toleranceB
   */
  public void setToleranceB(double toleranceB) {
    double oldToleranceB = getToleranceB();
    this.toleranceB = toleranceB;
    toleranceBPrefAdapter.setDouble(toleranceB);
    firePropertyChange(PROPERTYNAME_TOLERANCEB, oldToleranceB, toleranceB);
  }

  /**
   * Gets the current value of maxIterations from preferences
   * @return Current value of maxIterations
   */
  public int getMaxIterations() {
    maxIterations = maxIterationsPrefAdapter.getInt();
    return maxIterations;
  }

  /**
   * Sets the value of maxIterations to preferences
   * @param maxIterations New value for maxIterations
   */
  public void setMaxIterations(int maxIterations) {
    int oldMaxIterations = getMaxIterations();
    this.maxIterations = maxIterations;
    maxIterationsPrefAdapter.setInt(maxIterations);
    firePropertyChange(PROPERTYNAME_MAXITERATIONS, oldMaxIterations,
            maxIterations);
  }

  /**
   * Gets the current value of roiSize from preferences
   * @return Current value of roiSize
   */
  public int getRoiSize() {
    roiSize = roiSizePrefAdapter.getInt();
    return roiSize;
  }

  /**
   * Sets the value of roiSize to preferences
   * @param roiSize New value for roiSize
   */
  public void setRoiSize(int roiSize) {
    int oldRoiSize = getRoiSize();
    this.roiSize = roiSize;
    roiSizePrefAdapter.setInt(roiSize);
    firePropertyChange(PROPERTYNAME_ROISIZE, oldRoiSize, roiSize);
  }

  /**
   * Gets the current value of commandFormat from preferences
   * @return Current value of commandFormat
   */
  public int getCommandFormat() {
    commandFormat = commandFormatPrefAdapter.getInt();
    return commandFormat;
  }

  /**
   * Sets the value of commandFormat to preferences
   * @param commandFormat New value for commandFormat
   */
  public void setCommandFormat(int commandFormat) {
    int oldCommandFormat = getCommandFormat();
    this.commandFormat = commandFormat;
    commandFormatPrefAdapter.setInt(commandFormat);
    firePropertyChange(PROPERTYNAME_COMMANDFORMAT, oldCommandFormat,
            commandFormat);
  }

  /**
   * Gets the current value of baudRate from preferences
   * @return Current value of baudRate
   */
  public int getBaudRate() {
    baudRate = baudRatePrefAdapter.getInt();
    return baudRate;
  }

  /**
   * Sets the value of baudRate to preferences
   * @param baudRate New value for baudRate
   */
  public void setBaudRate(int baudRate) {
    int oldBaudRate = getBaudRate();
    this.baudRate = baudRate;
    baudRatePrefAdapter.setInt(baudRate);
    firePropertyChange(PROPERTYNAME_BAUDRATE, oldBaudRate, baudRate);
  }

  /**
   * Gets the current value of commPort from preferences
   * @return Current value of commPort
   */
//    public String getCommPort() {
//        commPort = commPortPrefAdapter.getString();
//        return commPort;
//    }
//
//    /**
//     * Sets the value of commPort to preferences
//     * @param commPort New value for commPort
//     */
//    public void setCommPort(String commPort) {
//        String oldCommPort = getCommPort();
//        this.commPort = commPort;
//        commPortPrefAdapter.setString(commPort);
//        firePropertyChange(PROPERTYNAME_COMMPORT, oldCommPort, commPort);
//    }
  /**
   * Gets the current value of toleranceB from preferences
   * @return Current value of toleranceB
   */
  public double getStabilityLevel() {
    stabilityLevel = stabilityLevelPrefAdapter.getDouble();
    return stabilityLevel;
  }

  /**
   * Sets the value of toleranceB to preferences
   * @param toleranceB New value for toleranceB
   */
  public void setStabilityLevel(double level) {
    double oldLevel = getStabilityLevel();
    this.stabilityLevel = level;
    stabilityLevelPrefAdapter.setDouble(stabilityLevel);
    firePropertyChange(PROPERTYNAME_STABILITYLEVEL, oldLevel, stabilityLevel);
  }

  // </editor-fold>
}
