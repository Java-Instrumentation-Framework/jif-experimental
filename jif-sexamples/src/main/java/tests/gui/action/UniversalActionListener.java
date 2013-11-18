package tests.gui.action;
// This example is from the book "Java in a Nutshell, Second Edition".
// Written by David Flanagan.  Copyright (c) 1997 O'Reilly & Associates.
// You may distribute this source code for non-commercial purposes only.
// You may study, modify, and use this example for any purpose, as long as
// this notice is retained.  Note that this example is provided "as is",
// WITHOUT WARRANTY of any kind either expressed or implied.

import java.awt.event.*;
import java.lang.reflect.*;
import java.awt.*;   // Only used for the test program below.

public class UniversalActionListener implements ActionListener {
  protected Object target;
  protected Object arg;
  protected Method m;

  public UniversalActionListener(Object target, String methodname, Object arg)
       throws NoSuchMethodException, SecurityException
  {
    this.target = target;                            // Save the target object.
    this.arg = arg;                                  // And method argument.

    // Now look up and save the Method to invoke on that target object
    Class c, parameters[];
    c = target.getClass();                           // The Class object.
    if (arg == null) parameters = new Class[0];      // Method parameter.
    else parameters = new Class[] { arg.getClass() };
    m = c.getMethod(methodname, parameters);         // Find matching method.
  }

  public void actionPerformed(ActionEvent event) {
    Object[] arguments;
    if (arg == null) arguments = new Object[0];      // Set up arguments.
    else arguments = new Object[] { arg };
    try { m.invoke(target, arguments); }             // And invoke the method.
    catch (IllegalAccessException e) {               // Should never happen.
      System.err.println("UniversalActionListener: " + e);
    } catch (InvocationTargetException e) {          // Should never happen.
      System.err.println("UniversalActionListener: " + e);
    }
  }

  // A simple test program for the UniversalActionListener
  public static void main(String[] args) throws NoSuchMethodException {
    Frame f = new Frame("UniversalActionListener Test");// Create window.
    f.setLayout(new FlowLayout());                      // Set layout manager.
    Button b1 = new Button("tick");                     // Create buttons.
    Button b2 = new Button("tock");
    Button b3 = new Button("Close Window");
    f.add(b1); f.add(b2); f.add(b3);                    // Add them to window.

    // Specify what the buttons do.  Invoke a named method with
    // the UniversalActionListener object.
    b1.addActionListener(new UniversalActionListener(b1, "setLabel", "tock"));
    b1.addActionListener(new UniversalActionListener(b2, "setLabel", "tick"));
    b1.addActionListener(new UniversalActionListener(b3, "hide", null));
    b2.addActionListener(new UniversalActionListener(b1, "setLabel", "tick"));
    b2.addActionListener(new UniversalActionListener(b2, "setLabel", "tock"));
    b2.addActionListener(new UniversalActionListener(b3, "show", null));
    b3.addActionListener(new UniversalActionListener(f, "dispose", null));

    f.pack();                                             // Set window size.
    f.show();                                             // And pop it up.
  }
}
