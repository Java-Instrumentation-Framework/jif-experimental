/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.workframe;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class ToolPanel extends JPanel implements Dockable {

    @Override
    public DockKey getDockKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getComponent() {
        return this;
    }

}
