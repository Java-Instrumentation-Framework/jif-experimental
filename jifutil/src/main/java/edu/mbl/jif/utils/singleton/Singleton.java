package edu.mbl.jif.utils.singleton;

public class Singleton
{

   protected Singleton () { // Exists only to thwart instantiation
   }


   public static Singleton getInstance (String classname) {
      return (Singleton) SingletonRegistry.REGISTRY.getInstance(classname);
   }


   public static void destroyInstance (String classname) {
      SingletonRegistry.REGISTRY.destroyInstance(classname);
   }

}
