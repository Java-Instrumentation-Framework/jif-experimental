/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import static org.bbg.prefs.Main.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A general TreeNode which represents one Preference node.
 */
@SuppressWarnings("serial")
public class TNode extends DefaultMutableTreeNode implements Comparable<TNode> {

	private String name;				// local name of this node
	private Preferences pref;			// associated Preferences node, or null
	protected Map<String,String> attrs;	// preferences stored in this node
	private   String[]	keys;			// sorted keys, for access by index

	/**
	 * Creates a new empty node. If parent has associated preference node,
	 * new child preference node is also created.
	 * @param parent The parent node, null for the root node.
	 * @param name The name for this node, ignored for the root node.
	 */
	TNode (TNode parent, String name) throws BackingStoreException {
		if (parent == null)
			this.name = "";
		else {
			assert name.indexOf('/') < 0 && parent.getChild(name) == null;
			this.name = name;
			if (parent.pref != null) {
				pref = parent.pref.node(name);	// create actual pref node
				pref.flush();
			}
		}
	}

    /**
     * Creates a new Node which represents existing preference node. 
     * This method does not change the actual preferences.
     * @param parent The parent node, null for the root node.
     * @param pref The associated existing preference node.
     * @see #populateTree()
     */
    TNode (TNode parent, Preferences pref) throws BackingStoreException {
    	assert pref.nodeExists("");
    	this.pref = pref;
    	this.name = pref.name();
        if (parent != null)
        	parent.insert(this, parent.getChildCount());
        fillAttrs();
    }
    
    void fillAttrs() throws BackingStoreException {
        String[] pkeys = pref.keys();
        if (pkeys.length > 0) {
	        attrs = new HashMap<String, String>();
	        for (String key : pkeys)
	        	attrs.put(key, pref.get(key, null));
	        fillKeys();
        } else
        	attrs = null;
    }
    
    private void fillKeys() {
        keys = new String[attrs.size()];
        attrs.keySet().toArray(keys);
        Arrays.sort(keys);
    }
    
    /**
     * Deep copy initializer. The copy is not associated with any 
     * actual preferences. 
     * @param that The instance to copy.
     */
    TNode (final TNode that) {
    	name = that.name;
    	setParent(null);
    	if (that.attrs != null) {
    		attrs = new HashMap<String, String>();
    		for (String key : that.attrs.keySet())
    			attrs.put(key, that.attrs.get(key));
    		fillKeys();
    	}
    	for (int i = 0; i < that.getChildCount(); i++)
    		add(new TNode(that.getChild(i)));
    }
    
    /** Is this preference node still there? */ 
    boolean exists() throws BackingStoreException {
    	return pref == null ? true : pref.nodeExists("");
    }
    
    /**
     * Recursively creates all child nodes starting from this existing 
     * preference node. 
     */
    void populateTree() throws BackingStoreException {
    	assert pref != null;
        for (String name : pref.childrenNames()) {
            TNode child = new TNode(this, pref.node(name));
            child.populateTree();
        }
    }
    
    /**
     * Attaches given Node with all its subtree as a child of this node.
     * If this node has associated preferences, creates all preference
     * subnodes too.
     * @param that Node to attach.
     */
    void paste (TNode that) throws BackingStoreException {
    	if (pref != null) {
    		that.recreate(pref);
    		pref.flush();
    	}
    }
    
    /**
     * Create actual preferences tree using attrs of this node. 
     * @param from The root of created tree.
     */
    private void recreate (Preferences from) {
    	assert pref == null;
    	pref = from.node(name);
    	if (attrs != null)
    		for (String key : attrs.keySet())
    			pref.put(key, attrs.get(key));
    	for (int i = 0; i < getChildCount(); i++)
    		getChild(i).recreate(pref);
    }
    
    /** Removes this node with all its descendants. */
    void dispose() throws BackingStoreException {
    	if (pref != null) {
	        Preferences sup = pref.parent();
	        pref.removeNode();
	        sup.flush();
	        pref = null;
    	}
        attrs = null; keys = null;
    }
    
    /** 
     * Makes a copy of this node under a new name and attaches it to the
     * parent node. This node is disposed.
     * @param newName Node name for the copy.
     * @return The newly created copy.
     */
    TNode rename (String newName) throws BackingStoreException {
    	TNode parent = (TNode) getParent();
    	assert parent.getChild(newName) == null;
    	TNode copy = new TNode(this);
    	copy.name = newName;
    	parent.paste(copy);
    	dispose();
    	return copy;
    }
    
    /**
     * Compares subtree of this node with subtree of that node.
     * @param that The node to compare with.
     * @param attrsOnly If {@code true}, node names are ignored in comparison.
     * @return The first node (in the subtree of this) which differs from the
     * corresponding successor of that node. Returns {@code null} if no
     * discrepancy was found.
     */
    TNode diff (TNode that, boolean attrsOnly) {
    	if (!attrsOnly && !name.equals(that.name))
    		return this;
    	if (!(attrs == null && that.attrs == null)) {
    		if (attrs == null || that.attrs == null)
    			return this;
    		if (attrs.size() != that.attrs.size())
    			return this;
    		for (String key : attrs.keySet()) {
    			if (!that.attrs.containsKey(key))
    				return this;
    			if (!(attrs.get(key)).equals(that.attrs.get(key)))
    				return this;
    		}
    	}
    	int count = getChildCount(); 
    	if (count != that.getChildCount())
    		return this;
    	for (int i = 0; i < count; i++) {
    		TNode d = getChild(i).diff(that.getChild(i), attrsOnly);
    		if (d != null)
    			return d;
    	}
    	return null;
    }
    
    /** Returns list of this node and all its descendants in depth-first order */
    List<TNode> childList() {
    	List<TNode> list = new ArrayList<TNode>();
    	listChilds(list);
    	return list;
    }
    
    private void listChilds (List<TNode> list) {
    	list.add(this);
    	for (int i = 0; i < getChildCount(); i++)
    		getChild(i).listChilds(list);
    }
    
	/** 
	 * Gets a child TNode by name.
	 * @param name The child name, which can have more than one level,
	 * like foo/bar, but must not be absolute. If name is empty, this
	 * node is returned.
	 */
	TNode getChild (String name) {
		if (name.length() == 0)
			return this;
		int j = name.indexOf('/');
		assert j != 0;
		String head = j > 0 ? name.substring(0, j) : name;
		String tail = j > 0 && j+1 < name.length() ? name.substring(j+1) : null;
		for (int i = 0; i < getChildCount(); i++) {
			TNode child = (TNode) getChildAt(i);
			if (child.name.equals(head))
				return tail == null ? child : child.getChild(tail);
		}
		return null;
	}
	
	/** Get a child by index, can throw IndexOutOfBoundsException */
	final TNode getChild (int n) {
		return (TNode)getChildAt(n);
	}

	/** Returns a copy of child nodes. */
	TNode[] childs() {
    	int n = getChildCount();
    	TNode[] ret = new TNode[n];
    	for (int i = 0; i < n; i++)
    		ret[i] = (TNode) getChildAt(i);
    	return ret;
    }

	String getName() { return name; }
	
	/** 
	 * Returns full path from root to this node, like /foo/bar.
	 * If this node is not a leaf, the path is ended by "/".
	 */ 
	String fullName() {
		return isRoot() ? "/" : ((TNode)getParent()).fullName() + name +
				(isLeaf() ? "" : "/");
	}
	
	int keyCount() { 
		return keys == null ? 0 : keys.length; 
	}
	
	int indexOf (String key) {
		return keys == null ? -1 : Arrays.binarySearch(keys, key);
	}
	
	public String toString () {
		return getParent() == null ? "/" : name;
	}
	
	void print (PrintWriter wr) {
		wr.println("<p class=\"javaprefs\">" + fullName() + "</p>");
    	if (attrs != null) {
    		wr.println("<table class=\"javaprefs\">");
    		//wr.println("<caption>" + fullName() + "</caption>");
    		for (String key : keys) {
    			wr.println("<tr class=\"javaprefs\">");
    			wr.println("<td class=\"javaprefs\">" + htmlOf(key).replace(" ", "\u00b7") +  
    					"</td><td class=\"javaprefs\">" + 
    					htmlOf(attrs.get(key)).replace(" ", "\u00b7") + 
    					"</td></tr>");
    		}
    		wr.println("</table>");
    	}
    	for (int i = 0; i < getChildCount(); i++)
    		((TNode)getChildAt(i)).print(wr);
	}
	
	private static String htmlOf (String str) {
		return str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
	
	boolean isUser() {
		return pref == null ? false : pref.isUserNode();
	}

	// attributes

	void setValue (String key, Object value) throws BackingStoreException {
		assert key != null && value != null;
		if (pref != null) {
	        if (value instanceof Boolean)
	            pref.putBoolean(key, (Boolean)value);
	        else if (value instanceof byte[])
	            pref.putByteArray(key, (byte[])value);
	        else if (value instanceof Double)
	            pref.putDouble(key, (Double)value);
	        else if (value instanceof Float)
	            pref.putFloat(key, (Float)value);
	        else if (value instanceof Integer)
	            pref.putInt(key, (Integer)value);
	        else if (value instanceof Long)
	            pref.putLong(key, (Long)value);
	        else 
	            pref.put(key, value.toString());
	        pref.flush();
		}
		if (attrs == null)
			attrs = new HashMap<String, String>();
		boolean newKey = !attrs.containsKey(key);
		attrs.put(key, value.toString());
		if (newKey)
			fillKeys();
	}

	void setValue (int row, Object value) throws BackingStoreException {
		setValue(keys[row], value);
	}
	
	String getValue (String key) {
		return attrs == null ? null : attrs.get(key);
	}

	boolean hasKey (String key) { 
		return attrs == null ? false : attrs.containsKey(key);
	}
	
	String getKey (int n) {
		return attrs == null ? null : keys[n];
	}
	
	String getValue (int n) {
		return attrs == null ? null : attrs.get(keys[n]);
	}

    void deleteAllKeys() throws BackingStoreException {
		if (pref != null) {
			pref.clear(); pref.flush();
		}
		attrs = null; keys = null;
	}

	void renameKey (int row, String newKey) throws BackingStoreException {
		String key = keys[row];
		assert !attrs.containsKey(newKey) && attrs.containsKey(key);
		if (pref != null) {
	        String value = pref.get(key, null);
	        pref.put(newKey, value);
	        pref.remove(key);
	        pref.flush();
		}
		attrs.put(newKey, attrs.get(key));
		attrs.remove(key);
		keys[row] = newKey;
		Arrays.sort(keys);
	}

	void deleteKey (String key) throws BackingStoreException {
		assert attrs.containsKey(key);
		if (pref != null) {
			pref.remove(key);
			pref.flush();
		}
		attrs.remove(key);
		if (attrs.size() == 0) {
			attrs = null; keys = null;
		} else
			fillKeys();
	}

	public int compareTo (TNode that) {
		return name.compareTo(that.name);
	}

	// export and import

	private void exportNode (Writer out) throws IOException {
		if (!isRoot())	// root node has no name
			out.write(String.format("<node name=\"%s\">", htmlOf(name)));
		if (attrs != null && !attrs.isEmpty()) {
			out.write("<map>");
			for (String key : attrs.keySet()) {
				out.write(String.format("<entry key=\"%s\" value=\"%s\"/>",
				        htmlOf(key), htmlOf(attrs.get(key))));
			}
			out.write("</map>");
		} else
			out.write("<map/>");
		// subnodes are exported in sorted order
		TNode[] subs = childs();
		Arrays.sort(subs);
		for (TNode n : subs)
			n.exportNode(out);
		if (!isRoot())
			out.write("</node>");
	}

	/**
	 * Writes XML representation of this TNode, in UTF-8 encoding.
	 * @param os The target OutputStream.
	 * @param type The target Preferences tree, must be either "user" or
	 *            "system".
	 * @throws IOException, inherited from the underlying I/O.
	 */
	void export (OutputStream os, String type) 
			throws IOException, BackingStoreException {
		if (pref != null)
			pref.exportSubtree(os);
		else {
			if (type == null)
				type = "user";
			assert type.equals("user") || type.equals("system");
			Writer out = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
			out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			out.write("<!DOCTYPE preferences SYSTEM "
			        + "\"http://java.sun.com/dtd/preferences.dtd\">");
			out.write("<preferences EXTERNAL_XML_VERSION=\"1.0\">");
			out.write("<root type=\"" + type + "\"><map/>");
			exportNode(out);
			out.write("</root></preferences>");
			out.close();
		}
	}

    private static SAXParserFactory parserFactory = null;
    private static SAXParser parser = null;
    private static Handler handler = null; 
    
	/**
	 * Reads the TNode tree exported by {@link #export(OutputStream, String)}
	 * @param file The file to read.
	 * @return The root TNode of the tree.
	 */
	static TNode parse (File file) throws ParserConfigurationException,
	        SAXException, IOException {
		if (parserFactory == null)
			parserFactory = SAXParserFactory.newInstance();
		if (parser == null)
			parser = parserFactory.newSAXParser();
		else
			parser.reset();
		if (handler == null)
			handler = new Handler();
		else
			handler.reset();
		parser.parse(file, handler);
		if (handler.top == null || !handler.closed)
			throw new SAXException("parsing ended prematurely");
		return handler.top;
	}

    private static class Handler extends DefaultHandler {
        
        boolean closed = false;

        private Stack<TNode> stack = null;
        protected TNode top = null;  // top of stack
        
        Handler() { super(); }
        
        public void startElement(String uri, String localname, String qname,
                Attributes attr) throws SAXException {
            assert !closed;
            if (stack == null) {
                if (qname.equals("preferences")) 
                    stack = new Stack<TNode>();
                else
                    throw new SAXException("Not a preferences XML");
            }
            try {
	            if (qname.equals("root")) {
	                top = new TNode(null, "");
	                stack.push(top);
	                logger.fine("<root>");
	            } else if (qname.equals("node")) {
	                String nodeName = attr.getValue("name");
	                TNode curr = new TNode(top, nodeName);
	                top.insert(curr, top.getChildCount());
	                top = stack.push(curr);
	                logger.fine(String.format("<node name=\"%s\">", nodeName));
	            } else if (qname.equals("entry")) {
	                top.setValue(attr.getValue("key"), attr.getValue("value"));
	            }
            } catch (BackingStoreException ex) {
            	// TODO can be safely ignored?
            }
        }

        public void endElement(String uri, String localname, String qname) {
        	logger.fine(String.format("</%s>", qname));
            if (qname.equals("node")) {
                stack.pop();
                top = stack.peek();
            } else if (qname.equals("root")) {
                assert stack.size() == 1;
                closed = true;
            }
        }
        
        public void error(SAXParseException e) throws SAXException {
        	System.err.println(e.getColumnNumber());
        }
        
        void reset() {
            top = null;
            stack.clear(); stack = null;
            closed = false;
        }
    }

}
