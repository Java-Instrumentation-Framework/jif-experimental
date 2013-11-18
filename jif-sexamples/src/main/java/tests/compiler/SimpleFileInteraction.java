/*
 * SimpleFileInteraction.java
 *
 * Created on May 24, 2007, 1:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.compiler;

 import java.io.File;
 import java.io.Writer;
 
 import javax.tools.JavaCompiler;
 import javax.tools.JavaFileManager;
 import javax.tools.JavaFileObject;
 import javax.tools.ToolProvider;
 
 /**
  * @author Tom
  * 
  */
 public class SimpleFileInteraction {
 
 	/**
 	 * @param args
 	 */
// 	public static void main(String[] args) {
// 		JavaCompiler javaCompilerTool = ToolProvider.getSystemJavaCompiler();
// 		JavaFileManager javaFileManager = javaCompilerTool
// 				.getStandardFileManager();
// 		try {
// 			JavaFileObject javaFileObject = javaFileManager
// 				    .getFileForInput("bin/Tutorials.java");
// 
// 			Writer writer = javaFileObject.openWriter();
// 			writer.write("public class Tutorials{"
// 				    + "public static void main(String[] args){"
// 				    + "System.out.println(\"We love JavaLobby :)\");"
// 					+ "}" + "}");
// 			writer.close();
// 			
// 			javaCompilerTool.setOutputDirectory(new File("bin"));
// 		    System.out.println(javaCompilerTool.run(null,new JavaFileObject[]{javaFileObject}).getDiagnostics());
// 
// 			Class.forName("Tutorials").getDeclaredMethod("main",
// 				    new Class[] { String[].class }).invoke(null,
// 				    new Object[] { null });
// 
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		}
// 	}
 }