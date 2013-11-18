package tests.gui.icewalker;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class LabelFieldPanel { //TODO  extends ScrollablePanel {
	
/*	JPanel labels, fields;	
	boolean tidy = true;
	int hgap = 5, vgap = 5;
	
	
	public LabelFieldPanel() {
		super();
		createUI();
	}
	
	public LabelFieldPanel(String label, Component field) {
		this();
		addGroup(label, field);
	}
	
	public LabelFieldPanel(Component label, Component field) {
		this();
		addGroup(label, field);
	}
	
	public LabelFieldPanel(Component label, Component field, boolean tidy) {
		super();
		makeTidy(tidy);
		
		if(isTidy()) {			
			labels = new JPanel( new GridLayout(0,1,5,10) );
			labels.setOpaque(false);
			if(label != null)
				labels.add(label);

			fields = new JPanel( new GridLayout(0,1,5,10) );
			fields.setOpaque(false);
			if(field != null)
				fields.add(field);

			JPanel panel = new JPanel();
			panel.setLayout( new BoxLayout(panel, BoxLayout.X_AXIS) );
			panel.add(labels);
			panel.add(fields);
			// TODO add(panel);
		} else {
			// TODO setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
			if(label != null)
				// TODO add(label);
			if(field != null)
			// TODO 	add(field);
		}		
	}
	
	public LabelFieldPanel(String label, Component field, boolean tidy) {
		this(Utilities.customLabel(label, JLabel.LEFT), field, tidy );
	}
	
	public LabelFieldPanel(boolean tidy) {
		this((String)null, null, tidy);
	}
	
	public LabelFieldPanel(int hgap, int vgap) {
		this();
		setGaps(hgap, vgap);
	}
	
	public void addGroup(String label, Component field) {
		addGroup( Utilities.customLabel(label, JLabel.LEFT), field );		
	}
	
	public void addGroup(Component label, Component field) {
		if(isTidy()) {
			labels.add(label);
			fields.add(field);
		} else {
			add(label);
			add(field);
		}
		
		validate();
		repaint();
	}
	
	public void insertGroup(String label, Component field, int index) {
		insertGroup( Utilities.customLabel(label, JLabel.LEFT), field, index );		
	}
	
	public void insertGroup(Component label, Component field, int index) {
		if(isTidy()) {
			labels.add(label, index);
			fields.add(field, index);
		} else {
			add(label, index);
			add(field, index);
		}
		
		validate();
		repaint();
	}
	
	public void makeTidy(boolean b) {
		tidy = b;
	}
	
	public boolean isTidy() {
		return tidy;
	}
	
	public JPanel getLabels() {
		return labels;
	}
	
	public JPanel getFields() {
		return fields;
	}
	
	public void setLabels(JPanel labels) {
		this.labels = labels;
		validate();
		repaint();
	}
	
	public void setFields(JPanel fields) {
		this.fields = fields;
		validate();
		repaint();
	}
	
	public void setLabels(Vector<Object> labels) {
		this.labels.removeAll();
		
		for(int i = 0; i < labels.size(); i++) {
			Object lab = labels.elementAt(i);
			if(lab instanceof String) {
				this.labels.add( new JLabel((String)lab) );
			} else if(lab instanceof Component) {
				this.labels.add( (Component)lab );
			}
		}
	}
	
	public void createUI() {
		if(getComponents().length != 0)
			removeAll();
		
		setLayout( new FlowLayout(FlowLayout.CENTER) );
		
		labels = new JPanel( new GridLayout(0,1,5,10) );
		labels.setOpaque(false);
		fields = new JPanel( new GridLayout(0,1,5,10) );
		fields.setOpaque(false);
		
		JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setLayout( new BoxLayout(panel, BoxLayout.X_AXIS) );
			panel.add(labels);
			panel.add(fields);
			
		add(panel);
	}
	
	public void removeGroup(int location) {
		labels.remove(location);
		fields.remove(location);
		validate();
		repaint();
	}
	
	public void setVGap(int vgap) {
		setGaps( getHGap(), vgap );
	}
	
	public void setHGap(int hgap) {
		setGaps( hgap, getVGap() );
	}
	
	public void setGaps(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
		updateLayoutManager();
	}
	
	public int getVGap() {
		return vgap;
	}
	
	public int getHGap() {
		return hgap;
	}
	
	public void updateLayoutManager() {
		if(labels != null) {
			GridLayout layout = new GridLayout(0,1, getHGap(), getVGap() );
			labels.setLayout( layout );
			fields.setLayout( layout );
		}
	}
      */
}