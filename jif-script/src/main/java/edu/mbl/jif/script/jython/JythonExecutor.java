package edu.mbl.jif.script.jython;

/*
 *  JythonExecutor.java - JythonInterpreter Shell
 *  Copyright (C) 10 June 2001 Carlos Quiroz
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.python.core.*;
import org.python.util.*;


/**
 *  The JythonExecutor encapsulates the Jython Interpreter. It sets a thread
 *  where requests are pushed. The output and error stream are taken and
 *  displayed on a Jython Console
 *
 * @author     Carlos Quiroz
 * @version    $Id: JythonExecutor.java,v 1.46 2004/05/05 19:57:46 orutherfurd Exp $
 */
public class JythonExecutor
{
   private static JythonExecutor executor;
   private InteractiveInterpreter interpreter;
   private boolean initialised;
   private boolean useInternalLib = false;
   private PySystemState sys = null;
   private PyStringMap originalLocals = null;

   private void JythonExecutor () {
   }


   public static synchronized JythonExecutor getExecutor () {
      if (executor == null) {
         executor = new JythonExecutor();
      }
      return executor;
   }


   protected synchronized InteractiveInterpreter getInterpreter () {
      if (interpreter == null) {
         // verify if it has been ever initialised
         if (!initialised) {
            // set any custom user properties
            Properties props = new Properties();
//          String key = null;
//          String value = null;
//          props.setProperty(key, value);

            // try to detect if jython is available in the classpath,
            // if not use bundled
            Properties sysProperties = System.getProperties();
            String classpath = sysProperties.getProperty("java.class.path");
            if (classpath != null) {
               // check if jython is in the classpath
               int jpy = classpath.toLowerCase().indexOf("jython.jar");
               if (jpy == -1) {
                  useInternalLib = true;
               }
            }
            // Modify cachedir property if has not been set before
            if (useInternalLib && !props.containsKey("python.cachedir")) {
               props.setProperty("python.cachedir", "cachedir");
            }
            try {
               // Initialize with no args
               InteractiveInterpreter.initialize(sysProperties, props,
                                                 new String[] {""});
            }
            catch (Exception e) {
               System.err.println("Error initializing InteractiveInterpreter: "
                                  + e.getMessage());
            }
            finally {
               initialised = true;
            }
         }

         // adds __main__ module
         PyModule mod = imp.addModule("__main__");

         // if using internal lib add the appropriate jars to sys.path
         PySystemState sys = Py.getSystemState();

         // deal with locals...
         if (originalLocals == null) {
            originalLocals = ((PyStringMap) mod.__dict__);
            originalLocals = originalLocals.copy();
         }

         // create the InteractiveInterpreter
         try {
            if (originalLocals != null && sys != null) {
               interpreter = new InteractiveInterpreter(originalLocals);
            }
            else {
               interpreter = new InteractiveInterpreter();
            }
         }
         catch (Exception e) {
            System.err.println("Not possible to build interpreter");
            e.printStackTrace();
            return null;
         }

         if (useInternalLib) {
            Properties sysProps = System.getProperties();
            if (sysProps.getProperty("python.home") == null) {
               sysProps.put("python.home", ".");
            }
            String pyHome = sysProps.getProperty("python.home");
            System.out.println("python.home = " + pyHome);
            String path = new File(pyHome, "jythonlib.jar").toString();
            sys.packageManager.addJar(path, true);
            sys.path.insert(0, new PyString(path + "/Lib"));
            path = new File(pyHome, "jython.jar").toString();
            sys.packageManager.addJar(path, true);
         }
         interpreter.runsource("import sys");
         /** @todo add java, ij, jif ... */
         //interpreter.runsource("from _ import _");
      }
      return interpreter;
   }
}
