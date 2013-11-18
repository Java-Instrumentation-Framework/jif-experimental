/*
 * IntensityMeasurer.java
 *
 * Created on March 1, 2007, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.acq;

import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;

/**
 *
 * @author GBH
 */
public class IntensityMeasurer implements StreamGenerator {

    private int width;
    private int height;

    private StreamSource source;

    
    /** Creates a new instance of IntensityMeasurer */
    public IntensityMeasurer() {
    }

    public StreamSource getStreamSource() {
        return source;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
       return height;
    }

    public void startStream() {
    }

    public void stopStream() {
    }

    public boolean isStreaming() {
        return false;
    }

    public void closeStreamSource() {
    }

    public double getCurrentFPS() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
