package edu.mbl.jif.gui.error;

import java.awt.*;

import java.io.*;

import javax.swing.*;

/**
 * UncaughtExceptionHandler Window:
 * Creates a JFrame which receives first and subsequent uncaught exceptions.
 * Example usage:
 *
 *     Thread.UncaughtExceptionHandler handler =
 *           new edu.mbl.jif.gui.error.StackWindow("Show Exception Stack", 400, 200);
 *     Thread.setDefaultUncaughtExceptionHandler(handler);
 */
public class StackWindow extends JFrame implements Thread.UncaughtExceptionHandler {

    private JTextArea textArea;

    public StackWindow(String title, final int width, final int height) {
        super(title);
        setSize(width, height);
        textArea = new JTextArea();
        JScrollPane pane = new JScrollPane(textArea);
        textArea.setEditable(false);
        getContentPane().add(pane);
    }

    public void uncaughtException(Thread t, Throwable e) {
        addStackInfo(e);
    }

    public void addStackInfo(final Throwable t) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Bring window to foreground
                setVisible(true);
                toFront();
                // Convert stack dump to string
                StringWriter sw = new StringWriter();
                PrintWriter out = new PrintWriter(sw);
                t.printStackTrace(out);
                // Add string to end of text area
                textArea.append(sw.toString());
            /** @todo add: Also Log this... */
            }
        });
    }

    public static void main(String[] args) {
        Thread.UncaughtExceptionHandler handler =
            new edu.mbl.jif.gui.error.StackWindow("Show Exception Stack", 400, 200);
        Thread.setDefaultUncaughtExceptionHandler(handler);
        // test
        new Thread() {
            public void run() {
                throw new RuntimeException("x");
            }
        }.start();
    }

}
