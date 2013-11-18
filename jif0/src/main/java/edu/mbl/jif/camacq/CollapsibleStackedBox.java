
/*
 * CollapsibleStackedBox.java
 *
 * Created on August 14, 2006, 9:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.camacq;
/*
 * $Id: CollapsibleStackedBox.java.txt,v 1.1 2006/03/09 20:48:47 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.VerticalLayout;

/**
 * Stacks components vertically in boxes. Each box is created with a title and a
 * component.<br>
 * 
 * <p>
 * The <code>CollapsibleStackedBox</code> can be added to a
 * {@link javax.swing.JScrollPane}.
 * 
 * <p>
 * Note: this class is not part of the SwingX core classes. It is just an
 * example of what can be achieved with the components.
 * 
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public class CollapsibleStackedBox
        extends JPanel
        implements Scrollable {

    private Color titleBackgroundColor;
    private Color titleForegroundColor;
    private Color separatorColor;
    private Border separatorBorder;


    public CollapsibleStackedBox() {
        setLayout(new VerticalLayout());
        setOpaque(true);
        setBackground(Color.WHITE);

        separatorBorder = new SeparatorBorder();
        setTitleForegroundColor(Color.BLACK);
        setTitleBackgroundColor(new Color(230, 230, 230));
        setSeparatorColor(new Color(214, 223, 247));
    }


    public Color getSeparatorColor() {
        return separatorColor;
    }


    public void setSeparatorColor(Color separatorColor) {
        this.separatorColor = separatorColor;
    }


    public Color getTitleForegroundColor() {
        return titleForegroundColor;
    }


    public void setTitleForegroundColor(Color titleForegroundColor) {
        this.titleForegroundColor = titleForegroundColor;
    }


    public Color getTitleBackgroundColor() {
        return titleBackgroundColor;
    }


    public void setTitleBackgroundColor(Color titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
    }


    /**
     * Adds a new component to this <code>CollapsibleStackedBox</code>
     * 
     * @param title
     * @param component
     */
    public void addBox(String title, Component component, boolean collapsed) {
        final JXCollapsiblePane collapsible = new JXCollapsiblePane();
        collapsible.getContentPane().setBackground(Color.WHITE);
        collapsible.add(component);
        collapsible.setBorder(new CompoundBorder(separatorBorder,
                collapsible.getBorder()));
        collapsible.setAnimated(false);
        collapsible.setCollapsed(collapsed);
        Action toggleAction = collapsible.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
        // use the collapse/expand icons from the JTree UI
        toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON,
                UIManager.getIcon("Tree.expandedIcon"));
        toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON,
                UIManager.getIcon("Tree.collapsedIcon"));

        JXHyperlink link = new JXHyperlink(toggleAction);
        link.setText(title);
        link.setFont(link.getFont().deriveFont(Font.BOLD));
        link.setOpaque(true);
        link.setBackground(getTitleBackgroundColor());
        link.setFocusPainted(false);

        link.setUnclickedColor(getTitleForegroundColor());
        link.setClickedColor(getTitleForegroundColor());

        link.setBorder(new CompoundBorder(separatorBorder,
                BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        link.setBorderPainted(true);

        add(link);
        add(collapsible);
    }


    /**
     * @see Scrollable#getPreferredScrollableViewportSize()
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }


    /**
     * @see Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                            int orientation, int direction) {
        return 10;
    }


    /**
     * @see Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport) {
            return (((JViewport) getParent()).getHeight() > getPreferredSize().height);
        } else {
            return false;
        }
    }


    /**
     * @see Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }


    /**
     * @see Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        return 10;
    }


    /**
     * The border between the stack components. It separates each component with a
     * fine line border.
     */
    class SeparatorBorder
            implements Border {

        boolean isFirst(Component c) {
            return c.getParent() == null || c.getParent().getComponent(0) == c;
        }


        public Insets getBorderInsets(Component c) {
            // if the collapsible is collapsed, we do not want its border to be
            // painted.
            if (c instanceof JXCollapsiblePane) {
                if (((JXCollapsiblePane) c).isCollapsed()) {
                    return new Insets(0, 0, 0, 0);
                }
            }
            return new Insets(isFirst(c) ? 4 : 1, 0, 1, 0);
        }


        public boolean isBorderOpaque() {
            return true;
        }


        public void paintBorder(Component c, Graphics g, int x, int y,
                                 int width,
                                 int height) {
            g.setColor(getSeparatorColor());
            if (isFirst(c)) {
                g.drawLine(x, y + 2, x + width, y + 2);
            }
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }


    }


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame frame = new JFrame("CollapsibleAtWork");
        frame.setSize(200, 400);

        CollapsibleStackedBox box = new CollapsibleStackedBox();
        JScrollPane scrollPane = new JScrollPane(box);
        scrollPane.setBorder(null);
        frame.add(scrollPane, BorderLayout.CENTER);

        Border contentBorder = BorderFactory.createEmptyBorder(6, 8, 6, 8);

        // the control pane
        JToolBar controls = new JToolBar();
        controls.setFloatable(false);
        controls.setBorder(contentBorder);
        controls.setRollover(true);
        controls.setOpaque(false);

        JButton button = new JButton(UIManager.getIcon("InternalFrame.icon"));
        button.setOpaque(false);
        controls.add(button);
        button = new JButton(UIManager.getIcon("FileChooser.newFolderIcon"));
        button.setOpaque(false);
        controls.add(button);
        button = new JButton(UIManager.getIcon("FileChooser.upFolderIcon"));
        button.setOpaque(false);
        controls.add(button);
        box.addBox("Controls", controls, false);

        // the status pane
        JPanel status = new JPanel(new GridLayout(3, 2));
        status.setOpaque(false);
        status.setBorder(contentBorder);
        status.add(makeBold(new JLabel("Type:")));
        status.add(new JLabel("CPU"));
        status.add(makeBold(new JLabel("Configuration:")));
        status.add(new JLabel("Preset"));
        status.add(makeBold(new JLabel("Status:")));
        status.add(new JLabel("Running"));
        box.addBox("Status", status, false);

        // the profiling results
        JPanel profilingResults = new JPanel(new BorderLayout(3, 3));
        profilingResults.add("Center", new JScrollPane(new JTree()));
        profilingResults.setPreferredSize(new Dimension(200, 100));
        profilingResults.setBorder(contentBorder);
        box.addBox("Profiling Results", profilingResults, false);

        // the saved snapshots pane
        JPanel savedSnapshots = new JPanel(new BorderLayout(3, 3));
        savedSnapshots.setBorder(contentBorder);
        savedSnapshots.setOpaque(false);

        JComboBox combo = new JComboBox(new Object[]{" Java Demo"});
        savedSnapshots.add("North", combo);
        JList list = new JList(new Object[]{"<html><b>&nbsp;03:53:54 PM</b>"});
        list.setVisibleRowCount(5);
        savedSnapshots.add("Center", new JScrollPane(list));
        box.addBox("Saved Snapshots", savedSnapshots, true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    static JLabel makeBold(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }


}
