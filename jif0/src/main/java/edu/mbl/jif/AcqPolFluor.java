package edu.mbl.jif;

import edu.mbl.jif.camacq.CamAcq;

/**
 * Example Acquisition Routine
 * @author GBH
 */
public class AcqPolFluor extends AbstractAcqRoutine {

  public AcqPolFluor() {
    camAcq = CamAcq.getInstance();
  }


  public void runRoutine() {
    // Set expos 10 msec
    setExposure(10);
    // Switch halogen On
    shutterXmis(true);
    // Acq 1 frame
    acq1Frame();
    // Switch halogen Off
    shutterXmis(false);
    // Set expos 20 msec
    setExposure(20);
    // Open EpiShutter
    shutterEpi(true);
    // Take PolStack-5Frame
    acqPolStack5Frame();
    filter.setPosition(2);
    // Close Epi Shutter
    shutterEpi(false);
  }

  public void setExposure(int exp) {
    cameraModel.setExposureAcq((double) exp);
  }

  public void shutterXmis(boolean open) {
    // ?? illum.setLevelXmis(0);
    illumModel.setOpenXmis(open);
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
}
