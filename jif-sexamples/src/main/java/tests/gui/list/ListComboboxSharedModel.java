/*
 * ListComboboxSharedModel.java
 *
 * Created on October 15, 2007, 2:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListComboboxSharedModel {

    final String partsList[][] = {{"Item 1", "10", "99c"},
            {"Item 2", "12", "$18.99"}, {"Item 3", "1", "$10.00"}};

    class PartsListModel extends DefaultListModel implements ComboBoxModel {

        Object currentValue;

        public PartsListModel() {
            for (int i = 0,  n = partsList.length; i < n; i++) {
                addElement(partsList[i]);
            }
        }

        // ComboBoxModel methods

        public Object getSelectedItem() {
            return currentValue;
        }

        public void setSelectedItem(Object anObject) {
            currentValue = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    class MyLabelRenderer extends JLabel implements ListCellRenderer {

        public MyLabelRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                String values[] = (String[]) value;
                String setting = values[0] + " / " + values[1] + " / " + values[2];
                setText(setting);
            }
            setBackground(isSelected ? Color.BLUE : Color.WHITE);
            setForeground(isSelected ? Color.WHITE : Color.BLUE);
            return this;
        }
    }

    public ListComboboxSharedModel() {
        JFrame f = new JFrame();
        final PartsListModel pcm = new PartsListModel();
        ListCellRenderer lcr = new MyLabelRenderer();
        final JList jl = new JList(pcm);
        jl.setCellRenderer(lcr);
        ListSelectionModel lsm = jl.getSelectionModel();
        //lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    //lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lsm.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jl.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            int[] selectedIx = jl.getSelectedIndices();
                            for (int i = 0; i < selectedIx.length; i++) {
                                int j = selectedIx[i];
                                String element[] = (String[]) pcm.getElementAt(j);
                                System.out.println(element[0] + " : " + element[1] + " : " + element[2]);
                            }
                            Object[] selected = jl.getSelectedValues();
                        }
                    }
                });
        JScrollPane jsp = new JScrollPane(jl);
        JComboBox jc = new JComboBox(pcm);
        jc.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JComboBox cb = (JComboBox) e.getSource();
                        Object selected = cb.getSelectedItem();
                        System.out.println("ComboSelected: " + selected);
                    }
                });
        jc.setRenderer(lcr);
        JButton jb = new JButton("Add Merchandise");
        jb.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        pcm.addElement(partsList[(int) (Math.random() * partsList.length)]);
                    }
                });
        Container c = f.getContentPane();
        c.add(jsp, BorderLayout.NORTH);
        c.add(jc, BorderLayout.CENTER);
        c.add(jb, BorderLayout.SOUTH);
        f.setSize(250, 250);
        f.show();
    }

    public static void main(String args[]) {
        new ListComboboxSharedModel();
    }
}