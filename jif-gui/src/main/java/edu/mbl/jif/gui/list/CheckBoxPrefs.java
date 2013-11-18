package edu.mbl.jif.gui.list;

import edu.mbl.jif.utils.prefs.Prefs;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;


public class CheckBoxPrefs
      extends JCheckBox
{

   private String label = null;
   private String propsKey = null;
   boolean defaultSelected = false;

   public CheckBoxPrefs () {
      this("CheckBoxPrefs", "CheckBoxPrefs", true); }


   public CheckBoxPrefs (String label, String propsKey, boolean defaultSelected) {
      super(label);
      this.label = label;
      this.defaultSelected = defaultSelected;
      this.propsKey = propsKey;
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      super.setSelected(Prefs.usr.getBoolean(propsKey, defaultSelected));
      super.setFont(new java.awt.Font("Dialog", 0, 10));
      super.setOpaque(false);
      super.setText(label);
      addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            checkBox_actionPerformed(e);
         }
      });
   }


// +++ NodeListener

   public void checkBox_actionPerformed (ActionEvent e) {
      Prefs.usr.putBoolean(propsKey, this.isSelected());
   }


   public void setValue (boolean b) {
      super.setSelected(b);
      Prefs.usr.putBoolean(propsKey, b);
   }

}
