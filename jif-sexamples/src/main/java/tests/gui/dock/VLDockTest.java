package tests.gui.dock;


/* Copyright 2005 VLSolutions.
 * This code is provided "as is" and can be freely used (or parts of it) in any
 * application.
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import com.vlsolutions.swing.docking.*;
import com.vlsolutions.swing.docking.event.*;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import com.vlsolutions.swing.toolbars.VLToolBar;
import java.io.*;
import java.awt.event.*;

/** This is the sample application of the VLDocking Framework 1.0 tutorial.
 *<p>
 * It is not meant to be a real life application, but will introduce the most
 * important points of the docking framework :
 * <ul>
 *   <li> the Dockable interface
 *   <li> the DockingDesktop and its layout
 *   <li> how to customize the display and behaviour of the desktop
 * </ul>
 * */
public class VLDockTest
        extends JFrame {


    // our 4 dockable components
    MyTextEditor editorPanel = new MyTextEditor();
    MyTree treePanel = new MyTree();
    MyGridOfButtons buttonGrid = new MyGridOfButtons();
    MyJTable tablePanel = new MyJTable();

    // the desktop (which will contain dockables)
    DockingDesktop desk = new DockingDesktop();

    // byte array used to save a workspace (custom layout of dockables)
    byte[] savedWorkpace;

    // action used to save the current workspace
    Action saveWorkspaceAction = new AbstractAction("Save Workspace") {

        public void actionPerformed(ActionEvent e) {
            saveWorkspace();
        }


    };

    // action used to reload a workspace
    Action loadWorkspaceAction = new AbstractAction("Reload Workspace") {

        public void actionPerformed(ActionEvent e) {
            loadWorkspace();
        }


    };


    /** Default and only frame constructor */
    public VLDockTest() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // insert our desktop as the only one component of the frame
        ToolBarContainer container = ToolBarContainer.createDefaultContainer(true, true, true, true);  // (top, left, bottom, right)

        getContentPane().add(container, BorderLayout.CENTER);


        // set the initial dockable
        desk.addDockable(editorPanel);
        // and layout the others
        desk.split(editorPanel, tablePanel, DockingConstants.SPLIT_LEFT);
        desk.split(editorPanel, buttonGrid, DockingConstants.SPLIT_RIGHT);
//        desk.createTab(treePanel, tablePanel, 1);
        desk.split(tablePanel, treePanel, DockingConstants.SPLIT_BOTTOM);
        container.add(desk, BorderLayout.CENTER);

        // cannot reload before a workspace is saved
        loadWorkspaceAction.setEnabled(false);

        // add sale/reload menus
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        menubar.add(actions);
        actions.add(saveWorkspaceAction);
        actions.add(loadWorkspaceAction);
        setJMenuBar(menubar);

        // addToolBars...

        // top border
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
        // left border
        ToolBarPanel leftPanel = container.getToolBarPanelAt(BorderLayout.WEST);
        leftPanel.add(tb3, new ToolBarConstraints(0, 0)); // first column, first row
        leftPanel.add(tb4, new ToolBarConstraints(0, 1)); // first column,second row 


        // listen to dockable state changes before they are commited
        desk.addDockableStateWillChangeListener(new DockableStateWillChangeListener() {

            public void dockableStateWillChange(DockableStateWillChangeEvent event) {
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


    /** Save the current workspace into an instance byte array */
    private void saveWorkspace() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            desk.writeXML(out);
            out.close();
            savedWorkpace = out.toByteArray();
            loadWorkspaceAction.setEnabled(true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /** Reloads a saved workspace  */
    private void loadWorkspace() {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(savedWorkpace);
            desk.readXML(in);
            in.close();
        } catch (Exception ex) {
            // catch all exceptions, including those of the SAXParser
            ex.printStackTrace();
        }
    }


    /** Basic application starter */
    public static void main(String[] args) {
        final VLDockTest frame = new VLDockTest();
        frame.setSize(800, 600);
        frame.validate();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.setVisible(true);
            }


        });

    }


    class MyTreeDockablePanel
            extends JPanel
            implements Dockable {

        JTree tree = new JTree();
        DockKey key = new DockKey("mine");


        public MyTreeDockablePanel() {
            setLayout(new BorderLayout());
            JScrollPane jsp = new JScrollPane(tree);
            jsp.setPreferredSize(new Dimension(200, 200));
            add(jsp, BorderLayout.CENTER);
        }


        public DockKey getDockKey() {
            return key;
        }


        public Component getComponent() {
            return this;
        }


    }

    /** Inner class describing a dockable text editor.
     * */
    class MyTextEditor
            extends JPanel
            implements Dockable {

        JTextArea textArea = new JTextArea("A Text Area");
        DockKey key = new DockKey("textEditor");


        public MyTextEditor() {
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
            //
            key.setResizeWeight(1.0f); // takes all resizing
        }


        /** implement Dockable  */
        public DockKey getDockKey() {
            return key;
        }


        /** implement Dockable  */
        public Component getComponent() {
            return this;
        }


    }

    class MyTree
            extends JPanel
            implements Dockable {

        JTree tree = new JTree();
        DockKey key = new DockKey("tree");


        public MyTree() {
            setLayout(new BorderLayout());
            JScrollPane jsp = new JScrollPane(tree);
            jsp.setPreferredSize(new Dimension(200, 200));
            add(jsp, BorderLayout.CENTER);
        }


        public DockKey getDockKey() {
            return key;
        }


        public Component getComponent() {
            return this;
        }


    }

    class MyGridOfButtons
            extends JPanel
            implements Dockable {

        DockKey key = new DockKey("grid of buttons");


        public MyGridOfButtons() {
            setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    add(new JButton("btn " + (i * 3 + j)));
                }
            }
            setPreferredSize(new Dimension(200, 300));
        }


        public DockKey getDockKey() {
            return key;
        }


        public Component getComponent() {
            return this;
        }


    }

    class MyJTable
            extends JPanel
            implements Dockable {

        JTable table = new JTable();
        DockKey key = new DockKey("table");


        public MyJTable() {
            setLayout(new BorderLayout());
            table.setModel(new DefaultTableModel(5, 5));
            JScrollPane jsp = new JScrollPane(table);
            jsp.setPreferredSize(new Dimension(200, 200));
            add(jsp, BorderLayout.CENTER);
        }


        public DockKey getDockKey() {
            return key;
        }


        public Component getComponent() {
            return this;
        }


    }
}

