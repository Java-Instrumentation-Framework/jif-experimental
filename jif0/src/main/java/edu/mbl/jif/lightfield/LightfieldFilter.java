/*
 * LightfieldFilter.java
 *
 * Created on November 12, 2006, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.lightfield;

import java.awt.image.ImageFilter;

/**
 *
 * @author GBH
 */
public class LightfieldFilter extends ImageFilter {
    
    /** Creates a new instance of LightfieldFilter */
    public LightfieldFilter() {
    }
    
}

//    private static ColorModel defaultCM = ColorModel.getRGBdefault();
//    private int savedWidth;
//    private int savedHeight;
//    private int[] savedPixels;
//
//    public void setDimensions(int width, int height) {
//        savedWidth = width;
//        savedHeight = height;
//        savedPixels = new int[width * height];
//        consumer.setDimensions(width, height);
//    }
//
//    public void setColorModel(ColorModel model) {
//        // Change color model to model you are generating
//        consumer.setColorModel(defaultCM);
//    }
//
//    public void setHints(int hintflags) {
//        // Set new hints, but preserve SINGLEFRAME setting
//        consumer.setHints(TOPDOWNLEFTRIGHT | COMPLETESCANLINES | SINGLEPASS |
//            (hintflags & SINGLEFRAME));
//    }
//
//    private void setThePixels(int x, int y, int width, int height,
//        ColorModel cm, Object pixels, int offset, int scansize) {
//        int sourceOffset = offset;
//        int destinationOffset = (y * savedWidth) + x;
//        boolean bytearray = (pixels instanceof byte[]);
//
//        for (int yy = 0; yy < height; yy++) {
//            for (int xx = 0; xx < width; xx++)
//                if (bytearray) {
//                    savedPixels[destinationOffset++] = cm.getRGB(((byte[]) pixels)[sourceOffset++] &
//                            0xff);
//                } else {
//                    savedPixels[destinationOffset++] = cm.getRGB(((int[]) pixels)[sourceOffset++]);
//                }
//
//            sourceOffset += (scansize - width);
//            destinationOffset += (savedWidth - width);
//        }
//    }
//
//    public void setPixels(int x, int y, int width, int height, ColorModel cm,
//        byte[] pixels, int offset, int scansize) {
//        setThePixels(x, y, width, height, cm, pixels, offset, scansize);
//    }
//
//    public void setPixels(int x, int y, int width, int height, ColorModel cm,
//        int[] pixels, int offset, int scansize) {
//        setThePixels(x, y, width, height, cm, pixels, offset, scansize);
//    }
//
//    public void imageComplete(int status) {
//        if ((status == IMAGEABORTED) || (status == IMAGEERROR)) {
//            consumer.imageComplete(status);
//
//            return;
//        } else {
//            int[] pixels = new int[savedWidth];
//            int position;
//            int[] sumArray;
//            int sumIndex;
//            sumArray = new int[9]; // maxsize - vs. Vector for performance
//
//            for (int yy = 0; yy < savedHeight; yy++) {
//                position = 0;
//
//                int start = yy * savedWidth;
//
//                for (int xx = 0; xx < savedWidth; xx++) {
//                    sumIndex = 0;
//                    sumArray[sumIndex++] = savedPixels[start + xx];
//
//                    if (yy != (savedHeight - 1)) {
//                        sumArray[sumIndex++] = savedPixels[start + xx +
//                            savedWidth];
//                    }
//
//                    if (yy != 0) {
//                        sumArray[sumIndex++] = savedPixels[(start + xx) -
//                            savedWidth];
//                    }
//
//                    if (xx != (savedWidth - 1)) {
//                        sumArray[sumIndex++] = savedPixels[start + xx + 1];
//                    }
//
//                    if (xx != 0) {
//                        sumArray[sumIndex++] = savedPixels[(start + xx) - 1];
//                    }
//
//                    if ((yy != 0) && (xx != 0)) {
//                        sumArray[sumIndex++] = savedPixels[(start + xx) -
//                            savedWidth - 1];
//                    }
//
//                    if ((yy != (savedHeight - 1)) && (xx != (savedWidth - 1))) {
//                        sumArray[sumIndex++] = savedPixels[start + xx +
//                            savedWidth + 1];
//                    }
//
//                    if ((yy != 0) && (xx != (savedWidth - 1))) {
//                        sumArray[sumIndex++] = savedPixels[(start + xx) -
//                            savedWidth + 1];
//                    }
//
//                    if ((yy != (savedHeight - 1)) && (xx != 0)) {
//                        sumArray[sumIndex++] = savedPixels[(start + xx +
//                            savedWidth) - 1];
//                    }
//
//                    pixels[position++] = avgPixels(sumArray, sumIndex);
//                }
//
//                consumer.setPixels(0, yy, savedWidth, 1, defaultCM, pixels, 0,
//                    savedWidth);
//            }
//
//            consumer.imageComplete(status);
//        }
//    }