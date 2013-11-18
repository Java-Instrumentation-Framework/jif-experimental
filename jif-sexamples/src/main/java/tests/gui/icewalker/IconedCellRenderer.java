package tests.gui.icewalker;

import java.awt.*;
import java.awt.event.*;
//import java.io.*;
//import java.net.*;
import java.util.*;
import javax.swing.*;
//import javax.swing.event.*;
import javax.swing.border.*;
//import javax.swing.table.*;

public class IconedCellRenderer extends DefaultListCellRenderer implements MouseListener {

	public Icon icon, selIcon;
	Icon[] icons = null;
	public boolean useIconBackground = true, useIndexSensitiveIcons = false,
			useCheckBoxAsIcon = false, useLinkState = false;
	Dimension labelDim = null;
	public JLabel iconLabel;
	public JCheckBox box = null;
	public JPanel noback;
	public SelectionStateHandler selStateHandler = null;

	private JList theList = null;

	public boolean[] selState = null, enableState = null;

	int offset = 5;
	Rectangle rect = null;
	
	int currentLinkRow = -1;
	boolean isOnRow = false, paintDivider = false;
	private Icon dividerImage = null;
	
	private Color linkColor = Color.blue, hoverColor = Color.red, selectedLinkColor = Color.green;

	/* Initialises the renderer with one icon that is displayed without the
	 * cell background.
	 **/
	public IconedCellRenderer(Icon icon) {
		this(icon,false);
	}

	/* Initialises the renderer with two icons that provide a switch capability
	 * when a row is selected/deselected
	 */
	public IconedCellRenderer(Icon icon, Icon selIcon) {
		this.icon = icon;
		this.selIcon = selIcon;
		//addMouseListener(this);
	}

	/* Initialises the renderer with two icons that provide a switch capability
	 * when a row is selected/deselected. The boolean argument enables the icon
	 * to either use the renderer background or appear transparent.
	 */
	public IconedCellRenderer(Icon icon, Icon selIcon, boolean useIconBackground) {
		this(icon, selIcon);
		setIconHasBackground(useIconBackground);
		createNoBackgroundPanel();
	}

	/* Initialises the renderer with a single no siwthing icon. The boolean
	 * argument enables the icon to either use the renderer background
	 * or appear transparent.
	 */
	public IconedCellRenderer(Icon icon, boolean useIconBackground) {
		this(icon, icon, useIconBackground);
	}

	/* Initialises the renderer to load two icons from the provided image locations.
	 * This enables icon switching on selection.
	 */
	public IconedCellRenderer(String iconLoc, String selIconLoc) {
		icon = new ImageIcon(iconLoc);
		selIcon = new ImageIcon(selIconLoc);
		//addMouseListener(this);
	}

	/* Initialises the renderer to load a single icon from the provided image location.
	 * The icon can either have the renderer background or not based on the
	 * boolean property.
	 */
	public IconedCellRenderer(String iconLoc, boolean iconBackground) {
		this(iconLoc, iconLoc, iconBackground);
	}

	/* Initialises the renderer to load two icons from the provided image locations.
	 * This enables icon switching on selection. The icon can either have the renderer
	 * background or not based on the boolean property.
	 */
	public IconedCellRenderer(String iconLoc, String selIconLoc, boolean iconBackground) {
		this(iconLoc, selIconLoc);
		setIconHasBackground(iconBackground);
		createNoBackgroundPanel();
	}

	/* Initialises the renderer to load a single icon from the provided image location.
	 */
	public IconedCellRenderer(String iconLoc) {
		this(iconLoc, true);
	}

	/* Initialises the renderer with an array of image icons that are repeated for
	 * each row in the list.
	 */
	public IconedCellRenderer(Icon[] icons, boolean useIconBackground) {
		this(icons[0], icons[0], useIconBackground);
	}

	public void createNoBackgroundPanel() {

		iconLabel = new JLabel((Icon)null, JLabel.CENTER);
		if(labelDim != null) { 	iconLabel.setPreferredSize(labelDim); }
		iconLabel.setBorder( new EmptyBorder(1,5,1,5) );

		noback = new JPanel( new BorderLayout() ) {
			/**
		    * Overridden for performance reasons.
		    * See the <a href="#override">Implementation Note</a>
		    * for more information.
		    */
		    //public void validate() {}
		   // public void invalidate() {}		
		    public void repaint() {}
		    //public void revalidate() {}
		    public void repaint(long tm, int x, int y, int width, int height) {}
		    public void repaint(Rectangle r) {}
		   
		    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
				// Strings get interned...
				if (propertyName == "text"
			                || ((propertyName == "font" || propertyName == "foreground")
			                    && oldValue != newValue)) {
			
				    super.firePropertyChange(propertyName, oldValue, newValue);
			        }
		    }
		
		    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
		    public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
		    public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
		    public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
		    public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
		    public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
		    public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
		    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

		};

		if(useCheckBoxAsIcon()) {
			box = new JCheckBox();
			//box.addMouseListener(this);
			box.setOpaque(false);
			noback.add( box, BorderLayout.WEST );
			//noback.addMouseListener(this);

			rect = box.getBounds();

			//list.addMouseListener(this);
		} else {
			noback.add( iconLabel, BorderLayout.WEST );
		}

		noback.add( this, BorderLayout.CENTER );
		noback.setBorder( new EmptyBorder(1,1,1,1) );
		noback.setOpaque(false);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {

		if(theList == null || theList != list) {
			theList = list;
			
			if(useLinkState) {
				attachLinkSimulationListener();
			}
		}

		setOpaque(true);
		setText( value == null ? "" : value.toString());
		
		if(useIndexSensitiveIcons) {
			icon = getIcon(index);
		}

		if(useIconBackground)
			setIcon(icon);		
			

		setFont( list.getFont() );
		setToolTipText(value.toString() );

		if(list.isEnabled())
			setEnabled( isEnabled(index) );
		else
			setEnabled( list.isEnabled() );

		if(isSelected && isEnabled(index) ) {
			setForeground( Color.black );
			setBackground( new Color(223, 235, 245) );

			if(useIndexSensitiveIcons) {
				selIcon = getIcon(index);
			}

			if(useIconBackground)
				setIcon(selIcon);
		} else {
			setForeground(Color.black);
			setBackground(Color.white);
			setBorder(null);
		}

		if(cellHasFocus) {
			setBorder( new CompoundBorder( new LineBorder( new Color(150, 150, 220) ),
										new EmptyBorder(2,2,2,2)  ) );
		} else {
			setBorder( new EmptyBorder(2,2,2,2));
		}	
		
		
		if(useLinkState) {
			if(currentLinkRow == index) {
				setText("<html><u>" + value.toString() + "</u></html>" );	
				setForeground( getHoverLinkColor() );			
			} else {
				setForeground( getLinkColor() );
			}
			
			if(isSelected) {
				setForeground( getSelectedLinkColor() );
			}
			
			setBackground(Color.white);
			setBorder( new EmptyBorder(1,1,1,1) );
		}
		
		if( shdPaintDivider() ) {
			Border border = null;
			if(dividerImage != null) {
				border = new MatteBorder(0,0,1,0, dividerImage);
			} else {
				border = new MatteBorder(0,0,1,0, getLinkColor() );
			}
			
			if(index < theList.getModel().getSize() - 1 ) {
				setBorder( new CompoundBorder(getBorder(),border) );
			} else {
				setBorder( new EmptyBorder(1,1,1,1) );
			}
		}

		if(useIconBackground == false) {
		    if(isSelected)
		        iconLabel.setIcon(selIcon);
		    else
		        iconLabel.setIcon(icon);
		
		    if(useCheckBoxAsIcon()) {
		        if(selState == null) {
		            updateSelectionStateTrackers(list);
		        }
		
		        if(selStateHandler == null) {
		            list.addMouseListener( selStateHandler = new SelectionStateHandler(list) );
		        }
		
		        try {
		            box.setSelected( selState[index] );
		        } catch(Exception e) {}
		    }
		    
		    if( shdPaintDivider() ) {               
		        
		        //if(index < theList.getModel().getSize() - 1 ) {
		            noback.setBorder( getBorder() );
		            setBorder( new EmptyBorder(1,1,1,1) );
		        //} else {
		        //  noback.get
		        //}
		    }
		    
		    // this should cause a JComboBox to paint the Label instead of the 
		    // check box + label combination
		    if(index == -1) {
		        JLabel label = new JLabel( this.getText() );
		        if(iconLabel.getIcon() != null) {
		        	label.setIcon( iconLabel.getIcon() );
		        }
		            
		        return label;
		    }
		
		    return noback;
		}

		return this;
	}

	public void updateSelectionStateTrackers(JList list) {
		selState = new boolean[ list.getModel().getSize() ];
		enableState = new boolean[ list.getModel().getSize() ];

		for(int i = 0; i < selState.length; i++) {
			selState[i] = false;
			enableState[i] = true;
		}


	}

	public int[] getSelectedIndices() {
		if(!useCheckBoxAsIcon()) {
			return new int[0];
		}

		int length = 0;
		for(int i = 0; i < selState.length; i++) {
			if(selState[i]) {
				length++;
			}
		}

		int[] indices = new int[length];
		for(int i = 0, n = 0; i < selState.length; i++) {
			if(selState[i]) {
				indices[n++] = i;
			}
		}

		//System.out.println("Selected Indices.length = " + indices.length);

		return indices;
	}

	public Vector<Object> getSelectedObjects() {
		int[] indices = getSelectedIndices();

		Vector<Object> objects = new Vector<Object>();

		for(int i = 0; i < indices.length; i++) {
			objects.addElement( theList.getModel().getElementAt(indices[i]) );
		}

		return objects;
	}


	public void setIconHasBackground(boolean b) {
		useIconBackground = b;
	}

	public Icon[] getIcons() {
		return icons;
	}

	public Icon getIcon(int index) {
		if(icons != null && icons.length == 0) {
			return icon;
		}

		if(icons != null && index > icons.length) {
			index = index - (icons.length - 1);
		}

		return icons[index];
	}

	public void setIcons(Icon[] icons) {
		if(icons != null) {
			useIndexSensitiveIcons = true;
		}

		this.icons = icons;
	}

	public void setIcon(Icon icon, int index) {
		if(icons != null && icons.length > 0) {
			icons[index] = icon;
		}
	}

	public void setIconLabelDimension(Dimension dim) {
		labelDim = dim;
	}

	public static IconedCellRenderer getCheckBoxRendererInstance() {
		IconedCellRenderer cr = new IconedCellRenderer(new EmptyIcon());
			cr.setUseCheckBoxAsIcon(true);

		return cr;
	}

	public void setUseCheckBoxAsIcon(boolean use) {
		useCheckBoxAsIcon = use;
		createNoBackgroundPanel();
	}
	
	public boolean useCheckBoxAsIcon() {
		return useCheckBoxAsIcon;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public void setDisplayItemsAsLinks(boolean use) {
		useLinkState = use;
	}
	
	public void setLinkColor(Color color) {
		linkColor = color;
		if(theList != null) {
			theList.repaint();
		}
	}
	
	public Color getLinkColor() {
		return linkColor;
	}
	
	public void setHoverLinkColor(Color color) {
		hoverColor = color;
		
		if(theList != null) {
			theList.repaint();
		}
	}
	
	public Color getHoverLinkColor() {
		return hoverColor;
	}
	
	public void setSelectedLinkColor(Color color) {
		selectedLinkColor = color;
		
		if(theList != null) {
			theList.repaint();
		}
	}
	
	public Color getSelectedLinkColor() {
		return selectedLinkColor;
	}
	
	public void attachLinkSimulationListener() {
		theList.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
		
		theList.addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				isOnRow = true;				
			}
			
			public void mouseExited(MouseEvent e) {
				isOnRow = false;
                currentLinkRow = -1;
                theList.repaint();
			}
		});
		
		theList.addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
                isOnRow = true;
                currentLinkRow = theList.locationToIndex( e.getPoint() );
                theList.repaint();
            }
		});
		
		/*table.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                
            }
            
            public void mouseExited(MouseEvent e) {
                isOnRow = false;
                currentHighlightRow = -1;
                table.repaint();
            }
        });
        
        table.addMouseMotionListener( new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                isOnRow = true;
                currentHighlightRow = table.rowAtPoint( e.getPoint() );
                table.repaint();
            }
        });*/
	}
	
	public boolean shdPaintDivider() {
		return paintDivider;
	}
	
	public void setPaintDivider(boolean paintDivider) {
		this.paintDivider = paintDivider;
	}
	
	public void setDividerImage(Icon icon) {
		this.dividerImage = icon;
		setPaintDivider(true);
	}
	
	private void dispatchEvent(MouseEvent me) {

        if(rect != null && box != null && rect.contains(me.getX(), me.getY())){
            Point pt = me.getPoint();
            pt.translate(0,0);
            box.setBounds(rect);
            box.dispatchEvent(new MouseEvent(box, me.getID()
                    , me.getWhen(), me.getModifiers()
                    , pt.x, pt.y, me.getClickCount()
                    , me.isPopupTrigger(), me.getButton()));

            if(!box.isValid()) {
            	repaint();
            	System.out.println("Dispatch Event: Box.invalid called");
            }


           System.out.println("Dispatch Event called");
        } else {
        	System.out.println("Dispatch Event Called, rect null");
        }
    }

    public void mouseClicked(MouseEvent me){
        dispatchEvent(me);
    }

    public void mouseEntered(MouseEvent me){
        dispatchEvent(me);
    }

    public void mouseExited(MouseEvent me){
        dispatchEvent(me);
    }

    public void mousePressed(MouseEvent me){
        dispatchEvent(me);
    }

    public void mouseReleased(MouseEvent me){
        dispatchEvent(me);
    }

    public class SelectionStateHandler extends MouseAdapter {
    	JList list = null;

    	public SelectionStateHandler(JList list) {
    		this.list = list;
    	}

		/*
		 * Handles the checkbox selection process. Uses the bounds property of the
		 * check box within the selected cell to determine whether the checkbox should
		 * be selected or not
		 **/
		public void mouseReleased(MouseEvent e) {
		    if(list == null || list.getSelectedIndex() == -1
		        || !isEnabled( list.locationToIndex(e.getPoint()) ) ) {
		        return;
		    }
		
		    int[] indices = list.getSelectedIndices();
		
		    // get the current relative position of the check box
		    //rect = box.getBounds(rect);
		
		    for(int i = 0; i < indices.length; i++) {
		    
		        // get the current relative position of the check box
		        int loc = list.locationToIndex( e.getPoint() );
		        rect = list.getCellBounds(loc,loc);
		        
		        // ensure the point clicked in within the checkBox
		        if(e.getX() < (rect.getX() + 20) ) {
		            selState[indices[i]] = !selState[indices[i]];
		        }
		    }
		
		    list.revalidate();
		    list.repaint();
		}

    	public void selectAll(boolean b) {
    		for(int i = 0; i < list.getModel().getSize(); i++) {
    			try {
    				selState[i] = b;
    			} catch(ArrayIndexOutOfBoundsException aie) {
    				updateSelectionStateTrackers(list);
    				selectAll(b);

    				return;
    			}

    		}

    		if(list != null) {
    			list.revalidate();
    			list.repaint();
    		}
    	}

    	public void setSelectedIndex(int index) {
    		for(int i = 0; i < list.getModel().getSize(); i++) {
    			selState[i] = false;
    		}

    		selectIndex(index);
    	}

    	public void selectIndex(int index) {
    		try {
    			selState[index] = true;
    		} catch(ArrayIndexOutOfBoundsException aie) {
    			updateSelectionStateTrackers(list);
    			selectIndex(index);

    			return;
    		}


    		if(list != null) {
    			list.revalidate();
    			list.repaint();
    		}
    	}

    	public void setEnabled(int index, boolean b) {
    		try {
    			enableState[index] = b;
    		} catch(ArrayIndexOutOfBoundsException aie) {
    			updateSelectionStateTrackers(list);
    			setEnabled(index, b);
    		}

    	}

    	public boolean isEnabled(int index) {
    		if(index == -1) {
    			return true;
    		}
    		
    		boolean isEnabled = true;

    		try {
    			isEnabled = enableState[index];
    		} catch(ArrayIndexOutOfBoundsException aie) {
    			updateSelectionStateTrackers(list);
    			return isEnabled(index);
    		}

    		return isEnabled;
    	}

    	public void enableAll(boolean b) {
    		for(int i = 0; i < enableState.length; i++) {
    			enableState[i] = b;
    		}
    	}
    }

    public void selectAll(boolean b) {
    	if(selStateHandler == null) {
    		return;
    	}

    	selStateHandler.selectAll(b);
    }

    public void setSelectedIndex(int index) {
    	if(selStateHandler == null) {
    		return;
    	}

    	selStateHandler.setSelectedIndex(index);
    }

    public void selectIndex(int index) {
    	if(selStateHandler == null) {
    		return;
    	}

    	selStateHandler.selectIndex(index);
    }

    public void enableAll(boolean b) {
    	if(selStateHandler == null) {
    		return;
    	}

    	selStateHandler.enableAll(b);
    }

    public void setEnabled(int index, boolean enable) {
    	if(selStateHandler == null) {
    		return;
    	}

    	selStateHandler.setEnabled(index, enable);
    }

    public boolean isEnabled(int index) {
    	if(selStateHandler == null) {
    		return true;
    	}

    	return selStateHandler.isEnabled(index);
    }

    public boolean isEnabledAll() {
    	if(enableState == null) return true;

    	for(int i = 0; i < enableState.length; i++) {
    		if(!isEnabled(i)) {
    			return false;
    		}
    	}

    	return true;
    }
    
    
    // EmptyIcon implementation
    public static class EmptyIcon implements Icon {
    
	    int width = 16, height = 16;
	    
	    public EmptyIcon() {
	        setSize(16,16);
	    }
	    
	    public EmptyIcon(int width, int height) {
	        setSize(width, height);
	    }
	    
	    public void setSize(int width, int height) {
	        this.width = width;
	        this.height = height;
	    }   
	    
	        
	    public int getIconWidth() {  return width; }
	    
	    public int getIconHeight() { return height; }
	    
	    public void paintIcon(Component c, Graphics g, int x, int y) {}
	}
}