/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.workframe;

/* Copyright 2005 VLSolutions.
 * This code is provided "as is" and can be freely used (or parts of it) in any
 * application.
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import com.vlsolutions.swing.docking.*;
import com.vlsolutions.swing.docking.event.*;
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
public class MyFirstFrame extends JFrame {


    // our 4 dockable components
    MyTextEditor editorPanel = new MyTextEditor();
    MyTree treePanel = new MyTree();
    MyGridOfButtons buttonGrid = new MyGridOfButtons();
    MyJTable tablePanel = new MyJTable();

    // the desktop (which will contain dockables)
    DockingDesktop desk = new DockingDesktop();

    // byte array used to save a workspace (custom layout of dockables)
    byte [] savedWorkpace;

    // action used to save the current workspace
    Action saveWorkspaceAction = new AbstractAction("Save Workspace"){
        public void actionPerformed(ActionEvent e){
            saveWorkspace();
        }
    };

    // action used to reload a workspace
    Action loadWorkspaceAction = new AbstractAction("Reload Workspace"){
        public void actionPerformed(ActionEvent e){
            loadWorkspace();
        }
    };

    /** Default and only frame constructor */
    public MyFirstFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // insert our desktop as the only one component of the frame
        getContentPane().add(desk, BorderLayout.CENTER);

        // set the initial dockable
        desk.addDockable(editorPanel);
        // and layout the others
        desk.split(editorPanel, tablePanel, DockingConstants.SPLIT_LEFT);
        desk.split(editorPanel, buttonGrid, DockingConstants.SPLIT_RIGHT);
        //desk.createTab(treePanel, tablePanel, 1);
        desk.split(tablePanel, treePanel, DockingConstants.SPLIT_BOTTOM);

        // cannot reload before a workspace is saved
        loadWorkspaceAction.setEnabled(false);

        // add sale/reload menus
        JMenuBar menubar = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        menubar.add(actions);
        actions.add(saveWorkspaceAction);
        actions.add(loadWorkspaceAction);
        setJMenuBar(menubar);

        // listen to dockable state changes before they are commited
        desk.addDockableStateWillChangeListener(new DockableStateWillChangeListener(){
            public void dockableStateWillChange(DockableStateWillChangeEvent  event) {
                DockableState current = event.getCurrentState();
                if (current.getDockable() == editorPanel){
                    if (event.getFutureState().isClosed()){
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
        final MyFirstFrame frame = new MyFirstFrame();
        frame.setSize(800, 600);
        frame.validate();
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               frame.setVisible(true);
            }
        });
    
    }


    /** Inner class describing a dockable text editor.
     * */
    class MyTextEditor extends JPanel implements Dockable {
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


    class MyTree extends JPanel implements Dockable {
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


    class MyGridOfButtons extends JPanel implements Dockable {
        DockKey key = new DockKey("grid of buttons");
        public MyGridOfButtons() {
            setLayout(new FlowLayout(FlowLayout.LEADING, 3,3));
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


    class MyJTable extends JPanel implements Dockable {
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

