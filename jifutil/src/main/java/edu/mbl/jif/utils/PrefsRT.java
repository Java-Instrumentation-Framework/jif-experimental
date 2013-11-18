package edu.mbl.jif.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.prefs.*;
//import edu.mbl.jif.gui.table.sort.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
//import edu.mbl.jif.gui.DialogBoxI;


// Preferences
public class PrefsRT
{
    public static final String SAVE_DIR = "saveDir";
    public static Preferences usr = Preferences.userNodeForPackage(PrefsRT.class);
    public static Preferences sys =
            Preferences.systemNodeForPackage(PrefsRT.class);

    static {
        usr.addPreferenceChangeListener(new PreferenceChangeListener()
        {
            public void preferenceChange (PreferenceChangeEvent pce) {
                if (PrefsRT.usr.getBoolean("deBug", false)) {
                    System.out.println("Prefs: " + pce.getKey() + " <-- "
                                       + pce.getNewValue());
                }
            }
        });
    }


    //static Preferences prefsUsr = Preferences.userRoot();
    //  String pkgNode = "/Camera";
    //  Preferences prefsUsr = Preferences.userRoot().node(pkgNode);
    //  Preferences prefsSys = Preferences.systemRoot().node(pkgNode);


    private PrefsRT () {}


    // Display all the entries
    public static String keysList () {
        System.out.println(usr);
        String lk = "PREFERENCES (.usr) --------------------------\n";
        try {
            String[] keys = usr.keys();
            for (int i = 0, n = keys.length; i < n; i++) {
                //System.out.println(lk + keys[i] + ": " + usr.get(keys[i], "Unknown"));
                lk = lk + keys[i] + ": " + usr.get(keys[i], "Unknown") + "\n";
            }
        }
        catch (BackingStoreException e) {
            System.err.println("Unable to read backing store: " + e);
        }
        return lk;
    }


//
    //----------------------------------------------------------------
    //
    public static void checkPrefsSet () {
        String set = PrefsRT.usr.get("psj.defaultPrefsSet", "notSet");
        if (set.equalsIgnoreCase("notSet")) {
            PrefsRT.resetToDefaults();
        }
    }


//
//---------------------------------------------------------------------------
    public static void resetToDefaults () {
//        File file = new File(".\\PSjPrefsDefault.xml");
//        try {
//            FileInputStream stream = new FileInputStream(file);
//            try {
//                Preferences.importPreferences(stream);
//            }
//            catch (InvalidPreferencesFormatException exception) {
//                DialogBoxI.boxError("Resetting Preferences",
//                                     "Could not reset Preferences.");
//                exception.printStackTrace();
//            }
//            finally {
//                stream.close();
//            }
//        }
//        catch (IOException exception) {
//            DialogBoxI.boxError("Resetting Preferences",
//                                 "DefaultPreferences file not found.");
//            exception.printStackTrace();
//        }
        //PrefsRT.usr.put("psj.defaultPrefsSet", PSjUtils.timeStamp());
        //PSjUtils.event("Preferences set to Defaults");
    }


    //
    //--------------------------------------------------------------------------
    // Get object array of prefs for display in table
    public static Object[][] getPrefsObjects () {
        Object[][] obj = null;
        try {
            String[] keys = usr.keys();
            obj = new Object[keys.length][2];

            for (int i = 0, n = keys.length; i < n; i++) {
                //System.out.println(lk + keys[i] + ": " + usr.get(keys[i], "Unknown"));
                obj[i][0] = keys[i];
                obj[i][1] = usr.get(keys[i], "Unknown") + "\n";
            }
        }
        catch (BackingStoreException e) {
            System.err.println("Unable to read backing store: " + e);
        }
        return obj;
    }


    //-------------------------------------------------------------------------
//    public static void main (String[] args) {
//        JFrame f = new JFrame("Preferences");
//        //----------------------------------------------------------------------
//        String[] headerStr = {
//                "Parameter", "Value"};
//        int[] columnWidth = {
//                100, 150};
//        Object[][] dataObjectArray = PrefsRT.getPrefsObjects();
//
//        SortableTableModel dataModel = new SortableTableModel()
//        {
//            public Class getColumnClass (int col) {
//                switch (col) {
//                    case 0:
//                        return String.class;
//                    case 1:
//                        return String.class;
//                    default:
//                        return Object.class;
//                }
//            }
//
//
//            public boolean isCellEditable (int row, int col) {
//                switch (col) {
//                    case 1:
//                        return false;
//                    default:
//                        return false;
//                }
//            }
//
//
//            public void setValueAt (Object obj, int row, int col) {
//                switch (col) {
//                    case 2:
//                        super.setValueAt(new Integer(obj.toString()), row, col);
//                        return;
//                    default:
//                        super.setValueAt(obj, row, col);
//                        return;
//                }
//            }
//        };
//        // SortableTableModel
//        //-----------------------------------------------------------------
//
//        f.getContentPane().add(
//                new SortableTablePanel(headerStr, columnWidth, dataObjectArray,
//                                       dataModel),
//                BorderLayout.CENTER);
//        f.setSize(400, 160);
//        f.setVisible(true);
//        f.addWindowListener(new WindowAdapter()
//        {
//            public void windowClosing (WindowEvent e) {
//                System.exit(0); }
//        });
//    }

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

   public void clearPreferences(){
 try {
   prefsUsr.clear();
 } catch (BackingStoreException e) {
   e.printStackTrace();
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
