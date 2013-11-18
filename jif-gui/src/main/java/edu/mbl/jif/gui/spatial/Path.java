package edu.mbl.jif.gui.spatial;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author GBH
 */
public class Path {

    ArrayList<Point2D> points = new ArrayList<Point2D>();

    public Path() {
    }

    public void addPoint(Point2D p) {
        points.add(p);
    }

    public Iterator<Point2D> getPoints() {
        return points.iterator();
    }

    public void listPoints() {
        Iterator<Point2D> points = getPoints();
        while (points.hasNext()) {
            Point2D element = points.next();
            System.out.printf("%.4f %.4f \n", element.getX(), element.getY());
        }
    }

    public static void main(String[] args) {
        Path path = new Path();
        path.addPoint(new Point2D.Double(0.1, 5.6));
        path.addPoint(new Point2D.Double(0.4, 0.9));
        path.addPoint(new Point2D.Double(-0.9, 2.3));
        path.listPoints();
    }

}
