package edu.mbl.jif.gui.j2d;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon;

/**
 * Simple Class to wrap a Shape into an Icon, filled only,
 * may add Stroke and Stroke's Paint for hollow shapes later.
 * Set antiAlias to control how to paint, true by default. 
 * 
 * @author www.peterbuettner.de
 */
public class ShapeIcon implements Icon{
	private Shape shape;
	private Paint paint; 
	private int w,h;
	private boolean antiAlias = true;
/**
 * The shape is translated to have it's bounding rect at (0,0)
 * @param shape is cloned
 * @param paint fill color
 */	
public ShapeIcon(Shape shape, Paint paint){
	this.paint= paint;

	Rectangle2D b = shape.getBounds2D();
	this.shape = translateShape(shape, -b.getMinX(), -b.getMinY());//clones

	w = (int) Math.ceil( b.getWidth());
	h = (int) Math.ceil( b.getHeight());
	}
/**
 * The shape is translated to have it's bounding rect at (0,0),
 * it is scaled to fit width and height
 * @param shape is cloned
 * @param paint fill color
 * @param width
 * @param height
 */	
public ShapeIcon(Shape shape, Paint paint, int width, int height){
	// dont call this(...) we save shape.getBounds2D and some ...
	this.paint= paint;

	Rectangle2D b = shape.getBounds2D(); 
	this.shape = translateShape(shape, -b.getMinX(), -b.getMinY());//clones

	// scale shape
	// the 'last pixel' is cut, maybe because float (transform, generalPath)
	// uses only 22 bits (~7 decimals) for precision, so
	// subtract a small value, nobody will see this.
	// to evaluate the mistake draw() the shape not only fill() it
	double s = 0.001;
	this.shape = scaleShape(this.shape, (width-s)/b.getWidth(),(height-s)/b.getHeight() );
	w = width;
	h = height;
}
/**
 * The shape is translated to have it's bounding rect at (0,0),
 * it is scaled to fit size
 * @param shape is cloned
 * @param paint fill color
 * @param size shape is scaled to this size
 */	
public ShapeIcon(Shape shape, Paint paint, int size){this(shape, paint, size, size);}


public int getIconWidth() {return w;}
public int getIconHeight() {return h;}

public void paintIcon(Component c, Graphics g, int x, int y) {
	Graphics2D g2 = (Graphics2D) g;
	Object aa = null; 
	if(antiAlias){ // aa will be null if it was set before 
		aa = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		if(aa != RenderingHints.VALUE_ANTIALIAS_ON)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		else 
			aa=null;
		}
	Paint p = g2.getPaint();
	g2.setPaint(paint);
	g2.fill(translateShape(shape, x, y));
	g2.setPaint(p);
	if(aa!=null)g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aa);
	}

private static Shape translateShape(Shape s, double dx, double dy) {
	return AffineTransform.getTranslateInstance(dx,dy).createTransformedShape(s);
}
private static Shape scaleShape(Shape s, double sx, double sy) {
	return AffineTransform.getScaleInstance(sx,sy).createTransformedShape(s);
}
	

public boolean isAntiAlias() {return antiAlias;}
public void setAntiAlias(boolean antiAlias) {this.antiAlias = antiAlias;}
}