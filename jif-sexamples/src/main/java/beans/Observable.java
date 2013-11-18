package beans;

/**
 *
 * @author GBH
 */
// From package org.izvin.client.desktop.ui.util;

import java.beans.PropertyChangeListener;

public interface Observable {
    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public PropertyChangeListener[] getPropertyChangeListeners();

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    public boolean hasListeners(String propertyName);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener); 
}