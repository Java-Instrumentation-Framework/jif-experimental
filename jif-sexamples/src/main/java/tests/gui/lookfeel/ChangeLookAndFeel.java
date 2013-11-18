/*
 * ChangeLookAndFeel.java
 *
 * Created on February 11, 2006, 6:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.lookfeel;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ChangeLookAndFeel extends AbstractAction {
        public ChangeLookAndFeel(UIManager.LookAndFeelInfo laf) {
                super(laf.getName());
                this.laf = laf;
        }

        public void actionPerformed(ActionEvent evt) {
                Component c = (Component) evt.getSource();
                while (null != c.getParent()) {
                        c = c.getParent();
                        if (c instanceof JPopupMenu) {
                                JPopupMenu popup = (JPopupMenu) c;
                                c = popup.getInvoker();
                        }
                }
                try {
                        UIManager.setLookAndFeel(laf.getClassName());
                        SwingUtilities.updateComponentTreeUI(c);
                } catch (IllegalAccessException e) {
                        // problem with laf, we don't care.
                } catch (UnsupportedLookAndFeelException e) {
                        // problem with laf, we don't care.
                } catch (InstantiationException e) {
                        // problem with laf, we don't care.
                } catch (ClassNotFoundException e) {
                        // problem with laf, we don't care.
                }
        }

        public static JMenu createMenu() {
                JMenu lafMenu = new JMenu("Look and feel");
                ButtonGroup group = new ButtonGroup();
                UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
                LookAndFeel current = UIManager.getLookAndFeel();

                for (int i = 0; i < laf.length; i++) {
                        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(
                                new ChangeLookAndFeel(laf[i]));
                        if (laf[i].getName().equals(current.getName())) {
                                menuItem.setSelected(true);
                        }
                        group.add(menuItem);
                        lafMenu.add(menuItem);
                }
                return lafMenu;
        }

        private UIManager.LookAndFeelInfo laf;

}
