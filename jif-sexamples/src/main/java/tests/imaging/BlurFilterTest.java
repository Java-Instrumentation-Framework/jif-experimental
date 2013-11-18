/*
 * BlurFilterTest.java
 *
 * Created on October 27, 2006, 9:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.imaging;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

import java.io.File;

import javax.imageio.ImageIO;


public class BlurFilterTest {
    Image i;
    ImageProducer producer;
    Image filterImg;

//    public static void main(String[] args) {
//        (new BlurFilterTest()).init();
//    }
//
//    public void init() {
//        i = load("D:\\_TestImages\\ByType\\jpg\\cow.jpg");
//
//        producer = new FilteredImageSource(i.getSource(), new BlurFilter());
//        filterImg = Toolkit.getDefaultToolkit().createImage(producer);
//        System.out.println("Created");
//    }
//
//    public void paint(Graphics g) {
//        g.drawImage(i, 10, 10, null); // regular
//
//        if (filterImg != null) {
//            g.drawImage(filterImg, 250, 10, null); // average
//        }
//    }
//
//    public Image load(String image_file_name) {
//        Image image = null;
//
//        try {
//            image = ImageIO.read(new File(image_file_name));
//        } catch (Exception e) {
//            System.out.println("Exception loading: " + image_file_name);
//            e.printStackTrace();
//        }
//
//        return image;
//    }
//}
//
//
//// This example is from the book _Java AWT Reference_ by John Zukowski.
//// Written by John Zukowski. Copyright (c) 1997 O'Reilly & Associates.
//// You may study, use, modify, and distribute this example for any purpose.
//// This example is provided WITHOUT WARRANTY either expressed or
class BlurFilter extends ImageFilter {
    
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
//
//    private int avgPixels(int[] pixels, int size) {
//        float redSum = 0;
//        float greenSum = 0;
//        float blueSum = 0;
//        float alphaSum = 0;
//
//        for (int i = 0; i < size; i++)
//            try {
//                int pixel = pixels[i];
//                redSum += defaultCM.getRed(pixel);
//                greenSum += defaultCM.getGreen(pixel);
//                blueSum += defaultCM.getBlue(pixel);
//                alphaSum += defaultCM.getAlpha(pixel);
//            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("Ooops");
//            }
//
//        int redAvg = (int) (redSum / size);
//        int greenAvg = (int) (greenSum / size);
//        int blueAvg = (int) (blueSum / size);
//        int alphaAvg = (int) (alphaSum / size);
//
//        return ((0xff << 24) | (redAvg << 16) | (greenAvg << 8) |
//        (blueAvg << 0));
//    }
}
