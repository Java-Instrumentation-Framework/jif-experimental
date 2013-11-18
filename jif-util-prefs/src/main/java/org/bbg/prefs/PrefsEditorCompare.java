/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class PrefsEditorCompare extends JPanel implements ActionListener {

	private JCheckBox	keysBox, valuesBox;
	private JPanel		ctrlPanel;
	private JButton		closeBtn, nextBtn;
	private JLabel		msgLabel;
	
	private PrefsEditorViewer	view, srcView, tgtView;
	private JPanel	owner;
	private Component old;
	private Border	oldBorder;
	
	private TNode	source, target;
	private	TNode	that, curr;
    private int		index;		// of current node
    private int		row, col;	// of current key and value;
    private int		rows;		// of current node
    private int		pass;		// first or second pass
    private String	prefix;		// full path of the target node
	private List<TNode> list;	// the target tree, depth first order
	
	private static PrefsEditorCompare self = null;		// singleton
	
	private PrefsEditorCompare (JPanel owner) {
		super(new BorderLayout());
		this.owner = owner;
		ctrlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		closeBtn = new JButton(Main.closeIcon);
		closeBtn.setContentAreaFilled(false);
		//closeBtn.setBorderPainted(false);
		keysBox = new JCheckBox("keys", true);
		valuesBox = new JCheckBox("values", true);
		nextBtn = new JButton(Main.runIcon);
		nextBtn.setContentAreaFilled(false);
		//nextBtn.setBorder(null);
		JCheckBox[] boxes = new JCheckBox[] { keysBox, valuesBox };
		for (JCheckBox box : boxes) {
			box.setBorderPainted(false);
			ctrlPanel.add(box);
		}
		ctrlPanel.add(nextBtn);
		msgLabel = new JLabel();
		ctrlPanel.add(msgLabel);
		add(ctrlPanel, BorderLayout.WEST);
		add(closeBtn, BorderLayout.EAST);
		closeBtn.addActionListener(this);
		nextBtn.addActionListener(this);
	}
	
	static void create (JPanel owner) {
		assert self == null;
		self = new PrefsEditorCompare(owner);
	}
	
	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == closeBtn)
	    	close();
		else if (e.getSource() == nextBtn)
			proceed();
	}
	
	private boolean next() {
		if (col < 1 && rows > 0)
			++col;
		else if (row < rows-1) {
			++row; col = -1;
		} else if (index < list.size()-1) {
			curr = list.get(++index);
			row = col = -1;
			rows = curr.attrs == null ? 0 : curr.attrs.size();
		} else
			return false;
		return true;
	}
	
	private void proceed() {
		boolean inKeys = keysBox.isSelected();
		boolean inValues = valuesBox.isSelected();
		boolean finished = true;
		while (next()) {
			finished = false;
    		if (row >= 0) {
    			if (that == null)
    				continue;
	    		String key = curr.getKey(row);
		    	if (col == 0 && inKeys && curr.attrs != null) {
	    			if (!that.hasKey(key)) {
	    				showText("Key only in this node.");
	    				view.select(curr); view.select(row, 0);
	    				col = 2;
	    				return;
	    			}
		    	}
		    	if (col == 1 && inValues && curr.attrs != null) {
		    		String val = curr.getValue(row);
	    			if (that.hasKey(key) && !that.getValue(key).equals(val)) {
	    				showText("Values are different.");
	    				view.select(curr); view.select(row, 1);
	    				return;
	    			}
		    	}
		    	continue;
    		}
			String path = curr.fullName();
			assert path.startsWith(prefix);
			path = path.substring(prefix.length());	// relative path
			that = source.getChild(path);
			if (that == null) {
				showText("Node only in this tree.");
				int j = index+1; 
				while (j < list.size() && list.get(j).isNodeAncestor(curr))
						list.remove(j);
				row = rows = -1; col = 2;
				view.select(curr); return;
			}
		}
		if (pass == 0) {
			pass = 1;
			view = srcView;
			TNode temp = target;
			target = source; source = temp;
			that = source; 
			curr = target;
			prefix = target.fullName();
			list = target.childList();
	    	index = row = rows = -1; col = 2;
	    	proceed();
		} else {
			if (finished)
				close();
			else
				showText("Compare finished.");
		}
	}
	
	private void open (PrefsEditorViewer srcView, PrefsEditorViewer tgtView, TNode source, TNode target) {
		oldBorder = owner.getBorder();
		old = owner.getComponent(0);
		owner.remove(0);
		owner.setBorder(null);
		owner.add(this);
		owner.getParent().validate();
		pass = 0;
		this.tgtView = tgtView;
		this.srcView = srcView;
		this.source = that = source; 
		this.target = curr = target;
		//
		view = this.tgtView;
		prefix = target.fullName();
		list = target.childList();
    	index = row = rows = -1; col = 2;
    	showText("<-- Press to start");
	}
	
	static void start(PrefsEditorViewer srcView, PrefsEditorViewer tgtView, TNode source, TNode target) {
		self.open(srcView, tgtView, source, target);
	}
	
	private void close() {
		owner.remove(0);
		owner.setBorder(oldBorder);
		owner.add(old);
		owner.getParent().validate();
    	list = null;
    	source = target = that = curr = null;
    	view = srcView = tgtView = null;
	}
	
	private void showText (String text) {
		msgLabel.setText(text);
	}
	
}
