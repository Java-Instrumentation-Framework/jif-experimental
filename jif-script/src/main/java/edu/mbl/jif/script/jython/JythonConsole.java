package edu.mbl.jif.script.jython;

/*
 * Jython Console
 * Copyright (C) 2004 Benjamin Jung <bpjung@terreon.de>
 * See http://www.phreakzone.com/jython/ for further details.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Modified and enhanced by Grant Harris and Barry Mather, at edu.mbl, Mar 2005
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
//
import org.python.util.*;
import org.python.core.*;

// jif.CamAcq...
//import edu.mbl.jif.camera.camacq.CamAcq;
// ImageJ...
import ij.WindowManager;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.Properties;
import java.util.Enumeration;


/**
 * This simple Jython Console offers the basic functionality that you would
 * expect of Jython running in interactive mode. This class was tested against
 * Jython 2.1 ... it might work under 2.0 ... it might not!
 * @author Benjamin P. Jung
 * @version 1.6, 06/24/04
 * @see org.python.util.InteractiveConsole
 *  ---
 * Additions/Modifications by Grant Harris, gharris@mbl.edu April 2005
 * Incorporated code from jEdit.JythonInterpreter.JythonExecutor to
 * allow use of jythonlib.jar
 * and other path-related things...
 *
 */
public class JythonConsole
      extends JFrame implements ActionListener
{

   /** The default background color */
   public final static Color BACKGROUND = Color.WHITE;
   /** The default foreground color */
   public final static Color FOREGROUND = Color.BLACK;

   public final static int STYLE_NONE = -1;
   public final static int STYLE_IN = 0;
   public final static int STYLE_OUT = 1;
   public final static int STYLE_ERR = 2;

   private final static String TITLE = "Jungian Jython Console";
   private final static String VERSION = "1.4";
   private final static String AUTHOR = "Benjamin P. Jung";
   private final static String HOMEPAGE = "http://www.phreakzone.com/jython/";

   private final static String ACTION_ENTER = "Enter";
   private final static String ACTION_CLEAR = "Clear";

   protected JPanel contentPanel;
   protected OutputPanel output;
   protected JTextField input;
   protected JButton enterButton;

   private final static int HISTORY_MAX_SIZE = 100;
   private LinkedList history;
   private int history_pos = 0;
   private File lastScriptRun;
   ReRunScriptAction reRunAction;

   protected InteractiveConsole console;
   //PythonInterpreter _interpreter = new PythonInterpreter();

   private boolean initialised;
   private boolean useInternalLib = false;
   //private PySystemState sys = null;
   private PyStringMap originalLocals = null;

   private final static String SCRIPTS_PATH =
         //System.getProperty("user.dir") + "\\scripts";
         "..\\IJ134a\\scripts";
   /**
    * Creates a new Jython Console.
    */
   public JythonConsole () {
      System.out.println("");
      history = new LinkedList();
      // Initializes the GUI components.
      initComponents();
   }


   /**
    * Initializes this form.
    */
   private void initComponents () {
      // Sets the title for this frame.
      setTitle(TITLE);
      // Tries to set the icon for this frame.
      try {
         setIconImage(new ImageIcon(this.getClass().getResource(
               "icons/mandala16.gif")).getImage());
      }
      catch (Exception ex) {

      }

      // Sets the default close operation for this frame.
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      // Adds a menu bar to the frame.
      setJMenuBar(new ConsoleMenuBar());
      reRunAction.setEnabled(lastScriptRun != null);
      // Instantiates the actual content panel.
      contentPanel = new JPanel();
      // Sets the layout of the content panel.
      contentPanel.setLayout(new GridBagLayout());
      // Adds the content panel to the frame.
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      // Creates constraints for the grid bag layout.
      GridBagConstraints constraints = new GridBagConstraints(
            0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(4, 4, 2, 4),
            4, 4
                                       );
      // Instantiates the output panel.
      output = new OutputPanel();
      // Modifies the constraints to fit the output panel's needs.
      constraints.gridwidth = 3;
      // Adds the output panel onto the content panel.
      contentPanel.add(output, constraints);
      // Creates a nice Python label (kinda ">>>" replacement).
      JLabel promptLabel = new JLabel();
      // Loads a nice icon onto the prompt label.
//      imgURL = JythonConsole.class.getResource("icon16.png");
//      if (imgURL != null) {
//         promptLabel.setIcon(new ImageIcon(imgURL));
//         promptLabel.setToolTipText("Jython rocks!!!");
//      }
//      else {
      // Sets the text for the prompt label if the icon failed to load.
      promptLabel.setText(">>>");
      //}
      // Modifies the constraints to fit the prompt label's needs.
      constraints.gridx = 0;
      constraints.gridy = 1;
      constraints.weightx = 0.0;
      constraints.weighty = 0.0;
      constraints.ipadx = 0;
      constraints.ipady = 0;
      constraints.gridwidth = 1;
      constraints.insets = new Insets(4, 4, 4, 4);
      // Adds the prompt label onto the content panel.
      contentPanel.add(promptLabel, constraints);
      // Instantiates the input panel.
      input = new JTextField();
      // Sets the foreground color of the text pane.
      input.setForeground(FOREGROUND);
      // Sets the background color of the text pane.
      input.setBackground(BACKGROUND);
      // Sets the border of the text pane.
      input.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
      // Modifies the constraints to fit the input panel's needs.
      constraints.gridx = 1;
      constraints.weightx = 1.0;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.ipadx = 4;
      constraints.ipady = 4;
      constraints.gridwidth = 1;
      constraints.insets = new Insets(2, 2, 4, 2);
      // Sets the action command for the input text field.
      input.setActionCommand(ACTION_ENTER);
      // Adds the action listener to the input panel.
      input.addActionListener(this);
      // Adds a (new) key listener to the input text field.
      input.addKeyListener(new KeyAdapter()
      {
         /* (non-Javadoc)
          * @see KeyListener#keyPressed(KeyEvent)
          */
         public void keyPressed (KeyEvent e) {
            int c = e.getKeyCode();
            if (c == KeyEvent.VK_UP || c == KeyEvent.VK_KP_UP) {
               String s = getHistory();
               if (s != null) {
                  input.setText(s);
               }
               --history_pos;
            } else if (c == KeyEvent.VK_DOWN || c == KeyEvent.VK_KP_DOWN) {
               ++history_pos;
               String s = getHistory();
               if (s != null) {
                  input.setText(s);
               }
            }
         }
      });
      // Adds the input text field onto the content panel.
      contentPanel.add(input, constraints);
      // Instantiates the enter button.
      enterButton = new JButton();
      // Sets the icon for the button.
      try {
         enterButton.setIcon(new ImageIcon(JythonConsole.class.getResource(
               "icons/Go24.gif")));
      }
      catch (Exception ex) {}
      // Sets the tool tip text for the button.
      enterButton.setToolTipText("Execute");
      enterButton.setPreferredSize(new Dimension(22, 22));
      enterButton.setMinimumSize(new Dimension(22, 22));
      enterButton.setMaximumSize(new Dimension(22, 22));
      enterButton.setActionCommand(ACTION_ENTER);
      enterButton.addActionListener(this);
      // Modifies the constraints to fit the button's needs.
      constraints.gridx = 2;
      constraints.fill = GridBagConstraints.NONE;
      constraints.weightx = 0.0;
      constraints.insets = new Insets(2, 2, 4, 4);
      // Adds the "enter" buttons onto the content panel.
      contentPanel.add(enterButton, constraints);

      //----------------------------------------------------
      // Instantiates the Jython Interactive Console.
      // * PySystemState.initialize();
      // * console = new InteractiveConsole();
      console = getInterpreter();
      // *** setupNamespace(console);
      // Instantiates the console (out) writer.
      ConsoleOutWriter outWriter = new ConsoleOutWriter();
      // Sets stdout of the Jython interactive console.
      console.setOut(outWriter);
      // Instantiates the console (err) writer.
      ConsoleErrorWriter errWriter = new ConsoleErrorWriter();
      // Sets stderr of the Jython interactive console.
      console.setErr(errWriter);
      // Clears the output text pane and shows a nice and copyright notice.
      output.clear(
            // .
            TITLE + " " + VERSION + "\n" +
            "Copyright (C) 2004 " + AUTHOR + "\n\n"
            );
      // Sets the initial size of the dialog.
      setSize(new Dimension(600, 600));

      // Test adding jar...
//      String path = new File(getPythonHomePath(), "_ijPlugins.jar").toString();
//      addJarPath(path, "/Camera");
   }


   String getPythonHomePath () {
      Properties sysProps = System.getProperties();
      String pyHome = sysProps.getProperty("python.home");
      System.out.println("python.home = " + pyHome);
      return pyHome;

   }


   public void addJarPath (String jarPath, String pkg) {
      // path to jar + "/packageName"
      PySystemState sys = Py.getSystemState();
      sys.packageManager.addJar(jarPath, true);
      sys.path.insert(0, new PyString(jarPath + "/" + pkg));
   }


   void setupNamespace (PythonInterpreter interp) {
      /** @todo hand this a list of string/objects and set them... */
      try {
         // for ImageJ
         // expose ImagePlus for current image window
         System.out.println("setupNamespace");
         ImagePlus imp = WindowManager.getCurrentImage();
         ImageProcessor ip = imp.getProcessor();
         interp.set("imp", imp);
         interp.set("ip", ip);
         interp.set("IJ", new IJ()); // expose IJ utility class
         // for QCamJ
         //interp.set("CamAcq", CamAcq.getInstance());
      }

      catch (Exception e) {}
      ;
      // create Editor object for easy script access to this class!
      interp.set("Editor", this);
   }


   protected synchronized InteractiveConsole getInterpreter () {
      InteractiveConsole interpreter = null;
      Properties sysProps = System.getProperties();
      if (interpreter == null) {
         // verify if it has been ever initialised
         if (!initialised) {
            // set any custom user properties
            Properties postProps = new Properties();
            // put System properties (those set with -D) in postProps
            Enumeration e = sysProps.propertyNames();
            while (e.hasMoreElements()) {
               String name = (String) e.nextElement();
               if (name.startsWith("python.")) {
                  postProps.put(name, System.getProperty(name));
               }
            }

//          String key = null;
//          String value = null;
//          postProps.setProperty(key, value);

            // try to detect if jython is available in the classpath,
            // if not use bundled

//            String classpath = sysProps.getProperty("java.class.path");
//            if (classpath != null) {
//               // check if jython is in the classpath
//               int jpy = classpath.toLowerCase().indexOf("jython.jar");
//               if (jpy == -1) {
//                  useInternalLib = true;
//               }
//            }
            // Modify cachedir property if has not been set before
            if (useInternalLib && !postProps.containsKey("python.cachedir")) {
               postProps.setProperty("python.cachedir", "cachedir");
            }
            try {
               // *** Initialize with no args *****************************
               InteractiveInterpreter.initialize(
                     sysProps, postProps, new String[] {""});
            }
            catch (Exception e2) {
               System.err.println("Error initializing InteractiveInterpreter: "
                                  + e2.getMessage());
            }
            finally {
               initialised = true;
            }
         }

         // adds __main__ module
         PyModule mod = imp.addModule("__main__");

         // if using internal lib add the appropriate jars to sys.path
         PySystemState sys = Py.getSystemState();

         // deal with locals...
//         if (originalLocals == null) {
//            originalLocals = ((PyStringMap) mod.__dict__);
//            originalLocals = originalLocals.copy();
//         }

         // create the InteractiveInterpreter
         try {
            if (originalLocals != null && sys != null) {
               interpreter = new InteractiveConsole(originalLocals);
            } else {
               interpreter = new InteractiveConsole();
            }
         }
         catch (Exception e) {
            System.err.println("Not possible to build interpreter");
            e.printStackTrace();
            return null;
         }
         useInternalLib = true;
         if (useInternalLib) {
            // python.home
            if (sysProps.getProperty("python.home") == null) {
               sysProps.put("python.home", ".");
            }
            String pyHome = sysProps.getProperty("python.home");
            System.out.println("python.home = " + pyHome);

            // jythonlib.jar
            String path = new File(pyHome, "jythonlib.jar").toString();
            sys.packageManager.addJar(path, true);
            // Note !!!
            sys.path.insert(0, new PyString(path + "/Lib"));
            System.out.println("sys.packageManager.addJar,jythonlibpath=" +
                               path);

            // jython.jar
//            path = new File(pyHome, "jython.jar").toString();
//            sys.packageManager.addJar(path, true);
//            System.out.println("sys.packageManager.addJar,jythonpath=" + path);
         }

         interpreter.runsource("import sys");
         /** @todo add os, ..., java, ij, jif ... */
         //interpreter.runsource("from _ import _");
//         PySystemState sys = Py.getSystemState();
//         sys.add_package("javax.servlet");
//         sys.add_classdir(rootPath + "WEB-INF" + File.separator + "classes");
//         sys.add_extdir(rootPath + "WEB-INF" + File.separator + "lib", true);

      }
      return interpreter;
   }


   /*
         public void reset() {
        destroyCache();
        interp = new PythonInterpreter(null, new PySystemState());
        cache.clear();
        PySystemState sys = Py.getSystemState();
        sys.path.append(new PyString(rootPath));

        String modulesDir = rootPath + "WEB-INF" +
                            File.separator + "jython";
        sys.path.append(new PyString(modulesDir));
    }
    */


//
//---------------------------------------------------------------------------


   /**
    * Clears the history of the Jython Conosole.
    */
   public void clearHistory () {
      history.clear();
      history_pos = 0;
   }


   /**
    * Appends the given <code>String</code> object to the history.
    * @param command   the command to append
    */
   private void appendToHistory (String command) {
      history.addLast(command);
      if (history.size() >= HISTORY_MAX_SIZE) {
         history.removeFirst();
      }
   }


   /**
    * Returns the command that was stored in the history on position n.
    * @return  the command (a <code>String</code> object) at the nth position
    */
   private String getHistory () {
      if (history.size() == 0) {
         return null;
      }
      if (history_pos >= history.size()) {
         history_pos = 0;
      } else if (history_pos < 0) {
         history_pos = history.size() - 1;
      }
      return (String) history.get(history_pos);
   }


   /**
    * Resets the current position (index) in history.
    */
   private void resetHistoryPosition () {
      history_pos = history.size();
   }


   /* (non-Javadoc)
    * @see ActionListener#actionPerformed(ActionEvent)
    */
   public void actionPerformed (ActionEvent e) {
      String ac = e.getActionCommand();
      if (ac.equals(ACTION_ENTER)) {
         resetHistoryPosition();
         String command = input.getText();
         appendToHistory(command);
         output.append(command + "\n", STYLE_IN);
         // Hands the input text to the Python interpreter.
         console.push(command + "\n");
         output.append("\n", STYLE_NONE);
         // Clears the input text pane.
         input.setText("");
         // (Re-)requests focus for the input text pane.
         input.requestFocus();
      } else if (ac.equals(ACTION_CLEAR)) {
         resetHistoryPosition();
         output.clear();
      }
   }


   /**
    * @author Benjamin P. Jung
    * @version 1.2, 05/19/04
    * @since JythonConsole 1.0
    */
   private class ConsoleOutWriter
         extends Writer
   {
      /* (non-Javadoc)
       * @see java.io.Writer#close()
       */
      public void close () throws IOException { /* ... */}


      /* (non-Javadoc)
       * @see java.io.Writer#flush()
       */
      public void flush () throws IOException { /* ... */}


      /* (non-Javadoc)
       * @see java.io.Writer#write(char[], int, int)
       */
      public void write (char[] cbuf, int off, int len) throws IOException {
         output.append(new String(cbuf, off, len), STYLE_OUT);
      }
   }



   /**
    * @author Benjamin P. Jung
    * @version 1.1, 05/19/04
    * @since JythonConsole 1.0
    */
   private class ConsoleErrorWriter
         extends Writer
   {
      /* (non-Javadoc)
       * @see java.io.Writer#close()
       */
      public void close () throws IOException { /* ... */}


      /* (non-Javadoc)
       * @see java.io.Writer#flush()
       */
      public void flush () throws IOException { /* ... */}


      /* (non-Javadoc)
       * @see java.io.Writer#write(char[], int, int)
       */
      public void write (char[] cbuf, int off, int len) throws IOException {
         output.append(new String(cbuf, off, len), STYLE_ERR);
      }
   }



   /**
    * The <tt>OutputPanel</tt> is nothing more than an extended
    * <tt>JScrollPane</tt> that takes hold of a very ordinary
    * <tt>JTextPane</tt>.
    * @author	Benjamin P. Jung
    * @version	1.2, 05/19/04
    * @since	JythonConsole 1.0
    */
   private class OutputPanel
         extends JScrollPane
   {
      // The text pane for the output text to be displayed.
      private JTextPane outputText;
      private Style errorStyle;
      private Style outputStyle;
      private Style inputStyle;
      /**
       * Creates a new <tt>OutputPanel</tt>
       */
      private OutputPanel () {
         outputText = new JTextPane();
         // Sets the output text pane to be non-editable.
         outputText.setEditable(false);
         outputText.setForeground(FOREGROUND);
         outputText.setBackground(BACKGROUND);
         setViewportView(outputText);
         setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
         // Creates the error style for the styled text.
         errorStyle = outputText.addStyle("error_style", null);
         StyleConstants.setForeground(errorStyle, Color.RED);
         // Creates the output style for the styled text.
         outputStyle = outputText.addStyle("output_style", null);
         // Creates the input style for the styled text.
         inputStyle = outputText.addStyle("input_style", null);
         StyleConstants.setForeground(inputStyle, Color.BLUE);
         StyleConstants.setBold(inputStyle, true);
      }


      /**
       * Clears the text pane.
       */
      public void clear () {
         outputText.setText("");
      }


      /**
       * Clears the text pane and displays a new text (such as a copyright
       * notice).
       * @param text  the text to be displayed after clearing the output pane
       */
      public void clear (String text) {
         outputText.setText(text);
      }


      /**
       * Appends a <code>String</code> to the output text pane's document.
       * @param	string the <code>String</code> to be appended
       * @param	style
       */
      public void append (String string, int style) {
         Document doc = outputText.getDocument();
         Style textStyle = null;
         switch (style) {
            case (STYLE_ERR):
               textStyle = errorStyle;
               break;
            case (STYLE_OUT):
               textStyle = outputStyle;
               break;
            case (STYLE_IN):
               textStyle = inputStyle;
               break;
            default:
               break;
         }
         try {
            // Inserts the string.
            doc.insertString(doc.getLength(), string, textStyle);
         }
         catch (BadLocationException e) { /* ... */}
         // Sets the caret / scrolls to the bottom of the text pane.
         outputText.setCaretPosition(doc.getLength());
      }


      /**
       * Returns the <code>Document</code> of the output's
       * <code>JTextPane</code>.
       * @return	the <code>Document</code> of the output's
       *          <code>JTextPane</code>
       */
      public Document getDocument () {
         return outputText.getDocument();
      }
   }



   /**
    * The <code>ConsoleMenuBar</code> is an extension of the
    * <code>JMenuBar</code> class.
    * It should fit the needs of the Jython Console quite well.
    * @author	Benjamin P. Jung
    * @version	1.1, 06/22/04
    * @since JythonConsole 1.3
    */
   private class ConsoleMenuBar
         extends JMenuBar
   {
      private JMenu fileMenu;
      private JMenu editMenu;
      private JMenu viewMenu;
      private JMenu helpMenu;
      private JMenuItem clearMenuItem;
      /**
       * Creates a new <code>ConsoleMenuBar</code>.
       */
      private ConsoleMenuBar () {
         // Instantiates the file menu.
         fileMenu = new JMenu("File");
         fileMenu.setMnemonic(KeyEvent.VK_F);
         fileMenu.add(new LoadScriptAction());
         reRunAction = new ReRunScriptAction();
         fileMenu.add(reRunAction);
         fileMenu.add(new JSeparator(JSeparator.HORIZONTAL));
         fileMenu.add(new ExitAction());
         add(fileMenu);
         editMenu = new JMenu("Edit");
         editMenu.setMnemonic(KeyEvent.VK_E);
         clearMenuItem = new JMenuItem("Clear Output");
         clearMenuItem.setActionCommand(ACTION_CLEAR);
         clearMenuItem.addActionListener(JythonConsole.this);
         editMenu.add(clearMenuItem);
         add(editMenu);
         viewMenu = new JMenu("View");
         viewMenu.setMnemonic(KeyEvent.VK_V);
         viewMenu.add(new ViewHistoryAction());
         add(viewMenu);
         helpMenu = new JMenu("Help");
         helpMenu.setMnemonic(KeyEvent.VK_H);
         helpMenu.add(new AboutAction());
         add(helpMenu);
      }
   }



   /**
    * extension of the JFileChooser class that has a
    * default filter which allows only python scripts (that end with .py) to
    * be opened.
    */
   private class PythonFileChooser
         extends JFileChooser
   {
      /**
       * Defines a new <code>PythonFileChooser</code>.
       */
      private PythonFileChooser () {
         // Sets the file filter for this file chooser.
         setFileFilter(new FileFilter()
         {
            /* (non-Javadoc)
             * @see FileFilter#accept(File)
             */
            public boolean accept (File f) {
               if (f.getName().endsWith(".py") || f.isDirectory()) {
                  return true;
               }
               return false;
            }


            /* (non-Javadoc)
             * @see FileFilter#getDescription()
             */
            public String getDescription () {
               return "Python Scripts (*.py)";
            }
         });
         setAcceptAllFileFilterUsed(false);
         setFileSelectionMode(JFileChooser.FILES_ONLY);
      }
   }



   /**
    * The <code>LoadScriptAction</code> prompts the user of the console with
    * a modified <code>JFileChooser</code> dialog that lets us select a
    * Python(.py) file. The file will be loaded (line by line) by the
    * Console and thus executed.
    * @author Benjamin P. Jung
    * @version 1.0, 06/22/04
    * @since JythonConsole 1.4
    */
   private class LoadScriptAction
         extends AbstractAction
   {
      /**
       * Defines a new <code>LoadScriptAction</code>.
       */
      private LoadScriptAction () {
         putValue(NAME, "Run Script ...");
         putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
      }


      /* (non-Javadoc)
       * @see ActionListener#actionPerformed(ActionEvent)
       */
      public void actionPerformed (ActionEvent e) {
         JFileChooser fileChooser = new PythonFileChooser();
         fileChooser.setCurrentDirectory(new File(SCRIPTS_PATH));
         int retval = fileChooser.showOpenDialog(JythonConsole.this);
         if (retval == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            lastScriptRun = f;
            reRunAction.setEnabled(lastScriptRun != null);
            FileInputStream in = null;
            String line = null;
            output.append("Loading script: ", STYLE_IN);
            output.append(f.getAbsolutePath() + "\n", STYLE_IN);
            // (Re-)requests focus for the input text pane.
            input.requestFocus();
            try {
               in = new FileInputStream(f);
            }
            catch (FileNotFoundException e1) {
               output.append("file not found!", STYLE_ERR);
               return;
            }
            executeScriptFile(f.getAbsolutePath());
            // console.execfile(in);
         }
      }
   }



   private class ReRunScriptAction
         extends AbstractAction
   {

      private ReRunScriptAction () {
         putValue(NAME, "Re-Run Script");
         putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));

      }


      public void actionPerformed (ActionEvent e) {
         if (lastScriptRun == null) {
            return;
         }
         File f = lastScriptRun;
         FileInputStream in = null;
         String line = null;
         output.append("Running script: ", STYLE_IN);
         output.append(lastScriptRun + "\n", STYLE_IN);
         // (Re-)requests focus for the input text pane.
         input.requestFocus();
         try {
            in = new FileInputStream(f);
         }
         catch (FileNotFoundException e1) {
            output.append("file not found!", STYLE_ERR);
            return;
         }
         executeScriptFile(f.getAbsolutePath());
         // console.execfile(in);
      }
   }



   public void executeScriptFile (String scriptFile) {
      //throws InterpreterDriver.InterpreterException {
      // Executes a .py file on a new thread
      try {
         final String s2 = scriptFile;
         Runnable interpretIt = new Runnable()
         {
            public void run () {
//               PySystemState.initialize(System.getProperties(),null, new String[] {""},this.getClass().getClassLoader());

               PyModule mod = imp.addModule("__main__");
               console.setLocals(mod.__dict__);
               try {
                  console.execfile(s2);
               }
               catch (org.python.core.PySyntaxError pse) {
                  pse.printStackTrace();
                  System.out.println("PySyntaxError: " + pse.toString());
                  //JOptionPane.showMessageDialog(s1,pse,"Error in script " + s2,JOptionPane.ERROR_MESSAGE);
               }
               catch (org.python.core.PyException pse) {
                  pse.printStackTrace();
                  System.out.println("PyException: " + pse.toString());
                  //JOptionPane.showMessageDialog(s1,pse,"Error in script " + s2,JOptionPane.ERROR_MESSAGE);
               }
               finally {
                  //s1.setMacroRunning(false);
               }
            }

         };

         // lets start interpreting it.
         Thread interpThread = new Thread(interpretIt);
         interpThread.setDaemon(true);
         interpThread.start();

      }
      catch (PyException ex) {
         System.err.println("PyException in executeScriptFile: " +
                            ex.getStackTrace());
         //throw new InterpreterDriver.InterpreterException(ex);
      }
      catch (Exception ex2) {
         System.err.println("Exception in executeScriptFile: " +
                            ex2.getStackTrace());
         //throw new InterpreterDriver.InterpreterException(ex2);
      }
   }


   private class ViewHistoryAction
         extends AbstractAction
   {
      /**
       * Defines a new <code>ViewHistoryAction</code>.
       */
      private ViewHistoryAction () {
         putValue(NAME, "Command History ...");
         putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_H));
      }


      /* (non-Javadoc)
       * @see ActionListener#actionPerformed(ActionEvent)
       */
      public void actionPerformed (ActionEvent e) {
         String[] commands = new String[history.size()];
         for (int i = 0; i < history.size(); ++i) {
            commands[i] = (String) history.get(i);
         }
         final JList list = new JList(commands);
         final JScrollPane scrollPane = new JScrollPane(list);
         final JDialog dialog = new JDialog(JythonConsole.this, true);
         list.addMouseListener(new MouseAdapter()
         {
            /* (non-Javadoc)
             * @see MouseListener#mouseClicked(MouseEvent)
             */
            public void mouseClicked (MouseEvent e) {
               if (e.getClickCount() == 2) {
                  input.setText((String) list.getSelectedValue());
                  resetHistoryPosition();
                  dialog.dispose();
               }
            }
         });
         dialog.setTitle("Command History");
         dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
         if (commands.length > 0) {
            dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
         } else {
            JPanel contentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints(
                  0, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.NONE,
                  new Insets(4, 4, 4, 4), 0, 0
                                   );
            JLabel label = new JLabel(

                  "<html>" +
                  "<h1 align=\"center\">Command History is empty</h1>" +
                  "</html>"
                           );
            contentPanel.add(label, c);
            dialog.getContentPane().add(contentPanel);
         }
         dialog.setSize(400, 200);
         dialog.setLocationRelativeTo(JythonConsole.this);
         dialog.setVisible(true);
      }
   }



   /**
    * The <code>AboutAction</code> class provides a nice way to tell the user
    * about who we are what we want and why we are here ... well:
    * why ARE we here actually ... ?!?!?
    * @author Benjamin P. Jung
    * @version 1.0, 06/22/04
    * @since JythonConsole 1.4
    */
   private class AboutAction
         extends AbstractAction
   {
      /**
       * Defines a new <code>AboutAction</code>.
       */
      private AboutAction () {
         putValue(NAME, "About " + TITLE);
         putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
      }


      /* (non-Javadoc)
       * @see ActionListener#actionPerformed(ActionEvent)
       */
      public void actionPerformed (ActionEvent e) {
         JDialog aboutDialog = new JDialog(JythonConsole.this);
         aboutDialog.setTitle("About " + TITLE);
         aboutDialog.setModal(true);
         aboutDialog.setResizable(false);
         aboutDialog.setDefaultCloseOperation(
               WindowConstants.DISPOSE_ON_CLOSE);
         JPanel contentPanel = new JPanel(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints(
               0, 0, 1, 1, 1.0, 1.0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
               new Insets(4, 4, 4, 4), 0, 0
                                );
         contentPanel.add(new JLabel(
               "<html>" +
               "<h1 align=\"center\">" + TITLE + " " + VERSION + "</h1>" +
               "<p align=\"center\">" +
               "<br>" +
               "<b>Copyright (C) 2004 " + AUTHOR +
               "<br>" +
               HOMEPAGE +
               "</p>" +
               "<br><hr>" +
               "<h2 align=\"center\">License</h2>" +
               "<p align=\"center\">" +
               "This program is free software; you can redistribute it" +
               "<br>" +
               "and/or modify it under the terms of the" +
               "<br>" +
               "<i>GNU Lesser General Public License</i>" +
               "<br>" +
               "as published by the <i>Free Software Foundation</i>;" +
               "<br>" +
               "either version 2 of the License, or (at your option)" +
               "<br>" +
               "any later version." +
               "<br><br>" +
               "This program is distributed in the hope that it" +
               "<br>" +
               "will be useful, but WITHOUT ANY WARRANTY;" +
               "<br>" +
               "without even the implied warranty of MERCHANTABILITY" +
               "<br>" +
               "or FITNESS FOR A PARTICULAR PURPOSE." +
               "<br>" +
               "See the <i>GNU Lesser General Public License</i>" +
               "<br>" +
               "for more details." +
               "<br><br>" +
               "You should have received a copy of the" +
               "<br>" +
               "<i>GNU Lesser General Public License</i>" +
               "<br>" +
               "along with this program;" +
               "<br>" +
               "if not, write to the Free Software Foundation, Inc.," +
               "<br>" +
               "59 Temple Place - Suite 330, Boston," +
               "<br>" +
               "MA  02111-1307, USA." +
               "<br></p>" +
               "</html>"
                          ), c
               );
         aboutDialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
         aboutDialog.pack();
         aboutDialog.setLocationRelativeTo(JythonConsole.this);
         aboutDialog.setVisible(true);
      }
   }



   /**
    * The <code>ExitAction</code> class does a simple System.exit(1).
    * @author Benjamin P. Jung
    * @version 1.0, 06/22/04
    * @since JythonConsole 1.4
    */
   private class ExitAction
         extends AbstractAction
   {
      /**
       * Defines a new <code>ExitAction</code>
       */
      private ExitAction () {
         putValue(NAME, "Exit");
         putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
         putValue(ACCELERATOR_KEY,
                  KeyStroke.getKeyStroke(KeyEvent.VK_F4,
                                         InputEvent.ALT_DOWN_MASK));
      }


      public void actionPerformed (ActionEvent e) {
         // Just exits with a return value of 1.
         System.exit(1);
      }
   }



   /**
    * M A I N   L O O P
    * The main method here is mainly used for testing and demonstration
    * purposes. Usually you surely want to implement the Jython Console in
    * your project and not let it run stand-alone.
    * @param	args the command line arguments
    */
   public final static void main (String args[]) {
      javax.swing.SwingUtilities.invokeLater(new Runnable()
      {
         /* (non-Javadoc)
          * @see java.lang.Runnable#run()
          */
         public void run () {
            /*
             * Instantiates a new Jython Console and shows it.
             * The newly created <code>JythonConsole</code will be centered
             * in the middle of the screen. (setPositionRelativeTo(null)
             */
            JythonConsole console = new JythonConsole();
            console.setLocationRelativeTo(null);
            console.setVisible(true);
         }
      });
   }
}
