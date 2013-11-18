package tests.gui.icewalker.colormenu;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;

public class MenuedButton extends JPanel implements ActionListener {

	public JButton main, popper;
	public Vector<Object> filenames = new Vector<Object>();
	public Hashtable locations = new Hashtable();
	public JPopupMenu menu = new JPopupMenu();
	public ActionListener listener;
	
	protected Point popLocation = new Point(0,0);
	protected int locX = 0, locY = 0;
	public static int BELOW = 1, ABOVE = 2, LOCATION = BELOW;
	
	private boolean shdPopup = false;
	private ActionListener pl = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			showMenu();
		}
	};
		
	public MenuedButton(){
		this( (String)null, (Icon)null);
	}
	
	public MenuedButton(String text) {
		this(text, (Icon)null);
	}
	
	public MenuedButton(Icon icon) {
		this(null, icon);
	}
	
	public MenuedButton(String text, Icon icon) {
		createButtons(text, icon);
		createPopupMenu();
		//setPopupLocation(BELOW);
	}
	
	public MenuedButton(JButton main) {
		//this(null, null);
		setMainButton(main);
	}
	
	public MenuedButton(String butText, JPopupMenu menu) {
		setMainButton( new JButton(butText) );
		setPopupMenu(menu);
		setShdPopupOnBtnClick(true);
		setPopupLocation(BELOW);
	}
	
	public MenuedButton(Action action, JPopupMenu menu) {
		setMainButton( new JButton(action) );
		setPopupMenu(menu);
		setShdPopupOnBtnClick(true);
		setPopupLocation(BELOW);
	}
	
	public void createButtons(String text, Icon icon) {
		if(main == null) {
			main = new JButton(text,icon);
			
			if(text == null) {
				main.setMargin( new Insets(0,0,0,0) );
			}
		}		
		
		//main.setFont( new Font("Verdana", Font.PLAIN, 11) );
		ImageIcon img = new ImageIcon("resources/images/popicon.gif");
		popper = new JButton( /*img*/ ) {
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D g2 = (Graphics2D) g;
				int width = getWidth(), height = getHeight();
				int midWidth = width/2, midHeight = height/2;
				
				int[] xpoints = { midWidth - 3, midWidth, midWidth + 3 };
				int[] ypoints = { midHeight - 3, midHeight + 3, midHeight - 3};
				
				g2.setColor( Color.black );
				g2.fill( new Polygon(xpoints, ypoints, 3) );
			}
		};
		
		//popper.setPreferredSize( new Dimension(img.getImage().getWidth(this) + 10, 
		popper.setPreferredSize( new Dimension(14, main.getPreferredSize().height ) );
		popper.addActionListener(this);
				
		setBorder( null );
		//setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
		setLayout( new BorderLayout() );
		add(main, BorderLayout.CENTER);
		add(popper, BorderLayout.EAST);
		addMenuItem("Test Text", "Location");
	}
	
	public void createPopupMenu() {
		menu = new JPopupMenu();			
		for(int i = filenames.size() - 2, j = 0; i >= 0; i--, j++) {
			menu.add((String)filenames.elementAt(i));
			JMenuItem mi = (JMenuItem) menu.getComponent(j);
				mi.setFont( new Font("Arial", Font.PLAIN, 11) );
				mi.addActionListener(this);
		}
				
		menu.pack();
		//setPopupLocation(200, 200);	
	}
	
	public void showMenu() {
		if(LOCATION == BELOW) {
			setPopupLocation(main.getX() - main.getWidth(), main.getY() + getHeight());
		}
		 
		if(LOCATION == ABOVE) {
			setPopupLocation(main.getX() - main.getWidth(), main.getY() - menu.getHeight());
		}
		
		menu.show(popper, getPopupX(), getPopupY() );	
	}
	
	public void setMainButton(JButton main) {
		this.main = main;
		removeAll();
		createButtons(null, null );
		validate();
		repaint();
	}
	
	public JButton getMainButton() {
		return main;
	}
	
	public void setPopupMenu(JPopupMenu menu) {
		this.menu = menu;		
	}
	
	public void setPopupMenu(JPopupMenu menu, int position) {
		setPopupMenu(menu);
		setPopupLocation(position);
	}
	
	public void setPopupLocation(int x, int y) {
		locX = x; locY = y;
	}
	
	public void setPopupLocation(Point location) {
		setPopupLocation((int)location.getX(), (int)location.getY() );
	}
	
	public void setPopupLocation(int location) {
		LOCATION = location;
		
		if(location == -1){
			throw new IllegalArgumentException("Invalid value: " + location);
		}
	}
	
	public void setShdPopupOnBtnClick(boolean shdPopup) {
		this.shdPopup = shdPopup;
		if(shdPopup) {
			main.addActionListener(pl);
		} else {
			main.removeActionListener(pl);
		}
	}
	
	public boolean shdPopupOnBtnClick() {
		return shdPopup;
	}
	
	public int getPopupX() { return locX; }
	public int getPopupY() { return locY; }
	
	public void addMenuItem(String text, String loc) {
		locations.put(text, loc);
		if(filenames.contains("Test Text")) {
			filenames.remove("Test Text");
			filenames.addElement(text);
		} else {
			filenames.addElement(text);
		}
		createPopupMenu();
	}
	
	public void setEnabled(boolean b) {
		main.setEnabled(b);
		popper.setEnabled(b);
	}
	
	public void addActionListener(ActionListener l) {
		main.addActionListener(l);
		listener = l;
	}
	
	public void displayPage(JEditorPane pane, String text) {
		try {
			File helpFile = new File( (String) locations.get(text) );
			String loc = "file:" + helpFile.getAbsolutePath();
			URL page = new URL(loc);
				
			pane.setPage(page);
							
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Help Topic Unavailable");
			pane.getParent().repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == popper) {		
			showMenu();
		}
	}
	
}