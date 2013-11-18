/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.tasking;

/*
      public void execute() {
        routine.commence();
        routine.prepare();
        routine.initialize();
           steps.initialize();
        try {
            routine.perform();
               doStep();
               for substeps
                 subSteps.enter();
                 subSteps.perform();
                 subSteps.exit();
        } catch (StepException ex) {
		// TODO Retry/Recover/Cleanup from exception condition
		routine.cancel();
            ex.printStackTrace();
        } finally {
            routine.terminate(); 
               steps.terminate();
            routine.cleanUp();
            routine.complete();
        }
    }

* 

How Steps are called
enter();
perform()
    for steps/iterations {
        doNextStep(nextObj);
        doSubTasks();
    }
exit();

* 
 */