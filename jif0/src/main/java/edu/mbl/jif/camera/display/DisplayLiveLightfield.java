/*
 * DisplayLive.java
 * Created on May 9, 2006, 1:48 PM
 */
package edu.mbl.jif.camera.display;

import edu.mbl.jif.gui.imaging.DisplayLiveInterface;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Live display of images streamed from Camera containing an ImageDisplayPanel
 * @author GBH
 */
public class DisplayLiveLightfield
        extends JFrame
        implements DisplayLiveInterface {

    ImageDisplayPanel viewPanel;
    StreamGenerator gen;
    StreamSource source;


    public DisplayLiveLightfield(final StreamGenerator gen) {
        this.gen = gen;
        System.out.println("Opening Lightfield Display");
        this.setTitle("Lightfield Live (" + gen.getWidth() + "x" + gen.getHeight() + ")");
        try {
            this.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/lightfield16.png"))).getImage());
        } catch (Exception ex) {
        }

        //get the camera's StreamSource
        source = gen.getStreamSource();
        if (source != null) {
            Dimension imageDim = new Dimension(gen.getWidth(), gen.getHeight());
            viewPanel = new ImageDisplayPanel(imageDim);
            viewPanel.setStreamingSource(source); // <<<<<<<<<<<
            add(viewPanel, BorderLayout.CENTER);
            Rectangle r = sizeAppropriately(imageDim);
            this.setSize(r.width, r.height);
            this.setLocation(r.x, r.y);

            //pack();
            setVisible(true);
            //viewPanel.onResize();
            //System.out.println(cam.listAllParms());
            super.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(WindowEvent winEvt) {
                    onCloseContainer();
                }


            });
            super.addComponentListener(new ComponentAdapter() {

                public void componentResized(ComponentEvent e) {
                    viewPanel.onResize();
                }


            });
        } else {
            Application.getInstance().error("DisplayLive: Couldn't open stream.");
        }
    }


    public Rectangle sizeAppropriately(Dimension imageDim) {
        int xBuffer = 50;
        int yBuffer = 50;
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        // temp
        //Rectangle r = new Rectangle(10, 10, 600, 550);
        r.x = r.x + xBuffer;
        r.y = r.y + yBuffer;
        r.width = r.width - (2 * xBuffer);
        r.height = r.height - (2 * yBuffer);
        if (imageDim.getWidth() < r.width) {
            r.width = (int) imageDim.getWidth() + 12;
        }
        if (imageDim.getHeight() < r.height) {
            r.height = (int) imageDim.getHeight() + 57;
        }
        return r;
    }


    public void setStreamSource(StreamSource source) {
        this.source = source;
        viewPanel.setStreamingSource(source);
    }


    public StreamSource getStreamSource() {
        return source;
    }

    //--------------------------------------------------------
    public void setSize(Dimension dim) {
        super.setSize((int) dim.getWidth(), (int) dim.getHeight());
    }


    public void setSize(int w, int h) {
        super.setSize(w, h);
    }


    public void setScale(float scale) {
    }


    public void fitToScreen() {
    }


    public void setMirrorImage(boolean t) {
    }


    public boolean isMirrorImage() {
        return false; // ++
    }


    public void suspend() {
        gen.stopStream();
    }


    public void resume() {
        gen.startStream();
    }


    public void restart() {
        viewPanel.setStreamingSource(gen.getStreamSource()); // <<<<<<<<<<<
        gen.startStream();
    //        SwingUtilities.invokeLater(new Runnable() {
    //            public void run() {
    //                viewPanel.setStreamingSource(cam.getStreamSource()); // <<<<<<<<<<<
    //                cam.startStream();                //...
    //            }
    //        });
    }


    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }


    public void setSelectedROI(Rectangle roi) {
        viewPanel.setROI(roi);
    }


    public Rectangle getSelectedROI() {
        return viewPanel.getROI();
    }


    public boolean isROISet() {
        return viewPanel.isROIset();
    }

    // Closing...
    public void onCloseContainer() {
        close();
    }


    public void close() {
        this.setVisible(false);
        viewPanel.releaseStreamingSource();
        viewPanel = null;
        source = null;
        ((InstrumentController) CamAcqJ.getInstance().getController()).setDisplayLive(null);
        this.dispose();
    }


}
