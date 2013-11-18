/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.joystick.labjack;

import edu.mbl.jif.joystick.PolledDevice;
import edu.mbl.jif.joystick.PolledDeviceDIData;
import edu.mbl.jif.joystick.PolledDeviceData;

/**
 *
 * @author GBH
 */
public class LabJackDevice  implements PolledDevice {

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean open() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PolledDeviceData read() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPollingDelay(int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
