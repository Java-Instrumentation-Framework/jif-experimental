/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.laser;

import edu.mbl.jif.stage.*;

/**
 *
 * @author GBH
 */
public class LaserFirePeriod
        implements Operation {

    LaserControllerDigital laserCtrl;
    private long numPulses = 0;
    private double period = 0;

    public LaserFirePeriod(Object... args) {
                if (args.length > 0) {
            int n = 0;
            for (Object o : args) {
                if (n == 0) {
                    laserCtrl = (LaserControllerDigital) o;
                }                
                if (n == 1) {
                    period = (Double) o;
                }
                n++;
            }
        }
    }

    @Override
    public int perform() {
        
        laserCtrl.firePeriod(period);
        System.out.println("Performing...");
        System.out.println("period = " + period);
        return 0;
    }

    public static void main(String[] args) {
        Operation laser = new LaserFirePeriod(10,300);
        laser.perform();
    }

}

