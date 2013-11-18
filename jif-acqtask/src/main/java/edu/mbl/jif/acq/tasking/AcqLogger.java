/*
 * AcqLogger.java
 *
 * Created on February 3, 2007, 5:45 PM
 */
package edu.mbl.jif.acq.tasking;

import java.util.List;

/**
 *
 * @author GBH
 */
public enum AcqLogger {
  INSTANCE; // singleton
  private int level = 0;

  public void addLevel() {
    level++;
  }

  public void subLevel() {
    level--;
  }

  public void logEvent(List<String> eventStrs) {
     for (String string : eventStrs) {
        logEvent(string);
     }
  }
  
  public void logEvent(String eventStr) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < level; i++) {
      sb.append("  ");
    }
    sb.append(eventStr);
    System.out.println(sb.toString());
  }
  
  public static void main(String[] args) {
    AcqLogger a = AcqLogger.INSTANCE;
    a.logEvent("Event 0");
    a.addLevel();
    a.logEvent("Event 1");
    a.addLevel();
    a.logEvent("Event 2");
    a.logEvent("Event 2a");
    a.subLevel();
    a.logEvent("Event 3");
    a.subLevel();
    a.logEvent("Event 4");
  }

}
