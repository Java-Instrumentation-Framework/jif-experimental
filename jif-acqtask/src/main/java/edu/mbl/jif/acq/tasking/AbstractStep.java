/*
 * AbstractStep.java
 *
 * Created on January 28, 2007, 1:43 PM
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * AbstractStep requires a Device in order to be capable of an acquistion. (e.g. a camera is
 * required to acquire an image.) it is not required for a single acquisition (at the current stage
 * position)
 *
 * Tasks, SeriesTasks, SeqenceTask and Acquisition have DataStoreDefn's DataStoreDefn's are
 * registered with the DataStoreManager. A AbstractStep that stores data uses a DataStoreScheme.
 *
 * @author GBH
 */
abstract public class AbstractStep implements Step {

   private String name;
   private AbstractStep parent;
   private List<AbstractStep> subSteps = new ArrayList<AbstractStep>();
   //
   //private AcquisitionDevice device;
   private boolean capable;
   private boolean active;
   //
   private DataStoreDefn dataStoreDefn;
   private DataStoreScheme dataStoreScheme = new DataStoreScheme();
   //
   private Step.State state;     // ++
   private long startTime;
   private long duration;
   //
   private String currentValue;
   private AcquisitionContext context;
   

   public AbstractStep(String name) {
      this.name = name;
   }

   public AbstractStep(String name, DataStoreDefn dataStoreDefn) {
      this.name = name;
      // this.device = device;
      this.dataStoreDefn = dataStoreDefn;
   }

//  public void setDevice(AcquisitionDevice device) {
//    this.device = device;
//  }
//
//  public AcquisitionDevice getDevice() {
//    return device;
//  }
   @Override
   public boolean isCapable() {
      return capable;
   }

   @Override
   public void setCapable(boolean capable) {
      this.capable = capable;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public void setDataStoreDefn(DataStoreDefn dataStoreDefn) {
      this.dataStoreDefn = dataStoreDefn;
      DataStoreManager.INSTANCE.addDataStoreDefn(dataStoreDefn);
   }

   @Override
   public DataStoreDefn getDataStoreDefn() {
      return dataStoreDefn;
   }

   @Override
   public DataStoreScheme getDataStoreScheme() {
      return dataStoreScheme;
   }

   @Override
   public void setDataStoreScheme(DataStoreScheme dataStoreScheme) {
      this.dataStoreScheme = dataStoreScheme;
   }

   public AcquisitionContext getContext() {
      return context;
   }

   public void setContext(AcquisitionContext context) {
      this.context = context;
   }

   
   @Override
   public String getCurrentValue() {
      return currentValue;
   }

   // used by dataStoreScheme to construct id, file, or directory names
   @Override
   public void setCurrentValue(String value) {
      this.currentValue = value;
      getDataStoreDefn().setDataIdString(currentValue);
   }
   // SubSteps

   @Override
   public List getSubSteps() {
      return subSteps;
   }

   @Override
   public void addSubStep(AbstractStep step) {
      getSubSteps().add(step);
   }

   @Override
   public void setActive(boolean active) {
      this.active = active;
   }

   @Override
   public boolean isActive() {
      return active;
   }

   
   @Override
   public void initializeSelf() {
      AcqLogger.INSTANCE.logEvent("initializeSelf: " + getName());
   }

   @Override
   public void initialize() {
      initializeSelf();
      AcqLogger.INSTANCE.addLevel();
      for (AbstractStep step : subSteps) {
         step.initialize();
      }
      AcqLogger.INSTANCE.subLevel();
   }

   @Override
   public void enter(AcquisitionContext context) {
      AcqLogger.INSTANCE.addLevel();
      AcqLogger.INSTANCE.logEvent("enter: " + getName());
      startTime = System.currentTimeMillis();
      setContext(context);
   }

   @Override
   public void perform() throws StepException {
      startTime = System.nanoTime();
      doStep();
      for (AbstractStep subStep : subSteps) {
         subStep.enter(getContext());
         subStep.perform();
         subStep.exit();
      }
      duration = System.nanoTime() - startTime;
   }

   @Override
   public void exit() {
      this.duration = startTime - System.currentTimeMillis();
      AcqLogger.INSTANCE.logEvent("exit: " + getName());
      AcqLogger.INSTANCE.subLevel();
   }

   @Override
   public void terminate() {
      AcqLogger.INSTANCE.addLevel();
      for (AbstractStep step : subSteps) {
         step.terminate();
      }
      AcqLogger.INSTANCE.subLevel();
      terminateSelf();
   }

   @Override
   public void terminateSelf() {
      AcqLogger.INSTANCE.logEvent("terminateSelf: " + getName());
   }

   @Override
   public long getStartTime() {
      return startTime;
   }

   @Override
   public long getDuration() {
      return duration;
   }
}
