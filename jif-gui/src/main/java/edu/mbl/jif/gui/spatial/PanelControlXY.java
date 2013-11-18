/*
 * PanelStageXY.java
 *
 * Created on May 29, 2006, 8:02 AM
 */
package edu.mbl.jif.gui.spatial;

import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.gui.keyboard.GlobalHotkeyManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ActionMapUIResource;

/**
 * XY Directional Controller Panel
 * 
 * 
 * @author  GBH
 */
public class PanelControlXY
        extends javax.swing.JPanel {

    DirectionalXYController dirCtrl;

    /**
     * Creates new form PanelStageXY
     * 
     * modifier keys: SHIFT+arrow = 10 X, CTRL+arrow = 100 X
     */
    public PanelControlXY() {
        initComponents();
        buttonCenter.setText("");
        buttonDown.setText("");
        buttonLeft.setText("");
        buttonLowerLeft.setText("");
        buttonLowerRight.setText("");
        buttonRight.setText("");
        buttonUp.setText("");
        buttonUpperLeft.setText("");
        buttonUpperRight.setText("");

        initButtonKeyBinding(buttonRight, new RightAction());
        initButtonKeyBinding(buttonLeft, new LeftAction());
        initButtonKeyBinding(buttonUp, new UpAction());
        initButtonKeyBinding(buttonDown, new DownAction());
        initButtonKeyBinding(buttonUpperRight, new UpRightAction());
        initButtonKeyBinding(buttonLowerRight, new DownRightAction());
        initButtonKeyBinding(buttonUpperLeft, new UpLeftAction());
        initButtonKeyBinding(buttonLowerLeft, new DownLeftAction());

//        KeyStroke _actionHotkey = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK + KeyEvent.ALT_MASK, false);
//        addShiftedGlobalHotKeyBindings(new RightAction());
//        addShiftedGlobalHotKeyBindings(new LeftAction());
//        addShiftedGlobalHotKeyBindings(new UpAction());
//        addShiftedGlobalHotKeyBindings(new DownAction());
//        addShiftedGlobalHotKeyBindings(new UpRightAction());
//        addShiftedGlobalHotKeyBindings(new DownRightAction());
//        addShiftedGlobalHotKeyBindings(new UpLeftAction());
//        addShiftedGlobalHotKeyBindings(new DownLeftAction());
    }

    public PanelControlXY(DirectionalXYController dirCtrl) {
        this();
        this.dirCtrl = dirCtrl;
    }

    public void addShiftedGlobalHotKeyBindings(Action action) {
        KeyStroke key = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        addGlobalHotKeyBinding(key, action);
        int keyCode = key.getKeyCode();
        KeyStroke ctrlKeyStroke = KeyStroke.getKeyStroke(keyCode, KeyEvent.CTRL_DOWN_MASK, false);
        addGlobalHotKeyBinding(ctrlKeyStroke, action);
        KeyStroke ctrlShiftKeyStroke = KeyStroke.getKeyStroke(keyCode, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK, false);
        addGlobalHotKeyBinding(ctrlShiftKeyStroke, action);
    }

    public void addGlobalHotKeyBinding(KeyStroke keyStroke, Action action) {
        String actionKey = (String) action.getValue(Action.NAME);
        GlobalHotkeyManager hotkeyManager = GlobalHotkeyManager.getInstance();
        hotkeyManager.getInputMap().put(keyStroke, actionKey);
        hotkeyManager.getActionMap().put(actionKey, action);
    }

    /**
     * Binds an Action to a JComponent via the Action's configured ACCELERATOR_KEY.
     */
    public static void initKeyBinding(JComponent component, Action action) {
        String name = (String) action.getValue(Action.NAME);
        KeyStroke keyStroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        component.getActionMap().put(name, action);
        component.getInputMap().put(keyStroke, name);
    }

    public void initButtonKeyBinding(JComponent button, Action action) {
        // set the button to receive action when key is pressed
        String name = (String) action.getValue(Action.NAME);
        InputMap keyMap = new ComponentInputMap(button);
        KeyStroke keyStroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        keyMap.put(keyStroke, name);
        KeyStroke keyStrokeC = KeyStroke.getKeyStroke(keyStroke.getKeyCode(), 0, false);
        keyMap.put(keyStrokeC, name);
        keyStrokeC = KeyStroke.getKeyStroke(keyStroke.getKeyCode(), KeyEvent.SHIFT_MASK , false);
        keyMap.put(keyStrokeC, name);
        keyStrokeC = KeyStroke.getKeyStroke(keyStroke.getKeyCode(), KeyEvent.SHIFT_MASK + KeyEvent.CTRL_MASK , false);
        keyMap.put(keyStrokeC, name);
        // KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK | KeyEvent.ALT_MASK
        
        ActionMap actionMap = new ActionMapUIResource();
        actionMap.put(name, action);
        SwingUtilities.replaceUIActionMap(button, actionMap);
        SwingUtilities.replaceUIInputMap(button, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);
    }

    public void setDirectionalXYController(DirectionalXYController dirCtrl) {
        this.dirCtrl = dirCtrl;
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

        buttonUpperLeft.setAction(new UpLeftAction());
        buttonUpperLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xminusyplus.png"))); // NOI18N
        buttonUpperLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonUpperLeft.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonUpperLeft);

        buttonUp.setAction(new UpAction()
        );
        buttonUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/yPlus.gif"))); // NOI18N
        buttonUp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonUp.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonUp);

        buttonUpperRight.setAction(new UpRightAction());
        buttonUpperRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xplusyplus.png"))); // NOI18N
        buttonUpperRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonUpperRight.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonUpperRight);

        buttonLeft.setAction(new LeftAction());
        buttonLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xMinus.gif"))); // NOI18N
        buttonLeft.setToolTipText("left");
        buttonLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonLeft.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonLeft);

        buttonCenter.setAction(new CenterAction());
        buttonCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/center.png"))); // NOI18N
        buttonCenter.setToolTipText("center");
        buttonCenter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonCenter.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonCenter);

        buttonRight.setAction(new RightAction());
        buttonRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xPlus.gif"))); // NOI18N
        buttonRight.setToolTipText("right");
        buttonRight.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonRight.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonRight);

        buttonLowerLeft.setAction(new DownLeftAction());
        buttonLowerLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xminusyminus.png"))); // NOI18N
        buttonLowerLeft.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonLowerLeft.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonLowerLeft);

        buttonDown.setAction(new DownAction());
        buttonDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/yMinus.gif"))); // NOI18N
        buttonDown.setToolTipText("down");
        buttonDown.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonDown.setPreferredSize(new java.awt.Dimension(24, 24));
        jPanel1.add(buttonDown);

        buttonLowerRight.setAction(new DownRightAction());
        buttonLowerRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/gui/spatial/icons/xplusyminus.png"))); // NOI18N
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
   
    public class RightAction
            extends AbstractAction {

        public RightAction() {
            super("Move Right");
            putValue(SHORT_DESCRIPTION, "Move Right");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goRight(getMultiplier(e));
        }

    }

    public class LeftAction
            extends AbstractAction {

        public LeftAction() {
            super("Move Left");
            putValue(SHORT_DESCRIPTION, "Move Left");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dirCtrl.goLeft(getMultiplier(e));
        }

    }

    public class UpAction
            extends AbstractAction {

        public UpAction() {
            super("Move Up");
            putValue(SHORT_DESCRIPTION, "Move Up");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goUp(getMultiplier(e));
        }

    }

    public class DownAction
            extends AbstractAction {

        public DownAction() {
            super("Move Down");
            putValue(SHORT_DESCRIPTION, "Move Down");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goDown(getMultiplier(e));
        }

    }

    public class CenterAction
            extends AbstractAction {

        public CenterAction() {
            super("Move Center");
            putValue(SHORT_DESCRIPTION, "Move Center");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD , 0)); 
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goCenter(getMultiplier(e));
        }

    }

    public class UpRightAction
            extends AbstractAction {

        public UpRightAction() {
            super("Move UpRight");
            putValue(SHORT_DESCRIPTION, "Move UpRight");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goUpRight(getMultiplier(e));
        }

    }

    public class DownRightAction
            extends AbstractAction {

        public DownRightAction() {
            super("Move DownRight");
            putValue(SHORT_DESCRIPTION, "Move DownRight");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goDownRight(getMultiplier(e));
        }

    }

    public class DownLeftAction
            extends AbstractAction {

        public DownLeftAction() {
            super("Move DownLeft");
            putValue(SHORT_DESCRIPTION, "Move DownLeft");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
        }

        public void actionPerformed(ActionEvent e) {
            dirCtrl.goDownLeft(getMultiplier(e));
        }

    }

    public class UpLeftAction
            extends AbstractAction {

        public UpLeftAction() {
            super("Move UpLeft");
            putValue(SHORT_DESCRIPTION, "Move UpLeft");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
        }

        public void actionPerformed(ActionEvent e) {
            int mod = e.getModifiers();
            if ((mod & ActionEvent.SHIFT_MASK) > 0) {
                
            } else {
            dirCtrl.goUpLeft(getMultiplier(e));
            }
            
        }

    }
    // @todo Add four diagonal Actions...
    // Action Multiplier
    // CTRL or SHIFT = X10
    // CTRL &  SHIFT = X100            
    private int getMultiplier(ActionEvent e) {
        System.out.println("ActionEvent: " + e.paramString());
        System.out.println("      class: " + e.getClass());
        System.out.println("     source: " + e.getSource());
        System.out.println("  modifiers: " + e.getModifiers());
        int mod = e.getModifiers();
        if (e.getSource() instanceof javax.swing.JButton) {
//            System.out.println("Twas a Button");
            if ((mod & ActionEvent.CTRL_MASK) > 0 && (mod & ActionEvent.SHIFT_MASK) > 0) {
                return 100;
            }
            if ((mod & ActionEvent.SHIFT_MASK) > 0) {
                return 10;
            }
            return 1;
        } else { // was a keystroke
//            System.out.println("Twas a KeyStroke");
            if ((mod & InputEvent.CTRL_MASK) > 0 && (mod & InputEvent.SHIFT_MASK) > 0) {
                return 100;
            }
            if ((mod & InputEvent.SHIFT_MASK) > 0) {
                return 10;
            }
            return 1;
        }
    }
}

    
