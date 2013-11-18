package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.ControlVLC;
import edu.mbl.jif.varilc.multi.PanelPathVLC;
import edu.mbl.jif.varilc.multi.PathVLC;
import edu.mbl.jif.varilc.multi.ControlDevice;
import edu.mbl.jif.varilc.multi.Retarder;
import javax.swing.JPanel;
import edu.mbl.jif.gui.test.FrameForTest;

import javax.swing.BoxLayout;

import javax.swing.UIManager;
import edu.mbl.jif.utils.*;
import edu.mbl.jif.comm.SerialPortConnection;
import java.io.IOException;


public class ScanningApertureVariLC
{
   public final static int RETARDERS = 12;
   public final static int PATHS = 4;
   public final static int SETTINGS = 5;
   public final static int DEVICES = 4;

   public Retarder[] retarder;
   public PathVLC[] path;
   public ControlVLC vlcCtrl;
   public ControlDevice[] ctrl;

   SerialPortConnection port;

   public ScanningApertureVariLC () {
      openCommPort();
      createVLCControl();
   }


   void openCommPort () {
      String commPort = "COM5";
      try {
         port = new SerialPortConnection();
         port.setBaudRate(9600);
         port.setPortName(commPort);
         port.setDebug(false);
         port.setWait(10L, 50);
         port.openConnection("Test");
         port.createMonitorFrame();
      }
      catch (IOException ex1) {
         System.out.println("Could not open comPort:" + commPort + ": " + ex1.getMessage());
      }
      catch (Exception ex) {
         System.err.println("Could not open comPort:" + commPort);
         return;
      }
   }


   public void createVLCControl () {
      // Retarders
      retarder = new Retarder[RETARDERS];
      retarder[0] = new Retarder(Retarder.CELL_0_DEG, 0.5f, 0.01f, 0.99f);
      retarder[1] = new Retarder(Retarder.CELL_0_DEG, 0.5f, 0.01f, 0.99f);
      retarder[2] = new Retarder(Retarder.CELL_0_DEG, 0.5f, 0.01f, 0.99f);
      retarder[3] = new Retarder(Retarder.CELL_0_DEG, 0.5f, 0.01f, 0.99f);
      retarder[4] = new Retarder(Retarder.CELL_45_DEG, 0.5f, 0.01f, 0.99f);
      retarder[5] = new Retarder(Retarder.CELL_45_DEG, 0.5f, 0.01f, 0.99f);
      retarder[6] = new Retarder(Retarder.CELL_45_DEG, 0.5f, 0.01f, 0.99f);
      retarder[7] = new Retarder(Retarder.CELL_45_DEG, 0.5f, 0.01f, 0.99f);
      retarder[8] = new Retarder(Retarder.ATTENUATOR, 0.9f, 0.01f, 0.99f);
      retarder[9] = new Retarder(Retarder.ATTENUATOR, 0.9f, 0.01f, 0.99f);
      retarder[10] = new Retarder(Retarder.ATTENUATOR, 0.9f, 0.01f, 0.99f);
      retarder[11] = new Retarder(Retarder.ATTENUATOR, 0.9f, 0.01f, 0.99f);

      ctrl = new ControlDevice[DEVICES];
      ctrl[0] = new ControlDevice(port, "wakeup_0", "toSleep_0");
      ctrl[0].addRetarder(retarder[0]);
      ctrl[0].addRetarder(retarder[1]);
      ctrl[0].addRetarder(retarder[2]);

      ctrl[1] = new ControlDevice(port, "wakeup_1", "toSleep_1");
      ctrl[1].addRetarder(retarder[3]);
      ctrl[1].addRetarder(retarder[4]);
      ctrl[1].addRetarder(retarder[5]);

      ctrl[2] = new ControlDevice(port, "wakeup_2", "toSleep_2");
      ctrl[2].addRetarder(retarder[6]);
      ctrl[2].addRetarder(retarder[7]);
      ctrl[2].addRetarder(retarder[8]);

      ctrl[3] = new ControlDevice(port, "wakeup_3", "toSleep_3");
      ctrl[3].addRetarder(retarder[9]);
      ctrl[3].addRetarder(retarder[10]);
      ctrl[3].addRetarder(retarder[11]);

      // VariLC Controller
      vlcCtrl = new ControlVLC(ctrl);

      // Paths
      path = new PathVLC[PATHS];
      Retarder[] path_0_retarders = {retarder[0], retarder[4], retarder[8]};
      Retarder[] path_1_retarders = {retarder[1], retarder[5], retarder[9]};
      Retarder[] path_2_retarders = {retarder[2], retarder[6], retarder[10]};
      Retarder[] path_3_retarders = {retarder[3], retarder[7], retarder[11]};

      path[0] = new PathVLC("I", vlcCtrl, path_0_retarders, SETTINGS);
      path[1] = new PathVLC("II", vlcCtrl, path_1_retarders, SETTINGS);
      path[2] = new PathVLC("III", vlcCtrl, path_2_retarders, SETTINGS);
      path[3] = new PathVLC("IV", vlcCtrl, path_3_retarders, SETTINGS);

      initializeSettings();

      // Panels
      PanelPathVLC panelPath_0 = new PanelPathVLC(path[0]);
      PanelPathVLC panelPath_1 = new PanelPathVLC(path[1]);
      PanelPathVLC panelPath_2 = new PanelPathVLC(path[2]);
      PanelPathVLC panelPath_3 = new PanelPathVLC(path[3]);
      //
      JPanel test = new JPanel();
      test.setLayout(new BoxLayout(test, BoxLayout.X_AXIS));
      test.add(panelPath_0);
      test.add(panelPath_1);
      test.add(panelPath_2);
      test.add(panelPath_3);
      FrameForTest f = new FrameForTest(test);
      f.setTitle("ScanningAperture VariLC Control");
   }


//---------------------------------------------------------------------------
   public void initializeSettings () {
      float swing = 0.25f;
      float extinctA = 0.25f;
      float extinctB = 0.50f;
      float atten = 0.5f;
      float LC_Swing = (float) edu.mbl.jif.utils.PrefsRT.usr.getDouble("LC_Swing", 0.10);
      for (int i = 0; i < path.length; i++) {
         path[i].setSetting(0, new float[] {extinctA, extinctB, atten});
         path[i].setSetting(1, new float[] {extinctA + LC_Swing, extinctB, atten});
         path[i].setSetting(2, new float[] {extinctA, extinctB + LC_Swing, atten});
         path[i].setSetting(3, new float[] {extinctA, extinctB - LC_Swing, atten});
         path[i].setSetting(4, new float[] {extinctA - LC_Swing, extinctB, atten});
         path[i].switchToSetting(0);
      }
   }


   public String getAllSettings () {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < path.length; i++) {
         sb.append("Path " + i + ":");
         path[i].getSettingsString();
         sb.append("\n");
      }
      return sb.toString();
   }


   public void listAllSettings () {
      System.out.println(getAllSettings());
   }


   public static void main (String[] args) {
      new ScanningApertureVariLC();
   }
}
