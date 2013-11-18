/*
 * AcquisitionDevice.java
 *
 * Created on January 28, 2007, 4:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.acq.tasking;

/**
 *
 * @author GBH
 */
public interface AcquisitionDevice {
    
    public boolean isCapable();
    public boolean isSeriesCapable();
}
