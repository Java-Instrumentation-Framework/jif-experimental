/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc;

/**
 *
 * @author GBH
 */
public interface VLCComm {

	boolean busyCheck();

	int checkErrorFlags(byte errByte);

	boolean checkForError(byte errByte);

	boolean checkIfResponsive();

	int clearElements();

	void closeComPort();

	int deActivate();

	boolean defineElement(int element);

	int enableControlPort(int on);

	int escape();

	int exercise();

	boolean getDefinedElements();

	float getElementA(int element);

	float getElementB(int element);

	float[] getElementSettings();

	int getSelectedElement();

	int getSettlingTime();

	String getVersion();

	String initialize();

	int loadSettings();

	int reActivate();

	int reset();

	void resetSettleTimer();

	int selectElement(int element);

	int selectElementNext();

	int selectElementPrev();

	int selectElementWait(int element);

	void sendChar(char[] ch);

	String sendCommand(String cmd);

	String sendCommandAndWait(String cmd);

	String sendCommandAndWait(String cmd, int time);

	int setCommandFormat(int fmt);

	void setElement(int element, float retA, float retB);

	int setIncrementRetardance(float incr);

	int setIncrementWavelength(float incr);

	int setMode(int mode);

	int setRetardance(float retA, float retB);

	int setRetardanceWait(float retA, float retB);

	void setRetardersToDefaults();

	void setSettlingTime(int _settleTime);

	int setState(float wave, float ret);

	void setType(String type);

	int setUnits(int units);

	int setWavelength(float wave);

	int showElements();

	boolean statusCheck();

	int stepWavelengthDown();

	int stepWavelengthUp();

	int trigger();

	String waitForResponse(int time);

	String waitForResponse();
	
}
