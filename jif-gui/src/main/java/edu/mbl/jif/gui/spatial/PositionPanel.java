/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.spatial;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class PositionPanel
        extends JPanel {

    double width;
    double height;
    double x;
    double y;
    double viewW;
    double viewH;
    private double xScale;
    private double yScale;

    public PositionPanel() {
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
//                if (scale > 0.0) {
//                    if (isFullImageInPanel()) {
//                        centerImage();
//                    } else if (isImageEdgeInPanel()) {
//                        scaleOrigin();
//                    }
//                    if (isNavigationImageEnabled()) {
//                        createNavigationImage();
//                    }
//                    repaint();
//                }
//                previousPanelSize = getSize();
                xScale = getSize().getWidth() / width;
                yScale = getSize().getHeight() / height;
                viewW = xScale * viewW;
                viewH = yScale * viewH;
                x = xScale * x;
                y = yScale * y;
                repaint();
            }

        });
    }

    public void setDimensions(double width, double height,
                              double viewW, double viewH) {
        this.width = width;
        this.height = height;
        xScale = getSize().getWidth() / width;
        yScale = getSize().getHeight() / height;
        this.viewW = xScale * viewW;
        this.viewH = yScale * viewH;

    }

    public void updatePosition(double x, double y) {
        this.x = xScale * x;
        this.y = yScale * y;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.red);
        //g2.draw(new Rectangle2D.Double(x, y, viewW, viewH));
        g2.drawRect((int) x, (int) y, (int) viewW, (int) viewH);

    }

}
