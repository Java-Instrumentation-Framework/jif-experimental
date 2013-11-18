package tests.gui.list.listSorted;

/** 
 * This example demonstrates how to implement a JList that supports
 * a single selection mode where selecting a component toggles its 
 * selection state.  The default behavior (for all look and feels
 * at the moment) is that clicking on a list entry that's already
 * selected has no effect.  To get the behavior we want we replace
 * the JLists selection model with one that toggles an entries selection 
 * state in this case.
 * 
 * Tested against swing-1.1, JDK1.1.7.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.lang.reflect.Method;


/** 
 * This DefaultSelectionModel subclass enables SINGLE_SELECTION
 * mode and overrides setSelectionInterval so that the first 
 * selection update in a gesture (like mouse press, drag, release)
 * toggles the current selection state.  A "gesture" starts
 * when the first update to the selection model occurs, and
 * the gesture ends when the isAdjusting ListSelectionModel 
 * property is set to false.
 */
class ToggleSelectionModel extends DefaultListSelectionModel
{
    boolean gestureStarted = false;
    
    public void setSelectionInterval(int index0, int index1) {
	if (isSelectedIndex(index0) && !gestureStarted) {
	    super.removeSelectionInterval(index0, index1);
	}
	else {
	    super.setSelectionInterval(index0, index1);
	}
	gestureStarted = true;
    }

    public void setValueIsAdjusting(boolean isAdjusting) {
	if (isAdjusting == false) {
	    gestureStarted = false;
	}
    }
}


/** 
 * Create a JList that displays all of the methods in JComponent.
 * The JLists SelectionModel is replaced with one that toggles
 * the selection; clicking on the same list entry toggles its
 * selection state.
 */
public class ToggleSelection
{
    public static void main(String[] args) throws Exception
    {
	Method[] methods = JComponent.class.getMethods();
	
	JList list = new JList(methods);
	list.setSelectionModel(new ToggleSelectionModel());

	JFrame frame = new JFrame("ToggleSelection Demo");

	WindowListener l = new WindowAdapter() {
	    public void windowClosing(WindowEvent e) { System.exit(0); }
	};
	frame.addWindowListener(l);

	frame.getContentPane().add(new JScrollPane(list));
	frame.pack();
	frame.setVisible(true);
    }
}
