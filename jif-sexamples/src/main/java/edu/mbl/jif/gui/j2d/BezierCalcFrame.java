package edu.mbl.jif.gui.j2d;

/*
A little playground for cubic bezier curves as seen in
   - java2d: PathIterator.SEG_CUBICTO / GeneralPath.curveTo 
   - postscript: curveto 
   - wmf/emf/Windows API/GDI: PolyBezier 
   - ... 

This is
   * for choosing the right control points to send them to a graphics engine.
   * not for interpolating the pixels between the control points, the 
     graphics engine will do this. 
   * quick and dirty not optimized code, spit out to see what happens and
     to play with it.


Look at BezierPanel#paintComponent() for the (simple) calculating parts

(c)2004 www.peterbuettner.de



As an example of the common descriptions, from PathIterator.SEG_CUBICTO:
	This form of curve is commonly known as a B�zier curve.
	... set of 3 points that specify a cubic parametric curve to be drawn 
	from the most recently specified point. The curve is interpolated
	by solving the parametric control equation in the range (t=[0..1]) 
	using the most recently specified (current) point (CP), the first 
	control point (P1), the second control point (P2), and the final 
	interpolated control point (P3). The parametric control equation 
	for this curve is:
		P(t) = B(3,0)*CP + B(3,1)*P1 + B(3,2)*P2 + B(3,3)*P3 ; 
		where 0 <=t<=1
	mth coefficient of nth degree Bernstein polynomial
        B(n,m) = C(n,m) * t^(m) * (1 � t)^(n-m) 
		C(n,m) = n! / (m! * (n-m)!)

which gives (Points: CP, P1, P2, P3) 
P(t) = (1-t)^3 * CP + 3(1-t)^2 t * P1 + 3(1-t)t^2 * P2 + t^3*P3
P'(t) = [-3 (1 - t)^2] *CP + [3 - 12 t + 9 t^2] * P1 + [3 (2 - 3 t) t] * P2  [3 t^2] *P3
P''(t) = [6(1-t)] * CP + [-6(2-3t)]*P1 + [6(1-3t)]*P2 + [6t]*P3

P'(t=0)  = -3*CP + 3*P1         
P''(t=0) = 6*CP -12*P1 + 6*P2  =  -6*CP -4*P'(0) + 6*P2 
    now write P' for P'(t=0)
P1= P'/3 +CP
P2 = P''/6 +2P1 -CP= P''/6 +2/3P' +CP

An example bezier segment (sketch only:)

                             P2
         P1
                .   .
          .             .
      .                    .
   .                         .
  .                           .
 .
CP                             .

                                P3



*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BezierCalcFrame extends JFrame{
	static 	String[] lf = {
			"com.jgoodies.plaf.plastic.Plastic3DLookAndFeel",
			"com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel",
			"com.sun.java.swing.plaf.motif.MotifLookAndFeel",
			"com.incors.plaf.kunststoff.KunststoffLookAndFeel",
			"com.jgoodies.plaf.plastic.PlasticLookAndFeel",
			"com.l2fprod.gui.plaf.skin.SkinLookAndFeel",
			"net.sourceforge.mlf.metouia.MetouiaLookAndFeel",
			UIManager.getSystemLookAndFeelClassName(),
			UIManager.getCrossPlatformLookAndFeelClassName(),
			"com.jgoodies.plaf.windows.ExtWindowsLookAndFeel",
			"com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
			"com.l2fprod.gui.plaf.skin.LinuxLookAndFeel",
			};
	
public static void main(String[] args) {
	try {UIManager.setLookAndFeel(lf[0]);}
	catch (Exception e) {e.printStackTrace();}
	new BezierCalcFrame();
	}

// ################ GUI #########################################
	
	BezierPanel bezierPanel = new BezierPanel();
	
public BezierCalcFrame()  {
	super("Cubic Bezier Interpolation - Simple Example - www.PeterBuettner.de");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	JPanel pnlTools = new JPanel(new BorderLayout());
	JPanel pnlShow = new JPanel();
	int iSize=14;
	//Shape line = new Rectangle2D.Double(0,0,10,3);
	Shape line = AffineTransform.getRotateInstance(-3.14/8).createTransformedShape(new Rectangle2D.Double(0,0,10,1));
//	pnlShow.add(new JLabel("Symbols:"));
	pnlShow.add(new ShowHideButton("Control", new ShapeIcon(BezierPanel.getShapeCtrl(),Color.RED,iSize)));
	Stroke ctrlStroke = new BasicStroke( 0.7f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1,new float[]{1,3},0);
	Shape s = ctrlStroke.createStrokedShape(new Line2D.Double(0,0,10,-10));
	pnlShow.add(new ShowHideButton("Handles", new ShapeIcon(s,Color.RED,iSize)));
	
	pnlShow.add(new ShowHideButton("Base", new ShapeIcon(BezierPanel.getShapeBase(),Color.BLUE,iSize)));
//	pnlShow.add(new JLabel("       Lines:"));
	pnlShow.add(new JLabel("  "));	
	pnlShow.add(new ShowHideButton("Linear", new ShapeIcon(line,Color.RED,iSize)));
	pnlShow.add(new ShowHideButton("Real", new ShapeIcon(line,Color.GREEN,iSize)));
	pnlShow.add(new ShowHideButton("Bezier", new ShapeIcon(line,Color.BLACK,iSize)));
	
	
	
	
	pnlTools.add(pnlShow, BorderLayout.WEST);

	JSpinner spNumPoints = new JSpinner(new SpinnerNumberModel(17,3,1000,1));
	JSpinner spZoom = new JSpinner(new SpinnerNumberModel(1,1,100,1));
	JPanel pnlSpinZoom = new JPanel();
	pnlSpinZoom.add(new JLabel("Zoom:"));
	pnlSpinZoom.add(spZoom);
	pnlSpinZoom.add(new JLabel("#Pts:"));
	pnlSpinZoom.add(spNumPoints);
	pnlTools.add(pnlSpinZoom, BorderLayout.EAST);
	

	getContentPane().add(new JScrollPane(bezierPanel));
	getContentPane().add(pnlTools, BorderLayout.NORTH);
	
	spNumPoints.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			bezierPanel.setNumBasePoints(((Number)((JSpinner) e.getSource()).getValue()).intValue());
		}});
	spZoom.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			bezierPanel.setZoom(((Number)((JSpinner) e.getSource()).getValue()).intValue());
		}});
	
	pack();
	setLocationRelativeTo(null);
	show();
}// ------------------------------------------------------------------
class ShowHideButton extends JToggleButton{
	ShowHideButton(String name){this(name, null);}
	ShowHideButton(String name, Icon icon){
		setAction(new AbstractAction(name, icon){
			public void actionPerformed(ActionEvent e) {
			bezierPanel.setShowElement(e.getActionCommand(), isSelected());
			}});
		setSelected(true);
//		setMargin(new Insets(2,4,2,4));
		}
}


// ################################ Bezier ################################   
static class BezierPanel extends JPanel{
	// define the function and their first two derivates (using only the first yet)
	private final double omega=4; 
	private double f(double x){   return  Math.sin(omega*x); }
	private double fi(double x){  return  Math.cos(omega*x)*omega; }
	private double fii(double x){ return -Math.sin(omega*x)*omega*omega; }
//	private double f(double x){   return  Math.exp(-omega*x); }
//	private double fi(double x){  return  -Math.exp(-omega*x)*omega; }
//	private double fii(double x){ return Math.exp(-omega*x)*omega*omega; }
	private int numBasePoints=17;
	private int zoom=1;
	private boolean showLinear=true;
	private boolean showReal=true;
	private boolean showBezier=true;
	private boolean showControl=true;
	private boolean showHandles=true;
	private boolean showBase=true;
	
	protected void paintComponent(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		g.setColor(new Color(0x010101 * 0xfa));
		g.fillRect(0,0,getWidth(),getHeight());

		// real units, not on screen
		double xWidth = 2*Math.PI + Double.MIN_VALUE;
		double yHeight= 2.2;
		
		int border=5; // screen units
		
		double dx = xWidth/numBasePoints;// distance of BasePoints

		// scale, so painting is in units of f(x) 
		double sx =  (getWidth()-2*border)/xWidth; 
		double sy = -getHeight()/yHeight;
		g.scale( sx,sy);
		g.translate(border/sx,-yHeight/2);
		
		// calc scaled symbols
		AffineTransform st = AffineTransform.getScaleInstance(1/sx,1/sy);
		Shape symBase = st.createTransformedShape( getShapeBase() );
		Shape symCtrl = st.createTransformedShape( getShapeCtrl() );
		
		Stroke lineStroke = new BasicStroke( (float)(1/sx));// 1 screen unit
		Stroke controlStroke = new BasicStroke( (float)(0.5/sx),BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER,1,new float[]{(float) (1/sx),(float) (3/sx)},0);
		// ..................................
	
		// Interpolation/Base Pts
		if (showBase) {
			g.setColor(Color.BLUE);
			for(double x=0;x<xWidth;x+=dx) g.fill(translateShape(symBase,x, f(x)));
			}
		
		// control points and curve
		g.setStroke(controlStroke);
		g.setColor(Color.RED);
		GeneralPath curve = new GeneralPath();
		curve.moveTo((float)0,(float)f(0));
		for(double x=0;x<xWidth;x+=dx){
			double y=f(x);
			double x3 = x + dx; // next base point
			double y3 = f(x3);

			double rd=0.333f; // relative distance (ctrl/base) relative to (base/base)
			double x1= x + dx*rd;        // ctrl point 1 
			double y1= y + fi(x)*dx*rd;
			double x2= x3 - dx*rd;       // ctrl point 2
			double y2= y3 - fi(x3)*dx*rd;
			
//			double x2= x + 5*dx/6); 
//			double y2= fii(x)*dx*dx/6 + fi(x)*dx*2/3 +y;
			
//			double x2= x/2 + x3/2 + 3*dx/12; 
//			double y2= -fii(x)*dx/12 + fi(x)*dx/6 +y/2 + y3/2;
			
			curve.curveTo((float)x1,(float)y1,(float)x2,(float)y2, (float)x3,(float)y3); // bezier line
			if (showControl) {
				g.fill(translateShape(symCtrl, x1,y1));// control point P1
				g.fill(translateShape(symCtrl, x2,y2));// control point P2
				}
			if (showHandles) {
				g.draw(new Line2D.Double(x,y,x1,y1));
				g.draw(new Line2D.Double(x3,y3,x2,y2));
				}
			}		

		g.setStroke(lineStroke); 
		// ugly linear interpolation. 
		// use 3* 'number of points' for similar chances, 
		// since each bezier segment uses ~3 points (overall average)
		if (showLinear) {
			g.setColor(Color.RED);
			for(double x=0;x<xWidth;x+=dx/3)
				g.draw(new Line2D.Double(x,f(x),x+dx/3,f(x+dx/3)));
			} 			

		// paint bezier
		if(showBezier){
			g.setColor(Color.BLACK);
			g.draw(curve);
			}

		// paint 'real' data, resolution: 1 screen unit ~ 1 pixel
		if(showReal){
			GeneralPath real = new GeneralPath();
			real.moveTo(0, 0);
			// if zoomed out, this is *much* faster
			Rectangle2D r = g.getClip().getBounds2D();
			double maxX = Math.min(xWidth + r.getMinX(), r.getMaxX());
			for (double x = r.getMinX(); x < maxX; x += 1 / sx)
				real.lineTo((float) x, (float) f(x));
			g.setColor(Color.GREEN);
			g.draw(real);
			}
		
		// restore g
		g.translate(-0.1,1.5);
		g.scale( 1/sx,1/sy);
	}

// ################################ Helper ################################

static Shape translateShape(Shape s, double dx, double dy) {
	return AffineTransform.getTranslateInstance(dx,dy).createTransformedShape(s);
}// ---------------------------------------------------------------
static Shape getShapeCtrl(){// screen units ~ pixels
	GeneralPath gp = new GeneralPath(); // triangle 
	gp.moveTo(-0.5f,0);
	gp.lineTo(0.5f,0);
	gp.lineTo(0,-1);
	gp.closePath();
	return AffineTransform.getScaleInstance(7,7).createTransformedShape(
		translateShape(gp,0,0.4f)
		);
}// ---------------------------------------------------------------
static Shape getShapeBase() {// screen units ~ pixels
	GeneralPath gp = new GeneralPath(); // cross
	gp.append(new Rectangle2D.Double( -8,-1.5,16,3),false);
	gp.append(new Rectangle2D.Double( -1.5,-8,3,16),false);
	return AffineTransform.getScaleInstance(0.6,0.6).createTransformedShape(gp);
}// ---------------------------------------------------------------

public Dimension getPreferredSize(){return new Dimension(600*zoom,300*zoom);}

public void setNumBasePoints(int numBasePoints) {
	this.numBasePoints = numBasePoints;
	repaint();
	}
public void setZoom(int zoom) {
	this.zoom = zoom;
	getParent().doLayout();
	}
public void setShowElement(String element, boolean show){
	if( element.equals("Linear"))        showLinear = show;
	else if( element.equals("Real"))     showReal = show;
	else if( element.equals("Bezier"))   showBezier = show;
	else if( element.equals("Control"))  showControl = show;
	else if( element.equals("Handles"))  showHandles = show;
	else if( element.equals("Base"))     showBase = show;
	
	else throw new IllegalArgumentException("Unknown element name.");
	repaint();

}

}

}
