/*
 * Actuator.java
 * Created on January 28, 2007, 1:59 PM
 */
package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AcqRoutineExecutor;
import edu.mbl.jif.acq.tasking.AcquisitionRoutine;

/**
 *
 * @author GBH
 */
public class Actuator {

  /** Creates a new instance of Actuator */
  public Actuator() {
//    try {
      // construct the TaskStructure
      // At each time interval, at each site, do task1 5 times.


//      Step task1 = new TrivialStep("One");
//
//      // Add a SubTask to task1
//      Step task2 = new TrivialStep("Two");
//      task1.addSubStep(task2);
//
//      // do tast1 5 times
//      SeriesOfSteps series1 = new TrivialSeriesTask();
//      series1.setIterations(2);
//      series1.addSubStep(task1);
//
//      // Add a SeriesOfSteps with a list of steps, in this case sites at points
//      List siteList = new ArrayList();
//      siteList.addAll(Arrays.asList(new Point[]{
//                new Point(1, 1),
//                new Point(2, 2)
////                        ,
////                new Point(3,3),
////                new Point(4,4),
//              }));
//
//      SeriesOfSteps seriesSites = new ListOfPoints(siteList);
//      seriesSites.addSubStep(series1);
//
//      // enclose in a timed sequence
//      TimedSequence sequence = new TimedSequence(2000, 2, seriesSites);
//      //sequence.addSubStep(seriesSites);

      // create Acquisition process and execute it
      AcquisitionRoutine acqRoutine;
      // acqRoutine = new TestAcqRoutine1("TestRoutine1");
       acqRoutine = new TestAcqRoutineZ("TestRoutineZ");
      AcqRoutineExecutor exec = new AcqRoutineExecutor(acqRoutine);
      exec.execute();
//
//    } catch (StepSetupException ex) {
//      ex.printStackTrace();
//    } finally {
//    }
  }
	public static void main(String[] args) {
		new Actuator();
	}
}
