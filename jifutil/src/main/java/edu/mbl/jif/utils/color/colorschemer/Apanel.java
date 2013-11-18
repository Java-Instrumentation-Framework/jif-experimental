package edu.mbl.jif.utils.color.colorschemer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Apanel
    extends JPanel {

  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel5 = new JPanel();
  JPanel jPanel7 = new JPanel();
  JPanel jPanel8 = new JPanel();
  JPanel jPanel6 = new JPanel();
  int vals[] = new int[6];
  final int RED = 0;
  final int GRN = 1;
  final int BLU = 2;
  final int H = 3;
  final int L = 4;
  final int S = 5;

  float hue = 0.0f;
  float sat = 0.5f;
  float value = 0.5f;

  String name;
  JPanel jPanel1 = new JPanel();
  JPanel jPanel9 = new JPanel();
  JSpinner jSpinner1 = new JSpinner();
  JLabel jLabel1 = new JLabel();
  JPanel jPanel10 = new JPanel();

  public Apanel(String name) {
    super();
    this.name = name;
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setLayout(null);
    setBorder(BorderFactory.createEtchedBorder());
    setSize(new Dimension(379, 83));
    setLayout(null);

    jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel2.setBounds(new Rectangle(13, 11, 120, 60));
    jPanel2.setLayout(null);
    jPanel3.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel3.setBounds(new Rectangle(146, 11, 100, 60));
    jPanel3.setLayout(null);
    jPanel4.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel4.setPreferredSize(new Dimension(20, 20));
    jPanel4.setBounds(new Rectangle(17, 7, 25, 15));
    jPanel4.setLayout(null);
    jPanel5.setBorder(BorderFactory.createEtchedBorder());
    jPanel5.setBounds(new Rectangle(63, 7, 30, 41));
    jPanel7.setBorder(BorderFactory.createEtchedBorder());
    jPanel7.setBounds(new Rectangle(16, 9, 59, 29));
    jPanel7.setLayout(null);
    jPanel8.setLayout(null);
    jPanel8.setBounds(new Rectangle(12, 7, 93, 47));
    jPanel8.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel6.setLayout(null);
    jPanel6.setBounds(new Rectangle(8, 7, 33, 43));
    jPanel6.setBorder(BorderFactory.createLoweredBevelBorder());
    jPanel6.setPreferredSize(new Dimension(20, 20));
    jPanel1.setBackground(Color.lightGray);
    jPanel1.setBounds(new Rectangle(45, 7, 15, 41));
    jPanel9.setBounds(new Rectangle(5, 14, 23, 18));
    jPanel9.setBackground(Color.white);
    jLabel1.setText("Hue");
    jLabel1.setBounds(new Rectangle(10, 11, 22, 15));
    jPanel10.setBorder(BorderFactory.createEtchedBorder());
    jPanel10.setBounds(new Rectangle(261, 13, 104, 37));
    jPanel10.setLayout(null);
    jSpinner1.setBounds(new Rectangle(42, 6, 53, 26));
    jPanel3.add(jPanel5, null);
    jPanel3.add(jPanel6, null);
    jPanel6.add(jPanel9, null);
    jPanel3.add(jPanel1, null);
    this.add(jPanel10, null);
    jPanel10.add(jSpinner1, null);
    jPanel10.add(jLabel1, null);
    this.add(jPanel2, null);
    jPanel2.add(jPanel8, null);
    jPanel8.add(jPanel7, null);
    jPanel7.add(jPanel4, null);
    this.add(jPanel3, null);
  }

  public void setHueSat(float _hue, float _sat) {
    hue = _hue;
    sat = _sat;
  }
  public Color HLStoRGB(float h, float l, float s) {
    vals[H] = (int) (h * 255);
    vals[L] = (int) (l * 255);
    vals[S] = (int) (s * 255);
    toRGB(vals);
    return new Color(vals[RED], vals[GRN], vals[BLU]);
  }

  public void setV(float v0, float v1, float v2, float v3, float v4) {
    setBackground(new Color(Color.HSBtoRGB(hue, sat, v0)));
    jPanel2.setBackground(HLStoRGB(hue, v1, sat));
    jPanel3.setBackground(HLStoRGB(hue, v2, sat));
    jPanel8.setBackground(HLStoRGB(hue, v2, sat));
    jPanel5.setBackground(HLStoRGB(hue, v3, sat));
    jPanel7.setBackground(HLStoRGB(hue, v3, sat));
    jPanel6.setBackground(HLStoRGB(hue, v4, sat));
    jPanel4.setBackground(HLStoRGB(hue, v4, sat));
    outColor("0", HLStoRGB(hue, v0, sat));
    outColor("1", HLStoRGB(hue, v1, sat));
    outColor("2", HLStoRGB(hue, v2, sat));
    outColor("3", HLStoRGB(hue, v3, sat));
    outColor("4", HLStoRGB(hue, v4, sat));
  }

  void outColor(String s, Color c) {
    System.out.println( name +"["+s+"]"+
                       " = new Color(" +
                       c.getRed() + ", " +
                       c.getGreen() + ", " +
                       c.getBlue() + ");");
  }

  public double abs(double v) {
    if (v > 0) {
      return v;
    }
    else {
      return -v;
    }
  }

  public double max(double a, double b) {
    if (a > b) {
      return a;
    }
    else {
      return b;
    }
  }

  public double min(double a, double b) {
    if (a < b) {
      return a;
    }
    else {
      return b;
    }
  }

  public double clip(double v) {
    // Clip to 0..1
    return min(1, max(0, v));
  }

// From Computer Graphics Principles and Practice, Foley, van Dam,
// Feiner, Hughes
  public double value(double n1, double n2, double hue) {
    if (hue < 0) {
      hue += 360;
    }
    if (hue > 360) {
      hue -= 360;
    }
    if (hue < 60) {
      return n1 + (n2 - n1) * hue / 60.;
    }
    else if (hue < 180) {
      return n2;
    }
    else if (hue < 240) {
      return n1 + (n2 - n1) * (240 - hue) / 60.;
    }
    else {
      return n1;
    }
  }

  public void toRGB(int vals[]) {
    double h, l, s;
    double r, g, b;
    h = vals[H] / 255. * 360;
    l = vals[L] / 255.;
    s = vals[S] / 255.;
    double m1, m2;
    if (l < .5) {
      m2 = l * (1 + s);
    }
    else {
      m2 = l + s - l * s;
    }
    m1 = 2 * l - m2;
    if (s == 0) {
      r = l;
      g = l;
      b = l;
    }
    else {
      r = value(m1, m2, h + 120);
      g = value(m1, m2, h);
      b = value(m1, m2, h - 120);
    }
    vals[RED] = (int) (r * 255);
    vals[GRN] = (int) (g * 255);
    vals[BLU] = (int) (b * 255);
  }

  public void fromRGB(int vals[]) {
    double r, g, b;
    double h, l, s;
    r = vals[RED] / 255.;
    g = vals[GRN] / 255.;
    b = vals[BLU] / 255.;
    double min = min(min(r, g), b);
    double max = max(max(r, g), b);
    l = (max + min) / 2;
    if (max == min) {
      s = 0;
      h = 0;
    }
    else {
      if (l <= 0.5) {
        s = (max - min) / (max + min);
      }
      else {
        s = (max - min) / (2 - max - min);
      }
      double delta = max - min;
      if (r == max) {
        h = (g - b) / delta;
      }
      else if (g == max) {
        h = 2 + (b - r) / delta;
      }
      else {
        h = 4 + (r - g) / delta;
      }
      h /= 6.;
      if (h < 0) {
        h += 1;
      }
    }
    vals[H] = (int) (h * 255);
    vals[L] = (int) (l * 255);
    vals[S] = (int) (s * 255);
  }

}