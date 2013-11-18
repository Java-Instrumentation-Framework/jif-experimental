package edu.mbl.jif.utils.exec;

import java.io.*;

// ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start notepad.exe");
// Process process = builder.start();


// Executes external program under the OS
public class ExecutorExternal
{
   public ExecutorExternal (String command) {
      try {
         Runtime rt = Runtime.getRuntime();
         Process process = rt.exec(command);
         InputStreamReader reader = new InputStreamReader(process.getInputStream());
         BufferedReader bufReader = new BufferedReader(reader);
         String line;
         while ((line = bufReader.readLine()) != null) {
            System.out.println(line);
         }
      }
      catch (IOException e) {
         System.out.println(e);
      }
   }

}
