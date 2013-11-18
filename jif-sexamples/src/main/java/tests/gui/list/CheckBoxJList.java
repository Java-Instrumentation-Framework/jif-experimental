/*
 * CheckBoxJList.java
 *
 * Created on July 19, 2006, 10:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.list;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class CheckBoxJList extends JList
		implements ListSelectionListener {

	// Colors
	static Color listForeground, listBackground,
			listSelectionForeground, listSelectionBackground;

	static {
		UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
		listForeground = uid.getColor("List.foreground");
		listBackground = uid.getColor("List.background");
		listSelectionForeground = uid.getColor("List.selectionForeground");
		listSelectionBackground = uid.getColor("List.selectionBackground");
	}
	//

	HashSet selectionCache = new HashSet();
	int toggleIndex = -1;
	boolean toggleWasSelected;

	public CheckBoxJList() {
		super();
		setCellRenderer(new CheckBoxListCellRenderer());
		DefaultListModel defListModel = new DefaultListModel();
		setModel(defListModel);
		addListSelectionListener(this);
	}

	Map<String, Boolean> selections;

	public void setSelectionList(Map<String, Boolean> selections) {
		this.selections = selections;
	}
	
	public Map<String, Boolean> getSelectionList( ) {
			return selections;		
	}
	
	// ListSelectionListener implementation
	public void valueChanged(ListSelectionEvent lse) {
		System.out.println(lse);
		if (!lse.getValueIsAdjusting()) {
			removeListSelectionListener(this);

			// remember everything selected as a result of this action
			HashSet newSelections = new HashSet();
			int size = getModel().getSize();
			for (int i = 0; i < size; i++) {
				if (getSelectionModel().isSelectedIndex(i)) {
					newSelections.add(new Integer(i));
				}
			}

			// turn on everything that was previously selected
			Iterator it = selectionCache.iterator();
			while (it.hasNext()) {
				int index = ((Integer) it.next()).intValue();
				System.out.println("adding " + index);
				getSelectionModel().addSelectionInterval(index, index);
			}

			// add or remove the delta
			it = newSelections.iterator();
			while (it.hasNext()) {
				Integer nextInt = (Integer) it.next();
				int index = nextInt.intValue();
				if (selectionCache.contains(nextInt)) {
					getSelectionModel().removeSelectionInterval(index, index);
				} else {
					getSelectionModel().addSelectionInterval(index, index);
				}
			}

			// save selections for next time
			selectionCache.clear();
			for (int i = 0; i < size; i++) {
				if (getSelectionModel().isSelectedIndex(i)) {
					System.out.println("caching " + i);
					selectionCache.add(new Integer(i));
				}
			}
			addListSelectionListener(this);
		}
	}

	public static void main(String[] args) {
         try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception ex) {
               }
		final JList list = new CheckBoxJList();
		DefaultListModel defListModel = new DefaultListModel();
		list.setModel(defListModel);
		String[] listItems = {
			"Chris", "Joshua", "Daniel", "Michael",
			"Don", "Kimi", "Kelly", "Keagan"
		};
		Iterator it = Arrays.asList(listItems).iterator();
		while (it.hasNext()) {
			defListModel.addElement(it.next());
		}
		// show list
		JScrollPane scroller =
				new JScrollPane(list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JFrame frame = new JFrame("Checkbox JList");
		frame.getContentPane().add(scroller, BorderLayout.CENTER);
//		JButton buttonAll = new JButton("Select All");
//				buttonAll.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				for (int j = 0; j < list.getModel().getSize(); j++) {
//					System.out.println(
//							"" + list.getModel().getElementAt(j) + ": " + list.getSelectionModel().isSelectedIndex(j));
//				}
//			}
//		});
//		frame.getContentPane().add(buttonAll, BorderLayout.NORTH);
		JButton button = new JButton("Doit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int j = 0; j < list.getModel().getSize(); j++) {
					System.out.println(
							"" + list.getModel().getElementAt(j) + ": " + list.getSelectionModel().isSelectedIndex(j));
				}
			}
		});
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}

	class CheckBoxListCellRenderer extends JComponent
			implements ListCellRenderer {

		DefaultListCellRenderer defaultComp;
		JCheckBox checkbox;

		public CheckBoxListCellRenderer() {
			setLayout(new BorderLayout());
			defaultComp = new DefaultListCellRenderer();
			checkbox = new JCheckBox();
			add(checkbox, BorderLayout.WEST);
			add(defaultComp, BorderLayout.CENTER);
		}

		public Component getListCellRendererComponent(JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			defaultComp.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			/*
			 checkbox.setSelected (isSelected);
			 checkbox.setForeground (isSelected ?
			 listSelectionForeground :
			 listForeground);
			 checkbox.setBackground (isSelected ?
			 listSelectionBackground :
			 listBackground);
			 */
			checkbox.setSelected(isSelected);
			Component[] comps = getComponents();
			for (int i = 0; i < comps.length; i++) {
				comps[i].setForeground(listForeground);
				comps[i].setBackground(listBackground);
			}
			return this;
		}

	}
}
