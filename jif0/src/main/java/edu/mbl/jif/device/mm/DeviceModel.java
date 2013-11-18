package edu.mbl.jif.device.mm;

import com.jgoodies.binding.beans.Model;

/**
 * A Java object (model) corresponding to an MM device adapter
 *
 * @author GBH
 */
public abstract class DeviceModel extends Model {

   private final CMMCore mmc;
   private int propA;

   DeviceModel(CMMCore mmc) {
      this.mmc = mmc;
   }

   
   
// A trick for the moment... TODO remove
   interface CMMCore {

      void setProperty(String one, String two, String three);
   }
}
