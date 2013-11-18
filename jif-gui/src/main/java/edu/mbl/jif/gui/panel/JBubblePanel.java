package edu.mbl.jif.gui.panel;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
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





    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JBubblePanel bubblePanel = new JBubblePanel();
        JTextPane textPane = new JTextPane();
        bubblePanel.setLayout(new BorderLayout());
        bubblePanel.add(textPane, BorderLayout.CENTER);

        Font normalFont = new Font("Arial", Font.PLAIN, 12);
        Font boldFont = new Font("Arial", Font.BOLD, 12);

        SimpleAttributeSet normal = new SimpleAttributeSet();
        SimpleAttributeSet bold = new SimpleAttributeSet();
        StyleConstants.setBold(bold, true);



        try {
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                "Your connection to ",
                normal);
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                "cvs.dev.java.net ",
                bold);
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                "failed. Here are a few possible reasons.\n\n",
                normal);
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                " Your computer is may not be connected to the network.\n"
                    + "* The CVS server name may be entered incorrectly.\n\n",
                normal);
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                "If you still can not connect, please contact support at ",
                normal);
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                "support@cvsclient.org",
                bold);
            textPane.getDocument().insertString(
                textPane.getDocument().getLength(),
                ".",
                normal);
        } catch (BadLocationException ex){
            ex.printStackTrace();
        }


        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(bubblePanel, BorderLayout.CENTER);

        frame.setBounds(200,300, 400,360);
        frame.setVisible(true);


    }

}
