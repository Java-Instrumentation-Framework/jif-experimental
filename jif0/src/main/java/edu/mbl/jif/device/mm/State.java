package edu.mbl.jif.device.mm;

 /**
    * State device API, e.g. filter wheel, objective turret, etc.
    */
   public interface State extends Device
   {
      // MMDevice API
      static DeviceType Type = DeviceType.StateDevice;
      
      // MMStateDevice API
      public int setPosition(long pos);
      public int setPosition(String label);
      public int getPosition(long pos);
      public int getPosition(String label);
      public int getPositionLabel(long pos, String label);
      public int getLabelPosition(String label, long pos);
      public int setPositionLabel(long pos, String label);
      public int getNumberOfPositions();
   };