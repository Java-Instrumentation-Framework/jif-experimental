/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.maven;


import edu.mbl.jif.utils.FileUtil;
import edu.mbl.jif.utils.file.DeepFileSet;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * POMitizer.java Maven Project Converter
 *
 * Moves all non-java files out of src/main to src/resources in a parallel directory tree.
 *
 *
 * replicate src dir structure to dest
 *
 * .java, *.form to src/main/java
 *
 * not .java to src/main/resources
 *
 * @author GBH
 */
public class Pomitizer {

   static String[][] projects = new String[][]{
      {"C:/GBH_Projs/ImgNav",
      
      "C:/GitHub/jif/imagenavigator"}
   };
   
   static String[][] TheRealprojects = new String[][]{
      {"C:/openpolscope/trunk/PolAcquisition",                        "polacquisition"}};
//           ,
//      {"C:/openpolscope/trunk/PolAnalyzer_",                          "polanalyzer"},
//      {"C:/openpolscope/trunk/PolScopeAPI/CompatibilityMMJ1413",      "compatibilitymmj1413"},     
//      {"C:/openpolscope/trunk/PolScopeAPI/Orientation_LinesV3_",      "plugin/orientation3"},     
//      {"C:/openpolscope/trunk/PolScopeAPI/Orientation_LinesV4_",      "plugin/orientation4"},     
//      {"C:/openpolscope/trunk/PolScopeAPI/PostProc_ColorMap",         "plugin/post/colormap"},        
//      {"C:/openpolscope/trunk/PolScopeAPI/PostProc_ExportView",       "plugin/post/exportview"},      
//      {"C:/openpolscope/trunk/PolScopeAPI/PostProc_ImagejMacro",      "plugin/post/imagejmacro"},     
//      {"C:/openpolscope/trunk/PolScopeAPI/PostProc_MatlabDemoPlugin", "plugin/post/matlabdemoplugin"},
//      {"C:/openpolscope/trunk/PolScopeAPI/PreProc_ImagejMacro",       "plugin/pre/imagejmacro"},      
//      {"C:/openpolscope/trunk/PolScopeAPI/PreProc_MatlabDemoPlugin",  "plugin/pre/matlabdemoplugin"}, 
//      {"C:/openpolscope/trunk/PolScopeAPI/PreProc_Ratioing",          "plugin/pre/ratioing"},         
//      {"C:/openpolscope/trunk/PolScopeAPI/Proc_Birefringence_",       "plugin/proc/birefringence"},      
//      {"C:/openpolscope/trunk/PolScopeAPI/Proc_DEMOAlgoPlugin_",      "plugin/proc/demoalgoplugin"},     
//      {"C:/openpolscope/trunk/PolScopeAPI/Proc_Dichroism_",           "plugin/proc/dichroism"},          
//      {"C:/openpolscope/trunk/PolScopeAPI/Proc_FluorPol_",            "plugin/proc/fluorpol"},           
//      {"C:/openpolscope/trunk/PolScopeAPI/Proc_RTPol_",               "plugin/proc/rtpol"},              
//      {"C:/openpolscope/trunk/PolScopeAPI/PsPluginsAppApi",           "api/psplugins"}
//      };
   
   public static void main(String[] args) {

      String destJava = "/src/main/java/";
      String destResources = "/src/main/resources/";
      
      // Destination for mavenized projects
      //String baseDest = "C:/ops/openpolscope/";
      String baseDest = "";

            
      // for each project
      for (int i = 0; i < projects.length; i++) {

         
         String source = projects[i][0];
         String destProj = projects[i][1];
         

         //
         String dest = baseDest + destProj;
         System.out.println("\nsource: " + source);
         System.out.println("dest:   " + dest);
         
         DeepFileSet deepFileSet = new DeepFileSet(source + "/src/*.*");
         for (File f : deepFileSet) {
            System.out.println(f.getParent() + " " + f.getName());
            String extension = FileUtil.getFileExtension(f.getName(), false);
            String path = f.getParent();
            String base = source + "/src";
            String packagePath = new File(base).toURI().relativize(new File(path).toURI()).getPath();
            //System.out.println("Relative: " + packagePath);
            if (extension.equalsIgnoreCase("java") || extension.equalsIgnoreCase("form")) {
               //System.out.println("   " + extension + "  >>> is Java");
               copyFromTo(f, dest + destJava + packagePath + f.getName());
            } else {
               //System.out.println("   " + extension + "  >>> is Other");
               copyFromTo(f, dest + destResources + packagePath + f.getName());
            }
         }
      }
   }

   public static void copyFromTo(File file, String dest) {
      System.out.println("Copying " + file.getAbsolutePath() + "\n   >>>  " + dest + "\n");
      File destFile = new File(dest);
      //System.out.println("destFile.getParent() = " + destFile.getParent());
      assureExistsDir(destFile.getParent());
      try {      
         FileUtil.copyFile(file, destFile, true);
      } catch (IOException ex) {
         Logger.getLogger(Pomitizer.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }

   static void assureExistsDir(String path) {
      // does the path exist already, if not, create it.
      File f = new File(path);
      if (f.exists()) {
         return;
      } else {
         System.out.println("Created folder: " + f.getAbsolutePath());
         f.mkdirs();
      }
   }
   
}
