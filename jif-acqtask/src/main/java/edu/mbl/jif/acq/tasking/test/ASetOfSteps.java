/*
 * TrivialSeriesOfSteps.java
 *
 * Created on January 28, 2007, 3:40 PM
 *
 */
package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AbstractStep;
import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.DataStoreDefn;
import edu.mbl.jif.acq.tasking.except.StepException;

/**
 *
 * @author GBH
 */
public class ASetOfSteps extends AbstractStep {

   //DataStoreDefn dsDefnTST =  new DataStoreDefn("setOfSteps", "~~", "__", "setOfStepsMeta");
   static final String DS_META = "setOfStepsMeta";

   public ASetOfSteps(String name) {
      super(name, null);
      super.setDataStoreDefn(new DataStoreDefn("setOfSteps", "~~", "__", DS_META));
      //this.setSeriesCapable(true);
   }

//   public ASetOfSteps(String name, List steps) {
//      super(name, null, steps);
//      setDataStoreDefn(new DataStoreDefn("setOfSteps", "~~", "__", DS_META));
//      this.setSeriesCapable(true);
//   }

//   @Override
//   public void doNextStep(Object obj) throws StepException {
//      AcqLogger.INSTANCE.logEvent("doNextStep: " + getName() + ": " + obj);
//      setCurrentValue(String.valueOf(obj));
//      setCurrentMetadata(obj);
//   }
//
   @Override
   public void doStep() throws StepException {
      AcqLogger.INSTANCE.logEvent("doStep: " + getName());
   }

   @Override
   public void setCurrentMetadata(Object obj) {
      getDataStoreDefn().getMetadata().setValue(DS_META, String.valueOf(obj));
   }

   @Override
   public int getExtent() {
      return getSubSteps().size();
   }

}
