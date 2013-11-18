/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.gui;


import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;

//
//
// PopUpDemo
//
//
public class PopUpDemo extends Applet
{
	final Applet applet = this;

	// Default color
	private Color m_color = Color.orange;

	private PopupMenu m_menu = new PopupMenu();
	private MenuItem  m_red = new MenuItem("Red");
	private MenuItem  m_green = new MenuItem("Green");
	private MenuItem  m_blue = new MenuItem("Blue");
	private MenuItem  m_white = new MenuItem("White");
	

	// Initialization code
	public void init()
	{
		// Setup the menu
		m_menu.add (m_red);
		m_menu.add (m_green);
		m_menu.add (m_blue);

		// Add to applet, as menu must have a parent component
		add(m_menu);

		// Separator
		m_menu.add (new MenuItem("-"));

		m_menu.add(m_white);

		// Create action listener for right click
		addMouseListener ( new MouseAdapter ()	{
			public void mouseReleased(MouseEvent e)
			{
				// Check for a right click pop-up
  				if (e.isPopupTrigger() )
				{
					int x = e.getX();
					int y = e.getY();

					// Show the popup meun
					m_menu.show ( applet, x, y );					
				}
			}
		});

		// Create action listeners for menu items
		m_red.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
				m_color = Color.red;
				applet.repaint();
			}

		});

		m_green.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
				m_color = Color.green;
				applet.repaint();
			}

		});

		m_blue.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
				m_color = Color.blue;
				applet.repaint();
			}

		});

		m_white.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
				m_color = Color.white;
				applet.repaint();
			}

		});

	}

	// Color the applet
	public void paint(Graphics g)
	{
		g.setColor (m_color);
		g.fillRect ( 0, 0, 400, 200);
	}

}

