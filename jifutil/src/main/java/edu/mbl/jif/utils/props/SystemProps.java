package edu.mbl.jif.utils.props;

import java.util.Properties;

/**
 * Try adding one or more item(s) to class path.
 */
public class SystemProps {

  static Properties p = System.getProperties();


  public static void appendToClassPath(String path) {
     p.setProperty("java.class.path", getClassPath() + ';'
        + path);
  }

  public static void main(String[] argv) {
    System.out.println("System Properties:");
    System.out.println("java.class.path now = " + getClassPath());
    p.setProperty("java.class.path", getClassPath() + ';'
        +  "C:\\Progra~1\\Java\\jdk1.6.0_06\\lib\\tools.jar");
    System.out.println("java.class.path now = " + getClassPath());
    try {
      Class.forName("sun.tools.javac.javac");
    } catch (Exception e) {
      System.err.println(e);
      return;
    }
    System.out.println("Got it!!");
  }

  static String getClassPath() {
    return p.getProperty("java.class.path", null);
  }
}
