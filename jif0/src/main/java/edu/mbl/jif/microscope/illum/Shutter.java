package edu.mbl.jif.microscope.illum;

/**
 *
 * @author GBH
 */
public interface Shutter {

  public enum ShutterType {

    EPI,
    XMIS;
  }

  void setOpen(boolean open);

  boolean isOpen();
}
