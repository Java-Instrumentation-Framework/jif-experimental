/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.icon;

import edu.mbl.jif.utils.StaticSwingUtils;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Simple Class to wrap a Shape into an Icon, filled only, may add Stroke and Stroke's Paint for
 * hollow shapes later. Set antiAlias to control how to paint, true by default.
 *
 * @author www.peterbuettner.de
 */
public class ShapeIcon implements Icon {

   private Shape shape;
   private Paint paint;
   private int w, h;
   private boolean antiAlias = true;

   /**
    * The shape is translated to have it's bounding rect at (0,0)
    *
    * @param shape is cloned
    * @param paint fill color
    */
   public ShapeIcon(Shape shape, Paint paint) {
      this.paint = paint;

      Rectangle2D b = shape.getBounds2D();
      this.shape = translateShape(shape, -b.getMinX(), -b.getMinY());//clones

      w = (int) Math.ceil(b.getWidth());
      h = (int) Math.ceil(b.getHeight());
   }

   /**
    * The shape is translated to have it's bounding rect at (0,0), it is scaled to fit size
    *
    * @param shape is cloned
    * @param paint fill color
    * @param size shape is scaled to this size
    */
   public ShapeIcon(Shape shape, Paint paint, int size) {
      this(shape, paint, size, size);
   }

   /**
    * The shape is translated to have it's bounding rect at (0,0), it is scaled to fit width and
    * height
    *
    * @param shape is cloned
    * @param paint fill color
    * @param width
    * @param height
    */
   public ShapeIcon(Shape shape, Paint paint, int width, int height) {
      // dont call this(...) we save shape.getBounds2D and some ...
      this.paint = paint;

      Rectangle2D b = shape.getBounds2D();
      this.shape = translateShape(shape, -b.getMinX(), -b.getMinY());//clones

      // scale shape
      // the 'last pixel' is cut, maybe because float (transform, generalPath)
      // uses only 22 bits (~7 decimals) for precision, so
      // subtract a small value, nobody will see this.
      // to evaluate the mistake draw() the shape not only fill() it
      double s = 0.001;
      this.shape = scaleShape(this.shape, (width - s) / b.getWidth(), (height - s) / b.getHeight());
      w = width;
      h = height;
   }

   public int getIconWidth() {
      return w;
   }

   public int getIconHeight() {
      return h;
   }

   public void paintIcon(Component c, Graphics g, int x, int y) {
      Graphics2D g2 = (Graphics2D) g;
      Object aa = null;
      if (antiAlias) { // aa will be null if it was set before 
         aa = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
         if (aa != RenderingHints.VALUE_ANTIALIAS_ON) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         } else {
            aa = null;
         }
      }
      Paint p = g2.getPaint();
      g2.setPaint(paint);
      g2.fill(translateShape(shape, x, y));
      g2.setPaint(p);
      if (aa != null) {
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aa);
      }
   }

   private static Shape translateShape(Shape s, double dx, double dy) {
      return AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(s);
   }

   private static Shape scaleShape(Shape s, double sx, double sy) {
      return AffineTransform.getScaleInstance(sx, sy).createTransformedShape(s);
   }

   public boolean isAntiAlias() {
      return antiAlias;
   }

   public void setAntiAlias(boolean antiAlias) {
      this.antiAlias = antiAlias;
   }

   public static void main(String[] args) {

      StaticSwingUtils.QuickFrame f = new StaticSwingUtils.QuickFrame("ShapeIcon");
      f.setLayout(new FlowLayout());
      int iSize = 14;
      Shape line = AffineTransform.getRotateInstance(-3.14 / 8).
              createTransformedShape(new Rectangle2D.Double(0, 0, 10, 1));
      f.add(new JLabel("Symbols:"));
      f.add(new JButton("Control", new ShapeIcon(getShapeTriangle(), Color.RED, iSize)));
      //
      f.add(new JButton("Handles", new ShapeIcon(getShapeLineDiag(), Color.RED, iSize)));
      //
      f.add(new JButton("Base", new ShapeIcon(getShapeCross(), Color.BLUE, iSize)));
      f.pack();
      f.setVisible(true);
   }

   static Shape getShapeLineDiag() {// screen units ~ pixels
      Stroke ctrlStroke =
              new BasicStroke(2f, BasicStroke.CAP_SQUARE,
              BasicStroke.JOIN_MITER);
//      Stroke ctrlStroke =
//              new BasicStroke(0.7f, BasicStroke.CAP_SQUARE, 
//              BasicStroke.JOIN_MITER, 1, new float[]{1, 3}, 0);
      Shape s = ctrlStroke.createStrokedShape(new Line2D.Double(0, 0, 10, -10));
      return s;
   }

   static Shape getShapeTriangle() {// screen units ~ pixels
      GeneralPath gp = new GeneralPath(); // triangle 
      gp.moveTo(-0.5f, 0);
      gp.lineTo(0.5f, 0);
      gp.lineTo(0, -1);
      gp.closePath();
      return AffineTransform.getScaleInstance(7, 7).createTransformedShape(
              translateShape(gp, 0, 0.4f));
   }// ---------------------------------------------------------------

   static Shape getShapeCross() {// screen units ~ pixels
      GeneralPath gp = new GeneralPath(); // cross
      gp.append(new Rectangle2D.Double(-8, -1.5, 16, 3), false);
      gp.append(new Rectangle2D.Double(-1.5, -8, 3, 16), false);
      return AffineTransform.getScaleInstance(0.6, 0.6).createTransformedShape(gp);
   }
}