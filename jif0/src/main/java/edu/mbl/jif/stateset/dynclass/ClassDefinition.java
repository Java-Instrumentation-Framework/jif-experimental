package edu.mbl.jif.stateset.dynclass;

import java.util.Iterator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class provides sufficient information for generating a java
 * Class.
 */
public class ClassDefinition {

    private Class superclass;
    private List interfaces;
    
    private List methodList;

    private boolean noargCtor = false;

    /**
     * Create a ClassDefinition with the given
     * interfaces.  Implementations of
     * interface methods and abstract methods will be created implicitly.
     * @param interfaces the interfaces to implement in the generated
     * class.  null and 0-length arrays are equivalent.
     */
    public ClassDefinition(Class[] interfaces) {

        this(Object.class, interfaces, null);
    }

    /**
     * Create a ClassDefinition with the given superclass and
     * interfaces.  Implementations of
     * interface methods and abstract methods will be created implicitly.
     * @param interfaces the interfaces to implement in the generated
     * class.  null and 0-length arrays are equivalent.
     */
    public ClassDefinition(Class superclass,
                           Class[] interfaces) {

        this(superclass, interfaces, null);
    }

    /**
     * Create a ClassDefinition with the given superclass,
     * interfaces and methods to implement or override.  Note that 
     * implementations of
     * interface methods and abstract methods will be created implicitly,
     * so the only methods that need to be explicity specified are 
     * non-abstract methods that you wish to override.
     * @param interfaces the interfaces to implement in the generated
     * class.  null and 0-length arrays are equivalent.
     * @param methods the list of methods to override.  In addition to
     * methods specified in this list, abstract methods will be implemented
     * in the generated class.
     * null and 0-length arrays are equivalent.
     */
    public ClassDefinition(Class superclass,
                           Class[] interfaces,
                           MethodDefinition[] methods) {

        if (superclass == null) {
            throw new IllegalArgumentException("null superclass");
        }
        this.superclass = superclass;
        this.interfaces = (interfaces==null)? Collections.EMPTY_LIST :
            Collections.unmodifiableList(Arrays.asList(interfaces));
        this.methodList = (methods==null)? Collections.EMPTY_LIST :
            Collections.unmodifiableList(Arrays.asList(methods));
    }

    // This constructor creates a definition with the given superclass
    // and no interfaces or methods.  The generated class will have a
    // no-argument constructor (and no other constructors).  Really only
    // for creating BeanInfo's.
    ClassDefinition(Class superclass) {

        this(superclass, null);
        noargCtor = true;
    }

    /**
     * Return the superclass of the defined class.
     */
    public Class getSuperclass() {

        return superclass;
    }

    /**
     * Return the interfaces implemented by the defined class.
     */
    public Class[] getInterfaces() {

        return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
    }

    /**
     * Return the methods of the defined class.
     */
    public List getMethodDefinitions() {

        return methodList;
    }

    boolean getMakeCtorNoarg() {

        return noargCtor;
    }

    public int hashCode() {

        return superclass.hashCode() + interfaces.hashCode() + methodList.hashCode();
    }

    public boolean equals(Object other) {

        if (!(other instanceof ClassDefinition)) {
            return false;
        }

        if (this == other) {
            return true;
        }

        ClassDefinition rhs = (ClassDefinition) other;

        return this.superclass.equals(rhs.superclass) &&
            this.interfaces.equals(rhs.interfaces) &&
            this.methodList.equals(rhs.methodList) &&
            this.noargCtor == rhs.noargCtor;
    }
}
