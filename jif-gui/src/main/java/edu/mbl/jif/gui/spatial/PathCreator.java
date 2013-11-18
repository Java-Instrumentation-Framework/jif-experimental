package edu.mbl.jif.gui.spatial;

import java.awt.geom.Point2D;

/**
 *
 * @author GBH
 */
public class PathCreator {

    public static Path straightLine(Point2D begin, Point2D end, double stepSize) {

        Path path = new Path();
        double distX = end.getX() - begin.getX();
        double distY = end.getY() - begin.getY();
        double dist = Math.sqrt(Math.pow(distX, 2.0) + Math.pow(distY, 2.0));
        long numSteps = Math.round(dist / stepSize);
        double stepX = distX / numSteps;
        double stepY = distY / numSteps;
        System.out.printf("distX %.6f, distY %.6f, dist %.6f, numSteps %03d, stepX %.6f, stepY %.6f \n",
                distX, distY, dist, numSteps, stepX, stepY);
        for (int i = 0; i < numSteps+1; i++) {
             double x = begin.getX() + i * stepX;
             double y = begin.getY()+ i * stepY;
             path.addPoint(new Point2D.Double(x, y));
        }
        return path;
    }
    
    
    public static Path straightLine(Point2D begin, double length, double angle, double stepSize) {
        double endX = 0;
        double endY = 0;
        return straightLine(begin, (new Point2D.Double(endX, endY)), stepSize);
        
    }
    
    
    public static void main(String[] args) {
        Path p = PathCreator.straightLine(new Point2D.Double(0,0), new Point2D.Double(1,1), 0.1);
        p.listPoints();
        Path p2 = PathCreator.straightLine(new Point2D.Double(.01,0), new Point2D.Double(3.2,9.1), 0.1);
        p2.listPoints();
    }
}
