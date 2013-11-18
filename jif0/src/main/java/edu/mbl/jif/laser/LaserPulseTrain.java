
package edu.mbl.jif.laser;

import edu.mbl.jif.stage.DigitalOutputMock;

/**
 *
 * @author GBH
 */
public class LaserPulseTrain
        implements Operation {

    LaserControllerDigital laserCtrl;
    private long numPulses = 0;
    private double interval = 0;

    // expects three parameters, laserController, number of pulses, pulse interval
    public LaserPulseTrain(Object... args) {
                if (args.length > 0) {
            int n = 0;
            for (Object o : args) {
                if (n == 0) {
                    laserCtrl = (LaserControllerDigital) o;
                }                
                if (n == 1) {
                    numPulses = (Long) o;
                }
                if (n == 2) {
                    interval = (Double) o;
                }
                n++;
            }
        }
    }

    @Override
    public int perform() {
        laserCtrl.pulse(numPulses, interval);
        System.out.println("Performing...");
        System.out.println("numPulses = " + numPulses);
        System.out.println("period = " + interval);
        return 0;
    }

    public static void main(String[] args) {
        LaserControllerDigital laserCtrl = new LaserControllerDigital( new DigitalOutputMock());
        Operation laser = new LaserPulseTrain(laserCtrl,10,300);
        laser.perform();
    }

}

