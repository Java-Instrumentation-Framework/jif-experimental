package edu.mbl.jif.comm;


import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.text.DecimalFormat;

import javax.swing.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class SerialPortTester extends JFrame {
    static DecimalFormat dFmt = new DecimalFormat("0.000");
    private boolean initialized = false;
    JButton buttonOpen = new JButton("Open");
    SerialPortConnection port;
    SerialPortMonitor mon;
    PanelSerialConfig config;

    public SerialPortTester() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(300, 400));
        //
        initialize();

        // Monitor
        mon = new SerialPortMonitor(port);
        this.add(mon, BorderLayout.CENTER);

        // Configuration Panel
        config = new PanelSerialConfig(port);
        this.add(config, BorderLayout.NORTH);

        // Open/Close button
        buttonOpen.setText("Open");
        this.add(buttonOpen, BorderLayout.SOUTH);
        buttonOpen.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!port.isOpen()) {
                            try {
                                port.openConnection("Test");
                                buttonOpen.setText("Close");
                            } catch (IOException ex) {
                                System.out.println(
                                        "Could not open comPort:" + port.getPortName() + ": " +
                                        ex.getMessage());
                            }
                        } else {
                            port.closeConnection();
                            buttonOpen.setText("Open");
                        }
                    }
                });
        pack();
        setVisible(true);

        //    new ComponentDependencyHandler(buttonInit) {
        //      public void dependencyNotification() {
        //        buttonMonitor.setEnabled(initialized);
        //      }
        //    };
    }

    //----------------------------------------------------------------------------
    // VariLC Initialization
    //
    void initialize() {
        port = new SerialPortConnection();
        port.setBaudRate(9600);
        port.setPortName("COM4"); 
        port.setDebug(false);
        port.setWait(10L, 50);
        port.setTerminatorSend(new char[]{SerialPortConnection.CHAR_CR});
        //port.openConnection("VariLC");
        //      }
        //      catch (IOException ex1) {
        //         System.out.println("Could not open comPort:" + commPortVariLC +
        //                            ": " + ex1.getMessage());
        //      }
        //      catch (Exception ex) {
        //         System.err.println(
        //               "Could not open comPort:" + commPortVariLC);
        //         return;
        //      }
        initialized = true;
        System.out.println("CommPort initialized");
        //}
    }

    public void clearMonitor() {
        port.setMonitor(null);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            port.closeConnection();
            System.exit(0);
        }
    }

    //-------------------------------------------------------------------------
    public static void main(String[] args) {
        SerialPortTester p = new SerialPortTester();

        //FrameForTest ft = new FrameForTest(p);
        //   ft.setSize(new Dimension(550, 500));
    }

    //----------------------------------------------------------------------------
}
