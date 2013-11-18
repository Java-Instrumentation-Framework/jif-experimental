package edu.mbl.jif.gui.plot;

import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;


// PlotPanel
//
public class PlotPanel
      extends JPanel
{
   int width = 200;
   int height = 75;
   int[] data = new int[200];
   int max = 100;
   String maxStr;
   int last = 0;

   public PlotPanel () {
      super();
   }


   public void plotPoint (double diff) {
      data[last] = (int) (max * (diff / 255)) + 2;
      last++;
      repaint();
   }


   public void setLast (int x) {
      last = x;
   }


   public synchronized void paintComponent (Graphics g) {
      Graphics2D g2D = null;
      if (g instanceof Graphics2D) {
         g2D = (Graphics2D) g;
      } else {
         return;
      }
      int width = getSize().width;
      int height = getSize().height;
      g2D.setColor(getBackground());
      g2D.fillRect(0, 0, width, height);
      if (data == null) {
         return;
      }
      g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
      g2D.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
      g2D.setColor(Color.white);
      int length = data.length;
      float slope_x = (float) width / (float) (length - 1);
      float slope_y = (float) height / (float) max;
      int x0 = 0;
      int y0 = (int) ((float) data[0] * slope_y);
      //      g2D.setColor(Color.black);
      //      g2D.drawLine(0, height - 10, width, height - 10);
      //for (int i = 1; i < length; i++) {
      for (int i = 1; i < last; i++) {
         int x = (int) ((float) i * slope_x);
         int y = (int) ((float) data[i] * slope_y);
         //g2D.setColor(Color.black);
         //g2D.drawLine(x0, height, x0, 0);
         g2D.setColor(Color.blue);
         g2D.drawLine(x0, height - y0, x, height - y);
         x0 = x;
         y0 = y;
      }
      g.dispose();
      g2D.dispose();
   }
}
