package edu.mbl.jif.gui.error;

import java.awt.EventQueue;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * UncaughtExceptionHandler
 * Adapted from Scott Violet's Application Architecture blog...
 * @author GBH
 */

public class UncaughtExceptionHandler
    implements Thread.UncaughtExceptionHandler {

    private Throwable throwable;

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
    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        synchronized (this) {
            if (this.throwable != null) {
                // An exception has occured while we're trying to display
                // the current exception, bale.
                System.err.println("exception thrown while altering user");
                throwable.printStackTrace();
            //System.exit(1);
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
            "An unrecoverable error has occured. ",
            //+ getName()  + " will now exit",
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

        new StackWindow( /*getName() + */" Error", throwable, 600, 400);

        //System.exit(1);

    }

    private class Handler
        implements Runnable {

        Handler() {
        }

        public void run() {
            uncaughtException0();
        }

    }

    private class StackWindow extends JFrame {

        private JTextArea textArea;

        public StackWindow(String title, final Throwable t, final int width, final int height) {
            super(title);
            setSize(width, height);
            textArea = new JTextArea();
            JScrollPane pane = new JScrollPane(textArea);
            textArea.setEditable(false);
            getContentPane().add(pane);
            addStackInfo(t);
        }

        public void addStackInfo(final Throwable t) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    // Bring window to foreground
                    setVisible(true);
                    toFront();
                    // Convert stack dump to string
                    StringWriter sw = new StringWriter();
                    PrintWriter out = new PrintWriter(sw);
                    t.printStackTrace(out);
                    // Add string to end of text area
                    textArea.append(sw.toString());
                /** @todo add: Also Log this... */
                }

            });
        }

    }
}
   
/* Notes:
 * 
 The process of delegation for the uncaught exception handling.

   1. Use the uncaughtExceptionHandler for the current thread to handle the exception.

   2. If the uncaughtExceptionHandler is null, use the thread group for the thread 
   (the thread group is actually the default implementation of the 
   uncaughtExceptionHandler if it wasn't set explicitly by the user).

   3. If the thread group is the default implementation, but it isn't 
   the root thread group, delegate error handling to the parent thread group.

   4. If the thread group is the default implementation and it is the root 
   thread group, use the defaultUncaughtExceptionHandler set on the Thread class.

   5. If the defaultUncaughtExceptionHandler isn't set on the Thread class, 
   simply invoke the legacy uncaught exception algorithm from the pre Java 5 
   days in the ThreadGroup class. 
   
 */
