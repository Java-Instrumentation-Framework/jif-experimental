package edu.mbl.jif.stateset;

import edu.mbl.jif.stateset.dynclass.BeanCreator;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class SavedState {

    SavedStateMap savedStateMap;
    Object stateBean;
    //HashMap map;
    Class stateClass;
    Object model;
    String modelName;
    Class modelClass;
    PropertyDescriptor[] stateProperties;
    PropertyDescriptor[] modelProperties;
    ArrayList<PropSyncher> propSynchers = new ArrayList<PropSyncher>();
    String displayName = "displayName";

    // Construct from a restored SavedStateMap
    public SavedState(SavedStateMap savedStateMap) {
        this.savedStateMap = savedStateMap;
        this.modelName = savedStateMap.associatedModel;
        this.displayName = savedStateMap.displayName;
        model = findModel(this.modelName);
        createBean();
        if (stateBean != null) {
            this.makeSynchronizers();
        }
    }

    // Construct a new SavedState
    public SavedState(SavedStateMap savedStateMap, Object model) {
        this.savedStateMap = savedStateMap;
        this.displayName = savedStateMap.displayName;
        this.model = model;
        modelName = model.getClass().getName();

        createBean();
        BeanInfo bi = new SimpleBeanInfo();
        PropertyDescriptor[] pd;
        try {
            bi = Introspector.getBeanInfo(stateBean.getClass());
            pd = bi.getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            Logger.getLogger(SavedState.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        if (stateBean != null) {
            this.makeSynchronizers();
        }

    }

    private Object findModel(String modelName) {
        return null;
    }
//    static void printSuperclasses(Object o)
//      {
//        Class subclass = o.getClass();
//        Class superclass = subclass.getSuperclass();
//        while (superclass != null) {
//            String className = superclass.getName();
//            System.out.println(className);
//            subclass = superclass;
//            superclass = subclass.getSuperclass();
//        }
//      }

    private void createBean() {
        try {
            stateBean = BeanCreator.createBeanFromMap(savedStateMap.propertiesMap);
        } catch (Exception ex) {
            Logger.getLogger(SavedState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void associateWithModel(Object model) {
        this.model = model;
        this.makeSynchronizers();
    }

    public void enter() {
    }

    public void exit() {
    }

    public void showPropSynchers() {
        for (PropSyncher propSync : propSynchers) {
            System.out.println(propSync);
        }
    }

    public void syncToModel() {
        for (PropSyncher propSync : propSynchers) {
            propSync.stateToModel();
        }
    }

    public void syncFromModel() {
        for (PropSyncher propSync : propSynchers) {
            propSync.modelToState();
        }
    }

    public void makeSynchronizers() {
        stateClass = stateBean.getClass();
        modelClass = model.getClass();
        System.out.println("makeSynchronizers: state: " + stateClass.getName() + ", model: " +
                modelClass.getName());
        stateProperties = getProperties(stateClass);
        modelProperties = getProperties(modelClass);
        for (int i = 0; i < stateProperties.length; i++) {
            Class s = stateProperties[i].getPropertyType();
            if (s == null) {
                continue;
            }
            String statePropName = stateProperties[i].getName();
            System.out.print("  State: " + statePropName);
            System.out.print(" (" + s.getName() + ")");
            // lookup matching properties in the model
            for (int j = 0; j < modelProperties.length; j++) {
                Class m = modelProperties[j].getPropertyType();
                if (m == null) {
                    continue;
                }
                String modelPropName = modelProperties[j].getName();
                if (modelPropName.equalsIgnoreCase(statePropName)) {
                    // for each matching field/property, create a syncher...
                    System.out.print(" matches ");
                    System.out.print("Model: " + modelPropName);
                    System.out.println(" (" + m.getName() + ")");
                    Method modelReadMethod = modelProperties[j].getReadMethod();
                    Method stateReadMethod = stateProperties[i].getReadMethod();
                    Method modelWriteMethod = modelProperties[j].getWriteMethod();
                    Method stateWriteMethod = stateProperties[i].getWriteMethod();
                    propSynchers.add(new PropSyncher(statePropName, m,
                            this, stateReadMethod, stateWriteMethod,
                            model, modelReadMethod, modelWriteMethod));
                }
            }
        }
    }

    PropertyDescriptor[] getProperties(Class bean) {
        BeanInfo bi = new SimpleBeanInfo();
        try {
            bi = Introspector.getBeanInfo(bean, Object.class);
        } catch (IntrospectionException e) {
            System.err.println("Couldn't introspect " + bean.getName());
            return null;
        }
        PropertyDescriptor[] properties = bi.getPropertyDescriptors();
        return properties;
    }
}
