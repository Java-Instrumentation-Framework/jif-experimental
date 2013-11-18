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
import java.util.prefs.Preferences;

/**
 *
 * @author GBH
 */
public class SavedStatePrefs {

    Preferences containingNode;
    Object model;
    String modelName;
    Class modelClass;
    PropertyDescriptor[] modelProperties;
    ArrayList<PrefModelSyncher> propSynchers = new ArrayList<PrefModelSyncher>();
    String displayName = "displayName";

    // Construct from a restored SavedStateMap
    public SavedStatePrefs(Preferences node, Object model) {
        String modelKey = "modelKey"; //model.getKey();
        this.containingNode = node.node(modelName);
        this.makeSynchronizers();
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
    public void associateWithModel(Object model) {
        this.model = model;
        this.makeSynchronizers();
    }

    public void enter() {
    }

    public void exit() {
    }

    public void showPropSynchers() {
        for (PrefModelSyncher propSync : propSynchers) {
            System.out.println(propSync);
        }
    }

    public void syncToModel() {
        for (PrefModelSyncher propSync : propSynchers) {
            propSync.prefToModel(containingNode);
        }
    }

    public void syncFromModel() {
        for (PrefModelSyncher propSync : propSynchers) {
            propSync.modelToPref(containingNode);
        }
    }

    public void makeSynchronizers() {
        modelClass = model.getClass();
        modelProperties = getProperties(modelClass);
        Method prefGetMethod = null;
        Method prefPutMethod = null;
        for (int j = 0; j < modelProperties.length; j++) {
            String modelPropName = modelProperties[j].getName();
            Method modelReadMethod = modelProperties[j].getReadMethod();
            Method modelWriteMethod = modelProperties[j].getWriteMethod();
            Class m = modelProperties[j].getPropertyType();

            try {
                if (m.equals(String.class)) {
                    prefPutMethod = Preferences.class.getDeclaredMethod("put", String.class, String.class);
                    prefGetMethod = Preferences.class.getDeclaredMethod("get", String.class, String.class);
                } else if (m.equals(Boolean.class)) {
                    prefPutMethod = Preferences.class.getDeclaredMethod("putBoolean", String.class, boolean.class);
                    prefGetMethod = Preferences.class.getDeclaredMethod("getBoolean", String.class, boolean.class);
                } else if (m.equals(Integer.class)) {
                    prefPutMethod = Preferences.class.getDeclaredMethod("putInt", String.class, int.class);
                    prefGetMethod = Preferences.class.getDeclaredMethod("getInt", String.class, int.class);
                } else if (m.equals(Long.class)) {
                    prefPutMethod = Preferences.class.getDeclaredMethod("putLong", String.class, long.class);
                    prefGetMethod = Preferences.class.getDeclaredMethod("getLong", String.class, long.class);
                } else if (m.equals(Float.class)) {
                    prefPutMethod = Preferences.class.getDeclaredMethod("putFloat", String.class, float.class);
                    prefGetMethod = Preferences.class.getDeclaredMethod("getFloat", String.class, float.class);
                } else if (m.equals(Double.class)) {
                    prefPutMethod = Preferences.class.getDeclaredMethod("putDouble", String.class, double.class);
                    prefGetMethod = Preferences.class.getDeclaredMethod("getDouble", String.class, double.class);
                }
                if (prefGetMethod != null && prefPutMethod != null) {
                    propSynchers.add(new PrefModelSyncher(modelPropName, m,
                        prefGetMethod, prefPutMethod,
                        model, modelReadMethod, modelWriteMethod));
                }
            } catch (NoSuchMethodException noSuchMethodException) {
            } catch (SecurityException securityException) {
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
