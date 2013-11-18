/*
 * TimedSequence.java
 *
 * Created on January 28, 2007, 1:43 PM
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import java.util.Date;
import java.util.ListIterator;

/**
 *
 * @author GBH
 */
public class TimedSequence extends StepIterator implements Sequence {

   Date sequenceStartTime;
   int interval;
   int iterations;
   /*
    * Not actually timing yet... !!!
    */
   public TimedSequence(String name, int interval, int iterations, AbstractStep step) {
      super(name);
      super.addSubStep(step);
      this.interval = interval;
      this.iterations = iterations;
   }

   @Override
   public int getExtent() {
      return iterations;
   }

   @Override
   public void setIterations(int iterations) {
      this.iterations = iterations;
   }

   public int getInterval() {
      return interval;
   }

   public void setInterval(int interval) {
      this.interval = interval;
   }

   @Override
   public Date getBeginTime() {
      return sequenceStartTime;
   }

   @Override
   public void setBeginTime(Date beginTime) {
      this.sequenceStartTime = beginTime;
   }

   /**
    * Execute all nested Steps.
    *
    * @throws StepException - if one of the nested Steps fails.
    * @throws BuildException //
    */
   @Override
   public void perform() throws StepException {
      AcqLogger.INSTANCE.logEvent("Started: " + getName());

      for (int i = 0; i < iterations; i++) {
         AcqLogger.INSTANCE.logEvent("TimePoint (" + getName() + "): " + i);
         //AcqLogger.INSTANCE.addLevel();
         //
         ListIterator stepIterator = getSubSteps().listIterator();
         while (stepIterator.hasNext()) {
            // should only be one step (or one seriesOfSteps) here...
            AbstractStep nextStep = (AbstractStep) stepIterator.next();
            // AcqLogger.INSTANCE.logEvent(">> performing step: " + next.toString());
            getContext().put("TimePoint", i);
            nextStep.enter(getContext());
            nextStep.perform();
            nextStep.exit();
         }
         //
         //AcqLogger.INSTANCE.subLevel();
      }
      AcqLogger.INSTANCE.logEvent("Complete: " + getName());
   }

   @Override
   public void doNextStep(int i) throws StepException {
      AcqLogger.INSTANCE.logEvent("doNextStep: " + getName() + ": " + i);
      setCurrentValue(String.valueOf(i));
      setCurrentMetadata(i);
      setCurrentValue(String.valueOf(i));
   }
   
   @Override
   public void doStep() throws StepException {
   }

   @Override
   public void setCurrentMetadata(Object obj) {
   }

}