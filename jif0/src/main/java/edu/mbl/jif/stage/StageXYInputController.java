/*
 * StageXYInputController.java
 *
 * Created on December 17, 2006, 10:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.stage;

import edu.mbl.jif.gui.spatial.DirectionalXYController;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */
public class StageXYInputController
    implements DirectionalXYController {

    private StageXYController stageXYCtrl;

    public StageXYInputController(StageXYController ctrl) {
        this.stageXYCtrl = ctrl;
        if (ctrl != null) {
            Point2D initPos = ctrl.getPos();
        }
    }

    public void reReference() {
        getStageXYCtrl().reference();
    }

    public void stop() {
        getStageXYCtrl().stop();
    }

    public int goUp(int n) {
        moveRelative(0,-n);
        return 0;
    }

    public int goDown(int n) {
        moveRelative(0,n);
        return 0;
    }

    public int goLeft(int n) {
        moveRelative(-n, 0);
        return 0;
    }

    public int goRight(int n) {
        moveRelative(n,0);
        return 0;
    }

    public int goCenter(int n) {
        getStageXYCtrl().goHome();
        return 0;
    }

    public int goUpRight(int n) {
        moveRelative(n, -n);
        return 0;
    }

    public int goDownRight(int n) {
        moveRelative(n, n);
        return 0;
    }

    public int goUpLeft(int n) {
        moveRelative(-n, -n);
        return 0;
    }

    public int goDownLeft(int n) {
        moveRelative(-n, n);
        return 0;
    }

    public int goTop(int n) {
        return 0;
    }

    public int goBottom(int n) {
        return 0;
    }

    public int goLeftLimit(int n) {
        return 0;
    }

    public int goRightLimit(int n) {
        return 0;
    }

    private void moveRelative(final int x, final int y) {
        // is edt necessary here?
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
                getStageXYCtrl().moveRelative(x, y);
//            }
//        });
    }

    public StageXYController getStageXYCtrl() {
        return stageXYCtrl;
    }

}


