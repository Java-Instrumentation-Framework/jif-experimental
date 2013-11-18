
package edu.mbl.jif.joystick;

/** An external device whose data have to be read by polling. It
    generates button states, directional statess, and axis change
    values.

  @version 0.1.0

*/

public abstract class PolledDeviceDI implements PolledDevice {

    protected int id;

    protected int delayDuration = 0;


    /** Multiple devices of the same type will differ by an ID
        value. */
    public int getID() {
	return id;
    }


    /** Set the delay between successive polling cycles. */
    public void setPollingDelay(int d) {
	delayDuration = d;
    }


    /** Execute the polling delay. */
    protected void delay() {
	if (delayDuration > 0)
	    HiresClock.delay(delayDuration);
    }


    /** Get the number of axes contained in the device. */
    public abstract int getNumberOfAxes();


    /** Get the number of buttons contained in the device. */
    public abstract int getNumberOfButtons();


    /** Get the number of available directional features.  */
    public abstract int getNumberOfDirectionals();
}
