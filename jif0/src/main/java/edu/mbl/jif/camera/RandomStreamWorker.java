/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.camera;

//--------------------------------------------------------------------------

import java.awt.Graphics2D;
import java.util.Random;
/*
 * ((Unused)) copy in MockCamera
 */

    // Mock image stream generator...
   public class RandomStreamWorker
        extends Thread {

        private int count;
        private volatile boolean done = false;
        private boolean suspended = false;
        int arrayLength;
        byte[] pixels;
        int[] iArray;
        int width;
        int height;
        long frames = 0;
        MockCamera cam;

//        int fps = 50;
//        private long delayMillis = 1000 / fps;
        
        
        public RandomStreamWorker(int width, int height, byte[] pixels, MockCamera cam) {
            super("MockCamera-Random");
            this.cam = cam;
            this.width = width;
            this.height = height;
            arrayLength = width * height;
            this.pixels = pixels;
            iArray = new int[width * height];
        }

        public synchronized void safeStop() {
            System.out.println("MockCamera Worker stopped");
            done = true;
        }

        public synchronized void safeSuspend() {
            System.out.println("suspended");
            suspended = true;
        }

        public synchronized void safeResume() {
            System.out.println("resumed");
            suspended = false;
            notify();
        }

        synchronized boolean ok() {
            return (!done);
        }

        int h = 0;

        synchronized void doWork() {
            if (!suspended) {
                try {
                    Thread.sleep((int) cam.getExposureAcq());
                } catch (InterruptedException e) { /* die */

                }

                //Rectangle rect = new Rectangle(0,h,width, height);
                Graphics2D g = (Graphics2D) cam.bigImage.getGraphics();
                g.drawImage(cam.bigImage, width, height, null);
                g.drawString(String.valueOf(frames), 100, 100);
                cam.bigImage.getData().getPixels(0, h, width, height, iArray);//.getSubimage(0,0,width, height);
                //source.imageArrayByte[]
                applyTransform(width, height, pixels);
//                for (int x = 0; x < width; x++) {
//                    for (int y = 0; y < height; y++) {
//                        int offset = (y * width) + x;
//                        //if (y == h) {
//                        pixels[offset] = (byte) iArray[offset];
////                        } else {
////                            pixels[(y * width) + x] = 0;
////                        }
//                    }
//                }
//                h = h + 1;
//                if (h > bigImage.getHeight() - height) {
//                    h = 0;
//                }
                // Push out the new data
                frames++;

                cam.getStreamSource().callBack();
            } else {
                while (suspended) {
                }
            }
        }

        public void run() {
            while (ok()) {
                doWork();
            }

        }

        Random r = new Random();
        private double percent = 0.25;

         int rand(int min, int max) {
            return min + (int) (r.nextDouble() * (max - min));
        }

         byte[] applyTransform(int width, int height, byte[] pixels) {
            int n = (int) (percent * width * height);
            byte[] output = pixels;
            int rx, ry;
            int xmin = 0;
            int xmax = width;
            int ymin = 0;
            int ymax = height;
            for (int i = 0; i < n / 2; i++) {
                rx = rand(xmin, xmax);
                ry = rand(ymin, ymax);
                output[ry * width + rx] = (byte) 255;
                rx = rand(xmin, xmax);
                ry = rand(ymin, ymax);
                output[ry * width + rx] = (byte) 0;
            }
            return output;
        }

    }
