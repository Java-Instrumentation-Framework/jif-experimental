package edu.mbl.jif;

import ij.IJ;
import ij.ImagePlus;

/**
 * Example Acquisition Routine
 * @author GBH
 */
public class AcqRoutine1 extends AbstractAcqRoutine {

  public AcqRoutine1() {
    setup();
  }


  public void runRoutine() {

    System.out.println("Acquiring Stack...");
    //camAcq.displayOpen();
    //camAcq.setExposureAcq(10);

    int nSlices = 5;
    ImagePlus imgPlus = null;
    if (camAcq.getDepth() == 8) {
      imgPlus = camAcq.newCaptureStackByte(7);
    } else {
      imgPlus = camAcq.newCaptureStackShort(7);
    }

    camAcq.setImagePlusForCapture(imgPlus); // sets default stack for capture

    // temporary... set for background multiframe setting
    int multiFrameWas = 1;
    camAcq.startAcq(true);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    for (int i = 0; i < nSlices; i++) {
      camAcq.captureImageToSlice(imgPlus, i + 2);
    }
    camAcq.captureStackFinish();
    IJ.save(imgPlus, camAcq.getImageDirectory() + "\\" + camAcq.makeFilename() + ".tif");
    camAcq.displayResume();

//        // Set expos 10 msec
//        setExposure(10);
//        // Switch halogen On
//        shutterXmis(true);
//        // Acq 1 frame
//        acq1Frame();
//        // Switch halogen Off
//        shutterXmis(false);
//        // Set expos 20 msec
//        setExposure(20);
//        // Open EpiShutter
//        shutterEpi(true);
//        // Take PolStack-5Frame
//        acqPolStack5Frame();
//        filter.setPosition(2);
//        // Close Epi Shutter
//        shutterEpi(false);
  }

  public void setExposure(int exp) {
    cameraModel.setExposureAcq((double) exp);
  }

  public void shutterXmis(boolean on) {
    // ?? illum.setLevelXmis(0);
    illumModel.setOpenXmis(on);
  }

  public void shutterEpi(boolean open) {
    illumModel.setOpenEpi(open);
  }

  public void acq1Frame() {
    camAcq.captureToImagePlus();
  }

  public void acqPolStack5Frame() {
    // assumes that VariLC settings 0-4 are defined
    }

  public void runTest() {
    cameraModel.setExposureStream(100);
  // open serial port
  }

  public static void main(String[] args) {
    AcqRoutine1 aar = new AcqRoutine1();
    aar.runRoutine();
  }
}
