package tests.DynProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
 * from Implementing Active Objects with Java Dynamic Proxies
 * http://benpryor.com/blog/page/4/?/archives/16-API-Design-The-Principle-of-Audience_html
 */
public class ActiveObjectProxy implements InvocationHandler {

    private List callQueue = new ArrayList();
    private Object serviceObject;

    public ActiveObjectProxy(Object serviceObject) {
        this.serviceObject = serviceObject;
        new WorkerThread().start();
    }

    private synchronized void put(Invokable invokable) {
        callQueue.add(invokable); 
        notifyAll();
    }

    private synchronized Invokable get() {
        while (callQueue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return (Invokable) callQueue.remove(0);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invokable invokable = new Invokable();
        invokable.method = method;
        invokable.args = args;
        put(invokable);
        return null;
    }

    private class WorkerThread extends Thread {
        public void run() {
            while (true) {
                Invokable invokable = get();
                try {
                    invokable.method.invoke(serviceObject, invokable.args);
                } catch (Throwable t) {
                }
            }
        }
    }

    private static class Invokable {

        public Method method;
        public Object[] args;
    }
}

