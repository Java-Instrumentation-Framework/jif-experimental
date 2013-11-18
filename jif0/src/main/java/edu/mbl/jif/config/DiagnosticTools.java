package edu.mbl.jif.config;

import edu.mbl.jif.camera.TextWindow;
//import edu.mbl.jif.comm.SerialUtils;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.gui.swingthread.SwingThread;
import edu.mbl.jif.utils.diag.thread.ThreadViewer;
import edu.mbl.jif.utils.props.PropsWassup;
import java.io.IOException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */
public class DiagnosticTools {

  public static void launchWindowsDeviceManager() {
    // Launch the Windows Device Manager
    String os = System.getProperty("os.name").toLowerCase();
    if (os.startsWith("win")) {
      try {
        String mmc = System.getenv("windir") + "\\system32\\mmc.exe";
        String devmgr = System.getenv("windir") + "\\system32\\devmgmt.msc";
        //System.out.println("ExecString: " + mmc + " " + devmgr);
        ProcessBuilder pb = new ProcessBuilder(new String[] { "cmd.exe", "/C", "start", mmc, devmgr});
				// adding the "cmd.exe", "/C", "start" to the constructor makes this work on Win7 (re: UAC issue)
        Map<String, String> environment = pb.environment();
        environment.put("devmgr_show_nonpresent_devices", "1");
        pb.redirectErrorStream(false);
        Process p = pb.start();
      } catch (IOException ex) {
        Logger.getLogger(PanelDiagnostics.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void launchWindowsSystemInformation() {
    // Launch the Windows System Information Utility
    String os = System.getProperty("os.name").toLowerCase();
    if (os.startsWith("win")) {
      try {
        Process p = Runtime.getRuntime().exec(
                "\"C:/Program Files/Common Files/Microsoft Shared/MSInfo/msinfo32.exe\"");
      //p.waitFor();
//                String cmd = "msinfo32.exe";
//                //  System.getenv("windir") + "\\system32\\msinfo32.exe";
//                ProcessBuilder pb = new ProcessBuilder(cmd);
//                pb.directory(new File(
//                    "\"C:/Program Files/Common Files/Microsoft Shared/MSInfo/\""));
//                pb.redirectErrorStream(false);
//                Process p = pb.start();
      } catch (Exception ex) {
        Logger.getLogger(PanelDiagnostics.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void launchDSBDeview() {
    // Launch UBSDeview.exe: Diagnostics tool for USB devices
    String os = System.getProperty("os.name").toLowerCase();
    if (os.startsWith("win")) {
      try {
        Process p = Runtime.getRuntime().exec("\"./usbdeview/USBDeview.exe\"");
      } catch (Exception ex) {
        Logger.getLogger(PanelDiagnostics.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }


  public static void showOutErrLog() {
    StaticSwingUtils.dispatchToEDT(new Runnable() {

      public void run() {
        edu.mbl.jif.gui.text.TextWindow.listFileWindow("outerr.log","OutErr.log");
      }
    });
  }

  public static void showCommPortStatus() {
    StaticSwingUtils.dispatchToEDT(new Runnable() {

      public void run() {
        TextWindow tf = new TextWindow("Serial Ports");
        tf.setSize(600, 600);
        tf.setLocation(200, 20);
        tf.setVisible(true);
        tf.set("Serial Ports \n\n");
// TODO        String status = SerialUtils.getStatusOfPorts();
//        tf.append(status);
      }
    });
  }

  public static void showPermissions() {
    StaticSwingUtils.dispatchToEDT(new Runnable() {

      public void run() {
        StringBuffer sBuff = new StringBuffer();
        TextWindow tf = new TextWindow("Permissions");
        tf.setSize(600, 600);
        tf.setLocation(200, 20);
        tf.setVisible(true);
        tf.set("PROPERTIES ------------------------\n");
        // Get the protection domain for the class
        //ProtectionDomain domain = CamAcqJ.getInstance().getClass().getProtectionDomain();
        ProtectionDomain domain = DiagnosticTools.class.getProtectionDomain();
        // With the protection domain, get all the permissions from the Policy object
        PermissionCollection pcoll = Policy.getPolicy().getPermissions(domain);
        // View each permission in the permission collection
        Enumeration enumer = pcoll.elements();
        for (; enumer.hasMoreElements();) {
          Permission p = (Permission) enumer.nextElement();
          tf.append(p.toString() + "\n");
        }
      }
    });
  }

  public static void showProperties() {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        StringBuffer sBuff = new StringBuffer();
        TextWindow tf = new TextWindow("Properties & Preferences");
        tf.setSize(600, 600);
        tf.setLocation(200, 20);
        tf.setVisible(true);
        tf.set("PROPERTIES ------------------------\n");
        tf.append(PropsWassup.displayAllProperties("\n"));
        tf.append("\n\nEnvironment Variables -------------------------\n");
        Map<String, String> variables = System.getenv();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
          String name = entry.getKey();
          String value = entry.getValue();
          tf.append(name + "=" + value + "\n");
        }
      }
    });
  }

  public static void viewThreads() {
    SwingThread.dispatchToEDT(new Runnable() {

      @Override
      public void run() {
        JFrame f = new JFrame("Threads");
        ThreadViewer viewer = new ThreadViewer();
        f.setContentPane(viewer);
        f.setLocation(100, 100);
        f.setSize(600, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      }
    });
  }
}
