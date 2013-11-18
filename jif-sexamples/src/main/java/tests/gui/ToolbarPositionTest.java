/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.gui;

/**
 *
 * @author GBH
 */
import javax.swing.*;
import java.util.prefs.*;
import java.awt.*;
import java.awt.event.*;


/**
* A Skeleton class for an application featuring a JToolBar, that can
* persistently save it's layout. This class is ready to use. Only the
* methods buildMainContentArea(), buildToolBar() and buildMenuBar()
* have to be replaced with real code.
*/
public class ToolbarPositionTest extends JFrame implements WindowListener {
 
  /**
   * Persistent preference storage
   */
  private Preferences cfg;
 
  /**
   * The default window geometry
   */
  public static final Rectangle GEOMETRY = new Rectangle(0,0,640,480);
 
  /**
   * The default position of the toolbar
   */
  public static final String LOCATION = BorderLayout.PAGE_END;
 
  /**
   * the default orientation of the toolbar
   */
  public static int ORIENTATION = JToolBar.HORIZONTAL;
 
  /**
   * Constructor
   * @param title the title of the window
   */
  public ToolbarPositionTest(String title) {
    super(title);
   
    // Load preferences
    cfg = Preferences.userNodeForPackage(ToolbarPositionTest.class);
   
    // Build window content - delegate to keep the constructor uncluttered.
    setJMenuBar(buildMenuBar());
    setContentPane(buildContent());
   
    // Restore window geometry
    Rectangle bounds = new Rectangle();
    bounds.x = cfg.getInt("window.x",GEOMETRY.x);
    bounds.y = cfg.getInt("window.y",GEOMETRY.y);
    bounds.width = cfg.getInt("window.width",GEOMETRY.width);
    bounds.height = cfg.getInt("window.height",GEOMETRY.height);
    setBounds(bounds); // Do not use pack()!
   
    // For saving settings upon window closing
    addWindowListener(this);
  }
 
  /**
   * Build the content of the window.
   * @return A panel, assembled from the result of buildToolBar()
   * and buildMainContentArea()
   */
  public JPanel buildContent() {
    // The preferred way to build a window with a toolbar is to use the Borderlayout
    // for the toplevel container and nest the real content in the center position.
    // The four sides are then were the toolbar might be dragged to.
    JPanel content = new JPanel();
    content.setLayout(new BorderLayout());
   
    JToolBar toolbar = buildToolBar();
   
    // Restore the toolbar where the user left it. Don't trust the data in the
    // persistent storage, however. It can easily be tempered with.
    try {
      String loc = cfg.get("toolbar.location",LOCATION);
      int orientation = cfg.getInt("toolbar.orientation",ORIENTATION);
      toolbar.setOrientation(orientation);
      content.add(toolbar,loc);
    }
    catch (IllegalArgumentException exp) {
      // The data in the persistent storage is corrupt! Fall back to
      // safe defaults
      toolbar.setOrientation(ORIENTATION);
      content.add(toolbar,LOCATION);
    }
  
    content.add(buildMainContentArea(),BorderLayout.CENTER);
    return content;
  }
 

  /**
   * Save settings and quit.
   */
  public void quit() {
    Rectangle bounds = getBounds();
    cfg.putInt("window.x",bounds.x);
    cfg.putInt("window.y",bounds.y);
    cfg.putInt("window.width",bounds.width); 
    cfg.putInt("window.height",bounds.height);
   
    // Figure out, which component is the toolbar
    Container content = getContentPane();
    BorderLayout layout = (BorderLayout)content.getLayout();
    Component comps[] = content.getComponents();
    for (Component c:comps) {
      if (c instanceof JToolBar) {
        cfg.put("toolbar.location",layout.getConstraints(c).toString());
        cfg.putInt("toolbar.orientation",((JToolBar)c).getOrientation());
      }
    } 
    System.exit(0);
  }
 
   // Interface implemented
  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowDeactivated(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}
  public void windowClosing(WindowEvent e) {  
    quit();
  }
 
 
  /*
   * The methods below should be replaced by real code.
   */

  /**
   * Build the main content area
   * @return A panel displaying whatever the application is to display besides
   * menubar and toolbar.
   */
  public JPanel buildMainContentArea() {
    // you might want to do better then this.
    JPanel ret = new JPanel();
    ret.setLayout(new GridLayout(0,1)); // Easiest way to expand the panel
    ret.add(new JScrollPane(new JTextArea("Hello world")));
    return ret;
  }
 
  /**
   * Build the menubar
   * @return the menubar of the application
   */
  public JMenuBar buildMenuBar() {
    // you might want to do better then this
    JMenuBar bar = new JMenuBar();
    JMenu hello = new JMenu("Hello");
    hello.add(new JMenuItem("World"));
    bar.add(hello);
    return bar;
  }
 
  /**
   * Build the toolbar
   * @return the toolbar of the application
   */
  public JToolBar buildToolBar() {
    // you might want to do better then this
    JToolBar ret = new JToolBar();
    ret.add(new JButton("Hello"));
    ret.add(new JButton("World"));
    return ret;
  }

 
  /**
   * For demonstration purposes.
   */
  public static void main(String[] args) {
    new ToolbarPositionTest("Hello world").setVisible(true);
  }
}