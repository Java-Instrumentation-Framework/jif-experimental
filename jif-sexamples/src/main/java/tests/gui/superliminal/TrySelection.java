/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.gui.superliminal;

import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */
public class TrySelection {

	public TrySelection() {
	}

	public void test() {
		FocusManager fm = new FocusManager();
		Selection s = new Selection();
		Selection.Listener l = new Selection.Listener() {
			public void selectionChanged(Object newSelection) {
				System.out.println("SelectionChanged: " + newSelection);
			}
		};
		s.addSelectionListener(l);
		
		JFrame f1 = new JFrame("One");
		f1.setVisible(true);
		f1.addFocusListener(fm);

		JFrame f2 = new JFrame("Two");
		f2.setVisible(true);
		f2.addFocusListener(fm);
	}

	class FocusManager implements FocusListener {

		public FocusManager() {
		}

		public void focusGained(FocusEvent e) {
			System.out.println("Gained: " + e.getSource());
		}

		public void focusLost(FocusEvent e) {
			System.out.println("Lost: " + e.getSource());
		}

	}

	public static void main(String[] args) {
		(new TrySelection()).test();
	}

}
