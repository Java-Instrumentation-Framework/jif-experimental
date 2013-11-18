package edu.mbl.jif.utils.log;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Level;


public class Log
{

   static Logger logger;

   static {
      Logger logger = Logger.getLogger("edu.mbl.jif.utils.log.Log");
      try {
         // Create a file handler that uses 3 logfiles, each with a limit of 1Mbyte
         String pattern = "PSj%g.log";
         int limit = 1000000; // 1 Mb
         int numLogFiles = 3;
         FileHandler fh = new FileHandler(pattern, limit, numLogFiles);
         // Add FileHandler to logger
         logger.addHandler(fh);
      }
      catch (IOException e) {
          e.printStackTrace();
      }
   }


   public static void main (String[] args) {
      // Log a few message at different severity levels
      logger.severe("my severe message");
      logger.warning("my warning message");
      logger.info("my info message");
      logger.config("my config message");
      logger.fine("my fine message");
      logger.finer("my finer message");
      logger.finest("my finest message");

      // This method should be used when an exception is encounted
      try {
         // Test with an exception
         throw new IOException();
      }
      catch (Throwable e) {
         // Log the exception
         logger.log(Level.SEVERE, "Uncaught exception", e);
      }
      // When a method is throwing an exception, this method should be used
      Exception ex = new IllegalStateException();
      //logger.throwing(this.getClass().getName(), "myMethod", ex);
   }

}
/*
 Limiting the Size of a Log by Using a Rotating Sequence of Files
 ... new FileHandler(pattern, limit, numLogFiles)
 The FileHandler allows a more effective approach by allowing you to use a
 sequence of files to hold the log information. When a file fills up, the '
 oldest file is emptied and logging resumes in that file. More specifically,
 if there are N log files in the sequence, records are always dumped into
 logfile0. When logfile0 is filled, logfileN-2 is renamed to logfileN,
 logfileN-3 is renamed to logfileN-2, etc. Finally, logfile0 is renamed
 logfile1. A new logfile0 is created and logging resumes in the
 new logfile0. To read the log records in chronological order, you need to
 process the files from logfileN-1 to logfile0.

 The logfile number is called the generation number and ranges from
 0 to the number of logfiles - 1. When specifying the filename pattern
 to use for the logfiles, you need to include the location of the
 generation number using the %g placeholder. For example, using a
 filename pattern of my%g.log with three log files will result in the
 files my0.log, my1.log, and my2.log.
 */

// Configuring Logger Default Values with a Properties File
/* The default values for loggers and handlers can be set using a
 properties file. This example demonstrates a sample logging properties file.
 For more information about logging properties, see the
 lib/logging.properties file in the JRE directory.
 */

/*
    # Specify the handlers to create in the root logger
    # (all loggers are children of the root logger)
    # The following creates two handlers
    handlers = java.util.logging.ConsoleHandler, java.util.logging.FileHandler
    # Set the default logging level for the root logger
    .level = ALL
    # Set the default logging level for new ConsoleHandler instances
    java.util.logging.ConsoleHandler.level = INFO
    # Set the default logging level for new FileHandler instances
    java.util.logging.FileHandler.level = ALL
    # Set the default formatter for new ConsoleHandler instances
    java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter
    # Set the default logging level for the logger named com.mycompany
    com.mycompany.level = ALL

    The custom logging properties file is loaded by specifying a system
    property on the command line:
    java -Djava.util.logging.config.file=mylogging.properties <class>
 */
