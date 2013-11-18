/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stateset;

// : c14:BeanDumper.java
// Introspecting a Bean.
// From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
// www.BruceEckel.com. See copyright notice in CopyRight.txt.
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BeanDumper extends JFrame {

    private JTextField query = new JTextField(20);
    private JTextArea results = new JTextArea();

    public void print(String s)
      {
        results.append(s + "\n");
      }

    public void dump(Class bean)
      {
        results.setText("");
        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(bean, Object.class);
        } catch (IntrospectionException e) {
            print("Couldn't introspect " + bean.getName());
            return;
        }
        
        PropertyDescriptor[] properties = bi.getPropertyDescriptors();
        print(" Properties =====================");
        for (int i = 0; i < properties.length; i++) {
            Class p = properties[i].getPropertyType();
            if (p == null) {
                continue;
            }
            print(properties[i].getName());
            print("  type:  " + p.getName());
            Method readMethod = properties[i].getReadMethod();
            if (readMethod != null) {
                print("    read method: " + readMethod);
            }
            Method writeMethod = properties[i].getWriteMethod();
            if (writeMethod != null) {
                print("    write method: " + writeMethod);
            }
            print("--------------------------------");
        }
        
        print("Public methods ==================");
        MethodDescriptor[] methods = bi.getMethodDescriptors();
        for (int i = 0; i < methods.length; i++) {
            print(methods[i].getMethod().toString());
        }
        print("======================");
        
        print("Event support:");
        EventSetDescriptor[] events = bi.getEventSetDescriptors();
        for (int i = 0; i < events.length; i++) {
            print("Listener type:\n  " + events[i].getListenerType().getName());
            Method[] lm = events[i].getListenerMethods();
            for (int j = 0; j < lm.length; j++) {
                print("Listener method:\n  " + lm[j].getName());
            }
            MethodDescriptor[] lmd = events[i].getListenerMethodDescriptors();
            for (int j = 0; j < lmd.length; j++) {
                print("Method descriptor:\n  " + lmd[j].getMethod());
            }
            Method addListener = events[i].getAddListenerMethod();
            print("Add Listener Method:\n  " + addListener);
            Method removeListener = events[i].getRemoveListenerMethod();
            print("Remove Listener Method:\n  " + removeListener);
            print("====================");
        }
      }

    class Dumper implements ActionListener {

        public void actionPerformed(ActionEvent e)
          {
            String name = query.getText();
            Class c = null;
            try {
                c = Class.forName(name);
            } catch (ClassNotFoundException ex) {
                results.setText("Couldn't find " + name);
                return;
            }
            dump(c);
          }

    }

    public BeanDumper()
      {
        Container cp = getContentPane();
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Qualified bean name:"));
        p.add(query);
        cp.add(BorderLayout.NORTH, p);
        cp.add(new JScrollPane(results));
        Dumper dmpr = new Dumper();
        query.addActionListener(dmpr);
        query.setText("frogbean.Frog");
        // Force evaluation
        dmpr.actionPerformed(new ActionEvent(dmpr, 0, ""));
      }

    public static void main(String[] args)
      {
        BeanDumper bd = new BeanDumper();
        run(bd, 600, 500);
        String name = "edu.mbl.jif.camera.CameraModel";
        Class c = null;
            try {
                c = Class.forName(name);
            } catch (ClassNotFoundException ex) {
                bd.results.setText("Couldn't find " + name);
                return;
            }
            bd.dump(c);
      }

    public static void run(JFrame frame, int width, int height)
      {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
      }

} ///:~