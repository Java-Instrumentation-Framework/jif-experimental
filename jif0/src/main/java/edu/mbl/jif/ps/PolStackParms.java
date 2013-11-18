/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.ps;

import edu.mbl.jif.camera.Camera;
import edu.mbl.jif.utils.prefs.Prefs;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PolStackParms
		implements Serializable, Cloneable {
	// ID

	public String polStackID = "0000";
	// Project / Session (original)
	public String project;
	public String session;
	// Acquisition
	// Camera settings
	public long width;
	public long height;
	public long binning;
	public long depth;
	public long exposure;
	public long gain;
	public long offset;
	public Rectangle roi;
	// Multi-frame integration or averaging
	public int frames = 1;
	public int averaging = 1;
	// Series acq.
	public int NthInSeries = 0;
	public int inSeriesOf = 0;
	// Other
	public long timeAcquired = 0;
	public long timeToAcquire = 0;
	// Averaging (old, 4:1 type, no longer used)
	public boolean averaged = false;
	// VariLC settings
	public float[] retarderA = {
		0,
		0,
		0,
		0,
		0
	};
	public float[] retarderB = {
		0,
		0,
		0,
		0,
		0
	};
	// PolStack parameters
	public float retardanceMax = 0; // Maximum,regardless of retCeiling.
	public float swingFraction = 0.03f; // =swing/wavelength ~ 0.03
	public float wavelength = 546f; // wavelength in nm ~ 546 nm
	public float zeroIntensity = 0.0f;
	public int retCeiling = 10; // maximum image retardance in nm
// @todo ***
	public int algorithm = 4;
	public int azimuthRef = 0; // azimuth reference in (whole) degrees
	public float dynamicRange = 1.0f; // dynamic range of ...
	// Background correction - filename of stack used for correction in magImage
	public String backGroundStack = null;
	public boolean doBkgdCorrection = false;
	// Ratioing Correction
	public Rectangle roiRatioing = new Rectangle(0, 0, 0, 0);
	public double[] ratioingAvg = null; // for each slice
	public boolean doRatioing = false;

	//========================================================================
	//========================================================================
	public PolStackParms(String id) {
		polStackID = id;
	}

	Map makeMap() {
		Map parmsMap = new HashMap();
		
		return parmsMap;
	}

	//----------------------------------------------------------------
	// setToAcquisitionSettings  - Set the parameters from acquisition Settings.
	//
	public void setToAcquisitionSettingsPS() {
		if (polStackID == "0000") {
			System.err.println("Error in PolStackParms - no polStackID");
		}
		// @todo ***
//      project = DataAccess.dataPath_Project;
//      session = DataAccess.dataPath_Session;
		timeAcquired = System.currentTimeMillis();
		setAcq_CameraSettings();
		setAcq_ImageSettings();
		setAcq_VariLCSettings();
		setAcq_CalculationSettings();
		setAcq_CorrectionSettings();
	}

	//----------------------------------------------------------------
	//
	void setAcq_CameraSettings() {

		// Camera settings
		width = Camera.width;
		height = Camera.height;
		depth = Camera.depth;
		binning = Camera.binning;
		exposure = Camera.exposure;
		gain = Camera.gain;
		offset = Camera.offset;
		roi =
				new Rectangle((int) Camera.roiX, (int) Camera.roiY, (int) Camera.roiW,
				(int) Camera.roiH);
	}

	//----------------------------------------------------------------
	//
	void setAcq_VariLCSettings() {
		// VariLC settings
		// @todo ***
//      wavelength = psj.PSj.variLC.wavelength;
//      swingFraction = PSj.variLC.LC_Swing;
//      retarderA = PSj.variLC.retarderA;
//      retarderB = PSj.variLC.retarderB;
	}

	//----------------------------------------------------------------
	//
	void setAcq_CalculationSettings() {
		// Calculation settings
		// @todo *** algorithm = getAcqAlgorithm();
		azimuthRef = Prefs.usr.getInt("acq_azimuthRef", 0);
		retCeiling = Prefs.usr.getInt("acq_retCeiling", 5);
		zeroIntensity = (float) Prefs.usr.getDouble("acq_zeroIntensity", 0);
		dynamicRange = (float) Prefs.usr.getDouble("acq_dynamicRange", 3.0);
	}

	//----------------------------------------------------------------
	//
	public void setAcq_CorrectionSettings() {
		// Background Stack
		int size = (int) width * (int) height;
		// int size = (int)width * (int)height;
		// @todo ***
            /*
		 if (BackgroundPolStack.isBkgdStackAvailable(size)) {
		 backGroundStack =
		 FileUtil.getJustFilenameNoExt(
		 BackgroundPolStack.getBkgdStack_FilenameOfSize(size));
		 /// @todo change psParms for this stack to backGroundStack  
		 doBkgdCorrection = Prefs.usr.getBoolean("acq_doBkgdCorr", false);

		 // RatioingROI
		 if (Ratioing.acqRoi.width > 0) {
		 roiRatioing = Ratioing.acqRoi;
		 }
		 if (Prefs.usr.getBoolean("acq_doRatioing", false)) {
		 doRatioing = true;
		 }
		 } else {
		 backGroundStack = null;
		 doBkgdCorrection = false;
		 doRatioing = false;
		 }
		 */
	}

	//----------------------------------------------------------------
	//
	void setAcq_ImageSettings() {
		// Multi-frame
		if (polStackID != null && polStackID.startsWith("BG")) {
			frames = Prefs.usr.getInt("acqPS.numberOfExposuresBkgd", 1);
		} else {
			frames = Prefs.usr.getInt("acqPS.numberOfExposures", 1);
		}
		if (frames > 1) {
			averaged = Prefs.usr.getBoolean("acqPS.divide", false);
		} else {
			averaged = false;
		}
	}

	//-----------------------------------------------------------
	//
	int getAcqAlgorithm() {
// @todo ***
       /*
		 if (PSj.acqCtrlr != null) {
		 if (PSj.acqCtrlr.getStackType() == PSj.TWO_FRAME) {
		 return 2;
		 }
		 if (PSj.acqCtrlr.getStackType() == PSj.THREE_FRAME) {
		 return 3;
		 }
		 if (PSj.acqCtrlr.getStackType() == PSj.FOUR_FRAME) {
		 return 4;
		 }
		 if (PSj.acqCtrlr.getStackType() == PSj.FIVE_FRAME) {
		 return 5;
		 }
		 }
		 */
		return 0;
	}

	//----------------------------------------------------------------
	//
	//
	public void setToAcquisitionSettingsImg() {
		// Camera settings

		/*
		 width = Camera.width;
		 height = Camera.height;
		 depth = Camera.depth;
		 binning = Camera.binning;
		 exposure = Camera.exposure;
		 gain = Camera.gain;
		 offset = Camera.offset;
		 roi =
		 new Rectangle( (int) Camera.roiX, (int) Camera.roiY,
		 (int) Camera.roiW, (int) Camera.roiH);
		 // Path
		 project = DataAccess.dataPath_Project;
		 session = DataAccess.dataPath_Session;
		 timeAcquired = System.currentTimeMillis();
		 // Multi-frame
		 frames = Prefs.usr.getInt("framesToAvgImg", 1);
		 if (frames > 1)
		 averaged = Prefs.usr.getBoolean("divideImg", true);
		 else
		 averaged = false;
		 */
	}

	//////////////////////////////////////////////////////////////////////////
	// Set the parameters from the User Settings...
	// User settings are saved to and retrieved from the Properties file.
	//
//  public void setToUserSettings() {
//    // Sets the psParms to the values "calc_*" in PSjProps
//    //
//    // Camera settings
//    width = 0;
//    height = 0;
//    depth = 8;
//    binning = 0;
//    exposure = 0;
//    gain = 0;
//    offset = 0;
//    roi = new Rectangle(0, 0, 0, 0);
//    // VariLC settings
//    wavelength = (float) Prefs.usr.getDouble("calc_wavelength", 546.0);
//    swingFraction = (float) Prefs.usr.getDouble("calc_swingFraction", 0.03);
//    retCeiling =
//      Prefs.usr.getInt("calc_retCeiling",
//        (int) ((swingFraction * wavelength) / 2));
//    azimuthRef = Prefs.usr.getInt("calc_azimuthRef", 90);
//    zeroIntensity = (float) Prefs.usr.getDouble("calc_zeroIntensity", 0);
//    algorithm = Prefs.usr.getInt("calc_algorithm", PSj.CALC_FIVE);
//    backGroundStack = null;
//  }
	//-----------------------------------------------------------
	// Save User Settings
//  public void saveUserSettings() {
//    Prefs.usr.putFloat("calc_wavelength", wavelength);
//    Prefs.usr.putFloat("calc_swingFraction", swingFraction);
//    Prefs.usr.putInt("calc_azimuthRef", azimuthRef);
//    Prefs.usr.putInt("calc_retCeiling", retCeiling);
//    Prefs.usr.putInt("calc_algorithm", algorithm);
//    Prefs.usr.putFloat("calc_zeroIntensity", zeroIntensity);
//    Prefs.usr.putFloat("calc_dynamicRange", dynamicRange);
//  }
	//----------------------------------------------------------------
	//
	public void setToRetrievedSettings() {
	}

	//
	////////////////////////////////////////////////////////////////////////
	// Get listing of the PolStackParameters
	//
	public String getList() {
		String parms =
				"polStackID: " + polStackID + "\n" + project + " / " + session + "\n\n"
				+ "  { \n" + "  Camera:       " + "\n" + "    width         "
				+ String.valueOf(width) + "\n" + "    height        "
				+ String.valueOf(height) + "\n" + "    depth         "
				+ String.valueOf(depth) + "\n" + "    binning       "
				+ String.valueOf(binning) + "\n" + "    exposure [ms] "
				+ String.valueOf((float) exposure / 1000) + "\n"
				+ "    gain          "
				+ String.valueOf(gain) + "\n" + "    offset        "
				+ String.valueOf(offset) + "\n" + "    roi           "
				// @todo ***  + psj.PSjUtils.rectangleToString(roi) + "\n"
				+ getMultiFrameInfo() + "\n\n"
				+ "  VariLC:            " + "\n" + "    wavelength       "
				+ String.valueOf(wavelength) + "\n" + "    swingFraction    "
				+ String.valueOf(swingFraction) + "\n" + "    retarderA[0-4]   "
				+ String.valueOf(retarderA[0]) + ", " + String.valueOf(retarderA[1])
				+ ", " + String.valueOf(retarderA[2]) + ", "
				+ String.valueOf(retarderA[3]) + ", " + String.valueOf(retarderA[4])
				+ "\n" + "    retarderB[0-4]   " + String.valueOf(retarderB[0]) + ", "
				+ String.valueOf(retarderB[1]) + ", " + String.valueOf(retarderB[2])
				+ ", " + String.valueOf(retarderB[3]) + ", "
				+ String.valueOf(retarderB[4]) + "\n\n" + "  Calculation:       "
				+ "\n"
				// @todo ***    + "    algorithm        " + String.valueOf(algorithm) + "\n"
				+ "    retCeiling       " + String.valueOf(retCeiling) + "\n"
				+ "    azimuthRef       " + String.valueOf(azimuthRef) + "\n"
				+ "  Correction:        " + "\n" + "    BackgroundStack  "
				+ ((backGroundStack == null) ? "none" : String.valueOf(backGroundStack))
				+ "\n" + "    Zero Intensity   " + String.valueOf(zeroIntensity) + "\n"
				/**
				 * Need to not show RatioingAvgs if this is a parm for a series...
				 */
				+ "\n" + getListRatioingAvg()
				+ ((timeToAcquire > 0)
				? ("\n" + "Time to Acquire(ms): " + String.valueOf(timeToAcquire) + "\n")
				: "\n")
				+ ((NthInSeries > 0)
				? ("\n" + "             Number: " + String.valueOf(NthInSeries) + " in ")
				: "\n") + "\n"
				+ ((inSeriesOf > 0)
				? ("\n" + "          Series of: " + String.valueOf(inSeriesOf) + "\n")
				: "\n") + "  Calc'd Max. Retardance: " + String.valueOf(retardanceMax)
				+ "\n" + "\n}";
		return parms;
	}

	String getListRatioingAvg() {
		if (ratioingAvg == null) {
			return "    RatioingAvg     none\n";
		} else {
			String s =
					"      Ratioing ROI: "
					// @todo *** + psj.PSjUtils.rectangleToString(roiRatioing)
					+ "\n";
			for (int i = 0; i < ratioingAvg.length; i++) {
				s = s + "    RatioingAvg[" + String.valueOf(i) + "]: "
						+ String.valueOf(ratioingAvg[i]) + "\n";
			}
			return s;
		}
	}

	String getMultiFrameInfo() {
		String s = "";
		if (frames == 1) {
			s = "    Single frame acquisition";
		} else {
			s = "    MultiFrame acquisition: " + String.valueOf(frames)
					+ " frames, " + (averaged ? "averaged" : "integrated") + "\n";
		}
		return s;
	}

	//-----------------------------------------------------------
	//
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(System.err);
		}
		return o;
	}

}