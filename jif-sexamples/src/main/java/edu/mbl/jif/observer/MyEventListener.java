package edu.mbl.jif.observer;

import java.util.EventObject;

public class MyEventListener implements MyEventClassListener {
  // ... code here
 
  //implement the required method(s) of the interface
  public void handleMyEventClassEvent(EventObject e)    {
    // handle the event any way you see fit
     System.out.println("An event happened.");
  }
}