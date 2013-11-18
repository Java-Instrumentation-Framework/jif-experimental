/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stage;

/**
 *
 * @author GBH
 */
public class MockZStage implements ZStageController {

  @Override
  public void sendCmd(String cmd) {
    System.out.println("sendCmd = " + cmd);
  }

  @Override
  public void test() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean checkResponsive() {

    return true;
  }

  @Override
  public String queryStatus() {
    return "ok";
  }

  @Override
  public void clearErrors() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void initialize() {
    System.out.println("init()");
  }

  @Override
  public boolean areLimitsSet() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getHighLimit() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getLowLimit() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setHighLimit(int z) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setLowLimit(int z) {
    System.out.println("z = " + z);
  }

  @Override
  public void setIncrement(int _increment) {
    System.out.println("_increment = " + _increment);
  }

  @Override
  public void setNmPerIncrement(int nmPerIncrement) {
    System.out.println("nmPerIncrement = " + nmPerIncrement);

  }

  @Override
  public String nmToHexIncrements(int nanometers) {
    return "###";
  }

  @Override
  public int getZeroIndexPosition() {
    System.out.println("getZeroIndexPosition()");
    return 0;
  }

  @Override
  public void setZeroPosition() {
    System.out.println("setZeroPosition()");
  }

  @Override
  public int getCurrentPosition() {
    return 0;
  }

  @Override
  public void moveDown(int moveDn) {
    System.out.println("moveDn = " + moveDn);
  }

  @Override
  public void moveRelative(int nmDiff) {
    System.out.println("moveRel nmDiff = " + nmDiff);
  }

  @Override
  public int moveRelativeAck(int nmDiff) {
    System.out.println("moveRelAck nmDiff = " + nmDiff);
    return 0;
  }

  @Override
  public void moveTo(int moveTo) {
    System.out.println("moveTo = " + moveTo);
  }

  @Override
  public int moveToAck(int moveToAck) {
    System.out.println("moveToAck = " + moveToAck);
    return 0;
  }

  @Override
  public void moveUp(int moveUp) {
    System.out.println("moveUp = " + moveUp);
  }

  @Override
  public void stop() {
    System.out.println("stop()");
  }
}
