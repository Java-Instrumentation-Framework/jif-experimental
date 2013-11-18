/*
 * MyAppActions.java
 *
 * Created on July 3, 2006, 9:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.action;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


public class MyAppActions extends JFrame {
    // Create the actions
    private FileOpenAction fileOpenAction = new FileOpenAction(this);

    // Create the menu
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem menuFileOpen = new JMenuItem(fileOpenAction);

    // Create the toolbar
    private JToolBar toolbar = new JToolBar("Main Toolbar", JToolBar.HORIZONTAL);
    private JButton tbFileOpen = new JButton(fileOpenAction);

    public MyAppActions() {
        //...
        /**
         * Build the menu
         */
        fileMenu.add(menuFileOpen);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        //...

        /**
         * Build the toolbar
         */
        tbFileOpen.setText("");
        toolbar.add(tbFileOpen);
        this.add(toolbar);

        //...
    }

    //...
    public void someCallback(File f) {
        System.out.println(f.getAbsolutePath());
    }

    public static void main(String[] args) {
        MyAppActions maa = new MyAppActions();
        maa.pack();
        maa.setVisible(true);
    }
}
