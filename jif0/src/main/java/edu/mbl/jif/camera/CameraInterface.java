package edu.mbl.jif.camera;

import edu.mbl.jif.device.Device;
import java.awt.Rectangle;
import java.util.ArrayList;


public interface CameraInterface extends Device {
   // Fields need in implementing class:
   //
   // int camNum = -1;
   // long jniHandle = -1;
   // StreamSource sourceStream = null;
   // boolean busy = false;

   /**
    * Camera() constructor normally does the open()
    * @return Opened camera successfully
    */
   public boolean open();

   public boolean isOpen();

   public boolean initialize();

   public String getCameraState();

   public int pushSettings();

   // for 2 cameras, A & B (ONLY for Lumenera
   //public void setChannel(int channel);
   //public int  getChannel();

   // --- Format ---
   public ArrayList getAvailFormats();

   public void setFormat(int fmt);

   public int getWidth();

   public int getHeight();

   public void setDepth(int _depth); // Acq only, always 8-bit Streaming

   public int getDepth();

   public void setBinning(int _binning);

   public int getBinning();

   // --- Exposure --- times are in milliseconds
   // Streaming and Acq setting are one-in-the-same in which case, use ExposureAcq 
   public boolean isSameSetAcqStream();

   public void setExposureStream(double _exposure);

   public double getExposureStream();

   public double getExposureStreamMax();

   public double getExposureStreamMin();

   public void setExposureAcq(double _exposure);

   public double getExposureAcq();

   public double getExposureAcqMax();

   public double getExposureAcqMin();

   // --- Gain --- is a factor, 1.0 to N
   public void setGainStream(double gain);

   public double getGainStream();

   public void setGainAcq(double gain);

   public double getGainAcq();

   // --- Offset ---
   public void setOffsetStream(double _offset);

   public double getOffsetStream();

   public void setOffsetAcq(double _offset);

   public double getOffsetAcq();

   // --- ROI ---
   public void setROI(Rectangle roi);

   public Rectangle getROI();

   public void clearROI();

   public boolean isRoiSet();

   public void setSpeed(double _speed);

   public double getSpeed();

   // --- Temperature / Cooler
   public void setCoolerActive(boolean onOff);

   public boolean isCoolerActive();

   public double getTemperature();

   // --- SnapShot ---
   public byte[] takeSnapshot8();

   public short[] takeSnapshot16();

   // --- FastFrame ---
   public void enableFastAcq(int depth);

   public byte[] acqFast8(); // return new array

   public int acqFast8(byte[] imgArray); // acq into existing array

   public short[] acqFast16();

   public int acqFast16(short[] imgArray);

   public void disableFastAcq();

//   
//   // --- Streaming ---
//   // Only 8-bit supported
//   public StreamSourceCamera getStreamSource();
//
//   public void startStream();
//
//   public void stopStream();
//
//   public boolean isStreaming();
//
//   public void closeStreamSource();
//   
   
   public  double getGainMultiplier();
   
   public  double getExposureMultiplier();
   
   public void close();

   // --- Info/Diag ---
   public String listAllParms();

}
