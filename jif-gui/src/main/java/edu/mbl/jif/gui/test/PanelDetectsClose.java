package edu.mbl.jif.gui.test;

//Subclass CleanupPanel and override method cleanup to slide in you custom cleanup code...
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelDetectsClose extends JPanel {

    private WindowListener wl = new WindowAdapter() {
        public void windowClosing(WindowEvent evt) {
            cleanup();
        }
    };

    @Override
    public void addNotify() {
        super.addNotify();
        System.out.println("addNotify");
        SwingUtilities.windowForComponent(this).addWindowListener(wl);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        System.out.println("removeNotify");
        SwingUtilities.windowForComponent(this).removeWindowListener(wl);
    }

    protected void cleanup() {
        System.out.println("cleanup called");
    }

    //sample main
    public static void main(String[] args) {
        final JPanel app = new PanelDetectsClose();
        app.add(new JLabel("Hi, mom"));
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                createFrame(this, app);
            }

        };
        createFrame(al, app);
    }

    static void createFrame(ActionListener al, JPanel panel) {
        Window window = SwingUtilities.windowForComponent(panel);
        if (window != null) {
            JFrame f = (JFrame) window;
            f.getContentPane().remove(panel);
            f.validate();
            f.repaint();
        }
        JFrame f = new JFrame("CleanupPanel");
        Container cp = f.getContentPane();
        JButton btn = new JButton("launch");
        btn.addActionListener(al);
        cp.add(panel, BorderLayout.CENTER);
        cp.add(btn, BorderLayout.SOUTH);
        f.pack();
        f.setVisible(true);
    }

}