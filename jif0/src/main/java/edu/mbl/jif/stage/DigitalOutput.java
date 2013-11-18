/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.stage;

/**
 *
 * @author GBH
 */
public interface DigitalOutput {
    
    void setOutput(int channel, boolean hilow);

    void dioRepeat(int n, int period);
    
}
