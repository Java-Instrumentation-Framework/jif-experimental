package tests.gui.icewalker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class PanelTitler extends JPanel {
	
	protected JLabel titler, commenter, iconLabel;
	public String title, comment;
	public Icon icon;
	
	public PanelTitler(String title, String comment, Icon icon) {
		super( new BorderLayout() );
		this.title = title;
		this.comment = comment;
		
		setBackground(Color.white);
		
		add(getTitles(), BorderLayout.CENTER );
		
		if(icon != null) {
			this.icon = icon;
			add( getIconLabel(), BorderLayout.EAST );
		}
	}
	
	public PanelTitler(String title, String comment) {
		this(title, comment, null);
	}
	
	public PanelTitler(String title) {
		this(title, null, null);
	}
	
	public JPanel getTitles() {
		titler = new JLabel(title);
		titler.setFont( new Font("Verdana", Font.BOLD, 13));
		titler.setBorder( new EmptyBorder(5,15,5,10) );			
		titler.setOpaque(false);
		
		JPanel all = new JPanel();
			all.setLayout( new BoxLayout(all, BoxLayout.Y_AXIS) );
			all.setOpaque(false);	
			all.setBorder( new EtchedBorder(EtchedBorder.LOWERED) );
		
		if(title != null) {
			all.add(titler);
		}
			
		if(comment != null) {
			commenter = new JLabel(comment);
			commenter.setBorder(new EmptyBorder(5,30,10,10));
			commenter.setOpaque(false);
				
			all.add(commenter);
		} else {
			titler.setBorder( new EmptyBorder(5,15,10,10) );
		}
		
		return all;
	}
	
	public JLabel getIconLabel() {
		iconLabel = new JLabel(icon);		
		iconLabel.setOpaque(false);
		
		return iconLabel;
	}
}