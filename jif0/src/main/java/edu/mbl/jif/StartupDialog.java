/*
 * OkCancelDialog.java
 *
 * Created on November 27, 2007, 10:34 AM
 */
package edu.mbl.jif;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.gui.*;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.bbg.prefs.PrefsEditor;

/**
 *
 * @author  GBH
 */
public class StartupDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form OkCancelDialog */
    public StartupDialog(java.awt.Window parent, String title, Dialog.ModalityType modal) {
        super(parent, title, modal);
        initComponents();
    }

    public StartupDialog(java.awt.Frame parent, String title, boolean modal) {
        super(parent, modal);
        super.setTitle(title);
        initComponents();
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public void addPanel(JComponent panel) {
        this.remove(panelOkCancel);
        this.add(panel, BorderLayout.CENTER);
        this.add(panelOkCancel, BorderLayout.SOUTH);
        this.pack();
    }

    public void enableOKButton(final boolean t) {
        okButton.setEnabled(t);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        panelOkCancel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel1.setText("CamAcqJ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jButton1.setText("jButton1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(176, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButton1)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        panelOkCancel.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        panelOkCancel.add(cancelButton);

        getContentPane().add(panelOkCancel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
      doClose(RET_OK);
  }//GEN-LAST:event_okButtonActionPerformed

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
      doClose(RET_CANCEL);
  }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
  private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
      doClose(RET_CANCEL);
  }//GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                StartupDialog dialog = new StartupDialog(new javax.swing.JFrame(), "Title...",
                    true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }

                });
                dialog.setVisible(true);
                int ret = dialog.getReturnStatus();
                System.out.println("return: " + ret);
                if (ret == StartupDialog.RET_CANCEL) {
//                        SwingUtilities.invokeLater(new Runnable() {
//
//        public void run() {
                    PrefsEditor pe = new PrefsEditor(
                        Preferences.userRoot().node("/edu/mbl/jif/camera/camacq"), 
                        Preferences.systemRoot());
                    pe.addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.out.println("PrefsEditor exiting...");
                            //System.exit(0);
                            new CamAcqJ().start();
                        }
                    });
                    pe.setVisible(true);
//                CamAcqJ.getInstance().getPreferences(),
//                CamAcqJ.getInstance().getSystemPreferences())).setVisible(true);
//        }
//    });
                }
            }

        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel panelOkCancel;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
