package edu.mbl.jif.script.jython;

//import edu.mbl.jif.camera.camacq.CamAcq;
//import edu.mbl.jif.fabric.Application;
import java.io.*;
import java.awt.Dimension;
import javax.swing.*;

import ij.*;
import ij.process.*;


import org.python.util.PythonInterpreter;
import org.python.core.*;


public class ScriptRunner {
//    extends Thread {

  public String script;
  //String path = Prefs.usr.get("scriptPath", ".\\scripts") + "\\";
  String path = ".\\scripts" + "\\";

  public ScriptRunner(String s) {
    //super("JythonInterpreter");
    this.script = s;
    if ( (new File(path + script)).exists()) {
      //start();
      doIt();
    } else {
      //Application.getInstance().error("No script found: " + path + script);
    }
  }

  public void doIt() { // run() {
    String resultString = "";
    PySystemState.initialize();
    PythonInterpreter interp1 = new PythonInterpreter();
    PythonInterpreter interp = new org.python.util.InteractiveConsole();
    setupNamespace(interp);

//    StringWriter outStream = new StringWriter();
//    interp.setOut(outStream);
//    StringWriter errStream = new StringWriter();
//    interp.setOut(errStream);

    // Output Console
    JFrame consoleFrame = new JFrame();
    consoleFrame.setSize(new Dimension(300, 350));
    ScriptConsole sConsole = new ScriptConsole(interp,
                             "Script Execution: " + script);
    consoleFrame.getContentPane().add(sConsole);
    consoleFrame.setTitle( "ExecScript: " + script);
    consoleFrame.setVisible(true);

    // prolog
    System.out.println("prolog");
    if ( (new File(path + "prolog.py")).exists()) {
      interp.execfile(path + "prolog.py");
    }

    // run the script
    System.out.println("path + script: " + path + script);
    interp.execfile(path + script);

    // epilog
    System.out.println("epilog");
    if ( (new File(path + "epilog.py")).exists()) {
      interp.execfile(path + "epilog.py");
    }
    //
    //IJ.write(outStream.toString());
    interp.cleanup();
    interp = null;
  }

  void setupNamespace(PythonInterpreter interp) {
    try {
      // for ImageJ
      // expose ImagePlus for current image window
      ImagePlus imp = WindowManager.getCurrentImage();
      ImageProcessor ip = imp.getProcessor();
      interp.set("imp", imp);
      interp.set("ip", ip);
      interp.set("IJ", new IJ()); // expose IJ utility class
      // for QCamJ
      //interp.set("CamAcq", CamAcq.getInstance());
    }
    catch (Exception e) {};
    // create Editor object for easy script access to this class!
    interp.set("Editor", this);
  }

}
