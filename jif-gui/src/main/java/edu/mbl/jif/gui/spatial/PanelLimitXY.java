/*
 * PanelStageXY.java
 *
 * Created on May 29, 2006, 8:02 AM
 */

package edu.mbl.jif.gui.spatial;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author  GBH
 */
public class PanelLimitXY extends javax.swing.JPanel {
    
    DirectionalXYController dirCtrl;
    /**
     * Creates new form PanelStageXY
     */
    public PanelLimitXY() {
        initComponents();
    }
    
    public PanelLimitXY(DirectionalXYController dirCtrl) {
        this.dirCtrl = dirCtrl;
        initComponents();
        
        if (buttonUp.getIcon() != null) buttonUp.setText("");
        if (buttonDown.getIcon() != null) buttonDown.setText("");
        if (buttonRight.getIcon() != null) buttonRight.setText("");
        if (buttonLeft.getIcon() != null) buttonLeft.setText("");
        if (buttonCenter.getIcon() != null) buttonCenter.setText("");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        buttonUpperLeft = new javax.swing.JButton();
        buttonUp = new javax.swing.JButton();
        buttonUpperRight = new javax.swing.JButton();
        buttonLeft = new javax.swing.JButton();
        buttonCenter = new javax.swing.JButton();
        buttonRight = new javax.swing.JButton();
        buttonLowerLeft = new javax.swing.JButton();
        buttonDown = new javax.swing.JButton();
        buttonLowerRight = new javax.swing.JButton();

        setBackground(new java.awt.Color(232, 217, 235));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMaximumSize(new java.awt.Dimension(76, 76));
        setMinimumSize(new java.awt.Dimension(76, 76));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(72, 72));
        jPanel1.setMinimumSize(new java.awt.Dimension(72, 72));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(3, 3));

        buttonUpperLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitTopLeft.png"))); // NOI18N
        buttonUpperLeft.setToolTipText("");
        buttonUpperLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonUpperLeft.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonUpperLeft);

        buttonUp.setAction(new UpAction()
        );
        buttonUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitTop.png"))); // NOI18N
        buttonUp.setText("");
        buttonUp.setToolTipText("");
        buttonUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonUp.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonUp);

        buttonUpperRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitTopRight.png"))); // NOI18N
        buttonUpperRight.setToolTipText("");
        buttonUpperRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonUpperRight.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonUpperRight);

        buttonLeft.setAction(new LeftAction());
        buttonLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitLeft.png"))); // NOI18N
        buttonLeft.setText("");
        buttonLeft.setToolTipText("left");
        buttonLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonLeft.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonLeft.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonLeft.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonLeft);

        buttonCenter.setAction(new CenterAction());
        buttonCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/setCenter.png"))); // NOI18N
        buttonCenter.setText("");
        buttonCenter.setToolTipText("center");
        buttonCenter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonCenter.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonCenter);

        buttonRight.setAction(new RightAction());
        buttonRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitRight.png"))); // NOI18N
        buttonRight.setText("");
        buttonRight.setToolTipText("right");
        buttonRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonRight.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonRight);
        buttonRight.getAccessibleContext().setAccessibleDescription("");

        buttonLowerLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitBottomLeft.png"))); // NOI18N
        buttonLowerLeft.setToolTipText("");
        buttonLowerLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonLowerLeft.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonLowerLeft);

        buttonDown.setAction(new DownAction());
        buttonDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitBottom.png"))); // NOI18N
        buttonDown.setText("");
        buttonDown.setToolTipText("down");
        buttonDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonDown.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonDown);

        buttonLowerRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/limitBottomRight.png"))); // NOI18N
        buttonLowerRight.setToolTipText("");
        buttonLowerRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonLowerRight.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonLowerRight);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
                    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCenter;
    private javax.swing.JButton buttonDown;
    private javax.swing.JButton buttonLeft;
    private javax.swing.JButton buttonLowerLeft;
    private javax.swing.JButton buttonLowerRight;
    private javax.swing.JButton buttonRight;
    private javax.swing.JButton buttonUp;
    private javax.swing.JButton buttonUpperLeft;
    private javax.swing.JButton buttonUpperRight;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
    
    
    public class RightAction extends AbstractAction {
        public RightAction() {
            super("Move Right");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xPlus.gif")));
            putValue(SHORT_DESCRIPTION, "Move Right");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_RIGHT));
        }
        
        public void actionPerformed(ActionEvent e) {
            dirCtrl.goRight(1);
        }
    }

       public class LeftAction extends AbstractAction {
        public LeftAction() {
            super("Move Left");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xMinus.gif")));
            putValue(SHORT_DESCRIPTION, "Move Left");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_LEFT));
        }
        
        public void actionPerformed(ActionEvent e) {
            dirCtrl.goLeft(1);
        }
    }

       public class UpAction extends AbstractAction {
        public UpAction() {
            super("Move Up");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xMinus.gif")));
            putValue(SHORT_DESCRIPTION, "Move Up");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_UP));
        }
        
        public void actionPerformed(ActionEvent e) {
            dirCtrl.goUp(1);
        }
    }
       public class DownAction extends AbstractAction {
        public DownAction() {
            super("Move Down");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xMinus.gif")));
            putValue(SHORT_DESCRIPTION, "Move Down");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_DOWN));
            putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0));
        }
        
        public void actionPerformed(ActionEvent e) {
            dirCtrl.goDown(1);
        }
    }
    
       public class CenterAction extends AbstractAction {
        public CenterAction() {
            super("Move Center");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xMinus.gif")));
            putValue(SHORT_DESCRIPTION, "Move Center");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_HOME));
        }
        
        public void actionPerformed(ActionEvent e) {
            dirCtrl.goCenter(1);
        }
    }
}
