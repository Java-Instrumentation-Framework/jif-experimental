/*
 * Project: PropertyListenerDemo
 * Package: domain
 * File: Model.java
 *
 * Created on Jan 29, 2006
 */
package tests.binding.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * This class represents the "model" in a Model-View-Controller (MVC) design.
 * In an actual application, it would typically be implemented by a number of
 * classes, instances (objects) of which act together to maintain the model.
 * 
 * For example, in a sales system, this class might be named <code>Sale</code>,
 * and would create <code>SalesLineItem</code>s and retrieve <code>ItemDescription</code>s
 * from a <code>ProductCatalog</code>.
 *
 * This implementation illustrates the use of an instance of the PropertyChangeSupport class
 * as a member field, to which the operations of a property change event source in Java's
 * Delegation Event Model are delegated (e.g., <code>addPropertyChangeListener</code> and
 * <code>firePropertyChange</code>).
 * 
 * Reference:  Cay S. Hortstmann and Gary Cornell, Core Java 2, Volume II - Advanced Features
 *             (Sun Microsystems Press/Prentice-Hall PTR, 2000), p. 629-630. 
 * 
 * @author Jonathan Mohr
 * @version 2006.01.29
 */
public class Model
{
	public Model()
	{
		maxValue = Double.MIN_VALUE;
		minValue = Double.MAX_VALUE;
		values = new ArrayList<Double>();
		changeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		// Delegate adding a property change listener to the PropertyChangeSupport instance.
		changeSupport.addPropertyChangeListener( listener );
	}
	
	public void removePropertyChangeListener( PropertyChangeListener listener )
	{
		// Delegate removing a property change listener to the PropertyChangeSupport instance.
		changeSupport.removePropertyChangeListener( listener );
	}
	
	public void acceptEntry( double newValue )
	{
		// Just for illustration purposes, let's say that the model keeps track of
		// all the values entered by the user, as well as the maximum and the minimum
		// values of the sequence of values entered.
		if ( newValue > maxValue )
		{
			// Send a property change event to all listeners, delegating the broadcast
			// to the member PropertyChangeSupport instance.
			changeSupport.firePropertyChange("model.maxValue", maxValue, newValue );
			maxValue = newValue;
		}
		if ( newValue < minValue )
		{
			changeSupport.firePropertyChange("model.minValue", minValue, newValue );
			minValue = newValue;
		}
		
		// The list of values entered is a bound indexed property.
		int i = values.size();
		values.add( newValue );		// 'newValue' is converted to Double by autoboxing.
		changeSupport.fireIndexedPropertyChange("model.values", i, null, values ); 
		
	}
	
	// Some values from the domain model.
	// In the terminology of the Delegation Event Model, these are "bound properties",
	// which means that a listener will be sent a property change event whenever the
	// value of either of these properties changes.
	private double maxValue;
	private double minValue;
	
	// This is a "bound indexed property".
	private ArrayList<Double> values;

	// Delegate property change event source operations to this object.
	private PropertyChangeSupport changeSupport;
}
