package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLC_RT;
import edu.mbl.jif.gui.dialog.DialogBoxI;
import edu.mbl.jif.utils.color.JifColor;
import edu.mbl.jif.utils.JifUtils;
import edu.mbl.jif.utils.prefs.Prefs;

import java.text.DecimalFormat;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;



// PanelVariLC_RT
/*
 * ((Unused)) 
 */

public class PanelVariLC_RT
      extends JPanel
{

   //
//   CycleThread cycleThread = null;
   final DecimalFormat fmtDec2 = new DecimalFormat("0.00");

   GridLayout gridLayout3 = new GridLayout();

   JLabel labelExtinctionRatio = new JLabel();
   JLabel labelExtinctionWarning = new JLabel();
   JLabel labelLastCalibrated = new JLabel();
   JLabel labelVariance = new JLabel();
   JLabel valueExtinctionRatio = new JLabel();
   JLabel valueVariance = new JLabel();
   JLabel value_LastCalibrated = new JLabel();
   JPanel panelExtRatio = new JPanel();
   JPanel panelVariance = new JPanel();

   private JButton button_Calibrate = new JButton();
   private JButton button_Defaults = new JButton();

   byte[] saturatedPixels8; // +++++
   private GridLayout gridLayout1 = new GridLayout();
   private JLabel jLabel9 = new JLabel();
   private JLabel label_variLCIcon1 = new JLabel();
   private JPanel panelCalibrate = new JPanel();

   private JLabel jLabelT1 = new JLabel();
   //private JPanel tab_Elements = new JPanel();
   PanelElementsVariLC pvlc = new PanelElementsVariLC();
   JPanel panelSettings = new JPanel();

   private SpinnerNumberModel modelMRSwing;
   private float maxRetVal = 0.0f;

   //JPanel panelChannels = new JPanel();
   //PanelChannelVLC vlcChannel_1, vlcChannel_2, vlcChannel_3, vlcChannel_4;
   //JPanel panelControllers = new JPanel();

   BorderLayout borderLayout1 = new BorderLayout();
   VariLC_RT vlc;

//   public PanelVariLC_RT () {
//      try {
//         new PanelVariLC_RT(new VariLC_RT(new SerialPortConnection()));
//      }
//      catch (Exception ex) {
//      }
//   }


//   public PanelVariLC_RT (VariLC_RT vlc) {
//      this.vlc = vlc;

   public PanelVariLC_RT () {
      try {
         jbInit();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public void setVariLC (VariLC_RT vlc) {
      this.vlc = vlc;
   }


   //Component initialization ==============================================
   private void jbInit () throws Exception {
      setBackground(JifColor.yellow[3]);
      setBorder(BorderFactory.createLoweredBevelBorder());
      setLayout(null);

      label_variLCIcon1.setIcon(JifUtils.loadImageIcon("variLC.gif", this.getClass()));
      label_variLCIcon1.setBounds(new Rectangle(206, 5, 0, 0));

      //------Element Tab & Buttons ------------------------------------------------

      // Get the defined elements from the VariLC and put into the spinners
//      if (vlc.isFunctional) {
//         vlc.getDefinedElements();
//      }

      // Calibration
      panelCalibrate.setBorder(BorderFactory.createLoweredBevelBorder());
      panelCalibrate.setLayout(null);

      button_Calibrate.setText("Calibrate");
      button_Calibrate.setBackground(new Color(157, 185, 236));
      button_Calibrate.setBounds(new Rectangle(11, 10, 101, 28));
      button_Calibrate.setEnabled(true);
      button_Calibrate.setFont(new java.awt.Font("Dialog", 1, 12));
      button_Calibrate.setToolTipText("Open Calibration Panel");
      button_Calibrate.setMargin(new Insets(2, 4, 2, 4));
      button_Calibrate.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Calibrate_actionPerformed(e);
         }
      });

//      toggle_Cycle.setFont(new java.awt.Font("Dialog", 0, 11));
//      toggle_Cycle.setMargin(new Insets(2, 4, 2, 4));
//      toggle_Cycle.setText("Cycle");
//      toggle_Cycle.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            toggle_Cycle_actionPerformed(e);
//         }
//      });
//      toggle_Cycle.setToolTipText("Cycle repeatedly thru non-extinction settings");
//      toggle_Cycle.setBounds(new Rectangle(10, 10, 65, 28));

      button_Defaults.setBounds(new Rectangle(13, 288, 104, 23));
      button_Defaults.setToolTipText("Set Retarders to Defaults");
      labelLastCalibrated.setFont(new java.awt.Font("Dialog", 0, 10));
      labelLastCalibrated.setHorizontalAlignment(SwingConstants.CENTER);
      labelLastCalibrated.setText("Last calibrated:");
      labelLastCalibrated.setBounds(new Rectangle(116, 14, 86, 18));
      value_LastCalibrated.setFont(new java.awt.Font("Dialog", 0, 10));
      value_LastCalibrated.setHorizontalAlignment(SwingConstants.CENTER);
      value_LastCalibrated.setText(Prefs.usr.get("calib_LastTime", "(unknown)"));
      value_LastCalibrated.setBounds(new Rectangle(200, 16, 137, 16));
      panelCalibrate.add(labelLastCalibrated, null);
      panelCalibrate.add(value_LastCalibrated, null);
      panelCalibrate.add(button_Calibrate, null);
      this.add(button_Defaults);
      panelCalibrate.setBounds(new Rectangle(8, 316, 340, 46));
      panelSettings.setMinimumSize(new Dimension(100, 100));
      panelSettings.setBounds(new Rectangle(10, 10, 338, 270));
      this.add(panelSettings, null);

      // Extinction Ration
      panelExtRatio.setBackground(JifColor.yellow[4]);
      panelExtRatio.setBorder(null);
      panelExtRatio.setBounds(new Rectangle(10, 191, 120, 54));
//      panelExtRatio.setLayout(gridLayout2);
//      gridLayout2.setColumns(1);
//      gridLayout2.setRows(3);
      labelExtinctionRatio.setFont(new java.awt.Font("Dialog", 0, 12));
      labelExtinctionRatio.setHorizontalAlignment(SwingConstants.CENTER);
      labelExtinctionRatio.setText("Extinction Ratio:");
      valueExtinctionRatio.setFont(new java.awt.Font("Dialog", 1, 12));
      valueExtinctionRatio.setHorizontalAlignment(SwingConstants.CENTER);
      valueExtinctionRatio.setText("-");

      labelExtinctionWarning.setFont(new java.awt.Font("Dialog", 1, 12));
      labelExtinctionWarning.setForeground(new Color(198, 0, 0));
      labelExtinctionWarning.setHorizontalAlignment(SwingConstants.CENTER);
      labelExtinctionWarning.setText("");
      panelVariance.setVisible(false);
      panelVariance.setBorder(null);
      panelVariance.setBounds(new Rectangle(209, 190, 104, 68));
      panelVariance.setLayout(gridLayout3);
      labelVariance.setFont(new java.awt.Font("Dialog", 0, 12));
      labelVariance.setHorizontalAlignment(SwingConstants.CENTER);
      labelVariance.setText("Variance:");
      gridLayout3.setColumns(1);
      gridLayout3.setHgap(0);
      gridLayout3.setRows(3);
      valueVariance.setHorizontalAlignment(SwingConstants.CENTER);
      valueVariance.setText("-");
      panelExtRatio.add(labelExtinctionRatio, null);
      panelExtRatio.add(valueExtinctionRatio, null);
      panelExtRatio.add(labelExtinctionWarning, null);
      panelSettings.add(panelVariance);
      panelSettings.add(panelExtRatio, null);
      panelVariance.add(labelVariance, null);
      panelVariance.add(valueVariance, null);
      this.add(panelCalibrate, null);
      panelSettings.add(pvlc, null);
      button_Defaults.setFont(new java.awt.Font("Dialog", 0, 10));
      button_Defaults.setMargin(new Insets(2, 4, 2, 4));
      button_Defaults.setText("Reset to Defaults");
      button_Defaults.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Defaults_actionPerformed(e);
         }
      });

      // Settings panel
      panelSettings.setPreferredSize(new Dimension(100, 100));
      panelSettings.setBorder(BorderFactory.createEtchedBorder());
      panelSettings.setLayout(null);
      panelSettings.setBackground(JifColor.yellow[4]); }


   /////////////////////////////////////////////////////////////////////////
   // Set Retarders to Default values
   //
   void button_Defaults_actionPerformed (ActionEvent e) {
//      stopCycle();
      JifUtils.waitFor(100);
      if (DialogBoxI.boxConfirm("Reset VariLC to Default Settings", "Are you sure?")) {
         vlc.setRetardersToDefaults();
         //	setElementsToArrayValues();
//         updateLCSpinnerValues();
         if (!vlc.statusCheck()) {
            if (DialogBoxI.boxConfirm("VariLC Error", "VariLC Reports an Error.  Reset?")) {
               vlc.reset();
            }
         }
      }
   }


   //////////////////////////////////////////////////////////////////////
   // Calibration - open and run calibration frame
   //
   void button_Calibrate_actionPerformed (ActionEvent e) {
      /*
           stopCycle();
           // +++ check that nothing else going on...
           PSjUtils.waitFor(100);
           //long binningWas = Camera.binning;
           //    if (binningWas < 2) {
           //      DialogBoxes.boxError("Cannot Do Calibration",
           //          "Camera binning must be set to\n" + " 2x2 for Calibration");
           //      return;
           //    }
           //        try {
           //          Camera.display.setSelected(true);
           //        } catch (java.beans.PropertyVetoException pve) {}
           PanelCalibrate panelC = new PanelCalibrate();
           InternalFrameModal calFrame =
         new InternalFrameModal("Calibration", panelC, 20, 50);
           panelC.addCloseButtonListner(calFrame);
           calFrame.setLocation(5, 50);
           calFrame.setSize(new Dimension(490, 760));
           Calibrator.setCalibPanel(panelC);
           calFrame.setModal();
           //    final SwingWorker worker =
           //        new SwingWorker() {
           //      public Object construct() {
           //        try {
           //          Camera.display.setSelected(true);
           //        } catch (java.beans.PropertyVetoException pve) {}
           //        PanelCalibrate panelC = new PanelCalibrate();
           //        InternalFrameModal calFrame = new InternalFrameModal("Calibration",
           //            panelC, 20, 50);
           //        calFrame.setLocation(5, 5);
           //        calFrame.setSize(new Dimension(490, 760));
           //        Calibrator.setCalibPanel(panelC);
           //        calFrame.setModal();
           //        return null;
           //      }
           //
           //      public void finished() {
           //      }
           //    };
           //    worker.start();
         }

         //      PSj.calibFrame = new FrameCalibrate();
         //      PSj.deskTopFrame.addFrame(PSj.calibFrame);
         //    }
         //    PSj.calibFrame.setup();
         //    PSj.calibFrame.setLocation(
         //    PSj.calibFrame.setVisible(true);
         //    try {
         //      PSj.calibFrame.setSelected(true);
         //    } catch (java.beans.PropertyVetoException pve) {}
       */
   }


   //----------------------------------------------------------------
   //
   public static double stdDev (double[] data) {
      return Math.sqrt(variance(data));
   }


   public static double variance (double[] data) {
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


   //public void synchValues() {
   //float wave = (float) Prefs.usr.getDouble("wavelength", 546);
   //float LC_SwingVal = (float) Prefs.usr.getDouble("LC_Swing", 0.03);
   //setLCSwing(LC_SwingVal);
   //value_LCSwing.setText(String.valueOf(LC_SwingVal));
   //modelMRSwing.setValue(new Float(LC_SwingVal * wave));
   //repaint();
   //}
   //
   ////////////////////////////////////////////////////////////////////////\
   // Cycle Thread
   //////////////////////////////////////////////////////////////////
   // Cycle thru settings 1-4
   //
//   void toggle_Cycle_actionPerformed (ActionEvent e) {
//      if (toggle_Cycle.isSelected()) {
//         // Create and start the thread
//         cycleThread = new CycleThread();
//         cycleThread.start();
//      } else {
//         stopCycle();
//      }
//   }
//
//
//   void cycleStop () {
//      // Stop the thread
//      cycleThread.allDone = true;
//   }
//
//
//   public void stopCycle () {
//      if (cycleThread != null) {
//         cycleStop();
//         PSjUtils.waitFor(250);
//         toggle_Cycle.setSelected(false);
//         vlc.selectElement(1);
//         cycleThread = null;
//      }
//   }


   //----------------------------------------------------------------
   //
//   class CycleThread
//         extends Thread
//   {
//      boolean allDone = false;
//
//      public void run () {
//         while (true) {
//            // Do work...
//            vlc.selectElement(1);
//            //showSelectedElement();
//            PSjUtils.waitFor((int) (vlc.getSettlingTime() + (Camera.exposure / 1000)
//                  + Prefs.usr.getInt("variLC.cycleTime", 50)));
//            //        vlc.selectElement(2);
//            //        showSelectedElement();
//            //        PSjUtils.waitFor((int) (vlc.getSettlingTime()
//            //          + (CameraInterface.exposure / 1000) + 20));
//            //        vlc.selectElement(3);
//            //        showSelectedElement();
//            //        PSjUtils.waitFor((int) (vlc.getSettlingTime()
//            //          + (CameraInterface.exposure / 1000) + 20));
//            vlc.selectElement(2);
//            //showSelectedElement();
//            PSjUtils.waitFor((int) (vlc.getSettlingTime() + (Camera.exposure / 1000)
//                  + Prefs.usr.getInt("variLC.cycleTime", 50)));
//            //
//            if (allDone) {
//               vlc.selectElement(1);
//               showSelectedElement();
//               return;
//            }
//         }
//      }
//   }
}
