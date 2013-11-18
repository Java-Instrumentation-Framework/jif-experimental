/*
 * FileOpenAction.java
 *
 * Created on July 3, 2006, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.action;

import edu.mbl.jif.gui.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;

// uses BaseAction
import tests.gui.action.BaseAction;

public class FileOpenAction extends BaseAction {
   private MyAppActions owner;

   public FileOpenAction(MyAppActions owner) {
      // Initialize our look and behavior
      super("Open", null, //IconUtils.getGeneralIcon( "Open", 16 ),
         "Open a File", new Integer(KeyEvent.VK_O),
         KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

      // Save a reference to our owner
      this.owner = owner;
   }

   /**
    * Invoked when an action occurs.
    */
   public void actionPerformed(ActionEvent event) {
      JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
      if (fc.showOpenDialog(this.owner) == JFileChooser.APPROVE_OPTION) {
         try {
            // Open the file
            File f = fc.getSelectedFile();

            // Do something...

            // Notify our owner
            this.owner.someCallback(f);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
