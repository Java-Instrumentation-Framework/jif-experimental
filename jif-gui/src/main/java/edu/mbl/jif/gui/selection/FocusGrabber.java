package edu.mbl.jif.gui.selection;

import javax.swing.*;


public class FocusGrabber
      implements Runnable
{
   private JComponent component;

   public FocusGrabber (JComponent component) {
      this.component = component;
   }


   public void run () {
      //  component.getTopLevelAncestor();
      // ((component.getParent()).getParent()).getParent().setVisible(true);
      // (component.getParent()).getParent().requestFocus();
      // component.getParent().requestFocus();
      component.grabFocus();
   }
}
