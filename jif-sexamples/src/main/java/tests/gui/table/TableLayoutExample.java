//**********************************************************************
// Package
//**********************************************************************

package tests.gui.table;

//**********************************************************************
// Import list
//**********************************************************************

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This example compares the GridBagLayout to the TableLayout. The
 * same layout is created using each layout manager. The layouts are
 * displayed in their own window.
 *
 * @author Antonio Freixas
 */

// Copyright � 2004 Antonio Freixas
// All Rights Reserved.

public class TableLayoutExample
{

//**********************************************************************
// Public Constants
//**********************************************************************

//**********************************************************************
// Private Constants
//**********************************************************************

//**********************************************************************
// Private Members
//**********************************************************************

//**********************************************************************
// main
//**********************************************************************

public static void
main(
    String[] args)
{
    new TableLayoutExample();
}

//**********************************************************************
// Constructors
//**********************************************************************

/**
 * Create the two windows and display them.
 */

public
TableLayoutExample()
{
    JFrame frame1 = createGridBagLayout();
    JFrame frame2 = createTableLayout();
    frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame1.pack();
    frame2.pack();
    frame1.setLocation(10, 100);
    frame2.setLocation(400, 100);
    frame1.setVisible(true);
    frame2.setVisible(true);
}

//**********************************************************************
// Private
//**********************************************************************

/**
 * Create the layout using GridBagLayout. This code comes from the API
 * page for GridBagLayout.
 *
 * @return The JFrame containing the layout.
 */

public JFrame
createGridBagLayout()
{
    JFrame frame = new JFrame("GridBagLayout");

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    JPanel panel = new JPanel(gridbag);
    frame.getContentPane().add(panel);

    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    makebutton(panel, "Button1", gridbag, c);
    makebutton(panel, "Button2", gridbag, c);
    makebutton(panel, "Button3", gridbag, c);

    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    makebutton(panel, "Button4", gridbag, c);

    c.weightx = 0.0;			//reset to the default
    makebutton(panel, "Button5", gridbag, c);	//another row

    c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
    makebutton(panel, "Button6", gridbag, c);

    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    makebutton(panel, "Button7", gridbag, c);

    c.gridwidth = 1;			//reset to the default
    c.gridheight = 2;
    c.weighty = 1.0;
    makebutton(panel, "Button8", gridbag, c);

    c.weighty = 0.0;			//reset to the default
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.gridheight = 1;			//reset to the default
    makebutton(panel, "Button9", gridbag, c);
    makebutton(panel, "Button10", gridbag, c);

    return frame;
}

/**
 * Helper method for createGridBagLayout().
 *
 * @param panel The panel to add the button to.
 * @param name The button's label.
 * @param gridbag The GridBagLayout to use.
 * @param c The constraints to use.
 */

private void
makebutton(
    JPanel panel,
    String name,
    GridBagLayout gridbag,
    GridBagConstraints c)
{
    JButton button = new JButton(name);
    gridbag.setConstraints(button, c);
    panel.add(button);
}

/**
 * Create the layout using TableLayout.
 *
 * @return The JFrame containing the layout.
 */

public JFrame
createTableLayout()
{
    JFrame frame = new JFrame("TableLayout");

    JPanel panel = new JPanel(new TableLayout("cols=4"));
    frame.getContentPane().add(panel);

    panel.add(new JButton("Button1"));
    panel.add(new JButton("Button2"));
    panel.add(new JButton("Button3"));
    panel.add(new JButton("Button4"));
    panel.add(new JButton("Button5"), "cspan=4");
    panel.add(new JButton("Button6"), "cspan=3");
    panel.add(new JButton("Button7"));
    panel.add(new JButton("Button8"), "rspan=2");
    panel.add(new JButton("Button9"), "cspan=3");
    panel.add(new JButton("Button10"), "cspan=3 rweight=1");

    return frame;
}

}
