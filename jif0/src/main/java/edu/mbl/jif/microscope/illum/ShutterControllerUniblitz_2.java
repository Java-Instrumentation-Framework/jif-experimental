package edu.mbl.jif.microscope.illum;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.device.SerialDevice;

/**
 * Controls Uniblitz shutter #2 on mulltichannel controller
 *
 * @author GBH
 */
public class ShutterControllerUniblitz_2 extends SerialDevice implements Shutter {

    InstrumentController instCtrl;

    public ShutterControllerUniblitz_2(InstrumentController instCtrl, String commport) {
        super(instCtrl, "ShutterUniblitz2", commport);
        getSerialPortConnection().setTerminatorSend(new char[]{'\r'});
    }

    private boolean open;

    @Override
    public void setOpen(boolean open) {
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
        sendCommand("B");
    }

    public void close() {
        open = false;
        sendCommand("C");
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
