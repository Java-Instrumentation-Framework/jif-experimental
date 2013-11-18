package tests.gui.icewalker;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/* A JPanel implementation that has Rounded Edges. */

public class RoundedPanel extends JPanel {

	int curvature = 20;
	Color panelColor = UIManager.getColor("Panel.background");
	
	public RoundedPanel() {
		this( new FlowLayout(FlowLayout.LEFT), 20 );
	}
	
	public RoundedPanel(LayoutManager layout) {
		this(layout, 20);			
	}
	
	public RoundedPanel(LayoutManager layout, int curvature) {
		super(layout);
		setCurvature(curvature);
		setBorder( new EmptyBorder(1,5,1,5) );
		setOpaque(false);
	}
	
	public RoundedPanel(int curvature) {
		this( new FlowLayout(FlowLayout.LEFT), curvature );		
	}
	
	public void setBackground(Color c) {
		setPanelColor(c);	
	}
	
	public void setPanelColor(Color c) {
		panelColor = c;
		repaint();
	}
	
	public void setCurvature(int curvature) {
		this.curvature = curvature;
		repaint();
	}
	
	public void paintComponent(Graphics g) {			
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);
		//g.clearRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		g2.setColor( panelColor.darker() );
		//g2.drawRoundRect(0, 0, getPreferredSize().width, getPreferredSize().height,
		//curvature, curvature);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), curvature, curvature);
		//g2.draw3DRect(0, 0, getPreferredSize().width, getPreferredSize().height, false);
		
		g2.setColor(panelColor);
		//g2.fillRoundRect(1, 1, getPreferredSize().width - 2, getPreferredSize().height - 2,
		//curvature, curvature);
		g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, curvature, curvature);
		//g2.fill3DRect(2, 2, getPreferredSize().width, getPreferredSize().height, true);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		super.paintComponent(g);
	}
}