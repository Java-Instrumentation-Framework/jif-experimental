/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.spatial;

/**
 * This may be linear or rotational...
 * @author GBH
 */
public class AbstractAxisController {

    private double displacement;
    private double offset;  // zero
    private double limitUpper;
    private double limitLower;
    private double velocity;
    private double acceleration;
    private double repeatRate;

/*
    // Limits / Bounds
  int positiveLimit = -1; // upper limit of travel
  int negativeLimit = -1; // lower limit of travel(= refPos)
  
  int refPos = -1; // microscope ref. position
  boolean wasZeroed = false;
 
// IterativeScanAcquisition
  int scanBegin;
  int scanEnd;
  int restPosition;
  int increment = 0; // in nm
  int sections; // number of sections in Z-scan range
  int rangeTop; // top of Z-scan range
  int rangeBottom; // bottom of Z-scan range  

// Mechanical characteristics
  int units;
  int stepSize;
  double  unitsPerStep = 0;
  int backlashSteps = 0;  
  int preSteps = 0;

// State
  position; //could be angle
	velocity;
	acceleration;

// Timing
  int reactionLatency = 10;
  int initDelay = reactionLatency;
  int settleDelay;
  
// Communications
  public SerialConnection io = null;
  String comPort = "";
  public boolean isConnected = false;
  public boolean isFunctional = false;
  
  Kinematic Accuracy & Repeatability
  Backlash
  (hysteresis)
  How to Measure Backlash
  */
    
    // moveTo(double position)
    
    public double getDisplacement() {
        return displacement;
    }

    public void setDisplacement(double displacement) {
        this.displacement = displacement;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getLimitUpper() {
        return limitUpper;
    }

    public void setLimitUpper(double limitUpper) {
        this.limitUpper = limitUpper;
    }

    public double getLimitLower() {
        return limitLower;
    }

    public void setLimitLower(double limitLower) {
        this.limitLower = limitLower;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getRepeatRate() {
        return repeatRate;
    }

    public void setRepeatRate(double repeatRate) {
        this.repeatRate = repeatRate;
    }

}
