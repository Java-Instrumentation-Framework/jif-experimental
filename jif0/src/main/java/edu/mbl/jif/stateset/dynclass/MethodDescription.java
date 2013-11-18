package edu.mbl.jif.stateset.dynclass;

import java.util.List;
import java.util.Collections;
import java.util.Arrays;

import java.lang.reflect.Method;

/**
 * <p>This class provides sufficient information to uniquely identify a
 * method in a class (name and parameters), but not enough to
 * fully describe a method (since it doesn't have return type or
 * exceptions).  For that, you need MethodDefinition.</p>
 *
 * <p>Usually, MethodDescription instances are not created directly.
 * Instead they are accessed through a MethodDefinition.</p>
 */
public final class MethodDescription {

    private String name;
    private List params;

    /**
     * Create a description with the given name and parameter list.
     * @param params the parameter type list.  null and 0-length arrays
     * are equivalent.
     */
    public MethodDescription(String name, Class[] params) {

        this.name = name;
        if (params == null) {
            this.params = Collections.EMPTY_LIST;
        }
        else {
            this.params = Collections.unmodifiableList(Arrays.asList(params));
        }
    }

    public String getName() {

        return name;
    }

    public Class[] getParameterTypes() {

        return (Class[]) params.toArray(new Class[params.size()]);
    }

    /**
     * Inspect the given Class for a Method matching this description.
     * Throws NoSuchMethodException if not matching method is found.
     */
    public Method getMethodFromClass(Class cls) throws NoSuchMethodException {

        return cls.getMethod(name, getParameterTypes());
    }

    public int hashCode() {

        return name.hashCode() + params.hashCode();
    }

    public boolean equals(Object other) {

        if (!(other instanceof MethodDescription)) {
            return false;
        }

        if (this == other) {
            return true;
        }

        MethodDescription rhs = (MethodDescription) other;
        return this.name.equals(rhs.name) && this.params.equals(rhs.params);
    }
}
