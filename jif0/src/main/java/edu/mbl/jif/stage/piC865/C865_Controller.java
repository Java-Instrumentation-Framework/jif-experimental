package edu.mbl.jif.stage.piC865;

import java.util.Enumeration;
import java.util.Properties;

public class C865_Controller implements edu.mbl.jif.stage.DigitalOutput {

    private int ID;
    int commPort = 6;
    int baudRate = 57600; //115000;  // 115200;//416667; //57600; //115200;

    public C865_Controller(int commPort) {
        this.commPort = commPort;
    }
    //private String stages = " M-663.465 ";  //????
    byte[] stages = new byte[1023];
    byte[] strBuff = new byte[50];
    byte[] axes = "1".getBytes();

    public boolean open() {
        if(!C865.initialize()){
            return false;
        }
        // open, set stage and initialize this axis
        System.out.println("Initializing PI C865 Controller >>>");
        // connect to the C-865
        boolean isConnected = C865.IsConnected(0);
        System.out.println("C865> Is connected >>>" + isConnected);

        ID = C865.ConnectRS232(commPort, baudRate);
        if (ID < 0) {
            System.err.println("C865> ConnectRS232 failed on commPort: " + commPort);
            return false;
        }
        System.out.println("C865> Connected, ID: " + ID);


//        if (!C865.qIDN(ID, strBuff, 50)) {
//            checkError("qIDN");
//            return false;  // read name from stage
//        }
//        System.out.printf("C865> Connected to %son COM%d\n", strBuff, commPort);

//        if (!C865.SetErrorCheck(ID, true)) {
//            checkError("setErrorCheck");
//            return false;
//        }

        if (!C865.qCST(ID, axes, stages, 1023)) {
            this.checkError("qCST");
            return false;
        }
        System.out.println("C865> qCST() initially returned " + stages);
        // the output should be "1=NOSTAGE\n"


        if (!C865.qSAI(ID, axes, 9)) {
            this.checkError("qSAI");
            return false;
        }
        System.out.println("C865> qSAI() initially returned [" + axes + "]");
        // the output should be "" - no configured axes

        //----------------------------------
        // we want to connect a M-663.465 to channel 1

        stages = "M-663.465\u0000".getBytes();
        String[] s = {"M-663.465"};

        if (!C865.CST(ID, axes, stages)) {  // 124 ?????
            checkError("CST");
            return false;
        }

        if (!C865.qSAI(ID, axes, 9)) {
            return false;
        }
        System.out.println("C865> qSAI() after CST returned [" + axes + "]");
        //System.out.println("axes: " + HexString.toHexString(axes));
        // the output should be "1" - the new configured axes


        if (!C865.qCST(ID, axes, stages, 1023)) {
            return false;
        }
        System.out.println("C865> qCST() after CST returned " + stages);
        // the output should be "1= M-663.465 \n"

        // call INI for all axes
        // "" as axes string will address all configured axes
        if (!C865.INI(ID, axes)) {
            this.checkError("INI");
            return false;
        }
        System.out.println("C865> INI done.");
        return true;
    }

    public void close() {
        System.out.println("C865> closed.");
        C865.CloseConnection(ID);
    }

    public boolean reference() {
        System.out.print("Referencing...");
        boolean[] isReferencing = {true};
        if (!C865.REF(ID, axes)) {
            checkError("Ref");
            return false;
        } else {

//            while (isReferencing[0]) {
//                if (!C865.IsReferencing(ID, axes, isReferencing)) {
//                    checkError("IsReferencing");
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {                }
//            }

            return true;
        }
    }

    public void moveRelative(double distance) {
        double[] dist = {distance};
    //c865.C865_MVR(ID, axes, dist);
    }

    public boolean move(double pos) {
     //   System.out.println("C865> Moving to: " + pos);
        double[] posA = {pos};
        if (!C865.MOV(ID, axes, posA)) {
            checkError("Mov");
            return false;
        } else {
            return true;
        }
    }

    public boolean isMoving() {
        return C865.IsMoving(ID, null, null);
    }

    public double getPosition() {
        double[] pos = new double[1];
        C865.qPOS(ID, axes, pos);
        return pos[0];
    }

    public boolean setVelocity(double vel) {
       // System.out.println("C865> Velocity set to: " + vel);
        double[] velA = {vel};
        if (!C865.VEL(ID, axes, velA)) {
            checkError("VEL");
            return false;
        } else {
            return true;
        }
    }

    public void stop() {
        C865.STP(ID);
    }

    public void clear() {
        //c865.C865_CLR(ID, axes);
    }

    public double getVelocity() {
        // c865.C865_qVEL(ID, ,);
        return 0;
    }

    public boolean hasLimitSwitch() {
        // c865.C865_qLIM(ID, , );
        return false;
    }
//    public void clear() {
//                c865.;
//    }
    public void checkError(String where) {
        int err = C865.GetError(ID);
        // @todo Pass down changable string
        byte[] errMsg = new byte[1023];
        C865.TranslateError(err, errMsg, 1023);
        System.err.printf("C865> ERROR at %s,  Error %d => %s\n", where, err, errMsg);
    }

    public static String listProperties() {
        Properties props = //new Properties();
                System.getProperties();
        StringBuffer sBuff = new StringBuffer();
        props.list(System.out);
        Enumeration enumr = props.propertyNames();
        while (enumr.hasMoreElements()) {
            String key = (String) enumr.nextElement();
            String value = props.getProperty(key);
            sBuff.append(key + " = " + value + "\n");
        }
        return sBuff.toString();
    }

    public static void main(String[] args) {

        //System.out.println(listProperties());
        C865_Controller ctrlX = new C865_Controller(7);
        //C865_Controller ctrlY = new C865_Controller(6);
        if (!ctrlX.open()) {
            System.err.println("Failed to open ctrlX");
            return;
        }
        ctrlX.reference();

        ctrlX.move(5.0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        ctrlX.move(10.0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        ctrlX.move(15.0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
    //        if (!ctrlY.open()) {
//            System.err.println("Failed to open ctrlY");
//        //return;
//        }
    //ctrlX.moveRelative(100);
    // ctrlX.close();
    //ctrlY.close();
    }

    public void digitalOutput(boolean on) {
        if (on) {
            C865.DIOON(ID);
        } else {
            C865.DIOOFF(ID);
        }
//        boolean[] state = new boolean[1];
//        state[0] = on;
//        C865.GetOutputChannelNames(ID, strBuff, 0);
//        if (!C865.DIO(ID, null, state)) {
//            System.err.println("Failed in DIO");
//            return;
//        }
    }

    @Override
    public void dioRepeat(int n, int period) {
        boolean DIOREPEAT = C865.DIOREPEAT(ID, n, period);
    }

    @Override
    public void setOutput(int channel, boolean hilow) {
        digitalOutput(hilow);
    }


    /*
    ========================================================
    char[] stages = new char[1024];
    char[] axes = new char[10];
    int ID;
    
    // connect to the C-865
    ID = C865_ConnectRS232(1, 115200);
    if (ID<0)
    return FALSE;
    // nothing is configured
    if (!C865_qCST(ID, "1", stages, 1023))
    return FALSE;
    // the output should be "1=NOSTAGE\n"
    printf("qCST() returned \"%s\"", stages);
    if (!C865_qSAI(ID, axes, 9))
    return FALSE;
    // the output should be "" - no configured axes
    printf("qSAI() returned \"%s\"", axes);
    // we want to connect a M-663.465 to channel 1
    sprintf(stages, " M-663.465 ");
    if (!C865_CST(ID, "124", stages))
    return FALSE;
    if (!C865_qSAI(ID, axes, 9))
    return FALSE;
    // the output should be "1" - the new configured axes
    printf("qSAI() returned \"%s\"", axes);
    if (!C865_qCST(ID, "1", stages, 1023))
    return FALSE;
    // the output should be "1= M-663.465 \n"
    printf("qCST() returned \"%s\"", stages);
    // call INI for all axes
    // "" as axes string will address all configured axes
    if (!C865_INI(ID, ""))
    return FALSE;
    ========================================================
     */
}
