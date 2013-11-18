package edu.mbl.jif.camera;

import edu.mbl.jif.camera.camacq.*;
import edu.mbl.jif.camacq.InstrumentController;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.adapter.ToggleButtonAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.JSpinner;
import org.jdesktop.swingx.action.ActionManager;

/*
 * PanelCamera.java
 * Created on June 26, 2002, 3:36 PM
 */
public class PanelCamera
        extends BasePanelForm {

    private final InstrumentController ctrl;
    private final CameraModel camModel;
    private final CameraModel.CameraPresentation cpm;


    public PanelCamera(InstrumentController ctrl) {
        this.ctrl = ctrl;
        this.cpm = new CameraModel.CameraPresentation((CameraModel) ctrl.getModel("camera"));
        this.camModel = (CameraModel) ctrl.getModel("camera");
        initComponents();
            spinExposure.setModel(SpinnerAdapterFactory.createNumberAdapter(
                    new PropertyAdapter(
                    camModel, CameraModel.PROPERTYNAME_EXPOSURESTREAM, true),

                    //cpm.getComponentModel(CameraModel.PROPERTYNAME_EXPOSURESTREAM),
                    ((CameraModel) cpm.getBean()).getExposureStream(),   // defaultValue
                    ((CameraModel) cpm.getBean()).getExposureStreamMin(),
                    ((CameraModel) cpm.getBean()).getExposureStreamMax(),
                    1.0)); // step

            spinGain.setModel(SpinnerAdapterFactory.createNumberAdapter(
                    cpm.getComponentModel(CameraModel.PROPERTYNAME_GAINSTREAM),
                    ((CameraModel) cpm.getBean()).getGainStream(),
                    CameraModel.GAIN_MIN,
                    CameraModel.GAIN_MAX,
                    0.1)); // step

            spinOffset.setModel(SpinnerAdapterFactory.createNumberAdapter(
                    cpm.getComponentModel(CameraModel.PROPERTYNAME_OFFSETSTREAM),
                    ((CameraModel) cpm.getBean()).getOffsetStream(),  // defaultValue
                    -2040.0,   // minValue
                    2040.0, // maxValue
                    10.0)); // step

            spinZeroIntensity.setModel(SpinnerAdapterFactory.createNumberAdapter(
                    cpm.getComponentModel(CameraModel.PROPERTYNAME_ZEROINTENSITY),
                    ((CameraModel) cpm.getBean()).getZeroIntensity(),  // defaultValue
                    0,   // minValue
                    128, // maxValue
                    1)); // step
//            this.toggleAutoExposure.setModel(new ToggleButtonAdapter(
//                    cpm.getModel(CameraModel.PROPERTYNAME_AUTOEXPOSURE)));

            revalidate();
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
        cpm.addBeanPropertyChangeListener(new UpdateHandler());
    }


    private final class UpdateHandler
            implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.print("propertyChange: " + evt.getPropertyName());
            System.out.println(" " + evt.getNewValue());
            // updateActionEnablement();
            try {
                final double value = Double.parseDouble(String.valueOf(cpm.getComponentModel(CameraModel.PROPERTYNAME_EXPOSUREACQ).getValue()));
                setExpSpinStep(value);
            } catch (NumberFormatException nfe) {
            }
        }
    }


    public void displayOpen() {
        ((AbstractAction) ActionManager.getInstance().getAction("openLiveDisplay")).actionPerformed(null);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        comboBinning = BasicComponentFactory.createComboBox(new SelectionInList(CameraModel.BINNINGX_OPTIONS,
            cpm.getModel(CameraModel.PROPERTYNAME_BINNINGX)));
    comboSpeed = BasicComponentFactory.createComboBox(new SelectionInList(CameraModel.SPEED_OPTIONS,
        cpm.getModel(CameraModel.PROPERTYNAME_SPEED)));
spinOffset = new javax.swing.JSpinner();
jLabel6 = new javax.swing.JLabel();
jPanel3 = new javax.swing.JPanel();
buttonRoi = new javax.swing.JButton();
checkCooler = BasicComponentFactory.createCheckBox(cpm.getModel(
    CameraModel.PROPERTYNAME_COOLERACTIVE), "Cooler");
    buttonInfo = new javax.swing.JButton();
    jPanel5 = new javax.swing.JPanel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    spinExposure = new javax.swing.JSpinner();
    spinGain = new javax.swing.JSpinner();
    jPanel4 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    buttonMeasureZero = new javax.swing.JButton();
    spinZeroIntensity = new javax.swing.JSpinner();
    jLabel2 = new javax.swing.JLabel();
    labelFps = BasicComponentFactory.createLabel(
        ConverterFactory.createStringConverter(
            cpm.getModel(CameraModel.PROPERTYNAME_CURRENTFPS), NumberFormat.getIntegerInstance()));

    setBackground(new java.awt.Color(211, 220, 236));
    setPreferredSize(new java.awt.Dimension(450, 180));

    jPanel1.setBackground(new java.awt.Color(211, 220, 236));
    jPanel1.setPreferredSize(new java.awt.Dimension(450, 180));

    jPanel2.setOpaque(false);

    jLabel3.setText("Binning");

    jLabel7.setText("Readout");

    comboBinning.setPreferredSize(new java.awt.Dimension(65, 22));

    spinOffset.setMinimumSize(new java.awt.Dimension(29, 24));
    spinOffset.setPreferredSize(new java.awt.Dimension(29, 24));

    jLabel6.setText("Offset");

    jPanel3.setOpaque(false);

    buttonRoi.setText("ROI");
    buttonRoi.setToolTipText("Set / Clear Camera ROI");
    buttonRoi.setMargin(new java.awt.Insets(2, 4, 2, 4));
    buttonRoi.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            changeCameraRoi(evt);
        }
    });

    checkCooler.setText("Cooler");
    checkCooler.setToolTipText("Turn cooler on or off");
    checkCooler.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    checkCooler.setMargin(new java.awt.Insets(0, 0, 0, 0));
    checkCooler.setOpaque(false);

    buttonInfo.setText("info");
    buttonInfo.setMargin(new java.awt.Insets(2, 4, 2, 4));
    buttonInfo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            showInfo(evt);
        }
    });

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(buttonRoi)
                .add(buttonInfo)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, checkCooler, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel3Layout.createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(buttonRoi)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(checkCooler, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(buttonInfo))
    );

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel3)
                .add(jLabel6)
                .add(jLabel7))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(comboSpeed, 0, 51, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, comboBinning, 0, 51, Short.MAX_VALUE)
                .add(spinOffset, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel2Layout.createSequentialGroup()
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel6)
                        .add(spinOffset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel3)
                        .add(comboBinning, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel7)
                        .add(comboSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(8, Short.MAX_VALUE))
    );

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Streaming"));
    jPanel5.setOpaque(false);

    jLabel4.setText("Exposure");

    jLabel5.setText("Gain");

    spinExposure.setMaximumSize(new java.awt.Dimension(60, 22));
    spinExposure.setMinimumSize(new java.awt.Dimension(60, 22));
    spinExposure.setPreferredSize(new java.awt.Dimension(60, 22));

    spinGain.setMaximumSize(new java.awt.Dimension(60, 32767));
    spinGain.setPreferredSize(new java.awt.Dimension(60, 20));

    org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel5Layout.createSequentialGroup()
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(8, 8, 8))
                .add(jLabel5))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(spinExposure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(spinGain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel5Layout.createSequentialGroup()
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel4)
                .add(spinExposure, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel5)
                .add(spinGain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(27, 27, 27))
    );

    jPanel4.setBackground(new java.awt.Color(200, 200, 219));
    jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

    jLabel1.setText(" Zero Intensity: ");
    jPanel4.add(jLabel1);

    buttonMeasureZero.setText("Measure");
    buttonMeasureZero.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonMeasureZeroActionPerformed(evt);
        }
    });
    jPanel4.add(buttonMeasureZero);

    spinZeroIntensity.setMinimumSize(new java.awt.Dimension(40, 18));
    spinZeroIntensity.setPreferredSize(new java.awt.Dimension(40, 18));
    jPanel4.add(spinZeroIntensity);

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabel2.setText("Frames/sec:");

    labelFps.setBackground(new java.awt.Color(191, 202, 215));
    labelFps.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
    labelFps.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labelFps.setText("000");
    labelFps.setPreferredSize(new java.awt.Dimension(18, 18));

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel1Layout.createSequentialGroup()
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel1Layout.createSequentialGroup()
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(18, 18, 18)
                    .add(jLabel2)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(labelFps, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .add(26, 26, 26))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(jPanel1Layout.createSequentialGroup()
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                .add(jPanel2, 0, 101, Short.MAX_VALUE)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
            .add(5, 5, 5)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(8, 8, 8)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel2)
                        .add(labelFps, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

private void buttonMeasureZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMeasureZeroActionPerformed
    camModel.measureZeroIntensity();
}//GEN-LAST:event_buttonMeasureZeroActionPerformed

private void showInfo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInfo
String parms = ((CameraModel) ctrl.getModel("camera")).listAllParms();
        TextWindow tf = new TextWindow("Camera parameters:");
        tf.setSize(600,
                600);
        tf.setLocation(200,
                20);
        tf.setVisible(true);
        tf.set("PROPERTIES ------------------------\n");
        tf.append(parms);
        QCamJNI.getInfo();
        QCamJNI.getParameters();
        tf.append("QCamJNI Parameters Listing\n");
        tf.append(QCamJNI.showCameraInfo() + "\n");
        tf.append(QCamJNI.showCameraState());
}//GEN-LAST:event_showInfo

private void changeCameraRoi(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeCameraRoi
    ((CameraModel) ctrl.getModel("camera")).setRoiToDisplayRoi();
    // Reopen display
    displayOpen();
}//GEN-LAST:event_changeCameraRoi




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonInfo;
    private javax.swing.JButton buttonMeasureZero;
    private javax.swing.JButton buttonRoi;
    private javax.swing.JCheckBox checkCooler;
    private javax.swing.JComboBox comboBinning;
    private javax.swing.JComboBox comboSpeed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel labelFps;
    private javax.swing.JSpinner spinExposure;
    private javax.swing.JSpinner spinGain;
    private javax.swing.JSpinner spinOffset;
    private javax.swing.JSpinner spinZeroIntensity;
    // End of variables declaration//GEN-END:variables
}
