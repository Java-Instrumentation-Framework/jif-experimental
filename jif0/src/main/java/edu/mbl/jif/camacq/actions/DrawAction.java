/*
 * DrawAction.java
 *
 * Created on August 9, 2006, 9:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.camacq.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author GBH
 */
public     
    class DrawAction extends AbstractAction {
        public DrawAction() {
            putValue(NAME, "openPlasmaDraw");
            putValue(Action.ACTION_COMMAND_KEY, "openPlasmaDraw");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/draw.png")));
            putValue(SHORT_DESCRIPTION, "Open PlasmaDraw");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
        }
        public void actionPerformed(ActionEvent ae) {
            System.out.println("Opening PlasmaDraw...");
        }
    }
    