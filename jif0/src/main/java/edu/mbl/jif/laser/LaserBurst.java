
package edu.mbl.jif.laser;

/**
 *
 * @author GBH
 */
public class LaserBurst
        implements Operation {

    LaserController laserCtrl;
    private long numPulses = 0;
    private double interval = 0;

    // expects three parameters, laserController, number of pulses, pulse interval
    public LaserBurst(Object... args) {
                if (args.length > 0) {
            int n = 0;
            for (Object o : args) {
                if (n == 0) {
//                    laserCtrl = (LaserControllerBurst) o;
                }                
                if (n == 1) {
                    numPulses = (Long) o;
                }
                if (n == 2) {
                    interval = (Double) o;
                }
                n++;
            }
            laserCtrl.setup(numPulses, interval);
        }
    }

    @Override
    public int perform() {
        laserCtrl.burst();
        return 0;
    }

    public static void main(String[] args) {
        //LaserController laserCtrl = new LaserControllerBurst(new BurstGenerator(new SerialPortConnection()));
        //Operation laser = new LaserBurst(laserCtrl,10,300);
        //laser.perform();
    }

}

