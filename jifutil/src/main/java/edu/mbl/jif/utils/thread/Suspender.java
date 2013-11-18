package edu.mbl.jif.utils.thread;

import java.util.Vector;
import java.util.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Suspender
{
   public Suspender () {
   }


   Vector suspendProc = new Vector(); // List of Suspendable processes

   public void addSuspendable (Suspendable sus) {
      suspendProc.add(sus);
   }


   public void removeSuspendable (Suspendable sus) {
      suspendProc.remove(sus);
   }


   public void notifySuspend () {
      Iterator itera = suspendProc.iterator();
      while (itera.hasNext()) {
         Suspendable sus = (Suspendable) itera.next();
         sus.suspend();
      }

   }


   public void notifyResume () {
      Iterator itera = suspendProc.iterator();
      while (itera.hasNext()) {
         Suspendable sus = (Suspendable) itera.next();
         sus.resume();
      }
   }

}
