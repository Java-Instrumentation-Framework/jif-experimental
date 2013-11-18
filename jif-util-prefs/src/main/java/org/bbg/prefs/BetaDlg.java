/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import static java.lang.Math.*;

@SuppressWarnings("serial")
public class BetaDlg extends JDialog implements ActionListener {
	
	boolean agreed = false;
	
	private JButton okBtn, cancelBtn;

	public BetaDlg(Frame owner) {
		super(owner, "Disclaimer", true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new BetaPanel(), BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		okBtn = new JButton("I agree");
		cancelBtn = new JButton("Cancel");
		JPanel buts = new JPanel(new GridLayout(1, 2, 6, 0));
		buts.setBorder(new EmptyBorder(4, 0, 4, 0));
		buts.add(okBtn); buts.add(cancelBtn);
		bottom.add(buts);
		getContentPane().add(bottom, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		okBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okBtn)
			agreed = true;
		dispose();
	}

	@SuppressWarnings("serial")
	class BetaPanel extends JPanel {
		
		private BufferedImage bg;
		//private Font bold = new Font("Lucida Sans", Font.BOLD, 14);
		private Font bold = new Font("dialog", Font.BOLD, 14);
		private List<String> legal = new ArrayList<String>();
		private int lh = 0;
		
		private String beta = " BETA ";
		private String title = "Java Preferences Tool, version 0.7";
		private String copyr = "\u00a9 2006, Boris Gontar. All rights reserved.";

		public BetaPanel() { 
			super();
			init();
		}
		
		public BetaPanel(LayoutManager layout) { 
			super(layout);
			init();
		}
		
		private void init() {
			setOpaque(true);
			setBackground(Color.WHITE);
			bg = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bg.createGraphics();
			g2.setFont(new Font("Tahoma", Font.BOLD, 48));
			g2.setColor(Color.BLUE);
			Rectangle2D r = g2.getFontMetrics().getStringBounds(beta, g2);
			double w = r.getWidth()*cos(PI/6) + r.getHeight()*sin(PI/6);
			double h = r.getWidth()*sin(PI/6) + r.getHeight()*cos(PI/6);
			//
			InputStream inp = getClass().getResourceAsStream("disclaimer.txt");
			int maxlen = 0;
			String longest = null;
			try {
				BufferedReader rdr = new BufferedReader(new InputStreamReader(inp, "utf-8"));
				String line = null;
				while ((line = rdr.readLine()) != null) {
					legal.add(line.trim());
					if (maxlen < line.length()) {
						maxlen = line.length(); longest = line;
					}
				}
				rdr.close();
			} catch (Exception ex) {
				// ignore
			}
			g2.setFont(bold);
			r = g2.getFontMetrics().getStringBounds(longest, g2);
			lh = (int)r.getHeight() + 3;		// line distance
			setPreferredSize(new Dimension((int)r.getWidth() + 48, 
					(int)r.getHeight()*(legal.size()+3) + 64));
			g2.dispose();
			//
			bg = new BufferedImage((int)w, (int)h, BufferedImage.TYPE_INT_ARGB);
			g2 = bg.createGraphics();
			g2.setFont(new Font("Tahoma", Font.BOLD, 48));
			g2.setColor(Color.YELLOW);
			g2.rotate(-PI/6);
			g2.drawString(beta, -(int)h/4, (int)(h - w/8));
			g2.dispose();
		}
		
		public void paintComponent (Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			// background
			//Composite prev = g2.getComposite();
			//Composite alpha = AlphaComposite.getInstance(
	        //        AlphaComposite.SRC_OVER, 0.5f);
			//g2.setComposite(alpha);
			int w = bg.getWidth(this), h = bg.getHeight(this);
			for (int x = 0; x < getWidth(); x += w)
				for (int y = 0; y < getHeight(); y += h)
					g2.drawImage(bg, x, y, this);
			//g2.setComposite(prev);
			g2.setColor(Color.BLACK);
			// foreground
			g2.setFont(bold);
			Rectangle2D r = g2.getFontMetrics().getStringBounds(copyr, g2);
			int y0 = (int)r.getHeight()+16;
			g2.drawString(title, 16, y0);
			g2.drawString(copyr, 16, y0 += (int)r.getHeight());
			g2.drawLine(16, y0+4, 16 + (int)r.getWidth(), y0+4);
			g2.drawLine(16, y0+5, 16 + (int)r.getWidth(), y0+5);
			y0 += 32;
			for (String str : legal) {
				g2.drawString(str, 16, y0);
				y0 += lh;
			}

		}
		
	}

}
