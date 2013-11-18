/*
 * TrivialSeriesOfSteps.java
 *
 * Created on January 28, 2007, 3:40 PM
 *
 */
package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.DataStoreDefn;
import edu.mbl.jif.acq.tasking.StepListIterator;
import edu.mbl.jif.acq.tasking.except.StepException;
import java.util.List;

/**
 *
 * @author GBH
 */
public class TrivialSeriesOfSteps extends StepListIterator {

   //DataStoreDefn dsDefnTST =  new DataStoreDefn("tSeries1", "~~", "__", "metaname1");
   /**
    * Creates a new instance of TrivialSeriesOfSteps
    */
   public TrivialSeriesOfSteps() {
      super("TrivialSeriesOfSteps", null);
      super.setDataStoreDefn(new DataStoreDefn("tSeries1", "~~", "__", "tSeriesMeta"));
      this.setSeriesCapable(true);
   }

   public TrivialSeriesOfSteps(List steps) {
      super("TrivialSeriesofStepsList", null, steps);
      setDataStoreDefn(new DataStoreDefn("tSeriesSteps", "~~", "__", "tSeriesStepsMeta"));
      this.setSeriesCapable(true);
   }

   @Override
   public void doNextStep(Object obj) throws StepException {
      AcqLogger.INSTANCE.logEvent("doNextStep: " + getName() + ": " + obj);
      setCurrentValue(String.valueOf(obj));
      setCurrentMetadata(obj);
   }

   @Override
   public void doStep() throws StepException {
      AcqLogger.INSTANCE.logEvent("doStep: " + getName());
   }

   @Override
   public void setCurrentMetadata(Object obj) {
      getDataStoreDefn().getMetadata().setValue("tSeriesMeta", String.valueOf(obj));
   }
}
