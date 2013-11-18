/*
 * Acq.java
 */
package edu.mbl.jif.acq;


/**
 *
 * @author GBH
 */
public interface ImageAcquisitioner {
   // public Acquisitioner(Camera cam);

   // --- setup ---
   public void setDepth(int depth);

   public void setMirrorImage(boolean t);

   public void setMultiFrame(int n, boolean divide); // +

   // --- acquire ---
   public void start();

   public void start(boolean flushFirst);

   public long acquireImage(Object imageArray); // returns acq time

   public void finish();
}
