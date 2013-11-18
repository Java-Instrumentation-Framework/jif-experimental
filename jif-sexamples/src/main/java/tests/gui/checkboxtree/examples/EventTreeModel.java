/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.gui.checkboxtree.examples;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author GBH
 */
public class EventTreeModel {
	

    protected static TreeModel getEventTreeModel() {
        DefaultMutableTreeNode      root = new DefaultMutableTreeNode("ImageJEvent");
	DefaultMutableTreeNode      parent;

	parent = new DefaultMutableTreeNode("ObjectEvent");
	root.add(parent);
	parent.add(new DefaultMutableTreeNode("ObjectCreatedEvent"));
	parent.add(new DefaultMutableTreeNode("ObjectDeletedEvent"));
 	parent.add(new DefaultMutableTreeNode("ObjectModifiedEvent"));
	parent.add(new DefaultMutableTreeNode("ObjectsAddedEvent"));
	parent.add(new DefaultMutableTreeNode("ObjectsChangedEvent"));
	parent.add(new DefaultMutableTreeNode("ObjectsRemovedEvent"));


	parent = new DefaultMutableTreeNode("sports");
	root.add(parent);
	parent.add(new DefaultMutableTreeNode("basketball"));
	parent.add(new DefaultMutableTreeNode("soccer"));
	parent.add(new DefaultMutableTreeNode("football"));
	parent.add(new DefaultMutableTreeNode("hockey"));

	parent = new DefaultMutableTreeNode("food");
	root.add(parent);
	parent.add(new DefaultMutableTreeNode("hot dogs"));
	parent.add(new DefaultMutableTreeNode("pizza"));
	parent.add(new DefaultMutableTreeNode("ravioli"));
	parent.add(new DefaultMutableTreeNode("bananas"));
        return new DefaultTreeModel(root);
    }
}