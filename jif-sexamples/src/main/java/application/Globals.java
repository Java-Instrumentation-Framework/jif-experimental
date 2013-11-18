package application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Template for Singleton Class

public class Globals {
    // methods and attributes for Singleton pattern
    
    private Globals() { // private constructor
        initGlobals();
    }
    
    
    static private Globals _instance; // defaults to null
    
    // Singleton getter utilizing Double Checked Locking pattern
    public static Globals getInstance() {
        if (_instance == null) {
            synchronized (Globals.class) {
                if (_instance == null) {
                    _instance = new Globals();
                }
            }
        }
        return _instance;
    }
    
    
//----------------------------------------------------------------
    // methods and attributes for global data
    
    // constructor for global variables
    private void initGlobals() {
        globalInteger = 5;
        globalBoolean = false;
        idCounter = 0;
    }
    
    
    // keep global data private
    private int globalInteger;
    
    public int getGlobalInteger() {
        return globalInteger;
    }
    
    
    public void setGlobalInteger(int value) {
        globalInteger = value;
    }
    
    private boolean globalBoolean;
    
    public boolean isGlobalBoolean() {
        return globalBoolean;
    }
    
    
    public void setGlobalBoolean(boolean value) {
        globalBoolean = value;
    }
    
//----------------------------------------------------------------
    private int idCounter;
    
    synchronized public int getUniqueID() {
        idCounter++;
        return idCounter;
    }
    
    public static void listAll() {
        /** @todo access to private fields ? */
        Field[] flds = Globals.getInstance().getClass().getFields();
        for (int i = 0; i < flds.length; i++) {
            System.out.println(flds[i].getName() +": " +
                    flds[i].toString());
        }
        Method[] meths = Globals.getInstance().getClass().getMethods();
        for (int i = 0; i < meths.length; i++) {
            System.out.println(meths[i].getName() +": " +
                    meths[i].toString());
        }
        
    }
    public static void main(String[] args) {
        System.out.println(Globals.getInstance().getGlobalInteger());
        System.out.println(Globals.getInstance().isGlobalBoolean());
        System.out.println(Globals.getInstance().getUniqueID());
        System.out.println(Globals.getInstance().getUniqueID());
        
        Globals.getInstance().listAll();
        
    }
}
