
package tests.gui.list.listSorted;

/** 
 * This example demonstrates how to simplify tracking the selection
 * in a JList whose selection model is in SINGLE_SELECTION mode.
 * 
 * Tested against swing-1.1, JDK1.1.7.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.lang.reflect.Method;


/**
 * Create a SINGLE_SELECTION ListSelectionModel that calls a new 
 * method, updateSingleSelection(), each time the selection
 * changes.  This can be a little bit more convienent than using the 
 * ListModels ListSelectionListener, since ListSelectionListeners
 * are only given the range of indices that the change spans.
 */
class SingleSelectionModel extends DefaultListSelectionModel 
{
    public SingleSelectionModel() {
	setSelectionMode(SINGLE_SELECTION);
    }

    public void setSelectionInterval(int index0, int index1) {
	int oldIndex = getMinSelectionIndex();
	super.setSelectionInterval(index0, index1);
	int newIndex = getMinSelectionIndex();
	if (oldIndex != newIndex) {
	    updateSingleSelection(oldIndex, newIndex);
	}
    }

    public void updateSingleSelection(int oldIndex, int newIndex) {
    }
}


/** 
 * Create a JList that displays all of the methods in JComponent.
 * The list has a SingleSelectionModel that displays selection
 * changes in a pair of text fields.
 */
public class SingleSelection
{
    public static void main(String[] args) throws Exception
    {
	Method[] methods = JComponent.class.getMethods();
	final JList list = new JList(methods);

	final JTextField oldTextField = new JTextField("Select a List Item");
	final JTextField newTextField = new JTextField();

	/* The SingleSelectionModel class calls updateSingleSelection() each 
	 * time the selection changes.  We override that method here to 
	 * update the old/new textfields.
	 */
	ListSelectionModel selectionModel = new SingleSelectionModel() {
	    public void updateSingleSelection(int oldIndex, int newIndex) {
		ListModel m = list.getModel();
		Object oldValue = (oldIndex == -1) ? "<none>" : m.getElementAt(oldIndex);
		Object newValue = (newIndex == -1) ? "<none>" : m.getElementAt(newIndex);
		oldTextField.setText("Old value was " + oldValue);
		newTextField.setText("New value is " + newValue);
	    }
	};
	list.setSelectionModel(selectionModel);

	Box panel = Box.createVerticalBox();
	panel.add(new JScrollPane(list));
	panel.add(oldTextField);
	panel.add(newTextField);

	JFrame frame = new JFrame("SingleSelection Demo");

	WindowListener l = new WindowAdapter() {
	    public void windowClosing(WindowEvent e) { System.exit(0); }
	};
	frame.addWindowListener(l);

	frame.getContentPane().add(panel);
	frame.pack();
	frame.setVisible(true);
    }
}
