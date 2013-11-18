
package tests.gui.tree;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

class TreeDemo {

  JLabel jlab;

  TreeDemo() {
    // Create a new JFrame container.
    JFrame jfrm = new JFrame("JTree Demo");

    // Specify FlowLayout for the layout manager.
    jfrm.setLayout(new FlowLayout());

    // Give the frame an initial size.
    jfrm.setSize(260, 240);

    // Terminate the program when the user closes the application.
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a label that will dislay the tree selection
    // and set its size.
    jlab = new JLabel();
    jlab.setPreferredSize(new Dimension(230, 50));

    // Create a tree that shows the relationship of several
    // Swing classes.

    // First, create the root node of the tree.
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("JComponent");

    // Next, create two subtrees.  One begins with
    // AbstractrButton and the other with JTextComponent.

    // Create the root of the AbstractButton subtree.
    DefaultMutableTreeNode absBtnNode =
                      new DefaultMutableTreeNode("AbstractButton");
    root.add(absBtnNode); // add the AbstractButton node to the tree.

    // The AbstractButton subtree has two nodes:
    // JButton and JToggleButton. Under JToggleButton
    // are JCheckBox and JRadioButton. These nodes
    // are created by the following statements.

    // Create the nodes under AbstractButton.
    DefaultMutableTreeNode jbtnNode =
                      new DefaultMutableTreeNode("JButton");
    absBtnNode.add(jbtnNode); // add JButton to AbstractButton

    DefaultMutableTreeNode jtoggleNode =
                      new DefaultMutableTreeNode("JToggleButton");
    absBtnNode.add(jtoggleNode); // add JToggleButton to AbstractButton

    // Add a subtree under JToggleButton
    jtoggleNode.add(new DefaultMutableTreeNode("JCheckBox"));
    jtoggleNode.add(new DefaultMutableTreeNode("JRadioButton"));

    // Now, create a JTextComponent subtree.
    DefaultMutableTreeNode jtxtCompNode =
                      new DefaultMutableTreeNode("JTextComponent");
    root.add(jtxtCompNode); // add JTextComponent to the root

    // Populate the JTextComponent subtree.
    DefaultMutableTreeNode jtxtFieldNode =
                      new DefaultMutableTreeNode("JTextField");
    jtxtCompNode.add(jtxtFieldNode);
    jtxtCompNode.add(new DefaultMutableTreeNode("JTextArea"));
    jtxtCompNode.add(new DefaultMutableTreeNode("JEditorPane"));

    // Create a subtree under JTextField.
    jtxtFieldNode.add(new DefaultMutableTreeNode("JFormattedTextField"));
    jtxtFieldNode.add(new DefaultMutableTreeNode("JPasswordField"));

    // Now, create a JTree that uses the structure
    // defined by the preceding statements.
    JTree jtree = new JTree(root);

    // Allow the tree to be edited so that model
    // events can be generated.
    jtree.setEditable(true);

    // Set the tree selection mode to single-selection.
    TreeSelectionModel tsm = jtree.getSelectionModel();
    tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    // Wrap the tree in a scroll pane.
    JScrollPane jscrlp = new JScrollPane(jtree);

    // Set the preferred size of the scroll pane.
    jscrlp.setPreferredSize(new Dimension(160, 140));

    // Listen for tree expansion events.
    jtree.addTreeExpansionListener(new TreeExpansionListener() {
      public void treeExpanded(TreeExpansionEvent tee) {
        // Get the path to the expansion point.
        TreePath tp = tee.getPath();

        // Display the node
        jlab.setText("Expansion: " +
                     tp.getLastPathComponent());
      }

      public void treeCollapsed (TreeExpansionEvent tee) {
        // Get the path to the expansion point.
        TreePath tp = tee.getPath();

        // Display the node
        jlab.setText("Collapse: " +
                     tp.getLastPathComponent());
      }
    });

    // Listen for tree selection events.
    jtree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent tse) {
        // Get the path to the selection.
        TreePath tp = tse.getPath();

        // Display the selected node.
        jlab.setText("Selection event: " +
                     tp.getLastPathComponent());
      }
    });

    // Listen for tree model events. Notice that the
    // listener is registered with the tree model.
    jtree.getModel().addTreeModelListener(new TreeModelListener() {
      public void treeNodesChanged(TreeModelEvent tme) {

        // Get the path to the parent of the node that changed.
        TreePath tp = tme.getTreePath();

        // Get the children of the parent of the node that changed.
        Object[] children = tme.getChildren();

        DefaultMutableTreeNode changedNode;

        if(children != null)
          changedNode = (DefaultMutableTreeNode) children[0];
        else
          changedNode = (DefaultMutableTreeNode) tp.getLastPathComponent();


        // Display the path
        jlab.setText("<html>Model change path: " + tp + "<br>" +
                     "New data: " +  changedNode.getUserObject());

      }

      // Empty implementations of the remaing TreeModelEvent
      // methods. Implement these if your application
      // needs to handle these actions.
      public void treeNodesInserted(TreeModelEvent tse) {}
      public void treeNodesRemoved(TreeModelEvent tse) {}
      public void treeStructureChanged(TreeModelEvent tse) {}
    });

    // Add the tree and label to the content pane.
    jfrm.add(jscrlp);
    jfrm.add(jlab);

    // Display the frame.
    jfrm.setVisible(true);
  }

  public static void main(String args[]) {
    // Create the frame on the event dispatching thread.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new TreeDemo();
      }
    });
  }
}