package tests.DynProxy.ProxyStub.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import tests.DynProxy.ProxyStub.CUT;
import tests.DynProxy.ProxyStub.MyDAO;
import tests.DynProxy.ProxyStub.MyVO;


public class MyTest  {
    private MyDAO stubDAO = StubWrapper.wrap(MyDAO.class, new StubDAO());
    private boolean daoCalled;
    
    public void testMyClassUnderTest() {
        CUT cut = new CUT();
        cut.setDao(stubDAO);
        cut.doSomething();
        
    }
    
    // no need to implement the interface; we get here via a proxy
    private class StubDAO{
        public Collection<MyVO> findSomeStuff() {
            daoCalled = true;
            MyVO vo = new MyVO();
            vo.setSomeAttribute("someValue");
            ArrayList<MyVO> l = new ArrayList<MyVO>();
            l.add(vo);
            return l;
        }
    }

    private static class StubWrapper implements InvocationHandler {
        private Object wrappedStub;
        @SuppressWarnings("unchecked")
        private Class clazz;

        public StubWrapper(Object wrappedStub) {
            super();
            this.wrappedStub = wrappedStub;
            this.clazz = wrappedStub.getClass();
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            try {
                return clazz.getMethod(method.getName(),
                        method.getParameterTypes()).invoke(wrappedStub, args);
            } catch (Throwable t) {
                t.printStackTrace();
                System.out.println("Stub method invocation failed");
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T wrap(Class<T> type, Object stub) {
            return (T) Proxy.newProxyInstance(StubWrapper.class
                    .getClassLoader(), new Class[] { type }, new StubWrapper(
                    stub));
        }
    }

    public static void main(String[] args) {
      (new MyTest()).testMyClassUnderTest();

  }
}
