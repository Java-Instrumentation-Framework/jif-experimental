/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface Stage extends Device
   {
   
      // Device API
      public DeviceType getType();
      static DeviceType Type = DeviceType.StageDevice;
   
      // Stage API
      public int setPositionUm(double pos);
      public int getPositionUm(double pos);
      public int setPositionSteps(long steps);
      public int getPositionSteps(long steps);
      public int setOrigin();
      public int getLimits(double lower, double upper);
   };