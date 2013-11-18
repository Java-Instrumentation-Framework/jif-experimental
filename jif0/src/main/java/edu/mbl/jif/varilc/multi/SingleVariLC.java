package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.ControlDevice;
import edu.mbl.jif.varilc.multi.Retarder;
import javax.swing.JPanel;
import edu.mbl.jif.gui.test.FrameForTest;

import javax.swing.BoxLayout;


import edu.mbl.jif.utils.*;
import edu.mbl.jif.comm.SerialPortConnection;
import java.io.IOException;


public class SingleVariLC
{
   public final static int RETARDERS = 2;
   public final static int PATHS = 1;
   public final static int SETTINGS = 5;
   public final static int DEVICES = 1;

   public Retarder[] retarder;
   public PathVLC[] path;
   public ControlVLC vlcCtrl;
   public ControlDevice[] ctrl;

   SerialPortConnection port;

   public SingleVariLC () {
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
         System.out.println("Could not open comPort:" + commPort +
                            ": " + ex1.getMessage());
      }
      catch (Exception ex) {
         System.err.println(
               "Could not open comPort:" + commPort);
         return;
      }

   }


   // Quad Attenuator settings (?)
   //  [0] = 0.51
   //  [1] = 0.31
   //  [2] = 0.43
   //  [3] = 0.38
   //-------
   // 1 | 0
   // -----
   // 3 | 2

   public void createVLCControl () {
      // Retarders
      retarder = new Retarder[RETARDERS];
      retarder[0] = new Retarder(Retarder.CELL_0_DEG, 0.5f, 0.01f, 0.99f);
      retarder[1] = new Retarder(Retarder.CELL_45_DEG, 0.5f, 0.01f, 0.99f);

      ctrl = new ControlDevice[DEVICES];
      ctrl[0] = new ControlDevice(port, "wakeup_0", "toSleep_0");
      ctrl[0].addRetarder(retarder[0]);
      ctrl[0].addRetarder(retarder[1]);


      // VariLC Controller
      vlcCtrl = new ControlVLC(ctrl);

      // Channels / Optical paths
      path = new PathVLC[PATHS];
      Retarder[] path_0_retarders = {retarder[0], retarder[1]};

      path[0] = new PathVLC("I", vlcCtrl, path_0_retarders, SETTINGS);

      initializeSettings();

      // Panels
      PanelPathVLC panelChannel_0 = new PanelPathVLC(path[0]);
      //
      JPanel test = new JPanel();
      test.setLayout(new BoxLayout(test, BoxLayout.X_AXIS)); 
      test.add(panelChannel_0);
      FrameForTest f = new FrameForTest(test);
      f.setTitle("Quad VariLC Control");
   }


//---------------------------------------------------------------------------
   public void initializeSettings () {
      float swing = 0.25f;
      float extinctA = 0.25f;
      float extinctB = 0.50f;
      float LC_Swing =
            (float) edu.mbl.jif.utils.PrefsRT.usr.getDouble("LC_Swing", 0.10);
      for (int i = 0; i < path.length; i++) {
         path[i].setSetting(0, new float[] {extinctA, extinctB});
         path[i].setSetting(1, new float[] {extinctA + LC_Swing, extinctB});
         path[i].setSetting(2, new float[] {extinctA, extinctB + LC_Swing});
         path[i].setSetting(3, new float[] {extinctA, extinctB - LC_Swing});
         path[i].setSetting(4, new float[] {extinctA - LC_Swing, extinctB});
         path[i].switchToSetting(0);
      }
   }


   public static void main (String[] args) {
//    MetalLookAndFeel lookFeel =
//      new com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel();
//    try {
//      UIManager.setLookAndFeel(lookFeel);
//      //SwingUtilities.updateComponentTreeUI();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

      new SingleVariLC();

   }
}
