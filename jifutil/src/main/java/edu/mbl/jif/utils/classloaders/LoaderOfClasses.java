package edu.mbl.jif.utils.classloaders;

import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LoaderOfClasses
{

   public LoaderOfClasses () {
   }


   Class aClass = (new SimpleClassLoader()).getClass();

   public void testClassLoader () {
      ClassLoader cl = aClass.getClassLoader();
      while (cl != null) {
         System.out.println(cl);
         cl = cl.getParent();
      }
      try {
         System.out.println(aClass.getProtectionDomain().getCodeSource().getLocation());
      }
      catch (Exception e) {}

   }


   /*
     public void explicitLoading() {
       URL url = new File("classes").toURL();
       URL[] urls = new URL[] {
           url};
       ClassLoader ucl = new URLClassLoader(urls);
       Class cls = ucl.loadClass("BaseballTeam");
     }
    */

   // Using the thread context loader
   /*
        Vehicle getVehicle(String name) {
        ClassLoader cl = Thread.currentThread().\
        getContextClassLoader();
        Class cls = cl.loadClass(name);
        Object o = cls.newInstance();
        return (Vehicle) o;
      }
    */


   // To Modify the classpath...

   public void addURLToClassPath (URL url) {
      System.out.println("Adding " + url.toString() + " to CLASSPATH");
      System.out.println("Before:" + System.getProperty("java.class.path"));
      try {
         URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().
               getContextClassLoader();
         Class c = Class.forName("java.net.URLClassLoader");
         Class[] parameterTypes = new Class[1];
         parameterTypes[0] = Class.forName("java.net.URL");
         Method m = c.getDeclaredMethod("addURL", parameterTypes);
         m.setAccessible(true);
         Object[] args = new Object[1];
         args[0] = url;
         m.invoke(urlClassLoader, args);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println("After:" + System.getProperty("java.class.path"));
   }


   void useJarJarClassLoader () {
      try {
         JarJarClassLoader jjcl = new JarJarClassLoader();
         jjcl.addInternalJar("foo.jar");
         Class theClass = Class.forName("className", true, jjcl);
         Object theInstance = theClass.newInstance();
      }
      catch (Exception ex) {
      }
   }


   public static void main (String[] args) {
      LoaderOfClasses l = new LoaderOfClasses();
      l.testClassLoader();

   }
}
