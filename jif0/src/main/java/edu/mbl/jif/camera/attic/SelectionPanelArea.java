package edu.mbl.jif.camera.attic;

import edu.mbl.jif.camera.*;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

//import psj.Camera.*;
//import edu.mbl.jif.utils.Prefs; import psj.Utils.*;

// Selects an area from the display frame to be used to change the ROI
public class SelectionPanelArea extends JLabel {
  Rectangle currentRect = null;
  Rectangle rectToDraw = null;
  Rectangle previousRectDrawn = new Rectangle();
  VideoPanel2 controller;
  double sX = 1.0;
  double sY = 1.0;

  public SelectionPanelArea() {};

  public SelectionPanelArea(VideoPanel2 controller) {
    super(); //This component displays an image.
    this.controller = controller;

    //setOpaque(true);
    //setMinimumSize(new Dimension(10, 10));      //don't hog space
    MyListener myListener = new MyListener();
    addMouseListener(myListener);
    addMouseMotionListener(myListener);
  }

  void updateDrawableRect(int compWidth, int compHeight) {
    int x = currentRect.x;
    int y = currentRect.y;
    int width = currentRect.width;
    int height = currentRect.height;

    //Make the width and height positive, if necessary.
    if (width < 0) {
      width = 0 - width;
      x = x - width + 1;
      if (x < 0) {
        width += x;
        x = 0;
      }
    }
    if (height < 0) {
      height = 0 - height;
      y = y - height + 1;
      if (y < 0) {
        height += y;
        y = 0;
      }
    }

    //The rectangle shouldn't extend past the drawing area.
    if ((x + width) > compWidth) {
      width = compWidth - x;
    }
    if ((y + height) > compHeight) {
      height = compHeight - y;
    }

    //Update rectToDraw after saving old value.
    if (rectToDraw != null) {
      previousRectDrawn.setBounds(
        rectToDraw.x, rectToDraw.y, rectToDraw.width, rectToDraw.height);
      rectToDraw.setBounds(x, y, width, height);
    } else {
      rectToDraw = new Rectangle(x, y, width, height);
    }
  }

  public void reScale(double _sX, double _sY) {
    sX = _sX;
    sY = _sY;
  }

  class MyListener extends MouseInputAdapter {

    public void mousePressed(MouseEvent e) {
     int x = (int) (e.getX() / sX);
      int y = (int) (e.getY() / sY);
      currentRect = new Rectangle(x, y, 0, 0);
      updateDrawableRect(getWidth(), getHeight());
      //repaint();
      controller.rectChanged(rectToDraw);
    }

    public void mouseDragged(MouseEvent e) {
      updateSize(e);
    }

    public void mouseReleased(MouseEvent e) {
      updateSize(e);
      controller.roiChanged();
    }

    public void mouseExited(MouseEvent e) {
      controller.valuePoint(-1, -1);
    }

    void updateSize(MouseEvent e) {
      int x = (int) (e.getX() / sX);
      int y = (int) (e.getY() / sY);
      //System.out.println("X/Y: " + x + "/" + y);
      currentRect.setSize(x - currentRect.x, y - currentRect.y);
      updateDrawableRect(getWidth(), getHeight());
      //System.out.println("SA w,h: " + getWidth() +","+ getHeight());
      controller.rectChanged(rectToDraw);
    }

    /////////////////////////////////////////////////////////
    // shows the value of the pixel under the cursor
    public void mouseMoved(MouseEvent e) {
      valuePoint(e);
    }

    void valuePoint(MouseEvent e) {
      int x = (int) (e.getX() / sX);
      int y = (int) (e.getY() / sY);
      controller.valuePoint(x, y);
    }
  }
}
