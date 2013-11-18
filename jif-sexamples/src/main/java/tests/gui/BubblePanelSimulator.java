package tests.gui;


import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jonathan Simon
 * Date: Jun 15, 2005
 * Time: 7:25:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class BubblePanelSimulator {

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
