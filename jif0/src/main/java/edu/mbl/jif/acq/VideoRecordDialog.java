/*
 * VideoRecordDialog.java
 *
 * Created on January 5, 2007, 9:49 AM
 */

package edu.mbl.jif.acq;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camera.camacq.*;
import edu.mbl.jif.acq.AcquisitionController;
import java.awt.Color;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author  GBH
 */
public class VideoRecordDialog extends javax.swing.JDialog {
        InstrumentController instrumentCtrl;
    /** Creates new form VideoRecordDialog */
    public VideoRecordDialog(java.awt.Frame parent, boolean modal,  InstrumentController instrumentCtrl) {
        super(parent, modal);
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                onCloseContainer();
            }
        });
        this.instrumentCtrl = instrumentCtrl;
        super.setBackground(Color.RED);
        this.setLocation(100,100);
        initComponents();
    }

    public void update(int frames)
      {
       valueFrames.setText("Frames: " + frames);
      }
    
    private void  onCloseContainer() {
        stopRecording();
    }
    private void stopRecording() {
        ((AcquisitionController)instrumentCtrl.getController("acq")).stopRecordVideo();
        CamAcqJ.getInstance().statusBarMessage("Video recording stopped.");
        ((AcquisitionController)instrumentCtrl.getController("acq")).displayResume();
        this.setVisible(false);
        this.dispose();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        valueFrames = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setBackground(new java.awt.Color(254, 126, 126));
        jButton1.setText("Stop Recording");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopTheRecording(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 14));
        jLabel1.setText("Recording Video...");

        valueFrames.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueFrames.setText("-");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1))
                    .add(layout.createSequentialGroup()
                        .add(23, 23, 23)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(valueFrames, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jButton1))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(valueFrames)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stopTheRecording(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopTheRecording
        stopRecording();
    }//GEN-LAST:event_stopTheRecording
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel valueFrames;
    // End of variables declaration//GEN-END:variables
    
}