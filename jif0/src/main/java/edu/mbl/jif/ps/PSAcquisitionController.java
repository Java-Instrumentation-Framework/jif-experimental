package edu.mbl.jif.ps;

import edu.mbl.jif.PSAcqRoutine;
import edu.mbl.jif.acq.AcqModel;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.utils.FileUtil;
import edu.mbl.jif.varilc.camacq.VariLCController;
import edu.mbl.jif.varilc.VariLCModel;
import ij.IJ;
import ij.ImagePlus;
import ij.Macro;

import ij.Prefs;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author GBH
 */
public class PSAcquisitionController {

  private VariLCController vlcCtrl;
  private VariLCModel vlcModel;
  private AcquisitionController acqCtrl;
  private AcqModel acqModel;
  private PSCalcModel psCalcModel;
  //
  ImagePlus lastAcquiredImgPlus = null;    // PS Calc values
  String sampleStackTitle = "";
  String bgStackTitle = "";
  //boolean mirror;
  //private boolean fastCalc;
  //boolean autoCalc = false;
  //String bitDepth;
  //double wavelength;
  //double swing;
  //double retCeiling;
  //double azimRef;
  //private String calcPlugin = "Calc_5Fr";

  public PSAcquisitionController(InstrumentController instCtrl) {
    this.vlcCtrl = (VariLCController) instCtrl.getController("variLC");
    this.vlcModel = (VariLCModel) instCtrl.getModel("variLC");
    this.acqModel = (AcqModel) instCtrl.getModel("acq");
    this.psCalcModel = new PSCalcModel();
    this.getIJPrefs();
  }
// <editor-fold defaultstate="collapsed" desc=" PolStack Acquisition ">

  public void doAcquirePolStack() {
    doAcquirePolStack(false);
  }

  public void doAcquireBkgdPolStack() {
    doAcquirePolStack(true);
  }

//    public void doAcquirePolStack(boolean background) {
//        CamAcq camAcq = CamAcq.getInstance();
//        if (camAcq == null) {
//            return;
//        }
//        System.out.println("Acquiring PolStack...");
//        //camAcq.displayOpen();
//        //camAcq.setExposureAcq(10);
//
//        int nSlices = 5;
//        ImagePlus imgPlus = null;
//        if (camAcq.getDepth() == 8) {
//            imgPlus = camAcq.newCaptureStackByte(7);
//        } else {
//            imgPlus = camAcq.newCaptureStackShort(7);
//        }
//
//        camAcq.setImagePlusForCapture(imgPlus); // sets default stack for capture
//
//        // temporary... set for background multiframe setting
//        int multiFrameWas = 1;
//        camAcq.startAcq(true);
//        if(background) {
//            multiFrameWas = acqModel.getMultiFrame();
//            camAcq.setMultiFrame(acqModel.getMultiFrameBkgd());
//        }
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {}
//        for (int i = 0; i < nSlices; i++) {
//            //camAcq.vlcSelect(i); // change LC...
//            vlcCtrl.selectElementWait(i);
//            camAcq.captureImageToSlice(imgPlus, i + 2);
//        }
//        camAcq.captureStackFinish();
//        if(background) {
//            camAcq.setMultiFrame(multiFrameWas);
//        }
//        //camAcq.finishAcq();
//        lastAcquiredImgPlus = imgPlus;
//        String stackTitle = lastAcquiredImgPlus.getTitle();
//
//        // +++++ get current values from CamAcqJ modules
////        if (background) {
////            bgStackTitle = "NoBg";
////        }
//        sampleStackTitle = stackTitle;
//        //setIJPrefs();
//        if (psCalcModel.isAutoCalc()) {
//            if (background) {
//                doBkgdPSCalculation();
//            } else {
//                doPSCalculation();
//            }
//        }
//        if (background) {
//            setBkgdStack();
//        }
//        IJ.save(imgPlus, camAcq.getImageDirectory() + "\\" + stackTitle + ".tif");
//
//        camAcq.displayResume();
//    }
  public void doAcquirePolStack(boolean background) {
    CamAcq camAcq = CamAcq.getInstance();
    if (camAcq == null) {
      return;
    }
    System.out.println("Acquiring PolStack...");

    int nSlices = 5;
    ImagePlus imgPlus = null;
    if (camAcq.getDepth() == 8) {
      imgPlus = camAcq.newCaptureStackByte(7);
    } else {
      imgPlus = camAcq.newCaptureStackShort(7);
    }
    //---------

//  psj.PolStack.PolStack pStk = new psj.PolStack.PolStack(5, 100, 100, camAcq.getDepth(), "stackID");
    PSAcqRoutine psAcqRoutine  = new PSAcqRoutine();
    psAcqRoutine.setup();
    psAcqRoutine.doAcquirePolStack(imgPlus.getStack(), background);

    camAcq.captureStackComplete(imgPlus, imgPlus.getShortTitle());
    //----------
    lastAcquiredImgPlus = imgPlus;
    String stackTitle = lastAcquiredImgPlus.getTitle();

    // +++++ get current values from CamAcqJ modules
//        if (background) {
//            bgStackTitle = "NoBg";
//        }
    sampleStackTitle = stackTitle;
    //setIJPrefs();
    if (psCalcModel.isAutoCalc()) {
      if (background) {
        doBkgdPSCalculation();
      } else {
        doPSCalculation();
      }
    }
    if (background) {
      setBkgdStack();
    }
    IJ.save(imgPlus, camAcq.getImageDirectory() + "\\" + stackTitle + ".tif");

    camAcq.displayResume();
  }
// </editor-fold>

  public void setBkgdStack() {
    if (IJ.getImage().getTitle() != null) {
      bgStackTitle = IJ.getImage().getTitle();
      setIJPrefs();
    }
  }

  public void clearBkgdStack() {
    bgStackTitle = "NoBg";
    setIJPrefs();

  }

  public PSCalcModel getPSCalcModel() {
    return this.psCalcModel;
  }

  public boolean isAutoCalc() {
    return psCalcModel.isAutoCalc();
  }

  public void setAutoCalc(boolean autoCalc) {
    psCalcModel.setAutoCalc(autoCalc);
  }

  public String getCalcPlugin() {
    return psCalcModel.getCalcPlugin();
  }

  public void setCalcPlugin(String calcPlugin) {
    psCalcModel.setCalcPlugin(calcPlugin);
  }

  public void selectCalcPlugin() {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        String plugPath = chooseFileInPscalcDir();
        if (plugPath != null) {
          String calcPlugin = FileUtil.getJustFilenameNoExt(plugPath);
          setCalcPlugin(calcPlugin);
          setIJPrefs();
        }
      }
    });
  }

  public String chooseFileInPscalcDir() {
    JFileChooser fc = new JFileChooser();
    fc.setName("Save VariLC Settings");
    FileFilter filter = new FileNameExtensionFilter("Class", "class");
    // Default to Image data dir.
    String pluginsDir = IJ.getDirectory("plugins");
    //System.out.println(pluginsDir);
    if (pluginsDir == null) {
      Application.getInstance().error("No IJ Plugin dir. found.");
      return null;
    }
    fc.setCurrentDirectory(new File(pluginsDir));
    fc.setFileFilter(filter);
    int option = fc.showDialog(null, "Choose");
    if (JFileChooser.APPROVE_OPTION == option) {
      return fc.getSelectedFile().getAbsolutePath();
    }
    return null;
  }

  // runImageJPluginAsMacro
  public void runImageJPluginAsMacro(String className, String arg) {
    //IJ.run(className, arg);
    System.out.println("runImageJPluginAsMacro-className: " + className);
    System.out.println("arg: " + arg);
    Thread thread = Thread.currentThread();
    String name = thread.getName();
    if (!name.startsWith("Run$_")) {
      thread.setName("Run$_" + name);
    }
    Macro.setOptions(arg);
    IJ.runPlugIn(className, arg);
  }

  public void doPSCalculation() {
    String className = psCalcModel.getCalcPlugin();
    String arg = "sample=" + sampleStackTitle;
    runImageJPluginAsMacro(className, arg);
  }

  public void doBkgdPSCalculation() {
    String className = psCalcModel.getCalcPlugin();
    String arg = "sample=" + sampleStackTitle + " background=NoBg";
    runImageJPluginAsMacro(className, arg);
  }

  // <editor-fold defaultstate="collapsed" desc=" IJPrefs get/set ">
  public void getIJPrefs() {
    sampleStackTitle = Prefs.get("ps.sampleStackTitle", "sampleStack");
    bgStackTitle = Prefs.get("ps.bgStackTitle", "NoBg");
    psCalcModel.setMirrorInPath(Prefs.get("ps.mirror", false));
    psCalcModel.setFastCalc(Prefs.get("ps.fastCalc", false));
    psCalcModel.setWavelength(Prefs.get("ps.wavelength", 546));
    psCalcModel.setSwingFraction(Prefs.get("ps.swing", 0.03d));
    psCalcModel.setRetCeiling(Prefs.get("ps.retCeiling", 3d));
    psCalcModel.setRefAngle(Prefs.get("ps.azimRef", 0d));
    psCalcModel.setCalcPlugin(Prefs.get("ps.calcPlugin", "Calc_5Fr"));
  }

  public void setIJPrefs() {
    Prefs.set("ps.sampleStackTitle", sampleStackTitle);
    Prefs.set("ps.bgStackTitle", bgStackTitle);
    Prefs.set("ps.mirror", psCalcModel.isMirrorInPath());
    Prefs.set("ps.fastCalc", psCalcModel.isFastCalc());
    Prefs.set("ps.wavelength", psCalcModel.getWavelength());
    Prefs.set("ps.swing", psCalcModel.getSwingFraction());
    Prefs.set("ps.retCeiling", psCalcModel.getRetCeiling());
    Prefs.set("ps.azimRef", psCalcModel.getRefAngle());
    Prefs.set("ps.calcPlugin", psCalcModel.getCalcPlugin());
    Prefs.savePreferences();
  }
// </editor-fold>

  public static void main(String[] args) {
  }

  /*
  macro "Calculate 5Fr [F5]" {
  run("Calc 5Fr", "sample="+getTitle());
  }

  macro "Calculate 5Fr with Query [F6]" {
  call("ij.Prefs.set", "ps.sampleStackTitle", getTitle());
  call("ij.Prefs.savePreferences");
  run("Calc 5Fr");
  }

  macro "Set Bg stack [F7]" {
  // activate background stack before running this macro
  bgStackTitle = getTitle();
  showStatus("Background stack: "+bgStackTitle);
  call("ij.Prefs.set", "ps.bgStackTitle", bgStackTitle);
  call("ij.Prefs.savePreferences");
  }

  macro "Enter Ret. Ceiling  [F8]" {
  // enter retardance ceiling value
  retCeiling = call("ij.Prefs.get", "ps.retCeiling", "3.0");
  retCeiling = getNumber("Retardance ceiling [nm]:", retCeiling);
  showStatus("Retardance ceiling: "+retCeiling+" nm");
  call("ij.Prefs.set", "ps.retCeiling", retCeiling);
  call("ij.Prefs.savePreferences");
  }
   *///        // Test Run Calc-5Fr IJ Plugin
//        
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                String testDataPath = com.myjavatools.lib.Files.getcwd() + "/testdata/";
//                System.out.println(testDataPath);
//                IJ.open(edu.mbl.jif.Constants.testDataPath + "ps/BG.tiff");
//                IJ.open(edu.mbl.jif.Constants.testDataPath + "ps/PS.tiff");
//
//                String className = "Calc_5Fr";
//                String arg = "sample=" + IJ.getImage().getTitle();
//                // In order to setOptions
////                Thread thread = Thread.currentThread();
////                String name = thread.getName();
////                if (!name.startsWith("Run$_")) {
////                    thread.setName("Run$_" + name);
////                }
////                Macro.setOptions(arg);
////                IJ.runPlugIn(className, arg);
//                IJ.run(className, arg);
//            }
//
//        });
}
