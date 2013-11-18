package edu.mbl.jif.gui.cursor;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class XORCursorTest {
    private static final int CURSOR_SIZE = 32;
    XORCursorTest() {
        final JFrame f=  new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("Diatoms.jpg");
        final JLabel label = new JLabel(icon) ;
        f.add(label, BorderLayout.CENTER);
        f.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                Point screenLoc = me.getLocationOnScreen();
                int x = screenLoc.x - CURSOR_SIZE / 2;
                int y = screenLoc.y - CURSOR_SIZE / 2;
                Shape r = new Rectangle(x, y, CURSOR_SIZE, CURSOR_SIZE);
                f.setCursor(CursorFactory.createXORCursor(r));
            }
        });
        f.setSize(400,300);
        f.setVisible(true);
        
    }
    
    public static void main(String[] args) {
        new XORCursorTest();
    }
    
    private static class CursorFactory {
        private static Toolkit kit = Toolkit.getDefaultToolkit();
        private static Robot bot;
        static {
            try {
                bot = new Robot();
            } catch (AWTException ae) {
                ae.printStackTrace();
                System.exit(0);
            }
        }
        static Cursor createXORCursor(Shape s) {
            Image xorImage = getXORImage(s);
            int w = s.getBounds().width;
            int h = s.getBounds().height;
            return kit.createCustomCursor(xorImage, new Point(w/2,h/2), "");
        }
        
        private static Image getXORImage (Shape s) {
            RGBImageFilter f = new XORFilter();
            Image bi = getCurrentImage(s);
            
            Image img = kit.createImage(new FilteredImageSource(bi.getSource(), f));
            return img;
        }
        
        private static Image getCurrentImage(Shape s) {
            return bot.createScreenCapture(s.getBounds());
        }
    }
    
    private static class XORFilter extends RGBImageFilter {
        XORFilter() {
            canFilterIndexColorModel = true;
        }
        public int filterRGB(int x, int y, int rgb) {
            return (rgb & 0xff000000) +     // preserve transparency
            (rgb & 0xffffff) ^ 0xffffff; // xor the components
        }
    }
}