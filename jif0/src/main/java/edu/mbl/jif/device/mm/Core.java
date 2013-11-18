/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface Core {

    public int logMessage(Device caller, String msg, boolean debugOnly);


    public Device getDevice(Device caller, String label);


    public int setSerialCommand(Device caller, String portName, String command, String term);


    public int getSerialAnswer(Device caller, String portName, long ansLength, String answer, String term);


    public int writeToSerial(Device caller, String port, String buf, long length);


    public int readFromSerial(Device caller, String port, String buf, long length, long read);


    public int purgeSerial(Device caller, String portName);


    public int onStatusChanged(Device caller);


    public int onFinished(Device caller);


    public long getClockTicksUs(Device caller);
    
 /* And... 
  * 
      virtual MM::PortType GetSerialPortType(const char* portName) const = 0;      
      virtual MM::MMTime GetCurrentMMTime() = 0;

      // continuous acquisition
      virtual int OpenFrame(const Device* caller) = 0;
      virtual int CloseFrame(const Device* caller) = 0;
      virtual int AcqFinished(const Device* caller, int statusCode) = 0;
      virtual int PrepareForAcq(const Device* caller) = 0;
      virtual int InsertImage(const Device* caller, const unsigned char* buf, unsigned width, unsigned height, unsigned byteDepth, ImageMetadata* md = 0) = 0;
      virtual int InsertMultiChannel(const Device* caller, const unsigned char* buf, unsigned numChannels, unsigned width, unsigned height, unsigned byteDepth, ImageMetadata* md = 0) = 0;
      virtual void SetAcqStatus(const Device* caller, int statusCode) = 0;

      // device management
      virtual MM::ImageProcessor* GetImageProcessor(const MM::Device* caller) = 0;
      virtual MM::State* GetStateDevice(const MM::Device* caller, const char* deviceName) = 0;
   
  */

};
