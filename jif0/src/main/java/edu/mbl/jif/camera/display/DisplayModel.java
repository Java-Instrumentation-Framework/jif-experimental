package edu.mbl.jif.camera.display;

import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.workframe.AbstractApplicationController;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * 
 * @author GBH
 */
public class DisplayModel {

    AbstractApplicationController appCtrl;

    public DisplayModel() {
    }

    public DisplayModel(AbstractApplicationController appCtrl) {
        this.appCtrl = appCtrl;
    }

    // <editor-fold defaultstate="collapsed" desc=" Pixel Highlighting ">
    protected int fullPixelValue = 255;
    public static final String PROP_FULLPIXELVALUE = "fullPixelValue";

    public int getFullPixelValue() {
        return fullPixelValue;
    }

    public void setFullPixelValue(int fullPixelValue) {
        int oldFullPixelValue = this.fullPixelValue;
        this.fullPixelValue = fullPixelValue;
        propertyChangeSupport.firePropertyChange(PROP_FULLPIXELVALUE, oldFullPixelValue,
                fullPixelValue);
    }
    protected int emptyPixelValue = 0;
    public static final String PROP_EMPTYPIXELVALUE = "emptyPixelValue";

    public int getEmptyPixelValue() {
        return emptyPixelValue;
    }

    public void setEmptyPixelValue(int emptyPixelValue) {
        int oldEmptyPixelValue = this.emptyPixelValue;
        this.emptyPixelValue = emptyPixelValue;
        propertyChangeSupport.firePropertyChange(PROP_EMPTYPIXELVALUE, oldEmptyPixelValue,
                emptyPixelValue);
    }
    protected boolean showPixelsSaturated = false;
    public static final String PROP_SHOWPIXELSSATURATED = "showPixelsSaturated";

    public boolean isShowPixelsSaturated() {
        return showPixelsSaturated;
    }

    public void setShowPixelsSaturated(boolean showPixelsSaturated) {
        boolean oldShowPixelsSaturated = this.showPixelsSaturated;
        this.showPixelsSaturated = showPixelsSaturated;
        propertyChangeSupport.firePropertyChange(PROP_SHOWPIXELSSATURATED, oldShowPixelsSaturated,
                showPixelsSaturated);
    }
    protected boolean showPixelsZero = false;
    public static final String PROP_SHOWPIXELSZERO = "showPixelsZero";

    public boolean isShowPixelsZero() {
        return showPixelsZero;
    }

    public void setShowPixelsZero(boolean showPixelsZero) {
        boolean oldShowPixelsZero = this.showPixelsZero;
        this.showPixelsZero = showPixelsZero;
        propertyChangeSupport.firePropertyChange(PROP_SHOWPIXELSZERO, oldShowPixelsZero,
                showPixelsZero);
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Image Adjustments ">
    protected boolean equalizeHistogram = false;
    public static final String PROP_EQUALIZEHISTOGRAM = "equalizeHistogram";

    public boolean isEqualizeHistogram() {
        return equalizeHistogram;
    }

    public void setEqualizeHistogram(boolean equalizeHistogram) {
        boolean oldEqualizeHistogram = this.equalizeHistogram;
        this.equalizeHistogram = equalizeHistogram;
        propertyChangeSupport.firePropertyChange(PROP_EQUALIZEHISTOGRAM, oldEqualizeHistogram,
                equalizeHistogram);
    }
    protected int max = 255;
    public static final String PROP_MAX = "max";

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        int oldMax = this.max;
        this.max = max;
        propertyChangeSupport.firePropertyChange(PROP_MAX, oldMax, max);
    }
    protected int min = 0;
    public static final String PROP_MIN = "min";

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        int oldMin = this.min;
        this.min = min;
        propertyChangeSupport.firePropertyChange(PROP_MIN, oldMin, min);
    }
// </editor-fold>

    public void displayLiveOpen() {
        if (((InstrumentController) appCtrl).getDisplayLive() != null) {
            System.out.println("Display already open.");
            return;
        }
        System.out.println("Opening Live Display...");

        Runnable runner = new Runnable() {

            public void run() {
                DisplayLiveCamera disp =
                        new DisplayLiveCamera((StreamGenerator) ((CameraModel) ((InstrumentController) appCtrl).getModel("camera")).getCamera());
                //new StreamMonitorFPS((CameraModel) ctrl.getModel("camera"), disp.getStreamSource()); // for fps updates
                try {
                    // for fps updates
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DisplayModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (disp != null) {
                    ((InstrumentController) CamAcqJ.getInstance().getController()).setDisplayLive(disp);
                    disp.resume();
                }
            }
        };
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(runner);
        } else {
            runner.run();
        }
    }
// <editor-fold defaultstate="collapsed" desc=">>>--- PropertyChangeSupport ---<<<" >
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
// </editor-fold>
}
