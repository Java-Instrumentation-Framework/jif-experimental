package tests.gui.icewalker;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LinkLabel extends JLabel {
	
	Color linkColor = Color.blue;
	Color hoverColor = Color.red;
	String textData;
	
	public LinkLabel(String text) {
		this(text, JLabel.LEFT);
	}
	
	public LinkLabel(String text, int align) {
		super(text, align);
		setCursor( new Cursor(Cursor.HAND_CURSOR) );
		setForeground( getLinkColor() );
		this.textData = text;
		
		addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setForeground( getHoverColor() );					
			}	
			public void mouseExited(MouseEvent e) {
				setForeground( getLinkColor() );					
			}
		});
	}
	
	public void setLinkColor(Color linkColor) {
		this.linkColor = linkColor;	
		setForeground(this.linkColor);
	}
	
	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}
	
	public Color getLinkColor() { return linkColor; }
	public Color getHoverColor() { return hoverColor; }
	
}