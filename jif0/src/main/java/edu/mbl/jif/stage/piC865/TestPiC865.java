/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stage.piC865;

import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.utils.diag.edt.ThreadCheckingRepaintManager;

//import edu.mbl.jif.script.jython.JythonConsole;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

public class TestPiC865 {

    JPanel buttons = new JPanel();
    private double targetVel;

    public TestPiC865() {
        //      Thread.UncaughtExceptionHandler handler =
        //            new StackWindow("Show Exception Stack", 400, 200);
        //      Thread.setDefaultUncaughtExceptionHandler(handler);
        FrameForTest f = new FrameForTest();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        addButton("Open",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        
                        StaticSwingUtils.dispatchToEDT(new Runnable() {

                            public void run() {

                                open();

                            }

                        });
                    }

                });

        addButton("Close",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        close();
                    }

                });
        addButton("Reference",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        reference();
                    }

                });
        addButton("Vel 0.1",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ctrlX.setVelocity(0.1);
                        ctrlY.setVelocity(0.1);
                    }

                });
        addButton("Vel 1",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ctrlX.setVelocity(1.0);
                        ctrlY.setVelocity(1.0);
                    }

                });
        addButton("Vel 3",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ctrlX.setVelocity(3.0);
                        ctrlY.setVelocity(3.0);
                    }

                });
        addButton("X Move 5",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveX(5.0);
                    }

                });
        addButton("X Move 10",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveX(10.0);
                    }

                });
        addButton("X Move 15",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveX(15.0);
                    }

                });

        addButton("Y Move 5",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveY(5.0);
                    }

                });
        addButton("Y Move 10",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveY(10.0);
                    }

                });
        addButton("Y Move 15",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveY(15.0);
                    }

                });

        addButton("GetPos",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        getPos();
                    }

                });
        addButton("dVEL while Mov",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        changeVelocityWhileMoving(15.0);
                    }

                });
        addButton("Move and Stop",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        moveAndStop();

                    }

                });

        addButton("Monitor",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        monitor();
                    }

                });
        addButton("DIO High",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dio(true);
                    }

                });
        addButton("DIO Low",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dio(false);
                    }

                });

        addButton("DIO Repeat",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dioRepeat(20, 100);
                    }

                });
        addButton("ImageJ",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        // TODO edu.mbl.jif.imagej.IJMaker.openImageJ();
                    }

                });
        addButton("Jython",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        openJythonConsole();
                    }

                });
        addButton("Inspect",
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        inspectCam();
                    }

                });
        f.add(buttons);
        f.setLocation(50, 50);
        f.pack();
        f.setVisible(true);
        //
        RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));

        System.out.println("Initialize... ");

    }

    private void addButton(String label, ActionListener action) {
        Button b = new Button(label);
        if (action != null) {
            b.addActionListener(action);
        }
        buttons.add(b);
    }

    //--------------------------------------------------------------------
    C865_Controller ctrlX;
    C865_Controller ctrlY;

    public void open() {
        // 1st camera...
        try {
            ctrlX = new C865_Controller(7);
            ctrlY = new C865_Controller(6);
            if (!ctrlX.open()) {
                System.err.println("Failed to open ctrlX");
                return;
            }
            System.out.println("X Axis opened and initialized.");
            if (!ctrlY.open()) {
                System.err.println("Failed to open ctrlY");
                return;
            }
            System.out.println("Y Axis opened and initialized.");
        } catch (Exception ex) {
            return;
        }
    }

    public void reference() {
        // 1st camera...
        try {
            ctrlX.reference();
            ctrlY.reference();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
            }
            System.out.println(" Referencing complete.");
            // sets default velocities to 3 mm/sec.
            ctrlX.setVelocity(3.0);
            ctrlY.setVelocity(3.0);
            this.setTaregtVelocity(3.0);
        } catch (Exception ex) {
            return;
        }
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

    private void getPos() {
        // get current position
        double posX = ctrlX.getPosition();
        double posY = ctrlY.getPosition();
        System.out.println("Pos: (" + posX + ", " + posY + ")");
    }

    public void moveTo(double x, double y) {
        // Moves to point x,y at the currently set target velocity
        double posX = ctrlX.getPosition();
        double posY = ctrlY.getPosition();
        double distX = Math.abs(x - posX);
        double distY = Math.abs(y - posY);
        // calc velocity for both axis
        double dist = Math.sqrt(Math.pow(distX, 2.0) + Math.pow(distY, 2.0));
        double time = dist / targetVel;
        double velX = distX / time;
        double velY = distY / time;
        System.out.printf("posX %.6f, posY %.6f, distX %.6f, distY %.6f, dist %.6f, time %.6f, velX %.6f, velY %.6f \n",
                posX, posY, distX, distY, dist, time, velX, velY);
        try {
            ctrlX.setVelocity(velX);
            ctrlY.setVelocity(velY);
            ctrlX.move(x);
            System.out.println("MoveX done.");
            ctrlY.move(y);
            System.out.println("MoveY done.");
        } catch (Exception ex) {
            return;
        }
    }

    public void monitor() {
        // Tests to see that position can be query during a move.
        moveTo(1.0, 10);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        getPos();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        getPos();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        getPos();
    }

    private void dio(boolean on) {
        ctrlY.digitalOutput(on);
    }

    private void dioRepeat(final int n, final int milliseconds) {
        ctrlY.dioRepeat(n, milliseconds);
//        Runnable runnable = new Runnable() {
//
//            public void run() {
//                for (int i = 0; i < 1000; i++) {
//                    ctrlY.digitalOutput(true);
//                    try {
//                        Thread.sleep(milliseconds);
//                    } catch (InterruptedException ex) {
//                    }
//                    ctrlY.digitalOutput(false);
//                    try {
//                        Thread.sleep(milliseconds);
//                    } catch (InterruptedException ex) {
//                    }
//                }
//            }
//
//        };
//        runnable.run();
    }

    public void close() {
        // 1st camera....
        try {
            ctrlX.close();
            ctrlY.close();
        } catch (Exception ex) {
            return;
        }
    }

    public void setTaregtVelocity(double targetVel) {
        this.targetVel = targetVel;
    }

    private void changeVelocityWhileMoving(double d) {
        ctrlX.setVelocity(1.0);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        moveX(0.001);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        moveX(18.00);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        ctrlX.setVelocity(4.0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        ctrlX.setVelocity(1.0);
    }

    private void moveAndStop() {
        setTaregtVelocity(1.0);
        moveTo(5.0, 5.0);
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
        }
        getPos();

        throw new UnsupportedOperationException("Not yet implemented");
    }
    // Diags -------------------------------------------------------------
    public void inspectCam() {
        //org.pf.joi.Inspector.inspect(ctrlX);
    }

    public void openJythonConsole() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            /* (non-Javadoc)
             * @see java.lang.Runnable#run()
             */

            public void run() {
                /*
                 * Instantiates a new Jython Console and shows it.
                 * The newly created <code>JythonConsole</code will be centered
                 * in the middle of the screen. (setPositionRelativeTo(null)
                 */
               // TODO
//                JythonConsole console = new JythonConsole();
//                console.setLocationRelativeTo(null);
//                console.setVisible(true);
            }

        });
    }

    // Test Main....................................
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TestPiC865 testlucam = new TestPiC865();

            // org.pf.joi.Inspector.inspect(testlucam);
            }

        });
    }

}
