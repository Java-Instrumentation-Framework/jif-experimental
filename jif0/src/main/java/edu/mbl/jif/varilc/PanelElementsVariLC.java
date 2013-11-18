package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLC_RT;
import java.text.DecimalFormat;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.*;

import edu.mbl.jif.camera.Camera;
import edu.mbl.jif.gui.swingthread.SwingWorker3;
import edu.mbl.jif.utils.*;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.utils.prefs.Prefs;
//import psj.PSjUtils;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PanelElementsVariLC
      extends JPanel
{
   BorderLayout borderLayout1 = new BorderLayout();
   private JLabel jLabelT1 = new JLabel();
   private JLabel jLabelT2 = new JLabel();
   private JLabel jLabelC1 = new JLabel();
   private JLabel jLabelC2 = new JLabel();
   private JLabel label_i0 = new JLabel();
   private JLabel label_i1 = new JLabel();
   private JLabel label_i2 = new JLabel();
   private JLabel label_i3 = new JLabel();
   private JLabel label_i4 = new JLabel();
   private JLabel jLabel9 = new JLabel();
   double[] intensity = new double[5];
   private JToggleButton button_Element_1 = new JToggleButton();
   private JToggleButton button_Element_2 = new JToggleButton();
   private JToggleButton button_Element_3 = new JToggleButton();
   private JToggleButton button_Element_4 = new JToggleButton();
   private JToggleButton button_Element_5 = new JToggleButton();
   private JToggleButton toggle_Cycle = new JToggleButton();

   float step = 0.002f; // LC Element Spinner Increment
   double minSetA = 0.001;
   double maxSetA = 0.999;
   double minSetB = 0.001;
   double maxSetB = 0.999;

   JSpinner spinner0 = new JSpinner();
   JSpinner spinner1 = new JSpinner();
   JSpinner spinner2 = new JSpinner();
   JSpinner spinner3 = new JSpinner();
   JSpinner spinner4 = new JSpinner();
   JSpinner spinner5 = new JSpinner();
   JSpinner spinner6 = new JSpinner();
   JSpinner spinner7 = new JSpinner();
   JSpinner spinner8 = new JSpinner();
   JSpinner spinner9 = new JSpinner();

   SpinnerNumberModel model0 = new SpinnerNumberModel(0.5, minSetA, maxSetA, step);
   SpinnerNumberModel model1 = new SpinnerNumberModel(0.5, minSetB, maxSetB, step);
   SpinnerNumberModel model2 = new SpinnerNumberModel(0.5, minSetA, maxSetA, step);
   SpinnerNumberModel model3 = new SpinnerNumberModel(0.5, minSetB, maxSetB, step);
   SpinnerNumberModel model4 = new SpinnerNumberModel(0.5, minSetA, maxSetA, step);
   SpinnerNumberModel model5 = new SpinnerNumberModel(0.5, minSetB, maxSetB, step);
   SpinnerNumberModel model6 = new SpinnerNumberModel(0.5, minSetA, maxSetA, step);
   SpinnerNumberModel model7 = new SpinnerNumberModel(0.5, minSetB, maxSetB, step);
   SpinnerNumberModel model8 = new SpinnerNumberModel(0.5, minSetA, maxSetA, step);
   SpinnerNumberModel model9 = new SpinnerNumberModel(0.5, minSetB, maxSetB, step);

   private JButton button_CheckIntensities = new JButton();
   GridLayout gridLayout1 = new GridLayout();
   VariLC_RT vlc;

   public PanelElementsVariLC () {
      try {
         new VariLC_RT(new SerialPortConnection());
      }
      catch (Exception ex) {
      }

   }


   public PanelElementsVariLC (VariLC_RT vlc) {
      this.vlc = vlc;
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      this.setLayout(gridLayout1);
      gridLayout1.setColumns(4);
      gridLayout1.setRows(6);
      setBorder(BorderFactory.createLoweredBevelBorder());
      setBounds(new Rectangle(10, 10, 282, 171));
      model0.setValue(vlc.getElementA(0));
      model1.setValue(vlc.getElementB(0));
      model2.setValue(vlc.getElementA(1));
      model3.setValue(vlc.getElementB(1));
      model4.setValue(vlc.getElementA(2));
      model5.setValue(vlc.getElementB(2));
      model6.setValue(vlc.getElementA(3));
      model7.setValue(vlc.getElementB(3));
      model8.setValue(vlc.getElementA(4));
      model9.setValue(vlc.getElementB(4));
      spinner0.setModel(model0);
      spinner1.setModel(model1);
      spinner2.setModel(model2);
      spinner3.setModel(model3);
      spinner4.setModel(model4);
      spinner5.setModel(model5);
      spinner6.setModel(model6);
      spinner7.setModel(model7);
      spinner8.setModel(model8);
      spinner9.setModel(model9);
      JSpinner.NumberEditor neditor0 = new JSpinner.NumberEditor(spinner0, ".000");
      JSpinner.NumberEditor neditor1 = new JSpinner.NumberEditor(spinner1, ".000");
      JSpinner.NumberEditor neditor2 = new JSpinner.NumberEditor(spinner2, ".000");
      JSpinner.NumberEditor neditor3 = new JSpinner.NumberEditor(spinner3, ".000");
      JSpinner.NumberEditor neditor4 = new JSpinner.NumberEditor(spinner4, ".000");
      JSpinner.NumberEditor neditor5 = new JSpinner.NumberEditor(spinner5, ".000");
      JSpinner.NumberEditor neditor6 = new JSpinner.NumberEditor(spinner6, ".000");
      JSpinner.NumberEditor neditor7 = new JSpinner.NumberEditor(spinner7, ".000");
      JSpinner.NumberEditor neditor8 = new JSpinner.NumberEditor(spinner8, ".000");
      JSpinner.NumberEditor neditor9 = new JSpinner.NumberEditor(spinner9, ".000");
      spinner0.setEnabled(false);
      spinner1.setEnabled(false);
      spinner2.setEnabled(false);
      spinner3.setEnabled(false);
      spinner4.setEnabled(false);
      spinner5.setEnabled(false);
      spinner6.setEnabled(false);
      spinner7.setEnabled(false);
      spinner8.setEnabled(false);
      spinner9.setEnabled(false);
      gridLayout1.setColumns(4);
      gridLayout1.setRows(6);
      jLabelT1.setHorizontalAlignment(SwingConstants.CENTER);
      jLabelT1.setText(" Setting");
      jLabelC1.setHorizontalAlignment(SwingConstants.CENTER);
      jLabelC1.setText("LC-A");
      jLabelC2.setHorizontalAlignment(SwingConstants.CENTER);
      jLabelC2.setText("LC-B");
      jLabel9.setFont(new java.awt.Font("Dialog", 0, 11));
      jLabel9.setMinimumSize(new Dimension(20, 17));
      jLabel9.setPreferredSize(new Dimension(20, 17));
      jLabel9.setHorizontalAlignment(SwingConstants.CENTER);
      jLabel9.setHorizontalTextPosition(SwingConstants.CENTER);
      jLabel9.setText("Intensity");
      button_Element_1.setBorder(BorderFactory.createRaisedBevelBorder());
      button_Element_2.setBorder(BorderFactory.createRaisedBevelBorder());
      button_Element_3.setBorder(BorderFactory.createRaisedBevelBorder());
      button_Element_4.setBorder(BorderFactory.createRaisedBevelBorder());
      button_Element_5.setBorder(BorderFactory.createRaisedBevelBorder());

      label_i0.setMinimumSize(new Dimension(20, 17));
      label_i0.setPreferredSize(new Dimension(20, 17));
      label_i0.setHorizontalAlignment(SwingConstants.CENTER);
      label_i0.setHorizontalTextPosition(SwingConstants.CENTER);
      label_i0.setText("--");

      label_i1.setMinimumSize(new Dimension(20, 17));
      label_i1.setPreferredSize(new Dimension(20, 17));
      label_i1.setHorizontalAlignment(SwingConstants.CENTER);
      label_i1.setHorizontalTextPosition(SwingConstants.CENTER);
      label_i1.setText("--");

      label_i2.setMinimumSize(new Dimension(20, 17));
      label_i2.setPreferredSize(new Dimension(20, 17));
      label_i2.setHorizontalAlignment(SwingConstants.CENTER);
      label_i2.setHorizontalTextPosition(SwingConstants.CENTER);
      label_i2.setText("--");

      label_i3.setMinimumSize(new Dimension(20, 17));
      label_i3.setPreferredSize(new Dimension(20, 17));
      label_i3.setHorizontalAlignment(SwingConstants.CENTER);
      label_i3.setHorizontalTextPosition(SwingConstants.CENTER);
      label_i3.setText("--");
      label_i4.setMinimumSize(new Dimension(20, 17));
      label_i4.setPreferredSize(new Dimension(20, 17));
      label_i4.setHorizontalAlignment(SwingConstants.CENTER);
      label_i4.setHorizontalTextPosition(SwingConstants.CENTER);
      label_i4.setText("--");

      // Element Buttons
      button_Element_1.setFont(new java.awt.Font("Dialog", 0, 11));
      button_Element_2.setFont(new java.awt.Font("Dialog", 0, 11));
      button_Element_3.setFont(new java.awt.Font("Dialog", 0, 11));
      button_Element_4.setFont(new java.awt.Font("Dialog", 0, 11));
      button_Element_5.setFont(new java.awt.Font("Dialog", 0, 11));
      button_Element_1.setMaximumSize(new Dimension(78, 25));
      button_Element_1.setMinimumSize(new Dimension(78, 25));
      button_Element_2.setMaximumSize(new Dimension(78, 25));
      button_Element_2.setMinimumSize(new Dimension(78, 25));
      button_Element_3.setMaximumSize(new Dimension(78, 25));
      button_Element_3.setMinimumSize(new Dimension(78, 25));
      button_Element_4.setMaximumSize(new Dimension(78, 25));
      button_Element_4.setMinimumSize(new Dimension(78, 25));
      button_Element_5.setMaximumSize(new Dimension(78, 25));
      button_Element_5.setMinimumSize(new Dimension(78, 25));

      button_Element_1.setIcon(VariLC_RT.pStackIcon[2]);
      button_Element_1.setText("1");
      button_Element_1.setMargin(new Insets(2, 4, 2, 4));
      button_Element_1.setPreferredSize(new Dimension(78, 25));
      button_Element_1.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Element_1_actionPerformed(e);
         }
      });
      button_Element_2.setPreferredSize(new Dimension(78, 25));
      button_Element_2.setMargin(new Insets(2, 4, 2, 4));
      button_Element_2.setIcon(VariLC_RT.pStackIcon[3]);
      button_Element_2.setText("2");
      button_Element_2.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Element_2_actionPerformed(e);
         }
      });
      button_Element_3.setPreferredSize(new Dimension(78, 25));
      button_Element_3.setMargin(new Insets(2, 4, 2, 4));
      button_Element_3.setIcon(VariLC_RT.pStackIcon[4]);
      button_Element_3.setText("3");
      button_Element_3.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Element_3_actionPerformed(e);
         }
      });
      button_Element_4.setPreferredSize(new Dimension(78, 25));
      button_Element_4.setMargin(new Insets(2, 4, 2, 4));
      button_Element_4.setIcon(VariLC_RT.pStackIcon[5]);
      button_Element_4.setText("4");
      button_Element_4.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Element_4_actionPerformed(e);
         }
      });
      button_Element_5.setPreferredSize(new Dimension(78, 25));
      button_Element_5.setMargin(new Insets(2, 4, 2, 4));
      button_Element_5.setIcon(VariLC_RT.pStackIcon[6]);
      button_Element_5.setText("5");
      button_Element_5.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Element_5_actionPerformed(e);
         }
      });

      ButtonGroup group = new ButtonGroup();
      group.add(button_Element_1);
      group.add(button_Element_2);
      group.add(button_Element_3);
      group.add(button_Element_4);
      group.add(button_Element_5);

      // Listener for palette element spinners ----------------------------
      ChangeListener listener = new ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            int element = 0;
            int channel = 0;
            SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
            if (source.equals(model0)) {
               element = 0;
               channel = 0;
            }
            if (source.equals(model1)) {
               element = 0;
               channel = 1;
            }
            if (source.equals(model2)) {
               element = 1;
               channel = 0;
            }
            if (source.equals(model3)) {
               element = 1;
               channel = 1;
            }
            if (source.equals(model4)) {
               element = 2;
               channel = 0;
            }
            if (source.equals(model5)) {
               element = 2;
               channel = 1;
            }
            if (source.equals(model6)) {
               element = 3;
               channel = 0;
            }
            if (source.equals(model7)) {
               element = 3;
               channel = 1;
            }
            if (source.equals(model8)) {
               element = 4;
               channel = 0;
            }
            if (source.equals(model9)) {
               element = 4;
               channel = 1;
            }

            adjust(element, channel, source.getNumber().floatValue());

            JifUtils.waitFor((int) (vlc.getSettlingTime() + (Camera.exposure / 1000)));

            // display Average Intensity Value if ROI selected
            if (source.equals(model0) || source.equals(model1)) {
               label_i0.setText(averageOfROI(1));
            }
            if (source.equals(model2) || source.equals(model3)) {
               label_i1.setText(averageOfROI(2));
            }
            if (source.equals(model4) || source.equals(model5)) {
               label_i2.setText(averageOfROI(3));
            }
            if (source.equals(model6) || source.equals(model7)) {
               label_i3.setText(averageOfROI(4));
            }
            if (source.equals(model8) || source.equals(model9)) {
               label_i4.setText(averageOfROI(5));
            }
         }
      };
      model0.addChangeListener(listener);
      model1.addChangeListener(listener);
      model2.addChangeListener(listener);
      model3.addChangeListener(listener);
      model4.addChangeListener(listener);
      model5.addChangeListener(listener);
      model6.addChangeListener(listener);
      model7.addChangeListener(listener);
      model8.addChangeListener(listener);
      model9.addChangeListener(listener);

      add(jLabelT1, null);
      add(jLabelC1, null);
      add(jLabelC2, null);
      add(jLabel9, null);
      add(button_Element_1, null);
      add(spinner0);
      add(spinner1);
      add(label_i0, null);
      add(button_Element_2, null);
      add(spinner2);
      add(spinner3);
      add(label_i1, null);
      add(button_Element_3, null);
      add(spinner4);
      add(spinner5);
      add(label_i2, null);
      add(button_Element_4, null);
      add(spinner6);
      add(spinner7);
      add(label_i3, null);
      add(button_Element_5, null);
      add(spinner8);
      add(spinner9);
      add(label_i4, null);

      // Check
      button_CheckIntensities.setBackground(new Color(182, 190, 255));
      button_CheckIntensities.setToolTipText("Check Intensities for all elements");
      button_CheckIntensities.setBounds(new Rectangle(10, 8, 76, 27));
      button_CheckIntensities.setFont(new java.awt.Font("Dialog", 1, 12));
      button_CheckIntensities.setMargin(new Insets(2, 4, 2, 4));
      button_CheckIntensities.setText("Check");
      button_CheckIntensities.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_CheckIntensities_actionPerformed(e);
         }
      });

   }


   ///////////////////////////////////////////////////////////////////////////
   // Element Buttons: Allows adjustment of retarder settings for each element
   //
   public void button_Element_1_actionPerformed (ActionEvent e) {
      spinner0.setEnabled(true);
      spinner1.setEnabled(true);
      spinner2.setEnabled(false);
      spinner3.setEnabled(false);
      spinner4.setEnabled(false);
      spinner5.setEnabled(false);
      spinner6.setEnabled(false);
      spinner7.setEnabled(false);
      spinner8.setEnabled(false);
      spinner9.setEnabled(false);
      setROI();
      checkElement0();
   }


   public void button_Element_2_actionPerformed (ActionEvent e) {
      spinner0.setEnabled(false);
      spinner1.setEnabled(false);
      spinner2.setEnabled(true);
      spinner3.setEnabled(true);
      spinner4.setEnabled(false);
      spinner5.setEnabled(false);
      spinner6.setEnabled(false);
      spinner7.setEnabled(false);
      spinner8.setEnabled(false);
      spinner9.setEnabled(false);
      setROI();
      checkElement1();
   }


   public void button_Element_3_actionPerformed (ActionEvent e) {
      spinner0.setEnabled(false);
      spinner1.setEnabled(false);
      spinner2.setEnabled(false);
      spinner3.setEnabled(false);
      spinner4.setEnabled(true);
      spinner5.setEnabled(true);
      spinner6.setEnabled(false);
      spinner7.setEnabled(false);
      spinner8.setEnabled(false);
      spinner9.setEnabled(false);
      setROI();
      checkElement2();
   }


   public void button_Element_4_actionPerformed (ActionEvent e) {
      spinner0.setEnabled(false);
      spinner1.setEnabled(false);
      spinner2.setEnabled(false);
      spinner3.setEnabled(false);
      spinner4.setEnabled(false);
      spinner5.setEnabled(false);
      spinner6.setEnabled(true);
      spinner7.setEnabled(true);
      spinner8.setEnabled(false);
      spinner9.setEnabled(false);
      setROI();
      checkElement3();
   }


   public void button_Element_5_actionPerformed (ActionEvent e) {
      spinner0.setEnabled(false);
      spinner1.setEnabled(false);
      spinner2.setEnabled(false);
      spinner3.setEnabled(false);
      spinner4.setEnabled(false);
      spinner5.setEnabled(false);
      spinner6.setEnabled(false);
      spinner7.setEnabled(false);
      spinner8.setEnabled(true);
      spinner9.setEnabled(true);
      setROI();
      checkElement4();
   }


   //
   void adjust (int e, int c, float value) {
      float eA = 0;
      float eB = 0;
      if (c == 0) { // adjust A channel
         eA = value;
         eB = vlc.getElementB(e);
      }
      if (c == 1) { // adjust B channel
         eA = vlc.getElementA(e);
         eB = value;
      }

      //System.out.println(e+", "+eA+", "+eB);
     // vlc.setElement(e, eA, eB);
   }


   ////////////////////////////////////////////////////////////////////
   // updateLCSpinnerValues
   //
   public void updateLCSpinnerValues () {
      if (vlc.isConnected) {
         // update the values of the SpinnerNumberModels
         model0.setValue(new Float(vlc.getElementA(0)));
         model1.setValue(new Float(vlc.getElementB(0)));
         model2.setValue(new Float(vlc.getElementA(1)));
         model3.setValue(new Float(vlc.getElementB(1)));
         model4.setValue(new Float(vlc.getElementA(2)));
         model5.setValue(new Float(vlc.getElementB(2)));
         model6.setValue(new Float(vlc.getElementA(3)));
         model7.setValue(new Float(vlc.getElementB(3)));
         model8.setValue(new Float(vlc.getElementA(4)));
         model9.setValue(new Float(vlc.getElementB(4)));
         showSelectedElement();
         //value_LastCalibrated.setText(Prefs.usr.get("calib_LastTime", "(unknown)"));
      }
   }


   public void showSelectedElement () {
      final SwingWorker3 worker = new SwingWorker3()
      {
         public Object construct () {
            button_Element_1.setSelected(false);
            button_Element_2.setSelected(false);
            button_Element_3.setSelected(false);
            button_Element_4.setSelected(false);
            button_Element_5.setSelected(false);
            if (vlc.getSelectedElement() == 0) {
               button_Element_1.setSelected(true);
            }
            if (vlc.getSelectedElement() == 1) {
               button_Element_2.setSelected(true);
            }
            if (vlc.getSelectedElement() == 2) {
               button_Element_3.setSelected(true);
            }
            if (vlc.getSelectedElement() == 3) {
               button_Element_4.setSelected(true);
            }
            if (vlc.getSelectedElement() == 4) {
               button_Element_5.setSelected(true);
            }
            //PSj.lcButtonsPanel.showSelectedElement();
            return null;
         }


         public void finished () {}
      }; worker.start();
   }


   //----------------------------------------------------------------
   //
   void checkElement0 () {
      setLCandWait(0);
      label_i0.setText(averageOfROI(1));
      if (Camera.getSampleMaxROI() > 254) {
         label_i0.setForeground(Color.red);
      } else {
         label_i0.setForeground(Color.black);
      }
      label_i0.repaint();
   }


   void checkElement1 () {
      setLCandWait(1);
      label_i1.setText(averageOfROI(2));
      if (Camera.getSampleMaxROI() > 254) {
         label_i1.setForeground(Color.red);
      } else {
         label_i1.setForeground(Color.black);
      }
      label_i1.repaint();
   }


   void checkElement2 () {
      setLCandWait(2);
      label_i2.setText(averageOfROI(3));
      if (Camera.getSampleMaxROI() > 254) {
         label_i2.setForeground(Color.red);
      } else {
         label_i2.setForeground(Color.black);
      }
      label_i2.repaint();
   }


   void checkElement3 () {
      setLCandWait(3);
      label_i3.setText(averageOfROI(4));
      if (Camera.getSampleMaxROI() > 254) {
         label_i3.setForeground(Color.red);
      } else {
         label_i3.setForeground(Color.black);
      }
      label_i3.repaint();
   }


   void checkElement4 () {
      setLCandWait(4);
      label_i4.setText(averageOfROI(5));
      if (Camera.getSampleMaxROI() > 254) {
         label_i4.setForeground(Color.red);
      } else {
         label_i4.setForeground(Color.black);
      }
      label_i4.repaint();
   }


   synchronized void setLCandWait (int element) {
      System.out.println("selecting element " + element);
      vlc.selectElementWait(element);
      //    vlc.selectElement(element);
      //    PSjUtils.waitFor(120);
      //    try {
      //      System.out.println("waiting... " + element);
      //
      //      isSettled.wait_for_true();
      //    } catch (InterruptedException ex) {}
      //    System.out.println("done. " + element);
      //
      //    resetSettleTimer();
   }


   //////////////////////////////////////////////////////////////////////
   // CheckIntensities
   //
   void button_CheckIntensities_actionPerformed (ActionEvent e) {
      //stopCycle();
      JifUtils.waitFor(100);

      //saturatedPixels8 = new byte[(int) Camera.size];
      final SwingWorker3 worker = new SwingWorker3()
      {
         public Object construct () {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setROI();
            checkElement0();
            checkElement1();
            checkElement2();
            checkElement3();
            checkElement4();
            return null;
         }


         public void finished () {
            int zero = Math.round(Prefs.usr.getFloat("acq_zeroIntensity", 6));
            double intensityRatio = (intensity[1] - zero) / (intensity[0] - zero);
//            double extinctionRatio = intensityRatio / Math.pow(Math.tan(vlc.LC_Swing
//                  * Math.PI), 2);
            //valueExtinctionRatio.setText(fmtDec2.format(extinctionRatio));
//            if (extinctionRatio < 2.0) {
//               labelExtinctionWarning.setText("Warning: < 2.0");
//            } else {
//               labelExtinctionWarning.setText("");
//            }
            // valueVariance.setText(fmtDec2.format(variance(intensity)));
            vlc.selectElement(1);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            showSelectedElement();
         }
      }; worker.start();
   }


   ////////////////////////////////////////////////////////////////////
// setROI
//
   boolean setROI () {
      int roiSize = 50;
      if ((Camera.display != null) && Camera.isDisplayOn()) {
         // if no ROI selected, set to default ROI
         if (!Camera.isRoiSelected()) {
            Camera.setROIRectangle(new Rectangle((int) ((Camera.width / 2) - (roiSize / 2)),
                  (int) ((Camera.height / 2) - (roiSize / 2)), roiSize, roiSize));
         }
      }
      return true;
   }


////////////////////////////////////////////////////////////////////
// Average of the selected ROI
//
   String averageOfROI (int element) {
      if ((Camera.display != null) && Camera.isDisplayOn()) {
         // PSjUtils.waitFor(vlc.getSettlingTime() + 100);
         Camera.grabSampleFrame();
         float roiAvg = Camera.getSampleAverageROI();
         intensity[element - 1] = roiAvg;
         DecimalFormat df1 = new DecimalFormat("###.00");
         if (roiAvg > -1.0f) {
            return df1.format(roiAvg);
         } else {
            return "---";
         }
      } else {
         return "---";
      }
   }


   public static void main (String[] args) {
      PanelElementsVariLC p = null;
      try {
         p = new PanelElementsVariLC(new VariLC_RT(new SerialPortConnection()));
      }
      catch (Exception ex) {
      }
      FrameForTest f = new FrameForTest(p);
   }
}
