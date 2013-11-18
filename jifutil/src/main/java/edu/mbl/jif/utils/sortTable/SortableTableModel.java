/* (swing1.1) */
package edu.mbl.jif.utils.sortTable;

import javax.swing.table.*;


public class SortableTableModel
      extends DefaultTableModel
{
   public int[] indexes;
   TableSorter sorter;
   private int lastSortedColumn = -1;
   private boolean isAscent = false;

   public SortableTableModel () {
   }


   public Object getValueAt (int row, int col) {
      int rowIndex = row;
      if (indexes != null) {
         rowIndex = indexes[row];
      }
      return super.getValueAt(rowIndex, col);
   }


   public void setValueAt (Object value, int row, int col) {
      int rowIndex = row;
      if (indexes != null) {
         rowIndex = indexes[row];
      }
      super.setValueAt(value, rowIndex, col);
   }


   public void sortByColumn (int column, boolean isAscent) {
      lastSortedColumn = column;
      this.isAscent = isAscent;
      if (sorter == null) {
         sorter = new TableSorter(this);
      }
      sorter.sort(column, isAscent);
      fireTableDataChanged();
   }


   public int[] getIndexes () {
      int n = getRowCount();
      if (indexes != null) {
         if (indexes.length == n) {
            return indexes;
         }
      }
      indexes = new int[n];
      for (int i = 0; i < n; i++) {
         indexes[i] = i;
      }
      return indexes;
   }


   public void updateIndexes () {
      indexes = getIndexes();
   }


   public void reSort () {
      if (lastSortedColumn >= 0) {
         sortByColumn(lastSortedColumn, isAscent);
      }
   }

}
