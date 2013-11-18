/*
 * ClassExposer.java
 * Helps in building .jar file using AutoJar
 * Created on June 7, 2006, 12:57 PM
*/
package edu.mbl.jif;
/**
 * For use with AutoJar
 * This class is used only to indicate to AutoJar where to start in determining
 * which classes to include in the jar file that it generates.
 * components 
 * (See AutoJar docs)
 * @author GBH
 */

/*
AutoJar Notes:

AutoJar generates self-contained jars starting from a given list of classes. It searches the bytecode recursively for referenced classes, extracts the corresponding files from wherever they reside, and creates an archive containing only the classes you really need.

"Autojar helps creating jar files of minimal size from different inputs like own classes, external archives etc. It starts from a given class (e.g., an applet), recursively searches the bytecode for  references to other classes, extracts these classes from the input archives, and copies them to the output. The resulting archive will only contain the classes you really need. Thus you can keep the size 
and loading time of applets low or make applications independent of installed libraries.

In a similar way, autojar can search directories and archives for other resources (like image files), extract them and copy them to the output.

Note that the program can't tell which classes will be loaded dynamically (by Class.forName()). If you know these classes in advance, you can simply provide them on the command line. If you don't, however, autojar may be not suitable."


makeJar.bat 
-----------
java -jar autojar.jar -D -v ^
-c classes;lib/*.jar ^
-o all.jar ^
edu\mbl\jif\camera\camacq\CamAcqTester.class ^
-C classes edu/mbl/jif/camera/icons/*.gif ^
-C classes edu/mbl/jif/gui/imaging/icons/*.gif > autoResult.txt

Run AutoJar with Debug, verbose:
java -jar autojar.jar -D -v ^


Specify the Classpath, which can include wildcards
-c classes;lib/*.jar ^


Name the resulting Output .jar file:
-o all.jar ^


The "main" cls added to the archive. Autojar then scans the bytecode, tries to find all classes that this 
 class uses (directly or indirectly), extracts the corresponding files from their archives and adds them, too.

edu\mbl\jif\camera\camacq\CamAcqTester.class ^


[ -m manifest.mf
Path of the manifest file. The class in the Main-Class entry will be treated like classes given on the command line.
]

Other files to include:

-C classes edu/mbl/jif/camera/icons/*.gif ^

-C classes edu/mbl/jif/gui/imaging/icons/*.gif > autoResult.txt


For ImageJ Plugins...

Since ImageJ plugins are normally not directly referred to by any compiled code,
we need to indicate to AutoJar what to include.
ClassExposer.java -> reference class 
containing reference to any classes that might be called...

This class is actually never instantiated, so it doesn't really matter what it does, 
as long as it makes a reference to any classes that AutoJar would not otherwise find.

*/


public class ClassExposer {
   
   /** Creates a new instance of ClassExposer */
   public ClassExposer() {
      try {
         /*
          * edu/mbl/jif/utils/PSjUtils leads to inclusion of all of PSj.
          */
//         (new edu.mbl.jif.ijplugins.ijWidgets_()).run("");
//         (new edu.mbl.jif.ijplugins.jEdit_()).run("");
//         (new edu.mbl.jif.ijplugins.JavaConsole_()).run("");
//         (new edu.mbl.jif.ijplugins.varius.Varius_()).run("");
//         (new edu.mbl.jif.ijplugins.CamAcqJ_()).run("");
//         (new edu.mbl.jif.ijplugins.stackcalc.StackCalc_()).run("");
//         (new edu.mbl.jif.ijplugins.PSj_()).run("");
//         (new edu.mbl.jif.comm.SerialPlugin_()).run("");
//         (new edu.mbl.jif.ijplugins.ImgDataMgr_()).run("");
         new edu.mbl.jif.gui.ConsoleJava();
            edu.mbl.jif.camacq.CamAcqTester.main(null);
            edu.mbl.jif.camacq.CamAcq.getInstance();
         // de.jppietsch.prefedit.PrefEdit.main(null);
         new com.l2fprod.common.propertysheet.PropertySheetPanel();
         
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
