// Reinhard's note:
// Lifted from http://java.sun.com/docs/books/tutorial/uiswing/components/example-swing/TableSorter.java

/** 
 * In a chain of data manipulators some behaviour is common. TableMap
 * provides most of this behavour and can be subclassed by filters
 * that only need to override a handful of specific methods. TableMap 
 * implements TableModel by routing all requests to its model, and
 * TableModelListener by routing all events to its listeners. Inserting 
 * a TableMap which has not been subclassed into a chain of table filters 
 * should have no effect.
 *
 * @version 1.4 12/17/97
 * @author Philip Milne */



/*
  The Broad Institute
  SOFTWARE COPYRIGHT NOTICE AGREEMENT
  This software and its documentation are copyright (2006) by the
  Broad Institute/Massachusetts Institute of Technology. All rights are
  reserved.

  This software is supplied without any warranty or guaranteed support
  whatsoever. Neither the Broad Institute nor MIT can be responsible for its
  use, misuse, or functionality.
*/

package calhoun.gebo.util;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TableMap extends AbstractTableModel 
                      implements TableModelListener {
    protected TableModel m_model; 

    public TableModel getModel() {
        return m_model;
    }

    public void setModel(TableModel model) {
        this.m_model = model; 
        
        // TODO: DJ -- I don't think anything removes this listener...
        model.addTableModelListener(this); 
    }

    // By default, implement TableModel by forwarding all messages 
    // to the model. 

    public Object getValueAt(int aRow, int aColumn) {
        return m_model.getValueAt(aRow, aColumn); 
    }
        
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        m_model.setValueAt(aValue, aRow, aColumn); 
    }

    public int getRowCount() {
        return (m_model == null) ? 0 : m_model.getRowCount(); 
    }

    public int getColumnCount() {
        return (m_model == null) ? 0 : m_model.getColumnCount(); 
    }
        
    public String getColumnName(int aColumn) {
        return m_model.getColumnName(aColumn); 
    }

    public Class getColumnClass(int aColumn) {
        return m_model.getColumnClass(aColumn); 
    }
        
    public boolean isCellEditable(int row, int column) { 
         return m_model.isCellEditable(row, column); 
    }
//
// Implementation of the TableModelListener interface, 
//
    // By default forward all events to all the listeners. 
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
