
package edu.mbl.jif.gui;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * JavaP prints structural information about classes. For each class, all public
 * fields and methods are listed. The "Reflection" API is used to look up the
 * information.
 * 
 * @version $Id: MyJavaP.java,v 1.6 2004/03/14 17:45:51 ian Exp $
 */
public class MyJavaP {

  /**
   * Simple main program, construct self, process each class name found in
   * argv.
   */
  public static void main(String[] argv) {
    MyJavaP pp = new MyJavaP();

//    if (argv.length == 0) {
//      System.err.println("Usage: MyJavaP className [...]");
//      System.exit(1);
//    } else
//      for (int i = 0; i < argv.length; i++)
//        pp.doClass(argv[i]);
          String[] clazz = new String[]{"edu.mbl.jif.laser.Surgeon"};
                  pp.doClass(clazz[0]);
  }

  /**
   * Format the fields and methods of one class, given its name.
   */
  protected void doClass(String className) {

    try {
      Class c = Class.forName(className);
      System.out.println(Modifier.toString(c.getModifiers()) + ' ' + c
          + " {");

      int mods;
      Field fields[] = c.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
//        if (!Modifier.isPrivate(fields[i].getModifiers())
//            && !Modifier.isProtected(fields[i].getModifiers()))
       // System.out.println("\t" + fields[i]);
        System.out.println(fields[i].getName() + ", "
            + fields[i].getType().getName() + ", " + Modifier.toString(fields[i].getModifiers()));
       // System.out.println("");
      
      }
      Constructor[] constructors = c.getConstructors();
      for (int j = 0; j < constructors.length; j++) {
        Constructor constructor = constructors[j];
        System.out.println("\t" + constructor);

      }
      Method methods[] = c.getDeclaredMethods();
      for (int i = 0; i < methods.length; i++) {
        if (!Modifier.isPrivate(methods[i].getModifiers())
            && !Modifier.isProtected(methods[i].getModifiers()))
          System.out.println("\t" + methods[i].toGenericString()+ " " );
      }
      System.out.println("}");
    } catch (ClassNotFoundException e) {
      System.err.println("Error: Class " + className + " not found!");
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}