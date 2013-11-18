/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.config;

import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * addToSettingGroup(“SettingsGroup”, “Setting”, iconUrl, component)
 * 
 * If SettingGroup has not already been referred to, it is created and added as a tab.
 * @author GBH
 */
public class ConfigurationManager {

    JTabbedPane tabs;
    Map<String, SettingsGroupPanel> settingGroups = new HashMap<String, SettingsGroupPanel>();

    public JTabbedPane getConfigManagerPanel() {
        tabs = new JTabbedPane();
        return tabs;
    }

    public JFrame openConfigManagerFrame(String title) {
        FrameForTest f = new FrameForTest();
        f.setTitle(title);
        f.setLayout(new BorderLayout());
        f.add("Center", getConfigManagerPanel() );
        f.pack();
        return f;
    }

    /* addToSettingGroup(“SettingsGroup”, “Setting”, iconUrl, component)
    If SettingGroup has not already been referred to, it is created and added as a tab.
     */
    public void addToSettingGroup(String settingsGroup, String title, String iconUrl,
                                  final Component component) {
        if (settingGroups.containsKey(settingsGroup)) {
            (settingGroups.get(settingsGroup)).addButton(title, iconUrl, component);
        } else {
            addSettingsGroup(settingsGroup);
            addToSettingGroup(settingsGroup, title, iconUrl, component);
        }
    }

    public void addSettingsGroup(String setGroup) {
        JButtonBar toolbar = new JButtonBar(JButtonBar.VERTICAL);
        toolbar.setUI(new BlueishButtonBarUI());
        SettingsGroupPanel panel = new SettingsGroupPanel(toolbar);
        tabs.addTab(setGroup, panel);
        settingGroups.put(setGroup, panel);
    }

    // just for demo
    private static JPanel makePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel top = new JLabel(title);
        top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        top.setFont(top.getFont().deriveFont(Font.BOLD));
        top.setOpaque(true);
        top.setBackground(panel.getBackground().brighter());
        panel.add("North", top);
        panel.setPreferredSize(new Dimension(400, 300));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        return panel;
    }

    static class SettingsGroupPanel extends JPanel {

        JButtonBar toolbar;
        ButtonGroup group;
        private Component currentComponent;

        public SettingsGroupPanel(JButtonBar _toolbar) {
            this.toolbar = _toolbar;
            setLayout(new BorderLayout());
            add("West", toolbar);
            group = new ButtonGroup();
        }

        private void show(Component component) {
            if (currentComponent != null) {
                remove(currentComponent);
            }
            add("Center", currentComponent = component);
            revalidate();
            repaint();
        }

        private void addButton(String title, String iconUrl,
                               final Component component) {
            addButton(title, iconUrl, component, this.toolbar, this.group);
        }

        private void addButton(String title, String iconUrl,
                               final Component component, JButtonBar bar, ButtonGroup group) {
            Action action = new AbstractAction(title) //, new ImageIcon( ButtonBarPanel.class.getResource(iconUrl)))
            {
                public void actionPerformed(ActionEvent e) {
                    show(component);
                }
            };
            JToggleButton button = new JToggleButton(action);

            try {
                button.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconUrl)));

            } catch (Exception e) {
            }
            bar.add(button);
            group.add(button);
            if (group.getSelection() == null) {
                button.setSelected(true);
                show(component);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {

                ConfigurationManager configMgr = new ConfigurationManager();
                JFrame f = configMgr.openConfigManagerFrame("Configuration Manager Demo");
                configMgr.addToSettingGroup("First", "Number1", "icons/cubeVGB16.gif", makePanel(
                    "NumberOne"));
                configMgr.addToSettingGroup("First", "Number2", "icons/cubeVGB16.gif", makePanel(
                    "NumberTwo"));
                configMgr.addToSettingGroup("Second", "And1", "icons/cubeVGB16.gif", makePanel(
                    "AndOne"));
                f.pack();
            //configMgr.addButton("Test", "", component);

            }

        });
    }

}