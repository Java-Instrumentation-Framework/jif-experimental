package edu.mbl.jif.script;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.util.JConsole;
//import edu.mbl.jif.camera.camacq.CamAcq;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BeanShellConsole
        extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    
    //Construct the frame
    public BeanShellConsole() {
        super("BeanShell");
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    //Component initialization
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame1.class.getResource("[Your Icon]")));
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("Frame Title");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
                JConsole console = new JConsole();
                contentPane.add(console);
                Interpreter interpreter = new Interpreter(console);
                try {
                    //interpreter.set("cam", CamAcq.getInstance());
                    interpreter.set("thisApp", this); // Provide a reference to your app
                    interpreter.eval("setAccessibility(true)"); // turn off access restrictions
                } catch (EvalError ex) {
                    ex.printStackTrace();
                }
                new Thread(interpreter).start(); // start a thread to call the run() method
                setVisible(true);
//            }
//        });
        
        
    }
    
    
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//            interpreter = null;
            this.dispose();
            //System.exit(0);
            
        }
    }
    public static void main(String[] args) {
        new BeanShellConsole();
    }
}
