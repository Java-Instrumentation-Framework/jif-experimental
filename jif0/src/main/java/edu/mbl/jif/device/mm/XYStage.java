package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public    interface XYStage extends Device
   {
      static DeviceType Type = DeviceType.XYStageDevice;
      // XYStage API
      public int setPositionUm(double x, double y);
      public int getPositionUm(double x, double y);
      public int setPositionSteps(long x, long y);
      public int getPositionSteps(long x, long y);
      public int setOrigin();
      public int getLimits(double xMin, double xMax, double yMin, double yMax);
   };