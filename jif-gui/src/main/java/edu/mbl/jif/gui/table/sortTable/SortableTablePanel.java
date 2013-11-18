package edu.mbl.jif.gui.table.sortTable;

import edu.mbl.jif.gui.test.FrameForTest;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
//import edu.mbl.jif.gui.FrameForTest;


/**
 * Sortable Table Panel
 */
public class SortableTablePanel
      extends JPanel
{
   private SmallButton addRowButton;
   private SmallButton removeRowButton;
   private SmallButton testButton;
   public SortableTableModel dataModel;
   JTable table;

   public SortableTablePanel (String[] headerStr, int[] columnWidth,
         Object[][] dataObjectArray, SortableTableModel _dataModel) {
      dataModel = _dataModel;
      setLayout(new BorderLayout());
      dataModel.setDataVector(dataObjectArray, headerStr);
      table = new JTable(dataModel);

      //table.setShowGrid(false);
      table.setShowVerticalLines(true);
      table.setShowHorizontalLines(true);

      //
      SortButtonRenderer renderer = new SortButtonRenderer();
      TableColumnModel model = table.getColumnModel();
      for (int i = 0; i < headerStr.length; i++) {
         model.getColumn(i).setHeaderRenderer(renderer);
         model.getColumn(i).setPreferredWidth(columnWidth[i]);
      }

      Font font = new Font("Serif", Font.PLAIN, 10);
      JTableHeader header = table.getTableHeader();
      header.setFont(font);
      header.addMouseListener(new SortableHeaderListener(header, renderer));
      JScrollPane pane = new JScrollPane(table);
      add(pane, BorderLayout.CENTER);
      addRowButton = new SmallButton("Add");
      removeRowButton = new SmallButton("Delete");
      testButton = new SmallButton("test");
      addRowButton.addActionListener(new AddRowListener());
      removeRowButton.addActionListener(new RemoveRowListener());
      testButton.addActionListener(new testListener());
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(addRowButton);
      buttonPanel.add(removeRowButton);
      buttonPanel.add(testButton);
      add(buttonPanel, BorderLayout.SOUTH);
   }


   //
   //---------------------------------------------------------------------
   // Listeners
   private class testListener
         implements ActionListener
   {
      public void actionPerformed (ActionEvent e) {
         dumpDataTable();
      }
   }



   private class AddRowListener
         implements ActionListener
   {
      public void actionPerformed (ActionEvent e) {
         dataModel.indexes = null;
         Vector vec = new Vector();
         dataModel.addRow(vec);
         dataModel.updateIndexes();
         dataModel.reSort();
      }
   }



   private class RemoveRowListener
         implements ActionListener
   {
      public void actionPerformed (ActionEvent e) {
         if (table.getSelectedRow() != -1) {
            dataModel.removeRow(table.getSelectedRow());
            table.validate();
            table.updateUI();
         }
      }
   }



   //
   //---------------------------------------------------------------------
   class SmallButton
         extends JButton
   {
      public SmallButton (String label) {
         super();
         this.setText(label);
         setFont(new Font("Serif", Font.PLAIN, 10));
         setMargin(new Insets(0, 2, 0, 2));
      }
   }



   //---------------------------------------------------------------------------
   private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,
         Locale.US);

   private static Date getDate (String dateString) {
      Date date = null;
      try {
         date = dateFormat.parse(dateString);
      }
      catch (ParseException ex) {
         date = new Date();
      }
      return date;
   }


   //
   //---------------------------------------------------------------------
   public void dumpDataTable () {
      int rows = dataModel.getRowCount();
      int cols = dataModel.getColumnCount();

      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            //get value at row(i)/column(j)
            System.out.print(((Vector) dataModel.getDataVector().elementAt(i)).elementAt(
                  j) + "  ");
         }
         System.out.println();
      }
   }


   //-------------------------------------------------------------------------
   // Test sortable table of Preferences
   //-------------------------------------------------------------------------
   public static void main (String[] args) {
      //----------------------------------------------------------------------
      //    String[] headerStr = {"Parameter", "Value"};
      //    int[] columnWidth = {100, 150};
      //    Object[][] dataObjectArray = Prefs.getPrefsObjects();
      // Table Definition (Example) --------------------------------------------------------
      String[] headerStr = {"Name", "Date", "Size", "Dir"};
      int[] columnWidth = {100, 150, 100, 50};
      Object[][] dataObjectArray = new Object[][] { {"b", getDate("98/12/02"),
            new Integer(14), new Boolean(false)}, {"a", getDate("99/01/01"),
            new Integer(67), new Boolean(false)}, {"d", getDate("99/02/11"),
            new Integer(2), new Boolean(false)}, {"c", getDate("99/02/27"), new Integer(7),
            new Boolean(false)}, {"foo", new Date(), new Integer(5), new Boolean(true)},
            {"bar", new Date(), new Integer(10), new Boolean(true)}
      };

      //
      //---------------------------------------------------------------------
      // Define the SortableTableModel
      SortableTableModel dataModel = new SortableTableModel()
      {
         public Class getColumnClass (int col) {
            switch (col) {
               case 0:
                  return String.class;
               case 1:
                  return Date.class;
               case 2:
                  return Integer.class;
               case 3:
                  return Boolean.class;
               default:
                  return Object.class;
            }
         }


         public boolean isCellEditable (int row, int col) {
            switch (col) {
               case 1:
                  return false;
               default:
                  return true;
            }
         }


         public void setValueAt (Object obj, int row, int col) {
            switch (col) {
               case 2:
                  super.setValueAt(new Integer(obj.toString()), row, col);
                  return;
               default:
                  super.setValueAt(obj, row, col);
                  return;
            }
         }
      };

      // SortableTableModel
      //-----------------------------------------------------------------
      SortableTablePanel stp = new SortableTablePanel(headerStr, columnWidth,
            dataObjectArray, dataModel);

        FrameForTest f = new FrameForTest(stp);
      stp.dumpDataTable();
   }
}

//
// Notes... -----------------------------------------------------------
/*
   public class SimpleTableDemo ... implements TableModelListener {
      ...
      public SimpleTableDemo() {
          ...
          model = table.getModel();
          model.addTableModelListener(this);
          ...
      }
      public void tableChanged(TableModelEvent e) {
          ...
          int row = e.getFirstRow();
          int column = e.getColumn();
          String columnName = model.getColumnName(column);
          Object data = model.getValueAt(row, column);
   //        int getFirstRow()
   //          Returns the index of the first row that changed.
   //          TableModelEvent.HEADER_ROW specifies the table header.
   //        int getLastRow()
   //          The last row that changed. Again, HEADER_ROW is a possible value.
   //        int getColumn()
   //          Returns the index of the column that changed.
   //          The constant TableModelEvent.ALL_COLUMNS specifies that all
   //          the columns might have changed.
   //        int getType()
   //          What happened to the changed cells: TableModelEvent.INSERT,
   //          TableModelEvent.DELETE, or TableModelEvent.UPDATE.
           ...// Do something with the data...
       }
       ...
    }
 */
