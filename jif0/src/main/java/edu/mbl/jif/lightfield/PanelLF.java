/*
 * PanelLF.java
 *
 * Created on October 25, 2006, 8:09 AM
 */

package edu.mbl.jif.lightfield;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.gui.spatial.PanelControlXY;
import java.awt.BorderLayout;

/**
 *
 * @author  GBH
 */
public class PanelLF extends javax.swing.JPanel {
    
    private final InstrumentController ctrl;
    
    private final LFPresentation lFPresentation;
    //LFViewController lfvc;
    
    public PanelLF(InstrumentController ctrl) {
        this.ctrl = ctrl;
        this.lFPresentation = new LFPresentation((LFModel) ctrl.getModel("lightfield"));
        //this.lfvc = lfvc;
        initComponents();
        
        spinPitch.setModel(SpinnerAdapterFactory.createNumberAdapter(
                lFPresentation.getModel(LFModel.PROPERTYNAME_PITCHINT),
                17, 4, 24, 1));
        spinX0.setModel(SpinnerAdapterFactory.createNumberAdapter(
                lFPresentation.getModel(LFModel.PROPERTYNAME_OFFSETX),
                7, 1, 19, 1));
        
        spinY0.setModel(SpinnerAdapterFactory.createNumberAdapter(
                lFPresentation.getModel(LFModel.PROPERTYNAME_OFFSETY),
                7, 1, 19, 1));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        spinX0 = new javax.swing.JSpinner();
        spinY0 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        spinPitch = new javax.swing.JSpinner();
        checkInterpolate =    BasicComponentFactory.createCheckBox(lFPresentation.getModel(LFModel.PROPERTYNAME_INTERPOLATE), "Interpolate");
        arrowPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(246, 217, 157));
        jPanel1.setOpaque(false);

        jLabel3.setText("x0:");

        jLabel4.setText(" y0:");

        jLabel5.setText("Pitch");

        checkInterpolate.setText("Interpolate");
        checkInterpolate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkInterpolate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkInterpolate.setOpaque(false);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spinX0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(spinPitch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(spinY0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(checkInterpolate))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spinPitch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5)
                    .add(checkInterpolate))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spinY0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4)
                    .add(jLabel3)
                    .add(spinX0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        arrowPanel.setLayout(new java.awt.BorderLayout());

        arrowPanel.setMaximumSize(new java.awt.Dimension(80, 80));
        arrowPanel.setMinimumSize(new java.awt.Dimension(80, 80));
        arrowPanel.setPreferredSize(new java.awt.Dimension(80, 80));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(arrowPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(arrowPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createSequentialGroup()
                .add(11, 11, 11)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
            
    public void addArrowPanel(PanelControlXY pxy) {
        arrowPanel.add(pxy, BorderLayout.CENTER);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel arrowPanel;
    private javax.swing.JCheckBox checkInterpolate;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner spinPitch;
    private javax.swing.JSpinner spinX0;
    private javax.swing.JSpinner spinY0;
    // End of variables declaration//GEN-END:variables
    
}
