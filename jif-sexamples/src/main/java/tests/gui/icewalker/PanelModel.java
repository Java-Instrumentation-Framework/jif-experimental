package tests.gui.icewalker;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class PanelModel {
	
	public Vector<Component> panels;
	public Vector<String> labels;
	public Component main;
	
	public PanelModel(int size) {
		panels = new Vector<Component>(size);
		labels = new Vector<String>(size);
		setMainPanel( new JPanel() );
	}
	
	public PanelModel() {
		this(1);
	}
	
	public int getPanelCount() {
		return panels.size();
	}
	
	public int getSize() {
		return panels.size();
	}
	
	public void addPanel(Component panel, String label) {		
		panels.add(panel);
		labels.add(label);
		
		if(panels.size() == 1) {
			setMainPanel(panel);
		}
	}
	
	public void addPanel(Component panel) {
		String label = "Panel " + (labels.size() + 1);
		addPanel(panel, label);
	}
	
	public void insertPanelAt(Component panel, String label, int index) {		
		panels.insertElementAt(panel, index);
		labels.insertElementAt(label, index);
		
		if(panels.size() == 1) {
			setMainPanel(panel);
		}
	}
	
	public void insertPanelAt(Component panel, int index) {
		String label = "Panel " + (labels.size() + 1);
		insertPanelAt(panel, label, index);
	}
	
	public void removePanel(String label) {
		if(labels.indexOf(label) != -1) {
			int location = labels.indexOf(label);
			removePanel(location);
		} else {
			JOptionPane.showMessageDialog(null, "Panel \"" + label + "\" Unavailable For Removal");
		}
	}
	
	public void removePanel(int location) {
		panels.removeElementAt(location);
		labels.removeElementAt(location);
	}
	
	public void removePanel(Component panel) {
		if(panels.indexOf(panel) != -1) {
			int location = panels.indexOf(panel);
			removePanel(location);
		} else {
			JOptionPane.showMessageDialog(null, "Panel Unavailable For Removal");
		}
	}
	
	public void removeAll() {
		for(int i = panels.size() - 1; i > -1; i--) {
			removePanel(i);
		}
	}
	
	public void setMainPanel(Component main) {
		this.main = main;
	}
	
	public Component getMainPanel() {
		return main;
	}
	
	public void setPanel(int index, Component panel, String label) {
		panels.setElementAt(panel, index);
		labels.setElementAt(label, index);
	}
	
	public void setLabel(int index, String label) {
		labels.setElementAt(label, index);
	}
	
	public String getLabel(int index) {
		return labels.elementAt(index);
	}
	
	public Vector<String> getLabels() {
		return labels;
	}
	
	public Component getPanel(int index) {
		return panels.elementAt(index);
	}
	
	public Component getPanel(String label) {
		return getPanel( labels.indexOf(label) );
	}
	
	public void sortPanelsByLabel() {
		Collections.sort(labels);
	}
	
	public int getPanelIndex(String label) {
		return labels.indexOf(label);
	}
	
	public int getPanelIndex(Component c) {
		return panels.indexOf(c);
	}
}