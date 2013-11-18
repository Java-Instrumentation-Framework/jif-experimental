package edu.mbl.jif.comm;

import java.io.IOException;
import java.text.DecimalFormat;

public class CommTest1 {

    private boolean initialized = false;
    SerialPortConnection port;

    public CommTest1() {
        initialize();
    }

    //
    void initialize() {
        String commPort = "COM3";
        try {
            port = new SerialPortConnection();
            port.setBaudRate(9600);
            port.setPortName(commPort);
            port.setDebug(false);
            port.setWait(10L, 50);
            port.openConnection("Test");
            port.createMonitorFrame();
        } catch (IOException ex1) {
            System.out.println("Could not open comPort:" + commPort +
                    ": " + ex1.getMessage());
        } catch (Exception ex) {
            System.err.println(
                    "Could not open comPort:" + commPort);
            return;
        }
        initialized = true;
        System.out.println("CommPort initialized");
        port.send("Testing from Com6");

    }

//-------------------------------------------------------------------------
    public static void main(String[] args) {
        CommTest1 p = new CommTest1();

    }
}
