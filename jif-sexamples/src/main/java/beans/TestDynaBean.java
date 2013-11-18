package beans;

import java.util.Date;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.apache.commons.beanutils.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TestDynaBean
{
   public TestDynaBean () {
   }


   public static void main (String[] args) {

      // In...
      DynaProperty[] properties = {
            new DynaProperty("loginDate", Date.class),
            new DynaProperty("name", String.class),
            new DynaProperty("password", String.class)};

      DynaClass userDynaClass = new BasicDynaClass("user", null, properties);

      DynaBean user = null;
      try {
         user = userDynaClass.newInstance();
      }
      catch (InstantiationException ex) {
      }
      catch (IllegalAccessException ex) {
      }

      user.set("name", "jason");
      user.set("password", "secret");
      user.set("loginDate", new Date());

      org.pf.joi.Inspector.inspectWait(user);

      // Out...
      user.getDynaClass().getDynaProperties();
      DynaProperty[] dp = user.getDynaClass().getDynaProperties();
      for (int i = 0; i < dp.length; i++) {
         DynaProperty d = dp[i];
         if (d == null) {
            System.out.println("** null property");
         } else {
            System.out.println(d.getName() + ": " + d.getType() + " = "
                  + user.get(d.getName()));
         }
      }
      XStream xstream = new XStream(new DomDriver());
      String xml = xstream.toXML(user);
      System.out.println(xml);
      try {
         saveTxtFile("dynaBean.xml", xml, false);
      }
      catch (IOException ex) {
         ex.printStackTrace();
      }

      //Bean b = (Bean) user;


      DynaBean db = (DynaBean) xstream.fromXML(xml);
      org.pf.joi.Inspector.inspectWait(db);


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
