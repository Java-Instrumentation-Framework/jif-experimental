/**
 * JPythonInterpreterDriver.java
 *
 *
 * Created: Wed Dec 23 16:03:41 1998
 *
 * @author
 * @version
 */

package edu.mbl.jif.script.scripting;

import java.io.File;

import org.python.util.PythonInterpreter;
import org.python.core.*;

import javax.swing.JOptionPane;


public class JPythonInterpreterDriver
      implements InterpreterDriver
{

   private static JPythonInterpreterDriver _instance;

   private PythonInterpreter _interpreter = new PythonInterpreter();

   static {
      // the inizialization is being done in the startup program
      //      Properties props = new Properties();
      //      props.setProperty("python.path", ".;jt400.jar");
      //      PythonInterpreter.initialize(System.getProperties(), props,
      //                        new String[] {""});
      System.setProperty("python.cachedir",
                         System.getProperty("user.home") + File.separator +
                         ".jifJython" +
                         File.separator);
      _instance = new JPythonInterpreterDriver();
      InterpreterDriverManager.registerDriver(_instance);
   }


   public void executeScript (String script) throws InterpreterDriver.
         InterpreterException {
      try {
         _interpreter = new PythonInterpreter();
         _interpreter.exec(script);
      }
      catch (PyException ex) {
         throw new InterpreterDriver.InterpreterException(ex);
      }
   }


   public void executeScriptFile (String scriptFile) throws InterpreterDriver.
         InterpreterException {

      try {
         final String s2 = scriptFile;

         Runnable interpretIt = new Runnable()
         {
            public void run () {
//               PySystemState.initialize(System.getProperties(),null, new String[] {""},this.getClass().getClassLoader());

               _interpreter = new PythonInterpreter();
               //_interpreter.set("_session",s1);
               try {
                  _interpreter.execfile(s2);
               }
               catch (org.python.core.PySyntaxError pse) {
                  //JOptionPane.showMessageDialog(s1,pse,"Error in script " + s2,JOptionPane.ERROR_MESSAGE);
               }
               catch (org.python.core.PyException pse) {
                  //JOptionPane.showMessageDialog(s1,pse,"Error in script " + s2,JOptionPane.ERROR_MESSAGE);
               }
               finally {
                  //s1.setMacroRunning(false);
               }
            }

         };

         // lets start interpreting it.
         Thread interpThread = new Thread(interpretIt);
         interpThread.setDaemon(true);
         interpThread.start();

      }
      catch (PyException ex) {
         throw new InterpreterDriver.InterpreterException(ex);
      }
      catch (Exception ex2) {
         throw new InterpreterDriver.InterpreterException(ex2);
      }
   }


   public String[] getSupportedExtensions () {
      return new String[] {
            "py"};
   }


   public String[] getSupportedLanguages () {
      return new String[] {
            "Python", "JPython"};
   }


   public static void main (String[] args) {
      String script = "import JinSitu\nJinSitu.runshell()";

      try {
         _instance.executeScript(script);
         //_instance.executeScriptFile("test.py");
      }
      catch (Exception ex) {
         System.out.println(ex);
      }
   }
}
