package edu.mbl.jif.device;

import java.util.*;

/**
 * <p>Title: AsynchDevices</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

// @todo merge with Suspendable...
public class AsynchDevices
{
   // @todo AsynchDevices - finish
   
   // Registry for Asychronous Devices, enabling suspension
   // and resumption of their activities / threads / services.

   public AsynchDevices () {
   }

   static ArrayList devices = new ArrayList();

   public static void register (SuspendableDevice device) {
      devices.add(device);
   }


   public static void suspend () {
      Iterator iter = devices.iterator();
      while (iter.hasNext()) {
         SuspendableDevice device = (SuspendableDevice) iter.next();
         device.suspend();
      }
   }


   public static void resume () {
      Iterator iter = devices.iterator();
      while (iter.hasNext()) {
         SuspendableDevice device = (SuspendableDevice) iter.next();
         if (device.isSuspended()) {
            device.resume();
         }
      }
   }


   public static void terminateAll () {
      Iterator iter = devices.iterator();
      while (iter.hasNext()) {
         Device device = (Device) iter.next();
         device.terminate();
      }
   }

   public static void clearRegistry () {
   devices.clear();
}

}

