package edu.mbl.jif.comm;

//import javax.comm.CommPortIdentifier;
import javax.comm.CommPortIdentifier;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SerialUtils {

    public SerialUtils() {
    }

    /** --------------------------------------------------------------------------
     * Get serial ports
     * @return String array of ports on workstation (may return null)
     */
    public static String[] getAvailablePorts() {
        Enumeration enumr = CommPortIdentifier.getPortIdentifiers();
        String[] retStr = null;
        ArrayList list = new ArrayList();
        while (enumr.hasMoreElements()) {
            CommPortIdentifier comId = (CommPortIdentifier) enumr.nextElement();
            if (comId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                list.add(comId.getName());
            //System.out.println(comId.getName());
            }
        }
        Object[] ports = list.toArray();
        if (ports != null) {
            retStr = new String[ports.length];
            for (int i = 0; i < ports.length; i++) {
                retStr[i] = (String) ports[i];
            }
        }
        return retStr;
    }

    public static String getStatusOfPorts() {
        Enumeration enumr = CommPortIdentifier.getPortIdentifiers();
        StringBuffer s = new StringBuffer();
        while (enumr.hasMoreElements()) {
            CommPortIdentifier comId = (CommPortIdentifier) enumr.nextElement();
            if (comId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                s.append(comId.getName() + ": ");
                if (comId.isCurrentlyOwned()) {
                    s.append("owner: " + comId.getCurrentOwner() + " ");
                } else {
                    s.append("no owner.");
                }
                s.append("\n");
            }
        }
        return s.toString();
    }

    /**
     *  Check that the port specified is in fact available;
     * @return The portName specified, or, if not available, return first port found (to use as default)
     */
    public static String checkPortAvailable(String portName) {
        String firstPort = null;
        Enumeration enumr = CommPortIdentifier.getPortIdentifiers();
        int i = 1;
        while (enumr.hasMoreElements()) {
            CommPortIdentifier comId = (CommPortIdentifier) enumr.nextElement();
            if (comId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (i == 1) {
                    firstPort = comId.getName();
                }
                i++;
                if (portName.equalsIgnoreCase(comId.getName())) {
                    return portName;
                }
            }
        }
        return firstPort;
    }

    /**
     *  Check that the port specified is in fact available;
     * @return true or false
     */
    public static boolean isPortAvailable(String portName) {
        Enumeration enumr = CommPortIdentifier.getPortIdentifiers();
        while (enumr.hasMoreElements()) {
            CommPortIdentifier comId = (CommPortIdentifier) enumr.nextElement();
            if (comId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portName.equalsIgnoreCase(comId.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean openPortSettingsDialog(SerialPortConnection port, String title,
                                                 String message) {
        port.closeConnection();
        JOptionPane optionPane = new JOptionPane();
        PanelSerialConfig configPanel = new PanelSerialConfig(port);
        Object msg[] = {message, configPanel};
        optionPane.setMessage(msg);
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(null, title);
        dialog.setVisible(true);
        Object value = optionPane.getValue();
        if (value == null || !(value instanceof Integer)) {
            System.out.println("Closed");
        } else {
            int i = ((Integer) value).intValue();
            if (i == JOptionPane.OK_OPTION) {
                try {
                    port.getParameters().save();
                //setCommPort(getPort().getParameters().getPortName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // re-open the port
                port.reOpenConnection();
            //System.out.println("OKAY - value is: " + optionPane.getInputValue());
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Ports: ");
        String[] ports = getAvailablePorts();
        for (int i = 0; i < ports.length; i++) {
            System.out.println(ports[i]);

        }
        System.out.println("checkPortAvailable(COM10): " + checkPortAvailable("COM10"));
        System.out.println("isPortAvailable(COM10): " + isPortAvailable("COM10"));
        System.out.println(getStatusOfPorts());
    // System.out.println("checkPortAvailable(COM3): "+ checkPortAvailable("COM3"));
    }

}
