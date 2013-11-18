package beans;

import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.apache.commons.beanutils.*;


/**
 Creates TestBean and XStreams it to XML...
 */
public class TestTestBean
{
   public TestTestBean () {
   }


   public static void main (String[] args) {
      TestBean tBean = new TestBean();
      XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library, uses JAXP DOM
      String xml = xstream.toXML(tBean);
      System.out.println(xml);
      try {
         saveTxtFile("testBean.xml", xml, false);
      }
      catch (IOException ex) {
         ex.printStackTrace();
      }

      // Deserializing an object back from XML
      String xml2 = null;
      try {
         xml2 = readTxtFile("testBean.xml");
         TestBean tBean2 = (TestBean) xstream.fromXML(xml2);
         System.out.println(tBean2.toString());
      }
      catch (IOException ex1) {
         ex1.printStackTrace();
      }
      DynaBean dynabean = new WrapDynaBean(new TestBean());
      dynabean.getDynaClass().getDynaProperties();
      DynaProperty[] dp = dynabean.getDynaClass().getDynaProperties();
      for (int i = 0; i < dp.length; i++) {
         DynaProperty d = dp[i];
         if (d == null) {
            System.out.println("** null property");
         } else {
            System.out.println(d.getName() + ": " + d.getType() + " = "
                  + dynabean.get(d.getName()));
         }
      }

   }
                  /**
     * Save a string to a text file
     */
    public static void saveTxtFile(String pathname, String data, boolean append) throws IOException
      {
        saveTxtFile(new File(pathname), data, append);
      }

    /**
     * Save a string to a text file
     */
    public static void saveTxtFile(File f, String data, boolean append) throws IOException
      {
        BufferedWriter out = new BufferedWriter(new FileWriter(f, append));
        out.write(data);
        out.close();
      }

    /**
     * Read a text file into a string
     */
    public static String readTxtFile(String pathname) throws IOException
      {
        return (readTxtFile(new File(pathname)));
      }

    /**
     * Read a text file into a string
     */
    public static String readTxtFile(File f) throws IOException
      {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String result = "";
        String str = null;
        while ((str = in.readLine()) != null) {
            result += str;
            result += "\n";
        }
        in.close();
        return (result);
      }
}
