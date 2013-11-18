/*
 * DialogPortSetting.java
 *
 * Created on March 8, 2007, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.varilc.attic;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.mbl.jif.comm.*;

/**
 *
 * @author GBH
 */
public class DialogPortSetting
        extends JDialog {
    
    BorderLayout borderLayout1 = new BorderLayout();
    SerialPortConnection port;
    PanelSerialConfig configPanel;
    
    public DialogPortSetting(Frame owner, String title, boolean modal,
            SerialPortConnection port) {
        super(owner, title, modal);
        this.port = port;
        try {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    
    public DialogPortSetting() {
        this(new Frame(), "Port Setting", true, new SerialPortConnection());
    }
    
    
    private void jbInit() throws Exception {
        setLayout(borderLayout1);
        configPanel = new PanelSerialConfig(port);
        add(configPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}
