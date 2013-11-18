/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.spatial;

/**
 *
 * @author GBH
 */
public interface DirectionalXYExtendedController extends DirectionalXYController{
    
    public int goUpContinuousStart(int multiplier);
    public int goUpContinuousStop();
    public int goDownContinuousStart(int multiplier);
    public int goDownContinuousStop();
    public int goRightContinuousStart(int multiplier);
    public int goRightContinuousStop();
    public int goLeftContinuousStart(int multiplier);
    public int goLeftContinuousStop();
    
}
/*
 implements KeyListener {

   
    public void keyPressed(KeyEvent e) {. . .}
    public void keyReleased(KeyEvent e){. . .}
    public void keyTyped(KeyEvent e)   {. . .}
 * 
 public void keyPressed(KeyEvent e){
   switch(e.getKeyCode()) {
    case KeyEvent.VK_LEFT: leftKey = true; 
 }}
 */
