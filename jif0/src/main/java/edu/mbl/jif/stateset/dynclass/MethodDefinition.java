package edu.mbl.jif.stateset.dynclass;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.lang.reflect.Method;

/**
 * Instances of this class provide enough information to declare
 * a method.  Identifying information is accessed via the
 * MethodDescription property of this class.
 */
public final class MethodDefinition {

    private MethodDescription desc;
    private Class rtype;
    private List exceptions;

    /**
     * Create a MethodDefinition whose name, parameter type list, 
     * exception list and return type are taken from the given 
     * Method.
     */
    public MethodDefinition(Method source) {

        this(source.getName(),
             source.getReturnType(),
             source.getParameterTypes(),
             source.getExceptionTypes());
    }

    /**
     * Create a MethodDefinition with the given name, parameter type list, 
     * and return type, and an empty exception list.
     * @param params the parameter type list.  null and 0-length arrays
     * are equivalent.
     */
    public MethodDefinition(String name,
                            Class rtype,
                            Class[] params) {

        this(name, rtype, params, null);
    }

    /**
     * Create a MethodDefinition with the given name, parameter type list, 
     * and return type, and exception list.
     * @param params the parameter type list.  null and 0-length arrays
     * are equivalent.
     * @param exceptions the declared exceptions.  null and 0-length arrays
     * are equivalent.
     */
    public MethodDefinition(String name,
                            Class rtype,
                            Class[] params,
                            Class[] exceptions) {
        
        desc = new MethodDescription(name, params);
        this.rtype = rtype;
        if (exceptions == null) {
            this.exceptions = Collections.EMPTY_LIST;
        }
        else {
            this.exceptions = Collections.unmodifiableList(Arrays.asList(exceptions));
        }
    }

    public MethodDescription getDescription() {

        return desc;
    }

    public Class getReturnType() {

        return rtype;
    }

    public Class[] getExceptionTypes() {

        return (Class[]) exceptions.toArray(new Class[exceptions.size()]);
    }

    public int hashCode() {

        return rtype.hashCode() + desc.hashCode() + exceptions.hashCode();
    }

    public boolean equals(Object other) {

        if (!(other instanceof MethodDefinition)) {
            return false;
        }

        if (this == other) {
            return true;
        }

        MethodDefinition rhs = (MethodDefinition) other;
        return this.rtype.equals(rhs.rtype) && 
            this.desc.equals(rhs.desc) &&
            this.exceptions.equals(rhs.exceptions);
    }
}
