package tests.gui.icewalker;
/*
 *# Membership Manager Logo List

total=6

logo1=resources/images/mm_default_logo.gif
logo2=resources/images/mm_logo_coffee.gif
logo3=resources/images/mm_logo_star.gif
logo4=resources/images/mm_logo_t3.gif
logo5=resources/images/mm_logo_prius.gif
logo6=resources/images/mm_logo_soccer.gif

*/
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.imageio.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;




public class Utilities extends Object {

	private static Font main = new Font("Verdana", Font.PLAIN, 10);
	private static Color mainFontColor;
	private static Color mainBackgroundColor;
	private static JDialog waiter;
	private static boolean isPrintReady = false;
	public static String[] monthNames = new String[] {
										"January", "February", "March", "April", 
										"May", "June", "July", "August", "September",
										"October", "November", "December"};
	
	protected Utilities() {
	
	}
	
	
	// instant panels ==============================================================
	public static JPanel customizedPanel(Color color) {
		JPanel customized = new JPanel();
			customized.setBackground(color);	
			
		return customized;
	}
	
	public static JPanel customizedPanel() {
		Color mainColor = getMainBackgroundColor();
		JPanel customized = customizedPanel(Color.white);	
		if(mainColor != null)
			customized = customizedPanel(getMainBackgroundColor());			
			
		return customized;
	}
	
	public static JPanel customizedPanel(LayoutManager layout) {
		JPanel customized = customizedPanel();
			customized.setLayout(layout);	
			
		return customized;
	}
	
	public static JPanel customizedPanel(LayoutManager layout, Color color) {
		JPanel customized = customizedPanel(color);
			customized.setLayout(layout);	
			
		return customized;
	}
	
	public static JPanel getLabelled(Component c, String label) {
		JPanel p = new JPanel( );
			p.add( customLabel(label, JLabel.RIGHT) );
			p.add(c);
			
		return p;
	}
	
	public static JPanel createPanel(Component c, LayoutManager manager) {
		return createPanel(c, manager, true);
	}
	
	public static JPanel createPanel(Component c, LayoutManager manager, boolean opaque) {
		JPanel p = new JPanel();
			p.setLayout(manager);
			p.setOpaque(opaque);
			p.add(c);
			
		return p;
	}
	
	public static JPanel createPanel(Component c) {
		return createPanel(c, new FlowLayout(FlowLayout.CENTER) );	
	} // instant panels end
	
	public static JLabel customLabel(String label, int align) {
		JLabel custom = new JLabel(label, align);
			//custom.setFont(main);
			if(getMainFontColor() != null)
				custom.setForeground(getMainFontColor());
			
		return custom;
	}
	
	public static JLabel customLabel(String text) {
		return customLabel(text, JLabel.RIGHT);
	}	
	
	public static JLabel underlinedLabel(String text, Icon icon, int align, final Color line) {
		JLabel custom = new JLabel(text, icon, align) {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int width = getWidth();
				int height = getHeight();
				g.setColor(line);					
				g.drawLine(0, height-1, width, height-1);
			}
		};
		
		//custom.setFont(main);		
		if(getMainFontColor() != null)
			custom.setForeground(getMainFontColor());
			
		return custom;
	}
	
	public static JLabel underlinedLabel(String text) {
		JLabel underlined = underlinedLabel(text, null, JLabel.LEFT, Color.black);
		return underlined;
	}
	
	public static JLabel underlinedLabel(Icon icon){
		return underlinedLabel( null, icon, JLabel.CENTER, Color.black);
	}
	
	public static JLabel underlinedLabel(String text, int align) {
		return underlinedLabel(text, null, align, Color.black);
	}
	
	public static JLabel underlinedLabel(String text, int align, Color line) {
		return underlinedLabel(text, null, align, line);
	}
	
	public static JButton customButton(String text, ActionListener listener) {
		JButton custom = new JButton(text);
			//custom.setFont(main);
			custom.addActionListener(listener);
			
		return custom;
	}
	
	public static JButton customButton(Icon icon, ActionListener listener) {
		JButton custom = customButton((String)null, listener);
			custom.setIcon(icon);
			
		return custom;
	}
	
	public static JButton aligned(String text, Icon icon, ActionListener listener) {
		//AlignedButton align = new AlignedButton(text, icon);
		JButton align = aligned(text, icon, listener, 90, 60);			
			
		return align;
	}
	
	public static JButton aligned(String text, Icon icon, ActionListener listener, 
										int width, int height) {
		//AlignedButton align = new AlignedButton(text, icon);	
		
		JButton align = new JButton(text, icon);
			align.setPreferredSize( new Dimension(width,height) );
			//align.setFont(main);
			align.addActionListener(listener);
			align.setHorizontalTextPosition(JButton.CENTER);
			align.setVerticalTextPosition(JButton.BOTTOM);
			align.setMargin( new Insets(3,3,3,3) );
			
		return align;
	}
	
	public static Vector<Object> loadPropFileAsVector(File file, String propName, int startIndex) {
			
		Properties prop = new Properties();
		boolean loaded = false;		
		Vector<Object> propVector = new Vector<Object>();
		
		try {
			prop.load( new FileInputStream(file) );
			loaded = true;
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Failed To Load: " + file.getName(),
			"Load Error", JOptionPane.ERROR_MESSAGE );
		}
		
		if(loaded) {
			int total = Integer.parseInt( prop.getProperty("total") );
			
			for(int i = startIndex; i <= total; i++) {
				try {
					String property = prop.getProperty(propName + i);
					propVector.addElement(property);
				} catch(ArrayIndexOutOfBoundsException ai) {}
			}
		}
		
		return propVector;
	}
	
	public static void updateView(Component c) {
		c.validate();
		
		if(c.getParent() != null) {
			c.getParent().validate();
			updateView(c.getParent());
		} 
		
		c.repaint();
	}
	
	public static String toSentenceCase(String string, String delimiter) {
		if(delimiter.equals(" ")) {
			string = string.toLowerCase();
		}
		
		String[] parts = string.split(delimiter);
		if(string.length() == 1) {
			parts = new String[] {string};
		}
		
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < parts.length; i++) {
			try {
				buf.append( parts[i].substring(0,1).toUpperCase() + parts[i].substring(1));
			} catch(StringIndexOutOfBoundsException sie) {
				buf.append( parts[i] );
			}
			
			
			if(i != parts.length - 1) {
				buf.append(delimiter);
			}
		}
		
		return buf.toString();
	}
	
	public static String toSentenceCase(String string) {
		return toSentenceCase(string, " ");
	}
	
	public static String getDisplayNameForMonth(int month) 
							throws ArrayIndexOutOfBoundsException {
		return monthNames[month];
	}
	
	public static LinkLabel linkLabel(String text, int align) {
		LinkLabel linker = new LinkLabel(text, align);
		
		return linker;
	}
	
	public static void setMainFont(Font m) {
		main = m;
	}
	
	public static Font getMainFont() { return main; }
	
	public static void setMainFontColor(Color color) {
		mainFontColor = color;	
	}
	
	public static Color getMainFontColor() { return mainFontColor; }
	
	public static void setMainBackgroundColor(Color color) {
		mainBackgroundColor = color;	
	}
	
	public static Color getMainBackgroundColor() { return mainBackgroundColor; }
	
	public static void setIsPrintReady(boolean b) {
		isPrintReady = b;
	}
	
	public static boolean isPrintReady() {
		return isPrintReady;
	}
	
	public static void print(Window window, Printable printable, String header, String footer) {
		PrintOptionsDialog pod = null;
		if(window instanceof JDialog) {
			pod = new PrintOptionsDialog( (JDialog)window );			
		} else if(window instanceof JFrame) {
			pod = new PrintOptionsDialog( (JFrame)window );
		} else {
			pod = new PrintOptionsDialog( new JFrame() );
		}
		
		pod.setPrintable(printable);
		pod.setHeaderText(header);
		pod.setFooterText(footer);
		pod.setVisible(true);
	}
	
	public static void printTextComponent(Window window, JTextComponent printable, String header, String footer) {
		TextPrintOptionsDialog pod = null;
		if(window instanceof JDialog) {
			pod = new TextPrintOptionsDialog( (JDialog)window );			
		} else if(window instanceof JFrame) {
			pod = new TextPrintOptionsDialog( (JFrame)window );
		} else {
			pod = new TextPrintOptionsDialog( new JFrame() );
		}
		
		pod.setTextComponent(printable);
		pod.setHeaderText(header);
		pod.setFooterText(footer);
		pod.setVisible(true);
	}
	
	public static void printTable(Window window, JTable table, String header, String footer) {
		TablePrintOptionsDialog tpo = null;
		if(window instanceof JDialog) {
			tpo = new TablePrintOptionsDialog( (JDialog)window );			
		} else if(window instanceof JFrame) {
			tpo = new TablePrintOptionsDialog( (JFrame)window );
		} else {
			tpo = new TablePrintOptionsDialog( new JFrame() );
		}
		
		tpo.setTable(table);
		tpo.setHeaderText(header);
		tpo.setFooterText(footer);
		tpo.setVisible(true);
	}
	
	public void printTable(JTable table) {
		printTable(table, false);
	}
	
	public static void printTable(JTable table, boolean fitwidth) {
		printTable(table, fitwidth, null, null, null);
	}
	
	public static void printTable(JTable table, boolean fitWidth, 
			String headerText, String footerText, PrintRequestAttributeSet prset) {
		
        MessageFormat headerFmt;
        MessageFormat footerFmt;
        System.out.println("Printing Mode: " + (fitWidth ? "Fit Width" : "Normal") );
        JTable.PrintMode printMode = fitWidth ? JTable.PrintMode.FIT_WIDTH : 
        							 	 		JTable.PrintMode.NORMAL;

        String text = headerText;
        
        if (text != null && text.length() > 0) {
            headerFmt = new MessageFormat(text);
        } else {
            headerFmt = null;
        }

        text = footerText;
        if (text != null && text.length() > 0) {
            footerFmt = new MessageFormat(text);
        } else {
            footerFmt = null;
        }

        try {
            boolean status = table.print(printMode, headerFmt, footerFmt, 
            		true, prset, true);
            //boolean status = table.print(printMode);

            if (status) {
                JOptionPane.showMessageDialog(table, "Printing Successfully Started",
                                                    "Print Status",
                                                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(table, "Printing Cancelled",
                                                    "Print Status",
                                                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(table, "<html><center>An Error Occured Whiles Trying To Print The Table.<br>"
            									+ "Printing Cancelled!</center></html>",
                                                "Print Status", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void showPleaseWaitDialog(final Window owner, final String text) {
			Thread s = new Thread( new Runnable(){
			public void run() {
				JLabel label = new JLabel(text, JLabel.CENTER);
		    		label.setBorder( new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED),
							new EmptyBorder(20,25,20,25) ) );
		    		
		    	if(owner instanceof Dialog) {
		    		waiter = new JDialog((Dialog)owner,"Please Wait...",false);
		    	} else if(owner instanceof Frame) {
		    		waiter = new JDialog((Frame)owner,"Please Wait...",false);
		    	} else {
		    		waiter = new JDialog((Dialog)null,"Please Wait...",false);
		    	}
		    	
		    	waiter.setUndecorated(true);
		    	waiter.getContentPane().add(label);
				waiter.pack();
				waiter.setLocationRelativeTo(null);
				waiter.setVisible(true);
			}
		});
		//s.setPriority(Thread.MAX_PRIORITY);
		s.start();
    }
    
    public static void hidePleaseWaitDialog() {
    	if(waiter != null) {
	    	waiter.setVisible(false);
	    	waiter.dispose();
	    	waiter = null;
	    	System.gc();	
    	}
    }
    
    public static void createThumbnailImage(File original, File thumb, int maxDim) {
    	createThumbnailImage(original.getAbsolutePath(), thumb.getAbsolutePath(), maxDim );
    }
    
    /**
     * Reads an image in a file and creates a thumbnail in another file.
     * @param orig The name of image file.
     * @param thumb The name of thumbnail file.  
     * Will be created if necessary.
     * @param maxDim The width and height of the thumbnail must be maxDim pixels or less.
     */
    public static void createThumbnailImage(String orig, String thumb, int maxDim) {
        try {
            // Get the image from a file.
            Image inImage = new ImageIcon(orig).getImage();

            // Determine the scale.
	    	double scale = (double)maxDim / (double)inImage.getHeight(null);
	    	
            if (inImage.getWidth(null) > inImage.getHeight(null)) {
                scale = (double)maxDim / (double)inImage.getWidth(null);
            }

            // Determine size of new image. 
            // One of them should equal maxDim.
            int scaledW = (int)(scale * inImage.getWidth(null));
            int scaledH = (int)(scale * inImage.getHeight(null));
            
            if(scale > 1.0d) {
            	scaledW = (int)inImage.getWidth(null);
            	scaledH = (int)inImage.getHeight(null);
            }
            
            // Paint image.
            if(inImage.getWidth(null) > maxDim) {
				inImage = inImage.getScaledInstance(maxDim, -1, Image.SCALE_SMOOTH);
				inImage = new ImageIcon(inImage).getImage();
			}
			
			if(inImage.getHeight(null) > maxDim) {
				inImage = inImage.getScaledInstance(-1, maxDim, Image.SCALE_SMOOTH);
				inImage = new ImageIcon(inImage).getImage();
			}
			
			ImageIcon img = new ImageIcon(inImage);
			// Create an image buffer in which to paint on.
            BufferedImage outImage = new BufferedImage(img.getIconWidth(), img.getIconHeight(),
                						BufferedImage.TYPE_INT_RGB);

            // Set the scale.
            AffineTransform tx = new AffineTransform();

            // If the image is smaller than the desired image size,
            // don't bother scaling.
            //if (scale < 1.0d) {
            //    tx.scale(scale, scale);
            //}
            
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(img.getImage(), tx, null);
            g2d.dispose();

            // JPEG-encode the image and write to file.
            //OutputStream os = new FileOutputStream(thumb);
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            //encoder.encode(outImage);
            //os.close();
            ImageIO.write(outImage, "jpg", new File(thumb) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {		
		TablePrintOptionsDialog tpo = new TablePrintOptionsDialog( new JDialog() );
			tpo.setVisible(true);
	}
	
	static class PrintOptionsDialog extends JDialog implements ActionListener {
		
		public JCheckBox fitWidth, includePageNums, includeDate;
		public JRadioButton portrait, landscape;
		public JTextField headerText, footerText;
		public JLabel fitInfo;
		public DialogButtonSet bset;
		public JButton preview, page_setup;
		public JTable table;
		public String[] fitInfoText = {"Spread columns and rows"
			+ " over multiple pages as necessary", "Force all columns on single page and"
			+ " spread rows over multiple pages as necessary"};
		
		public Printable printable;
		
		public PrintOptionsDialog(JFrame parent) {
			super(parent, "Print Options", false);
			createUI();
		}
		
		public PrintOptionsDialog(JDialog parent) {
			super(parent, "Print Options", false);
			createUI();
		}
		
		public void setVisible(boolean b) {
			setIsPrintReady(b);
			super.setVisible(b);
		}
		
		public void setHeaderText(String text) {
			headerText.setText(text);
		}
		
		public void setFooterText(String text) {
			footerText.setText(text);
		}
		
		public void setPrintable(Printable printable) {
			this.printable = printable;
		}
		
		public Printable getPrintable() {
			return printable;
		}
		
		public void createUI() {
			fitWidth = new JCheckBox("Fit Width    -   ", true);
			fitWidth.setFocusPainted(false);
			fitWidth.setEnabled(false);
			fitWidth.addChangeListener( new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if(fitWidth.isSelected()) {
						fitInfo.setText("<html>" + fitInfoText[1] + "</html>");
					} else {
						fitInfo.setText("<html>" + fitInfoText[0] + "</html>");
					}
				}
			});
			
			
			
			fitInfo = new JLabel("<html>" + fitInfoText[1] + "</html>");
			//fitInfo.setBorder( new TitledBorder("Details"));
			fitInfo.setPreferredSize( new Dimension(330,55) );
			
			JPanel fitty = new JPanel( new BorderLayout() );
				fitty.add(fitWidth, BorderLayout.WEST);
				fitty.add(fitInfo, BorderLayout.CENTER);
			
			LabelFieldPanel lfp = new LabelFieldPanel();
				// TODO lfp.addGroup("Header Text: ", headerText = new JTextField(20));
				// TODO lfp.addGroup("Footer Text: ", footerText = new JTextField(20));
			
			
			
			JPanel tops = new JPanel( new GridLayout(0,2) );
				tops.setPreferredSize( new Dimension(400,50) );
				//tops.setBorder( new TitledBorder("Page Options") );
				tops.add( portrait = new JRadioButton("Portrait",true) );						
				tops.add( includePageNums = new JCheckBox("Page Numbers", true) );
				tops.add( landscape = new JRadioButton("Landscape"));
				tops.add( includeDate = new JCheckBox("Include Date", false) );
			
			ButtonGroup group = new ButtonGroup();
				group.add(portrait);
				group.add(landscape);
				
			JPanel widthPanel = new JPanel();
				widthPanel.setLayout( new BoxLayout(widthPanel, BoxLayout.Y_AXIS) );
				widthPanel.add( new TitledSeparator("Sizing") );
				widthPanel.add(fitty);
				widthPanel.add( new TitledSeparator("Headers And Footers") );
				// TODO widthPanel.add( lfp );
				widthPanel.add( new TitledSeparator("Page Options") );
				widthPanel.add( createPanel(tops, new FlowLayout(FlowLayout.RIGHT) ) );
				
			JPanel all = new JPanel();
				all.add(widthPanel);
			
			JLabel top = new JLabel("Print Options");
				top.setBackground(Color.white);
				top.setFont( new Font("Verdana", Font.BOLD, 12) );
				top.setOpaque(true);
				top.setBorder( new CompoundBorder( new EtchedBorder(EtchedBorder.LOWERED),
					 new EmptyBorder(10,18,10,18) ) );
			
			preview = new JButton("Preview...");
			preview.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Thread p = new Thread( new Runnable() {
						public void run() {
							setCursor(Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR));
							
							Printable printable = getPrintable();
							
							int orientation = landscape.isSelected() ? PageFormat.LANDSCAPE
																	 : PageFormat.PORTRAIT; 	
							// TODO new PrintPreview( printable, orientation );
							
							//new PrintPreviewS2( table ).showInDialog( TablePrintOptionsDialog.this );
							
							//preview.setPageOrientation( landscape.isSelected() ? 
							//		PageFormat.LANDSCAPE : PageFormat.PORTRAIT);
							//preview.setVisible(true);
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
					});
					p.start();
				}
			});
			
			page_setup = new JButton("Page Setup...");
			page_setup.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Thread p = new Thread( new Runnable() {
						public void run() {
							setCursor(Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR));
							
							PrinterJob job = PrinterJob.getPrinterJob();
							job.pageDialog( job.defaultPage() );
									
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
					});
					p.start();
				}
			});
			
			bset = new DialogButtonSet(this,this);
			bset.getDoneButton().setText("Print...");
			//bset.add( page_setup, 0);
			bset.add( preview, 0);
			//bset.add( new JLabel("    ") );			
				
			getContentPane().add(top, BorderLayout.NORTH);
			getContentPane().add(all,BorderLayout.CENTER);
			getContentPane().add(bset, BorderLayout.SOUTH);
			pack();
			setLocationRelativeTo(null);			
		}
		
		public String getFormattedFooterText() {
			String footer = footerText.getText();
			
			if(includeDate.isSelected()) {
				Calendar cal = Calendar.getInstance();
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int month = cal.get(Calendar.MONTH) + 1;
				int year = cal.get(Calendar.YEAR);
				String date = "(" + day + "/" + month + "/" + year + ")";
				
				if(footer.length() > 0) {
					footer = footer + " - ";
				}
				
				footer = footer + date;
			}
			
			if(includePageNums.isSelected()) {
				if(footer.length() > 0) {
					footer = footer + " - ";
				}
				
				footer = footer + "Page {0}";
			}
			
			return footer;
		}
		
		public JTable.PrintMode getPrintMode() {
			return fitWidth.isSelected() ?  JTable.PrintMode.FIT_WIDTH : 
        							 	 		JTable.PrintMode.NORMAL;
		}
		
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == bset.getCancelButton()) {
				setVisible(false);
			}
			
			if(source == bset.getDoneButton()) {
				if(getPrintable() == null) {
					JOptionPane.showMessageDialog(this, "Target Printable not available",
					"Data Error", JOptionPane.ERROR_MESSAGE);
					setVisible(false);
					return;
				}
				
				/*OrientationRequested orientation = landscape.isSelected() ?
													OrientationRequested.LANDSCAPE :
													OrientationRequested.PORTRAIT;
				
				
				HashPrintRequestAttributeSet hpset = new HashPrintRequestAttributeSet(
					new PrintRequestAttribute[] {orientation} );
					
				printTable(getTable(), fitWidth.isSelected(), 
					headerText.getText(), getFormattedFooterText(), hpset );*/
				
				try {
	                PrinterJob prnJob = PrinterJob.getPrinterJob();
	                prnJob.setPrintable(getPrintable());
	                if (prnJob.printDialog()) {
	                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	                    prnJob.print();
	                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	                }
	                
	            } catch (PrinterException ex) {
	                ex.printStackTrace();
	                System.err.println("Printing error: " + ex.toString());
	            }
					
				setVisible(false);
			}
		}
	}
	
	static class TextPrintOptionsDialog extends PrintOptionsDialog {
		
		public JTextComponent txtComponent;
		
		public TextPrintOptionsDialog(JFrame frame) {
			super(frame);
		}
		
		public TextPrintOptionsDialog(JDialog dialog) {
			super(dialog);
		}
		
		public void setTextComponent(JTextComponent txt) {
			txtComponent = txt;
		}
		
		public JTextComponent getTextComponent() {
			return txtComponent;
		}
		
		public Printable getPrintable() {
			return txtComponent.getPrintable(new MessageFormat(headerText.getText()), 
									new MessageFormat( getFormattedFooterText() ) );
		}
		
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == bset.getCancelButton()) {
				setVisible(false);
			}
			
			if(source == bset.getDoneButton()) {
				if(getTextComponent() == null) {
					JOptionPane.showMessageDialog(this, "Target Component not available",
					"Data Error", JOptionPane.ERROR_MESSAGE);
					setVisible(false);
					return;
				}
				
				OrientationRequested orientation = landscape.isSelected() ?
													OrientationRequested.LANDSCAPE :
													OrientationRequested.PORTRAIT;
				
				
				HashPrintRequestAttributeSet hpset = new HashPrintRequestAttributeSet(
					new PrintRequestAttribute[] {orientation} );
					hpset.add(MediaSizeName.ISO_A4);
				
   
   				PrintService service = 
   				              PrintServiceLookup.lookupDefaultPrintService();
   				
   				try {
   					txtComponent.print( new MessageFormat(headerText.getText()), 
						new MessageFormat(getFormattedFooterText()), true, service, hpset, true);	
   				} catch(PrinterException pe) {
   					JOptionPane.showMessageDialog(null, "Could Not Start The Print Process",
   						"Printer Error", JOptionPane.ERROR_MESSAGE);
   				}       
				
					
				setVisible(false);
			}
		}
	}
	
	static class TablePrintOptionsDialog extends PrintOptionsDialog {
		
		public TablePrintOptionsDialog(JFrame parent) {
			super(parent);
		}
		
		public TablePrintOptionsDialog(JDialog parent) {
			super(parent);			
		}
		
		public void createUI() {
			super.createUI();
			fitWidth.setEnabled(true);
		}
		
		public void setTable(JTable table) {
			this.table = table;
		}
		
		public JTable getTable() {
			return table;
		}
		
		public Printable getPrintable() {
			return table.getPrintable( getPrintMode(),
									new MessageFormat(headerText.getText()), 
									new MessageFormat( getFormattedFooterText() ) );
		}
		
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == bset.getCancelButton()) {
				setVisible(false);
			}
			
			if(source == bset.getDoneButton()) {
				if(getTable() == null) {
					JOptionPane.showMessageDialog(this, "Target Table not available",
					"Data Error", JOptionPane.ERROR_MESSAGE);
					setVisible(false);
					return;
				}
				
				OrientationRequested orientation = landscape.isSelected() ?
													OrientationRequested.LANDSCAPE :
													OrientationRequested.PORTRAIT;
				
				
				HashPrintRequestAttributeSet hpset = new HashPrintRequestAttributeSet(
					new PrintRequestAttribute[] {orientation} );
					
				printTable(getTable(), fitWidth.isSelected(), 
					headerText.getText(), getFormattedFooterText(), hpset );
					
				setVisible(false);
			}
		}
	}
}