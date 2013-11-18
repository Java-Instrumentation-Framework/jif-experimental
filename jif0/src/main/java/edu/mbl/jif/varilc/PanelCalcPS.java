/*
 * PanelAcqPS.java
 *
 * Created on February 29, 2008, 9:37 AM
 */
package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLCController;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.ps.PSAcquisitionController;
import edu.mbl.jif.ps.ui.PanelPSCalcParms;

/**
 *
 * @author  GBH
 */
public class PanelCalcPS
    extends javax.swing.JPanel {

    VariLCModel vlcm;
    VariLCPresentation vlcp;
    InstrumentController ctrl;
    private VariLCController vlcCtrl;
    PSAcquisitionController acqPSCtrl;

    /** Creates new form PanelAcqPS */
    public PanelCalcPS(InstrumentController ctrl) {
        this.ctrl = ctrl;
        this.vlcm = (VariLCModel) ctrl.getModel("variLC");
        vlcp = new VariLCPresentation(vlcm);
        this.vlcCtrl = (VariLCController) ctrl.getController("variLC");
        this.acqPSCtrl = (PSAcquisitionController) ctrl.getController("acqPS");
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel2 = new javax.swing.JPanel();
    buttonCalculate = new javax.swing.JButton();
    buttonSetPlugin = new javax.swing.JButton();
    buttonClearBkgd = new javax.swing.JButton();
    buttonSetBkgd = new javax.swing.JButton();

    setBackground(new java.awt.Color(219, 219, 170));

    jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

    buttonCalculate.setText("Calculate");
    buttonCalculate.setAlignmentX(0.5F);
    buttonCalculate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        buttonCalculateActionPerformed(evt);
      }
    });
    jPanel2.add(buttonCalculate);

    buttonSetPlugin.setText("SetPlugin ");
    buttonSetPlugin.setAlignmentX(0.5F);
    buttonSetPlugin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        buttonSetPluginActionPerformed(evt);
      }
    });
    jPanel2.add(buttonSetPlugin);

    buttonClearBkgd.setText("ClearBkgd");
    buttonClearBkgd.setToolTipText("Clear the background stack");
    buttonClearBkgd.setAlignmentX(0.5F);
    buttonClearBkgd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        buttonClearBkgdActionPerformed(evt);
      }
    });
    jPanel2.add(buttonClearBkgd);

    buttonSetBkgd.setText("Set Bkgd");
    buttonSetBkgd.setAlignmentX(0.5F);
    buttonSetBkgd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        buttonSetBkgdActionPerformed(evt);
      }
    });
    jPanel2.add(buttonSetBkgd);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(263, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(20, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

    private void buttonCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCalculateActionPerformed
        acqPSCtrl.setIJPrefs();
        acqPSCtrl.doPSCalculation();
    }//GEN-LAST:event_buttonCalculateActionPerformed

private void buttonSetPluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSetPluginActionPerformed
    acqPSCtrl.selectCalcPlugin();
}//GEN-LAST:event_buttonSetPluginActionPerformed

private void buttonClearBkgdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearBkgdActionPerformed
    acqPSCtrl.clearBkgdStack();
}//GEN-LAST:event_buttonClearBkgdActionPerformed

private void buttonSetBkgdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSetBkgdActionPerformed
    acqPSCtrl.setBkgdStack();
}//GEN-LAST:event_buttonSetBkgdActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton buttonCalculate;
  private javax.swing.JButton buttonClearBkgd;
  private javax.swing.JButton buttonSetBkgd;
  private javax.swing.JButton buttonSetPlugin;
  private javax.swing.JPanel jPanel2;
  // End of variables declaration//GEN-END:variables
}
