/*
 * StreamMonitor.java
 *
 * Created on November 13, 2006, 10:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.camera;

import edu.mbl.jif.imaging.stream.StreamMonitor;
import edu.mbl.jif.imaging.stream.StreamSource;
import javax.swing.SwingUtilities;


/**
 * Watches a stream and reports the frames per second.
 * The image date itself is not made available.
 * @author GBH
 */
public class StreamMonitorFPS implements StreamMonitor {

    private CameraModel cameraModel;
    private StreamSource ss;
    private double period = 250;
    ;
    private long frames;

    /** Creates a new instance of StreamMonitor */
    public StreamMonitorFPS(CameraModel cameraModel, StreamSource ss) {
        this.cameraModel = cameraModel;
        this.ss = ss;
        ss.attachMonitor(this);
    }

    public void setUpdatePeriod(double period) { // in milliseconds
        this.period = period;
    }

    long last = System.nanoTime();
    long lastFrame = 0;

    public void update(long frame) {
        double dur = (System.nanoTime() - last) / 1000000;
        if (dur > period) {
            updateFPS(frame - lastFrame, dur);
            last = System.nanoTime();
            lastFrame = frame;
        }
    }

    private void updateFPS(long frames, double dur) {
        final double fps = 1000.0 * frames / period;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                cameraModel.setCurrentFPS(fps);
            }

        });

    }

}
