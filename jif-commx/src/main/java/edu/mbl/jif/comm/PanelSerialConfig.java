package edu.mbl.jif.comm;

import java.awt.event.ItemListener;
import java.awt.Choice;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;
import java.awt.Label;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import edu.mbl.jif.utils.DialogBox;

/**
 * <p>Title: Serial Comm Configuration Parameters Panel</p>
 *
 *  This is the one currently used as of Aug '08, GBH
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PanelSerialConfig
        extends JPanel implements ItemListener {

  private SerialPortConnection connection;
  private SerialParameters parameters;
  private Label portNameLabel;
  private Choice portChoice;
  private Label baudLabel;
  private Choice baudChoice;
  private Label flowControlInLabel;
  private Choice flowChoiceIn;
  private Label flowControlOutLabel;
  private Choice flowChoiceOut;
  private Label databitsLabel;
  private Choice databitsChoice;
  private Label stopbitsLabel;
  private Choice stopbitsChoice;
  private Label parityLabel;
  private Choice parityChoice;
  /**
  Creates and initilizes the configuration panel. The initial settings
  are from the parameters object.
   */
  BorderLayout borderLayout1 = new BorderLayout();


  public PanelSerialConfig() {
    try {
      jbInit();
    } catch (Exception exception) {
      exception.printStackTrace();
    }

  }

  public PanelSerialConfig(SerialPortConnection connection) {
    this.connection = connection;
    try {
      jbInit();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    try {
      this.parameters = connection.getParameters();
    } catch (SerialConnectionException ex) {
      ex.printStackTrace();
    }

  }

  public void setConnection(SerialPortConnection connection) {
    this.connection = connection;
    try {
      this.parameters = connection.getParameters();
    } catch (SerialConnectionException ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    //this.setLayout(borderLayout1);
    setLayout(new GridLayout(3, 4));
    parameters = new SerialParameters();

//    portNameLabel = new Label("Port Name:", Label.LEFT);
//    add(portNameLabel);
//    portChoice = new Choice();
//    portChoice.addItemListener(this);
//    add(portChoice);
//    listPortChoices();
//    portChoice.select(parameters.getPortName());

    baudLabel = new Label("Baud Rate:", Label.LEFT);
    add(baudLabel);
    baudChoice = new Choice();
    baudChoice.addItem("300");
    baudChoice.addItem("2400");
    baudChoice.addItem("9600");
    baudChoice.addItem("14400");
    baudChoice.addItem("28800");
    baudChoice.addItem("38400");
    baudChoice.addItem("57600");
    baudChoice.addItem("152000");
    baudChoice.addItem("230400");
    baudChoice.addItem("460800");
    baudChoice.addItem("921600");
    baudChoice.select(Integer.toString(parameters.getBaudRate()));
    baudChoice.addItemListener(this);
    add(baudChoice);

    databitsLabel = new Label("Data Bits:", Label.LEFT);
    add(databitsLabel);
    databitsChoice = new Choice();
    databitsChoice.addItem("5");
    databitsChoice.addItem("6");
    databitsChoice.addItem("7");
    databitsChoice.addItem("8");
    databitsChoice.select(parameters.getDatabitsString());
    databitsChoice.addItemListener(this);
    add(databitsChoice);

    stopbitsLabel = new Label("Stop Bits:", Label.LEFT);
    add(stopbitsLabel);
    stopbitsChoice = new Choice();
    stopbitsChoice.addItem("1");
    stopbitsChoice.addItem("1.5");
    stopbitsChoice.addItem("2");
    stopbitsChoice.select(parameters.getStopbitsString());
    stopbitsChoice.addItemListener(this);
    add(stopbitsChoice);

    parityLabel = new Label("Parity:", Label.LEFT);
    add(parityLabel);
    parityChoice = new Choice();
    parityChoice.addItem("None");
    parityChoice.addItem("Even");
    parityChoice.addItem("Odd");
    parityChoice.select("None");
    parityChoice.select(parameters.getParityString());
    parityChoice.addItemListener(this);
    add(parityChoice);

    flowControlInLabel = new Label("Flow Ctrl In:", Label.LEFT);
    add(flowControlInLabel);
    flowChoiceIn = new Choice();
    flowChoiceIn.addItem("None");
    flowChoiceIn.addItem("Xon/Xoff In");
    flowChoiceIn.addItem("RTS/CTS In");
    flowChoiceIn.select(parameters.getFlowControlInString());
    flowChoiceIn.addItemListener(this);
    add(flowChoiceIn);

    flowControlOutLabel = new Label("Flow Ctrl Out:", Label.LEFT);
    add(flowControlOutLabel);
    flowChoiceOut = new Choice();
    flowChoiceOut.addItem("None");
    flowChoiceOut.addItem("Xon/Xoff Out");
    flowChoiceOut.addItem("RTS/CTS Out");
    flowChoiceOut.select(parameters.getFlowControlOutString());
    flowChoiceOut.addItemListener(this);
    add(flowChoiceOut);

    //    this.setConfigurationPanel();
  }

  /**
  Sets the configuration panel to the settings in the parameters object.
   */
  public void setConfigurationPanel() {
    portChoice.select(parameters.getPortName());
    baudChoice.select(parameters.getBaudRateString());
    flowChoiceIn.select(parameters.getFlowControlInString());
    flowChoiceOut.select(parameters.getFlowControlOutString());
    databitsChoice.select(parameters.getDatabitsString());
    stopbitsChoice.select(parameters.getStopbitsString());
    parityChoice.select(parameters.getParityString());
  }

  /**
  Sets the parameters object to the settings in the configuration panel.
   */
  public void setParameters() {
    parameters.setPortName(portChoice.getSelectedItem());
    parameters.setBaudRate(baudChoice.getSelectedItem());
    parameters.setFlowControlIn(flowChoiceIn.getSelectedItem());
    parameters.setFlowControlOut(flowChoiceOut.getSelectedItem());
    parameters.setDatabits(databitsChoice.getSelectedItem());
    parameters.setStopbits(stopbitsChoice.getSelectedItem());
    parameters.setParity(parityChoice.getSelectedItem());
  }

  /**
  Sets the elements for the portChoice from the ports available on the
  system. Uses an emuneration of comm ports returned by
  CommPortIdentifier.getPortIdentifiers(), then sets the current
  choice to a mathing element in the parameters object.
   */
  void listPortChoices() {
    CommPortIdentifier portId;
    Enumeration en = CommPortIdentifier.getPortIdentifiers();
    // iterate through the ports.
    while (en.hasMoreElements()) {
      portId = (CommPortIdentifier) en.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        portChoice.addItem(portId.getName());
      }
    }
    portChoice.select(parameters.getPortName());
  }
  /**
  Event handler for changes in the current selection of the Choices.
  If a port is open the port can not be changed.
  If the choice is unsupported on the platform then the user will
  be notified and the settings will revert to their pre-selection
  state.
   */
  public void itemStateChanged(ItemEvent e) {
    // Check if port is open.
    if (connection.isOpen()) {
      // If port is open do not allow port to change.
      if (e.getItemSelectable() == portChoice) {
        DialogBox.boxError(this, "Port Open!",
                "Port can not be changed\n" + "while a port is open.");
        // Return configurationPanel to pre-choice settings.
        setConfigurationPanel();
        return;
      }
      // Set the parameters from the choice panel.
      setParameters();
      try {
        // Attempt to change the settings on an open port.
        connection.setConnectionParameters();
      } catch (SerialConnectionException ex) {
        // If setting can not be changed, alert user, return to
        // pre-choice settings.
        DialogBox.boxError(this, "Unsupported Configuration!",
                "Configuration Parameter unsupported, select new value.\n" + "Returning to previous configuration.");
        setConfigurationPanel();
      }
    } else {
      // Since port is not open, just set the parameter object.
      setParameters();

    }
  }
}
