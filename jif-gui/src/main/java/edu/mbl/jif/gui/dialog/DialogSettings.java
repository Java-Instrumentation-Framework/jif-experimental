package edu.mbl.jif.gui.dialog;


import edu.mbl.jif.gui.list.SelectableList;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author GBH
 */
public class DialogSettings {
    
    /** Creates a new instance of DialogSettings */
    public DialogSettings() {
    }
    public static void make(String title, String prompt, JComponent component, JComponent parent) {
        JOptionPane optionPane = new JOptionPane();
        Object msg[] = {title, component};
        optionPane.setMessage(msg);
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(parent, prompt);
        dialog.setVisible(true);
        Object value = optionPane.getValue();
        if (value == null || !(value instanceof Integer)) {
            System.out.println("Closed");
        } else {
            int i = ((Integer) value).intValue();
            if (i == JOptionPane.CLOSED_OPTION) {
                System.out.println("Closed");
            } else if (i == JOptionPane.OK_OPTION) {
                System.out.println("OKAY - value is: " + optionPane.getInputValue());
            } else if (i == JOptionPane.CANCEL_OPTION) {
                System.out.println("Cancelled");
            }
        }
    }
    
    public static void main(String[] args) {
    String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    SelectableList selectableList = new SelectableList(labels);
        (new DialogSettings()).make("Title", "Prompt", selectableList, null);
        int [] selected = selectableList.getSelectedItems();
        System.out.println("Those selected:" + Arrays.toString(selected));

    }
}
