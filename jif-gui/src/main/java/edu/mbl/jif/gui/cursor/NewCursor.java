package edu.mbl.jif.gui.cursor;

import java.awt.*;

// UNUSED as of Jan 2005

public class NewCursor extends Frame {
    
  public NewCursor() {
    this.resize(100, 100);
    this.setVisible(true);
    Image image = this.createImage(30, 30);
    drawCursor(image.getGraphics());
    Point  point = new Point(0, 0);
    String string = new String("myCursor");
    Cursor cursor = this.getToolkit().createCustomCursor(image, point, string);
    this.setCursor(cursor);
  }

  private void drawCursor(Graphics g) {
    g.setColor(new Color(0, 255, 0));
    g.fillRect(0, 0, 30, 30);
    g.setColor(new Color(0, 0, 0));
    g.drawLine(1, 1, 10, 20);
    g.drawLine(20, 1, 10, 20);
  }
  public static void main (String[] args) {
     new NewCursor();
  }
}
