package edu.mbl.jif.ps;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class ArcPoints extends JPanel {
    final int NUM_POINTS = 10;
    Arc2D.Double arc = new Arc2D.Double(100, 40, 250, 300,
                                        135, 90, Arc2D.OPEN);

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.blue);
        g2.draw(arc);
        g2.setPaint(Color.red);
        Point2D.Double[] p = getPoints();
        for(int j = 0; j < p.length; j++) {
            if(p[j] == null) continue;  // just in case
            g2.fill(new Ellipse2D.Double(p[j].x-2, p[j].y-2, 4, 4));
        }
    }

    private Point2D.Double[] getPoints() {
        Point2D.Double[] points = new Point2D.Double[NUM_POINTS];
        double arcLength = getLength();
        double segLength = arcLength/(NUM_POINTS-1);
        System.out.printf("arcLength = %.1f  segLength = %.1f%n",
                           arcLength, segLength);
        // Traverse the arc and create a new point at the
        // beginning and at every segLength along it.
        // totalDeviation in traverse is affected by flatness value.
        double flatness = 0.0001;
        PathIterator pit = arc.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0, totalLength = 0;
        double totalDeviation = 0;
        int count = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    points[count++] = new Point2D.Double(lastX, lastY);
                    break;
                case PathIterator.SEG_LINETO:
                    double distance = Point2D.distance(lastX, lastY,
                                                       coords[0], coords[1]);
                    if(totalLength + distance > segLength) {  // found a minimum
                        points[count++] = new Point2D.Double(lastX, lastY);
                        double deviation = segLength - totalLength;
                        System.out.printf("count = %2d  totalLength = %.2f  " +
                                          "deviation = %.2f%n",
                                           count, totalLength, deviation);
                        totalDeviation += deviation;
                        totalLength = 0;
                    }
                    totalLength += distance;
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                default:
                    System.out.println("unexpected type: " + type);
            }
            pit.next();
        }
        System.out.printf("totalDeviation = %f for flatness = %f%n",
                           totalDeviation, flatness);
        return points;
    }

    private double getLength() {
        double flatness = 0.01;
        PathIterator pit = arc.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0, arcLength = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    arcLength += Point2D.distance(lastX, lastY,
                                                  coords[0], coords[1]);
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                default:
                    System.out.println("unexpected type: " + type);
            }
            pit.next();
        }
        return arcLength;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new ArcPoints());
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}