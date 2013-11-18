/*
 * TrivialAcqProcess.java
 *
 * Created on January 28, 2007, 3:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.AcquisitionRoutine;
import edu.mbl.jif.acq.tasking.StepIterator;

/**
 *
 * @author GBH
 */
public class TrivialAcqRoutine extends AcquisitionRoutine {

  /** Creates a new instance of TrivialAcqProcess */
  public TrivialAcqRoutine(String name, StepIterator seq) {
    super(name, seq);
  }

	@Override
  public void prepare() {
    AcqLogger.INSTANCE.logEvent("TrivialAcqRoutine prepare()");
  }

	@Override
  public void cancel() {
    AcqLogger.INSTANCE.logEvent("TrivialAcqRoutine cancel()");
  }

	@Override
  public void cleanUp() {
    AcqLogger.INSTANCE.logEvent("TrivialAcqRoutine cleanup()");
  }
}
