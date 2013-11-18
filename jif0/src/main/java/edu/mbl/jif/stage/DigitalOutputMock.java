/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.stage;

/**
 *
 * @author GBH
 */
public class DigitalOutputMock  implements edu.mbl.jif.stage.DigitalOutput {

    @Override
    public void setOutput(int channel, boolean hilow) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dioRepeat(int n, int period) {
        System.out.println("dioRepeat: " + n + ", " + period);
    }

}
