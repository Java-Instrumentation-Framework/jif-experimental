package edu.mbl.jif.stateset.dynclass;

import java.util.Map;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Public only because generated classes use it and they live in another
 * package.  Not for general use!
 */
public class Handler {

    private InvocationHandler delegate;
    private Map idToMethod;

    Handler(InvocationHandler delegate, Map idToMethod) {

        this.delegate = delegate;
        this.idToMethod = idToMethod;
    }

    public Object handle(Object proxy, String name, Object[] args) throws Throwable {

        Method m = (Method) idToMethod.get(name);
        if (m == null) {
            throw new RuntimeException("Unknown method id: " + name + 
                                       "; known set: " + idToMethod.keySet());
        }

        Object rval;
        rval = delegate.invoke(proxy, m, args);

        // what if an exception is thrown that the caller didn't declare?

        return rval;
    }
}
