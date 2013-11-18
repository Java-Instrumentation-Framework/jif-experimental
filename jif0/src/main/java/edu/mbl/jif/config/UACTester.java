
package edu.mbl.jif.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * User Access Control on Win 7
 * FROM:
 * http://mark.koli.ch/2009/12/uac-prompt-from-java-createprocess-error740-the-requested-operation-requires-elevation.html
 */
public class UACTester {
  
  private static final String UAC_EXAMPLE_EXE = "uac-example.exe";

  public static void main(String[] args) {

    final File uacExample = new File(UAC_EXAMPLE_EXE);
    File workingDir;
    
    try {
      workingDir = new File(args[0]);
    } catch ( Exception e ) {
      workingDir = new File(".");
    }
    
    try {
      // Build the command list to be given to the ProcessBuilder
      final List<String> cmdArgs = new ArrayList<String>();
      cmdArgs.add(uacExample.getAbsolutePath());
      cmdArgs.add(workingDir.getAbsolutePath());

      // Create a process, and start it.
      final ProcessBuilder p = new ProcessBuilder(cmdArgs);
      p.directory(new File("."));
      p.start();
    } catch (Throwable t) {
      t.printStackTrace(System.out);
    }
    
  }

}