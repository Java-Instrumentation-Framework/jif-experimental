package binding;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
//import edu.mbl.jif.acq.mode.ImagingMode;


/**
 * Consists only of static methods that return instances
 * reused in multiple examples of the JGoodies Binding tutorial.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.12 $
 */
public final class TutorialUtils {


    private TutorialUtils() {
        // Suppresses default constructor, ensuring non-instantiability.
    }


    /**
     * Creates and returns a renderer for Modes in a list.
     *
     * @return a renderer for Modes in lists.
     */
    public static ListCellRenderer createModeListCellRenderer() {
        return new ModeListCellRenderer();
    }


    /**
     * Creates and returns a TableModel for Modes with columns
     * for the title, artist, classical and composer.
     *
     * @param listModel   the ListModel of Modes to display in the table
     * @return a TableModel on the list of Modes
     */
    public static TableModel createModeTableModel(ListModel listModel) {
        return new ModeTableModel(listModel);
    }


    public static PropertyChangeListener createDebugPropertyChangeListener() {
        PropertyChangeListener listener = new DebugPropertyChangeListener();
        debugListeners.add(listener);
        return listener;
    }


    public static Action getCloseAction() {
        return new CloseAction();
    }


    /**
     * Locates the given component on the screen's center.
     *
     * @param component   the component to be centered
     */
    public static void locateOnScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (screenSize.height - paneSize.height) / 2);
    }


    // Renderer ***************************************************************

    /**
     * Used to renders Modes in JLists and JComboBoxes. If the combobox
     * selection is null, an empty text <code>""</code> is rendered.
     */
    private static class ModeListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);

            String mode = (String) value;
            //setText(Mode == null ? "" : (" " + Mode.getModeStr()));
            return component;
        }
    }


    // TableModel *************************************************************

    /**
     * Describes how to present an Mode in a JTable.
     */
    private static final class ModeTableModel extends AbstractTableAdapter {

        private static final String[] COLUMNS = {"Mode", "Exposure", "View", "Stripe"};

        private ModeTableModel(ListModel listModel) {
            super(listModel, COLUMNS);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
//            ImagingMode Mode = (ImagingMode) getRow(rowIndex);
//            switch (columnIndex) {
//                case 0 : return Mode.getModeStr();
//                case 1 : return Mode.getExposure();
//                case 2 : return Boolean.valueOf(Mode.isViewImmediately());
//                case 3 : return Boolean.valueOf(Mode.isAddDataStripe());
//                default :
//                    throw new IllegalStateException("Unknown column");
//            }
           return null;
        }

    }


    // Debug Listener *********************************************************

    /**
     * Used to hold debug listeners, so they won't be removed by
     * the garbage collector, even if registered by a listener list
     * that is based on weak references.
     *
     * @see #createDebugPropertyChangeListener()
     */
    private static List debugListeners = new LinkedList();


    /**
     * Writes the source, property name, old/new value to the system console.
     */
    private static class DebugPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println();
            System.out.println("The source: " + evt.getSource());
            System.out.println(
                    "changed '" + evt.getPropertyName()
                  + "' from '" + evt.getOldValue()
                  + "' to '" + evt.getNewValue() + "'.");
        }
    }


    // Actions ****************************************************************

    /**
     * An Action that exists the System.
     */
    private static final class CloseAction extends AbstractAction {

        private CloseAction() {
            super("Close");
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }


}
