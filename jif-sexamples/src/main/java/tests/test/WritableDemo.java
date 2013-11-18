/*
 * WritableDemo.java
 *
 * Created on December 14, 2006, 11:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.test;

// WritableDemo.java

import java.io.File;

public class WritableDemo
{
  public static void main (String [] args)
  {
   if (args.length < 1 || args.length > 2)
   {
     System.err.println ("usage : java WritableDemo [option] filespec");
     System.err.println ("");
     System.err.println ("options");
     System.err.println ("");
     System.err.println (" + makes the file writable");
     System.err.println (" - makes the file read-only");
     System.err.println (" no option returns files writable status");
     System.err.println ("");
     System.err.println ("example: java WritableDemo test.dat");
     return;
   }

   String option = (args.length == 1) ? "" : args [0];
   File filespec = new File (args [(args.length == 1) ? 0 : 1]);

   if (option.equals ("+"))
   {
     if (filespec.setWritable (true))
       System.out.println (filespec + " is now writable");
     else
       System.out.println ("Permission denied");
   }
   else
   if (option.equals ("-"))
   {
     if (filespec.setWritable (false))
       System.out.println (filespec + " is now read-only");
     else
       System.out.println ("Permission denied");
   }
   else
     System.out.println (filespec + " is " + 
               (filespec.canWrite ()
               ? "writable" : "read-only"));
  }
}