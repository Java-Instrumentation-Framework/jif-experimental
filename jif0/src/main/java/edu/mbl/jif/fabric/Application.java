/**
 * $Id$
 *
 * Copyright (c) 2006, Sun Microsystems, Inc
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the PasswordStore project nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.mbl.jif.fabric;

import edu.mbl.jif.workframe.ApplicationController;
import edu.mbl.jif.gui.status.jx.JXStatusBar2;
//import edu.mbl.jif.gui.error.JifErrorDialog;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.utils.diag.CheckThreadViolationRepaintManager;
import edu.mbl.jif.utils.diag.edt.EventDispatchThreadHangMonitor;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.lang.ref.WeakReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

/**
 * Base Application class for Swing apps. Application provides the following
 * functionality:
 * <ul>
 * <li>A sequence of methods to override that are invoked as part
 *     of starting the application. All methods are invoked on the event
 *     dispatch thread.
 * <li>Has a ResourceBundle. This is useful for smallish apps that wish to
 *     place all their localized resources into a single place.
 * <li>Has a Preferences node. This is useful for apps that wish to persist
 *     state between sessions.
 * <li>Registers itself as the UncaughtExceptionHandler. If an uncaught
 *     exception is encountered the application will show a warning dialog and
 *     exit.
 * <li>Provides a single entry point to call when the user wishes to exit
 *     the app. This can be overriden to warn the user about unsaved changes,
 *     or provide similar functionality.
 * <li>Provides a ThreadFactory that produces background threads suitable
 *     for Swing applications. The application will block until all background
 *     threads vended from the thread factory have completed.
 * <li>An arbitrary set of key/value pairs.
 * </ul>
 * <p><a name="initSequence"></a>
 * Application provides a handful of methods that are invoked as part of
 * starting the application. Subclasses need only override those they are
 * interested in. The following outlines the order the methods are invoked in
 * as well as what they are intended for. All methods are invoked on the
 * event dispatching thread.
 * <ol>
 * <li>preInit is invoked first. preInit is provided for any initialization
 * that needs to be done prior to creating the UI. Application's implementation
 * invokes installLookAndFeel to install the system look and feel.
 * <li>init is invoked after preInit. init is intended for creating the actual
 * UI.
 * <li>ApplicationListeners are notified that the application has initialized
 *     (appDidInit).
 * <li>postInit is invoked after the listeners are notified, and is intended
 *     for any cleanup that needs to be done.
 * <li>postInitEventQueueEmpty is invoked after postInit and after the event
 *     queue has processed all pending events, such as paint events or
 *     revalidate requests generated during the earlier stages of
 *     initialization. Subclasses that need to do processing after the UI is
 *     completely showing should override this method.
 * </ol>
 * <p>
 * <a name="exitSequence">
 * Application also provides a sequence of methods for exiting the
 * application. When exit is invoked the following methods are invoked:
 * <ol>
 * <li>canExit is invoked, if this returns false the application will not exit.
 * <li>ApplicationListeners are asked if the application can exit. If any
 *     ApplicationListener returns false from canApplicationExit the application
 *     will not exit.
 * <li>waitForBackgroundThreads is invoked to block until any background threads
 *     have completed.
 * <li>All ApplicationListeners are notified that the application is exiting.
 * <li>exiting is invoked.
 * <li>Lastly, System.exit is invoked.
 * </ol>
 * <p>
 * Concrete implementations need only override getName, but will undoubtedly
 * override one of the various init methods as well.
 *
 * @version $Revision$
 */
public abstract class Application
        implements Thread.UncaughtExceptionHandler {

    // types for Handlerd
    private static final int RUN_POST_INIT_EVENT_QUEUE_EMPTY = 0;
    private static final int RUN_START_ON_EDT = 1;
    private static final int RUN_WAIT_EVENTS = 2;
    private static final int UNCAUGHT_EXCEPTION = 3;
    private static final int RUN_WAIT_FOR_BACKGROUND_THREADS = 4;
    // Exception that has been thrown. This is used to track if an exception
    // is thrown while alerting the user to the current exception.
    private Throwable throwable = null;
    // ThreadFactory for vending threads that are automatically registered.
    private ThreadFactory threadFactory;
    // Threads we'll block on when exiting.
    private List<WeakReference<Thread>> threads;
    // Whether or not the app has started
    private boolean started;
    // Preferences nodes for the app. for User
    private Preferences preferences;
    // Preferecnes nodes for the app. for System
    private Preferences preferencesSystem; //++GBH
    // ResourceBundle for the app
    private ResourceBundle resources;
    // Arbitrary user data.
    private Map<Object, Object> data;
    // ApplicationListeners
    private List<ApplicationListener> appListeners;
    // Is debugging on?
    private static boolean debug;
    private JXStatusBar2 statusBar;
    private JFrame hostFrame;
    private ApplicationController controller;
    private static Application APPLICATION;

    /**
     * Returns the single Application instance.
     *
     * @return the single Application instance
     */
    public static Application getInstance() {
        return APPLICATION;
    }

    public static void clearInstance() {
        APPLICATION = null;
    }

    public static void setDebug(boolean _debug) {
        debug = _debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    /**
     * Convenience method to return a resource from the Application as
     * a String.
     *
     * @param key the key identifying the resource to obtain
     * @return the resource as a String
     */
    public static String getResourceAsString(String key) {
        return getInstance().getResourceBundle().getString(key);
    }

    /**
     * Creates a new Application instance.  Subclasses very rarely need
     * put any logic in the constructor, especially not code that creates
     * Swing components. Override one of the various init methods instead
     * which are guaranteed to be invoked on the event dispatching thread.
     *
     * @throws IllegalStateException if an Application has already been created
     * @see <a href="#initSequence">init sequence</a>
     */
    public Application() {
        if (APPLICATION != null) {
            throw new IllegalStateException("Can only have one Application");
        }
        threads = new LinkedList<WeakReference<Thread>>();
        APPLICATION = this;
        data = new HashMap<Object, Object>(1);

        //if (System.getProperty("nohandler") == null) {
        // Thread.setDefaultUncaughtExceptionHandler(this);
        //}

        // Add Swing EDT diagnostics
        if (this.getPreferences().getBoolean("checkRepaint", false)) {
            RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        }
        if (this.getPreferences().getBoolean("checkEDT", false)) {
            EventDispatchThreadHangMonitor.initMonitoring();
        }
    }

    /**
     * Adds a listener for application events.
     *
     * @param listener the ApplicationListener to add
     */
    public void addApplicationListener(ApplicationListener listener) {
        if (appListeners == null) {
            appListeners = new CopyOnWriteArrayList<ApplicationListener>();
        }
        appListeners.add(listener);
    }

    /**
     * Adds a listener for application events.
     *
     * @param listener the ApplicationListener to add
     */
    public void removeApplicationListener(ApplicationListener listener) {
        if (appListeners != null) {
            appListeners.remove(listener);
        }
    }

    /**
     * Associated the specified value with the specified key. This is intended
     * for developers to place application specific data in.
     *
     * @param key the key to store the value in
     * @param value the value to associated with key
     */
    public final void putData(Object key, Object value) {
        data.put(key, value);
    }

    /**
     * Returns the value for the specified user key.
     *
     * @param key the key used to retrieve the specified value
     * @return the value for the specified user key
     */
    public final Object getData(Object key) {
        return data.get(key);
    }

    /**
     * Returns the Preferences object for the Application.
     *
     * @return the Preferences object for the Application
     * @see #getPreferencesKey
     */
    public final Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(getPreferencesKey());
        }
        return preferences;
    }

    public final Preferences getSystemPreferences() { //++GBH

        if (preferencesSystem == null) {
            preferencesSystem =
                    Preferences.systemNodeForPackage(getPreferencesKey());
        }
        return preferencesSystem;
    }

    /**
     * Returns the Class key used to fetch the Preferences object. This
     * implementation returns the Application class.
     *
     * @return the key used to fetch the Preferences object
     * @see #getPreferences
     */
    protected Class getPreferencesKey() {
        return getClass();
    }

    /**
     * Returns the ResourceBundle for the Application. The ResourceBundle
     * is loaded using the value returned from getResourceBundleName.
     *
     * @return the ResourceBundle for the Application
     * @see #getResourceBundleName
     */
    public final ResourceBundle getResourceBundle() {
        if (resources == null) {
            resources = ResourceBundle.getBundle(getResourceBundleName());
        }
        return resources;
    }

    /**
     * Returns the key for loading the resources for the Application.
     * This implementation returns
     * <code>getClass().getName().resources.Resources</code>.
     *
     * @return the name used to locate the Applications ResourceBundle
     */
    protected String getResourceBundleName() {
        return getClass().getPackage().getName() + ".resources.Resources";
    }

    /**
     * Returns a ThreadFactory suitable for threads used within Swing
     * applications. Threads created by the returned ThreadFactory are
     * automatically registered with the Application, are not daemon, and
     * have a priority of Thread.MIN_PRIORITY.
     * <p>
     * When exit is invoked the Application will block until all background
     * threads have exited.
     *
     * @return a ThreadFactory suitable for background threads
     * @see #registerThread
     * @see #waitForBackgroundThreadsToExit
     */
    public ThreadFactory getBackgroundThreadFactory() {
        if (threadFactory == null) {
            threadFactory = new SwingThreadFactory();
        }
        return threadFactory;
    }

    /**
     * Registers a background thread with the Application. When the application
     * exits it will block until all background threads have completed.
     * <p>
     * This method is thread safe.
     *
     * @param thread the Thread to wait for completion on
     * @throws IllegalArgumentException if thread is null
     */
    public final void registerThread(Thread thread) {
        if (thread == null) {
            throw new IllegalArgumentException("Thread must be non-null");
        }
        synchronized (threads) {
            // Prune any bogus references
            Iterator<WeakReference<Thread>> threadsIterator = threads.iterator();
            while (threadsIterator.hasNext()) {
                if (threadsIterator.next().get() == null) {
                    threadsIterator.remove();
                }
            }
            threads.add(new WeakReference<Thread>(thread));
        }
    }

    /**
     * Returns the name of the application.
     *
     * @return the name of the Application
     */
    public abstract String getName();

    public abstract String getVersion(); // +GBH

    /**
     * Invoked from preInit to set the look and feel for the Application. This
     * implementation sets the look and feel to the system look and feel.
     * If setting the look and feel results in throwing an exception it will
     * be ignored.
     *
     * @see <a href="#initSequence">init sequence</a>
     */
    protected void installLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (ClassNotFoundException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }
    }


    // Shutdown Hook
    //   @todo: add shutDownHook
    //        Runtime.getRuntime().addShutdownHook(new Thread() {
    //                        public void run() {
    //                            // System.out.println("shutdownHook...");
    //                            // terminate();
    //                            _instance.closeCamera();
    //                        }
    //                    });
    //
    /**
     * Invoked as part of starting the application. This method invokes
     * installLookAndFeel. See
     * <a href="#initSequence">init sequence</a> for details on when this
     * is invoked.
     *
     * @see #installLookAndFeel
     */
    protected void preInit() {
        installLookAndFeel();
    }

    /**
     * Invoked as part of starting the application. See
     * <a href="#initSequence">init sequence</a> for details on when this
     * is invoked.
     */
    protected void init() {
    }

    /**
     * Invoked as part of starting the application. See
     * <a href="#initSequence">init sequence</a> for details on when this
     * is invoked.
     */
    protected void postInit() {
    }

    /**
     * Invoked as part of starting the application. See
     * <a href="#initSequence">init sequence</a> for details on when this
     * is invoked.
     */
    protected void postInitEventQueueEmpty() {
    }

    /**
     * Starts the Application.  This method is typically invoked
     * directly from <code>main</code>. Refer to
     * <a href="#initSequence">init sequence</a> for details on which methods
     * this invokes.
     *
     * @throws IllegalStateException if start has already been invoked
     * @see <a href="#initSequence">init sequence</a>
     */
    public final void start() {
        if (started) {
            throw new IllegalStateException("Application is already running");
        }
        //ClassPathHacker.initLibraries();
        started = true;
        if (!EventQueue.isDispatchThread()) {
            SwingUtilities.invokeLater(new Handler(RUN_START_ON_EDT));
        } else {
            startOnEDT();
        }
    }

    private void startOnEDT() {
        preInit();
        init();
        fireApplicationDidInit();
        postInit();
        new Thread(new Handler(RUN_WAIT_EVENTS)).start();
    }


    // === GBH's Wonderful Additions ;-) ==================================
    public final void setController(ApplicationController controller) {
        this.controller = controller;
    }

    public final ApplicationController getController() {
        return controller;
    }

    public final JFrame getHostFrame() { // GBH

        return hostFrame;
    }

    public final void setAlwaysOnTop(boolean t) { // GBH

        getPreferences().putBoolean("app.alwaysOnTop", t);
        getHostFrame().setAlwaysOnTop(t);
    }

    public final void setAuditoryFeedback(boolean t) { // GBH

        getPreferences().putBoolean("app.auditoryFeedback", t);
    }

    // setHostFrame - GBH
    public void setHostFrame(JFrame frame) {
        hostFrame = frame;
        createStatusBar();
    }


    // createStatusBar
    void createStatusBar() {
        statusBar = new JXStatusBar2();
        statusBar.setMinimumSize(new java.awt.Dimension(27, 14));
        statusBar.setMaximumSize(new java.awt.Dimension(1200, 14));
        hostFrame.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    public void statusBarMessage(final String msg) {
        if (statusBar != null) {
            StaticSwingUtils.dispatchToEDT(new Runnable() {

                public void run() {
                    statusBar.setLeadingMessage(msg);
                }
            });
        }
    }

    /**
     * Returns whether the application should be allowed to exit. This
     * is invoked from exit. A return value of false will stop the application
     * from exiting.
     *
     * @return whether or not the application should be allowed to exit; this
     *         implementation unconditionally returns true
     * @see #exit
     */
    protected boolean canExit() {
        return true;
    }

    /**
     * Invoked as part of exiting the application. Refer to
     * <a href="#exitSequence">exit sequence</a> for details on when this
     * method is invoked.
     */
    protected void exiting() {
    }

    private boolean listenersCanExit() {
        if (appListeners != null) {
            for (ApplicationListener listener : appListeners) {
                if (!listener.canApplicationExit()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Blocks until all registered threads have completed. This is invoked
     * from exit. If necessary this method will invoke
     * createBackgroundThreadDialog to create a modal dialog that is shown
     * while waiting for background threads to exit.
     *
     * @see #exit
     * @see #createBackgroundThreadDialog
     */
    protected void waitForBackgroundThreadsToExit() {
        // Do a quick check to see if we'll need to show a modal dialog. If
        // after 100 ms threads are still running, then show a modal dialog.
        if (!waitForBackgroundThreadsToExit(100)) {
            // spawn background thread
            JDialog threadDialog = createBackgroundThreadDialog();
            new Thread(
                    new Handler(RUN_WAIT_FOR_BACKGROUND_THREADS, threadDialog)).start();
            threadDialog.setVisible(true);
        }
    }


    // Invoked on a background thread
    private void waitForBackgroundThreadsToExitInBackground(JDialog dialog) {
        waitForBackgroundThreadsToExit(Long.MAX_VALUE);
        // The background thread is spawned prior to making the dialog visible.
        // As such, we need to block until the dialog is visible.
        while (!dialog.isVisible()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        dialog.setVisible(false);
    }


    // Blocks until the background threads exit, or maxWaitTime milliseconds has
    // expired.
    private boolean waitForBackgroundThreadsToExit(long maxWaitTime) {
        boolean done = false;
        long start = System.currentTimeMillis();
        while (!done) {
            Thread thread = null;
            synchronized (threads) {
                Iterator<WeakReference<Thread>> threadsIterator =
                        threads.iterator();
                while (threadsIterator.hasNext()) {
                    thread = threadsIterator.next().get();
                    if (thread != null) {
                        threadsIterator.remove();
                        break;
                    }
                }
            }
            if (thread != null) {
                try {
                    thread.join(
                            Math.max(10,
                            maxWaitTime - (System.currentTimeMillis() - start)));
                } catch (InterruptedException ex) {
                }
                if (thread.isAlive()) {
                    // It's still alive, reregister so that we loop back
                    // through.
                    registerThread(thread);
                }
                if ((System.currentTimeMillis() - start) >= maxWaitTime) {
                    // More than maxWaitTime has elapsed, bail.
                    return false;
                }
            } else {
                done = true;
            }
        }
        return true;
    }

    private void fireApplicationExiting() {
        if (appListeners != null) {
            for (ApplicationListener listener : appListeners) {
                listener.applicationExiting();
            }
        }
    }

    private void fireApplicationDidInit() {
        if (appListeners != null) {
            for (ApplicationListener listener : appListeners) {
                listener.applicationDidInit();
            }
        }
    }

    /**
     * Exits the application. Refer to <a href="#exitSequence">exit sequence</a>
     * for details on which methods this invokes.
     *
     * @see #canExit
     * @see #waitForBackgroundThreadsToExit
     */
    public final void exit() {
        if (canExit() && listenersCanExit()) {
            waitForBackgroundThreadsToExit();
            fireApplicationExiting();
            exiting();
            //System.out.println("Application exiting...");
            //IJClient ijClient = IJClientFactory.getIJClient(false);
            //ijClient.quit();
//            if (IJ.getInstance() != null) {
//                ImageJ ij = IJ.getInstance();
//                ij.exitWhenQuitting(true);
//                ij.quit();
//            }
            System.out.println("CamAcqJ Application Exited");
            System.exit(0);
//            } else {
//                APPLICATION = null;
//            }


        }
    }

    // Error handling
    public final void error(String msg) {
        error(msg, "Application error");
    }

    public final void error(String msg, String title) {
        error(msg, title, null);
    }

    public final void error(final String msg, final String title, String details) {

//        StaticSwingUtils.dispatchToEDT(new Runnable() {
//
//            public void run() {
//                int type = JOptionPane.ERROR_MESSAGE;
//                JOptionPane.showMessageDialog(getHostFrame(), msg, title, type);
//            }
//        });
        ErrorInfo errorInfo = new ErrorInfo(
                title,
                msg,
                details, // details
                null, // catagory for logging
                null,
                ErrorLevel.SEVERE,
                null); /*Map of additional state*/
        JXErrorPane.showFrame(getHostFrame(), errorInfo);
        System.err.println("ERROR: " + title + " -- " + msg);
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
            SwingUtilities.invokeLater(new Handler(UNCAUGHT_EXCEPTION));
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

        new StackWindow(getName() + " Error", throwable, 600, 400);
    //System.exit(1);
    }

    /**
     * Returns the dialog to show when waiting for any background threads
     * to exit. The returned dialog must be modal.
     *
     * @return dialog to shown when waiting for background threads to exit
     */
    protected JDialog createBackgroundThreadDialog() {
        JDialog dialog = new JDialog((Frame) null, getName(), true);
        dialog.setLayout(new GridBagLayout());
        // PENDING: localize this
        dialog.add(
                new JLabel("Waiting for processing to complete..."),
                new GridBagConstraints(
                0,
                0,
                1,
                1,
                0,
                0,
                GridBagConstraints.LINE_START,
                GridBagConstraints.NONE,
                new Insets(5, 5, 0, 50),
                0,
                0));
        JProgressBar pb = new JProgressBar();
        pb.setIndeterminate(true);
        dialog.add(pb,
                new GridBagConstraints(
                0,
                1,
                1,
                1,
                1,
                0,
                GridBagConstraints.LINE_START,
                GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5),
                0,
                0));
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        return dialog;
    }

    private void waitForEmptyEventQ() {
        boolean qEmpty = false;
        JPanel bogusComponent = new JPanel();
        EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue();
        while (!qEmpty) {
            NotifyingPaintEvent e = new NotifyingPaintEvent(bogusComponent);
            q.postEvent(e);
            synchronized (e) {
                // Wait until the event has been dispatched
                while (!e.isDispatched()) {
                    try {
                        e.wait();
                    } catch (InterruptedException ie) {
                    }
                }
                qEmpty = e.qEmpty();  // Check if the q is empty

            }
        }
        SwingUtilities.invokeLater(new Handler(RUN_POST_INIT_EVENT_QUEUE_EMPTY));
    }

    // Coalesced Runnable implementation to avoid numerous inner classes.
    private class Handler
            implements Runnable {

        private final int type;
        private final Object[] args;

        Handler(int type, Object... args) {
            this.type = type;
            this.args = args;
        }

        public void run() {
            switch (this.type) {
                case RUN_POST_INIT_EVENT_QUEUE_EMPTY:
                    postInitEventQueueEmpty();
                    break;
                case RUN_START_ON_EDT:
                    startOnEDT();
                    break;
                case RUN_WAIT_EVENTS:
                    waitForEmptyEventQ();
                    break;
                case UNCAUGHT_EXCEPTION:
                    uncaughtException0();
                    break;
                case RUN_WAIT_FOR_BACKGROUND_THREADS:
                    waitForBackgroundThreadsToExitInBackground((JDialog) this.args[0]);
                    break;
            }
        }
    }

    // Used in determining when the event Q is empty
    private class NotifyingPaintEvent
            extends PaintEvent
            implements ActiveEvent {

        private boolean _dispatched = false;
        private boolean _qEmpty;

        NotifyingPaintEvent(Component x) {
            super(x, PaintEvent.UPDATE, null);
        }

        public synchronized boolean isDispatched() {
            return _dispatched;
        }

        public synchronized boolean qEmpty() {
            return _qEmpty;
        }

        public void dispatch() {
            EventQueue q =
                    Toolkit.getDefaultToolkit().getSystemEventQueue();
            _qEmpty = (q.peekEvent() == null);
            synchronized (this) {
                _dispatched = true;
                notifyAll();
            }
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

    private static class SwingThreadFactory
            implements ThreadFactory {

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "swing-thread");
            thread.setDaemon(false);
            thread.setPriority(Thread.MIN_PRIORITY);
            Application.getInstance().registerThread(thread);
            return thread;
        }
    }
}
