package edu.mbl.jif.utils.singleton;

/**
 *
 * @author GBH
 */

 public class SingletonWithHolder {
   // Protected constructor is sufficient to suppress unauthorized calls to the constructor
   protected SingletonWithHolder() {}
 
   /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
   private static class SingletonHolder { 
     private final static SingletonWithHolder INSTANCE = new SingletonWithHolder();
   }
 
   public static SingletonWithHolder getInstance() {
     return SingletonHolder.INSTANCE;
   }
   
   
   public static void main(String[] args) {
         SingletonWithHolder.getInstance();
     }
 }