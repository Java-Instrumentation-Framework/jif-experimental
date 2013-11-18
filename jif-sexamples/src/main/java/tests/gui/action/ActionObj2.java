/*
 * ActionObj2.java
 *
 * Created on July 3, 2006, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.action;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


public class ActionObj2 {
    public static void main(String[] args) {
        new GUI();
    } //end main
} //end ActionObj02
//===================================//


class GUI extends JFrame {
    //Create three containers
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JToolBar toolBar = new JToolBar();
    JPanel panel = new JPanel();

    //Create two Action objects
    Action actionObj01 = new MyAction();
    Action actionObj02 = new MyAction();

    //Create four check boxes and a
    // control panel, which will be used
    // to manipulate the Action objects
    // and their visual components.
    JCheckBox ckBox01 = new JCheckBox("Disable01");
    JCheckBox ckBox02 = new JCheckBox("Disable02");
    JCheckBox ckBox03 = new JCheckBox("Toggle Icon01");
    JCheckBox ckBox04 = new JCheckBox("Toggle Icon02");
    JPanel controlPanel = new JPanel();

    GUI() { //constructor
            //Construct and decorate the menu
            // and put it in place.
        menuBar.add(menu);
        menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
        setJMenuBar(menuBar);

        //Decorate the JToolBar and place
        // it in the North position.
        toolBar.setBorder(new BevelBorder(BevelBorder.RAISED));
        getContentPane().add(toolBar, BorderLayout.NORTH);

        //Decorate the JPanel and place it
        // in the South position.
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        getContentPane().add(panel, BorderLayout.SOUTH);

        //Set some keyed properties for
        // each of the two Action objects.
        actionObj01.putValue(Action.NAME, "actionObj01");
        actionObj01.putValue(Action.SMALL_ICON, new ImageIcon("purple.gif"));
        actionObj01.putValue(Action.SHORT_DESCRIPTION, "ToolTip for actionObj01");

        actionObj02.putValue(Action.NAME, "actionObj02");
        actionObj02.putValue(Action.SMALL_ICON, new ImageIcon("purple.gif"));
        actionObj02.putValue(Action.SHORT_DESCRIPTION, "ToolTip for actionObj02");

        //Put a JMenuItem on the menu.  Set
        // its Action object using the
        // setAction method. This is 
        // one approach.
        JMenuItem mnuA1 = new JMenuItem();
        mnuA1.setAction(actionObj01);
        menu.add(mnuA1);

        //Put a JMenuItem on the menu. Set
        // its Action object in the
        // constructor. This is a 
        // different approach.
        JMenuItem mnuA2 = new JMenuItem(actionObj02);
        menu.add(mnuA2);

        //Put a JButton on the toolbar.
        // Set its Action object using the
        // setAction method.  Also register
        // an ordinary Action listener
        // on it.
        JButton butB1 = new JButton();
        butB1.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Ordinary Action Listener");
                    } //end actionPerformed()
                } //end ActionListener
        ); //end addActionListener
        butB1.setAction(actionObj01);
        toolBar.add(butB1);

        //Put a JButton on the toolbar.
        // Set its Action object.
        JButton butB2 = new JButton();
        butB2.setAction(actionObj02);
        toolBar.add(butB2);

        //Put a JButton on the JPanel.
        // Set its Action object.
        JButton butC = new JButton();
        butC.setAction(actionObj01);
        panel.add(butC);

        //Put a JMenuItem on the JPanel.
        // Set its Action object.
        JMenuItem mnuC = new JMenuItem();
        mnuC.setAction(actionObj02);
        panel.add(mnuC);

        //Construct the control panel and
        // put it in the Center
        controlPanel.setLayout(new GridLayout(2, 2, 3, 3));
        controlPanel.add(ckBox01);
        controlPanel.add(ckBox02);
        controlPanel.add(ckBox03);
        controlPanel.add(ckBox04);
        getContentPane().add(controlPanel, BorderLayout.CENTER);

        //Set the size and make the GUI
        // visible
        setSize(350, 200);
        setVisible(true);
        setTitle("Copyright 2002, " + "R.G.Baldwin");

        //The following anonymous inner
        // allow the user to manipulate the
        // Action objects and their 
        // associated visual components.

        //Register ActionListener objects
        // on each of the check boxes.
        // This makes it possible to 
        // disable and enable the two
        // Actions objects independently of
        // one another.  It also makes it
        // possible to toggle the icons
        // between two different images
        // on each Action object when the
        // Action object is enabled.
        ckBox01.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equals("Disable01")) {
                            ckBox01.setText("Enable01");
                            actionObj01.setEnabled(false);
                            //Disable ability to toggle
                            // the IconImage.
                            ckBox03.setEnabled(false);
                        } else {
                            ckBox01.setText("Disable01");
                            actionObj01.setEnabled(true);
                            //Enable ability to toggle
                            // the IconImage.
                            ckBox03.setEnabled(true);
                        } //end else
                    } //end actionPerformed()
                } //end ActionListener
        ); //end addActionListener

        ckBox02.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equals("Disable02")) {
                            ckBox02.setText("Enable02");
                            actionObj02.setEnabled(false);
                            //Disable ability to toggle
                            // the IconImage.
                            ckBox04.setEnabled(false);
                        } else {
                            ckBox02.setText("Disable02");
                            actionObj02.setEnabled(true);
                            //Enable ability to toggle
                            // the IconImage.
                            ckBox04.setEnabled(true);
                        } //end else
                    } //end actionPerformed()
                } //end ActionListener
        ); //end addActionListener

        ckBox03.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get file name for the
                        // ImageIcon object.
                        String gif = (actionObj01.getValue(
                                    Action.SMALL_ICON)).toString();

                        if ((gif).equals("purple.gif")) {
                            actionObj01.putValue(Action.SMALL_ICON, new ImageIcon("purple.gif"));
                        } else {
                            actionObj01.putValue(Action.SMALL_ICON, new ImageIcon("purple.gif"));
                        } //end else
                    } //end actionPerformed()
                } //end ActionListener
        ); //end addActionListener    

        ckBox04.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get file name for the
                        // ImageIcon object.
                        String gif = (actionObj02.getValue(
                                    Action.SMALL_ICON)).toString();

                        if ((gif).equals("purple.gif")) {
                            actionObj02.putValue(Action.SMALL_ICON, new ImageIcon("purple.gif"));
                        } else {
                            actionObj02.putValue(Action.SMALL_ICON, new ImageIcon("purple.gif"));
                        } //end else
                    } //end actionPerformed()
                } //end ActionListener
        ); //end addActionListener

        //Create a WindowListener used
        // to terminate the program
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    } //end windowClosing()
                } //end WindowAdapter
        ); //end addWindowListener
    } //end constructor
} //end GUI class


//===================================//

//This is the class from which the
// Action objects are instantiated.
class MyAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action: " + ((AbstractButton) e.getSource()).getActionCommand());
    } //end actionPerformed
} //end class MyAction
