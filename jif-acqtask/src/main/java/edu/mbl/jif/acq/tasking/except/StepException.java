/*
 * StepException.java
 *
 * Created on January 28, 2007, 1:46 PM
 */

package edu.mbl.jif.acq.tasking.except;

/**
 *
 * @author GBH
 */
public class StepException extends Exception {

    private String message;
    
    /** Creates a new instance of StepException */
    public StepException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    
}
