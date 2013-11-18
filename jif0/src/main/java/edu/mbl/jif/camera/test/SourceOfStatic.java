package edu.mbl.jif.camera.test;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SourceOfStatic implements Runnable {
    int arrayLength, pixels[];
    MemoryImageSource source;
    Image image;
    int width, height;
    long frames = 0;
    
    public SourceOfStatic(int width, int height) {
        
    }
    public void init() {
        
        this.width = width;
        this.height = height;
        arrayLength = width * height;
        pixels = new int[arrayLength];
//        source = new MemoryImageSource(width, height, pixels, 0, width);
//        source.setAnimated(true);
        //image = createImage(source);
        new Thread(this).start();
    }
    
    public ImageProducer getSource() {
        return source;
    }

    public void run() {
        int h = 1;
        int white = Color.white.getRGB();
        int black = Color.black.getRGB();
        while (true) {
            try {
                Thread.sleep(8); //1000 / 24);
            } catch (InterruptedException e) { /* die */}
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if( y == h) {
                        pixels[y * width + x] = white ;
                    } else {
                        pixels[y * width + x] =  black;
                    }
                    
                    
                /*
               boolean rand = Math.random() > 0.5;
               pixels[y * width + x] =
                     rand ? Color.black.getRGB() : Color.white.getRGB();
                 */
                }
            }
            h++;
            if(h>height) h =1;
            // Push out the new data
            frames++;
            source.newPixels(0, 0, width, height);
        }
    }
    
  
    
    public static void main(String[] args) {
        JFrame f = new JFrame("Aim For the Center");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        StaticTV s = new StaticTV();
        f.getContentPane().add(s, BorderLayout.CENTER);
        f.setPreferredSize(new Dimension(640,480));
        f.pack();
        f.show();
        s.init();
    }
    
    
    
}

