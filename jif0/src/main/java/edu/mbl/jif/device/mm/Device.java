package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface Device {

// FILE:          MMDeviceConstants.h
//////////////////////////////////////////////////////////////////////////////
// Global constants
//
// common device error codes
// TODO: revise values - the range might clash with the native driver codes
    public static final int DEVICE_OK = 0;
    public static final int DEVICE_ERR = 1; // generic, undefined error
    public static final int DEVICE_INVALID_PROPERTY = 2;
    public static final int DEVICE_INVALID_PROPERTY_VALUE = 3;
    public static final int DEVICE_DUPLICATE_PROPERTY = 4;
    public static final int DEVICE_INVALID_PROPERTY_TYPE = 5;
    public static final int DEVICE_NATIVE_MODULE_FAILED = 6; // unable to load or initialize native module
    public static final int DEVICE_UNSUPPORTED_DATA_FORMAT = 7; // we don't know how to handle camera data
    public static final int DEVICE_INTERNAL_INCONSISTENCY = 8; // internal data structures are corrupted
    public static final int DEVICE_NOT_SUPPORTED = 9;
    public static final int DEVICE_UNKNOWN_LABEL = 10;
    public static final int DEVICE_UNSUPPORTED_COMMAND = 11;
    public static final int DEVICE_UNKNOWN_POSITION = 12;
    public static final int DEVICE_NO_CALLBACK_REGISTERED = 13;
    public static final int DEVICE_SERIAL_COMMAND_FAILED = 14;
    public static final int DEVICE_SERIAL_BUFFER_OVERRUN = 15;
    public static final int DEVICE_SERIAL_INVALID_RESPONSE = 16;
    public static final int DEVICE_SERIAL_TIMEOUT = 17;
    public static final int DEVICE_SELF_REFERENCE = 18;
    public static final int DEVICE_NO_PROPERTY_DATA = 19;
    public static final int DEVICE_DUPLICATE_LABEL = 20;
    public static final int DEVICE_INVALID_INPUT_PARAM = 21;
    int MaxStrLength = 1024;    // system-wide property names
    public static final String g_Keyword_Name = "Name";
    public static final String g_Keyword_Description = "Description";
    public static final String g_Keyword_CameraName = "CameraName";
    public static final String g_Keyword_CameraID = "CameraID";
    public static final String g_Keyword_Binning = "Binning";
    public static final String g_Keyword_Exposure = "Exposure";
    public static final String g_Keyword_PixelType = "PixelType";
    public static final String g_Keyword_ReadoutTime = "ReadoutTime";
    public static final String g_Keyword_ReadoutMode = "ReadoutMode";
    public static final String g_Keyword_Gain = "Gain";
    public static final String g_Keyword_EMGain = "EMGain";
    public static final String g_Keyword_Offset = "Offset";
    public static final String g_Keyword_CCDTemperature = "CCDTemperature";
    public static final String g_Keyword_CCDTemperatureSetPoint = "CCDTemperatureSetPoint";
    public static final String g_Keyword_State = "State";
    public static final String g_Keyword_Label = "Label";
    public static final String g_Keyword_Position = "Position";
    public static final String g_Keyword_Type = "Type";
    public static final String g_Keyword_Delay = "Delay_ms";
    public static final String g_Keyword_BaudRate = "BaudRate";
    public static final String g_Keyword_DataBits = "DataBits";
    public static final String g_Keyword_StopBits = "StopBits";
    public static final String g_Keyword_Parity = "Parity";
    public static final String g_Keyword_Handshaking = "Handshaking";
    public static final String g_Keyword_Port = "Port";
    public static final String g_Keyword_Speed = "Speed";
    public static final String g_Keyword_CoreDevice = "Core";
    public static final String g_Keyword_CoreInitialize = "Initialize";
    public static final String g_Keyword_CoreCamera = "Camera";
    public static final String g_Keyword_CoreShutter = "Shutter";
    public static final String g_Keyword_CoreFocus = "Focus";
    public static final String g_Keyword_CoreAutoShutter = "AutoShutter";
    public static final String g_Keyword_Channel = "Channel";
    public static final String g_Keyword_Version = "Version";
    // configuration file format constants
    public static final String g_FieldDelimiters = ",";
    public static final String g_CFGCommand_Device = "Device";
    public static final String g_CFGCommand_Label = "Label";
    public static final String g_CFGCommand_Property = "Property";
    public static final String g_CFGCommand_Configuration = "Config";
    public static final String g_CFGCommand_ConfigGroup = "ConfigGroup";
    public static final String g_CFGCommand_Equipment = "Equipment";
    public static final String g_CFGCommand_Delay = "Delay";
    public static final String g_CFGCommand_ImageSynchro = "ImageSynchro";
    // configuration groups
    public static final String g_CFGGroup_System = "System";
    public static final String g_CFGGroup_System_Startup = "Startup";
    public static final String g_CFGGroup_System_Shutdown = "Shutdown";

    public DeviceType getType();

    public void getName(String name);

    public int getNumberOfProperties();

    public int getProperty(String name, String value);

    public int setProperty(String name, String value);

    public boolean getPropertyName(int idx, String name);

    public int getPropertyReadOnly(String name, boolean readOnly);

    public int getPropertyInitStatus(String name, boolean preInit);

    public int getNumberOfPropertyValues(String propertyName);

    public boolean getPropertyValueAt(String propertyName, int index, String value);

    public boolean getErrorText(int errorCode, String errMessage);

    public boolean isBusy();

    public double getDelayMs();

    public void setDelayMs(double delay);

    // library handle management (for use only in the client code)
    // public  HDEVMODULE getModuleHandle();
    // public  void setModuleHandle(HDEVMODULE hLibraryHandle);
    public void setLabel(String label);

    public void getLabel(String name);

    public void setModuleName(String label);

    public void getModuleName(String name);

    public int initialize();

    public int shutdown();

    public void setCallback(Core callback);

}

enum PropertyType {
    Undef,
    String,
    Float,
    Integer
}

enum ActionType {
    NoAction,
    BeforeGet,
    AfterSet
}

// Notification constants
enum DeviceNotification {
    Attention,
    Done,
    StatusChanged
};