/*
 * CameraListEntry.java
 *
 * Created on April 25, 2006, 9:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.camera;

/**
 *
 * @author GBH
 */
public class CameraListEntry {
   
   private long cameraId;
   private long cameraType;
   private long uniqueId;
   private boolean open; // 1 if already open
   
   
   public CameraListEntry() {
   }
   
   
   public long getCameraId() {
      return cameraId;
   }
   
   
   public long getCameraType() {
      return cameraType;
   }
   
   
   public long getUniqueId() {
      return uniqueId;
   }
   
   
   public boolean isOpen() {
      return open;
   }
   
   
   public void setCameraId(long cameraId) {
      this.cameraId = cameraId;
   }
   
   
   public void setCameraType(long cameraType) {
      this.cameraType = cameraType;
   }
   
   
   public void setOpen(boolean open) {
      this.open = open;
   }
   
   
   public void setUniqueId(long uniqueId) {
      this.uniqueId = uniqueId;
   }
   
}
