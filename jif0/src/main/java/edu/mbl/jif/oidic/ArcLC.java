package edu.mbl.jif.oidic;

import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.FileUtil;
import edu.mbl.jif.gui.imaging.IntensityWatcher;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.gui.dialog.DialogBox;
import edu.mbl.jif.io.xml.ObjectStoreXML;

import edu.mbl.jif.varilc.Measurer;
import edu.mbl.jif.varilc.camacq.VariLCController;
import edu.mbl.jif.varilc.VariLCSettings;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ArcLC implements ArcLCController {

  DotNetBridge dotNetBridge;
  InstrumentController instrumentCtrl;

  public ArcLC(InstrumentController instrumentCtrl) {
    this.instrumentCtrl = instrumentCtrl;
    dotNetBridge = new DotNetBridge();
    System.out.println("Connected to ArcLC");
  //String getString = getSerialNumber();
  // System.out.println("GetSerialNumber() returned:   " + getString);
  }

	@Override
	public boolean setupVLC() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
  public String getSerialNumber() {
    return dotNetBridge.GetSerialNumber();
  }

  @Override
  public boolean setDACVoltageA(double volts) {
    return setDACVoltage(volts, (byte) 0);
  }

  @Override
  public boolean setDACVoltageB(double volts) {
    return setDACVoltage(volts, (byte) 1);
  }

  public boolean setDACVoltage(double volts, byte dac) {
    System.out.println("SetVoltage: " + volts + " on DAC " + dac);
    return dotNetBridge.SetDACVoltage(volts, dac);
  }

  // <editor-fold defaultstate="collapsed" desc=">>>--- Define & Set  ---<<<" >
  public int setRetardance(double retA, double retB) {
    setDACVoltageA(retA);
    setDACVoltageB(retB);
    return 0;
  }

  public int setRetardance(float retA, float retB) {
    return setRetardance((double) retA, (double) retB);
  }

  /**
   * Sets default values for Extinction to the current value of A0 and B0
   */
  public void setExtinctionDefaultsToCurrent() {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    vlcModel.setExtinctA(vlcModel.getA0());
    vlcModel.setExtinctB(vlcModel.getB0());
  }

  public void setRetardersToNominalSwing() {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    double extA = vlcModel.getExtinctA();
    double extB = vlcModel.getExtinctB();
    double LC_Swing = vlcModel.getSwing();
    vlcModel.setA0(extA);
    vlcModel.setB0(extB);
    vlcModel.setA1(extA + LC_Swing);
    vlcModel.setB1(extB);
    vlcModel.setA2(extA);
    vlcModel.setB2(extB + LC_Swing);
    vlcModel.setA3(extA);
    vlcModel.setB3(extB - LC_Swing);
    vlcModel.setA4(extA - LC_Swing);
    vlcModel.setB4(extB);
    transmitAllElements();
  //vlcRT.getDefinedElements();
  //vlcRT.showElements();
  }

  public void transmitAllElements() {
    defineElement(0);
    defineElement(1);
    defineElement(2);
    defineElement(3);
    defineElement(4);
  }

  public void defineElementAndMeasure(int element) {
    defineElement(element);
  //this.measureSetting(element);
  }

  public void defineElement(int element) {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    switch (element) {
      case 0:
        setRetardance((float) vlcModel.getA0(),
                (float) vlcModel.getB0());
        break;
      case 1:
        setRetardance((float) vlcModel.getA1(),
                (float) vlcModel.getB1());
        break;
      case 2:
        setRetardance((float) vlcModel.getA2(),
                (float) vlcModel.getB2());
        break;
      case 3:
        setRetardance((float) vlcModel.getA3(),
                (float) vlcModel.getB3());
        break;
      case 4:
        setRetardance((float) vlcModel.getA4(),
                (float) vlcModel.getB4());
        break;
      case 5:
        setRetardance((float) vlcModel.getA5(),
                (float) vlcModel.getB5());
        break;
      default:
        ;
    }
  //vlcRT.defineElement(element);

  //TimerHR.waitMillisecs(defineLatency);
  //TimerHR.waitMillisecs(getSettlingTime());
  }

  public void reset() {
    //vlcRT.reset();
  }

  public void statusCheck() {
//    if (vlcRT != null) {
//      if (vlcRT.statusCheck()) {
//        Application.getInstance().error("VariLC reports an error.  (Do a reset)");
//      }
//    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc=">>>--- Run Calibration ---<<<" >
  public void runCalibration() {
    // if (setupForMeasurements()) {
    // Check for ROI set... if not, use default
    Rectangle roi = CamAcq.getInstance().getSelectedROI();
    if (roi.getWidth() == 0) {
      int w = CamAcq.getInstance().getWidth();
      int h = CamAcq.getInstance().getHeight();
      roi = new Rectangle(w / 2, h / 2, 32, 32);
      CamAcq.getInstance().setSelectedROI(roi);
    // May have an EDT issue here...
    }
//    acqCtrl.displaySuspend();
//    CalibratorNew calib = new CalibratorNew(this, vlcModel, roi, instrumentCtrl);
//    calib.doCalibration();
  //   }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< Measurement >>>-----------------------">
    /*
   * ### This was an experiment to do calib while streaming... using IntensityWatcher
   */
  public void measureAll() {
    if (setupForMeasurements()) {
      System.out.println("runningTest");
      (new MeasureAllTask()).execute();
    }
  }

  public boolean setupForMeasurements() {
    DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
    if (dlc == null) {
      //DialogBox.boxNotify("Note","Display must be open for VariLC measurements");
      return false;
    }
    if (dlc.getSelectedROI().getX() == 0) {
      //DialogBox.boxNotify("Note", "No ROI selected for measurement");
      return false;
    }

//        if (iWatcher == null) {
//            iWatcher = new IntensityWatcher(); //(1, "./intensity.csv");
//            dlc.getImageDisplayPanel().addRoiChangeListener(iWatcher);
//            iWatcher.setStabilityLevel((float) vlcModel.getStabilityLevel());
//            iWatcher.setMeasurementFreq(1);
//            measurer = new Measurer(this, iWatcher);
//        }
    //System.out.println("setupForMeasurements complete");
    return true;
  }

  public boolean testMeasurementStreaming() {

    testMeasurementStreaming2(this);
    return true;
  }

  public boolean testMeasurementStreaming2(final ArcLC vlc) {
    Runnable runnable = new Runnable() {

      public void run() {
        DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
        if (dlc == null) {
          //iWatcher = null;
          DialogBox.boxNotify("Note",
                  "Display must be open for VariLC measurements");
          return;
        }
        float intensity = getStableIntensity(0, dlc);
        System.out.println("Result of Measurement: " + intensity);
      }
    };
    (new Thread(runnable)).start();
    return true;
  }

  public float getStableIntensity(int setting, DisplayLiveCamera dlc) {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    IntensityWatcher iWatcher = new IntensityWatcher();
    dlc.getImageDisplayPanel().addRoiChangeListener(iWatcher);
    iWatcher.setStabilityLevel((float) vlcModel.getStabilityLevel());
    iWatcher.setMeasurementFreq(1);
    Measurer measurer = new Measurer(this, iWatcher);
    //measurer.setTimeOut((int)getTimeToStable());
    measurer.setTimeOut(1000);
    Measurer.MeasurementTask mTask = measurer.doCommand(setting);
    float intensity = measurer.waitFor(mTask);
    return intensity;
  }

  public long getTimeToStable() {
    CameraModel cam = (CameraModel) ((InstrumentController) CamAcqJ.getInstance().getController()).getModel("camera");
    if (cam == null) {
      System.err.println("cameraModel null");
      return -1;
    }
    long exp = (long) cam.getExposureStream();
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    long maxTimeToStable = (2 * vlcModel.getSettleTime() + exp);
    return maxTimeToStable;
  }

//  public float getMean() {
//    if (iWatcher != null) {
//      long maxTimeToStable = getTimeToStable();
//      try {
////            System.out.println("maxTimeToStable: " + maxTimeToStable);
//        Thread.sleep(maxTimeToStable);
//      } catch (InterruptedException ex) {
//        ex.printStackTrace();
//      }
//      float mean = iWatcher.getMean();
//      //float mean = iWatcher.getStableMean(maxTimeToStable);
//      //long time = iWatcher.getTimeToStable();
//      //System.out.println("mean: " + mean + "  time: " + time);
//      return mean;
//    }
//    return -1.0f;
//  }
  public void selectElement(int n) {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    switch (n) {
      case 0:
        this.setRetardance(vlcModel.getA0(), vlcModel.getB0());
        break;
      case 1:
        this.setRetardance(vlcModel.getA1(), vlcModel.getB1());
        break;
      case 2:
        this.setRetardance(vlcModel.getA2(), vlcModel.getB2());
        break;
      case 3:
        this.setRetardance(vlcModel.getA3(), vlcModel.getB3());
        break;
      case 4:
        this.setRetardance(vlcModel.getA4(), vlcModel.getB4());
        break;
      case 5:
        this.setRetardance(vlcModel.getA5(), vlcModel.getB5());
        break;
    }

  }

  public void selectElementWait(int setting) {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    vlcModel.setSelectedSetting(setting);
    selectElement(setting);
  }

  // Called from CompensatorSettingPanel when button is pushed for a setting
  public void setElementAndMeasure(final int setting) {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    vlcModel.setSelectedSetting(setting);
    if (setupForMeasurements()) {
      MeasureTask measureTask = new MeasureTask(setting, true);
      measureTask.execute();
    } else {
      selectElement(setting);
    }
  }

  public void measureSetting(final int setting) {
    if (setupForMeasurements()) {
      MeasureTask measureTask = new MeasureTask(setting, false);
      measureTask.execute();
    }
  }

  public float calcExtinctionRatio() {
    int zeroIntensity = 5;
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    // @todo hook this to actual zeroIntensity
    // Math.round(Prefs.usr.getFloat("acq_zeroIntensity", 6));
    double intensityRatio = (vlcModel.getIntensity1() - zeroIntensity) /
            (vlcModel.getIntensity0() - zeroIntensity);
    float extinctionRatio =
            (float) (intensityRatio / Math.pow(Math.tan(vlcModel.getSwing() * Math.PI), 2));
    return extinctionRatio;
  }

  public double checkVariance() {
    ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
    double[] intensities = {vlcModel.getIntensity1(), vlcModel.getIntensity2(),
      vlcModel.getIntensity3(), vlcModel.getIntensity4()};
    return stdDev(intensities);
  }

//  public double acquireAndMeasure(Rectangle roi, boolean isDisplayLastAcquired) {
//    double measured = 0;
//    //try {
//    //System.out.println("acquireImageAndMeasure");
//    //Thread.currentThread().sleep(this.vlcModel.getSettleTime());
//    byte[] data = acqCtrl.acquireSampleImage();
//    //this.acqCtrl.acquireImage(data);
//    // test
//    if (isDisplayLastAcquired) {
//      BufferedImage img = acqCtrl.byteArrayToBufferedImage(data);
//      DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
//      if (disp != null) {
//        disp.getImageDisplayPanel().showImage(img);
//      }
//    }
//    // measure intensity at ROIs
//    ImageStatistics iStats = ImageAnalyzer.getStatsOnlyROI(
//            data,
//            roi,
//            new Dimension(acqCtrl.getImageWidth(),
//            acqCtrl.getImageHeight()));
//    measured = iStats.meanInROI;
////        } catch (InterruptedException ex) {
////            Logger.getLogger(CalibratorNew.class.getName()).log(Level.SEVERE,
////                null,
////                ex);
////        }
//    return measured;
//  }
  @Override
  public boolean initializeController() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isInitialized() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="<<< MeasureAllTask SwingWorker >>>">
  //
  // SettingMeasurement just holds the setting number along with the measured intensity
  class SettingMeasurement {

    final int setting;
    final float measurement;

    SettingMeasurement(int setting, float measurement) {
      this.setting = setting;
      this.measurement = measurement;
    }
  }

  // Measure intensity for all settings
  class MeasureAllTask extends SwingWorker<Void, SettingMeasurement> {

    @Override
    protected Void doInBackground() {
      DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
      if (dlc == null) {
        //iWatcher = null;
        DialogBox.boxNotify("Note",
                "Display must be open for VariLC measurements");
        return null;
      }
//      Rectangle roi = CamAcq.getInstance().getSelectedROI();
//      if (roi.getWidth() == 0) {
//        int w = CamAcq.getInstance().getWidth();
//        int h = CamAcq.getInstance().getHeight();
//        roi = new Rectangle(w / 2, h / 2, 32, 32);
//      }
      for (int i = 0; i < 5; i++) {
        float intensity;
        intensity = getStableIntensity(i, dlc);
        publish(new SettingMeasurement(i, intensity));
      }
      return null;
    }

    @Override
    protected void process(List<SettingMeasurement> measurements) {
      ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
      for (SettingMeasurement measure : measurements) {
        int setting = measure.setting;
        float mean = measure.measurement;
        switch (setting) {
          case 0:
            vlcModel.setIntensity0(mean);
            break;
          case 1:
            vlcModel.setIntensity1(mean);
            break;
          case 2:
            vlcModel.setIntensity2(mean);
            break;
          case 3:
            vlcModel.setIntensity3(mean);
            break;
          case 4:
            vlcModel.setIntensity4(mean);
            break;
          default:
            ;
        }
      }
    }

    @Override
    protected void done() {
      double stdDev = checkVariance();
      float extinct = calcExtinctionRatio();
      ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
      vlcModel.setExtinctionRatio(extinct);
      DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
      if (disp != null) {
        disp.restart();
      }
    }
  }

  // Measure intensity for a single setting
  // with or without selecting the setting first (selectFirst)
  // (i.e. it is already set to the desired setting (as in having just been defined))
  class MeasureTask
          extends SwingWorker<Void, SettingMeasurement> {

    private int setting;
    private boolean selectFirst;

    public MeasureTask(int setting, boolean selectFirst) {
      this.setting = setting;
      this.selectFirst = selectFirst;
    }

    @Override
    protected Void doInBackground() {
      DisplayLiveCamera dlc = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
      if (dlc == null) {
        //iWatcher = null;
        DialogBox.boxNotify("Note",
                "Display must be open for VariLC measurements");
        return null;
      }

      Rectangle roi = CamAcq.getInstance().getSelectedROI();
      if (roi.getWidth() == 0) {
        int w = CamAcq.getInstance().getWidth();
        int h = CamAcq.getInstance().getHeight();
        roi = new Rectangle(w / 2, h / 2, 32, 32);
      }
      float intensity;
      intensity = getStableIntensity(setting, dlc);
      publish(new SettingMeasurement(setting, intensity));
      return null;
    }

    @Override
    protected void process(List<SettingMeasurement> measurements) {
      ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
      for (SettingMeasurement measure : measurements) {
        int setting = measure.setting;
        float mean = measure.measurement;
        switch (setting) {
          case 0:
            vlcModel.setIntensity0(mean);
            break;
          case 1:
            vlcModel.setIntensity1(mean);
            break;
          case 2:
            vlcModel.setIntensity2(mean);
            break;
          case 3:
            vlcModel.setIntensity3(mean);
            break;
          case 4:
            vlcModel.setIntensity4(mean);
            break;
          default:
            ;
        }
      }
    }

    @Override
    protected void done() {
    }
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc=" Save/Load Settings ">
  public void saveVLCSettings() {
    String filename = chooseFileSave();
    if (filename != null) {
      filename = FileUtil.getJustPath(filename) + "\\" + FileUtil.getJustFilenameNoExt(filename) + ".xml";
      if (new File(filename).exists()) {
        if (!DialogBox.boxConfirm("File Exists", "Over-write file?")) {
          return;
        }
      }
      try {
        ArcLCSettings variLCSettings = new ArcLCSettings();
        ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
        variLCSettings.getCurrent(vlcModel);
        //String path = Files.path(dataDir, "vlcSetting.xml");
        ObjectStoreXML.write(variLCSettings, filename);
      } catch (Throwable ex) {
        Logger.getLogger(VariLCController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public String chooseFileSave() {
    JFileChooser fc = new JFileChooser();
    fc.setName("Save VariLC Settings");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
    // Default to Image data dir.
    DataModel data = (DataModel) instrumentCtrl.getModel("data");
    String dataDir = data.getImageDirectory();
    fc.setCurrentDirectory(new File(dataDir));
    fc.setFileFilter(filter);
    int option = fc.showSaveDialog(null);
    if (JFileChooser.APPROVE_OPTION == option) {
      return fc.getSelectedFile().getAbsolutePath();
    }
    return null;
  }

  public void loadVLCSettings() {
    String filename = chooseFileLoad();
    if (filename != null) {
      try {
        ArcLCSettings variLCSettings;
        variLCSettings = (ArcLCSettings) ObjectStoreXML.read(filename);
        ArcLCModel vlcModel = (ArcLCModel) instrumentCtrl.getModel("arcLC");
        variLCSettings.setCurrent(vlcModel);
        // transmitAllElements();
      } catch (Throwable ex) {
        Logger.getLogger(VariLCController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public String chooseFileLoad() {
    JFileChooser fc = new JFileChooser();
    fc.setName("Load VariLC Settings");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
    // Default to Image data dir.
    DataModel data = (DataModel) instrumentCtrl.getModel("data");
    String dataDir = data.getImageDirectory();
    fc.setCurrentDirectory(new File(dataDir));
    fc.setFileFilter(filter);
    int option = fc.showOpenDialog(null);
    if (JFileChooser.APPROVE_OPTION == option) {
      return fc.getSelectedFile().getAbsolutePath();
    }
    return null;
  }

// </editor-fold>
  // <editor-fold defaultstate="collapsed" desc=">>>--- Statistical functions ---<<<" >
  public static double stdDev(double[] data) {
    return Math.sqrt(variance(data));
  }

  public static double variance(double[] data) {
    final int n = data.length;
    if (n < 2) {
      return Double.NaN;
    }
    double avg = data[0];
    double sum = 0;
    for (int i = 1; i < data.length; i++) {
      double newavg = avg + ((data[i] - avg) / (i + 1));
      sum += ((data[i] - avg) * (data[i] - newavg));
      avg = newavg;
    }
    return sum / (double) (n - 1);
  }
  // </editor-fold>

  public void test() {
    //acqCtrl.testMeasurement();
  }
} 