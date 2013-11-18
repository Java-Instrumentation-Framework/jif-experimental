package edu.mbl.jif.device;

/**
 *
 * @author GBH
 */
public interface Device {
        
    //public String getDeviceClass();
    
    public String getDeviceName();
    
    public String getDeviceType();
    
    public boolean isConnected();
    
    public boolean isInitialized();
    
    public boolean isBusy();
    
    public void reset();
    
    public void terminate();
    
}
