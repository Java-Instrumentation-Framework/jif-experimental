/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jactivelist;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListModel;

/**
 *
 * @author wert
 */
public class JActiveList extends JPanel {
	
	
	public JActiveList(){
		
		initComponents();
	}
	
	
	public JActiveList(ListModel model){
		
		initComponents();
		setModel(model);
	}
	
	public JActiveList(ListModel model, JActiveListCellRenderer renderer) {
		
		initComponents();
		setModel(model);
		setCellRenderer(cellRenderer);
	}
	

	public JActiveListCellRenderer getCellRenderer() {
		
		return cellRenderer;
	}

	
	public void setCellRenderer(JActiveListCellRenderer cellRenderer) {
		
		this.cellRenderer = cellRenderer;
		render();
	}

	
	public ListModel getModel() {
		return model;
	}
	

	public void setModel(ListModel model) {
		
		this.model = model;
		render();
	}

	
	private void revalidateAll() {
		
		bgndPanel.invalidate();
		scrollPane.validate();
	}
	
	
	private void rerender() {
		
		bgndPanel.removeAll();
		Map<Object, Component> newCache = new HashMap();
		for(int i = 0; i < model.getSize(); i++) {
			Object value = model.getElementAt(i);
			Component comp = getDisplayCache().get(i);
			if(comp == null) {
				comp = cellRenderer.getComponent(this, value, i);
			}
			newCache.put(value, comp);
			bgndPanel.add(comp);
		}
		
		clearDisplayCache();
		setDisplayCache(newCache);
		
		revalidateAll();
	}
	
	
	private void render(){
		
		clearDisplayCache();
		rerender();
	}
	
	
	private void clearDisplayCache(){
		
		for(Object obj : displayCache.keySet()){
			displayCache.get(obj).removeComponentListener(compListener);
		}
		displayCache.clear();
	}
	
	
	private void setDisplayCache(Map<Object, Component> newCache) {
		
		for(Object o : newCache.keySet()) {
			newCache.get(o).addComponentListener(compListener);
		}
		displayCache = newCache;
	}
	
	
	private Map<Object, Component> getDisplayCache() {
		
		return displayCache;
	}
	
	private void initComponents() {

		scrollPane = new javax.swing.JScrollPane();
		bgndPanel = new javax.swing.JPanel();

		bgndPanel.setAlignmentX(0.5F);
		bgndPanel.setLayout(new javax.swing.BoxLayout(bgndPanel, javax.swing.BoxLayout.Y_AXIS));
		scrollPane.setViewportView(bgndPanel);

		scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
	}
	
	private javax.swing.JPanel bgndPanel;
	private javax.swing.JScrollPane scrollPane;
	
	
	private ListModel				model			= new DefaultListModel();
	private JActiveListCellRenderer cellRenderer	= new DefaultActiveListCellRenderer();
	
	private Map<Object, Component> displayCache = new HashMap();
	
	private ComponentListener compListener = new ComponentAdapter() {

		@Override
		public void componentResized(ComponentEvent e) {
			JActiveList.this.revalidateAll();
			System.out.println("resized: " + e.getComponent());
		}
	};
}
