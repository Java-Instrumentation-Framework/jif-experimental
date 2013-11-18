/*
 * Project: PropertyListenerDemo
 * Package: ui
 * File: GraphicalFrame.java
 *
 * Created on Jan 27, 2006
 */
package tests.binding.domain;

//import static Controller.LOCALE;

import tests.binding.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class GraphicalFrame extends JFrame implements PropertyChangeListener
{
	private static final long serialVersionUID = 1706661732458852359L;
	
	// Use a number formatter to simplify and internationalize number formatting.
	public static final NumberFormat FORMATTER = NumberFormat.getInstance(Controller.LOCALE);
	
	// Dimensions of the frame
	private static final int FRAME_WIDTH = 320;
	private static final int FRAME_HEIGHT = 300;

	// Dimensions of the text area
	private static final int ROWS = 10;
	private static final int COLUMNS = 24;

	public GraphicalFrame( Controller ctrl )
	{
		// Use helper methods to construct frame components.
		createInputField();
		createButton();
		createInputPanel();
		createTextPane();
		createResultPanel();
		
		setSize( FRAME_WIDTH, FRAME_HEIGHT );
		setTitle("Max/Min Value Tracker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// Keep a reference to the controller for the domain layer.
		controller = ctrl;

		// My preference is to display "whole numbers" as integers. 
		if (FORMATTER instanceof DecimalFormat) 
		     ((DecimalFormat) FORMATTER).setDecimalSeparatorAlwaysShown(false);
	}
	
	private void createInputField()
	{
		inputLabel = new JLabel("Input value: ");
		final int FIELD_WIDTH = 10;
		inputField = new JTextField(FIELD_WIDTH);
		inputField.setText("");
	}
	
	// Create listeners as inner classes.
	// This listener forwards the value entered by the user to the controller object
	// when the user clicks the button.
	public class ClickListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			// check for a null input string
			if ( inputField.getText().length() > 0 )
			{
				try
				{
					controller.enterValue( Double.parseDouble( inputField.getText()));
				}
				catch( NumberFormatException e )
				{
					JOptionPane.showMessageDialog(null, "Input must be numeric.",
							"Input Format Error", JOptionPane.ERROR_MESSAGE );
				}
				inputField.setText("");
				inputField.requestFocus();
			}
		}
	}

	private void createButton()
	{
		button = new JButton("Enter");
		ActionListener listener = new ClickListener();
		button.addActionListener(listener);
	}

	private JTextField createResultField()
	{
		final int FIELD_WIDTH = 10;
		JTextField resultField = new JTextField(FIELD_WIDTH);
		resultField.setText("");
		resultField.setEditable(false);
		return resultField;
	}
	
	private void createInputPanel()
	{
		JPanel inputPanel = new JPanel();
		
		// This uses the default layout - flow layout - within the panel.
		inputPanel.add(inputLabel);
		inputPanel.add(inputField);
		inputPanel.add(button);
		
		// Add this panel as the north panel of a border layout.
		add(inputPanel, BorderLayout.NORTH);
	}
	
	private void createTextPane()
	{
		JPanel textPanel = new JPanel();
		
		textArea = new JTextArea(ROWS, COLUMNS);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane( textArea );
		add(scrollPane);
	}

	private void createResultPanel()
	{
		JPanel resultPanel = new JPanel();
		
		JLabel resultLabel1 = new JLabel("Max: ");
		resultField1 = createResultField();
		JLabel resultLabel2 = new JLabel("Min: ");
		resultField2 = createResultField();

		resultPanel.add(resultLabel1);
		resultPanel.add(resultField1);
		resultPanel.add(resultLabel2);
		resultPanel.add(resultField2);

		add(resultPanel, BorderLayout.SOUTH);
	}

	// This is the implementation of the method defined in the PropertyChangeListener interface. 
	public void propertyChange(PropertyChangeEvent event)
	{
		if ( event.getPropertyName().equals("model.values"))
		{
			int index = ((IndexedPropertyChangeEvent) event).getIndex();
			Double newValue = ((List<Double>) event.getNewValue()).get(index);
			textArea.append(FORMATTER.format( newValue ) + "\n");
		}
		else if ( event.getPropertyName().equals("model.maxValue"))
		{
			Double newValue = (Double) event.getNewValue();
			resultField1.setText(FORMATTER.format( newValue ));
		}
		else if ( event.getPropertyName().equals("model.minValue"))
		{
			Double newValue = (Double) event.getNewValue();
			resultField2.setText(FORMATTER.format( newValue ));
		}
	}

	// Use instance fields for components that need to be referenced after creation.
	private JLabel inputLabel;
	private JTextField inputField;
	private JButton button;
	private JTextArea textArea;
	private JTextField resultField1;
	private JTextField resultField2;
	
	// Keep a reference to the controller object from the domain package (the "model"
	// of the Model-View-Controller architecture).
	private Controller controller;
}
