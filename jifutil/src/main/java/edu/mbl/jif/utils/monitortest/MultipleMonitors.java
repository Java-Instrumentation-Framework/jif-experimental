package edu.mbl.jif.utils.monitortest;

import java.awt.*;
import javax.swing.*;

// Check Multi-Monitor Capabilities

public class MultipleMonitors {

    /* NOTES: 
     * 
     * DualView enabled: the two monitors are SEPARATED. getScreenDevices() returns two GraphicsDevices. 
    JFrames maximize on a single monitor, the one related to the GraphicsConfiguration they are bound to. 
    It is still possible to manually drag the border of a JFrame across the monitor boundaries, and 
    the JFrame will appear half on the first monitor and half on the second monitor (yes, I agree, this 
    is quite strange...).
    
     * DualView disabled: the two monitors are viewed as a single extended display. 
    getScreenDevices() returns just ONE GraphicsDevice. JFrames maximize across both monitors.
     */
    
    
    public static void main(String[] argv) {
        showConfigurations();
        //test();
    }

    public static void test() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for (int j = 0; j < gs.length; j++) {
            GraphicsDevice gd = gs[j];
            GraphicsConfiguration[] gc = gd.getConfigurations();
            for (int i = 0; i < gc.length; i++) {
                JFrame f = new JFrame(gs[j].getDefaultConfiguration());
                GCCanvas c = new GCCanvas(gc[i]);
                Rectangle gcBounds = gc[i].getBounds();
                System.out.println("Bounds: " + j + ", " + i + " - " +  gcBounds );
                int xoffs = gcBounds.x;
                int yoffs = gcBounds.y;
                f.getContentPane().add(c);
                f.setTitle("Screen# " + Integer.toString(j) + ", GC# " + Integer.toString(i));
                f.setSize(300, 150);
                f.setLocation((i * 50) + xoffs, (i * 60) + yoffs);
                f.setVisible(true);
            }
        }
    }

    public static void showConfigurations() {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = ge.getScreenDevices();
        System.out.println("Screen Devices: ");
        for (int i = 0; i < screenDevices.length; i++) {
            System.out.println("    " + screenDevices[i].getIDstring());
        }
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle r = defaultScreen.getDefaultConfiguration().getBounds();
        System.out.println("defaultBounds: " + r);
        GraphicsConfiguration[] configurations = defaultScreen.getConfigurations();
        System.out.println("Default screen device: " + defaultScreen.getIDstring());
        for (int i = 0; i < configurations.length; i++) {
            System.out.println("  Configuration " + (i + 1));
            System.out.println("  " + configurations[i].getColorModel());
            configurations[i].getBounds();
            //configurations[i].createCompatibleImage(i, i)
        }
        System.exit(0);
    }

    static class GCCanvas
        extends Canvas {

        GraphicsConfiguration gc;
        Rectangle bounds;

        public GCCanvas(GraphicsConfiguration gc) {
            super(gc);
            this.gc = gc;
            bounds = gc.getBounds();
        }

        public Dimension getPreferredSize() {
            return new Dimension(300, 150);
        }

        public void paint(Graphics g) {
            g.setColor(Color.red);
            g.fillRect(0, 0, 100, 150);
            g.setColor(Color.green);
            g.fillRect(100, 0, 100, 150);
            g.setColor(Color.blue);
            g.fillRect(200, 0, 100, 150);
            g.setColor(Color.black);
            g.drawString("ScreenSize=" +
                Integer.toString(bounds.width) +
                "X" + Integer.toString(bounds.height), 10, 15);
            g.drawString(gc.toString(), 10, 30);
        }
    }
}
