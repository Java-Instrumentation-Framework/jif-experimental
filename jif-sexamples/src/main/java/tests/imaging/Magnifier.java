/*
 * Magnifier.java
 *
 * Created on November 15, 2006, 7:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.imaging;



import java.awt.*;
import java.awt.image.renderable.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.media.jai.*;
import javax.swing.*;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import edu.mbl.jif.imaging.jai.ImageDisplay;

/**
 * An output widget used as a magnifing glass derived from
 * javax.swing.JComponent, and can be used in any context
 * that calls for a * JComponent.
 *
 * @author Dennis Sigel
 */

public class Magnifier extends JComponent {
    
    private PlanarImage image;
    private JComponent parent = null;
    private float magnification = 2.0F;
    
    public Magnifier() {
        setOpaque(true);
    }
    
    public void setSource(JComponent parent) {
        this.parent = parent;
        parent.addMouseListener(new MouseClickHandler());
        parent.addMouseMotionListener(new MouseMotionHandler());
        
        if ( parent instanceof ImageDisplay ) {
            image = (PlanarImage)((ImageDisplay)parent).getImage();
        }
    }
    
    public void setMagnification(float f) {
        if ( f < 0.005 ) {
            magnification = 0.005F;
        } else {
            magnification = f;
        }
        
        repaint();
    }
    
    /**
     * Paint the image onto a Graphics object. The painting is
     * performed tile-by-tile, and includes a grey region covering the
     * unused portion of image tiles as well as the general
     * background.
     */
    public synchronized void paintComponent(Graphics g) {
        
        Graphics2D g2D = null;
        if (g instanceof Graphics2D) {
            g2D = (Graphics2D)g;
        } else {
            System.err.println("not a Graphic2D");
            return;
        }
        
        g2D.setColor(getBackground());
        g2D.fillRect(0, 0, getWidth(), getHeight());
        
        if ( image != null ) {
            Dimension d = getSize();
            Point p = getLocation();
            Insets insets = parent.getInsets();
            
// area to crop (plus a bit)
            int w = (int)((float)d.width / magnification + .5F) + 1;
            int h = (int)((float)d.height / magnification + .5F) + 1;
            
            int x = p.x + (d.width - w)/2 - insets.left;
            int y = p.y + (d.height - h)/2 - insets.top;
            
// must clip for cropping
            if ( x < 0 ) x = 0;
            if ( y < 0 ) y = 0;
            
            if ( (x + w) > image.getWidth() ) {
                w = image.getWidth() - x;
            }
            
            if ( (y + h) > image.getHeight() ) {
                h = image.getHeight() - y;
            }
            
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(image);
            pb.add((float)x);
            pb.add((float)y);
            pb.add((float)w);
            pb.add((float)h);
            RenderedOp tmp = JAI.create("crop", pb, null);
            
            PlanarImage dst = JAI.create("scale",
                    tmp,
                    magnification,
                    magnification,
                    (float)-x*magnification,
                    (float)-y*magnification,
                    Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
            
            AffineTransform t = AffineTransform.getTranslateInstance(0, 0);
            g2D.drawRenderedImage(dst, t);
        }
    }
    
// moves the slider box
    class MouseClickHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            int mods = e.getModifiers();
            Point p = e.getPoint();
            
            if ( (mods & InputEvent.BUTTON1_MASK) != 0 ) {
                moveit(p.x, p.y);
            }
        }
        
        public void mouseReleased(MouseEvent e) {
        }
    }
    
    class MouseMotionHandler extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            int mods = e.getModifiers();
            
            if ( (mods & InputEvent.BUTTON1_MASK) != 0 ) {
                moveit(p.x, p.y);
            }
        }
    }
    
    public final void moveit(int px, int py) {
        Insets inset = parent.getInsets();
        Dimension dm = getSize();
        Dimension dp = parent.getSize();
        
        int pw = dm.width / 2;
        int ph = dm.height / 2;
        int x = px - pw;
        int y = py - ph;
        
        if ( px < inset.left ) x = -pw + inset.left;
        if ( py < inset.top ) y = -ph + inset.top;
        if ( px >= (dp.width - inset.right ) ) x = dp.width - pw - inset.right;
        if ( py >= (dp.height - inset.bottom) ) y = dp.height - ph - inset.bottom;
        
// magnifier origin
        setLocation(x, y);
    }
}