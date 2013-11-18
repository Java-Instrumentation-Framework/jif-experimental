/*
 * TrivialSeriesTask.java
 *
 * Created on January 28, 2007, 3:40 PM
 *
 */
package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.AcquisitionContext;
import edu.mbl.jif.acq.tasking.DataStoreDefn;
import edu.mbl.jif.acq.tasking.StepListIterator;
import edu.mbl.jif.acq.tasking.except.StepException;
//import edu.mbl.jif.stage.StageZModel;
import java.util.List;

/**
 *
 * @author GBH
 */
public class ZScanSeriesTask extends StepListIterator {

   static final String NAME = "ZScanSeriesTask";
   static final String DS_NAME = "zSeries1";
   static final String META = "zMeta";
   DataStoreDefn dsDefn = new DataStoreDefn(DS_NAME, "|", "|", META);
   //StageZModel zModel;

   /**
    * Creates a new instance of TrivialSeriesTask
    */
   public ZScanSeriesTask() {
      super(NAME, null);
      setDataStoreDefn(dsDefn);
      this.setSeriesCapable(true);
   }

//   public ZScanSeriesTask(StageZModel zModel) {
//      super(NAME, null);
//      this.zModel = zModel;
//      setDataStoreDefn(dsDefn);
//      this.setSeriesCapable(true);
//   }

   public ZScanSeriesTask(List steps) {
      super(NAME + "ListSteps", null, steps);
      setDataStoreDefn(dsDefn);
      this.setSeriesCapable(true);
   }

   @Override
   public void enter(AcquisitionContext context) {
      super.enter(context);
      // preposition for Z-scan
      AcqLogger.INSTANCE.logEvent("enter: " + getName());
   }

   @Override
   public void initializeSelf() {
      AcqLogger.INSTANCE.logEvent("initializeSelf: " + getName());
      // prepare for ZScan, backlash compensation...
   }



   @Override
   public void doNextStep(Object obj) throws StepException {
      AcqLogger.INSTANCE.logEvent("doNextStep: " + getName() + ": " + obj);
      setCurrentValue(String.valueOf(obj));
      setCurrentMetadata(obj);
      //this.setCurrentValue("1");
   }

   @Override
   public void doStep() throws StepException {
      AcqLogger.INSTANCE.logEvent("doStep: " + getName());
   }

   @Override
   public void exit() {
      super.exit();
      // move to rest position
   }

   @Override
   public void terminateSelf() {
      AcqLogger.INSTANCE.logEvent("terminateSelf: " + getName());
      // return to rest position
   }

   @Override
   public void setCurrentMetadata(Object obj) {
      getDataStoreDefn().getMetadata().setValue(META, String.valueOf(obj));
   }
}
