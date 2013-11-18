/*
 * Copyright 2007 Phidgets Inc.  All rights reserved.
 */
package edu.mbl.jif.device;

//import com.phidgets.*;
//import com.phidgets.event.*;

// Phidgets ServoPhidget Control


public class ServoPhidget {
//    com.phidgets.ServoPhidget servo;
//    int numberServos;
//
//    public ServoPhidget() {
//        System.out.println(Phidget.getLibraryVersion());
//        try {
//            servo = new com.phidgets.ServoPhidget();
//
//            servo.addAttachListener(new AttachListener() {
//                    public void attached(AttachEvent ae) {
//                        System.out.println("attachment of " + ae);
//                    }
//                });
//            servo.addDetachListener(new DetachListener() {
//                    public void detached(DetachEvent ae) {
//                        System.out.println("detachment of " + ae);
//                    }
//                });
//            servo.addErrorListener(new ErrorListener() {
//                    public void error(ErrorEvent ee) {
//                        System.out.println("error event for " + ee);
//                    }
//                });
//            servo.addServoPositionChangeListener(new ServoPositionChangeListener() {
//                    public void servoPositionChanged(ServoPositionChangeEvent oe) {
//                        System.out.println(oe);
//                    }
//                });
//
//            servo.openAny();
//            System.out.println("waiting for Servo attachment...");
//            servo.waitForAttachment();
//            System.out.println("Serial: " + servo.getSerialNumber());
//            System.out.println("Servos: " + servo.getMotorCount());
//            numberServos = servo.getMotorCount();
//        } catch (PhidgetException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void setPosition(int motor, int position) {
//        try {
//            servo.setPosition(motor, position);
//        } catch (PhidgetException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void close() {
//        try {
//            servo.close();
//        } catch (PhidgetException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    
//    
//    public static final void main(String[] args) throws Exception {
//        ServoPhidget servo = new ServoPhidget();
//        servo.setPosition(0, 230);
//        servo.setPosition(1, 0);
//        servo.setPosition(2, 230);
//        servo.setPosition(3, 0);
//        Thread.sleep(1550);
//        servo.setPosition(0, 0);
//        servo.setPosition(1, 230);
//        servo.setPosition(2, 0);
//        servo.setPosition(3, 230);
//
//        Thread.sleep(1550);
//        servo.setPosition(0, 230);
//        servo.setPosition(1, 0);
//        servo.setPosition(2, 230);
//        servo.setPosition(3, 0);
//        Thread.sleep(1500);
//        for (int i = 0; i < 230; i++) {
//            servo.setPosition(0, i);
//            Thread.sleep(30);
//            //System.out.println("Position: " + servo.getPosition(0));
//        }
//
//        //System.out.println("Outputting events.  Input to stop.");
//        //System.in.read();
//        servo.setPosition(0, -23);
//        System.out.print("closing...");
//        servo.close();
//        servo = null;
//        System.out.println(" ok");
//    }
}
