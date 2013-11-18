/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.mode;

import edu.mbl.jif.acq.*;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.StreamingVideoRecorder;
import edu.mbl.jif.camera.Utils;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.microscope.illum.IllumModel;

/**
 *  .... from AcquisitionController ...
 * @author GBH
 */
public class AbstractAcqMode {

  InstrumentController instrumentCtrl;
  //CameraInterface cam;
  CameraModel cameraModel;
  AcqModel acqModel;
  ImageAcquisitioner acq;
  DataModel dataModel;
  String startTime = null;
  long acqTime;
  boolean doSequence = false;
  SequenceController seqCtrl;
  private boolean doZseries;
  boolean recording = false;
  StreamingVideoRecorder vidRecA = null;
  IllumModel illum;
  boolean wasEpiShutterOpen;

  /** Creates a new instance of AcquisitionController */
  public AbstractAcqMode(InstrumentController instrumentCtrl) {
    this.instrumentCtrl = instrumentCtrl;
    this.acqModel = (AcqModel) instrumentCtrl.getModel("acq");
    if (acqModel == null) {
      Application.getInstance().error("acqModel = null");
    }
    this.cameraModel = (CameraModel) instrumentCtrl.getModel("camera");
    this.dataModel = (DataModel) instrumentCtrl.getModel("data");
    this.illum = (IllumModel) instrumentCtrl.getModel("illum");
  }
  // <editor-fold defaultstate="collapsed" desc="<<< Acquistion Start/Stop >>>">
  // Single image acquisition - start/finish are done for you.

  public void captureSingleImage(Object imageArray) {
    start();
    acquireImage(imageArray);
    finish();
    //Utils.log("Image acquired at " + startTime);
  }

  // For a fast series acquisition, use:
  //   start();
  //   loop doing {
  //     acquireImage();
  //   }
  //   finish();
  //
  public void start() {
    start(false);
  }

  public void start(boolean flushFirst) {
    if (cameraModel == null) {
      System.err.println("Attempted to start Acq without camera.");
      return;
    }
    displaySuspend();
    // Enter AcqMode(X)
    if (illum != null) {
      wasEpiShutterOpen = illum.isOpenEpi();
      if (!wasEpiShutterOpen) {
        illum.setOpenEpi(true);
      }
    }
    // @todo what of XMission Shutter ???

    if (!cameraModel.isSameSetAcqStream()) {
      cameraModel.setExposureAcq(cameraModel.getExposureAcq());
      cameraModel.setGainAcq(cameraModel.getGainAcq());
    }
    String type = cameraModel.getCameraType();

    if (type.equalsIgnoreCase(CameraModel.TYPE_MOCK)) {
      acq = new Acquisitioner_Mock(cameraModel.getCamera());
    } else if (type.equalsIgnoreCase(CameraModel.TYPE_QCAM)) {
      acq = new Acquisitioner_Q(cameraModel.getCamera());
    } else if (type.equalsIgnoreCase(CameraModel.TYPE_LUCAM)) {
      acq = new Acquisitioner_Lu(cameraModel.getCamera());
    } else {
      return;
    }

    acq.setDepth(acqModel.getDepth());
    //
    acq.setMultiFrame(acqModel.getMultiFrame(), acqModel.isDiv());
    //
    acq.setMirrorImage(acqModel.isMirrorImage());
    if (flushFirst) {
      acq.start(true);
    } else {
      acq.start();
    }
    startTime = Utils.timeStamp();

    //  if (acqModel.getDepth() == 8) {
    //   } else {
    //     acq.setMultiFrame(Prefs.usr.getInt("framesToAvg", 1), true); // must div 8-bit
    //   }
    //System.out.println("started.");
    return;
  }

  public long acquireImage(Object imageArray) {
    long acqTime = -1;
    if (acq != null) {
      acqTime = acq.acquireImage(imageArray);
    } else {
      System.err.println("Attempted to acquireImage() without start()");
    }
    //System.out.println("acquireImage() done.");
    this.acqTime = acqTime;
    return acqTime;
  }

  public void finish() {
    if (acq != null) {
      acq.finish();
      acq = null;
    //System.out.println("finished.");
    }
    if (illum != null) {
      if (!wasEpiShutterOpen) {
        illum.setOpenEpi(false);
      }
    }
    displayResume();
  }

  public void displaySuspend() {
    DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
    if (disp != null) {
      disp.suspend();
    }
  }

  public void displayResume() {
    DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
    if (disp != null) {
      disp.restart();
    }
  }
}
