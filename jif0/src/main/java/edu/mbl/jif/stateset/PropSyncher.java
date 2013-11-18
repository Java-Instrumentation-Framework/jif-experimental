package edu.mbl.jif.stateset;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

 public class PropSyncher {

        String name;
        Class type;
        Method stateReadMethod;
        Method stateWriteMethod;
        Method modelReadMethod;
        Method modelWriteMethod;
        Object model;
        Object state;

        public PropSyncher(String name, Class type,
            Object state,
            Method stateReadMethod, Method stateWriteMethod,
            Object model,
            Method modelReadMethod, Method modelWriteMethod)
          {
            this.name = name;
            this.type = type;
            this.state = state;
            this.stateReadMethod = stateReadMethod;
            this.stateWriteMethod = stateWriteMethod;
            this.model = model;
            this.modelReadMethod = modelReadMethod;
            this.modelWriteMethod = modelWriteMethod;
          }

        public void stateToModel()
          {
            try {
                Object value = stateReadMethod.invoke(state, (Object[]) null);
                modelWriteMethod.invoke(model, new Object[]{value});
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            }
          }

        public void modelToState()
          {
            try {
                Object value = modelReadMethod.invoke(model, (Object[]) null);
                stateWriteMethod.invoke(state, new Object[]{value});
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ModelStateSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
            }
          }

        public String toString()
          {
            StringBuffer sb = new StringBuffer();
            sb.append(name + "\n");
            sb.append(stateReadMethod.getName() + "\n");
            sb.append(stateWriteMethod.getName() + "\n");
            sb.append(modelReadMethod.getName() + "\n");
            sb.append(modelWriteMethod.getName() + "\n");
            return sb.toString();
          }

    }