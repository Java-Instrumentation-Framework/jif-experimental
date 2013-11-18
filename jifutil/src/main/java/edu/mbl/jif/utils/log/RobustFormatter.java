package edu.mbl.jif.utils.log;

/*
 *  Copyright(c)2003 Forward Computing and Control Pty. Ltd.
 *  ACN 003 669 994, NSW, Australia      All rights Reserved
 *
 *  Written by Dr. M.P. Ford

 Logging package
 Copyright (c) 2003 Forward Computing and Control Pty. Ltd.
 NSW. Australia,  www.forward.com.au
 All rights reserved.

 Redistribution of the source of this package, with or without
 modification, is NOT permitted.

 Redistribution of the compiled java .class files of this package, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions in compiled java .class form must reproduce the above
   copyright notice, this list of conditions and the following disclaimer
   in the documentation and/or other materials provided with the distribution.

 2. If the software is modified, the compiled java .class distribution must
   include, in the documentation and/or other materials provided with the
   distribution, a statement that the software has been modified and
   identify the person who was responsable for the modifications.

 3. Neither the name of the author nor Forward Computing and Control may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 4.  All advertising materials mentioning features or use of this
    software must display the following acknowledgment:
    "This product includes software developed by Matthew Ford
     and Forward Computing and Control Pty. Ltd.
    (http://www.forward.com.au)."


 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 *
 */
import java.util.logging.*;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 *  A Robust formatter for LogRecords.
 *
 *@author     Matthew Ford
 *@created    October 9, 2003
 */

public abstract class RobustFormatter
      extends Formatter
{

   /**
    *  Returns the newLine setting for this class
    *
    *@return    the string to use for new lines.
    */
   protected abstract String newLineString ();


   /**
    *  Return toString of object handling null inputs
    *  if obj is a <code>Throwable</code> returns the stack trace as a <code>String</code>.
    *
    * @param  obj  the Object to convert to a <code>String</code>.
    * @return      "null" if obj is null, else
    *              the contents of stack trace if obj is a Throwable else
    *              obj.toString()
    *              If obj.toString() throws an exception then that exception and its stack trace are returned
    */
   public static String toString (Object obj) {
      if (obj == null) {
         return "null";
      }

      Throwable e;
      if (obj instanceof Throwable) {
         e = (Throwable) obj;
         StringWriter strWriter = new StringWriter();
         PrintWriter printWriter = new PrintWriter(strWriter);
         e.printStackTrace(printWriter);
         return strWriter.toString();
      } //else {
      try {
         return obj.toString();
      }
      catch (Throwable t) {
         return (toString(t));
      }

   }


   /**
    *  Format the message using resource bundle if available catch all possible
    *  errors and handle them here return the most information we can.
    *
    *@param  record  the log record to be formatted
    *@return         the string to log
    */
   public synchronized String formatMessage (LogRecord record) {
      String recordFormat = null;
      String catalogFormat = null;
      try {
         recordFormat = record.getMessage();
      }
      catch (Throwable t) {
         // handle below
      }
      if (recordFormat == null) {
         // error in getMessage()
         catalogFormat = "Error getting record message.";
      } else {
         try {
            java.util.ResourceBundle catalog = record.getResourceBundle();
            if (catalog != null) {
               try {
                  catalogFormat = catalog.getString(recordFormat);
               }
               catch (java.util.MissingResourceException ex) {
                  // not found just use recordFormat
                  catalogFormat = recordFormat;
               }
            } else {
               // just use the message we have as there is no resource bundle
               catalogFormat = recordFormat;
            }
         }
         catch (Throwable tex) {
            // some other error drop through
         }
         if (catalogFormat == null) {
            catalogFormat = "Error looking up resource bundle for message '"
                  + recordFormat + "'";
         }
      }
      // Do the formatting.
      Object parameters[] = null;
      try {
         parameters = record.getParameters();
         if (parameters == null || parameters.length == 0) {
            // No parameters.  Just return format string.
            return catalogFormat;
         }
         // Is is a java.text style format?
         if (catalogFormat.indexOf("{0") >= 0) {
            return java.text.MessageFormat.format(catalogFormat, parameters);
         } else {
            // else just append parameters to message is {0} missing
            catalogFormat += newLineString();
            for (int i = 0; i < parameters.length; i++) {
               catalogFormat += " " + toString(parameters[i]);
            }
         }
         return catalogFormat;
      }
      catch (Throwable t) {
         // Formatting or getParameters() failed: just append any parameters to the message.
         if (parameters == null || parameters.length == 0) {
            // No parameters.  Just return format string.
         } else {
            catalogFormat += newLineString();
            for (int i = 0; i < parameters.length; i++) {
               catalogFormat += " " + toString(parameters[i]);
            }
         }
         return catalogFormat;
      }
   }

}
