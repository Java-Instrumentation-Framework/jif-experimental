package edu.mbl.jif.varilc.sap;

//import application.Application;
import edu.mbl.jif.comm.SerialConnectionException;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.comm.SerialPortMonitor;
import edu.mbl.jif.comm.SerialUtils;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.io.xml.ObjectStoreXML;
import edu.mbl.jif.utils.FileUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 * SLM
 * @author GBH
 */
public class SLMController {

    String PORT_OWNER_NAME = "SLM";
    private SerialPortConnection port;
    private SerialPortMonitor monitor;
    String commPort = "COM5";
    public SLMSettings settings;  // saved, calibrated values
    public double[] currentlySetA = new double[9];
    public double[] currentlySetB = new double[9];


    public SLMController() {
        loadRetardanceTables();
        settings = new SLMSettings();
        // load defaults 
        this.restoreSettings(FileUtil.getcwd() + "\\SLMSetsDefault.xml");
        settings.showSettings();
    //setTransmitOpaqueSettings();  // @todo change this to use settings from file

    }


    public void init() {
        setCircularToMaskState(bothStates);
        setEllipticalToMaskState(noneStates);
        setRetardersInit();
    }

    int initSetting = 1;
    public void setRetardersInit() {
        for (int sector = 0; sector < 9; sector++) {
            System.out.println("  Sector : " + sector);
            this.setRetA(sector, settings.retardSetA[initSetting][sector]);
            this.setRetB(sector, settings.retardSetB[initSetting][sector]);
        }
    }
    // <editor-fold desc=">>> Pixel Mapping <<<" defaultstate="collapsed">

    // Map cell/segment to pixel number ==================================
    // cell 4 (9)
    int[] retardSectorB = {126, 124, 122, 120, 118, 116, 114, 112, 110};
    // cell 3 (9)
    int[] retardSectorA = {108, 106, 104, 102, 100, 98, 96, 94, 92};
    // cell 2 (8)
    int[] ellipticalMaskPixel = {90, 88, 86, 84, 82, 80, 78, 76};
    // cell 1 (2)
    int[] circularMaskPixel = {74, 72};
    // Center = ? Surround = ?
    // for testing
    public HashMap<Integer, Integer> pixelValues = new HashMap<Integer, Integer>();


    public void setPixel(int pix, int value) {
        // System.out.println("    pix: " + pix + ", value: " + value);
        pixelValues.put(pix, value);
        defineElement(value, pix);
    }


    public int getPixel(int pix) {
        return pixelValues.get(pix);
    }


    public void dumpPixelValuesMap() {
        dumpMap(pixelValues);
    }


    public static void dumpMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    // </editor-fold>

    // <editor-fold desc=">>> Define Frames <<<" defaultstate="collapsed">
    int frame;


    public void setToFrame(int frame) {
        //?????
        selectMask(frame);
        selectActiveFrame(frame);
    }


    private void setDefiningFrame(int i) {
        frame = i;
        this.clearFrame(frame);
        this.selectDefiningFrame(frame);
    }

    // Defines mask and retarder settings for 25 frames
    public void defineFrames() {
        setDefiningFrame(0);
        setMask(Mask.North);
        runThruRetarderSettings();
        setMask(Mask.South);
        runThruRetarderSettings();
        setMask(Mask.East);
        runThruRetarderSettings();
        setMask(Mask.West);
        runThruRetarderSettings();
        setMask(Mask.Center);
        runThruRetarderSettings();
    }

    // Set both A & B retarder settings for all the sectors
    public void runThruRetarderSettings() {
        for (int setting = 0; setting < 5; setting++) {
            System.out.println("Retarder Setting: " + setting);
            for (int sector = 0; sector < 9; sector++) {
                System.out.println("  Sector : " + sector);
                this.setRetarderSector(sector, setting);
            }
            setDefiningFrame(frame++);
        }
    }
    // </editor-fold>

    // <editor-fold desc=">>>----- Masks -----<<<" defaultstate="collapsed">

    // functions mapping segments to pixels
    public void setElliptical(int n, double value) {
        setPixel(ellipticalMaskPixel[n], (int) value);
    }


    public void setCircular(int n, double value) {
        setPixel(circularMaskPixel[n], (int) value);
    }


    // MaskStates: 1 = transparent, 0 = opaque;
    // circular Mask States:
    public static int[] innerStates = {1, 0};
    public static int[] outerStates = {0, 1};
    public static int[] bothStates = {0,0};
    public static int[] nietherStates = {1, 1};
    // elliptical Mask States:
    public static int[] northStates = {1, 1, 0, 0, 0, 0, 0, 1};
    public static int[] southStates = {0, 0, 0, 1, 1, 1, 0, 0};
    public static int[] eastStates = {0, 1, 1, 1, 0, 0, 0, 0};
    public static int[] westStates = {0, 0, 0, 0, 0, 1, 1, 1};
    public static int[] noneStates = {1, 1, 1, 1, 1, 1, 1, 1};
    public static int[] allStates = {0, 0, 0, 0, 0, 0, 0, 0};


    public void setEllipticalToMaskState(int[] set) {
        for (int i = 0; i < set.length; i++) {
            if (set[i] == 1) {
                setElliptical(i, settings.ellipticalTransmit[i]);
            } else {
                setElliptical(i, settings.ellipticalExtinct[i]);
            }
        }
    }


    public void setCircularToMaskState(int[] set) {
        for (int i = 0; i < set.length; i++) {
            if (set[i] == 1) {
                setCircular(i, settings.circularTransmit[i]);
            } else {
                setCircular(i, settings.circularExtinct[i]);
            }
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- Set Calibrated settings ---<<<" >

    // Calibrated Mask settings for max. extinction and transmission

    // sets to test value...
//    public void setTransmitOpaqueSettings() {
//        for (int i = 0; i < settings.circularExtinct.length; i++) {
//            settings.circularExtinct[i] = SLMModel.circMin;
//            settings.circularTransmit[i] = SLMModel.circMax;
//        }
//        for (int i = 0; i < settings.ellipticalExtinct.length; i++) {
//            settings.ellipticalExtinct[i] = SLMModel.ellipMin;
//            settings.ellipticalTransmit[i] = SLMModel.ellipMax;
//        }
//    }
    public void setAllEllipticalToExtinction() {
        setEllipticalToMaskState(allStates);
    }


    public void setAllEllipticalToTransmission() {
        setEllipticalToMaskState(noneStates);
    }


    // <editor-fold defaultstate="collapsed" desc=">>>--- Set SLMSettings ---<<<" >
    public void setEllipticalExtinctSettings(double[] currentsettings) {
        for (int i = 0; i < currentsettings.length; i++) {
            settings.ellipticalExtinct[i] = (int) currentsettings[i];
        }
    }


    public void setEllipticalTransmitSettings(double[] currentsettings) {
        for (int i = 0; i < currentsettings.length; i++) {
            settings.ellipticalTransmit[i] = (int) currentsettings[i];
        }
    }


    public void setCircularExtinctSettings(double[] currentsettings) {
        for (int i = 0; i < currentsettings.length; i++) {
            settings.circularExtinct[i] = (int) currentsettings[i];
        }
    }


    public void setCircularTransmitSettings(double[] currentsettings) {
        for (int i = 0; i < currentsettings.length; i++) {
            settings.circularTransmit[i] = (int) currentsettings[i];
        }
    }


    public void setCalibratedRetarderASettings(int set,
                                                double[] currentsettings) {
        for (int sector = 0; sector < settings.retardSetA[set].length; sector++) {
            settings.retardSetA[set][sector] = (int) currentsettings[sector];
        }
    }


    public void setCalibratedRetarderBSettings(int set,
                                                double[] currentsettings) {
        for (int sector = 0; sector < settings.retardSetB[set].length; sector++) {
            settings.retardSetB[set][sector] = (int) currentsettings[sector];
        }
    }
    // </editor-fold>

    public void setMask(Mask mask) {
        switch (mask) {
            case North:
                System.out.println("Mask: North");
                setCircular(0, settings.circularTransmit[0]);
                setCircular(1, settings.circularTransmit[1]);
                setEllipticalToMaskState(northStates);
                break;
            case South:
                System.out.println("Mask: South");
                setCircular(0, settings.circularTransmit[0]);
                setCircular(1, settings.circularTransmit[1]);
                setEllipticalToMaskState(southStates);
                break;
            case East:
                System.out.println("Mask: East");
                setCircular(0, settings.circularTransmit[0]);
                setCircular(1, settings.circularTransmit[1]);
                setEllipticalToMaskState(eastStates);
                break;
            case West:
                System.out.println("Mask: West");
                setCircular(0, settings.circularTransmit[0]);
                setCircular(1, settings.circularTransmit[1]);
                setEllipticalToMaskState(westStates);
                break;
            case Center:
                System.out.println("Mask: Center");
                setCircular(0, settings.circularExtinct[0]);
                setCircular(1, settings.circularTransmit[1]);
                setEllipticalToMaskState(noneStates);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- RetardanceTables ---<<<" >
    public RetardanceTable retTableA;
    public RetardanceTable retTableB;


    public void loadRetardanceTables() {
        // load retarder tables
        retTableA = new RetardanceTable(FileUtil.getcwd() + "\\RetACalib.csv");
        retTableB = new RetardanceTable(FileUtil.getcwd() + "\\RetBCalib.csv");
    }
    // </editor-fold>}

    // <editor-fold defaultstate="collapsed" desc=">>>--- Retarders ---<<<" >
    /*
    Calibrated Retarder settings... 5 settings x 9 sectors array
     */
    public void setRetA(int sector, int value) {
        setPixel(retardSectorA[sector], value);
    }


    public int getRetA(int sector) {
        return getPixel(retardSectorA[sector]);
    }


    public double getRetardanceForSettingA(int sector, int setting) {
        return retTableA.getRetardance(sector, setting);
    }


    public void setRetA(int sector, double retardance) {
        if (retTableA == null) {
            return;
        }
        setPixel(retardSectorA[sector], retTableA.getSetting(sector, retardance));
        currentlySetA[sector] = retardance;
    }


    public void setRetB(int sector, int value) {
        setPixel(retardSectorB[sector], value);
    }


    public int getRetB(int sector) {
        return getPixel(retardSectorB[sector]);
    }


    public double getRetardanceForSettingB(int sector, int setting) {
        return retTableB.getRetardance(sector, setting);
    }


    public void setRetB(int sector, double retardance) {
        if (retTableB == null) {
            return;
        }
        setPixel(retardSectorB[sector], retTableB.getSetting(sector, retardance));
        currentlySetB[sector] = retardance;
    }


    public void setRetardersA(int setting) {
        for (int i = 0; i < 9; i++) {
            setRetA(i, settings.retardSetA[setting][i]);
        }
    }


    public void setRetardersA(double retardance) {
        if (retTableA == null) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            setRetA(i, retTableA.getSetting(i, retardance));
        }
    }


    public void setRetardersB(int setting) {
        for (int i = 0; i < 9; i++) {
            setRetB(i, settings.retardSetB[setting][i]);
        }
    }


    public void setRetardersB(double retardance) {
        if (retTableB == null) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            setRetB(i, retTableB.getSetting(i, retardance));
        }
    }


    public void setRetarderSector(int sector, int setting) {
        setRetA(sector, settings.retardSetA[setting][sector]);
        setRetB(sector, settings.retardSetB[setting][sector]);
    }


    // </editor-fold>

    // <editor-fold desc=">>> Exercise <<<" defaultstate="collapsed">
    public void exercise(final int nTimes, final long waitTime) {
        TaskSetAllTo task = new TaskSetAllTo(nTimes, waitTime);
        task.execute();
    }


    public void setAllTo(int value) {
        this.setAllRetarderATo(value);
        this.setAllRetarderBTo(value);
        this.setAllEllipticalTo(value);
        this.setAllCircularTo(value);
    }


    void setFullTransmission() {
        setAllRetarderATo(SLMModel.retardAMax);
        setAllRetarderBTo(SLMModel.retardBMax);
        setAllEllipticalTo(SLMModel.ellipMax);
        setAllCircularTo(SLMModel.circMax);
    }


    public void setAllRetarderATo(int value) {
        for (int i = 0; i < retardSectorA.length; i++) {
            setPixel(retardSectorA[i], value);
        }
    }


    public void setAllRetarderBTo(int value) {
        for (int i = 0; i < retardSectorB.length; i++) {
            setPixel(retardSectorB[i], value);
        }
    }


    public void setAllEllipticalTo(int value) {
        for (int i = 0; i < ellipticalMaskPixel.length; i++) {
            setPixel(ellipticalMaskPixel[i], value);
        }
    }


    public void setAllCircularTo(int value) {
        for (int i = 0; i < circularMaskPixel.length; i++) {
            setPixel(circularMaskPixel[i], value);
        }
    }


    class TaskSetAllTo
            extends SwingWorker {

        final int theNumber;
        final long waitTime;


        TaskSetAllTo(int theNumber, long waitTime) {
            this.theNumber = theNumber;
            this.waitTime = waitTime;
        }


        @Override
        public Object doInBackground() {
            int maxValue = 3500;
            int minValue = 500;
            int finalValue = 1000;

            for (int i = 0; i < theNumber; i++) {
                try {
                    setAllTo(maxValue);
                    Thread.currentThread().sleep(waitTime);
                    setAllTo(minValue);
                    Thread.currentThread().sleep(waitTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SLMController.class.getName()).log(Level.SEVERE,
                            null, ex);
                }
            }
            setAllTo(finalValue);
            return null;
        }


    }
    // </editor-fold>

    // <editor-fold desc=">>> SLM Command Set <<<" defaultstate="collapsed">

    // @todo check are lists for all of these...
    public void blockTransfer(int n) {
        sendCmd("B", n);
    }


    public void clearFrame(int n) {
        sendCmd("C", n);
    }


    public void defineElement(int value, int pixel) {
        sendCmd("D", value, pixel);
    }


    public void selectDefiningElement(int n) {
        sendCmd("E", n);
    }


    public void selectDefiningFrame(int n) {
        sendCmd("F", n);
    }


    public void selectMask(int n) {
        sendCmd("M", n);
    }


    public void setBlockXferTimeout(int n) {
        sendCmd("O", n);
    }


    public void selectActiveFrame(int n) {
        sendCmd("P", n);
    }


    public void version(int n) {
        sendCmd("V", n);
    }


    public void executeTrigger(int n) {
        sendCmd("C", n);
    }


    public void resetError() {
        sendCmd("R", 1);
    }


    public void checkForError() {
        sendCmd("R?");
        String error = waitForResponse();
        System.err.println("SLM Error: " + error);
    // check
    // interpret
    // report if error
    }


    public void sendCmd(String cmd) {
        sendCommand(cmd);
    }


    public void sendCmd(String cmd, int arg) {
        String argS = String.valueOf(arg);
        String toSend = cmd + " " + argS;
        sendCommand(toSend);
    }


    public void sendCmd(String cmd, int arg, int arg1) {
        String argS = String.valueOf(arg);
        String arg1S = String.valueOf(arg1);
        String toSend = cmd + " " + argS + " " + arg1S;
        sendCommand(toSend);
    }
    // </editor-fold>

    // <editor-fold desc=">>> Serial Device Control <<<" defaultstate="collapsed">
    public boolean initializeController() {
        // If (Include VariLC_RT in Initialization)&
//        vlcRT.reset();
//        vlcRT.setCommandFormat(this.getCommandFormat());
//        vlcRT.setUnits(0); // nanometers
//        vlcRT.setWavelength((float) getWavelength());
//        vlcRT.setSettlingTime(this.getSettleTime());
//        resp = vlcRT.sendCommandAndWait("I?", 500); // initialize
//        isCalibrated = false;
//        transmitAllElements();
        //isFunctional = true;
        System.out.println("SLM initialized");
        return true;
    }


    public boolean openConnection() {
        String commPortSLM = getCommPort();
        //statusMsg("Initializing VariLC on " + commPortVariLC);
        try {
            port = new SerialPortConnection();
            port.setBaudRate(460800);
            if (!SerialUtils.isPortAvailable(commPortSLM)) {
                // If it is not the port last used, ask user what to do...
                return openPortSettingsDialog(true);
            }
            String portToUse = port.setPortName(commPortSLM);
            port.setDebug(false);
            port.setWait(10L, 50);
            port.openConnection(PORT_OWNER_NAME);
        } catch (Exception ex1) {
            Application.getInstance().error("Could not open comPort:" + commPortSLM + "for SLM: " +
                    ex1.getMessage());
            return false;
        }
        return true;
    }


    public boolean openPortSettingsDialog(boolean onInitialize) {
        String portOwnerName = "SLM";
        if (getPort() != null) {
            getPort().closeConnection();
        }
        String message;
        String title;
        if (onInitialize) {
            title = "Serial port for " + portOwnerName + " not found";
            message = "Select alternative port and OK, or Cancel";
        } else {
            title = "Serial Port Settings for " + portOwnerName;
            message = "Change Serial Port Settings";
        }
        if (SerialUtils.openPortSettingsDialog(getPort(), title, message)) {
            try {
                setCommPort(getPort().getParameters().getPortName());
            } catch (SerialConnectionException ex) {
                ex.printStackTrace();
            }
            return true;
        } else {
            return false;
        }

    }


    public String getCommPort() {
        return commPort;
    }


    public void setCommPort(String commPort) {
        this.commPort = commPort;
    }


    public SerialPortConnection getPort() {
        return port;
    }


    public void openMonitor() {
        monitor = new SerialPortMonitor(port);
        JFrame monFrame = new JFrame("SLM I/O Monitor");
        monFrame.setSize(new Dimension(250, 400));
        monFrame.setLocation(100, 20);
        setMonitor(monitor);
        monFrame.add(monitor, BorderLayout.CENTER);
        monFrame.setVisible(true);
    }


    public void setMonitor(SerialPortMonitor _monitor) {
        monitor = _monitor;
        port.setMonitor(monitor);
    }


    public void clearMonitor() {
        port.setMonitor(null);
    }
    // </editor-fold>

    // <editor-fold desc=">>> Low Level IO Functions <<<" defaultstate="collapsed">
    public String sendCommand(String cmd) {
        if (!SLMModel.useMockRetarders) {
            try {
                //System.out.println("SendCmd: " + cmd);
                port.sendString(cmd);
            } catch (IOException ex) {
                System.err.println("IOException in SLM Controller on SendCmd\n" + ex.getMessage());
            }
        }
        return "-";
    }


    public String sendCommandAndWait(String cmd) {
        port.clearQueue();
        sendCommand(cmd);
        return waitForResponse();
    }


    public String sendCommandAndWait(String cmd, int time) {
        port.clearQueue();
        sendCommand(cmd);
        return waitForResponse(time);
    }


    public void sendChar(char[] ch) {
        try {
            port.sendChars(ch);
        } catch (IOException ex) {
            System.err.println("IOException in VariLC_RT on SendCmd\n" + ex.getMessage());
        }
    }


    public String waitForResponse(int time) {
        String r = port.receiveString(time);
        //String r = port.receiveString(50);
        System.out.println("Receive: " + r.trim());
        return r; //r.trim();
    }


    public String waitForResponse() {
        String r = port.receiveString();
        System.out.println("Receive: " + r.trim());
        return r; //r.trim();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- Settings, save/restore ---<<<" >
    void restoreSettings(String filename) {
        try {
            settings = (SLMSettings) ObjectStoreXML.read(filename);
        } catch (Throwable ex) {
            //Logger.getLogger(SLMController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }


    void saveSettings(String filename) {
        try {
            ObjectStoreXML.write(settings, filename);
        } catch (Throwable ex) {
            //Logger.getLogger(SLMController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }


    public void showSettings() {
        settings.showSettings();
    }
    // </editor-fold>

// Test ===========================================================
    public static void main(String[] args) {
        SLMController slm = new SLMController();
        slm.defineFrames();
    }

    /*
    Original lookup table from CRI...
     * 
    Cell	Segment	Pixel
    4	1	126
    4	2	124
    4	3	122
    4	4	120
    4	5	118
    4	6	116
    4	7	114
    4	8	112
    4	9	110
     * 
    3	1	108
    3	2	106
    3	3	104
    3	4	102
    3	5	100
    3	6	98
    3	7	96
    3	8	94
    3	9	92
     * 
    2	1	90
    2	2	88
    2	3	86
    2	4	84
    2	5	82
    2	6	80
    2	7	78
    2	8	76
     * 
    1	1	74
    1	2	72
     * 
     */

}

