/*
 * DisplayLive.java
 * Created on May 9, 2006, 1:48 PM
 */
package edu.mbl.jif.camera.display;

import edu.mbl.jif.gui.imaging.DisplayLiveInterface;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.border.MatteBorder;

/**
 * Live display of images streamed from Camera containing an jif.gui.imaging.ImageDisplayPanel
 * @author GBH
 */
public class DisplayLiveCamera extends JFrame implements DisplayLiveInterface {

    ImageDisplayPanel viewPanel;
    StreamGenerator gen;
    StreamSource source;

    public DisplayLiveCamera(final StreamGenerator gen) {
        this.gen = gen;
        System.out.println("opening Display");
        this.setTitle("Camera Live (" + gen.getWidth() + "x" + gen.getHeight() + ")");
        try {
            this.setIconImage((new javax.swing.ImageIcon(getClass().getResource(
                "/edu/mbl/jif/camera/icons/liveDisplay16.gif"))).getImage());
        } catch (Exception ex) {
        }

        //get the camera's StreamSource
        source = gen.getStreamSource();
        if (source != null) {
            Dimension imageDim = new Dimension(gen.getWidth(), gen.getHeight());
            viewPanel = new ImageDisplayPanel(imageDim);
            viewPanel.addPlotButton();
            viewPanel.addMagnifierButton();
            viewPanel.addMarkPointButton();
            viewPanel.setStreamingSource(source); // <<<<<<<<<<<

//            JPanel identifyingColor = new JPanel();
//            identifyingColor.setBackground(Color.blue);
//            identifyingColor.setMaximumSize(new Dimension(9999, 4));
//            add(identifyingColor, BorderLayout.NORTH);
            MatteBorder matte = new MatteBorder(3, 3, 3, 3, new Color(250, 250, 140));
            //MatteBorder matte = new MatteBorder(3, 3, 3, 3, new Color(134,197,250));
            viewPanel.setBorder(matte);
            add(viewPanel, BorderLayout.CENTER);
            this.setSize(StaticSwingUtils.sizeFrameForDefaultScreen(imageDim));
            StaticSwingUtils.locateLowerRight(this);
            //this.setLocation(r.x, r.y);

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
                    rememberBounds();
                }

                public void componentMoved(ComponentEvent e) {
                    rememberBounds();
                }

            });
        } else {
            Application.getInstance().error("DisplayLive: Couldn't open stream.");
        }
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public ImageDisplayPanel getImageDisplayPanel() {
        return viewPanel;
    }

    private void rememberBounds() {
        Rectangle bounds = this.getBounds();
    }

    public void setSize(Dimension dim) {
        super.setSize((int) dim.getWidth(), (int) dim.getHeight());
    }

    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
    }

    @Override
    public void setScale(float scale) {
    }

    public void fitToScreen() {
    }

    // <editor-fold defaultstate="collapsed" desc=" Streaming ">
    public void setStreamSource(StreamSource source) {
        this.source = source;
        viewPanel.setStreamingSource(source);
    }

    public StreamSource getStreamSource() {
        return source;
    }

    public void suspend() {
        gen.stopStream();
    }

    public void resume() {
        gen.startStream();
    }

    public void restart() {
//        viewPanel.setStreamingSource(gen.getStreamSource()); // <<<<<<<<<<<
//        gen.startStream();
        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {
                viewPanel.setStreamingSource(gen.getStreamSource()); // <<<<<<<<<<<
                gen.startStream();
            }

        });
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- ROI ---<<<" >
    public void setSelectedROI(Rectangle roi) {
        viewPanel.setROI(roi);
    }

    public Rectangle getSelectedROI() {
        return viewPanel.getROI();
    }

    public boolean isROISet() {
        return viewPanel.isROIset();
    }
// </editor-fold>
    
    // Closing...
    public void onCloseContainer() {
        close();
    }

    public void close() {
        this.setVisible(false);
        viewPanel.releaseStreamingSource();
        gen.closeStreamSource(); // <<<<< ???
        //source.detachMonitor(mon)
        viewPanel = null;
        gen = null;
        source = null;
        ((InstrumentController) CamAcqJ.getInstance().getController()).setDisplayLive(null);
        this.dispose();
    }

}
