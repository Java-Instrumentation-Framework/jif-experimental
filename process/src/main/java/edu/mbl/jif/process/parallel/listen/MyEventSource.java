
package edu.mbl.jif.process.parallel.listen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyEventSource {
   
  private List _listeners = new ArrayList();
  public synchronized void addEventListener(CompletionListener listener)  {
    _listeners.add(listener);
  }
  public synchronized void removeEventListener(CompletionListener listener)   {
    _listeners.remove(listener);
  }
 
  // call this method whenever you want to notify
  //the event listeners of the particular event
  private synchronized void fireEvent() {
    CompletionEvent event = new CompletionEvent(1);
    Iterator i = _listeners.iterator();
    while(i.hasNext())  {
      ((CompletionListener) i.next()).handleCompletionEvent(event);
    }
  }
   public static void main(String[] args) {
       // register the MyEventListener object with the MyEventSource object 
      // by call its addEventListener method.
      MyEventSource source = new MyEventSource();
      source.addEventListener(new MyEventListener());
      source.fireEvent();
      
   }
}