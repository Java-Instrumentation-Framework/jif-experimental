/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc;

import java.awt.Rectangle;

/**
 *
 * @author GBH
 */
public interface VLCController {

	void defineElement(int element);

	void defineElementAndMeasure(int element);

	boolean initializeController();

	boolean isInitialized();

	boolean setupVLC(); 
	
	void reset();

	void selectElement(int n);

	void selectElementWait(int n);

	/**
	 * Sets default values for Extinction to the current value of A0 and B0
	 */
	void setExtinctionDefaultsToCurrent();

	int setRetardance(double retA, double retB);

	int setRetardance(float retA, float retB);

	void setRetardersToNominalSwing();

	void statusCheck();

	void transmitAllElements();


	
	
}
