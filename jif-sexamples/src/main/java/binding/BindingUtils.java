/*
 * BindingUtils.java
 * Created on August 13, 2006, 9:43 AM
 */

package binding;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 * Supports binding of lists / comboBoxes / etc.
 * @author GBH
 */
public class BindingUtils {
    
    public static ListCellRenderer createBinningListCellRenderer() {
        return new BinningListCellRenderer();
    }
   
    // Used to renders Modes in JLists and JComboBoxes. If the combobox
    // selection is null, an empty text <code>""</code> is rendered.
    
    private static class BinningListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
           // System.out.println("value="+value);
            
            Integer v = (Integer) value;
            setText(v == null ? "" : (" " + v.toString()));
            return component;
        }
    }

    
    public static ListCellRenderer createSpeedListCellRenderer() {
        return new SpeedListCellRenderer();
    }
   
    // Used to renders Modes in JLists and JComboBoxes. If the combobox
    // selection is null, an empty text <code>""</code> is rendered.
    
    private static class SpeedListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
           // System.out.println("value="+value);
            
            Double v = (Double) value;
            setText(v == null ? "" : (" " + v.toString()));
            return component;
        }
    }
    
    // Debug Listener *********************************************************
    
    
      public static PropertyChangeListener createDebugPropertyChangeListener() {
        PropertyChangeListener listener = new DebugPropertyChangeListener();
        debugListeners.add(listener);
        return listener;
    }
    
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
    
}
