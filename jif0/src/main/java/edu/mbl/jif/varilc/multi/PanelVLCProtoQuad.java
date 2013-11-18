package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.gui.html.HTMLDisplay;
import edu.mbl.jif.varilc.camacq.VariLC_RT;
import java.io.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mbl.jif.comm.*;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.utils.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PanelVLCProtoQuad extends JPanel {
  JPanel jPanel1 = new JPanel();
  static DecimalFormat dFmt = new DecimalFormat("0.000");
  private boolean initialized = false;
  SerialPortConnection port;
  SerialPortMonitor monitor;
//0.51
//0.31
//.43
//.38

  static SpinPrefDouble[] retardSpin = {
      new SpinPrefDouble("q0-0", "q0-0", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q1-0", "q1-0", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q2-0", "q2-0", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q3-0", "q4-0", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q0-45", "q0-45", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q1-45", "q1-45", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q2-45", "q2-45", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q3-45", "q4-45", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q0-A", "q0-A", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q1-A", "q1-A", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q2-A", "q2-A", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
      new SpinPrefDouble("q3-A", "q4-A", 0.5, 0.01, 1.999, 0.01, "0.00", 4),
  };
  SpinPrefDouble wave = new SpinPrefDouble("Wavelength", "wavelength",
      546, 300, 1000, 1, "000", 4);

  JButton buttonSend = new JButton();

  //
  static VariLC_RT vlcRT;
  GridLayout gridLayout1 = new GridLayout();
  JButton button_Initialize = new JButton();
  JButton button_Exercise = new JButton();
  JButton button_CheckStatus = new JButton();
  JButton button_Reset = new JButton();
  SerialPortMonitor mon;
  JButton jButton1 = new JButton();
  JButton buttonEsc = new JButton();
  JPanel panelOther = new JPanel();

  public PanelVLCProtoQuad() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void doUpdate() {

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        sendCurrentSettingsToVariLC();
      }
    });

  }

  void jbInit() throws Exception {
    this.setLayout(null);
    this.setPreferredSize(new Dimension(300, 400));
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.setBounds(new Rectangle(22, 16, 480, 138));
    jPanel1.setLayout(gridLayout1);
    for (int i = 0; i < retardSpin.length; i++) {
      jPanel1.add(retardSpin[i]);
    }
    buttonSend.setBackground(new Color(248, 124, 99));
    buttonSend.setBounds(new Rectangle(427, 209, 75, 29));
    buttonSend.setFont(new java.awt.Font("Dialog", 0, 12));
    buttonSend.setText("Update");
    buttonSend.addActionListener(new PanelVLCProto_buttonSend_actionAdapter(this));
    gridLayout1.setColumns(4);
    gridLayout1.setHgap(12);
    gridLayout1.setRows(3);
    gridLayout1.setVgap(4);
    button_Initialize.setBackground(new Color(212, 165, 255));
    button_Initialize.setBounds(new Rectangle(209, 213, 58, 24));
    button_Initialize.setFont(new java.awt.Font("Dialog", 0, 10));
    button_Initialize.setMaximumSize(new Dimension(55, 24));
    button_Initialize.setMinimumSize(new Dimension(55, 24));
    button_Initialize.setPreferredSize(new Dimension(55, 24));
    button_Initialize.setMargin(new Insets(2, 4, 2, 4));
    button_Initialize.setText("Re-Init");
    button_Initialize.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Initialize_actionPerformed(e);
      }
    });
    button_Exercise.setBackground(new Color(212, 165, 255));
    button_Exercise.setBounds(new Rectangle(274, 213, 76, 24));
    button_Exercise.setFont(new java.awt.Font("Dialog", 0, 10));
    button_Exercise.setMaximumSize(new Dimension(55, 24));
    button_Exercise.setMinimumSize(new Dimension(55, 24));
    button_Exercise.setPreferredSize(new Dimension(55, 24));
    button_Exercise.setToolTipText("");
    button_Exercise.setMargin(new Insets(2, 4, 2, 4));
    button_Exercise.setText("Exercise LCs");
    button_Exercise.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Exercise_actionPerformed(e);
      }
    });
    button_CheckStatus.setBackground(new Color(212, 165, 255));
    button_CheckStatus.setBounds(new Rectangle(20, 212, 76, 24));
    button_CheckStatus.setFont(new java.awt.Font("Dialog", 0, 10));
    button_CheckStatus.setMaximumSize(new Dimension(55, 24));
    button_CheckStatus.setMinimumSize(new Dimension(55, 24));
    button_CheckStatus.setPreferredSize(new Dimension(55, 24));
    button_CheckStatus.setMargin(new Insets(2, 4, 2, 4));
    button_CheckStatus.setText("Check Status");
    button_CheckStatus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_CheckStatus_actionPerformed(e);
      }
    });
    button_Reset.setText("Reset");
    button_Reset.setMargin(new Insets(2, 4, 2, 4));
    button_Reset.setToolTipText("");
    button_Reset.setBackground(new Color(212, 165, 255));
    button_Reset.setBounds(new Rectangle(103, 213, 57, 24));
    button_Reset.setFont(new java.awt.Font("Dialog", 0, 10));
    button_Reset.setMaximumSize(new Dimension(55, 24));
    button_Reset.setMinimumSize(new Dimension(55, 24));
    button_Reset.setPreferredSize(new Dimension(55, 24));
    button_Reset.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Reset_actionPerformed(e);
      }
    });

    //
    initializeVariLC();
    mon = new SerialPortMonitor(port);
    mon.setBounds(new Rectangle(20, 249, 484, 344));
    setMonitor(mon);
    //
    jButton1.setBounds(new Rectangle(362, 214, 47, 23));
    jButton1.setFont(new java.awt.Font("Dialog", 0, 10));
    jButton1.setMargin(new Insets(2, 4, 2, 4));
    jButton1.setText("Help");
    jButton1.addActionListener(new PanelVLCProto_jButton1_actionAdapter(this));
    buttonEsc.setBounds(new Rectangle(166, 213, 37, 25));
    buttonEsc.setFont(new java.awt.Font("Dialog", 0, 10));
    buttonEsc.setMargin(new Insets(2, 4, 2, 4));
    buttonEsc.setText("Esc");
    buttonEsc.addActionListener(new PanelVLCProto_buttonEsc_actionAdapter(this));
    panelOther.setBorder(BorderFactory.createEtchedBorder());
    panelOther.setBounds(new Rectangle(22, 160, 480, 41));
    wave.setBounds(5, 5, 50, 16);
    panelOther.add(wave, null);
    this.add(jPanel1, null);
    this.add(mon, null);
    this.add(panelOther, null);
    this.add(buttonSend, null);
    this.add(button_CheckStatus, null);
    this.add(button_Reset, null);
    this.add(button_Exercise, null);
    this.add(jButton1, null);
    this.add(button_Initialize, null);
    this.add(buttonEsc, null);
    this.setPreferredSize(new Dimension(540, 700));
//    new ComponentDependencyHandler(buttonInit) {
//      public void dependencyNotification() {
//        buttonMonitor.setEnabled(initialized);
//      }
//    };
  }

//----------------------------------------------------------------------------
  // VariLC Initialization
  //
  void initializeVariLC() {
    if (PrefsRT.usr.getBoolean("COM_PORT_VARILC_Enabled", true)) {
      String commPortVariLC = "COM5";
      //Prefs.usr.get("COM_PORT_VARILC", "COM1");
      //PSjUtils.statusProgress("Initializing VariLC on " + commPortVariLC, 20);
      try {
        port = new SerialPortConnection();
        port.setBaudRate(9600);
        port.setPortName(commPortVariLC);
        port.setDebug(false);
        port.setWait(10L, 50);
        port.openConnection("VariLC");
      } catch (IOException ex1) {
        System.out.println("Could not open comPort:" + commPortVariLC +
            "for VariLC: " + ex1.getMessage());
      } catch (Exception ex) {
        System.err.println(
            "Could not open comPort:" + commPortVariLC + "for VariLC");
        return;
      }
      try {
        vlcRT = new VariLC_RT(port);
      } catch (Exception ex2) {
        System.err.println("Exception on new VariLC:" + ex2.getMessage());
      }
      System.out.println("vlcRT.isFunctional " + vlcRT.isFunctional);
      if (vlcRT.isFunctional) {
//         if (!vlcRT.statusCheck()) {
//           vlcRT.reset();
//           PSjUtils.waitFor(250);
//         }
        if (!vlcRT.statusCheck()) {
          System.err.println("VariLC reports bad status after reset.");
          return;
        }
        //vlcRT.getDefinedElements();
      } else {
        System.err.println("Error: VariLC on comPort:" +
            commPortVariLC + " not functioning");
        return;
      }
      initialized = true;
      System.out.println("VariLC initialized");
    }
  }

  public void setMonitor(SerialPortMonitor _monitor) {
    monitor = _monitor;
    port.setMonitor(monitor);
  }

  public void clearMonitor() {
    port.setMonitor(null);
  }

  //----------------------------------------------------------------------------

  void buttonSend_actionPerformed(ActionEvent e) {
    sendCurrentSettingsToVariLC();
  }

  static void sendCurrentSettingsToVariLC() {
    String s = "L";
    for (int i = 0; i < retardSpin.length; i++) {
      s = s + " " + dFmt.format(retardSpin[i].getValue());
    }
    vlcRT.sendCommand(s);
  }

//-------------------------------------------------------------------------

  public static void main(String[] args) {
    PanelVLCProtoQuad p = new PanelVLCProtoQuad();

    FrameForTest ft = new FrameForTest(p);
//    ft.setSize(new Dimension(550, 500));
  }

  void button_Initialize_actionPerformed(ActionEvent e) {
    vlcRT.initialize();
  }

  void button_Reset_actionPerformed(ActionEvent e) {
    vlcRT.reset();
  }

  void button_Exercise_actionPerformed(ActionEvent e) {
    vlcRT.exercise();
  }

  void button_CheckStatus_actionPerformed(ActionEvent e) {
    if (!vlcRT.statusCheck()) {
    //   DialogBoxes.boxError("VariLC Status", "Error!");
    }
  }

  void buttonEsc_actionPerformed(ActionEvent e) {
    vlcRT.escape();
  }

  void jButton1_actionPerformed(ActionEvent e) {
    JFrame f = new JFrame();
    HTMLDisplay hd = new HTMLDisplay();
    f.getContentPane().add(hd);
    File file = new File("VariLCCodes.htm");
    hd.showURL("file:" + file.getAbsolutePath());
    f.setSize(new Dimension(680, 650));
    f.setVisible(true);
  }

//----------------------------------------------------------------------------

}
class PanelVLCProto_buttonSend_actionAdapter
    implements java.awt.event.ActionListener {
  PanelVLCProtoQuad adaptee;

  PanelVLCProto_buttonSend_actionAdapter(PanelVLCProtoQuad adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSend_actionPerformed(e);
  }
}

class PanelVLCProto_buttonEsc_actionAdapter
    implements java.awt.event.ActionListener {
  PanelVLCProtoQuad adaptee;

  PanelVLCProto_buttonEsc_actionAdapter(PanelVLCProtoQuad adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.buttonEsc_actionPerformed(e);
  }
}

class PanelVLCProto_jButton1_actionAdapter
    implements java.awt.event.ActionListener {
  PanelVLCProtoQuad adaptee;

  PanelVLCProto_jButton1_actionAdapter(PanelVLCProtoQuad adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}
