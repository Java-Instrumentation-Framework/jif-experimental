/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.gui.combo;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class ComboBoxToolTipExample extends JFrame {

   String[] comboboxitems = {"ja", "j2", "Js"};
   String[] tooltip = {"Java bdbdbd bbbbbbbs bsbs b bbbbbbbbbbbbbbss bbb  b b b b bbbbbbbbb", 
      "J2EE bdbdbd bbbbbbbs bsbs b bbbbbbbbbbbbbbss bbb  b b b b bbbbbbbbb",
      "Java Script bdbdbd bbbbbbbs bsbs b bbbbbbbbbbbbbbss bbb  b b b b bbbbbbbbb"};

   public ComboBoxToolTipExample() {
       super("ToolTip Example for Combo Box");

       JComboBox combobox = new JComboBox(comboboxitems);
       combobox.setRenderer(new ComboBoxRenderer());

       getContentPane().setLayout(new FlowLayout());
       getContentPane().add(combobox);
   }

   class ComboBoxRenderer extends BasicComboBoxRenderer {

       @Override
       public Component getListCellRendererComponent(JList list, Object value,
               int index, boolean isSelected, boolean cellHasFocus) {
           if (isSelected) {
               setBackground(list.getSelectionBackground());
               setForeground(list.getSelectionForeground());
               if (-1 < index) {
                   list.setToolTipText(tooltip[index]);
               }
           } else {
               setBackground(list.getBackground());
               setForeground(list.getForeground());
           }
           setFont(list.getFont());
           setText((value == null) ? "" : value.toString());
           return this;
       }
   }

   public static void main(String args[]) {
       try {
           UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
       } catch (Exception evt) {
           evt.printStackTrace();
       }

       ComboBoxToolTipExample fram = new ComboBoxToolTipExample();
       fram.addWindowListener(new WindowAdapter() {

           public void windowClosing(WindowEvent e) {
               System.exit(0);
           }
       });
       fram.setSize(200, 140);
       fram.setVisible(true);
   }
}