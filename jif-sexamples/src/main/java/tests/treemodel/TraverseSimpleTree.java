package tests.treemodel;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.awt.*;

public class TraverseSimpleTree extends JPanel {
  JTree tree;
  DefaultMutableTreeNode root, n1, n2, n3,n4,n5;
  public TraverseSimpleTree() {
    root = new DefaultMutableTreeNode("root", true);
    n1 = new DefaultMutableTreeNode("node 1", true);
    n2 = new DefaultMutableTreeNode("node 2" , true);
    n3 = new DefaultMutableTreeNode("node 3" , true);
    n4 = new DefaultMutableTreeNode("node 4" , true);
    n5 = new DefaultMutableTreeNode("node 5" , true);
    root.add(n1);
    n1.add(n2);
    root.add(n3);
    n3.add(n4);
    n4.add(n5);
    setLayout(new BorderLayout());
    tree = new JTree(root);
    add(new JScrollPane((JTree)tree),"Center");
    }
  public void traverse(JTree tree) {
    TreeModel model = tree.getModel();
    if (model != null) {
        Object root = model.getRoot();
        System.out.println(root.toString());
        walk(model,root);
        }
    else
       System.out.println("Tree is empty.");
    }

  protected void walk(TreeModel model, Object o){
    int  cc;
    cc = model.getChildCount(o);
    for( int i=0; i < cc; i++) {
      Object child = model.getChild(o, i );
      if (model.isLeaf(child))
        System.out.println(child.toString());
      else {
        System.out.print(child.toString()+"--");
        walk(model,child );
        }
     }
   }


  public Dimension getPreferredSize(){
    return new Dimension(200, 120);
    }

  public static void main(String s[]){
    MyJFrame frame = new MyJFrame("Traverse Tree");
    }
  }

class WindowCloser extends WindowAdapter {
  public void windowClosing(WindowEvent e) {
    Window win = e.getWindow();
    win.setVisible(false);
    System.exit(0);
    }
  }

class MyJFrame extends JFrame implements ActionListener {
  JButton b1, b2, b3;
  TraverseSimpleTree panel;
  MyJFrame(String s) {
    super(s);
    setForeground(Color.black);
    setBackground(Color.lightGray);
    panel = new TraverseSimpleTree();
    getContentPane().add(panel,"Center");

    b1 = new JButton("Traverse (check the console)");

    b1.addActionListener(this);
    getContentPane().add(b1,"West");
    setSize(300,300);
    setVisible(true);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowCloser());
    }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == b1) {
			panel.traverse(panel.tree);
		}
    }



}