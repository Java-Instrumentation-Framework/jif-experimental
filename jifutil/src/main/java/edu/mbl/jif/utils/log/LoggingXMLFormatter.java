package edu.mbl.jif.utils.log;

/*
 *  Copyright(c)2003 Forward Computing and Control Pty. Ltd.
 *  ACN 003 669 994, NSW, Australia      All rights Reserved
 *
 *  Written by Dr. M.P. Ford
 *
 */
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.*;


/**
 *  This class provides a robust xml formatter.
 *
 *@author     Matthew Ford
 *@created    3 Oct 2003
 */
public class LoggingXMLFormatter
      extends RobustFormatter
{

   /**
    *  Returns the newLine setting for this class for XML use \n for new lines
    *
    *@return    the string to use for new lines.
    */
   protected String newLineString () {
      return "\n";
   }


   /**
    *  Return the header string for a set of XML formatted records.
    *
    *@param  h  The target handler.
    *@return    header string
    */
   public String getHead (Handler h) {
      StringBuffer sb = new StringBuffer();
      sb.append("<?xml version=\"1.0\"");

      String encoding = h.getEncoding();

      if (encoding == null) {
         // Figure out the default encoding.
         encoding = sun.io.Converters.getDefaultEncodingName();
      }

      // Try to map the encoding name to a canonical name.
      try {
         Charset cs = Charset.forName(encoding);
         encoding = cs.name();
      }
      catch (Exception ex) {
         // We hit problems finding a canonical name.
         // Just use the raw encoding name.
      }

      sb.append(" encoding=\"");
      sb.append(encoding);
      sb.append("\"");
      sb.append(" standalone=\"no\"?>\n");
      sb.append("<!DOCTYPE log SYSTEM \"logger.dtd\">\n");
      sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"logger.xsl\"?>\n");
      sb.append("<log>\n");

      return sb.toString();
   }


   /**
    *  Return the tail string for a set of XML formatted records.
    *
    *@param  h  The target handler.
    *@return    tail string
    */
   public String getTail (Handler h) {
      return "</log>\n";
   }


   /**
    *  Format the given message to XML.
    *
    *@param  record  the log record to be formatted.
    *@return         a formatted log record
    */
   public String format (LogRecord record) {
      StringBuffer sb = new StringBuffer(500);
      sb.append("<record>\n");

      sb.append("  <date>");
      appendISO8601(sb, record.getMillis());
      sb.append("</date>\n");

      sb.append("  <millis>");
      sb.append(record.getMillis());
      sb.append("</millis>\n");

      sb.append("  <sequence>");
      sb.append(record.getSequenceNumber());
      sb.append("</sequence>\n");

      String name = record.getLoggerName();
      if (name != null) {
         sb.append("  <logger>");
         escape(sb, name);
         sb.append("</logger>\n");
      }

      sb.append("  <level>");
      escape(sb, record.getLevel().toString());
      sb.append("</level>\n");

      if (record.getSourceClassName() != null) {
         sb.append("  <class>");
         escape(sb, record.getSourceClassName());
         sb.append("</class>\n");
      }

      if (record.getSourceMethodName() != null) {
         sb.append("  <method>");
         escape(sb, record.getSourceMethodName());
         sb.append("</method>\n");
      }

      sb.append("  <thread>");
      sb.append(record.getThreadID());
      sb.append("</thread>\n");

      String message = "";

      if (record.getMessage() != null) {
         // Format the message string and its accompanying parameters.
         message = formatMessage(record);
      }

      if (record.getThrown() != null) {
         Throwable th = record.getThrown();
         if (message.length() > 0) {
            message += "\n";
         }
         message = StringUtils.toString(th);
      }

      sb.append("  <message>");
      escape(sb, message);
      sb.append("</message>");
      sb.append("\n");
      sb.append("</record>\n");
      return sb.toString();
   }


   /**
    *  Append to the given StringBuffer an escaped version of the given text
    *  string where XML special characters have been escaped. For a null string we
    *  append "<null>"
    *
    *@param  sb    the StringBuffer to append to
    *@param  text  the text to be escaped.
    */
   private void escape (StringBuffer sb, String text) {
      if (text == null) {
         text = "<null>";
      }
      for (int i = 0; i < text.length(); i++) {
         char ch = text.charAt(i);
         if (ch == '<') {
            sb.append("&lt;");
         } else if (ch == '>') {
            sb.append("&gt;");
         } else if (ch == '&') {
            sb.append("&amp;");
         } else {
            sb.append(ch);
         }
      }
   }


   /**
    *  Append the time and date in ISO 8601 format java.text.SimpleDateFormat
    *  dateFormat = new java.text.SimpleDateFormat("yyyy-mm-ddTHH:mm:ss"); stops
    *  this class from loading ??
    *
    *@param  sb      StringBuffer to append to
    *@param  millis  the time in milliseconds
    */
   private void appendISO8601 (StringBuffer sb, long millis) {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(millis);
      sb.append(calendar.get(Calendar.YEAR));
      sb.append('-');
      a2(sb, calendar.get(Calendar.MONTH) + 1);
      sb.append('-');
      a2(sb, calendar.get(Calendar.DAY_OF_MONTH));
      sb.append('T');
      a2(sb, calendar.get(Calendar.HOUR_OF_DAY));
      sb.append(':');
      a2(sb, calendar.get(Calendar.MINUTE));
      sb.append(':');
      a2(sb, calendar.get(Calendar.SECOND));
   }


   /**
    *  Append a two digit number.
    *
    *@param  sb  output buffer to append to
    *@param  x   int to format
    */
   private void a2 (StringBuffer sb, int x) {
      if (x < 10) {
         sb.append('0');
      }
      sb.append(x);
   }

}
