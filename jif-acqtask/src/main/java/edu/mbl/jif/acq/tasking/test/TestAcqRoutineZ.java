package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AbstractStep;
import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.AcquisitionRoutine;
import edu.mbl.jif.acq.tasking.StepListIterator;
import edu.mbl.jif.acq.tasking.TimedSequence;

/**
 * Z-Scan test routine
 *
 * @author GBH
 */
public class TestAcqRoutineZ extends AcquisitionRoutine {

   // ListOfPoints
   //
   public TestAcqRoutineZ(String name) {
      super(name);
      // construct the TaskStructure
      // At each time interval, at each site, do task1 5 times.
      TimedSequence sequence = null;

      AbstractStep task1 = new ImageAcqStep("channelA");

      // Add a SubTask to task1
      AbstractStep task2 = new ImageAcqStep("channelB");
      task1.addSubStep(task2);

      // do Z-Scan
      StepListIterator zSeries = new ZScanSeriesTask();
      zSeries.addSubStep(task1);

      // enclose in a timed sequence
      sequence = new TimedSequence("TimedSeq", 2000, 2, zSeries);

      this.setSteps(sequence);

   }

   @Override
   public void prepare() {
      AcqLogger.INSTANCE.logEvent("prepare: " + getName());
   }

   @Override
   public void cancel() {
      AcqLogger.INSTANCE.logEvent("cancel: " + getName());
   }

   @Override
   public void cleanUp() {
      AcqLogger.INSTANCE.logEvent("cleanup: " + getName());
   }
}
