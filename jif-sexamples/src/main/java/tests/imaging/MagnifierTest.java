/*
 * MagnifierTest.java
 *
 * Created on November 15, 2006, 7:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.imaging;

import com.sun.media.jai.widget.DisplayJAI;

import edu.mbl.jif.imaging.jai.ImageDisplay;

import java.awt.*;
import java.awt.Image.*;
import java.awt.MediaTracker.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.image.renderable.*;

import java.io.*;
import java.io.BufferedReader;

/**
 *
 * @author GBH
 */
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.*;

import javax.imageio.*;

import javax.media.jai.*;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.widget.*;

import javax.swing.*;


public class MagnifierTest extends JFrame {
    private PlanarImage source = null;
    private FileDialog fileDialog = new FileDialog(this, "Select File");
    private String dir = null;
    private ImageDisplay canvas = null;

    /** Creates a new instance of Main */
    public MagnifierTest() {
        String filename = GetFile();

        File f = new File(filename);

        if (f.exists() && f.canRead()) {
            source = JAI.create("fileload", filename);

            // Load the image which file name was passed as the first argument to the
            // application.
            PlanarImage image = JAI.create("fileload", filename);

            // Get some information about the image
            String imageInfo = "Dimensions: " + image.getWidth() + "x" + image.getHeight() +
                " Bands:" + image.getNumBands();

            // Create a frame for display.
            JFrame frame = new JFrame();
            frame.setTitle("DisplayJAI: " + filename);
            // Get the JFrame's ContentPane.
            Container contentPane = frame.getContentPane();
            contentPane.setLayout(new BorderLayout());
            // Create an instance of DisplayJAI.
            DisplayJAI dj = new DisplayJAI(image);
            // Add to the JFrame's ContentPane an instance of JScrollPane containing the
            // DisplayJAI instance.
            contentPane.add(new JScrollPane(dj), BorderLayout.CENTER);
            // Add a text label with the image information.
            contentPane.add(new JLabel(imageInfo), BorderLayout.SOUTH);
            // Set the closing operation so the application is finished.
            ImageDisplay canvas = new ImageDisplay(image);

            Magnifier mag = new Magnifier();
            //mag.setLocation(50, 50);
            //Rectangle Rect = new Rectangle();
            //Rect.width = 30;
            //Rect.height = 30;
            mag.setSize(50, 50);
            mag.setMagnification(3.0F);
            mag.setSource(canvas);
            //mag.computeVisibleRect(Rect);
            mag.setSource(dj);
            //mag.isVisible();
            dj.add(mag);

            contentPane.add(dj, BorderLayout.CENTER);
            //getContentPane().add(mag);
            //getContentPane().add(dj);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400); // adjust the frame size.
            frame.setVisible(true); // show the frame.
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MagnifierTest frame = new MagnifierTest();

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack(); frame.show();
    }

    public String GetFile() {
        FileDialog dlg = new FileDialog(this, "Choose File", FileDialog.LOAD);
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        System.out.println(f.getAbsolutePath());
//        //set current directory
//        if (dir != null) {
//            dlg.setDirectory(dir);
//        }
//        dlg.setVisible(true);
//        //get image name and path
//        String imgFile1 = dlg.getDirectory() + dlg.getFile();
//        return imgFile1;
        return f.getAbsolutePath();
    }
}
