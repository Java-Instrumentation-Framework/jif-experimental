package edu.mbl.jif.camera;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.*;
import java.util.*;

import java.text.DecimalFormat;


public class CamUtils
{
   //static Image icon = null;
   public static Properties props = new Properties();
   static final String propsFilename = ".\\QCamJProps";

//----------------------------------------------------------------
// High resolution Timer functions
//
   static final DecimalFormat format = new DecimalFormat();
   static {
      format.setMinimumFractionDigits(3);
      format.setMaximumFractionDigits(3);
   }


//-----------------------------------------------------------
// Status & Error



////////////////////////////////////////////////////////////////////////
// Frame Positioning
   public static void setFramePosition (String propStr, int xPos, int yPos) {
      String s = propStr.substring(6, propStr.length());

      //System.out.println("set: " + s);
      props.setProperty(s + "_posX", String.valueOf(xPos));
      props.setProperty(s + "_posY", String.valueOf(yPos));
   }


   public static Point getFramePosition (String propStr) {
      String s = propStr.substring(6, propStr.length());
      int x = getInt(s + "_posX", 100);
      int y = getInt(s + "_posY", 100);
      return new Point(x, y);
   }


///////////////////////////////////////////////////////////////////////////
// loadImageIcon
   public static ImageIcon loadImageIcon (String gifFile) {
      ImageIcon img = null;
      try {
         img = //new ImageIcon("./Camera/" + gifFile);
               new ImageIcon(ClassLoader.getSystemResource("Camera/" +
               gifFile));
         //ImageIcon icon = createImageIcon(".\\" + gifFile, null);
         //ImageIcon icon = new ImageIcon(getClass().getResource("icons/icon.gif"));
      }
      catch (Exception e) {
         System.out.println("Exception loading: " + gifFile);
      }
      if (img == null) {
         System.out.println("Could Not Load Image: " + gifFile);
      }
      return img;
   }


   /** Returns an ImageIcon, or null if the path was invalid. */
   protected static ImageIcon createImageIcon (String path,
         String description) {
      java.net.URL imgURL = CamUtils.class.getResource(path);
      if (imgURL != null) {
         return new ImageIcon(imgURL, description);
      } else {
         System.err.println("Couldn't find file: " + path);
         return null;
      }
   }


///////////////////////////////////////////////////////////////////////////
// Time/Date functions
///////////////////////////////////////////////////////////////////////////

   public synchronized static void waitFor (int msecs) {
      try {
         Thread.sleep(msecs);
      }
      catch (InterruptedException e) {}
   }


//  public static String timeStamp(long t) {
//	long days, hours, minutes, seconds, millis;
//	String sDays, sHours, sMinutes, sSeconds, sMillis;
//	days = (t / 86400000); //- 10957;  // 0 = about 2000
//	t = t - ((days) * 86400000);
//	hours = t / 3600000;
//	t = t - (hours * 3600000);
//	minutes = t / 60000;
//	t = t - (minutes * 60000);
//	seconds = t / 1000;
//	t = t - (seconds * 1000);
//	millis = t;
//	if(days<10) sDays = "0" + String.valueOf(days);
//	else sDays = String.valueOf(days);
//	if(hours<10) sHours = "0" + String.valueOf(hours);
//	else sHours = String.valueOf(hours);
//	if(minutes<10) sMinutes = "0" + String.valueOf(minutes);
//	else sMinutes = String.valueOf(minutes);
//	if(seconds<10) sSeconds = "0" + String.valueOf(seconds);
//	else sSeconds = String.valueOf(seconds);
//	if(millis > 99) sMillis = String.valueOf(millis);
//	else if(millis > 9) sMillis = "0" + String.valueOf(millis);
//		 else sMillis = "00" + String.valueOf(millis);
//
//	return  sDays+"_"+sHours+"_"+sMinutes+"_"+sSeconds+"_"+sMillis;
//
//  }
/*
      public static String timeStamp () {
      SimpleDateFormat formatter =
            new SimpleDateFormat("yy_MMdd_HHmm_ss", Locale.getDefault());
      Date currentDate = new Date();
      String dateStr = formatter.format(currentDate);
      return dateStr;
   }


   public static String timeStampMilliSecs () {
      SimpleDateFormat formatter =
            new SimpleDateFormat("yy_MMdd_HHmm_ssSSS", Locale.getDefault());
      Date currentDate = new Date();
      String dateStr = formatter.format(currentDate);
      return dateStr;
   }


   public static String dateToday () {
      SimpleDateFormat formatter =
            new SimpleDateFormat("yy_MMM_dd", Locale.getDefault());
      Date currentDate = new Date();
      String dateStr = formatter.format(currentDate);
      return dateStr;
   }


   public static String minuteStamp () {
      SimpleDateFormat formatter =
            new SimpleDateFormat("yy_MMM_dd_HHmm", Locale.getDefault());
      Date currentDate = new Date();
      String minStr = formatter.format(currentDate);
      return minStr;
   }


   public static String dateTimeStamp () {
      SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss z", Locale.getDefault());
      Date currentDate = new Date();
      String dateStr = formatter.format(currentDate);
      return dateStr;
   }
*/

///////////////////////////////////////////////////////////
// Else...

   public static void mirrorImageArray (byte[] img, int w, int h) {
      /** @todo ?? */
   }


////////////////////////////////////////////////////////////////////////
// Properties
////////////////////////////////////////////////////////////////////////

   public static void listSystemProperties () {
      System.getProperties().list(System.out);
   }


   public static void loadProperties () {
      try {
         props.load(new FileInputStream(propsFilename));
      }
      catch (IOException e) {}
   }


   public static void saveProperties () {
      try {
         props.store(new FileOutputStream(propsFilename), "Properties");
      }
      catch (IOException e) {
         System.out.println("Exception saving properties: " + e.getMessage());
      }
   }


   public static String listProperties () {
      StringBuffer sBuff = new StringBuffer();
      props.list(System.out);
      Enumeration enumr = props.propertyNames();
      while (enumr.hasMoreElements()) {
         String key = (String) enumr.nextElement();
         String value = props.getProperty(key);
         sBuff.append(key + " = " + value + "\n");
      }
      return sBuff.toString();
   }


   public static String listSortedProperties () {
      StringBuffer sBuff = new StringBuffer();
      java.util.List list = new LinkedList();
      Enumeration enumr = props.propertyNames();
      while (enumr.hasMoreElements()) {
         String key = (String) enumr.nextElement();
         list.add(key);
      }
      Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
      for (Iterator i = list.iterator(); i.hasNext(); ) {
         String key = (String) i.next();
         String value = props.getProperty(key);
         sBuff.append(key + " = " + value + "\n");
      }
      return sBuff.toString();
   }

//---------------------------------------------------------------------

   public static void changeProp (String key, String s) {
      props.setProperty(key, s);
      saveProperties();
   }


   public static void changeProp (String key, int i) {
      props.setProperty(key, String.valueOf(i));
      saveProperties();
   }


   public static void changeProp (String key, float f) {
      props.setProperty(key, String.valueOf(f));
      saveProperties();
   }


   public static void changeProp (String key, boolean b) {
      props.setProperty(key, String.valueOf(b));
      saveProperties();
   }


   public static String getString (String key, String defaultValue) {
      if (props == null) {
         return defaultValue;
      }
      String s = props.getProperty(key);
      if (s == null) {
         return defaultValue;
      } else {
         return s;
      }
   }


   public static boolean getBoolean (String key, boolean defaultValue) {
      if (props == null) {
         return defaultValue;
      }
      String s = props.getProperty(key);
      if (s == null) {
         return defaultValue;
      } else {
         return s.equals("true");
      }
   }


   public static int getInt (String key, int defaultValue) {
      if (props == null) { //workaround for Netscape JIT bug
         return defaultValue;
      }
      String s = props.getProperty(key);
      if (s != null) {
         try {
            return Integer.decode(s)
                  .intValue();
         }
         catch (NumberFormatException e) {}
      }
      return defaultValue;
   }


   /** Looks up a real number in properties */
   public static double getDouble (String key, double defaultValue) {
      if (props == null) {
         return defaultValue;
      }
      String s = props.getProperty(key);
      Double d = null;
      if (s != null) {
         try {
            d = new Double(s);
         }
         catch (NumberFormatException e) {
            d = null;
         }
         if (d != null) {
            return (d.doubleValue());
         }
      }
      return defaultValue;
   }


   public static String rectangleToPropString (Rectangle r) {
      String s =
            String.valueOf(r.x) + "," + String.valueOf(r.y) + ","
            + String.valueOf(r.width) + "," + String.valueOf(r.height);
      return s;
   }


   public static String rectangleToString (Rectangle r) {
      if (r != null) {
         return (String.valueOf(r.x) + ", " + String.valueOf(r.y) + ", "
                 + String.valueOf(r.width) + ", " + String.valueOf(r.height));
      } else {
         return null;
      }
   }

// Utils

}
