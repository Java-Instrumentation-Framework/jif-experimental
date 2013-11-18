/*
 * LFViewController.java
 */
package edu.mbl.jif.lightfield;

import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.gui.imaging.DisplayLiveStream;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.gui.spatial.DirectionalXYController;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;


/**
 *
 * @author GBH
 */
public class LFViewController implements DirectionalXYController {
    double currentX   = 0;
    double currentY   = 0;
    double top        = 0;
    double bottom     = 0;
    double limitLeft  = 0;
    double limitRight = 0;
    
    private int xOffset = 8;
    private int yOffset = 8;
    private int pitch = 17;
    boolean interpolate = true;
    
    //FrameImageDisplay f;
    LightFieldView lfv;
    LFModel lFModel;
    InstrumentController instCtrl;
    
    public LFViewController(LFModel lFModel, InstrumentController instCtrl) {
        this.lFModel = lFModel;
        this.instCtrl = instCtrl;
        
    }
    
    /** Creates a new instance of LFViewController */
    public LFViewController(String sourceFilename) {
        this.lfv = new LightFieldView(sourceFilename, pitch);
        openFrame();
    }
    
    public LFViewController(byte[] sourceArray, int w, int h) {
        this.lfv = new LightFieldView(sourceArray, w, h, pitch);
        openFrame();
    }
    
    public void openFrame() {
//        lfv.setOffset(xOffset, yOffset);
//        PanelLF plf = new PanelLF(this);
//        plf.addArrowPanel( new PanelControlXY(this));
//        plf.validate();
//        f = new FrameImageDisplay(this.getViewImage());
//        f.add(plf, BorderLayout.NORTH);
//        f.pack();
//        f.setVisible(true);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public int goUp(int n) {
        //System.out.println("<Up>");
        lFModel.setDisplacementY(lFModel.getDisplacementY()+1);
        return 0;
    }
    
    
    public int goDown(int n) {
        //System.out.println("<Down>");
        lFModel.setDisplacementY(lFModel.getDisplacementY()-1);
        return 0;
    }
    
    
    public int goLeft(int n) {
        //System.out.println("<Left>");
        lFModel.setDisplacementX(lFModel.getDisplacementX()+1);
        return 0;
    }
    
    
    public int goRight(int n) {
        //    System.out.println("<Right>");
        lFModel.setDisplacementX(lFModel.getDisplacementX()-1);
        return 0;
    }
    
    
    public int goCenter(int n) {
        //   System.out.println("<Center>");
        lFModel.setDisplacementX(0);
        lFModel.setDisplacementY(0);
        return 0;
    }
    
    
    public int goTop(int n) {
        return 0;
    }
    
    
    public int goBottom(int n) {
        return 0;
    }
    
    
    public int goLeftLimit(int n) {
        return 0;
    }
    
    
    public int goRightLimit(int n) {
        return 0;
    }
    
    
    public int goUpRight(int n) {
        return 0;
    }
    
    public int goDownRight(int n) {
        return 0;
    }
    
    public int goUpLeft(int n) {
        return 0;
    }
    
    public int goDownLeft(int n) {
        return 0;
    }
    //--------------------------------------
    
//    public void setOffsetX(int xOffset) {
//        if(xOffset<=0) return;
//        this.xOffset = xOffset;
//        System.out.println("xOffset set to " + xOffset);
//        lfv.setOffset(xOffset, yOffset);
//        update();
//    }
//
//    public void setOffsetY(int yOffset) {
//        if(yOffset<=0) return;
//        this.yOffset = yOffset;
//        System.out.println("yOffset set to " + yOffset);
//        lfv.setOffset(xOffset, yOffset);
//        update();
//    }
//
//    public void setDiameter(int diameter) {
//        this.pitch = diameter;
//        System.out.println("diameter set to " + diameter);
//    }
    
    //---------------------------------------------------------
    public void updateView() {
        lfv.updateView();
    }
    
    
    private BufferedImage getViewImage() {
        return lfv.getViewImage();
    }
    
    
    void update() {
        lfv.perspective(currentX, currentY);
        //f.changeImage(getViewImage());
        
    }
    
    public void  createStreamingFilter() {
        
    }
    
    
    public  DisplayLiveStream lfView = null;
    
    public void openLightviewPerspective() {
        if (lfView != null) {
            synchronized (lfView) {
                lfView.close();
                lfView = null;
            }
        }
        final String title = "lfView";
        CameraModel cameraModel =  (CameraModel)instCtrl.getModel("camera");
        final int width = cameraModel.getWidth() / lFModel.getPitchInt();
        final int height = cameraModel.getHeight() / lFModel.getPitchInt();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass()
                                                             .getResource("/edu/mbl/jif/camera/icons/lightfield16.png"));
                DisplayLiveStream lfView = new DisplayLiveStream(title, new LightfieldPerspectiveFilteredSource(lFModel, instCtrl), width, height, icon);
                lfView.setSize(300, 300);
//                DisplayLiveCamera disp = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
//                if(disp!=null) {
//                    disp.resume();
//                } else {
//                    ((StreamGenerator)instCtrl.getCamera()).startStream();
//                }
            }
        });
    }
    
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                String sourceFilename = "C:\\_dev\\testdata\\lightfield\\SourceInt.tif";
                LFViewController lfvc = new LFViewController(sourceFilename);
                
            }
        });
    }

    
}
