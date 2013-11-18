package edu.mbl.jif.stateset.dynclass;

import java.beans.SimpleBeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class of BeanInfo for auto-generated bean classes.  
 * Not for general use!
 */
// Note: this class interprets method names on a class.  What this
// class does could be simplified by keeping a name->descriptor
// Map, but that would be even more per-class data to manage.

public abstract class DynBeanInfoBase extends SimpleBeanInfo {

    private static void checkGetter(Method m) {

        if (!Object.class.equals(m.getReturnType()) ||
            m.getParameterTypes().length > 0) {
            throw new RuntimeException("Not a dynclass method: " + m);
        }
    }
 
    private static void checkSetter(Method m) {

        Class[] params = m.getParameterTypes();
        if (!Void.TYPE.equals(m.getReturnType()) ||
            params.length != 1 || !Object.class.equals(params[0])) {
            throw new RuntimeException("Not a dynclass method: " + m);
        }
    }

   static PropertyDescriptor[] getPropertyDescriptors(Class beanType) {

        // have to read it from the class, to keep the "no extra 
        // cached data" rule
        Map getterMap = new HashMap();
        Map setterMap = new HashMap();

        Method[] methods = beanType.getDeclaredMethods();
        for (int i=0; i < methods.length; i++) {
            Method m = methods[i];
            String name = m.getName();
            String propName = PropertyMethodNameTx.decodeProperty(name, 3);
            if (name.startsWith("g")) {
                checkGetter(m);
                getterMap.put(propName, m);
            }
            else if (name.startsWith("s")) {
                checkSetter(m);
                setterMap.put(propName, m);
            }
            else {
                throw new RuntimeException("Not a dynclass bean: " + beanType.getName());
            }
        }

        if (getterMap.size() != setterMap.size()) {
            throw new RuntimeException("Not a dynclass bean: " + beanType.getName());
        }

        List descriptors = new ArrayList(getterMap.size());
        Iterator iter = getterMap.keySet().iterator();
        while (iter.hasNext()) {

            String prop = (String) iter.next();
            try {
                descriptors.add(new PropertyDescriptor(prop, (Method) getterMap.get(prop), (Method) setterMap.get(prop)));
            }
            catch(IntrospectionException ie) {
                throw new RuntimeException("Could not create PropertyDescriptor for " + prop + ":" + ie.getMessage());
            }
        }

        return (PropertyDescriptor[]) descriptors.toArray(new PropertyDescriptor[descriptors.size()]);
    }

    public PropertyDescriptor[] getPropertyDescriptors() {

        // If this class is FooBeanInfo, then the bean class is Foo
        String myName = getClass().getName();
        String beanClassName = myName.substring(0, myName.length() - "BeanInfo".length());
        try {
            Class beanType = getClass().getClassLoader().loadClass(beanClassName);
            return getPropertyDescriptors(beanType);
        }
        catch(ClassNotFoundException cnfe) {
            throw new RuntimeException("No class for name: " + beanClassName);
        }
    }
}
