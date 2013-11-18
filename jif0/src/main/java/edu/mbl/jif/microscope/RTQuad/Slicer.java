package edu.mbl.jif.microscope.RTQuad;

import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import edu.mbl.jif.gui.imaging.GraphicOverlay;
import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author GBH
 */
public class Slicer {

    static Rectangle rect1;
    static Rectangle rect2;
    static Rectangle rect3;
    static Rectangle rect4;
    static int originalWidth;
    static int originalHeight;

    public static void sliceItUp(int w, int h, // acquired frame dimension
        int insetW, int insetH, // insets
        int offsetW1, int offsetH1, int offsetW2, int offsetH2, int offsetW3, int offsetH3, int offsetW4, int offsetH4,
        FrameImageDisplay fid)
      {
        originalWidth = w;
        originalHeight = h;
        // subImage dimension
        int subW = w / 2 - 2 * insetW;
        int subH = h / 2 - 2 * insetW;

        int subX1 = insetW + offsetW1;
        int subY1 = insetH + offsetH1;

        int subX2 = w / 2 + insetW + offsetW2;
        int subY2 = insetH + offsetH2;

        int subX3 = insetW + offsetW3;
        int subY3 = h / 2 + insetH + offsetH3;

        int subX4 = w / 2 + insetW + offsetW4;
        int subY4 = h / 2 + insetH + offsetH4;

        rect1 = new Rectangle(subX1, subY1, subX1 + subW, subY1 + subH);
        rect2 = new Rectangle(subX2, subY2, subX2 + subW, subY2 + subH);
        rect3 = new Rectangle(subX3, subY3, subX3 + subW, subY3 + subH);
        rect4 = new Rectangle(subX4, subY4, subX4 + subW, subY4 + subH);

        System.out.println("" + rect1);
        System.out.println("" + rect2);
        System.out.println("" + rect3);
        System.out.println("" + rect4);
        System.out.println("");
        // Add an overlay
        GraphicOverlay overlay = new GraphicOverlay() {

            public void drawGraphicOverlay(ZoomGraphics zg)
              {
                zg.setColor(Color.red);
                zg.drawRect(rect1.x, rect1.y, rect1.width, rect1.height);
                zg.setColor(Color.blue);
                zg.drawRect(rect2.x, rect2.y, rect2.width, rect2.height);
                zg.setColor(Color.yellow);
                zg.drawRect(rect3.x, rect3.y, rect3.width, rect3.height);
                zg.setColor(Color.green);
                zg.drawRect(rect4.x, rect4.y, rect4.width, rect4.height);
              }

        };
        fid.getImageDisplayPanel().imagePane.addGraphicOverlay(overlay);
        
        BufferedImage bi = ImageFactoryGrayScale.testImageByte(1024, 1024);
        
        // get the current Image
        ImagePlus img = new ImagePlus("Image",bi);
        //ImagePlus img = IJ.getImage();
        ImageProcessor ip = img.getProcessor();
        // create new Stack
        ImageStack stack = new ImageStack(subW, subH);

        ip.setRoi(rect1.x, rect1.y, rect1.width, rect1.height);
        ImageProcessor ipNew1 = ip.crop();
        stack.addSlice("1", ipNew1);

        ip.setRoi(rect2.x, rect2.y, rect2.width, rect2.height);
        ImageProcessor ipNew2 = ip.crop();
        stack.addSlice("2", ipNew2);

        ip.setRoi(rect3.x, rect3.y, rect3.width, rect3.height);
        ImageProcessor ipNew3 = ip.crop();
        stack.addSlice("3", ipNew3);

        ip.setRoi(rect4.x, rect4.y, rect4.width, rect4.height);
        ImageProcessor ipNew4 = ip.crop();
        stack.addSlice("4", ipNew4);
        
        ImagePlus newImg = new ImagePlus("Stack",stack);
        newImg.show();


      }

    public static void main(String[] args)
      {

        int width = 1024;
        int height = 1024;
        FrameImageDisplay fid = new FrameImageDisplay(ImageFactoryGrayScale.testImageByte(width, height), "byte");
        Slicer.sliceItUp(width, height, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, fid);
        //Slicer.sliceItUp(width, height, 15, 20, 0, 0, 0, 0, 0, 0, 0, 0, fid);
      }

}
