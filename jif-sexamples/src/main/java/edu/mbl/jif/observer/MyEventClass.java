/*
 from http://castever.wordpress.com/2008/07/31/how-to-create-your-own-events-in-java/
 */
package edu.mbl.jif.observer;

public class MyEventClass extends java.util.EventObject {

   //here's the constructor
   public MyEventClass(Object source) {
      super(source);
   }
}
