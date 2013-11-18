/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stateset;

import com.l2fprod.common.propertysheet.PropertySheetPanel;
import java.awt.BorderLayout;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author GBH
 */
public class ModelStateSynchronizer {

    String stateName;
    String modelName;
    Class stateClass;
    Class modelClass;
    Object state;
    Object model;
    PropertyDescriptor[] stateProperties;
    PropertyDescriptor[] modelProperties;
    ArrayList<PropSyncher> propSynchers = new ArrayList<PropSyncher>();

    public ModelStateSynchronizer(Object state, Object model)
      {
        this.state = state;
        this.model = model;
        stateClass = state.getClass();
        modelClass = model.getClass();
                  System.out.println("ModelStateSynchronizer(state: " + stateClass.getName() + ", model: " +
                  modelClass.getName()+")");
        stateProperties = getProperties(stateClass);
        modelProperties = getProperties(modelClass);
        for (int i = 0; i < stateProperties.length; i++) {
            Class s = stateProperties[i].getPropertyType();
            if (s == null) {
                continue;
            }
            String statePropName = stateProperties[i].getName();
            System.out.print("  State: " + statePropName);
            System.out.print(" (" + s.getName() + ") ");
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
                        state, stateReadMethod, stateWriteMethod,
                        model, modelReadMethod, modelWriteMethod));
                }
            }
        }
//        for (int i = 0; i < stateProperties.length; i++) {
//            Class p = stateProperties[i].getPropertyType();
//            if (p == null) {
//                continue;
//            }
//            System.out.println(stateProperties[i].getName());
//            System.out.println("  type:  " + p.getName());
//            Method readMethod = properties[i].getReadMethod();
//            if (readMethod != null) {
//                print("    read method: " + readMethod);
//            }
//            Method writeMethod = properties[i].getWriteMethod();
//            if (writeMethod != null) {
//                print("    write method: " + writeMethod);
//            }
//            print("--------------------------------");
//        }
      }

    public PropertyDescriptor[] getProperties(Class bean)
      {
        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(bean, Object.class);
        } catch (IntrospectionException e) {
            System.err.println("Couldn't introspect " + bean.getName());
            return null;
        }
        PropertyDescriptor[] properties = bi.getPropertyDescriptors();
        return properties;
      }

    public ModelStateSynchronizer(String state, String model)
      {
        this.stateName = state;
        this.modelName = model;

        try {
            stateClass = Class.forName(state);
        } catch (ClassNotFoundException ex) {
            System.err.println("Couldn't find " + state);
        }
        try {
            modelClass = Class.forName(model);
        } catch (ClassNotFoundException ex) {
            System.err.println("Couldn't find " + model);
        }
        stateProperties = getProperties(stateClass);
        modelProperties = getProperties(modelClass);
        for (int i = 0; i < stateProperties.length; i++) {
            Class s = stateProperties[i].getPropertyType();
            if (s == null) {
                continue;
            }
            String statePropName = stateProperties[i].getName();
            System.out.print("State: " + statePropName);
            System.out.print(" (" + s.getName() + ") ");
            for (int j = 0; j < modelProperties.length; j++) {
                Class m = modelProperties[j].getPropertyType();
                if (m == null) {
                    continue;
                }
                String modelPropName = modelProperties[j].getName();
                if (modelPropName.equalsIgnoreCase(statePropName)) {
                    System.out.print("matches ");
                    System.out.print("Model: " + modelPropName);
                    System.out.println(" (" + m.getName() + ")");
                    Method modelReadMethod = modelProperties[j].getReadMethod();
                    Method stateReadMethod = stateProperties[i].getReadMethod();
                    Method modelWriteMethod = modelProperties[j].getWriteMethod();
                    Method stateWriteMethod = stateProperties[i].getWriteMethod();
                    propSynchers.add(new PropSyncher(statePropName, m,
                        state, stateReadMethod, stateWriteMethod,
                        model, modelReadMethod, modelWriteMethod));
                }
                else {
                    System.out.println(" ... no match.");
                }
            }
        }
      }

    public void showPropSynchers()
      {
				System.out.println("/nPropSynchers:");
        for (PropSyncher propSync : propSynchers) {
            System.out.println(propSync);
        }
      }

    public void syncStateToModel()
      {
        for (PropSyncher propSync : propSynchers) {
            propSync.stateToModel();
        }
      }

    public void syncModelToState()
      {
        for (PropSyncher propSync : propSynchers) {
            propSync.modelToState();
        }
      }


    // for each PropertyDescriptor in State, 
    // find corresponding Property in Model
    public static void main(String[] args)
      {
        //String state = "edu.mbl.jif.camera.CameraState";
        //String model = "edu.mbl.jif.camera.CameraModel";
        TestModelBean model = new TestModelBean();
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
              {
                System.out.println("PropChange: " + evt.getPropertyName() + " = " +
                    evt.getNewValue());
              }
        });
        model.setAnInteger(5);
        TestSavedStateBean state = new TestSavedStateBean(model);
        ModelStateSynchronizer mss = new ModelStateSynchronizer(state, model);
        mss.showPropSynchers();
        //mss.syncModelToState();
        System.out.println("\nDoing state-to-model sync");
        mss.syncStateToModel();
      }
    

}