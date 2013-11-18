package edu.mbl.jif.camera;

import java.io.*;
import java.util.*;

public class DataRecorder {
  /* Use:
  DataRecorder dRec = new DataRecorder("filename.txt");
  dRec.add("");
  dRec.flushToFile();
  */
    
  String fileName;
  Vector vector = new Vector();

  public DataRecorder(String _fileName) {
        fileName = _fileName;
  }

  public void add(String s) {
        vector.addElement(s);
  }
  public void add(String s, int n) {
        vector.addElement( pad(s, n) );
  }

  public String pad(String s, int len) {
        while (s.length() < len) {
          s = s + " ";
        }
        return s;
  }

  public void flushToFile() {
        if (vector.isEmpty())
          return;
        PrintWriter out = null;
        try {
          out = new PrintWriter(new FileWriter(fileName, true));
          Iterator iterator = vector.iterator();
          while (iterator.hasNext()) {
                out.println(iterator.next().toString());
          }
          vector.removeAllElements();
        } catch (ArrayIndexOutOfBoundsException e) {
          System.err.println("Caught ArrayIndexOutOfBoundsException: "
                + e.getMessage());
        } catch (IOException e) {
          System.err.println("Caught IOException: " + e.getMessage());
        } finally {
          if (out != null) {
                out.close();
          }
        }
  }
}
