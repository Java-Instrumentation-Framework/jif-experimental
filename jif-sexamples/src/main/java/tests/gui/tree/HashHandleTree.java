
package tests.gui.tree;

import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
public class HashHandleTree {
  public static void main(String[] args) {
    JFrame f = new JFrame("Tree Created From a Hashtable");
    Hashtable h = new Hashtable();
    h.put("One", "Number one");
    h.put("Two", "Number two");
    h.put("Three", "Number three");
    h.put("Four", "Number four");
    h.put("Five", "Number five");
    JTree t = new JTree(h);
    t.putClientProperty("JTree.lineStyle", "Angled");

    t.setShowsRootHandles(false);
    f.getContentPane().add(t);
    f.pack();
    f.setVisible(true);
  }
}