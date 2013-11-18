package edu.mbl.jif.stage;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.gui.dialog.DialogBoxI;

import java.io.IOException;

import edu.mbl.jif.device.SerialDevice;

import edu.mbl.jif.utils.convert.HexConverter;
import edu.mbl.jif.utils.prefs.Prefs;

// PSj: Focus/Stage Controller
// for *** N I K O N ***   with Focus Controller Unit
//
//
public class StageCtrlNikon extends SerialDevice implements ZStageController {
   int highLimit = -1; // upper limit of travel
   int rangeTop; // top of Z-scan range
   int position = 0; // current position
   int rangeBottom; // bottom of Z-scan range
   int lowLimit = -1; // lower limit of travel(= refPos)
   int refPos = -1; // microscope ref. position

   boolean wasZeroed = false;

   int increment = 0; // in nm
   int sections; // number of sections in Z-scan range

   int nmPerIncrement = 0;
   int backlash = 0;
   int preSteps = 0;

// Time calculations
   int reactionLatency = 10;
   int initDelay = reactionLatency;
   int settleDelay;
   float velocity; // average, in nm / msec.
//
//   public SerialPortConnection io = null;
   public boolean isConnected = false;
   public boolean isFunctional = false;

//======================================================================
// Usage:
// StageCtrlNikon.setComPort("com4");
// StageCtrlNikon stage = StageCtrlNikon.getInstance ();
// state.moveUp(100);
// etc...
    static String comPort = "";

    public static void setComPort(String _comport) {
        comPort = _comport;
    }

    public StageCtrlNikon(InstrumentController instrumentCtrl,
                          String portOwnerName,
                          String commPortName) {
        super(instrumentCtrl, portOwnerName, commPortName);

        //public StageCtrlNikon(SerialPortConnection io) {
        //
        // Microscope-specific:
        // For the Nikon Microphot-SA with External Focus Controller:
        // Increment size = 0.1 micron = 100 nm;

        nmPerIncrement = Prefs.usr.getInt("stageNmPerIncrement", 100);
        backlash = Prefs.usr.getInt("stageBacklash", 400); // nm.
        preSteps = Prefs.usr.getInt("stagePreSteps", 5);
        //
//        this.io = io;
//        io.setBaudRate(9600);
//        // Nikon requires Flow Control
//        io.setFlowControl(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
//        try {
//            io.openConnection("StageController.Nikon");
//        } catch (IOException ex1) {
//            DialogBox.boxError("Could not open " + io.getPortName(),
//                "Error attaching to Focus/Stage Controller");
//            // isConnected = false;
//            System.out.println("Unable to open port " + io.getPortName() + "\n" + ex1.getMessage());
//        //throw new Exception();
//        }
        isConnected = true;
        checkResponsive();
        updateFields();
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    public void closeComPort() {
//        isConnected = false;
//        io.closeConnection();
//    }
    public void test() {
    }

    public int getNmPerIncrement() {
        return nmPerIncrement;
    }
    //---------------------------------------------------------------------------
    // updateFields
    //
    void updateFields() {
        highLimit = Prefs.usr.getInt("stageHighLimit", 100);
        lowLimit = Prefs.usr.getInt("stageLowLimit", -100);
        //
        increment = Prefs.usr.getInt("seriesAcqZincrement", 0);
        sections = Prefs.usr.getInt("seriesAcqZsections", 0);
        rangeBottom = Prefs.usr.getInt("seriesAcqZStart", 0);
        rangeTop = rangeBottom + (sections * increment);
    //position = Get it from the Microscope
    }
    ////////////////////////////////////////////////////////////////////////
    // Nikon controls
    //
    public boolean checkResponsive() {
        //io.checkControlState();
        //      if (queryStatus()
        //                   .indexOf(":A") == -1) {
        //         DialogBoxes.boxError("Focus/Stage Control Error", "Not responsive.");
        //         isFunctional = false;
        //         return false;
        //      } else {
        isFunctional = true;
        return true;
    //      }
    }

    public String queryStatus() {
//    sendCmd("WZ");
//    String response = waitForResponseStr(500);
//    return response;
        return "NOT WORKING";
    }


    /*
    public void initializeStagePosition() {
    // set the user's zero to the current position so we can return here.
    int startPosition = getCurrentPosition();
    System.out.println("startPosition: " + startPosition);
    setZeroAtLowerStop();
    // setLowLimit();
    // return to starting position
    moveToAck(startPosition);
    System.out.println("initializeStagePosition, returned to: "
    + getCurrentPosition());
    zeroPosition = getCurrentPosition();
    setHighLimit(zeroPosition + 100000);
    }
    // default limit on extent of motion... 2mm from lowest?
    void setZeroAtLowerStop() {
    // Warn the user !!!
    if (DialogBoxes.boxConfirm("WARNING: Microscope Focus Will Move!",
    "Focus will be moved to lowest position as reference.\nProceed?")) {
    sendCmd("ZGI2,1");  // move to lower stop
    jif.utils.PSjUtils.waitFor(500);
    System.out.println("setZeroAtLowerStop (after wait)");
    //sendCmd("Zpi");  // get calib. ref. index to confirm
    //int pos = waitForPosition(0);
    //      if (pos != 0) {
    //        DialogBoxes.boxError("Error Setting Reference Position",
    //          "Error in response to set reference position.");
    //        refPos = -1;  // error
    //      } else {
    //        setLowLimit(pos);  // define it as lower limit
    //        refPos = 0;  // and the reference position
    //      }
    }
    }
     *///----------------------------------------------------------------------
// Move Relative
//
    public void moveRelative(int nmDiff) {
        int increments = (int) ((float) nmDiff / (float) nmPerIncrement);
        sendCmd("RZ " + increments);
    }

    public int moveRelativeAck(int nmDiff) {
        try {
            moveRelative(nmDiff);
            waitForResponseStr(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return position;
    }
//----------------------------------------------------------------
// Move to...
//
    public int moveToAck(int nmPos) {
        int iPos = (int) ((float) nmPos / (float) nmPerIncrement);
        sendCmd("MZ " + iPos);
        waitForResponseStr(0);
        return position;
    }
    /////////////////////////////////////////////////////////////
    // Move
    //
    public void moveTo(int pos) {
      moveToAck(pos);
//    if (pos == position) {
//      return;
//    }
//    if ( (pos <= highLimit) && (pos >= lowLimit)) {
//      int nSteps = 0;
//      if (pos > position) {
//        nSteps = pos - position;
//        moveUp(nSteps);
//      } else if (pos < position) {
//        nSteps = position - pos;
//        moveDown(nSteps);
//      }
//    }
//    position = pos;
    }
//---------------------------------------------------------------------------
    public void moveUp(int n) {
        if ((position + n) <= highLimit) {
            moveRelativeAck(n);
        }
    }

    public void moveDown(int n) {
        if ((position - n) >= lowLimit) {
            moveRelativeAck(-n);
        }
    }

    public void stop() {
    }

    public void setIncrement(int _increment) {
        increment = _increment;
    }
    //---------------------------------------------------------------------------
    // Zero
    //
    public void setZeroPosition() {
        sendCmd("ZERO");
        waitForResponseStr(0);
        wasZeroed = true;
    }

    public int getZeroIndexPosition() {
        return 0; //pos;
    }

    public boolean isZereod() {
        return wasZeroed;
    }
//---------------------------------------------------------------------------
    // Limits
    //
    // LowLimit is the ZERO for referencing position.
    //
    public void setHighLimit(int z) { // set to current position
        highLimit = z;
        //getCurrentPosition();
        System.out.println("setHighLimit: " + highLimit);
        // to set limit in microscope too...
        // sendCmd("ZU" + HexConverter...);
        // highLimit = waitForPosition(0);
    }

    public void setLowLimit(int z) {
        lowLimit = z;
        //    lowLimit = getCurrentPosition();
        //    System.out.println("setLowLimit: " + lowLimit);
        // to set limit in microscope too...
        // sendCmd("ZL" + HexConverter...);
        // lowLimit = waitForPosition(0);
        System.out.println("setLowLimit: " + lowLimit);
    }

    public int getHighLimit() {
        // sendCmd("Zu");
        // highLimit = waitForPosition(0);
        return highLimit;
    }

    public int getLowLimit() {
        // sendCmd("Zl");
        // lowLimit = waitForPosition(0);
        return lowLimit;
    }

    public boolean areLimitsSet() {
        return true;
    //return ((highLimit > -1) && (lowLimit > -1));
    }

    int timeToIncrement(int dZ) { // msecs
        return (int) (initDelay + (dZ * velocity) + settleDelay);
    }

    int timeToReset() {
        return 0;
    // (initDelay +
    // transitTime(rangeTop - rangeBottom + backlash) +
    // reverseDelay +
    // transitTime(backlash) +
    // (preIncrements * timeToIncrement(increment)));
    }

    int transitTimeTest() {
        return 0;
    }
    //////////////////////////////////////////////////////////////////////////
    // Z-Scan Acquisition
    //////////////////////////////////////////////////////////////////////////
    //
    public void prepareZscan() {
//    Prefs.usr.getInt("seriesAcqZStart", 0);
//    Prefs.usr.getInt("seriesAcqZincrement", 0);
//    Prefs.usr.getInt("seriesAcqZsections", 0);
    }

    public void prePositionForZscan(int start, int incr, int secs) {
        System.out.println("======= Prepositioning stage: start: " + start);
        int mPos = start;
        super.getSerialPortConnection().clearQueue();
        moveToAck(mPos);
        try {
            System.out.println("======= (1)" + getCurrentPosition());
        } catch (Exception ex1) {
        }

        int below = -((preSteps * incr) + backlash);
        mPos = mPos + below;
        moveToAck(mPos);
        mPos = mPos + backlash;
        moveToAck(mPos);

//    try {
//      System.out.println("======= (2)" +
//          getCurrentPosition());
//    } catch (Exception ex1) {}

        int i = 0;
        do {
            mPos = mPos + incr;
            moveToAck(mPos);
            i++;
        } while (i < preSteps);
        // should be at start position
        int now = 0;
        try {
            now = getCurrentPosition();
        } catch (Exception ex) {
        }
        int delta = Math.abs(now - start);
        System.out.println(
            "\nReady to begin Z-Scan (" + incr + ", " + secs + ") at position: " + now);
        if (delta > 100) {
            DialogBoxI.boxError("Focus/Stage Position Error",
                "Initial Z-Scan Position is off by " +
                String.valueOf(delta) + " nm.");
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    // Send Commands & get Responses
    ////////////////////////////////////////////////////////////////////////////
    //
//  public int waitForPosition(int timeOut) throws Exception {
//    try {
//      String r = io.receiveString();
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//    return getPosition();
//  }
    public int getCurrentPosition()  {
        int n = 0;
        try {
            char[] esc = {
                0x27
            };
            String s = new String(esc) + "WZ";
            //io.sendString(s);
            sendCmd(s);
            String r = super.waitForResponse();
            //System.out.println("getPosition.receiveString: [" + r + "]");

            if (r == null || r.length() == 0) {
                System.err.println("getCurrentPosition: no position returned");
                throw new Exception();
            }
            // System.out.println("getPosition.response: [" + r.trim() + "]");
            if (r.trim().length() > 2) {
                String rr =
                    r.trim().substring(3, r.trim().length()).trim();
                System.out.println(rr);
                try {
                    n = Integer.parseInt(rr);
                } catch (NumberFormatException nfex) {
                    System.err.println("parseInt Ex: " + nfex.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        position = n;
        return n;
    }
//  public int getPosition() { // in nm.
//    return position;
//  }

//  public boolean checkStagePosition() { // is it = to assumed position
//    int pos = 0;
//    sendCmd("WZ");
//    try {
//      pos = waitForPosition(200);
//    } catch (Exception ex) {
//      jif.utils.PSjUtils.event("Exception in waitForPosition in checkStagePosition");
//    }
//    int nmPos = pos * nmPerIncrement;
//    if (position != nmPos) {
//      DialogBoxes.boxError("Error in stage position",
//          "Actual: " + nmPos + "  Assumed: " + position + "\nPress OK to reset");
//      return false;
//    } else {
//      return true;
//    }
//  }

//---------------------------------------------------------------------------
// I/O
    @Override
    public void sendCmd(String cmd) {
        char[] esc = {
            0x27
        };
        String s = new String(esc) + cmd;
        super.sendCommand(s);
    }

    public String waitForResponseStr(int time) {
        String r = super.waitForResponse();
        // Nikon focus controller returns ":A\n\r" = [0x3a][0x41][0xa][0xd]
        // so, strip off the \n\r
        // if(r.length() < 3) return null;
        System.out.println("Focus.Receive: " + r.trim());
        return r.trim();
    }

    public void clearErrors() {
        sendCmd("");
    }

    public String nmToHexIncrements(int nanometers) {
        int increments = (int) ((float) nanometers / (float) nmPerIncrement);
        return HexConverter.IntegerToHex(increments);
    }

    @Override
    public void setNmPerIncrement(int nmPerIncrement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
