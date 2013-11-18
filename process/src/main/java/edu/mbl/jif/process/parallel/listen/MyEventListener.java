package edu.mbl.jif.process.parallel.listen;

import java.util.EventObject;

public class MyEventListener implements CompletionListener {
  // ... code here
 
  //implement the required method(s) of the interface
  public void handleCompletionEvent(EventObject e)    {
    // handle the event any way you see fit
     System.out.println("An event happened.");
  }
}