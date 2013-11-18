/*
 * DirectionalXYController.java
 */

package edu.mbl.jif.gui.spatial;

/**
 *
 * @author GBH
 */


// Notes on JxInput
// NuLooq axis values range: 0 to 5000.
// Gamepad axis: 0 to 1.

public interface DirectionalXYController {
    
    // attach AxisControllers, perhaps X & Y 
    
    public int goUp(int n);
    public int goDown(int n);
    public int goLeft(int n);
    public int goRight(int n);
    
    public int goCenter(int n);
    
    public int goUpRight(int n);    //pgUp
    public int goDownRight(int n);  //pgDn
    public int goUpLeft(int n);     // home
    public int goDownLeft(int n);   // end
    
    
    public int goTop(int n);
    public int goBottom(int n);
    public int goLeftLimit(int n);
    public int goRightLimit(int n);
    
    
    // + Alt / Ctrl / Shift
    
}
