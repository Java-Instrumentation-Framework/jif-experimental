/*
 * Project: PropertyListenerDemo
 * Package: domain
 * File: Controller.java
 *
 * Created on Jan 27, 2006
 */
package tests.binding.domain;

import tests.binding.domain.Model;
import tests.binding.domain.GraphicalFrame;
import java.beans.PropertyChangeListener;
import java.util.Locale;


/**
 * This class represents a conflation of an initial domain object and a controller
 * in the design model suggested by Craig Larman in "Applying UML and Patterns".
 * In a real-world implementation, it might be divided into two or more classes.
 * 
 * For example, in a sales system, the initial domain object might be named 
 * <code>Store</code>, and a <code>Register</code> object might be the controller.
 * 
 * This implementation illustrates a simple use of Java's Delegation Event Model,
 * in which the user interface (UI) object, a <code>GraphicalFrame</code>, implements
 * the <code>PropertyChangeListener</code> interface and a <code>Model</code> object
 * is a property change event source.  Note that the controller's constructor creates
 * the model object and then adds the UI object to the model object's list of
 * property change listeners.
 * 
 * @author Jonathan Mohr
 * @version 2006.01.29
 */
public class Controller
{
	// For ease of internationalization.  In a data-driven design,
	// the correct locale would be read from a configuration file.
	public static final Locale LOCALE = Locale.CANADA;
	
	public Controller()
	{
		// In this very simple case, all of the following could be done
		// within the 'main' method, but then we wouldn't have an
		// "initial domain object", just an initializing class.
 
		// Create the interface to the presentation layer (a.k.a. View or UI),
		// specifying which concrete class will be used to implement the 
		// property change listener interface. The domain layer should only
		// know about the presentation layer in terms of an interface.
		PropertyChangeListener listener = new GraphicalFrame( this );
		
		model = new Model();
		model.addPropertyChangeListener( listener );		
	}

	/**
	 * This method is called by the user interface in order to pass to the controller
	 * an input value entered by the user.  In this simple example, the controller
	 * simply passes it along to the model object, but depending on the design of the
	 * domain layer, the controller might use the input to retrieve information from
	 * the persistence layer that is then passed to the model object. 
	 * @param d the value entered by the user
	 */
	public void enterValue(double d)
	{
		model.acceptEntry( d );
	}

	// The controller communicates with a model object.
	private Model model;

	public static void main(String[] args)
	{
		 // Create the initial domain object
		new Controller();	
	}
}
