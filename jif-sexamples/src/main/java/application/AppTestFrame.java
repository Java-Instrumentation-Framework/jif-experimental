package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AppTestFrame
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
    public AppTestFrame () {
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
        image1 = new ImageIcon(AppTestFrame.class.getResource(
                "./icons/openFile.png"));
        image2 = new ImageIcon(AppTestFrame.class.getResource(
                "./icons/closeFile.png"));
        image3 = new ImageIcon(AppTestFrame.class.getResource(
                "./icons/help.png"));
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("Frame Test");
        statusBar.setText(" ");
        jMenuFile.setText("File");
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new
                                        FrameTest_jMenuFileExit_ActionAdapter(this));
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new
                                         FrameTest_jMenuHelpAbout_ActionAdapter(this));
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
    }


    //Overridden so we can exit when window is closed
    protected void processWindowEvent (WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }
}

class FrameTest_jMenuFileExit_ActionAdapter
        implements ActionListener
{
    AppTestFrame adaptee;

    FrameTest_jMenuFileExit_ActionAdapter (AppTestFrame adaptee) {
        this.adaptee = adaptee;
    }


    public void actionPerformed (ActionEvent e) {
        adaptee.jMenuFileExit_actionPerformed(e);
    }
}

class FrameTest_jMenuHelpAbout_ActionAdapter
        implements ActionListener
{
    AppTestFrame adaptee;

    FrameTest_jMenuHelpAbout_ActionAdapter (AppTestFrame adaptee) {
        this.adaptee = adaptee;
    }


    public void actionPerformed (ActionEvent e) {
        adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}
