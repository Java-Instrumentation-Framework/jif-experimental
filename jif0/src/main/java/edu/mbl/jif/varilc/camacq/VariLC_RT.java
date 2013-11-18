package edu.mbl.jif.varilc.camacq;

import edu.mbl.jif.gui.dialog.DialogBox;
import edu.mbl.jif.utils.convert.Type;
import java.util.*;

import com.holub.asynch.Condition;
import java.io.IOException;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.utils.FlagByte;
import edu.mbl.jif.utils.*;
import edu.mbl.jif.varilc.VLCComm;
import javax.swing.ImageIcon;


///////////////////////////////////////////////////////////////////////////
// PSj - VariLC_RT
// Control functions for ** V a r i L C ** liquid crystal controller
// "Universal Compensator" controller
// NOTES:
// Upon Powering-Up the VariLC_RT:  The yellow <Err> light will flash for
// about 10 seconds as the unit initializes.
///////////////////////////////////////////////////////////////////////////
//
/** @todo Address commands to specific quadrant */
public class VariLC_RT implements VLCComm {

    boolean deBug = true;
    public boolean isConnected = false;
    public boolean isFunctional = false;
    public boolean isCalibrated = false;
    float incrementWavelength = 0;
    float incrementRetardance = 0;
    int selectedElement = -1; // -1 : none selected
    String version;
    public String lcType = null;
    public float wavelength = PrefsRT.usr.getFloat("wavelength", 540.0f);
    public float LC_Swing = (float) PrefsRT.usr.getDouble("LC_Swing", 0.10);
    // Settling Time
    /** @todo account for temperature dependency of settlingTime */
    public int settleTime = 150;
    public Condition isSettled = new Condition(false);
    // to reflect setting of hardware switches (OFF, ON)
    boolean characterEcho = true; // (true, false)
    int baudRate = 9600; // (9600, 19200)
    char terminationCharacter = 0xD; // (<cr> | <lf>)
    boolean initializationAtPowerOn = true; // (yes, no)
    // Latency time for VariLC_RT, in addition to serial port latency time
    public int defineLatency = PrefsRT.usr.getInt("lc.definelatency", 50);
    // Serial port latency time
    public int serialioLatency = PrefsRT.usr.getInt("serial.latency", 15);
    // Serial Port settings for VariLC_RT device:
    // 9600 (or 19,200) baud, 8 data bits, 1 stop bit, no parity (256 byte buffer)
    public float[] retarderA = {0, 0, 0, 0, 0};
    public float[] retarderB = {0, 0, 0, 0, 0};
    boolean[] defined = {false, false, false, false, false};
    // +++ We need to document these parameters
    //=====================================================================
    public float maxSetA = PrefsRT.usr.getFloat("LC_maxSetA", 0.99f);
    public float minSetA = PrefsRT.usr.getFloat("LC_minSetA", 0.17f);
    public float maxSetB = PrefsRT.usr.getFloat("LC_maxSetB", 0.99f);
    public float minSetB = PrefsRT.usr.getFloat("LC_minSetB", 0.17f);
    //
    public float extinctA = PrefsRT.usr.getFloat("LC_extinctA", 0.25f);
    public float extinctB = PrefsRT.usr.getFloat("LC_extinctB", 0.50f);
    public float acceptableVarExtinctA = 0.05f;
    public float acceptableVarExtinctB = 0.10f;
    //======================================================================
    // ------------------  Images & Icons  -----------------------------
    public static ImageIcon[] pStackIcon;
    public static ImageIcon[] pStackIconSelected;


    static {
        // Load PStack Icon images
        pStackIcon = new ImageIcon[7];
        // Load PStack Icon images
        pStackIconSelected = new ImageIcon[7];
//      pStackIcon[0] = loadIcon("pstackiconmag.gif");
//      pStackIcon[1] = loadIcon("pstackiconazim.gif");
        pStackIcon[2] = loadIcon("setExt.gif");
        pStackIcon[3] = loadIcon("set00.gif");
        pStackIcon[4] = loadIcon("set45.gif");
        pStackIcon[5] = loadIcon("set135.gif");
        pStackIcon[6] = loadIcon("set90.gif");
        pStackIconSelected[0] = null;
        pStackIconSelected[1] = null;
        pStackIconSelected[2] = loadIcon("setExtSelected.gif");
        pStackIconSelected[3] = loadIcon("set00Selected.gif");
        pStackIconSelected[4] = loadIcon("set45Selected.gif");
        pStackIconSelected[5] = loadIcon("set135Selected.gif");
        pStackIconSelected[6] = loadIcon("set90Selected.gif");

    }

    static ImageIcon loadIcon(String file) {
        return JifUtils.loadImageIcon(file, VariLC_RT.class);
    }
    SerialPortConnection io;
    private boolean quadParameters = false;  // for OLD VariLC;


    ///////////////////////////////////////////////////////////////////
    // VariLC_RT constructor
    //
    public VariLC_RT(SerialPortConnection _io) {
        // Open the serial port connection
        //PrefsRT.usr.putInt("lc.definelatency", 50);
        io = _io;
        isConnected = true;
        selectedElement = -1;
    //initializeController();
    }

	@Override
    public void closeComPort() {
        io.closeConnection();
    //PSjUtils.event("VariLC_RT commPort closed.");
    }


    // ================== Setup ===================================
    //
    /*
    public int initializeController() {
    // If (Include VariLC_RT in Initialization)&
    String resp;
    boolean responsive = checkIfResponsive();
    if (!responsive) {
    //      DialogBoxes.boxError("VariLC_RT Initialization Error",
    //          "Port opened, but VariLC_RT does not respond.\n"
    //          +
    //          "(Quit and re-start PSjCheck after checking VariLC_RT and connections.)");
    //      PSjUtils.event("VariLC_RT initialization failed");
    System.err.println("VariLC_RT initialization failed");
    isFunctional = false;
    return 0;
    }
    reset();
    setCommandFormat(1); // brief
    //    if (!checkSerialNumber(getVersion())) {
    //      DialogBoxes.boxError("VariLC_RT Serial Number Error",
    //          "VariLC_RT serial number incorrect.");
    //      PSjUtils.event("VariLC_RT serial number incorrect");
    //      isFunctional = false;
    //      return 0;
    //    }
    setUnits(0); // nanometers
    float wavelength = PrefsRT.usr.getFloat("wavelength", 546);
    setWavelength(wavelength);
    resp = sendCommandAndWait("I?", 500); // initialize
    isCalibrated = false;
    // load setting from saved properties
    //    loadSettings();
    isFunctional = true;
    //PSjUtils.event("VariLC_RT initialized");
    System.out.println("VariLC_RT initialized");
    return 0;
    }
     */
    boolean checkSerialNumber(String ver) {
        return true;
    }


    //////////////////////////////////////////////////////////////////////
    //
	@Override
    public String initialize() {
        return sendCommandAndWait("I1");
    }

	@Override
    public String getVersion() {
        version = sendCommandAndWait("V?", 300);
        return version;
    }

	@Override
    public int setUnits(int units) {
        // units of retardance:
        // 0 wavelenghts(def), 1 nanometers
        sendCommandAndWait("Q" + Integer.toString(units));
        // reply =
        return 0;
    }

	@Override
    public int setCommandFormat(int fmt) {
        sendCommandAndWait("B" + Integer.toString(fmt));
        sendCommandAndWait("B?");
        // reply = 0: normal, 1: brief, 2: auto-confirm
        return 0;
    }

	@Override
    public int exercise() {
        sendCommandAndWait("E1");
        while (busyCheck()) {
            try {
                Thread.sleep(1000); // wait a quarter of a sec.
            } catch (InterruptedException e) {
            }
        }
        return 0;
    }


    ///////////////////////////////////////////////////////////////////////
    // Setup and Configuration
    //
	@Override
    public void setType(String type) {
        // "7_micron" or "13_micron"
        PrefsRT.usr.put("LC_Type", type);
    }


//-----------------------------------------------------------------------
// Settling Time
	@Override
    public void setSettlingTime(int _settleTime) {
        settleTime = _settleTime;
    }

	@Override
    public int getSettlingTime() {
        return settleTime;
    }

	@Override
    public synchronized void resetSettleTimer() {
        isSettled.reset();
    }


//----------------------------------------------------------------------
// sets and remembers new setting
//
	@Override
    public void setElement(int element, float retA, float retB) {
        retarderA[element] = retA;
        retarderB[element] = retB;
        setRetardance(retarderA[element], retarderB[element]);
        defineElement(element);
        JifUtils.waitFor(defineLatency);
        defined[element] = true;
        // change properties and save
        PrefsRT.usr.putFloat("LC_A" + String.valueOf(element),
                retarderA[element]);
        PrefsRT.usr.putFloat("LC_B" + String.valueOf(element),
                retarderB[element]);
        JifUtils.waitFor(getSettlingTime());
    }

	@Override
    public float getElementA(int element) {
        return retarderA[element];
    }

	@Override
    public float getElementB(int element) {
        return retarderB[element];
    }


// RetarderSwingMatrix
	@Override
    public void setRetardersToDefaults() {
        LC_Swing = (float) PrefsRT.usr.getDouble("LC_Swing", 0.10);
        // default values
        setElement(0, extinctA, extinctB);
        setElement(1, extinctA + LC_Swing, extinctB);
        setElement(2, extinctA, extinctB + LC_Swing);
        setElement(3, extinctA, extinctB - LC_Swing);
        setElement(4, extinctA - LC_Swing, extinctB);
    //PSjUtils.event("VariLC_RT set to defaults with swing of " + String.valueOf(LC_Swing));
    }

	@Override
    public int loadSettings() {
        wavelength = PrefsRT.usr.getFloat("wavelength", 546);
        setWavelength(wavelength);
        lcType = PrefsRT.usr.get("LC_Type", "7_micron");
        LC_Swing = (float) PrefsRT.usr.getDouble("LC_Swing", 0.03);
        settleTime = PrefsRT.usr.getInt("LC_SettleTime", 150);
        //
        setElement(0, (float) PrefsRT.usr.getDouble("LC_A0", extinctA),
                (float) PrefsRT.usr.getDouble("LC_B0", extinctB));
        setElement(1,
                (float) PrefsRT.usr.getDouble("LC_A1", extinctA + LC_Swing),
                (float) PrefsRT.usr.getDouble("LC_B1", extinctB));
        setElement(2, (float) PrefsRT.usr.getDouble("LC_A2", extinctA),
                (float) PrefsRT.usr.getDouble("LC_B2", extinctB + LC_Swing));
        setElement(3, (float) PrefsRT.usr.getDouble("LC_A3", extinctA),
                (float) PrefsRT.usr.getDouble("LC_B3", extinctB - LC_Swing));
        setElement(4,
                (float) PrefsRT.usr.getDouble("LC_A4", extinctA - LC_Swing),
                (float) PrefsRT.usr.getDouble("LC_B4", extinctB));
        //
        //System.out.println("VariLC_RT settings loaded from properties file.");
        return 0;
    }


    // <editor-fold defaultstate="collapsed" desc=">>>---  Get / Define / Select Elements ---<<<" >
	@Override
    public int showElements() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Element[" + i + "]: " + getElementA(i) + ", " + getElementB(i));
        }
        return 0;
    }

	@Override
    public int clearElements() {
        sendCommandAndWait("C1");
        for (int i = 0; i < 5; i++) {
            retarderA[i] = 0;
            retarderB[i] = 0;
            defined[i] = false;
        }
        return 0;
    }


    ////////////////////////////////////////////////////////////////////
    // Get the element values from the Palette in the VariLC_RT
    //  "D?" returns:
    //	D?
    //	5
    //	540.0 0.20000 0.35000
    //	540.0 0.20000 0.35000
    //	540.0 0.20000 0.35000
    //	540.0 0.20000 0.35000
    // 540.0 0.20000 0.35000
    //
	@Override
    public boolean getDefinedElements() {
        int n = 0;
        String line;
        String ss;
        String r = sendCommandAndWait("D?", 500);

        // +++ Test for null here
        //System.out.println(r);
        if (r == null) {
            System.err.println("Unable to getDefinedElements");
            return false;
        }
        try {
            StringTokenizer parser = new StringTokenizer(r, "\r\n");
            if (parser.hasMoreTokens()) {
                line = parser.nextToken();
                //System.out.println(line);
                if (line.startsWith("D")) {
                    try {
                        ss = parser.nextToken();
                        n = Integer.parseInt(ss); // number of defined elements
                        //System.out.println(n);
                        try {
                            for (int i = 0; i < n; i++) {
                                ss = parser.nextToken();
                                extract2(ss, i);
                            }
                        } catch (Exception e) {
                            DialogBox.boxError("VariLC_RT I/O Error",
                                    "Exception in getDefinedElements\n" + e.getMessage());
                            e.printStackTrace(System.err);
                            return false;
                        }
                    } catch (Exception e) {
                        DialogBox.boxError("VariLC_RT I/O Error",
                                "Exception in getDefinedElements\n" + e.getMessage());
                        e.printStackTrace(System.err);
                        return false;
                    }
                }
            }
        } catch (Exception exx) {
            DialogBox.boxError("VariLC_RT I/O Error",
                    "Exception in getDefinedElements\n" + exx.getMessage());
            exx.printStackTrace(System.err);
            return false;
        }
        return true;
    }


    // this pulls the A & B element settings out of each line as parsed
    private void extract2(String s, int element) {
        float wave;
        float a;
        float b = 0;
        StringTokenizer parser2 = new StringTokenizer(s, " \r\n");
        try {
            wave = Float.parseFloat(parser2.nextToken());
            a = Float.parseFloat(parser2.nextToken());
            b = Float.parseFloat(parser2.nextToken());
            System.out.println(element + ": " + wave + ", " + a + ", " + b);
            retarderA[element] = a;
            retarderB[element] = b;
            defined[element] = true;
        } catch (Exception e) {
            DialogBox.boxError("VariLC_RT I/O Error",
                    "Exception in getDefinedElements\n" + e.getMessage());
        }
    }


//------------------------- defineElement ---------------------------------
	@Override
    public boolean defineElement(int element) {
        sendCommandAndWait("D" + Integer.toString(element));
        // ++ errorHandling...
        return true;
    }


//-------------------------------------------------------------------------
//
	@Override
    public synchronized int selectElement(int element) {
        //if (defined[element]) {
        isSettled.reset();
        //sendCommandAndWait("P" + Integer.toString(element));
        sendCommandAndWait("P" + Integer.toString(element));
        selectedElement = element;
        // the condition isSettled will be set true when settleTime
        // has elapsed, but this method returns immediately
        return 0;
//        } else {
//            return -1;
//        }
    }

	@Override
    public synchronized int selectElementWait(int element) {
        //if (defined[element]) {
        sendCommandAndWait("P" + Integer.toString(element));
        selectedElement = element;
        JifUtils.waitFor(getSettlingTime());
        return 0;
//        } else {
//            return -1;
//        }
    }


//-------------------------------------------------------------------------
//
	@Override
    public int getSelectedElement() {
        return selectedElement;
    }

	@Override
    public int selectElementNext() {
        sendCommandAndWait("P>");
        if (selectedElement == 3) {
            selectedElement = 0;
        } else {
            selectedElement = selectedElement + 1;
        }
        return 0;
    }

	@Override
    public int selectElementPrev() {
        sendCommandAndWait("P<");
        if (selectedElement == 0) {
            selectedElement = 3;
        } else {
            selectedElement = selectedElement - 1;
        }
        return 0;
    }

    // </editor-fold>
//----------------- execute: trigger jump to next --------------------------
	@Override
    public int trigger() {
        sendCommandAndWait("X"); // eXecute
        //jump to next Element (like strobe, in mode 0 or 1)
        return 0;
    }


//======================= set mode =========================================
	@Override
    public int setMode(int mode) {
        sendCommandAndWait("M " + Integer.toString(mode));
        //0: cycle Element on strob;
        //1: Element random access
        //2: wavelength sweep on stobe
        //3: retardance sweep on strobe
        return 0;
    }

// ========================== ControlPort ==================================
	@Override
    public int enableControlPort(int on) {
        sendCommandAndWait("G " + Integer.toString(on)); //Go
        return 0;
    }


//-------------------------------------------------------------------------
// set LC Swing value
//
    //  public void setLCSwing(float _s) {
    //LC_Swing = _s;
    //PrefsRT.usr.putFloat("LC_Swing", LC_Swing);
    //PSjUtils.event("LC_Swing set to: " + String.valueOf(LC_Swing));
    //  }
// ============================================================= Wavelength
	@Override
    public int setWavelength(float wave) {
        sendCommandAndWait("W" + Float.toString(wave), 250);
//        wavelength = wave;
//        PrefsRT.usr.putFloat("wavelength", wavelength);
//        System.out.println("VariLC_RT wavelength set to: " + wavelength + " (nm)");
        return 0;
    }

	@Override
    public int setIncrementWavelength(float incr) {
        sendCommandAndWait("J" + Float.toString(incr)); //J for Jump
        incrementWavelength = incr;
        return 0;
    }

	@Override
    public int stepWavelengthUp() {
        sendCommandAndWait("W>"); // by setIncrementWavelength
        return 0;
    }

	@Override
    public int stepWavelengthDown() {
        sendCommandAndWait("W<"); // by setIncrementWavelength
        return 0;
    }


// ============================================================= Retardance
	@Override
    public int setRetardance(float retA, float retB) {
        if (quadParameters) {
            sendCommandAndWait("L " + Float.toString(retA) + " " + Float.toString(retB) + " 0.5 0.5");
        } else {
            sendCommandAndWait("L " + Float.toString(retA) + " " + Float.toString(retB));
        }
        return 0;
    }


// #####
	@Override
    public synchronized int setRetardanceWait(float retA, float retB) {
        sendCommandAndWait("L " + Float.toString(retA) + " " + Float.toString(retB));
        JifUtils.waitFor(getSettlingTime());
        return 0;
    }

	@Override
    public int setIncrementRetardance(float incr) {
        sendCommandAndWait("K" + Float.toString(incr));
        incrementRetardance =
                incr;
        return 0;
    }


//====================== set wavelength & retardance =====================
	@Override
    public int setState(float wave, float ret) {
        //Update
        sendCommandAndWait("U" + Float.toString(wave) + "," + Float.toString(ret));
        return 0;
    }


// Other ========================= Other ==================================
	@Override
    public int reset() {
        /** @todo add <esc>  as a prefix*/
        // escape();
        sendCommandAndWait("R1", 200);
        /** @todo deal with returned error code; add Table of Error Codes */
        return 0;
    }

	@Override
    public int reActivate() {
        /** @todo Add device addressing... */
        sendCommandAndWait("A1");
        return 0;
    }

	@Override
    public int deActivate() {
        //Sleep
        sendCommandAndWait("S0");
        return 0;
    }


// ===================== Single Character commands ======================
	@Override
    public int escape() {
        // <esc>
        char[] esc = {0x27};
        sendChar(esc);
        return 0;
    }

	@Override
    public boolean checkIfResponsive() {
        if (io == null) {
            return false;
        }
        char[] c = {'@'};
        sendChar(c);
        String r = io.receiveString(300);
        System.out.println("recv: " + r);
//        io.clearQueue();
        if (r.indexOf("@") == -1) {
            return false;
        } else {
            return true;
        }
    }

	@Override
    public boolean statusCheck() {
        if (io == null) {
            return false;
        }

        io.clearQueue();
        char[] c = {'@'};
        sendChar(c);
        char[] resp = io.receiveChars();
        return checkForError((byte) resp[1]);
    }

	@Override
    public float[] getElementSettings() {
        /** @todo ??  */
        return null;
    }

	@Override
    public boolean busyCheck() {
        char[] c = {'!'};
        sendChar(c);
        String resp = waitForResponse();
        // "<" = busy, ">" = not busy
        if (resp.indexOf(">") < 0) {
            return true;
        } else {
            return false;
        }

    }

	@Override
    public boolean checkForError(byte errByte) {
        FlagByte b = new FlagByte(errByte);
        // b.dump();
        return b.get(5);
    }

	@Override
    public int checkErrorFlags(byte errByte) {
        //
        //  VariLC StatusCheck Response Character Bits
        //  Bit      Description
        //  8 (msb)  <reserved>        Always 0
        //  7        <reserved>        Always 1
        //  6        errorStatus       none / pending
        //  5        terminator        <cr> / <lf>
        //  4        briefMode         normal / brief or auto-confirm
        //  3        paletteDefined    no / yes
        //  2        exercised         not yet / has been exercised
        //  1 (lsb)  initialized       not initialized / initialized
        //
        FlagByte b = new FlagByte(errByte);
        System.out.println("checkErrorFlags: " + Type.byteToHex(errByte));
        return 1;
    }

// <editor-fold defaultstate="collapsed" desc=">>>--- Low Level IO Functions ---<<<" >
	@Override
    public String sendCommand(String cmd) {
        try {
            //System.out.println("SendCmd: " + cmd);
            io.sendString(cmd);
        } catch (IOException ex) {
            System.err.println("IOException in VariLC_RT on SendCmd\n" + ex.getMessage());
        }

        return "-";
    }

	@Override
    public String sendCommandAndWait(String cmd) {
        io.clearQueue();
        sendCommand(cmd);
        return waitForResponse();
    }

	@Override
    public String sendCommandAndWait(String cmd, int time) {
        io.clearQueue();
        sendCommand(cmd);
        return waitForResponse(time);
    }

	@Override
    public void sendChar(char[] ch) {
        try {
            io.sendChars(ch);
        } catch (IOException ex) {
            System.err.println("IOException in VariLC_RT on SendCmd\n" + ex.getMessage());
        }

    }

	@Override
    public String waitForResponse(int time) {
        String r = io.receiveString(time);
        //String r = io.receiveString(50);
        //System.out.println("Receive: " + r.trim());
        return r; //r.trim();
    }

	@Override
    public String waitForResponse() {
        String r = io.receiveString();
        //System.out.println("Receive: " + r.trim());
        return r; //r.trim();
    }
// </editor-fold>
}
