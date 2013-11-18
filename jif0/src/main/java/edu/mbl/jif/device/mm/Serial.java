/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface Serial extends Device {

    // serial port constants
    public static final int _DATABITS_5 = 5;
    public static final int _DATABITS_6 = 6;
    public static final int _DATABITS_7 = 7;
    public static final int _DATABITS_8 = 8;
    public static final int _FLOWCONTROL_NONE = 0;
    public static final int _FLOWCONTROL_RTSCTS_IN = 1;
    public static final int _FLOWCONTROL_RTSCTS_OUT = 2;
    public static final int _FLOWCONTROL_XONXOFF_IN = 4;
    public static final int _FLOWCONTROL_XONXOFF_OUT = 8;
    public static final int _PARITY_EVEN = 2;
    public static final int _PARITY_MARK = 3;
    public static final int _PARITY_NONE = 0;
    public static final int _PARITY_ODD = 1;
    public static final int _PARITY_SPACE = 4;
    public static final int _STOPBITS_1 = 1;
    public static final int _STOPBITS_1_5 = 3;
    public static final int _STOPBITS_2 = 2;
    
    static DeviceType Type = DeviceType.SerialDevice;

    // Serial API

    public int setCommand(String command, String term);

    public int getAnswer(String txt, int maxChars, String term);

    public int write(String buf, long bufLen);

    public int read(String buf, long bufLen, long charsRead);

    public int purge();
};