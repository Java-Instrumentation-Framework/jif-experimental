package edu.mbl.jif.stage;

/**
 * Microscope / Stage-specific control
 * @author GBH
 */
public interface ZStageController {

    void sendCmd(String cmd);

    void test();

    boolean checkResponsive();

    String queryStatus();

    void clearErrors();

    void initialize();

    // Limits
    boolean areLimitsSet();

    int getHighLimit();

    int getLowLimit();

    void setHighLimit(int z);

    void setLowLimit(int z);

    // Increment
    void setIncrement(int _increment);

    void setNmPerIncrement(int nmPerIncrement);

    String nmToHexIncrements(int nanometers);

    // Zero
    int getZeroIndexPosition();

    void setZeroPosition();

    // Position    
    int getCurrentPosition(); // throws Exception;

    // Move
    void moveDown(int n);

    void moveRelative(int nmDiff);

    int moveRelativeAck(int nmDiff);

    void moveTo(int pos);

    int moveToAck(int nmPos);

    void moveUp(int n);

    void stop();


}
