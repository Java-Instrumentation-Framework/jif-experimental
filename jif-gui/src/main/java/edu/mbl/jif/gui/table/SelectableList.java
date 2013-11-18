/*
 * SelectingJListSample.java
 */
package edu.mbl.jif.gui.table;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class SelectableList extends JScrollPane {
  private JList jList;

  public SelectableList() {
  }

  public SelectableList(Object[] stringList) {
      super();
    jList = new JList(stringList);
       super.getViewport().setView(jList);
    //  JScrollPane scrollPane1 = new JScrollPane(jlist);
    ListSelectionListener listSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
          System.out.print("First index: " + listSelectionEvent.getFirstIndex());
          System.out.print(", Last index: " + listSelectionEvent.getLastIndex());
          boolean adjust = listSelectionEvent.getValueIsAdjusting();
          System.out.println(", Adjusting? " + adjust);
          if (!adjust) {
            JList list = (JList) listSelectionEvent.getSource();
            int[] selections = list.getSelectedIndices();
            Object[] selectionValues = list.getSelectedValues();
            for (int i = 0, n = selections.length; i < n; i++) {
              if (i == 0) {
                System.out.print("  Selections: ");
              }
              System.out.print(selections[i] + "/" + selectionValues[i] + " ");
            }
            System.out.println();
          }
        }
      };
    jList.addListSelectionListener(listSelectionListener);
    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent mouseEvent) {
          JList theList = (JList) mouseEvent.getSource();
          if (mouseEvent.getClickCount() == 2) {
            int index = theList.locationToIndex(mouseEvent.getPoint());
            if (index >= 0) {
              Object o = theList.getModel().getElementAt(index);
              System.out.println("Double-clicked on: " + o.toString());
            }
          }
        }
      };
    jList.addMouseListener(mouseListener);
  }

  public int[] getSelectedItems() {
    int[] selections = jList.getSelectedIndices();
    Object[] selectionValues = jList.getSelectedValues();
    for (int i = 0, n = selections.length; i < n; i++) {
      if (i == 0) {
        System.out.print("  Selections: ");
      }
      System.out.print(selections[i] + "/" + selectionValues[i] + " ");
    }
    return selections;
  }

  public static void main(String[] args) {
    // pass in an array of Strings as the list
    String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    JFrame frame = new JFrame("Selecting JList");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container contentPane = frame.getContentPane();
    SelectableList selectableList = new SelectableList(labels);
//selectableList.setPreferredSize(new Dimension(100,200));
    contentPane.add(selectableList, BorderLayout.CENTER);
    frame.setSize(350, 200);
    frame.setVisible(true);
  }
}
