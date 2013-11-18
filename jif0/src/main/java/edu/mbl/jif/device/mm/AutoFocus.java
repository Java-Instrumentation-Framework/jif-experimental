/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface AutoFocus extends Device
   {
      static final DeviceType Type = DeviceType.AutoFocusDevice;
    // MMDevice API
      public DeviceType getType();

      // AutoFocus API
      public int setContinuousFocusing(boolean state);
      public int getContinuousFocusing(boolean state);
      public int focus();
      public int getFocusScore(double score);
   };