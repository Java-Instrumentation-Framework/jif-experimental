/*
 * ImageJAction.java
 *
 * Created on August 9, 2006, 9:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.camacq.actions;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.lightfield.LFViewController;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author GBH
 */
public  class LFDisplayOpenAction extends AbstractAction {
    
    InstrumentController ctrl;
    
    public LFDisplayOpenAction(final InstrumentController ctrl) {
        this.ctrl = ctrl;
        putValue(NAME, "LFDisplay");
        putValue(Action.ACTION_COMMAND_KEY, "openLightfieldDisplay");
        putValue(SMALL_ICON,
                new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/lightfield24.png")));
        putValue(SHORT_DESCRIPTION, "Open Lightfield Display");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
    }
    
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Opening Lightfield Display...");
        ((LFViewController)((InstrumentController) CamAcqJ.getInstance().getController()).getController("lightfield")).openLightviewPerspective();
//        Runnable runner = new Runnable() {
//            public void run() {
//                ((InstrumentController) CamAcqJ.getInstance().getController()).getLFVCtrl().openLightviewPerspective();
//            }
//        };
//        // runner.run();
//        EventQueue.invokeLater(runner);
    }
}