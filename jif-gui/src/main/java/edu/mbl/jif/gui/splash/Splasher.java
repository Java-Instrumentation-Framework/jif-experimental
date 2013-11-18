package edu.mbl.jif.gui.splash;

import edu.mbl.jif.gui.*;
/*
 * @(#)Splasher.java  2.0  January 31, 2004
 *
 * Copyright (c) 2003-2004 Werner Randelshofer
 * Staldenmattweg 2, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * This software is in the public domain.
 */

/**
 *
 * @author  werni
 */
public class Splasher {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SplashWindow.splash(Splasher.class.getResource("splash.gif"));
        SplashWindow.invokeMain("edu.mbl.jif.gui.MyApplication", args);
        SplashWindow.disposeSplash();
    }

}
