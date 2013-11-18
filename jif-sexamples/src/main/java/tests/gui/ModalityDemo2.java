/*
 * ModalityDemo2.java
 *
 * Created on June 18, 2007, 4:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui;

import sun.awt.*;

import java.awt.*;
import java.awt.event.*;


public class ModalityDemo2 {
    // First document (red): frame, modeless dialog box, document-modal dialog box
    private static Frame f1;
    private static Dialog d11;
    private static Dialog d12;

    // Second document (blue): frame, modeless dialog box, document-modal dialog box
    private static Frame f2;
    private static Dialog d21;
    private static Dialog d22;

    // Third document (green): modal excluded frame
    private static Frame f3;

    // Fourth document (grey): frame, file dialog box application-modal dialog box
    private static Frame f4;
    private static FileDialog fd4;
    private static WindowListener closeWindow = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        };

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        int sw = gc.getBounds().width - 64;
        int sh = gc.getBounds().height - 64;

        Label l;
        Button b;
        MenuBar mb;
        Menu m;
        MenuItem mi;

        Font labelFont = new Font("Tahoma", 24, Font.PLAIN);

        // First document
        f1 = new Frame("Parent Frame");
        f1.setBounds(32, 32, 300, 200);
        f1.addWindowListener(closeWindow);
        mb = new MenuBar();
        m = new Menu("Test");
        mi = new MenuItem("Show modeless");
        mi.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        d11.setVisible(true);
                    }
                });
        m.add(mi);
        mb.add(m);
        f1.setMenuBar(mb);
        f1.setLayout(new BorderLayout());
        l = new Label("FRAME");
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        l.setBackground(Color.RED);
        f1.add(l, BorderLayout.CENTER);
        f1.setVisible(true);

        d11 = new Dialog(f1, "Modeless Dialog");

        // An old constructor. Because the flag "modal" is missed, the dialog box is modeless.
        d11.setBounds(132, 132, 300, 200);
        d11.addWindowListener(closeWindow);
        d11.setLayout(new BorderLayout());
        l = new Label("MODELESS");
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        l.setBackground(Color.RED);
        d11.add(l, BorderLayout.CENTER);
        b = new Button("Show document-modal");
        b.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        d12.setVisible(true);
                    }
                });
        d11.add(b, BorderLayout.SOUTH);

        d12 = new Dialog((Window) d11, "Document-modal Dialog", Dialog.ModalityType.DOCUMENT_MODAL);

        // New constructor with parameter for its modality type
        // Parameter type is enum Dialog.ModalityType.
        d12.setBounds(232, 232, 300, 200);
        d12.addWindowListener(closeWindow);
        d12.setLayout(new BorderLayout());
        l = new Label("DOCUMENT_MODAL");
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        l.setBackground(Color.RED);
        d12.add(l, BorderLayout.CENTER);

        // Second document
        f2 = new Frame("Parent Frame");
        f2.setBounds(sw - 300 + 32, 32, 300, 200);
        f2.addWindowListener(closeWindow);
        mb = new MenuBar();
        m = new Menu("Test");
        mi = new MenuItem("Show modeless");
        mi.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        d21.setVisible(true);
                    }
                });
        m.add(mi);
        mb.add(m);
        f2.setMenuBar(mb);
        f2.setLayout(new BorderLayout());
        l = new Label("FRAME");
        l.setBackground(Color.BLUE);
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        f2.add(l, BorderLayout.CENTER);
        f2.setVisible(true);

        d21 = new Dialog(f2, "Modeless Dialog");
        d21.setBounds(sw - 400 + 32, 132, 300, 200);
        d21.addWindowListener(closeWindow);
        d21.setLayout(new BorderLayout());
        l = new Label("MODELESS");
        l.setBackground(Color.BLUE);
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        d21.add(l, BorderLayout.CENTER);
        b = new Button("Show document-modal");
        b.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        d22.setVisible(true);
                    }
                });
        d21.add(b, BorderLayout.SOUTH);

        d22 = new Dialog((Window) d21, "Document-modal Dialog", Dialog.ModalityType.DOCUMENT_MODAL);

        /* A dialog box that uses DOCUMENT_MODAL blocks input to all top-level windows from the 
         same document, except those from its own child hierarchy. A document is a top-level 
         window without an owner. It may contain child windows that are treated as a single 
         document together with the top-level window. Because every top-level window must 
         belong to some document, its root can be found at the top-nearest window without an owner.
        */
        
        d22.setBounds(sw - 500 + 32, 232, 300, 200);
        d22.addWindowListener(closeWindow);
        d22.setLayout(new BorderLayout());
        l = new Label("DOCUMENT_MODAL");
        l.setBackground(Color.BLUE);
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        d22.add(l, BorderLayout.CENTER);

        // Third document
        f3 = new Frame("Excluded Frame");

        f3.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        /*A dialog box set to APPLICATION_MODAL blocks all top-level windows from the same
         Java application, except those from its own child hierarchy. If several applets are
         launched in a browser, they can be treated either as separate applications or as a
         single one. This behavior is implementation-dependent.
         Note that here, f3 will not be blocked by APPLICATION_MODAL and DOCUMENT_MODAL dialog boxes.
        */
        f3.setBounds(32, sh - 200 + 32, 300, 200);
        f3.addWindowListener(closeWindow);
        f3.setLayout(new BorderLayout());
        l = new Label("EXCLUDED FRAME");
        l.setBackground(Color.GREEN);
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        f3.add(l, BorderLayout.CENTER);
        b = new Button("I'm alive!");
        f3.add(b, BorderLayout.SOUTH);
        f3.setVisible(true);

        // Fourth document
        f4 = new Frame("Parent Frame");
        f4.setBounds(sw - 300 + 32, sh - 200 + 32, 300, 200);
        f4.addWindowListener(closeWindow);
        f4.setLayout(new BorderLayout());
        l = new Label("FRAME");
        l.setBackground(Color.GRAY);
        l.setAlignment(Label.CENTER);
        l.setFont(labelFont);
        b = new Button("Show file dialog");
        b.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fd4.setVisible(true);
                    }
                });
        f4.add(b, BorderLayout.SOUTH);
        f4.setVisible(true);

        fd4 = new FileDialog(f4, "File Dialog", FileDialog.LOAD);
        //File dialog boxes are APPLICATION_MODAL by default for backward compatibility.
        fd4.setBounds(sw - 400 + 32, sh - 300 + 32, 300, 200);
    }
}
