package edu.mbl.jif.varilc.sap;

import com.jgoodies.binding.beans.Model;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.gui.imaging.GraphicOverlay;
import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import edu.mbl.jif.utils.FileUtil;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.SwingWorker;

/**
 *
 * @author GBH
 */
public class SLM_Main
        extends Model {

    SLMController slmCtrl;
    SLM_Roi slmROI;
    SLM_Calibrator calibrator;
    InstrumentController instrumentCtrl;
    public SLMModel slmModel;

    Rectangle[] retarderROIs;
    Rectangle[] ellipticalROIs;
    Rectangle[] circularROIs;



    public SLM_Main(InstrumentController instrumentCtrl) {
        this.instrumentCtrl = instrumentCtrl;
        slmCtrl = new SLMController();
        slmModel = new SLMModel();
        initialize();
    }


    public SLMController getController() {
        return slmCtrl;
    }

    // <editor-fold defaultstate="collapsed" desc=">>>--- Preparation ---<<<" >
    public void initialize() {
        slmCtrl.openConnection();
        slmCtrl.initializeController();
        slmROI = new SLM_Roi();
        retarderROIs = slmROI.getRetarderROIs();
        ellipticalROIs = slmROI.getEllipticalROIs();
        circularROIs = slmROI.getCircularROIs();

        calibrator = new SLM_Calibrator(slmModel, slmCtrl, slmROI, instrumentCtrl);
       // org.pf.joi.Inspector.inspect(slmCtrl.settings);
       // org.pf.joi.Inspector.inspect(slmCtrl.pixelValues);
        slmCtrl.init();
                //.defineFrames();
    }


    public void exerciseAll() {
        slmCtrl.exercise(2, 30000);
    }


    public void measureROIsRetarders() {
        calibrator.setup(Layer.retarderA);
        calibrator.measureROIs();
    }
    
    public void runResponseCurves(Layer layer) {
        calibrator.setup(layer);
        calibrator.responseCurves();
    }


    public void runCalibrationFull() {
        this.doCalibrateMasks();
        this.doCalibrateRetarders();
        this.saveSettings();

    }

    
    public void findExtinction() {
        calibrator.findExtinctionOnly();
       
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=">>>--- Mask Calibration ---<<<" >
    public void doCalibrateMasks() {
        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {

            @Override
            protected Integer doInBackground() {
                calibrator.calibrateMasks();

                return 0;
            }


            @Override
            protected void done() {
            }


        };
        worker.execute();
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- Retarder Calibration ---<<<" >
    // >>>>>>>>>>>>>>>>>>>>>>>> Retarder Calibration
    public void doCalibrateRetarders() {
        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {

            @Override
            protected Integer doInBackground() {
                calibrator.calibrateRetarders();
                return 0;
            }


            @Override
            protected void done() {
                System.out.println("doCalibrateRetarders done()");
                calibrator.displayMeasuredIntensities();
            }


        };
        worker.execute();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- ShowROI ---<<<" >

    //    public ImageStatistics[] measureROIsIn(byte[] data, Rectangle[] rois) {
//        ImageStatistics[] stats = new ImageStatistics[rois.length];
//        for (int i = 0; i < rois.length; i++) {
//            stats[i] = ImageAnalyzer.getStatsOnlyROI(data,
//                    rois[i], new Dimension((int) imageW, (int) imageH));
//        }
//        return stats;
//    }
    
    public void showROIs() {
        DisplayLiveCamera fid = this.instrumentCtrl.getDisplayLive();
        GraphicOverlay overlay1 = new GraphicOverlay() {

            public void drawGraphicOverlay(ZoomGraphics zg) {
                zg.setColor(Color.green);
                for (int i = 0; i < retarderROIs.length; i++) {
                    drawArect(zg,
                            retarderROIs[i]);
                }
            }


            public void drawArect(ZoomGraphics zg,
                                   Rectangle rect) {
                zg.drawRect(rect.getX(),
                        rect.getY(),
                        rect.getWidth(),
                        rect.getHeight());
            }


        };

        fid.getImageDisplayPanel().imagePane.addGraphicOverlay(overlay1);

        GraphicOverlay overlay2 = new GraphicOverlay() {

            public void drawGraphicOverlay(ZoomGraphics zg) {
                zg.setColor(Color.blue);
                for (int i = 0; i < ellipticalROIs.length; i++) {
                    drawArect(zg,
                            ellipticalROIs[i]);
                }
            }


            public void drawArect(ZoomGraphics zg,
                                   Rectangle rect) {
                zg.drawRect(rect.getX(),
                        rect.getY(),
                        rect.getWidth(),
                        rect.getHeight());
            }


        };

        fid.getImageDisplayPanel().imagePane.addGraphicOverlay(overlay2);

        GraphicOverlay overlay3 = new GraphicOverlay() {

            public void drawGraphicOverlay(ZoomGraphics zg) {
                zg.setColor(Color.red);
                for (int i = 0; i < circularROIs.length; i++) {
                    drawArect(zg,
                            circularROIs[i]);
                }
            }


            public void drawArect(ZoomGraphics zg,
                                   Rectangle rect) {
                zg.drawRect(rect.getX(),
                        rect.getY(),
                        rect.getWidth(),
                        rect.getHeight());
            }


        };
        fid.getImageDisplayPanel().imagePane.addGraphicOverlay(overlay3);
    }

    //    public void testShowROIsAndMeasure() {
//        // Test Image data
//        float max = 256f;
//        int len = imageW * imageH;
//        byte[] data = new byte[len];
//        double scale = max / (float) len;
//        for (int i = 0; i < len; i++) {
//            data[i] = (byte) ((float) i * scale);
//        }
//        BufferedImage image = ImageFactoryGrayScale.createImage(imageW, imageH, 8, data);
//        fid = new FrameImageDisplay(image, "byte");
//        new TestAppHarness(fid);
//        showROIs();
//        ImageStatistics[] stats = measureROIsIn(data, retarderROIs);
//
//        for (int i = 0; i < stats.length; i++) {
//            ImageStatistics imageStatistics = stats[i];
//            System.out.println(imageStatistics.meanInROI);
//        }
//    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- Save/Restore Settings ---<<<" >
    void restoreSettings() {
        this.restoreSettings(FileUtil.getcwd() + "/SLMsettings.xml");
    }


    void saveSettings() {
        this.saveSettings(FileUtil.getcwd() + "/SLMsettings.xml");
    }


    public void saveSettings(String filename) {
        slmCtrl.saveSettings(filename);
    }


    public void restoreSettings(String filename) {
        slmCtrl.restoreSettings(filename);
    }
    // </editor-fold>

}
