package edu.mbl.jif.utils.classloaders;

import java.util.*;

/**
 * A utility for predictively resolving classes
 * This can be used to help offset one of the biggest detractors from
 * the Java language:  Class loading delays.
 * Creation date: (12/20/99 3:07:46 PM) @author: Jason Marshall
 **/

public class BackgroundClassLoader
        implements Runnable
{
    /**
     * Thread used to load classes in the background
     **/

    private static Thread controlThread;

    /**
     * Storage for the class names still to be resolved
     **/

    private static Vector queue;

    static {
        try {
            // This is an IBM-specific hack that prevents a nasty
            // error in the IBM resource lookup code -JDM
            java.text.DateFormat.getDateInstance();
        }
        catch (Exception e) {
        }
        queue = new Vector();
    }


    /**
     * Private constructor, to enforce singleton status
     * Creation date: (12/20/99 3:10:32 PM)
     **/

    private BackgroundClassLoader () {
    }


    /**
     * Enqueue a class for background loading
     * Creation date: (12/20/99 3:23:28 PM)
     * @param className java.lang.String
     **/

    public static void enqueueClass (String className) {
        synchronized (queue) {
            queue.addElement(className);
            /*mjr: addElement adds to the _end_ of the Vector.
                  I think you want to add to the front.  Here's
                  why:
                      1) Assume we go so some screen, say Screen1
                      2) From screen 1, we start loading Screen2
                         because we expect Screen2 to be loaded next.
                      3) Screen2 adds some classes it needs to the
                         vector.  See you Screen3 example for this.
                      4) Now, assume that the user _doesn't_ go to
                         Screen2, but instead goes somewhere else,
                         say ScreenA.
                         We probably want the stuff we expect to
                         follow ScreenA, say ScreenB, to take
                         priority over the Screen2 preloads.
                  As written Screen2 will finish its background
                  loads before ScreenB gets to start its background
                  loads.  I think this is wrong. If _I_ am wrong,
                  a comment explaining why would be good.*/

            if (controlThread == null) {
                start();
            }
            else {
                queue.notify();
            }
        }
    }


    /**
     * Start loading
     * Creation date: (12/20/99 3:15:29 PM)
     **/

    public void run () {
        if (Thread.currentThread() != controlThread) {
            return;
        }
        Hashtable done = new Hashtable();
        while (controlThread.isAlive()) {
            String className;
            synchronized (queue) {
                if (queue.isEmpty()) {
                    try {
                        queue.wait();
                    }
                    catch (InterruptedException ie) {
                    }
                }
                if (!queue.isEmpty()) {
                    className = (String) queue.elementAt(0);
                    queue.removeElementAt(0);
                }
                else {
                    continue;
                }
            }
            if (!done.containsKey(className)) {
                try {
                    Class resolved = Class.forName(className);
                    done.put(resolved.getName(), resolved);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            className = null;
        }
        controlThread = null;
    }


    /**
     * Insert the method's description here.
     * Creation date: (4/5/00 3:24:04 PM)
     */

    private static void start () {
        controlThread = new Thread(new BackgroundClassLoader());
        try {
            controlThread.setName("BackgroundLoader");
            controlThread.setPriority((Thread.MIN_PRIORITY +
                                       Thread.NORM_PRIORITY) /
                                      2);
        }
        catch (Exception e) {
        }
        controlThread.start();
    }
}
