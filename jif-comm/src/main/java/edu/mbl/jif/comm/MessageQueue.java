package edu.mbl.jif.comm;

import java.util.LinkedList;

/**
 * Class to synchronize values received from serial connection
 */


public class MessageQueue
{
   public MessageQueue () {
   }


   /** Queue containing responses */
   public LinkedList queue = new LinkedList();

   public void clearMessages () {
      synchronized (queue) {
         while (!queue.isEmpty()) {
            queue.removeFirst();
         }
      }
   }


   /**
    * Add message to queue
    * @param txt The message to add
    */
   public void addMessage (String txt) {
      synchronized (queue) {
         queue.addLast(txt);
      }
   }


   /**
    * Add characters to queue
    * @param chars Array of char to add
    */
   public void addChars (char[] chars) {
      synchronized (queue) {
         queue.addLast(chars);
      }
   }


   /**
    * Get message from queue as a char array
    * @return char[]
    */
   public char[] getChars () {
      boolean empty = true;
      char[] chars = null;
      synchronized (queue) {
         empty = queue.isEmpty();
      }
      if (!empty) {
         synchronized (queue) {
            chars = (char[]) queue.removeFirst();
         }
      }
      return chars;
   }


   /**
    * Size of the queue
    * @return Number of messages in queue
    */
   public int size () {
      synchronized (queue) {
         return queue.size();
      }
   }


   /**
    * Check if queue is empty
    * @return True if empty, false otherwise
    */
   public boolean isEmpty () {
      synchronized (queue) {
         return queue.isEmpty();
      }
   }


   /**
    * Get String message from queue
    * @return The message as a string
    */
   public String getMessage () {
      String msg = "";
      synchronized (queue) {
         if (!queue.isEmpty()) {
            Object o = queue.removeFirst();
            if (o instanceof char[]) {
               char[] chars = (char[]) o;
               if (chars != null) {
                  msg = new String(chars);
               }
            }
            if (o instanceof String) {
               msg = (String) o;
            }
         }
         return msg;
      }
   }

}
