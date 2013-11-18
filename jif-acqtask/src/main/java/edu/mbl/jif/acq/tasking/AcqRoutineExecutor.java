/*
 * A test...
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import java.util.List;

/**
 *
 * @author GBH
 */
public class AcqRoutineExecutor {

    private AcquisitionRoutine routine;

    public AcqRoutineExecutor(AcquisitionRoutine routine) {
        this.routine = routine;
    }

    public void execute() {
        routine.commence();
         List<AcqDimension> dimensions  = routine.findDimensionalExtents();
         routine.showDimensionalExtents(dimensions);
        routine.prepare();
        routine.initialize();
        try {
            routine.perform();
        } catch (StepException ex) {
		// TODO Retry/Recover/Cleanup from exception condition
		routine.cancel();
            ex.printStackTrace();
        } finally {
            routine.terminate(); 
            routine.cleanUp();
            routine.complete();
        }
    }
}
