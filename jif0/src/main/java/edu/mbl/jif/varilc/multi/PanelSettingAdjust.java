package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.PathVLC;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;


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
public class PanelSettingAdjust
      extends JPanel
{

   JToggleButton jButton1 = new JToggleButton();
   JSpinner[] spin;
   JLabel valueIntensity = new JLabel();
   PathVLC path;
   int setting;
   ButtonGroup group;


   public PanelSettingAdjust () {
      this(new PathVLC(), 0, new ButtonGroup());
   }


   public PanelSettingAdjust (PathVLC path, int setting, ButtonGroup group) {
      this.path = path;
      this.group = group;
      this.setting = setting;
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
      jButton1.setText(String.valueOf(setting));
      jButton1.setPreferredSize(new Dimension(50,12));
      jButton1.addChangeListener(new PanelSettingAdjust_jButton1_changeAdapter(this));
      this.add(jButton1);
      spin = new JSpinner[path.getNumRetarders()];
      path.getNumRetarders();
      double step = 0.01;
      for (int i = 0; i < path.getNumRetarders(); i++) {
         spin[i] = new JSpinner(new SpinnerNumberModel(path.setting[i][setting],
               path.getRetarders()[i].getMin(), path.getRetarders()[i].getMax(), step));
         this.add(spin[i]);
      }
      valueIntensity.setText("000");
      valueIntensity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      valueIntensity.setPreferredSize(new Dimension(42,12));
      this.add(valueIntensity);
   }


   public JToggleButton getSelectButton () {
      return jButton1;
   }



   public void jButton1_stateChanged (ChangeEvent e) {
      final boolean selected = ((AbstractButton) e.getSource()).isSelected();
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            System.out.println(setting + " - " + selected);
         }
      });
   }
}



class PanelSettingAdjust_jButton1_changeAdapter
      implements ChangeListener
{
   private PanelSettingAdjust adaptee;
   PanelSettingAdjust_jButton1_changeAdapter (PanelSettingAdjust adaptee) {
      this.adaptee = adaptee; }


   public void stateChanged (ChangeEvent e) {
      adaptee.jButton1_stateChanged(e); }}
