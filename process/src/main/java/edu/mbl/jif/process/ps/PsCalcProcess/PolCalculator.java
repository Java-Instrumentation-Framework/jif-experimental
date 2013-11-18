package edu.mbl.jif.process.ps.PsCalcProcess;

import java.util.Map;
import javax.swing.JFrame;

public class PolCalculator {
    //
    // Constants

    // TODO - Ratioing...
    static double onePi = Math.PI;
    static double twoPi = Math.PI * 2;
    // radian equivalents of degrees
    static double DEG_270 = (Math.PI * 3) / 2;
    static double DEG_180 = Math.PI;
    static double DEG_135 = (Math.PI * 3) / 4;
    static double DEG_90 = Math.PI / 2;
    static double DEG_45 = Math.PI / 4;
    // Parameters
    float wavelength = 0;
    float swingFraction = 0;
    float retCeiling = 0;
    float azimuthRef = 0;
    float zeroIntensity = 0;
    int depth = 8;
    int algorithm = 5;
    // do or don't do...
    boolean doAzim = true;
    boolean doMag = true;
    // Background stack - set to the appropriate one for the image size
    PolStack bkgdStack = null;
    public boolean doBkgdCorrect = false;
    public boolean doRatioing = false;
    double[] bkgdRatio = null;
    // Calculated values
    float swingInNanoMeters = 0;
    float swingAngle = 0;
    float tanSwingAngleDiv2 = 0;
    float retCeilingAngle = 0;
    double largestValue = 0;
    float pixValueMax = 255.f;
    float factor = 1;
    //
    boolean invertedMicroscope = true;
    // *** Prefs.usr.getBoolean("microscopeInverted", false);
    // LookupTable for fast calculation
    public byte[][] polCalcLookup = new byte[256][256];
    //----------------------------------------------------------------
    //
    boolean isListingCalc = false;

    public void setIsListingCalc(boolean t) {
        isListingCalc = t;
    }

    //-----------------------------------------------------------
    // setupParametersAndCorrections
    // setupParametersAndCorrections using a Specific Background
    public void setupParametersAndCorrections(PolStack pStack,
            PolStack backgroundStack) {
        doBkgdCorrect = false;
        doRatioing = false;
        bkgdStack = backgroundStack;
        if (pStack.psParms.doBkgdCorrection) {
            doBkgdCorrect = true;
        } else { // No appropriately sized BkgdStack found
            doBkgdCorrect = false;
            doRatioing = false;
        }
        setup(pStack);
    }

    public void setupParametersAndCorrections(PolStack pStack) {
        bkgdStack = null;
        doBkgdCorrect = false;
        doRatioing = false;
        // no background specified, so use current
        if (pStack.psParms.doBkgdCorrection) {
            // Set the Background stack to the appropriate CURRENT
            // background based on the image size for this pStack
            // @todo *** bkgdStack = BackgroundPolStack.getCompatibleBkgdStack(pStack);
            if (bkgdStack != null) {
                pStack.psParms.backGroundStack = bkgdStack.polStackID;
                doBkgdCorrect = true;
            } else { // No appropriately sized BkgdStack found
                pStack.psParms.backGroundStack = null;
                doBkgdCorrect = false;
                doRatioing = false;
            }
        }
        setup(pStack);
    }

    public void setup(PolStack pStack) {
        initializeFromPSParms(pStack);
        if (pStack.psParms.doBkgdCorrection && bkgdStack != null) {
            if (bkgdStack.bg21 == null) {
                calcBkgdArrays(bkgdStack);
            }
            // Ratioing
            if (pStack.psParms.doRatioing
                    && (pStack.psParms.roiRatioing.getWidth() > 0)) {
                // @todo ***  Ratioing.calcRatioingFactors(pStack, bkgdStack);
                doRatioing = true;
            } else {
                doRatioing = false;
            }
        }
    }

//-----------------------------------------------------------
// Initializes the calculation parameters using the parameters in
// this PolStack's PolStackParms
//
    void initializeFromPSParms(PolStack pStack) {
        initializeFromPSParms(pStack.psParms);
    }

    void setParameters(Map parMap) {
        this.wavelength = (Float) parMap.get("wavelength");
        this.swingFraction = (Float) parMap.get("swingFraction");
        this.retCeiling = (Float) parMap.get("retCeiling");
        this.azimuthRef = (Float) parMap.get("azimuthRef");
        this.zeroIntensity = (Float) parMap.get("zeroIntensity");
        this.doBkgdCorrect = (Boolean) parMap.get("doBkgdCorrect");
        this.algorithm = (Integer) parMap.get("algorithm");
        preCalculateValues();
    }

    void setParameters(
            float wavelength,
            float swingFraction,
            float retCeiling,
            float azimuthRef,
            float zeroIntensity,
            boolean doBkgdCorrect,
            int algorithm) {
        this.wavelength = wavelength;
        this.swingFraction = swingFraction;
        this.retCeiling = retCeiling;
        this.azimuthRef = azimuthRef;
        this.zeroIntensity = zeroIntensity;
        this.doBkgdCorrect = doBkgdCorrect;
        this.algorithm = algorithm;
        preCalculateValues();
    }

    void initializeFromPSParms(PolStack.PolStackParms psParms) {
        //    wavelength = 546.0f;
        //    swingFraction = 0.2f;
        //    retCeiling = 273.0f;
        //    azimuthRef = 90;
        //    zeroIntensity = 0;
        //    dynamicRange = 1;
        //
        //  from defaults...
        //  wavelength = Prefs.usr.getFloat("wavelength", 546.0);
        //  swingFraction = (float)Prefs.usr.getDouble("LC_Swing", 0.03);
        //  defaultRetCeiling = 0.5f*wavelength*swingFraction;
        //  retCeiling = (float)Prefs.usr.getDouble("retCeiling", (double)defaultRetCeiling);
        //  azimuthRef = (float)Prefs.usr.getInt("azimuthRef", 90);
        //
        wavelength = psParms.wavelength;
        swingFraction = psParms.swingFraction;
        retCeiling = psParms.retCeiling;
        azimuthRef = psParms.azimuthRef;
        zeroIntensity = psParms.zeroIntensity;
        algorithm = psParms.algorithm;
        //depth = (int) psParms.depth;
        // X dynamicRange = psParms.dynamicRange;
        //
        preCalculateValues();
    }

    private void preCalculateValues() {
        swingInNanoMeters = swingFraction * wavelength;
        swingAngle = (float) (2.0f * Math.PI * swingFraction);
        tanSwingAngleDiv2 = (float) (Math.tan(swingAngle / 2.f));
        retCeilingAngle = (2f * (float) Math.PI * retCeiling) / wavelength; // ~ .092
        if (depth == 8) {
            pixValueMax = 255.f; // for 8-bit grey, pixel value 0...255
        } else {
            pixValueMax = 4095.f; // for 12-bit
        }
        factor = pixValueMax / retCeilingAngle;
        //
    }

//////////////////////////////////////////////////////////////////////////
// Pre-Calculate the arrays for a Background PolStack
// Background stack is always a 5-frame
//
    public void calcBkgdArrays(PolStack pStack) {
        int size = pStack.size;
        pStack.bg21 = new double[size];
        pStack.bg31 = new double[size];
        pStack.bg21_5frame = new double[size];
        pStack.bg31_5frame = new double[size];
        pStack.kFor2Frame = 0f;
        double t1;
        double t2;
        double t3;
        double t1_5frame;
        double t2_5frame;
        double t3_5frame;
        int p0;
        int p1;
        int p2;
        int p3;
        int p4;
        for (int i = 0; i < size; i++) {
            if (pStack.depth == 8) {
                p0 = (int) (((byte[]) pStack.slice0)[i] & 0xFF);
                p1 = (int) (((byte[]) pStack.slice1)[i] & 0xFF);
                p2 = (int) (((byte[]) pStack.slice2)[i] & 0xFF);
                p3 = (int) (((byte[]) pStack.slice3)[i] & 0xFF);
                p4 = (int) (((byte[]) pStack.slice4)[i] & 0xFF);
            } else {
                p0 = (int) (((short[]) pStack.slice0)[i] & 0xFFFF);
                p1 = (int) (((short[]) pStack.slice1)[i] & 0xFFFF);
                p2 = (int) (((short[]) pStack.slice2)[i] & 0xFFFF);
                p3 = (int) (((short[]) pStack.slice3)[i] & 0xFFFF);
                p4 = (int) (((short[]) pStack.slice4)[i] & 0xFFFF);
            }
            // 4-Frame
            t1 = ((p2 + p3) - (2 * p0));
            t2 = (p2 - p3);
            t3 = ((p2 + p3) - (2 * p1));
            if (t1 != 0) {
                pStack.bg21[i] = t2 / t1;
                pStack.bg31[i] = t3 / t1;
            }

            // 5-Frame
            t1_5frame = ((p1 + p2 + p3 + p4) - (4 * p0)) / 2;
            t2_5frame = (p2 - p3);
            t3_5frame = (p4 - p1);
            if (t1_5frame != 0) {
                pStack.bg21_5frame[i] = (t2_5frame / t1_5frame);
                pStack.bg31_5frame[i] = (t3_5frame / t1_5frame);
            }

            // 2-Frame
            //pStack.kFor2Frame = (float) (pStack.kFor2Frame + t1);
            pStack.kFor2Frame = (float) (pStack.kFor2Frame + (p2 + p4) - (2 * p0));
        }

        // 2-Frame, average it
        pStack.kFor2Frame = (float) (pStack.kFor2Frame / size);
    }

//-----------------------------------------------------------
// Calculate Retardance Images for this PolStack
//
    public void calcRetardanceImages(PolStack pStack,
            boolean _doMag,
            boolean _doAzim) {
        //    if(Prefs.usr.getBoolean("display.suspendable", true)) {
        //      Camera.displaySuspend();
        //    }

        doMag = _doMag;
        doAzim = _doAzim;
        // Allocate imgMagnitude & imgAzimuth slices, if necessary
        if (pStack.imgMagnitude == null) {
            if (pStack.depth == 8) {
                pStack.imgMagnitude = new byte[pStack.size];
            } else {
                pStack.imgMagnitude = new short[pStack.size];
            }
        }
        if (pStack.imgAzimuth == null) {
            if (pStack.depth == 8) {
                pStack.imgAzimuth = new byte[pStack.size];
            } else {
                pStack.imgAzimuth = new short[pStack.size];
            }
        }
        //
        if ((algorithm == 2) && (pStack.numSlices >= 2)) {
            //calc2Frame();
        }
        if ((algorithm == 3) && (pStack.numSlices >= 3)) {
            //calc3Frame();
        }
        if ((algorithm == 4) && (pStack.numSlices >= 4)) {
            calc4Frame(pStack);
        }
        if ((algorithm == 5)) {
            if (pStack.numSlices == 5) {
                // calc5Frame(pStack);
                calculate(pStack, doMag, doAzim, false);
            }
            if (pStack.numSlices == 4) {
                algorithm = 4;
                calculate(pStack, doMag, doAzim, false);
            }
        }
        //	pStack.psParms.retardanceMax = (float) largestValue;
        // this isn't valid for raw PolStacks....
        //      if (pStack.psParms.algorithm < pStack.numSlices) {
        //         DialogBoxes.boxError("Calculation Error",
        //            "Too few slices for selected calculation algorithm.");
        //      }
        //    if (Prefs.usr.getBoolean("display.suspendable", true)) {
        //      Camera.displayResume();
        //    }
    }

//-----------------------------------------------------------
// calcRetardanceImagesNoBkgd
//
    public void calcRetardanceImagesNoBkgd(PolStack pStack,
            boolean _doMag, boolean _doAzim) {
        boolean was = doBkgdCorrect;
        // NOT setupParametersAndCorrections(pStack);
        initializeFromPSParms(pStack);
        doBkgdCorrect = false;
        doRatioing = false;
        calcRetardanceImages(pStack, _doMag, _doAzim);
        doBkgdCorrect = was;
    }

    public float[] calc5FrameFloat(PolStack pStack) {
        return calculate(pStack, doMag, doAzim, true);

    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // PolStack Calculation
    // New as of Sept 2005, PSj 1.6b
    // Handles both 4 and 5 frame
    public float[] calculate(PolStack pStack,
            boolean doMag, boolean doAzim, boolean makeFloat) {

        boolean doRounding = true;
        int algo = algorithm;
        boolean is4frame = (algo == 4);
        boolean is5frame = (algo == 5);
        boolean is8bit = (pStack.depth == 8);

        double t1 = 0;
        double t2 = 0;
        double t3 = 0;
        double tb21 = 0;
        double tb31 = 0;
        int p0, p1, p2, p3, p4 = 0;
        double azim = 0;
        double magnitude = 0;
        double rawMag = 0;
        double largestValue = 0;
        int zI = (int) Math.round(zeroIntensity);
        int size = pStack.size;
//         long size = 0;
//         if (pStack.depth == 8) {
//            size = ((byte[]) pStack.imgMagnitude).length;
//         } else {
//            size = ((short[]) pStack.imgMagnitude).length;
//         }
        // Float calculation
        float[] retFloatArray = null;
        if (makeFloat) {
            retFloatArray = new float[size];
        }

        int statusIncr = size / 10;

        for (int i = 0; i < size; i++) {
            if (pStack.depth == 8) {
                p0 = (int) (((byte[]) pStack.slice0)[i] & 0xFF);
                p1 = (int) (((byte[]) pStack.slice1)[i] & 0xFF);
                p2 = (int) (((byte[]) pStack.slice2)[i] & 0xFF);
                p3 = (int) (((byte[]) pStack.slice3)[i] & 0xFF);
                if (is5frame) {
                    p4 = (int) (((byte[]) pStack.slice4)[i] & 0xFF);
                }
            } else { // if (pStack.depth == 12) {
                p0 = (int) (((short[]) pStack.slice0)[i] & 0xFFFF);
                p1 = (int) (((short[]) pStack.slice1)[i] & 0xFFFF);
                p2 = (int) (((short[]) pStack.slice2)[i] & 0xFFFF);
                p3 = (int) (((short[]) pStack.slice3)[i] & 0xFFFF);
                if (is5frame) {
                    p4 = (int) (((short[]) pStack.slice4)[i] & 0xFFFF);
                }
            }

            if (doRatioing) { // apply ratioing correction
                p0 = (int) ((p0 - zI) * pStack.psParms.ratioingAvg[0]) + zI;
                p1 = (int) ((p1 - zI) * pStack.psParms.ratioingAvg[1]) + zI;
                p2 = (int) ((p2 - zI) * pStack.psParms.ratioingAvg[2]) + zI;
                p3 = (int) ((p3 - zI) * pStack.psParms.ratioingAvg[3]) + zI;
                if (is5frame) {
                    p4 = (int) ((p4 - zI) * pStack.psParms.ratioingAvg[4]) + zI;
                }
            }

            if (is4frame) {
                t1 = (double) ((p2 + p3) - (2 * p0));
                t2 = (double) (p2 - p3);
                t3 = (double) ((p2 + p3) - (2 * p1));
            }
            // Note (double) *****

            if (is5frame) {
                t1 = (double) (((p1 + p2 + p3 + p4) - (4 * p0)) / 2);
                t2 = (double) (p2 - p3);
                t3 = (double) (p4 - p1);
            }

            if (t1 == 0) {
                if (is8bit) {
                    ((byte[]) pStack.imgMagnitude)[i] = (byte) (DEG_90 * factor);
                } else {
                    ((short[]) pStack.imgMagnitude)[i] = (short) (DEG_90 * factor);
                }
                largestValue = DEG_90;
            } else {
                if ((doBkgdCorrect) && (bkgdStack != null)) {
                    if (is4frame) {
                        tb21 = (t2 / t1) - bkgdStack.bg21[i];
                        tb31 = (t3 / t1) - bkgdStack.bg31[i];
                    }
                    if (is5frame) {
                        tb21 = (t2 / t1) - bkgdStack.bg21_5frame[i];
                        tb31 = (t3 / t1) - bkgdStack.bg31_5frame[i];
                    }
                } else {
                    tb21 = (t2 / t1);
                    tb31 = (t3 / t1);
                }

                if (doMag) {
                    rawMag = Math.atan(tanSwingAngleDiv2
                            * Math.sqrt((tb21 * tb21) + (tb31 * tb31)));
                    if (t1 > 0) {
                        magnitude = Math.round(rawMag * factor);
                    } else {
                        magnitude = Math.round((Math.PI - rawMag) * factor);
                        rawMag = Math.PI - rawMag;
                    }

                    if (magnitude > largestValue) {
                        largestValue = magnitude;
                    }
                    if (magnitude > pixValueMax) {
                        if (is8bit) {
                            ((byte[]) pStack.imgMagnitude)[i] = (byte) pixValueMax;
                        } else {
                            ((short[]) pStack.imgMagnitude)[i] = (short) pixValueMax;
                        }

                    } else {
                        if (is8bit) {
                            ((byte[]) pStack.imgMagnitude)[i] = (byte) magnitude;
                        } else {
                            ((short[]) pStack.imgMagnitude)[i] = (short) magnitude;
                        }
                    }
                }
                if (makeFloat) {
                    retFloatArray[i] = (float) rawMag;
                }
                //if (magnitude > largestValue) { largestValue = magnitude; }
            } // if doMag
            //

            // A Z I M U T H  computation
            if (doAzim) {
                if ((tb21 == 0) && (tb31 == 0)) {
                    azim = 0;
                } else { // tb21 != 0
                    azim = ((Math.acos(tb21 / Math.sqrt((tb21 * tb21) + (tb31 * tb31))))
                            / 2);
                    if (tb31 > 0) {
                        azim = DEG_90 + azim;
                    } else {
                        azim = DEG_90 - azim;
                    }
                }

                // Add azimuthRef (after converting to radians)

                // from 4frame
                // double azim1 = azim + ((azimuthRef * onePi) / 180) + onePi;
                // from 5frame
                double azim1 = azim + ((onePi * azimuthRef) / 180);
                double azim2 = azim1 % onePi;

                // Correct if there is a mirror in the optical path
                if (invertedMicroscope) {
                    azim2 = onePi - azim2;
                }

                // convert to degrees (0-180)
                // added Math.round to this 5-frame calc
                if (!doRounding) {
                    if (is8bit) {
                        ((byte[]) pStack.imgAzimuth)[i] =
                                (byte) ((azim2 * 180) / onePi);
                    } else {
                        ((short[]) pStack.imgAzimuth)[i] =
                                (short) ((azim2 * 180) / onePi);
                    }
                } else {
                    if (is8bit) {
                        ((byte[]) pStack.imgAzimuth)[i] =
                                (byte) Math.round((azim2 * 180) / onePi);
                    } else {
                        ((short[]) pStack.imgAzimuth)[i] =
                                (short) Math.round((azim2 * 180) / onePi);
                    }
                }

            }
        }
        return retArray;
    }

/////////////////////////////////////////////////////////////////////////
// 4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4
// 4 Frame Calcuation
//
    void calc4Frame(PolStack pStack) {
        double t1 = 0;
        double t2 = 0;
        double t3 = 0;
        double tb21 = 0;
        double tb31 = 0;
        int p0;
        int p1;
        int p2;
        int p3;
        double azim = 0;
        double magnitude = 0;
        int zI = (int) Math.round(zeroIntensity);
        long size = 0;
        if (pStack.depth == 8) {
            size = ((byte[]) pStack.imgMagnitude).length;
        } else {
            size = ((short[]) pStack.imgMagnitude).length;
        }

        //    System.out.println("Run 4-frame calc.");
        //
        //initializeFromPSParms(pStack);

        //assert (bkgdStack.size == pStack.imgMagnitude.length);

        //    System.out.println("doBkgdCorrect: " + doBkgdCorrect +
        //    "  bkgdStack: " + bkgdStack);

        //scaleFactor =
        //  CalcTable.dimSqrtSumX/(float)(2.f*Math.PI*swingFraction*dynamicRange);
        //
        long statusIncr = size / 10;

        for (int i = 0; i < size; i++) {
            if (pStack.depth == 8) {
                p0 = (int) (((byte[]) pStack.slice0)[i] & 0xFF);
                p1 = (int) (((byte[]) pStack.slice1)[i] & 0xFF);
                p2 = (int) (((byte[]) pStack.slice2)[i] & 0xFF);
                p3 = (int) (((byte[]) pStack.slice3)[i] & 0xFF);
            } else {
                p0 = (int) (((short[]) pStack.slice0)[i] & 0xFFFF);
                p1 = (int) (((short[]) pStack.slice1)[i] & 0xFFFF);
                p2 = (int) (((short[]) pStack.slice2)[i] & 0xFFFF);
                p3 = (int) (((short[]) pStack.slice3)[i] & 0xFFFF);
            }

            //
            if (doRatioing) { // apply ratioing correction
                p0 = (int) ((p0 - zI) * pStack.psParms.ratioingAvg[0]) + zI;
                p1 = (int) ((p1 - zI) * pStack.psParms.ratioingAvg[1]) + zI;
                p2 = (int) ((p2 - zI) * pStack.psParms.ratioingAvg[2]) + zI;
                p3 = (int) ((p3 - zI) * pStack.psParms.ratioingAvg[3]) + zI;
            }
            //
            t1 = (double) ((p2 + p3) - (2 * p0));
            t2 = (double) (p2 - p3);
            t3 = (double) ((p2 + p3) - (2 * p1));
            //
            // 4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4  4
            // RETARDANCE MAGNITUDE computation
            if (t1 == 0) {
                if (pStack.depth == 8) {
                    ((byte[]) pStack.imgMagnitude)[i] = (byte) (DEG_90 * factor);
                } else {
                    ((short[]) pStack.imgMagnitude)[i] = (short) (DEG_90 * factor);
                }
                largestValue = DEG_90;
            } else {
                if (doBkgdCorrect && (bkgdStack != null)) {
                    tb21 = (t2 / t1) - bkgdStack.bg21[i];
                    tb31 = (t3 / t1) - bkgdStack.bg31[i];
                } else {
                    tb21 = (t2 / t1);
                    tb31 = (t3 / t1);
                }
                if (t1 > 0) {
                    magnitude = (Math.round((Math.atan(tanSwingAngleDiv2
                            * Math.sqrt((tb21 * tb21) + (tb31 * tb31)))) * factor));
                } else {
                    magnitude = (Math.round((Math.PI
                            - Math.atan(tanSwingAngleDiv2
                            * Math.sqrt((tb21 * tb21) + (tb31 * tb31)))) * factor));
                }
            }
            if (magnitude > largestValue) {
                largestValue = magnitude;
            }
            if (magnitude > pixValueMax) {
                if (pStack.depth == 8) {
                    ((byte[]) pStack.imgMagnitude)[i] = (byte) pixValueMax;
                } else {
                    ((short[]) pStack.imgMagnitude)[i] = (short) pixValueMax;
                }
            } else {
                if (pStack.depth == 8) {
                    ((byte[]) pStack.imgMagnitude)[i] = (byte) magnitude;
                } else {
                    ((short[]) pStack.imgMagnitude)[i] = (short) magnitude;
                }
            }

            // AZIMUTH computation  4 4 4 4 4
            if ((tb21 == 0) && (tb31 == 0)) {
                azim = 0;
            } else { // tb21 != 0
                azim = ((Math.acos(tb21 / Math.sqrt((tb21 * tb21) + (tb31 * tb31))))
                        / 2);
                if (tb31 > 0) {
                    azim = DEG_90 + azim;
                } else {
                    azim = DEG_90 - azim;
                }
            }

            // Add azimuthRef (after converting to radians)
            double azim1 = azim + ((azimuthRef * onePi) / 180) + onePi;
            double azim2 = azim1 % onePi;
            if (invertedMicroscope) {
                azim2 = onePi - azim2;
            }

            // convert to degrees (0-180)
            if (pStack.depth == 8) {
                ((byte[]) pStack.imgAzimuth)[i] = (byte) ((azim2 * 180) / onePi);
            } else {
                ((short[]) pStack.imgAzimuth)[i] = (short) ((azim2 * 180) / onePi);
            }
        }

        //pStack.imgAzimuth[i] = (byte) Math.round(azim2 * 180 / onePi);
        // for auto-scaling
        // if(isAutoScale) {

        //	  if (retard[i] > largestValue)
        //		largestValue = retard[i];
        //	  if (retard[i] > retCeilingAngle)
        //		retard[i] = retCeilingAngle;

        // if(isAutoScale) {
        //   applyAutoScale(largestValue,
        //
        //	float factor = pixValueMax/largestValue;
        //	for (int i = 0; i < imgMagnitude.length; i++) {
        //	  pStack.imgMagnitude[i] = (byte)(Math.round(retard[i]*factor));
        //	}
    }

/////////////////////////////////////////////////////////////////////////
// 5 Frame Calcuation
//
//  5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5
//
    void calc5Frame(PolStack pStack) {
        calc5Frame(pStack, false);
    }
    float[] retArray = null;

    void calc5Frame(PolStack pStack, boolean makeFloat) {
        double t1 = 0;
        double t2 = 0;
        double t3 = 0;
        double tb21 = 0;
        double tb31 = 0;
        int p0, p1, p2, p3, p4;
        double largestValue = 0;
        double azim = 0;
        double magnitude = 0;
        double rawMag = 0;

        int zI = (int) Math.round(zeroIntensity);
        int size = pStack.size;
        if (makeFloat) {
            retArray = new float[size];
        }
        //Inspector.inspectWait(pStack);
        System.out.println("doratioing = " + doRatioing);
        //    System.out.println("Run 5-frame calc.");
        //    assert(bkgdStack.size == (pStack.imgMagnitude.length + 1));
        int statusIncr = size / 10;

        //
        for (int i = 0; i < size; i++) {
            if (pStack.depth == 8) {
                p0 = (int) (((byte[]) pStack.slice0)[i] & 0xFF);
                p1 = (int) (((byte[]) pStack.slice1)[i] & 0xFF);
                p2 = (int) (((byte[]) pStack.slice2)[i] & 0xFF);
                p3 = (int) (((byte[]) pStack.slice3)[i] & 0xFF);
                p4 = (int) (((byte[]) pStack.slice4)[i] & 0xFF);
            } else { // if (pStack.depth == 12) {
                p0 = (int) (((short[]) pStack.slice0)[i] & 0xFFFF);
                p1 = (int) (((short[]) pStack.slice1)[i] & 0xFFFF);
                p2 = (int) (((short[]) pStack.slice2)[i] & 0xFFFF);
                p3 = (int) (((short[]) pStack.slice3)[i] & 0xFFFF);
                p4 = (int) (((short[]) pStack.slice4)[i] & 0xFFFF);
            }

            if (doRatioing) { // apply ratioing correction
                // This was changed Aug 29, 2005 to be consistant with 4-frame
                // where zI is subtracted after the multiplication
                p0 = (int) ((p0 - zI) * pStack.psParms.ratioingAvg[0]) + zI;
                p1 = (int) ((p1 - zI) * pStack.psParms.ratioingAvg[1]) + zI;
                p2 = (int) ((p2 - zI) * pStack.psParms.ratioingAvg[2]) + zI;
                p3 = (int) ((p3 - zI) * pStack.psParms.ratioingAvg[3]) + zI;
                p4 = (int) ((p3 - zI) * pStack.psParms.ratioingAvg[3]) + zI;
            }

            t1 = ((p1 + p2 + p3 + p4) - (4 * p0)) / 2;
            t2 = (p2 - p3);
            t3 = (p4 - p1);
            if (t1 == 0) {
                if (pStack.depth == 8) {
                    ((byte[]) pStack.imgMagnitude)[i] = (byte) (DEG_90 * factor);
                } else {
                    ((short[]) pStack.imgMagnitude)[i] = (short) (DEG_90 * factor);
                }
                largestValue = DEG_90;
            } else {
                if ((doBkgdCorrect) && (bkgdStack != null)) {
                    tb21 = (t2 / t1) - bkgdStack.bg21_5frame[i];
                    tb31 = (t3 / t1) - bkgdStack.bg31_5frame[i];
                } else {
                    tb21 = (t2 / t1);
                    tb31 = (t3 / t1);
                }

                //  5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5
                // R E T A R D A N C E  computation
                if (doMag) {
                    rawMag = Math.atan(tanSwingAngleDiv2
                            * Math.sqrt((tb21 * tb21) + (tb31 * tb31)));

                    if (t1 > 0) {
                        magnitude = Math.round(rawMag * factor);
                    } else {
                        magnitude = Math.round((Math.PI - rawMag) * factor);
                        rawMag = Math.PI - rawMag;
                    }

                    if (magnitude > largestValue) {
                        largestValue = magnitude;
                    }
                    if (magnitude > pixValueMax) {
                        if (pStack.depth == 8) {
                            ((byte[]) pStack.imgMagnitude)[i] = (byte) pixValueMax;
                        } else {
                            ((short[]) pStack.imgMagnitude)[i] = (short) pixValueMax;
                        }

                    } else {
                        if (pStack.depth == 8) {
                            ((byte[]) pStack.imgMagnitude)[i] = (byte) magnitude;
                        } else {
                            ((short[]) pStack.imgMagnitude)[i] = (short) magnitude;
                        }
                    }
                }
                if (makeFloat) {
                    if (t1 > 0) {
                        retArray[i] = (float) rawMag;
                    } else {
                        retArray[i] = (float) (Math.PI - rawMag);
                    }
                }
                if (magnitude > largestValue) {
                    largestValue = magnitude;
                }
            }
            // A Z I M U T H  computation  5 5 5 5 5 5 5 5 5 5 5
            if (doAzim) {
                if ((tb21 == 0) && (tb31 == 0)) {
                    azim = 0;
                } else { // tb21 != 0
                    azim = ((Math.acos(tb21 / Math.sqrt((tb21 * tb21) + (tb31 * tb31))))
                            / 2);
                    if (tb31 > 0) {
                        azim = DEG_90 + azim;
                    } else {
                        azim = DEG_90 - azim;
                    }
                }

                // Add azimuthRef (after converting to radians)
                double azim1 = azim + ((onePi * azimuthRef) / 180);
                double azim2 = azim1 % onePi;
                if (invertedMicroscope) {
                    azim2 = onePi - azim2;
                }

                // convert to degrees (0-180)
                // added Math.round to this 5-frame calc
                if (pStack.depth == 8) {
                    ((byte[]) pStack.imgAzimuth)[i] =
                            (byte) Math.round((azim2 * 180) / onePi);
                } else {
                    ((short[]) pStack.imgAzimuth)[i] =
                            (short) Math.round((azim2 * 180) / onePi);
                }

            }
        }
    }

/////////////////////////////////////////////////////////////////////////
// 2 Frame Calcuation
//
//    void calc2Frame(PolStack pStack) {
//      double t1 = 0;
//      double t2 = 0;
//      double t3 = 0;
//      double tb21 = 0;
//      double tb31 = 0;
//      int p0;
//      int p1;
//      int p2;
//      int p3;
//      int p4;
//      double largestValue = 0;
//      double azim = 0;
//      double magnitude = 0;
//      double rawMag = 0;
//      int zI = (int) Math.round(zeroIntensity);
//     // kFor2Frame = mean(bg3 + bg4 - 2*bg1);  k = ~125.
//     // pStack.kFor2Frame
//     // float tanSwingAngleDiv2K = (float) (Math.tan(swingAngle / 2.)) / Bkgd.kFor2Frame; // 3.05e-6
//      //    System.out.println("Run 5-frame calc.");
//      assert(bkgdStack.size == (pStack.imgMagnitude.length + 1));
//      jif.utils.PSjUtils.statusProgress("Calculating Retardance Images...", 0);
//      int statusIncr = pStack.imgMagnitude.length / 10;
//      //
//      for (int i = 0; i < pStack.imgMagnitude.length; i++) {
//        //
//        if ( (i % statusIncr) == 0) {
//          jif.utils.PSjUtils.statusProgress("Calculating Retardance Images...",
//              (int) (100 * ( (float) pStack.imgMagnitude.length / (float) i)));
//        }
////////////////////////////////////////

    /*   void calc2Frame(PolStack pStack) {
     p2 = pixels[i] & 0xFF;
     p4 = pixels4[i] & 0xFF;
     bg_2 = bg2[i] & 0xFF;
     bg_4 = bg4[i] & 0xFF;
     a = (float) (p2 - bg_2) * tanSwingAngleDiv2K;
     b = (float) (p4 - bg_4) * tanSwingAngleDiv2K;
     retard[i] = (float) Math.atan(2 * Math.sqrt(a * a + b * b));
     //if (b!=0) azim[i] = arctan(a/b)/2
     //else azim[i]=1.57f;
     }
     /////////////////////////////////////////
     //
     p1 = (int) (pStack.slice1[i] & 0xFF);
     p4 = (int) (pStack.slice4[i] & 0xFF);
     //
     if (doRatioing) { // apply ratioing correction
     p1 = (int) ( (p1 - zI) * bkgdStack.psParms.ratioingAvg[1]) + zI;
     p4 = (int) ( (p4 - zI) * bkgdStack.psParms.ratioingAvg[4]) + zI;
     }
     t1 = (double) ( (p1 + p2 + p3 + p4) - (4 * p0)) / 2;
     t2 = (double) (p2 - p3);
     t3 = (double) (p4 - p1);
     if (t1 == 0) {
     pStack.imgMagnitude[i] = (byte) (DEG_90 * factor);
     largestValue = DEG_90;
     }
     else {
     if ( (doBkgdCorrect) && (bkgdStack != null)) {
     tb21 = (t2 / t1) - bkgdStack.bg21_5frame[i];
     tb31 = (t3 / t1) - bkgdStack.bg31_5frame[i];
     }
     else {
     tb21 = (t2 / t1);
     tb31 = (t3 / t1);
     }
     // R E T A R D A N C E  MAGNITUDE computation 5 5 5 5 5 5 5 5
     if (doMag) {
     rawMag =
     Math.atan(tanSwingAngleDiv2 * Math.sqrt( (tb21 * tb21)
     + (tb31 * tb31)));
     if (t1 > 0) {
     magnitude = Math.round(rawMag * factor);
     }
     else {
     magnitude = Math.round( (Math.PI - rawMag) * factor);
     rawMag = Math.PI - rawMag;
     }
     }
     if (magnitude > largestValue) {
     largestValue = magnitude;
     }
     if (magnitude > pixValueMax) {
     pStack.imgMagnitude[i] = (byte) pixValueMax;
     }
     else {
     pStack.imgMagnitude[i] = (byte) magnitude;
     }
     }
     // A Z I M U T H  computation  5 5 5 5 5 5 5 5 5 5 5
     //
     if (doAzim) {
     if ( (tb21 == 0) && (tb31 == 0)) {
     azim = 0;
     }
     else { // tb21 != 0
     azim = ( (Math.acos(tb21 / Math.sqrt( (tb21 * tb21)
     + (tb31 * tb31)))) / 2);
     if (tb31 > 0) {
     azim = DEG_90 + azim;
     }
     else {
     azim = DEG_90 - azim;
     }
     }
     // Add azimuthRef (after converting to radians)
     double azim1 = azim + ( (onePi * azimuthRef) / 180);
     double azim2 = azim1 % onePi;
     if (invertedMicroscope) {
     azim2 = onePi - azim2;
     }
     // convert to degrees (0-180)
     //pStack.imgAzimuth[i] =  (byte) ((azim2 * 180) / onePi);
     pStack.imgAzimuth[i] = (byte) Math.round( (azim2 * 180) / onePi);
     }
     }
     }
     */
//-----------------------------------------------------------
// initialize2FrameLookup8
//
    public void initialize2FrameLookup8(PolStack.PolStackParms psParmsFor2Frame) {
        // set PS Parameters
        initializeFromPSParms(psParmsFor2Frame);
        // addition Calculated values for 2-Frame
        factor = pixValueMax / retCeilingAngle;
        float tanSwingAngleDiv2K = tanSwingAngleDiv2;

        //    (float) (Math.tan(swingAngle / 2.)) / 0.000003f;
        // replace 0.0000003f with Bkgd.kFor2Frame; // 3.05e-6
        //
        float a;

        //    (float) (Math.tan(swingAngle / 2.)) / 0.000003f;
        // replace 0.0000003f with Bkgd.kFor2Frame; // 3.05e-6
        //
        float b;
        float bg_2;
        float bg_4;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                bg_2 = 0; //bg2[i] & 0xFF;
                bg_4 = 0; // bg4[i] & 0xFF;
                a = (float) (i - bg_2) * tanSwingAngleDiv2K;
                b = (float) (j - bg_4) * tanSwingAngleDiv2K;
                polCalcLookup[i][j] = (byte) (Math.round(Math.atan(2
                        * Math.sqrt((a * a) + (b * b))) * factor));
            }
        }
        System.out.println("lookupInitialized");
        /*   void calc2Frame(PolStack pStack) {
         p2 = pixels[i] & 0xFF;
         p4 = pixels4[i] & 0xFF;
         bg_2 = bg2[i] & 0xFF;
         bg_4 = bg4[i] & 0xFF;
         a = (float) (p2 - bg_2) * tanSwingAngleDiv2K;
         b = (float) (p4 - bg_4) * tanSwingAngleDiv2K;
         retard[i] = (float) Math.atan(2 * Math.sqrt(a * a + b * b));
         //if (b!=0) azim[i] = arctan(a/b)/2
         //else azim[i]=1.57f;
         }
         */
    }

//----------------------------------------------------------------
//
    public byte[] getPSLookupTable() {
        byte[] pix = new byte[65536];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                pix[(i * 256) + j] = polCalcLookup[i][j];
            }
        }
        return pix;
    }

// OLD JUNK =========================================================
   /*
     // Nov 25
     // AZIMUTH computation  4 4 4 4 4
     if ((tb21 == 0) && (tb31 == 0)) {
     azim = 0;
     } else { // tb21 != 0
     azim = ((Math.acos(tb21 / Math.sqrt((tb21 * tb21) + (tb31 * tb31)))) / 2);
     if (Double.isNaN(azim)) {
     if (t1 >= 0) {
     azim = 0;
     } else {
     azim = DEG_90;
     }
     }
     if (t3 < 0) {
     azim = onePi - azim;
     }
     if (t1 >= 0) {
     azim = azim + DEG_90;
     } else {
     azim = DEG_180 - azim;
     }
     }
     */
    /*
     Copy of  === A Z I M U T H  computation  5 5 5 5 5 5 5 5 5 5 5
     //
     if ((tb21 == 0) && (tb31 == 0)) {
     azim = 0;
     //      if (tb31 == 0) {
     //        if (tb21 == 0) {
     //          azim = 0;
     //        } else {
     //          if (t1 <= 0) {
     //            azim = 0;
     //          }
     //          else {
     //            azim = DEG_90;
     //          }
     //        }
     } else { // tb21 != 0
     azim = ((Math.acos(tb21 / Math.sqrt((tb21 * tb21) + (tb31 * tb31)))) / 2);
     if (tb31 > 0) {
     azim = DEG_90 + azim;
     } else {
     azim = DEG_90 - azim;
     }
     //        ((tb31 > 0) && (rawMag <= DEG_90))
     //              || ((tb31 <= 0) && (rawMag > DEG_90))) {
     //        if (Double.isNaN(azim)) {
     //          if (t1 >= 0) {
     //            azim = 0;
     //          } else {
     //            azim = DEG_90;
     //          }
     //        }
     //        if (t3 < 0) {
     //          azim = DEG_180 - azim;
     //        }
     //        if (t1 >= 0) {
     //          azim = azim + DEG_90;
     //        } else {
     //          azim = DEG_180 - azim;
     //        }
     // t3>=0 & t1 <0
     }
     */
    public static void main(String[] args) {
        testFastMath();
        try {
//			PolStack bkgdStack = new PolStack(edu.mbl.jif.Constants.testDataPath	+ "ps\\BG.tiff",true);
//			Inspector.inspectWait(bkgdStack);
            JFrame f = new JFrame();
            f.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    // test
    static int n = 100;

    public static void testFastMath() {

//        
//        for (int i = 0; i < n; i++) {
//             // -2pi to +2pi
//            double range = 4*Math.PI;
//            double x = range / n * (double) (i - n / 2);
//            System.out.println(x);
//        }

        for (int i = 0; i < n; i++) {
            // -2pi to +2pi
            double range = Math.PI / 2;
            double x = range / n * (double) (i - n / 2);
            double math = Math.atan(x);
            double fast = atan_66(x);
            System.out.println(x + ": error= " + (math - fast));
        }

        long start = 0;

        start = System.nanoTime();
        for (int j = 0; j < 100000; j++) {
            for (int i = 0; i < n; i++) {
                // -2pi to +2pi
                double range = Math.PI / 2;
                double x = range / n * (double) (i - n / 2);
                double math = Math.atan(x);
            }
        }
        long mathatantime = System.nanoTime() - start;
        System.out.println("Math.atan:            " + mathatantime);

        start = System.nanoTime();
        for (int j = 0; j < 100000; j++) {
            for (int i = 0; i < n; i++) {
                // -2pi to +2pi
                double range = Math.PI / 2;
                double x = range / n * (double) (i - n / 2);
                double math = atan_66(x);
            }
        }
        long atan_66time = System.nanoTime() - start;
        System.out.println("atan_66:              " + atan_66time);
        System.out.println((float) mathatantime / (float) atan_66time);
    }
    // <editor-fold defaultstate="collapsed" desc=">>>--- Fast Math Functions  ---<<<" >
    static final double pi = 3.1415926535897932384626433;	// pi
    static final double twopi = 2.0 * pi;			// pi times 2
    static final double two_over_pi = 2.0 / pi;                 // 2/pi
    static final double halfpi = pi / 2.0;			// pi divided by 2
    static final double threehalfpi = 3.0 * pi / 2.0;  		// pi times 3/2, used in tan routines
    static final double four_over_pi = 4.0 / pi;		// 4/pi, used in tan routines
    static final double qtrpi = pi / 4.0;			// pi/4.0, used in tan routines
    static final double sixthpi = pi / 6.0;			// pi/6.0, used in atan routines
    static final double tansixthpi = Math.tan(sixthpi);		// tan(pi/6), used in atan routines
    static final double twelfthpi = pi / 12.0;			// pi/12.0, used in atan routines
    static final double tantwelfthpi = Math.tan(twelfthpi);	// tan(pi/12), used in atan routines

    static double atan_66(double x) {
        double y; // return from atan__s function
        boolean complement = false; // true if arg was >1
        boolean region = false; // true depending on region arg is in
        boolean sign = false; // true if arg was < 0
        if (x < 0) {
            x = -x;
            sign = true; // arctan(-x)=-arctan(x)
        }
        if (x > 1.0) {
            x = 1.0 / x; // keep arg between 0 and 1
            complement = true;
        }
        if (x > tantwelfthpi) {
            x = (x - tansixthpi) / (1 + tansixthpi * x); // reduce arg to under tan(pi/12)
            region = true;
        }
        y = atan_66s(x); // run the approximation
        if (region) {
            y += sixthpi;
        } // correct for region we're in
        if (complement) {
            y = halfpi - y;
        } // correct for 1/x if we did that
        if (sign) {
            y = -y;
        } // correct for negative arg
        return (y);
    }
    // atan_66s computes atan(x)
    // Accurate to about 6.6 decimal digits over the range [0, pi/12].
    static final double c1 = 1.6867629106;
    static final double c2 = 0.4378497304;
    static final double c3 = 1.6867633134;

    public static double atan_66s(double x) {
        double x2 = x * x;
        return (x * (c1 + x2 * c2) / (c3 + x2));
    }

    //===============================================================================
    /*
     * "The atan2 C++ function calculates the arctangent of the two variables y and x. 
     It is similar to calculating the arctangent of y/x, except that the signs of 
     both arguments are used to determine the quadrant of the result."
     Effectively, this means that atan2 finds the counterclockwise angle in 
     radians between the x-axis and the vector <x,y>
     This produces results in the range [-?,?], which can be mapped to [0,2?]
     by adding 2? to the negative values.
     As defined above, and traditionally, atan2(0,0) is undefined. 
     */
    public static double aTan2(double y, double x) {
        double coeff_1 = Math.PI / 4d;
        double coeff_2 = 3d * coeff_1;
        double abs_y = Math.abs(y);
        double angle;
        if (x >= 0d) {
            double r = (x - abs_y) / (x + abs_y);
            angle = coeff_1 - coeff_1 * r;
        } else {
            double r = (x + abs_y) / (abs_y - x);
            angle = coeff_2 - coeff_1 * r;
        }
        return y < 0d ? -angle : angle;
    }
// </editor-fold>
}
