package edu.mbl.jif.stateset.dynclass;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

/**
 * Responsible for loading defined classes, and also creating their instances.
 */
class DynClassLoader extends ClassLoader {

    private int counter = 0;
    private int methodCount = 0;

    private Map descToClass = new HashMap();
    private Map classToMethodMap = Collections.synchronizedMap(new HashMap());

    DynClassLoader() {

        super(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Return the total number of methods defined by this classloader.
     * This is the best indicator of the amount of memory that is reachable
     * from this object.  Once a DynClassLoader reaches a certain threshold 
     * it is usually discarded.
     */
    int getMethodCount() {

        return methodCount;
    }

    /**
     * Create a Class from the given definition.  The class will have a 
     * generated unique name.
     */
    synchronized Class defineClass(ClassDefinition def) {

        String name = "GeneratedClass" + (++counter);
        return defineClass(def, name);
    }

    /**
     * Create a Class from the given definition.  Caller is responsible for
     * making the name unique.
     */
    synchronized Class defineClass(ClassDefinition def, String name) {

        Class cls = (Class) descToClass.get(def);
        if (cls != null) {
            return cls;
        }

        Translator tx = new Translator(name, def);

        byte[] bytes = tx.getClassBytes();
        cls = super.defineClass(name, bytes, 0, bytes.length);

        try {
            Map idToMethod = translateMethodMap(cls, tx.getIdToDescMap());
            classToMethodMap.put(cls, idToMethod);
        }
        catch(NoSuchMethodException nsme) {
            throw new RuntimeException("Can't find method on newly created class: " + nsme.getMessage());
        }

        descToClass.put(def, cls);

        methodCount += tx.getMethodCount();

        return cls;
    }

    Object instantiate(Class cls, InvocationHandler impl) throws Exception {

        Map idToMethod = (Map) classToMethodMap.get(cls);
        if (idToMethod == null) {
            String msg;
            if (cls.getClassLoader() == this) {
                msg = "Strange - no data for: " + cls.getName();
            }
            else {
                msg = "Not the classloader for: " + cls.getName();
            }
            throw new RuntimeException(msg);
        }
        Constructor ctor = cls.getConstructor(new Class[] {
            Handler.class,
        });
        return ctor.newInstance(new Object[] {
            new Handler(impl, idToMethod),
        });
    }

    /**
     * idToDesc maps generated ids to MethodDescription instances.
     * This method translates these MethodDescription instances to
     * actual java Methods.  This map is used by the Handler class
     * to provide Methods to the client-supplied InvocationHandler.
     */
    private Map translateMethodMap(Class cls, Map idToDesc) throws NoSuchMethodException {

        Map newMap = new HashMap(idToDesc.size());
        Iterator iter = idToDesc.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            MethodDescription desc = (MethodDescription) idToDesc.get(key);
            Method m = desc.getMethodFromClass(cls);
            newMap.put(key, m);
        }
        return Collections.unmodifiableMap(newMap);
    }    
}
