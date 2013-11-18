package edu.mbl.jif.laser;

import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.spatial.Path;
import edu.mbl.jif.gui.spatial.PathCreator;
import edu.mbl.jif.stage.StageXYController;
import java.awt.geom.Point2D;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;

public class SurgeonModel extends Model {

  Preferences prefs;
  static final String DEFAULT_PREFS_KEY = "Surgeon";
  // <editor-fold defaultstate="collapsed" desc="<<< Declarations >>>">

  // --- beginX ---
  public static final String PROPERTYNAME_BEGINX = "beginX";
  private double beginX;
  static final double BEGINX_DEFAULT = 0;
  PreferencesAdapter beginXPrefAdapter;

  // --- beginY ---
  public static final String PROPERTYNAME_BEGINY = "beginY";
  private double beginY;
  static final double BEGINY_DEFAULT = 0;
  PreferencesAdapter beginYPrefAdapter;

  // --- endX ---
  public static final String PROPERTYNAME_ENDX = "endX";
  private double endX;
  static final double ENDX_DEFAULT = 0;
  PreferencesAdapter endXPrefAdapter;

  // --- endY ---
  public static final String PROPERTYNAME_ENDY = "endY";
  private double endY;
  static final double ENDY_DEFAULT = 0;
  PreferencesAdapter endYPrefAdapter;

  // --- pathIncrement ---
  public static final String PROPERTYNAME_PATHINCREMENT = "pathIncrement";
  private double pathIncrement;
  static final double PATHINCREMENT_DEFAULT = 1;
  PreferencesAdapter pathIncrementPrefAdapter;

  // --- pathLength ---
  public static final String PROPERTYNAME_PATHLENGTH = "pathLength";
  private double pathLength;
  static final double PATHLENGTH_DEFAULT = 0;
  PreferencesAdapter pathLengthPrefAdapter;

  // --- pathAngle ---
  public static final String PROPERTYNAME_PATHANGLE = "pathAngle";
  private double pathAngle;
  static final double PATHANGLE_DEFAULT = 0;
  PreferencesAdapter pathAnglePrefAdapter;

  // --- numberPulses ---
  public static final String PROPERTYNAME_NUMBERPULSES = "numberPulses";
  private double numberPulses;
  static final double NUMBERPULSES_DEFAULT = 1;
  PreferencesAdapter numberPulsesPrefAdapter;

  // --- pulseFreq ---
  public static final String PROPERTYNAME_PULSEFREQ = "pulseFreq";
  private double pulseFreq;
  static final double PULSEFREQ_DEFAULT = 1;
  PreferencesAdapter pulseFreqPrefAdapter;

  // --- delayBetweenPoints ---
  public static final String PROPERTYNAME_DELAYBETWEENPOINTS = "delayBetweenPoints";
  private double delayBetweenPoints;
  static final double DELAYBETWEENPOINTS_DEFAULT = 1;
  PreferencesAdapter delayBetweenPointsPrefAdapter;

  // --- firePeriod ---
  public static final String PROPERTYNAME_FIREPERIOD = "firePeriod";
  private double firePeriod;
  static final double FIREPERIOD_DEFAULT = 1;
  PreferencesAdapter firePeriodPrefAdapter;

  // --- backlashX ---
  public static final String PROPERTYNAME_BACKLASHX = "backlashX";
  private double backlashX;
  static final double BACKLASHX_DEFAULT = 0;
  PreferencesAdapter backlashXPrefAdapter;

  // --- backlashY ---
  public static final String PROPERTYNAME_BACKLASHY = "backlashY";
  private double backlashY;
  static final double BACKLASHY_DEFAULT = 0;
  PreferencesAdapter backlashYPrefAdapter;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< Constructor >>>">
  InstrumentController instrumentCtrl;
  //
  StageXYController stageXYCtrl;
  LaserController laser;
  private Point2D endPointOfPath;
  private PanelPath pathPanel;

  public SurgeonModel(InstrumentController instrumentCtr) {
    this(instrumentCtr, DEFAULT_PREFS_KEY);
  }

  public SurgeonModel(InstrumentController instrumentCtr, String key) {
    this.instrumentCtrl = instrumentCtr;
    this.stageXYCtrl = (StageXYController) instrumentCtrl.getController("xyStage");
    this.laser = (LaserController) instrumentCtrl.getController("laser");
    initializePreferencesAdapters(key);

  }

  // </editor-fold>

  public void initializePreferencesAdapters(String key) {
    prefs = Application.getInstance().getPreferences().node(key);
    beginXPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_BEGINX, BEGINX_DEFAULT);
    beginYPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_BEGINY, BEGINY_DEFAULT);
    endXPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_ENDX, ENDX_DEFAULT);
    endYPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_ENDY, ENDY_DEFAULT);
    pathIncrementPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_PATHINCREMENT, PATHINCREMENT_DEFAULT);
    pathLengthPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_PATHLENGTH, PATHLENGTH_DEFAULT);
    pathAnglePrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_PATHANGLE, PATHANGLE_DEFAULT);
    numberPulsesPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_NUMBERPULSES, NUMBERPULSES_DEFAULT);
    pulseFreqPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_PULSEFREQ, PULSEFREQ_DEFAULT);
    delayBetweenPointsPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_DELAYBETWEENPOINTS, DELAYBETWEENPOINTS_DEFAULT);
    firePeriodPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_FIREPERIOD, FIREPERIOD_DEFAULT);
    backlashXPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_BACKLASHX, BACKLASHX_DEFAULT);
    backlashYPrefAdapter =
            new PreferencesAdapter(prefs, SurgeonModel.PROPERTYNAME_BACKLASHY, BACKLASHY_DEFAULT);
  }
  //
  // <editor-fold defaultstate="collapsed" desc="<<< Accessors >>>">

  /**
   * Gets the current value of beginX from preferences
   * @return Current value of beginX
   */
  public double getBeginX() {
    beginX = beginXPrefAdapter.getDouble();
    return beginX;
  }

  /**
   * Sets the value of beginX to preferences
   * @param beginX New value for beginX
   */
  public void setBeginX(double beginX) {
    double oldBeginX = getBeginX();
    this.beginX = beginX;
    beginXPrefAdapter.setDouble(beginX);
    firePropertyChange(PROPERTYNAME_BEGINX, oldBeginX, beginX);
  }

  /**
   * Gets the current value of beginY from preferences
   * @return Current value of beginY
   */
  public double getBeginY() {
    beginY = beginYPrefAdapter.getDouble();
    return beginY;
  }

  /**
   * Sets the value of beginY to preferences
   * @param beginY New value for beginY
   */
  public void setBeginY(double beginY) {
    double oldBeginY = getBeginY();
    this.beginY = beginY;
    beginYPrefAdapter.setDouble(beginY);
    firePropertyChange(PROPERTYNAME_BEGINY, oldBeginY, beginY);
  }

  /**
   * Gets the current value of endX from preferences
   * @return Current value of endX
   */
  public double getEndX() {
    endX = endXPrefAdapter.getDouble();
    return endX;
  }

  /**
   * Sets the value of endX to preferences
   * @param endX New value for endX
   */
  public void setEndX(double endX) {
    double oldEndX = getEndX();
    this.endX = endX;
    endXPrefAdapter.setDouble(endX);
    firePropertyChange(PROPERTYNAME_ENDX, oldEndX, endX);
  }

  /**
   * Gets the current value of endY from preferences
   * @return Current value of endY
   */
  public double getEndY() {
    endY = endYPrefAdapter.getDouble();
    return endY;
  }

  /**
   * Sets the value of endY to preferences
   * @param endY New value for endY
   */
  public void setEndY(double endY) {
    double oldEndY = getEndY();
    this.endY = endY;
    endYPrefAdapter.setDouble(endY);
    firePropertyChange(PROPERTYNAME_ENDY, oldEndY, endY);
  }

  /**
   * Gets the current value of pathIncrement from preferences
   * @return Current value of pathIncrement
   */
  public double getPathIncrement() {
    pathIncrement = pathIncrementPrefAdapter.getDouble();
    return pathIncrement;
  }

  /**
   * Sets the value of pathIncrement to preferences
   * @param pathIncrement New value for pathIncrement
   */
  public void setPathIncrement(double pathIncrement) {
    double oldPathIncrement = getPathIncrement();
    this.pathIncrement = pathIncrement;
    pathIncrementPrefAdapter.setDouble(pathIncrement);
    firePropertyChange(PROPERTYNAME_PATHINCREMENT, oldPathIncrement, pathIncrement);
  }

  /**
   * Gets the current value of pathLength from preferences
   * @return Current value of pathLength
   */
  public double getPathLength() {
    pathLength = pathLengthPrefAdapter.getDouble();
    return pathLength;
  }

  /**
   * Sets the value of pathLength to preferences
   * @param pathLength New value for pathLength
   */
  public void setPathLength(double pathLength) {
    double oldPathLength = getPathLength();
    this.pathLength = pathLength;
    pathLengthPrefAdapter.setDouble(pathLength);
    firePropertyChange(PROPERTYNAME_PATHLENGTH, oldPathLength, pathLength);
  }

  /**
   * Gets the current value of pathAngle from preferences
   * @return Current value of pathAngle
   */
  public double getPathAngle() {
    pathAngle = pathAnglePrefAdapter.getDouble();
    return pathAngle;
  }

  /**
   * Sets the value of pathAngle to preferences
   * @param pathAngle New value for pathAngle
   */
  public void setPathAngle(double pathAngle) {
    double oldPathAngle = getPathAngle();
    this.pathAngle = pathAngle;
    pathAnglePrefAdapter.setDouble(pathAngle);
    firePropertyChange(PROPERTYNAME_PATHANGLE, oldPathAngle, pathAngle);
  }

  /**
   * Gets the current value of numberPulses from preferences
   * @return Current value of numberPulses
   */
  public double getNumberPulses() {
    numberPulses = numberPulsesPrefAdapter.getDouble();
    return numberPulses;
  }

  /**
   * Sets the value of numberPulses to preferences
   * @param numberPulses New value for numberPulses
   */
  public void setNumberPulses(double numberPulses) {
    double oldNumberPulses = getNumberPulses();
    this.numberPulses = numberPulses;
    numberPulsesPrefAdapter.setDouble(numberPulses);
    firePropertyChange(PROPERTYNAME_NUMBERPULSES, oldNumberPulses, numberPulses);
  }

  /**
   * Gets the current value of pulseFreq from preferences
   * @return Current value of pulseFreq
   */
  public double getPulseFreq() {
    pulseFreq = pulseFreqPrefAdapter.getDouble();
    return pulseFreq;
  }

  /**
   * Sets the value of pulseFreq to preferences
   * @param pulseFreq New value for pulseFreq
   */
  public void setPulseFreq(double pulseFreq) {
    double oldPulseFreq = getPulseFreq();
    this.pulseFreq = pulseFreq;
    pulseFreqPrefAdapter.setDouble(pulseFreq);
    firePropertyChange(PROPERTYNAME_PULSEFREQ, oldPulseFreq, pulseFreq);
  }

  /**
   * Gets the current value of delayBetweenPoints from preferences
   * @return Current value of delayBetweenPoints
   */
  public double getDelayBetweenPoints() {
    delayBetweenPoints = delayBetweenPointsPrefAdapter.getDouble();
    return delayBetweenPoints;
  }

  /**
   * Sets the value of delayBetweenPoints to preferences
   * @param delayBetweenPoints New value for delayBetweenPoints
   */
  public void setDelayBetweenPoints(double delayBetweenPoints) {
    double oldDelayBetweenPoints = getDelayBetweenPoints();
    this.delayBetweenPoints = delayBetweenPoints;
    delayBetweenPointsPrefAdapter.setDouble(delayBetweenPoints);
    firePropertyChange(PROPERTYNAME_DELAYBETWEENPOINTS, oldDelayBetweenPoints, delayBetweenPoints);
  }

  /**
   * Gets the current value of firePeriod from preferences
   * @return Current value of firePeriod
   */
  public double getFirePeriod() {
    firePeriod = firePeriodPrefAdapter.getDouble();
    return firePeriod;
  }

  /**
   * Sets the value of firePeriod to preferences
   * @param firePeriod New value for firePeriod
   */
  public void setFirePeriod(double firePeriod) {
    double oldFirePeriod = getFirePeriod();
    this.firePeriod = firePeriod;
    firePeriodPrefAdapter.setDouble(firePeriod);
    firePropertyChange(PROPERTYNAME_FIREPERIOD, oldFirePeriod, firePeriod);
  }

  /**
   * Gets the current value of backlashX from preferences
   * @return Current value of backlashX
   */
  public double getBacklashX() {
    backlashX = backlashXPrefAdapter.getDouble();
    return backlashX;
  }

  /**
   * Sets the value of backlashX to preferences
   * @param backlashX New value for backlashX
   */
  public void setBacklashX(double backlashX) {
    double oldBacklashX = getBacklashX();
    this.backlashX = backlashX;
    backlashXPrefAdapter.setDouble(backlashX);
    firePropertyChange(PROPERTYNAME_BACKLASHX, oldBacklashX, backlashX);
  }

  /**
   * Gets the current value of backlashY from preferences
   * @return Current value of backlashY
   */
  public double getBacklashY() {
    backlashY = backlashYPrefAdapter.getDouble();
    return backlashY;
  }

  /**
   * Sets the value of backlashY to preferences
   * @param backlashY New value for backlashY
   */
  public void setBacklashY(double backlashY) {
    double oldBacklashY = getBacklashY();
    this.backlashY = backlashY;
    backlashYPrefAdapter.setDouble(backlashY);
    firePropertyChange(PROPERTYNAME_BACKLASHY, oldBacklashY, backlashY);
  }

  // </editor-fold>
  //    public void approach() {
//        double dX = this.getPathLength() * Math.cos(Math.toRadians(getPathAngle()));
//        double dY = this.getPathLength() * Math.sin(Math.toRadians(getPathAngle()));
//        Point2D endPoint = new Point2D.Double(getBeginX() + dX, getBeginY() + dY);
//        System.out.println("dX, dY: " + dX + ", " + dY + "   endpoint: " + endPoint);
//
//    }
  public void calculateEndPoint() {
    double dX = this.getPathLength() * Math.cos(Math.toRadians(getPathAngle()));
    double dY = this.getPathLength() * Math.sin(Math.toRadians(getPathAngle()));
    Point2D endPoint = new Point2D.Double(getBeginX() + dX, getBeginY() + dY);
    System.out.println("dX, dY: " + dX + ", " + dY + "   endpoint: " + endPoint);
    endPointOfPath = endPoint;
  }

  public void doApproach(PanelPath pathPanel) {
    this.pathPanel = pathPanel;
    // Approach is 180 deg. from path
    // endPoint for Appraoch is beginPoint
    // beginPoint for Approach backlashDistance at 180 deg. from path beginPoint.
    double dX = this.getBacklashX() * Math.cos(Math.toRadians(getPathAngle() + 180));
    double dY = this.getBacklashY() * Math.sin(Math.toRadians(getPathAngle() + 180));
    Point2D endPoint = new Point2D.Double(getBeginX(), getBeginY());
    Point2D beginPoint = new Point2D.Double(getBeginX() + dX, getBeginY() + dY);
    System.out.println("dX, dY: " + dX + ", " + dY + "   beginPoint: " + beginPoint + "   endPoint: " + endPoint);
    PathWalker pw = new PathWalker(stageXYCtrl);
    Path path = PathCreator.straightLine(beginPoint, endPoint, this.getPathIncrement() / 1000);
    path.listPoints();
    stageXYCtrl.moveTo(beginPoint.getX(), beginPoint.getY());
    // @todo Deal with Backlash...
    stageXYCtrl.presetVelocity(beginPoint, endPoint);
    Operation op = null;
    pw.walk(path, (int) this.getDelayBetweenPoints(), op, pathPanel);
  }

  public void markBegin() {
    Point2D currentPos = stageXYCtrl.getPos();
    this.setBeginX(currentPos.getX());
    this.setBeginY(currentPos.getY());

  }

  public void laserTestFirePeriod() {
    laser.firePeriod(this.getFirePeriod());

  }

  public void preview(PanelPath pathPanel) {
    this.pathPanel = pathPanel;
    burnIt(false);
    System.out.println("Previewing...");
  }

  public void burn(PanelPath pathPanel) {
    this.pathPanel = pathPanel;
    burnIt(true);
    System.out.println("Burning...");
  }

  boolean testFiring = false;

  public void laserUpdate() {
    System.out.println("Update Laser");
    laser.setup((int)getNumberPulses(), 1000000 / getPulseFreq());
  }

  public void laserTestFire(boolean selected) {
    System.out.println("Test Firing Laser... " + selected);
    laser.burst();
  //this.laser.pulse(this.getNumberPulses(), this.getPulseFreq());
  }

  public void burnIt(final boolean burn) {
    this.calculateEndPoint();
    if (endPointOfPath.getX() == getBeginX() && endPointOfPath.getY() == getBeginY()) {
      return;
    }
    // PathWalker pw = new PathWalker(stageXYCtrl);
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        Point2D begin = new Point2D.Double(beginX, beginY);
        Path path = PathCreator.straightLine(begin, endPointOfPath, getPathIncrement() / 1000);
        path.listPoints();
        stageXYCtrl.moveTo(beginX, beginY);
        // @todo Deal with Backlash...
        stageXYCtrl.presetVelocity(begin, endPointOfPath);
        Operation op;
        if (burn) {
          // op = new LaserPulseTrain(laser, getNumberPulses() * 2, getPulseFreq());
          op = new LaserBurst(laser, getNumberPulses() * 2, getPulseFreq());
        } else {
          op = null;
        }
        PathWalker pw = new PathWalker(stageXYCtrl);
        pw.walk(path, (int) getDelayBetweenPoints(), op, pathPanel);
      //SurgeonTaskFrame pw = new SurgeonTaskFrame(stageXYCtrl, path, (int) getDelayBetweenPoints(), op);
      }
    });

  }
}