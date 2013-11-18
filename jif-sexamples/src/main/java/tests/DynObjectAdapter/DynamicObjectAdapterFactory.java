/*
From Dynamic Object Adapter using Dynamic Proxies
 * http://www.javaspecialists.eu/archive/Issue108.html
 * also see: http://www.artima.com/weblogs/viewpost.jsp?thread=109017
 */
package tests.DynObjectAdapter;

import java.lang.reflect.*;
import java.util.*;

public class DynamicObjectAdapterFactory {

  public static <T> T adapt(final Object adaptee,
          final Class<T> target,
          final Object adapter) {
    return (T) Proxy.newProxyInstance(
            target.getClassLoader(),
            new Class[]{target},
            new InvocationHandler() {

              private Map<MethodIdentifier, Method> adaptedMethods = new HashMap<MethodIdentifier, Method>();
              // initializer block - find all methods in adapter object
              {
                Method[] methods = adapter.getClass().getDeclaredMethods();
                for (Method m : methods) {
                  adaptedMethods.put(new MethodIdentifier(m), m);
                }
              }

              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                  Method other = adaptedMethods.get(new MethodIdentifier(method));
                  if (other != null) {
                    return other.invoke(adapter, args);
                  } else {
                    return method.invoke(adaptee, args);
                  }
                } catch (InvocationTargetException e) {
                  throw e.getTargetException();
                }
              }
            });
  }

  private static class MethodIdentifier {

    private final String name;
    private final Class[] parameters;

    public MethodIdentifier(Method m) {
      name = m.getName();
      parameters = m.getParameterTypes();
    }
    // we can save time by assuming that we only compare against
    // other MethodIdentifier objects

    public boolean equals(Object o) {
      MethodIdentifier mid = (MethodIdentifier) o;
      return name.equals(mid.name) &&
              Arrays.equals(parameters, mid.parameters);
    }

    public int hashCode() {
      return name.hashCode();
    }
  }
}

/*
 * 'Tweeked version from
 * http://stackoverflow.com/questions/16112675/java-abstract-base-enum-class-for-singleton
 * public class DynamicObjectAdapterFactory {
  // Use methods in adaptee unless they exist in target in which case use adapter.
  // Implement target in passing.
  public static <T> T adapt(final Object adaptee,
                            final Class<T> target,
                            final Object adapter) {

    return (T) Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[]{target},
            new InvocationHandler() {
      private final String name =
              adaptee.getClass().getSimpleName() + "(" + adaptee.toString() + ")"
              + "+" + adapter.getClass().getSimpleName() + "(" + adapter.toString() + ")";
      // The methods I wish to adapt.
      private Map<MethodIdentifier, Method> adaptedMethods = new HashMap<>();

      {
        // initializer block - find all methods in adapter object
        Method[] methods = adapter.getClass().getDeclaredMethods();
        for (Method m : methods) {
          // Keep a map of them.
          adaptedMethods.put(new MethodIdentifier(m), m);
        }
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
          // Has it been adapted?
          Method otherMethod = adaptedMethods.get(new MethodIdentifier(method));
          if (otherMethod != null) {
            return otherMethod.invoke(adapter, args);
          } else {
            return method.invoke(adaptee, args);
          }
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      }

      @Override
      public String toString() {
        StringBuilder s = new StringBuilder();
        // Really simple. May get more flexible later.
        s.append("Adapted: ").append(name);
        return s.toString();
      }
    });
  }

  private static class MethodIdentifier {
    private final String name;
    private final Class[] parameters;

    public MethodIdentifier(Method m) {
      name = m.getName();
      parameters = m.getParameterTypes();
    }

    @Override
    public boolean equals(Object o) {
      // I am always equal to me.
      if (this == o) {
        return true;
      }
      // I cannot be equal to something of a different type.
      if (!(o instanceof MethodIdentifier)) {
        return false;
      }
      MethodIdentifier mid = (MethodIdentifier) o;
      return name.equals(mid.name) && Arrays.equals(parameters, mid.parameters);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
}
 */
