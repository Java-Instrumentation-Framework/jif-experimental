/*
 * ActionKeyLister.java
 *
 * Created on June 3, 2006, 5:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.action;


import edu.mbl.jif.utils.StaticSwingUtils;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
//import quicktime.std.comp.Component;

/**
 *
 * @author GBH
 */
public class ActionKeyLister {
   
   /** Creates a new instance of ActionKeyLister */
   public ActionKeyLister() {
   }
   // e860. Listing the Key Bindings in a Component
// This example demonstrates how to list all the key bindings in a component. 
// Text //components have an additional set of key bindings called a keymap. 
// See e1005 Listing //the Key Bindings in a JTextComponent Keymap for an example on 
// how to list those key bindings.
   public static void listKeyBindings(JComponent component) {
      
   
    // List keystrokes in the WHEN_FOCUSED input map of the component
    InputMap map = component.getInputMap(JComponent.WHEN_FOCUSED);
    listKeys(map, map.keys());
    // List keystrokes in the component and in all parent input maps
    listKeys(map, map.allKeys());
    
    // List keystrokes in the WHEN_ANCESTOR_OF_FOCUSED_COMPONENT input map of the component
    map = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    listKeys(map, map.keys());
    // List keystrokes in all related input maps
    listKeys(map, map.allKeys());
    
    // List keystrokes in the WHEN_IN_FOCUSED_WINDOW input map of the component
    map = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    listKeys(map, map.keys());
    // List keystrokes in all related input maps
    listKeys(map, map.allKeys());
    

   }
    public static void listKeys(InputMap map, KeyStroke[] keys) {
        if (keys == null) {
            return;
        }
        for (int i=0; i<keys.length; i++) {
            // This method is defined in e859 Converting a KeyStroke to a String
            String keystrokeStr =  StaticSwingUtils.keyStroke2String(keys[i]);
            System.out.println(keystrokeStr);
            // Get the action name bound to this keystroke
            while (map.get(keys[i]) == null) {
                map = map.getParent();
            }
            if (map.get(keys[i]) instanceof String) {
                String actionName = (String)map.get(keys[i]);
                System.out.println("   "+actionName);
            } else {
                Action action = (Action)map.get(keys[i]);
            }
        }
    }
    
    
  // e856. Listing the Actions in a Component
  // This example demonstrates how to list all the actions in a component.
    
   public static void listActions (JComponent component) {

      ActionMap map = component.getActionMap();
    
    // List actions in the component
    list(map, map.keys());
    
    // List actions in the component and in all parent action maps
    list(map, map.allKeys());
    
   
   }
 public static void list(ActionMap map, Object[] actionKeys) {
        if (actionKeys == null) {
            return;
        }
        for (int i=0; i<actionKeys.length; i++) {
            // Get the action bound to this action key
            while (map.get(actionKeys[i]) == null) {
                map = map.getParent();
            }
            Action action = (Action)map.get(actionKeys[i]);
        }
    }
 
 public static void main(String[] args) {
     JPanel f = new JPanel();
     //f.setVisible(true);

    listKeyBindings(f);
    listActions(f);
          
 }       
}
