package edu.mbl.jif.laser;

import edu.mbl.jif.stage.DigitalOutput;

/**
 * Old Laser pulse controller interfaced thru PI Stage controller digital IO port
 * @author GBH
 */
public class LaserControllerDigital implements LaserController {

    DigitalOutput output;

    public LaserControllerDigital(DigitalOutput output) {
        this.output = output;
    }

    
    public void pulse(long numPulses, double interval) {
        int intervalL = (int)Math.round(interval);
        output.dioRepeat((int)numPulses, intervalL);
    }
    
    public void firePeriod(double period) {
        long p = (long) Math.round(period);
        System.out.println("firePeriod: " + p);
        output.setOutput(1, true);
        try {
            Thread.sleep(p);
        } catch (InterruptedException ex) {
        }
        output.setOutput(1, false);        
        
    }

    @Override
    public void setup(long numPulses, double interval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void burst() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
