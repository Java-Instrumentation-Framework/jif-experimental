package edu.mbl.jif.gui.plot;

import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;


public class PlotXYPanel
      extends JPanel
{
   int width = 200;
   int height = 75;
   int[][] data = new int[2][100];
   int maxX = 10;
   int maxY = 255;
   String maxStr;
   int last = 0;

   public PlotXYPanel () {
      super();
   }


   public void plotPoint (int x, int y) {
      data[0][last] = x;
      data[1][last] = y;
      last++;
      repaint();
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
      float slope_x = (float) width / (float) maxX;
      float slope_y = (float) height / (float) maxY;
      int x = 0;
      int y = 0;
      //      g2D.setColor(Color.black);
      //      g2D.drawLine(0, height - 10, width, height - 10);
      //for (int i = 1; i < length; i++) {
      for (int i = 0; i < last; i++) {
         x = (int) ((Math.log((double) data[0][i]) + 5) * slope_x);
         y = (int) (data[1][i] * slope_y);
         g2D.setColor(Color.blue);
         g2D.drawArc(x, y, 2, 2, 0, 360);
      }
      g.dispose();
      g2D.dispose();
   }
   }
