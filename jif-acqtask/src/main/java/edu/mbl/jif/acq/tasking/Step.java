package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import java.util.List;

/**
 *
 * @author GBH
 */
public interface Step {

   public enum State {

      INITIALIZING,
      INITIALIZED,
      WAITING,
      EXECUTING,
      COMPLETED,
      CANCELLED,
      FAILED;
   }

   // name is set with Step(String name) constructor in implementation
   String getName();

   int getExtent();  // for dimensional extent
   
//  void setDevice(AcquisitionDevice device);
//
//  AcquisitionDevice getDevice();
   void setCapable(boolean capable);

   boolean isCapable();
   
   void setActive(boolean active);
   
   boolean isActive();

   void addSubStep(AbstractStep step);

   List<Step> getSubSteps();

   void setDataStoreDefn(DataStoreDefn dataStoreDefn);

   DataStoreDefn getDataStoreDefn();

   DataStoreScheme getDataStoreScheme();

   void setDataStoreScheme(DataStoreScheme dataStoreScheme);

   void initialize();

   void initializeSelf();

   void enter(AcquisitionContext context);  // called upon entering this 'level' of Acq.

   // Execution...
   void doStep() throws StepException;

   void perform() throws StepException;

   void setCurrentValue(String value);

   String getCurrentValue();

   // Metadata...
   // Put in Context ?
   void setCurrentMetadata(Object obj);  

   void exit();

   long getStartTime();

   long getDuration();

   void terminateSelf();

   void terminate();
}
