/*
 * SeeFloorViewControl.java
 *
 * Created on January 25, 2007, 3:05 PM
 */
package edu.mbl.jif.camera;

import edu.mbl.jif.gui.spatial.DirectionalXYController;

/**
 *
 * @author GBH
 */
public class SeeFloorViewControl implements DirectionalXYController {

  SeeFloor seeFloor;
    private double multiplier;


  /** Creates a new instance of SeeFloorViewControl */
  public SeeFloorViewControl(SeeFloor seeFloor) {
    this.seeFloor = seeFloor;
  }

  public int goUp(int n) {
    seeFloor.move(0, n);
    return 0;
  }

  public int goDown(int n) {
    seeFloor.move(0, -n);
    return 0;
  }

  public int goLeft(int n) {
    seeFloor.move(-n, 0);
    return 0;
  }

  public int goRight(int n) {
    seeFloor.move(n, 0);
    return 0;
  }

  public int goCenter(int n) {
    seeFloor.setPosition(0, 0);
    return 0;
  }

  public int goUpRight(int n) {
    return 0;
  }

  public int goDownRight(int n) {
    return 0;
  }

  public int goUpLeft(int n) {
    return 0;
  }

  public int goDownLeft(int n) {
    return 0;
  }

  public int goTop(int n) {
    return 0;
  }

  public int goBottom(int n) {
    return 0;
  }

  public int goLeftLimit(int n) {
    return 0;
  }

  public int goRightLimit(int n) {
    return 0;
  }

}
