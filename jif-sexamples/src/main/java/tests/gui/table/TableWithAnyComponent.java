/*
 * TableWithAnyComponent.java
 * Created on October 15, 2007, 11:14 AM
 */

package tests.gui.table;

// From Table.java, Author: Zafir Anjum
import tests.gui.*;
import tests.gui.table.JComponentCellEditor;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class TableWithAnyComponent
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Table");
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				Window win = e.getWindow();
				win.setVisible(false);
				win.dispose();
				System.exit(0);
			}
		} );
		
		JTable table = new JTable( 6,4 )
		{
			public TableCellRenderer getCellRenderer(int row, int column) {
				TableColumn tableColumn = getColumnModel().getColumn(column);
				TableCellRenderer renderer = tableColumn.getCellRenderer();
				if (renderer == null) {
					Class c = getColumnClass(column);
					if( c.equals(Object.class) )
					{
						Object o = getValueAt(row,column);
						if( o != null )
							c = getValueAt(row,column).getClass();
					}
					renderer = getDefaultRenderer(c);
				}
				return renderer;
			}
			
			public TableCellEditor getCellEditor(int row, int column) {
				TableColumn tableColumn = getColumnModel().getColumn(column);
				TableCellEditor editor = tableColumn.getCellEditor();
				if (editor == null) {
					Class c = getColumnClass(column);
					if( c.equals(Object.class) )
					{
						Object o = getValueAt(row,column);
						if( o != null )
							c = getValueAt(row,column).getClass();
					}
					editor = getDefaultEditor(c);
				}
				return editor;
			}
			
		};
		
		// Buttons
		table.setValueAt( new JButton("Button"), 0, 0 );
		table.setValueAt( new JButton("Button"), 0, 1 );
		
		
		// Combobox
		JComboBox combo = new JComboBox( new String[] {"First", "Second", "Third"} );
		table.setValueAt( combo, 1, 1 );
		
		// Labels
		table.setValueAt( new JLabel("Label"), 1, 0 );
		JLabel label = new JLabel( "Label"); //, new ImageIcon( table.getClass().getResource("new.gif") ),JLabel.LEFT);
		table.setValueAt( label, 1, 2 );
		
		// Scrollbar
		table.setValueAt( new JScrollBar(JScrollBar.HORIZONTAL), 2,1 );
		
		// RadioButtons
		JRadioButton b1 = new JRadioButton( "Group1 Button1" );
		JRadioButton b2 = new JRadioButton( "Group1 Button2" );
		JRadioButton b3 = new JRadioButton( "Group1 Button3" );
		
		ButtonGroup g1 = new ButtonGroup();
		g1.add( b1 );
		g1.add( b2 );
		g1.add( b3 );
		
		table.setValueAt( b1, 3, 0 );
		table.setValueAt( b2, 3, 1 );
		table.setValueAt( b3, 3, 2 );
		
		table.setDefaultRenderer( JComponent.class, new JComponentCellRenderer() );
		table.setDefaultEditor( JComponent.class, new JComponentCellEditor() );
		JScrollPane sp = new JScrollPane(table);
		
		
		frame.getContentPane().add( sp );
		frame.pack();
		frame.show();
	}
}


class JComponentCellRenderer implements TableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
        return (JComponent)value;
    }
}






