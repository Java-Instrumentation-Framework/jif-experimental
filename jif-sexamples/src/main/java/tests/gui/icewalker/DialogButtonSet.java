package tests.gui.icewalker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.event.*;


public class DialogButtonSet extends JPanel {
	
	public JButton done, cancel;
	public Window dialog = new Window( new JFrame() );
	public JPanel butPanel;
	public ActionListener listener; 
	
	public DialogButtonSet() {
		super();
		setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );		
		
		listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				
				if(source == done) {
					dialog.setVisible(false);
				}
				
				if(source == cancel) {
					dialog.setVisible(false);
				}
			}
		};
		
		done = new JButton("Done");
		done.addActionListener(listener);
		done.setActionCommand("done");
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(listener);
		cancel.setActionCommand("cancel");
		
		butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		butPanel.add(done);
		butPanel.add(cancel);
		
		add( new JSeparator() );
		add( butPanel);
	}
	
	public DialogButtonSet(Window dialog) {
		this();
		setDialog(dialog);
	}
	
	public DialogButtonSet(Window dialog, ActionListener listener) {
		this(dialog);
		setActionListener(listener);
	}
	
	public Component add(Component c) {
		if(c instanceof JButton) {
			return butPanel.add(c);
		}
		
		return super.add(c);
	}
	
	public Component add(Component c, int index) {
		if(c instanceof JButton) {
			return butPanel.add(c, index);
		}
		
		return super.add(c, index);
	}
	
	public void setDialog(Window dialog) {
		this.dialog = dialog;
	}
	
	public Window getDialog() {
		return dialog;
	}
	
	public void addActionListener(ActionListener listener) {
		done.addActionListener(listener);
		cancel.addActionListener(listener);
	}
	
	public void setActionListener(ActionListener listener) {
		done.removeActionListener(this.listener);
		cancel.removeActionListener(this.listener);
		this.listener = listener;
		done.addActionListener(listener);
		cancel.addActionListener(listener);
	}
	
	public ActionListener getActionListener() {
		return listener;
	}
	
	public JButton getDoneButton() {
		return done;
	}
	
	public JButton getCancelButton() {
		return cancel;
	}
	
	public JPanel getButtonPanel() {
		return butPanel;
	}
}