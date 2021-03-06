/*
 * PI_Controller.java
 *
 * Created on June 11, 2008, 4:18 PM
 */
package edu.mbl.jif.stage.piC865;

import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.laser.BurstGenerator;
import edu.mbl.jif.laser.LaserController;
import edu.mbl.jif.laser.LaserControllerBurst;
import edu.mbl.jif.laser.LaserControllerDigital;
import edu.mbl.jif.laser.PanelLaser;
import edu.mbl.jif.laser.PanelPath;
import edu.mbl.jif.stage.StageXYController;
import edu.mbl.jif.stage.StageXY_Mock;
import edu.mbl.jif.laser.Surgeon;
import javax.swing.UIManager;

/**
 *
 * @author  GBH
 */
public class PI_Controller
    extends javax.swing.JFrame {

    StageXYController stageXYCtrl;
    Surgeon surg;

    public PI_Controller() {
        boolean test = true;

        if (test) {
            stageXYCtrl = new StageXY_Mock();
        } else {
            stageXYCtrl = new StageXYController_C865();
        }
        if (!stageXYCtrl.open()) {
            System.err.println("StageXYControl failed to initialize.");
            return;
        }
        LaserController laserCtrl;
        if (test) {
            laserCtrl = new LaserControllerDigital((StageXY_Mock) stageXYCtrl);
        } else {
            //    laserCtrl = new LaserControllerDigital(((StageXYController_C865) stageXYCtrl).ctrlY);

            // Using Sanger Burst Generator...
            String portName = "COM17";
            SerialPortConnection port = new SerialPortConnection();
            port.setPortName(portName);
            laserCtrl = new LaserControllerBurst(new BurstGenerator(null, "BurstGenerator", portName));
        }
        surg = new Surgeon(stageXYCtrl, laserCtrl);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    panelLaser1 = new PanelLaser(surg);
    panelPath1 = new PanelPath(surg);
    panelXYStage1 = new edu.mbl.jif.stage.PanelXYStage();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    panelLaser1.setBorder(javax.swing.BorderFactory.createTitledBorder("Laser"));

    panelPath1.setBorder(javax.swing.BorderFactory.createTitledBorder("Path"));

    panelXYStage1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(panelLaser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(panelXYStage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panelPath1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(panelLaser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panelXYStage1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addGroup(layout.createSequentialGroup()
        .addGap(44, 44, 44)
        .addComponent(panelPath1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(249, 249, 249))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //edu.mbl.jif.gui.lookfeel.LooksAndFeels.setSyntheticaIce();
        try {
            com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
                // new com.jgoodies.looks.plastic.theme.DesertBluer());
                //new com.jgoodies.looks.plastic.theme.Silver());
                new com.jgoodies.looks.plastic.theme.ExperienceBlue());
            //new com.jgoodies.looks.plastic.theme.DesertBlue());
            com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        } catch (javax.swing.UnsupportedLookAndFeelException use) {
            UIManager.getSystemLookAndFeelClassName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Nimbus
//        try {
//            UIManager.setLookAndFeel("org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeel");
//        } catch (javax.swing.UnsupportedLookAndFeelException use) {
//            UIManager.getSystemLookAndFeelClassName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
// 
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PI_Controller().setVisible(true);
            }

        });
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private edu.mbl.jif.laser.PanelLaser panelLaser1;
  private edu.mbl.jif.laser.PanelPath panelPath1;
  private edu.mbl.jif.stage.PanelXYStage panelXYStage1;
  // End of variables declaration//GEN-END:variables
}


