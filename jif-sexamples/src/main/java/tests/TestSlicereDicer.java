/*
 * TestSlicereDicer.java
 *
 * Created on October 22, 2007, 5:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests;

import ij.*;
import ij.gui.GenericDialog;
import ij.io.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

/*
 * SlicerDicer_.java
 *
 * Created on October 22, 2007, 4:56 PM
 */
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import loci.common.Location;


/**
 *
 * @author GBH
 */
public class TestSlicereDicer {


  String path;
  int width = 640;
    int height = 220;
  int x = 150;
  int y = 50;
  int w = 400;
  int h = 150;

  Rectangle subImageROI = new Rectangle(x, y, w, h);
  
  /** Creates a new instance of SlicerDicer_ */
  public TestSlicereDicer(String dir) {
    //
    path = dir;
  }

  public void doit() {
    // get the working directory
//    Location tempFile = new Location(path).getAbsoluteFile();
//    Location workingDir = new Location(path).getAbsoluteFile(); // tempFile.getParentFile();
//    if (workingDir == null) {
//      workingDir = new Location(".");
//    }
//    String workingDirPath = workingDir.getPath() + File.separator;
//    String[] ls = workingDir.list();
//    int numYSlices = ls.length;
//    byte[][][] arr = new byte[w][h][numYSlices];
//    for (int f = 0; f < ls.length; f++) {
//      String filename = path + "\\" + ls[f];
//      System.out.println(filename);
//      DataInputStream is = null;
//      FileInputStream fs = null;
//      byte ch;
//      try {
//        fs = new FileInputStream(filename);
//        is = new DataInputStream(fs);
//        byte[] bite = new byte[w];
//        for (int j = y; j < h; j++) {
//          int offset = x + (width * j);
//          System.out.println(offset);
//          int bytesRead = is.read(bite, offset, w);
//          System.out.print(bite);
//        }
//      } catch (EOFException eof) {
//        System.out.println(" >> Normal program termination.");
//      } catch (FileNotFoundException noFile) {
//        System.err.println("File not found! " + noFile);
//      } catch (IOException io) {
//        System.err.println("I/O error occurred: " + io);
//      } catch (Throwable anything) {
//        System.err.println("Abnormal exception caught !: " + anything);
//      } finally {
//        if (is != null) {
//          try {
//            is.close();
//          } catch (IOException ignored) {
//          }
//        }
//      }
//      return;
//    }

    //
  }

  public static void main(String[] args) {
    (new TestSlicereDicer("C:\\_Dev\\testData\\PRSA")).doit();
  }

  /**
   * This ImageJ PlugInFilter makes a right handed or optionally left handed
   * (arg = "AXIAL_LEFT") axial stack from an input stack.
   *
   *   Axial   Coronal  Sagital
   *    z        y       -x
   *   /        /
   *   -----x   -----x   -----y
   *  |        |        |
   *  |        |        |
   *  y       -z       -z
   * x goes from right-to-left
   * y goes from anterior-to-posterior
   * z goes from caudal-to-cephalic
   * The orientations may make less sense in non-radiologic applications.
   *
   * @author J. Anthony Parker, MD PhD <J.A.Parker@IEEE.org>
   * @version 30January2002
   *
   * @see ij.plugin.filter.PlugInFilter
   */
  public class To_AxialTP implements PlugInFilter {
    private ImagePlus imp;
    private ImageProcessor ip;
    private String arg;
    private byte[][] pixelsB;
    private int[][] pixelsC;
    private float[][] pixelsF;
    private Object[] pixelsIn;
    private Object[] pixelsOut;
    private short[][] pixelsS;
    private boolean flipHor = false;
    private boolean flipVer = false;
    private int h;
    private int hIn;
    private int inOrientation;
    private int ipType;
    private int outOrientation;
    private int size;
    private int sizeIn;
    private int w;
    private int wIn;
    private static final int AXIAL_RIGHT = 0;
    private static final int AXIAL_LEFT = 1;
    private static final int CORONAL_RIGHT = 2;
    private static final int CORONAL_LEFT = 3;
    private static final int SAGITAL_RIGHT = 4;
    private static final int SAGITAL_LEFT = 5;
    private static final int BYTE = 0;
    private static final int SHORT = 1;
    private static final int FLOAT = 2;
    private static final int COLOR = 3;

    /**
     * @param arg output format = "AXIAL_LEFT" for left handed
     * @param imp ImagePlus
     */
    public int setup(String arg, ImagePlus imp) {
      this.arg = arg.toLowerCase().trim();
      this.imp = imp;
      if (this.arg.equals("about")) {
        showAbout();
        return DONE;
      }
      if (((this.arg.indexOf("axial")) >= 0) && (this.arg.indexOf("left") >= 0)) {
        outOrientation = AXIAL_LEFT;
      } else {
        outOrientation = AXIAL_RIGHT;
      }
      return DOES_ALL + STACK_REQUIRED + NO_CHANGES;
    }

    /**
     * @param ip currrent ImageProcessor from ImagePlus
     */
    public void run(ImageProcessor ip) {
      //IJ.debugMode = true;
      wIn = imp.getWidth();
      hIn = imp.getHeight();
      sizeIn = imp.getStackSize();
      this.ip = ip;
      if (!askOrientation()) {
        return;
      }
      if (!processorType()) {
        return;
      }
      // get input pixels
      pixelsIn = new Object[sizeIn];
      for (int n = 0; n < sizeIn; n++)
        pixelsIn[n] = imp.getStack().getPixels(n + 1);

      // convert
      for (int n = 0; n < size; n++) {
        for (int j = 0; j < h; j++)
          for (int i = 0; i < w; i++) {
            int n2 = 0;
            int j2 = 0;
            int i2 = 0;
            switch (inOrientation) {
            case AXIAL_LEFT:
              n2 = size - 1 - n;
              j2 = j;
              i2 = i;
              break;
            case CORONAL_RIGHT:
              n2 = j;
              j2 = size - 1 - n;
              i2 = i;
              break;
            case CORONAL_LEFT:
              n2 = h - 1 - j;
              j2 = size - 1 - n;
              i2 = i;
              break;
            case SAGITAL_RIGHT:
              n2 = w - 1 - i;
              j2 = size - 1 - n;
              i2 = j;
              break;
            case SAGITAL_LEFT:
              n2 = i;
              j2 = size - 1 - n;
              i2 = j;
              break;
            }
            if (flipHor) {
              i2 = wIn - 1 - i2;
            }
            if (flipVer) {
              j2 = hIn - 1 - j2;
            }
            if (outOrientation == AXIAL_LEFT) {
              n = size - 1 - n;
            }
            switch (ipType) {
            case BYTE:
              pixelsB[n][(j * w) + i] = ((byte[]) pixelsIn[n2])[(j2 * wIn) + i2];
              break;
            case SHORT:
              pixelsS[n][(j * w) + i] = ((short[]) pixelsIn[n2])[(j2 * wIn) + i2];
              break;
            case FLOAT:
              pixelsF[n][(j * w) + i] = ((float[]) pixelsIn[n2])[(j2 * wIn) + i2];
              break;
            case COLOR:
              pixelsC[n][(j * w) + i] = ((int[]) pixelsIn[n2])[(j2 * wIn) + i2];
              break;
            }
          } // end for i
      } // end for n
        // make output
      ImageStack stackOut = new ImageStack(w, h);
      for (int n = 0; n < size; n++) {
        ImageProcessor ipOut = ip.createProcessor(w, h);
        ipOut.setPixels(pixelsOut[n]);
        stackOut.addSlice("", ipOut);
      }
      ImagePlus impOut = new ImagePlus(imp.getTitle(), stackOut);
      impOut.show();
      impOut.updateAndDraw();
      return;
    }

    private boolean askOrientation() {
      String[] choice = {
          "Axial (caudal-to-cephalic)", "Axial (cephalic-to-caudal)",
          "Coronal (anterior-to-posterior)", "Coronal (posterior-to-anterior)",
          "Sagital (left-to-right)", "Sagital (right-to-left)"
        };
      GenericDialog gd = new GenericDialog("Reorient Stack To Axial");
      if (outOrientation == AXIAL_LEFT) {
        gd.addMessage("Make a left handed axial stack, i.e. " + "axial (cephalic-to-caudal).");
        gd.addChoice("Input stack:", choice, choice[0]);
      } else {
        gd.addMessage("Make a right handed axial stack, i.e. " + "axial (caudal-to-cephalic).");
        gd.addChoice("Input stack:", choice, choice[1]);
      }
      gd.addCheckbox("Flip horizontal", flipHor);
      gd.addCheckbox("Flip vertical", flipVer);
      gd.showDialog();
      if (gd.wasCanceled()) {
        return false;
      }
      switch (gd.getNextChoiceIndex()) {
      case 0:
        inOrientation = AXIAL_RIGHT;
        w = wIn;
        h = hIn;
        size = sizeIn;
        break;
      case 1:
        inOrientation = AXIAL_LEFT;
        w = wIn;
        h = hIn;
        size = sizeIn;
        break;
      case 2:
        inOrientation = CORONAL_RIGHT;
        w = wIn;
        h = sizeIn;
        size = hIn;
        break;
      case 3:
        inOrientation = CORONAL_LEFT;
        w = wIn;
        h = sizeIn;
        size = hIn;
        break;
      case 4:
        inOrientation = SAGITAL_RIGHT;
        w = sizeIn;
        h = wIn;
        size = hIn;
        break;
      case 5:
        inOrientation = SAGITAL_LEFT;
        w = sizeIn;
        h = wIn;
        size = hIn;
        break;
      default:
        IJ.error("Invalid choice index.");
        return false;
      }
      flipHor = gd.getNextBoolean();
      flipVer = gd.getNextBoolean();
      return true;
    }

    private boolean processorType() {
      if (ip instanceof ByteProcessor) {
        ipType = BYTE;
        pixelsB = new byte[size][w * h];
        pixelsOut = (Object[]) pixelsB;
      } else if (ip instanceof ShortProcessor) {
        ipType = SHORT;
        pixelsS = new short[size][w * h];
        pixelsOut = (Object[]) pixelsS;
      } else if (ip instanceof FloatProcessor) {
        ipType = FLOAT;
        pixelsF = new float[size][w * h];
        pixelsOut = (Object[]) pixelsF;
      } else if (ip instanceof ColorProcessor) {
        ipType = COLOR;
        pixelsC = new int[size][w * h];
        pixelsOut = (Object[]) pixelsC;
      } else {
        IJ.error("Unknown processor type");
        return false;
      }
      return true;
    }

    void showAbout() {
      IJ.showMessage("About To_AxialTP",
        "This PlugInFilter makes an axial stack from a coronal or\n" +
        "sagital stack.  For a right handed stack:\n" + "    x goes from right-to-left\n" +
        "    y goes from anterior-to-posterior\n" + "    z goes from caudal-to-cephalic\n" +
        "Install with arguement \"AXIAL_LEFT\" to make a left handed\n" +
        "axial stack (cephalic-to-caudal).  These orientations may\n" +
        "make less sense in non-radiologic applications.\n" + "     Axial   Coronal  Sagital\n" +
        "      z        y       -x\n" + "     /        /        /\n" +
        "     -----x   -----x   -----y\n" + "    |        |        |\n" +
        "    |        |        |\n" + "    y       -z       -z");
      return;
    }
  }
}
