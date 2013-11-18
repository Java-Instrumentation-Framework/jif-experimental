
package tests.binding.veto;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class Listener implements VetoableChangeListener { 
    public void vetoableChange(PropertyChangeEvent event) throws PropertyVetoException { 
        if (!isUndo(event)) 
            if (event.getPropertyName().equals("value")) 
                if ((Integer) event.getNewValue() < 0) 
                    throw new PropertyVetoException("I object!", event); 
    } 

    private static boolean isUndo(PropertyChangeEvent event) { 
        try { 
            return event.getNewValue().equals(getCurrentValue(event)); 
        } catch (Exception exception) { 
            return false; 
        } 
    } 

    private static Object getCurrentValue(PropertyChangeEvent event) throws Exception { 
        Object bean = event.getSource(); 
        String name = event.getPropertyName(); 
        BeanInfo info = Introspector.getBeanInfo(bean.getClass()); 
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) { 
            if (pd.getName().equals(name)) { 
                return pd.getReadMethod().invoke(bean); 
            } 
        } 
        throw new IllegalArgumentException(name); 
    } 
}