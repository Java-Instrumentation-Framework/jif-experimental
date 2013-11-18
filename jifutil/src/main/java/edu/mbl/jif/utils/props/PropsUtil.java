package edu.mbl.jif.utils.props;

import java.io.*;
import java.util.*;

import java.awt.*;

import edu.mbl.jif.utils.*;


/**
 * <p>Title: Properties Utilities</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */


/*
 Examples:

   // Get a system property
    String dir = System.getProperty("user.dir");
   // Set a system property
   String previousValue = System.setProperty("application.property", "newValue");

   // from the Command Line
   java -Dmy.prop="my value" MyApp
   // Get the value of the system property
   String prop = System.getProperty("my.prop");

   // Get all system properties
   Properties props = System.getProperties();
   // Enumerate all system properties
   Enumeration enum = props.propertyNames();
   for (; enum.hasMoreElements(); ) {
        // Get property name
        String propName = (String)enum.nextElement();
        // Get property value
        String propValue = (String)props.get(propName);
   }

 */

public class PropsUtil
{
   public static Properties props = new Properties();


   public PropsUtil () {
   }


   ////////////////////////////////////////////////////////////////////////
   // Properties
   // Things stored in Properties...
   // Serial port assignments: port_variLC, port_stage, port_shutter, ...
   // Frame positions
   // Paths
   // Configuration variables
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


   public static void loadProperties () {
      try {
         props.load(new FileInputStream("PSjProps"));
      }
      catch (IOException e) {
      }
   }


   public static void saveProperties () {
      try {
         props.store(new FileOutputStream("PSjProps"), "PSj Properties");
      }
      catch (IOException e) {
         //psj.PSjUtils.event("Exception saving properties: " + e.getMessage());
      }
   }


   public static void resetProperties () {
      try {
         FileUtil.fileCopy(".\\backupPSjProps.", ".\\PSjProps.");
      }
      catch (IOException e) {
         //DialogBoxI.boxError("Error resetting defaults", e.getMessage());
          System.err.println("Error resetting defaults" + e.getMessage());
      }
      loadProperties();
      System.out.println("Properties reset to defaults");
      //psj.PSjUtils.event("Properties reset to defaults");
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


   public static String rectangleToString (Rectangle r) {
      if (r != null) {
         return (String.valueOf(r.x) + ", " + String.valueOf(r.y) + ", "
               + String.valueOf(r.width) + ", " + String.valueOf(r.height));
      } else {
         return null;
      }
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


   /*   public static String getString(String key, String defaultValue) {
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
      public static boolean getBoolean(String key, boolean defaultValue) {
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
      public static int getInt(String key, int defaultValue) {
      if (props == null) {  //workaround for Netscape JIT bug
      return defaultValue;
      }
      String s = props.getProperty(key);
      if (s != null) {
      try {
      return Integer.decode(s)
           .intValue();
      } catch (NumberFormatException e) {}
      }
      return defaultValue;
      }
      public static double getDouble(String key, double defaultValue) {
      if (props == null) {
      return defaultValue;
      }
      String s = props.getProperty(key);
      Double d = null;
      if (s != null) {
      try {
      d = new Double(s);
      } catch (NumberFormatException e) {
      d = null;
      }
      if (d != null) {
      return (d.doubleValue());
      }
      }
      return defaultValue;
      }
    */
   public static String rectangleToPropString (Rectangle r) {
      String s = String.valueOf(r.x) + "," + String.valueOf(r.y) + ","
            + String.valueOf(r.width) + "," + String.valueOf(r.height);
      return s;
   }

// ++++
//  public static String rectangleFromPropString(Rectangle r) {
//    String s =
//      String.valueOf(r.x) + "," + String.valueOf(r.y) + "," +
//      String.valueOf(r.width) + "," + String.valueOf(r.height);
//    return s;
//  }

}
