package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.ListOfXYPoints;
import edu.mbl.jif.acq.tasking.AcquisitionRoutine;
import edu.mbl.jif.acq.tasking.StepIterator;
import edu.mbl.jif.acq.tasking.AbstractStep;
import edu.mbl.jif.acq.tasking.except.StepSetupException;
import edu.mbl.jif.acq.tasking.TimedSequence;
import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.StepListIterator;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author GBH
 */
public class TestAcqRoutine1 extends AcquisitionRoutine {

   // ListOfXYPoints
   //
   public TestAcqRoutine1(String name) {
      super(name);
      // construct the TaskStructure
      // At each time interval, at each site, do task1 2 times.
      //
      // TimedSequence
      //   ListOfXYPoints
      //     SeriesOfStep(2)
      //       task1
      //         taskA
      //         taskB
      //
      TimedSequence sequence = null;
      try {
         AbstractStep setOfSteps = new ASetOfSteps("acquireChannels");

         // Add 2 SubTasks to task1
         AbstractStep taskA = new ImageAcqStep("channelA");
         setOfSteps.addSubStep(taskA);
         AbstractStep taskB = new ImageAcqStep("channelB");
         setOfSteps.addSubStep(taskB);

         // do task1 2 times

//         StepListIterator series1 = new TrivialSeriesOfSteps();
//         series1.addSubStep(setOfSteps);

         // Add a StepIterator with a list of steps, in this case sites at points
         List siteList = new ArrayList();
         siteList.addAll(Arrays.asList(new Point[]{
            new Point(1, 1),
            new Point(2, 2), //  new Point(3, 3),
         //  new Point(4, 4)
         }));
         StepListIterator seriesSites = new ListOfXYPoints(siteList);
         seriesSites.addSubStep(setOfSteps);
         //seriesSites.addSubStep(series1);

         // enclose in a timed sequence
         sequence = new TimedSequence("TimedSeq", 2000, 2, seriesSites);

      } catch (StepSetupException stepSetupException) {
         stepSetupException.printStackTrace();
      }
      this.setSteps(sequence);

   }

   @Override
   public void prepare() {
      AcqLogger.INSTANCE.logEvent("prepare: " + getName());
   }

   @Override
   public void cancel() {
      AcqLogger.INSTANCE.logEvent("cancel :" + getName());
   }

   @Override
   public void cleanUp() {
      AcqLogger.INSTANCE.logEvent("cleanUp: " + getName());
   }
}
