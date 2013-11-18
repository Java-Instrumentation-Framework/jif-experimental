package edu.mbl.jif;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * In lieu of a 'real' mocking framework, this is a simple mocker that just
 * outputs the interface and method that was called, its arguement values
 * and the return type.
 *
 *  Example usage: Simply,
 *
 *  __Interface proxyStub = MockProxyStub.getProxy(__Interface.class);
 *
 * This evolved from this,
InvocationHandler handler = new ProxyStubHandler();
__Interface proxy = (__Interface) Proxy.newProxyInstance(
__Interface.class.getClassLoader(),
new Class[]{__Interface.class},
handler);
 * ... to this...
__Interface proxy = (__Interface) Proxy.newProxyInstance(
__Interface.class.getClassLoader(),
new Class[]{__Interface.class},
new ProxyStubHandler());
 *
 * ... then ... as is, using generics in the getProxy.
 * July 1, 2009
 * @author GBH
 * 
 *  Also see: http://butterfly.jenkov.com/testing-tools/index.html#mock
 */

public class MockProxyStub implements InvocationHandler {

  public static <T> T getProxy(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
            new Class[]{clazz},
            new MockProxyStub());
  }
  List<String> overriddenMethods = new ArrayList<String>();

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
          throws Throwable {

    System.out.print("<mock> :: " + proxy.getClass().getInterfaces()[0] + " # " + method.getName() + "(");
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        Object object = args[i];
        System.out.print(" " + object);
      }
    }
    System.out.print(") ");
    for (String overriddenMethod : overriddenMethods) {
      if (method.getName().equalsIgnoreCase(overriddenMethod)) {
      }

    }
    if (char.class.isAssignableFrom(method.getReturnType())) {
      System.out.println(" > char ");
      return 'c';
    }
    if (int.class.isAssignableFrom(method.getReturnType())) {
      System.out.println(" > int ");
      return 1;
    }
    if (float.class.isAssignableFrom(method.getReturnType())) {
      System.out.println(" > float ");
      return 1.2;
    }
    if (double.class.isAssignableFrom(method.getReturnType())) {
      System.out.println(" > double ");
      return 2.1;
    }
    if (boolean.class.isAssignableFrom(method.getReturnType())) {
      System.out.println(" > boolean ");
      return true;
    }
    if (String.class.isAssignableFrom(method.getReturnType())) {
      System.out.println(" > string ");
      return "A String";
    }
    System.out.println("> " + method.getReturnType());
    return null;
  }
}
