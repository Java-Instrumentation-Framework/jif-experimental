package edu.mbl.jif.utils.log;

/*
 *  Copyright(c)2003 Forward Computing and Control Pty. Ltd.
 *  ACN 003 669 994, NSW, Australia      All rights Reserved
 *
 *  Written by Dr. M.P. Ford
 *
 */

import java.util.*;
import java.util.logging.*;
import java.text.MessageFormat;


/**
 *  This class provides an xml formatter for Parallel.
 *
 *@author     Matthew Ford
 *@created    October 9, 2003
 */
public class LoggingSimpleFormatter
      extends RobustFormatter
{

   /**
    *  the string to use for new lines defaults to \n if not specified
    */
   private static String newLine = "\n";

   static {
      try {
         newLine = (String) java.security.AccessController.doPrivileged(
               new sun.security.action.GetPropertyAction("line.separator"));
      }
      catch (Throwable t) {
         // print an error
         // LogStdStreams initialized in the main application will redirect this to a file.
         System.err.println(
               "Error getting system line separator for Logging will use \\n\n"
               + StringUtils.toString(t));
      }
   }


   /**
    *  the date object for the time/date output
    */
   Date date = new Date();
   /**
    *  the date time format
    */
   private final static String format = "{0,date} {0,time}";

   /**
    *  the formatter for the date and time
    */
   private MessageFormat formatter = null;

   /**
    *  the arguments for the date time formatter
    */
   private Object args[] = new Object[1];

   //~ Methods ..................................................................

   /**
    *  Returns the newLine setting for this class use system newLine
    *
    *@return    the string to use for new lines.
    */
   protected String newLineString () {
      return newLine;
   }


   /**
    *  Format the given LogRecord. Don't allow any errors to be thrown here
    *
    *@param  record  the log record to be formatted.
    *@return         a formatted log record
    */
   public synchronized String format (LogRecord record) {
      try {
         StringBuffer sb = new StringBuffer();
         StringBuffer text = new StringBuffer();
         if (formatter == null) {
            formatter = new MessageFormat(format);
         }
         try {
            date.setTime(record.getMillis());
            args[0] = date;
            formatter.format(args, text, null);
         }
         catch (Throwable t1) {
            text.append("Error formatting record date and time" + newLineString()
                  + StringUtils.toString(t1));
         }
         sb.append(text);
         sb.append(" ");
         try {
            if (record.getSourceClassName() != null) {
               sb.append(record.getSourceClassName());
            } else {
               sb.append(record.getLoggerName());
            }
         }
         catch (Throwable t2) {
            sb.append("Error getting class name" + newLineString()
                  + StringUtils.toString(t2));
         }
         try {
            if (record.getSourceMethodName() != null) {
               sb.append(" ");
               sb.append(record.getSourceMethodName());
            }
         }
         catch (Throwable t3) {
            sb.append("Error getting method name" + newLineString()
                  + StringUtils.toString(t3));
         }

         sb.append(newLineString());
         try {
            sb.append(record.getLevel().getLocalizedName());
         }
         catch (Throwable t4) {
            sb.append("Error getting localized level name" + newLineString()
                  + StringUtils.toString(t4));
         }
         sb.append(": ");
         try {
            sb.append(formatMessage(record));
         }
         catch (Throwable t5) {
            sb.append("Error formatting record message" + newLineString()
                  + StringUtils.toString(t5));
         }
         sb.append(newLineString());
         try {
            Throwable t = record.getThrown();
            if (t != null) {
               sb.append(StringUtils.toString(t));
            }
         }
         catch (Throwable t6) {
            sb.append("Error getting record exception thrown" + newLineString()
                  + StringUtils.toString(t6));
         }
         sb.append(newLineString());
         return sb.toString();
      }
      catch (Throwable t) {
         return ("Unexpected error caught while trying to log a record" + newLineString()
               + StringUtils.toString(t));
      }
   }

}
