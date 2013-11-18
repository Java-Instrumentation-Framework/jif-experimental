/*
 * JBubblePanel.java
 *
 * Created on May 1, 2006, 8:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui;

import javax.swing.*;
import java.awt.*;

public class JBubblePanel extends JPanel {

    private static final Color YELLOW_SNOW = new Color(255, 255, 204);
    private static final Color BUBBLE_BORDER = Color.GRAY;
    private final int ARC_WIDTH = 8;
    private final int ARC_HEIGHT = 8;
    protected static final int X_MARGIN = 4;
    protected static final int Y_MARGIN = 2;    

    public JBubblePanel() {
        super();
        init();
    }

    public JBubblePanel(LayoutManager layout) {
        super(layout);
        init();
    }

    public JBubblePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    public JBubblePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    protected void init() {
        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    /**
     * Paints the component.
     *
     * @param g the Graphics context to draw on
     */
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Insets insets = getRealInsets();
        Color savedColor = g2d.getColor();

        int rectX = insets.left;
        int rectY = insets.top;
        int rectWidth = getWidth() - insets.left - insets.right;
        int rectHeight = getHeight() - insets.top - insets.bottom;

        // Paint the yellow interior
        g2d.setColor(YELLOW_SNOW);
        g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, ARC_WIDTH, ARC_HEIGHT);

        // Draw the gray border
        g2d.setColor(BUBBLE_BORDER);
        g2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, ARC_WIDTH, ARC_HEIGHT);

        g2d.setColor(savedColor);
    }

    protected Insets getRealInsets() {
        return super.getInsets();
    }

    public Insets getInsets() {
        Insets realInsets = getRealInsets();
        Insets fakeInsets = new Insets(realInsets.top + Y_MARGIN, realInsets.left + X_MARGIN,
                realInsets.bottom + Y_MARGIN, realInsets.right + X_MARGIN);

        return fakeInsets;
    }

}
