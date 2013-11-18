package edu.mbl.jif.camera.test;


import java.awt.*;
import java.awt.image.*;
import java.net.*;

public class ImageObserverExample implements ImageObserver {
    
    public BufferedImage createBufferedImage(Image image) {
        if(image instanceof BufferedImage)
            return (BufferedImage)image;
        int w = image.getWidth(this);
        int h = image.getHeight(this);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(image, 0, 0, this);
        g.dispose();
        return bi;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if ((infoflags & WIDTH) != 0)
            System.out.println("width=" + width);
        if ((infoflags & HEIGHT) != 0)
            System.out.println("height=" + height);
        return true;
    }

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://today.java.net/jag/bio/JagHeadshot-small.jpg");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        ImageObserverExample app = new ImageObserverExample();
        try {
            app.createBufferedImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*
 class myConsumer extends ImageConsumer {
 public void setPixels(int x, int y, int width, int height,
 ColorModel model, int pixels[], int off, int scansize) {
// do something with the 32-bit pixel data.
 }
 public void setPixels(int x, int y, int width, int height,
 ColorModel model, byte pixels[], int off, int scansize) {
// do something with the 8-bit pixel data.
 }
}
 */
