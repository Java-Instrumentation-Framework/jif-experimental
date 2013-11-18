package tests.gui.list;
// Example from http://www.crionics.com/products/opensource/faq/swing_ex/SwingExamples.html
//File:CheckListPanel.java
/* (swing1.1.1beta2) */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * @version 1.0 04/24/99
 */
public class CheckListPanel extends JPanel {

   JList list;

   public CheckListPanel() {
   }

   public CheckListPanel(Map<String, Boolean> selections) {
      this.setBackground(Color.white);
      list = new JList(createData(selections));
      // set "home" icon
//		Icon icon = MetalIconFactory.getFileChooserHomeFolderIcon();
//		((CheckableItem) list.getModel().getElementAt(1)).setIcon(icon);
      //list.setBackground(Color.red);
      list.setCellRenderer(new CheckListRenderer());
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.setBorder(new EmptyBorder(0, 4, 0, 0));
      list.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            int index = list.locationToIndex(e.getPoint());
            CheckableItem item = (CheckableItem) list.getModel().getElementAt(index);
            item.setSelected(!item.isSelected()); 
            Rectangle rect = list.getCellBounds(index, index);
            list.repaint(rect);
         }

      });
      // not scrollable
      //		JScrollPane sp = new JScrollPane(list);
      //		this.add(sp);
      this.add(list);
   }

   private CheckableItem[] createData(String[] strs) {
      int n = strs.length;
      CheckableItem[] items = new CheckableItem[n];
      for (int i = 0; i < n; i++) {
         items[i] = new CheckableItem(strs[i]);
      }
      return items;
   }

   // ===========
   Map<String, Boolean> selections = new LinkedHashMap<String, Boolean>();

   private CheckableItem[] createData(Map<String, Boolean> selections) {
      this.selections = selections;
      CheckableItem[] items = new CheckableItem[selections.entrySet().size()];
      int i = 0;
      for (Map.Entry<String, Boolean> entry : selections.entrySet()) {
         String key = entry.getKey();
         boolean selected = entry.getValue();
         items[i] = new CheckableItem(key, selected);
         i++;
      }
      return items;
   }

   public boolean isSelectedIndex(int j) {
      return list.getSelectionModel().isSelectedIndex(j);
   }

   public Map<String, Boolean> getSelectionMap() {
      for (int j = 0; j < list.getModel().getSize(); j++) {
         CheckableItem item = (CheckableItem) list.getModel().getElementAt(j);
         selections.put(item.toString(), item.isSelected());
      }
      return selections;
   }

   class CheckableItem {

      private String str;
      private boolean isSelected;

      public CheckableItem(String str) {
         this.str = str;
         isSelected = false;
      }

      public CheckableItem(String str, boolean selected) {
         this.str = str;
         isSelected = selected;
      }

      public void setSelected(boolean b) {
         isSelected = b;
      }

      public boolean isSelected() {
         return isSelected;
      }

      public String toString() {
         return str;
      }
   }

   class CheckListRenderer extends JCheckBox implements ListCellRenderer {

      public CheckListRenderer() {
         setBackground(UIManager.getColor("List.textBackground"));
         setForeground(UIManager.getColor("List.textForeground"));
      }

      public Component getListCellRendererComponent(JList list, Object value,
              int index, boolean isSelected, boolean hasFocus) {
         setEnabled(list.isEnabled());
         setSelected(((CheckableItem) value).isSelected());
         setFont(list.getFont());
         setText(value.toString());
         return this;
      }
   }


   public static void main(String args[]) {
//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch (Exception evt) {
//		}
            try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception ex) {
               }

      Map<String, Boolean> selections = new LinkedHashMap<String, Boolean>();
      selections.put("One", false);
      selections.put("Two", true);
      selections.put("Three", false);
      //
      final CheckListPanel panel = new CheckListPanel(selections);
      
      JFrame frame = new JFrame();
//      final JTextArea textArea = new JTextArea(3, 10);
//      JScrollPane textPanel = new JScrollPane(textArea);
      
      JButton printButton = new JButton("print");
      printButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            panel.getSelectionMap();
            // + list them...
         }
      });
      frame.add(panel, BorderLayout.CENTER);
      frame.add(printButton, BorderLayout.SOUTH);
      frame.pack();
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });
      frame.setVisible(true);
   }

}
