/*
 * ImageCaptureStack.java
 *
 * Created on December 7, 2006, 10:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.acq;

import java.awt.image.BufferedImage;

/**
 *
 * @author GBH
 */
public interface ImageCaptureStack {
    
    // implemented for ImageJ or jif
    
    // public ImageCaptureStack(String title) {}
    
    // public ImageCaptureStack(String title, int n) {} 
    
    
    public void addImageToStack(BufferedImage bImage);
    
    public void addImageToStack(byte[] imageArray);
    
    public void addImageToStack(short[] imageArray);
    
}
