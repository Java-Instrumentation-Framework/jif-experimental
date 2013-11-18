/*
 * DependentAction.java
 * Created on July 31, 2006, 1:20 PM
 */

package edu.mbl.jif.gui.action;

import com.jgoodies.binding.value.ValueModel;
//import edu.mbl.jif.binding.ExampleBindingBean;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
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
public class DependentAction extends BoundAction {
    
    private ArrayList andDependencies = new ArrayList<ValueModel>();
    private ArrayList orDependencies = new ArrayList<ValueModel>();
    
    
    /** Creates a new instance of DependentAction and does registerCallback */
    public DependentAction(String name, String command, Icon icon, Object handler, String method) {
        super(name,  command,  icon);
        // handler.method invoked on action
        registerCallback(handler, method);
        
    }
    
    // add a dependency - a boolean ValueModel
    //
    public void addDependencyAnd(ValueModel value) {
        value.addValueChangeListener(new UpdateHandler());
        andDependencies.add(value);
    }
    public void addDependency(ValueModel value) {
        addDependencyOr(value);
    }
    
    public void addDependencyOr(ValueModel value) {
        value.addValueChangeListener(new UpdateHandler());
        orDependencies.add(value);
    }
    
    private final class UpdateHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.print(evt.getPropertyName());
            System.out.println(" "+evt.getNewValue());
            boolean allowAction = true;
            for (Iterator i = andDependencies.iterator(); i.hasNext(); ) {
                Object v = ((ValueModel)i.next()).getValue();
                if(v instanceof Boolean) {
                    if(Boolean.valueOf((Boolean)(v))==false)
                        allowAction = false;
                }
            }
            for (Iterator i = orDependencies.iterator(); i.hasNext(); ) {
                Object v = ((ValueModel)i.next()).getValue();
                if(v instanceof Boolean) {
                    if(Boolean.valueOf((Boolean)(v))== true)
                        allowAction = true;
                }
            }
            System.out.println("doAction="+allowAction);
            setAllowed(allowAction);
        }
    }
    
    
    public void setAllowed(boolean t) {
        this.setEnabled(t);
    }
    
    //---------------------------------------------
    public static void main(String[] args) {
//        PresentationModel pm =
//                new PresentationModel(new ExampleBindingBean());
//        DependentAction dAct = new DependentAction(
//                "Acquire Image", "acqImage", null, new Object(), "method");
//        dAct.addDependencyAnd(
//                pm.getModel(ExampleBindingBean.PROPERTYNAME_BOOLEAN_VALUE));
//        pm.getModel(ExampleBindingBean.PROPERTYNAME_BOOLEAN_VALUE).setValue(false);
    }
}
