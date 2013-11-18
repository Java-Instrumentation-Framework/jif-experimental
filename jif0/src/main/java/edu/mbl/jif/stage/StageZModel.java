package edu.mbl.jif.stage;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camera.display.NextFrameWatcher;
import edu.mbl.jif.camera.display.OverlayMessage;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.dialog.DialogBox;
import java.util.prefs.Preferences;

public class StageZModel extends Model {

  // ***  All units in nanometers !!!
  Preferences prefs;
  static final String DEFAULT_PREFS_KEY = "ZStage";
  InstrumentController instrumentCtrl;
  ZStageController zCtrl;
  final static int DIRECTION_UP = 0;
  final static int DIRECTION_DOWN = 1;
  private int direction = DIRECTION_UP;

  // <editor-fold defaultstate="collapsed" desc="<<< Property Declarations >>>">

  // --- highLimit ---
  public static final String PROPERTYNAME_HIGHLIMIT = "highLimit";
  private double highLimit;
  static final double HIGHLIMIT_DEFAULT = -1;
  PreferencesAdapter highLimitPrefAdapter;

  // --- lowLimit ---
  public static final String PROPERTYNAME_LOWLIMIT = "lowLimit";
  private double lowLimit;
  static final double LOWLIMIT_DEFAULT = -1;
  PreferencesAdapter lowLimitPrefAdapter;

  // --- rangeTop ---
  public static final String PROPERTYNAME_RANGETOP = "rangeTop";
  private double rangeTop;
  static final double RANGETOP_DEFAULT = 0;
  PreferencesAdapter rangeTopPrefAdapter;

  // --- rangeBottom ---
  public static final String PROPERTYNAME_RANGEBOTTOM = "rangeBottom";
  private double rangeBottom;
  static final double RANGEBOTTOM_DEFAULT = 0;
  PreferencesAdapter rangeBottomPrefAdapter;
  // --- range ---
  public static final String PROPERTYNAME_RANGE = "range";
  private double range;
  static final double RANGE_DEFAULT = 0;
  // --- increment --- in Microns
  public static final String PROPERTYNAME_MOVEINCREMENT = "moveIncrement";
  private double moveIncrement;
  static final double MOVEINCREMENT_DEFAULT = 10.0;
  PreferencesAdapter moveIncrementPrefAdapter;

  // --- increment --- in Microns
  public static final String PROPERTYNAME_INCREMENT = "increment";
  private double increment;
  static final double INCREMENT_DEFAULT = 10.0;
  PreferencesAdapter incrementPrefAdapter;

  // --- sections ---
  public static final String PROPERTYNAME_SECTIONS = "sections";
  private int sections;
  static final int SECTIONS_DEFAULT = 1;
  PreferencesAdapter sectionsPrefAdapter;

  // --- restPosition ---
  public static final String PROPERTYNAME_RESTPOSITION = "restPosition";
  private double restPosition;
  static final double RESTPOSITION_DEFAULT = 0;
  PreferencesAdapter restPositionPrefAdapter;

  // --- position ---
  public static final String PROPERTYNAME_POSITION = "position";
  private double position;
  static final double POSITION_DEFAULT = 0;
  PreferencesAdapter positionPrefAdapter;

  // --- referencePosition ---
  public static final String PROPERTYNAME_REFPOS = "referencePosition";
  private double referencePosition;
  static final double REFPOS_DEFAULT = -1;
  PreferencesAdapter referencePositionPrefAdapter;

  // --- nmPerIncrement ---
  public static final String PROPERTYNAME_NMPERINCREMENT = "nmPerIncrement";
  private int nmPerIncrement;
  static final int NMPERINCREMENT_DEFAULT = 25;
  PreferencesAdapter nmPerIncrementPrefAdapter;

  // --- backlash ---
  public static final String PROPERTYNAME_BACKLASH = "backlash";
  private double backlash;
  static final double BACKLASH_DEFAULT = 400;
  PreferencesAdapter backlashPrefAdapter;

  // --- preSteps ---
  public static final String PROPERTYNAME_PRESTEPS = "preSteps";
  private int preSteps;
  static final int PRESTEPS_DEFAULT = 4;
  PreferencesAdapter preStepsPrefAdapter;

  // --- reactionLatency ---
  public static final String PROPERTYNAME_REACTIONLATENCY = "reactionLatency";
  private int reactionLatency;
  static final int REACTIONLATENCY_DEFAULT = 10;
  PreferencesAdapter reactionLatencyPrefAdapter;

  // --- settleDelay ---
  public static final String PROPERTYNAME_SETTLEDELAY = "settleDelay";
  private int settleDelay;
  static final int SETTLEDELAY_DEFAULT = 0;
  PreferencesAdapter settleDelayPrefAdapter;

  // --- settleDelay ---
  public static final String PROPERTYNAME_PREVIEWDELAY = "previewDelay";
  private int previewDelay;
  static final int PREVIEWDELAY_DEFAULT = 0;
  PreferencesAdapter previewDelayPrefAdapter;

  // --- velocity ---
  public static final String PROPERTYNAME_VELOCITY = "velocity";
  private float velocity;
  static final float VELOCITY_DEFAULT = 0;
  PreferencesAdapter velocityPrefAdapter;
  // </editor-fold>


// <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
  /*
   * Creates a new instance of ZStage
   */
  public StageZModel(InstrumentController instrumentCtrl, ZStageController zCtrl) {
    this(instrumentCtrl, zCtrl, DEFAULT_PREFS_KEY);
  }

  public StageZModel(InstrumentController instrumentCtrl, ZStageController zCtrl, String key) {
    this.instrumentCtrl = instrumentCtrl;
    this.zCtrl = zCtrl;
    initializePreferencesAdapters(key);
    zCtrl.setNmPerIncrement(this.getNmPerIncrement());
    System.out.println("Position = " + zCtrl.getCurrentPosition());
    setRange(getIncrement() * getSections());
  }
  // </editor-fold>

// <editor-fold defaultstate="collapsed" desc=" Movement ">
  void goDown(int i) {
    zCtrl.moveDown(i * (int) (getMoveIncrement() * 1000));
    updateCurrentPosition();
  }

  void goToBottom() {
    zCtrl.moveTo((int) (getRangeBottom() * 1000));
    updateCurrentPosition();
  }

  void goToRest() {
    zCtrl.moveTo((int) (getRestPosition() * 1000));
    updateCurrentPosition();
  }

  void goToTop() {
    zCtrl.moveTo((int) (getRangeTop() * 1000));
    updateCurrentPosition();
  }

  void goToZero() {
    zCtrl.moveTo(0);
    updateCurrentPosition();
  }

  void goUp(int i) {
    zCtrl.moveUp(i * (int) (getMoveIncrement() * 1000));
    updateCurrentPosition();
  }

  public void updateCurrentPosition() {
    setPosition(zCtrl.getCurrentPosition());
  }

  public int getZeroIndexPosition() {
    return zCtrl.getZeroIndexPosition();
  }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc=" Setting ">
  void setBottom() {
    setRangeBottom(getPosition());
    setRange(getIncrement() * getSections());
    setRangeTop(getPosition() + getRange());
    if (getRestPosition() > getRangeTop()) {
      setRestPosition(getRangeBottom());
    }
    if (getRestPosition() < getRangeBottom()) {
      setRestPosition(getRangeBottom());
    }
    direction = DIRECTION_UP;
  }

  void setRestPoint() {
    setZero();
    updateCurrentPosition();
    setRestPosition(getPosition());
  }

  void setTop() {
    setRangeTop(getPosition());
    setRange(getIncrement() * getSections());
    setRangeBottom(getPosition() - getRange());
    if (getRestPosition() > getRangeTop()) {
      setRestPosition(getRangeTop());
    }
    if (getRestPosition() < getRangeBottom()) {
      setRestPosition(getRangeTop());
    }
    direction = DIRECTION_DOWN;
  }

  void setZero() {
    zCtrl.setZeroPosition();
    updateCurrentPosition();
  }

// </editor-fold>


// <editor-fold defaultstate="collapsed" desc=" Scanning ">
  public void preview() {
    Runnable runnable = new Runnable() {

      public void run() {
        DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
        if (dlc == null) {
          DialogBox.boxNotify("Note", "Display must be open for Z-Scan Preview");
          return;
        }
        OverlayMessage oMsg = new OverlayMessage();
        dlc.getImageDisplayPanel().getImagePane().addGraphicOverlay(oMsg);
        oMsg.setMessage("Z-Scan Preview: 0 / " + getSections());
        NextFrameWatcher nextFrameWatcher = new NextFrameWatcher(dlc);
        prePositionForZscan();
        int i = 1;
        do {
          nextFrameWatcher.waitForNextFrame();  // waits at least for next frame.
          try {
            Thread.sleep(500);
          } catch (InterruptedException ex) {
          }
          if (direction == DIRECTION_UP) {
            zCtrl.moveRelativeAck((int) (getIncrement() * 1000));
          } else {
            zCtrl.moveRelativeAck(-(int) (getIncrement() * 1000));
          }
          oMsg.setMessage("Z-Scan Preview: " + i + " / " + getSections());
          i++;
        } while (i <= getSections());
        nextFrameWatcher = null;
        dlc.getImageDisplayPanel().getImagePane().removeGraphicOverlay(oMsg);
      }
    };
    (new Thread(runnable)).start();
  }

  public void test() {
    zCtrl.test();
  }

    public void prePositionForZscan() {
      if (direction == DIRECTION_UP) {
          prePositionForZscanUp(getRangeBottom(), getIncrement(), getSections());
        } else {
          prePositionForZscanDown(getRangeTop(), getIncrement(), getSections());
        }
    }

  public void prePositionForZscanDown(double start, double incr, int secs) {
    //System.out.println("Prepositioning stage...");
    zCtrl.moveToAck((int)((start + backlash)*1000));
    zCtrl.moveDown((int)(backlash*1000));
  }

  public void prePositionForZscanUp(double start, double incr, int secs) {
    //System.out.println("Prepositioning stage...");
    zCtrl.moveToAck((int)((start - backlash)*1000));
    zCtrl.moveUp((int)(backlash*1000));


  /** @todo Remove these waits... */
//    JifUtils.waitFor(100);
//    int below = -((preSteps * incr) + backlash);
//    zCtrl.moveRelativeAck(below);
//    JifUtils.waitFor(100);
//    zCtrl.moveRelativeAck(backlash);
//    JifUtils.waitFor(100);
//    int i = 0;
//    do {
//      zCtrl.moveRelativeAck(incr);
//      JifUtils.waitFor(200);
//      i++;
//    //System.out.println("prePositionForZscan.moveRelative(" + incr + "), " + i);
//    } while (i < (preSteps - 1));
//    int now = zCtrl.moveRelativeAck(incr);
//
//    // should be at start position
//    JifUtils.waitFor(200);
//    //int now = getCurrentPosition();
//    int delta = Math.abs(now - start);
//
//    //System.out.println("\nBegin Z-Scan (" + incr + ", " + secs + ") at position: " + now);
//    if (delta > 100) {
//      System.err.println("Focus/Stage Position Error, " +
//              "Initial Z-Scan Position is off by " + String.valueOf(delta) + " nm.");
//    }
  }

// </editor-fold>

  public void initializePreferencesAdapters(String key) {
    prefs = Application.getInstance().getPreferences().node(key);
    highLimitPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_HIGHLIMIT, HIGHLIMIT_DEFAULT);
    lowLimitPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_LOWLIMIT, LOWLIMIT_DEFAULT);
    rangeTopPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_RANGETOP, RANGETOP_DEFAULT);
    rangeBottomPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_RANGEBOTTOM, RANGEBOTTOM_DEFAULT);
    moveIncrementPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_MOVEINCREMENT, MOVEINCREMENT_DEFAULT);
    incrementPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_INCREMENT, INCREMENT_DEFAULT);
    sectionsPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_SECTIONS, SECTIONS_DEFAULT);
    restPositionPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_RESTPOSITION, RESTPOSITION_DEFAULT);
//        positionPrefAdapter =
//                new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_POSITION, POSITION_DEFAULT);
    referencePositionPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_REFPOS, REFPOS_DEFAULT);
    nmPerIncrementPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_NMPERINCREMENT, NMPERINCREMENT_DEFAULT);
    backlashPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_BACKLASH, BACKLASH_DEFAULT);
    preStepsPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_PRESTEPS, PRESTEPS_DEFAULT);
    reactionLatencyPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_REACTIONLATENCY, REACTIONLATENCY_DEFAULT);
    settleDelayPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_SETTLEDELAY, SETTLEDELAY_DEFAULT);
    previewDelayPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_PREVIEWDELAY, PREVIEWDELAY_DEFAULT);
    velocityPrefAdapter =
            new PreferencesAdapter(prefs, StageZModel.PROPERTYNAME_VELOCITY, VELOCITY_DEFAULT);
  }
  //
  // <editor-fold defaultstate="collapsed" desc="<<< Accessors >>>">

  /**
   * Gets the current value of highLimit from preferences
   * @return Current value of highLimit
   */
  public double getHighLimit() {
    highLimit = highLimitPrefAdapter.getInt();
    return highLimit;
  }

  /**
   * Sets the value of highLimit to preferences
   * @param highLimit New value for highLimit
   */
  public void setHighLimit(double highLimit) {
    double oldHighLimit = getHighLimit();
    this.highLimit = highLimit;
    highLimitPrefAdapter.setDouble(highLimit);
    firePropertyChange(PROPERTYNAME_HIGHLIMIT, oldHighLimit, highLimit);
  }

  /**
   * Gets the current value of lowLimit from preferences
   * @return Current value of lowLimit
   */
  public double getLowLimit() {
    lowLimit = lowLimitPrefAdapter.getDouble();
    return lowLimit;
  }

  /**
   * Sets the value of lowLimit to preferences
   * @param lowLimit New value for lowLimit
   */
  public void setLowLimit(double lowLimit) {
    double oldLowLimit = getLowLimit();
    this.lowLimit = lowLimit;
    lowLimitPrefAdapter.setDouble(lowLimit);
    firePropertyChange(PROPERTYNAME_LOWLIMIT, oldLowLimit, lowLimit);
  }

  /**
   * Gets the current value of rangeTop from preferences
   * @return Current value of rangeTop
   */
  public double getRangeTop() {
    rangeTop = rangeTopPrefAdapter.getDouble();
    return rangeTop;
  }

  /**
   * Sets the value of rangeTop to preferences
   * @param rangeTop New value for rangeTop
   */
  public void setRangeTop(double rangeTop) {
    double oldRangeTop = getRangeTop();
    this.rangeTop = rangeTop;
    rangeTopPrefAdapter.setDouble(rangeTop);
    firePropertyChange(PROPERTYNAME_RANGETOP, oldRangeTop, rangeTop);
  }

  /**
   * Gets the current value of rangeBottom from preferences
   * @return Current value of rangeBottom
   */
  public double getRangeBottom() {
    rangeBottom = rangeBottomPrefAdapter.getDouble();
    return rangeBottom;
  }

  /**
   * Sets the value of rangeBottom to preferences
   * @param rangeBottom New value for rangeBottom
   */
  public void setRangeBottom(double rangeBottom) {
    double oldRangeBottom = getRangeBottom();
    this.rangeBottom = rangeBottom;
    rangeBottomPrefAdapter.setDouble(rangeBottom);
    firePropertyChange(PROPERTYNAME_RANGEBOTTOM, oldRangeBottom, rangeBottom);
  }

  /**
   * Gets the current value of rangeBottom from preferences
   * @return Current value of rangeBottom
   */
  public double getRange() {
    return range;
  }

  /**
   * Sets the value of rangeBottom to preferences
   * @param rangeBottom New value for rangeBottom
   */
  public void setRange(double range) {
    double oldRange = getRange();
    this.range = range;
    firePropertyChange(PROPERTYNAME_RANGE, oldRange, range);
  }

  /**
   * Gets the current value of moveIncrement from preferences
   * @return Current value of increment
   */
  public double getMoveIncrement() {
    moveIncrement = moveIncrementPrefAdapter.getDouble();
    return moveIncrement;
  }

  /**
   * Sets the value of increment to preferences
   * @param increment New value for increment
   */
  public void setMoveIncrement(double increment) {
    double oldIncrement = getMoveIncrement();
    this.moveIncrement = increment;
    moveIncrementPrefAdapter.setDouble(increment);
    firePropertyChange(PROPERTYNAME_MOVEINCREMENT, oldIncrement, increment);
  }

  /**
   * Gets the current value of increment from preferences
   * @return Current value of increment
   */
  public double getIncrement() {
    increment = incrementPrefAdapter.getDouble();
    return increment;
  }

  /**
   * Sets the value of increment to preferences
   * @param increment New value for increment
   */
  public void setIncrement(double increment) {
    double oldIncrement = getIncrement();
    this.increment = increment;
    incrementPrefAdapter.setDouble(increment);
    setRange((double) (getIncrement() * getSections()));
    firePropertyChange(PROPERTYNAME_INCREMENT, oldIncrement, increment);
  }

  /**
   * Gets the current value of sections from preferences
   * @return Current value of sections
   */
  public int getSections() {
    sections = sectionsPrefAdapter.getInt();
    return sections;
  }

  /**
   * Sets the value of sections to preferences
   * @param sections New value for sections
   */
  public void setSections(int sections) {
    int oldSections = getSections();
    this.sections = sections;
    sectionsPrefAdapter.setInt(sections);
    setRange((int) (getIncrement() * getSections())); // <<--
    firePropertyChange(PROPERTYNAME_SECTIONS, oldSections, sections);
  }

  /**
   * Gets the current value of restPosition from preferences
   * @return Current value of restPosition
   */
  public double getRestPosition() {
    restPosition = restPositionPrefAdapter.getDouble();
    return restPosition;
  }

  /**
   * Sets the value of restPosition to preferences
   * @param restPosition New value for restPosition
   */
  public void setRestPosition(double restPosition) {
    double oldRestPosition = getRestPosition();
    this.restPosition = restPosition;
    restPositionPrefAdapter.setDouble(restPosition);
    firePropertyChange(PROPERTYNAME_RESTPOSITION, oldRestPosition, restPosition);
  }

  /**
   * Gets the current value of position from preferences
   * @return Current value of position
   */
  public double getPosition() {
    return position;
  }

  /**
   * Sets the value of position to preferences
   * @param position New value for position
   */
  public void setPosition(double position) {
    double oldPosition = getPosition();
    this.position = position;
    firePropertyChange(PROPERTYNAME_POSITION, oldPosition, position);
  }

  /**
   * Gets the current value of referencePosition from preferences
   * @return Current value of referencePosition
   */
  public double getReferencePosition() {
    referencePosition = referencePositionPrefAdapter.getDouble();
    return referencePosition;
  }

  /**
   * Sets the value of referencePosition to preferences
   * @param referencePosition New value for referencePosition
   */
  public void setReferencePosition(double referencePosition) {
    double oldReferencePosition = getReferencePosition();
    this.referencePosition = referencePosition;
    referencePositionPrefAdapter.setDouble(referencePosition);
    firePropertyChange(PROPERTYNAME_REFPOS, oldReferencePosition, referencePosition);
  }

  /**
   * Gets the current value of nmPerIncrement from preferences
   * @return Current value of nmPerIncrement
   */
  public int getNmPerIncrement() {
    nmPerIncrement = nmPerIncrementPrefAdapter.getInt();
    return nmPerIncrement;
  }

  /**
   * Sets the value of nmPerIncrement to preferences
   * @param nmPerIncrement New value for nmPerIncrement
   */
  public void setNmPerIncrement(int nmPerIncrement) {
    int oldNmPerIncrement = getNmPerIncrement();
    this.nmPerIncrement = nmPerIncrement;
    nmPerIncrementPrefAdapter.setInt(nmPerIncrement);
    firePropertyChange(PROPERTYNAME_NMPERINCREMENT, oldNmPerIncrement, nmPerIncrement);
  }

  /**
   * Gets the current value of backlash from preferences
   * @return Current value of backlash
   */
  public double getBacklash() {
    backlash = backlashPrefAdapter.getDouble();
    return backlash;
  }

  /**
   * Sets the value of backlash to preferences
   * @param backlash New value for backlash
   */
  public void setBacklash(double backlash) {
    double oldBacklash = getBacklash();
    this.backlash = backlash;
    backlashPrefAdapter.setDouble(backlash);
    firePropertyChange(PROPERTYNAME_BACKLASH, oldBacklash, backlash);
  }

  /**
   * Gets the current value of preSteps from preferences
   * @return Current value of preSteps
   */
  public int getPreSteps() {
    preSteps = preStepsPrefAdapter.getInt();
    return preSteps;
  }

  /**
   * Sets the value of preSteps to preferences
   * @param preSteps New value for preSteps
   */
  public void setPreSteps(int preSteps) {
    int oldPreSteps = getPreSteps();
    this.preSteps = preSteps;
    preStepsPrefAdapter.setInt(preSteps);
    firePropertyChange(PROPERTYNAME_PRESTEPS, oldPreSteps, preSteps);
  }

  /**
   * Gets the current value of reactionLatency from preferences
   * @return Current value of reactionLatency
   */
  public int getReactionLatency() {
    reactionLatency = reactionLatencyPrefAdapter.getInt();
    return reactionLatency;
  }

  /**
   * Sets the value of reactionLatency to preferences
   * @param reactionLatency New value for reactionLatency
   */
  public void setReactionLatency(int reactionLatency) {
    int oldReactionLatency = getReactionLatency();
    this.reactionLatency = reactionLatency;
    reactionLatencyPrefAdapter.setInt(reactionLatency);
    firePropertyChange(PROPERTYNAME_REACTIONLATENCY, oldReactionLatency, reactionLatency);
  }

  /**
   * Gets the current value of settleDelay from preferences
   * @return Current value of settleDelay
   */
  public int getSettleDelay() {
    settleDelay = settleDelayPrefAdapter.getInt();
    return settleDelay;
  }

  /**
   * Sets the value of settleDelay to preferences
   * @param settleDelay New value for settleDelay
   */
  public void setSettleDelay(int settleDelay) {
    int oldSettleDelay = getSettleDelay();
    this.settleDelay = settleDelay;
    settleDelayPrefAdapter.setInt(settleDelay);
    firePropertyChange(PROPERTYNAME_SETTLEDELAY, oldSettleDelay, settleDelay);
  }

  /**
   * Gets the current value of previewDelay from preferences
   * @return Current value of previewDelay
   */
  public int getPreviewDelay() {
    previewDelay = previewDelayPrefAdapter.getInt();
    return previewDelay;
  }

  /**
   * Sets the value of previewDelay to preferences
   * @param previewDelay New value for previewDelay
   */
  public void setPreviewDelay(int previewDelay) {
    int oldPreviewDelay = getPreviewDelay();
    this.previewDelay = previewDelay;
    previewDelayPrefAdapter.setInt(previewDelay);
    firePropertyChange(PROPERTYNAME_PREVIEWDELAY, oldPreviewDelay, previewDelay);
  }

  /**
   * Gets the current value of velocity from preferences
   * @return Current value of velocity
   */
  public float getVelocity() {
    velocity = velocityPrefAdapter.getFloat();
    return velocity;
  }

  /**
   * Sets the value of velocity to preferences
   * @param velocity New value for velocity
   */
  public void setVelocity(float velocity) {
    float oldVelocity = getVelocity();
    this.velocity = velocity;
    velocityPrefAdapter.setFloat(velocity);
    firePropertyChange(PROPERTYNAME_VELOCITY, oldVelocity, velocity);
  }

  // </editor-fold>
}