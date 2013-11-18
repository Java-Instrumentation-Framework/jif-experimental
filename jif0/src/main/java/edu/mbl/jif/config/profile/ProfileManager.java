package edu.mbl.jif.config.profile;


import edu.mbl.jif.gui.table.sortable.TableHeaderSorter;
import edu.mbl.jif.gui.table.sortable.TableSorter;
import edu.mbl.jif.gui.test.FrameForTest;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;



/**
 *
 * @author GBH
 */
public class ProfileManager {
    
    
// <editor-fold defaultstate="collapsed" desc=">>>--- Test data maker ---<<<" >
    // test data ==============
    public static void makePrefsData() {
        Preferences usrRoot = Preferences.userRoot().node("testProfiles");
        String[] profiles = new String[]{
            "ProfileA", "ProfileB", "ProfileC",
        };
        String[] settings = new String[]{
            "Setting1", "Setting2", "Setting3"
        };
        // for each profile
        for (String profile : profiles) {
            makeNamedNode(usrRoot, profile);
            Preferences profilePrefNode = usrRoot.node(profile);
            for (String setting : settings) {
                makeNamedNode(profilePrefNode, setting);
            }
        }
    }

    public static void makeNamedNode(Preferences node, String name) {
        node.node(name).put("name", "My-" + name);
        node.node(name).put("description", name + " Description");
    }
// </editor-fold>
   
    
    public static void main(String args[]) {

        Runnable runnable = new Runnable() {

            public void run() {
                try {
                     makePrefsData();
                    Preferences usrRoot = Preferences.userRoot().node("testProfiles");
                    java.util.Hashtable<Object, Object> profilesTable = makeProfileHashTable(usrRoot);
                    OpenPrefsTable("Profiles", profilesTable);
                    java.util.Hashtable<Object, Object> setTable = makeSavedSetHashTable(usrRoot.node("ProfileA"));
//                    test("Settings", setTable);
                } catch (Exception exception) {
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    static final JTable table = new JTable();

    public static java.util.Hashtable<Object, Object> makeProfileHashTable(Preferences usrRoot) {
        // get the defined profiles
        String[] profiles;
        java.util.Hashtable<Object, Object> profileTable =
            new java.util.Hashtable<Object, Object>();
        try {
            profiles = usrRoot.childrenNames();
            for (String profile : profiles) {
                Preferences profileNode = usrRoot.node(profile);
                String name = profileNode.get("name", "");
                String desc = profileNode.get("description", "");
                profileTable.put(name, desc);
            }
        } catch (BackingStoreException ex) {
            Logger.getLogger(ProfilesTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profileTable;
    }

    public static java.util.Hashtable<Object, Object> makeSavedSetHashTable(Preferences profileNode) {
        // get the defined profiles
        String[] sets;
        java.util.Hashtable<Object, Object> settingsTable =
            new java.util.Hashtable<Object, Object>();
        try {
            sets = profileNode.childrenNames();
            for (String savedSet : sets) {
                Preferences setNode = profileNode.node(savedSet);
                String name = setNode.get("name", "");
                String desc = setNode.get("description", "");
                settingsTable.put(name, desc);

            }
        } catch (BackingStoreException ex) {
            Logger.getLogger(ProfilesTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return settingsTable;
    }

    public void testSavedSets() {
        Preferences profileNode = Preferences.userRoot().node("testProfiles").node("firstname");
        makeSavedSetHashTable(profileNode);
    }

    public static void OpenPrefsTable(String title, java.util.Hashtable<Object, Object> hTable) {
        final FrameForTest frame = new FrameForTest();
        frame.setTitle(title);
        final ProfilesTableModel model = new ProfilesTableModel();

        model.updateTableContents(hTable);
        TableSorter sorter = new TableSorter(model);
        table.setModel(sorter);
        TableHeaderSorter.install(sorter, table);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setWidth(20);
        table.getColumnModel().getColumn(1).setWidth(34);
        //SelectionListener listener = new SelectionListener(table);
        //table.getSelectionModel().addListSelectionListener(listener);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        Container content = frame.getContentPane();
        //content.add(toolbar, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        //
        // Select
        Action selectAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (!table.getSelectionModel().isSelectionEmpty()) {
                    int selected = table.getSelectionModel().getMinSelectionIndex();
                    System.out.println("Selected: " + selected);
                }
            }
        };
        selectAction.putValue(Action.NAME, "Selected");
        JButton selectButton = new JButton(selectAction);
        buttonPanel.add(selectButton);
        //
        // New
        Action newAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Prompt for Copy from Existing or from Default");
                System.out.println("Create New");
            }
        };
        newAction.putValue(Action.NAME, "New");
        JButton newButton = new JButton(newAction);
        buttonPanel.add(newButton);
        //
        // Delete
        Action deleteAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (!table.getSelectionModel().isSelectionEmpty()) {
                    int selected = table.getSelectionModel().getMinSelectionIndex();
                    System.out.println("Deleted: " + selected);
                }
            }
        };
        deleteAction.putValue(Action.NAME, "Delete");
        JButton deleteButton = new JButton(deleteAction);
        buttonPanel.add(deleteButton);
        //
        content.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}

