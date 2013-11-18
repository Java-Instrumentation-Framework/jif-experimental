package edu.mbl.jif.stateset.dynclass;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationHandler;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

/**
 * ClassCreator turns a ClassDefinition into a real-live Class.
 * It also has utility methods for constructing instances of
 * classes it creates.
 */
public class ClassCreator {

    private static DynClassLoader classLoader = new DynClassLoader();

    /**
     * Create a new instance of the given class, which must have been
     * created by ClassCreator.  Method calls will be dispatched to
     * the given InvocationHandler.
     */
    public static Object newInstance(Class cls, InvocationHandler impl) throws Exception {

        if (cls == null) {
            throw new IllegalArgumentException("Class is null");
        }
        if (impl == null) {
            throw new IllegalArgumentException("Handler is null");
        }
        ClassLoader cl = cls.getClassLoader();
        if (!(cl instanceof DynClassLoader)) {
            throw new IllegalArgumentException("Not a DynClass: " + cls.getName());
        }

        return ((DynClassLoader)cl).instantiate(cls, impl);
    }

    /*
    public static Object newInstance(Class cls, 
                                     Object[] args, 
                                     InvocationHandler impl) throws Exception {
    }
    */

    /**
     * Create a Class from the given description.  Recent definitions are
     * cached, so if an equivalent ClassDefinition was provided recently
     * then there is a good chance the same class will be returned.
     */
    public static Class defineClass(ClassDefinition def) {

        if (classLoader.getMethodCount() > 300) {
            // put current loader out to pasture
            classLoader = new DynClassLoader();
        }
        return classLoader.defineClass(def);
    }
}
