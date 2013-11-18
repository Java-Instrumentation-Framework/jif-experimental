/*
 * VariLVMeasurer
 */
package edu.mbl.jif.varilc;

import java.awt.Rectangle;

/**
 *
 * @author GBH
 */
public interface VLCMeasurer {
	
	// Measurement
	
	// float getMean();
	
	boolean setupForMeasurements();

	long getTimeToStable();

	void measureAll();

	void measureSetting(final int n);

	void setElementAndMeasure(final int n);

	double acquireAndMeasure(Rectangle roi, boolean isDisplayLastAcquired);

	float calcExtinctionRatio();

	double checkVariance();

	void runCalibration();

}
