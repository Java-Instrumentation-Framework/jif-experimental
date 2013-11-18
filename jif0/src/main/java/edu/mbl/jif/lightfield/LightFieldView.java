/*
 * LightFieldView.java
 *
 * Created on September 29, 2006, 5:01 PM
 */
package edu.mbl.jif.lightfield;

import edu.mbl.jif.gui.imaging.array.ImageArrayDisplayPanel;
import edu.mbl.jif.gui.imaging.ScannerTZ;
import edu.mbl.jif.imaging.api.SeriesOfImages;
import edu.mbl.jif.imaging.series.SeriesOfImagesMultipageTiff;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import javax.swing.JFrame;

public class LightFieldView {
    byte[] view;
    BufferedImage viewImage;
    WritableRaster wr;
    byte[] source;
    private int pitch;
    
    int wS;
    int hS;
    int nx;
    int ny;
    int dx;
    int dy;
    int xOffset;
    int yOffset;
    double xDisplacement;
    double yDisplacement;
    
    // ImageJ...
    boolean useIJ = false;
    ImagePlus sourceImagePlus;
    ImagePlus viewImagePlus;
    ImageProcessor viewIp = null;

    boolean debug = false;
    
    
    // load source from file
    public LightFieldView(String file,int diameter) {
        this.loadSource(file);
        this.pitch = diameter;
        this.nx = wS / diameter;
        this.ny = hS / diameter;
        init();
    }
    
    // source is ImagePlus
    public LightFieldView(ImagePlus sourceImagePlus, int pitch) {
        source = (byte[])sourceImagePlus.getProcessor().getPixels();
        this.pitch = pitch;
        wS = sourceImagePlus.getWidth();
        hS = sourceImagePlus.getHeight();
        this.nx = wS / pitch;
        this.ny = hS / pitch;
        viewImagePlus = NewImage.createByteImage("Lightfield View", nx, ny, 1, 0);
        init();
    }
    
    // source in memory
    public LightFieldView(byte[] source, int wS, int hS, int pitch) {
        this(source, wS, hS, wS / pitch, hS / pitch);
        this.pitch = pitch;
    }
    
    public LightFieldView(byte[] source, int wS, int hS, int nx, int ny) {
        //super(nx, ny, BufferedImage.TYPE_BYTE_GRAY);
        //super();
        this.source = source;
        this.wS = wS;
        this.hS = hS;
        this.nx = nx;
        this.ny = ny;
        init();
    }
    
    
    private void init() {
        int depth = 8;
        System.out.println("nx, ny:" + nx + ", " + ny);
        view = new byte[nx * ny];
        DataBuffer db = new DataBufferByte(view, view.length);
        try {
            viewImage = new BufferedImage(nx, ny, BufferedImage.TYPE_BYTE_GRAY);
            wr = viewImage.getRaster();
            wr.setDataElements(0, 0, nx, ny, view);
        } catch (Exception e) {
        }
        dx = wS / nx;
        dy = hS / ny;
    }
    
    
    public void loadSource(String sourceFilename) {
        ArrayList imgs = MultipageTiffFile.loadImageArrayList(sourceFilename);
        //getImageDataType((BufferedImage)imgs.get(0));
        source = ((DataBufferByte)((BufferedImage)imgs.get(0)).getRaster().getDataBuffer())
        .getData();
        wS = ((BufferedImage)imgs.get(0)).getWidth();
        hS = ((BufferedImage)imgs.get(0)).getHeight();
    }
    
//    public static BufferedImage byteArrayToBufferedImage(byte[] imgArray, int width,
//            int height) {
//        int size = width * height;
//        BufferedImage image = null;
//        byte[] image_data = new byte[size];
//        image_data = imgArray;
//        DataBuffer db = new DataBufferByte(image_data, image_data.length);
//        try {
//            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//            WritableRaster wr = image.getRaster();
//            wr.setDataElements(0, 0, width, height, image_data);
//        } catch (Exception e) {
//        }
//        return image;
//        // If you need a planar image
//        // PlanarImage pi = PlanarImage.wrapRenderedImage(ri)
//    }
    
    
    public void setOffset(int x, int y) {
        xOffset = x;
        yOffset = y;
    }
    
    
    public void setDiameter(int diam) {
        pitch = diam;
    }
    
    
    public void perspective(double xDisplacement, double yDisplacement) {
        this.xDisplacement = xDisplacement;
        this.yDisplacement = yDisplacement;
        updateView();
    }
    
    
    public void updateView() {
        int xV = 0;
        int yV = 0;
        for (int yS = yOffset + (int)yDisplacement; yS < hS; yS = yS + dy) {
            xV = 0;
            for (int xS = xOffset + (int)xDisplacement; xS < wS; xS = xS + dx) {
                //this.getRaster().setPixel(xV, yV, source[xS + (yS)]);
                //int p = source[xS + (yS * wS)] & 0xff;
                if (debug) {
                    System.out.println(
                            "(" + xS + ", " + yS + ") ---> (" + xV + ", " + yV + ")  s: "
                            + source[xS + (yS * wS)]);
                }
                int indexV = xV + (yV * ny);
                int indexS = xS + (yS * wS);
                if ((indexV < view.length) && (indexS < source.length)) {
                    view[indexV] = source[indexS];
                }
                xV++;
            }
            yV++;
        }
        if (useIJ) {
            viewImagePlus.getProcessor().setPixels(view);
            viewImagePlus.updateAndDraw();
            viewImagePlus.updateAndRepaintWindow();
        } else {
            wr.setDataElements(0, 0, nx, ny, view);
        }
        //displayFrame.changeImage(img);
        System.out.println("updated");
    }
    
    public BufferedImage getViewImage() {
        return viewImage;
    }
    
//==============================================================================
    
    Rectangle scanWindow;
    
    public void setScanWindow(int x1, int y1, int spanX, int spanY) {
        scanWindow = new Rectangle(x1, y1, spanX, spanY);
    }
    
    
    public void generateSeriesToFile(String viewSetFilename) {
        MultipageTiffFile f = new MultipageTiffFile(viewSetFilename, true);
        for (int j = scanWindow.y; j < scanWindow.y + scanWindow.height; j++) {
            for (int i = scanWindow.x; i < scanWindow.x + scanWindow.width; i++) {
                perspective(i, j);
                
                f.appendImage(scale(viewImage,3.0));
            }
        }
        f.close();
    }
    
    
    public void generateSeriesToStackIJ(ImagePlus imp) {
        // create NewStack
        for (int j = scanWindow.y; j < scanWindow.y + scanWindow.height; j++) {
            for (int i = scanWindow.x; i < scanWindow.x + scanWindow.width; i++) {
                perspective(i, j);
                //put in stack
            }
        }
        // show stack
    }
    
    
    
// see SubSampleAverage
    
    private BufferedImage scale(BufferedImage in, double scale) {
        AffineTransform tx = new AffineTransform();
        tx.scale(scale, scale);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(in, null);
        
    }
//    public BufferedImage integralScale(BufferedImage source, double diam) {
//        ImageObserver imgObs = null;
//        AffineTransform aTransform = new AffineTransform()
//        Graphics2D g2 = source.getGraphics();
//        g2.drawImage(aTransform, imgObs);
//    }
//BufferedImage scale(image,horizontal,vertical) {
//transform = new AffineTransform();
//transform.scale(0.5 ,0 .5); //Note This
//op = new AffineTransformOp(transform,AffineTransformOp.TYPE_BILINEAR);
//return op.filter(image,null);
//}
    
    
    public static void main(String[] args) {
        int w;
        int h;
        // = new byte[w*h];
        //        for (int i = 0; i < src.length; i++) {
        //            src[i] = (byte) 90;
        //        }
        String sourceFilename = "C:\\_dev\\testdata\\lightfield\\SourceInt.tif";
        int diameter = 17;
        double measuredDiameter = 16.9;
        int xOffset = 4;
        int yOffset = 4;
        int X1=7;
        int spanX=9;
        int Y1=7;
        int spanY=9;
        
        String viewSetFilename = "C:\\_dev\\testdata\\lightfield\\Result";
        
        LightFieldView lfv = new LightFieldView(sourceFilename, diameter);
        lfv.setOffset(xOffset, yOffset);
        lfv.setScanWindow(X1, Y1, spanX, spanY);
        lfv.generateSeriesToFile(viewSetFilename);
        int zSections = spanX;
				SeriesOfImages series = new SeriesOfImagesMultipageTiff(viewSetFilename+".tif", zSections);
        ImageArrayDisplayPanel.openInFrame(series);
        lfv.openInScannerTZ(viewSetFilename+".tif",  zSections);
//        SeriesPlayerZoomFrame f = new SeriesPlayerZoomFrame(viewSetFilename+".tif");
//        f.pack();
//        f.setVisible(true);
    }
    
    public void openInScannerTZ(String fieldSetFile, int zSections) {
        JFrame frame = new JFrame("ScannerTZ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SeriesOfImagesMultipageTiff series = new SeriesOfImagesMultipageTiff(fieldSetFile, zSections);
        frame.setContentPane(new ScannerTZ(series));
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
    
// loadSource(String
}

