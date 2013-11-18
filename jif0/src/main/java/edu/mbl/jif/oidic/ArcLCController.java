/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.oidic;

import edu.mbl.jif.varilc.VLCController;

/**
 *
 * @author GBH
 */
public interface ArcLCController extends VLCController {

  boolean setDACVoltageA(double volts);

  boolean setDACVoltageB(double volts);


}
