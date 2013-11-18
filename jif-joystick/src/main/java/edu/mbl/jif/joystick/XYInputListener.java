/*
 * XYInputListener.java
 * 
 * Created on Sep 21, 2007, 1:10:59 PM
 */

package edu.mbl.jif.joystick;

/**
 *
 * @author GBH
 */
public interface XYInputListener {
    
    public void updateAxis(String name, double delta, double value);
    
}
