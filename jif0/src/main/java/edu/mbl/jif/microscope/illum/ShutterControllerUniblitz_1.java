package edu.mbl.jif.microscope.illum;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.device.SerialDevice;


/**
 *
 * Controls Uniblitz shutter - single channel controller
 *
 * @author GBH
 */
public class ShutterControllerUniblitz_1 extends SerialDevice implements Shutter {

    InstrumentController instCtrl;
    
    public ShutterControllerUniblitz_1(InstrumentController instCtrl, String commport) {
        super(instCtrl, "ShutterUniblitz1", commport);
        //super.port = port;
        //getSerialPortConnection().setTerminatorSend(new char[]{'\r'});
        getSerialPortConnection().setTerminatorSend(new char[]{SerialPortConnection.CHAR_LF});
//        getSerailPortConnection().setPortName(commport);
//        try {
//            getSerailPortConnection().openConnection();
//        } catch (IOException ex) {
//            Logger.getLogger(ShutterControllerUniblitz_1.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }

    private boolean open;

    @Override
    public void setOpen(boolean open) {
        if (open) {
            open();
        } else {
            close();
        }
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
        sendCommand("@");
    //System.out.println("> " + waitForResponse(333));
    }

    public void close() {
        open = false;
        //PSj.actionsPanel.panelCamera.setShutterButton(isOpen);
        sendCommand("A");
    //System.out.println("> " + waitForResponse(333));
    }

    public boolean toggle() {
        if (open) {
            close();
        } else {
            open();
        }
        return open;
    }

}
