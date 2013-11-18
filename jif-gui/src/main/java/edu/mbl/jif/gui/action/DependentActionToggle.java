/*
 * DependentAction.java
 * Created on July 31, 2006, 1:20 PM
 */

package edu.mbl.jif.gui.action;


import javax.swing.Icon;

/**
 *
 * @author GBH
 * Action is allowed or not alloed based on one or more boolean dependencies...
 * Values added with addDependencyAnd() are and'ed.
 * Values added addDependencyOr() are or'ed.
 *
 * DependentAction(
 *   String name, // visible
 *   String command, // ACTION_COMMAND_KEY
 *   Icon icon, 
 *   Object callbackHandlerClass, String method) 
 */
public class DependentActionToggle extends DependentAction {
    
        
    /** Creates a new instance of DependentAction and does registerCallback */
    public DependentActionToggle(String name, String command, Icon icon, Object handler, String method) {
        super(name,command, icon, handler, method);
        super.setStateAction(true);
        registerCallback(handler, method);
    }
 
//            public void actionPerformed(java.awt.event.ActionEvent e) {
//            //do nothing
//        }

//        public void itemStateChanged(ItemEvent e) {
//            switch (e.getStateChange()) {
//                case ItemEvent.SELECTED:
//                    
//                    System.out.println(this.getName() + ": SELECTED");
//
//                    break;
//                case ItemEvent.DESELECTED:
//                    
//                    System.out.println(this.getName() + ": DE-SELECTED");
//                    break;
//            }
//        }
    }
    

