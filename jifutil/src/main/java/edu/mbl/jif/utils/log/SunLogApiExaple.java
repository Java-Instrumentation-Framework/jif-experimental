package edu.mbl.jif.utils.log;


public class SunLogApiExaple
{
   public SunLogApiExaple () {
   }}
/*
 Java Logging API Basics

 New to the Java 2 Platform, Standard Edition, verison 1.4, the j
 ava.util.logging package offers the Java Logging API. This logging
 facility allows you to embed messages inside your code, and configure
 at runtime whether or not the mesages should be displayed or logged.
 If you don't need the information all the time, turn it off, and enable
 it when the user needs it (or you need it to get debug output from the user).
 Want to control the level of messages logged such that you only want serious
 logging messages from a particular package? This too is configurable.

 In the simplest case, you get the logger and log the message:

 // Get the Logger for the currrent package
 Logger logger = Logger.getLogger(packageName);

 // Log your message
 logger.log(level, text, exception);

 To demonstrate, the following program logs a severe message:

 package com.sun.java;
 import java.util.logging.*;

 public class LogSample {
     private static Logger logger = Logger.getLogger("com.sun.java");
     public static void main(String args[]) {
         logger.log(Level.SEVERE, "Testing - Level = SEVERE");
     }
 }

 First, compile the program. Since the program is in a package, be sure to
 use the -d option to send the .class file to the appropriate subdirectory:

 javac -d . LogSample.java

 Then, run the program:

 java com.sun.java.LogSample

 By default, logging output goes to the console.
 Depending upon when you run the program, you'll get an appropriate time stamp.

 May 15, 2004 9:08:33 AM com.sun.java.LogSample main
 SEVERE: Testing - Level = SEVERE

 The reason you get the Logger for a particular package is so that you can
 enable or disable logging for a particular package. Before we look at that
 though, let us explain the logging levels, of which there are seven.

 * SEVERE
 * WARNING
 * INFO
 * CONFIG
 * FINE
 * FINER
 * FINEST

 By having different logging levels, you can control when a message is logged
 and the level of detail. So, for a SEVERE-level message, you might dump a stack
 trace at a location where the program shouldn't get to under normal circumstances.
 For INFO-level, you might have entry / exit from methods. And, FINEST-level might
 be as each line is executed. There is no hard definition of what each level must
 print out. It is completely up to you during your development. There is a small
 amount of overhead to test if the current logging level is enabled for the given
 package, but having the logging information available during testing allows a
 better level of debug information to come back to the development team from say
 a quality assurance (QA) or user testing group.


 There's much more to the logging facility than just saying, here's a message,
 log it. For instance, to send out to a file, use a FileHandler:

 package com.sun.java;
 import java.util.logging.*;
 import java.io.*;

 public class LogSample2 {
   private static Logger logger = Logger.getLogger("com.sun.java");
   public static void main(String args[]) throws IOException {
   FileHandler fh = new FileHandler("log.out");
   // Send logger output to FileHandler.
   logger.addHandler(fh);
  // Log all
  logger.setLevel(Level.ALL);
  logger.log(Level.FINE, "Testing - Level = FINE");
  }
 }

 This sends the log message to the file log.out, as XML.

 <?xml version="1.0" encoding="windows-1252" standalone="no"?>
 <!DOCTYPE log SYSTEM "logger.dtd">
 <log>
 <record>
 <date>2004-05-26T09:41:20</date>
 <millis>1085578880859</millis>
 <sequence>0</sequence>
 <logger>com.sun.java</logger>
 <level>FINE</level>
 <class>com.sun.java.LogSample2</class>
 <method>main</method>
 <thread>10</thread>
 <message>Testing - Level = FINE</message>
 </record>
 </log>

 The system also provides other handlers, like the MemoryHandler for storing
 the last 'n' records, without worrying about formatting until you wish to
 display / save. Through constructive use of the logging facility, you can
 better maintain and service all your software. For additional information on
 the available APIs, see the Java Logging Overview document.
 */
