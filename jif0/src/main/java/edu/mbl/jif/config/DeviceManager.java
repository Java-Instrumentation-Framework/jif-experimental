package edu.mbl.jif.config;

import java.util.HashMap;

/**
 *
 * @author GBH
 */
// Usage:
// DeviceManager.INSTANCE.addDevice("deviceName", device);
// ...
// DeviceType device = (DeviceType) DeviceManager.INSTANCE.getDevice("deviceName");

public enum DeviceManager {

    INSTANCE;
    HashMap map = new HashMap();

    public void addDevice(String deviceName, Object device) {
        map.put(deviceName, device);
    }

    public Object getDevice(String deviceName) {
        return map.get(deviceName);
    }

    public void removeDevice(String deviceName) {
        map.remove(deviceName);
    }

    public void showDevices() {
    }

}
