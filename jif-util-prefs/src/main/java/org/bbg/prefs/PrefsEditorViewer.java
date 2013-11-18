/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class PrefsEditorViewer extends JPanel 
		implements ActionListener, TreeSelectionListener {

    private JSplitPane		splitPane = null;
    private JScrollPane		treePane = null;
    protected JTree			tree = null;
    private DefaultTreeModel treeModel = null;
    private JScrollPane		tablePane = null;
    protected JTable		table = null;
    protected Data			data = new Data();
    protected JPopupMenu	dataPopup = new JPopupMenu();
    protected JPopupMenu	nodePopup = new JPopupMenu();
    
    private JMenuItem rowEditItem = new JMenuItem("Edit row", Main.editIcon);
    private JMenuItem keyDelItem = new JMenuItem("Delete key", Main.delIcon);
    private JMenuItem keyAddItem = new JMenuItem("Add new key", Main.addIcon);
	private JMenuItem[] dataItems = new JMenuItem[] {
			rowEditItem, keyDelItem, keyAddItem
	};

    private JMenuItem nodeAddItem = new JMenuItem("Add subnode", Main.blankIcon);
    private JMenuItem nodeRenItem = new JMenuItem("Rename", Main.blankIcon);
    private JMenuItem nodeCutItem = new JMenuItem("Cut", Main.cutIcon);
    private JMenuItem nodeCopyItem = new JMenuItem("Copy", Main.copyIcon);
    private JMenuItem nodePasteItem = new JMenuItem("Paste", Main.pasteIcon);
    private JMenuItem nodeMergeItem = new JMenuItem("Merge", Main.blankIcon);
    private JMenuItem editFindItem = new JMenuItem("Find", Main.findIcon);
    private JMenuItem nodeCmpItem = new JMenuItem("Compare", Main.blankIcon);
    private JMenuItem nodeDelItem = new JMenuItem("Delete", Main.delIcon);
    private JMenuItem nodeElimItem = new JMenuItem("Eliminate", Main.blankIcon);
    private JMenuItem nodePruneItem = new JMenuItem("Prune", Main.blankIcon);
    private JMenuItem refreshItem = new JMenuItem("Refresh", Main.refrIcon);
    private JMenuItem printItem = new JMenuItem("Save as HTML", Main.printIcon);
    private JMenuItem[] nodeItems = {
    		nodeAddItem, nodeRenItem, nodeDelItem, nodePruneItem, nodeElimItem, 
    		refreshItem, null, nodeCutItem, nodeCopyItem, nodePasteItem,  
    		nodeMergeItem, null, editFindItem, nodeCmpItem,	printItem
    };
    protected PrefsEditor	main;
    private TNode	root;
    protected boolean dirty;			// has unsaved changes
    private boolean real;				// represents actual preferences
    protected int	row, col;           // current table cell
    protected TNode	currNode = null;    // currently selected Node
    
    private	File	file = null;		// for loaded worksheets
    
    // TODO undo
    
    // copy target is common for all viewers
    protected static TNode copyNode = null;
    protected static TNode copyRef = null;	// points to TNode in copy buffer
    protected static TNode cutNode = null;
    private   static PrefsEditorViewer copyView = null;	// viewer owning copyRef
    
    public PrefsEditorViewer (PrefsEditor main, Preferences pref, int divloc)
    		throws BackingStoreException {
        this.main = main; 
        real = true; dirty = false;
        initialize();
        splitPane.setDividerLocation(divloc);
        root = new TNode(null, pref);
        root.populateTree();
        treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);
        tree.setCellRenderer(new MyCellRenderer());
        checkActions();
    }
    
    public PrefsEditorViewer (PrefsEditor main, File file, int divloc) 
    		throws ParserConfigurationException, SAXException, IOException,
    		BackingStoreException {
        this.main = main;
        this.file = file;
        real = false; dirty = false;
        initialize();
        splitPane.setDividerLocation(divloc);
        root = file != null ? TNode.parse(file) : new TNode(null, "");
        treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);
        tree.setCellRenderer(new MyCellRenderer());
        checkActions();
    }

    int getDividerLocation() {
        return splitPane.getDividerLocation();
    }
    
    boolean isReal() { return real; }
    
    boolean isDirty() { return !real && dirty; }
    
    static boolean hasCopyNode() { return copyNode != null; }
    
    TNode selectedNode() { return currNode; }

    File getFile() { return file == null ? null : file; }
    
    /** Return suggested title for HTML export page */
    String title() {
    	if (file != null)
    		return file.getPath();
    	String ret = root.isUser() ? "USER@" : "SYSTEM@";
    	try {
    		ret += InetAddress.getLocalHost().getHostName();
    	} catch (UnknownHostException ex) {
    		ret += "localhost";
    	}
    	return ret;
    }
    
    public void actionPerformed (ActionEvent e) {
    	try {
	    	if (e.getSource() == rowEditItem)
	    		tableEdit();
	    	else if (e.getSource() == keyAddItem)
	    		keyAdd();
	    	else if (e.getSource() == keyDelItem)
	    		keyDelete();
	    	else if (e.getSource() == nodeAddItem)
	    		nodeAdd();
	    	else if (e.getSource() == nodeRenItem)
	    		nodeRename();
	    	else if (e.getSource() == nodeDelItem)
	    		nodeDelete();
	    	else if (e.getSource() == nodePruneItem)
	    		nodePrune();
	    	else if (e.getSource() == nodeElimItem)
	    		nodeEliminate();
	    	else if (e.getSource() == nodeCutItem)
	    		cut();
	    	else if (e.getSource() == nodeCopyItem)
	    		copy();
	    	else if (e.getSource() == nodePasteItem)
	    		paste();
	    	else if (e.getSource() == nodeMergeItem)
	    		merge();
	    	else if (e.getSource() == nodeCmpItem)
	    		compare();
	    	else if (e.getSource() == editFindItem)
	    		startSearch();
	    	else if (e.getSource() == refreshItem)
	    		refresh();
	    	else if (e.getSource() == printItem)
	    		main.filePrint(currNode);
	    	else {
	    		assert false;
	    	}
    	} catch (BackingStoreException ex) {
    		showError(ex.getMessage());
    	}
    }    
    
    private final void initialize() {
    	for (JMenuItem it : dataItems) {
    		it.addActionListener(this); dataPopup.add(it);
    	}
    	for (JMenuItem it : nodeItems) {
    		if (it != null) {
    			it.addActionListener(this); nodePopup.add(it);
    		} else
    			nodePopup.addSeparator();
    	}
        //
        tree = new JTree();
        tree.setScrollsOnExpand(true);
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
            public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
            private void maybeShowPopup(MouseEvent e) {
                if (!e.isPopupTrigger())
                    return;
                TreePath currPath = tree.getPathForLocation(e.getX(), e.getY());
                if (currPath == null)
                    return;
                tree.setSelectionPath(currPath);
                nodePopup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        treePane = new JScrollPane();
        treePane.setViewportView(tree);
        //
        table = new JTable(data);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(true);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
            public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
            private void maybeShowPopup(MouseEvent e) {
                if (!e.isPopupTrigger())
                    return;
                row = table.rowAtPoint(e.getPoint());
                col = table.columnAtPoint(e.getPoint());
                if (row < 0 || col < 0)
                    return;
                dataPopup.show(e.getComponent(), e.getX(), e.getY());
            }
            public void mouseClicked (MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    row = table.rowAtPoint(e.getPoint());
                    col = table.columnAtPoint(e.getPoint());
                if (row >= 0)
                    tableEditAction(col);
            	}
            }
        });
        
        tablePane = new JScrollPane();
        tablePane.setViewportView(table);
        //
        setLayout(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        		treePane, tablePane);
        add(splitPane, BorderLayout.CENTER);
        //
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent ev) {
				SwingUtilities.updateComponentTreeUI(dataPopup);
				SwingUtilities.updateComponentTreeUI(nodePopup);
			}
		});
    }
    
    void startSearch() {
    	PrefsEditor.search.reset(this, currNode == null ? root : currNode);
    }
    
    void checkActions() {
    	boolean nonRoot = currNode != null && !currNode.isRoot();
    	nodeRenItem.setEnabled(nonRoot);
    	nodeDelItem.setEnabled(nonRoot);
    	nodePruneItem.setEnabled(currNode != null);
    	nodeElimItem.setEnabled(nonRoot);
    	nodeCutItem.setEnabled(nonRoot);
    	nodePasteItem.setEnabled(copyNode != null);
    	nodeMergeItem.setEnabled(copyNode != null);
    	nodeCmpItem.setEnabled(copyNode != null);
    	keyAddItem.setEnabled(currNode != null);
    	refreshItem.setEnabled(real);
    	main.checkActions();
    }
    
    /**
     * Called on change in tree node selection.
     * Any editing in progress is cancelled.
     */
    public void valueChanged (TreeSelectionEvent ev) {
        currNode = (TNode)tree.getLastSelectedPathComponent();
        if (data.editRow >= 0) {
            table.editingCanceled(null);
            data.editCol = data.editRow = -1;
        }
        dataChanged();
        checkActions();
    }
    
    /** Selects given node, switching tab and expanding the tree if necessary. */
    void select (TNode node) {
    	JTabbedPane owner = (JTabbedPane) getParent();
    	if (owner.getSelectedComponent() != this)
    		owner.setSelectedComponent(this);
		tree.setSelectionPath(new TreePath(node.getPath()));
    }
    
    void select (int row, int col) {
    	table.setColumnSelectionInterval(col, col);
    	table.setRowSelectionInterval(row, row);
    }
    
    protected void dataChanged() {
        data.fireTableDataChanged();
    }
    
    protected void tableEditAction (int col) {
        data.editRow = row; data.editCol = col;
        table.editCellAt(row, col);
        JTextField edit = (JTextField)table.getEditorComponent();
        edit.grabFocus();
        edit.selectAll();
        edit.setCaretPosition(edit.getText().length());
    }
    
    private void tableEdit () {
    	String key = (String)data.getValueAt(row, 0);
    	String value = (String)data.getValueAt(row, 1); 
    	Editor edit = new Editor(main, key, value, currNode);
    	edit.setVisible(true);
    	if (edit.isCancelled())
    		return;
    	if (!value.equals(edit.newValue))
    		data.setValueAt(edit.newValue, row, 1);
    	if (!key.equals(edit.newKey))
    		data.setValueAt(edit.newKey, row, 0);
    }
    
    void keyAdd () {
        if (currNode == null)
            return;
        String key = JOptionPane.showInputDialog(this, "Enter new key:");
        if (key == null)
            return;
        if (currNode.indexOf(key) >= 0) {
            showError("Key already exists: " + key);
            return;
        }
        try {
        	currNode.setValue(key, "");
        } catch (BackingStoreException ex) {
        	showError(ex.getMessage());
        }
        dirty = true; checkActions();
        dataChanged();
        row = currNode.indexOf(key);
        data.editRow = row; data.editCol = 1;
        table.editCellAt(row, 1);
    }
    
    protected void keyDelete() throws BackingStoreException {
        String key = currNode.getKey(row);
        int ans = JOptionPane.showConfirmDialog(table,
                "Delete key '" + key + "' ?", "Confirm", 
                JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            currNode.deleteKey(key);
            dirty = true; checkActions();
            dataChanged();
        }
    }
    
    private void nodeAdd() {
        assert currNode != null;
        String name = JOptionPane.showInputDialog(this, "Enter new node name:");
        if (name == null || (name = name.trim()).length() == 0)
            return;
        if (currNode.getChild(name) != null) {
        	showError("Duplicate names not allowed."); return;
        }
        if (!validNodeName(name))
        	return;
        try {
            TNode newNode = new TNode(currNode, name);
            treeModel.insertNodeInto(newNode, currNode, 
                    currNode.getChildCount());
            dirty = true; checkActions();
        } catch (BackingStoreException ex) {
            showError(ex.getMessage());
        }
    }
    
    private void nodeRename() {
        assert currNode != null;
        String currName = currNode.toString();
        String name = JOptionPane.showInputDialog(this, 
                "Enter new name for " + currName);
        if (name == null || (name = name.trim()).length() == 0 || 
        		name.equals(currName) || !validNodeName(name))
            return;
        TNode parent = (TNode) currNode.getParent();
        if (parent.getChild(name) != null) {
            showError("Cannot change to existing name");
            return;
        }
        try {
            TNode renamed = currNode.rename(name);
            int index = treeModel.getIndexOfChild(parent, currNode); 
            treeModel.removeNodeFromParent(currNode);
            treeModel.insertNodeInto(renamed, parent, index);
            dirty = true; checkActions();
        } catch (BackingStoreException e) {
            showError(e.getMessage());
        }
    }
    
    private boolean validNodeName (String name) {
        if (name.indexOf('/') >= 0) {
            showError("Node name cannot contain slashes."); 
            return false;
        }
        if (name.length() > Preferences.MAX_NAME_LENGTH) {
        	showError(String.format("Name cannot be longer than %d characters.", 
        			Preferences.MAX_NAME_LENGTH));
        	return false;
        }
        return true;
    }
    
    void nodeClear() {
        if (!showConfirm("Delete all keys for selected node?"))
            return;
        try {
            currNode.deleteAllKeys();
            dataChanged();
            dirty = true; checkActions();
        } catch (BackingStoreException ex) {
            showError(ex.getMessage());
        }
    }

    private void nodeDelete (TNode node) throws BackingStoreException {
        if (node.isNodeDescendant(copyRef)) {
        	copyNode = copyRef = null; copyView = null;
        }
        if (node.isNodeDescendant(cutNode))
        	cutNode = null;
        node.dispose();
        treeModel.removeNodeFromParent(node);
    }
    
    void nodeDelete() {
        assert currNode != null && !currNode.isRoot();
        if (!showConfirm("Delete node '" + currNode.toString() + "'\n" +
                "and all its subnodes?"))
            return;
        try {
        	nodeDelete(currNode);
            dirty = true; checkActions();
        } catch (BackingStoreException ex) {
            showError("Error committing changes:\n" + ex.getMessage());
        }
        currNode = null;
    }
    
    void nodePrune() {
    	assert currNode != null;
    	try {
	    	if (!showConfirm("Delete all subnodes of '" + currNode.toString() + "'?"))
	    		return;
	    	for (TNode sub : currNode.childs())
	    		nodeDelete(sub);
	        dirty = true; checkActions();
        } catch (BackingStoreException ex) {
            showError("Error committing changes:\n" + ex.getMessage());
        }
    }
    
	void nodeEliminate() {
        assert currNode != null && !currNode.isRoot();
        if (!showConfirm("Eliminate node '" + currNode.getName() + "'\n" +
        		"and reconnect its subnodes to its parent?"))
        	return;
        TNode parent = (TNode) currNode.getParent();
        TNode[] childs = currNode.childs();
        for (TNode child : childs) {
        	String name = child.getName();
        	if (child != currNode && parent.getChild(name) != null) {
        		showError("The parent already have subnode named " + name);
        		return;
        	}
        }
        TNode elim = currNode;
        treeModel.removeNodeFromParent(elim);
        for (TNode child : childs) {
        	currNode = child;
        	cut();
        	currNode = parent;
        	paste();
        }
        try {
        	elim.dispose();
        } catch (BackingStoreException ex) {
            showError("Error committing changes:\n" + ex.getMessage());
        }
    }
    
    void copy() {
    	assert currNode != null;
    	if (cutNode != null) {
    		TNode tmp = cutNode;
    		cutNode = null;
    		treeModel.nodeChanged(tmp);
    	}
        copyNode = new TNode(currNode);
        copyRef = currNode;	copyView = this;
    	treeModel.nodeChanged(currNode);
        main.showStatus("Copied: " + copyRef.fullName());
    }
    
    void cut() {
    	if (copyRef != null) {
    		TNode tmp = copyRef;
    		copyRef = null; copyView = null;
    		treeModel.nodeChanged(tmp);
    	}
    	assert currNode != null;
    	copyNode = new TNode(currNode);
    	cutNode = currNode;
    	treeModel.nodeChanged(currNode);
    	main.showStatus("Cut: " + cutNode.fullName());
    }
    
    void paste() {
        assert currNode != null && copyNode != null;
        try {
        	if (currNode.getChild(copyNode.getName()) != null) {
        		showError("Duplicate names not allowed."); return;
        	}
        	currNode.paste(copyNode);
            treeModel.insertNodeInto(copyNode, currNode, currNode.getChildCount());
            postPaste();
        } catch (BackingStoreException ex) {
            showError("Error committing changes:\n" + ex.getMessage()); return;
        }
    }
    
    void merge() {
        assert currNode != null && copyNode != null;
        try {
        	merge(copyNode, currNode);
        	postPaste();
        } catch (BackingStoreException ex) {
            showError("Error committing changes:\n" + ex.getMessage()); return;
        }
    }
    
    private void merge (TNode src, TNode dst) throws BackingStoreException {
    	for (TNode sub : src.childs()) {
    		TNode par = dst.getChild(sub.getName()); 
    		if (par == null) {
    			dst.paste(sub);
    			treeModel.insertNodeInto(sub, dst, dst.getChildCount());
    		} else
    			merge(sub, par);
    	}
    }
    
    private void postPaste() throws BackingStoreException {
    	if (copyRef != null) {
    		TNode tmp = copyRef;
    		copyRef = null; 
    		treeModel.nodeChanged(tmp);
    	}
        if (cutNode != null) {
        	cutNode.dispose();
        	treeModel.removeNodeFromParent(cutNode);
        }
		cutNode = copyRef = copyNode = null;
		copyView = null;
		tree.expandPath(new TreePath(currNode.getPath()));
		main.showStatus("Ready");
        dirty = true; checkActions();
    }
    
    void compare() {
    	if (currNode == null)
    		return;
    	if (copyNode == null) {
    		showError("First, copy a node to compare with."); return;
    	}
    	PrefsEditorCompare.start(copyView, this, copyRef, currNode);
    	/*
    	TNode d = currNode.diff(copyNode, false);
    	if (d == null)
    		main.showStatus("Node trees are identical.");
    	else {
    		//tree.expandPath(d.getPath());
    		select(d);
    		//main.showStatus("The first non-identical subnode is selected.");
    	}*/
    }
    
    void expandAll (boolean expand) {
    	TreePath path = currNode == null ? new TreePath(root) : 
    		new TreePath(currNode.getPath());
    	if (expand)
    		expandAll(path);
    	else
    		collapseAll(path);
    }
    
    private void expandAll (TreePath path) {
    	TreeNode node = (TreeNode) path.getLastPathComponent();
    	if (node.isLeaf())
    		return;
    	tree.expandPath(path);
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode child = node.getChildAt(i);
			TreePath next = path.pathByAddingChild(child);
			expandAll(next);
		}
    }
    
    private void collapseAll (TreePath path) {
    	TreeNode node = (TreeNode) path.getLastPathComponent();
    	if (node.isLeaf())
    		return;
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode child = node.getChildAt(i);
			TreePath next = path.pathByAddingChild(child);
			collapseAll(next);
		}
    	tree.collapsePath(path);
    }

    void print (PrintWriter wr, TNode node) throws IOException {
    	if (node == null)
    		node = root; 
    	wr.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"" +
    			" \"http://www.w3.org/TR/html4/strict.dtd\">");
    	wr.println("<html><head>");
    	wr.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
    	wr.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"javaprefs.css\">");
    	wr.println("<title>" + node.fullName() + "</title");
    	wr.println("</head><body>");
    	wr.println("<h1 class=\"javaprefs\">" + title() + "</h1>");
    	node.print(wr);
    	wr.println("</body></html>");
    	if (wr.checkError())
    		throw new IOException("Error occured during export operation.");
    }
    
    /** Reloads entire subtree of this Node from actual preferences */
	void refresh() {
    	try {
	    	TNode node = currNode == null ? root : currNode;
	    	if (!node.exists()) {
	    		treeModel.removeNodeFromParent(node);
	    		main.showStatus(node.getName() + " has been removed.");
	    		return;
	    	}
	    	// removals are made using a copy of node.children()
	    	for (TNode child : node.childs())
	    		treeModel.removeNodeFromParent(child);
	    	node.fillAttrs();
	    	node.populateTree();
	    	treeModel.nodeStructureChanged(node);
			tree.expandPath(new TreePath(node.getPath()));
	    	dataChanged();
	    	main.showStatus("Reloaded from preferences."); 
    	} catch (BackingStoreException ex) {
    		showError(ex.getMessage());
    	}
    }
    
    void save (File file) {
    	if (file == null)
    		file = this.file;
        try {
            FileOutputStream out = new FileOutputStream(file); 
            root.export(out, null);
            out.close();
            if (!isReal()) {
            	this.file = file;
            	dirty = false;
            }
            checkActions();
        } catch (IOException ex) {
            showError(ex.getMessage());
        } catch (BackingStoreException ex) {
        	showError(ex.getMessage());
        }
    }
    
    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", 
                JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean showConfirm (String msg) {
        int ans = JOptionPane.showConfirmDialog(this, msg, "Confirm",
                JOptionPane.YES_NO_OPTION);
        return ans == JOptionPane.YES_OPTION;
    }

    class Data extends AbstractTableModel {

        int editRow = -1, editCol = -1;
        private String[] colName = { "Key", "Value" };
        
        public int getRowCount() { 
        	return currNode == null ? 0 : currNode.keyCount(); 
        }

        public int getColumnCount() { return 2; }

        public String getColumnName(int col) { return colName[col]; }
        
        public Object getValueAt(int row, int col) {
            if (currNode == null || row < 0 || row >= currNode.keyCount() 
            		|| col < 0 || col > 2)
                return null;
            return col == 0? currNode.getKey(row) : currNode.getValue(row);
        }
        
        // for editing cells
        
        public boolean isCellEditable(int row, int col) { 
            return row == editRow && col == editCol; 
        }
        
        public void setValueAt(Object value, int row, int col) {
        	try {
	            if (col == 1) { // changing value
	            	if (!currNode.getValue(row).equals(value)) {
	            		currNode.setValue(row, value);
	            		fireTableCellUpdated(row, col);
	            		dirty = true;
	            		checkActions();
	            	}
	                return;
	            }
	            // renaming key
	            String newKey = value.toString().trim();
	            if (newKey.equals(currNode.getKey(row)))
	                return;
	            if (currNode.indexOf(newKey) >= 0)
	                showError("Key already exists: " + newKey);
	            else {
	                currNode.renameKey(row, newKey);
	                dataChanged();
	                dirty = true; checkActions();
	            }
        	} catch (BackingStoreException ex) {
        		main.showStatus(ex.getMessage());
        	}
        }
    }

    private class MyCellRenderer extends DefaultTreeCellRenderer {
    	
    	private Icon closedIcon, openedIcon;
    	
    	MyCellRenderer() { 
    		super(); 
    		openedIcon = getOpenIcon();
    		closedIcon = getClosedIcon();
    	}

    	public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf,
                int row, boolean hasFocus) {
    		TreePath path = tree.getPathForRow(row);
    		boolean cut = path == null ? false : path.getLastPathComponent() == cutNode;
    		boolean copy = path == null ? false : path.getLastPathComponent() == copyRef;
    		setLeafIcon(cut ? Main.cutIcon : copy ? Main.copyIcon : Main.fileIcon);
    		setOpenIcon(cut ? Main.cutIcon : copy ? Main.copyIcon : openedIcon);
    		setClosedIcon(cut ? Main.cutIcon : copy ? Main.copyIcon : closedIcon);
    		return super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row, hasFocus);
    	}
    }
}
