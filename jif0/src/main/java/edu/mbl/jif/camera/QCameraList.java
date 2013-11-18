/*
 * CameraList.java
 *
 * Created on February 20, 2006, 3:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.camera;

import edu.mbl.jif.camera.qcam.QCameraDriver;


public class QCameraList {
    
    static long[][] i2d = new long[QCameraDriver.MAX_CAMERAS][4];
    
    // an array of 10 arrays of long[4]
    public static CameraListEntry[] camEntry =
            new CameraListEntry[QCameraDriver.MAX_CAMERAS];
    
    public QCameraList() {}
    

    
    public static CameraListEntry[] getCameraList() {
        System.out.println("\nlength of array is " + i2d.length);
        System.out.println("length of array[0] is " + i2d[0].length);
        // set to -1: uninitialized
        for (int i=0; i < QCameraDriver.MAX_CAMERAS; i++)
            for (int j=0; j<4; j++)
                i2d[i][j] = -1;
        System.out.println("original values:");
        for (int i=0; i<QCameraDriver.MAX_CAMERAS; i++)
            for (int j=0; j < 4; j++)
                System.out.println("i2d["+i+"]["+j+"] = " + i2d[i][j]);
        
        // nativeProcess2DArray(i2d, n);
        
        System.out.println("modified values:");
        for (int i=0; i < QCameraDriver.MAX_CAMERAS; i++) {
            if (i2d[i][0] >= 0 ) {
                for (int j=0; j < 4; j++) {
                    System.out.println("i2d["+i+"]["+j+"] = " + i2d[i][j]);
                    camEntry[i].setCameraId(i2d[i][0]);
                    camEntry[i].setCameraType(i2d[i][1]);
                    camEntry[i].setUniqueId(i2d[i][2]);
                    camEntry[i].setOpen((i2d[i][3] == 1)? true: false); // 1 if already open
                }
            }}
        return camEntry;
    }

    
    
    public static void main(String[] args) {
        CameraListEntry[] cameras = QCameraList.getCameraList();
    }
}