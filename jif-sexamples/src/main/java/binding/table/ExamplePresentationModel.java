package binding.table;
/*
 * Copyright (c) 2007 Component House (Hugo Vidal Teixeira). All Rights Reserved.
 * Visit: http://www.componenthouse.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Component House (Hugo Vidal Teixeira) nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.LinkedListModel;

import javax.swing.AbstractAction;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import java.awt.Frame;
import java.awt.event.ActionEvent;

/**
 * Holds the state of the GUI.
 *
 * @author Hugo Teixeira
 * @since September/2007
 */
public class ExamplePresentationModel {

	private static final String[] COLUMNS = new String[] { "Name", "Email", "Weight" };

	private Frame parent;

	private LinkedListModel<User> userListModel;
	private ValueModel userSelectionHolder;
	private SelectionInList userSelectionInList;

	private AbstractTableModel tableModel;
	private AbstractAction addAction = new AddAction();
	private AbstractAction editAction = new EditAction();
	private AbstractAction removeAction = new RemoveAction();	

	public ExamplePresentationModel() {
		retrieveData();
		userSelectionHolder = new ValueHolder();
		userSelectionInList = new SelectionInList((ListModel) userListModel, userSelectionHolder);		
		tableModel = new ExampleTableModel();
	}

	private void retrieveData() {
		userListModel = new LinkedListModel<User>();
		userListModel.add(new User("Hugo", "hvidal@componenthouse.com", 177));
		userListModel.add(new User("Joe", "joe@componenthouse.com", 172));
		userListModel.add(new User("Mike", "mike@componenthouse.com", 181));
	}

	public SelectionInList getUserSelectionInList() { return userSelectionInList; }
	public AbstractTableModel getTableModel() { return tableModel; }
	public AbstractAction getAddAction() { return addAction; }
	public AbstractAction getEditAction() { return editAction; }
	public AbstractAction getRemoveAction() { return removeAction; }

	public Frame getParent() { return parent; }
	public void setParent(Frame parent) { this.parent = parent; }

	public class ExampleTableModel extends AbstractTableModel {

        public int getRowCount() {
            return userSelectionInList.getSize();
        }

        public int getColumnCount() {
            return COLUMNS.length;
        }

        public String getColumnName(int column) {
            return COLUMNS[column];
        }

		public Object getValueAt(int rowIndex, int columnIndex) {
            User user = (User) userSelectionInList.getElementAt(rowIndex);
            switch(columnIndex) {
                case 0: return user.getName();
                case 1: return user.getEmail();
				case 2: return user.getWeight();
			}
            return "";
        }
	}

	private class AddAction extends AbstractAction {
		public AddAction() { super("New"); }

		public void actionPerformed(ActionEvent e) {
			User user = new User();
			Editor editor = new Editor(parent, user);
			if (editor.getButtonPressed() == Editor.BUTTON_OK) {
				userListModel.add(user);
				tableModel.fireTableDataChanged();
			}
		}
	}

	private class EditAction extends AbstractAction {
		public EditAction() { super("Edit"); }

		public void actionPerformed(ActionEvent e) {
			User user = (User) userSelectionHolder.getValue();
			if (user != null) {
				Editor editor = new Editor(parent, user);
				if (editor.getButtonPressed() == Editor.BUTTON_OK) {
					tableModel.fireTableDataChanged();
				}
			}
		}
	}

	private class RemoveAction extends AbstractAction {
		public RemoveAction() { super("Remove"); }

		public void actionPerformed(ActionEvent e) {
			User user = (User) userSelectionHolder.getValue();
			if (user != null) {
				userListModel.remove(user);
				tableModel.fireTableDataChanged();				
			}
		}
	}
}
