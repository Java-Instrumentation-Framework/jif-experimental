package edu.mbl.jif.joystick;

/**
 *
 * @author GBH
 */
public interface PolledDeviceData {

     // Getting the values in the data is up to the specific implementation...
    
    /**
     * Get the device which generated this data.
     */
    PolledDevice getSource();

    /**
     * Get the time in nanoseconds when this data object had been generated.
     */
    long getWhen();
    

}
