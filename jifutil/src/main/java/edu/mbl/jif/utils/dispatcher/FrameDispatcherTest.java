package edu.mbl.jif.utils.dispatcher;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.holub.asynch.Synchronous_dispatcher;
import com.holub.tools.Generic_command;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class FrameDispatcherTest extends JFrame {

    JPanel contentPane;
    JPanel jPanel1 = new JPanel();
    JButton buttonGo = new JButton();
    JButton buttonDoThis = new JButton();
    JButton buttonSyncDispatch = new JButton();
    JButton buttonCommand = new JButton();
    JButton buttonProcess = new JButton();
    InterruptableFoxtrotProcessFrame iFrame;
    Synchronous_dispatcher dispatcher = new Synchronous_dispatcher();

    //Construct the frame
    public FrameDispatcherTest() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(null);
        this.setSize(new Dimension(478, 300));
        this.setTitle("Frame Title");
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setBounds(new Rectangle(11, 7, 206, 250));
        jPanel1.setLayout(null);
        buttonGo.setBounds(new Rectangle(30, 19, 82, 25));
        buttonGo.setText("Go");
        buttonGo.addActionListener(new Frame1_buttonGo_actionAdapter(this));
        buttonDoThis.setText("DoThis");
        buttonDoThis.setBounds(new Rectangle(30, 53, 82, 25));
        buttonDoThis.addActionListener(new Frame1_buttonDoThis_actionAdapter(this));
        buttonSyncDispatch.setBounds(new Rectangle(28, 89, 109, 28));
        buttonSyncDispatch.setText("SyncDispatch");
        buttonSyncDispatch.addActionListener(new Frame1_buttonSyncDispatch_actionAdapter(this));
        buttonCommand.setBounds(new Rectangle(30, 124, 102, 27));
        buttonCommand.setText("Do Command");
        buttonCommand.addActionListener(new Frame1_buttonCommand_actionAdapter(this));
        buttonProcess.setBounds(new Rectangle(36, 193, 150, 30));
        buttonProcess.setText("Run Interuptable Process");
        buttonProcess.addActionListener(new Frame1_buttonProcess_actionAdapter(this));
        jPanel1.add(buttonGo, null);
        jPanel1.add(buttonDoThis, null);
        jPanel1.add(buttonSyncDispatch, null);
        jPanel1.add(buttonCommand, null);
        jPanel1.add(buttonProcess, null);
        contentPane.add(jPanel1, null);
    }

    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    //-----------------------------------------------------------
    //
    void buttonGo_actionPerformed(ActionEvent e) {
        Dispatcher.doit();
    }

    //-----------------------------------------------------------
    //
    void buttonDoThis_actionPerformed(ActionEvent e) {
        System.out.println("DoThis starting...");
        Dispatcher.doThis("This and that and all those others too");
    }

    //-----------------------------------------------------------
    // Synchronous_dispatcher TEST
    //
    void buttonSyncDispatch_actionPerformed(ActionEvent e) {
        test();
    }

    public void test() {
        //    dispatcher.add_handler(
        //        new Runnable() { public void run() {
        //            System.out.print("hello");
        //          }
        //        }
        //    );
        //    dispatcher.add_handler(
        //        new Runnable() { public void run() {
        //            System.out.print(" world");
        //          }
        //        }
        //    );
        //    dispatcher.dispatch(1);
        //    dispatcher.metered_dispatch(2, 1000);

        //------------------------------------------------
        // Test two tasks, passed to the dispatcher as arrays
        // of chunks. Should print:
        //			Hello (Bonjour) world (monde)
        Runnable[] first_task = {
            new Runnable() {

                public void run() {
                    System.out.print("Hello");
                }

            },
            new Runnable() {

                public void run() {
                    System.out.print(" World");
                }

            }
        };

        Runnable[] second_task = {
            new Runnable() {

                public void run() {
                    System.out.print(" Bonjour");
                }

                ;
            },
            new Runnable() {

                public void run() {
                    System.out.print(" Monde\n");
                }

            }
        };

        //    dispatcher = new Synchronous_dispatcher();
        dispatcher.add_handler(first_task);
        dispatcher.dispatch(1);
        dispatcher.remove_all_handlers();
        dispatcher.add_handler(second_task);
        dispatcher.dispatch(1);
    }

    public void print(String first, String second) { // must be public
        System.out.println(first + " " + second);
    }

    public void showNums(java.lang.Integer first, java.lang.Double second) { // must be public
        System.out.println(first + " " + second);
    }

    void buttonCommand_actionPerformed(ActionEvent e) {
        testiness(3, 5.04);
        try {
            Runnable command =
                new Generic_command(this, "print", new Object[]{"This", " and that"});
            command.run();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testiness(int _x, double _y) {
        int x = _x;
        double y = _y;
        try {
            Runnable command =
                new Generic_command(this, "showNums", new Object[]{new Integer(x), new Double(y)});
            command.run();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    void buttonProcess_actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

                                   public void run() {
                                       System.out.println("Starting InterruptableProcessFrame...");
                                       iFrame = new InterruptableFoxtrotProcessFrame();
                                       iFrame.setPrompts("BigProcess", "Start it up");
                                       iFrame.setLocation(100, 100);
                                       iFrame.setSize(283, 132);
                                       iFrame.setVisible(true);
                                   }

                               });
    }

}

class Frame1_buttonGo_actionAdapter implements java.awt.event.ActionListener {

    FrameDispatcherTest adaptee;

    Frame1_buttonGo_actionAdapter(FrameDispatcherTest adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonGo_actionPerformed(e);
    }

}

class Frame1_buttonDoThis_actionAdapter implements java.awt.event.ActionListener {

    FrameDispatcherTest adaptee;

    Frame1_buttonDoThis_actionAdapter(FrameDispatcherTest adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonDoThis_actionPerformed(e);
    }

}

class Frame1_buttonSyncDispatch_actionAdapter implements java.awt.event.ActionListener {

    FrameDispatcherTest adaptee;

    Frame1_buttonSyncDispatch_actionAdapter(FrameDispatcherTest adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonSyncDispatch_actionPerformed(e);
    }

}

class Frame1_buttonCommand_actionAdapter implements java.awt.event.ActionListener {

    FrameDispatcherTest adaptee;

    Frame1_buttonCommand_actionAdapter(FrameDispatcherTest adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonCommand_actionPerformed(e);
    }

}

class Frame1_buttonProcess_actionAdapter implements java.awt.event.ActionListener {

    FrameDispatcherTest adaptee;

    Frame1_buttonProcess_actionAdapter(FrameDispatcherTest adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonProcess_actionPerformed(e);
    }

}
