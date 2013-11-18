package application;
/*
 * ApplicationListener.java
 *
 * Created on January 26, 2006, 7:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.EventListener;

/**
 * ApplicationListener is used by Application to notify listeners of major
 * events during the lifecycle of an Application.
 *
 * @author sky
 */
public interface ApplicationListener extends EventListener {
    /**
     * Invoked after the application has finished intializing.
     */
    public void applicationDidInit();

    /**
     * Invoked to determine if the application should exit. A return value
     * of false will stop the application from exiting.
     *
     * @return true if the application should be allowed to exit, otherwise
     *         false
     */
    public boolean canApplicationExit();

    /**
     * Notification that the application is about to exit.
     */
    public void applicationExiting();
}

