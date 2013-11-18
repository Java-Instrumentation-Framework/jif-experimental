package edu.mbl.jif.camera;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

// SpinInteger
//
public class SpinInteger extends JPanel
        implements Observer {
   JLabel                label;
   JSpinner              spinner;
   SpinnerNumberModel    model;
   JSpinner.NumberEditor editor;
   ChangeListener        listener;

   public SpinInteger() {
       enableEvents(AWTEvent.WINDOW_EVENT_MASK);
       try {
           jbInit();
       }
       catch(Exception e) {
           e.printStackTrace();
       }
   }
   //Component initialization
    private void jbInit() throws Exception  {

    }

   public void setEnabled(boolean t) {
     editor.setEnabled(t);
   }

   // Integer Numeric Spinner
   //
   public SpinInteger(String name, int init, int low, int high, int incr,
      String fmt, int columns) {
      super();
      label = new JLabel(name);
      model = new SpinnerNumberModel(init, low, high, incr);
      spinner = new JSpinner(model);
      editor = new JSpinner.NumberEditor(spinner, fmt);
      spinner.setEditor(editor);
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
      format(columns);
   }

   // Integer Numeric Spinner with shared model
   //
   public SpinInteger(String name, SpinnerNumberModel _model, String fmt,
      int columns) {
      super();
      label = new JLabel(name);
      model = _model;
      spinner = new JSpinner(model);
      editor = new JSpinner.NumberEditor(spinner, fmt);
      spinner.setEditor(editor);
      //    listener = new ChangeListener() {
      //          public void stateChanged(ChangeEvent e) {
      //            // SpinnerModel source = (SpinnerModel) e.getSource();
      //            // JSpinner     spinner = (JSpinner) e.getSource();
      //            // Object       value = spinner.getValue();
      //            // System.out.println("The value is: " + source.getValue());
      //            processChange(model.getNumber().intValue());
      //          }
      //        };
      //    model.addChangeListener(listener);
      format(columns);
   }

   void format(int columns) {
      setInputFieldSize(columns);
      Font font = new java.awt.Font("Dialog", 0, 12);
      label.setFont(font);
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
   void setLimits() {}

   void setTo(int i) {
      model.setValue(new Integer(i));
   }

   void forceTo(int i) {
      // get them so they can be restored...
      model.removeChangeListener(listener);
      model.setValue(new Integer(i));
      model.addChangeListener(listener);
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
         ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
      int                 top = 0;
      int                 left = 2;
      int                 bottom = 0;
      int                 right = 2;
      Insets              insets = new Insets(top, left, bottom, right);
      tf.setMargin(insets);
      tf.setColumns(columns);
   }

   public void enableKeyInput(boolean t) {
      JFormattedTextField tf =
         ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
      tf.setEditable(t);
   }
}
