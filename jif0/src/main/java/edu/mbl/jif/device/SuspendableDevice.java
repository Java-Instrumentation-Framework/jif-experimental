package edu.mbl.jif.device;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author GBH
 * @version 1.0
 */

// Asychronous Device which has  activities / threads / services
// which can be suspended and resumed in order to conserve resources.

public interface SuspendableDevice
{
   boolean suspended = false;

   public boolean isSuspended ();

   public void suspend ();

   public void resume ();

}
