package edu.mbl.jif.camacq;

import edu.mbl.jif.camera.camacq.*;
import javax.swing.*;
import javax.swing.table.*;
import com.vlsolutions.swing.docking.*;
import com.vlsolutions.swing.docking.event.*;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import com.vlsolutions.swing.toolbars.VLToolBar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.io.*;
import java.awt.event.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import edu.mbl.jif.gui.action.ActionContainerFactory;
import edu.mbl.jif.gui.action.ActionManager;

/** Application Frame incorporating VLDocking
 * 
 * GBH, April 2008
 * */
public class AppFrameVLDockTester
    extends JFrame
    implements ApplicationFrame {
    // our 4 dockable components
    MyTextEditor editorPanel = new MyTextEditor();
    MyTextEditor editorPanel1 = new MyTextEditor();
    MyTree treePanel = new MyTree();
    MyGridOfButtons buttonGrid = new MyGridOfButtons();
    MyJTable tablePanel = new MyJTable();    
    
    // the desktop (which will contain dockables)
    DockingDesktop desk = new DockingDesktop();
    ToolBarContainer container;

    /** Default and only frame constructor */
    public AppFrameVLDockTester(String iconPath)
      {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage((new ImageIcon(getClass().getResource(iconPath))).getImage());
        // insert our desktop as the only one component of the frame
        container = ToolBarContainer.createDefaultContainer(true, true, true, true);  // (top, left, bottom, right)

        getContentPane().add(container, BorderLayout.CENTER);
        container.add(desk, BorderLayout.CENTER);
        //System.out.println("DefaultDir: " + com.myjavatools.lib.Files.getcwd());
        
        // set the initial dockable
        desk.addDockable(editorPanel);
        // and layout the others
        //desk.split(editorPanel, tablePanel, DockingConstants.SPLIT_BOTTOM);
       // desk.split(editorPanel, buttonGrid, DockingConstants.SPLIT_RIGHT);
//        desk.createTab(tablePanel, treePanel, 1);
//        desk.split(tablePanel, treePanel, DockingConstants.SPLIT_BOTTOM);
//        desk.addHiddenDockable(treePanel, RelativeDockablePosition.LEFT);
        //desk.addHiddenDockable(tablePanel, RelativeDockablePosition.RIGHT);

        //desk.addHiddenDockable(buttonGrid, RelativeDockablePosition.LEFT);
        
        //desk.addToTabbedGroup(tablePanel, treePanel);
        
       // desk.addToTabbedGroup(treePanel, treePanel)
//        // cannot reload before a workspace is saved
//        loadWorkspaceAction.setEnabled(true);

        // Grouping
       
//        DockGroup documentsGroup = new DockGroup("documents");
//        DockGroup helperGroup = new DockGroup("helper dockables");
//        // set the documentsGroup to all "documents" DockKeys 
//        editorPanel.getDockKey().setDockGroup(documentsGroup);
//        tablePanel.getDockKey().setDockGroup(helperGroup);
//        buttonGrid.getDockKey().setDockGroup(helperGroup);
//        DockKey documentKey1 = new DockKey("document_1", "Document 1");
//        DockKey documentKey2 = new DockKey("document_2", "Document 2");
//        documentKey1.setDockGroup(documentGroup);
//        documentKey2.setDockGroup(documentGroup);
        // set the helperGroup to all other DockKeys
//        DockKey propertyEditorKey = ...
//        propertyEditorKey.setDockGroup(helperGroup);
//        DockKey historyKey = ...
//        historyKey.setDockGroup(helperGroup);
  //      ...
        

        // Nested,Compound dockable container
        //DockingDesktop desk = ...
        Dockable tab1 = new MyTree();
        Dockable tab2 = new MyGridOfButtons();
       // CompoundDockable compound = new CompoundDockable(new DockKey("Nested"));
        //Dockable nested1 = ...;
        //Dockable nested2 = ...;
        desk.addDockable(tab1);
        desk.createTab(tab1, tab2, 1);
        desk.createTab(tab1, tablePanel, 1);
        
        //desk.createTab(tab1, compound, 2); // compound is added as a tab
        //desk.addDockable(compound, nested1); // now we insert nested1
        //desk.split(nested1, nested2, DockingConstants.SPLIT_RIGHT); // and we split it
        

        // create an anchor manager for a given context and a top level container (here : the desktop)
        /*
        AnchorManager am = new AnchorManager(desk.getContext(), desk);
        // and specify constraints (here : top + right)
        AnchorConstraints constraints = 
        new AnchorConstraints(AnchorConstraints.ANCHOR_RIGHT|AnchorConstraints.ANCHOR_TOP); 
        am.putDockableContraints(dockable1, constraints);
        // and that's it, the top right corner of your desktop is now reserved to "dockable1".
        // top left corner :
        AnchorConstraints topLeft = new AnchorConstraints(
        AnchorConstraints.ANCHOR_TOP
        |AnchorConstraint.ANCHOR_LEFT)
        // full top side :
        AnchorConstraints fullTop = new AnchorConstraints(
        AnchorConstraints.ANCHOR_TOP                      // side
        |AnchorConstraint.ANCHOR_LEFT  			   // and borders
        |AnchorConstaints.RIGHT)
        // always on the left, but not taking full height :
        AnchorConstraints left = new AnchorConstraints(
        AnchorConstraint.ANCHOR_LEFT );
         */

        // add sale/reload menus
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        menubar.add(actions);
        actions.add(saveWorkspaceAction);
        actions.add(loadWorkspaceAction);
        setJMenuBar(menubar);

        // addToolBars...
        // Toolbar on top border
        ToolBarPanel topPanel = container.getToolBarPanelAt(BorderLayout.NORTH);
        VLToolBar tb1 = new VLToolBar("tb1");
        VLToolBar tb2 = new VLToolBar("tb2");
        VLToolBar tb3 = new VLToolBar();
        tb3.setName("tb3"); // alternative way of giving a name to a toolbar
        VLToolBar tb4 = new VLToolBar("tb4");
        tb1.add(new JButton("1A"));
        tb1.add(new JButton("1B"));
        tb2.add(new JButton("2A"));
        tb2.add(new JButton("2B"));
        tb3.add(new JButton("3A"));
        tb3.add(new JButton("3B"));
        tb4.add(new JButton("4A"));
        tb4.add(new JButton("4B"));
        topPanel.add(tb1, new ToolBarConstraints(0, 0)); // first row, first column
        topPanel.add(tb2, new ToolBarConstraints(0, 1)); // first row, second column
        
        // Toolbar on left border 
        ToolBarPanel leftPanel = container.getToolBarPanelAt(BorderLayout.WEST);
        leftPanel.add(tb3, new ToolBarConstraints(0, 0)); // first column, first row
        leftPanel.add(tb4, new ToolBarConstraints(0, 1)); // first column,second row 


        // listen to dockable state changes before they are commited
        desk.addDockableStateWillChangeListener(new DockableStateWillChangeListener() {

            public void dockableStateWillChange(DockableStateWillChangeEvent event)
              {
                DockableState current = event.getCurrentState();
                if (current.getDockable() == editorPanel) {
                    if (event.getFutureState().isClosed()) {
                        // we are facing a closing of the editorPanel
                        event.cancel(); // refuse it

                    }
                }
              }
        });
      }
//    Dockable firstDockable = null;
//    int tabNumber = 0;
    // <editor-fold defaultstate="collapsed" desc=">>>--- ApplicationFrame implementation ---<<<" >
    public void addBox(String name, JComponent comp)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    public void addTool(String name, String iconPath, JPanel panel, String tooltip)
      {
        addTool(name, iconPath, panel, tooltip, false);
      }

    public void addTool(String name, String iconPath, JPanel panel, String tooltip, boolean open)
      {
        if (open) {
            desk.addDockable(new MyDockablePanel(name, iconPath, panel, tooltip), RelativeDockablePosition.BOTTOM);
        } else {
            desk.addHiddenDockable(new MyDockablePanel(name, iconPath, panel, tooltip), RelativeDockablePosition.BOTTOM);
        }
//        if (firstDockable == null) {
//            firstDockable = new MyDockablePanel(name, iconPath, panel, tooltip);
//            desk.addDockable(firstDockable);
//        } else {
//            tabNumber++;
//            desk.createTab(firstDockable, new MyDockablePanel(name, iconPath, panel, tooltip),tabNumber);
//        }
      }

    public void setToolBar(List actions)
      {
        ToolBarPanel topPanel = container.getToolBarPanelAt(BorderLayout.NORTH);
        VLToolBar bar = createVLToolBar("TheToolbar", actions);
        bar.setName("ToolBar");
        topPanel.add(bar, new ToolBarConstraints(0, 0)); // first row, first column
      }
    public void addToolBar(String name, List actions)
      {
        ToolBarPanel topPanel = container.getToolBarPanelAt(BorderLayout.NORTH);
        VLToolBar bar = createVLToolBar(name, actions);
        topPanel.add(bar, new ToolBarConstraints(0, 0)); // first row, first column
      }
    public void setToolBar(JToolBar toolBar)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    public Icon loadIcon(String path)
      {
        return (Icon) new javax.swing.ImageIcon(getClass().getResource(path));
      }
// </editor-fold>

    public DockingDesktop getDesk() {
        return desk;
    }
    public VLToolBar createVLToolBar(String name, List list)
      {
        VLToolBar toolbar = new VLToolBar(name);
        ActionContainerFactory factory = new ActionContainerFactory(ActionManager.getInstance());
        
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (element == null) {
                toolbar.addSeparator();
            } else {
                AbstractButton button = factory.createButton(element, toolbar);
                // toolbar buttons shouldn't steal focus
                button.setFocusable(false);
                /*
                 * TODO
                 * The next two lines improve the default look of the buttons.
                 * This code should be changed to retrieve the default look
                 * from some UIDefaults object.
                 */
                button.setMargin(new Insets(1, 1, 1, 1));
                button.setBorderPainted(false);
                toolbar.add(button);
            }
        }
        return toolbar;
      }

    /** Basic application starter */
    public static void main(String[] args)
      {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {
        }
        DockingPreferences.initHeavyWeightUsage();
        DockingPreferences.setSingleHeavyWeightComponent(true);

        SwingUtilities.invokeLater(new Runnable() {

            public void run()
              {
                final AppFrameVLDockTester frame = new AppFrameVLDockTester("");
                frame.setSize(800, 600);
                frame.validate();
                frame.setVisible(true);
              }
        });

      }

    // Panels are added to this dockable panel
    class MyDockablePanel
        extends JPanel
        implements Dockable {

        DockKey key;
        java.awt.Container actualPanel;

        public MyDockablePanel(String name, String iconPath, java.awt.Container actualPanel, String tooltip)
          {
            this(name, iconPath, actualPanel, tooltip, false, false, false, 0);
          }

        public MyDockablePanel(String name, String iconPath, java.awt.Container actualPanel, String tooltip,
            boolean closable, boolean hideable, boolean floatable, float resizeWeight)
          {
            this.actualPanel = actualPanel;
            key = new DockKey(name);
            setLayout(new BorderLayout());
            //actualPanel.setPreferredSize(new Dimension(400, 180));
            add(actualPanel, BorderLayout.CENTER);
            desk.setDockableWidth(this, 1.0);
            key.setName(name);
            key.setTooltip(tooltip);
            key.setIcon(new ImageIcon(getClass().getResource(iconPath)));
            // @todo use resourceManager...
            // customized behaviour
            key.setCloseEnabled(false);
            key.setAutoHideEnabled(false);
            key.setFloatEnabled(true);
            key.setResizeWeight(resizeWeight);
          }

        public DockKey getDockKey()
          {
            return key;
          }

        public Component getComponent()
          {
            return this;
          }
    }
    // <editor-fold defaultstate="collapsed" desc=">>>--- Workspace Save/Restore  ---<<<" >
    byte[] savedWorkpace;      // byte array used to save a workspace (custom layout of dockables)

    // action used to save the current workspace
    Action saveWorkspaceAction = new AbstractAction("Save Workspace") {

        public void actionPerformed(ActionEvent e)
          {
            saveWorkspace();
          }
    };    // action used to reload a workspace
    Action loadWorkspaceAction = new AbstractAction("Reload Workspace") {

        public void actionPerformed(ActionEvent e)
          {
            loadWorkspace();
          }
    };
    //File workspaceFile = new File(com.myjavatools.lib.Files.getcwd() + "/workspace1");
    File workspaceFile = new File("./workspace1");

    public void setWorkspaceFile(File workspaceFile)
      {
        this.workspaceFile = workspaceFile;
      }

    /** Save the current workspace into an instance byte array */
    private void saveWorkspace()
      {
        try {
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(workspaceFile));
            desk.writeXML(out);
            desk.getDockables();
            out.close();
            //savedWorkpace = out.toByteArray();
            loadWorkspaceAction.setEnabled(true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
      }

    /** Reloads a saved workspace  */
    public void loadWorkspace()
      {
        // have to registerDockable(Dockable d)  all your dockables before calling  readXML(InputStream in)

        try {
            // first : declare the dockables to the desktop (they will be in the "closed" dockable state).
//            desk.registerDockable(editorPanel);
//            desk.registerDockable(tablePanel);
//            desk.registerDockable(buttonGrid);
//            desk.registerDockable(treePanel);
            // or

//            DockingContext ctx = desk.getContext();
//            DockableResolver resolver = new DockableResolver() {
//
//                public Dockable resolveDockable(String keyName) {
////                    if (keyName.equals("dockable1")) {
////                        // instanciate dockable1 
////                        return dockable1;
////                    } else if (keyName.equals("dockable2") {
////                        // instanciate dockable2 
////                        return dockable2;
////                    } else {
////                        return null; // for unknown dockable keys
////
////                    }
//                    return null;
//                }
//
//
//            };
//            ctx.setDockableResolver(resolver);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(workspaceFile));
            // then, load the workspace 
            desk.readXML(in);
            in.close(); // stream isn't closed

        } catch (Exception ex) {
            // catch all exceptions, including those of the SAXParser
            ex.printStackTrace();
        }
      }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=">>>--- From VL Demo...  ---<<<" >
    /** Inner class describing a dockable text editor.
     * */
    class MyTextEditor
        extends JPanel
        implements Dockable {

        JTextArea textArea = new JTextArea("A Text Area");
        DockKey key = new DockKey("textEditor");

        public MyTextEditor()
          {
            setLayout(new BorderLayout());
            JScrollPane jsp = new JScrollPane(textArea);
            jsp.setPreferredSize(new Dimension(300, 400));
            add(jsp, BorderLayout.CENTER);
            // customized display
            key.setName("The Text Area");
            key.setTooltip("This is the text area tooltip");
            //key.setIcon(new ImageIcon(getClass().getResource("document16.gif")));
            // customized behaviour
            key.setCloseEnabled(false);
            key.setAutoHideEnabled(false);
            key.setResizeWeight(1.0f); // takes all resizing

          }

        /** implement Dockable  */
        public DockKey getDockKey()
          {
            return key;
          }

        /** implement Dockable  */
        public Component getComponent()
          {
            return this;
          }
    }

    class MyTree
        extends JPanel
        implements Dockable {

        JTree tree = new JTree();
        DockKey key = new DockKey("tree");

        public MyTree()
          {
            setLayout(new BorderLayout());
            JScrollPane jsp = new JScrollPane(tree);
            jsp.setPreferredSize(new Dimension(200, 200));
            add(jsp, BorderLayout.CENTER);
            key.setAutoHideEnabled(true);
          }

        public DockKey getDockKey()
          {
            return key;
          }

        public Component getComponent()
          {
            return this;
          }
    }

    class MyGridOfButtons
        extends JPanel
        implements Dockable {

        DockKey key = new DockKey("grid of buttons");

        public MyGridOfButtons()
          {
            setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    add(new JButton("btn " + (i * 3 + j)));
                }
            }
            setPreferredSize(new Dimension(200, 300));
          }

        public DockKey getDockKey()
          {
            return key;
          }

        public Component getComponent()
          {
            return this;
          }
    }

    class MyJTable
        extends JPanel
        implements Dockable {

        JTable table = new JTable();
        DockKey key = new DockKey("table");

        public MyJTable()
          {
            setLayout(new BorderLayout());
            table.setModel(new DefaultTableModel(5, 5));
            JScrollPane jsp = new JScrollPane(table);
            jsp.setPreferredSize(new Dimension(200, 200));
            add(jsp, BorderLayout.CENTER);
          }

        public DockKey getDockKey()
          {
            return key;
          }

        public Component getComponent()
          {
            return this;
          }
    }
    // </editor-fold>
 
    
    /** A simple console panel */
  class ConsolePanel extends JPanel implements Dockable {
    DockKey key = new DockKey("log", "Console",  "Console for logging error messages",
        new ImageIcon(getClass().getResource("console16.png")));
    
    JTextArea jta = new JTextArea(10, 80);
    
    PrintStream out = new PrintStream(new LogStream(), true);
    
    public ConsolePanel(){  
      super(new BorderLayout());
      out.println("Console logging initialized "+ new Date());
      jta.setEditable(false);
      JScrollPane jsp = new JScrollPane(jta);
      jsp.setPreferredSize(new Dimension(400,100));
      add(jsp, BorderLayout.CENTER);
      
      key.setAutoHideBorder(DockingConstants.HIDE_BOTTOM);
    }
    
    public DockKey getDockKey() {
      return key;
    }
    
    public Component getComponent() {
      return this;
    }
    
    
    class LogStream extends OutputStream {
      StringBuffer temp = new StringBuffer(512);
      boolean append;
      
      public synchronized void write(int b) throws IOException {
        temp.append( (char) b);
        append = true;
      }
      
      public synchronized void flush() throws IOException {
        if (append) {
          jta.append(temp.toString());
          append = false;
        }
        temp = new StringBuffer(512);
      }
    }
  }

    @Override
    public void setup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

