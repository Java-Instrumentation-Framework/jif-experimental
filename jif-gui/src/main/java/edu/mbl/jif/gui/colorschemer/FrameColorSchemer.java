package edu.mbl.jif.gui.colorschemer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class FrameColorSchemer
    extends JFrame {
  JPanel contentPane;
  int n = 9;
  String[] name = {
      "red", "orange", "yellow", "green", "aqua", "blue", "purple", "violet",
      "fusia"};
  int[] hues = {
      0, 32, 50, 120, 160, 230, 275, 300, 345};
  Apanel[] panes = new Apanel[n];
  float hueShift = 0f;
  float saturation = 0.0f;

  float v0 = 0.3f;
  float v1 = 0.6f;
  float v2 = 0.7f;
  float v3 = 0.8f;
  float v4 = 0.9f;
  JPanel jPanel1 = new JPanel();
  JSlider jSlider1 = new JSlider();
  JSlider jSlider2 = new JSlider();

  //Construct the frame
  public FrameColorSchemer() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    setTitle("ColorSchemer");
    setSize(new Dimension(449, 800));
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.setBackground(Color.lightGray);
float h = 0;
//
for (int i = 0; i < n; i++) {
  panes[i] = new Apanel(name[i]);
  panes[i].setPreferredSize(new Dimension(273, 125));

  h = (float) hues[i] / 360.0f;
//      h = ( (float) i / n) + 0.1f;
//      h = (h > 0.99999f) ? 0.99999f : h;
  panes[i].setHueSat(h, saturation);
  panes[i].setV(v0, v1, v2, v3, v4);
  contentPane.add(panes[i]);
}
    jSlider1.addChangeListener(new Frame0_jSlider1_changeAdapter(this));
    jSlider2.addChangeListener(new Frame0_jSlider2_changeAdapter(this));
    contentPane.add(jPanel1, null);
    jPanel1.add(jSlider1, null);
    jPanel1.add(jSlider2, null);
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void jSlider1_stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    if (!source.getValueIsAdjusting()) {
      int v = (int) source.getValue();
      setNewHue(v);
      System.out.println(v);
    }
  }

  private void setNewHue(int v) {
    hueShift = (float) v / n / 100.0f;
    for (int i = 0; i < n; i++) {
      float h = hues[i] / 360f + hueShift;
      h = (h > 0.99999f) ? 0.99999f : h;
      panes[i].setHueSat(h, saturation);
      panes[i].setV(v0, v1, v2, v3, v4);
    }
    repaint();
  }

  void jSlider2_stateChanged(ChangeEvent e) {
    JSlider source = (JSlider) e.getSource();
    if (!source.getValueIsAdjusting()) {
      int v = (int) source.getValue();
      setNewSat(v);
      System.out.println("sat: " + v);
    }
  }

  private void setNewSat(int v) {
    for (int i = 0; i < n; i++) {
      saturation = (float) v / 100f;
      float h = hues[i] / 360f + hueShift;
      h = (h > 0.99999f) ? 0.99999f : h;
      panes[i].setHueSat(h, saturation);
      panes[i].setV(v0, v1, v2, v3, v4);
    }
    repaint();

  }


}

class Frame0_jSlider1_changeAdapter
    implements javax.swing.event.ChangeListener {
  FrameColorSchemer adaptee;

  Frame0_jSlider1_changeAdapter(FrameColorSchemer adaptee) {
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e) {
    adaptee.jSlider1_stateChanged(e);
  }
}

class Frame0_jSlider2_changeAdapter
    implements javax.swing.event.ChangeListener {
  FrameColorSchemer adaptee;

  Frame0_jSlider2_changeAdapter(FrameColorSchemer adaptee) {
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e) {
    adaptee.jSlider2_stateChanged(e);
  }
}
