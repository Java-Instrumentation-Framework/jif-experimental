package edu.mbl.jif.camera.test;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class StaticTV
        extends JPanel {//implements Runnable {
    int arrayLength, pixels[];
    MemoryImageSource source;
    Image image;
    int width, height;
    long frames = 0;
    
    
    public void init() {
        width = getSize().width;
        height = getSize().height;
        SourceOfStatic source = new SourceOfStatic(width, height);
        //image = createImage(source);
    }
    
   
//    public void update(Graphics g) {
//        paint(g);
//    }
//    public void paint(Graphics g) {
//        g.drawImage(image, 0, 0, this);
//    }
    public void paintComponent(Graphics g) {
        //super.paintComponents(g);
        g.setColor(Color.black);
        g.drawImage(image, 0, 0, this);
        g.setColor(Color.white);
        g.drawString(String.valueOf(frames), 10,20);
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
