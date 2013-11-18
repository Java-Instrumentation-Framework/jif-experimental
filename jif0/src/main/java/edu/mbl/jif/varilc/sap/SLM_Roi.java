/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc.sap;

import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author GBH
 */
public class SLM_Roi {

    // assuming image size 640 x 480...
    Point center = new Point(400, 300);  // pixel
    double radiusRetarders = 215; // pixels
    double radiusElliptical = 210; // pixels
    double radiusCircularSurround = 225;
    int roiSize = 16;
    
    // Color Code:
    // retarders: green
    // elliptical: blue
    // circular: red
    
    double angleRetarders = Math.PI;
    double angleElliptical = angleRetarders; // + Math.PI / 8.0;
    
    Point[] roiPointsRetarders = new Point[9];
    Point[] roiPointsElliptical = new Point[8];

    public Rectangle[] getRetarderROIs() {
        double angle = 0;
        for (int i = 0; i < 8; i++) {
            angle = angleRetarders + i * Math.PI / 4;
            int x = (int) (center.getX() + radiusRetarders * Math.cos(angle));
            int y = (int) (center.getY() + radiusRetarders * Math.sin(angle));
            roiPointsRetarders[i] = new Point(x, y);
        }
        //System.out.println("Retarder ROIs");
        Rectangle[] retardersROIs = new Rectangle[9];

        for (int i = 0; i < 8; i++) {
            Point centerPoint = roiPointsRetarders[i];
            //System.out.print("Center: " + (int) centerPoint.getX() + ", " + (int) centerPoint.getY() + ": ");
            retardersROIs[i] = new Rectangle(
                    (int) (centerPoint.getX() - roiSize / 2),
                    (int) (centerPoint.getY() - roiSize / 2),
                    (int) roiSize,
                    (int) roiSize);
            //System.out.println("[" + (int) retardersROIs[i].getX() + "," +
            //        (int) retardersROIs[i].getY() + "," + (int) retardersROIs[i].getWidth() + "," + (int) retardersROIs[i].getHeight() + "]");
        }
        // central sector
        retardersROIs[8] = new Rectangle(
                (int) (center.getX() - roiSize / 2),
                (int) (center.getY() - roiSize / 2),
                (int) roiSize,
                (int) roiSize);
//        System.out.println("[" + (int) retardersROIs[8].getX() + "," +
//                (int) retardersROIs[8].getY() + "," +
//                (int) retardersROIs[8].getWidth() + "," +
//                (int) retardersROIs[8].getHeight() + "]");
        return retardersROIs;
    }

    public Rectangle[] getEllipticalROIs() {
        double angle = 0;
        for (int i = 0; i < 8; i++) {
            angle = angleElliptical + i * Math.PI / 4;
            int x = (int) (center.getX() + radiusElliptical * Math.cos(angle));
            int y = (int) (center.getY() + radiusElliptical * Math.sin(angle));
            roiPointsElliptical[i] = new Point(x, y);
        }
        //System.out.println("Elliptical ROIs");
        Rectangle[] ellipticalROIs = new Rectangle[8];

        for (int i = 0; i < roiPointsElliptical.length; i++) {
            Point centerPoint = roiPointsElliptical[i];
            ellipticalROIs[i] = roiAt((int)centerPoint.getX(), (int) centerPoint.getY());
        }
        return ellipticalROIs;

    }
    
    public Rectangle roiAt(int x, int y) {
        return new Rectangle(
                    (int) (x - roiSize / 2),
                    (int) (y - roiSize / 2),
                    (int) roiSize,
                    (int) roiSize);
    }

    Rectangle[] getCircularROIs() {
        //System.out.println("Elliptical ROIs");
        Rectangle[] circularROIs = new Rectangle[2];
        // central sector
        circularROIs[0] = new Rectangle(
                (int) (center.getX() - roiSize / 2),
                (int) (center.getY() - roiSize / 2),
                (int) roiSize,
                (int) roiSize);
//        System.out.println("[" + (int) circularROIs[0].getX() + "," +
//                (int) circularROIs[0].getY() + "," +
//                (int) circularROIs[0].getWidth() + "," +
//                (int) circularROIs[0].getHeight() + "]");
        // surround sector
        
        circularROIs[1] = new Rectangle(
                (int) (center.getX() - roiSize / 2),
                (int) (center.getY() + this.radiusCircularSurround - roiSize / 2),
                (int) roiSize,
                (int) roiSize);
//        System.out.println("[" + (int) circularROIs[1].getX() + "," +
//                (int) circularROIs[1].getY() + "," +
//                (int) circularROIs[1].getWidth() + "," +
//                (int) circularROIs[1].getHeight() + "]");        
        return circularROIs;
    }
}
