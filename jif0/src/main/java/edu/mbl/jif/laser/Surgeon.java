/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.laser;

import edu.mbl.jif.stage.*;
import edu.mbl.jif.gui.spatial.Path;
import edu.mbl.jif.gui.spatial.PathCreator;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.SwingUtilities;

/**
 *   ######## SUPERFLUOUS ############# July 30, 09
 * @author GBH
 */
public class Surgeon {

    StageXYController stageXYCtrl;
    LaserController laser;
    private Point2D endPointOfPath;
    private PanelPath pathPanel;

    public Surgeon(StageXYController stageXYCtrl, LaserController laser) {
        this.stageXYCtrl = stageXYCtrl;
        this.laser = laser;
    }

    // <editor-fold defaultstate="collapsed" desc=">>>--- Getters/Setters ---<<<" >
    protected double beginX = 8.0;
    public static final String PROP_BEGINX = "beginX";

    /**
     * Get the value of beginX
     *
     * @return the value of beginX
     */
    public double getBeginX() {
        return beginX;
    }

    /**
     * Set the value of beginX
     *
     * @param beginX new value of beginX
     */
    public void setBeginX(double beginX) {
        double oldBeginX = this.beginX;
        this.beginX = beginX;
        propertyChangeSupport.firePropertyChange(PROP_BEGINX, oldBeginX, beginX);
    }

    protected double beginY = 8.0;
    public static final String PROP_BEGINY = "beginY";

    /**
     * Get the value of beginY
     *
     * @return the value of beginY
     */
    public double getBeginY() {
        return beginY;
    }

    /**
     * Set the value of beginY
     *
     * @param beginY new value of beginY
     */
    public void setBeginY(double beginY) {
        double oldBeginY = this.beginY;
        this.beginY = beginY;
        propertyChangeSupport.firePropertyChange(PROP_BEGINY, oldBeginY, beginY);
    }

    protected double endX = 8.0;
    public static final String PROP_ENDX = "endX";

    /**
     * Get the value of endX
     *
     * @return the value of endX
     */
    public double getEndX() {
        return endX;
    }

    /**
     * Set the value of endX
     *
     * @param endX new value of endX
     */
    public void setEndX(double endX) {
        double oldEndX = this.endX;
        this.endX = endX;
        propertyChangeSupport.firePropertyChange(PROP_ENDX, oldEndX, endX);
    }

    protected double endY = 8.0;
    public static final String PROP_ENDY = "endY";

    /**
     * Get the value of endY
     *
     * @return the value of endY
     */
    public double getEndY() {
        return endY;
    }

    /**
     * Set the value of endY
     *
     * @param endY new value of endY
     */
    public void setEndY(double endY) {
        double oldEndY = this.endY;
        this.endY = endY;
        propertyChangeSupport.firePropertyChange(PROP_ENDY, oldEndY, endY);
    }

    protected double pathIncrement = 1.0;
    public static final String PROP_PATHINCREMENT = "pathIncrement";
    protected double pathLength = 0;
    public static final String PROP_PATHLENGTH = "pathLength";

    /**
     * Get the value of pathLength
     *
     * @return the value of pathLength
     */
    public double getPathLength() {
        return pathLength;
    }

    /**
     * Set the value of pathLength
     *
     * @param pathLength new value of pathLength
     */
    public void setPathLength(double pathLength) {
        double oldPathLength = this.pathLength;
        this.pathLength = pathLength;
        propertyChangeSupport.firePropertyChange(PROP_PATHLENGTH, oldPathLength, pathLength);
    }

    protected double pathAngle = 0;
    public static final String PROP_PATHANGLE = "pathAngle";

    /**
     * Get the value of pathAngle
     *
     * @return the value of pathAngle
     */
    public double getPathAngle() {
        return pathAngle;
    }

    /**
     * Set the value of pathAngle
     *
     * @param pathAngle new value of pathAngle
     */
    public void setPathAngle(double pathAngle) {
        double oldPathAngle = this.pathAngle;
        this.pathAngle = pathAngle;
        propertyChangeSupport.firePropertyChange(PROP_PATHANGLE, oldPathAngle, pathAngle);
    }

    /**
     * Get the value of pathIncrement
     *
     * @return the value of pathIncrement
     */
    public double getPathIncrement() {
        return pathIncrement;
    }

    /**
     * Set the value of pathIncrement
     *
     * @param pathIncrement new value of pathIncrement
     */
    public void setPathIncrement(double pathIncrement) {
        double oldPathIncrement = this.pathIncrement;
        this.pathIncrement = pathIncrement;
        propertyChangeSupport.firePropertyChange(PROP_PATHINCREMENT, oldPathIncrement, pathIncrement);
    }

    protected long numberPulses = 10;
    public static final String PROP_NUMBERPULSES = "numberPulses";

    /**
     * Get the value of numberPulses
     *
     * @return the value of numberPulses
     */
    public long getNumberPulses() {
        return numberPulses;
    }

    /**
     * Set the value of numberPulses
     *
     * @param numberPulses new value of numberPulses
     */
    public void setNumberPulses(long numberPulses) {
        long oldNumberPulses = this.numberPulses;
        this.numberPulses = numberPulses;
        propertyChangeSupport.firePropertyChange(PROP_NUMBERPULSES, oldNumberPulses, numberPulses);
    }

    protected double pulseFreq = 5000;
    public static final String PROP_PULSEFREQ = "pulseFreq";

    /**
     * Get the value of pulseFreq
     *
     * @return the value of pulseFreq
     */
    public double getPulseFreq() {
        return pulseFreq;
    }

    /**
     * Set the value of pulseFreq
     *
     * @param pulseFreq new value of pulseFreq
     */
    public void setPulsePeriod(double pulseFreq) {
        double oldPulseFreq = this.pulseFreq;
        this.pulseFreq = pulseFreq;
        propertyChangeSupport.firePropertyChange(PROP_PULSEFREQ, oldPulseFreq, pulseFreq);
    }

    protected double delayBetweenPoints = 10;
    public static final String PROP_DELAYBETWEENPOINTS = "delayBetweenPoints";

    /**
     * Get the value of delayBetweenPoints
     *
     * @return the value of delayBetweenPoints
     */
    public double getDelayBetweenPoints() {
        return delayBetweenPoints;
    }

    /**
     * Set the value of delayBetweenPoints
     *
     * @param delayBetweenPoints new value of delayBetweenPoints
     */
    public void setDelayBetweenPoints(double delayBetweenPoints) {
        double oldDelayBetweenPoints = this.delayBetweenPoints;
        this.delayBetweenPoints = delayBetweenPoints;
        propertyChangeSupport.firePropertyChange(PROP_DELAYBETWEENPOINTS, oldDelayBetweenPoints, delayBetweenPoints);
    }

    protected double firePeriod = 100;
    public static final String PROP_FIREPERIOD = "firePeriod";

    /**
     * Get the value of firePeriod
     *
     * @return the value of firePeriod
     */
    public double getFirePeriod() {
        return firePeriod;
    }

    /**
     * Set the value of firePeriod
     *
     * @param firePeriod new value of firePeriod
     */
    public void setFirePeriod(double firePeriod) {
        double oldFirePeriod = this.firePeriod;
        this.firePeriod = firePeriod;
        propertyChangeSupport.firePropertyChange(PROP_FIREPERIOD, oldFirePeriod, firePeriod);
    }


    protected double backlashX = 7;    ;
    public static final String PROP_BACKLASHX = "backlashX";

    public double getBacklashX() {
        return backlashX;
    }

    public void setBacklashX(double backlashX) {
        double oldBacklashX = this.backlashX;
        this.backlashX = backlashX;
        propertyChangeSupport.firePropertyChange(PROP_BACKLASHX, oldBacklashX, backlashX);
    }

    protected double backlashY = 7;
    public static final String PROP_BACKLASHY = "backlashY";

    public double getBacklashY() {
        return backlashY;
    }

    public void setBacklashY(double backlashY) {
        double oldBacklashY = this.backlashY;
        this.backlashY = backlashY;
        propertyChangeSupport.firePropertyChange(PROP_BACKLASHY, oldBacklashY, backlashY);
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
        laser.setup(getNumberPulses(), 1000000/getPulseFreq());
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
    // <editor-fold defaultstate="collapsed" desc=">>>--- PropertyChangeSupport ---<<<" >
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }



    // </editor-fold>
}
