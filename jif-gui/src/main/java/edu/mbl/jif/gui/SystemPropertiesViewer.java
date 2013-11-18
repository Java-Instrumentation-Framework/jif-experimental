package edu.mbl.jif.gui;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;

/** Displays system properties in a sorted, categorized tree heirarchy.
 *  Select a property node to display its corresponding value.
 *
 * @author Jane Griscti  jane@janeg.ca
 * @version 1.0  Dec-21-2001
 */
public class SystemPropertiesViewer
        extends JPanel
{
    private Properties props = System.getProperties();
    private JTree tree;
    private JPanel owner;

    /** Creates a JPanel containing a JTree. Nodes are categorized
     *  according to the first element of the property name. For example,
     *  all properties beginning with 'java' are categorized under
     *  the node 'java'.
     */
    public SystemPropertiesViewer () {
        super();
        owner = this;
        createSortedTree();
        JScrollPane jsp = new JScrollPane(tree);
        jsp.setPreferredSize(new Dimension(400, 300));
        jsp.setMinimumSize(getPreferredSize());
        add(jsp);
    }


    /** Builds the JTree. The properties are given to a TreeMap, which automatically
     *  sorts them. The keys from the TreeMap are used to create the JTree nodes.
     *  A StringTokenizer is used to extract the first portion of the property name
     *  to build category nodes.
     */
    private void createSortedTree () {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                "System Properties");
        Set keySet = new TreeMap(props).keySet();
        Iterator iter = keySet.iterator();
        DefaultMutableTreeNode key = null;
        DefaultMutableTreeNode category = null;
        String currentCategory = "";
        String newCategory = "";
        while (iter.hasNext()) {
            key = new DefaultMutableTreeNode(iter.next());
            StringTokenizer stok = new StringTokenizer((String) key.
                    getUserObject(), ".");
            newCategory = stok.nextToken();

            if (!currentCategory.equals(newCategory)) {
                currentCategory = newCategory;
                category = new DefaultMutableTreeNode(newCategory);
                top.add(category);
            }
            category.add(key);
        }
        tree = new JTree(top);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.
                                                  SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new TreeListener());
    }


    /** The JTree listener. When a property node is selected a JOptionPane
     *  is created to display the value associated with the property.
     */
    private class TreeListener
            implements TreeSelectionListener
    {
        public void valueChanged (TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            Object nodeInfo = node.getUserObject();
            if (node.isLeaf()) {
                String property = (String) nodeInfo;
                String value = props.getProperty(property);
                if (value.equals("")) {
                    value = "No associated value.";
                }
                JOptionPane.showMessageDialog(owner,
                                              value,
                                              property,
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
      try {
         SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
               JFrame f = new JFrame("title");
               f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
               SystemPropertiesViewer view = new SystemPropertiesViewer();
               f.getContentPane().add(view);
               f.pack();
               f.setVisible(true);
            }
         });
      } catch (InterruptedException e) {
      } catch (InvocationTargetException e) {
         // Error: Startup has failed badly.
         // We can not continue running our application.
         InternalError error = new InternalError();
         error.initCause(e);
         throw error;
      }
   }
    

}
