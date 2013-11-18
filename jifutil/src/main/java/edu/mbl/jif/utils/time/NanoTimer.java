package edu.mbl.jif.utils.time;

import java.text.DecimalFormat;


/*
 * SimpleTimerNanos.java Copyright (c) 2004,2005 Gregory Kotsaftis gregkotsaftis@yahoo.com
 * http://zeus-jscl.sourceforge.net
 */
/**
 * A very simple timer for timing java method calls and other processes (JDK 5, nanos implementation).
 *
 * @author Gregory Kotsaftis
 * @since 1.06
 */
public final class NanoTimer {

	static final DecimalFormat format = new DecimalFormat();

	static {
		format.setMinimumFractionDigits(3);
		format.setMaximumFractionDigits(3);
	}

	/**
	 * Used to get the call time.
	 */
	private long m_nanos = 0L;

	/**
	 * Constructor.
	 */
	public NanoTimer() {
	}

	/**
	 * Resets the timer.
	 */
	public synchronized void reset() {
		m_nanos = 0L;
	}

	/**
	 * Starts timing a job.
	 */
	public synchronized void start() {
		// Get current time in milliseconds
		m_nanos = System.nanoTime();
	}

	/**
	 * Gets elapsed time in nanoseconds. <p>
	 *
	 * @return The elapsed time in nanoseconds.
	 */
	public long elapsedNanos() {
		// Get elapsed time in nanoseconds
		long elapsed = System.nanoTime() - m_nanos;
		return (elapsed);
	}

	/**
	 * Gets elapsed time in milliseconds. <p>
	 *
	 * @return The elapsed time in milliseconds.
	 */
	public double elapsedMillis() {
		double elapsed = (double) elapsedNanos() / 1000000.0;
		return (elapsed);
	}

	public String elapsedMillisStr() {
		double elapsed = (double) elapsedNanos() / 1000000.0;
		return format.format(elapsed);
	}

	/**
	 * Gets elapsed time in seconds. <p>
	 *
	 * @return The elapsed time in seconds.
	 */
	public double elapsedSeconds() {
		// Get elapsed time in seconds
		double elapsed = elapsedMillis() / 1000.0;
		return (elapsed);
	}

	public String elapsedSecondsStr() {
		// Get elapsed time in seconds
		double elapsed = elapsedMillis() / 1000.0;
		return format.format(elapsed);
	}

	public static void main(String[] args) {
		NanoTimer nt = new NanoTimer();
		nt.reset();
		int n = 50;
		double[] t = new double[n];
		for (int i = 0; i < n; i++) {
			nt.start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
			}
			System.out.println("period: " + nt.elapsedMillis());
			//t[i] = nt.elapsedMillis();
		}
		//        for (int i = 0; i < n; i++) {
		//            System.out.println("peroid: " + t[i]);
		//        }        
	}

	void usage() {
		NanoTimer nt = new NanoTimer();
		nt.reset();
		//...
		nt.start();
		//...
		System.out.println("period: " + nt.elapsedMillis());
	}

}
