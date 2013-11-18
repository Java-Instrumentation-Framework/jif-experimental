/*
 * panel_Compass.java
 *
 * Created on September 17, 2007, 6:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.graphic;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Rectangle;
//To import an image
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Used for Timer
import javax.swing.Timer;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.Point;
import java.applet.Applet;

public class panel_Compass extends JPanel  implements Runnable{

        private static final long serialVersionUID = 1L;

        //Import the image of the compass
        //Image compass = new ImageIcon("comp1.gif").getImage();  //  @jve:decl-index=0:
        Image compass = new ImageIcon("compass.gif").getImage();  //  @jve:decl-index=0:
        //Import the image of the arrow
        Image arrow = new ImageIcon("arrow.gif").getImage();  //  @jve:decl-index=0:
        //Import the image of the boat
        Image boat = new ImageIcon("boat.gif").getImage();  //  @jve:decl-index=0:
        //Timer timer;
        Thread thread;
        //Buffered Images
        Image bufferImage;  //  @jve:decl-index=0:
        //Buffred graphic
        Graphics2D bufferGraphic;  //  @jve:decl-index=0:


        //Store current Average Angle
        double avgAngle = 0.0;
        //Store current index to get average angle
        int index = 1;
        int index2 = 1;

        private JButton startButton = null;

        /**
         * This is the default constructor
         */
        public panel_Compass() {
                super();
                initialize();
                //Create a thread to display the compass
                thread = new Thread(this);

        }

        /**
         * This method initializes this
         *
         * @return void
         */
        private void initialize() {
                this.setLayout(null);
                this.setSize(400, 400);
                this.setOpaque(false);
                this.setDoubleBuffered(false);
                //this.setTitle("Frame");
                this.add(getStartButton(), null);

        }

        public double getAngle(){
                //Randomly generate a number between 0 - 360;
                double newAngle = Math.random()*360;
                System.out.println("New Angle = " + Double.toString(newAngle));
                //Return the random angle
                return newAngle;
        }

        public double avgAngle() {
                double angle = Math.random()*360;
                this.avgAngle = this.avgAngle + angle;
                double outputAngle = this.avgAngle/this.index;
                index++;

                System.out.println("Index =" + index + " avgAngle=" + avgAngle + " outputAngle =" + outputAngle);

                return outputAngle;
        }

        public void paintComponent(Graphics g){
                //Create a 2d graphics object
                Graphics2D g2d = (Graphics2D)g;

                //Reduced the area that is being updated to this rectangle
                g2d.clipRect(0, 0, 400, 400);

                //Create a buffer
                if (bufferImage == null){
                        bufferImage = createImage(400,400);
                }
                //Create something in the buffer
                bufferGraphic = (Graphics2D)bufferImage.getGraphics();

                //Display a compass in the buffer
                bufferGraphic.drawImage(compass, 0,0,400,400,this);

                //Display a rotated arrow in the buffer
                bufferGraphic.rotate(Math.toRadians(getAngle()),200 , 200);
                bufferGraphic.drawImage(arrow,100,100,200,200,this);

                //Display a rotated boat image in the buffer
                bufferGraphic.rotate(Math.toRadians(getAngle()),200 , 200);
                bufferGraphic.drawImage(boat, 100,100,200,200,this);

                //Display the image from the buffer
                g2d.drawImage(bufferImage, 0, 0,400,400,  this);

                //Clear the buffer for a new image
                bufferGraphic.dispose();
                //g2d.dispose();
                //g.dispose();
        }


        /**
         * This method initializes startButton
         *
         * @return javax.swing.JButton
         */
        private JButton getStartButton() {
                if (startButton == null) {
                        startButton = new JButton();
                        startButton.setBounds(new Rectangle(6, 32, 40, 21));
                        startButton.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent e) {
                                        System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                                        if(!thread.isAlive()){
                                                thread.start();
                                        }
                                }
                        });
                }
                return startButton;
        }

        //The thread will repaint using overloaded update() every 200ms
        public void run() {

                while(true){
                try {
                repaint();
                Thread.sleep(200);
                }
                catch(Exception e) {
                        System.out.println("Stop");
                        thread.stop();
                }
                }
        }
}
        