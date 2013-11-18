package tests.gui;

import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.gui.value.PanelValuePoint;
import edu.mbl.jif.utils.PrefsRT;
import edu.mbl.jif.varilc.camacq.VariLC_RT;

import ij.ImagePlus;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import javax.swing.*;


public class TestingFrame extends JFrame {
    JPanel contentPane;
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu jMenuFile = new JMenu();
    JMenuItem jMenuFileExit = new JMenuItem();
    JMenu jMenuHelp = new JMenu();
    JMenuItem jMenuHelpAbout = new JMenuItem();
    JToolBar jToolBar = new JToolBar();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    JLabel statusBar = new JLabel();
    JButton buttonGo = new JButton();
    JPanel panelResults = new JPanel();

    //JPanel panelActionButtons = new JPanel();
    TestCommandlButtons panelActionButtons = new TestCommandlButtons();
    JScrollPane jScrollPane1;
    JTextArea jTextPane1 = new JTextArea();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel panelMain = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    VariLC_RT vlcRT;
    SerialPortConnection port;

    /**Construct the frame*/
    public TestingFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Component initialization*/
    private void jbInit() throws Exception {
        image1 = new ImageIcon(application.TestingFrame.class.getResource("icons/openFile.png"));
        image2 = new ImageIcon(application.TestingFrame.class.getResource("icons/closeFile.png"));
        image3 = new ImageIcon(application.TestingFrame.class.getResource("icons/help.png"));

        //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame1.class.getResource("[Your Icon]")));
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(null);
        this.setSize(new Dimension(616, 695));
        this.setTitle("Tester");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setText(" ");
        statusBar.setBounds(new Rectangle(8, 620, 572, 23));
        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuFileExit_actionPerformed(e);
                }
            });
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jMenuHelpAbout_actionPerformed(e);
                }
            });
        panelMain.setLayout(borderLayout2);
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuHelp);
        //
        this.setJMenuBar(jMenuBar1);
        jButton1.setIcon(image1);
        jButton1.setToolTipText("Open File");
        jButton2.setIcon(image2);
        jButton2.setToolTipText("Close File");
        jButton3.setIcon(image3);
        jButton3.setToolTipText("Help");
        jToolBar.setBounds(new Rectangle(1, 4, 581, 33));
        jToolBar.add(jButton1);
        jToolBar.add(jButton2);
        jToolBar.add(jButton3);
        //
        panelMain.setBorder(BorderFactory.createEtchedBorder());
        panelMain.setBounds(new Rectangle(156, 44, 418, 431));
        //
        addComponents();
        contentPane.add(panelMain, null);
        //
        panelActionButtons.setBorder(BorderFactory.createRaisedBevelBorder());
        panelActionButtons.setBounds(new Rectangle(8, 43, 139, 573));
        //
        TestsToTest ttt = new TestsToTest(panelActionButtons);  // <<<<<<
        //
        panelResults.setBorder(BorderFactory.createEtchedBorder());
        panelResults.setBounds(new Rectangle(153, 481, 422, 134));
        panelResults.setLayout(borderLayout1);
        jTextPane1.setText("jTextPane1");
        jScrollPane1 = new JScrollPane(jTextPane1);
        //jScrollPane1.getViewport().add(jTextPane1, null);
        panelResults.add(jScrollPane1, BorderLayout.CENTER);
        contentPane.add(panelResults, null);
        contentPane.add(panelActionButtons, null);
        contentPane.add(statusBar, null);
        contentPane.add(jToolBar, null);
    }

    /**File | Exit action performed*/
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /**Help | About action performed*/
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    }

    /**Overridden so we can exit when window is closed*/
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }

    //----------------------------------------------------------------
    //
    void addComponents() {
        PanelValuePoint vpPanel = new PanelValuePoint();
        panelMain.add(vpPanel, BorderLayout.SOUTH);
    }



    void CaptureToStack() {
        CamAcq camAcq = CamAcq.getInstance();
        //initializeVariLC ();
        // edu.mbl.jif.comm.SerialPortConnection.setDebug(0)
        //vlcRT.attachMonitor()
        /*vlcRT.outputConfirm("off");
               vlcRT.setNumOfElements(2);
               vlcRT.setUnits(0);
               vlcRT.clearPalette();
               double retardanceSwing = .03;
               vlcRT.setWavelength(500);
               vlcRT.setRetardance(0.25, 0.5);
               vlcRT.definePalette(0);
               vlcRT.setRetardance(0.25 + retardanceSwing, 0.5);
               vlcRT.definePalette(1);
               vlcRT.setRetardance(0.25, 0.5 + retardanceSwing);
               vlcRT.definePalette(2);
               vlcRT.setRetardance(0.25, 0.5 - retardanceSwing);
               vlcRT.definePalette(3);
               vlcRT.setRetardance(0.25 - retardanceSwing, 0.5);
               vlcRT.definePalette(4);
         */
        camAcq.setMultiFrame(1);

        double exp = camAcq.getExposure();

        //vlcRT.selectPalette(0);
        int imagesInStack = 5;

        // Assuming 8 bit acquisition!!!
        ImagePlus img = camAcq.newCaptureStackByte(imagesInStack);
        //newCaptureStack(8, nSlices); // pre-allocate slices
        //setCaptureStack(imgPlus);
        camAcq.startAcq();
        for (int z = 0; z < imagesInStack; z++) {
            camAcq.captureImageToStack();

            //vlcRT.execute();
            //camAcq.wait(50);
        }

        camAcq.captureStackFinish();
        img.updateAndDraw();
        img.show();
        // camAcq.setExposure(initexp)
        //vlcRT.selectPalette(1);
        camAcq.displayResume();

        //swing.SwingUtilities.invokeLater(img.updateAndRepaintWindow())
    }

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
                System.out.println("Could not open comPort:" + commPortVariLC + "for VariLC: " +
                    ex1.getMessage());
            } catch (Exception ex) {
                System.err.println("Could not open comPort:" + commPortVariLC + "for VariLC");

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
                System.err.println("Error: VariLC on comPort:" + commPortVariLC +
                    " not functioning");

                return;
            }

            //initialized = true;
            System.out.println("VariLC initialized");
        }
    }
}
