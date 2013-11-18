package edu.mbl.jif.acq.mode;

import edu.mbl.jif.acq.AcqModel;
import edu.mbl.jif.camera.CameraModel;

/**
 *
 * @author GBH
 */
public class TestPropSet extends PropertySet {

  public TestPropSet() {
    addProp("acqDepth", acqModel, AcqModel.PROPERTYNAME_DEPTH);
    addProp("camExposure", cameraModel, CameraModel.PROPERTYNAME_EXPOSUREACQ);
  }

  public void testRead() {
    read();
    dump();
  }
  public void testWrite() {
    dump();
    write();
  }
}
