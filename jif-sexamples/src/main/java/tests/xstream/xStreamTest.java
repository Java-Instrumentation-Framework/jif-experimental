package tests.xstream;

import edu.mbl.jif.io.*;
import java.io.IOException;

import org.pf.joi.Inspector;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
//import edu.mbl.jif.utils.
import edu.mbl.jif.utils.FileUtil;


public class xStreamTest
{
   xStreamTest () {

      // instantiate the XStream class:
      // XStream xstream = new XStream(); // requires XPP3 library
   
      XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library, uses JAXP DOM

     // xstream.alias("psParms", psj.PolStack.PolStackParms.class);
      //xstream.alias("phonenumber", xPhoneNumber.class);

      // Serializing an object to XML
     // psj.PolStack.PolStackParms psp = new psj.PolStack.PolStackParms("test");
      //String xml = xstream.toXML(psp);
     // System.out.println(xml);
     // try {
     //  FileUtil.saveTxtFile("psParms.xml", xml, false);
     // }
     // catch (IOException ex) {
     //    ex.printStackTrace();
     // }

      // Deserializing an object back from XML
      String xml2 = null;
      try {
         xml2 = FileUtil.readTxtFile("psParms.xml");
        // psj.PolStack.PolStackParms psp0 = (psj.PolStack.PolStackParms) xstream.fromXML(xml2);
        // System.out.println(psp0.getList());
      }
      catch (IOException ex1) {
         ex1.printStackTrace();
      }
   }


   void xStreamTestOLD () { // was a class

      // instantiate the XStream class:
      // XStream xstream = new XStream(); // requires XPP3 library
      XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library, uses JAXP DOM

      xstream.alias("person", xPerson.class);
      xstream.alias("phonenumber", xPhoneNumber.class);

      // Serializing an object to XML
      xPerson joe = new xPerson("Joe", "Walnes");
      joe.setPhone(new xPhoneNumber(123, "1234-456"));
      joe.setFax(new xPhoneNumber(123, "9999-999"));
      String xml = xstream.toXML(joe);
      System.out.println(xml);

      /* The resulting XML looks like this:
       <person>
        <firstname>Joe</firstname>

        <lastname>Walnes</lastname>
        <phone>
          <code>123</code>
          <number>1234-456</number>
        </phone>
        <fax>
          <code>123</code>
          <number>9999-999</number>
        </fax>
       </person>
       */

// Deserializing an object back from XML
      xPerson newJoe = (xPerson) xstream.fromXML(xml);
      System.out.println("Name: " +
                         newJoe.getFirstname() + " " + newJoe.getLastname());
      //System.out.println(newJoe.getFax());

   }


   public static void main (String[] args) {
     // new xStreamTest();
    // psj.PolStack.PolStackParms psp = new psj.PolStack.PolStackParms("test");
     //Inspector.inspectWait(psp);
   try {
      //ObjectStoreXML.write(psp, "parmstest.prm");
   }
   catch (Throwable ex) {
      ex.printStackTrace();
   }
   //PolStackParms psp2 = null;
   try {
     // psp2 = (psj.PolStack.PolStackParms) ObjectStoreXML.read("parmstest.prm");
   }
   catch (Throwable ex1) {
      ex1.printStackTrace();
   }
     //Inspector.inspectWait(psp2);

   }
}
