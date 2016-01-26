package tests.gui.tree;

// from https://forums.oracle.com/thread/1558600
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class HighlightTreeNodeTest {

   private final JTree tree = new JTree();
   private final JTextField field = new JTextField("foo");
   private final JButton button = new JButton();
   private final MyTreeCellRenderer renderer
           = new MyTreeCellRenderer(tree.getCellRenderer());

   public JComponent makeUI() {
      field.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            fireDocumentChangeEvent();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            fireDocumentChangeEvent();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
         }
      });
      tree.setCellRenderer(renderer);
      renderer.q = field.getText();
      fireDocumentChangeEvent();
      JPanel p = new JPanel(new BorderLayout(5, 5));
      p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      p.add(field, BorderLayout.NORTH);
      p.add(new JScrollPane(tree));
      return p;
   }

   private void fireDocumentChangeEvent() {
      String q = field.getText();
      renderer.q = q;
      TreePath root = tree.getPathForRow(0);
      collapseAll(tree, root);
      if (!q.isEmpty()) {
         searchTree(tree, root, q);
      }
      tree.repaint();
   }

   private static void searchTree(JTree tree, TreePath path, String q) {
      TreeNode node = (TreeNode) path.getLastPathComponent();
      if (node == null) {
         return;
      }
      if (node.toString().startsWith(q)) {
         tree.expandPath(path.getParentPath());
      }
      if (!node.isLeaf() && node.getChildCount() >= 0) {
         Enumeration e = node.children();
         while (e.hasMoreElements()) {
            searchTree(tree, path.pathByAddingChild(e.nextElement()), q);
         }
      }
   }

   private static void collapseAll(JTree tree, TreePath parent) {
      TreeNode node = (TreeNode) parent.getLastPathComponent();
      if (!node.isLeaf() && node.getChildCount() >= 0) {
         Enumeration e = node.children();
         while (e.hasMoreElements()) {
            TreeNode n = (TreeNode) e.nextElement();
            TreePath path = parent.pathByAddingChild(n);
            collapseAll(tree, path);
         }
      }
      tree.collapsePath(parent);
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            createAndShowGUI();
         }
      });
   }

   public static void createAndShowGUI() {
      JFrame f = new JFrame();
      f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      f.getContentPane().add(new HighlightTreeNodeTest().makeUI());
      f.setSize(320, 240);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
   }
}

class MyTreeCellRenderer extends DefaultTreeCellRenderer {

   private final TreeCellRenderer renderer;
   public String q;

   public MyTreeCellRenderer(TreeCellRenderer renderer) {
      this.renderer = renderer;
   }

   @Override
   public Component getTreeCellRendererComponent(
           JTree tree, Object value, boolean isSelected, boolean expanded,
           boolean leaf, int row, boolean hasFocus) {
      JComponent c = (JComponent) renderer.getTreeCellRendererComponent(
              tree, value, isSelected, expanded, leaf, row, hasFocus);
      if (isSelected) {
         c.setOpaque(false);
         c.setForeground(getTextSelectionColor());
      } else {
         c.setOpaque(true);
         if (q != null && !q.isEmpty() && value.toString().startsWith(q)) {
            c.setForeground(getTextNonSelectionColor());
            c.setBackground(Color.YELLOW);
         } else {
            c.setForeground(getTextNonSelectionColor());
            c.setBackground(getBackgroundNonSelectionColor());
         }
      }
      return c;
   }
}
