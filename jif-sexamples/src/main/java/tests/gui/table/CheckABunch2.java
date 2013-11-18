package tests.gui.table;

/**
 *
 * @author GBH
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.DefaultListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * @see http://stackoverflow.com/questions/4526779
 */
public class CheckABunch2 extends JPanel {

	private int CHECK_COL = 1;
	private Object[][] data;
	private String[] columns;
	private DataModel dataModel;
	private JTable table;
	private DefaultListSelectionModel selectionModel;

	public CheckABunch2(final Object[][] data, final String[] columns) {
		super(new BorderLayout());
		this.data = data;
		this.columns = columns;
		dataModel = new DataModel(data, columns);
		table = new JTable(dataModel);
		this.add(new JScrollPane(table));
		this.add(new ControlPanel(), BorderLayout.SOUTH);
		table.setPreferredScrollableViewportSize(new Dimension(250, 175));
		selectionModel = (DefaultListSelectionModel) table.getSelectionModel();
	}

	private class DataModel extends DefaultTableModel {

		public DataModel(Object[][] data, Object[] columnNames) {
			super(data, columnNames);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == CHECK_COL) {
				return getValueAt(0, CHECK_COL).getClass();
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == CHECK_COL;
		}

	}

	private class ControlPanel extends JPanel {

		public ControlPanel() {
			this.add(new JLabel("Selection:"));
			Font font = new Font("Helvetica", Font.PLAIN, 10);
			JButton clear = new JButton(new SelectionAction("Clear", false));
			clear.setFont(font);
			JButton check = new JButton(new SelectionAction("Check", true));
			check.setFont(font);
			this.add(clear);
			this.add(check);
		}

	}

	private class SelectionAction extends AbstractAction {

		boolean value;

		public SelectionAction(String name, boolean value) {
			super(name);
			this.value = value;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < dataModel.getRowCount(); i++) {
				if (selectionModel.isSelectedIndex(i)) {
					dataModel.setValueAt(value, i, CHECK_COL);
				}
			}
		}

	}

	private static void createAndShowUI(Object[][] data, final String[] columns) {
		JFrame frame = new JFrame("CheckABunch");
		frame.add(new CheckABunch2(data, columns));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		final Object[][] DATA = {
			{"One", Boolean.TRUE}, {"Two", Boolean.FALSE},
			{"Three", Boolean.TRUE}, {"Four", Boolean.FALSE},
			{"Five", Boolean.TRUE}, {"Six", Boolean.FALSE},
			{"Seven", Boolean.TRUE}, {"Eight", Boolean.FALSE},
			{"Nine", Boolean.TRUE}, {"Ten", Boolean.FALSE}};
		final String[] COLUMNS = {"Number", "CheckBox"};

		java.awt.EventQueue.invokeLater(
				new Runnable() {
			@Override
			public void run() {
				createAndShowUI(DATA, COLUMNS);
			}

		});
	}

}