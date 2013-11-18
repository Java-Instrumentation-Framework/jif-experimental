package edu.mbl.jif.camera;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;

import javax.swing.JFrame;


/** Displays system properties in a sorted, categorized tree heirarchy.
 *  Select a property node to display its corresponding value.
 *
 * @author Jane Griscti  jane@janeg.ca
 * @version 1.0  Dec-21-2001
 */
public class PropertiesViewer
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
   public PropertiesViewer () {
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
         StringTokenizer stok = new StringTokenizer((String) key.getUserObject(),
               ".");
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



   public static void main (String[] args) {

      JFrame frame = new JFrame("Properties Viewer Demo");
      PropertiesViewer pv = new PropertiesViewer();
      frame.getContentPane().add(pv);
      frame.pack();
      frame.setVisible(true);

   }
}
