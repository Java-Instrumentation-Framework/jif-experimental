package tests.gui.icewalker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.html.*;
import javax.swing.tree.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

/** HelpViewer.java 
 *
 * This program is dummy version of Windows Help. 
 *
 * The HelpViewer uses a JTree and A JList to display it help content. The JTree is powered
 * by XML files. A Standard Version of the XML file should look (exactly) like the one below:
 * 
 * Reason: Code requires the tags "rootName", "leaf" and "node"
 *
 * To use more tags, the loadTreeData() method will have to be rewritten or modified.
 * <pre>
 	    <?xml version="1.0" encoding="UTF-8"?>
 		
 		<root>
 			<rootName url="resources/html/features.htm">M.M. Plus! Help</rootName>
 			
 			<node name="Introduction" url="resources/html/features.htm">		
 				<leaf url="resources/html/overview.htm">Overview</leaf>
 				<leaf url="resources/html/features.htm">Features</leaf>
 				<leaf url="resources/html/sys_require.htm">System Requirements</leaf>
 				<leaf url="resources/html/about.htm">About M.M. Plus!</leaf>
 			</node>
 			
 			<node name="Getting Started" url="resources/html/features.htm">
				<leaf url="resources/html/groups.htm">Defining Groups And Leaders</leaf>
				<leaf url="resources/html/adding.htm">Adding Member Profiles</leaf>
				<leaf url="resources/html/editing.htm">Editing Member Profiles</leaf>
				<leaf url="resources/html/deleting">Deleting Member Profile</leaf>
				<leaf url="resources/html/search.htm">Find Members Quickly</leaf>
			</node>
			
			<node name="Managing Finances" url="resources/html/features.htm">
				<leaf url="resources/html/financialManagement.htm">The Finances Editor</leaf>
			</node>
		</root> 
 * </pre>
 * The XML file when loaded causes the JTree to be filled and displayed.
 * 
 * The JList is still under constrcution and this will also most likely use and XML data
 * structure to produce it information.
 
   To use this, class the images references may have to be changed to suit the purpose of one's 
   application.
 **/

public class HelpViewer extends JFrame implements ActionListener, CaretListener {

	protected JTabbedPane tabPane;
	protected JList helpList;
	protected DefaultListModel listModel = new DefaultListModel();
	protected JList resultList;
	protected JTree helpTree;
	protected JTextField indexField;
	protected JTextField searchField;
	
	protected JEditorPane infoPane;
	protected String firstPage;
	protected Vector<Object> viewedPages = new Vector<Object>();
	
//	protected MenuedButton back;
//	protected MenuedButton forward;
	protected JButton hide, back, forward, refresh, display, search;
	
	Icon hideIcon = null;
	Icon showIcon = null;
	
	protected JSplitPane splitter;
	protected JComponent leftComponent;
	
	private String listFileLoc;
	protected Vector<Object> listData;
	protected Vector<Object> helpFileList;
	
	private String treeListLoc;
	protected Vector<Object> treeFileList;
	
	protected DocumentBuilderFactory builderFactory;
	protected DocumentBuilder builder;
	protected org.w3c.dom.Document document;
	
	public String mainHelpName = "Program Help";
	public DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(mainHelpName);
	public DefaultTreeCellRenderer treeRenderer;
	public Icon treeOpenIcon, treeClosedIcon, treeLeafIcon;
	
	public Hashtable<String,String> fileLoc = new Hashtable<String,String>();
	private Hashtable<String,String> switchMap = null;
	
	protected Container cp;
	private boolean useJList = true, useJTree = true;
	
	public HelpViewer(String title) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Color.white);
		
		hideIcon = new ImageIcon( getResource("resources/images/help_hide.gif") );
		showIcon = new ImageIcon( getResource("resources/images/help_show.gif") );
		//try {
		//	UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		//} catch(Exception e) {
		//	System.out.println(e);
		//}
		
		cp = getContentPane();
		cp.setLayout( new BorderLayout() );		
		
		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitter.setDividerLocation(200);
		splitter.setDividerSize(3);
		splitter.setRightComponent( infoPanel() );
		splitter.setOneTouchExpandable(false);
		splitter.setPreferredSize( new Dimension(650,500) );
		
		cp.add( splitter, BorderLayout.CENTER );
		cp.add(navButtonPanel(), BorderLayout.NORTH);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	public HelpViewer(String title, String listFileLoc) {
		this(title, listFileLoc, null);		
	}
	
	public HelpViewer(String title, String listFileLoc, String treeListLoc) {
		this(title);
		createList(listFileLoc);
		loadTreeList(treeListLoc);
		
		//getContentPane().add( indexPanel(), BorderLayout.WEST );		
	}
	
	public void createUI() {
		
	}
	
	public JTree createTreeData() {
		
		treeRenderer = new DefaultTreeCellRenderer();
		treeRenderer.setBorder( new EmptyBorder(2,2,2,2) );
		treeRenderer.setOpenIcon(treeOpenIcon);
		treeRenderer.setClosedIcon(treeClosedIcon);
		treeRenderer.setLeafIcon(treeLeafIcon);
		
		helpTree = new JTree(rootNode);
		helpTree.setFont( new Font("Verdana", Font.PLAIN, 11));
		helpTree.setCellRenderer(treeRenderer);
		helpTree.addTreeSelectionListener( new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode selRow = (DefaultMutableTreeNode) path.getLastPathComponent();
				viewPage( (String) fileLoc.get(selRow.getUserObject()), -1, NEW_PAGE );
			}
		});
		
		return helpTree;			
	}
	
	public void setTreeOpenIcon(Icon icon) {
		treeOpenIcon = icon;
		
		if(treeRenderer != null) {
			treeRenderer.setOpenIcon(treeOpenIcon);
		}
	}
	
	public void setTreeClosedIcon(Icon icon) {
		treeClosedIcon = icon;
		
		if(treeRenderer != null) {
			treeRenderer.setClosedIcon(treeClosedIcon);
		}
	}
	
	public void setTreeLeafIcon(Icon icon) {
		treeLeafIcon = icon;
		
		if(treeRenderer != null) {
			treeRenderer.setLeafIcon(treeLeafIcon);
		}
	}
	
	class ClickReporter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 1) {
			
			} else if(e.getClickCount() == 2) {
				try {
					int selRow = helpTree.getRowForLocation(e.getX(), e.getY());
					//File helpFile = new File( (String)treeFileList.elementAt(selRow) );
					//String path = "file:" + helpFile.getAbsolutePath();
					//URL page = new URL(path);
					URL page = getResource( (String)treeFileList.elementAt(selRow) );
				
					infoPane.setPage(page);
					
				} catch(IOException ioe) {
					JOptionPane.showMessageDialog(null, "Help Topic Unavailable");
					infoPane.getParent().repaint();
				}
			}
		}
	}
	
	public JPanel searchPanel() {
		searchField = new JTextField(15);
		searchField.setBorder( new CompoundBorder( new EmptyBorder(2,2,2,2), new LineBorder(Color.black) ) );
		searchField.addCaretListener(this);
			
		search = new JButton("Search");
		search.setFont( new Font("Verdana", Font.PLAIN, 10));
		search.addActionListener(this);
		
		JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			topPanel.setBackground(Color.white);
			topPanel.add(searchField);
			topPanel.add(search);
			
		Vector<Object> initialData = new Vector<Object>();
		
		resultList = new JList(initialData);
		resultList.setBorder( new CompoundBorder( new EmptyBorder(2,2,2,2), new LineBorder(Color.black) ) );
		resultList.setFont( new Font("Verdana", Font.PLAIN, 11));
			
		JPanel searchPanel = new JPanel( new BorderLayout());
			searchPanel.add(topPanel, BorderLayout.NORTH);
			searchPanel.add(resultList, BorderLayout.CENTER);
			
		return searchPanel;
	}
	
	public JPanel indexPanel() {		
		
		//helpList = new JList(listData);
		helpList = new JList(listModel);
		helpList.setFont( new Font("Verdana", Font.PLAIN, 11));
		helpList.setBorder( new CompoundBorder( new EmptyBorder(2,2,2,2), new LineBorder(Color.black) ) );
		//helpList.addActionListener(this);
		//helpList.setVisibleRowCount(15);
			
		indexField = new JTextField(20);
			indexField.setBorder( new CompoundBorder( new EmptyBorder(2,2,2,2), new LineBorder(Color.black) ) );
			indexField.addCaretListener(this);
		
		JPanel listIndexPanel = new JPanel( new BorderLayout());
			listIndexPanel.add( indexField, BorderLayout.NORTH);
			listIndexPanel.add( helpList, BorderLayout.CENTER);
		
		JTabbedPane tabPane = new JTabbedPane();			
	
		if(useJTree) {
			JScrollPane scroller = new JScrollPane( createTreeData() );
				scroller.setPreferredSize( new Dimension(200, 500) );
				
			tabPane.add( scroller , "Content");
		}
			
		if(useJList)
			tabPane.add( listIndexPanel, "Index");	
		//tabPane.add( searchPanel(), "Search");		
		
		tabPane.setFont( new Font("Verdana", Font.PLAIN, 11) );
		tabPane.setBackgroundAt(0, Color.white);
		tabPane.setPreferredSize( new Dimension(200, 500) );
		tabPane.setMinimumSize( new Dimension(200, 400) );
		
		
		display = new JButton("Display");
		display.addActionListener(this);
		display.setFont( new Font("Verdana",Font.BOLD,11) );
			
		JPanel indexBottomPanel = new JPanel( new FlowLayout(FlowLayout.RIGHT));
			indexBottomPanel.add(display);
			
		JPanel indexPanel = new JPanel( new BorderLayout());			
			indexPanel.setBorder( new CompoundBorder( new EmptyBorder(2,2,2,2), new LineBorder(Color.black) ) );
			indexPanel.add(tabPane, BorderLayout.CENTER);
			//indexPanel.add(indexBottomPanel, BorderLayout.SOUTH);
			
		return indexPanel;
		
	}
	
	public JComponent infoPanel() {
	
		infoPane = new JEditorPane();
		infoPane.setEditable(false);
		infoPane.addHyperlinkListener( createHyperLinkListener() );
		
		JScrollPane scroller = new JScrollPane(infoPane);
			scroller.setBorder( new CompoundBorder( new EmptyBorder(2,2,2,2), new LineBorder( new Color(0, 0, 150)) ) );
			scroller.setPreferredSize( new Dimension(400, 500) );
			
		return scroller;
	}
	
	public HyperlinkListener createHyperLinkListener() {
		return new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				    if (e instanceof HTMLFrameHyperlinkEvent) {
					((HTMLDocument)infoPane.getDocument()).processHTMLFrameHyperlinkEvent(
					    (HTMLFrameHyperlinkEvent)e);
				    } else {
						try {
						    //infoPane.setPage(e.getURL());
						    //System.out.println(e.getURL().toString());
						    
						    //replaced above functionality to provide history listing
						    viewPage( e.getURL().toString(), -1, NEW_PAGE );
						    
						    /*if(switchMap == null) {
						    	switchMap = new Hashtable<String,String>();
						    	
						    	Enumeration<String> enu = fileLoc.keys();
								while(enu.hasMoreElements()) {
									String key = enu.nextElement();
									String value = fileLoc.get(key);
									
									System.out.println("Key: " + value + ", Value: " + key);				
									switchMap.put(value, key);
								}
						    }
						    
						    Enumeration<String> keys = switchMap.keys();
						    while(keys.hasMoreElements()) {
						    	String key = keys.nextElement();
						    	if(key.contains(e.getDescription())) {
						    		selectNode( rootNode, switchMap.get(key) );
						    		break;
						    	}
						    }*/
						    
						} catch (Exception ioe) {
						    System.out.println("IOE: " + ioe);
						    ioe.printStackTrace();
						}
				    }
				}
		    }
		};
    }
	
	public JPanel navButtonPanel() {
		JPanel buttonPanel = new JPanel( new FlowLayout(FlowLayout.LEFT));
		
		//back = new MenuedButton("Back");
		hide = new JButton("Hide", hideIcon );
		hide.setPreferredSize( new Dimension(60,45) );
		hide.setMargin( new Insets(2,2,2,2) );
		
		back = new JButton("Back", new ImageIcon( getResource("resources/images/help_back.gif")) );
		back.setEnabled(false);
		back.setPreferredSize( new Dimension(60,45) );		
		
		forward = new JButton("Forward", new ImageIcon( getResource("resources/images/help_forward.gif")) );	
		forward.setPreferredSize( new Dimension(60,45) );
		forward.setEnabled(false);
		forward.setMargin( new Insets(2,2,2,2) );
		
		refresh = new JButton("Refresh", new ImageIcon( getResource("resources/images/help_refresh.gif")) );
		refresh.setPreferredSize( new Dimension(60,45) );
		refresh.addActionListener(this);
		refresh.setMargin( new Insets(2,2,2,2) );
		
		JButton[] button = {hide, back, forward, refresh};
		
		for(int i = 0; i < button.length; i++) {
			button[i].setFont( new Font("Arial", Font.PLAIN, 11) );
			button[i].addActionListener(this);			
			button[i].setHorizontalTextPosition(JButton.CENTER);
			button[i].setVerticalTextPosition(JButton.BOTTOM);
			button[i].setIconTextGap(5);
			button[i].setBackground( new Color(240,240,240) );
			buttonPanel.add(button[i]);
		}
		
		return buttonPanel;
	}
	
	public void createList(String listFileLoc) {
		if(listFileLoc == null) {
			useJList = false;
			return;
		}			
			
		this.listFileLoc = listFileLoc;		
		
		listData = new Vector<Object>();
		helpFileList = new Vector<Object>();
		
		Properties listProp =  new Properties();
		boolean loaded = false;
		
		try {
			listProp.load( new FileInputStream( new File(listFileLoc)));
			loaded = true;
		} catch(IOException ioe) {
			System.out.println(ioe);
		}
		
		if(loaded) {
			int total = 0;
			for(Enumeration<?> e = listProp.propertyNames(); e.hasMoreElements();) {
				Object item = e.nextElement();
				total++;
			}
			
			for(int i = 1; i < total; i++) {
				StringTokenizer st = new StringTokenizer(listProp.getProperty("item" + i) , ",");
				
				//listData.addElement(st.nextToken());
				listModel.addElement(st.nextToken());
				helpFileList.addElement(st.nextToken());
			}
			
			//helpList.setListData(listData);
			useJList = true;
		}
	}
	
	public void loadTreeList(String location) {
		if(location == null) {
			useJTree = false;
			return;
		}			
			
		this.treeListLoc = location;
		
		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			builder = builderFactory.newDocumentBuilder();
			
			//document = builder.parse( new File( getResource(location).toURI() ) );
			document = builder.parse( getResourceAsStream(location) );
			
			//org.w3c.dom.Element root = document.getElementById("root");
			NodeList root = document.getElementsByTagName("rootName");
			rootNode.setUserObject( root.item(0).getTextContent() );
			setFirstPage( ((org.w3c.dom.Element)root.item(0)).getAttribute("url") );
			fileLoc.put( root.item(0).getTextContent(), ((org.w3c.dom.Element)root.item(0)).getAttribute("url") );
			
			org.w3c.dom.Element rootElement = (org.w3c.dom.Element)document.getElementsByTagName("root").item(0);
			
			NodeList nodes = document.getElementsByTagName("node");
			for(int i = 0; i < nodes.getLength(); i++) {
				org.w3c.dom.Element node = (org.w3c.dom.Element)nodes.item(i);
				
				if(node.getParentNode().equals(rootElement)) {
					DefaultMutableTreeNode folder = new DefaultMutableTreeNode( node.getAttribute("name") );
					fileLoc.put(node.getAttribute("name"), node.getAttribute("url") );
					
					appendFiles(node, folder);
					rootNode.add(folder);
				}
			}
			
			NodeList leafs = document.getElementsByTagName("leaf");
			for(int i = 0; i < leafs.getLength(); i++) {
				org.w3c.dom.Element leaf = (org.w3c.dom.Element)leafs.item(i);
				
				if( !leaf.getParentNode().getNodeName().equals("node") ) {
					DefaultMutableTreeNode file = new DefaultMutableTreeNode( leaf.getTextContent() );
					fileLoc.put(leaf.getTextContent(), leaf.getAttribute("url") );
					
					rootNode.add(file);
				}
				
			}
			
			useJTree = true;
			
		} catch(org.xml.sax.SAXException sax) {
			sax.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void appendFiles(org.w3c.dom.Element nodeItem, DefaultMutableTreeNode folderNode) {
		
		try {
			NodeList nodes = nodeItem.getElementsByTagName("node");
	
			for(int i = 0; i < nodes.getLength(); i++) {
				org.w3c.dom.Element node = (org.w3c.dom.Element)nodes.item(i);
				
				if(node.getParentNode().equals(nodeItem)) {
					DefaultMutableTreeNode folder = new DefaultMutableTreeNode( node.getAttribute("name") );
					fileLoc.put(node.getAttribute("name"), node.getAttribute("url") );			
					
					appendFiles(node, folder);
					folderNode.add(folder);
				}
			}
			
			NodeList files = nodeItem.getElementsByTagName("leaf");
			for(int j = 0; j < files.getLength(); j++) {					
				if(files.item(j).getParentNode().equals(nodeItem)) {
					folderNode.add( new DefaultMutableTreeNode( files.item(j).getTextContent() ) );
					fileLoc.put(files.item(j).getTextContent(), ((org.w3c.dom.Element)files.item(j)).getAttribute("url") );
				}
			}			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresh() {
		try {
			URL current = infoPane.getPage();
			
			infoPane.setPage( getResource("resources/html/refresher.htm") );
			infoPane.setPage( current );
		} catch(IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	public void displayHelp() {
		try {
			if(helpList.getSelectedIndex() != -1) {
				URL page = getResource( (String)helpFileList.elementAt(helpList.getSelectedIndex()) );
				
				infoPane.setPage( page );
				
			} else {
				JOptionPane.showMessageDialog(null, "Select a topic to display");
			}
			
		} catch(IOException ioe) {
			System.out.println(ioe);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void setFirstPage(String firstPage) {
		this.firstPage = firstPage;
		
		try {
			URL page = getResource(firstPage);
			
			infoPane.setPage( page );
			
		} catch(IOException ioe) {
			System.out.println(ioe);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	int NEW_PAGE = 1, PREVIOUS_PAGE = 2, NEXT_PAGE = 3;
	int pageIndex = 0;
	
	public void viewPage(String pageLoc, int index, int status) {
		String pageLocation = "";
		
		if(status == NEW_PAGE) {
			if(pageIndex == 0 && viewedPages.size() == 0 )
				viewedPages.addElement(pageLoc);
			else {				
				if(pageIndex != viewedPages.size() - 1) {
					for(int i = viewedPages.size() - 1; i >= pageIndex; i--) {
						viewedPages.remove(i);					
					}
				}				
				viewedPages.add(pageLoc);				
				forward.setEnabled(false);
			}
			
			pageLocation = pageLoc;
			pageIndex = viewedPages.size() - 1;
		}
		
		if(status == PREVIOUS_PAGE) {
			pageLocation = (String) viewedPages.elementAt(index);
			forward.setEnabled(true);
			
			if(index == 0) {
				back.setEnabled(false);
			}
		}
		
		if(status == NEXT_PAGE) {
			pageLocation = (String) viewedPages.elementAt(index);
			back.setEnabled(true);
			
			if(index == viewedPages.size() - 1) {
				forward.setEnabled(false);
			}
		}
		
		URL currentPage = infoPane.getPage();
		try {
			URL url = getResource( pageLocation );
			
			try {
				infoPane.setPage( url );
			} catch(IOException i) {
				// added due to infoPane hyperlinklistener
				try {
					infoPane.setPage( new URL(pageLocation) );
				} catch(Exception e) {
					throw e;
				}
			}
			
			if(status == NEW_PAGE) {
				back.setEnabled(true);
			}
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Help Topic Unavailable");
			try {
				infoPane.setPage(currentPage);
			} catch(Exception e) {}			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void hideTabs(boolean hide) {
		if(hide) {
			splitter.setPreferredSize( new Dimension( 400, splitter.getPreferredSize().height ) );
			splitter.setLeftComponent(null);
		} else {
			splitter.setPreferredSize( new Dimension( 600, splitter.getPreferredSize().height ) );
			splitter.setLeftComponent(leftComponent);
		}
	}
	
	public void selectTopic(JTextField source) {
		String text = source.getText();
		boolean present = false;
		
		for(int i = 0; i < listData.size(); i++) {
			String item = (String) listData.elementAt(i);
	
			if(item.charAt(0) == text.charAt(0) ||
				item.substring(0, text.length()).toLowerCase().equals( text.toLowerCase() ) ||
				item.substring(0, text.length()).toUpperCase().equals( text.toUpperCase() ) ) {
				
				helpList.setSelectedIndex(i);
				break;
			}
		}
	}
	
	public void searchForTopic(String text) {
		if(text.length() == 0) {
			JOptionPane.showMessageDialog(null, "Please type text to search for");
		} else {
			Vector<Object> newData = new Vector<Object>();
			
			for(int i = 0; i < listData.size(); i++) {
				String topic = (String) listData.elementAt(i);
				boolean present = false;
				
				for(int x = 0; x < topic.length(); x++) {
					try {
						if(topic.charAt(x) == text.charAt(0) ) {
							if(topic.substring(x, text.length() - 2).toLowerCase().equals(text.toLowerCase()) ) {
								present = true;
							}
						}
					} catch(Exception e) {
						present = false;
						System.out.println(e);
					}
				}
				
				if(present) {
					newData.addElement(topic);
				} else {
					//newData.addElement("Not found");
				}
			}
			
			resultList.setListData(newData);
		}
	}
	
	public boolean selectNode(DefaultMutableTreeNode node, String nodeText) {
		if(node.getUserObject().toString().contains(nodeText)) {
			
			helpTree.expandRow( helpTree.getRowForPath( 
					new TreePath(node.getUserObjectPath()) ) );
			
			helpTree.setSelectionRow( helpTree.getRowForPath( 
					new TreePath(node.getUserObjectPath()) ) );
		
			System.out.println("Tried Expansion");
			helpTree.requestFocusInWindow();
			
			return true;
		}
		
		if(!node.isLeaf()) {
			for(int i = 0; i < node.getChildCount(); i++) {
				if(selectNode( (DefaultMutableTreeNode)node.getChildAt(i), nodeText )) {
					break;
				}
			}
		}
		
		return false;
	}
	
	public void setVisible(boolean b) {
		if(b && leftComponent == null) {
			splitter.setLeftComponent( leftComponent = indexPanel() );
		}
		
		super.setVisible(b);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == refresh)
			refresh();
			
		if(source == display)
			displayHelp();
			
		if(source == search) {
			try {
				searchForTopic(searchField.getText());
			} catch(Exception exc) {
				System.out.println(exc);
			}
		}
		
		if(source == back) {
			if(pageIndex == 0) {
				return;
			}
			
			viewPage(null, --pageIndex, PREVIOUS_PAGE);
		}
		
		if(source == forward) {
			if(pageIndex > viewedPages.size() - 1 ) {
				return;
			}
			
			viewPage(null, ++pageIndex, NEXT_PAGE);
		}
		
		if(source == hide) {
			hideTabs( leftComponent.isShowing() );
			
			if( !leftComponent.isShowing() ) {
				hide.setText("Show");
				hide.setIcon( hideIcon );
			} else {
				hide.setText("Hide");
				hide.setIcon( showIcon );
			}
				
		}
	}
	
	public void caretUpdate(CaretEvent e) {
		Object source = e.getSource();
		
		if(e.getDot() > 0) {
			if(source == indexField)
				selectTopic(indexField);
		}
	}
	
	public URL getResource(String location) {
		URL img = null;
		try {
			img = getClass().getResource("/" + location);			
		} catch(Exception mue) {
			System.out.println(mue);			
		}
		
		return img;
	}
	
	public InputStream getResourceAsStream(String location) {
		return getClass().getResourceAsStream("/" + location);
	}
	
	public static void main(String[] args) {
		String title = "Help";
		
		if(args.length > 0 && args[0] != null) {
			title = args[0];
		}
		
		HelpViewer help = new HelpViewer(title, null, "resources/html/treelist.xml");
			help.setTreeOpenIcon( new ImageIcon( help.getResource("resources/images/help_topic_open.gif") ) );
			help.setTreeClosedIcon( new ImageIcon( help.getResource("resources/images/help_topic_closed.gif") ) );
			help.setTreeLeafIcon( new ImageIcon( help.getResource("resources/images/help_topic_leaf.gif") ) );
			help.setVisible(true);
	}
}