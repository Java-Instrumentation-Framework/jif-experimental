/*
 * FrameCamAcqJ.java
 * Created on May 29, 2006, 6:07 PM
 */
package edu.mbl.jif.camacq;

import edu.mbl.jif.camacq.ApplicationFrame;
import com.vlsolutions.swing.docking.DockingDesktop;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.action.ActionManager;

/**
 *
 * @author  GBH
 */
public class ApplicationFrameStacked
    extends javax.swing.JFrame implements ApplicationFrame {

    CollapsibleStackedBox box;
    JScrollPane scrollPane;
    Border contentBorder;
    int lastTabIndex = 0;

    public ApplicationFrameStacked(String iconFile) {
        try {
            this.setIconImage(
                (new javax.swing.ImageIcon(getClass().getResource(iconFile))).getImage());
        } catch (Exception ex) {
        }
        this.setTitle("CamAcqJ");
        // ----------------------------------------------------------
        initComponents();
        box = new CollapsibleStackedBox();
        scrollPane = new JScrollPane(box);
        scrollPane.setBorder(null);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        contentBorder = BorderFactory.createEmptyBorder(6, 8, 6, 8);
    }

    public void setToolBar(JToolBar toolBar) {
        //box.addBox("Actions", toolBar, false);
        add(toolBar, BorderLayout.NORTH);
    }

    public void setToolBar(List actions) {
        JToolBar toolBar = new ActionContainerFactory(ActionManager.getInstance()).createToolBar(
            actions);
        setToolBar(toolBar);
    }

    public void addBox(String name, JComponent comp) {
        box.addBox("", comp, true);
    }

    public void addTool(String name, String iconPath, JPanel panel, String tooltip) {
        addTool(name, iconPath, panel, tooltip, false);
    }

    public void addTool(String name, String iconPath, JPanel panel, String tooltip,
                        boolean notCollapsed) {
        //panel.setOpaque(false);
        panel.setBorder(contentBorder);
        box.addBox(name, panel, !notCollapsed);

//        jTabbedPane1.addTab(name, loadIcon(iconPath), panel, tooltip);
//        jTabbedPane1.setBackgroundAt(lastTabIndex, panel.getBackground());
//        lastTabIndex++;
    }
//    public void addTab(String name, String iconPath, JPanel panel,
//                        String tooltip) {
//        jTabbedPane1.addTab("", loadIcon(iconPath), panel, tooltip);
//        jTabbedPane1.setBackgroundAt(lastTabIndex, panel.getBackground());
//        lastTabIndex++;
//    }
//
//
//    public void addTab(String name, Icon icon, JPanel panel, String tooltip) {
//        jTabbedPane1.addTab("", icon, panel, tooltip);
//        jTabbedPane1.setBackgroundAt(lastTabIndex, panel.getBackground());
//        lastTabIndex++;
//    }
//    public void addTab(IModule module) {
//        JPanel panel = module.getPanel();
//        if (panel != null) {
//            jTabbedPane1.addTab("",
//                    module.getIcon(),
//                    module.getPanel(),
//                    module.getName());
//            jTabbedPane1.setBackgroundAt(lastTabIndex, panel.getBackground());
//            lastTabIndex++;
//        }
//    }
    public Icon loadIcon(String path) {
        return (Icon) new javax.swing.ImageIcon(getClass().getResource(path));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public DockingDesktop getDesk() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
