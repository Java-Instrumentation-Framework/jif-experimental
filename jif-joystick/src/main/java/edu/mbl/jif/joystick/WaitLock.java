
package edu.mbl.jif.joystick;

/** A lock object which may be used to put the current thread to sleep
    for a given time and wake it up again. This class tries to get the
    millisecond resolution timing combined with a low CPU load factor.

  @version 0.4.0
  @see java.util.concurrent.locks.Condition

*/

/*

  2007/02/11 handle polled devices. 

*/



public class WaitLock {


    private boolean waiting;

    private static final long sleepBound = 20 * 1000000L;

    private static final long yieldBound = 2 * 1000000L;


    /** Causes the current thread to wait until it is told to wake up
	or the specified waiting time elapses.
	@param nanoDuration the maximum time to wait, given in
	nanoseconds. */



    /** Causes the current thread to wait until it is told to wake up
	or the specified waiting time elapses.
	@param nanoDuration the maximum time to wait, given in
	nanoseconds. 
	@param rm the ResponseManager which should be asked for
	polling an input device while waiting. */
    public void waitForNanos(long nanoDuration) {
	if (nanoDuration > 0) {
	    long t = HiresClock.getTimeNanos() + nanoDuration;
	    long t0 = t - sleepBound;
	    long t1 = t - yieldBound;
	    waiting = true;

		// no input device polling is required
		while (waiting && (HiresClock.getTimeNanos() < t0)) {
		    try { Thread.sleep(1); } catch (InterruptedException iex) {}
		}
		while (waiting && (HiresClock.getTimeNanos() < t1)) {
		    Thread.yield();
		}
		while (waiting && (HiresClock.getTimeNanos() < t)) {
		}
	}
    }
    

    /** Causes the current thread to wait until it is told to wake up. */
    public void waitFor() {
	waiting = true;
	while (waiting) {
	    try { 
		Thread.sleep(1); 
	    } catch (InterruptedException iex) {}
	}
    }
    

    /** Wake up the thread waiting for this lock. */ 
    public void wakeUp() {
	// Debug.show(Debug.EVENTS, "HiresWaitLock.wakeUp()");
	waiting = false;
    }


    /** Causes the current thread to wait until it is told to wake up
	or the specified deadline of nanoseconds elapses.
	@param nanoTime the nanosecond counter value to wait until. */
    public void waitUntilNanos(long nanoTime) {
	long d = nanoTime - HiresClock.getTimeNanos();
	if (d > 0) { 
	    waitForNanos(d);
	}
    }


    /** Causes the current thread to wait until it is told to wake up
	or the specified waiting time elapses.
	@param msDuration the maximum time to wait, given in
	milliseconds. */
    public void waitFor(long msDuration) {
	waitForNanos(1000000L * msDuration);
    }


    /** Causes the current thread to wait until it is told to wake up
	or the specified deadline of milliseconds elapses.
	@param msTime the millisecond counter value to wait until. */
    public void waitUntil(long msTime) {
	waitUntilNanos(1000000L * msTime);
    }
}
