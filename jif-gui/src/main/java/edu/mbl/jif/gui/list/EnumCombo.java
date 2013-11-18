package edu.mbl.jif.gui.list;

import edu.mbl.jif.gui.test.FrameForTest;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;

/**
 * ComboBox with Enumeration
 * @author GBH
 */
public class EnumCombo<E extends Enum<E>> extends JComboBox {
  private final Class<E> enumClass;

  public EnumCombo(Class<E> enumClass) {
    this.enumClass = enumClass;

    for (E e : enumClass.getEnumConstants()) {
      addItem(e);
    }
  }

  public E getSelectedItem() {
    return enumClass.cast(super.getSelectedItem());
  }

  public static void main(String[] args) {
    final EnumCombo<Status> statusCombo = new EnumCombo<Status>(Status.class);
    statusCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ie) {
        if (ie.getStateChange() != ItemEvent.SELECTED) return;
        Status key = statusCombo.getSelectedItem();
        System.out.println("status: " + key.toString());
      }});
    FrameForTest f = new FrameForTest(statusCombo);
    f.pack();
    f.setVisible(true);

  }
}

enum Status {
  ON,
  OFF,
  UNKNOWN
}