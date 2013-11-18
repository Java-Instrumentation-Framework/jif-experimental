/*
 * Driver.java
 *
 * Created on March 24, 2006, 5:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.camera;

import java.util.ArrayList;

/**
 *
 * @author GBH
 */
public abstract class Driver {

    public Driver() {}

    public static final int MAX_CAMERAS = 10;

    public static boolean driverLoaded = false;
    //Camera[] camera = new Camera[MAX_CAMERAS];

    public abstract ArrayList getCameraList();

    public abstract boolean load();

    public abstract void unLoad();

}
