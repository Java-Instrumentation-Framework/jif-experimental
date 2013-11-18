/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stage;

import java.awt.geom.Point2D;
import java.util.concurrent.FutureTask;

/**
 *
 * @author GBH
 */
public interface StageXYController {

    void goHome();
    
    void setMultiplier(double mult);
    
    double getMultiplier();

    FutureTask<Boolean> moveRelative(int dX, int dY);
    
    boolean moveRelativeWait(int dX, int dY, int limit);

    void moveToConstantVelocity(double x, double y);

    void close();

    Point2D getPos();

    double getX();

    double getY();

    void stop();

    boolean open();

    void reference();

    void setVelocity(double targetVel);

    double getVelocity();

    void setVelocityForMoveTo(double x, double y);

    void presetVelocity(Point2D begin, Point2D end);

    void moveTo(double x, double y);
    
    void setTarget();

    void returnToTarget();

    public void returnToBkgdSite();

    public void setBkgdSite();

    public void setCommPortX(int commPortX);

    public void setCommPortY(int commPortY);

}


