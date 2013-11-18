/*
 * PanelAcq.java
 * Created on April 26, 2006, 9:35 AM
 */
package edu.mbl.jif.acq.mode;

import edu.mbl.jif.acq.*;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.camacq.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import edu.mbl.jif.camera.CameraModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSpinner;

/**
 *
 * @author GBH
 */
public class PanelAcqPS extends BasePanelForm {

   private final InstrumentController ctrl;
   private final CameraModel.CameraPresentation cameraPresentation;
   private final AcqModelPS.AcqPresentation acqPresentation;

   /**
    * Creates new form PanelAcq
    */
   public PanelAcqPS(InstrumentController ctrl) {
      this.ctrl = ctrl;
      this.cameraPresentation = new CameraModel.CameraPresentation((CameraModel) ctrl.getModel("camera"));
      this.acqPresentation = new AcqModelPS.AcqPresentation((AcqModelPS) ctrl.getModel("acqPS"));

      initComponents();

      spinExposure.setModel(SpinnerAdapterFactory.createNumberAdapter(
              cameraPresentation.getComponentModel(CameraModel.PROPERTYNAME_EXPOSUREACQ),
              ((CameraModel) cameraPresentation.getBean()).getExposureAcq(), // defaultValue
              ((CameraModel) cameraPresentation.getBean()).getExposureAcqMin(),
              ((CameraModel) cameraPresentation.getBean()).getExposureAcqMax(),
              5.0));
      spinGain.setModel(SpinnerAdapterFactory.createNumberAdapter(
              cameraPresentation.getComponentModel(CameraModel.PROPERTYNAME_GAINACQ),
              ((CameraModel) cameraPresentation.getBean()).getGainAcq(), // defaultValue
              CameraModel.GAIN_MIN,
              CameraModel.GAIN_MAX,
              0.1));
      spinExposures.setModel(SpinnerAdapterFactory.createNumberAdapter(
              acqPresentation.getComponentModel(AcqModelPS.PROPERTYNAME_MULTIFRAME),
              ((AcqModelPS) acqPresentation.getBean()).getMultiFrame(), // defaultValue
              1, 256, 1));
      spinExposuresBkgd.setModel(SpinnerAdapterFactory.createNumberAdapter(
              acqPresentation.getComponentModel(AcqModelPS.PROPERTYNAME_MULTIFRAMEBKGD),
              ((AcqModelPS) acqPresentation.getBean()).getMultiFrameBkgd(), // defaultValue
              1, 256, 1));
      checkBoxSameAsStreaming = BasicComponentFactory.createCheckBox(
              cameraPresentation.getModel(CameraModel.PROPERTYNAME_SAMESETACQSTREAM), "Same as Streaming");
      checkAverage = BasicComponentFactory.createCheckBox(
              acqPresentation.getModel(AcqModel.PROPERTYNAME_DIV), "Average");
      radio8bit = BasicComponentFactory.createRadioButton(
              acqPresentation.getModel(AcqModel.PROPERTYNAME_DEPTH), AcqModel.DEPTH_BYTE, "8-bit");
      radio12Bit = BasicComponentFactory.createRadioButton(
              acqPresentation.getModel(AcqModel.PROPERTYNAME_DEPTH), AcqModel.DEPTH_SHORT, "12-bit");
      checkMirror = BasicComponentFactory.createCheckBox(
              acqPresentation.getModel(AcqModel.PROPERTYNAME_MIRRORIMAGE), "MirrorImage");
      checkFlip = BasicComponentFactory.createCheckBox(
              acqPresentation.getModel(AcqModel.PROPERTYNAME_FLIPIMAGE), "FlipImage");
      initEventHandling();
   }

   public void setExpSpinStep(double value) {
      String decFormat = "0";
      double stepSize = 1;
      if ((value >= 0) && (value < 0.05)) {
         stepSize = 0.005;
         decFormat = "0.000";
      }
      if ((value >= 0.05) && (value < 0.5)) {
         stepSize = 0.05;
         decFormat = "0.00";
      }
      if ((value >= 0.5) && (value < 2)) {
         stepSize = 0.1;
         decFormat = "0.00";
      }
      if ((value >= 2) && (value < 10)) {
         stepSize = 1;
         decFormat = "0.0";
      }
      if ((value >= 10) && (value < 20)) {
         stepSize = 2;
         decFormat = "0.0";
      }
      if ((value >= 20) && (value < 100)) {
         stepSize = 5;
         decFormat = "0.";
      }
      if ((value >= 100) && (value < 1000)) {
         stepSize = 10;
         decFormat = "0.";
      }
      if ((value >= 1000)) {
         stepSize = 100;
         decFormat = "0.";
      }
      // update the format
      ((JSpinner.NumberEditor) spinExposure.getEditor()).getModel().setStepSize(new Double(stepSize));
      ((JSpinner.NumberEditor) spinExposure.getEditor()).getFormat().applyPattern(decFormat);
   }

   private void initEventHandling() {
      cameraPresentation.addBeanPropertyChangeListener(new UpdateHandler());
   }

   private final class UpdateHandler
           implements PropertyChangeListener {

      public void propertyChange(PropertyChangeEvent evt) {
         System.out.print(evt.getPropertyName());
         System.out.println(" " + evt.getNewValue());
         // updateActionEnablement();
         try {
            final double value = Double.parseDouble(String.valueOf(
                    cameraPresentation.getComponentModel(CameraModel.PROPERTYNAME_EXPOSURESTREAM).getValue()));
            setExpSpinStep(value);
         } catch (NumberFormatException nfe) {
         }
      }
   }

   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT
    * modify this code. The content of this method is always regenerated by the Form Editor.
    */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup1 = new javax.swing.ButtonGroup();
    jPanel2 = new javax.swing.JPanel();
    jPanel5 = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    spinExposure = new javax.swing.JSpinner();
    spinGain = new javax.swing.JSpinner();
    jLabel5 = new javax.swing.JLabel();
    checkBoxSameAsStreaming = BasicComponentFactory.createCheckBox(cameraPresentation.getModel(CameraModel.PROPERTYNAME_SAMESETACQSTREAM), "Same as Streaming");
    jPanel4 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    checkAverage = BasicComponentFactory.createCheckBox(acqPresentation.getModel(AcqModel.PROPERTYNAME_DIV), "Average");
    spinExposures = new javax.swing.JSpinner();
    jLabel6 = new javax.swing.JLabel();
    spinExposuresBkgd = new javax.swing.JSpinner();
    jPanel1 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    radio8bit = BasicComponentFactory.createRadioButton(acqPresentation.getModel(AcqModel.PROPERTYNAME_DEPTH),AcqModel.DEPTH_BYTE, "8-bit");
    radio12Bit = BasicComponentFactory.createRadioButton(acqPresentation.getModel(AcqModel.PROPERTYNAME_DEPTH),AcqModel.DEPTH_SHORT, "12-bit");
    jPanel3 = new javax.swing.JPanel();
    checkMirror = BasicComponentFactory.createCheckBox(acqPresentation.getModel(AcqModel.PROPERTYNAME_MIRRORIMAGE), "MirrorImage");
    checkFlip = BasicComponentFactory.createCheckBox(acqPresentation.getModel(AcqModel.PROPERTYNAME_FLIPIMAGE), "FlipImage");

    setBackground(new java.awt.Color(224, 238, 224));
    setPreferredSize(new java.awt.Dimension(350, 150));

    jPanel2.setBackground(edu.mbl.jif.utils.color.JifColor.green[4]);
    jPanel2.setPreferredSize(new java.awt.Dimension(340, 150));

    jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jPanel5.setOpaque(false);

    jLabel4.setText("Exposure, ms");

    spinExposure.setMaximumSize(new java.awt.Dimension(60, 20));
    spinExposure.setMinimumSize(new java.awt.Dimension(60, 20));
    spinExposure.setPreferredSize(new java.awt.Dimension(60, 20));

    spinGain.setMaximumSize(new java.awt.Dimension(60, 20));
    spinGain.setMinimumSize(new java.awt.Dimension(60, 20));
    spinGain.setPreferredSize(new java.awt.Dimension(60, 20));

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel5.setText("Gain");

    checkBoxSameAsStreaming.setText("Same as Streaming");
    checkBoxSameAsStreaming.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    checkBoxSameAsStreaming.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    checkBoxSameAsStreaming.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkBoxSameAsStreaming.setOpaque(false);
    checkBoxSameAsStreaming.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        checkBoxSameAsStreamingItemStateChanged(evt);
      }
    });

    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(checkBoxSameAsStreaming)
          .add(jPanel5Layout.createSequentialGroup()
            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(spinExposure, 0, 0, Short.MAX_VALUE))
          .add(jPanel5Layout.createSequentialGroup()
            .add(32, 32, 32)
            .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(spinGain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel4)
          .add(spinExposure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel5)
          .add(spinGain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .add(checkBoxSameAsStreaming)
        .addContainerGap())
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Multi-Exposure", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
    jPanel4.setOpaque(false);

    jLabel1.setText("Sample");

    checkAverage.setText("Average");
    checkAverage.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    checkAverage.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkAverage.setOpaque(false);

    jLabel6.setText("Background");

    org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jPanel4Layout.createSequentialGroup()
            .add(spinExposures, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jLabel1))
          .add(jPanel4Layout.createSequentialGroup()
            .add(spinExposuresBkgd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jLabel6))
          .add(checkAverage))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel4Layout.createSequentialGroup()
        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(spinExposures, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jLabel1))
        .add(8, 8, 8)
        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jLabel6)
          .add(spinExposuresBkgd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(checkAverage)
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jPanel1.setOpaque(false);
    jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("Bit Depth:");
    jLabel2.setMaximumSize(new java.awt.Dimension(60, 14));
    jPanel1.add(jLabel2);

    radio8bit.setText("8");
    radio8bit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    radio8bit.setMargin(new java.awt.Insets(5, 5, 0, 0));
    radio8bit.setMaximumSize(new java.awt.Dimension(35, 15));
    radio8bit.setOpaque(false);
    radio8bit.setPreferredSize(new java.awt.Dimension(35, 15));
    radio8bit.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        radio8bitPropertyChange(evt);
      }
    });
    jPanel1.add(radio8bit);

    radio12Bit.setText("12");
    radio12Bit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    radio12Bit.setMargin(new java.awt.Insets(0, 0, 0, 0));
    radio12Bit.setOpaque(false);
    jPanel1.add(radio12Bit);

    jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jPanel3.setOpaque(false);
    jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

    checkMirror.setText("Mirror");
    checkMirror.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    checkMirror.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkMirror.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    checkMirror.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkMirror.setMaximumSize(new java.awt.Dimension(60, 15));
    checkMirror.setOpaque(false);
    checkMirror.setPreferredSize(new java.awt.Dimension(60, 15));
    jPanel3.add(checkMirror);

    checkFlip.setText("Flip");
    checkFlip.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    checkFlip.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    checkFlip.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    checkFlip.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkFlip.setMaximumSize(new java.awt.Dimension(60, 15));
    checkFlip.setOpaque(false);
    checkFlip.setPreferredSize(new java.awt.Dimension(60, 15));
    checkFlip.setRequestFocusEnabled(false);
    jPanel3.add(checkFlip);

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
          .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(225, 225, 225))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(jPanel2Layout.createSequentialGroup()
        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
          .add(jPanel2Layout.createSequentialGroup()
            .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
          .add(jPanel2Layout.createSequentialGroup()
            .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(18, 18, 18)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents

   private void checkBoxSameAsStreamingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxSameAsStreamingItemStateChanged
// TODO add your handling code here:
   }//GEN-LAST:event_checkBoxSameAsStreamingItemStateChanged

   private void radio8bitPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_radio8bitPropertyChange
      if (radio8bit.isSelected()) {
         checkAverage.setSelected(true);
         checkAverage.setEnabled(false);
      } else {
         checkAverage.setEnabled(true);
      }
   }//GEN-LAST:event_radio8bitPropertyChange
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.JCheckBox checkAverage;
  private javax.swing.JCheckBox checkBoxSameAsStreaming;
  private javax.swing.JCheckBox checkFlip;
  private javax.swing.JCheckBox checkMirror;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JRadioButton radio12Bit;
  private javax.swing.JRadioButton radio8bit;
  private javax.swing.JSpinner spinExposure;
  private javax.swing.JSpinner spinExposures;
  private javax.swing.JSpinner spinExposuresBkgd;
  private javax.swing.JSpinner spinGain;
  // End of variables declaration//GEN-END:variables
}
