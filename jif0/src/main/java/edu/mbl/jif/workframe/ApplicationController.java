/*
 * ApplicationController.java
 *
 * Created on August 9, 2006, 7:36 PM
 */
package edu.mbl.jif.workframe;

/**
 *
 * @author GBH
 */
public interface ApplicationController {

    void initialize();

    void startup();

    boolean canExit();

    // public Object getController(String ctrlName);

    // public Object getModel(String ctrlName);

    // public Object getPresentation(String ctrlName);
}
