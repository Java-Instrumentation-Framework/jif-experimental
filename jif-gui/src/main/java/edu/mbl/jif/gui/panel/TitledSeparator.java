package edu.mbl.jif.gui.panel;

import java.awt.*;
import javax.swing.*;

public class TitledSeparator extends JPanel {	
	
	UIDefaults defaults = UIManager.getLookAndFeelDefaults();
	JLabel label;
	JComponent comp;
	
	public TitledSeparator() {
        this("");
    }

	public TitledSeparator(String title) {
		//super(title, LEFT);
		super(new BorderLayout() );
		
		label = new JLabel(title, JLabel.LEFT) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int width = getWidth();
				int height = getHeight();
					
				FontMetrics fm = g.getFontMetrics(getFont());
				int strWidth = (int)fm.getStringBounds(getText(), g).getWidth();	
					
				int rectWidth = getWidth() - (strWidth + 10), rectHeight = 2;
		
				g.setColor( defaults.getColor("Separator.highlight") );
				g.fillRect(width - rectWidth, height/2, rectWidth, 2);
				g.setColor( defaults.getColor("Separator.foreground") );
				g.fillRect(width - rectWidth, height/2, rectWidth, 1);
				
			}
		};
		label.setFont( new Font("Verdana", Font.BOLD, 10) );
		label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		add(label, BorderLayout.CENTER);
		setOpaque(false);
		
		this.comp = label;
	}
	
	public TitledSeparator(JComponent component) {
		this.comp = component;
		setLayout( new FlowLayout(FlowLayout.LEFT) );
		add(comp);
		setOpaque(false);
	}
	
	public String getTitle() {
		if(comp != null) {
			return comp.toString();
		}
		
		return label.getText();
	}
	
	public Component getComponent() {
		return comp;
	}
	
	public void setTitle(String s) {
		label.setText(s);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(comp != null) {
			int width = getWidth();
			int height = getHeight();
				
			FontMetrics fm = g.getFontMetrics(getFont());
			//int strWidth = (int)fm.getStringBounds(getText(), g).getWidth();	
			int strWidth = comp.getWidth();	
			int rectWidth = getWidth() - (strWidth + 10), rectHeight = 2;
	
			g.setColor( defaults.getColor("Separator.highlight") );
			g.fillRect(width - rectWidth, height/2, rectWidth, 2);
			g.setColor( defaults.getColor("Separator.foreground") );
			g.fillRect(width - rectWidth, height/2, rectWidth, 1);	
		}
	}
	
	
	public static void main(String[] args) {
		JPanel someComps = new JPanel();
			someComps.setLayout( new BoxLayout(someComps, BoxLayout.Y_AXIS) );
			someComps.add( new JLabel("First Item") );
			someComps.add( new JLabel("Second Item") );
			someComps.add( new TitledSeparator("Seperation") );
			someComps.add( new JLabel("Third Item") );
			someComps.add( new JLabel("Ordinary Separator Follows") );
			someComps.add( new JSeparator() );
			someComps.add( new JLabel("Last Item") );
			
		JPanel all = new JPanel( new BorderLayout() );
			all.add( new TitledSeparator("Example Of TitledSeparator Usage"), BorderLayout.NORTH );
			all.add( someComps, BorderLayout.CENTER );
		
		JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setContentPane(all);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
	}
}