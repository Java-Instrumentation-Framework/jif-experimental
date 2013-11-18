/*
 * PanelCalibration.java
 *
 * Created on February 15, 2007, 8:08 PM
 */
package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLCController;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import edu.mbl.jif.acq.AcquisitionController;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.utils.color.JifColor;
import edu.mbl.jif.gui.dialog.DialogBox;
import edu.mbl.jif.varilc.camacq.VariLCMeasurer;
import java.text.NumberFormat;

/**
 *
 * @author GBH
 */
public class PanelCalibration
		extends javax.swing.JPanel {

	VariLCModel vlcm;
	VLCController vlcCtrl;
	VLCMeasurer  vlcMeasurer;
	VariLCPresentation vlcp;
	VariLCPersister variLCPersister;
	ValueModel a0;
	ValueModel b0;
	ValueModel a1;
	ValueModel b1;
	ValueModel a2;
	ValueModel b2;
	ValueModel a3;
	ValueModel b3;
	ValueModel a4;
	ValueModel b4;
	ValueModel i0;
	ValueModel i1;
	ValueModel i2;
	ValueModel i3;
	ValueModel i4;

	/**
	 * Creates new form PanelCalibration
	 */
	public PanelCalibration(InstrumentController ctrl) {
		this.vlcm = (VariLCModel) ctrl.getModel("variLC");
		this.vlcp = new VariLCPresentation(vlcm);
		this.vlcCtrl = (VariLCController) ctrl.getController("variLC");

		variLCPersister = new VariLCPersister(ctrl, vlcm, vlcCtrl);
		
		vlcMeasurer = new VariLCMeasurer(vlcm, vlcCtrl, 
				(AcquisitionController) ctrl.getController("acq"));
		
		a0 = vlcp.getModel(VariLCModel.PROPERTYNAME_A0);
		b0 = vlcp.getModel(VariLCModel.PROPERTYNAME_B0);
		a1 = vlcp.getModel(VariLCModel.PROPERTYNAME_A1);
		b1 = vlcp.getModel(VariLCModel.PROPERTYNAME_B1);
		a2 = vlcp.getModel(VariLCModel.PROPERTYNAME_A2);
		b2 = vlcp.getModel(VariLCModel.PROPERTYNAME_B2);
		a3 = vlcp.getModel(VariLCModel.PROPERTYNAME_A3);
		b3 = vlcp.getModel(VariLCModel.PROPERTYNAME_B3);
		a4 = vlcp.getModel(VariLCModel.PROPERTYNAME_A4);
		b4 = vlcp.getModel(VariLCModel.PROPERTYNAME_B4);
		i0 = vlcp.getModel(VariLCModel.PROPERTYNAME_INTENSITY0);
		i1 = vlcp.getModel(VariLCModel.PROPERTYNAME_INTENSITY1);
		i2 = vlcp.getModel(VariLCModel.PROPERTYNAME_INTENSITY2);
		i3 = vlcp.getModel(VariLCModel.PROPERTYNAME_INTENSITY3);
		i4 = vlcp.getModel(VariLCModel.PROPERTYNAME_INTENSITY4);

		// ++ icons

		initComponents();

		this.setBackground(JifColor.yellow[4]);
		// compensatorSettingPanel1

		spinSwing.setModel(SpinnerAdapterFactory.createNumberAdapter(
				vlcp.getModel(VariLCModel.PROPERTYNAME_SWING),
				vlcp.getModel(VariLCModel.PROPERTYNAME_SWING).doubleValue(), // defaultValue
				VariLCModel.SWING_MIN, VariLCModel.SWING_MAX,
				0.01)); // step
//        Enumeration buts = buttonGroup1.getElements();
//        JToggleButton tb = (JToggleButton) buts.nextElement();
//        tb.setSelected(true);

		this.validate();

	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        compensatorSettingPanel1 = new edu.mbl.jif.varilc.CompensatorSettingPanel(vlcm, vlcCtrl, vlcMeasurer, 0, a0, b0, i0, buttonGroup1);
        compensatorSettingPanel2 = new edu.mbl.jif.varilc.CompensatorSettingPanel(vlcm, vlcCtrl, vlcMeasurer, 1, a1, b1, i1, buttonGroup1);
        compensatorSettingPanel3 = new edu.mbl.jif.varilc.CompensatorSettingPanel(vlcm, vlcCtrl, vlcMeasurer,  2, a2, b2, i2, buttonGroup1);
        compensatorSettingPanel4 = new edu.mbl.jif.varilc.CompensatorSettingPanel(vlcm, vlcCtrl, vlcMeasurer, 3, a3, b3, i3, buttonGroup1);
        compensatorSettingPanel5 = new edu.mbl.jif.varilc.CompensatorSettingPanel(vlcm, vlcCtrl, vlcMeasurer, 4, a4, b4, i4, buttonGroup1);
        jPanel2 = new javax.swing.JPanel();
        buttonCalibrate = new javax.swing.JButton();
        spinSwing = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        buttonMeasure = new javax.swing.JButton();
        buttonDefaults = new javax.swing.JButton();
        buttonExtinctionDefaults = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        buttonLoad = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        valueExtinctionRatio =         BasicComponentFactory.createLabel(
            ConverterFactory.createStringConverter(
                vlcp.getModel(VariLCModel.PROPERTYNAME_EXTINCTIONRATIO), NumberFormat.getNumberInstance()));

        setBackground(new java.awt.Color(219, 219, 170));

        jPanel1.setBackground(new java.awt.Color(219, 219, 170));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(compensatorSettingPanel1);
        jPanel1.add(compensatorSettingPanel2);
        jPanel1.add(compensatorSettingPanel3);
        jPanel1.add(compensatorSettingPanel4);
        jPanel1.add(compensatorSettingPanel5);

        jPanel2.setOpaque(false);

        buttonCalibrate.setBackground(new java.awt.Color(228, 228, 175));
        buttonCalibrate.setText("Calibrate");
        buttonCalibrate.setToolTipText("Run calibration procedure");
        buttonCalibrate.setMargin(new java.awt.Insets(2, 4, 2, 4));
        buttonCalibrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCalibrateActionPerformed(evt);
            }
        });

        jLabel3.setText("Swing");

        buttonMeasure.setText("Check");
        buttonMeasure.setToolTipText("Measure all settings");
        buttonMeasure.setMargin(new java.awt.Insets(2, 4, 2, 4));
        buttonMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMeasureActionPerformed(evt);
            }
        });

        buttonDefaults.setText("Defaults");
        buttonDefaults.setToolTipText("Reset to default settings");
        buttonDefaults.setMargin(new java.awt.Insets(2, 4, 2, 4));
        buttonDefaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Reset(evt);
            }
        });

        buttonExtinctionDefaults.setText("ExtDefs");
        buttonExtinctionDefaults.setToolTipText("Set Extinction Defaults to current values");
        buttonExtinctionDefaults.setMargin(new java.awt.Insets(2, 4, 2, 4));
        buttonExtinctionDefaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExtinctionDefaultsActionPerformed(evt);
            }
        });

        buttonSave.setText("Save");
        buttonSave.setToolTipText("Save these settings");
        buttonSave.setMargin(new java.awt.Insets(2, 4, 2, 4));
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonLoad.setText("Load");
        buttonLoad.setToolTipText("Load saved settings");
        buttonLoad.setMargin(new java.awt.Insets(2, 4, 2, 4));
        buttonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonCalibrate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonMeasure))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spinSwing, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonDefaults)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonExtinctionDefaults))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonLoad)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCalibrate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonMeasure))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spinSwing))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDefaults)
                    .addComponent(buttonExtinctionDefaults))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSave)
                    .addComponent(buttonLoad))
                .addContainerGap())
        );

        jLabel1.setText("Extinction Ratio:");

        valueExtinctionRatio.setText("0000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(37, 37, 37)
                        .addComponent(valueExtinctionRatio))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(valueExtinctionRatio)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonExtinctionDefaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExtinctionDefaultsActionPerformed
		vlcCtrl.setExtinctionDefaultsToCurrent();
    }//GEN-LAST:event_buttonExtinctionDefaultsActionPerformed

    private void Reset(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Reset
		if (DialogBox.boxConfirm(this, "Reset VariLC to Defaults", "Proceed?")) {
			vlcCtrl.setRetardersToNominalSwing();
			vlcCtrl.transmitAllElements();
		}
    }//GEN-LAST:event_Reset

    private void buttonMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMeasureActionPerformed
		vlcMeasurer.measureAll();
    }//GEN-LAST:event_buttonMeasureActionPerformed

	/**
	 *
	 * @param evt
	 */
    private void buttonCalibrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCalibrateActionPerformed
		//if(vlcm.setupForMeasurements()) {
		boolean go = true; //DialogBox.boxConfirm(null, "Calibrate VariLC", "Proceed?");
		if (go) {
			Runnable runnable = new Runnable() {
				public void run() {
					vlcMeasurer.runCalibration();
				}
			};
			runnable.run();
		}
    }//GEN-LAST:event_buttonCalibrateActionPerformed

private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
	variLCPersister.saveVLCSettings();
}//GEN-LAST:event_buttonSaveActionPerformed

private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadActionPerformed
	variLCPersister.loadVLCSettings();
}//GEN-LAST:event_buttonLoadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCalibrate;
    private javax.swing.JButton buttonDefaults;
    private javax.swing.JButton buttonExtinctionDefaults;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonLoad;
    private javax.swing.JButton buttonMeasure;
    private javax.swing.JButton buttonSave;
    private edu.mbl.jif.varilc.CompensatorSettingPanel compensatorSettingPanel1;
    private edu.mbl.jif.varilc.CompensatorSettingPanel compensatorSettingPanel2;
    private edu.mbl.jif.varilc.CompensatorSettingPanel compensatorSettingPanel3;
    private edu.mbl.jif.varilc.CompensatorSettingPanel compensatorSettingPanel4;
    private edu.mbl.jif.varilc.CompensatorSettingPanel compensatorSettingPanel5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner spinSwing;
    private javax.swing.JLabel valueExtinctionRatio;
    // End of variables declaration//GEN-END:variables

	public static void main(String[] args) {
		// FrameForTest f = new FrameForTest() ;
		// f.addContents(new PanelCalibration(new VariLCModel(new InstrumentController())));
	}

}