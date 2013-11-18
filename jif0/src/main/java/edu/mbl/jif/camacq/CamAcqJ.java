/*
 * CamAcqJ.java
 */
package edu.mbl.jif.camacq;


import edu.mbl.jif.config.PanelDiagnostics;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.graphic.GraphicsUtils;
import edu.mbl.jif.utils.diag.edt.EventQueueWithWatchDog;

import ij.IJ;
import ij.ImageJ;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
//import net.infonode.gui.laf.InfoNodeLookAndFeel;
//import org.jvnet.substance.api.skin.SubstanceGeminiLookAndFeel;
//import net.infonode.gui.laf.InfoNodeLookAndFeel;

/**
 * @version $Revision$
 *
 * Application subclass. Almost all interesting processing is delegated to the
 * ApplicationController (InstrumentController)
 * 
 * To run stand-alone...
 *    javaw -cp CamAcqJ.jar edu.mbl.jif.camera.camacq.CamAcqJ 
 * 
 * Default tab panel size:  440 x 230
 */
public class CamAcqJ
    extends Application {

    private String version = "1.5";
    //private ApplicationFrameTabbed frame;
    private ApplicationFrame frame;
    // Controller in terms of MVC.
    private InstrumentController instCtrl;
    final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    // Overriden to return the name of the Application
    public String getName() {
        return "CamAcqJ";
    }

    public String getVersion() {
        return version;
    }

    // If this app. may be running as an ImageJ Plugin
    private static boolean IJPlugin = false;

    public static void setIJPlugin(boolean t) {
        IJPlugin = t;
    }

    public static boolean isIJPlugin() {
        return IJPlugin;
    }

    @Override
    public void installLookAndFeel() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        //UIManager.getSystemLookAndFeelClassName();

        try {
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
//                new com.jgoodies.looks.plastic.theme.DesertBluer());
//            //new com.jgoodies.looks.plastic.theme.Silver());
//            //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
//            //new com.jgoodies.looks.plastic.theme.DesertBlue());
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
//            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
          //UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel");
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
          //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
          //UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
        } catch (javax.swing.UnsupportedLookAndFeelException use) {
            UIManager.getSystemLookAndFeelClassName();
        } catch (Exception e) {
            e.printStackTrace();
        }
//   JFrame.setDefaultLookAndFeelDecorated(true);
//    try {
//      UIManager.setLookAndFeel(new SubstanceGeminiLookAndFeel());
//      //UIManager.setLookAndFeel("org.jvnet.substance.api.skin.SubstanceGeminiLookAndFeel");
//    } catch (Exception e) {
//      System.out.println("Substance Gemini failed to initialize");
//    }
        // If Globally using heavyweight components:
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        // Use Auditory Queues
        UIManager.put("AuditoryCues.playList", UIManager.get("AuditoryCues.defaultCueList"));
       
    }
    // Overriden to create the UI and show it.
    protected void init() {
        if (getPreferences().getBoolean("logOutErr", true)) {
      //      redirectErr();
        }
        System.out.println("\n\n>>>>>>> CamAcqJ Application Started " + timeStamp() +" <<<<<<<<<<<<<<<<<<<<<<<<<");
        processCommandLineArgs(commandLineArgs);

        GraphicsUtils.setupJvmProperties();

        // FileChooserFixWinZip.applyFileChooserTweak();

        /* @todo Setup CommDriver for serial port
         * Elliminates need for javax.comm.properties (on Windows)
         */
        // @todo create setupCommDriver()
        // public void setupCommDriver() {
        //        String driverName = "com.sun.comm.Win32Driver";
        //        CommDriver commDriver;
        //        try {
        //            commDriver = (CommDriver) Class.forName(driverName).newInstance();
        //            commDriver.initialize();
        //        } catch (Exception ex) {
        //            ex.printStackTrace();
        //        }

        //logger.severe("testing the loggability function");


        //frame    = new JFrame(getName());
        //frame    = new FrameCamAcqJ(null);

        //frame = new ApplicationFrameTabbed("/edu/mbl/jif/camera/icons/camJ16.gif");
        //frame = new ApplicationFrameStacked("/edu/mbl/jif/camera/icons/camJ16.gif");
         frame = new AppFrameVLDock("/edu/mbl/jif/camera/icons/camJ16.gif");
        //frame = new AppFrameInfoNode("/edu/mbl/jif/camera/icons/camJ16.gif");
         
        //frame = new ApplicationFrameTaskGroup("/edu/mbl/jif/camera/icons/camJ16.gif");
        
        // Closing and quiting....
        ((JFrame) frame).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ((JFrame) frame).addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    CamAcq.getInstance().terminate();
                    saveAppFramePosition((JFrame) frame);
                    ((JFrame) frame).setVisible(false);
                    frame = null;
                    System.out.println("\n\n>>>>>>> CamAcqJ Application Exited " + timeStamp() +" <<<<<<<<<<<<<<<<<<<<<<<<<");
                    exitConsideringImageJ();
                }
            });

        Application.getInstance().setHostFrame(((JFrame) frame));
        getHostFrame().setAlwaysOnTop(getPreferences().getBoolean("alwaysOnTop", false));
        ((JFrame) frame).setBounds(
            getPreferences().getInt("mainFrameX", 10),
            getPreferences().getInt("mainFrameY", 20),
            getPreferences().getInt("mainFrameW", 540),
            getPreferences().getInt("mainFrameH", 760));

        // First, add Diagnostics tab
        //  frame.addTool("Config", "/edu/mbl/jif/camera/icons/wrench.png",  new PanelConfigMgr(), "Configuration");
        frame.addTool("Diag", "/edu/mbl/jif/camera/icons/wrench.png",  new PanelDiagnostics(), "Diagnostics");

        ((JFrame) frame).setVisible(true);
        try {
            new InitProgressFrame(frame);
            //System.out.println("IsEDT: " + SwingUtilities.isEventDispatchThread());
        } catch (Exception ex) {
            Logger.getLogger(CamAcqJ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public void saveAppFramePosition(JFrame frame) {
        // save application frame position
        getPreferences().putInt("mainFrameX", (int) ((JFrame) frame).getBounds().getX());
        getPreferences().putInt("mainFrameY", (int) ((JFrame) frame).getBounds().getY());
        getPreferences().putInt("mainFrameW", (int) ((JFrame) frame).getBounds().getWidth());
        getPreferences().putInt("mainFrameH", (int) ((JFrame) frame).getBounds().getHeight());
    }

    public void exitConsideringImageJ() {
        if (isIJPlugin()) {
            // if running as an ImageJ Plugin, just close this app.
            // on close CamAcqJ, 
            Application.clearInstance();
        } else {
            // If this is the parent application, kill ImageJ too.
            if (IJ.getInstance() != null) {
                ImageJ ij = IJ.getInstance();
                ij.exitWhenQuitting(true);
                ij.quit();
            } else {
                // Exit on close if ImageJ not involved
                exit();
            }
        }
    }

    public InstrumentController getInstrumentController() {
        return instCtrl;
    }
    // Overriden to delegate to the controller.
    @Override
    protected boolean canExit() {
        if (Application.getInstance().getController() == null) {
            return true;
        } else {
            return Application.getInstance().getController().canExit();
        }
    }

    public static void main(String[] args) {
        commandLineArgs = args;
        System.out.println(">>> Command Line Args: " + asString(args, ","));
        
        // @todo add argument handling... https://args4j.dev.java.net/

        //Application.setDebug(true); 

       // StdioTerm term = new StdioTerm("CamAcqJ");
       // term.attach(null);

        //initQueue();

        new CamAcqJ().start();    }


          // Install and init the alternative queue
  private static void initQueue() {
    EventQueueWithWatchDog queue = EventQueueWithWatchDog.install();

    // Install 3-seconds single-shot watchdog timer
    queue.addWatchdog(3000, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(new Date() + " 3 seconds - single-shot");
      }
    }, false);

    // Install 3-seconds multi-shot watchdog timer
    queue.addWatchdog(3000, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(new Date() + " 3 seconds - multi-shot");
      }
    }, true);

    // Install 11-seconds multi-shot watchdog timer
    queue.addWatchdog(11000, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        System.out.println(new Date() + " 11 seconds - multi-shot");
      }
    }, true);
  }

//    private ApplicationFrameTabbed FrameCamAcqJStacked() {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
   public static String asString (String[] strings, String separator) {
      StringBuffer buffer = null;

      buffer = new StringBuffer(strings.length * 20);
      if (strings.length > 0) {
         buffer.append(strings[0].toString());
         for (int i = 1; i < strings.length; i++) {
            buffer.append(separator);
            if (strings[i] != null) {
               buffer.append(strings[i]);
            }
         }
      }
      return buffer.toString();
   } // asString()
    //----------------------------------------------------------------
// Console Redirect: System.out & System.err redirected to file
     void redirectErr() {
        if (getPreferences().getBoolean("logOutErr", true)) {
            String logfile = "outErr.log";
            System.out.println("System.out & System.err redirected to file: " + logfile);
            File f = new File(logfile);
            // if filesize exceeds 100000, rollover to new file... 
            if (f.exists()) {
                if (f.length() > 100000) {
                    f.renameTo(new File(timeStamp() + ".log"));
                }
            }
            try {
                PrintStream out = new PrintStream(new FileOutputStream(logfile, true), true);
                System.setOut(out);
                System.setErr(out);
            } catch (FileNotFoundException e) {
            }
        }
    }

    public static String timeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy_MMdd_HHmm_ss",
            Locale.getDefault());
        Date currentDate = new Date();
        String dateStr = formatter.format(currentDate);
        return dateStr;
    }

    static String[] commandLineArgs;

    void processCommandLineArgs(String[] args) {
        if (args == null) {
            return;
        }
        int i = 0;
        String arg = null;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];
            if (arg.equalsIgnoreCase("-noInstruments")) {
                System.out.println(">>Running with no instruments");
                getPreferences().putBoolean("instruments", false);
            }
            if (arg.equalsIgnoreCase("-instruments")) {
                System.out.println(">> Running with instruments");
                getPreferences().putBoolean("instruments", true);
            }
            if (arg.equalsIgnoreCase("-debug")) {
                System.out.println(">> Debug on");
                getPreferences().putBoolean("deBug", true);
                Application.setDebug(true);
            }
            if (arg.equalsIgnoreCase("-noLogOutErr")) {
                System.out.println(">> Console NOT directed to outerr.log ");
                getPreferences().putBoolean("logOutErr", false);
            }
            if (arg.equalsIgnoreCase("-?")) {
                System.out.println("Command Line Arguments:");
                System.out.println("-noInstruments - run with no instruments");
                System.out.println("-instruments - run with instruments");
                System.out.println("-debug - Debug on");
                System.out.println("-noLogOutErr - Console NOT directed to outerr.log");
            }

        }
    }

}
