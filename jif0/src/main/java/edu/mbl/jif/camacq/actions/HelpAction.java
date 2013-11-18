/*
 * HelpAction.java
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
public class HelpAction extends AbstractAction {
        public HelpAction() {
            putValue(NAME, "help");
            putValue(Action.ACTION_COMMAND_KEY, "help");
            putValue(SMALL_ICON,
                    new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/information.png")));
            putValue(SHORT_DESCRIPTION, "Help");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_H));
        }
        public void actionPerformed(ActionEvent ae) {
            System.out.println("Opening Help...");
        }
    }   
