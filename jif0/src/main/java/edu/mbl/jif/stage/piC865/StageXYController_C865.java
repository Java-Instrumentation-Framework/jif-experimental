/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stage.piC865;

import edu.mbl.jif.gui.spatial.Direction;
import edu.mbl.jif.stage.StageXYController;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author GBH
 */
public class StageXYController_C865
    implements StageXYController {

    private double centerY = 8.0;
    private double centerX = 8.0;
    private double limitMinX = 0.0;
    private double limitMaxX = 19.0;
    private double limitMinY = 0.0;
    private double limitMaxY = 19.0;
    private double multiplier = 0.001;
    private Point2D target = new Point2D.Double();
    private Point2D bkgdSite = new Point2D.Double();

    public StageXYController_C865() {
    }

    public C865_Controller ctrlX;
    public C865_Controller ctrlY;
    private int commPortX = 12;
    private int commPortY = 11;
    private boolean moving = false;

    public void setCommPortX(int commPortX) {
        this.commPortX = commPortX;
    }

    public void setCommPortY(int commPortY) {
        this.commPortY = commPortY;
    }

    @Override
    public boolean open() {
        // 1st camera...
        try {
            ctrlY = new C865_Controller(commPortY);
            ctrlX = new C865_Controller(commPortX);

            if (!ctrlY.open()) {
                System.err.println("C865> Failed to open ctrlY");
                return false;
            }
            System.out.println("C865> Y Axis opened and initialized.");
            if (!ctrlX.open()) {
                System.err.println("C865> Failed to open ctrlX");
                return false;
            }
            System.out.println("C865> X Axis opened and initialized.");
            reference();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public void reference() {
        // initial velocity
        System.out.println("C865>  Referencing...");
        try {
            ctrlX.reference();
            ctrlY.reference();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            System.out.println("C865>  Referencing complete.");
            // sets default velocities to 3 mm/sec.
            this.setVelocity(1.0);
        } catch (Exception ex) {
            return;
        }
        getPos();
    }

    public void moveX(double pos) {
        try {
            ctrlX.move(pos);
        } catch (Exception ex) {
            return;
        }
    }

    public void moveY(double pos) {
        try {
            ctrlY.move(pos);
        } catch (Exception ex) {
            return;
        }
    }

    @Override
    public Point2D getPos() {
        // get current position
        double posX = ctrlX.getPosition();
        double posY = ctrlY.getPosition();
        System.out.println("C865> Pos: (" + posX + ", " + posY + ")");
        Point2D pos = new Point2D.Double();
        pos.setLocation(posX, posY);
        setX(posX);
        setY(posY);
        return pos;
    }

    @Override
    public void goHome() {
        moveTo(centerX, centerY);
    }

    @Override
    public void moveTo(double x, double y) {
        // Moves to point x,y at the currently set target velocity
        System.out.println("moveTo: " + x + ", " + y);
        setVelocityForMoveTo(x, y);
        try {
            ctrlX.move(x);
            ctrlY.move(y);
        } catch (Exception ex) {
            return;
        }
        while (ctrlX.isMoving() || ctrlY.isMoving()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
        getPos();
    }

    @Override
    public void setVelocityForMoveTo(double x, double y) {
        // Moves to point x,y at the currently set target velocity
        Point2D p = getPos();
        double distX = Math.abs(x - p.getX());
        double distY = Math.abs(y - p.getY());
        // calc velocity for both axis
        double dist = Math.sqrt(Math.pow(distX, 2.0) + Math.pow(distY, 2.0));
        double time = dist / getVelocity();
        double velX = distX / time;
        double velY = distY / time;
//        System.out.printf("posX %.6f, posY %.6f, distX %.6f, distY %.6f, dist %.6f, time %.6f, velX %.6f, velY %.6f \n",
//                p.getX(), p.getY(), distX, distY, dist, time, velX, velY);
        try {
            ctrlX.setVelocity(velX);
            ctrlY.setVelocity(velY);
        } catch (Exception ex) {
            return;
        }
    }
    //=========================================
    @Override
    public void presetVelocity(Point2D begin, Point2D end) {
        double distX = end.getX() - begin.getX();
        double distY = end.getY() - begin.getY();
        // calc velocity for both axis
        double dist = Math.sqrt(Math.pow(distX, 2.0) + Math.pow(distY, 2.0));
        double time = dist / getVelocity();
        double velX = distX / time;
        double velY = distY / time;
//        System.out.printf("posX %.6f, posY %.6f, distX %.6f, distY %.6f, dist %.6f, time %.6f, velX %.6f, velY %.6f \n",
//                p.getX(), p.getY(), distX, distY, dist, time, velX, velY);
        System.out.printf("velX %.6f, velY %.6f \n", velX, velY);
        try {
            ctrlX.setVelocity(velX);
            ctrlY.setVelocity(velY);
        } catch (Exception ex) {
            return;
        }
    }

    @Override
    public void moveToConstantVelocity(double x, double y) {
        // Moves to point x,y at the currently set target velocity
        System.out.println("moveTo: " + x + ", " + y);
        try {
            ctrlX.move(x);
            ctrlY.move(y);
        } catch (Exception ex) {
            return;
        }
        while (ctrlX.isMoving() || ctrlY.isMoving()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        getPos();
    }
    //==================================    
    public FutureTask<Boolean> moveRelative(int dX, int dY) {
        Mover mover = new Mover( getX() + dX * multiplier, getY() + dY * multiplier);
        FutureTask<Boolean> task = new FutureTask<Boolean>(mover);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(task);
        System.out.println("\nPerforming the Task... \n");
        return task;
    }

//    @Override
//    public void moveRelative(int dX, int dY) {
//        //System.out.println("dx, dy: " + dX + ", " + dY);
//        moveTo(getX() + dX * multiplier, getY() + dY * multiplier);
//    }
//        Boolean result = false;
//        Mover mover = new Mover((int)(getX() + dX * multiplier), (int)(getY() + dY * multiplier));
//        FutureTask<Boolean> task = new FutureTask<Boolean>(mover);
//        ExecutorService es = Executors.newSingleThreadExecutor();
//        es.submit(task);
//        //thd = new Thread(task);
//        //thd.start();
//        System.out.println("\nPerforming the Task, (please wait) ... \n");
//        try {
////         result = task.get();  // run until complete
//            result = task.get(1000, TimeUnit.MILLISECONDS); // timeout in 5 seconds
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            result = false;
//        } catch (TimeoutException e) {
//            result = false;
//        }
//        System.out.println(result);        
//    }
    public boolean moveRelativeWait(int dX, int dY, int limit) {
        boolean result = false;
        FutureTask<Boolean> task = moveRelative(dX, dY);
        try {
//         result = task.get();  // run until complete
            result = task.get(limit, TimeUnit.MILLISECONDS); // timeout in 5 seconds
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            result = false;
        } catch (TimeoutException e) {
            result = false;
        }
        return result;
    }

    class Mover implements Callable {
        double x, y;
        public Mover(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Boolean call() throws Exception {
            moveTo(x, y);
            return true;
        }
    }
    // A test...
    public void continuousMove(Direction dir) {
        switch (dir) {
            case LEFT:
                ctrlX.move(limitMaxX);
                break;
        }
    }

    @Override
    public void stop() {
        ctrlX.stop();
        ctrlY.stop();
        System.out.println("C865> Stopped.");
    }

    public void setTarget() {
        target.setLocation(getX(), getY());
        System.out.println("Target set to: " + target);
    }

    public void returnToTarget() {
        moveTo(target.getX(), target.getY());
    }

    public void setBkgdSite() {
        bkgdSite.setLocation(getX(), getY());
        System.out.println("bkgdSite set to: " + target);
    }

    public void returnToBkgdSite() {
        moveTo(bkgdSite.getX(), bkgdSite.getY());
    }

    @Override
    public void close() {
        // 1st camera....
        try {
            ctrlX.close();
            ctrlY.close();
        } catch (Exception ex) {
            return;
        }
    }
    // <editor-fold defaultstate="collapsed" desc=">>>--- Properties ---<<<" >
    private double x = 8.0;
    public static final String PROP_X = "x";
    private double y = 8.0;
    public static final String PROP_Y = "y";

    public double getX() {
        return x;
    }

    public void setX(double x) {
        double oldX = this.x;
        this.x = x;
        propertyChangeSupport.firePropertyChange(PROP_X, oldX, x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        double oldY = this.y;
        this.y = y;
        propertyChangeSupport.firePropertyChange(PROP_Y, oldY, y);
    }

    protected double increment = 1.0;
    public static final String PROP_INCREMENT = "increment";

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        double oldIncrement = this.increment;
        this.increment = increment;
        setMultiplier(increment * 0.001);
        propertyChangeSupport.firePropertyChange(PROP_INCREMENT, oldIncrement, increment);
    }

    
    public double getMultiplier() {
        return this.multiplier;
    }
    
    public void setMultiplier(double mult) {
        this.multiplier = mult;
    }

    protected double velocity = 1.0;
    public static final String PROP_VELOCITY = "velocity";

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        double oldVelocity = this.velocity;
        this.velocity = velocity;
        propertyChangeSupport.firePropertyChange(PROP_VELOCITY, oldVelocity, velocity);
    }
    //======
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }// </editor-fold>

}