/*
 * ImageSequence.java
 *
 * Created on November 9, 2006, 11:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.imaging;

import java.awt.image.*;

import java.util.*;


public class ImageSequence extends Thread implements ImageProducer {
    int width;
    int height;
    int delay;
    ColorModel model = ColorModel.getRGBdefault();
    FrameARGBData frameData;
    private Vector consumers = new Vector();

    public ImageSequence(FrameARGBData src, int maxFPS) {
        frameData = src;
        width = frameData.size().width;
        height = frameData.size().height;
        delay = 1000 / maxFPS;
        setPriority(MIN_PRIORITY + 1);
    }

    public void run() {
        while (frameData != null) {
            frameData.nextFrame();
            sendFrame();
            try {
                sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void addConsumer(ImageConsumer c) {
        if (isConsumer(c)) {
            return;
        }
        consumers.addElement(c);
        c.setHints(ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.SINGLEPASS);
        c.setDimensions(width, height);
        c.setProperties(new Hashtable());
        c.setColorModel(model);
    }

    public synchronized boolean isConsumer(ImageConsumer c) {
        return (consumers.contains(c));
    }

    public synchronized void removeConsumer(ImageConsumer c) {
        consumers.removeElement(c);
    }

    public void startProduction(ImageConsumer ic) {
        addConsumer(ic);
    }

    public void requestTopDownLeftRightResend(ImageConsumer ic) {
    }

    private void sendFrame() {
        for (Enumeration e = consumers.elements(); e.hasMoreElements();) {
            ImageConsumer c = (ImageConsumer) e.nextElement();
            c.setPixels(0, 0, width, height, model, frameData.getPixels(), 0,
                width);
            c.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
        }
    }
}
