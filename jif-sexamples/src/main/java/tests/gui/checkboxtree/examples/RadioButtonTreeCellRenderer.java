/**
 * 
 */
package tests.gui.checkboxtree.examples;


import tests.gui.checkboxtree.CheckboxTree;
import tests.gui.checkboxtree.CheckboxTreeCellRenderer;
import tests.gui.checkboxtree.TreeCheckingModel;
import tests.gui.checkboxtree.TreeCheckingModel.CheckingMode;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

public class RadioButtonTreeCellRenderer implements CheckboxTreeCellRenderer {

    JRadioButton button = new JRadioButton();

    JPanel panel = new JPanel();

    JLabel label = new JLabel();

    public boolean isOnHotspot(int x, int y) {
	return (button.getBounds().contains(x, y));
    }

    public RadioButtonTreeCellRenderer() {
	label.setFocusable(true);
	label.setOpaque(true);
	panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	panel.add(button);
	panel.add(label);
	button.setBackground(UIManager.getColor("Tree.textBackground"));
	panel.setBackground(UIManager.getColor("Tree.textBackground"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, 
            boolean selected, boolean expanded, boolean leaf, int row,
	    boolean hasFocus) {
	label.setText(value.toString());
	if (selected)
	    label.setBackground(UIManager.getColor("Tree.selectionBackground"));
	else
	    label.setBackground(UIManager.getColor("Tree.textBackground"));
	TreeCheckingModel checkingModel = ((CheckboxTree) tree).getCheckingModel();
	TreePath path = tree.getPathForRow(row);
	boolean enabled = checkingModel.isPathEnabled(path);
	boolean checked = checkingModel.isPathChecked(path);
	boolean greyed = checkingModel.isPathGreyed(path);
	button.setEnabled(enabled);
	if (greyed) {
	    label.setForeground(Color.lightGray);
	} else {
	    label.setForeground(Color.black);
	}
	button.setSelected(checked);
	return panel;
    }

    public static void main(String[] args) {
	CheckboxTree tree = new CheckboxTree();
	//tree.getCheckingModel().setCheckingMode(CheckingMode.SIMPLE);
	tree.getCheckingModel().setCheckingMode(CheckingMode.PROPAGATE);
	tree.setCellRenderer(new RadioButtonTreeCellRenderer());
	JFrame frame = new JFrame("RadioButton tree");
	frame.add(tree);
	tree.expandAll();
	frame.pack();
	frame.setVisible(true);
    }
}