package edu.mbl.jif.gui.slider;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;


// SpinDouble
public class SpinDouble extends JPanel
        implements Observer {
   JLabel label;
   JSpinner spinner;
   SpinnerNumberModel model;
   JSpinner.NumberEditor editor;
   ChangeListener listener;
   Font smallFont = new java.awt.Font("Dialog", 0, 10);

   public SpinDouble() {
     // super();
     try {
       jbInit();
     } catch (Exception ex) {
       ex.printStackTrace();
     }
   }
   void jbInit() throws Exception {
   }

   public void setEnabled(boolean t) {
     spinner.setEnabled(t);
     editor.setEnabled(t);
     label.setEnabled(t);
   }

   // Double Numeric Spinner
   public SpinDouble(String name, double init, double low, double high,
      double incr, String fmt, int columns) {
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
                  processChange(model.getNumber().doubleValue());
               }
            };
      model.addChangeListener(listener);
      format(columns);
   }

   // Double Numeric Spinner
   public SpinDouble(String name, SpinnerNumberModel _model, String fmt,
      int columns) {
      super();
      label = new JLabel(name);
      model = _model;
      spinner = new JSpinner(model);
      editor = new JSpinner.NumberEditor(spinner, fmt);
      spinner.setEditor(editor);
      //    listener = new ChangeListener() {
      //          public void stateChanged(ChangeEvent e) {
      //            processChange(model.getNumber().doubleValue());
      //          }
      //        };
      //    model.addChangeListener(listener);
      format(columns);
   }

 //   public void setFont(Font font) {
//     label.setFont(font);
//     spinner.setFont(font);
//   }
   public void setSmall() {
     label.setFont(smallFont);
     spinner.setFont(smallFont);
     spinner.setMaximumSize(new Dimension(999, 16));
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
      spinner.setMaximumSize(new Dimension(999, 25));
   }

   /////////////////////////////////////////////////////////////////////////
   void processChange(int i) {
      //System.out.println("Integer: " + i);
      // notifyObservers();
   }

   void processChange(double d) {
      //System.out.println("Double: " + d);
   }

   public void update(Observable o, Object arg) {
      //((Double)arg).doubleValue());
   }

   /////////////////////////////////////////////////////////////////////////
   void setLimits() {}

   void setTo(double d) {
      model.setValue(new Double(d));
   }

   void forceTo(double d) {
      model.removeChangeListener(listener);
      model.setValue(new Double(d));
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
         ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
      tf.setEditable(t);
   }

}
