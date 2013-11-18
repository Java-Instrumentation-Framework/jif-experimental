package edu.mbl.jif.stateset;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class PrefModelSyncher {

    String name;
    Class type;
    Method prefGetMethod;
    Method prefPutMethod;
    Method modelReadMethod;
    Method modelWriteMethod;
    Object model;

    public PrefModelSyncher(String name, Class type,
            Method prefGetMethod, Method prefPutMethod,
            Object model,
            Method modelReadMethod, Method modelWriteMethod) {
        this.name = name;
        this.type = type;
        this.prefGetMethod = prefGetMethod;
        this.prefPutMethod = prefPutMethod;
        this.model = model;
        this.modelReadMethod = modelReadMethod;
        this.modelWriteMethod = modelWriteMethod;
    }

    public void prefToModel(Preferences prefNode) {
        try {
            Object value = prefGetMethod.invoke(prefNode, name, null);
            modelWriteMethod.invoke(model, value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modelToPref(Preferences prefNode) {
        try {
            Object value = modelReadMethod.invoke(model, (Object[]) null);
            prefPutMethod.invoke(prefNode, name, value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(name + "\n");
        sb.append(prefGetMethod.getName() + "\n");
        sb.append(prefPutMethod.getName() + "\n");
        sb.append(modelReadMethod.getName() + "\n");
        sb.append(modelWriteMethod.getName() + "\n");
        return sb.toString();
    }
}