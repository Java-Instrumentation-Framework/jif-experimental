/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.microscope.illum;

import edu.mbl.jif.camacq.InstrumentController;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class TestReflectProps {

    static InstrumentController instCtrl;

    public static void main(String[] args)
      {
        String className = "edu.mbl.jif.microscope.illum.IllumModel";

        // Find the specified class
        Class clas;

        // Find the specified method

        try {
            clas = Class.forName(className);
            Method[] method = clas.getDeclaredMethods();
            for (int i = 0; i < method.length; i++) {
                Method method1 = method[i];
                System.out.println(method1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestReflectProps.class.getName()).log(Level.SEVERE, null, ex);
        }


//        IllumModel obj = (IllumModel) instCtrl.getModel("illum");
//        String prop = "OpenEpi";
//        String mname = "get" + prop;
//        Class[] types = new Class[]{};
//        Method method = obj.getClass().getMethod(mname, types);
//        Object result = method.invoke(obj, new Object[0]);
//        System.out.println("get: " + result);
//        int value = ((Integer) result).intValue() + 1;
//        mname = "set" + prop;
//        types = new Class[]{int.class};
//        method = obj.getClass().getMethod(mname, types);
//        method.invoke(obj, new Object[]{new Integer(value)});

      //boolean epiShutter = ((IllumModel)getModel(“illumEpi”)).getShutterOpen();


      // Invoke the method on the narrowed arguments
//      Object retval = method.invoke( null, args );
//
//      // Return the result of the invocation
//      System.out.println( retval.toString());
//    } catch( ClassNotFoundException cnfe ) {
//      throw new CommandLineException(
//        "Can't find class "+className );
//    } catch( NoSuchMethodException nsme ) {
//      throw new CommandLineException(
//        "Can't find method "+methodName+" in "+className );
//    } catch( IllegalAccessException iae ) {
//      throw new CommandLineException(
//        "Not allowed to call method "+methodName+" in "+className );
//    } catch( InvocationTargetException ite ) {
//      // If the method itself throws an exception, we want to save it
//      throw
//        (CommandLineException)
//        new CommandLineException(
//          "Exception while executing command" ).initCause( ite );
//    }
      }

}
