package edu.mbl.jif.utils.props;

import java.util.*;
import java.io.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: Load and save properties to XML format (req 1.5 or >)</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ProperiesXML
{
   public ProperiesXML () {
   }


   public static void LoadSampleXML (String args[]) throws Exception {
      Properties prop = new Properties();
      FileInputStream fis = new FileInputStream("sampleprops.xml");
      prop.loadFromXML(fis);
      prop.list(System.out);
      System.out.println("\nThe foo property: " + prop.getProperty("foo"));
   }


   public static void StoreXML (String args[]) throws Exception {
      Properties prop = new Properties();
      prop.setProperty("one-two", "buckle my shoe");
      prop.setProperty("three-four", "shut the door");
      prop.setProperty("five-six", "pick up sticks");
      prop.setProperty("seven-eight", "lay them straight");
      prop.setProperty("nine-ten", "a big, fat hen");
      FileOutputStream fos = new FileOutputStream("rhyme.xml");
      prop.storeToXML(fos, "Rhyme");
      fos.close();
   }
}
