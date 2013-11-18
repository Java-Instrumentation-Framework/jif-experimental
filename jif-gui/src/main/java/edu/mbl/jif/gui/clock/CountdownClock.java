package edu.mbl.jif.gui.clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class CountdownClock extends JPanel {
	
	private double theAngle;
	private double thePhysicalAngle;
	private Thread theThread;
	private long remains;
	Timer timer;
	long nextTime;
	
	public CountdownClock() {
		setBackground(SystemColor.control);
		setOpaque(false);
		setAngle(0, 0);
	}
	
	public void setAngle(double angle, long remaining) {
		if (angle > 0) {
			thePhysicalAngle = angle;
			remains = remaining;
		} else {
			thePhysicalAngle = 0;
			remains = 0;
		}
		repaint();
	}
	
	public void setDone() {
		thePhysicalAngle = 0;
		remains = -1;
		repaint();
		if (timer != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					timer.stop();
					repaint();
				}
				
			});
		}
	}
	
	public double getAngle() {
		return theAngle;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(75, 75);
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(50, 50);
	}
	
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		int w = getSize().width;
		int h = getSize().height;
		int r = Math.min(w, h) / 2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		int needleLength = r;
		int needleWidth = r / 10;
		int dropShadowHeight = r / 30;
		int fontSize = r / 3;

		// draw dial
		g.setColor(getBackground());
		g.fillRect(0, 0, w, h);
		g.setColor(SystemColor.controlShadow);
		g.fillArc(0, 0, (2 * r) - 1, (2 * r) - 1, 45, 180);
		g.setColor(SystemColor.controlDkShadow);
		g.fillArc(1, 1, (2 * r) - 3, (2 * r) - 3, 45, 180);
		g.setColor(SystemColor.controlLtHighlight);
		g.fillArc(0, 0, (2 * r) - 1, (2 * r) - 1, 225, 180);
		g.setColor(SystemColor.controlHighlight);
		g.fillArc(1, 1, (2 * r) - 3, (2 * r) - 3, 225, 180);
		g.setColor(SystemColor.control);
		g.fillOval(2, 2, (2 * r) - 5, (2 * r) - 5);

		// draw letters
		g.setFont(new Font("sans-serif", Font.BOLD, fontSize));
		FontMetrics fm = g.getFontMetrics();
		g.setColor(SystemColor.controlText);
		if (remains + 1 > 0) {
			g.drawString(String.valueOf(remains + 1),
					r - (fm.stringWidth(String.valueOf(remains)) / 2), (2 * r) - 14);
		}
		if (remains == -1) {
			g.drawString("0", r - (fm.stringWidth("0") / 2), (2 * r) - 14);
		}

		// draw needle shadow
		g.setColor(SystemColor.controlShadow);
		Polygon poly = new Polygon();
		poly.addPoint((int) (r + dropShadowHeight + (needleLength * Math.sin(thePhysicalAngle))),
				(int) ((r + dropShadowHeight) - (needleLength * Math.cos(thePhysicalAngle))));
		poly.addPoint((int) (r + dropShadowHeight + (needleWidth * Math.cos(thePhysicalAngle))),
				(int) (r + dropShadowHeight + (needleWidth * Math.sin(thePhysicalAngle))));
		poly.addPoint((int) ((r + dropShadowHeight) - (needleWidth * Math.cos(thePhysicalAngle))),
				(int) ((r + dropShadowHeight) - (needleWidth * Math.sin(thePhysicalAngle))));
		g.fillPolygon(poly);

		// draw needle
		if (remains != -1) {
			g.setColor(SystemColor.red);
		} else {
			g.setColor(SystemColor.gray);
		}
		poly = new Polygon();
		poly.addPoint((int) (r + (needleLength * Math.sin(thePhysicalAngle))),
				(int) (r - (needleLength * Math.cos(thePhysicalAngle))));
		poly.addPoint((int) (r + (needleWidth * Math.cos(thePhysicalAngle))),
				(int) (r + (needleWidth * Math.sin(thePhysicalAngle))));
		poly.addPoint((int) (r - (needleWidth * Math.cos(thePhysicalAngle))),
				(int) (r - (needleWidth * Math.sin(thePhysicalAngle))));
		g.fillPolygon(poly);
	}
	
	public void createTimer(long _nextTime, final long period) {
		this.nextTime = _nextTime;
		timer = new Timer(200,
				new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispatchToEDT(
						new Runnable() {
					public void run() {
						long toGo = nextTime - System.currentTimeMillis();
						//System.out.println("Thread: " + Thread.currentThread().getName());
						//System.out.println("toGo: " + toGo + " NextTime: " + nextTime);
						if (toGo > 0) {
							double angle = 2 * Math.PI * ((float) toGo / (float) period);
							//System.out.println(angle);
							setAngle(angle, toGo / 1000);
						} else {
							timer.stop();
//															setAngle(0, 0);
//															repaint();

//                            System.out.println("setting done");
							setDone();
//                            timer.stop();
						}
					}
					
				});
			}
			
		});
		if (timer != null) {
			timer.start();
		}
	}
	
	public static void dispatchToEDT(Runnable runnable) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(runnable);
		} else {
			runnable.run();
		}
	}
	
	public void cancel() {
		setDone();
	}
	
	public static void main(String[] args) {
		final long period = 5000; // msec.
		int increment = 500; // msec.
		int steps = (int) (period / increment);
		double angleStep = (2 * Math.PI) / steps;
		long remaining = period;
		
		final CountdownClock am = new CountdownClock();
		JFrame f = new JFrame();
		f.setSize(100, 100);
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(am, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				am.createTimer(System.currentTimeMillis() + period, period);
			}

		});
		f.getContentPane().add(goButton, BorderLayout.SOUTH);
		f.setVisible(true);
		

		// ADD: Show wedge for AcqCutOff time.
		//        for (int i = steps; i >= 0; i--) {
		//            remaining = (long) ((i * increment) / 1000);
		//            am.setAngle((steps-i) * angleStep, remaining);
		//            try {
		//                Thread.sleep(increment);
		//            } catch (InterruptedException e) {}
		//        }
	}
	
}
