package edu.mbl.jif;

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
public abstract class AbstractAcqRoutine {

    CamAcq camAcq;
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
    // SerialPortConnection

    public void setup()
      {
        camAcq = CamAcq.getInstance();
        instrumentCtrl = (InstrumentController) CamAcqJ.getInstance().getController();
        dataModel = (DataModel) camAcq.getModel("data");
        cameraModel = (CameraModel) camAcq.getModel("camera");
        //liveDisplay = (DisplayLiveCamera) camAcq.getDisplay();
        displayModel = (DisplayModel) camAcq.getModel("display");
        acqModel = (AcqModel) camAcq.getModel("acq");
        acqCtrl = (AcquisitionController) camAcq.getController("acq");
        illumModel = (IllumModel) camAcq.getModel("illum");
        vlcModel = (VariLCModel) camAcq.getModel("variLC");
        vlcCtrl = (VariLCController) camAcq.getController("variLC");
        //lFController = (LFViewController) camAcq.getModel("");
        stageXYCtrl = (StageXYController) camAcq.getController("stageXY");
        laserCtrl = (LaserController) camAcq.getController("laser");
        surgeon = (Surgeon) camAcq.getController("surgeon");
        filter = (Filter) camAcq.getModel("filter");
      }

  //  public abstract void runRoutine();


}
