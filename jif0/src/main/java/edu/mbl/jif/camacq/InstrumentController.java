
/*
 * Controller.java
 *
 * Created on August 5, 2006, 4:37 PM
 */
package edu.mbl.jif.camacq;

import edu.mbl.jif.varilc.camacq.VariLCController;
import edu.mbl.jif.camera.PanelCamera;
import edu.mbl.jif.laser.PanelSurgeon;
import edu.mbl.jif.data.PanelData;
import edu.mbl.jif.varilc.PanelVLC;
import edu.mbl.jif.data.DataModel;
import edu.mbl.jif.acq.PanelSequence;
import edu.mbl.jif.acq.PanelAcq;
import edu.mbl.jif.camera.display.PanelDisplaySettings;
import edu.mbl.jif.config.PanelSys;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import edu.mbl.jif.MockProxyStub;
import edu.mbl.jif.acq.AcqModel;
//import edu.mbl.jif.acq.AcqPresentation;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.acq.mode.AcqModelPS;
import edu.mbl.jif.acq.mode.TestPropSet;
import edu.mbl.jif.camacq.InitProgressFrame.InitializationTask;
import edu.mbl.jif.camera.CameraInterface;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camera.FileUtil;
import edu.mbl.jif.camera.MockCamera;
import edu.mbl.jif.camera.PropertySheetDisplay;
import edu.mbl.jif.camacq.actions.DisplayOpenAction;
import edu.mbl.jif.camacq.actions.HelpAction;
import edu.mbl.jif.camacq.actions.IDMAction;
import edu.mbl.jif.camacq.actions.ImageJAction;
import edu.mbl.jif.camacq.actions.LFDisplayOpenAction;
import edu.mbl.jif.camera.display.DisplayModel;
import edu.mbl.jif.config.DeviceManager;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.spatial.PanelControlXY;
import edu.mbl.jif.laser.BurstGenerator;
import edu.mbl.jif.laser.LaserController;
import edu.mbl.jif.laser.LaserControllerBurst;
import edu.mbl.jif.lightfield.LFModel;
import edu.mbl.jif.lightfield.LFPresentation;
import edu.mbl.jif.lightfield.LFViewController;
import edu.mbl.jif.lightfield.PanelLF;
import edu.mbl.jif.microscope.PanelIllum;
import edu.mbl.jif.microscope.illum.IllumModel;
import edu.mbl.jif.laser.PanelLaser;
import edu.mbl.jif.laser.PanelPath;
import edu.mbl.jif.stage.PanelXYStage;
import edu.mbl.jif.stage.StageXYController;
import edu.mbl.jif.stage.StageXY_Mock;
import edu.mbl.jif.laser.Surgeon;
import edu.mbl.jif.laser.SurgeonModel;
import edu.mbl.jif.microscope.Filter;
import edu.mbl.jif.microscope.PanelFilter;
import edu.mbl.jif.microscope.ZeissAxiovert200M;
import edu.mbl.jif.microscope.illum.Shutter;
import edu.mbl.jif.microscope.illum.ShutterControllerUniblitz_1;
import edu.mbl.jif.microscope.illum.ShutterControllerUniblitz_2;
import edu.mbl.jif.oidic.ArcLC;
import edu.mbl.jif.oidic.ArcLCController;
import edu.mbl.jif.oidic.ArcLCModel;
import edu.mbl.jif.oidic.PanelArcLCCalibration;
import edu.mbl.jif.oidic.OiDicAcquistion;
import edu.mbl.jif.ps.PSAcquisitionController;
import edu.mbl.jif.stage.PanelStageZ;
import edu.mbl.jif.stage.ZStageController;
import edu.mbl.jif.stage.StageZModel;
import edu.mbl.jif.stage.piC865.StageXYController_C865;
import edu.mbl.jif.varilc.*;
import edu.mbl.jif.workframe.AbstractApplicationController;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

/**
 *
 * @author GBH
 */
public class InstrumentController
        extends AbstractApplicationController {

  ApplicationFrame frame;

  /*
   * Adding an IModule
   * addModule(new TestModule());
   *
  Adding a Model for binding:
   *
  xModel = new XModel(this);
  xPresentation = new XPresentation(acqModel);
  xCtrl = new XController(this);
  models.put("x", xModel);
  //presentations.put("x", xPresentation);
  controllers.put("x", xCtrl);
   */    // DataModel dataModel;
  // DataPresentation dataPresentation;
  CameraInterface camera;
  CameraModel cameraModel;
  //CameraPresentation cameraPresentation;
  DisplayLiveCamera liveDisplay;
  DisplayModel displayModel;
  AcqModel acqModel;
  // AcqPresentation acqPresentation;
  AcquisitionController acqCtrl;
  VariLCModel vlcModel;
  VLCController vlcCtrl;
  //VariLCPresentation vlcp;
  PSAcquisitionController pSAcquisitionController;
  // LiveViewModel     liveModel;
  // Lightfield
  LFModel lFModel;
  public LFPresentation lFPresentation;
  LFViewController lFController;
  StageXYController stageXYCtrl;
  LaserController laserCtrl;
  int offsetFrame = 0;
  Preferences appPrefs;
  public TestPropSet tp;
  boolean isCapableZseries = false;

  public InstrumentController() {
  }

  /* Use: Add this header/constructor argument in sub-controllers / models
   *
  InstrumentController instrumentCtrl;
  public SomeThing(InstrumentController instrumentCtrl) {
  this.instrumentCtrl = instrumentCtrl;

  // then
  someCtrl = (SomeController)instrumentCtrl.getController("some");
  someModel = (SomeModel)instrumentCtrl.getModel("some");
   *
   */
  public InstrumentController(ApplicationFrame host) {
    frame = host;
  }
  InitializationTask initTask;

  public void initialize(InitializationTask initTask) {
    this.initTask = initTask;
    appPrefs = Application.getInstance().getPreferences();
    initTask.updateProgress("Creating controllers...");
    initializeDevices();
    createControllers();
    //initTask.updateProgress("Creating actions...");


    // NEW module using jif.workframe ====================
//        IModule testModule = new TestModule(this);
//        addModule(testModule);
    //====================================================

//        // entryChangeListener = new PropertyChangeHandler();
//        // selection = Collections.emptyList();
//        // selection = Collections.unmodifiableList(selection);

    initTask.updateProgress("Initialization complete.");
  }

  void finish() {
    initTask.updateProgress("Creating actions...");
    createActions();
    initTask.updateProgress("Creating panels...");
    createPanels();
//        try {
//            ((AppFrameVLDock) frame).loadWorkspace();
//        } catch (Exception e) {
//            //e.printStackTrace();
//        }
    frame.setup();
    Application.getInstance().setController(this);
    Application.getInstance().statusBarMessage("Initialization complete.");
  }

  private void initializeDevices() {
    if (appPrefs.getBoolean("zeissA200M.enabled", false)) {
      String zeissCommPort = appPrefs.get("zeiss.commPort", "COM4");
      ZeissAxiovert200M axiov = new ZeissAxiovert200M(this, "Axiovert200M", zeissCommPort);
      DeviceManager.INSTANCE.addDevice("ZeissAxiovert200M", axiov);
    }
  // ... add other devices ...
  }

  private void illuminationInitialize() {
    initTask.updateProgress("Initialising illumination...");
    String uniblitz2CommPort = appPrefs.get("uniblitz2.commPort", "COM11");
    String uniblitz1CommPort = appPrefs.get("uniblitz1.commPort", "COM2");
    String epiShutterType = appPrefs.get("shutter.epi.type", "ZeissAxiovert200M");
    String xmisShutterType = appPrefs.get("shutter.xmis.type", "UNIBLITZ_1");
    // Because a single instance of ZeissAxiovert200M contains multiple devices,
    // it needs to be managed...

    Shutter shutterEpi = null;
    Shutter shutterXmis = null;
    if (xmisShutterType.equalsIgnoreCase("UNIBLITZ_1")) {
      shutterXmis = new ShutterControllerUniblitz_1(this, uniblitz1CommPort);
    } else if (xmisShutterType.equalsIgnoreCase("UNIBLITZ_2")) {
      shutterXmis = new ShutterControllerUniblitz_2(this, uniblitz2CommPort);
    } else if (xmisShutterType.equalsIgnoreCase("ZeissAxiovert200M")) {
      shutterXmis = (Shutter) DeviceManager.INSTANCE.getDevice("ZeissAxiovert200M");
    }
    if (epiShutterType.equalsIgnoreCase("UNIBLITZ_1")) {
      shutterEpi = new ShutterControllerUniblitz_1(this, uniblitz1CommPort);
    } else if (epiShutterType.equalsIgnoreCase("UNIBLITZ_2")) {
      shutterEpi = new ShutterControllerUniblitz_2(this, uniblitz2CommPort);
    } else if (epiShutterType.equalsIgnoreCase("ZeissAxiovert200M")) {
      shutterEpi = (Shutter) DeviceManager.INSTANCE.getDevice("ZeissAxiovert200M");
    }
    models.put("illum", new IllumModel(this, shutterEpi, shutterXmis));
  }

  private void stageXYInitialize(boolean testStageXY) {
    initTask.updateProgress("Initialising XY-stage...");
    try {
      if (testStageXY) {
        stageXYCtrl = new StageXY_Mock();
        controllers.put("stageXY", stageXYCtrl);
        ((StageXY_Mock) stageXYCtrl).setMockCamera((MockCamera) cameraModel.getCamera());
      } else {
        stageXYCtrl = new StageXYController_C865();
        int commPortX = appPrefs.getInt("stageXY.commPortX", 11);
        int commPortY = appPrefs.getInt("stageXY.commPortY", 12);
        stageXYCtrl.setCommPortX(commPortX);
        stageXYCtrl.setCommPortY(commPortY);
        if (stageXYCtrl.open()) {
          controllers.put("stageXY", stageXYCtrl);
        } else {
          System.err.println("StageXY failed to initialize");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void varlLCInitialize() {
    initTask.updateProgress("Initialising VariLC...");
    try {
      String variLCCommPort = appPrefs.get("variLC.commPort", "COM4");
      vlcCtrl = new VariLCController(this, "VariLC", variLCCommPort);
      // LCInterface vlcCtrl = MockProxyStub.getProxy(LCInterface.class);
      controllers.put("variLC", vlcCtrl);
      vlcModel = new VariLCModel(this);
      models.put("variLC", vlcModel);
      //vlcp = new VariLCPresentation(vlcModel);
      vlcCtrl.setupVLC();  // BAD kludge - Controller depends on Model
      pSAcquisitionController = new PSAcquisitionController(this);
      controllers.put("acqPS", pSAcquisitionController);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void arcLCInitialize() {
    // ArcOptix LC Controller
    initTask.updateProgress("Initialising ArcOptix LC...");
    try {
      ArcLCController arcLCCtrl = new ArcLC(this);
      //ArcLCController arcLCCtrl = MockProxyStub.getProxy(ArcLCController.class);
      controllers.put("arcLC", arcLCCtrl);
      ArcLCModel arcLCModel = new ArcLCModel(this);
      models.put("arcLC", arcLCModel);
      OiDicAcquistion oidicAcquisitionController = new OiDicAcquistion(this);
      controllers.put("acqOiDic", oidicAcquisitionController);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void laserInitialize() {
    initTask.updateProgress("Initialising laser...");
    String portName = appPrefs.get("laser.commPort", "COM17");
    BurstGenerator burstGen = new BurstGenerator(this, "BurstGenerator", portName);
    DeviceManager.INSTANCE.addDevice("BurstGenerator", burstGen);
    laserCtrl = new LaserControllerBurst(burstGen);
    controllers.put("laser", laserCtrl);
  }

  public void createControllers() {
    try {
      // Data
      initTask.updateProgress("Initialising data...");
      DataModel dataModel = new DataModel(this);
      models.put("data", dataModel);
      //DataPresentation dataPresentation = new DataPresentation(dataModel);
      initializeImageDir();

      // Camera
      initTask.updateProgress("Initialising camera...");
      if (appPrefs.getBoolean("camera.enabled", false)) {
        try {
          cameraModel = new CameraModel(this);
          // cameraPresentation = new CameraPresentation(cameraModel);
          models.put("camera", cameraModel);
        //presentations.put("camera", cameraPresentation);
        } catch (Exception e) {
          e.printStackTrace();
        }

        // Controls for Live Camera Display
        try {
          displayModel = new DisplayModel(this);
          //cameraPresentation = new CameraPresentation(cameraModel);
          models.put("display", displayModel);
        //presentations.put("camera", cameraPresentation);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      // Illumination
      if (appPrefs.getBoolean("illum.enabled", false)) {
        illuminationInitialize();
      }
      // FilterWheel
      if (appPrefs.getBoolean("filter.enabled", false)) {
        initTask.updateProgress("Initialising filter...");
        Filter filter = (Filter) DeviceManager.INSTANCE.getDevice("ZeissAxiovert200M");
        models.put("filter", filter);
      }

      // Acquisition
      // @todo Multi-Mode: create AcqModel for each mode.
      initTask.updateProgress("Initialising acquisition controller...");
      try {
        acqModel = new AcqModel(this);
        models.put("acq", acqModel);
        AcqModelPS acqModelPS = new AcqModelPS(this);
        models.put("acqPS", acqModelPS);
        //acqPresentation = new AcqPresentation(acqModel);
        //presentations.put("acq", acqPresentation);
        acqCtrl = new AcquisitionController(this);
        controllers.put("acq", acqCtrl);
      } catch (Exception e) {
        e.printStackTrace();
      }


      // VariLC
      if (appPrefs.getBoolean("vlc.enabled", false)) {
        varlLCInitialize();
      }
      // ArcLC
      if (appPrefs.getBoolean("arcLC.enabled", false)) {
        arcLCInitialize();
      }

      // StageXY
      boolean testStageXY = appPrefs.getBoolean("stageXY.mock", false);
      if (appPrefs.getBoolean("stageXY.enabled", false)) {
        stageXYInitialize(testStageXY);
      }

      // Laser
      if (appPrefs.getBoolean("laser.enabled", false)) {
        laserInitialize();
      }

      // Surgeon
      if (appPrefs.getBoolean("laser.enabled", false) || appPrefs.getBoolean("stageXY.enabled", false)) {
//        surgeon = new Surgeon(stageXYCtrl, laserCtrl);
//        controllers.put("surgeon", surgeon);
        SurgeonModel surgeonModel = new SurgeonModel(this);
        models.put("surgeon", surgeonModel);
      }

      // Sequence

      // ZStage
      if (appPrefs.getBoolean("stageZ.enabled", false)) {
        initTask.updateProgress("Initialising Z-stage...");
        isCapableZseries = true;
        ZStageController zStage;
        if (appPrefs.getBoolean("zeissA200M.enabled", false)) {
          zStage = (ZStageController) DeviceManager.INSTANCE.getDevice("ZeissAxiovert200M");
        } else {
          //ZStageController zStage = new MockZStage();
          zStage = MockProxyStub.getProxy(ZStageController.class);
        }
        controllers.put("zstage", zStage);
        StageZModel zStageModel = new StageZModel(this, zStage);
        models.put("zstage", zStageModel);
      }


      // LightField
      if (appPrefs.getBoolean("lightField.enabled", false)) {
        lFModel = new LFModel(this);
        models.put("lightfield", lFModel);
        //lFPresentation = new LFPresentation(lFModel);
        //presentations.put("lightfield", lFPresentation);
        lFController = new LFViewController(lFModel, this);
        controllers.put("lightfield", lFController);
      }

    //tp = new TestPropSet();
    // SLM
    // models.put("slm", new SLM_Main(this));

    // for diagnostics...
    // if(debug)
    //openPresentationModelViewer(cameraPresentation);
    //openPresentationModelViewer(acqPresentation);
    //openPresentationModelViewer(dataPresentation);
    //openPresentationModelViewer(vlcp);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void createActions() {
    if ((acqCtrl != null) && (cameraModel != null)) {
      BeanAdapter beanAdapter = new BeanAdapter(cameraModel, true);
      ValueModel camOpenValueModel = beanAdapter.getValueModel(CameraModel.PROPERTYNAME_OPEN);
      addDependentAction("Acquire Image", "acqImage",
              "/edu/mbl/jif/camera/icons/snap.gif", acqCtrl,
              "doAcqImageAction",
              KeyEvent.VK_A,
              camOpenValueModel);
      //
      addDependentActionToggle("Enable Time Sequence", "toggleSequence",
              "/edu/mbl/jif/camera/icons/timeseries2.gif", acqCtrl,
              "toggleSequence", KeyEvent.VK_S,
              camOpenValueModel);
      // set initial state of toggleSequence
      getManagedAction("toggleSequence").setSelected(false);

      addDependentAction("Do Time Sequence", "doSequence",
              "/edu/mbl/jif/camera/icons/timeseries2.gif", acqCtrl,
              "doAcqSequenceAction", KeyEvent.VK_S,
              camOpenValueModel);

      if (isCapableZseries) {
        addDependentActionToggle("Do Z-Series", "toggleZseries",
                "/edu/mbl/jif/camera/icons/acqZscan.gif", acqCtrl,
                "toggleZseries", KeyEvent.VK_Z,
                camOpenValueModel);
        getManagedAction("toggleZseries").setSelected(false);
      }

      addDependentAction("Record Video", "recordVideo",
              "/edu/mbl/jif/camera/icons/movieNew24.gif", acqCtrl,
              "recordVideo", KeyEvent.VK_V,
              camOpenValueModel);

      addDependentAction("Acquire Custom", "acqCustom",
              "/edu/mbl/jif/camera/icons/go.png", acqCtrl,
              "doAcqCustom",
              KeyEvent.VK_C, null);
      if (appPrefs.getBoolean("laser.enabled", false)) {
        addDependentAction("File Laser", "fireLaser",
                "/edu/mbl/jif/laser/icons/laser24.png",
                 (LaserController) controllers.get("laser"),
                "laserTestFire",
                KeyEvent.VK_L, null);
      }


      //         addDependentAction(
      //            "Grab a frame",
      //            "grabFromStream",
      //            "/edu/mbl/jif/camera/icons/movieNew24.gif",
      //            acqCtrl,
      //            "grabFromStream",
      //            cameraPresentation.getModel(CameraModel.PROPERTYNAME_OPEN));
      //
      //         ((DependentAction)ActionManager.getInstance()
      //                                        .getAction("grabFromStream"))
      //         .addDependencyAnd(
      //               cameraPresentation.getModel(CameraModel.PROPERTYNAME_STREAMING));
//            addDependentAction("Test", "testStageXY",
//                    "/edu/mbl/jif/camera/icons/snap.gif", acqCtrl, "testMeasurement",
//                    KeyEvent.VK_A,
//                    cameraPresentation.getModel(CameraModel.PROPERTYNAME_OPEN));

      // Defaults

      addAction(new DisplayOpenAction()); // open live camera display

      if (appPrefs.getBoolean("vlc.enabled", false)) {
        addDependentAction("Acquire PolStack", "acqPS",
                "/edu/mbl/jif/varilc/icons/psNew.png", pSAcquisitionController,
                "doAcquirePolStack",
                KeyEvent.VK_P, null);
        addDependentAction("Acquire BkgdPS", "acqBGPS",
                "/edu/mbl/jif/varilc/icons/bkgdNew.gif", pSAcquisitionController,
                "doAcquireBkgdPolStack",
                KeyEvent.VK_B, null);
      }
      //
      if (appPrefs.getBoolean("arcLC.enabled", false)) {
//        addDependentAction("Acquire OIDIC", "acqOIDIC",
//                "/edu/mbl/jif/varilc/icons/psNew.png",
//                (OiDicAcquistion) controllers.get("acqOiDic"),
//                "doAcquireOiDic",
//                KeyEvent.VK_O, null);
//        addDependentAction("Acquire BkgdPS", "acqBGPS",
//                "/edu/mbl/jif/varilc/icons/bkgdNew.gif", pSAcquisitionController,
//                "doAcquireBkgdPolStack",
//                KeyEvent.VK_B, null);
      }
      // Illumination
      if (appPrefs.getBoolean("illum.enabled", false)) {
        addDependentActionToggle("Epi Shutter", "toggleEpiShutter",
                "/edu/mbl/jif/microscope/illum/icons/epiIllumInvert24.png",
                getModel("illum"),
                "toggleEpiShutter", KeyEvent.VK_T,
                camOpenValueModel);
        // set initial state of epiShutter
        //ActionManager am = ActionManager.getInstance();
        ((AbstractActionExt) ActionManager.getInstance().getAction("toggleEpiShutter")).setSelected(
                false);
        addDependentActionToggle("Xmis Shutter", "toggleXmisShutter",
                "/edu/mbl/jif/microscope/illum/icons/diaIllumInvert24.png",
                getModel("illum"),
                "toggleXmisShutter", KeyEvent.VK_T,
                camOpenValueModel);
        // set initial state of epiShutter
        //ActionManager am = ActionManager.getInstance();
        ((AbstractActionExt) ActionManager.getInstance().getAction("toggleXmisShutter")).setSelected(
                false);
      }
    }
    if (appPrefs.getBoolean("lightField.enabled", false)) {
      addAction(new LFDisplayOpenAction(this));
    }

    addAction(new IDMAction());
    //  addAction(new DrawAction());
    if (!CamAcqJ.isIJPlugin()) {
      addAction(new ImageJAction());
    }
    addAction(new HelpAction());

    //JToolBar toolBar = new ActionContainerFactory(ActionManager.getInstance()).createToolBar(actions);
    //frame.addBox("",toolBar);
    //frame.setToolBar(toolBar);
    //frame.setToolBar(actions);
    frame.setToolBar(actions);
  }
  //==================

  public void createPanels() {
    // put LiveDisplay in DockingDesk
//        ((AppFrameVLDock) frame).setLiveDisplay(
//            new DisplayLiveDockable(
//            (StreamGenerator) ((CameraModel) this.getModel("camera")).getCamera()));

    if (cameraModel != null) {
      if (appPrefs.getBoolean("camera.enabled", false)) {
        frame.addTool("Camera", "/edu/mbl/jif/camera/icons/camera.png",
                new PanelCamera(this), "Camera", true);

        frame.addTool("Display", "/edu/mbl/jif/camera/icons/monitor.png",
                new PanelDisplaySettings(this), "Display");

        frame.addTool("ImageAcquistion",
                "/edu/mbl/jif/camera/icons/acqImage.gif", new PanelAcq(this),
                "Image Acquistion");

        frame.addTool("Sequence", "/edu/mbl/jif/camera/icons/clock.png",
                new PanelSequence(this), "Sequence Acquistion");
      }

      if (appPrefs.getBoolean("stageZ.enabled", false)) {
        frame.addTool("Focus", "/edu/mbl/jif/camera/icons/focus16.gif", new PanelStageZ(this), "Focus", true);
      }

      if (appPrefs.getBoolean("vlc.enabled", false) && vlcCtrl.isInitialized()) {
        PanelVLC pVLC = new PanelVLC(this);
        pVLC.addTab("Calc", new PanelCalcPS(this));
        // pVLC.addTab("Acq", new PanelAcqPS(this));
        pVLC.addTab("Calib", new PanelCalibration(this));
        pVLC.addTab("Setup", new PanelConfigureVariLC(this));
        frame.addTool("VariLC", "/edu/mbl/jif/camera/icons/vlclLogo.gif",
                pVLC, "VariLC", true);
      }
      if (appPrefs.getBoolean("arcLC.enabled", false)) {
        frame.addTool("ArcLC", "/edu/mbl/jif/camera/icons/arcLC.gif", new PanelArcLCCalibration(this), "ArcLC", true);
      }

      // IModule...
      // frame.addTab("SLM", "/edu/mbl/jif/varilc/icons/slm16.png",new PanelSLM(this), "SLM");
      // frame.addTab(getModule("testStageXY"));
      // For workframe modules:
      // iterate thru the modules and add the panels to the containter
      //


      // StageXY
      if (appPrefs.getBoolean("laser.enabled", false) && appPrefs.getBoolean("stageXY.enabled", false)) {
        PanelSurgeon sPanel = new PanelSurgeon(this);
        PanelXYStage panelXYStage = new PanelXYStage(this);
        PanelLaser panelLaser = new PanelLaser(this);
        PanelPath panelPath = new PanelPath(this); 
        sPanel.addTab("XYStage", panelXYStage);
        sPanel.addTab("Laser", panelLaser);
        sPanel.addTab("XYPath", panelPath);
        frame.addTool("Surgeon", "/edu/mbl/jif/laser/icons/laser16.png", sPanel, "Surgeon");

//                PanelXYStage panelXYStage = new PanelXYStage(this);
//                Dockable panelXYStageDockable =
//                    new MyDockablePanel("XYStage", "/edu/mbl/jif/camera/icons/xystage16.png",
//                    panelXYStage, "XYStage");
//
//                PanelLaser panelLaser = new PanelLaser(this);
//                Dockable panelLaserDockable =
//                    new MyDockablePanel("Laser", "/edu/mbl/jif/laser/icons/laser16.png",
//                    panelLaser, "Laser");
//
//                PanelPath panelPath = new PanelPath(this);
//                Dockable panelPathDockable =
//                    new MyDockablePanel("Path", "/edu/mbl/jif/stage/icons/path.png",
//                    panelPath, "Path");
//
//                frame.getDesk().addDockable(panelXYStageDockable);
//                frame.getDesk().createTab(panelXYStageDockable, panelPathDockable, 1);
//                frame.getDesk().createTab(panelXYStageDockable, panelLaserDockable, 2);
      }
      // Lightfield Control
      if (appPrefs.getBoolean("lightField.enabled", false)) {
        PanelLF plf = new PanelLF(this);
        plf.addArrowPanel(new PanelControlXY(lFController));
        frame.addTool("Lightfield", "/edu/mbl/jif/camera/icons/lightfield16.png",
                plf, "Lightfield");
      }
      if (appPrefs.getBoolean("illum.enabled", false)) {
        frame.addTool("Illumination",
                "/edu/mbl/jif/camera/icons/lightbulb.png",
                new PanelIllum(this), "Illumination");
      }
      if (appPrefs.getBoolean("filter.enabled", false)) {
        frame.addTool("Filter", "/edu/mbl/jif/camera/icons/filterCube.png",
                new PanelFilter(this), "Filter Turret");
      }
      /*
      jTabbedPane1.addTab("", loadIcon("/edu/mbl/jif/camera/icons/profiles.png"),
      new PanelProfile(), "Settings profiles");
      jTabbedPane1.addTab("", loadIcon("/edu/mbl/jif/camera/icons/script.png"),
      new PanelScripting(), "Scripting");
      jTabbedPane1.addTab("", loadIcon("/edu/mbl/jif/camera/icons/table.png"),
      new JPanel(), "Varius");
      frame.addTab("Movie", "/edu/mbl/jif/camera/icons/movie16.gif",
      new PanelMovie(this), "Movie");
      jTabbedPane1.addTab("", loadIcon("/edu/mbl/jif/journal/journal16.gif"),
      new PanelJournal(), "Journal");
       */
      frame.addTool("Data Storage", "/edu/mbl/jif/camera/icons/disk.png",
              new PanelData(this), "Data storage");
    }

    frame.addTool("System", "/edu/mbl/jif/camera/icons/cog.png", new PanelSys(), "System");
//        frame.addTool("Diag", "/edu/mbl/jif/camera/icons/wrench.png",
//            new PanelDiagnostics(), "Diagnostics");
  }

  /**
   * Opens a property sheet for this model specified
   * @param pm presentationModel to show
   */
  private void openPresentationModelViewer(PresentationModel pm) {
    if (pm == null) {
      return;
    }
    PropertySheetDisplay psd = new PropertySheetDisplay(pm);
    psd.showPropSheet(new Rectangle(600 + offsetFrame, 200 + offsetFrame,
            200, 500)); 
    offsetFrame = offsetFrame + 20;
  }

  public DisplayLiveCamera getDisplayLive() {
    return liveDisplay;
  }

  public void setDisplayLive(DisplayLiveCamera display) {
    liveDisplay = display;
  }

  //----------------------------------------
  private void initializeImageDir() {
    String dir = "/";
    String currentDir = ((DataModel) getModel("data")).getImageDirectory();
    if (currentDir == ".") {
      // use ImageJ's current dir.
      dir = ij.IJ.getDirectory("image");
      if ((dir == null) || !FileUtil.doesExist(dir)) {
        dir = userDirectory();
        if (!FileUtil.doesExist(dir)) {
          error("unable to initialize ImageData directory");
        }
      }
    } else if (FileUtil.doesExist(currentDir)) {
      dir = currentDir;
    }
    ((DataModel) getModel("data")).setImageDirectory(dir);
  }

  public static String userDirectory() {
    return System.getProperty("user.home") + System.getProperty("file.separator");
  }

  //-------------------------------
  private void error(String string) {
    Application.getInstance().error(string);
  }
}
