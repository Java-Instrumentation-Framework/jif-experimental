package edu.mbl.jif.camera.display;

import edu.mbl.jif.gui.imaging.event.RoiChangeEvent;
import edu.mbl.jif.gui.imaging.event.RoiChangeListener;

public class NextFrameWatcher implements RoiChangeListener {

  DisplayLiveCamera dlc;
  boolean frameArrived = false;

  public NextFrameWatcher(DisplayLiveCamera dlc) {
    this.dlc = dlc;
    dlc.getImageDisplayPanel().addRoiChangeListener(this);
  }

  @Override
  public void roiChanged(RoiChangeEvent evnt) {
    frameArrived = true;
  }

  public void waitForNextFrame() {
    frameArrived = false;
    while (!frameArrived) {
    }

  }
}