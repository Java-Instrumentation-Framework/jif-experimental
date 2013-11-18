/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.camacq;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class MyDockablePanel 
    // Panels are added to this dockable panel

        extends JPanel
        implements Dockable {

        DockKey key;
        JPanel actualPanel;

        public MyDockablePanel(String name, String iconPath, JPanel actualPanel, String tooltip)
          {
            this.actualPanel = actualPanel;
            key = new DockKey(name);
            setLayout(new BorderLayout());
            actualPanel.setPreferredSize(new Dimension(400, 180));
            add(actualPanel, BorderLayout.CENTER);
            //desk.setDockableWidth(this, 1.0);
            key.setName(name);
            key.setTooltip(tooltip);
            key.setIcon(new ImageIcon(getClass().getResource(iconPath)));

            // customized behaviour
            key.setCloseEnabled(false);
            //key.setAutoHideEnabled(false);
            key.setFloatEnabled(true);
            //key.setResizeWeight(0.5f); // takes all resizing
          }

        public DockKey getDockKey()
          {
            return key;
          }

        public Component getComponent()
          {
            return this;
          }

    }