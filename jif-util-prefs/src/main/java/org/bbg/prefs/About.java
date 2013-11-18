/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import static java.lang.Math.max;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class About extends JDialog implements ActionListener {

	private JPanel textPanel = new JPanel();
	private JPanel titlePanel = new JPanel(new BorderLayout());
	private JPanel bottomPanel = new JPanel(new FlowLayout());
	private JButton closeBtn = new JButton("Close");
	private JLabel titleLab = new JLabel("Java Preferences Tool",
			SwingConstants.CENTER);
	private JLabel versLab = new JLabel("Version 0.8, 2006/09/15.  " +
			"Free for non-commercial use.", SwingConstants.CENTER);
	private JLabel copyLab = new JLabel("\u00a9 2006, Boris Gontar. All rights reserved.");
	private JPanel licPanel = new JPanel(new BorderLayout());
	private JLabel logoLab = new JLabel(Main.appLogo);
	protected JScrollPane scrollPane = new JScrollPane(); 
	private JTextArea textArea = new JTextArea();

	public About(Frame owner) {
		super(owner, true);
		setTitle("About");
		textArea.setEditable(false);
		textArea.setBorder(new EmptyBorder(4, 4, 4, 4));
		licPanel.add(scrollPane, BorderLayout.CENTER);
		bottomPanel.add(closeBtn, null);
		//
		copyLab.setAlignmentX(0.5f);
		copyLab.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		versLab.setFont(new Font("Tahoma", Font.BOLD, 14));
		versLab.setForeground(Color.blue);
		versLab.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
		versLab.setAlignmentX(0.5f);
		//
		titleLab.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLab.setForeground(Color.blue);
		titleLab.setBorder(BorderFactory.createEmptyBorder(16, 0, 12, 0));
		titleLab.setAlignmentX(0.5f);
		//
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(titleLab);
		textPanel.add(versLab);
		textPanel.add(copyLab);
		//textPanel.add(licPanel);
		//
		titlePanel.add(textPanel, BorderLayout.CENTER);
		logoLab.setBorder(new EmptyBorder(0, 24, 0, 0));
		titlePanel.add(logoLab, BorderLayout.WEST);
		//
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(titlePanel, BorderLayout.NORTH);
		contentPane.add(licPanel, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		setContentPane(contentPane);
		//
		InputStream inp = getClass().getResourceAsStream("GPL.txt");
		int lines = 0, width = 0;
		StringBuilder buf = new StringBuilder();
		try {
			BufferedReader rdr = new BufferedReader(new InputStreamReader(inp, "utf-8"));
			String line = null;
			while ((line = rdr.readLine()) != null) {
				buf.append(line).append("\n");
				lines++;
				width = max(width, line.length());
			}
			rdr.close();
		} catch (Exception ex) {
			// ignore
		}
        textArea.setText(buf.toString());
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        scrollPane.setPreferredSize(new Dimension(fm.charWidth('o')*(width+4),
        		fm.getHeight()*25));
        scrollPane.setViewportView(textArea);
		pack();
		//
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(owner);
		closeBtn.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				scrollPane.getViewport().setViewPosition(new Point(0, 0));
				super.windowOpened(e);
			}
		});
	}

	public void actionPerformed (ActionEvent e) {
	    if (e.getSource() == closeBtn)
	    	dispose();
    }
/*
    @SuppressWarnings("serial") 
    class PaintedPanel extends JPanel {
    	
    	public PaintedPanel() {
    		super();
    		setBackground(new Color(250, 235, 215));
    	}
    	
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2 = (Graphics2D)g;
    		Composite prev = g2.getComposite();
    		Composite alphaComp = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.25f);
    		g2.setComposite(alphaComp);
    		//Image bg = Main.logoIcon.getImage();
    		Image bg = Main.appLogo.getImage();
    		int w = bg.getWidth(this), h = bg.getHeight(this);
    		for (int x = 0; x < getWidth(); x += w)
    			for (int y = 0; y < getHeight(); y += h)
    				g2.drawImage(bg, x, y, this);
			g2.setComposite(prev);
    	}
    	
    }
    */
}

