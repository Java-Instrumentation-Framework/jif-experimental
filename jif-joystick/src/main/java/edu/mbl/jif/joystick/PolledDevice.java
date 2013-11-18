package edu.mbl.jif.joystick;

/**
 *
 * @author GBH
 */
public interface PolledDevice {


    /**
     * Close the device. No further input will be requested.
     */
    void close();


    /**
     * Multiple devices of the same type will differ by an ID
     * value.
     */
    int getID();


    /**
     * Get the name of the device.
     * @return the name of the device.
     */
    String getName();


    /**
     * Open the device.
     * @return true if opening is successfull and false if not.
     */
    boolean open();


    /**
     * Get the current state data of the device.
     * @return a data structure containing the current device
     * state.
     */
    PolledDeviceData read();


    /**
     * Reset the device to an initial state.
     */
    void reset();


    /**
     * Set the delay between successive polling cycles.
     */
    void setPollingDelay(int d);

}
