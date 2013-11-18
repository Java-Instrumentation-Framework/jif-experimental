/* Java Preferences Tool, version 0.8. 
Copyright 2006, Boris Gontar.

This program is free software; you can redistribute it and/or modify
it under the terms of version 2 of the GNU General Public License 
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this program; if not, write to the 
Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
Boston, MA 02111-1307  USA

You can contact me regarding this program by email, at javaprefs@gmail.com. 
The web page is located at http://javaprefs.googlepages.com.
 */
package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * The main application class. 
 * 
 * @author Boris Gontar
 * @version 0.7 2006-08-15
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

    final static String version = "0.8";
    final static String fileExt = ".preferences";
    static Icon blankIcon = new Main.BlankIcon();
    static Icon copyIcon = new ImageIcon(Main.class.getResource("images/copy_edit.gif"));
    static Icon cutIcon = new ImageIcon(Main.class.getResource("images/cut_edit.gif"));
    static Icon pasteIcon = new ImageIcon(Main.class.getResource("images/paste_edit.gif"));
    static Icon delIcon = new ImageIcon(Main.class.getResource("images/delete_edit.gif"));
    static Icon editIcon = new ImageIcon(Main.class.getResource("images/text_edit.gif"));
    static Icon addIcon = new ImageIcon(Main.class.getResource("images/add_exc.gif"));
    static Icon findIcon = new ImageIcon(Main.class.getResource("images/search.gif"));
    static Icon nextIcon = new ImageIcon(Main.class.getResource("images/search_next.gif"));
    static Icon refrIcon = new ImageIcon(Main.class.getResource("images/refresh.gif"));
    static Icon helpIcon = new ImageIcon(Main.class.getResource("images/help_topic.gif"));
    static Icon aboutIcon = new ImageIcon(Main.class.getResource("images/help.gif"));
    static Icon fileIcon = new ImageIcon(Main.class.getResource("images/file_obj.gif"));
    static Icon closeIcon = new ImageIcon(Main.class.getResource("images/close_view.gif"));
    static Icon prevIcon = new ImageIcon(Main.class.getResource("images/prev_nav.gif"));
    static Icon runIcon = new ImageIcon(Main.class.getResource("images/run_exc.gif"));
    static Icon printIcon = new ImageIcon(Main.class.getResource("images/export_log.gif"));
    static Icon expandIcon = new ImageIcon(Main.class.getResource("images/expandall.gif"));
    static Icon collapIcon = new ImageIcon(Main.class.getResource("images/collapseall.gif"));
    static ImageIcon logoIcon = new ImageIcon(Main.class.getResource("images/javalogo1.gif"));
    static ImageIcon appIcon = new ImageIcon(Main.class.getResource("images/appicon16.png"));
    static ImageIcon appLogo = new ImageIcon(Main.class.getResource("images/javaprefs64c.png"));
    private JMenuBar mainMenu = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
    private JMenu viewMenu = new JMenu("View");
    private JMenu helpMenu = new JMenu("Help");
    private Action fileNewAction = new AbstractAction("New",
        new ImageIcon(Main.class.getResource("images/new_con.gif"))) {

        public void actionPerformed(ActionEvent e)
          {
            fileNew();
          }

    };
    private Action fileOpenAction = new AbstractAction("Open",
        new ImageIcon(Main.class.getResource("images/opentype.gif"))) {

        public void actionPerformed(ActionEvent e)
          {
            fileOpen();
          }

    };
    private Action fileSaveAction = new AbstractAction("Save",
        new ImageIcon(Main.class.getResource("images/save_edit.gif"))) {

        public void actionPerformed(ActionEvent e)
          {
            fileSave();
          }

    };
    private Action fileSaveAsAction = new AbstractAction("Save As...", blankIcon) {

        public void actionPerformed(ActionEvent e)
          {
            fileSaveAs();
          }

    };
    private Action filePrintAction = new AbstractAction("Save as HTML", printIcon) {

        public void actionPerformed(ActionEvent e)
          {
            filePrint(null);
          }

    };
    private Action fileExitAction = new AbstractAction("Exit", blankIcon) {

        public void actionPerformed(ActionEvent e)
          {
            if (mayExit(null)) {
                savePrefs();
                search.dispose();
                dispose();
            }
          }

    };
    private Action editCopyAction = new AbstractAction("Copy", copyIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().copy();
          }

    };
    private Action editCutAction = new AbstractAction("Cut", cutIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().cut();
          }

    };
    private Action editDelAction = new AbstractAction("Delete", delIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().nodeDelete();
          }

    };
    private Action editElimAction = new AbstractAction("Eliminate", blankIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().nodeEliminate();
          }

    };
    private Action editPasteAction = new AbstractAction("Paste", pasteIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().paste();
          }

    };
    private Action editMergeAction = new AbstractAction("Merge", blankIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().merge();
          }

    };
    private Action editFindAction = new AbstractAction("Find...", findIcon) {

        public void actionPerformed(ActionEvent e)
          {
            editFind(true);
          }

    };
    private Action keyAddAction = new AbstractAction("Add key", addIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().keyAdd();
          }

    };
    private Action nodeClearAction = new AbstractAction("Delete all keys", blankIcon) {

        public void actionPerformed(ActionEvent e)
          {
            currViewer().nodeClear();
          }

    };
    private Action viewExpandAction = new AbstractAction("Expand subtree", expandIcon) {

        public void actionPerformed(ActionEvent e)
          {
            viewExpand();
          }

    };
    private Action viewCollapseAction = new AbstractAction("Collapse subtree", collapIcon) {

        public void actionPerformed(ActionEvent e)
          {
            viewCollapse();
          }

    };
    private Action viewRefreshAction = new AbstractAction("Refresh subtree", refrIcon) {

        public void actionPerformed(ActionEvent e)
          {
            viewRefresh();
          }

    };
    private Action helpAction = new AbstractAction("Contents", helpIcon) {

        public void actionPerformed(ActionEvent e)
          {
            new Help().setVisible(true);
          }

    };
    private Action aboutAction = new AbstractAction("About", aboutIcon) {

        public void actionPerformed(ActionEvent e)
          {
            showAbout();
          }

    };
    private JPanel statusPanel = new JPanel(new BorderLayout());
    private JLabel status = new JLabel("Ready");
    protected JTabbedPane tabbedPane = new JTabbedPane();
    private JPopupMenu tabPopup = new JPopupMenu();
    protected JMenuItem tabCloseItem = new JMenuItem("Close this tab");
    private FileChooser fileChooser = null;
    private FileChooser htmlChooser = null;
    private Viewer user,  sys;
    protected List<Viewer> viewers = new ArrayList<Viewer>();
    static Search search;
    static Preferences prefs = Preferences.userNodeForPackage(Main.class);
    static Logger logger = Logger.getLogger("global");

    public Main()
      {
        // UI preferences
        int lx = 300, ly = 300, x0 = 0, y0 = 0;
        String geom = Main.prefs.get("main", null);
        if (geom != null) {
            try {
                String[] wh = geom.split(",");
                x0 = Integer.parseInt(wh[0]);
                y0 = Integer.parseInt(wh[1]);
                lx = Integer.parseInt(wh[2]);
                ly = Integer.parseInt(wh[3]);
            } catch (Exception ex) {
                // ignore
            }
        }
        int div1 = prefs.getInt("div.user", 100);
        int div2 = prefs.getInt("div.sys", 100);
        //
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setJMenuBar(mainMenu);
        setTitle("Java Preferences Tool, version " + version);
        //
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 4, 0, 4));
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        setContentPane(contentPane);
        //
        status.setBorder(new EmptyBorder(3, 3, 3, 3));
        statusPanel.add(status, BorderLayout.WEST);
        //
        mainMenu.add(fileMenu);
        fillMenu(fileMenu, fileNewAction, fileOpenAction, fileSaveAction,
            fileSaveAsAction, filePrintAction, null, fileExitAction);
        mainMenu.add(editMenu);
        fillMenu(editMenu, keyAddAction, nodeClearAction);
        mainMenu.add(viewMenu);
        fillMenu(viewMenu, viewExpandAction, viewCollapseAction,
            viewRefreshAction, null);
        viewMenu.add(new LnFMenu(this, "Look and Feel"));
        mainMenu.add(helpMenu);
        fillMenu(helpMenu, helpAction, aboutAction);
        //
        toolBar.add(fileOpenAction);
        toolBar.add(fileSaveAction);
        toolBar.addSeparator();
        toolBar.add(editCutAction);
        toolBar.add(editCopyAction);
        toolBar.add(editPasteAction);
        toolBar.addSeparator();
        toolBar.add(viewExpandAction);
        toolBar.add(viewCollapseAction);
        toolBar.add(keyAddAction);
        toolBar.add(editFindAction);
        // looks like java 5 makes big insets around toolbar icons:
        for (Component c : toolBar.getComponents()) {
            if (c instanceof JButton) {
                JButton but = (JButton) c;
                but.setMargin(new Insets(2, 2, 2, 2));
            }
        }
        search = new Search(this);
        Compare.create(statusPanel);
        //
        fileOpenAction.putValue(Action.SHORT_DESCRIPTION, "Load saved preferences");
        fileSaveAction.putValue(Action.SHORT_DESCRIPTION, "Save preferences to a file");
        editCutAction.putValue(Action.SHORT_DESCRIPTION, "Cut subtree of selected node");
        editCopyAction.putValue(Action.SHORT_DESCRIPTION, "Copy subtree of selected node");
        editPasteAction.putValue(Action.SHORT_DESCRIPTION,
            "Paste subtree as a child to selected node");
        viewExpandAction.putValue(Action.SHORT_DESCRIPTION, "Expand all or selected node");
        viewCollapseAction.putValue(Action.SHORT_DESCRIPTION, "Collapse all or selected node");
        keyAddAction.putValue(Action.SHORT_DESCRIPTION, "Add new key/value pair");
        editFindAction.putValue(Action.SHORT_DESCRIPTION, "Search...");
        //
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
              {
                if (mayExit(null)) {
                    savePrefs();
                    search.dispose();
                    dispose();
                }
              }

        });
        try {
            user = new Viewer(this, Preferences.userRoot(), div1);
            sys = new Viewer(this, Preferences.systemRoot(), div2);
        } catch (BackingStoreException ex) {
            showError(ex.getMessage());
            return;
        }
        tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent ev)
              {
                search.setVisible(false);
                checkActions();
              }

        });
        viewers.add(user);
        tabbedPane.add("User", user);
        tabbedPane.setToolTipTextAt(0, "The User preferences");
        viewers.add(sys);
        tabbedPane.add("System", sys);
        tabbedPane.setToolTipTextAt(1, "The System preferences");
        for (int count = 0; count < 100; count++) {
            String str = prefs.get("file." + count, null);
            if (str == null) {
                break;
            }
            String[] w = str.split(";");
            if (w.length != 2) {
                continue;
            }
            int divloc = -1;
            try {
                divloc = Integer.parseInt(w[0]);
            } catch (NumberFormatException ex) {
                // well, ignore
            }
            File file = new File(w[1].trim());
            if (file.exists()) {
                addTab(file, divloc);
            }
        }
        tabPopup.add(tabCloseItem);
        tabbedPane.setComponentPopupMenu(tabPopup);
        tabCloseItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
              {
                Viewer v = currViewer();
                if (mayExit(v)) {
                    tabbedPane.remove(v);
                    viewers.remove(v);
                }
              }

        });
        pack();
        setSize(lx, ly);
        setLocation(x0, y0);
        setIconImage(appIcon.getImage());
        //
        fileChooser = new FileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File f)
              {
                return f.isDirectory() || f.getName().endsWith(fileExt);
              }

            public String getDescription()
              {
                return "Java Preferences files";
              }

        });
        //
        htmlChooser = new FileChooser();
        htmlChooser.setMultiSelectionEnabled(false);
        htmlChooser.setAcceptAllFileFilterUsed(false);
        htmlChooser.setFileFilter(new FileFilter() {

            public boolean accept(File f)
              {
                return f.isDirectory() || f.getName().endsWith(".html");
              }

            public String getDescription()
              {
                return "HTML files";
              }

        });
        checkActions();
        //
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev)
              {
                lafChanged();
              }

        });
      }

    protected void lafChanged()
      {
        SwingUtilities.updateComponentTreeUI(this);
        validate();
        SwingUtilities.updateComponentTreeUI(search);
        search.validate();
      }

    void checkActions()
      {
        Viewer v = currViewer();
        boolean hasNode = v != null && v.selectedNode() != null;
        boolean hasNonRoot = v != null && hasNode && !v.selectedNode().isRoot();
        fileSaveAction.setEnabled(v != null && !v.isReal() && v.isDirty());
        editCopyAction.setEnabled(hasNode);
        editCutAction.setEnabled(hasNonRoot);
        editPasteAction.setEnabled(hasNode && Viewer.hasCopyNode());
        editMergeAction.setEnabled(hasNode && Viewer.hasCopyNode());
        editDelAction.setEnabled(hasNonRoot);
        editElimAction.setEnabled(hasNonRoot);
        keyAddAction.setEnabled(hasNode);
        nodeClearAction.setEnabled(hasNode);
        viewRefreshAction.setEnabled(v != null && v.isReal());
        tabCloseItem.setEnabled(v != null && !v.isReal());
      }

    private void fillMenu(JMenu menu, Action... action)
      {
        for (Action act : action) {
            if (act != null) {
                menu.add(act);
            } else {
                menu.addSeparator();
            }
        }
      }

    Viewer currViewer()
      {
        int j = tabbedPane.getSelectedIndex();
        return j < 0 ? null : viewers.get(j);
      }

    protected void fileNew()
      {
        addTab(null, -1);
      }

    protected void fileOpen()
      {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            File file = fileChooser.getSelectedFile().getCanonicalFile();
            if (findViewer(file) != null) {
                showError("This file is already loaded.");
                return;
            }
            addTab(file, -1);
            showStatus("Loaded file: " + file.getPath());
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
      }

    protected void fileSave()
      {
        Viewer v = currViewer();
        if (v.getFile() == null) {
            fileSaveAs();
        } else {
            v.save(null);
        }
      }

    protected void fileSaveAs()
      {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(fileExt)) {
            file = new File(file.getPath() + fileExt);
        }
        if (findViewer(file) != null) {
            showError("This file is curently opened");
            return;
        }
        Viewer v = currViewer();
        v.save(file);
        if (!v.isReal()) {
            nameTab(v);
        }
        showStatus("Saved file: " + file.getPath());
      }

    protected void filePrint(TNode node)
      {
        if (htmlChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = htmlChooser.getSelectedFile();
        if (!file.getName().endsWith(".html")) {
            file = new File(file.getPath() + ".html");
        }
        try {
            PrintWriter wr = new PrintWriter(file, "utf-8");
            currViewer().print(wr, node);
            wr.close();
            showStatus("Export successfull");
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
      }

    private Viewer findViewer(File file)
      {
        for (Viewer v : viewers) {
            File opened = v.getFile();
            if (opened != null && opened.equals(file)) {
                return v;
            }
        }
        return null;
      }

    protected void editFind(boolean start)
      {
        if (start) {
            currViewer().startSearch();
        }
      }

    protected void viewExpand()
      {
        currViewer().expandAll(true);
      }

    protected void viewCollapse()
      {
        currViewer().expandAll(false);
      }

    protected void viewRefresh()
      {
        currViewer().refresh();
      }

    private void showError(String msg)
      {
        JOptionPane.showMessageDialog(this, msg, "Error",
            JOptionPane.ERROR_MESSAGE);
      }

    private void addTab(File file, int divloc)
      {
        try {
            if (divloc < 0) {
                divloc = viewers.get(0).getDividerLocation();
            }
            Viewer v = new Viewer(this, file, divloc);
            viewers.add(v);
            tabbedPane.add(v);
            nameTab(v);
            tabbedPane.setSelectedComponent(v);
        } catch (SAXException ex) {
            showError(ex.getMessage());
        } catch (IOException ex) {
            showError(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            showError(ex.getMessage());
        } catch (BackingStoreException ex) {
            showError(ex.getMessage());
        }
      }

    private static int tally = 1;

    private void nameTab(Viewer v)
      {
        File file = v.getFile();
        String tabName = file == null ? "temp#" + (tally++) : file.getName();
        int j = tabName.lastIndexOf('.');
        if (j > 0) {
            tabName = tabName.substring(0, j);
        }
        int n = tabbedPane.indexOfComponent(v);
        tabbedPane.setTitleAt(n, tabName);
        tabbedPane.setToolTipTextAt(n, file == null ? "(no file)" : file.getAbsolutePath());
      }

    protected void savePrefs()
      {
        try {
            if (!prefs.nodeExists("")) {
                showError("Preference node " + prefs.absolutePath() +
                    "no longer exists. Will try to recreate.");
                prefs = Preferences.userNodeForPackage(Main.class);
            }
            prefs.put("main", String.format("%d,%d,%d,%d", getLocation().x,
                getLocation().y, getWidth(), getHeight()));
            prefs.putInt("div.user", user.getDividerLocation());
            prefs.putInt("div.sys", sys.getDividerLocation());
            int count = 0;
            while (count < 100) {
                String key = "file." + (count++);
                if (prefs.get(key, null) == null) {
                    break;
                }
                prefs.remove(key);
            }
            count = 0;
            for (Viewer v : viewers) {
                if (v.getFile() != null) {
                    prefs.put("file." + (count++), String.format("%d;%s",
                        v.getDividerLocation(), v.getFile().getAbsolutePath()));
                }
            }
            LnFMenu.savePrefs(prefs.node("lookAndFeel"));
        } catch (BackingStoreException ex) {
            showError("Error committing changes:\n" + ex.getMessage());
        }
      }

    protected boolean mayExit(Viewer curr)
      {
        boolean dirty = false;
        List<Viewer> tabs = viewers;
        if (curr != null) {
            tabs = new ArrayList<Viewer>(1);
            tabs.add(curr);
        }
        for (Viewer v : viewers) {
            if (v.isDirty()) {
                dirty = true;
            }
        }
        if (!dirty) {
            return true;
        }
        int ans = JOptionPane.showConfirmDialog(this,
            "There are unsaved changed. Save them?", "Confirm",
            JOptionPane.YES_NO_CANCEL_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            for (Viewer v : viewers) {
                if (v.isDirty()) {
                    v.save(null);
                }
            }
            return true;
        }
        return ans == JOptionPane.NO_OPTION;
      }

    protected void showAbout()
      {
        new About(this).setVisible(true);
      }

    void showStatus(String text)
      {
        status.setText(text);
      }

    public static void main(String[] args)
      {
        LnFMenu.loadPrefs(prefs.node("lookAndFeel"));
        if (prefs.get("version", null) == null) {
            BetaDlg dlg = new BetaDlg(null);
            dlg.setVisible(true);
            if (!dlg.agreed) {
                System.exit(0);
            }
            prefs.put("version", "1");
        }
        setUpLogging();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable ex)
              {
                dumpEx(ex);
              }

        });
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run()
              {
                new Main().setVisible(true);
              }

        });
      }

    static void dumpEx(Throwable ex)
      {
        try {
            File dumpFile = new File(System.getProperty("user.home"), "javaprefs.log");
            PrintWriter wr = new PrintWriter(dumpFile);
            wr.write(String.format("javaPrefs version: %s\n", version));
            ex.printStackTrace(wr);
            System.getProperties().list(wr);
            wr.close();
            JOptionPane.showMessageDialog(null,
                "<html><b>Oops! Didn't I tell you to expect bugs?</b>\n\n" +
                "An exception occured: " + ex.getMessage() +
                "\nThe debugging info is saved in\n" + dumpFile.getPath() +
                "\nPlease send it to javaprefs@gmail.com");
        } catch (IOException io) {
            // 
        }
        System.exit(1);
      }

    /**
     * Sets up java logger.
     * By default logging is off, to turn it on, run JVM with: <br>
     * <code> -Dlogging=fine,/tmp/poll.log </code>
     * substituting desired log level and optional output file name.
     * If not specified, log goes to standard output.
     */
    private static void setUpLogging()
      {
        String arg = System.getProperty("logging");
        String file = null;
        Level level = Level.OFF;
        String[] names = {
            "off", "finest", "finer", "fine", "config", "info", "warning",
            "severe", "all"
        };
        Level[] levels = {
            Level.OFF, Level.FINEST, Level.FINER, Level.FINE,
            Level.CONFIG, Level.INFO, Level.WARNING, Level.SEVERE, Level.ALL
        };
        String lvl = arg;
        if (arg != null) {
            int j = arg.indexOf(',');
            if (j > 0) {
                lvl = arg.substring(0, j);
                if (++j < arg.length()) {
                    file = arg.substring(j);
                }
            }
        }
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(lvl)) {
                level = levels[i];
                lvl = "";
                break;
            }
        }
        if (lvl != null && !lvl.equals("")) {
            System.err.println("Wrong logging level in " + arg + "\nuse one of: ");
            System.err.println(Arrays.toString(names));
        }
        logger.setLevel(level);
        try {
            Handler handler;
            if (file != null) {
                handler = new FileHandler(file);
                handler.setFormatter(new SimpleFormatter());
            } else {
                handler = new ConsoleHandler();
            }
            logger.addHandler(handler);
            handler.setLevel(level);
        } catch (IOException ex) {
            System.err.println("logging inoperative: " + ex.getMessage());
        }
      }



public static class BlankIcon implements Icon {

    public int getIconHeight()
      {
        return 16;
      }

    public int getIconWidth()
      {
        return 16;
      }

    public void paintIcon(Component c, Graphics g, int x, int y)
      {
        // leave blank
      }

}

@SuppressWarnings("serial")
public static class FileChooser extends JFileChooser {

    public void approveSelection()
      {
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
}
