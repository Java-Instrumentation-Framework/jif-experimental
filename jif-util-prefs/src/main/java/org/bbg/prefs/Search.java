/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial") 
public class Search extends JDialog implements ActionListener, ItemListener {
	
	private JPanel bottomPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JButton cancelBtn = new JButton("Close");
    private JButton okBtn = new JButton("Ok");
    private JPanel centerPanel = new JPanel();
    private JPanel checkPanel = new JPanel();
    private JPanel wherePanel = new JPanel();
    private JPanel howPanel = new JPanel();
    private JCheckBox keysBox = new JCheckBox("in preference keys");
    private JCheckBox namesBox = new JCheckBox("in node names");
    private JCheckBox valuesBox = new JCheckBox("in preference values");
    private JCheckBox substrBox = new JCheckBox("substrings match");
    private JCheckBox caseBox = new JCheckBox("ignore case");
    private JCheckBox regexpBox = new JCheckBox("regular expression");
    private JTextField textField = new JTextField();
    private JPanel textPanel = new JPanel();
    private TitledBorder topDir = BorderFactory.createTitledBorder("/");

    private Viewer view;
    private List<TNode> list;	// nodes to look into
    
    // search status
    private TNode	curr;		// current node
    private int		index;		// of current node
    private int		row, col;	// of current key and value;
    private int		rows;		// of current node
    
    private boolean substr;
    private boolean nocase;
    private boolean regexp;
    private String  prev = "";		// previous search string
    private Pattern pattern;
    private Matcher matcher;
    
    public Search (Frame owner) {
    	super(owner, false);
        initComponents();
    	setLocationRelativeTo(owner);
    	setAlwaysOnTop(true);
    	setVisible(false);
    	okBtn.setText("Start");
    	cancelBtn.addActionListener(this);
    	okBtn.addActionListener(this);
    	regexpBox.addItemListener(this);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setTitle("Search");
        centerPanel.setLayout(new BorderLayout());

        textPanel.setLayout(new GridLayout(1, 1, 0, 0));
        textPanel.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createEmptyBorder(8, 8, 0, 8), 
        		BorderFactory.createCompoundBorder(topDir, 
        				BorderFactory.createEmptyBorder(0, 0, 0, 0))));
        textField.setColumns(40);
        textPanel.add(textField);
        centerPanel.add(textPanel, BorderLayout.NORTH);
        
        checkPanel.setLayout(new GridLayout(1, 2, 0, 0));

        wherePanel.setLayout(new GridLayout(3, 0, 0, 0));
        wherePanel.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createEmptyBorder(8, 8, 8, 8), 
        		BorderFactory.createCompoundBorder(
        				BorderFactory.createTitledBorder("Find where:"), 
        				BorderFactory.createEmptyBorder(0, 8, 0, 8))));
        wherePanel.add(namesBox);
        wherePanel.add(keysBox);
        wherePanel.add(valuesBox);
        checkPanel.add(wherePanel);

        howPanel.setLayout(new GridLayout(3, 0, 0, 0));
        howPanel.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createEmptyBorder(8, 8, 8, 8), 
        		BorderFactory.createCompoundBorder(
        				BorderFactory.createTitledBorder("Find how:"), 
        				BorderFactory.createEmptyBorder(0, 8, 0, 8))));
        howPanel.add(substrBox);
        howPanel.add(caseBox);
        howPanel.add(regexpBox);
        checkPanel.add(howPanel);

        centerPanel.add(checkPanel, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 16, 5));
        buttonPanel.setLayout(new GridLayout(1, 0, 4, 0));
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        bottomPanel.add(buttonPanel);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setResizable(false);
    }

	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == cancelBtn)
	    	setVisible(false);
	    else
	    	proceed();
    }
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getItemSelectable() == regexpBox) {
			substrBox.setEnabled(!regexpBox.isSelected());
			caseBox.setEnabled(!regexpBox.isSelected());
		}
	}
	
	void reset (Viewer view, TNode start) {
		this.view = view;
		topDir.setTitle("Find in " + start.fullName());
		//validate();
		setVisible(true);
    	list = start.childList();
    	curr = null;
    	index = row = rows = -1; col = 2;
    	okBtn.setText("Start");
	}
	
	private boolean next() {
		if (col < 1)
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
    	String what = textField.getText();	// no trim
    	if (what.length() == 0) {
    		showError("Please specify what to search."); return;
    	}
    	boolean inKeys = keysBox.isSelected();
    	boolean inValues = valuesBox.isSelected();
    	boolean inNodes = namesBox.isSelected();
    	substr = substrBox.isSelected();
    	nocase = caseBox.isSelected();
    	regexp = regexpBox.isSelected();
    	if (!inKeys && !inValues && !inNodes) {
    		showError("Please select where to search."); return;
    	}
    	if (regexp && !what.equals(prev)) {
    		try {
    			pattern = Pattern.compile(what);
    			matcher = pattern.matcher("");
    			prev = what;
    		} catch (PatternSyntaxException ex) {
    			showError(ex.getMessage()); return;
    		}
    	}
    	okBtn.setText("Next");
    	while (next()) {
    		if (row >= 0) {
		    	if (col == 0 && inKeys && curr.attrs != null) {
		    		String key = curr.getKey(row);
	    			if (match(what, key)) {
	    				view.select(curr); view.select(row, 0);
	    				return;
	    			}
		    	}
		    	if (col == 1 && inValues && curr.attrs != null) {
		    		String val = curr.getValue(row);
	    			if (match(what, val)) {
	    				view.select(curr); view.select(row, 1);
	    				return;
	    			}
		    	}
		    	continue;
    		} 
    		if (col < 0 && inNodes && match(what, curr.getName())) {
	    		view.select(curr); return;
	    	}
    	}
		JOptionPane.showMessageDialog(this, "Search finished.");
		index = -1; okBtn.setText("Start");
    }

    private boolean match (String what, String str) {
    	if (regexp) 
    		return matcher.reset(str).matches();
    	if (nocase) {
    		what = what.toLowerCase(); str = str.toLowerCase();
    	}
    	return substr ? str.contains(what) : str.equals(what);
    }
    
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", 
                JOptionPane.ERROR_MESSAGE);
    }

}
