
/*
 * DndTwoLists.java
 *
 * Created on July 18, 2006, 11:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.dragDrop;

/** Moving items between two lists.
 *  This version uses drag and drop.
 *  based somewhat on the code from the Java Tutorial, 
 *  at: java.sun.com/docs/books/tutorial/index.html 
 *
 *  Some notes:
 *    The transferable used here is basically an integer that indicates
 *     the index of an item in a JList. The integer is actually transferred
 *     as a string - this allows us to use the 
 *     java.awt.datatransfer.StringSelection class to do most
 *     of the work.
 *     Roughly: when a drop event occurs, the list on which something is
 *      dropped grabs an item from the other list (using the integer
 *      dropped as an index to the item).
 *
 *  @author Dave Hollinger
 *
 */

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.io.File;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import javax.swing.ListSelectionModel;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JList;
import java.awt.Component;
import javax.swing.TransferHandler;
import javax.swing.JList;
import java.awt.datatransfer.StringSelection;
import javax.swing.JComponent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class DndTwoLists extends JApplet {

  public void init() {
        build(".");
  }

  public void build(String path) {
    JList jleft,jright;

        // set the size
        //        setSize(600,500);
        // establish what happens when the window is closed
        // (without this the program would keep running!)
        //        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // the lists on the left and right,
        // and some space in the middle. 
        // add an empty border to space things a little.
        setLayout(new BorderLayout());

        // Build the JList objects.
        String candies[] =  { "Snickers","Gum","Life Savers" };
        jleft = buildCandyList(candies);
        jright = buildCandyList(null);

        // create a transfer handler that can move an item
        // from one list to the other
        MyTransferHandler th = new MyTransferHandler(jleft,jright);

        // associate this transfer handler with each JList
        jleft.setTransferHandler(th);
        jright.setTransferHandler(th);

        // We want some space around the JLists, so we put
        // everything in a panel with a 10 pixel empty border.
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // each JList goes in a JScrollPane
        top.add(new JScrollPane(jleft),BorderLayout.WEST);
        top.add(new JScrollPane(jright),BorderLayout.EAST);

        // we need some space in the middle
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100,100));
        top.add(panel,BorderLayout.CENTER);

        // add the panel to the top level frame.
        add(top);
        setVisible(true);
  }

  /** create a JList 
   *  that can hold Candy objects (using a custom
   *  cell renderer and ListModel.)
   *  If a path is not null, use it to initialize the list.
   *  We need to turn on dragging support for the JList
   *  and to register a handler for drop events.
   */

  public JList buildCandyList(String candies[]) {

        CandyListModel fmodel;
        Candy clist[];
        if (candies != null) {
          clist = new Candy[candies.length];
          for (int i=0;i<candies.length;i++)
                clist[i] = new Candy(candies[i]);
          // model initially holds items from the array
          fmodel = new CandyListModel(clist);
        } else {
          // initially empty model
          fmodel = new CandyListModel();
        }
        JList jl = new JList(fmodel);
        // Custom cell renderer
        jl.setCellRenderer(new CandyCellRenderer());
        // add an empty border to leave some space around the edges
        // of the JList
        jl.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        jl.setPreferredSize(new Dimension(250,250));
        // only allow user to select one thing
    jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // enable dragging of items from the list
        jl.setDragEnabled(true);

        return(jl);
  }



/** This class is a ListModel that holds a list
 *        of Candy objects using an ArrayList<Candy>.
 *
 *  @author Dave H.
 */


class CandyListModel extends AbstractListModel {

  // We keep track of the files in a java.util.List
  List filelist; 

  /**
   * constructor is passed an array, and copies each
   * object reference into a List
   */
  CandyListModel(Candy[] f) {
        filelist = new ArrayList();
        for (int i=0;i<f.length;i++) 
          filelist.add(f[i]);
  }

  /** initializes to an empty list
   */
  CandyListModel() {
        filelist = new ArrayList();
  }
        
  public Object getElementAt(int index) {
        return(filelist.get(index));
  }

  /* Number of elements in the list */
  public int getSize() {
        return filelist.size();
  }

  /** Whenever an item is removed from a list,
   * we need to notify any listeners that some of 
   * the items have been removed. Here we call
   * the fireIntervalRemoved method inherited from
   * the AbstractListModel class.
   * Thanks AbstractListModel, this is very helpful.
   */
  public void remove(int index) {
        fireIntervalRemoved(this, index, index);
        filelist.remove(index);

  }
  /** add a new item (a Candy object) to the list, and
   * notify any listeners that this has happened
   */
  public void add(Candy f) {
        filelist.add(f);
        fireIntervalAdded(this, filelist.size()-1,filelist.size()-1);
  }

}


  static final Font ffont =  new Font("SansSerif",Font.BOLD,14);
  static final Color foreground = Color.BLACK;
  static final Color background = Color.WHITE;
  static final Color selectedbackground = new Color(170,200,230);



class CandyCellRenderer extends JLabel implements ListCellRenderer {


  /** This is the only method defined by ListCellRenderer.
   *  We just reconfigure the JLabel each time we're called.
   *  (we are a JLabel!).
   */
  public Component getListCellRendererComponent(
                                                                                                JList list,
                                                                                                Object value,            // value to display
                                                                                                int index,               // cell index
                                                                                                boolean isSelected,      // is the cell selected
                                                                                                boolean cellHasFocus)    // the list and the cell have the focus
  {
        Candy f = (Candy) value;
        setText(f.getName());
        setFont(ffont);
        if (isSelected) {
          setBackground(selectedbackground);
        }
        else {
          setBackground(background);
        }
        setForeground(foreground);
        setOpaque(true);
        return this;
  }
}

/** Candy object used for drag and drop applet
 *
 */

class Candy {
  String name;
  
  public Candy(String s) {
        name = s;
  }

  public String getName() {
        return(name);
  }
}



class MyTransferHandler extends TransferHandler {

  // Our transfer handler needs to know about the
  // two JLists !
  JList left,right;

  public MyTransferHandler(JList l, JList r) {
        super();
        left=l;
        right=r;
  }

  // This is the method called when a drop occurs.
  // The parameter c is where something is being dropped
  //  (one of our two JLists).
  // The parameter t is the thing being dropped
  public boolean importData(JComponent c, Transferable t) {
        JList jl = (JList) c;

        try {
          int sourceIndex = Integer.parseInt((String)t.getTransferData(DataFlavor.stringFlavor));

          // figure out which is the source and which is the dest.
          CandyListModel source,dest;

          if (jl == left) { 
                source=(CandyListModel) right.getModel(); 
                dest =(CandyListModel) left.getModel(); 
           } else { 
                source = (CandyListModel)left.getModel(); 
                dest = (CandyListModel)right.getModel(); 
          }
          // grab the Candy object from the source CandyListModel
          Candy f = (Candy) source.getElementAt(sourceIndex);
          // add it to the other model.
          dest.add(f);
          // and remove from the source
          source.remove(sourceIndex);
        } catch (Exception e) {
          System.out.println("Import failed!");
          return(false);
        }
        return(true);
  }

  /** creates a StringSelection transferrable. The string is 
          just an integer converted to string (if we actually
          transferred an int, we would need to write our own
          Transferrable, this way we can just use StringSelection).

          The JComponent passed in is the JList from which something
          is being dragged. We look up the selected index, convert 
          this to a string and wrap in a StringSelection transferrable.
  */

  protected Transferable createTransferable(JComponent c) {
        JList jl = (JList) c;
        // we want the currently selected index
        Integer index = new Integer(jl.getSelectedIndex());
        // and we transfer it as a string
        Transferable t = new StringSelection(index.toString());
        return t;
  }

  // We have to specify that things can be dragged
  // MOVE or 
  public int getSourceActions(JComponent c) {
        return MOVE;
  }


  // This is called before a drop, to make sure that we 
  // can handle the incoming data type. The flavors array
  // is a list of the data flavors the incoming data is available in.
  // All we want is a Unicode String, so we look for that and return
  // true if we find it.
  // This is more significant if we were supporting drag and drop
  // from non-java programs....

  public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (int i=0;i<flavors.length;i++) {
          if (flavors[i].getHumanPresentableName().equals("Unicode String")) {
                return(true);
          }
        }
        return(false);
  }
}




}


