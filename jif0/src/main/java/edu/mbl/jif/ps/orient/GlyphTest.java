/*
 * GBH, 2013
 */
package edu.mbl.jif.ps.orient;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.*;
//import org.jfree.ui.RectangleAnchor;

public class GlyphTest
		extends JComponent
		implements Runnable {

	ArrayList<Point2D> pts;
	OrientationGlyphs og;

	public GlyphTest() {
		float increment = 20f;
		pts = generateGridCoordList(1, 1, increment);
		og = new OrientationGlyphs();
		(new Thread(this)).start();
	}

	public void run() {
		try {
			for (;;) {
				Thread.sleep(500);
				repaint();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	public void paint(Graphics graphics) {
		super.paint(graphics);

		Graphics2D g = (Graphics2D) graphics;
//		ZoomGraphics g = (ZoomGraphics) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		size = getSize(size);
//		insets = getInsets(insets);
//		int radius = Math.min(size.width - insets.left - insets.top,
//				size.height - insets.top - insets.bottom) / 2;
//		//g.translate((double) size.width / 2D, (double) size.height / 2D);
//		g.translate(100, 100);

		// Set transparency...
		Composite originalComposite = g.getComposite();
		float alpha = 0.5f;
		g.setComposite(makeComposite(alpha));
		
		//
		g.setColor(Color.blue);
		Shape elli = createEllipseAt(100f, 100f, 0f, 50f, 0.2f); 		g.fill(elli);
		elli = createEllipseAt(200f, 100f, 90f, 50f, 0.2f); 		g.fill(elli);
		elli = createEllipseAt(300f, 100f, 90f, 50f, 0.5f); 		g.fill(elli);
		
		
		
		//
		//g.translate((double) size.width / 20D, (double) size.height / 2D);
//		g.setColor(Color.DARK_GRAY);
//		//
//		float angle = 0;
//		float extent = .1f;
//		float dAngle = extent * 180f;
//		//float startAngle = angle - dAngle / 2;
//		float length = radius / 20;
//		int i = 1;
//		int n = pts.size();
//		for (Point2D pt : pts) {
//			angle = 2 * i;
//			extent = .1f; // *(float) i;
//			dAngle = extent * 180f;
//			float width = extent * 10f;
//			//startAngle = angle - dAngle / 2;
//			//length = radius / i + 1;
//			//length = radius / 6;
//			float x = (float) pt.getX();
//			float y = (float) pt.getY();
////			g.setColor(Color.red);
////			og.drawFanGlyph(g, x, y, angle, length, dAngle);
//			//
//
//			g.setColor(Color.blue);
//			Shape elli = createEllipseAt(x, y, angle, length, dAngle);
//			//g.draw(elli);
//			g.fill(elli);
//			
//			i++;
			//
//		}
	}

//	public  void drawEllipseGlyph(ZoomGraphics g, float x, float y, float angle, float length, float dAngle) {
//		g.translate((double) x, (double) y);
//		g.rotate(angle);
//		float eccentricity = 1f * dAngle;
//		Ellipse2D.Float ellip = new Ellipse2D.Float(
//				0 - length / 2, -length * eccentricity / 2,
//				length, length * eccentricity);
//		g.fill(ellip);
//		g.rotate(-angle);
//		g.translate((double) -x, (double) -y);
//	}
//	private Area createEllipseArea(float r, float dAngle) {
//		Ellipse2D.Float ellip1 = createEllipseAt(0, 0, r + 2, -dAngle / 2, dAngle / 2);
//		Area ellipArea = new Area(ellip1);
//		return ellipArea;
//	}
	private Shape createEllipseAt(float x, float y, float angle, float length, float dAngle) {
		float eccentricity = 1f * dAngle;
		Ellipse2D.Float ellipse = new Ellipse2D.Float(
				0 - length / 2, -length * eccentricity / 2,
				length, length * eccentricity);
		Rectangle2D rect = ellipse.getBounds2D();
		float cX = (float) rect.getCenterX();
		float cY = (float) rect.getCenterY();
		Shape rotatedEllipse = rotateShape(ellipse, angle, cX, cY);
		Shape xlated = createTranslatedShape(rotatedEllipse, x, y);
		return xlated;
	}

	private static Stroke SEC_STROKE = new BasicStroke();
	private Dimension size = null;
	private Insets insets = new Insets(0, 0, 0, 0);

	public static void main(String[] args) {
		JFrame f = new JFrame("Glyph Test");
		GlyphTest clock = new GlyphTest();
		f.getContentPane().add(clock);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		f.setBounds(50, 50, 600, 700);
		f.setVisible(true);
	}

	static ArrayList<Point2D> generateGridCoordList(int nX, int nY, float increment) {
		ArrayList<Point2D> pts = new ArrayList<Point2D>();
		for (int i = 0; i < nX; i++) {
			for (int j = 0; j < nY; j++) {
				float x = i * increment;
				float y = j * increment;
				pts.add(new Point2D.Float(x, y));
				System.out.println("= " + x + ", " + y);
			}

		}
		return pts;

	}

	public static Shape createTranslatedShape(final Shape shape,
			final double transX,
			final double transY) {
		if (shape == null) {
			throw new IllegalArgumentException("Null 'shape' argument.");
		}
		final AffineTransform transform = AffineTransform.getTranslateInstance(
				transX, transY);
		return transform.createTransformedShape(shape);
	}

	/**
	 * Translates a shape to a new location such that the anchor point (relative to the rectangular
	 * bounds of the shape) aligns with the specified (x, y) coordinate in Java2D space.
	 *
	 * @param shape the shape (<code>null</code> not permitted).
	 * @param anchor the anchor (<code>null</code> not permitted).
	 * @param locationX the x-coordinate (in Java2D space).
	 * @param locationY the y-coordinate (in Java2D space).
	 *
	 * @return A new and translated shape.
	 */
	public static Shape createTranslatedShape(final Shape shape,
			final RectangleAnchor anchor,
			final double locationX,
			final double locationY) {
		if (shape == null) {
			throw new IllegalArgumentException("Null 'shape' argument.");
		}
		if (anchor == null) {
			throw new IllegalArgumentException("Null 'anchor' argument.");
		}
		Point2D anchorPoint = RectangleAnchor.coordinates(
				shape.getBounds2D(), anchor);
		final AffineTransform transform = AffineTransform.getTranslateInstance(
				locationX - anchorPoint.getX(), locationY - anchorPoint.getY());
		return transform.createTransformedShape(shape);
	}

	/**
	 * Rotates a shape about the specified coordinates.
	 *
	 * @param base the shape (<code>null</code> permitted, returns <code>null</code>).
	 * @param angle the angle (in radians).
	 * @param x the x coordinate for the rotation point (in Java2D space).
	 * @param y the y coordinate for the rotation point (in Java2D space).
	 *
	 * @return the rotated shape.
	 */
	public static Shape rotateShape(final Shape base, final double angle,
			final float x, final float y) {
		if (base == null) {
			return null;
		}
		final AffineTransform rotate = AffineTransform.getRotateInstance(
				angle, x, y);
		final Shape result = rotate.createTransformedShape(base);
		return result;
	}
	public static Shape rotateShape(final Shape base, final float angleDeg,
			final float x, final float y) {
		if (base == null) {
			return null;
		}
		float angle = angleDeg * (float)Math.PI / 180;
		final AffineTransform rotate = AffineTransform.getRotateInstance(
				angle, x, y);
		final Shape result = rotate.createTransformedShape(base);
		return result;
	}

}