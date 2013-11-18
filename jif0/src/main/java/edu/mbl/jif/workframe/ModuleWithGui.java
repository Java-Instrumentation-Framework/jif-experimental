/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.workframe;

import javax.swing.Icon;

/**
 *
 * @author GBH
 */
public interface ModuleWithGui  extends IModule{
   
   Icon getIcon();

   java.awt.Color getColor();

   java.awt.Container getPanel();

   java.awt.Container getPreferencesPanel();

   // For grouping...
   String getCatagory();
      
   // menuItems
   //   menuPath
   
   // ToolbarItems

}
