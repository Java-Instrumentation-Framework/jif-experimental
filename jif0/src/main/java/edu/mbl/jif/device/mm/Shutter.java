package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface Shutter extends Device
   {
      static DeviceType Type = DeviceType.ShutterDevice;   
      // Device API
      public DeviceType getType();
   
      // Shutter API
      public int setOpen(boolean open);
      public int getOpen(boolean open);
      public int fire(double deltaT);
   };

