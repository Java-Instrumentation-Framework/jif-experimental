//**********************************************************************************************
//		(C) Copyright 2002 by Dipl. Phys. Joerg Plewe, HARDCODE Development
//		All rights reserved. Copying, modification,
//		distribution or publication without the prior written
//		consent of the author is prohibited.
//
//	Created on 20. Februar 2002, 22:19
//**********************************************************************************************
package edu.mbl.jif.joystick.test;

import de.hardcode.jxinput.event.JXInputEventManager;
import de.hardcode.jxinput.event.JXInputAxisEventListener;
import de.hardcode.jxinput.event.JXInputAxisEvent;
import de.hardcode.jxinput.Axis;

/**
 * Example listener to an axis.
 *
 * @author Herkules
 */
public class AxisListener
        implements JXInputAxisEventListener {
    
    /**
     * Creates a new instance of AxisListener.
     */
    public AxisListener( Axis axis ) {
        JXInputEventManager.addListener( this, axis, 1000.1 );
    }
    
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double zRot = 0;
    
    public void changed( JXInputAxisEvent ev ) {
        System.out.println( "Axis " + ev.getAxis().getName() + 
                //" changed : value=" + ev.getAxis().getValue() + 
                ", delta=" + ev.getDelta() );
        if(ev.getAxis().getName().equalsIgnoreCase("X Axis")) {
            if(Math.abs(ev.getDelta()) < 10000) {
                x = x + ev.getDelta();
             System.out.println("x = " + x);   
            }
        }
        if(ev.getAxis().getName().equalsIgnoreCase("Y Axis")) {
            if(Math.abs(ev.getDelta()) < 10000) {
                y = y + ev.getDelta();
                System.out.println("y = " + y); 
            }
        }
        if(ev.getAxis().getName().equalsIgnoreCase("Z Axis")) {
            if(Math.abs(ev.getDelta()) < 10000) {
                z = z + ev.getDelta();
                             System.out.println("z = " + z); 
            }
        }
        if(ev.getAxis().getName().equalsIgnoreCase("Z Rotation")) {
            if(Math.abs(ev.getDelta()) < 100000) {
                zRot = zRot + ev.getDelta();
                             System.out.println("zRot = " + zRot); 
            }
        }
    }
    
    
    // System.out.println( ev.getAxis().getName() + ": " + ev.getAxis().getValue() + ", delta=" + ev.getDelta() );
    //ev.getAxis().getName() ev.getAxis().getValue() ev.getDelta();
}

