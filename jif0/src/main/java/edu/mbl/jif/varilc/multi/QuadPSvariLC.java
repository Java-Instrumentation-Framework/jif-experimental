package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.PanelPathVLC;
import edu.mbl.jif.varilc.multi.PathVLC;
import edu.mbl.jif.varilc.multi.ControlDevice;
import edu.mbl.jif.varilc.multi.Retarder;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import edu.mbl.jif.gui.test.FrameForTest;

import javax.swing.BoxLayout;
import java.text.NumberFormat;
import java.text.DecimalFormat;


import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.UIManager;
import edu.mbl.jif.utils.*;
import edu.mbl.jif.comm.SerialPortConnection;
import java.io.IOException;


public class QuadPSvariLC
{
   public final static int RETARDERS = 12;
   public final static int CHANNELS = 4;
   public final static int SETTINGS = 5;
   public final static int DEVICES = 1;

   public Retarder[] retarder;
   public PathVLC[] channel;
   public ControlVLC vlcCtrl;
   public ControlDevice[] ctrl;

   SerialPortConnection port;

   public QuadPSvariLC () {
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
      ctrl[0].addRetarder(retarder[3]);
      ctrl[0].addRetarder(retarder[4]);
      ctrl[0].addRetarder(retarder[5]);
      ctrl[0].addRetarder(retarder[6]);
      ctrl[0].addRetarder(retarder[7]);
      ctrl[0].addRetarder(retarder[8]);
      ctrl[0].addRetarder(retarder[9]);
      ctrl[0].addRetarder(retarder[10]);
      ctrl[0].addRetarder(retarder[11]);

      // VariLC Controller
      vlcCtrl = new ControlVLC(ctrl);

      // Channels
      channel = new PathVLC[CHANNELS];
      Retarder[] channel_0_retarders = {retarder[0], retarder[4], retarder[8]};
      Retarder[] channel_1_retarders = {retarder[1], retarder[5], retarder[9]};
      Retarder[] channel_2_retarders = {retarder[2], retarder[6], retarder[10]};
      Retarder[] channel_3_retarders = {retarder[3], retarder[7], retarder[11]};

      channel[0] = new PathVLC("I", vlcCtrl, channel_0_retarders, SETTINGS);
      channel[1] = new PathVLC("II", vlcCtrl, channel_1_retarders, SETTINGS);
      channel[2] = new PathVLC("III", vlcCtrl, channel_2_retarders, SETTINGS);
      channel[3] = new PathVLC("IV", vlcCtrl, channel_3_retarders, SETTINGS);

      initializeSettings();

      // Panels
      PanelPathVLC panelChannel_0 = new PanelPathVLC(channel[0]);
      PanelPathVLC panelChannel_1 = new PanelPathVLC(channel[1]);
      PanelPathVLC panelChannel_2 = new PanelPathVLC(channel[2]);
      PanelPathVLC panelChannel_3 = new PanelPathVLC(channel[3]);
      //
      JPanel test = new JPanel();
      test.setLayout(new BoxLayout(test, BoxLayout.X_AXIS)); 
      test.add(panelChannel_0);
      test.add(panelChannel_1);
      test.add(panelChannel_2);
      test.add(panelChannel_3);
      FrameForTest f = new FrameForTest(test);
      f.setTitle("Quad VariLC Control");
   }


//---------------------------------------------------------------------------
   public void initializeSettings () {
      float swing = 0.25f;
      float extinctA = 0.25f;
      float extinctB = 0.50f;
      float atten = 0.5f;
      float LC_Swing =
            (float) edu.mbl.jif.utils.PrefsRT.usr.getDouble("LC_Swing", 0.10);
      for (int i = 0; i < channel.length; i++) {
         channel[i].setSetting(0,
                               new float[] {extinctA, extinctB, atten});
         channel[i].setSetting(1,
                               new float[] {extinctA + LC_Swing, extinctB,
                               atten});
         channel[i].setSetting(2, new float[] {extinctA, extinctB + LC_Swing,
                               atten});
         channel[i].setSetting(3, new float[] {extinctA, extinctB - LC_Swing,
                               atten});
         channel[i].setSetting(
               4, new float[] {extinctA - LC_Swing, extinctB, atten});
         channel[i].switchToSetting(0);
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

      new QuadPSvariLC();

   }
}
