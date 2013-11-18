/*
 * TestAppHarness.java
 *
 * Created on October 13, 2006, 3:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.test;

import edu.mbl.jif.utils.diag.CheckThreadViolationRepaintManager;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
//import org.apache.ecs.xhtml.frame;

/**
 *
 * @author GBH
 */
public class TestAppHarness implements Thread.UncaughtExceptionHandler {
    // Exception that has been thrown. This is used to track if an exception
    // is thrown while alerting the user to the current exception.
    private Throwable throwable = null;
    
    /** Creates a new instance of TestAppHarness */
    public TestAppHarness(JFrame frame) {
        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Invoked when an uncaught exception is encountered.  This invokes
     * the method of the same name with the calling thread as an argument.
     *
     * @param throwable the thrown exception
     */
    public void uncaughtException(Throwable throwable) {
        uncaughtException(Thread.currentThread(), throwable);
    }
    
    
    /**
     * Invoked when an uncaught exception is encountered.  This will
     * show a modal dialog alerting the user, and exit the app. This does
     * <b>not</b> invoke <code>exit</code>.
     *
     * @param thread the thread the exception was thrown on
     * @param throwable the thrown exception
     * @see #getUncaughtExceptionDialog
     */
    public void uncaughtException(Thread thread, final Throwable throwable) {
        synchronized (this) {
            if (this.throwable != null) {
                // An exception has occured while we're trying to display
                // the current exception, bale.
                System.err.println("exception thrown while altering user");
                throwable.printStackTrace();
                System.exit(1);
            } else {
                this.throwable = throwable;
            }
        }
        throwable.printStackTrace();
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Handler());
        } else {
            uncaughtException0();
        }
    }
    
    
    /**
     * Returns the dialog that is shown when an uncaught exception is
     * encountered.
     *
     * @see #uncaughtException
     * @return dialog to show when an uncaught exception is encountered
     */
    protected JDialog getUncaughtExceptionDialog() {
        // PENDING: this needs to be localized.
        JOptionPane optionPane =
                new JOptionPane(
                "An unrecoverable error has occured. Will now exit",
                JOptionPane.ERROR_MESSAGE);
        return optionPane.createDialog(null, "Error");
    }
    
    
    private void uncaughtException0() {
        Throwable throwable;
        synchronized (this) {
            throwable = this.throwable;
        }
        JDialog dialog = getUncaughtExceptionDialog();
        dialog.setVisible(true);
        System.exit(1);
    }
    
// Coalesced Runnable implementation to avoid numerous inner classes.
    private class Handler implements Runnable {
        Handler() {}
        public void run() {
            uncaughtException0();
            
        }
    }
    
    
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestAppHarness(new JFrame());
                
            }
        });
    }
    
}

