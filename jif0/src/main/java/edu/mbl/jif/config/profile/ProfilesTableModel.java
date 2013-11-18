package edu.mbl.jif.config.profile;

/**
 * Generic Table (key, value) viewer (for selecting something...)
 *
 * @author GBH
 */

/*
derived from Definitive Guide to Swing for Java 2, Second Edition
By John Zukowski ISBN: 1-893115-78-X Publisher: APress
 */
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public class ProfilesTableModel extends AbstractTableModel {

        Vector keys = new Vector();
        Vector values = new Vector();
        private static final String columnNames[] = {
            "Profile",
            "Description"
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public int getRowCount() {
            return keys.size();
        }

        public Object getValueAt(int row, int column) {
            Object returnValue = null;
            if (column == 0) {
                returnValue = keys.elementAt(row);
            } else if (column == 1) {
                returnValue = values.elementAt(row);

            }
            return returnValue;
        }

        public synchronized void updateTableContents(java.util.Hashtable<Object, Object> hTable) {
            Enumeration newKeys = hTable.keys();
            keys.removeAllElements();
            while (newKeys.hasMoreElements()) {
                keys.addElement(newKeys.nextElement());
            }

            Enumeration newValues = hTable.elements();
            values.removeAllElements();
            while (newValues.hasMoreElements()) {
                values.addElement(newValues.nextElement());
            }
            fireTableDataChanged();
        }

    }


class SelectionListener
    implements ListSelectionListener {

    JTable table;
    // It is necessary to keep the table since it is not possible
    // to determine the table from the event's source
    SelectionListener(final JTable table) {
        this.table = table;
    }

    public void valueChanged(ListSelectionEvent e) {
        // If cell selection is enabled, both row and column change events are fired
        System.out.println(e.toString());
        if (e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()) {
            // Column selection changed
            int first = e.getFirstIndex();
            int last = e.getLastIndex();
            System.out.println("Col: " + first + " - " + last);
        } else if (e.getSource() == table.getColumnModel().getSelectionModel() && table.getColumnSelectionAllowed()) {
            // Row selection changed
            int first = e.getFirstIndex();
            int last = e.getLastIndex();
            System.out.println("Row: " + first + " - " + last);
        }

        if (e.getValueIsAdjusting()) {
            // The mouse button has not yet been released
            }
    }

}
