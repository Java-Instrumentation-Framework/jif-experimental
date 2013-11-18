package edu.mbl.jif.script.jython;

/**
 * <p>Title: JythonEmbeddedInterpreter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author gbh
 * @version 1.0
 */

import java.util.*;
import org.python.util.*;
import org.python.core.*;
import java.util.Properties;


public class JythonEmbeddedInterpreter
      extends PythonInterpreter
{

   private static String scriptPath = "..\\IJ134a\\scripts";
   private static String homePath = "lib\\jython";

   //PythonInterpreter interp;

   public JythonEmbeddedInterpreter (String[] sysArgv) {
      super();
      PySystemState.initialize();
      Properties props = new Properties();
      props.setProperty("python.home", homePath);
      // postProperties  Values like python.home, python.path and all other
      // values from the registry files can be added to this property set.
      // PostProperties will override system properties and registry properties.
      // sysArgv - Command line argument. These values will assigned to sys.argv.
      initialize(System.getProperties(), props, sysArgv);
      System.out.println("JythonEmbeddedInterpreter initialized.");
      // set out and err...

   }


   public void initializeDictionary () {
      PyDictionary dict = new PyDictionary();
      dict.__setitem__(new PyString("zero"), new PyInteger(0));
      dict.__setitem__(new PyString("one"), new PyInteger(1));
      set("dict", dict);
      execStatement("print dict");
      execStatement("print dict['zero'] + dict['one']");
   }


   // Execute Script from a file
   // Catches an Exception if Thrown by a Script
   public void executeScript (String scriptPath) {
      try {
         execfile(scriptPath);
      }
      catch (PyException e) {
         System.err.println(
               "PyException thrown in executeScript: " + scriptPath);
         handlePyException(e);
      }
   }


   public void execStatement (String statement) {
      try {
         exec(statement);
      }
      catch (PyException e) {
         System.err.println(
               "PyException thrown in execStatement: " + statement);
         handlePyException(e);
      }
   }


   void handlePyException (PyException e) {
      PyObject type = e.type;
      PyObject value = e.value;
      PyTraceback traceback = e.traceback;
      System.out.println("  " + traceback.dumpStack());
      System.out.println("  type: " + type);
      System.out.println("  value: " + value);
      System.out.println("  traceback: " + traceback);
      cleanup();
   }


   public void listProperties () throws PyException {
      execStatement("import sys");
      execStatement("print"); // Add empty line for clarity
      execStatement("print 'sys.prefix=', sys.prefix");
      execStatement("print 'sys.argv=', sys.argv");
      execStatement("print 'sys.path=', sys.path");
      execStatement("print 'sys.cachedir=', sys.cachedir");
      execStatement("print"); // Another blank for clarity
   }


   // Access the global namespace in the interpreter
   public void accessGlobalNamespace () {
      PyStringMap locals = (PyStringMap) getLocals();
      PyObject keys = locals.keys();
      PyObject key;
      for (int i = 0; (key = keys.__finditem__(i)) != null; i++) {
         System.out.print("key: " + key + " ");
         System.out.println(locals.__finditem__(key));
      }
   }

   // Open the JinSitu Editor/Debugger
   public void openJinsitu () {
      execStatement("import sys");
      execStatement("sys.path.append(\"scripts/jinsitu\")");
      execStatement("import JinSitu");
      execStatement("JinSitu.runshell()");
   }


   public static void main (String[] args) {
      JythonEmbeddedInterpreter ji = new JythonEmbeddedInterpreter(null);
      ji.listProperties();
      System.out.println("locals: ");
      ji.accessGlobalNamespace();
      ji.execStatement("dir(sys)");
      //ji.executeScript(scriptPath + "jyinfo.py");
      ji.openJinsitu();
   }
}
