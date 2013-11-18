/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.action;
// @author Santhosh Kumar T - santhosh@in.fiorano.com 

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GloballyContextSensitiveAction implements Action {

	String actionName, shortDescription, longDescription;
	Action delegate;
	JComponent source;
	private PropertyChangeListener focusOwnerListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("focusOwner".equals(evt.getPropertyName())) {
				if (evt.getNewValue() instanceof JComponent) {
					JComponent comp = (JComponent) evt.getNewValue();
					Action action = comp.getActionMap().get(actionName);
					if (action != null) {
						changeDelegate(comp, action);
					}
				}
			}
		}

	};

	public GloballyContextSensitiveAction(String actionName, String shortDescription, String longDescription) {
		this.actionName = actionName;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(focusOwnerListener);
	}

	/*-------------------------------------------------[ Delegate Listener ]---------------------------------------------------*/
	PropertyChangeSupport support = new PropertyChangeSupport(this);
	private PropertyChangeListener delegateListener = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent evt) {
			support.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		}

	};

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	private void changeDelegate(JComponent comp, Action action) {
		if (delegate != null) {
			delegate.removePropertyChangeListener(delegateListener);
		}
		source = comp;
		delegate = action;
		delegate.addPropertyChangeListener(delegateListener);
		boolean enabled = delegate.isEnabled();
		support.firePropertyChange("enabled", !enabled, enabled);
	}

	public boolean isEnabled() {
		return delegate != null && delegate.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		if (delegate != null) {
			delegate.setEnabled(enabled);
		}
	}

	public Object getValue(String key) {
		if (key.equals(Action.NAME)) {
			return actionName;
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			return shortDescription;
		} else if (key.equals(Action.LONG_DESCRIPTION)) {
			return longDescription;
		} else if (delegate != null) {
			return delegate.getValue(key);
		} else {
			return null;
		}
	}

	public void putValue(String key, Object value) {
		if (key.equals(Action.NAME)) {
			actionName = (String) value;
		} else if (key.equals(Action.SHORT_DESCRIPTION)) {
			shortDescription = (String) value;
		} else if (key.equals(Action.LONG_DESCRIPTION)) {
			longDescription = (String) value;
		} else if (delegate != null) {
			delegate.putValue(key, value);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (delegate != null) {
			delegate.actionPerformed(new ActionEvent(source, ae.getID(), ae.getActionCommand(), ae.getWhen(), ae.getModifiers()));
		}
	}

//    public static void main(String[] args) {
//         JList list = new JList(listData);
// JPanel listPanel = new JPanel(new BorderLayout());
// listPanel.add(new JScrollPane(list), BorderLayout.CENTER);
// Action action = new ContextSensitiveAction(list.getActionMap().get("selectAll"), list);
// listPanel.add(new JButton(action), BorderLayout.SOUTH);
//    }
}