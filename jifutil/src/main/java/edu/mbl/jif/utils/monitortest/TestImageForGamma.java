package edu.mbl.jif.utils.monitortest;

/*Java program to test the gamma calibration routines.
This program attempts to load the file Calibration.txt, and if it does
then it shows the original image and the linearized images on separate frames.
You may have to move one frame to see the other.
If the file Calibration.txt cannot be found in the current directory then the
program opens a dialog box to do the calibration.

If the calibration worked, the gray patches of the linearized image should
represent linear increases in luminance as measured by a photometer.
*/

import java.awt.image.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class TestImageForGamma{

  public static void main(String args[]) {
//    GammaDialog gammaDialog;
//    Vector gammaVector, errVector, abVector, grayVector;
//    TestGammaDialog testGammaDialog;
//
//    // Does Calibration.txt exist?
//    try
//    {
//      abVector = new Vector();
//      errVector = new Vector();
//
//      File file = new File("Calibration.txt");
//      FileReader reader = new FileReader(file);     //get progress
//      BufferedReader buff = new BufferedReader(reader);
//      String readline = buff.readLine();
//      abVector.addElement(readline);
//      readline = buff.readLine();
//      abVector.addElement(readline);
//
//      for (int l = 0; l < 8; l++) {
//        readline = buff.readLine();
//        errVector.addElement(readline);
//      }
//
//      reader.close();
//      System.out.println("a = " + abVector.elementAt(0) + "b = " + abVector.elementAt(1));
//      doImage("LinearGrayPatches.jpg", abVector);
//    }
//      // Run GammaDialog and write Calibration.txt if it doesn't
//      catch(IOException e)
//      {
//        gammaDialog = new GammaDialog();
//        gammaDialog.setVisible(true);
//        do{}while (gammaDialog.getCalibrationDone()==false);
//        gammaVector = gammaDialog.getGammaVector();
//        errVector = gammaDialog.getErrorVector();
//        abVector = gammaDialog.getAbVector();
//        grayVector = gammaDialog.getGrayVector();
//
//        gammaDialog.dispose();
//
//        try
//        {
//          File calfile = new File("Calibration.txt");
//          FileWriter writer = new FileWriter(calfile);     //save progress
//          writer.write(abVector.elementAt(0) + "\n" + abVector.elementAt(1) + "\n");
//          for (int loc = 0; loc < errVector.size(); loc++)
//            writer.write(errVector.elementAt(loc) + "\n");
//          writer.close();
//        }
//        catch (Exception exp){}
//        doImage("LinearGrayPatches.jpg", abVector);
//      }
//    }
//
//  static void doImage(String f, Vector abVector) {
//    Image i;
//    Image newi;
//    GammaDialog gd = new GammaDialog();
//
//    i = Utilities.blockingLoad(f);
//    newi = gd.linearizeImage(i, abVector);
//
//    //draw images
//    JFrame framei = new JFrame("Old Image");
//    JFrame framenewi = new JFrame("Linearized Image");
//    JLabel labeli = new JLabel(new ImageIcon(i));
//    JLabel labelnewi = new JLabel(new ImageIcon(newi));
//    labeli.setIcon(new ImageIcon(i));
//    labelnewi.setIcon(new ImageIcon(newi));
//    framei.getContentPane().add(labeli, BorderLayout.CENTER);
//    framenewi.getContentPane().add(labelnewi, BorderLayout.CENTER);
//    framei.pack();
//    framenewi.pack();
//    framei.setVisible(true);
//    framenewi.setVisible(true);
//    framei.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//    framenewi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
