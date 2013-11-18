package tests.dispatcher;

import com.holub.asynch.Active_object;
import com.holub.asynch.Condition;
import com.holub.asynch.Condition_set;

public class Dispatcher {

    private static Active_object dispatcher = new Active_object();
    

    static {
        dispatcher.start();
    }
    //dispatcher.dispatch(null);
    //dispatcher.join(); // wait for existing request to be processed
    public Dispatcher() {
    }

    public static void doit() {
        dispatcher.dispatch(new Runnable() {

            public void run() {
                /* To Execute... */
                testMethod("hello world");
            }

        });
    }

    public static void doThis(String _s) {
        final Condition godot = new Condition(false);
        final String s = _s;
        dispatcher.dispatch(new Runnable() {
            public void run() {
                //------------------------
                testMethod(s, godot);
            //------------------------
            }
        });
        dispatcher.dispatch(new Runnable() {
            public void run() {
                System.out.println("Another thread runs.");
            }
        });
        System.out.println("waiting for godot...");
        try {
            godot.wait_for_true();
        } catch (Exception ex) {
        }
        System.out.println("Godot is here..");
    }

    public static void doThisMethod(String method, String _s) {
        final Condition godot = new Condition(false);
        final String s = _s;
        dispatcher.dispatch(new Runnable() {
            public void run() {
                //------------------------
                testMethod(s, godot);
            //------------------------
            }
        });
        System.out.println("waiting for testMethod to complete...");
        try {
            godot.wait_for_true();
        } catch (Exception ex) {
        }
        System.out.println("This is done.");
    }

    private static synchronized void testMethod(String m, Condition godot) {
        System.out.println("Start testMethod: " + m);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            System.out.println("Exception on Thread.sleep: " + ex.getMessage());
        }
        // Waits for Completion of Task
        godot.set_true();
        System.out.println("End testMethod: " + m);
    }

    private static void testMethod(String m) {
        System.out.println("Start testMethod: " + m);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            System.out.println("Exception on Thread.sleep: " + ex.getMessage());
        }
        System.out.println("End testMethod: " + m);
    }

    public static void shut_down() {
        dispatcher.close();
        dispatcher = null;
    }

}

