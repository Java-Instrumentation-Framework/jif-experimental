package tests.log;


// Set LogProps file when starting:
//    -Djava.util.logging.config.file=C:\Temp\logging.properties

import java.util.logging.*;


/**
* Demonstrate Java's logging facilities, in conjunction
* with a logging config file.
*/
public final class LoggingTest {
   // PRIVATE //

   //This logger will inherit the config of its parent, and add
   //any further config as an override. A simple style is to use
   //all config of the parent except, perhaps, for logging level.

   //This style uses a hard-coded literal and should likely be avoided:
   //private static final Logger fLogger = Logger.getLogger("myapp.business");

   //This style has no hard-coded literals, but forces the logger
   //to be non-static.
   //private final Logger fLogger=Logger.getLogger(this.getClass().getPackage().getName());

   //This style uses a static field, but hard-codes a class literal.
   //This is probably acceptable.
   private static final Logger fLogger = Logger.getLogger(LoggingTest.class.getPackage().getName());

   public static void main(String[] argv) {
      LoggingTest thing = new LoggingTest();
      thing.doSomething();
   }

   public void doSomething() {
      //Log messages, one for each level
      //The actual logging output depends on the configured
      //level for this package. Calls to "inapplicable"
      //messages are inexpensive.
      fLogger.finest("this is finest");
      fLogger.finer("this is finer");
      fLogger.fine("this is fine");
      fLogger.config("this is config");
      fLogger.info("this is info");
      fLogger.warning("this is a warning");
      fLogger.severe("this is severe");

      //In the above style, the name of the class and
      //method which has generated a message is placed
      //in the output on a best-efforts basis only.
      //To ensure that this information is always
      //included, use the following "precise log"
      //style instead :
      fLogger.logp(Level.INFO, this.getClass().toString(), "doSomething", "blah");

      //For the very common task of logging exceptions, there is a
      //method which takes a Throwable :
      Throwable ex = new IllegalArgumentException("Some exception text");
      fLogger.log(Level.SEVERE, "Some message", ex);

      //There are convenience methods for exiting and
      //entering a method, which are at Level.FINER :
      fLogger.exiting(this.getClass().toString(), "doSomething");

      //Display user.home directory, if desired.
      //(This is the directory where the log files are generated.)
      System.out.println("user.home dir: " + System.getProperty("user.home") );
   }
}
