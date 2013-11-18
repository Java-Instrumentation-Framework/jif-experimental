
package tests.gui.tree;

/**
 *
 * @author GBH <imagejdev.org>
 */
import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class TreeArraySample {
  public static void main(String args[]) {
    JFrame frame = new JFrame("JTreeSample");

    Vector oneVector = new NamedVector("One", args);

    Vector twoVector = new NamedVector("Two", 
            new String[] { "Mercury", "Venus", "Mars" });

    Vector threeVector = new NamedVector("Three");

    threeVector.add(System.getProperties());
    threeVector.add(twoVector);

    Object rootNodes[] = { oneVector, twoVector, threeVector };
    Vector rootVector = new NamedVector("MenuBar", rootNodes);

    JTree tree = new JTree(rootVector);

    JScrollPane scrollPane = new JScrollPane(tree);
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    //    frame.getContentPane().add(tree, BorderLayout.CENTER);
    frame.setSize(300, 300);
    frame.setVisible(true);
  }
}

class NamedVector extends Vector {
  String name;

  public NamedVector(String name) {
    this.name = name;
  }

  public NamedVector(String name, Object elements[]) {
    this.name = name;
    for (int i = 0, n = elements.length; i < n; i++) {
      add(elements[i]);
    }
  }

  public String toString() {
    return "[" + name + "]";
  }
}