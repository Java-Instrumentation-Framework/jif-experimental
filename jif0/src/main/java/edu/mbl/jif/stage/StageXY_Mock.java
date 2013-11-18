/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stage;

import edu.mbl.jif.camera.MockCamera;
import edu.mbl.jif.gui.spatial.Direction;
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
public class StageXY_Mock
        implements StageXYController, edu.mbl.jif.stage.DigitalOutput {

  private double centerY = 8.0;
  private double centerX = 8.0;
  private double limitMinX = 0.0;
  private double limitMaxX = 19.0;
  private double limitMinY = 0.0;
  private double limitMaxY = 19.0;
  private double multiplier = 0.001;
  private Point2D target = new Point2D.Double();
  private MockCamera mockCam;

  public StageXY_Mock() {
  }

  public void setMockCamera(MockCamera mockCam) {
    this.mockCam = mockCam;
  }

  @Override
  public boolean open() {
    reference();
    return true;
  }

  @Override
  public void reference() {
    getPos();
  }

  public void moveX(double pos) {
    try {
      setX(pos);
    } catch (Exception ex) {
      return;
    }
  }

  public void moveY(double pos) {
    try {
      setY(pos);
    } catch (Exception ex) {
      return;
    }
  }

  @Override
  public Point2D getPos() {
    // get current position
    final double posX = getX();
    final double posY = getY();
    System.out.println("StageXY> Pos: (" + posX + ", " + posY + ")");
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
    System.out.println("StageXY> moveTo: " + x + ", " + y);
    Point2D p = getPos();
    double distX = Math.abs(x - p.getX());
    double distY = Math.abs(y - p.getY());
    // calc velocity for both axis
    double dist = Math.sqrt(Math.pow(distX, 2.0) + Math.pow(distY, 2.0));
    double time = dist / getVelocity();
    double velX = distX / time;
    double velY = distY / time;
    System.out.printf(
            "StageXY> posX %.6f, posY %.6f, distX %.6f, distY %.6f, dist %.6f, time %.6f, velX %.6f, velY %.6f \n",
            p.getX(), p.getY(), distX, distY, dist, time, velX, velY);
    try {
      //ctrlX.setVelocity(velX);
      //ctrlY.setVelocity(velY);
      //ctrlX.setVelocity(getVelocity());
      //ctrlY.setVelocity(getVelocity());
      mockCam.setPosition(x, y);
      //moveX(x);
      //moveY(y);

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  xySimulator.moveTo(x,y);

    } catch (Exception ex) {
      return;
    }

    getPos();
  }

  public void generatePath(Point2D start, Point2D end, double stepSize) {
  }

//    @Override
//    public void moveRelative(int dX, int dY) {
//        System.out.println("StageXY> dx, dy: " + dX + ", " + dY);
//        moveTo(getX() + dX * multiplier, getY() + dY * multiplier);
//    //?? * increment
//    }
  public FutureTask<Boolean> moveRelative(int dX, int dY) {
    Mover mover = new Mover(getX() + dX * multiplier, getY() + dY * multiplier);
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

  public void continuousMove(Direction dir) {
    switch (dir) {
      case LEFT:
        //ctrlX.move(limitMaxX);
        break;
    }
  }

  @Override
  public void stop() {

    System.out.println("StageXY> Stopped.");
  }

  public void setTarget() {
    target.setLocation(getX(), getY());
    System.out.println("StageXY> Target set to: " + target);
  }

  public void returnToTarget() {
    moveTo(target.getX(), target.getY());
  }

  @Override
  public void moveToConstantVelocity(double x, double y) {
    try {
      moveX(x);
      moveY(y);
    } catch (Exception ex) {
      return;
    }
    getPos();
  }

  @Override
  public void setVelocityForMoveTo(double x, double y) {
  }

  @Override
  public void presetVelocity(Point2D begin, Point2D end) {
  }

  @Override
  public void returnToBkgdSite() {
  }

  @Override
  public void setBkgdSite() {
  }

  @Override
  public void close() {
    // 1st camera....
    try {
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
  }

  @Override
  public void setOutput(int channel, boolean hilow) {
  }

  @Override
  public void dioRepeat(int n, int period) {
  }

  @Override
  public void setCommPortX(int commPortX) {
  }

  @Override
  public void setCommPortY(int commPortY) {
  }
// </editor-fold>
}