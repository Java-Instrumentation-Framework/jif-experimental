/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.gui.table;

/**
 *
 * @author GBH
 */
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class CheckABunch extends JPanel {
    private static final Object[][] DATA = {{"One", Boolean.TRUE}, {"Two", Boolean.FALSE},
        {"Three", Boolean.TRUE}, {"Four", Boolean.FALSE}, {"Five", Boolean.TRUE},
        {"Six", Boolean.FALSE}, {"Seven", Boolean.TRUE}, {"Eight", Boolean.FALSE}};
    private static final String[] COLUMNS = {"Number", "CheckBox"};
    private DefaultTableModel model = new DefaultTableModel(DATA, COLUMNS) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) {
                return getValueAt(0, 1).getClass();
            }
            return super.getColumnClass(columnIndex);
        }
    };
    private JTable table = new JTable(model);
    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    boolean tableModelListenerIsChanging = false;

    public CheckABunch() {
        add(new JScrollPane(table));

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRow = ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRow = ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableModelListenerIsChanging) {
                    return;
                }                
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 1 || maxSelectedRow == -1 || minSelectedRow == -1) {
                    return;
                }
                tableModelListenerIsChanging = true;
                boolean value = ((Boolean)model.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    model.setValueAt(Boolean.valueOf(value), i, column);
                }

                // *** edit: added two lines
                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("CheckABunch");
        frame.getContentPane().add(new CheckABunch());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }
}