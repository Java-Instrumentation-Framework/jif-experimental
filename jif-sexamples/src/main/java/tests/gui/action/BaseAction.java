/*
 * BaseAction.java
 *
 * Created on July 3, 2006, 9:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.action;

import javax.swing.*;


public abstract class BaseAction extends AbstractAction {
    public BaseAction(String name, ImageIcon icon, String tooltip, Integer mnemonic,
        KeyStroke accelerator) {
        this.putValue(this.NAME, name);
        if (icon != null) {
            this.putValue(this.SMALL_ICON, icon);
        }
        this.putValue(this.SHORT_DESCRIPTION, tooltip);
        this.putValue(this.MNEMONIC_KEY, mnemonic);
        this.putValue(this.ACCELERATOR_KEY, accelerator);
    }
}
