/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author GBH
 */
public class TestCmdProcessBldr {

   static String baseDir =    "G:/SourceCode/ops/openpolscope/";
   static String[][] projects = new String[][]{
      //{"directory"              ,"artifactId"	         }
      {"polacquisition", "polacquisition"},
      {"polanalyzer", "polanalyzer"},
      {"compatibilitymmj1413", "compatibilitymmj1413"},
      {"plugin/orientation3", "orientation3"},
      {"plugin/orientation4", "orientation4"},
      {"plugin/post/colormap", "post-colormap"},
      {"plugin/post/exportview", "post-exportview"},
      {"plugin/post/imagejmacro", "post-imagejmacro"},
      {"plugin/post/matlabdemoplugin", "post-matlabdemoplugin"},
      {"plugin/pre/imagejmacro", "pre-imagejmacro"},
      {"plugin/pre/matlabdemoplugin", "pre-matlabdemoplugin"},
      {"plugin/pre/ratioing", "pre-ratioing"},
      {"plugin/proc/birefringence", "proc-birefringence"},
      {"plugin/proc/demoalgoplugin", "proc-demoalgoplugin"},
      {"plugin/proc/dichroism", "proc-dichroism"},
      {"plugin/proc/fluorpol", "proc-fluorpol"},
      {"plugin/proc/rtpol", "proc-rtpol"},
      {"api/psplugins", "api-psplugins"}
   };

   public static void main(String args[])
           throws InterruptedException, IOException {
      for (int i = 0; i < projects.length; i++) {


         String dir = projects[i][0];
         String artifactId = projects[i][1];

         // generate from Archetype...
         String mavenExePath =
                 "C:/Program Files/Apache Software Foundation/apache-maven-3.0.3/bin/mvn.bat";

         // How to... Invoke Window batch file from Java

         List<String> command = new ArrayList<String>();
         command.add(System.getenv("windir") + "\\system32\\" + "tree.com");
         command.add("cmd.exe");
         command.add("/c");
         command.add(mavenExePath);
         command.add("archetype:generate");
         command.add("-DarchetypeArtifactId=openpolscope-archetype");
         command.add("-DinteractiveMode=false");
         command.add("-DgroupId=org.openpolscope");
         command.add("-DartifactId=" + artifactId);
         //
         ProcessBuilder builder = new ProcessBuilder(command);
         Map<String, String> environ = builder.environment();
         builder.directory(new File(baseDir + dir));

         System.out.println("Directory : " + System.getenv("temp"));
         final Process process = builder.start();
         InputStream is = process.getInputStream();
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr);
         String line;
         while ((line = br.readLine()) != null) {
            System.out.println(line);
         }
         System.out.println("Program terminated!");
      }
   }
}
