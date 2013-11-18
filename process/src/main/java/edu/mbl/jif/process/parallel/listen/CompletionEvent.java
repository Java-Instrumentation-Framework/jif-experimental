/*
 from http://castever.wordpress.com/2008/07/31/how-to-create-your-own-events-in-java/
 */
package edu.mbl.jif.process.parallel.listen;

public class CompletionEvent extends java.util.EventObject {
   
   //here's the constructor
   public CompletionEvent(int status) {
      super(status);
   }
}
