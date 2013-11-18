package edu.mbl.jif.camera;

import edu.mbl.jif.camacq.CamAcq;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Dimension;
import edu.mbl.jif.utils.prefs.Prefs;


public class SpinnerExposure
      extends JSpinner
{

   public SpinnerNumberModel model_Expos;
   private JSpinner.NumberEditor edit_Expos;
   ChangeListener listener_Expos;
   CamAcq cam;
   public SpinnerExposure (final CamAcq cam) {
      super();
      this.cam = cam;
      setPreferredSize(new Dimension(55, 27));
      setMaximumSize(new Dimension(55, 27));
      setMinimumSize(new Dimension(55, 27));
      setToolTipText("Exposure Setting");
      int initExp = Prefs.usr.getInt("camera.exposure", 10000);
      
      initExp = (initExp > 1000000) ? 1000000 : initExp;
      if (initExp <= 0) {
         initExp = 10000;
      }
      model_Expos = new SpinnerNumberModel((double) initExp / 1000, 0.050, 60000.0, 5.0);
      this.setModel(model_Expos);
      edit_Expos = new JSpinner.NumberEditor(this, "0.00");
      setEditor(edit_Expos);
      setExpSpinStep((float) initExp / 1000);
      setBorder(BorderFactory.createEtchedBorder());
      listener_Expos = new ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            //SpinnerModel source = (SpinnerModel) e.getSource();
            //String inStr = String.valueOf(source.getValue());
            String inStr = String.valueOf(model_Expos.getValue());
            float value = 0;
            try {
               value = Float.parseFloat(inStr);
            }
            catch (NumberFormatException nfe) {}
            //System.out.println("In PanelCam.SpinExpos: value = " + value);
            try { // Change the setting
               cam.setExposureStream(value);
               cam.setExposureAcq(value);
               //Camera.setExposureOnly((long) (value * 1000));
            }
            catch (Exception ex) {
               System.out.println("Error in : " + ex);
            }
            setExpSpinStep(value);
            // update the exposure spinner in the Camera Display Frame
            if (Camera.display != null) {
               //CameraInterface.display.ctrlPanel.updateExposureSpinner();
            }
            //CameraInterface.setDisplayOn();
            //CameraInterface.spinExpos.updateUI();

         }
      };
      model_Expos.addChangeListener(listener_Expos);
   }


   public void forceTo (double d) {
      model_Expos.removeChangeListener(listener_Expos);
      model_Expos.setValue(new Double(d));
      model_Expos.addChangeListener(listener_Expos);
   }


   public void setExpSpinStep (float value) {
// update the step
      String decFormat = "0";
      if ((value >= 0) && (value < 0.5)) {
         model_Expos.setStepSize(new Double(0.01));
         decFormat = "0.00";
      }
      if ((value >= 0.5) && (value < 2)) {
         model_Expos.setStepSize(new Double(0.1));
         decFormat = "0.00";
      }
      if ((value >= 2) && (value < 10)) {
         model_Expos.setStepSize(new Double(1));
         decFormat = "0.0";
      }
      if ((value >= 10) && (value < 20)) {
         model_Expos.setStepSize(new Double(2));
         decFormat = "0.0";
      }
      if ((value >= 20) && (value < 100)) {
         model_Expos.setStepSize(new Double(5));
         decFormat = "0.";
      }
      if ((value >= 100) && (value < 1000)) {
         model_Expos.setStepSize(new Double(10));
         decFormat = "0.";
      }
      if ((value >= 1000)) {
         model_Expos.setStepSize(new Double(100));
         decFormat = "0.";
      }
      // update the format
      edit_Expos.getFormat().applyPattern(decFormat);
//    setValue(new Double((double) value));
   }

}
