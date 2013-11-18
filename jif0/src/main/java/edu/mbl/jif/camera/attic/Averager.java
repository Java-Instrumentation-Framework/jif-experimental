package edu.mbl.jif.camera.attic;

import edu.mbl.jif.camera.*;
import java.awt.Dimension;
import java.awt.Rectangle;

public class Averager {
  static int numberFrames = 0;
  static int frame = 0;
  static float[] data;
  static long sum = 0;
  static int imageSize = 0;
  static int max = 0;
  static int min = 0;
  static int maxInROI = 0;
  static int minInROI = 9999;

  //
  static long startTime = 0;
  static long stopTime = 0;
  public static boolean done = true;

  public static void initialize(int _numberFrames) {
    numberFrames = _numberFrames;
    sum = 0;
    imageSize = (int) (Camera.width * Camera.height);
    data = new float[numberFrames];
    frame = 0;
    done = false;
    startTime = System.currentTimeMillis();
  }

  // get the average of the entire image
  public static void calcAndSave() {
    if (frame < numberFrames) {
      if (QCamJNI.wideDepth) { // 16-bit
        for (int i = 0; i < imageSize; i++) {
          sum = sum + (long) QCamJNI.pixels16[i];
        }
      }
      else {
        for (int i = 0; i < imageSize; i++) {
          sum = sum + (long) QCamJNI.pixels8[i];
        }
      }
      data[frame] = sum / imageSize;
      sum = 0;
      frame++;
    }
    else {
      stopTime = System.currentTimeMillis();
      done = true;
      System.out.println("Averager done.");
    }
  }

  public static int getMaxPixel() {
    return max;
  }

  public static int maxPixel(byte[] array) {
    max = 0;
    for (int i = 0; i < array.length; i++) {
      if ( (array[i] & 0xff) > max) {
        max = array[i] & 0xff;
      }
    }
    return max;
  }

  public static int maxPixel(short[] array) {
    max = 0;
    for (int i = 0; i < array.length; i++) {
      if ( (array[i] & 0xff) > max) {
        max = array[i] & 0xff;
      }
    }
    return max;
  }

  public static void listData() {
    System.out.println("milliseconds/frame: "
                       + ( (stopTime - startTime) / numberFrames));
    for (int i = 0; i < numberFrames; i++) {
      System.out.println(data[i]);
    }
  }

  // Average the Current Camera Display ROI
  public static synchronized float averageROI() {
    float average = 0;
    Rectangle roi;
    if (Camera.display == null) {
      return -1.0f;
    }
    if ( (Camera.selectedROI.width == 0) || (Camera.selectedROI.height == 0)) {
      roi = new Rectangle(0, 0, (int) Camera.width,
                          (int) Camera.height);
    }
    else {
      roi = Camera.selectedROI;
    }
    Dimension frame =
        new Dimension( (int) Camera.width,
                      (int) Camera.height);
    if (Camera.depth == 8) {
      average = averageROIin(QCamJNI.pixels8, roi, frame);
    }
    else if (Camera.depth == 12) {
      average = averageROIin(QCamJNI.pixels16, roi, frame);
    }
    else {
      average = -1;

      //System.out.println("Average in ROI: " + average);
    }
    return average;
  }

  // This ONLY WORKS with 8-Bit, BYTE arrays
  public static synchronized float averageROIin(byte[] array, Rectangle roi,
                                                Dimension frame) {
    int x = roi.x;
    int y = roi.y;
    int w = roi.width;
    int h = roi.height;
    int xFinal = x + w;
    int yFinal = y + h;
    int frameW = frame.width;
    int frameH = frame.height;
//      System.out.println("ROI:" + roi.x + "," + roi.y  + "," + roi.width + "," +
//roi.height + "  Frame:" + frameW + "," + frameH);

    int numPixels = w * h;
    float average = 0;
    int sum = 0;
    maxInROI = 0;
    for (int nY = y; nY < yFinal; nY++) {
      for (int nX = x; nX < xFinal; nX++) {
        sum = sum + (array[ (frameW * nY) + nX] & 0xff);
        if ( (array[ (frameW * nY) + nX] & 0xff) > maxInROI) {
          maxInROI = (int) (array[ (frameW * nY) + nX] & 0xff);
        }
      }
    }
    try {
      average = (float) sum / (float) numPixels;
    }
    catch (Exception ex) {}
    //System.out.println("averageROIin = " + average);
    // maxPixel(array);
    return average;
  }

  public static synchronized float averageROIin(short[] array, Rectangle roi,
                                                Dimension frame) {
    int x = roi.x;
    int y = roi.y;
    int w = roi.width;
    int h = roi.height;
    int xFinal = x + w;
    int yFinal = y + h;
    int frameW = frame.width;
    int frameH = frame.height;
//      System.out.println("ROI:" + roi.x + "," + roi.y  + "," + roi.width + "," +
//roi.height + "  Frame:" + frameW + "," + frameH);
    int numPixels = w * h;
    float average = 0;
    int sum = 0;
    maxInROI = 0;
    for (int nY = y; nY < yFinal; nY++) {
      for (int nX = x; nX < xFinal; nX++) {
        sum = sum + (array[ (frameW * nY) + nX] & 0xffff);
        if ( (array[ (frameW * nY) + nX] & 0xffff) > maxInROI) {
          maxInROI = (int) (array[ (frameW * nY) + nX] & 0xffff);
        }
      }
    }
    try {
      average = (float) sum / (float) numPixels;
    }
    catch (Exception ex) {}
    //System.out.println("averageROIin = " + average);
    //maxPixel(array);
    return average;
  }

  public static int getMaxInROI() {
    return maxInROI;
  }

  public static synchronized float averageROIin(int[] array, Rectangle roi,
                                                Dimension frame) {
    int x = roi.x;
    int y = roi.y;
    int w = roi.width;
    int h = roi.height;
    int xFinal = x + w;
    int yFinal = y + h;
    int frameW = frame.width;
    int frameH = frame.height;
    int numPixels = w * h;
    float average = 0;
    int sum = 0;
    for (int nY = y; nY < yFinal; nY++) {
      for (int nX = x; nX < xFinal; nX++) {
        // for B&W 8-bit Only
        sum = sum + (array[ (frameW * nY) + nX] & 0xff);
      }
    }
    try {
      average = (float) sum / (float) numPixels;
    }
    catch (Exception ex) {}

    //System.out.println("averageROIin = " + average);
    return average;
  }
  /*
     // Average the Current Camera Display ROI
   public static synchronized float averageROI() {
     int x = 0;
     int y = 0;
     int w = 0;
     int h = 0;
     int frameW = 0;
     int frameH = 0;
     int xFinal = 0;
     int yFinal = 0;
     float average = 0;
     int sum = 0;
     if (CameraInterface.display == null) {
   return -1;
     }
     x = (int) CameraInterface.roiX;
     y = (int) CameraInterface.roiY;
     xFinal = x + (int) CameraInterface.roiW;
     yFinal = y + (int) CameraInterface.roiH;
     //    x = CameraInterface.display.vPanel.box.x;
     //    xFinal = x + CameraInterface.display.vPanel.box.width;
     //    y = CameraInterface.display.vPanel.box.y;
     //    yFinal = y + CameraInterface.display.vPanel.box.height;
     if ((x == xFinal) | (y == yFinal)) {
   //System.out.println("No ROI selected.");
   return -1.0f;
     }
     frameW = (int) CameraInterface.width;
     frameH = (int) CameraInterface.height;
     int numPixels = (int) (CameraInterface.roiW * CameraInterface.roiH);
     for (int nY = y; nY < yFinal; nY++) {
   for (int nX = x; nX < xFinal; nX++) {
     if (QCamJNI.wideDepth) {
       sum = sum + QCamJNI.pixels16[(frameW * nY) + nX];
     } else {
       sum = sum + (QCamJNI.pixels8[(frameW * nY) + nX] & 0xff);
     }
   }
     }
     try {
   average = (float) sum / (float) numPixels;
     } catch (Exception ex) {
     }
     System.out.println("Average in ROI: " + average);
     return average;
   }
   */
}
