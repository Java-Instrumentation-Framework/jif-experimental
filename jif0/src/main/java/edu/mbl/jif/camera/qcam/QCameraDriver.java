/*
 * QCamera.java
 * Created on February 21, 2006, 10:42 AM
 */

package edu.mbl.jif.camera.qcam;

import edu.mbl.jif.camera.*;
import java.util.ArrayList;

/**
 * @author GBH
 */
public class QCameraDriver extends Driver {
   
   
   /** Creates a new instance of QCamera */
   public QCameraDriver() {
      load();
   }
   
   public  ArrayList getCameraList() {
      int i = 0;
      ArrayList cams = new ArrayList();
      //camera[i] = new QCamera(QCameraList.camEntry[i].getCameraID());
      return cams;
   }

   // Drivers - Load / Unload QCam Driver
   
   public  boolean load() {
      int err = -1;
      if (!driverLoaded) {
         try {
            err = QCamJNI.loadQCamDriver();
            
         } catch (Exception ex) {
            System.err.println("Exception in LoadCameraDriver\n" +
                  ex.getMessage());
         }
         if (err == 0) {
            driverLoaded = true;
            return true;
         }  else {
            System.out.println("Camera Error: Unable to load driver: " + String.valueOf(err));
            return false;
         }
      }
      return true;
   }
   
   
   public  void unLoad() {
      //         if (mode != "closed") {
      //            QCamJNI.closeTheCamera();
      //            mode = "closed";
      //         }
      if (driverLoaded) {
         QCamJNI.unLoadQCamDriver();
      }
      driverLoaded = false;
      System.out.println("Camera driver unloaded.");
   }
   //-----------------------------------------------------------------
   
   // Load the .dll library for native calls
   
//   public static native int loadQCamDriver();
//   public static native void getCameraList(long[][] cameraListArray);
//   public static native void unLoadQCamDriver();
   
   
   
}