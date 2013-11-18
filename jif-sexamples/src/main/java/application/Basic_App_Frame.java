package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.mbl.jif.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Basic_App_Frame
        extends JFrame
{
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
    BorderLayout borderLayout1 = new BorderLayout();

    //Construct the frame
    public Basic_App_Frame () {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Component initialization
    private void jbInit () throws Exception {
        image1 = new ImageIcon(application.Basic_App_Frame.class.getResource(
                "icons/openFile.png"));
        image2 = new ImageIcon(application.Basic_App_Frame.class.getResource(
                "icons/closeFile.png"));
        image3 = new ImageIcon(application.Basic_App_Frame.class.getResource(
                "icons/help.png"));
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("AppFrame");
        statusBar.setText(" ");
        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new
                                        FrameApp_jMenuFileExit_ActionAdapter(this));
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new
                                         FrameApp_jMenuHelpAbout_ActionAdapter(this));
        jButton1.setIcon(image1);
        jButton1.setToolTipText("Open File");
        jButton2.setIcon(image2);
        jButton2.setToolTipText("Close File");
        jButton3.setIcon(image3);
        jButton3.setToolTipText("Help");
        jToolBar.add(jButton1);
        jToolBar.add(jButton2);
        jToolBar.add(jButton3);
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuHelp);
        this.setJMenuBar(jMenuBar1);
        contentPane.add(jToolBar, BorderLayout.NORTH);
        contentPane.add(statusBar, BorderLayout.SOUTH);
    }


    //File | Exit action performed
    public void jMenuFileExit_actionPerformed (ActionEvent e) {
        System.exit(0);
    }


    //Help | About action performed
    public void jMenuHelpAbout_actionPerformed (ActionEvent e) {
        Basic_App_Frame_AboutBox dlg = new Basic_App_Frame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                        (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }


    //Overridden so we can exit when window is closed
    protected void processWindowEvent (WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }
}

class FrameApp_jMenuFileExit_ActionAdapter
        implements ActionListener
{
    Basic_App_Frame adaptee;

    FrameApp_jMenuFileExit_ActionAdapter (Basic_App_Frame adaptee) {
        this.adaptee = adaptee;
    }


    public void actionPerformed (ActionEvent e) {
        adaptee.jMenuFileExit_actionPerformed(e);
    }
}

class FrameApp_jMenuHelpAbout_ActionAdapter
        implements ActionListener
{
    Basic_App_Frame adaptee;

    FrameApp_jMenuHelpAbout_ActionAdapter (Basic_App_Frame adaptee) {
        this.adaptee = adaptee;
    }


    public void actionPerformed (ActionEvent e) {
        adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}
