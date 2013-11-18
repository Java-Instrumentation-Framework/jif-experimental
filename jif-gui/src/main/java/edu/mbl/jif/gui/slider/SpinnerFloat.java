package edu.mbl.jif.gui.slider;

import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerModel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JComponent;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SpinnerFloat
      extends JSpinner
{

   public SpinnerNumberModel modelSpin;
   private JSpinner.NumberEditor editorSpin;

   public SpinnerFloat (double value, double minimum, double maximum, double stepSize) {
      super();
      modelSpin = new SpinnerNumberModel(value, minimum, maximum, stepSize);
      super.setModel(modelSpin);
      editorSpin = new JSpinner.NumberEditor(this, "0.00");
      setEditor(editorSpin);
      setExpSpinStep(1);
      setBounds(new Rectangle(128, 13, 65, 24));
      setMinimumSize(new Dimension(31, 24));
      setPreferredSize(new Dimension(31, 24));
      ChangeListener listener = new ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            SpinnerModel source = (SpinnerModel) e.getSource();
            String inStr = String.valueOf(source.getValue());
            float value = 0;
            try {
               value = Float.parseFloat(inStr);
            }
            catch (NumberFormatException nfe) {}
            setExpSpinStep(value);
         }
      };
      modelSpin.addChangeListener(listener);
   }


   //

   void setExpSpinStep (float value) {
      // update the step
      String decFormat = "0";
      if ((value >= 0) && (value < 0.5)) {
         modelSpin.setStepSize(new Double(0.01));
         decFormat = "0.00";
      }
      if ((value >= 0.5) && (value < 2)) {
         modelSpin.setStepSize(new Double(0.1));
         decFormat = "0.00";
      }
      if ((value >= 2) && (value < 10)) {
         modelSpin.setStepSize(new Double(1));
         decFormat = "0.0";
      }
      if ((value >= 10) && (value < 20)) {
         modelSpin.setStepSize(new Double(2));
         decFormat = "0.0";
      }
      if ((value >= 20) && (value < 100)) {
         modelSpin.setStepSize(new Double(5));
         decFormat = "0.";
      }
      if ((value >= 100) && (value < 1000)) {
         modelSpin.setStepSize(new Double(10));
         decFormat = "0.";
      }
      if ((value >= 1000)) {
         modelSpin.setStepSize(new Double(100));
         decFormat = "0.";
      }
      // update the format
      ((JSpinner.NumberEditor)this.getEditor()).getFormat().applyPattern(decFormat);
      //spin_Expos.setValue(new Double((double) value));
   }


   public static void main (String[] args) {
      SpinnerFloat spinnerfloat = new SpinnerFloat(10, 0.050, 60000.0, 5.0);
      JFrame f = new JFrame();
      f.add(spinnerfloat, BorderLayout.CENTER);
      f.pack();
      f.setVisible(true);

   }

}
