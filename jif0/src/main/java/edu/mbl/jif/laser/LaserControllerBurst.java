package edu.mbl.jif.laser;

/**
 * Laser Controller for the Sanger Burst Generator
 * @author GBH
 */
public class LaserControllerBurst implements LaserController {

    BurstGenerator burstGen;

    public LaserControllerBurst(BurstGenerator output) {
        this.burstGen = output;
    }

    @Override
    public void pulse(long numPulses, double interval) {
        setup(numPulses, interval);
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
        }
        burst();
    }

    @Override
    public void setup(long numPulses, double interval) {
        int intervalL = (int) Math.round(interval);
        burstGen.setPulseWidth(intervalL);
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
        }
        burstGen.setPulses(numPulses);
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void burst() {
        burstGen.start();
    }

    @Override
    public void stop() {
        burstGen.stop();
    }

    @Override
    public void firePeriod(double period) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
