/*
 * SeeFloor.java
 *
 * Created on January 19, 2007, 5:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.camera;

import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import edu.mbl.jif.gui.spatial.PanelControlXY;
import edu.mbl.jif.gui.test.TestAppHarness;
import edu.mbl.jif.gui.spatial.DirectionalXYController;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This tests the SeeFloorViewControl, which uses PanelControlXY( DirectionalController ) {
 * @author GBH
 */
public class SeeFloor {

    private int xLimit;
    private int yLimit;
    private int windowW;
    private int windowH;
//    private int x0;       // origin position
//    private int y0;
    private int x;  // current
    private int y;
    private BufferedImage floor;
    FrameImageDisplay display;
    JLabel xValue = new JLabel("X");
    JLabel yValue = new JLabel("Y");

    /** Creates a new instance of SeeFloor */
    public SeeFloor() {
    }

    public SeeFloor(BufferedImage floorImage, FrameImageDisplay display) {
        this.display = display;
        this.floor = floorImage;
        setLimits(floorImage.getWidth(), floorImage.getHeight());
    }

    public void setLimits(int x, int y) {
        this.xLimit = x;
        this.yLimit = y;
    }

    public void setWindow(int w, int h) {
        windowW = w;
        windowH = h;
    // create BufferedImage --> ImageProducer
    }
//    public void setOrigin() {
//        x0 = x;
//        y0 = y;
//        x = x0 + windowW / 2;
//        y = y0 + windowH / 2;
//    }
    public Point setPosition(int x_, int y_) {
        // does not allow movement outside of limits
        x = x_;
        y = y_;
        if (x_ < 0) {
            x = 0;
        }
        if (y_ < 0) {
            y = 0;
        }
        if (x_ > xLimit) {
            x = xLimit - 1;
        }
        if (y_ > yLimit) {
            y = yLimit - 1;
        }        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {
                xValue.setText(String.valueOf(x));
                yValue.setText(String.valueOf(y));
            }

        });
        System.out.println(x + ", " + y);
        executeMoveTo(x, y);
        return new Point(x, y);
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public Point move(int mX, int mY) {
        int newX = x + mX;
        int newY = y + mY;
                        xValue.setText(String.valueOf(newX));
                yValue.setText(String.valueOf(newY));
        return setPosition(newX, newY);
    }

    private void executeMoveTo(final int x, final int y) {
        StaticSwingUtils.dispatchToEDT(new Runnable() {

            public void run() {
                xValue.setText(String.valueOf(x));
                yValue.setText(String.valueOf(y));
            }

        });
        if (floor != null) {
            display.changeImage(floor.getSubimage(x, y, windowW, windowH));
        }

    }

    public void test() {
        BufferedImage windowImage = ImageFactoryGrayScale.testImageByte();
        FrameImageDisplay fid = new FrameImageDisplay(windowImage, "SimuStage");
        BufferedImage bigImage = null;
        try {
            bigImage = ImageIO.read(new File("\\_dev\\testdata\\bigStageImage.tif"));
        } catch (Exception e) {
            System.out.println("Exception loading: bigStageImage");
            e.printStackTrace();
        }
        SeeFloor sf = new SeeFloor(bigImage, fid);
        sf.setWindow(windowImage.getWidth(), windowImage.getHeight());
        SeeFloorViewControl sfvCtrl = new SeeFloorViewControl(sf);
        PanelControlXY panelXY = new PanelControlXY();
        panelXY.setDirectionalXYController((DirectionalXYController)sfvCtrl);
        new TestAppHarness(fid);
        JFrame f = new JFrame();
        f.add(panelXY, BorderLayout.CENTER);
        JPanel posPanel = new JPanel();
        f.add(posPanel, BorderLayout.SOUTH);

        posPanel.setLayout(new BoxLayout(posPanel, BoxLayout.X_AXIS));
        posPanel.add(xValue);
        posPanel.add(Box.createHorizontalGlue());
        posPanel.add(yValue);
        f.pack();
        f.setVisible(true);

        //sf.setOrigin();
        System.out.println(sf.getPosition());
        System.out.println(sf.setPosition(100, 100));

    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
                (new SeeFloor()).test();


//        Utils.wait(600);
//
//        System.out.println(sf.move(-5, 30));
//        Utils.wait(600);
//        System.out.println(sf.move(200, 400));
//        Utils.wait(600);
//        System.out.println(sf.move(-100, -300));
//        Utils.wait(600);
//        System.out.println(sf.move(2, 4));
//        Utils.wait(600);
//        System.out.println(sf.move(-500, -600));

//    }

//        });
    }

}
