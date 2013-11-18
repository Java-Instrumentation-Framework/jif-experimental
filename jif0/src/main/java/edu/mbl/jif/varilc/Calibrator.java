package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLC_RT;
import com.holub.asynch.Condition;

import edu.mbl.jif.camera.Camera;
import edu.mbl.jif.utils.JifUtils;

import edu.mbl.jif.utils.prefs.Prefs;
import edu.mbl.jif.utils.time.NanoTimer;

import java.awt.*;

import java.text.*;


/*

 Calibration Procedure:

 Prompt "Select ROI of birefringence-free area"

 If Full Calibration,
    Intialize Exposure
        Reset LCs to default values
        Adjust exposure so that no saturation occurs at any setting.
    Calibrate LCs
    Check ExtinctionRatio
    Adjust Exposure
        Adjust exposure so that i1 ~= baseIntensity of saturation; (? i1 ?)
        (baseIntensityFraction = 0.33;)

Calibrate LCs
        Find Extinction (minimize) for LC0
        Set LC1 to nominal swing setting and measure intensity, i1.
        For LC2, LC3, LC4 - AdjustToIntensity(i1)
        (Optimization routine: Golden-section minimization)

    Check ExtinctionRatio
 */

/*
 measureZeroIntensity();
 variLC.setToDefaults();
 checkExposureforSetting1()


 */

////////////////////////////////////////////////////////////////////////////
// Notes:
// Check for changes that user may have made,
//   e.g. intensity - % to eyepiece, shutter, illum level...
//
//
public class Calibrator {
    float retA = 0f;
    float retB = 0f;
    float retA_Extinct = 0f;
    float retB_Extinct = 0f;
    int settingWithMaxIntensity = -1;
    double intensity = 0;
    float prevIntensity = 0;
    double intensityGoal = 0;
    double intensitySaturated = 255; // assuming 8-bit, but set below
    double i0 = 0;
    double i1 = 0;
    double i2 = 0;
    double i3 = 0;
    double i4 = 0;

    //   int exposureMax = 5000; // milliseconds
    int exposureMin = 40; // for Retiga EX
    int exposureSetting = 0;
    int exposureForZeroIntensity = 10000; // 10 msec.

    //
    float testROI = 0f;
    int roiSize = 50;
    NumberFormat fmt = new DecimalFormat("#.###");
    PanelCalibrate panelCalib;

    //private  boolean cancelled = false;
    public Condition cancelled = new Condition(false);

    // Remember the current settings in case of cancellation
    private float[] retarderA_was = new float[5];
    private float[] retarderB_was = new float[5];

    // Tolerances for VariLC
    double tolGross = Prefs.usr.getFloat("calib.toleranceGross", 0.007f);
    double tolFine = Prefs.usr.getFloat("calib.toleranceFine", 0.0005f);
    int maxN = Prefs.usr.getInt("calib.maxIterations", 20);

    //----------------------------------------------------------------
    //
    VariLC_RT variLC;

    public Calibrator(VariLC_RT variLC) {
        this.variLC = variLC;

        // Remember the current settings in case of cancellation
        for (int i = 0; i < 5; i++) {
            retarderA_was[i] = variLC.getElementA(i);
            retarderB_was[i] = variLC.getElementB(i);
        }
    }

    //   public  void setCalibPanel (PanelCalibrate _panelC) {
    //      panelCalib = _panelC;
    //   }
    void init() {
        cancelled.set_false();
        intensityGoal = 0;
        intensitySaturated = (Camera.depth == 12) ? 4095 : 255;
        setROI();
    }

    void cancel() {
        cancelled.set_true();
        //psj.PSjUtils.statusClear();
    }

    /////////////////////////////////////////////////////////////////////
    // Set the ROI area, if not already set.
    //
    boolean setROI() {
        if (!Camera.isRoiSelected()) { // if no ROI selected, set to default ROI
            Camera.setROIRectangle(new Rectangle((int) ((Camera.width / 2) - (roiSize / 2)),
                    (int) ((Camera.height / 2) - (roiSize / 2)), roiSize, roiSize));
        }
        return true;
    }

    //-----------------------------------------------------------
    // updateExposureStatus
    void updateExposureStatus(int intensity) {
        //panelCalib.updateExposureValue(intensity);
    }

    ///////////////////////////////////////////////////////////////////
    // Zero Intensity Value
    public float setZeroIntensity() {
        // determine the Zero Intensity value with no illumination
        // Prompt user to close shutter or turn off illuminator
        // Take average of several full frames
        init();
        // +++ If there is a shutter, close it.
        exposureForZeroIntensity = Prefs.usr.getInt("camera.exposure", exposureForZeroIntensity);
        double zI = intensityForExposure(exposureForZeroIntensity);
        return (float) zI;
    }

    //----------------------------------------------------------------
    // runExposureAdjustment
    //
    public boolean runExposureAdjustment(boolean reset)
        throws Exception {
        init();
        if (reset) {
            Camera.setExposureOnly(100);
            variLC.setRetardersToDefaults();
        }
        updateExposureStatus(0);
        long exposureWas = Camera.exposure;
        int exp = adjustExposure();
        if (exp == -1) {
            Camera.setExposureOnly(exposureWas);
            variLC.selectElementWait(1);
            //PSj.cameraPanel.synchValues();
            throw new Exception();
        }
        Camera.setExposureOnly((long) exposureSetting);
        variLC.selectElementWait(1);
        //PSj.cameraPanel.synchValues();
        return true;
    }

    //----------------------------------------------------------------
    // Adjust the Camera Exposure
    //
    int adjustExposure() {
        //psj.PSjUtils.statusProgress("Adjusting exposure...");
        int exp = 0;
        int maxIntensity = 0;
        double FRACTION_OF_MAX = Prefs.usr.getDouble("fractionOfMaxIntensity", 0.7);
        double BASE = 10.0;
        int DECADES = 10;
        int mid = 50; // mid-range, so exposure goes from 50 usecs. to 5 sec.
        double[] I = new double[DECADES];
        for (int i = 0; i < DECADES; i++) {
            I[i] = 0;
        }

        //
        double e = 0;
        int k = 0;
        boolean notEnough = true;
        cancelled.set_false();
        // increase the exposure from min. while the maximally bright
        // setting is less than FRACTION_OF_MAX of saturation intensity
        while (notEnough && !cancelled.is_true()) {
            exp = (int) (mid * Math.pow(BASE, e));
            //I[k] = findMaxIntensityAllSettings(exp);
            I[k] = intensityForSetting(1, exp);
            notEnough = (I[k] < (intensitySaturated * FRACTION_OF_MAX));
            updateExposureStatus((int) I[k]);
            // ROI must be defined, else use findMaxPixelAllSettings()
            System.out.println("e/k/exp/I/MaxSet: " + e + " / " + k + " / " + exp + " / " + I[k] +
                " / " + settingWithMaxIntensity);
            k++;
            e = e + 0.5;
            if (k == DECADES) {
                System.out.println("(k > DECADES), " + e + " > " + DECADES);
                return -1;
            }
        }

        // interpolation slope - change in intensity per msec. change in exposure
        // ++?? double zI = Prefs.usr.getDouble("calc_ZeroIntensity", 6);
        if (k < 2) {
            System.out.println("(k < 2), k, e: " + k + ", " + e);
            return -1;
        }
        double dI = I[k - 1] - I[k - 2];
        double dT = (mid * Math.pow(BASE, e - 0.5)) - (mid * Math.pow(BASE, e - 1));
        int targetExp = (int) Math.round((mid * Math.pow(BASE, e - 1)) +
                ((((intensitySaturated * FRACTION_OF_MAX) - I[k - 2]) * dT) / dI));
        double targetI = findMaxIntensityAllSettings(targetExp);
        System.out.println("targetI: " + targetI + "  Taget Exposure: " + targetExp);
        System.out.println("settingWithMaxIntensity: " + settingWithMaxIntensity);
        variLC.selectElementWait(settingWithMaxIntensity);
        //    try {
        //      variLC.isSettled.wait_for_true();
        //    } catch (InterruptedException ex) {}
        //    variLC.resetSettleTimer();
        exp = targetExp;
        while (intensityForExposure(exp) > (intensitySaturated * FRACTION_OF_MAX)) {
            exp = (int) (0.9f * (float) exp);
        }
        //      DialogBoxes.boxError("Error Adjusting Exposure",
        //          "Could not adjust to acceptable level. (" + String.valueOf(targetI)
        //          + ")");
        exposureSetting = exp;
        Prefs.usr.putInt("calib_ExposureSet", exposureSetting);
        updateExposureStatus((int) intensityForExposure(exposureSetting));
        //psj.PSjUtils.statusClear();
        return exp;
    }

    // findMaxIntensity
    // measure intensity for VariLC settings 1,2,3,4 to determine max.
    double findMaxIntensityAllSettings(int exposure) {
        double iMax = 0;
        Prefs.usr.putInt("camera.exposure", exposure);
        settingWithMaxIntensity = -1;
        i0 = intensityForSetting(0, exposure);
        if (i0 > iMax) {
            iMax = i0;
            settingWithMaxIntensity = 0;
        }
        i1 = intensityForSetting(1, exposure);
        if (i1 > iMax) {
            iMax = i1;
            settingWithMaxIntensity = 1;
        }
        i2 = intensityForSetting(2, exposure);
        if (i2 > iMax) {
            iMax = i2;
            settingWithMaxIntensity = 2;
        }
        i3 = intensityForSetting(3, exposure);
        if (i3 > iMax) {
            iMax = i3;
            settingWithMaxIntensity = 3;
        }
        i4 = intensityForSetting(4, exposure);
        if (i4 > iMax) {
            iMax = i4;
            settingWithMaxIntensity = 4;
        }
        System.out.println("I's/Max " + i0 + ", " + i1 + ", " + i2 + ", " + i3 + ", " + i4 +
            ".   " + iMax);
        updateExposureStatus((int) iMax);
        return iMax;
    }

    ////////////////////////////////////////////////////////////////////
    // intensityForSetting - Measure intensity with selected LC setting
    // Note: for absolute intensity, set intensityGoal to 0.
    double intensityForSetting(int setting, int exposure) {
        variLC.selectElementWait(setting);
        return intensityForExposure(exposure);
    }

    //////////////////////////////////////////////////
    // intensityForExposure, with current setting
    double intensityForExposure(double _exp) {
        Camera.setExposureOnly((long) (_exp));
        //TimerHR.waitMillisecs(_exp);
        float intens = averageInCurrentROI();

        //System.out.println("    exp: " + _exp + "   intensity: " + intens);
        return (double) Math.abs(intensityGoal - intens);
    }

    //=====================================================================
    //////////////////////////////////////////////////////////////////////
    // VariLC Calibration
    //////////////////////////////////////////////////////////////////////
    //
    // calibrate the VariLC retarder settings
    //   minimize the intensity at extinction, setting 0
    //   measure the intensity at setting 1
    //   match this intensity at settings 2, 3, & 4.
    //
    ///////////////////////////////////////////////////////////////////
    // runCalibrationOfVariLC
    //
    public boolean runCalibrationOfVariLC() {
        init();
        // +++ check if exposure is ok
        // remember current settings in case of cancellation
        if (!calibrateVariLC()) {
            cancelCalibrateVariLC();
            return false;
        } else {
            // +++ Save a Calibration PolStack
            panelCalib.updateValues(i0, i1, i2, i3, i4, 5);
            return true;
        }
    }

    // Cancel
    private void cancelCalibrateVariLC() {
        System.out.println("Restoring previous VariLC settings...");
        //psj.PSjUtils.event("VariLC  Calibration Cancelled");
        variLC.setElement(0, retarderA_was[0], retarderB_was[0]);
        variLC.setElement(1, retarderA_was[1], retarderB_was[1]);
        variLC.setElement(2, retarderA_was[2], retarderB_was[2]);
        variLC.setElement(3, retarderA_was[3], retarderB_was[3]);
        variLC.setElement(4, retarderA_was[4], retarderB_was[4]);
        variLC.selectElementWait(1);
        //PSj.vLCPanel.updateLCSpinnerValues();
    }

    //----------------------------------------------------------------
    //
    public boolean calibrateVariLC() {
        double lower = 0;
        double upper = 0;
        setExtinction();
        if (cancelled.is_true()) {
            return false;
        }
        // --- 1 ---
        // set Element 1 to default value and measure intensity
        // setElement(1, 0.25f + variLC.LC_Swing, 0.50f);
        variLC.setElement(1, retA_Extinct + variLC.LC_Swing, retB_Extinct);
        intensityGoal = averageInCurrentROI();
        i1 = (float) intensityGoal;
        panelCalib.updateValues(i0, i1, i2, i3, i4, 2);
        if (cancelled.is_true()) {
            return false;
        }
        // Match the intensityGoal for Elements 2, 3 & 4...
        //    setElement(n, A_setting, B_setting);
        // --- 2 ---  setElement(2, 0.25f, 0.50f + variLC.LC_Swing);
        retA = retA_Extinct;
        retB = retB_Extinct + variLC.LC_Swing;
        variLC.setElement(2, retA, retB);
        intensity = minIntensity("B", retB - (variLC.LC_Swing / 2), retB + (variLC.LC_Swing / 2),
                tolFine, maxN);
        variLC.setElement(2, retA, retB);
        i2 = (float) (intensityGoal - intensity);
        panelCalib.updateValues(i0, i1, i2, i3, i4, 3);
        if (cancelled.is_true()) {
            return false;
        }
        // --- 3 ---  setElement(3, 0.25f, 0.50f - variLC.LC_Swing);
        retA = retA_Extinct;
        retB = retB_Extinct - variLC.LC_Swing;
        variLC.setElement(3, retA, retB);
        intensity = minIntensity("B", retB - (variLC.LC_Swing / 2), retB + (variLC.LC_Swing / 2),
                tolFine, maxN);
        variLC.setElement(3, retA, retB);
        //    System.out.println(
        //      "  Element 3: (" + fmt.format(retA) + ", " + fmt.format(retB) + ")  = "
        //      + fmt.format(intensity));
        i3 = (float) (intensityGoal - intensity);
        panelCalib.updateValues(i0, i1, i2, i3, i4, 4);
        if (cancelled.is_true()) {
            return false;
        }
        // --- 4 ---   setElement(4, 0.25f - variLC.LC_Swing, 0.50f);
        retA = retA_Extinct - variLC.LC_Swing;
        retB = retB_Extinct;
        // Don't allow setting below 0.022 for the min. VariLC setting
        if (retA < 0.022f) {
            retA = 0.022f;
        }
        variLC.setElement(4, retA, retB);
        float lowerBound = ((retA - (variLC.LC_Swing / 2)) < 0.022f) ? 0.022f
                                                                     : (retA -
            (variLC.LC_Swing / 2));
        intensity = minIntensity("A", lowerBound, retA + (variLC.LC_Swing / 2), tolFine, maxN);
        variLC.setElement(4, retA, retB);
        //    System.out.println(
        //      "  Element 4: (" + fmt.format(retA) + ", " + fmt.format(retB) + ")  = "
        //      + fmt.format(intensity));
        i4 = (float) (intensityGoal - intensity);
        panelCalib.updateValues(i0, i1, i2, i3, i4, 5);
        if (cancelled.is_true()) {
            return false;
        }
        // when done... synchronize values with spinners in VariLC Settings tab
        variLC.selectElementWait(1);
        //PSj.vLCPanel.updateLCSpinnerValues();
        //PSj.vLCPanel.repaint();
        // leave a trace...
        variLC.isCalibrated = true;
        //PSj.isCalibratedVariLC = true;
        Prefs.usr.putBoolean("calib_VariLC", true);
        //Prefs.usr.put("calib_LastTime", psj.PSjUtils.timeStamp());
        //psj.PSjUtils.event("VariLC Calibration Completed.");
        return true;
    }

    //----------------------------------------------------------------
    //  Minimize Extinction setting
    void setExtinction() {
        //psj.PSjUtils.statusProgress("Calibrating VariLC, Extinction setting...", 10);
        intensityGoal = 0;
        float bound = variLC.LC_Swing;
        //    retA = variLC.extinctA;
        //    retB = variLC.extinctB;
        retA = variLC.getElementA(0);
        retB = variLC.getElementB(0);
        i0 = (float) minIntensity("A", retA - bound, retA + bound, tolGross, maxN);
        //    System.out.println(
        //      "retA, retB, Intensity: " + retA + ", " + retB + ", " + i0);
        panelCalib.updateValues(i0, i1, i2, i3, i4, 1);
        i0 = (float) minIntensity("B", retB - bound, retB + bound, tolGross, maxN);
        // Again, with lower tolerance...
        panelCalib.updateValues(i0, i1, i2, i3, i4, 1);
        i0 = (float) minIntensity("A", retA - (bound / 2), retA + (bound / 2), tolFine, maxN);
        panelCalib.updateValues(i0, i1, i2, i3, i4, 1);
        i0 = (float) minIntensity("B", retB - (variLC.LC_Swing / 2), retB + (variLC.LC_Swing / 2),
                tolFine, maxN);
        i0 = averageInCurrentROI();
        panelCalib.updateValues(i0, i1, i2, i3, i4, 1);
        //psj.PSjUtils.statusProgress("Calibrating VariLC...", 20);
        retA_Extinct = retA;
        retB_Extinct = retB;
        variLC.setElement(0, retA_Extinct, retB_Extinct);
        //    System.out.println(
        //      "  Element 0: (" + fmt.format(retA_Extinct) + ", "
        //      + fmt.format(retB_Extinct) + ")  = " + fmt.format(i0));
        panelCalib.label_E1.setBackground(Color.green);
    }

    /////////////////////////////////////////////////////////////////////////
    // Golden Section Search for Minimization of f(x)
    // minimizeIntensity("path", lower, upper, tolerance, maxIterations);
    //
    public double minIntensity(String path, double _a, double _b, double eps, int N) {
        double a = _a;
        if (a < 0.022) {
            a = 0.023;
        }
        double b = _b;
        double c = (Math.sqrt(5) - 1) / 2;
        System.out.println("    minIntensity (" + path + ")  " + fmt.format(a) + ", " +
            fmt.format(b) + ";  " + eps + ", " + N);
        double x1 = (c * a) + ((1 - c) * b);
        double fx1 = intensityWith(path, x1);
        double x2 = ((1 - c) * a) + (c * b);
        double fx2 = intensityWith(path, x2);
        for (int i = 1; i <= (N - 2); i++) {
            //      System.out.println(
            //        "      " + fmt.format(a) + "   " + fmt.format(b) + "   "
            //        + fmt.format(x1) + "   " + fmt.format(x2) + "   " + fmt.format(fx1)
            //        + "   " + fmt.format(fx2) + "   (" + fmt.format((b - a)) + ")   [["
            //        + retA + " : " + retB + "]]");
            if (fx1 < fx2) {
                b = x2;
                x2 = x1;
                fx2 = fx1;
                x1 = (c * a) + ((1 - c) * b);
                fx1 = intensityWith(path, x1);
            } else {
                a = x1;
                x1 = x2;
                fx1 = fx2;
                x2 = ((1 - c) * a) + (c * b);
                fx2 = intensityWith(path, x2);
            }
            if ( /*(Math.abs(fx1 - fx2) < 1) || */
                (Math.abs(b - a) < eps)) {
                //        System.out.println(
                //          "      " + fmt.format(a) + "   " + fmt.format(b) + "   "
                //          + fmt.format(x1) + "   " + fmt.format(x2) + "   " + fmt.format(fx1)
                //          + "   " + fmt.format(fx2) + "   (" + fmt.format((b - a)) + ")   [["
                //          + retA + " : " + retB + "]]");
                return x1;
            }
        }
        System.out.println("Unable to minimize after " + N + "steps");
        return 0;
    }

    ///////////////////////////////////////////////////////////////
    // Methods for Measuring
    ///////////////////////////////////////////////////////////////

    // findMaxPixelAllSettings
    double findMaxPixelAllSettings(int exposure) {
        double iMax = 0;
        i1 = maxPixelForSetting(1, exposure);
        if (i1 > iMax) {
            iMax = i1;
        }
        i2 = maxPixelForSetting(2, exposure);
        if (i2 > iMax) {
            iMax = i2;
        }
        i3 = maxPixelForSetting(3, exposure);
        if (i3 > iMax) {
            iMax = i3;
        }
        i4 = maxPixelForSetting(4, exposure);
        if (i4 > iMax) {
            iMax = i4;
        }
        return iMax;
    }

    // maxPixelForSetting
    double maxPixelForSetting(int setting, int exposure) {
        NanoTimer nt = new NanoTimer();
        nt.reset();
        Camera.setExposureOnly((long) (exposure));
        if (setting != variLC.getSelectedElement()) {
            variLC.selectElementWait(setting);
        }
        JifUtils.waitFor((int) nt.elapsedMillis() - exposure);
        float intens = maxPixelInROI();
        //System.out.println("    exp: " + exp + "   intensity: " + intens);
        return (double) Math.abs(intensityGoal - intens);
    }

    // intensityWith
    double intensityWith(String path, double x) {
        if (path == "A") {
            retA = (float) x;
        }
        if (path == "B") {
            retB = (float) x;
        }
        variLC.setRetardanceWait(retA, retB);
        float a = averageInCurrentROI();

        //      if (a >= intensityAtSaturation) {
        //         DialogBoxes.boxError("Error While Calibrating",
        //            "Camera image is saturated.  Reduce Exposure time and try again.");
        //         return -1.0f;
        //      }
        double diff = (double) Math.abs(a - intensityGoal);
        plotPoint(a);
        //System.out.println("   >>>> A: " + retA + "  B: " + retB );
        return diff;
    }

    private void plotPoint(double diff) {
        panelCalib.plotPoint(diff);
    }

    // Match Intensity
    void matchIntensity(float intensity, boolean pathA) {
        if (pathA) {
            variLC.setRetardanceWait(retA, retB);
        } else {
            variLC.setRetardanceWait(retA, retB);
        }
    }

    // get intensity in ROI - average n measurements with delay between
    // frameAveraging...
    public float averageInCurrentROI() {
        Camera.grabSampleFrame();
        return Camera.getSampleAverageROI();
    }

    public float maxPixelInROI() {
        Camera.grabSampleFrame();
        return Camera.getSampleMaxROI();
    }

    //  private  void warnCannotAdjustExposure() {
    //    DialogBoxes.boxError(panelCalib, "Cannot Adjust Exposure",
    //      "Check camera, illumination and optical path.");
    //  }
}
/*
    public  boolean BinarySearch (int[] tbl, int n) {
       int low = 0;
       int high = tbl.length - 1;
       while (low <= high) {
          int mid = (low + high) / 2;
          if (n == tbl[mid]) {
             return true;
          } else if (n < tbl[mid]) {
             high = mid - 1;
          } else {
             low = mid + 1;
          }
       }
       return false;
    }
 */

/////////////////////////////////////////////////////////////////////
// adjustExposureForIntensity - finds exposure time so that measured
// intensity (in ROI) is very close to intensityGoal
//   int adjustExposureToIntensity(double _intensityGoal) {
//    double intensityGoal = _intensityGoal;
//    // use microseconds for exposure time
//    System.out.println("Exposure Intensity Goal: " + intensityGoal);
//    double eps = 3000; // tolerance
//    int N = 20; // max. iterations
//
//    double c = (Math.sqrt(5) - 1) / 2; // 'Golden Section' constant
//    double a = (double) (exposureMin / c * 1000);
//    double b = (double) (exposureSetting / c);
//
//    //  System.out.println("a= " + a + ", b= " + b);
//    double x1 = (c * a) + ( (1 - c) * b);
//    double fx1 = intensityForExposure(x1);
//    double x2 = ( (1 - c) * a) + (c * b);
//    double fx2 = intensityForExposure(x2);
//    for (int i = 1; i <= (N - 2); i++) {
//      //      System.out.println(
//      //        "      " + fmt.format(a) + "   " + fmt.format(b) + "   "
//      //        + fmt.format(x1) + "   " + fmt.format(x2) + "   " + fmt.format(fx1)
//      //        + "   " + fmt.format(fx2) + "   (" + fmt.format((b - a)) + ")");
//      if (fx1 < fx2) {
//        b = x2;
//        x2 = x1;
//        fx2 = fx1;
//        x1 = (c * a) + ( (1 - c) * b);
//        fx1 = intensityForExposure(x1);
//      } else {
//        a = x1;
//        x1 = x2;
//        fx1 = fx2;
//        x2 = ( (1 - c) * a) + (c * b);
//        fx2 = intensityForExposure(x2);
//      }
//      if (Math.abs(b - a) < eps) {
//        if (fx1 > fx2) {
//          return (int) x1;
//        } else {
//          return (int) x2;
//        }
//      }
//    }
//    System.out.println("Unable to minimize after " + N + "steps");
//    return 0;
//  }
