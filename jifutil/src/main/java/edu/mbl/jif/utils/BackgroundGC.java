package edu.mbl.jif.utils;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class BackgroundGC
      implements Runnable
{

   public void run () {
      System.gc();
   }


   public BackgroundGC () {
   }


   public static void backgroundGC () {
      try {
         (new Thread(new BackgroundGC())).start();
      }
      catch (Throwable thr) {
         ExceptionReporter.reportException(thr,
               ExceptionReporter.EXCEPTION_DEBUG, null,
               "create a thread to release memory");
      }
   }


   public static void main (String[] args) {
      BackgroundGC.backgroundGC();
   }
}
