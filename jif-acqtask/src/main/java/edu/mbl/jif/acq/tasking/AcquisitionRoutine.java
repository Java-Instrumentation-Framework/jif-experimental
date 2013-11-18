/*
 * AcquisitionProcess.java
 * Created on January 28, 2007, 2:49 PM
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import java.util.ArrayList;
import java.util.List;

/**
 * An AcquisitionProcess is is executed by AcqRoutineExecutor
 *
 * @author GBH
 */
abstract public class AcquisitionRoutine {

   /**
    * Creates a new instance of AcquisitionProcess
    */
   String name;
   StepIterator steps;
   String currentValue;
   AcquisitionContext context;
   // depth or levels ... are dimensions ?

   public AcquisitionRoutine(String name, StepIterator steps) {
      this(name);
      this.steps = steps;
   }

   public AcquisitionRoutine(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public StepIterator getSteps() {
      return steps;
   }

   public void setSteps(StepIterator steps) {
      this.steps = steps;
   }

   public void commence() {
      AcqLogger.INSTANCE.logEvent("Commence: " + name + "====================================");
      context = new AcquisitionContext(name);
   }
   //-------------------- 
   List<AcqDimension> dimensions = new ArrayList<AcqDimension>();

   public List<AcqDimension> findDimensionalExtents() {
      return findDimensionalExtents(steps, new ArrayList<AcqDimension>());
   }

   public List<AcqDimension> findDimensionalExtents(Step step, List<AcqDimension> dimensions) {
      int extent = step.getExtent();
      AcqDimension dim = new AcqDimension(step.getName(), extent);
      dimensions.add(dim);
      List<Step> subSteps = step.getSubSteps();
      if (subSteps.size() > 0) {
         for (Step aStep : subSteps) {
         findDimensionalExtents(aStep, dimensions);
         }
      }
      return dimensions;
   }
//   public List<AcqDimension> findDimensionalExtents(Step step, List<AcqDimension> dimensions) {
//      int iterations;
//      if (StepIterator.class.isAssignableFrom(step.getClass())) {
//         iterations = ((StepIterator) step).getIterations();
//      } else {
//         iterations = step.getSubSteps().size();
//         if(iterations==0) iterations = 1;
//      }
//      AcqDimension dim = new AcqDimension(step.getName(), iterations);
//      dimensions.add(dim);
//      List<Step> subSteps = step.getSubSteps();
//      if (subSteps.size() > 0) {
//         for (Step aStep : subSteps) {
//         findDimensionalExtents(aStep, dimensions);
//         }
//      }
//      return dimensions;
//   }
   
   
  public void showDimensionalExtents(List<AcqDimension> dimensions) {
     System.out.println("[[ DimensionalExtents ");
     for (AcqDimension acqDimension : dimensions) {
        System.out.println("    " + acqDimension.getName() + " = " 
                + acqDimension.getIterations());
     }
     System.out.println("]] DimensionalExtents \n");
     
  }
   public List<AcqDimension> getDimensions() {
      return dimensions;
   }

   //-----------------------
   abstract public void prepare();

   public void initialize() {
      if (steps == null) {
         return;
      }
      AcqLogger.INSTANCE.logEvent("initialize: " + name);
      steps.initialize();
   }

   public void perform() throws StepException {
      if (steps == null) {
         return;
      }
      AcqLogger.INSTANCE.logEvent("perform: " + name);
      steps.enter(context);
      steps.perform();
      steps.exit();
   }

   abstract public void cancel();

   public void terminate() {
      if (steps == null) {
         return;
      }
      AcqLogger.INSTANCE.logEvent("terminate: " + name);
      steps.terminate();
   }

   abstract public void cleanUp();

   public void complete() {
      AcqLogger.INSTANCE.logEvent("Complete: " + name + "====================================");
   }
}
