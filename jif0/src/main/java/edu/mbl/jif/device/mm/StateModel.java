package edu.mbl.jif.device.mm;

/**
 * State device API, e.g. filter wheel, objective turret, etc.
 */
public abstract class StateModel extends DeviceModel implements State {
   // MMDevice API

   static DeviceType Type = DeviceType.StateDevice;

   int position;
   // MMStateDevice API
   
//      public int setPosition(long pos);
//      public int setPosition(String label);
//      public int getPosition(long pos);
//      public int getPosition(String label);
//      public int getPositionLabel(long pos, String label);
//      public int getLabelPosition(String label, long pos);
//      public int setPositionLabel(long pos, String label);
//      public int numberOfPositions();
   
   public StateModel(CMMCore mmc) {
      super(mmc);
   }

   @Override
   public int setPosition(long pos) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int setPosition(String label) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int getPosition(long pos) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int getPosition(String label) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int getPositionLabel(long pos, String label) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int getLabelPosition(String label, long pos) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int setPositionLabel(long pos, String label) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public int getNumberOfPositions() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
};