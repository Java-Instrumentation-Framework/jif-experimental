package edu.mbl.jif.utils.singleton;

import java.util.HashMap;


public class SingletonRegistry
{
   public static SingletonRegistry REGISTRY = new SingletonRegistry();

   private static HashMap map = new HashMap();

   protected SingletonRegistry () { // Exists to defeat instantiation
   }


   public static Object getInstance (String classname) {
      Object singleton = map.get(classname);

      synchronized (map) {
         if (singleton != null) {
            return singleton;
         }
         try {
            singleton = Class.forName(classname).newInstance();
            System.out.println("created singleton: " + singleton);
         }
         catch (ClassNotFoundException cnf) {
            System.err.println("Couldn't find class " + classname);
         }
         catch (InstantiationException ie) {
            System.err.println("Couldn't instantiate an object of type " + classname);
         }
         catch (IllegalAccessException ia) {
            System.err.println("Couldn't access class " + classname);
         }
         map.put(classname, singleton);
      }
      return singleton;
   }


   public static void destroyInstance (String classname) {
      Object singleton = map.get(classname);
      synchronized (map) {
         if (map.containsKey(classname)) {
            System.out.println("destroying singleton: " + classname);
            map.remove(classname);
         } else {
            System.err.println("Error: Attempt to destroy non-existant singleton: "
                  + classname);
         }
      }
   }
}
