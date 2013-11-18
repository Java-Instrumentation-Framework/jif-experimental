/*
 * JavaCompilerAPI.java
 *
 * Created on March 26, 2007, 3:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.compiler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


/**
 *
 * @author GBH
 */
public class JavaCompilerAPI {
    /**
     * Creates a new instance of JavaCompilerAPI
     */
    public JavaCompilerAPI() {
    }
    
    public void test() {
        String outputDirStr = "c:/bin";
        String javaFileStr = "c:/src/com/juixe/Entity.java";
        
        
        // Using the Java Compiler API
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager sjfm = jc.getStandardFileManager(null, null, null);
        
        /* The next step is to create a compilation task to the Java Compiler. You can add as
         many task to the Java compiler as you may need. Also note that the getJavaFileObjects
         method accepts a variable number of arguments so you could pass in as many files as
         you may need. Also, don't forget to close the Java File Manager when you are done with it.*/
        
        File javaFile = new File(javaFileStr);
        
        // getJavaFileObjects' param is a vararg
        Iterable fileObjects = sjfm.getJavaFileObjects(javaFile);
        jc.getTask(null, sjfm, null, null, null, fileObjects).call();
        
        // Add more compilation tasks...
        try {
            
            sjfm.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        /* At this point with the above code, if there are no errors the class files would
         compile to c:/src. If you want to output the class files in a different location
         you can add options to the compilation task with code similar to the following. */
        
        String[] options = new String[] { "-d", "c:/bin" };
        jc.getTask(null, sjfm, null, Arrays.asList(options), null, fileObjects).call();
        
        /* Once you have compiled the Java source file using the new Java Compiler API,
         you may want to load the class into your running system. To load a Java class compiled
         using the Compiler API you can use code similar to that used to load a class compiled
         using embedded Groovy. Here is how you can load a newly compiled class at runtime.*/
        
        File outputDir = new File(outputDirStr);
        URL[] urls;
        try {
            urls = new URL[] {outputDir.toURL()};
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URLClassLoader ucl = new URLClassLoader(urls, cl);
            Class clazz = ucl.loadClass("com.juixe.Entity");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}
