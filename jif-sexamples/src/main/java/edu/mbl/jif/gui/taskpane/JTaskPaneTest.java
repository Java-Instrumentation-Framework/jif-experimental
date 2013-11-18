/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.taskpane;

import javax.swing.*;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import com.l2fprod.common.swing.plaf.windows.WindowsLookAndFeelAddons;
import java.awt.Dimension;

public class JTaskPaneTest {

    private static final int NUM_TASK_PANE_GROUPS = 5;

    public static void main(String[] args) {
        try {
            // Simply installing Windows XP look and feel - see LookAndFeelAddons documentation
            // for more information.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);
        } catch (Exception e) {
        }

        // Create and set up the window.
        JFrame frame = new JFrame("JXScrollUpTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the task pane.
        JTaskPane rootPanel = new JTaskPane();
        createTaskPaneGroups(rootPanel);
        frame.setContentPane(rootPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    private static void createTaskPaneGroups(JTaskPane rootPanel) {
        for (int i = 0; i < NUM_TASK_PANE_GROUPS; i++) {
            JTaskPaneGroup taskGroup = new JTaskPaneGroup();
            boolean isEven = i % 2 == 0;
            // Task Groups support the concept of being 'special', which 
            // means they may be rendered uniquely to stand out from others.
            taskGroup.setSpecial(isEven);
            taskGroup.setExpanded(!isEven);
            taskGroup.setText("Task Group: " + i);
            taskGroup.add(new JButton("Button on TaskPane: " + i + " #1"));
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(100, 100));
            taskGroup.add(p);
            rootPanel.add(taskGroup);
        }
    }

    private static void addTaskPaneGroup(JTaskPane rootPanel, JComponent content, String title,
                                         boolean special, boolean expanded) {
        JTaskPaneGroup taskGroup = new JTaskPaneGroup();
        taskGroup.setSpecial(special);
        taskGroup.setExpanded(expanded);
        taskGroup.setText(title);
        taskGroup.add(content);
        rootPanel.add(taskGroup);
    }

}
