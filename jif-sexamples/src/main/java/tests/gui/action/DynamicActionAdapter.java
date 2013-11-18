/*
 * DynamicActionAdapter.java
 */
package tests.gui.action;

//file: DynamicActionAdapter.java
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.awt.event.*;

class DynamicActionAdapter implements ActionListener {

    Hashtable actions = new Hashtable();

    public void hookup(Object sourceObject, Object targetObject, String targetMethod) {
        actions.put(sourceObject, new Target(targetObject, targetMethod));
        invokeReflectedMethod(sourceObject, 
						"addActionListener", 
						new Object[]{this}, 
						new Class[]{ActionListener.class});
    }

    public void actionPerformed(ActionEvent e) {
        Target target = (Target) actions.get(e.getSource());
        if (target == null) {
            throw new RuntimeException("unknown source");
        }
        invokeReflectedMethod(target.object, target.methodName, null, null);
    }

    private void invokeReflectedMethod(
            Object target, String methodName,
            Object[] args, Class[] argTypes) {

        try {
            Method method = target.getClass().getMethod(methodName, argTypes);
            method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("invocation problem: " + e);
        }
    }

    class Target {

        Object object;
        String methodName;

        Target(Object object, String methodName) {
            this.object = object;
            this.methodName = methodName;
        }
    }
}
