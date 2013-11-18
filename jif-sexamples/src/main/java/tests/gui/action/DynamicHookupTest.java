/*
 * DynamicHookupTest.java
 *
 * Created on August 10, 2006, 8:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.action;

import java.awt.*;
import java.awt.event.*;

import java.lang.reflect.Method;

import java.util.Hashtable;


public class DynamicHookupTest extends java.applet.Applet {
    DynamicActionAdapterX actionAdapter = new DynamicActionAdapterX();

    public void init() {
        Button launchButton = new Button("Launch!");
        actionAdapter.hookup(launchButton, this, "launchTheMissiles");
        add(launchButton);
        Button recallButton = new Button("Recall");
        actionAdapter.hookup(recallButton, this, "recallTheMissiles");
        add(recallButton);
    }

    public void launchTheMissiles() {
        System.out.println("Fire...");
    }
    public void recallTheMissiles() {
        System.out.println("Stop, don't do it...");
    }
}


class DynamicActionAdapterX implements ActionListener {
    Hashtable actions = new Hashtable();

    public void hookup(Object sourceObject, Object targetObject, String targetMethod) {
        actions.put(sourceObject, new Target(targetObject, targetMethod));
        invokeReflectedMethod(
                sourceObject,
                "addActionListener",
                new Object[] { this },
                new Class[] { ActionListener.class });
    }

    public void actionPerformed(ActionEvent e) {
        Target target = (Target) actions.get(e.getSource());
        if (target == null) {
            throw new RuntimeException("unknown source");
        }
        invokeReflectedMethod(target.object, target.methodName, null, null);
    }

    private void invokeReflectedMethod(Object target, String methodName, Object[] args,
        Class[] argTypes) {
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
