package edu.mbl.jif.gui.value;

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PanelValuePoint
    extends JPanel {
  GridLayout gridLayout1 = new GridLayout();
  static NumberFormat fmtDec1;
  static NumberFormat fmtDec2;


  ValueLabel valuePixel = new ValueLabel("pixel (x, y)");

  ValueLabel valueStats = new ValueLabel("min < mean < max");
  ValueLabel valueROI = new ValueLabel("ROI");
  ValueLabel valueRetardance = new ValueLabel("Retardance");
  ValueLabel valueAzimuth = new ValueLabel("Azimuth");
  static {
    fmtDec1 = NumberFormat.getNumberInstance();
    fmtDec1.setMinimumFractionDigits(1);
    fmtDec1.setMaximumFractionDigits(1);
    fmtDec2 = NumberFormat.getNumberInstance();
    fmtDec2.setMinimumFractionDigits(2);
    fmtDec2.setMaximumFractionDigits(2);
  }

  private boolean showRetAzim = false;

  public PanelValuePoint() {
    this(0);
  }

  public PanelValuePoint(int show) {
    if (show == 1) {
      showRetAzim = true;
    }
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.setLayout(gridLayout1);
    gridLayout1.setColumns(5);
    gridLayout1.setRows(1);
    this.add(valuePixel, null);
    this.add(valueStats, null);
    this.add(valueROI, null);
    if (showRetAzim) {
      this.add(valueRetardance, null);
      this.add(valueAzimuth, null);
    }
  }

  public void setValuePixel(int v, int x, int y) {
    valuePixel.set(String.valueOf(v) + " (" +
                       String.valueOf(x) + ", " +
                       String.valueOf(y) + ")");
  }

  public void setValueStats(int min, float mean, int max) {
    valueStats.set(String.valueOf(min) + " < " +
                       fmtDec1.format(mean) + " <  " +
                       String.valueOf(max));
  }

  public void setValueROI(int h, int w) {
    valueROI.set("(" + String.valueOf(h) + " x " +
                     String.valueOf(w) + ")");
  }
  public void setValueROI() {
    valueROI.set("none");
  }

  public void setValueRetardance(float ret) {
    valueRetardance.set(fmtDec1.format(ret) + " nm.");
  }

  public void setValueAzimuth(int azim) {
    valueAzimuth.set(String.valueOf(azim) + " deg.");
  }

  public void blankAll() {
    valuePixel.set("");
    valueStats.set("");
    valueROI.set("");
    valueRetardance.set("");
    valueAzimuth.set("");
  }

  public void test() {
    setValuePixel(254, 12, 506);
    setValueStats(5, 34.2f, 123);
    setValueROI(104, 300);
    setValueRetardance(5.4f);
    setValueAzimuth(135);
  }
}
