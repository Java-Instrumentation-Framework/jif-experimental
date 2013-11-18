package edu.mbl.jif.stateset.dynclass;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

/**
 * <p>BeanCreator provides methods for representing a Map as a JavaBean.
 * The <code>createBeanFromMap()</code> method performs the entire conversion
 * in a single step.  This is the most convenient way to perform conversion;
 * however it is not the fastest, and may constribute to memory retention
 * (i.e. leaks) in some cases - see below.</p>
 *
 * <p>There is also a two-step Map-to-JavaBean process that is
 * more efficient and eliminates memory concerns, which can be used when
 * the set of properties on the generated bean is known in advance.  First,
 * create the JavaBeans Class with <code>createClassForProperties()</code>.
 * Cache this class for the life of the application (e.g. in a static 
 * variable).  Then, when a JavaBean with this property set is needed, call
 * <code>createBean()</code> with a Map reflecting the property values.</p>
 *
 * <p>Example:
 * <blockquote><code>
 * // Class variable:<br>
 * private static final Class userBeanClass = BeanCreator.createClassForProperties(new String[] { "userName", "email" });<br>
 * // ...<br>
 * Map map = ...; // create map with values for "userName" and "email"<br>
 * Object bean = BeanCreator.createBean(userBeanClass, map);
 * </code></blockquote>
 * </p>
 * <p><b>Memory issue:</b> BeanCreator will not retain references to 
 * Classes it generates, since this would prevent garbage collection of
 * the Class instances.  However, many third-party JavaBeans libraries
 * keep static caches of Classes they have analyzed, and do not provide for
 * releasing cached resources.  This will not be noticable in short-lived
 * applications, but long-running servers could be affected.  If you are
 * concerned about this, use <code>createClassFromPropertySet()</code> to
 * generate a fixed set of Classes, and instantiate them with 
 * <code>createBean()</code>.</p>
 *
 * <p><b>Property names:</b> Any String can be used as a property name,
 * even if it is not a valid Java identifier.  If BeanCreator encounters a
 * property name that does not follow the usual JavaBeans casing conventions
 * (or contains non-identifier characters), BeanCreator will generate a
 * BeanInfo class for the bean class.  This is transparent to clients and
 * bean inspection tools.</p>
 *
 */
public class BeanCreator {

    private BeanCreator() {
    }

    /**
     * <p>Generate a object with get and set methods for every key in the map
     * that can be translated into a JavaBeans property name.  See class
     * documentation for possible memory issues.
     */
    public static Object createBeanFromMap(Map map) throws Exception {

        return createBean(createClassForProperties(map.keySet()), map);
    }

    /**
     * Create a JavaBeans class from the given property list.
     */
    public static Class createClassForProperties(String[] props) {

        HashSet hs = new HashSet();
        hs.addAll(Arrays.asList(props));
        return createClassForProperties(hs);
    }

    /**
     * Create a JavaBeans class from the given property set.  Instances of
     * the class can be instantiated by <code>createBean</code>.
     */
    public static Class createClassForProperties(Set keySet) {

        List methods = new ArrayList(keySet.size() * 2);
        Iterator iter = keySet.iterator();
        boolean needsBeanInfo = false;

        while (iter.hasNext()) {
            Object key = iter.next();
            if (!(key instanceof String)) {
                continue;
            }
            String name = (String) key;
            String suffix = PropertyMethodNameTx.encodeProperty(name);

            boolean isStandardProperty = suffix.length() == name.length();
            
            String getterPrefix = (isStandardProperty? "get" : "g$t");
            methods.add(new MethodDefinition(getterPrefix + suffix,
                                             Object.class,
                                             null));
            String setterPrefix = (isStandardProperty? "set" : "s$t");
            methods.add(new MethodDefinition(setterPrefix + suffix,
                                             Void.TYPE,
                                             new Class[] { Object.class }));
            needsBeanInfo |= !isStandardProperty;
        }
        
        MethodDefinition[] ml = (MethodDefinition[])
            methods.toArray(new MethodDefinition[methods.size()]);
        ClassDefinition classDef = new ClassDefinition(Object.class, null, ml);
        
        Class cls = ClassCreator.defineClass(classDef);
        if (needsBeanInfo) {

            ClassDefinition infoClass = new ClassDefinition(DynBeanInfoBase.class);
            DynClassLoader cl = (DynClassLoader) cls.getClassLoader();
            cl.defineClass(infoClass, cls.getName() + "BeanInfo");
        }
        return cls;
    }

    /**
     * Create a JavaBean with the given class.  Property values are 
     * provided by propertyMap.
     * @param beanClass a JavaBeans class generated by <code>BeanCreator.createClassForProperties()</code>
     */
    public static Object createBean(Class beanClass, Map propertyMap) throws Exception {

        return ClassCreator.newInstance(beanClass, 
                                        new MapInvocationHandler(propertyMap));
    }

    private static class MapInvocationHandler implements InvocationHandler {

        private Map map;

        MapInvocationHandler(Map map) {
            
            this.map = map;
        }

        public Object invoke(Object p, Method m, Object[] args) {

            String name = m.getName();
            String propName = PropertyMethodNameTx.decodeProperty(name, 3);

            if (name.startsWith("get") || name.startsWith("g$t")) {
                return map.get(propName);
            }
            else if (name.startsWith("set") || name.startsWith("s$t")) {
                map.put(propName, args[0]);
                return null;
            }
            else {
                throw new IllegalArgumentException("Can't implement: " + name);
            }
        }
    }
}
