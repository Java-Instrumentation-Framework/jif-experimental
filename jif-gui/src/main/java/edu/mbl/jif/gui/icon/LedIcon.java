/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;


    /**
     * A small 3D light for control panels, etc.
     * May eventually contain methods to blink, etc.
     */
    public class LedIcon implements Icon {
        private Color color;
        private int HEIGHT = 16;
        private int WIDTH = 16;
        public int getIconWidth() { return WIDTH; }
        public int getIconHeight() { return HEIGHT; }

        public LedIcon(Color color) {
            setColor(color);
        }

        /**
         * Create a rectangle of the color, height and width desired
         * @param color
         * @param height
         * @param width
         */
        public LedIcon(Color color, int height, int width) {
            HEIGHT = height;
            WIDTH = width;
            setColor(color);
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paintIcon(Component comp, Graphics g, int x, int y) {
            g.setColor(color);
            g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
        }
    }