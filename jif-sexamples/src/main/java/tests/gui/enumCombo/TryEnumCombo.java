/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.gui.enumCombo;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author GBH
 */
public class TryEnumCombo {
}
//===================================================================
// ComboBox with Enum
/** Define enum class that will popuplate your combo box */
enum E_ComboBoxEnum {

    Item1("Item 1"),
    Item2("Item 2"),
    Item3("Item 3");
    private final String _displayName;

    /** constructor */
    E_ComboBoxEnum(final String displayName)
      {
        _displayName = displayName;
      }

    /** overrides method toString() in java.lang.Enum class */
    public String toString()
      {
        return _displayName;
      }

} // end enum E_ComboBoxEnum

/** whatever class contains the JComboBox object, could be JFrame, JDialog, etc */
class ComboBoxGUI extends javax.swing.JDialog {

    private javax.swing.JComboBox _myComboBox;

    public void createComboBox()
      {

        _myComboBox = new javax.swing.JComboBox();
        /** model is set to be a new instance of DefaultComboBoxModel. this model
        is initialized with an array of all possible values of E_ExpirationDateIntervals */
        Object[] E_ExpirationDateIntervals = {};
        _myComboBox.setModel(new DefaultComboBoxModel(E_ExpirationDateIntervals));
      }

    /** the combo box will now display the Strings defined in the E_ComboBoxEnum class, in the order that they were defined. */
    /** _myComboBox.getSelectedItem() will return an E_ComboBoxEnum object  (well technically a generic object that can be cast into an E_ComboBoxEnum object) */
} // end class ComboBoxGUI
