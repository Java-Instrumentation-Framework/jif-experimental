package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AcquisitionDevice;


/*
 * AcqDeviceTest.java
 *
 * Created on January 31, 2007, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


/**
 *
 * @author GBH
 */
public class AcqDeviceTest implements AcquisitionDevice {
    

    /** Creates a new instance of AcqDeviceTest */
    public AcqDeviceTest() {
    }

    public boolean isCapable() {
        return true;
    }

    public boolean isSeriesCapable() {
        return true;
    }
    
}
