/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.ps.orient;

import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author GBH
 */
public class OrientationGlyphs {

	enum Type {

		FAN, ELLIPSE, LINE
	}
	// drawFanGlyph(    ZoomGraphics g, float x, float y, float angle, float length, float dAngle) 
	// drawEllipseGlyph(ZoomGraphics g, float x, float y, float angle, float length, float dAngle)
	// drawLineGlyph(   ZoomGraphics g, float x, float y, float angle, float length, float width)

	// TODO: if dAngle too small, it disappears... use a line instead.
	
	public void drawFanGlyph(ZoomGraphics g, float x, float y, float angle, float length, float dAngle) {
				float overlap = 2f;
		Arc2D.Float arc1 = createArcAt(-overlap, 0, length / 2 + overlap, -dAngle / 2, dAngle);
		Arc2D.Float arc2 = createArcAt(overlap, 0, length / 2 + overlap, 180f - dAngle / 2, dAngle);
		Area fanArea = new Area(arc1);
		Area area2 = new Area(arc2);	
		fanArea.add(area2);
		Rectangle2D rect = fanArea.getBounds2D();
		float cX = (float) rect.getCenterX();
		float cY = (float) rect.getCenterY();
		Shape rotatedFan = rotateShape(fanArea, -angle, cX, cY);
		Shape xlated = createTranslatedShape(rotatedFan, x, y);
		g.fill(xlated);
		
	}
//	public void drawFanGlyph(ZoomGraphics g, float x, float y, float angle, float length, float dAngle) {
//		g.translate((double) x, (double) y);
//		g.rotate(angle);
//		Area fanArea = createFanArea(length, dAngle);
//		g.fill(fanArea);
//		g.rotate(-angle);
//		g.translate((double) -x, (double) -y);
//	}

//	private Area createFanArea(float r, float dAngle) {
//		float overlap = 2f;
//		Arc2D.Float arc1 = createArcAt(-overlap, 0, r / 2 + overlap, -dAngle / 2, dAngle);
//		Arc2D.Float arc2 = createArcAt(overlap, 0, r / 2 + overlap, 180f - dAngle / 2, dAngle);
//		Area fanArea = new Area(arc1);
//		Area area2 = new Area(arc2);	
//		Rectangle2D rect = area2.getBounds2D();
//		float cX = (float) rect.getCenterX();
//		float cY = (float) rect.getCenterY();
//		Shape rotatedFan = rotateShape(area2, -angle, cX, cY);
//		Shape xlated = createTranslatedShape(rotatedEllipse, x, y);
//		return xlated;
//	}
//		fanArea.add(area2);
//		return fanArea;
//	}

	private Arc2D.Float createArcAt(float x, float y, float r, float angle0, float angle1) {
		//float d = (float) (r / Math.sqrt(2));
		float d = r;
		float x0 = x - d;
		float y0 = y - d;
		float w = 2 * d;
		return new Arc2D.Float(x0, y0, w, w, angle0, angle1, Arc2D.PIE);
	}

	// Ellipse Glyph...
	public void drawEllipseGlyph(ZoomGraphics g, float x, float y, float angle, float length, float dAngle) {
		Shape elli = createEllipseAt(x, y, angle, length, dAngle);
		g.fill(elli);
	}

	private Shape createEllipseAt(float x, float y, float angle, float length, float dAngle) {
		float eccentricity = 1f * dAngle;
		Ellipse2D.Float ellipse = new Ellipse2D.Float(
				0 - length / 2, -length * eccentricity / 2,
				length, length * eccentricity);
		Rectangle2D rect = ellipse.getBounds2D();
		float cX = (float) rect.getCenterX();
		float cY = (float) rect.getCenterY();
		Shape rotatedEllipse = rotateShape(ellipse, -angle, cX, cY);
		Shape xlated = createTranslatedShape(rotatedEllipse, x, y);
		return xlated;
	}

	public static Shape rotateShape(final Shape base, final float angle,
			final float x, final float y) {
		if (base == null) {
			return null;
		}
		//float angle = angleDeg * (float) Math.PI / 180;
		final AffineTransform rotate = AffineTransform.getRotateInstance(
				angle, x, y);
		final Shape result = rotate.createTransformedShape(base);
		return result;
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
//
//	private Ellipse2D.Float createEllipseAt(float x, float y, float r, float angle0, float angle1) {
//		float d = (float) (r / Math.sqrt(2));
//		float x0 = x - d;
//		float y0 = y - d;
//		float w = 2 * d;
//		return new Ellipse2D.Float(x0, y0, w, w);
//	}

	public void drawLineGlyph(ZoomGraphics g, float x, float y, float angle, float length, float width) {
		g.translate((double) x, (double) y);
		g.rotate(angle);
		g.setStroke(new BasicStroke(width));
//		try {
//			g.setStroke(new TransformedStroke(new BasicStroke(2f),
//					g.getTransform()));
//		} catch (NoninvertibleTransformException ex) {
//			// should not occur if width and height > 0
//			ex.printStackTrace();
//		}
		Line2D.Float line = new Line2D.Float(-length / 2, 0, length / 2, 0);
//				0 - length / 2, -length * eccentricity / 2,
//				length, length * eccentricity);
		g.draw(line);
		g.rotate(-angle);
		g.translate((double) -x, (double) -y);
	}

}
