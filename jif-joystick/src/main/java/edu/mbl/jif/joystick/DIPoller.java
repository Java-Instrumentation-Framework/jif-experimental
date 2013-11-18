/*
 * DIPoller.java
 * 
 * Created on Sep 23, 2007, 12:34:11 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.joystick;

/**
 *
 * @author GBH
 */
public class DIPoller implements Runnable {
    
    AxisListener xAxisListener;
    AxisListener yAxisListener;
    AxisListener zAxisListener;
    // ButtonPanel[] buttonPanels;
    //DirectionalPanel[] directionalPanels;
    DIDevice device;
    int na;
     /** Polls the DirectInput device for data. */
    private int updatePeriod =200; // msecs
    
    public DIPoller(DIDevice device) {
        this.device = device;
        na = device.getNumberOfAxes();
    }
    
    public void run() {
        while (true) {
            update();
            HiresClock.delay(getUpdatePeriod());
        }
    }

     public void update() {
        device.getDevice().update();
//        for (int i = 0; i < na; i++) {
//            if (axisPanels[i] != null) {
//                axisPanels[i].update();
//            }
//        }
        if(xAxisListener != null) {
            xAxisListener.update();
        }
        if(yAxisListener != null) {
            yAxisListener.update();
        }
        if(zAxisListener != null) {
            zAxisListener.update();
        }

//        for (int i = 0; i < nb; i++) {
//            buttonPanels[i].update();
//        }
//        for (int i = 0; i < nd; i++) {
//            directionalPanels[i].update();
//        }
    }
    /** Polls the DirectInput device for data. */
    public int getUpdatePeriod() {
        return updatePeriod;
    }

    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

}
