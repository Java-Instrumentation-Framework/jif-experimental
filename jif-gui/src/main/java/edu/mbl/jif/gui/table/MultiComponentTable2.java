package edu.mbl.jif.gui.table;
// File : MultiComponentTable2.java
/* (swing1.1beta3) */
//package jp.gr.java_conf.tame.swing.examples;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.EventObject;


/**
 *
 * @version 1.0 11/09/98
 */
class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

  CheckBoxRenderer() {
    setHorizontalAlignment(JLabel.CENTER);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                         boolean isSelected, boolean hasFocus,
                          int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      //super.setBackground(table.getSelectionBackground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setSelected((value != null && ((Boolean)value).booleanValue()));
    return this;
  }
}


public class MultiComponentTable2 extends JFrame {

  public MultiComponentTable2(){
    super("MultiComponent Table");

    DefaultTableModel dm = new DefaultTableModel() {
      public boolean isCellEditable(int row, int column) {
        if (column == 0) {
          return true;
        }
        return false;
      }
    };
    dm.setDataVector(
      new Object[][]{
        {"true"             ,"String"  ,"JLabel"   ,"JComboBox"},
        {"false"            ,"String"  ,"JLabel"   ,"JComboBox"},
        {new Boolean(true)  ,"Boolean" ,"JCheckBox","JCheckBox"},
        {new Boolean(false) ,"Boolean" ,"JCheckBox","JCheckBox"},
        {"true"             ,"String"  ,"JLabel"   ,"JTextField"},
        {"false"            ,"String"  ,"JLabel"   ,"JTextField"}},
      new Object[]{"Component","Data","Renderer","Editor"});

    CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
    EachRowRenderer  rowRenderer         = new EachRowRenderer();
    rowRenderer.add(2, checkBoxRenderer);
    rowRenderer.add(3, checkBoxRenderer);
    JComboBox comboBox = new JComboBox();
    comboBox.addItem("true");
    comboBox.addItem("false");
    JCheckBox checkBox = new JCheckBox();
    checkBox.setHorizontalAlignment(JLabel.CENTER);
    DefaultCellEditor comboBoxEditor = new DefaultCellEditor(comboBox);
    DefaultCellEditor checkBoxEditor = new DefaultCellEditor(checkBox);
    EachRowEditor rowEditor = new EachRowEditor();
    rowEditor.add(0, comboBoxEditor);
    rowEditor.add(1, comboBoxEditor);
    rowEditor.add(2, checkBoxEditor);
    rowEditor.add(3, checkBoxEditor);
    JTable table = new JTable(dm);
    table.getColumn("Component").setCellRenderer(rowRenderer);
    table.getColumn("Component").setCellEditor(rowEditor);

    JScrollPane scroll = new JScrollPane(table);
    getContentPane().add( scroll );
    setSize( 400, 160 );
    setVisible(true);
  }

  public static void main(String[] args) {
    MultiComponentTable2 frame = new MultiComponentTable2();
    frame.addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent e ) {
        System.exit(0);
      }
    });
  }
}
