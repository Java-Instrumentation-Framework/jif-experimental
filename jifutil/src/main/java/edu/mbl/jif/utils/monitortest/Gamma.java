package edu.mbl.jif.utils.monitortest;


/*
Gamma.java
Written by Karen Hull (kahull@indiana.edu) and Tom Busey (busey@indiana.edu)
at Indiana University, Bloomington.
This code is provided without warrenty. Users should independently verify that
the calibration they use is actually working in their applications.
This dialog box measures the gamma function of your monitor by comparing
a gray patch with lines that are lighter and darker than the patch. At a
sufficient viewing distance, the light and dark lines blur together and
mix to form gray.
This program bisects the range of grayscale values 7 times and then computes
a function relating the average of the two lines (e.g. if 255 and 0 are used,
127.5 would be the midpoint) against the measured gray patch required to match
this value. Usually the gray value required to match the midpoint is higher
than the midpoint, with reflects the non-linear gamma function.
Once we fit the gamma function, we produce predictions for new values and
ask the subject to adjust the gray patch again. If the calibration worked,
the values returned in errVector should be very small (less than 5). Thus we
have a way to check to see if the gamma calbiration worked.
See TestImage.java for examples of how to use this code. In its simplest form,
the function LinearizeImage takes an Image and returns the same image
linearized.
The most complicated part is linearizing the image, and the user should
work through how we convert the image. This is the essential part:
a and b are paramters of the gamma function:
a = Double.parseDouble((String)abVector.elementAt(0));
b = Double.parseDouble((String)abVector.elementAt(1));
double maxLinear =Math.pow((1.0/a)*255.0,(1/b));
maxLinear figures out the maximum value of the transformed values, which may
up greater than 255, which would cause clipping. To solve this, we
then convert each grayscale value (called newLeanrRedDouble because
we have to do this for all 3 colors). This scales the old values so that they
are in a range so that when we do the non-linear translation we don't get
clipping:
newLinearRedDouble =(redDouble/255.0)*(maxLinear-1)+1;
Finally, we do the non-linear translation to undo the gamma non-linearity
and produce a linear range of grayscales:
redLinearVoltageDouble=a*Math.pow(newLinearRedDouble,b);
Notes: This procedure does not work well on LCD monitors unless the screen
resolution is set to its native resolution for the LCD screen.
 */
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import java.applet.*;
import javax.swing.BorderFactory;
import javax.swing.border.*;
import java.awt.image.*;  // needed for pixelgrabber in linearization
import java.awt.image.BufferedImage.*;

public class Gamma extends JApplet implements ItemListener {

//Tom -- you don't need to include these variables in your applet
  JPanel appletPanel;
  JButton gammaDiagJB;
  JButton checkJB;
  JCheckBox calOnlyJCB;

//Tom -- you do need to include these variables in your applet.
//You also need to include the classes GammaDialog and Pixel Panel
  GammaDialog gammaDialog;
  Vector gammaVector, errVector, abVector, grayVector;
  TestGammaDialog testGammaDialog;

  public void init() {

    appletPanel = new JPanel();
    setContentPane(appletPanel);
    appletPanel.setBackground(Color.black);

//Tom --  you need to do this
    gammaDialog = new GammaDialog();

    gammaDiagJB = new JButton("Get gamma");
    gammaDiagJB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        gammaDialog.setVisible(true);

      }
    });

    checkJB = new JButton("check");
    checkJB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        System.out.println("");
        System.out.println("size of gammaVector passed back = " + gammaVector.size());
        System.out.println("errVector passed back = " + errVector);
        System.out.println("abVector passed back = " + abVector);
        System.out.println("grayVector passed back = " + grayVector);
      }
    });

    calOnlyJCB = new JCheckBox("calibrate only");
    calOnlyJCB.addItemListener(this);

//Tom -- you need these lines to get the data
    gammaVector = gammaDialog.getGammaVector();
    errVector = gammaDialog.getErrorVector();
    abVector = gammaDialog.getAbVector();
    grayVector = gammaDialog.getGrayVector();

    System.out.println("gammaVector init = " + gammaVector);
    System.out.println("errVector init = " + errVector);
    System.out.println("abVector init = " + abVector);
    System.out.println("grayVector init = " + grayVector);
    appletPanel.add(gammaDiagJB);
    appletPanel.add(calOnlyJCB);
    appletPanel.add(checkJB);
  }

  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      gammaDialog.setCalOnly(true);
    } else {
      gammaDialog.setCalOnly(false);
    }
  }
}

//===========================
//Class GammaDialog
//===========================
class GammaDialog extends JDialog implements ChangeListener {

  JPanel gammaPanel, testPanel;
  PixelPanel pixelPanel;
  JDialog thisJDiag;
  JButton resetJB, nextJB, finishJB;
  JSlider colorSlider;
  JTextArea messageJTA;
  int current, grayValue, sliderValue, min, max, l, d, error;
  Vector gammaVect, grayVect, ltVect, dkVect, errorVect, abVect;
  ChangeListener sliderListener;
  boolean userSetSlider, calOnly;
  boolean doneWithCalibration = false;

  public GammaDialog() {

    thisJDiag = this;

    gammaPanel = new JPanel();
    colorSlider = new JSlider(JSlider.VERTICAL, 200 - 128, 200, 128);
    colorSlider.addChangeListener(this);
    grayVect = new Vector();
    gammaVect = new Vector();
    fillGrayVect();
    ltVect = new Vector();
    fillLtVect();
    dkVect = new Vector();
    fillDkVect();
    errorVect = new Vector();
    fillErrorVect();
    abVect = new Vector();
    calOnly = false;
    current = 0;
    userSetSlider = true;
    makeGammaPanel();
    setSize(new Dimension(550, 660));
    setResizable(true);
    setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    setContentPane(gammaPanel);
    Utilities.centerDialog(this);


  }

  public void stateChanged(ChangeEvent e) {
    sliderValue = (int) colorSlider.getValue();
    pixelPanel.changePatch(sliderValue);
    if (!colorSlider.getValueIsAdjusting()) {

      sliderValue = (int) colorSlider.getValue();

      if (current == 0) {
        grayVect.removeElementAt(current);
        grayVect.insertElementAt(String.valueOf(sliderValue), current);
        ltVect.removeElementAt(1);
        ltVect.insertElementAt(String.valueOf(sliderValue), 1);
        ltVect.removeElementAt(4);
        ltVect.insertElementAt(String.valueOf(sliderValue), 4);
        dkVect.removeElementAt(2);
        dkVect.insertElementAt(String.valueOf(sliderValue), 2);
        dkVect.removeElementAt(5);
        dkVect.insertElementAt(String.valueOf(sliderValue), 5);

      } else {
        if (current == 1) {
          grayVect.removeElementAt(current);
          grayVect.insertElementAt(String.valueOf(sliderValue), current);
          ltVect.removeElementAt(3);
          ltVect.insertElementAt(String.valueOf(sliderValue), 3);
          dkVect.removeElementAt(4);
          dkVect.insertElementAt(String.valueOf(sliderValue), 4);
        } else {
          if (current == 2) {
            grayVect.removeElementAt(current);
            grayVect.insertElementAt(String.valueOf(sliderValue), current);
            ltVect.removeElementAt(5);
            ltVect.insertElementAt(String.valueOf(sliderValue), 5);
            dkVect.removeElementAt(6);
            dkVect.insertElementAt(String.valueOf(sliderValue), 6);
          } else {
            if ((current >= 3) && (current <= 6)) {
              grayVect.removeElementAt(current);
              grayVect.insertElementAt(String.valueOf(sliderValue), current);
            } else {
              error = sliderValue - grayValue;
              errorVect.removeElementAt(current - 7);
              errorVect.insertElementAt(String.valueOf(error), current - 7);
              System.out.println("errorVect = " + errorVect);
            }
          }
        }
      }
      if (current < 15) {
        nextJB.setEnabled(true);
      }
      if ((current == 15) && (userSetSlider)) {
        finishJB.setEnabled(true);
        messageJTA.setText("Finish this last match. When you match it, you will be done with the calibration. Make the match using the slider and then click the Finished button to continue on with the actual experiment.");
      } else {
        userSetSlider = true;
      }


    }
  }

  public void makeGammaPanel() {

    Box hBox, vBox, btnBx;

    gammaPanel.setBackground(Color.black);
    testPanel = new JPanel();
    testPanel.setLayout(new GridLayout(1, 1));
    testPanel.setPreferredSize(new Dimension(200, 200));
    testPanel.setMaximumSize(new Dimension(200, 200));

    pixelPanel = new PixelPanel();
    pixelPanel.setPreferredSize(new Dimension(200, 200));
    pixelPanel.changeGrid(255, 0);
    pixelPanel.changePatch(128);

    testPanel.add(pixelPanel);
    messageJTA = new JTextArea("In this first section you will calibrate your monitor. Above you see a gray square surrounded by black and white stripes. Squint your eyes a little so that you blur together the black and white stripes, and then move the slider up and down until the gray square is neither brighter nor darker than the blurred stripes. When they match, click the 'next' button. If you make a mistake, click the start over button. \n\n Hint: one way to know whether you are at the right value is that the vertical boundaries become very indistinct. So one way to do this is to  move the slider until the borders get hard to see. Try clicking on the slider to activate it, and then use the up and down arrow keys to make the center lighter and darker than the surround. Somewhere in the middle is the match. \nThere are 15 matches to do.", 6, 25);
    messageJTA.setFont(new Font("Arial", Font.PLAIN, 12));
    messageJTA.setForeground(Color.white);
    messageJTA.setBackground(Color.black);
    messageJTA.setLineWrap(true);
    messageJTA.setWrapStyleWord(true);
    messageJTA.setEditable(false);

    resetJB = new JButton("Start over");
    resetJB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        current = 0;
        userSetSlider = false;
        colorSlider.setMinimum(200 - 128);
        userSetSlider = false;
        colorSlider.setMaximum(200);
        userSetSlider = false;
        colorSlider.setValue(128);
        pixelPanel.changeGrid(255, 0);
        pixelPanel.changePatch(128);
        grayVect.removeAllElements();
        fillGrayVect();
        ltVect.removeAllElements();
        fillLtVect();
        dkVect.removeAllElements();
        fillDkVect();
        nextJB.setEnabled(false);
        abVect.removeAllElements();
        System.out.println("grayVect = " + grayVect);
      }
    });

    nextJB = new JButton("next >");
    nextJB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        current = current + 1;

        if (current == 7) {
          fillGammaVect();
          System.out.println("ltVect = " + ltVect);
          System.out.println("dkVect = " + dkVect);
          if (calOnly) {
            nextJB.setEnabled(false);
            finishJB.setEnabled(true);
            messageJTA.setText("You are now done with the calibration. Click the Finished button to  continue on with the actual experiment.");
          } else {
            addTestValues();
            System.out.println("ltVect = " + ltVect);
            System.out.println("dkVect = " + dkVect);
          }
        }
        if (current <= 15) {
          l = Integer.parseInt((String) ltVect.elementAt(current));
          d = Integer.parseInt((String) dkVect.elementAt(current));
        }

        grayValue = calcAve(l, d);
        pixelPanel.changeGrid(l, d);
        pixelPanel.changePatch(grayValue);
        System.out.println(" ");
        System.out.println("current = " + current);
        System.out.println("grayVect = " + grayVect);
        System.out.println("dark value = " + d + ", light value = " + l + ", patch = " + grayValue);
        min = grayValue - 60;
        max = grayValue + 60;
        if (min < 0) {
          min = 0;
        }
        if (max > 255) {
          max = 255;
        }
        userSetSlider = false;
        colorSlider.setMinimum(min);
        userSetSlider = false;
        colorSlider.setMaximum(max);
        userSetSlider = false;
        colorSlider.setValue(grayValue);
        nextJB.setEnabled(false);

        if ((current == 7) && (calOnly)) {
          pixelPanel.changeGrid(0, 0);
          pixelPanel.changePatch(0);
        }

      }
    });
    nextJB.setEnabled(false);

    finishJB = new JButton("finished");
    finishJB.setEnabled(false);
    finishJB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        thisJDiag.hide();
        doneWithCalibration = true;
      }
    });

    btnBx = Box.createHorizontalBox();
    btnBx.add(resetJB);
    btnBx.add(Box.createHorizontalStrut(15));
    btnBx.add(nextJB);
    btnBx.add(Box.createHorizontalStrut(15));
    btnBx.add(finishJB);

    hBox = Box.createHorizontalBox();
    hBox.add(testPanel);
    hBox.add(Box.createHorizontalStrut(50));
    hBox.add(colorSlider);

    vBox = Box.createVerticalBox();
    vBox.add(Box.createVerticalStrut(40));
    vBox.add(hBox);
    vBox.add(Box.createVerticalStrut(50));
    vBox.add(btnBx);
    vBox.add(Box.createVerticalStrut(30));
    vBox.add(messageJTA);
    gammaPanel.add(vBox);

    validate();
  }

  public void fillGrayVect() {

    grayVect.addElement("128");
    grayVect.addElement("64");
    grayVect.addElement("192");
    grayVect.addElement("32");
    grayVect.addElement("96");
    grayVect.addElement("160");
    grayVect.addElement("224");
    System.out.println("grayVect = " + grayVect);
    System.out.println("dark value = 0, light value = 255, patch = 128");

  }

  public void fillLtVect() {

    ltVect.addElement("255");
    ltVect.addElement("255");
    ltVect.addElement("255");
    ltVect.addElement("255");
    ltVect.addElement("255");
    ltVect.addElement("255");
    ltVect.addElement("255");
    ltVect.addElement("64");
    ltVect.addElement("96");
    ltVect.addElement("128");
    ltVect.addElement("160");
    ltVect.addElement("192");
    ltVect.addElement("224");
    ltVect.addElement("128");
    ltVect.addElement("160");
    ltVect.addElement("224");
  }

  public void fillDkVect() {

    dkVect.addElement("0");
    dkVect.addElement("0");
    dkVect.addElement("0");
    dkVect.addElement("0");
    dkVect.addElement("0");
    dkVect.addElement("0");
    dkVect.addElement("0");
    dkVect.addElement("32");
    dkVect.addElement("64");
    dkVect.addElement("96");
    dkVect.addElement("128");
    dkVect.addElement("160");
    dkVect.addElement("192");
    dkVect.addElement("32");
    dkVect.addElement("64");
    dkVect.addElement("64");

  }

  public void fillErrorVect() {

    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
  }

  public int calcAve(int i1, int i2) {

    int sum, numVals, val, average;
    float ave;

    sum = i1 + i2;
    ave = sum / 2;
    average = Math.round(ave);
    return average;
  }

  public void fillGammaVect() {

    int predY;

//Gray vect (128, 64, 192, 32, 96, 160, 224)
//            ^    ^   ^    ^   ^   ^    ^
//            0    1   2    3   4   5    6

//convert data to array of 2 arrays of doubles (x data and y data)

    double[] xydata0 = new double[2];
    double[] xydata1 = new double[2];
    double[] xydata2 = new double[2];
    double[] xydata3 = new double[2];
    double[] xydata4 = new double[2];
    double[] xydata5 = new double[2];
    double[] xydata6 = new double[2];
    double[] xydata7 = new double[2];
    double[] xydata8 = new double[2];


    double[][] data = new double[8][];

    xydata0[0] = (double) 128;
    xydata0[1] = (double) Integer.parseInt((String) grayVect.elementAt(0));
    xydata1[0] = (double) 64;
    xydata1[1] = (double) Integer.parseInt((String) grayVect.elementAt(1));
    xydata2[0] = (double) 192;
    xydata2[1] = (double) Integer.parseInt((String) grayVect.elementAt(2));
    xydata3[0] = (double) 32;
    xydata3[1] = (double) Integer.parseInt((String) grayVect.elementAt(3));
    xydata4[0] = (double) 96;
    xydata4[1] = (double) Integer.parseInt((String) grayVect.elementAt(4));
    xydata5[0] = (double) 160;
    xydata5[1] = (double) Integer.parseInt((String) grayVect.elementAt(5));
    xydata6[0] = (double) 224;
    xydata6[1] = (double) Integer.parseInt((String) grayVect.elementAt(6));
    xydata7[0] = (double) 255;
    xydata7[1] = (double) 255;


    data[0] = xydata0;
    data[1] = xydata1;
    data[2] = xydata2;
    data[3] = xydata3;
    data[4] = xydata4;
    data[5] = xydata5;
    data[6] = xydata6;
    data[7] = xydata7;

    double[] aAndb;
    double a, b;

    aAndb = getAandB(data);
    a = aAndb[0];
    b = aAndb[1];
    abVect.addElement(String.valueOf(a));
    abVect.addElement(String.valueOf(b));
    System.out.println(" ");
    System.out.println("a = " + a + ", b = " + b);
    System.out.println(" ");

//fill gammaVect with predicted values for 0-255

    for (int g = 0; g < 256; g++) {

      predY = applyFormula(g, a, b);
      gammaVect.insertElementAt(String.valueOf(predY), g);
// System.out.println(g + " --> " + predY);

    }
  }

  public static double[] getAandB(final double[][] data) {

    final int n = data.length;
    if (n < 2) {
      throw new IllegalArgumentException("Not enough data.");
    }

    double sumX = 0;
    double sumY = 0;
    double sumXX = 0;
    double sumXY = 0;
    for (int i = 0; i < n; i++) {
      final double x = Math.log(data[i][0]);
      final double y = Math.log(data[i][1]);
      sumX += x;
      sumY += y;
      final double xx = x * x;
      sumXX += xx;
      final double xy = x * y;
      sumXY += xy;
    }
    final double sxx = sumXX - (sumX * sumX) / n;
    final double sxy = sumXY - (sumX * sumY) / n;
    final double xbar = sumX / n;
    final double ybar = sumY / n;

    final double[] result = new double[2];
    result[1] = sxy / sxx;
    result[0] = Math.pow(Math.exp(1.0), ybar - result[1] * xbar);

    return result;

  }

  public int applyFormula(int x, double aval, double bval) {

    //predicted y = ax^b
    double xd, yd, ad, bd;
    float yf;
    int yint;

    xd = (double) x;
    ad = aval;
    bd = bval;

    try {
      xd = Math.pow(xd, bd);
    } catch (ArithmeticException ae) {
      System.out.println("arithmetic exception on power");
    }

    yd = ad * xd;
    yf = (float) yd;
    yint = Math.round(yf);
    return yint;
  }

  public void addTestValues() {

    dkVect.removeElementAt(7);
    dkVect.insertElementAt((String) gammaVect.elementAt(32), 7);
    ltVect.removeElementAt(7);
    ltVect.insertElementAt((String) gammaVect.elementAt(64), 7);
    dkVect.removeElementAt(8);
    dkVect.insertElementAt((String) gammaVect.elementAt(64), 8);
    ltVect.removeElementAt(8);
    ltVect.insertElementAt((String) gammaVect.elementAt(96), 8);
    dkVect.removeElementAt(9);
    dkVect.insertElementAt((String) gammaVect.elementAt(96), 9);
    ltVect.removeElementAt(9);
    ltVect.insertElementAt((String) gammaVect.elementAt(128), 9);
    dkVect.removeElementAt(10);
    dkVect.insertElementAt((String) gammaVect.elementAt(128), 10);
    ltVect.removeElementAt(10);
    ltVect.insertElementAt((String) gammaVect.elementAt(160), 10);
    dkVect.removeElementAt(11);
    dkVect.insertElementAt((String) gammaVect.elementAt(160), 11);
    ltVect.removeElementAt(11);
    ltVect.insertElementAt((String) gammaVect.elementAt(192), 11);
    dkVect.removeElementAt(12);
    dkVect.insertElementAt((String) gammaVect.elementAt(192), 12);
    ltVect.removeElementAt(12);
    ltVect.insertElementAt((String) gammaVect.elementAt(224), 12);
    dkVect.removeElementAt(13);
    dkVect.insertElementAt((String) gammaVect.elementAt(32), 13);
    ltVect.removeElementAt(13);
    ltVect.insertElementAt((String) gammaVect.elementAt(128), 13);
    dkVect.removeElementAt(14);
    dkVect.insertElementAt((String) gammaVect.elementAt(64), 14);
    ltVect.removeElementAt(14);
    ltVect.insertElementAt((String) gammaVect.elementAt(160), 14);
    dkVect.removeElementAt(15);
    dkVect.insertElementAt((String) gammaVect.elementAt(64), 15);
    ltVect.removeElementAt(15);
    ltVect.insertElementAt((String) gammaVect.elementAt(224), 15);
  }

  public boolean getCalibrationDone() {
    return doneWithCalibration;
  }

  public Vector getGammaVector() {
    return gammaVect;
  }

  public Vector getErrorVector() {
    return errorVect;
  }

  public Vector getAbVector() {
    return abVect;
  }

  public Vector getGrayVector() {
    return grayVect;
  }

  public void setCalOnly(boolean co) {
    calOnly = co;
  }

  public Image linearizeImage(Image i, Vector abVector) {
    Image j;

    try {
      int width = i.getWidth(this);
      int height = i.getHeight(this);
      int pixels[] = new int[width * height];
      PixelGrabber pg = new PixelGrabber(i, 0, 0, width, height, pixels, 0, width);
      if (pg.grabPixels() && ((pg.status() & ImageObserver.ALLBITS) != 0)) {
        i = createImage(new MemoryImageSource(width, height, linearizePixels(pixels, width, height, abVector), 0, width));
      } else {
        System.out.println("Problem with pg.status()?" + pg.status() + " " + ImageObserver.ALLBITS);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return i;//return changed image
  }

  private int[] linearizePixels(int pixels[], int width, int height, Vector abVector) {
    int newPixels[] = null;

    ColorModel defaultCM = ColorModel.getRGBdefault();

    int alpha = 255;

    int numClip = 0;
    int numClipneg = 0;
    long l;
    int noise, pixel, red, x, y;
    double a, b, redDouble, newLinearRedDouble, redLinearVoltageDouble;

    a = Double.parseDouble((String) abVector.elementAt(0));
    b = Double.parseDouble((String) abVector.elementAt(1));

    // double maxLinear = Math.pow(((1.0/a)*255.0),(1/b));
    double maxLinear = Math.pow((1.0 / a) * 255.0, (1 / b));

    System.out.println(" a = " + a + " b = " + b + " maxLinear = " + maxLinear);

    //just for testing
    for (int col = 0; col < 256; col++) {
      redDouble = (double) col;
      newLinearRedDouble = (redDouble / 255.0) * (maxLinear - 1) + 1;
      //    redLinearVoltageDouble=a*newLinearRedDouble^b;
      redLinearVoltageDouble = a * Math.pow(newLinearRedDouble, b);
      red = (int) Math.round(redLinearVoltageDouble);

    //System.out.println("Old Linear " + col + " New Linear " + newLinearRedDouble + " Linear Voltage " + red);
    }

    if ((width * height) == pixels.length) {
      newPixels = new int[width * height];
      int newIndex = 0;
      for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {

          pixel = pixels[y * width + x];

          redDouble = (double) defaultCM.getRed(pixel);
          newLinearRedDouble = (redDouble / 255.0) * (maxLinear - 1) + 1;
          //    redLinearVoltageDouble=a*newLinearRedDouble^b;
          redLinearVoltageDouble = a * Math.pow(newLinearRedDouble, b);
          red = (int) Math.round(redLinearVoltageDouble);
          //System.out.println("Old Linear " + redDouble + " New Linear " + newLinearRedDouble + " Linear Voltage " + red);

          //red = 128;

          //    red= (int)Math.round( (((double)defaultCM.getRed (pixel)-128)/contrastChange)+128+brightnessChange);
          // int green = defaultCM.getGreen (pixel)+noise;
          //int blue  = defaultCM.getBlue  (pixel)+noise;
          if (red > 255) {
            red = 255;
            numClip++;
          }
          //if (green>255) green = 255;
          //if (blue >255) blue = 255;
          if (red < 0) {
            red = 0;
            numClipneg++;
          }
          //if (green<0) green = 0;
          //if (blue < 0) blue = 0;

          //    int newPixel = (alpha << 24) | (red << 16) | (green << 8 ) | blue;
          int newPixel = (alpha << 24) | (red << 16) | (red << 8) | red;
          newPixels[newIndex++] = newPixel;
        }
      }
    }
    System.out.println("change brightness NumClipped = " + numClip);
    System.out.println("change brightness NumClipped neg = " + numClipneg);
    return newPixels;
  }
}

//===========================
//Class TestGammaDialog
//===========================
class TestGammaDialog extends JDialog implements ChangeListener {

  JPanel tstGamJP, tstGamtestJP;
  PixelPanel tstGamPP;
  JDialog tstGamJDiag;
  JButton tstGamprevJB, tstGamnextJB, tstGamfinJB;
  JSlider tstGamSlider;
  JTextArea tstGammsgJTA;
  int currentTest, predValue, sliderValue, mn, mx, l, d;
  int error;
  Vector gmaVect, predictedTestVect;
  Vector dkVector, ltVector;
  Vector obtainedVect, errorVect;
  ChangeListener sliderListener;
  boolean userSetSlider;

  public TestGammaDialog(Vector gv) {

    tstGamJDiag = this;

    tstGamJP = new JPanel();
    tstGamSlider = new JSlider(JSlider.VERTICAL, 0, 255, 128);
    tstGamSlider.addChangeListener(this);
    gmaVect = gv;
    predictedTestVect = new Vector();
    fillpredictedTestVect();
    dkVector = new Vector();
    filldkVector();
    ltVector = new Vector();
    fillltVector();
    errorVect = new Vector();
    fillerrorVect();
    makeTstGamPanel();
    currentTest = 0;
    userSetSlider = true;
    setSize(new Dimension(550, 550));
    setResizable(true);
    setDefaultLookAndFeelDecorated(true);
//  setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    setContentPane(tstGamJP);

  }

  public void stateChanged(ChangeEvent e) {
    sliderValue = (int) tstGamSlider.getValue();
    tstGamtestJP.setBackground(new Color(sliderValue, sliderValue, sliderValue));
//  tstGamPP.changePatch(sliderValue);
    System.out.println("patch gray value (slider value) = " + sliderValue);
  /*
  if (!tstGamSlider.getValueIsAdjusting()) {
  sliderValue = (int)tstGamSlider.getValue();
  error = sliderValue - predValue;
  errorVect.removeElementAt(currentTest);
  errorVect.insertElementAt(String.valueOf(error), currentTest);
  if (currentTest < 6){
  tstGamnextJB.setEnabled(true);
  }
  if ((currentTest == 6) && (userSetSlider)){
  tstGamfinJB.setEnabled(true);
  } else {
  userSetSlider =  true;
  }
  }
   */

  }

  public void makeTstGamPanel() {

    Box hBox, vBox, btnBx;

    tstGamJP.setBackground(Color.black);
    tstGamtestJP = new JPanel();
    tstGamtestJP.setLayout(new GridLayout(1, 1));
    tstGamtestJP.setPreferredSize(new Dimension(200, 200));
    tstGamtestJP.setMaximumSize(new Dimension(200, 200));

    /*
    tstGamPP = new PixelPanel();
    tstGamPP.setPreferredSize(new Dimension(200, 200));
    predValue = Integer.parseInt((String)predictedTestVect.elementAt(0));
    l = Integer.parseInt((String)ltVector.elementAt(0));
    d = Integer.parseInt((String)dkVector.elementAt(0));
    tstGamPP.changeGrid(l, d);
    tstGamPP.changePatch(predValue);
    tstGamtestJP.add(tstGamPP);
    tstGammsgJTA = new JTextArea("Move the slider until the top panel and the lower panel match.\n Click 'Next' when you are done.", 6, 25);
    tstGammsgJTA.setFont(new Font("Arial", Font.BOLD, 12));
    tstGammsgJTA.setForeground(Color.white);
    tstGammsgJTA.setBackground(Color.black);
    tstGamprevJB = new JButton("< prev");
    tstGamprevJB.setEnabled(false);
    tstGamprevJB.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e) {
    currentTest = currentTest - 1;
    if (currentTest == 0) {
    tstGamprevJB.setEnabled(false);
    }
    predValue = Integer.parseInt((String)predictedTestVect.elementAt(currentTest));
    tstGamPP.changePatch(predValue);
    mn = predValue - 40;
    mx = predValue + 40;
    if (mn < 0) {
    mn = 0;
    }
    if (mx > 255) {
    mx = 255;
    }
    userSetSlider = false;
    tstGamSlider.setMinimum(mn);
    userSetSlider = false;
    tstGamSlider.setMaximum(mx);
    userSetSlider = false;
    tstGamSlider.setValue(predValue);
    l = Integer.parseInt((String)ltVector.elementAt(currentTest));
    d = Integer.parseInt((String)dkVector.elementAt(currentTest));
    tstGamPP.changeGrid(l, d);
    if (currentTest == 5) {
    tstGamnextJB.setEnabled(true);
    }
    }
    });
    tstGamnextJB = new JButton("next >");
    tstGamnextJB.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e) {
    currentTest = currentTest + 1;
    if (currentTest <= 6){
    predValue = Integer.parseInt((String)predictedTestVect.elementAt(currentTest));
    }
    tstGamPP.changePatch(predValue);
    mn = predValue - 40;
    mx = predValue + 40;
    if (mn < 0) {
    mn = 0;
    }
    if (mx > 255) {
    mx = 255;
    }
    userSetSlider = false;
    tstGamSlider.setMinimum(mn);
    userSetSlider = false;
    tstGamSlider.setMaximum(mx);
    userSetSlider = false;
    tstGamSlider.setValue(predValue);
    tstGamnextJB.setEnabled(false);
    l = Integer.parseInt((String)ltVector.elementAt(currentTest));
    d = Integer.parseInt((String)dkVector.elementAt(currentTest));
    tstGamPP.changeGrid(l, d);
    if (currentTest == 1) {
    tstGamprevJB.setEnabled(true);
    }
    }
    });
    tstGamfinJB = new JButton("finished");
    tstGamfinJB.setEnabled(false);
    tstGamfinJB.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e) {
    tstGamJDiag.hide();
    }
    });
    tstGamSlider.setValue(predValue);
    mn = predValue - 60;
    mx = predValue + 60;
    if (mn < 0) {
    mn = 0;
    }
    if (mx > 255) {
    mx = 255;
    }
    tstGamSlider.setMinimum(mn);
    userSetSlider = false;
    tstGamSlider.setMaximum(mx);
    userSetSlider = false;
    tstGamnextJB.setEnabled(false);
    btnBx = Box.createHorizontalBox();
    btnBx.add(tstGamprevJB);
    btnBx.add(Box.createHorizontalStrut(15));
    btnBx.add(tstGamnextJB);
    btnBx.add(Box.createHorizontalStrut(15));
    btnBx.add(tstGamfinJB);
     */
    hBox = Box.createHorizontalBox();
    hBox.add(tstGamtestJP);
    hBox.add(Box.createHorizontalStrut(50));
    hBox.add(tstGamSlider);

    vBox = Box.createVerticalBox();
    vBox.add(Box.createVerticalStrut(40));
    vBox.add(hBox);
//  vBox.add(Box.createVerticalStrut(50));
//  vBox.add(btnBx);
//  vBox.add(Box.createVerticalStrut(30));
//  vBox.add(tstGammsgJTA);
    tstGamJP.add(vBox);

    validate();
  }

  public void fillpredictedTestVect() {


    predictedTestVect.addElement(gmaVect.elementAt(16));
    predictedTestVect.addElement(gmaVect.elementAt(48));
    predictedTestVect.addElement(gmaVect.elementAt(80));
    predictedTestVect.addElement(gmaVect.elementAt(112));
    predictedTestVect.addElement(gmaVect.elementAt(144));
    predictedTestVect.addElement(gmaVect.elementAt(176));
    predictedTestVect.addElement(gmaVect.elementAt(208));
  }

  public void filldkVector() {

    dkVector.addElement("0");
    dkVector.addElement("32");
    dkVector.addElement("32");
    dkVector.addElement("64");
    dkVector.addElement("64");
    dkVector.addElement("128");
    dkVector.addElement("192");

  }

  public void fillltVector() {

    ltVector.addElement("32");
    ltVector.addElement("64");
    ltVector.addElement("128");
    ltVector.addElement("160");
    ltVector.addElement("224");
    ltVector.addElement("224");
    ltVector.addElement("224");

  }

  public void fillerrorVect() {

    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");
    errorVect.addElement("0");

  }

  public Vector getErrorVector() {
    return errorVect;
  }
}

//===========================
//Class PixelPanel
//===========================
class PixelPanel extends JPanel {

  Color dark, light, patchColor;
  Rectangle2D patch;

  public PixelPanel() {

    setBackground(Color.black);
    patch = new Rectangle2D.Double(50, 50, 100, 100);
    light = Color.red;
    dark = Color.green;
    patchColor = Color.blue;
  }

  public void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g;
    super.paintComponent(g2);

    Dimension size = getSize();
    float paneWidth = (float) (size.getWidth());
    float paneHeight = (float) (size.getHeight());
    int cols = (int) paneWidth;
    int rows = (int) paneHeight;

    for (int ra = 0; ra <= rows; ra = ra + 2) {
      g2.setPaint(light);
      g2.draw(new Line2D.Double(0, ra, cols, ra));
    }

    for (int rb = 1; rb <= rows; rb = rb + 2) {
      g2.setPaint(dark);
      g2.draw(new Line2D.Double(0, rb, cols, rb));
    }

    g2.setPaint(patchColor);
    g2.fill(patch);

  }

  public void changePatch(int pcolor) {
    patchColor = new Color(pcolor, pcolor, pcolor);
    repaint();
  }

  public void changeGrid(int lg, int dg) {

    light = new Color(lg, lg, lg);
    dark = new Color(dg, dg, dg);
    repaint();

  }
}

