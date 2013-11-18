/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.oidic;

import edu.mbl.jif.acq.AcqModel;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.InstrumentController;

/**
 *
 * @author GBH
 */
public class OiDicAcquistion {


    private ArcLCController vlcCtrl;
    private ArcLCModel vlcModel;
    private AcquisitionController acqCtrl;
    private AcqModel acqModel;
    //private PSCalcModel psCalcModel;
    //
    ij.ImagePlus lastAcquiredImgPlus = null;    // PS Calc values
    String sampleStackTitle = "";
    String bgStackTitle = "";
    //boolean mirror;
    //private boolean fastCalc;
    //boolean autoCalc = false;
    //String bitDepth;
    //double wavelength;
    //double swing;
    //double retCeiling;
    //double azimRef;
    //private String calcPlugin = "Calc_5Fr";

    public OiDicAcquistion(InstrumentController instCtrl) {
        this.vlcCtrl = (ArcLCController) instCtrl.getController("arcLC");
        this.vlcModel = (ArcLCModel) instCtrl.getModel("arcLC");
        this.acqModel = (AcqModel) instCtrl.getModel("acq");
        //this.psCalcModel = new PSCalcModel();
        //this.getIJPrefs();
    }
// <editor-fold defaultstate="collapsed" desc=" PolStack Acquisition ">

    public void doAcquireOiDic() {
        doAcquireOiDic(false);
    }

    public void doAcquireBkgdOiDic() {
        doAcquireOiDic(true);
    }

    public void doAcquireOiDic(boolean background) {
        CamAcq camAcq = CamAcq.getInstance();
        if (camAcq == null) {
            return;
        }
        System.out.println("Acquiring PolStack...");
        //camAcq.displayOpen();
        //camAcq.setExposureAcq(10);

        int nSlices = 6;
        ij.ImagePlus imgPlus = null;
        if (camAcq.getDepth() == 8) {
            imgPlus = camAcq.newCaptureStackByte(6);
        } else {
            imgPlus = camAcq.newCaptureStackShort(6);
        }

        camAcq.setImagePlusForCapture(imgPlus); // sets default stack for capture

        // temporary... set for background multiframe setting
        int multiFrameWas = 1;
        camAcq.startAcq(true);
        if(background) {
            multiFrameWas = acqModel.getMultiFrame();
            camAcq.setMultiFrame(acqModel.getMultiFrameBkgd());
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        for (int i = 0; i < nSlices; i++) {
            //camAcq.vlcSelect(i); // change LC...
            vlcCtrl.selectElementWait(i);
            camAcq.captureImageToSlice(imgPlus, i);
        }
        camAcq.captureStackFinish();
        if(background) {
            camAcq.setMultiFrame(multiFrameWas);
        }
        //camAcq.finishAcq();
        lastAcquiredImgPlus = imgPlus;
        String stackTitle = lastAcquiredImgPlus.getTitle();

        // +++++ get current values from CamAcqJ modules
//        if (background) {
//            bgStackTitle = "NoBg";
//        }
        sampleStackTitle = stackTitle;
        //setIJPrefs();
//        if (psCalcModel.isAutoCalc()) {
//            if (background) {
//                doBkgdPSCalculation();
//            } else {
//                doPSCalculation();
//            }
//        }
//        if (background) {
//            setBkgdStack();
//        }
        ij.IJ.save(imgPlus, camAcq.getImageDirectory() + "\\" + stackTitle + ".tif");

        camAcq.displayResume();
    }
// </editor-fold>
}