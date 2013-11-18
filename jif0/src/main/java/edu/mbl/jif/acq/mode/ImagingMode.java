package edu.mbl.jif.acq.mode;

import edu.mbl.jif.acq.*;
import edu.mbl.jif.utils.*;
import edu.mbl.jif.utils.prefs.Prefs;
import java.io.IOException;

import java.util.*;

import com.jgoodies.binding.beans.Model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import edu.mbl.jif.microscope.illum.Shutter.ShutterType;

/**
 * <p>Title: </p>
 * <p>Description: Imaging and Acquisition Mode</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
/** @todo
 * firePropertyChange
 * Add PropChangeListeners...
 * */
public class ImagingMode extends Model implements ImagingModeInterface {

  DisplayMode displayMode;
  AcquisitionMode acqMode;
  ShutterType shutterType;

  public static final String PROPERTYNAME_MODESTR = "modeStr";
  public static final String PROPERTYNAME_ENABLED = "enabled";

  private String modeName = "";
  private boolean enabled = false;
  private int exposure = -1;
  private int gain = -1;
  private int shutter;
  private int numberExposures;
  private boolean viewImmediately;
  private boolean addDataStripe;

  public ImagingMode(String modeStr) {
    this.modeName = modeStr;
  //exposure        = Prefs.usr.getI
  }

  @Override
  public String getName() {
    return modeName;
  }

  @Override
  public String getNameShort() {
    return modeName;
  }

  @Override
  public String getIcon() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  // called at beginning/end of Acquisition

  public void initialize() {
  }

  public void terminate() {
  }

  // called at beginning/end of Time Series
  public void beginSeries() {
    // openFile()
    }

  public void endSeries() {
  }

  // called at beginning/end of Z-Scan
  public void beginScan() {
  }

  public void endScan() {
  }

  // called at beginning/end of image/stack
  public void enter() {
  }

  public void exit() {
  }

  // Acquire image/stack and process it
  public void acquire() {
  }

  public void process() {
  }

  // + public void process (SeriesDisplay display) {}
  public void postProcess() {
  }

  // Storage

  // fileTypeKey "FLU"
  // fileNameScheme
  // rawFilenameScheme
  // rawFilePath
  public void saveToRawFile() {
  }

  public void saveToFile() {
  }

  public void load() {
  }

  // move(), copy(), delete()

  // Background Correction
  public void acquireBkgd() {
  }

  public void acquireBkgdScan() {
  }

  // Metadata
  // acquistion record
  //
  void setExposure(int exp) {
    exposure = exp;
    Prefs.usr.putInt(modeName + ".exposure", exp);
  }

  /**
   * Sets the addDataStripe property for this <code>Mode</code> object to the given parameter.
   * @param addDataStripe an addDataStripe
   * @see #isAddDataStripe
   */    //   public void set_aVariable(boolean addDataStripe) {
  //      boolean oldValue = is_X_();
  //      this.addDataStripe = addDataStripe;
  //      firePropertyChange("addDataStripe", oldValue, addDataStripe);
  //   }
  //
  /**
   * save
   */
  public void saveSettings() {
    XStream xstream = new XStream(new DomDriver());
    String xml = xstream.toXML(this);
    System.out.println(xml);
    try {
      FileUtil.saveTxtFile(modeName + ".xml", xml, false);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void loadSettings() {
    try {
      XStream xstream = new XStream(new DomDriver());
      String xml = FileUtil.readTxtFile(modeName + ".xml");
      ImagingMode tBean2 = (ImagingMode) xstream.fromXML(xml);
    } catch (IOException ex1) {
      ex1.printStackTrace();
    }
  }

  //   private ExtendedPropertyChangeSupport changeSupport = new
  //         ExtendedPropertyChangeSupport(this);
  //
  //   public void addPropertyChangeListener (PropertyChangeListener x) {
  //      changeSupport.addPropertyChangeListener(x);
  //   }
  //
  //   public void removePropertyChangeListener (PropertyChangeListener x) {
  //      changeSupport.removePropertyChangeListener(x);
  //   }
  public static void main(String[] args) { // for Testing...
    ImagingMode x = new ImagingMode("modeX");

    //x.save();
    ImagingMode x2 = new ImagingMode("modeX");
    x2.load();
  }
}
