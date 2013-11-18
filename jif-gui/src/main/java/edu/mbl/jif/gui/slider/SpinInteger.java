package edu.mbl.jif.gui.slider;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

// SpinInteger
//
public class SpinInteger extends JPanel
    implements Observer {

  String name;
  int init;
  int low;
  int high;
  int incr;
  String fmt;
  int columns;

  private JLabel label;
  private JSpinner spinner;
  private SpinnerNumberModel model;
//  private JSpinner.NumberEditor editor;
  private ChangeListener listener;
  int fontSize = 12;
  Font smallFont;


  public SpinInteger() {
    // super();
    try {
      jbInit();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // Integer Numeric Spinner
  public SpinInteger(String name, int init, int low, int high, int incr,
      String fmt, int columns) {
    this();
    this.name = name;
    this.init = init;
    this.low = low;
    this.high = high;
    this.incr = incr;
    this.fmt = fmt;
    this.columns = columns;
    model = new SpinnerNumberModel(init, low, high, incr);
    try {
      init();
    } catch (Exception ex) {}
  }

  // Integer Numeric Spinner with shared model
  public SpinInteger(String name, SpinnerNumberModel model,
      String fmt, int columns, int FontSize) {
    this();
    this.name = name;
    this.fmt = fmt;
    this.columns = columns;
    this.model = model;
    fontSize = FontSize;
    try {
      init();
    } catch (Exception ex) {}
  }

  void jbInit() throws Exception {
  }

  void init() throws Exception {
    label = new JLabel(name);
    spinner = new JSpinner(model);
    Font f = new java.awt.Font("Dialog", 0, fontSize);
    //editor.getTextField().setFont(f);
    spinner.setFont(f);
    label.setFont(f);
    spinner.setEditor(new JSpinner.NumberEditor(spinner, fmt));
    //format(columns);
    setInputFieldSize(columns);
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    //setBorder(BorderFactory.createEtchedBorder());
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    add(Box.createHorizontalGlue());
    add(label);
    add(Box.createRigidArea(new Dimension(2, 0)));
    add(spinner);
    spinner.setMaximumSize(new Dimension(999, 24));
    listener =
        new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        // SpinnerModel source = (SpinnerModel) e.getSource();
        // JSpinner     spinner = (JSpinner) e.getSource();
        // Object       value = spinner.getValue();
        // System.out.println("The value is: " + source.getValue());
        processChange(model.getNumber().intValue());
      }
    };
    model.addChangeListener(listener);
  }

  public void setTheFont(Font font) {
    label.setFont(font);
    spinner.setFont(font);
  }

  void format(int columns) {
    setInputFieldSize(columns);
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    //setBorder(BorderFactory.createEtchedBorder());
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    add(Box.createHorizontalGlue());
    add(label);
    add(Box.createRigidArea(new Dimension(5, 0)));
    add(spinner);
    spinner.setMaximumSize(new Dimension(999, 24));
  }

  /////////////////////////////////////////////////////////////////////////
  void processChange(int i) {
    //System.out.println("Integer: " + i);
    // notifyObservers();
  }

  public void update(Observable o, Object arg) {
  //((Double)arg).doubleValue());
  }

  /////////////////////////////////////////////////////////////////////////
  public void setLimits(int min, int max, int incr) {
    model.setMinimum(new Integer(min));
    model.setMaximum(new Integer(max));
    model.setStepSize(new Integer(incr));
  }

  public void setValue(int i) {
    model.setValue(new Integer(i));
  }

  void forceTo(int i) {
    // get them so they can be restored...
    model.removeChangeListener(listener);
    model.setValue(new Integer(i));
    model.addChangeListener(listener);
  }

  public void setEnabled(boolean t) {
    spinner.setEnabled(t);
    //editor.setEnabled(t);
    label.setEnabled(t);
    this.updateUI();
  }

  //  public class SpinnerListener implements ChangeListener {
  //    public void stateChanged(ChangeEvent evt) {
  //      JSpinner spinner = (JSpinner) evt.getSource();
  //      Object   value = spinner.getValue();
  //      spinner.getValue().toString();
  //      processChange(model.getNumber());
  //    }
  //  }
  ////////////////////////////////////////////////////////////////////////////
  public void setInputFieldSize(int columns) {
    JFormattedTextField tf =
        ( (JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    int top = 0;
    int left = 2;
    int bottom = 0;
    int right = 2;
    Insets insets = new Insets(top, left, bottom, right);
    tf.setMargin(insets);
    tf.setColumns(columns);
  }

  public void enableKeyInput(boolean t) {
    JFormattedTextField tf =
        ( (JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    tf.setEditable(t);
  }
}
