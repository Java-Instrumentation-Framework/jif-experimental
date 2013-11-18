/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial") 
public class Editor extends JDialog implements ActionListener {

    private JPanel bigPanel = new JPanel();
    private JPanel keyPanel = new JPanel();
    private JTextField keyText = new JTextField();
    private JPanel valuePanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();
    private JTextArea valueText = new JTextArea();
    private JPanel bottomPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JButton okBtn = new JButton("Ok");
    private JButton cancelBtn = new JButton("Cancel");
    
    private boolean cancelled = false;
    private String key = null;
    private TNode node = null;
    
    String newKey, newValue;

    public Editor (Frame owner, String key, String value, TNode node) {
    	super(owner, true);
        initComponents();
    	setLocationRelativeTo(owner);
    	keyText.setText(key);
    	valueText.setText(value);
    	this.key = key;
    	this.node = node;
    	cancelBtn.addActionListener(this);
    	okBtn.addActionListener(this);
    }

    boolean isCancelled() { return cancelled; }
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit");
        bigPanel.setLayout(new java.awt.BorderLayout());
        bigPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 1, 8));
        keyPanel.setLayout(new java.awt.BorderLayout());
        keyPanel.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createTitledBorder("Key"), 
        		BorderFactory.createEmptyBorder(1, 8, 1, 8)));
        keyText.setText("jTextField1");
        keyPanel.add(keyText, java.awt.BorderLayout.CENTER);
        bigPanel.add(keyPanel, java.awt.BorderLayout.NORTH);
        valuePanel.setLayout(new java.awt.BorderLayout());
        valuePanel.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createTitledBorder("Value"), 
        		BorderFactory.createEmptyBorder(1, 8, 1, 8)));
        valueText.setColumns(60);
        valueText.setLineWrap(true);
        valueText.setRows(10);
        scrollPane.setViewportView(valueText);
        valuePanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        bigPanel.add(valuePanel, java.awt.BorderLayout.CENTER);
        getContentPane().add(bigPanel, java.awt.BorderLayout.CENTER);

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 5));
        buttonPanel.setLayout(new java.awt.GridLayout(1, 0, 6, 0));
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        bottomPanel.add(buttonPanel);
        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);
        pack();
    }

	public void actionPerformed (ActionEvent e) {
	    if (e.getSource() == cancelBtn) {
	    	cancelled = true; dispose();
	    	return;
	    }
	    newKey = keyText.getText().trim();
	    newValue = valueText.getText().trim();
	    if (newKey.length() == 0) {
	    	showError("Empty key not accepted."); return;
	    }
	    if (newKey.length() > Preferences.MAX_KEY_LENGTH) {
	    	showError(String.format(
	    			"Key length is %d characters, allowed maximum is %d.", 
	    			newKey.length(), Preferences.MAX_KEY_LENGTH));
	    	return;
	    }
	    if (!newKey.equals(key) && node.hasKey(newKey)) {
	    	showError("Duplicate keys not allowed."); return;
	    }
	    if (newValue.length() > Preferences.MAX_VALUE_LENGTH) {
	    	showError(String.format(
	    			"Value length is %d characters, allowed maximum is %d.", 
	    			newValue.length(), Preferences.MAX_VALUE_LENGTH));
	    	return;
	    }
	    cancelled = false;
	    dispose();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", 
                JOptionPane.ERROR_MESSAGE);
    }

}
