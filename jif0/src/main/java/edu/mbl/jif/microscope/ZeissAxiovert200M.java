package edu.mbl.jif.microscope;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.device.SerialDevice;
import edu.mbl.jif.microscope.illum.*;
import edu.mbl.jif.stage.StageCtrlZeiss;
import edu.mbl.jif.stage.ZStageController;
import edu.mbl.jif.utils.JifUtils;
import edu.mbl.jif.utils.convert.HexConverter;

/**
 *
 * @author GBH
 */
public class ZeissAxiovert200M extends SerialDevice implements ZStageController, Illuminator, Shutter, Filter {

  ZStageController zCtrl;

  public ZeissAxiovert200M(InstrumentController instrumentCtrl,
          String portOwnerName,
          String commPortName) {
    super(instrumentCtrl, portOwnerName, commPortName);
    zCtrl = new StageCtrlZeiss(this);
    if (zCtrl == null) {
      System.err.println("Unable to instanciate StageCtrlZeiss");
    }

  }
//
// <editor-fold defaultstate="collapsed" desc=" Illum/Shutter ">
// Shutter -----------------------------------------------------------------------------
  boolean open;

  @Override
  public void setOpen(boolean open) {
    if (open) {
      sendCommand("HPCK1,2"); // Open FL shutter
      open = true;
    } else {
      sendCommand("HPCK1,1"); // Close FL shutter
      open = false;
    }
  }

  @Override
  public boolean isOpen() {
    return open;
  }
  // Illuminator ---------------------------------------------------------------------

  @Override
  public void setLevel(double level) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public double getLevel() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
// </editor-fold>
//
// <editor-fold defaultstate="collapsed" desc=" Filter ">
  // Filter --------------------------------------------------------------------------
  // Reflector Turret
  // ("HPCR1," + position)

  @Override
  public int setPosition(long pos) {
    sendCommand("HPCR1," + pos);
    return 0;
  }

  @Override
  public int setPosition(String label) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getPosition(long pos) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getPosition(String label) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getPositionLabel(long pos, String label) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getLabelPosition(String label, long pos) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int setPositionLabel(long pos, String label) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getNumberOfPositions() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

// </editor-fold>
//
// <editor-fold defaultstate="collapsed" desc=" StageZ < < < < < < < < < < < <  ">
  @Override
  public boolean checkResponsive() {
    return zCtrl.checkResponsive();
  }

  @Override
  public void clearErrors() {
    zCtrl.clearErrors();
  }

  @Override
  public int getCurrentPosition() {
    return zCtrl.getCurrentPosition();
  }

  @Override
  public int getZeroIndexPosition() {
    return zCtrl.getZeroIndexPosition();
  }

  @Override
  public void moveDown(int n) {
    zCtrl.moveDown(n);
  }

  @Override
  public void moveRelative(int nmDiff) {
    zCtrl.moveRelative(nmDiff);
  }

  @Override
  public int moveRelativeAck(int nmDiff) {
    return zCtrl.moveRelativeAck(nmDiff);
  }

  @Override
  public void moveTo(int pos) {
    zCtrl.moveTo(pos);
  }

  @Override
  public int moveToAck(int nmPos) {
    return zCtrl.moveToAck(nmPos);
  }

  @Override
  public void moveUp(int n) {
    zCtrl.moveUp(n);
  }

  @Override
  public String nmToHexIncrements(int nanometers) {
    return zCtrl.nmToHexIncrements(nanometers);
  }

  @Override
  public String queryStatus() {
    return zCtrl.queryStatus();
  }

  @Override
  public void sendCmd(String cmd) {
    zCtrl.sendCmd(cmd);
  }

  @Override
  public void setIncrement(int _increment) {
    zCtrl.setIncrement(_increment);
  }

  @Override
  public void setZeroPosition() {
    zCtrl.setZeroPosition();
  }

  @Override
  public void stop() {
    zCtrl.stop();
  }

  @Override
  public void test() {
    zCtrl.test();
  }

  @Override
  public void setNmPerIncrement(int nmPerIncrement) {
    zCtrl.setNmPerIncrement(nmPerIncrement);
  }

  @Override
  public boolean areLimitsSet() {
    return zCtrl.areLimitsSet();
  }

  @Override
  public int getHighLimit() {
    return zCtrl.getHighLimit();
  }

  @Override
  public int getLowLimit() {
    return zCtrl.getLowLimit();
  }

  @Override
  public void setHighLimit(int z) {
    zCtrl.setHighLimit(z);
  }

  @Override
  public void setLowLimit(int z) {
    zCtrl.setLowLimit(z);
  }

  // ZStage fields...
//    int nmPerIncrement;
//    int position;
//    int highLimit = 9999999;
//    int lowLimit = -9999999;
//    int increment;
//    //
//        @Override
//    public void initialize() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void test() {
//        sendCmd("Zp");
//        String r = waitForResponseStr(200);
//        System.out.println("Response: " + r);
//        queryStatus();
//        moveToAck(10000);
//    }
//
//    public void setNmPerIncrement(int nmPerIncrement) {
//        this.nmPerIncrement = nmPerIncrement;
//    }
//
//    /////////////////////////////////////////////////////////////////////////
//    // updateFields
//    //
////    void updateFields() {
////        highLimit = Prefs.usr.getInt("stageHighLimit", 100);
////        lowLimit = Prefs.usr.getInt("stageLowLimit", -100);
////        //
////        increment = Prefs.usr.getInt("seriesAcqZincrement", 0);
////        sections = Prefs.usr.getInt("seriesAcqZsections", 0);
////        rangeBottom = Prefs.usr.getInt("seriesAcqZStart", 0);
////        rangeTop = rangeBottom + (sections * increment);
////    //position = Get it from the Microscope
////    }
//
//    ////////////////////////////////////////////////////////////////////////
//    // Axiovert 200M controls
//    //
//    public boolean checkResponsive() {
//        //io.checkControlState();
//        if (queryStatus().equalsIgnoreCase("###")) {
//            System.err.println("Focus/Stage Control Error: Not responsive.");
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public String queryStatus() {
//        sendCmd("ZFs"); // gets "PFxxxx"
//        String response = waitForResponseStr(200);
//        if (response != "###") {
//            return response;
//        } else { // try a second time
//            sendCmd("ZFs"); // gets "PFxxxx"
//            response = waitForResponseStr(200);
//            System.out.println("Focus Control queryStatus2: " + response);
//        }
//        return response;
//    }
//
//    /*
//    public void initializeStagePosition() {
//    // set the user's zero to the current position so we can return here.
//    int startPosition = getCurrentPosition();
//    System.out.println("startPosition: " + startPosition);
//    setZeroAtLowerStop();
//    // setLowLimit();
//    // return to starting position
//    moveToAck(startPosition);
//    System.out.println("initializeStagePosition, returned to: "
//    + getCurrentPosition());
//    zeroPosition = getCurrentPosition();
//    setHighLimit(zeroPosition + 100000);
//    }
//    // default limit on extent of motion... 2mm from lowest?
//    void setZeroAtLowerStop() {
//    // Warn the user !!!
//    if (DialogBoxes.boxConfirm("WARNING: Microscope Focus Will Move!",
//    "Focus will be moved to lowest position as reference.\nProceed?")) {
//    sendCmd("ZGI2,1");  // move to lower stop
//    jif.utils.PSjUtils.waitFor(500);
//    System.out.println("setZeroAtLowerStop (after wait)");
//    //sendCmd("Zpi");  // get calib. ref. index to confirm
//    //int pos = waitForPosition(0);
//    //      if (pos != 0) {
//    //        DialogBoxes.boxError("Error Setting Reference Position",
//    //          "Error in response to set reference position.");
//    //        refPos = -1;  // error
//    //      } else {
//    //        setLowLimit(pos);  // define it as lower limit
//    //        refPos = 0;  // and the reference position
//    //      }
//    }
//    }
//     */
//
////  public int getPosition() { // in nm.
////    return position;
////  }
//    public int getCurrentPosition() {
//        boolean bad = true;
//        int attempts = 0;
//        int pos = 0;
//        int nmPos = 0;
//        String response = null;
//        while (bad && (attempts < 10)) {
//            attempts++;
//            sendCmd("Zp");
//            //      try {
//            //        pos = waitForPosition(0);
//            //        nmPos = pos * nmPerIncrement;
//            //      } catch (Exception ex) {
//            //        jif.utils.PSjUtils.event("Exception in waitForPosition");
//            //      }
//            response = waitForResponse();
//            System.out.println("getCurrentPosition, response: " + response);
//            if (response.startsWith("PF")) {
//                try {
//                    pos =
//                        HexConverter.HexTripletToInteger(response.substring(2,
//                        response.length() - 1));
//                } catch (Exception e) {
//                    System.out.println(
//                        "getCurrentPosition, Response error.  len= " + response.length());
//                }
//                bad = false;
//            }
//        }
//        // nmPos = pos * nmPerIncrement;
//        System.out.println(
//            "getCurrentPosition: " + pos + ", nm: " + nmPos + "  attempts: " + attempts);
//        position = nmPos;
//        return nmPos;
//    }
//
//    public String nmToHexIncrements(int nanometers) {
//        int increments = (int) ((float) nanometers / nmPerIncrement);
//        return HexConverter.IntegerToHex(increments);
//    }
//
////  public boolean checkStagePosition() { // is it = to assumed position
////    int pos = 0;
////    sendCmd("Zp");
////    try {
////      pos = waitForResponse(200);
////    } catch (Exception ex) {
////      jif.utils.PSjUtils.event("Exception in waitForPosition");
////    }
////    int nmPos = pos * nmPerIncrement;
////    if (position != nmPos) {
////      DialogBoxes.boxError("Error in stage position",
////          "Actual: " + nmPos + "  Assumed: " + position
////          + "\nPress OK to reset");
////      return false;
////    } else {
////      return true;
////    }
////  }
//
//    ///////////////////////////////////////////////////////////////////////
//    // Move to...
//    //
//    public int moveToAck(int nmPos) {
//        // "ZDxxxxxx" moves to pos. and acks with "PFxxxxxx"
//        int iPos = (int) ((float) nmPos / nmPerIncrement);
//        int iPosNew = 0;
//        sendCmd("ZD" + HexConverter.IntegerToHex(iPos));
//        try {
//            iPosNew = waitForPositionResponse(500);
//            position = iPosNew * nmPerIncrement;
//        } catch (Exception ex) {
//            //psj.PSjUtils.event("Exception in waitForPosition");
//        }
//
//        //    if (position != iPosNew) {
//        //      DialogBoxes.boxError("Error in Focus Controller",
//        //        "moveToAck(" + iPos + ") not reached.  NewPosition = " + iPosNew);
//        //    }
//        //    int attempts = 0;
//        //    while (((position = getCurrentPosition()) != nmPos) && (attempts < 10)) {
//        //      System.out.println("moveToAck, getCurrentPosition() != nmPos: "
//        //        + position + " != " + nmPos);
//        //      attempts++;
//        //    }
//        //
//        //System.out.println("moveToAck(" + nmPos + ") -> " + position);
//        return position;
//    }
//
//    public int moveRelativeAck(int nmDiff) {
//        //"Zbxxxxxx" moves relative and acks with "PFxxxxxx"
//        int increments = (int) ((float) nmDiff / (float) nmPerIncrement);
//        int pos = 0;
//        try {
//            sendCmd("Zb" + HexConverter.IntegerToHex(increments));
//        //pos = serialDevice.waitForResponse(0);
//        //position = (pos * nmPerIncrement);
//        } catch (Exception ex) {
//            //psj.PSjUtils.event("Exception in waitForPosition");
//        }
//
//        // System.out.println("moveRelativeAck(" + nmDiff + ") -> " + position);
//        return position;
//    }
//
//    public void moveRelative(int nmDiff) {
//        //"ZBxxxxxx" moves relative
//        int increments = (int) ((float) nmDiff / nmPerIncrement);
//
//        //System.out.println("moveRelative increments: " + increments);
//        sendCmd("ZB" + HexConverter.IntegerToHex(increments));
//    }
//
//    /////////////////////////////////////////////////////////////
//    // Move
//    //
//    public void moveTo(int pos) {
//        if (pos == position) {
//            return;
//        }
//        if ((pos <= highLimit) && (pos >= lowLimit)) {
//            int nSteps = 0;
//            if (pos > position) {
//                nSteps = pos - position;
//                moveUp(nSteps);
//            } else if (pos < position) {
//                nSteps = position - pos;
//                moveDown(nSteps);
//            }
//        }
//        position = pos;
//    }
//
//    public void moveUp(int n) {
//       // if ((position + n) <= highLimit) {
//            moveRelative(n);
//       // }
//    }
//
//    public void moveDown(int n) {
//        //if ((position - n) >= lowLimit) {
//            moveRelative(-n);
//        //}
//    }
//
//    public void stop() {
//    }
//
//    public void setIncrement(int _increment) {
//        increment = _increment;
//    }
//
//    //--------------------------------------------------------------------------
//    // Zero
//    //
//    void setOriginHere() {
//        sendCmd("ZP"); //Home position ZP
//    }
//
//    public void setZeroPosition() {
//        setOriginHere();
//        position = getCurrentPosition();
//    }
//
//    public int getZeroIndexPosition() {
//        boolean bad = true;
//        int attempts = 0;
//        int pos = 0;
//        int nmPos = 0;
//        String response = null;
//        while (bad && (attempts < 10)) {
//            attempts++;
//            sendCmd("Zpi");
//            response = waitForResponse();
//            System.out.println("getZeroIndexPosition, response: " + response);
//            if (response.startsWith("PF")) {
//                try {
//                    pos =
//                        HexConverter.HexTripletToInteger(response.substring(2,
//                        response.length() - 1));
//                } catch (Exception e) {
//                    System.out.println(
//                        "getZeroIndexPosition, Response error.  len= " + response.length());
//                }
//                bad = false;
//            }
//        }
//        return pos;
//    }
//
//
//
//// <editor-fold defaultstate="collapsed" desc=" Limits ">
//    // LowLimit is the ZERO for referencing position.
//    //
//    public void setHighLimit(int z) { // set to current position
//        highLimit = z;
//        //getCurrentPosition();
//        System.out.println("setHighLimit: " + highLimit);
//        // to set limit in microscope too...
//        // sendCmd("ZU" + HexConverter...);
//        // highLimit = waitForPosition(0);
//    }
//
//    public void setLowLimit(int z) {
//        lowLimit = z;
//        //    lowLimit = getCurrentPosition();
//        //    System.out.println("setLowLimit: " + lowLimit);
//        // to set limit in microscope too...
//        // sendCmd("ZL" + HexConverter...);
//        // lowLimit = waitForPosition(0);
//        System.out.println("setLowLimit: " + lowLimit);
//    }
//
//    public int getHighLimit() {
//        // sendCmd("Zu");
//        // highLimit = waitForPosition(0);
//        return highLimit;
//    }
//
//    public int getLowLimit() {
//        // sendCmd("Zl");
//        // lowLimit = waitForPosition(0);
//        return lowLimit;
//    }
//
//    public boolean areLimitsSet() {
//        return true;
//        //return ((highLimit > -1) && (lowLimit > -1));
//    }
//
//// </editor-fold>
////---------------------------------------------------------------------------
//// Time / Velocity
////
////    int timeToIncrement(int dZ) { // msecs
////        return (int) (initDelay + (dZ * velocity) + settleDelay);
////    }
//
//    int timeToReset() {
//        return 0;
//    // (initDelay +
//    // transitTime(rangeTop - rangeBottom + backlash) +
//    // reverseDelay +
//    // transitTime(backlash) +
//    // (preIncrements * timeToIncrement(increment)));
//    }
//
//    int transitTimeTest() {
//        return 0;
//    }
//
////---------------------------------------------------------------------------
//// Z-Scan Acquisition
////
//    //---------------------------------------------------------------------------
//    // prePositionForZscan
//    //
//
//    //
//    @Override
//    public void clearErrors() {
//        sendCmd("Eb");
//    }
//
//  // <editor-fold defaultstate="collapsed" desc=" Send Commands & get Responses ">
//
//
//    // ? ? ?  ?
//
//
//    public int waitForPositionResponse(int timeOut) {
//        String r = waitForResponse();
//        // System.out.println("waitForPosition response:: " + r);
//        if ((r == "###") || (!r.startsWith("PF"))) {
//            JifUtils.waitFor(200);
//            return getCurrentPosition();
//            //throw new Exception();
//        } else {
//            try {
//                return HexConverter.HexTripletToInteger(r.substring(2, r.length() - 1));
//            } catch (Exception ex) {
//                return 0;
//            }
//        }
//    }
//
//    @Override
//    public void sendCmd(String cmd) {
//        // All commands are preceeded by "FP",
//        // F designating Focus Drive Controller,
//        // P designating  RS-232 Source
//        //System.out.println("Send: " + s);
//        System.out.println("SendCmd: " + cmd);
//        String s = "FP" + cmd;
//        sendString(s);
//    }
//
//    //  public int sendCmdWaitForPosition(String cmd) throws Exception {
//    //    String s = "FP" + cmd + "\r";
//    //    String r = "BULLJIT" ; //io.writeAndReply(s);
//    //    System.out.println("sendCmdWaitForPosition response:: " + r);
//    //    if ((r == "###") || (!r.startsWith("PF"))) {
//    //      throw new Exception();
//    //    }
//    //    return HexConverter.HexTripletToInteger(r.substring(2, r.length() - 1));
//    //  }
//    public String waitForResponseStr(int time) {
//        String response = waitForResponse();
//        //jif.utils.PSjUtils.waitFor(time);
//        return response; // io.getRecvBuffer();
//    }
// </editor-fold>

//---------------------------------------------------------------------------
// NOTES
//
  /*
  Z Displacements are in nanometers.
  Z-Series scans are performed from bottom to top.
  highLimit
  |   rangeTop
  |   |   position
  |   |   zeroPosition
  |   rangeBottom
  lowLimit
  increment, sections
  zeroPosition
  position
  lowLomit
  refPos
   */

  // "FPZFs" - >"PF000F": status flag bits 0...3 are active
  // ZmStatFlags -- Status Flags:

  /*
  ZMSF_DIRUP = 0x0001; // current move direction; 0: down, 1: up
  ZMSF_MOVING = 0x0002; // trajectory in progress
  ZMSF_SETTLE = 0x0004; // settling after movement
  ZMSF_VELTGT = 0x0008; // trajector y target velocity reached
  ZMSF_LIMDN = 0x0010; // beyond lower software limit
  ZMSF_LIMUP = 0x0020; // beyond upper software limit
  ZMSF_STPDN = 0x0040; // beyond lower stop
  ZMSF_STPUP = 0x0080; // beyond upper stop
  ZMSF_CTRLON = 0x0100; // motor controller on
  ZMSF_MCDYN = 0x0200; // MC parameter dynamic set active
  ZMSF_RES0400 = 0x0400; // reserved (always = 0)
  ZMSF_CALIB = 0x0800; // calibration performed
  ZMSF_RES1000 = 0x1000; // reserved (always = 0)
  ZMSF_RES2000 = 0x2000; // reser ved (always = 0)
  ZMSF_MODESET = 0x4000; // system in set mode
  ZMSF_EXC = 0x8000; // ecxeption occurred (until handled)
   */
// </editor-fold>
}
