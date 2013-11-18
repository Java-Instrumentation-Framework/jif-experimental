/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.ps;

import ij.ImagePlus;
import ij.ImageStack;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author GBH
 */
public interface Birefringence {

    BufferedImage magnitudeImage(ImageStack ip);

    BufferedImage orientationImage(ImageStack ip);


    BufferedImage magnitudeImage(byte[] ip);


    BufferedImage orientationImage(byte[] ip);


    BufferedImage magnitudeImage(short[] ip);


    BufferedImage orientationImage(short[] ip);

    
    // ImageJ

    ImagePlus magnitudeImagePlus(ImageStack ip);
    ImagePlus orientationImagePlus(ImageStack ip);


    //... ImagePlus magnitudeImagePlus(ImageStack ip, String title);

    // adds results to ImageStack
    void birefringence(ImagePlus ip);


    void birefringence(ImagePlus ip, ImagePlus ipBkgd);


    void birefringence(ImagePlus ip, 
            boolean magnitude, boolean orientation);


    void birefringence(ImagePlus ip, ImagePlus ipBkgd, 
            boolean magnitude,  boolean orientation);
    
    void setParameters();
    
    void setBackground(ImageStack ipBkgd);
    void setBackground(ImageStack ipBkgd, Rectangle roiRatioing);
    
    
 /*
   public float swingFraction = 0.03f; // =swing/wavelength ~ 0.03
   public float wavelength = 546f; // wavelength in nm ~ 546 nm
   public float zeroIntensity = 0.0f;
   public int retCeiling = 10; // maximum image retardance in nm
   public int algorithm =5;
   public int azimuthRef = 0; // azimuth reference in (whole) degrees
   public float dynamicRange = 1.0f; // dynamic range of ...

   // Background correction - filename of stack used for correction in magImage
   public String backGroundStack = null;
   public boolean doBkgdCorrection = false;
    
   float[] backgroundA;
   float[] backgroundB;

   // Ratioing Correction
   public Rectangle roiRatioing = new Rectangle(0, 0, 0, 0);
   public double[] ratioingAvg = null; // for each slice
   public boolean doRatioing = false;
*/

}
