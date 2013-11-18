/*
 * ImageJAction.java
 *
 * Created on August 9, 2006, 9:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.camacq.actions;

import edu.mbl.jif.imagej.IJMaker;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */
public class ImageJAction
        extends AbstractAction {

    public ImageJAction() {
        putValue(NAME, "openImageJ");
        putValue(Action.ACTION_COMMAND_KEY, "openImageJ");
        putValue(SMALL_ICON,
                new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/imagej.gif")));
        putValue(SHORT_DESCRIPTION, "Open ImageJ");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_I));
    }


    public void actionPerformed(ActionEvent ae) {
        System.out.println("Opening ImageJ...");
        // @todo alternate Classpath & working dir
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                IJMaker.openImageJ();
            }


        });

    }


}