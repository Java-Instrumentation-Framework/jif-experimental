package edu.mbl.jif.camera.display;

import edu.mbl.jif.camera.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.color.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.*;

//_______________________________________________________________________
// displays byte[] or short[] array as a BufferedImage in a frame.
public class DisplayImageFrame
        extends JFrame {

    private static int width,  height;
    imagePanel vPanel;
    BufferedImage bImage;
    WritableRaster wr;
    String label;


    public DisplayImageFrame(Object imagePixels, String _label) {
        this.width = (int) Camera.width;
        this.height = (int) Camera.height;
        label = _label;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // position the display frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (height > screenSize.height) {
            height = screenSize.height;
        }
        if (width > screenSize.width) {
            width = screenSize.width;
        }
        setLocation((int) ((screenSize.width - width - 60)), 0);
        setSize(width + Globals.frameInsetH, height + Globals.frameInsetV);
        setTitle("Image: " + String.valueOf(System.currentTimeMillis()));
        setVisible(true);
        vPanel = new imagePanel(width, height, imagePixels);
        getContentPane().setLayout(null);
        getContentPane().add(vPanel);
        vPanel.repaint();
    }

// videoPanel ---------------------------------------------------------------

    class imagePanel
            extends JPanel {

        public imagePanel(int width, int height, Object pix) {
            setSize(width, height);
            setBackground(Color.white);
            setVisible(true);
            if (pix instanceof byte[]) {
                bImage = createImage(width, height, 8, (byte[]) pix);
            } else if (pix instanceof short[]) {
                bImage = createImage(width, height, 12, (short[]) pix);
            } else {
                System.out.println("unknown type in imagePanel");
            }
        }


        public void update(Graphics g) {
            paint(g);
        }


        public void paint(Graphics g) {
            g.setColor(Color.yellow);
            Font font = new Font("Serif", Font.PLAIN, 12);
            g.setFont(font);
            g.drawImage(bImage, 0, 0, width - 1, height - 1, Color.white, null);
            g.drawString(label, 5, 15);
        }

//------------------------------------------------------------------------

        public BufferedImage createImage(int imageWidth, int imageHeight,
                                         int imageDepth, short[] data) {
            ComponentColorModel ccm =
                    new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                    new int[]{imageDepth}, false, false,
                    Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
            ComponentSampleModel csm =
                    new ComponentSampleModel(DataBuffer.TYPE_USHORT, imageWidth,
                    imageHeight, 1, imageWidth, new int[]{
                        0
                    });
            DataBuffer dataBuf =
                    new DataBufferUShort((short[]) data, imageWidth);

            WritableRaster wr =
                    Raster.createWritableRaster(csm, dataBuf, new Point(0, 0));
            Hashtable ht = new Hashtable();
            ht.put("owner", "QCamJ");
            return new BufferedImage(ccm, wr, true, ht);
        }


        public BufferedImage createImage(int imageWidth, int imageHeight,
                                         int imageDepth, byte[] data) {
            ComponentColorModel ccm =
                    new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                    new int[]{
                        imageDepth
                    }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            ComponentSampleModel csm =
                    new ComponentSampleModel(DataBuffer.TYPE_BYTE, imageWidth,
                    imageHeight, 1, imageWidth, new int[]{
                        0
                    });
            DataBuffer dataBuf =
                    new DataBufferByte((byte[]) data, imageWidth);

            WritableRaster wr =
                    Raster.createWritableRaster(csm, dataBuf, new Point(0, 0));
            Hashtable ht = new Hashtable();
            ht.put("owner", "QCamJ");
            return new BufferedImage(ccm, wr, true, ht);
        }


    }
}
