/*
 * StepIterator.java
 *
 * Created on January 28, 2007, 1:42 PM
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import edu.mbl.jif.acq.tasking.except.StepSetupException;
import java.util.List;
import java.util.ListIterator;

/**
 * StepIterator requires a Device in order to be seriesCapable; It may or may not require a Device
 * in order to be capable (of a single interation) (e.g. a Stage controller is required to acquire a
 * Z-series, but it is not required for a single acquisition (at the current stage position)
 *
 * @author GBH
 */
abstract public class StepIterator extends AbstractStep {

   private boolean seriesCapable;
   //Device device;
   private int iterations = 1;
   private int stepsDone = 0;
   private List stepsList;
   AcquisitionContext context;

   public StepIterator() {
      super("none");
   }

   public StepIterator(String name) {
      super(name);
   }

   public StepIterator(String name, DataStoreDefn dSDefn) {
      super(name, dSDefn);
   }

   public StepIterator(String name, DataStoreDefn dSDefn, int steps) {
      super(name, dSDefn);
      this.iterations = steps;
   }

   public boolean isSeriesCapable() {
      return true;
      //    if(getDevice() == null) return false;
      //        return getDevice().isSeriesCapable();
   }

   public void setSeriesCapable(boolean seriesCapable) {
      this.seriesCapable = seriesCapable;
   }

   public void setIterations(int iterations) throws StepSetupException {
      if (isSeriesCapable()) {
         this.iterations = iterations;
      } else {
         throw new StepSetupException("Attempt to setIterations > 1 when not SeriesCapable in "
                 + getName());
      }
   }

//   public void setSteps(List stepsList) throws StepSetupException {
//      if (isSeriesCapable()) {
//         this.stepsList = stepsList;
//      } else {
//         throw new StepSetupException("Attempt to setStepsList when not SeriesCapable in "
//                 + getName());
//      }
//   }
   @Override
   public void perform() throws StepException {
      // do tasks steps times
      for (int i = 0; i < iterations; i++) {
         doNextStep(i);
         doSubTasks();
      }
   }

   private void doSubTasks() {
      ListIterator listIteratorName = getSubSteps().listIterator();
      while (listIteratorName.hasNext()) {
         AbstractStep next = (AbstractStep) listIteratorName.next();
         //AcqLogger.INSTANCE.logEvent("doSubTask perform next: " + next.getName());
         try {
            next.enter(context);
            next.perform();
            next.exit();
         } catch (StepException ex) {
            ex.printStackTrace();
         }
      }
   }

   abstract public void doNextStep(int i) throws StepException;
//   abstract public void doNextStep(Object obj) throws StepException;
}
