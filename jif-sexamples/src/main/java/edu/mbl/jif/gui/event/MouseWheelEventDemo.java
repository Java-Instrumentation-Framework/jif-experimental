/*
 * MouseWheelEventDemo.java is a 1.4 example
 * that requires the following file:
 *   BlankArea.java
 */
package edu.mbl.jif.gui.event;

import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelEventDemo extends JApplet
							implements MouseWheelListener {
	//BlankArea blankArea;
	JTextArea textArea;
	final static String newline = "\n";

	public void init() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		JPanel contentPane = new JPanel();
		contentPane.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 1.0;

		c.insets = new Insets(1, 1, 1, 1);
		//blankArea = new BlankArea(new Color(0.98f, 0.97f, 0.85f));
		//gridbag.setConstraints(blankArea, c);
		//contentPane.add(blankArea);

		c.insets = new Insets(0, 0, 0, 0);
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(200, 75));
		gridbag.setConstraints(scrollPane, c);
		contentPane.add(scrollPane);

		//Register for mouse events on blankArea and applet.

		//blankArea.addMouseWheelListener(this);
		addMouseWheelListener(this);

		setContentPane(contentPane);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
	   saySomething("Mouse wheel moved (# of clicks: "
					+ e.getWheelRotation() + ")", e);
	}

	void saySomething(String eventDescription, MouseWheelEvent e) {
		textArea.append(eventDescription + " detected on "
						+ e.getComponent().getClass().getName()
						+ "." + newline);
	}
}
