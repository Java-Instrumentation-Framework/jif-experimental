package edu.mbl.jif.camera.display;


import javax.swing.*;

import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.media.jai.PlanarImage;
import java.awt.image.BufferedImage;

public class DisplayImage16 extends JPanel {
  int iWidth; // Image dimensions (pixels)
  int iHeight; // Image dimensions (pixels)
  int iSize; // Image dimensions (pixels)
  BufferedImage bImage;
  WritableRaster bRaster;
  Graphics2D buffGraphics;
  Font font = new Font("Arial", Font.PLAIN, 24);
  short[] imageBufferArray;

  public DisplayImage16(int w, int h) {
    this();
    iWidth = w;
    iHeight = h;
    iSize = iWidth * iHeight;
    imageBufferArray = new short[iSize];
    for (int i = 0; i < iSize; i++) {
      imageBufferArray[i] = 0;
    }
    bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_USHORT_GRAY);
    bRaster = bImage.getRaster();
    bRaster.setDataElements(0, 0, iWidth, iHeight, imageBufferArray);
//    buffGraphics = (Graphics2D) bImage.createGraphics();
//    buffGraphics.setFont(font);
    this.setPreferredSize(new Dimension(iWidth, iHeight));
    setVisible(true);
    //this.setBorder(BorderFactory.createLoweredBevelBorder());
  }

  public DisplayImage16() {
    super();
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

  }

  public void updateImage(RenderedImage rImage) {
    bImage = PlanarImage.wrapRenderedImage(rImage).getAsBufferedImage();
    repaint();
  }

  public void updateImage(BufferedImage rImage) {
    bImage.setData(rImage.getRaster());
    repaint();
  }

  public void updateImage(short[] imageData) {
    bRaster.setDataElements(0, 0, iWidth, iHeight, imageData);
    repaint();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Paint
  //
  public synchronized void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    //Graphics2D buffGraphics2 = (Graphics2D) buffGraphics;
//    g2.drawImage(bImage, 3, 3, getWidth() - 3, getHeight() - 3,
//        0, 0, iWidth, iHeight, null);
    g2.drawImage(bImage, 0, 0, iWidth, iHeight, null);
    g2.drawString(String.valueOf(System.currentTimeMillis()), 10,10);
  }

  // Show a message over frozen image in the display
  public void showMessage(String message) {
    Graphics g = getGraphics();
    g.setColor(Color.black);
    g.fillRect(30, (getHeight() / 2) - 16, message.length() * 10, 25);
    g.setColor(Color.white);
    g.drawString(message, 40, getHeight() / 2);
  }


  public static void main(String[] args) {
  }
}
