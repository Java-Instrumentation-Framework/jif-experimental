package net.html;

import static beans.TestTestBean.readTxtFile;
import static beans.TestTestBean.saveTxtFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;
import java.util.Vector;

import org.apache.ecs.html.A;
import org.apache.ecs.html.Body;
import org.apache.ecs.html.Font;
import org.apache.ecs.html.Head;
import org.apache.ecs.html.Html;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.Title;


public class GenerateReport {
   public static void main(String[] args) {
      Html  html = new Html();
      Head  head = new Head();
      Title title = new Title("Logging in HTML format");
      head.addElement(title);
      html.addElement(head);
      Body   body = new Body();
      Vector logs = new Vector();

      //		if (args.length < 1) {
      //			System.out.println("ERROR : Please provide path to log files");
      //			printUsage();
      //			System.exit(-1);
      //		}
      String logsFilesPath = "C:\\Docume~1\\GBH\\Desktop\\ecs";
      try {
         logs = getAllLogs(logsFilesPath);
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(-1);
      }
      // Setup Table...
      Table table = new Table();
      table.setBorder(0);
      TH       th = null;
      String[] tableHeaders =
         {
            "Serial Number", "Testcase Name", "Total variations", "Total variations ran",
            "Total Passed", "Total Failed", "Result", "Log File"
         };
      for (int j = 0; j < tableHeaders.length; j++) {
         th = new TH(tableHeaders[j]);
         table.addElement(th);
      }
      // Add the rows/entries
      TR       tr = null;
      TD       td = null;
      Font     font = null;
      Iterator it = logs.iterator();
      int      count = 1;
      while (it.hasNext()) {
         Log    log = (Log)it.next();
         String testcaseName = log.getTestcaseName();
         int    totalVariations = log.getTotalVariations();
         int    totalPassed = log.getTotalPassed();
         int    totalFailed = log.getTotalFailed();
         int    totalRan = log.getTotalRan();
         String result = log.getResult();
         String logFile = log.getLogFile();
         tr    = new TR();
         tr    = createTR(count++, testcaseName, totalVariations, totalRan, totalPassed,
               totalFailed, result, logFile);
         table.addElement(tr);
      }
      body.addElement(table);
      html.addElement(body);
      File f = new File("./result.html");
      try {         
         saveTxtFile (f, html.toString(), false); 
      } catch (IOException ex) {
         ex.printStackTrace();
      } 
//      //FileOutputStream fos = new FileOutputStream(f);
//      System.out.println(html.toString());
//      DataOutputStream output = null;
//      try {
//         output = new DataOutputStream(new BufferedOutputStream(
//                  new FileOutputStream("./result.html")));
//         output.write(html.toString().getBytes());
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
   }

   private static Vector getAllLogs(String path) throws Exception {
      Vector logs = new Vector();
      File   dir = new File(path);
      if (!dir.exists()) {
         throw new Exception("Directory " + dir + " does not exist");
      }
      if (!dir.isDirectory()) {
         throw new Exception("Passed argument " + dir + " is not a directory");
      }

      String[] list = dir.list();
      for (int i = 0; i < list.length; i++) {
         if (!list[i].endsWith(".log")) {
            continue;
         }
         String          logFilePath = path + File.separator + list[i];
         File            logFile = new File(logFilePath);
         DataInputStream input =
            new java.io.DataInputStream(new java.io.BufferedInputStream(
                  new FileInputStream(logFilePath)));
         String          line = input.readLine(); // Read one line at a time
         Log             log = new Log();
         line = line.substring(12);
         // Get testcase name from first line of logfile
         log.setTestcaseName(line);
         while (line != null) // Iterate for all lines in the file
          {
            line = input.readLine();
            if (line.startsWith("SUMMARY")) {
               break;
            }
         }
         line    = input.readLine();
         line    = input.readLine();
         log.setTotalVariations(Integer.parseInt(line.substring(line.indexOf(':') + 2)));
         line    = input.readLine();
         line    = input.readLine();
         log.setTotalRan(Integer.parseInt(line.substring(line.indexOf(':') + 2)));
         line    = input.readLine();
         line    = input.readLine();
         log.setTotalPassed(Integer.parseInt(line.substring(line.indexOf(':') + 2)));
         line    = input.readLine();
         line    = input.readLine();
         log.setTotalFailed(Integer.parseInt(line.substring(line.indexOf(':') + 2)));
         line    = input.readLine();
         line    = input.readLine();
         log.setResult(line.substring(line.indexOf(':') + 1));
         log.setLogFile(logFilePath);
         logs.add(log);
      }
      return logs;
   }

   private static TR createTR(int serialNumber, String testcaseName, int totalVariations,
      int totalRan, int totalPassed, int totalFailed, String result, String logfile) {
      TR tr = new TR();
      tr = new TR();
      TD td = new TD();
      td.addElement("" + serialNumber);
      tr.addElement(td);
      td = new TD();
      td.addElement(testcaseName);
      tr.addElement(td);
      td = new TD();
      td.addElement("" + totalVariations);
      tr.addElement(td);
      td = new TD();
      td.addElement("" + totalRan);
      tr.addElement(td);
      td = new TD();
      td.addElement("" + totalPassed);
      tr.addElement(td);
      td = new TD();
      td.addElement("" + totalFailed);
      tr.addElement(td);
      td = new TD();
      td.addElement("" + result);
      tr.addElement(td);
      td = new TD();
      A a = new A(logfile, "See log");
      a.addAttribute("href", logfile);
      a.setTitle("See log");
      td.addElement(a);
      tr.addElement(td);
      return tr;
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
   private static void printUsage() {
      System.out.println("This is an utility to generate HTML reports using ECS API.");
      System.out.println("USAGE : ");
      System.out.println("java com.ecs.log.GenerateReport <path_to_log_files>");
      System.out.println(
         "where <path_to_log_files> is absolute/relative path to the folder which contains log files.");
   }
}
