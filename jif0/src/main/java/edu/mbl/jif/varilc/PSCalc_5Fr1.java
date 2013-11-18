package edu.mbl.jif.varilc;

import ij.*;
import ij.plugin.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;

/**
Calculates retardance and orientation image using the 5 frame algorithm;
sample stack: slice 1 retardance, slice 2 orientation, slice 3 raw extinction, slice 4 raw ellipt1, etc.;
by Rudolf Oldenbourg, MBL;
4 Feb 07
added support for 8-bit and 16-bit image stacks,
added support for mirror in optical path,
added Bg correction and no Bg,
 */
public class PSCalc_5Fr1
        implements PlugInFilter {

    ImagePlus imp1, imp2;

// Variables from Preferences:
    String sampleStackTitle = Prefs.get("ps.sampleStackTitle", "sampleStack");
    String bgStackTitle = Prefs.get("ps.bgStackTitle", "NoBg");
    String mirror = Prefs.get("ps.mirror", "No");
    String bitDepth;
    float wavelength = (float) Prefs.get("ps.wavelength", 546d);
    float swing = (float) Prefs.get("ps.swing", 0.03d);
    float retCeiling = (float) Prefs.get("ps.retCeiling", 3d);
    float azimRef = (float) Prefs.get("ps.azimRef", 0d);
//	float intCeiling = 255f; // typically 255 or 4095, if set to 1 then ret pixel values are retardance in nm

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G + DOES_16 + STACK_REQUIRED;
    }


    public void run(ImageProcessor ip) {
        if (showDialog()) {
            Calc_5Fr(imp1, imp2);
        }
    }


    public void Calc_5Fr(ImagePlus imp1, ImagePlus imp2) {
        if (imp1.getType() != imp2.getType()) {
            error();
            return;
        }
        if (imp1.getType() == 0) {				//getType returns 0 for 8-bit, 1 for 16-bit
            bitDepth = "8-bit";
            Prefs.set("ps.bitDepth", bitDepth);
        } else {
            bitDepth = "16-bit";
            Prefs.set("ps.bitDepth", bitDepth);
        }
        int width = imp1.getWidth();
        int height = imp1.getHeight();
        if (width != imp2.getWidth() || height != imp2.getHeight()) {
            error();
            return;
        }

        ImageStack stack1 = imp1.getStack();
//		if (bgStackTitle != "NoBg") ImageStack stack2 = imp2.getStack();
        ImageStack stack2 = imp2.getStack();

        ImageProcessor ip = imp1.getProcessor();
        int dimension = width * height;
        byte[] pixB;
        short[] pixS;
        float[][] pixF = new float[5][dimension];
        float[][] pixFBg = new float[5][dimension];

        float a;
        float b;
        float den;
        float aSmp;
        float bSmp;
        float denSmp;
        float aBg;
        float bBg;
        float denBg;
        float retF;
        float azimF;

        byte[] retB = new byte[dimension];
        short[] retS = new short[dimension];
        byte[] azimB = new byte[dimension];
        short[] azimS = new short[dimension];
        // Derived Variables:
        float swingAngle = 2f * (float) Math.PI * swing;
        float tanSwingAngleDiv2 = (float) Math.tan(swingAngle / 2.f);
        float tanSwingAngleDiv2DivSqrt2 = (float) (Math.tan(swingAngle / 2.f) / Math.sqrt(2));
        float wavelengthDiv2Pi = wavelength / (2f * (float) Math.PI);

        // get the pixels of each slice in the stack and convert to float
        for (int i = 0; i < 5; i++) {
            if (bitDepth == "8-bit") {
                pixB = (byte[]) stack1.getPixels(i + 3);
                for (int j = 0; j < dimension; j++) {
                    pixF[i][j] = 0xff & pixB[j];
                }
                if (bgStackTitle != "NoBg") {
                    pixB = (byte[]) stack2.getPixels(i + 3);
                    for (int j = 0; j < dimension; j++) {
                        pixFBg[i][j] = 0xff & pixB[j];
                    }
                }
            } else {
                pixS = (short[]) stack1.getPixels(i + 3);
                for (int j = 0; j < dimension; j++) {
                    pixF[i][j] = (float) pixS[j];
                }
                if (bgStackTitle != "NoBg") {
                    pixS = (short[]) stack2.getPixels(i + 3);
                    for (int j = 0; j < dimension; j++) {
                        pixFBg[i][j] = (float) pixS[j];
                    }
                }
            }
        }

        // Algorithm
        //terms a and b
        for (int j = 0; j < dimension; j++) {
            denSmp = (pixF[1][j] + pixF[2][j] + pixF[3][j] + pixF[4][j] - 4 * pixF[0][j]) / 2;
            denBg = denSmp;
            a = (pixF[4][j] - pixF[1][j]);
            aSmp = a;
            aBg = a;
            b = (pixF[2][j] - pixF[3][j]);
            bSmp = b;
            bBg = b;
            if (bgStackTitle != "NoBg") {
                denBg = (pixFBg[1][j] + pixFBg[2][j] + pixFBg[3][j] + pixFBg[4][j] - 4 * pixFBg[0][j]) / 2;
                aBg = pixFBg[4][j] - pixFBg[1][j];
                bBg = pixFBg[2][j] - pixFBg[3][j];
            }
            // Special case of sample retardance half wave, denSmp = 0
            if (denSmp == 0) {
                retF = (float) wavelength / 4;
                azimF = (float) (a == 0 & b == 0 ? 0 : (azimRef + 90 + 90 * Math.atan2(a,
                        b) / Math.PI) % 180);
            } else {
                // Retardance, the background correction can be improved by separately considering sample retardance values larger than a quarter wave
                if (bgStackTitle != "NoBg") {
                    a = aSmp / denSmp - aBg / denBg;
                    b = bSmp / denSmp - bBg / denBg;
                } else {
                    a = aSmp / denSmp;
                    b = bSmp / denSmp;
                }
                retF = (float) Math.atan(tanSwingAngleDiv2 * Math.sqrt(a * a + b * b));
                if (denSmp < 0) {
                    retF = (float) Math.PI - retF;
                }
                retF = retF * wavelengthDiv2Pi; // convert to nm
                if (retF > retCeiling) {
                    retF = retCeiling;
                }

                // Orientation
                if ((bgStackTitle == "NoBg") || ((bgStackTitle != "NoBg") && (Math.abs(denSmp) < 1))) {
                    a = aSmp;
                    b = bSmp;
                }
                azimF = (float) (a == 0 & b == 0 ? 0 : (azimRef + 90 + 90 * Math.atan2(a,
                        b) / Math.PI) % 180);
            }
            if (bitDepth == "8-bit") {
                retB[j] = (byte) (((int) (255 * retF / retCeiling)) & 0xff);
            } else {
                retS[j] = (short) (4095 * retF / retCeiling);
            }
            if (mirror == "Yes") {
                azimF = 180 - azimF;
            }
            if (bitDepth == "8-bit") {
                azimB[j] = (byte) (((int) azimF) & 0xff);
            } else {
                azimS[j] = (short) (azimF * 10f);
            }
        }
        // show the resulting images in slice 1 and 2
        imp1.setSlice(3);
        if (bitDepth == "8-bit") {
            stack1.setPixels(retB, 1);
            stack1.setPixels(azimB, 2);
        } else {
            stack1.setPixels(retS, 1);
            stack1.setPixels(azimS, 2);
        }
        imp1.setSlice(1);
        IJ.selectWindow(imp1.getTitle());

        Prefs.set("ps.sampleStackTitle", sampleStackTitle);
        Prefs.set("ps.bgStackTitle", bgStackTitle);
        Prefs.set("ps.mirror", mirror);
        Prefs.set("ps.wavelength", wavelength);
        Prefs.set("ps.swing", swing);
        Prefs.set("ps.retCeiling", retCeiling);
        Prefs.set("ps.azimRef", azimRef);
        Prefs.savePreferences();
    }


    public boolean showDialog() {

        String bgChoice = "NoBg";
        int[] wList = WindowManager.getIDList();
        if (wList == null) {
            IJ.noImage();
            return false;
        }

        String[] sampleTitles = new String[wList.length];
        for (int i = 0; i < wList.length; i++) {
            ImagePlus imp = WindowManager.getImage(wList[i]);
            sampleTitles[i] = imp != null ? imp.getTitle() : "";
        }
        String[] bgTitles = new String[wList.length + 1];
        bgTitles[0] = "NoBg";
        for (int i = 1; i < wList.length + 1; i++) {
            bgTitles[i] = sampleTitles[i - 1];
        }
        for (int i = 1; i < (wList.length + 1); i++) {
            if (bgTitles[i] == bgStackTitle) {
                bgChoice = bgStackTitle;
            }
        }
        String sampleChoice = sampleTitles[0];
        for (int i = 1; i < (wList.length); i++) {
            if (sampleTitles[i] == sampleStackTitle) {
                sampleChoice = sampleStackTitle;
            }
        }

        String[] mirrors = new String[2];
        mirrors[0] = "No";
        mirrors[1] = "Yes";

        GenericDialog gd = new GenericDialog("PolScope 5Frame Calc");
        gd.addChoice("Sample:", sampleTitles, sampleChoice);
        gd.addChoice("Background:", bgTitles, bgChoice);
        gd.addChoice("Mirror:", mirrors, mirror);
        gd.addNumericField("Wavelength: ", wavelength, 1, 8, " nm");
        gd.addNumericField("Swing: ", swing, 3, 8, " wavelength");
        gd.addNumericField("Ret. Ceiling: ", retCeiling, 1, 8, " nm");
        gd.addNumericField("Orient. Ref.: ", azimRef, 1, 8, " degree");
        gd.showDialog();
        if (gd.wasCanceled()) {
            return false;
        }
        int index1 = gd.getNextChoiceIndex();
        int index2 = gd.getNextChoiceIndex();
        int index3 = gd.getNextChoiceIndex();
        wavelength = (float) gd.getNextNumber();
        swing = (float) gd.getNextNumber();
        retCeiling = (float) gd.getNextNumber();
        azimRef = (float) gd.getNextNumber();

        imp1 = WindowManager.getImage(wList[index1]);
        sampleStackTitle = sampleTitles[index1];
        bgStackTitle = bgTitles[index2];
        if (bgStackTitle == "NoBg") {
            imp2 = WindowManager.getImage(wList[index1]); //only to assign a valid ImagePlus to imp2 which is not used when NoBg
        } else {
            imp2 = WindowManager.getImage(wList[index2 - 1]);
        }
        mirror = mirrors[index3];

        return true;
    }


    void error() {
        IJ.showMessage("PolScope Calculator", "This plugin requires one or two stacks (two for BG correcion) that have\n" +
                "the same width, height, data type and at least 6 slices each.");
    }


}

