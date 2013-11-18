package edu.mbl.jif.varilc;

import edu.mbl.jif.gui.internal.InternalFrameModal;
import edu.mbl.jif.varilc.camacq.VariLC_RT;
import edu.mbl.jif.utils.JifUtils;
import java.beans.PropertyVetoException;
import java.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import edu.mbl.jif.gui.*;
import edu.mbl.jif.gui.plot.PlotPanel;
import edu.mbl.jif.gui.plot.PlotXYPanel;
import java.awt.Rectangle;
import java.awt.Font;
import edu.mbl.jif.comm.SerialPortConnection;


public class PanelCalibrate
      extends JPanel
{
   BorderLayout borderLayout1 = new BorderLayout();
   private JButton button_SetElement = new JButton();
   private JPanel panel_main = new JPanel();
   private JPanel panel_Calib = new JPanel();
   private JTextArea text_Calib = new JTextArea();
   private JButton button_Calib = new JButton();
   private JLabel label_CalibDone = new JLabel();
   private JTextArea text_Zero = new JTextArea();
   private JPanel panel_Zero = new JPanel();
   private JButton button_Zero = new JButton();
   private JLabel label_Zero = new JLabel();
   private JLabel ret2B = new JLabel();
   private JLabel ret2A = new JLabel();
   private JLabel jLabelT1 = new JLabel();
   private JLabel label_i4 = new JLabel();
   private JLabel label_i3 = new JLabel();
   private JLabel label_i2 = new JLabel();
   private JLabel label_i1 = new JLabel();
   private JPanel panel_Elements = new JPanel();
   private JLabel label_i0 = new JLabel();
   private JLabel jLabelC2 = new JLabel();
   private JLabel jLabelC1 = new JLabel();
   private JLabel ret0B = new JLabel();
   private JLabel ret3B = new JLabel();
   private JLabel ret0A = new JLabel();
   private JLabel ret3A = new JLabel();
   private GridLayout gridLayout1 = new GridLayout();
   private JLabel jLabel9 = new JLabel();
   private JLabel ret1B = new JLabel();
   private JLabel ret4B = new JLabel();
   private JLabel ret1A = new JLabel();
   private JLabel ret4A = new JLabel();
   public JLabel label_E3 = new JLabel();
   public JLabel label_E4 = new JLabel();
   public JLabel label_E5 = new JLabel();
   public JLabel label_E2 = new JLabel();
   public JLabel label_E1 = new JLabel();
   private boolean doAveragingOnAcq = false;
   private NumberFormat f1;
   private NumberFormat f3;
   NumberFormat fmtDec2 = new DecimalFormat("#.00");
   JButton button_Cancel = new JButton();
   PlotPanel panelPlot = new PlotPanel();
   PlotXYPanel panelPlotXY = new PlotXYPanel();
   JButton buttonClose = new JButton();


   Calibrator calib;

   public PanelCalibrate () {
      try {
         jbInit();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   void jbInit () throws Exception {
      this.setLayout(borderLayout1);
      //
      f1 = NumberFormat.getNumberInstance();
      f1.setMinimumFractionDigits(1);
      f1.setMaximumFractionDigits(2);
      f3 = NumberFormat.getNumberInstance();
      f3.setMinimumFractionDigits(3);
      //
      panel_main.setBackground(new Color(215, 215, 165));
      panel_main.setBorder(BorderFactory.createEtchedBorder());
      panel_main.setLayout(null);

      panel_Calib.setLayout(null);
      panel_Calib.setBounds(new Rectangle(5, 5, 462, 270));
      panel_Calib.setToolTipText("");
      panel_Calib.setBorder(BorderFactory.createEtchedBorder());
      text_Calib.setFont(new java.awt.Font("Dialog", Font.BOLD, 14));
      text_Calib.setOpaque(false);
      text_Calib.setEditable(false);
      text_Calib.setMargin(new Insets(2, 2, 2, 2));
      text_Calib.setBounds(new Rectangle(9, 6, 177, 28));
      text_Calib.setText("VariLC Calibration");

      button_Calib.setBounds(new Rectangle(12, 33, 105, 31));
      button_Calib.setEnabled(true);
      button_Calib.setMargin(new Insets(2, 4, 2, 4));
      button_Calib.setBackground(new Color(147, 173, 221));
      button_Calib.setText("Calibrate");
      button_Calib.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Calib_actionPerformed(e);
         }
      });

      label_CalibDone.setBounds(new Rectangle(15, 203, 33, 30));
      label_CalibDone.setToolTipText("");
      label_CalibDone.setBackground(Color.pink);

      ret2B.setFont(new java.awt.Font("Dialog", 0, 10));
      ret2B.setHorizontalAlignment(SwingConstants.CENTER);
      ret2A.setFont(new java.awt.Font("Dialog", 0, 10));
      ret2A.setHorizontalAlignment(SwingConstants.CENTER);
      jLabelT1.setText(" ");
      jLabelT1.setHorizontalAlignment(SwingConstants.CENTER);
      label_i4.setText("--");
      label_i4.setHorizontalTextPosition(SwingConstants.RIGHT);
      label_i4.setHorizontalAlignment(SwingConstants.CENTER);
      label_i4.setPreferredSize(new Dimension(20, 17));
      label_i4.setFont(new java.awt.Font("Dialog", 0, 10));
      label_i4.setMinimumSize(new Dimension(20, 17));
      label_i3.setText("--");
      label_i3.setHorizontalTextPosition(SwingConstants.RIGHT);
      label_i3.setHorizontalAlignment(SwingConstants.CENTER);
      label_i3.setPreferredSize(new Dimension(20, 17));
      label_i3.setFont(new java.awt.Font("Dialog", 0, 10));
      label_i3.setMinimumSize(new Dimension(20, 17));
      label_i2.setText("--");
      label_i2.setHorizontalTextPosition(SwingConstants.RIGHT);
      label_i2.setHorizontalAlignment(SwingConstants.CENTER);
      label_i2.setPreferredSize(new Dimension(20, 17));
      label_i2.setFont(new java.awt.Font("Dialog", 0, 10));
      label_i2.setMinimumSize(new Dimension(20, 17));
      label_i1.setText("--");
      label_i1.setHorizontalTextPosition(SwingConstants.RIGHT);
      label_i1.setHorizontalAlignment(SwingConstants.CENTER);
      label_i1.setPreferredSize(new Dimension(20, 17));
      label_i1.setFont(new java.awt.Font("Dialog", 0, 10));
      label_i1.setMinimumSize(new Dimension(20, 17));
      panel_Elements.setLayout(gridLayout1);
      panel_Elements.setBounds(new Rectangle(202, 12, 248, 116));
      panel_Elements.setBorder(BorderFactory.createEtchedBorder());
      label_i0.setText("--");
      label_i0.setHorizontalTextPosition(SwingConstants.RIGHT);
      label_i0.setHorizontalAlignment(SwingConstants.CENTER);
      label_i0.setPreferredSize(new Dimension(20, 17));
      label_i0.setFont(new java.awt.Font("Dialog", 0, 10));
      label_i0.setMinimumSize(new Dimension(20, 17));
      label_E2.setText("  Element 2");
      label_E2.setFont(new java.awt.Font("Dialog", 1, 10));
      jLabelC2.setText("LC-B");
      jLabelC2.setFont(new java.awt.Font("Dialog", 0, 10));
      jLabelC2.setHorizontalAlignment(SwingConstants.CENTER);
      jLabelC1.setText("LC-A");
      jLabelC1.setFont(new java.awt.Font("Dialog", 0, 10));
      jLabelC1.setHorizontalAlignment(SwingConstants.CENTER);
      ret0B.setFont(new java.awt.Font("Dialog", 0, 10));
      ret0B.setHorizontalAlignment(SwingConstants.CENTER);
      ret3B.setFont(new java.awt.Font("Dialog", 0, 10));
      ret3B.setHorizontalAlignment(SwingConstants.CENTER);
      ret0A.setFont(new java.awt.Font("Dialog", 0, 10));
      ret0A.setHorizontalAlignment(SwingConstants.CENTER);
      ret3A.setFont(new java.awt.Font("Dialog", 0, 10));
      ret3A.setHorizontalAlignment(SwingConstants.CENTER);
      gridLayout1.setRows(6);
      gridLayout1.setColumns(3);
      jLabel9.setText("Intensity");
      jLabel9.setHorizontalTextPosition(SwingConstants.CENTER);
      jLabel9.setHorizontalAlignment(SwingConstants.CENTER);
      jLabel9.setPreferredSize(new Dimension(20, 17));
      jLabel9.setMinimumSize(new Dimension(20, 17));
      jLabel9.setFont(new java.awt.Font("Dialog", 0, 10));
      label_E3.setText("  Element 3");
      label_E3.setFont(new java.awt.Font("Dialog", 1, 10));
      label_E4.setText("  Element 4");
      label_E4.setFont(new java.awt.Font("Dialog", 1, 10));
      label_E5.setText("  Element 5");
      label_E5.setFont(new java.awt.Font("Dialog", 1, 10));
      label_E1.setFont(new java.awt.Font("Dialog", 1, 10));
      label_E1.setText("  Element 1");
      ret1B.setFont(new java.awt.Font("Dialog", 0, 10));
      ret1B.setHorizontalAlignment(SwingConstants.CENTER);
      ret4B.setFont(new java.awt.Font("Dialog", 0, 10));
      ret4B.setHorizontalAlignment(SwingConstants.CENTER);
      ret1A.setFont(new java.awt.Font("Dialog", 0, 10));
      ret1A.setHorizontalAlignment(SwingConstants.CENTER);
      ret4A.setFont(new java.awt.Font("Dialog", 0, 10));
      ret4A.setHorizontalAlignment(SwingConstants.CENTER);


      button_Cancel.setBounds(new Rectangle(14, 171, 85, 29));
      button_Cancel.setEnabled(false);
      button_Cancel.setText("Cancel");
      button_Cancel.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Cancel_actionPerformed(e);
         }
      });

      panelPlot.setBackground(Color.gray);
      panelPlot.setBorder(null);
      panelPlot.setBounds(new Rectangle(203, 141, 247, 113));
      panelPlotXY.setBackground(Color.gray);
      panelPlotXY.setBorder(BorderFactory.createLoweredBevelBorder());
      panelPlotXY.setBounds(new Rectangle(251, 49, 199, 115));
      panelPlot.add(panelPlotXY);

      buttonClose.setBounds(new Rectangle(13, 206, 85, 28));
      buttonClose.setEnabled(true);
      buttonClose.setText("Close");

//      panel_Zero.add(label_Zero, null);
//      panel_Zero.add(button_Zero, null);
//      panel_Zero.add(text_Zero, null);

      panel_Elements.add(jLabelT1, null);
      panel_Elements.add(jLabelC1, null);
      panel_Elements.add(jLabelC2, null);
      panel_Elements.add(jLabel9, null);
      panel_Elements.add(label_E1, null);
      panel_Elements.add(ret0A);
      panel_Elements.add(ret0B);
      panel_Elements.add(label_i0, null);
      panel_Elements.add(label_E2, null);
      panel_Elements.add(ret1A);
      panel_Elements.add(ret1B);
      panel_Elements.add(label_i1, null);
      panel_Elements.add(label_E3, null);
      panel_Elements.add(ret2A);
      panel_Elements.add(ret2B);
      panel_Elements.add(label_i2, null);
      panel_Elements.add(label_E4, null);
      panel_Elements.add(ret3A);
      panel_Elements.add(ret3B);
      panel_Elements.add(label_i3, null);
      panel_Elements.add(label_E5, null);
      panel_Elements.add(ret4A);
      panel_Elements.add(ret4B);
      panel_Elements.add(label_i4, null);
      panel_Calib.add(button_Calib);

      panel_Calib.add(text_Calib, null);
      panel_Calib.add(buttonClose);
      panel_Calib.add(button_Cancel);
      panel_Calib.add(panelPlot, null);
      panel_Calib.add(label_CalibDone, null);
      panel_Calib.add(panel_Elements, null);
      //panel_main.add(panel_Zero, null);
      panel_main.add(panel_Calib, null);
      // ==== panel_main.add(panelFullCalib, null);

      add(panel_main, BorderLayout.CENTER);
      setButtonsEnabled(true);
   }


   public void addCloseButtonListner (final InternalFrameModal frame) {
      buttonClose.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            try {
               frame.setClosed(true);
            }
            catch (PropertyVetoException ex) {
            }
         }
      });
   }


//   public boolean setup () {
//      if (PSj.acqCtrlr == null) {
//         PSj.acqCtrlr = new AcqController();
//      }
//      if (!PSj.acqCtrlr.acqReady()) {
//         return false;
//      }
//      return true;
//   }


   ////////////////////////////////////////////////////////////////////////
   // Calibrate
   //
   void button_Calib_actionPerformed (ActionEvent e) {
      doCalibration();
   }


   void doCalibration () {
      showTask("Calibrating...");
      label_CalibDone.setIcon(null);
      text_Calib.setForeground(Color.red);
      text_Calib.setFont(new Font("Serif", Font.BOLD, 18));
      text_Calib.setText("Calibrating...");
      plotReset();
      label_E1.setForeground(Color.darkGray);
      label_E2.setForeground(Color.darkGray);
      label_E3.setForeground(Color.darkGray);
      label_E4.setForeground(Color.darkGray);
      label_E5.setForeground(Color.darkGray);
      //
      Calibrator calib = new Calibrator(new VariLC_RT(new SerialPortConnection()));
      calib.runCalibrationOfVariLC(); // << =======
      //
      label_CalibDone.setIcon(JifUtils.loadImageIcon("check.gif", this.getClass()));
      text_Calib.setText("Calibration\ncompleted.");
      //psj.PSjUtils.event("Calibration completed.");
      restoreControls();
      //psj.PSjUtils.soundDone();
   }


   //----------------------------------------------------------------
   //
   void button_Cancel_actionPerformed (ActionEvent e) {
      calib.cancel();
      restoreControls();
   }


   //  void buttonClose_actionPerformed(ActionEvent e) {
   //    WindowEvent evt = new WindowEvent(getParent(), WindowEvent.WINDOW_CLOSING);
   //    InternalFrameEvent evt = new InternalFrameEvent( (JInternalFrame) getParent(),
   //        InternalFrameEvent.INTERNAL_FRAME_CLOSED);
   //    ( (JInternalFrame) getParent()).dispatchEvent(evt);
   //    processWindowEvent(evt);
   //    this.getParent().dispatchEvent(
   //        new WindowEvent( (Window) getParent(), WindowEvent.WINDOW_CLOSING));
   //  }


   public void updateValues (final double i0, final double i1, final double i2,
         final double i3, final double i4, final int step) {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            if (step > 0) {
               label_E1.setForeground(Color.blue);
            }
            if (step > 1) {
               label_E2.setForeground(Color.blue);
            }
            if (step > 2) {
               label_E3.setForeground(Color.blue);
            }
            if (step > 3) {
               label_E4.setForeground(Color.blue);
            }
            if (step > 4) {
               label_E5.setForeground(Color.blue);
            }
            label_i0.setText(f1.format(i0));
            label_i1.setText(f1.format(i1));
            label_i2.setText(f1.format(i2));
            label_i3.setText(f1.format(i3));
            label_i4.setText(f1.format(i4));
//            ret0A.setText(f3.format(PSj.variLC.getElementA(0)));
//            ret0B.setText(f3.format(PSj.variLC.getElementB(0)));
//            ret1A.setText(f3.format(PSj.variLC.getElementA(1)));
//            ret1B.setText(f3.format(PSj.variLC.getElementB(1)));
//            ret2A.setText(f3.format(PSj.variLC.getElementA(2)));
//            ret2B.setText(f3.format(PSj.variLC.getElementB(2)));
//            ret3A.setText(f3.format(PSj.variLC.getElementA(3)));
//            ret3B.setText(f3.format(PSj.variLC.getElementB(3)));
//            ret4A.setText(f3.format(PSj.variLC.getElementA(4)));
//            ret4B.setText(f3.format(PSj.variLC.getElementB(4)));
            repaint();
            //psj.PSjUtils.statusProgress("Calibrating VariLC...", (100 * (step + 1)) / 5);
         }
      });
   }


   //----------------------------------------------------------------
   //
   void showTask (String task) {
      setButtonsEnabled(false);
      //((BlockingGlassPane) PSj.deskTopFrame.getGlassPane()).block(true);
      //setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
   }


   public void restoreControls () {
      //Camera.setDisplayOn();
      //CameraInterface.setExposureOnly( (long) (Calibrator.exposureSetting));
      setButtonsEnabled(true);
      //((BlockingGlassPane) PSj.deskTopFrame.getGlassPane()).block(false);
   }


   void setButtonsEnabled (boolean t) {
      button_Zero.setEnabled(t);
      button_Calib.setEnabled(t);
      buttonClose.setEnabled(t);
      button_Cancel.setEnabled(!t);
      button_Cancel.setVisible(!t);
   }


   public void plotPoint (double diff) {
      panelPlot.plotPoint(diff);
   }


   public void plotReset () {
      panelPlot.setLast(0);
   }

   //----------------------------------------------------------------
   //

}
