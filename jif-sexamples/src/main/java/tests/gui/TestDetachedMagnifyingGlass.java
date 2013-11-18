package tests.gui;

import edu.mbl.jif.gui.imaging.magnify.DetachedMagnifyingGlass;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestDetachedMagnifyingGlass extends Object {
    JFrame imgFrame;
    DetachedMagnifyingGlass mag;
    JFrame magFrame;
    double scale = 2.0;
    
    public TestDetachedMagnifyingGlass(File f) {
        // image frame
        ImageIcon i = new ImageIcon(f.getPath());
        JLabel l = new JLabel(i);
        imgFrame = new JFrame("Image");
        imgFrame.getContentPane().add(l);
        imgFrame.pack();
        imgFrame.setVisible(true);
        // magnifying glass frame
        magFrame = new JFrame("Mag");
        mag =  new DetachedMagnifyingGlass(l, new Dimension(150, 150), scale);
        magFrame.getContentPane().add(mag);
        magFrame.pack();
        magFrame.setLocation(new Point(
                imgFrame.getLocation().x + imgFrame.getWidth(),
                imgFrame.getLocation().y));
        
//        magFrame.addComponentListener(
//                new ComponentAdapter() {
//            public void componentResized(ComponentEvent e) {
//                onResize();
//            }
//        });
        magFrame.setVisible(true);
    }
    
//    public void onResize() {
//        Dimension newSize = magFrame.getSize();
//        mag.setSize(new Dimension((int)newSize.getWidth()-10, (int)newSize.getHeight() -34));
//    }
    
    
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        new TestDetachedMagnifyingGlass(f);
    }
    
}
