package edu.mbl.jif.utils.prefs;

import edu.mbl.jif.utils.DialogBox;
import edu.mbl.jif.utils.sortTable.SortableTableModel;
import edu.mbl.jif.utils.sortTable.SortableTablePanel;
import java.io.*;
import java.util.prefs.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


// Preferences
public class Prefs {

    public static final String SAVE_DIR = "saveDir";
    public static String pkgNode = "/jif"; // default is /jif
    public static Preferences usr = Preferences.userRoot();
    ;
    //Preferences.userRoot().node ("/psj/Utils");
    public static Preferences sys = Preferences.systemRoot();
    //Preferences.systemRoot().node ("/psj/Utils");
    //  String pkgNode = "/Camera";
    //  Preferences prefsUsr = Preferences.userRoot().node(pkgNode);
    //  Preferences prefsSys = Preferences.systemRoot().node(pkgNode);
    static Preferences prefsUsr = Preferences.userRoot();
    static Preferences prefsSys = Preferences.systemRoot();

    //public static Preferences usr = Preferences.userNodeForPackage(Prefs.class);
    //public static Preferences sys = Preferences.systemNodeForPackage(Prefs.class);
    private Prefs() {
    }

    public static void initialize(String _pkgNode) {
        pkgNode = _pkgNode;
        usr = Preferences.userRoot().node(pkgNode);
        sys = Preferences.systemRoot().node(pkgNode);
        PreferenceChangeListener listener = new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent pce) {
                if (Prefs.sys.getBoolean("deBug", false)) {
                    System.out.println("Pref set - " + pce.getNode() + ": " + pce.getKey() + " <-- " + pce.getNewValue());
                }
            }
        };
        usr.addPreferenceChangeListener(listener);
        sys.addPreferenceChangeListener(listener);
    }

    public static String getPkgNode() {
        return pkgNode;
    }

    // Display all the entries
    public static String listKeysUsr() {
        StringBuffer lk = new StringBuffer();
        if (usr != null) {
            try {
                String[] keys = usr.keys();
                for (int i = 0, n = keys.length; i < n; i++) {
                    lk.append(keys[i] + ": " + usr.get(keys[i], "unknown") + "\n");
                }
            } catch (BackingStoreException e) {
                System.err.println("Unable to read backing store: " + e);
            }
        }
        return lk.toString();
    }

    public static String listKeysSys() {
        String lk = "";
        if (sys != null) {
            try {
                String[] keys = sys.keys();
                for (int i = 0, n = keys.length; i < n; i++) {
                    lk = lk + keys[i] + ": " + sys.get(keys[i], "unknown") + "\n";
                }
            } catch (BackingStoreException e) {
                System.err.println("Unable to read backing store: " + e);
            }
        }
        return lk;
    }

    //----------------------------------------------------------------
    // Get object array of prefs for display in table
    // Get USER prefs
    public static Object[][] getPrefsUsrObjects() {
        Object[][] obj = null;
        if (usr != null) {
            try {
                String[] keys = usr.keys();
                obj = new Object[keys.length][2];
                for (int i = 0, n = keys.length; i < n; i++) {
                    //System.out.println(lk + keys[i] + ": " + usr.get(keys[i], "Unknown"));
                    obj[i][0] = keys[i];
                    obj[i][1] = usr.get(keys[i], "Unknown") + "\n";
                }
            } catch (BackingStoreException e) {
                System.err.println("Unable to read backing store: " + e);
            }
        }
        return obj;
    }

// Get SYSTEM prefs
    public static Object[][] getPrefsSysObjects() {
        Object[][] obj = null;
        if (sys != null) {
            try {
                String[] keys = sys.keys();
                obj = new Object[keys.length][2];
                for (int i = 0, n = keys.length; i < n; i++) {
                    //System.out.println(lk + keys[i] + ": " + usr.get(keys[i], "Unknown"));
                    obj[i][0] = keys[i];
                    obj[i][1] = sys.get(keys[i], "Unknown") + "\n";
                }
            } catch (BackingStoreException e) {
                System.err.println("Unable to read backing store: " + e);
            }
        }
        return obj;
    }

    final static String fileExt = ".preferences";

    public static void loadPrefsFile() {
        String f = selectPrefsFile();
        if (f != null) {
            importFrom(f);
        }
    }

    public static void savePrefsFile(String node) {
        String f = selectPrefsFile();
        if (f != null) {
            usr = Preferences.userRoot().node(node);
            exportUsrTo(f+"."+ fileExt);
        }
    }

    public static String selectPrefsFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        String prefsPath = getcwd() + "/prefs/";
        fileChooser.setCurrentDirectory(new File(prefsPath));
        //fileChooser.setFileFilter(new FileFilterImpl());
        fileChooser.setFileFilter(new FileNameExtensionFilter("prefs", "preferences"));
        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        //File file = fileChooser.getSelectedFile()..getCanonicalFile();
        String filename = fileChooser.getSelectedFile().getPath();
        return filename;
    }
		
		  public static String getcwd() {
    File here = new File(".");
    try {
      return here.getCanonicalPath();
    } catch (Exception e) {};
    return here.getAbsolutePath();
//    return System.getProperty("user.dir");
  }

    //----------------------------------------------------------------
    // Import
    public static void importFrom(String xmlFile) {
        // Create an input stream on a file
        File file = new File(xmlFile);
        try {
            FileInputStream stream = new FileInputStream(file);
            try {
                Preferences.importPreferences(stream);
            } catch (InvalidPreferencesFormatException exception) {
                DialogBox.boxError("Importing Preferences",
                        "Could not import: Invalid preferences format");
                exception.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        } catch (IOException exception) {
            DialogBox.boxError("Importing Preferences",
                    "Preferences file " + xmlFile + " not found.");
            exception.printStackTrace();
        }

    }

    // Export the node to a file
    public static void exportUsrTo(String filename) {
        try {
            usr.exportNode(new FileOutputStream(filename));
        } catch (IOException e) {
        } catch (BackingStoreException e) {
        }
    }

    public void clearUsrPreferences() {
        try {
            usr.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public void clearSysPreferences() {
        try {
            usr.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------

    public static void main(String[] args) {
        frameDisplayPrefs("/edu/mbl/jif/camera/camacq");

    }

    public static void frameDisplayPrefs(String root) {
        //      System.out.println("prefsSys.absolutePath: " + prefsSys.absolutePath());
        //      System.out.println("prefsUsr.absolutePath: " + prefsUsr.absolutePath());
        Prefs.initialize(root);
        frameDisplayPrefs();
    }

    public static void frameDisplayPrefs() {
        System.out.println("sys.absolutePath: " + sys.absolutePath());
        System.out.println("usr.absolutePath: " + usr.absolutePath());
        //Prefs.sys.put("test", "A Test for sys");
        //Prefs.usr.put("test", "A Test for usr");
        System.out.println("Usr:\n" + Prefs.listKeysUsr());
        System.out.println("Sys:\n" + Prefs.listKeysSys());
        JFrame f = new JFrame("Preferences: " + Prefs.getPkgNode());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //----------------------------------------------------------------------
        String[] headerStr = {"Parameter", "Value"};
        int[] columnWidth = {100, 150};
        Object[][] dataObjectArray = Prefs.getPrefsUsrObjects();

        SortableTableModel dataModel = new SortableTableModel() {

            public Class getColumnClass(int col) {
                switch (col) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    default:
                        return Object.class;
                }
            }

            public boolean isCellEditable(int row, int col) {
                switch (col) {
                    case 1:
                        return false;
                    default:
                        return false;
                }
            }

            public void setValueAt(Object obj, int row, int col) {
                switch (col) {
                    case 2:
                        super.setValueAt(new Integer(obj.toString()), row, col);
                        return;
                    default:
                        super.setValueAt(obj, row, col);
                        return;
                }
            }
        };
        // SortableTableModel
        //-----------------------------------------------------------------

        f.getContentPane().add(new SortableTablePanel(headerStr, columnWidth,
                dataObjectArray, dataModel), BorderLayout.CENTER);
        f.setSize(500, 600);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                //System.exit(0);
            }
        });
    }

    @SuppressWarnings("serial")
    public static class FileChooser extends JFileChooser {

        public void approveSelection() {
            if (getDialogType() == JFileChooser.SAVE_DIALOG && getSelectedFile().exists()) {
                int ans = JOptionPane.showConfirmDialog(this, "Overwrite existing file?",
                        "File exists", JOptionPane.YES_NO_OPTION);
                if (ans != JOptionPane.YES_OPTION) {
                    super.cancelSelection();
                    return;
                }
            }
            super.approveSelection();
        }
    }

    private static class FileFilterImpl implements FileFilter {

        public FileFilterImpl() {
        }

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(fileExt);
        }

        public String getDescription() {
            return "Java Preferences files";
        }
    }
}

/*
//----------------------------------------------------------------------
// Notes on Using Preferences API

// int numRows = prefsUsr.getInt(NUM_ROWS, 40);
MyListener listener = new MyListener();
// Create a node for testing
Preferences listeningNode = Preferences.userRoot().node("listening");
listeningNode.addNodeChangeListener(listener);
listeningNode.addPreferenceChangeListener(listener);
try {
listeningNode.put("key1", "bar");
listeningNode.putInt("key1", 8276);
listeningNode.clear();
Preferences test1 = listeningNode.node("test1");
Preferences test2 = test1.node("test2");
test2.removeNode();
test1.removeNode();
}
catch (BackingStoreException bsEx) {
// Ignore it
}
try {
Thread.sleep(5000);
}
catch (InterruptedException iEx) {
// Ignore it as well
}
}
class MyListener
implements NodeChangeListener, PreferenceChangeListener {

public void childAdded(NodeChangeEvent nceEvt) {
System.out.print("Child: ");
System.out.print(nceEvt.getChild().name());
System.out.print(" added to Parent: ");
System.out.println(nceEvt.getParent().name());
}
public void childRemoved(NodeChangeEvent nceEvt) {
System.out.print("Child: ");
System.out.print(nceEvt.getChild().name());
System.out.print(" removed from Parent: ");
System.out.println(nceEvt.getParent().name());
}
public void preferenceChange(PreferenceChangeEvent pceEvt) {
System.out.print("Preference change at Node: ");
System.out.print(pceEvt.getNode().name());
System.out.print(" key: " + pceEvt.getKey());
System.out.println(" value: " + pceEvt.getNewValue());
}
}


// put(String key, String value)
// putBoolean(String key, boolean value)
// putByteArray(String key, byte value[])
// putDouble(String key, double value)
// putFloat(String key, float value)
// putInt(String key, int value)
// putLong(String key, long value)
// get(String key, String default)
// getBoolean(String key, boolean default)
// getByteArray(String key, byte default[])
// getDouble(String key, double default)
// getFloat(String key, float default)
// getInt(String key, int default)
// getLong(String key, long default)
//  keys()
 */

/*
The Java platform provides the Preferences API in the java.util.prefs packages
for the storage and retrieval of system and user preferences. These preferences
are then saved in an implementation-specific way, like the Microsoft Windows
Registry, so you, as the developer, don't have to worry about where to store
settings. While there are many different tasks you can do with preferences,
essentially, working with the Preferences API consists of two primary tasks:
finding the root node to work with and then reading or writing a value from
it. There are two node hierarchies, one at the user level and another for
system level preferences, where node values can be strings, boolean values,
byte arrays, or numbers (float, long, int, or double).

The Preference class is a series of static and abstract methods. Once you
get the root node to manipulate with a static method, you are given a
concrete implementation from which you can call the concrete implementations
of the abstract methods. Then, you can get or set different node values.

There are four different ways to get a node, two for each working with
system and user nodes. If you are working in a non-static method, you
can pass an instance of an object whose package you want preferences
saved for to the userNodeForPackage and systemNodeForPackage methods:

Preferences userPrefs = Preferences.userNodeForPackage(this);
Preferences sysPrefs = Preferences.systemNodeForPackage(this);

If instead you are working within a static method like the main method,
you need to get the root node first, then pass in the package as a string
to move to the appropriate node:

Preferences userPrefs = Preferences.userRoot().node("/net/java");
Preferences sysPrefs = Preferences.systemRoot().node("/net/java");

Assuming your class was in the net.java package, the user node calls would
return one node to work with while the system node calls would return a
different one. You would use the system version for settings you wanted to
set for all users of the application on that single client machine.
Node names are limited to a maximum length of Preferences.MAX_NAME_LENGTH
(80 characters).

Once you have a node, you can store preferences in it with the put method.
There are also specialized versions of the method that convert different
data types to a String for storage. These are listed below. The key for
the preference is limited to a length of Preferences.MAX_KEY_LENGTH (80)
characters, while values are limited to Preferences.MAX_VALUE_LENGTH (8192)
characters.

 * put(String key, String value)
 * putBoolean(String key, boolean value)
 * putByteArray(String key, byte value[])
 * putDouble(String key, double value)
 * putFloat(String key, float value)
 * putInt(String key, int value)
 * putLong(String key, long value)

Retrieval of preferences works with the get method, where again there are
specialized methods for the different supported data types. These methods
retrieve the value for the key from the Preferences node. Notice that a
default value is made available for when the node is not found or is
unavailable. This is the one difference between the get and put calls.

 * get(String key, String default)
 * getBoolean(String key, boolean default)
 * getByteArray(String key, byte default[])
 * getDouble(String key, double default)
 * getFloat(String key, float default)
 * getInt(String key, int default)
 * getLong(String key, long default)

In addition to just setting and getting property values, the Preferences API
also provides support for listening for changes to preferences by attaching a
pair of listeners, either addNodeChangeListener or addPreferenceChangeListener.
You can also find a list of keys under a node with keys, or remove nodes and
values with calls to clear, remove, removeNode.

The following program demonstrates the Preferences API by saving some
characters of Berkeley Breathed's comic strips Bloom County and Outland.

package net.java.BloomCounty;

import java.util.prefs.*;

public class Sample {
public static void main(String args[]) {
String stuff[][] = {
{"Opus", "Penguin"},
{"Bill", "the Cat"},
{"Steve", "Dallas"},
{"Oliver", "Wendell Jones"},
{"Michael", "Binkley"},
{"Ronald", "Ann"},
{"Cutter", "John"},
{"Milque", "toast"},
{"Berkeley", "Breathed"}
};

Preferences prefs =
Preferences.userRoot().node("/net/java/BloomCounty");

prefs.addPreferenceChangeListener(new PreferenceChangeListener() {
public void preferenceChange(PreferenceChangeEvent evt) {
System.out.println("Change: " +
evt.getKey() + "\t" + evt.getNewValue() +
"\t" + evt.getNode());
}
});

for (int i=0, n=stuff.length; i < n; i++) {
prefs.put(stuff[i][0], stuff[i][1]);
}

try {
String keys[] = prefs.keys();
for (int i=0, n=keys.length; i < n; i++) {
System.out.println(keys[i] + ": " + prefs.get(keys[i], ""));
}
} catch (BackingStoreException e) {
System.err.println("Unable to read backing store: " + e);
}
}
}

 */
/*
// Export the node to a file
public void exportTo(String filename) {
try {
prefs.exportNode(new FileOutputStream("output.xml"));
} catch (IOException e) {
} catch (BackingStoreException e) {
}
}

// Returns true if node contains the specified key; false otherwise.
public static boolean contains(Preferences node, String key) {
return node.get(key, null) != null;
}

// Remove a preference in the node
final String PREF_NAME = "name_of_preference";
prefs.remove(PREF_NAME);

// Remove all preferences in the node
try {
prefs.clear();
} catch (BackingStoreException e) {
}
 */
