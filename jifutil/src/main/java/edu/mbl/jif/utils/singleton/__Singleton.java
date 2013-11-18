package edu.mbl.jif.utils.singleton;

// Template for Singleton Class pattern

public class __Singleton
{

// include in singleton class >>>
   static private __Singleton _instance; // defaults to null

   private __Singleton () { // private constructor
      // initialization...
      initGlobals();
   }


   // Singleton getter utilizing Double Checked Locking pattern
   public static __Singleton getInstance () {
      if (_instance == null) {
         synchronized (__Singleton.class) {
            if (_instance == null) {
               _instance = new __Singleton();
            }
         }
      }
      return _instance;
   }


   public static void destroyInstance () {
      synchronized (__Singleton.class) {
         _instance = null;
      }
   }


   // <<< include in singleton class

//----------------------------------------------------------------
   // methods and attributes for global data

   // constructor for global variables
   private void initGlobals () {
      globalInteger = 5;
      globalBoolean = false;
      idCounter = 0;
   }


   // keep global data private
   private int globalInteger;

   public int getGlobalInteger () {
      return globalInteger;
   }


   public void setGlobalInteger (int value) {
      globalInteger = value;
   }


   private boolean globalBoolean;

   public boolean isGlobalBoolean () {
      return globalBoolean;
   }


   public void setGlobalBoolean (boolean value) {
      globalBoolean = value;
   }


   //--------------------------------------------
   private int idCounter;

   synchronized public int getUniqueID () {
      idCounter++;
      return idCounter;
   }


//----------------------------------------------------------------

   public static void main (String[] args) {
      __Singleton single = __Singleton.getInstance();

      System.out.println(single.getGlobalInteger());
      single.setGlobalInteger(99);
      System.out.println(single.getGlobalInteger());
      System.out.println(single.isGlobalBoolean());
      single.setGlobalBoolean(true);
      System.out.println(single.isGlobalBoolean());
      System.out.println(single.getUniqueID());
      System.out.println(single.getUniqueID());
   }
}
/*
// With self-destruction on exit.

 public class S {
    private static S instance = new S();

    static {
        Runnable r = new Runnable() {
            public void run() {
                destroyInstance();
            }
        };
        Runtime.getRuntime().addShutdownHook(new Thread(r));
    }

    private S() {}

    public static synchronized S getInstance() {
        return instance;
    }

    private static void destroyInstance() {
        S old;
        synchronized (S.class) {
            old = instance;
            instance = null;
        }
        //your teardown code here
        System.out.println("one last " + old.beep());
    }

    public String beep() {
        return "beep";
    }

    public static void main(String[] args) {
       S s = S.getInstance();
       System.out.println(s.beep());
    }
 }
 */
