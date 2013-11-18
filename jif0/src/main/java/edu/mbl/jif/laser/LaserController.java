package edu.mbl.jif.laser;

/**
 * LaserController Interface
 * @author GBH
 */
public interface LaserController {

    void setup(long numPulses, double interval);

    void pulse(long numPulses, double interval);

    void firePeriod(double period);

    void burst();

    void stop();

}
