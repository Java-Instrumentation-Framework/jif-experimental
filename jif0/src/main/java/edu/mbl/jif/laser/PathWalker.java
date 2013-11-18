/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.laser;

import edu.mbl.jif.gui.spatial.Path;
import edu.mbl.jif.stage.StageXYController;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class PathWalker {

    // timeInterval in milliseconds
    
    StageXYController xyCtrl;
    Iterator<Point2D> points;
    PanelPath pathPanel;
    Path path;
    long timeInterval;
    Operation op;

    public PathWalker(StageXYController xyCtrl) {
        this.xyCtrl = xyCtrl;
    }

    public void walk(Path path, long timeInterval, Operation op, PanelPath pathPanel) {
        points = path.getPoints();
        this.path = path;
        this.timeInterval = timeInterval;
        this.op = op;
        this.pathPanel = pathPanel;
        PathWalkerTask pwTask = new PathWalkerTask();
        pathPanel.setPathWalkerTask(pwTask);
        pwTask.execute();

//        while (points.hasNext()) {
//            Point2D element = points.next();
//            System.out.printf("%.4f %.4f \n", element.getX(), element.getY());
//            xyCtrl.moveToConstantVelocity(element.getX(), element.getY());
//            if (op != null) {
//                op.perform();
//            }
//            try {
//                Thread.sleep(timeInterval);
//            } catch (InterruptedException ex) {
//            }
//
//        }
    }
 


   public class PathWalkerTask
            extends SwingWorker<Void, Point2D> {
        @Override
        protected Void doInBackground() {
            pathPanel.setButtonsEnabled(false);
            points = path.getPoints();
            while (!isCancelled() && points.hasNext()) {
                Point2D element = points.next();
                publish(element);
                //System.out.printf("%.4f %.4f \n", element.getX(), element.getY());
                xyCtrl.moveToConstantVelocity(element.getX(), element.getY());
                if (op != null) {
                    op.perform();
                }
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }

        @Override
        protected void process(List<Point2D> coord) {
            Point2D pair = coord.get(coord.size() - 1);
            pathPanel.setXYDuringPathwalk(pair);
        }

        @Override
        protected void done() {
            try {
                get();
                System.out.println("Path walk completed.");
                pathPanel.setButtonsEnabled(true);
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (CancellationException e) {
                System.out.println("Cancelled");
                pathPanel.setButtonsEnabled(true);
                return;
            }
        }
    

    }
}