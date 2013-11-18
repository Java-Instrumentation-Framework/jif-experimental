package edu.mbl.jif.gui.table.sortTable;


import java.awt.event.MouseEvent;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseAdapter;


public class SortableHeaderListener extends MouseAdapter {
    JTableHeader header;
    SortButtonRenderer renderer;

    public SortableHeaderListener(JTableHeader header, SortButtonRenderer _renderer) {
      this.header = header;
      this.renderer = _renderer;
    }

    public void mousePressed(MouseEvent e) {
      int col = header.columnAtPoint(e.getPoint());
      int sortCol = header.getTable().convertColumnIndexToModel(col);
      renderer.setPressedColumn(col);
      renderer.setSelectedColumn(col);
      header.repaint();
      if (header.getTable().isEditing()) {
        header.getTable().getCellEditor().stopCellEditing();
      }
      boolean isAscent;
      if (SortButtonRenderer.DOWN == renderer.getState(col)) {
        isAscent = true;
      } else {
        isAscent = false;
      }
      ( (SortableTableModel) header.getTable().getModel())
          .sortByColumn(sortCol, isAscent);
    }

    public void mouseReleased(MouseEvent e) {
      int col = header.columnAtPoint(e.getPoint());
      renderer.setPressedColumn( -1); // clear
      header.repaint();
    }
  }
