package edu.mbl.jif.script.jython;

// * * * * * Jython_Editor 1.0 by Ferdinand Jamitzky * * * * *
//
// 22 October 2002
//
//
// LEGAL
//
// The following code is freely distributable but remains the
// intellectual property of the author and may not be used
// commercially nor in an enterprise setting without prior
// approval. This code is for personal, non-job-related use
// only. You agree to use it at your own risk.
//
//            Copyright 2002 by Ferdinand Jamitzky.
//            Copyright 2002 by Kas Thomas.
//    Commercial or enterprise use prohibited without
//         the express permission of the author.
//
// See also the license terms of Jython (at URL below),
// which apply separately.
//

import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import ij.plugin.frame.*;
import java.awt.event.*;
import org.python.util.PythonInterpreter;
import org.python.core.*;


public class Jython_Editor
      extends PlugInFrame
{

// A lot of our instance variables are public so that they
// can be seen from Jython.

   public static final String pluginPath = Menus.getPlugInsPath();
   public String directory = Menus.getPlugInsPath(); // reasonable default
   public String path = ""; // used by open() and saveAs()
   private String originalFileContentsOnOpen = ""; // for Revert to Open
   public String nameOfCurrentFile = "";
   private boolean savedOnce = false;
   public String targetString = "";
   public TextArea ta = null;
   String wholeWindowTextCache = ""; // for Revert to Save
   public Properties props = new Properties();

   // This is the one and only constructor...        * * * CONSTRUCTOR * * *
   public Jython_Editor () throws Exception {
      super("Jython Editor");
      try {
         FileInputStream in = new FileInputStream(pluginPath +
               "Jython/Properties.properties");
         props.load(in);
         in.close();
      }
      catch (FileNotFoundException e) {
         IJ.showMessage("Could not find " + e.getMessage() +
                        ".\nPlease be sure this file exists.");
         throw new Exception();
      }

      buildAllMenus();
      setupEditWindow();
      PythonInterpreter interp = new PythonInterpreter();
      //~ interp.setOut(outStream);
      performReflections(interp);
      interp.execfile(pluginPath + "Jython/Editor_rc.py");

   }


   // ====================== buildAllMenus() =========================
   // This method calls on an inner class (below) to help with menu-building.
   public void buildAllMenus () {
      String[] menus = {
            "File", "Tools", "Help"};
      MenuBuilder builder = new MenuBuilder(props); // see further below
      MenuBar mb = new MenuBar();
      for (int i = 0; i < menus.length; i++) {
         Menu menu = builder.buildMenu(menus[i]);
         mb.add(menu);
      }
      setMenuBar(mb);
   }


   // ====================== setupEditWindow() =========================
   // Set up the editor environment.
   private void setupEditWindow () {
      int rows = Integer.parseInt(props.getProperty("WindowPrefs.rows"));
      int cols = Integer.parseInt(props.getProperty("WindowPrefs.columns"));
      ta = new TextArea(rows, cols);

      ta.setFont(new Font("Monospaced",
                          Integer.parseInt(props.getProperty(
            "FontPrefs.fontStyle")),
                          Integer.parseInt(props.getProperty(
            "FontPrefs.fontSize")))
            );
      setBackground(Color.lightGray);

      // create the 3 Buttons and their listeners...
      Button evalButton = new Button("Evaluate");
      Button clearButton = new Button("Clear");
      Button exitButton = new Button("Exit");

      evalButton.addActionListener(
            new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            String script = ta.getSelectedText();
            if (script.equals("")) {
               script = ta.getText();
            }
            evaluate(script);
         }
      }
      );

      clearButton.addActionListener(
            new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            ta.setText("");
            setTitle("Untitled");
         }
      }
      );

      exitButton.addActionListener(
            new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            exitEditor();
         }
      }
      );

      // create button panel
      Panel buttonPanel = new Panel();
      buttonPanel.add(clearButton);
      buttonPanel.add(evalButton);
      buttonPanel.add(exitButton);

      // add panels to window
      add(ta);
      add(buttonPanel, BorderLayout.SOUTH);
      pack();

      GUI.center(this);
      setVisible(true);
   }


   // ====================== evalThisTextArea() =========================
   public void evaluate () {
      String windowContent = ta.getText();
      ScriptRunner sr = new ScriptRunner(windowContent);
   }


   // =========================== evaluate() ============================
   // Evaluate (i.e., run) the script passed in 'str'.
   public void evaluate (String str) {
      ScriptRunner sr = new ScriptRunner(str);
      /* String resultString = "";
       try
       {
           Context cx = Context.enter();
           Scriptable scope = cx.initStandardObjects(null);
           String includes = readAll(pluginPath + "js/","includes.js");
           str = "\n" + includes + "\n" + str;
           performReflections(scope);
           try
           {
               Object result = cx.evaluateString(scope,
                                                 str,
                                                 "<cmd>",
                                                 1, null);
               resultString = cx.toString(result);
               IJ.write(resultString );
           }
           catch( JythonException jse)
           {
               IJ.write("Jython exception: " + jse.getMessage()) ;
           }
           Context.exit();

       }
       catch(Exception e)
       {
           IJ.write(e.getMessage()) ;
       }
       return resultString; */
   }


   // ========================= convert() ===========================
   // Create an ImageJ plugin (.java) that drives the current script
   public void convert () {
      evaluate("scriptToPlugin( )");
   }


   // ====================== revertToSave() =========================
   public void revertToSave () {
      ta.setText(wholeWindowTextCache);
   }


   // ====================== revertToOpen() =========================
   public void revertToOpen () {
      ta.setText(originalFileContentsOnOpen);
   }


   // ========================== open() =============================
   public void open () {
      if (checkForDirtyWindow() == false) { // meaning, it's not okay to continue
         return;
      }
      try {
         savedOnce = false;
         FileDialog f = new FileDialog(this,
                                       "Open File", FileDialog.LOAD);
         f.setDirectory(directory);
         f.setVisible(true);
         directory = f.getDirectory(); // remember dir
         String fileName = nameOfCurrentFile = f.getFile();
         if (fileName == null) {
            return;
         }
         path = directory + nameOfCurrentFile;
         String contents = "";
         contents =
               originalFileContentsOnOpen =
               wholeWindowTextCache =
               readAll(directory, fileName);
         ta.setText(contents);
         setTitle("Jython Editor: " + fileName);

      }
      catch (Exception e) {
         IJ.error("File Open Error. " + e.getMessage());
         return;
      }
   } // open


   // ====================== saveAs() =========================
   // Save As uses a  dialog . . .
   public boolean saveAs () {
      FileDialog fd = new FileDialog(this, "Save As...", FileDialog.SAVE);
      String name1 = nameOfCurrentFile;
      fd.setFile(name1);
      fd.setDirectory(directory);
      fd.setVisible(true);
      String name2 = fd.getFile();
      String dir = fd.getDirectory();
      fd.dispose();
      savedOnce = true;
      if (name2 != null) {
         path = dir + name2;
         save(path); // call save() now
         setTitle(name2);
         nameOfCurrentFile = name2;
         return true;
      }
      return false;
   }


   // ==================== saveToCurrentPath() ======================
   public void saveToCurrentPath () {
      if (path.equals("")) {
         saveAs();
      }
      else {
         save(path);
      }
   }


   // ====================== save() =========================
   // Save current window contents without popping a  dialog . . .
   public void save (String outpath) {
      String s = wholeWindowTextCache = ta.getText();
      try {
         BufferedWriter bw = new BufferedWriter(new FileWriter(outpath));
         if (bw != null) {
            bw.write(s, 0, s.length());
         }
         bw.close();
      }
      catch
            (IOException e) {
         IJ.showMessage("Error on Save As! " + e.getMessage());
         e.printStackTrace();
      }
   }


   // ====================== saveStringToFile() =========================
   public void saveStringToFile (String content, String outpath) {
      try {
         BufferedWriter bw = new BufferedWriter(new FileWriter(outpath));
         if (bw != null) {
            bw.write(content, 0, content.length());
         }
         bw.close();
      }
      catch
            (IOException e) {
         IJ.showMessage("Error on Save As! " + e.getMessage());
         e.printStackTrace();
      }
   }


   // ====================== closeFile() =========================
   public void closeFile () {
      if (checkForDirtyWindow() == true) { // meaning, we're going to close out
         // zero out a few things
         ta.setText("");
         originalFileContentsOnOpen = nameOfCurrentFile = "";
         setTitle("Untitled");
      }
   }


// ================checkForDirtyWindow() ==================
// A return value of true means it is okay to close out.
   public boolean checkForDirtyWindow () {
      if (wholeWindowTextCache.equals(ta.getText())) { // no changes
         return true;
      }

      YesNoCancelDialog answer =
            new YesNoCancelDialog(this,
                                  "Save?",
                                  "Save File Before Closing?");

      if (answer.cancelPressed()) {
         return true;
      }

      else if (answer.yesPressed()) {
         boolean cancel = !saveAs();
         if (cancel) {
            return false;
         }
      }
      return true;
   }


   // ======================== print() ===========================
   // This calls on a helper class further below.
   public void print () {
      Font font = new Font(ta.getFont().getName(),
                           ta.getFont().getStyle(), ta.getFont().getSize() - 4);
      new PrintUtility(this, ta.getText(), font);
   }


   // ====================== exitEditor() =========================
   // Quit and go back to ImageJ.
   public void exitEditor () {
      if (checkForDirtyWindow() == true) {
         dispose();
      }
   }


   // ====================== readAll() =========================
   // Read a file into a big string, and close the file.
   public String readAll (String dir, String fname) {
      File file = new File(dir, fname);
      String bigString = new String();
      try {
         StringBuffer sb = new StringBuffer(5000);
         BufferedReader r = new BufferedReader(new FileReader(file));

         while (true) {
            String s = r.readLine();
            if (s == null) {
               break;
            }
            else {
               bigString += s + "\n";
            }
         }
         r.close();
      }
      catch
            (IOException e) {
         IJ.showMessage("Could not read file. " + e.getMessage());
         return "";
      }
      return bigString;
   }


   // ====================== performReflections() =========================
   // Expose some custom objects for use as top-level Jython objects.
   void performReflections (PythonInterpreter interp) {
//        Scriptable jsArgs;
      try {
         ImagePlus imp = WindowManager.getCurrentImage();
         ImageProcessor ip = imp.getProcessor();

         // expose ImagePlus for current image window
         interp.set("imp", imp);
         interp.set("ip", ip);
      }
      catch (Exception e) {
         ;
      } // Don't bail just because an image wasn't open
      // create Editor object for easy script access to this class!
      interp.set("Editor", this);
      // expose IJ utility class
      interp.set("IJ", new IJ());
   }


   // ====================== find() =========================
   // Provide a decent Find facility.
   public void find () {
      targetString = IJ.getString("Search for:",
                                  ta.getSelectedText().toLowerCase());
      if (targetString.equals("")) {
         return; // sanity
      }
      int cursorPos = ta.getCaretPosition();
      String theText = ta.getText().substring(cursorPos);

      int firstHit = theText.toLowerCase().indexOf(targetString);
      if (firstHit == -1) {
         firstHit = theText.indexOf(targetString); // yes, this has a purpose
      }
      if (firstHit != -1) {
         ta.setCaretPosition(cursorPos + firstHit + targetString.length());
         ta.setSelectionStart(cursorPos + firstHit);
         ta.setSelectionEnd(cursorPos + firstHit + targetString.length());
      }
      else {
         IJ.showMessage("No matches found.");
      }
      toFront();
   }


   // ======================= findAgain() ==========================
   // Provide a decent Find Again facility.
   // (Todo: Manage menu item's enable state.)
   public void findAgain () {
      if (targetString.equals("")) {
         return; // sanity
      }

      int cursorPos = ta.getCaretPosition() + 1;
      String theText = ta.getText().substring(cursorPos);

      int nextHit = theText.toLowerCase().indexOf(targetString);
      if (nextHit == -1) {
         nextHit = theText.indexOf(targetString);
      }
      if (nextHit != -1) {
         ta.setCaretPosition(cursorPos + nextHit + targetString.length());
         ta.setSelectionStart(cursorPos + nextHit);
         ta.setSelectionEnd(cursorPos + nextHit + targetString.length());
      }
      else {
         IJ.showMessage("No more matches found.");
      }
      toFront();
   }


   // ======================= showAbout() ==========================
   // Please don't remove this.
   public void showAbout () {
      IJ.showMessage("Jython_Editor 1.0.1 by Ferdinand Jamitzky.\n" +
                     " \n " +
                     "A plugin to provide interactive image editing\n" +
                     "via Jython.\n" +
                     " \n" +
                     "Copyright 2002 by Ferdinand Jamitzky.\n" +
                     "Contact: f.jamitzky@mpe.mpg.de");
   }


   // ================================================================
   // * * * * * * * * * * INNER CLASS: MenuBuilder * * * * * * * * *
   //
   // This builds menus from Property strings, which makes it easy
   // to add or rearrange/modify menus, hotkey assignments, and
   // command-to-action-function bindings just by editing a text file.
   // ================================================================
   public class MenuBuilder
   {
      private Properties props = null;

      public MenuBuilder (Properties proplist) { // * * * CONSTRUCTOR * * *
         props = proplist; // get top-class instance variable 'proplist'
      }


      // Try to find the property 'menuName' and build a Menu
      // based on its value...

      public Menu buildMenu (String menuName) {
         Enumeration enumr = props.propertyNames();
         Menu menu = null;

         while (enumr.hasMoreElements()) { // check every property
            String theProp = enumr.nextElement().toString();
            String value = props.getProperty(theProp);

            if (theProp.indexOf("Menu") == 0
                && theProp.indexOf(menuName) != -1) { // look for Menu.MenuName
               menu = new Menu(theProp.substring(theProp.lastIndexOf(".") + 1));

               // The '*' delimiter demarcataes
               // MenuItemName|hotkey|callback strings:
               StringTokenizer st = new StringTokenizer(value, "*");

               while (st.hasMoreTokens()) { // parse menu commands for this Menu
                  MenuItem mi;

                  String theElement = st.nextElement().toString();

                  // The '|' delimiter separates name,hotkey,action
                  int index = theElement.indexOf("|");
                  if (index != -1) {
                     String theCommandName = theElement.substring(0,
                           index);
                     String theShortcutName = theElement.substring(index + 1,
                           index + 2);
                     MenuShortcut hotkey =
                           theShortcutName.equals(" ") ? null :
                           new MenuShortcut(theShortcutName.charAt(0));
                     mi = new MenuItem(theCommandName, hotkey);
                     int theCommandIndex = theElement.lastIndexOf("|");
                     final String theMethodName =
                           theElement.substring(theCommandIndex + 1);

                     // The actionListener (below) will execute the appropriate
                     // action method for the menu item, using Jython
                     // reflection. In general, trampolining back and forth
                     // between Jython and Java this way is not a best
                     // practice. But doing it this way takes only one line of code.

                     mi.addActionListener(
                           new ActionListener()
                     {
                        public void actionPerformed (ActionEvent event) {
                           evaluate(theMethodName); // trampoline into Jython
                        }
                     }
                     );
                  }
                  else { // No pipe character?
                     String theCommandName = theElement;
                     mi = new MenuItem(theCommandName);
                  }
                  menu.add(mi); // add the MenuItem

               } // while st (get all commands)
            } // if Menu  (build this menu)
         } //while props (search all properties)
         return menu; // returns null menu if not found
      } //method
   } // inner class MenuBuilder


   // ================================================================
   // * * * * * * * * * * INNER CLASS: ScriptRunner  * * * * * * * * *
   //
   // This class allows us to run a script in its own thread, so that a lengthy
   // operation won't lock us out of the program.
   // ================================================================

   class ScriptRunner
         extends Thread
   {
      public String str;

      ScriptRunner (String s) {
         super("Interpret");
         this.str = s;
         start();
      }


      public void run () {
         String resultString = "";
         StringWriter outStream = new StringWriter();
         StringWriter errStream = new StringWriter();

         PythonInterpreter interp = new PythonInterpreter();
         interp.setOut(outStream);
         performReflections(interp);
         interp.execfile(pluginPath + "Jython/prolog.py");
         // write string to temp file and execute it !
         try {
            FileOutputStream out = new FileOutputStream(pluginPath +
                  "Jython/temp.py");
            PrintStream pout = new PrintStream(out);
            pout.println(str);
            pout.close();
         }
         catch (Exception e) {
            System.err.println("Error writing to file  temp.py");
         }
         interp.execfile(pluginPath + "Jython/temp.py");

         interp.execfile(pluginPath + "Jython/epilog.py");
         IJ.write(outStream.toString());

      } // run()
   }

} // end class Jython_Editor



// ================================================================
// * * * * * * * * * * CLASS: PrintUtility * * * * * * * * *
//
// Out of haste, the code for this class was adapted from the
// ij.plugin.frame.Editor print() routines.
// ================================================================

class PrintUtility
{
   PrintJob pjob = null;
   Graphics pg = null;
   Font theFont;

   public PrintUtility (Frame frame, String theText, Font font) {
      pjob = frame.getToolkit().getPrintJob(frame,
                                            "Jython Job", new Properties());
      pg = pjob.getGraphics();
      theFont = font;
      printString(theText);
      pg.dispose();
      pjob.end();
   }


   void printString (String s) {
      int pageNum = 1;
      int linesForThisPage = 0;
      int linesForThisJob = 0;
      int topMargin = 50;
      int leftMargin = 50;
      int bottomMargin = 40;

      if (!(pg instanceof PrintGraphics)) {
         throw new IllegalArgumentException(
               "Graphics context not PrintGraphics");
      }
      if (IJ.isMacintosh()) {
         topMargin = leftMargin = bottomMargin = 0;
      }

      StringReader sr = new StringReader(s);
      LineNumberReader lnr = new LineNumberReader(sr);
      int pageHeight = pjob.getPageDimension().height - bottomMargin;
      pg.setFont(theFont);
      FontMetrics fm = pg.getFontMetrics(theFont);
      int fontHeight = fm.getHeight() - 1;
      int fontDescent = fm.getDescent();
      int curHeight = topMargin;
      try {
         String nextLine = "";
         do {
            nextLine = lnr.readLine();
            if (nextLine != null) {
               nextLine = detabLine(nextLine);
               if ((curHeight + fontHeight) > pageHeight) { // page break
                  pageNum++;
                  linesForThisPage = 0;
                  pg.dispose();
                  pg = pjob.getGraphics();
                  if (pg != null) {
                     pg.setFont(theFont);
                  }
                  curHeight = topMargin;
               }
               curHeight += fontHeight;
               if (pg != null) {
                  pg.drawString(nextLine,
                                leftMargin, curHeight - fontDescent);
                  linesForThisPage++;
                  linesForThisJob++;
               }
            }
         }
         while (nextLine != null);
      }
      catch (EOFException eof) {
         // Fine, ignore
      }
      catch (Throwable t) { // Anything else
         t.printStackTrace();
      }
   }


   String detabLine (String s) {
      if (s.indexOf('\t') < 0) {
         return s;
      }
      int tabSize = 4;
      StringBuffer sb = new StringBuffer((int) (s.length() * 1.25));
      char c;
      for (int i = 0; i < s.length(); i++) {
         c = s.charAt(i);
         if (c == '\t') {
            for (int j = 0; j < tabSize; j++) {
               sb.append(' ');
            }
         }
         else {
            sb.append(c);
         }
      }
      return sb.toString();
   }

} // end PrintUtility inner class
