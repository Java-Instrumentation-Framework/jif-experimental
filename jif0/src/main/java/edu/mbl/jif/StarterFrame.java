/*
 * StarterFrame.java
 *
 * Created on May 25, 2009, 10:52 AM
 */
package edu.mbl.jif;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.oidic.AcrLCTest;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 *
 *
 * @author  GBH
 */
public class StarterFrame extends javax.swing.JFrame {

  /** Creates new form StarterFrame */
  public StarterFrame() {
    System.out.println("CamAcqJ StarterFrame started.");
    try {
      //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     // UIManager.setLookAndFeel(new SynthLookAndFeel());
      com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
              //new com.jgoodies.looks.plastic.theme.DesertBluer());
              new com.jgoodies.looks.plastic.theme.Silver());
      //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
      //new com.jgoodies.looks.plastic.theme.DesertBlue());
      //com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
      UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    initComponents();
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        System.exit(0);
      }
    });
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPanel2 = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      buttonLaunch = new javax.swing.JButton();
      valueStatus = new javax.swing.JLabel();
      buttonLaunchWithConsole = new javax.swing.JButton();
      jPanel3 = new javax.swing.JPanel();
      buttonSurgeon = new javax.swing.JButton();
      buttonTestBurstGen = new javax.swing.JButton();
      buttonTest = new java.awt.Button();
      buttonExit = new javax.swing.JButton();
      jLabel1 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setMinimumSize(new java.awt.Dimension(410, 300));

      jPanel2.setPreferredSize(new java.awt.Dimension(0, 70));

      jLabel2.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
      jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/camJ32.gif"))); // NOI18N
      jLabel2.setText("CamAcqJ");

      buttonLaunch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
      buttonLaunch.setText("Launch");
      buttonLaunch.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonLaunchActionPerformed(evt);
         }
      });

      valueStatus.setText("-");

      buttonLaunchWithConsole.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      buttonLaunchWithConsole.setText("withConsole");
      buttonLaunchWithConsole.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonLaunchWithConsoleActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(valueStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(41, 41, 41)
            .addComponent(buttonLaunch)
            .addGap(61, 61, 61)
            .addComponent(buttonLaunchWithConsole)
            .addGap(50, 50, 50))
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel2)
               .addComponent(valueStatus)
               .addComponent(buttonLaunchWithConsole, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(buttonLaunch, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(27, Short.MAX_VALUE))
      );

      jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      buttonSurgeon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
      buttonSurgeon.setText("Surgeon");
      buttonSurgeon.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonSurgeonActionPerformed(evt);
         }
      });

      buttonTestBurstGen.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      buttonTestBurstGen.setText("BurstGen");
      buttonTestBurstGen.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonTestBurstGenActionPerformed(evt);
         }
      });

      buttonTest.setLabel("test");
      buttonTest.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonTestActionPerformed(evt);
         }
      });

      buttonExit.setText("Exit");
      buttonExit.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonExitActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(buttonSurgeon, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
               .addComponent(buttonExit)
               .addComponent(buttonTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(buttonTestBurstGen))
            .addContainerGap())
      );
      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addComponent(buttonSurgeon)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(buttonTestBurstGen)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(buttonTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(42, 42, 42)
            .addComponent(buttonExit)
            .addGap(26, 26, 26))
      );

      jLabel1.setBackground(new java.awt.Color(0, 0, 0));
      jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camacq/AsterColorSoft.gif"))); // NOI18N
      jLabel1.setMaximumSize(null);
      jLabel1.setMinimumSize(null);
      jLabel1.setPreferredSize(null);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
         .addGroup(layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(30, 30, 30)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addGap(10, 10, 10)
                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
  System.exit(0);
}//GEN-LAST:event_buttonExitActionPerformed

private void buttonLaunchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLaunchActionPerformed
  new CamAcqJ().start();
  this.setVisible(false);
  this.dispose();
}//GEN-LAST:event_buttonLaunchActionPerformed

private void buttonSurgeonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSurgeonActionPerformed
  edu.mbl.jif.stage.piC865.PI_Controller.main(null);
}//GEN-LAST:event_buttonSurgeonActionPerformed

private void buttonTestBurstGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestBurstGenActionPerformed
  edu.mbl.jif.laser.BurstGenerator.main(null);
}//GEN-LAST:event_buttonTestBurstGenActionPerformed

private void buttonTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestActionPerformed
  runtest();
}//GEN-LAST:event_buttonTestActionPerformed

private void buttonLaunchWithConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLaunchWithConsoleActionPerformed
  edu.mbl.jif.a_jar_stdio_terminal.StdioTerm.main(null);
  this.setVisible(false);
  this.dispose();
}//GEN-LAST:event_buttonLaunchWithConsoleActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {

      public void run() {
        new StarterFrame().setVisible(true);
      }
    });
  }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton buttonExit;
   private javax.swing.JButton buttonLaunch;
   private javax.swing.JButton buttonLaunchWithConsole;
   private javax.swing.JButton buttonSurgeon;
   private java.awt.Button buttonTest;
   private javax.swing.JButton buttonTestBurstGen;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JLabel valueStatus;
   // End of variables declaration//GEN-END:variables

  private void runtest() {
    String path = StarterFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    System.out.println("StarterFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath() = " + path);
    java.awt.EventQueue.invokeLater(new Runnable() {

      public void run() {
        new AcrLCTest().setVisible(true);
      }
    });
  }
}
