package edu.mbl.jif.utils.log;

/*
 *  Copyright(c)2003 Forward Computing and Control Pty. Ltd.
 *  ACN 003 669 994, NSW, Australia      All rights Reserved
 *
 *  Written by Dr. M.P. Ford
 *
 * assuming your current dir is the parent of au/com/forward
 * compile using javac au/com/forward/logging/*.java
 * compile using javac au/com/forward/utils/*.java
 * compile using javac au/com/forward/*.java
 * run using  java au.com.forward.ExampleLoggingApplication
 * or java -Djava.util.logging.config.file=logging.properties au.com.forward.ExampleLoggingApplication
 */
import java.util.logging.*;
import java.util.Date;
import java.io.File;

/**
 *  Example Logging Application
 *@author     Matthew Ford    October 9, 2003
 */
public class ExampleLoggingApplication {

    static {
        // any errors thrown here are fatal
        String userHome = System.getProperty("user.home", ".");
        String FILE_SEPARATOR = System.getProperty("file.separator", "/");
        String logFileName = userHome + FILE_SEPARATOR + "applicationLog.log";

        // set up logging for errors
        
        // redirect System.out as well as System.err and append to existing log
        LogStdStreams.initializeErrorLogging(logFileName,
                "Log File for ExampleLoggingApplication " + new Date(), true, true);
    }
    final static Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            // Logger.getLogger(ExampleLoggingApplication.class.getName());
    /**
     * an instance of a bad object
     * whose toString() throws an exception
     */
    BadObject badObject = new BadObject();

    /**
     *  Description of the Method
     *
     *@param  args  Description of the Parameter
     */
    public static void main(String args[]) {
        try {
            try {
                Handler[] handlers = logger.getHandlers();
                //Logger.getLogger("").getHandlers();
                boolean foundConsoleHandler = false;
                for (int index = 0; index < handlers.length; index++) {
                    // set console handler to SEVERE
                    if (handlers[index] instanceof ConsoleHandler) {
                        handlers[index].setLevel(Level.SEVERE);
                        handlers[index].setFormatter(new LoggingSimpleFormatter());
                        foundConsoleHandler = true;
                    }
                }
                if (!foundConsoleHandler) {
                    // no console handler found
                    System.err.println("No consoleHandler found, adding one.");
                    ConsoleHandler consoleHandler = new ConsoleHandler();
                    consoleHandler.setLevel(Level.SEVERE);
                    consoleHandler.setFormatter(new LoggingSimpleFormatter());
                    Logger.getLogger("").addHandler(consoleHandler);
                }
            } catch (Throwable t) {
                System.err.println("Unexpected Error setting up logging\n" + StringUtils.toString(t));
            }

            logger.setLevel(Level.FINEST);
            logger.finer("test finer");
            logger.info("test info");
            logger.severe("test severe");

            logger.finer("About to create new ExampleLoggingApplication()");
            ExampleLoggingApplication application = new ExampleLoggingApplication();
            logger.throwing(logger.getName(), "main",
                    new RuntimeException("throw a RuntimeException"));
            File file = new File("testfile");
            application.method1("test", application.badObject, file);
            //application.method1("test",null,file);
            //logger.log(Level.INFO,"Log a badObject {0} {1}",new Object[] {"string", application.badObject});
            logger.log(Level.SEVERE, "Log a badObject {0} {1}", new Object[]{"string",
                application.badObject
            });
        } catch (Throwable t) {
            System.err.println("Uncaught exception in main()\n" + StringUtils.toString(t));
        }
    }

    /**
     *  Constructor for the ExampleLoggingApplication object
     */
    public ExampleLoggingApplication() {
        logger.entering(this.getClass().getName(), "<init>");
        // .. constructor code here
        logger.exiting(this.getClass().getName(), "<init>");
    }

    /**
     *  Sample method to show argument logging
     *
     *@param  str1  a string argument
     *@param  obj1  an object argument
     *@param  obj2  another object argument
     */
    private void method1(String str1, Object obj1, Object obj2) {
        Object args[] = {"String:", str1, "Object1:", obj1, "Object2:", obj2};
        logger.entering(this.getClass().getName(), "method1", args);
        // ... method code here
        logger.exiting(this.getClass().getName(), "method1");
    }

    /**
     *  A Bad Object Class
     *
     *@author     Matthew Ford
     *@created    October 9, 2003
     */
    protected class BadObject {

        public BadObject() {
        }

        /**
         *  The toString() for this class
         *  Throws a RuntimeException
         * @return    does not return.  Throws a RuntimeException
         */
        public String toString() {
            //return "BadObject";
            throw new RuntimeException("BadObject toString throw RuntimeException");
        }
    }
}
