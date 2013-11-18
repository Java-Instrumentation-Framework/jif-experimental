package edu.mbl.jif.camacq;

import com.vlsolutions.swing.docking.DockingDesktop;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author GBH
 */
public interface ApplicationFrame {

    //void addBox(String name, JComponent comp);

    void addTool(String name, String iconPath, JPanel panel, String tooltip);

    void addTool(String name, String iconPath, JPanel panel, String tooltip, boolean notCollapsed);

    DockingDesktop getDesk();

    Icon loadIcon(String path);

    void setToolBar(JToolBar toolBar);

    void setToolBar(List actions);
    
    void setup();

}
