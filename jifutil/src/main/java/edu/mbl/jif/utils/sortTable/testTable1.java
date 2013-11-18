package edu.mbl.jif.utils.sortTable;

import java.util.Vector;
//import edu.mbl.jif.gui.FrameForTest;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class testTable1
{

   SortableTablePanel stp;

   public testTable1 () {

      // Table Definition -----------------------------------------------------
      String[] headerStr = {"Variable", "Value"};
      int[] columnWidth = {20, 30};
      Object[][] dataObjectArray = new Object[][] { {"wavelength", new Float(2.3)},
            {"swing", new Float(4.5)}, {"zeroIntensity", new Float(0.9)}
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
                  return Float.class;
               default:
                  return Object.class;
            }
         }


         public boolean isCellEditable (int row, int col) {
            switch (col) {
               case 1:
                  return true;
               default:
                  return true;
            }
         }


         public void setValueAt (Object obj, int row, int col) {
            switch (col) {
               case 1:
                  super.setValueAt(new Float(obj.toString()), row, col);
                  return;
               default:
                  super.setValueAt(obj, row, col);
                  return;
            }
         }
      };
      // SortableTableModel
      //-----------------------------------------------------------------
      stp = new SortableTablePanel(headerStr, columnWidth, dataObjectArray, dataModel);
//      FrameForTest f = new FrameForTest(stp);
//      f.setVisible(true);
      dumpData();
   }


   public void dumpData () {
      int rows = stp.dataModel.getRowCount();
      int cols = stp.dataModel.getColumnCount();

      for (int i = 0; i < rows; i++) {
         //get value at row(i)/column(j)
         System.out.println("float "
               + ((Vector) stp.dataModel.getDataVector().elementAt(i)).elementAt(0)
               + " = "
               + ((Vector) stp.dataModel.getDataVector().elementAt(i)).elementAt(1) + ";");
      }
   }


   //-------------------------------------------------------------------------
   public static void main (String[] args) {
      new testTable1();
   }
}
