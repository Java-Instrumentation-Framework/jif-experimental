/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.acq.mode;

import edu.mbl.jif.acq.AcqModel;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camera.display.DisplayModel;
import edu.mbl.jif.laser.LaserController;
import edu.mbl.jif.laser.Surgeon;
import edu.mbl.jif.lightfield.LFViewController;
import edu.mbl.jif.microscope.Filter;
import edu.mbl.jif.microscope.illum.IllumModel;
import edu.mbl.jif.stage.StageXYController;
import edu.mbl.jif.varilc.camacq.VariLCController;
import edu.mbl.jif.varilc.VariLCModel;

/**
 *
 * @author GBH
 */
public class Context {

    //CamAcq camAcq;
    InstrumentController instrumentCtrl;
    //
    DataModel dataModel;
    CameraModel cameraModel;
    //DisplayLiveCamera liveDisplay;
    DisplayModel displayModel;
    AcqModel acqModel;
    AcquisitionController acqCtrl;
    IllumModel illumModel;
    VariLCModel vlcModel;
    VariLCController vlcCtrl;
    LFViewController lFController;
    StageXYController stageXYCtrl;
    LaserController laserCtrl;
    Surgeon surgeon;
    Filter filter;

    public  Context() {
        //camAcq = CamAcq.getInstance();
        instrumentCtrl = (InstrumentController) CamAcqJ.getInstance().getController();
        dataModel = (DataModel) instrumentCtrl.getModel("data");
        cameraModel = (CameraModel) instrumentCtrl.getModel("camera");
        //liveDisplay = (DisplayLiveCamera) camAcq.getDisplay();
        displayModel = (DisplayModel) instrumentCtrl.getModel("display");
        acqModel = (AcqModel) instrumentCtrl.getModel("acq");
        acqCtrl = (AcquisitionController) instrumentCtrl.getController("acq");
        illumModel = (IllumModel) instrumentCtrl.getModel("illum");
        vlcModel = (VariLCModel) instrumentCtrl.getModel("variLC");
        vlcCtrl = (VariLCController) instrumentCtrl.getController("variLC");
        //lFController = (LFViewController) camAcq.getModel("");
        stageXYCtrl = (StageXYController) instrumentCtrl.getController("stageXY");
        laserCtrl = (LaserController) instrumentCtrl.getController("laser");
        surgeon = (Surgeon) instrumentCtrl.getController("surgeon");
        filter = (Filter) instrumentCtrl.getModel("filter");
      }


}
