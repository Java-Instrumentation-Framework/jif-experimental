package edu.mbl.jif.gui.table;

// File: EachRowRenderer.java
/*
 * (swing1.1beta3)
 */


import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 * @version 1.0 11/09/98
 */

public class EachRowRenderer implements TableCellRenderer {
  protected Hashtable renderers;
  protected TableCellRenderer renderer, defaultRenderer;

  public EachRowRenderer() {
    renderers = new Hashtable();
    defaultRenderer = new DefaultTableCellRenderer();
  }

  public void add(int row, TableCellRenderer renderer) {
    renderers.put(new Integer(row),renderer);
  }

  public Component getTableCellRendererComponent(JTable table,
      Object value, boolean isSelected, boolean hasFocus,
                                      int row, int column) {
    renderer = (TableCellRenderer)renderers.get(new Integer(row));
    if (renderer == null) {
      renderer = defaultRenderer;
    }
    return renderer.getTableCellRendererComponent(table,
             value, isSelected, hasFocus, row, column);
  }
}
