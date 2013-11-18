package edu.mbl.jif.camera;

public class LuCamJNI {
   
   // Native calls into JNI to access the LuCamInterface_.dll
   //
   public static native int getNumCameras();
   public static native long openCamera(long n); // returns camera handle
   
   // given camHandle, 
   public static native int closeCamera(long camHandle);
   public static native float getType(long camHandle);
   
   public static native int getProperties(long camHandle, Object camInstance);
   public static native int setProperties(long camHandle, Object camInstance);    
   
   public static native int setExposureGain(long camHandle,
         long depth, float exposure, float gain, float target);
   
   public static native int setSnapshotSettings(long camHandle,
         long depth, float exposure, float gain);
   
   public static native byte[] takeSnapshot8(long camHandle);
   public static native short[] takeSnapshot16(long camHandle);
   public static native byte[] takeSnapshot16b(long camHandle);   
   
   public static native int enableFastSnapshots(long camHandle);
   public static native byte[] takeFastFrame8(long camHandle);
   public static native int takeFastFrame8j(long camHandle, byte[] imgArray);
   public static native byte[] takeFastFrame16(long camHandle);
   public static native int takeFastFrame16j(long camHandle, byte[] imgArray);
   public static native int disableFastSnapshots(long camHandle);
   
   // Synchronous Image Acq, assuming 2 Cameras (1 & 2).
   public static native int initializeSynchronous();
   public static native byte[] snapshotSynchronous();
   public static native int terminateSynchronous();
   
   // sets the image arrays used for streaming, real-time display
   // callbackObj is the object containing a method named callBack()
   // which, when in streaming (freerun trigger) mode,  is executed as
   // a 'callback' when the next frame is ready.

   // Camera A
   public static native void setDisplayArray8(byte[] byteArray);
   public static native void disposeArrays8();
   public static native void callBackSetup(Object callbackObj);
   public static native int enableStreaming(long camHandle);
   public static native int startStreaming(long camHandle);
   public static native int stopStreaming(long camHandle);
   public static native int disableStreaming(long camHandle);
   // Camera A
   public static native void setDisplayArray8A(byte[] byteArray);
   public static native void disposeArrays8A();
   public static native void callBackSetupA(Object callbackObj);
   public static native int enableStreamingA(long camHandle);
   public static native int startStreamingA(long camHandle);
   public static native int stopStreamingA(long camHandle);
   public static native int disableStreamingA(long camHandle);
   // Camera B
   public static native void setDisplayArray8B(byte[] byteArray);
   public static native void disposeArrays8B();
   public static native void callBackSetupB(Object callbackObj);
   public static native int enableStreamingB(long camHandle);
   public static native int startStreamingB(long camHandle);
   public static native int stopStreamingB(long camHandle);
   public static native int disableStreamingB(long camHandle);
   
   public static native void setDisplayArray16(short[] shortArray);
   public static native void disposeArrays16();
   
   
   //********************************************************************************
   
    /*    
       public static native boolean openCameraStreaming ();
       //public static native int openCameraSynch(long exposure);
       // synchronous image grabbing function (we are not using it)
       //public static synchronized native int snapImage(byte[] frameData); // Synchronous
       //public static native void unLoadQCamDriver();
     
       public static native int getInfo ();
     
       public static native int getParameters ();
     
       // setFormat - LufmtMono8 = 2 | LufmtMono16 = 3
       public static native int setFormat (long fmt);
     
       public static native int setBinning (long bin);
     
       // ReadoutSpeed: 20M = 0, 10M	= 1, 5M	= 2, 2M5 = 3, _last	= 4
       public static native int setReadoutSpeed (long speed);
     
       public static native int setROI (long x, long y, long w, long h);
       public static native int setROIFull ();
     
       // Exposure is in microseconds
       public static native int setExposure (long exposure, long gain, long offset);
       public static native int getExposureMinMax ();
       public static native int queueExposureSet (long exposure);
     
       public static native int setCooler (boolean onOff);
     
       public static synchronized native int displayOn ();
     
       public static synchronized native int setToAcquire ();
     
       // tTriggerType: freerun=1, software=2, external=4
       public static native void setTriggerType (int tTriggerType);
       public static native int getTriggerType ();
       public static synchronized native int doSoftTrigger ();
     */
   // Queues the frame for the next capture
   // NOT USED // public static synchronized native int queueAcqFrame(AcqFrame f);
   
   
   //////////////////////////////////////////////////////////////////////////
   // Load the C-code library (.DLL)
    public static boolean loadDLL() {
        try {
            System.loadLibrary("LuCamJNI");  // assuming .dll is in java.library.path 
            //System.load(".\\LuCamJNI.dll");  // explicit path
            return true;
        } catch (Error e) {
            System.out.println(
                    "Could not load LuCamJNI.dll\njava.library.path= "
                    + System.getProperty("java.library.path"));
            return false;
        }
    }
     
   
//----------------------------------------------------------------
// LuCamJNI
}
