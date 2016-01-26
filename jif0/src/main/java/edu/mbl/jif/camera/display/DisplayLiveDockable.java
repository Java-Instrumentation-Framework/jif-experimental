/*
 * DisplayLive.java
 * Created on May 9, 2006, 1:48 PM
 */
package edu.mbl.jif.camera.display;

import edu.mbl.jif.gui.imaging.DisplayLiveInterface;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.fabric.Application;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 * Live display of images streamed from Camera containing an ImageDisplayPanel
 * @author GBH
 */
public class DisplayLiveDockable
    extends JPanel
    implements Dockable, DisplayLiveInterface {

    ImageDisplayPanel viewPanel;
    StreamGenerator gen;
    StreamSource source;
    private DockKey key;

    public DisplayLiveDockable(final StreamGenerator gen) {
        this.gen = gen;
        setLayout(new BorderLayout());
        System.out.println("opening Display");
        key = new DockKey("LiveDisplay");
        //desk.setDockableWidth(this, 1.0);
        key.setName("LiveDisplay");
        //  this.setTitle("Camera Live (" + gen.getWidth() + "x" + gen.getHeight() + ")");
        //key.setTooltip(tooltip);
        try {
            key.setIcon((new javax.swing.ImageIcon(getClass().getResource(
                "/edu/mbl/jif/camera/icons/liveDisplay16.gif"))));
        } catch (Exception ex) {
        }

        // customized behaviour
        key.setCloseEnabled(false);
        //key.setAutoHideEnabled(false);
        key.setFloatEnabled(true);
        //key.setResizeWeight(0.5f); // takes all resizing 



        //get the camera's StreamSource
        source = gen.getStreamSource();
        if (source != null) {
            Dimension imageDim = new Dimension(gen.getWidth(), gen.getHeight());
            viewPanel = new ImageDisplayPanel(imageDim);
            viewPanel.addMarkPointButton();
            viewPanel.addPlotButton();
            viewPanel.addMagnifierButton();
            viewPanel.setStreamingSource(source); // <<<<<<<<<<<

//            JPanel identifyingColor = new JPanel();
//            identifyingColor.setBackground(Color.blue);
//            identifyingColor.setMaximumSize(new Dimension(9999, 4));
//            add(identifyingColor, BorderLayout.NORTH);
            MatteBorder matte = new MatteBorder(3, 3, 3, 3, new Color(250, 250, 140));
            viewPanel.setBorder(matte);
            add(viewPanel, BorderLayout.CENTER);
            this.setSize(StaticSwingUtils.sizeFrameForDefaultScreen(imageDim));
        } else {
            Application.getInstance().error("DisplayLive: Couldn't open stream.");
        }
    }

    private void rememberBounds() {
        Rectangle bounds = this.getBounds();
    }

    public void setStreamSource(StreamSource source) {
        this.source = source;
        viewPanel.setStreamingSource(source);
    }

    public StreamSource getStreamSource() {
        return source;
    }

    public ImageDisplayPanel getImageDisplayPanel() {
        return viewPanel;
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
//        viewPanel.setStreamingSource(gen.getStreamSource()); // <<<<<<<<<<<
//        gen.startStream();

        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {

                viewPanel.setStreamingSource(gen.getStreamSource()); // <<<<<<<<<<<


                gen.startStream();

            }

        });

    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
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
    }

    @Override
    public DockKey getDockKey() {
        return key;
    }

    @Override
    public Component getComponent() {
        return this;
    }

}
