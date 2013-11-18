/*
 * TestImageConsumer.java
 * Created on November 8, 2006, 10:09 AM
 */

package tests.imaging;


import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.imaging.stream.StreamSource;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.util.Hashtable;


/**
 *
 * @author GBH
 */
public class TestImageConsumer 
        implements ImageConsumer {    

    Image img;
    StreamSource source;
    String fileName;
    MultipageTiffFile tif;
    //  int w, h;
    int[] pixels;
    String pixValue = "";
    int updatePeriod = 10;
    int frames = 0;
    long startTime;
    boolean recording = false;
    BufferedImage bi;
    Graphics2D g2;
    
    public TestImageConsumer(StreamSource source, String fileName) {
        this.source = source;
        bi = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        g2 = bi.createGraphics();
        this.fileName = fileName;
//      img = createImage(source);
//      pixels = new int[img.getWidth(this) * img.getHeight(this)];
//      setSize(img.getWidth(this), img.getHeight(th is));
//      System.out.println("source: " + img.getSource());
        tif = new MultipageTiffFile(fileName, true);
        if(tif != null) {
            System.out.println("Video file " + fileName + " opened...");
        } else {
            System.err.println("Unable to open video file " + fileName);
        }
    }
    
    public void record() {
        if(!recording) {
            source.attachToStream(this);
            source.startProduction(this);
            startTime = System.nanoTime();
            recording = true;
        }
    }
    
    public void stop() {
        if(recording) {
            source.detachFromStream(this);
            recording = false;
            System.out.println("Video recording to " + fileName + " stopped");
        }
    }
    
    public void finish() {
        stop();
        tif.close();
        System.out.println("Video file " + fileName + " closed.");
    }
    
    public boolean isRecording() {
        return recording;
    }
    
    byte[] incoming;
    int width, height;
    
    public void setPixels(int x, int y, int width, int height, ColorModel model,
            byte pixels[], int off, int scansize) {
        this.width = width;
        this.height = height;
        incoming = pixels;
    }
    
    
    public void setPixels(int x, int y, int width, int height,
            ColorModel model, int pixels[], int off, int scansize) {
        // do something with the 32-bit int pixel data.
    }
    
// the rest of the implementation of ImageConsumer...
    
    public void setDimensions(int width, int height) {
        //this.width = width;
        //this.height = height;
    }
    
    
    public void setProperties(Hashtable props) {
        //this.props = props;
    }
    
    
    public void setColorModel(ColorModel model) {
        // Ignore.
    }
    
    
    public void setHints(int hintflags) {
        //this.hintflags = hintflags;
    }
    
    
    public void imageComplete(int status) {
        // handle 8-bit pixel data.
        frames++;
        CamAcqJ.getInstance().statusBarMessage("Video frame: " + frames);
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//        Graphics2D g2 = bi.createGraphics();
        bi.getRaster().setDataElements(0, 0, width, height, incoming);
        //g.drawImage(img, 0, 0, getSize().width, getSize().height, this);
        //g2.drawImage(img, 0, 0, this);
        String t = String.valueOf(frames +": " + (System.nanoTime() - startTime) / 1000000);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2.setColor(Color.black);
        g2.drawString(t, 8, 14);
        g2.setColor(Color.white);
        g2.drawString(t, 9, 15);
        tif.appendImage(bi);
        //g2.dispose();
        return;
    }
    
}
