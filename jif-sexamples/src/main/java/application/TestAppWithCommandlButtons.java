/*
 * TestPanelButtons.java
 *
 * Created on April 27, 2006, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author GBH
 */
public class TestAppWithCommandlButtons extends JPanel {

   private final FrameForTest f;
   Box box;

   /**
    * Creates a new instance of TestPanelButtons
    */
   private TestAppWithCommandlButtons() {
      super();
      f = new FrameForTest();
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      box = Box.createVerticalBox();
      add(box, BorderLayout.CENTER);
      JLabel label = new JLabel("---- Testing ----");
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      box.add(label);
      box.add(Box.createVerticalStrut(15));
      log(Level.FINE, "One fine logging message");
      addCommands();
      f.addContents(this);
      //setPreferredSize(new Dimension(300, 400));
      f.setVisible(true);
   }

   public void addCommands() {
      addButton("Do Me", this, "doMe();");
   }

   //=====================================
   public void doMe() {
      System.out.println("Me done.");
   }

   //=====================================
   // ! Method passed must be public !
   public void addButton(String name, Object obj, String method) {
      JButton button = new JButton(name);
      try {
         button.addActionListener(CommandParsed.parse(obj, method));
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      box.add(button);
      box.add(Box.createVerticalStrut(5));
   }

   public void log(Level level, String logMsg) {
      f.log(level, logMsg);
   }

   public static void main(String[] args) {

      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new TestAppWithCommandlButtons();
         }
      });

   }

   public class FrameForTest
           extends JFrame {

      BorderLayout borderLayout1 = new BorderLayout();
      JComponent panel;

      public FrameForTest() {
         this(null);
      }

      public FrameForTest(JComponent _panel) {
         super();
         panel = _panel;
         // Diagnostics...
         RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));
         setUpLogging();
         Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable ex) {
               dumpEx(ex);
            }
         });
         try {
            jbInit();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      }

      void jbInit() throws Exception {
         setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         this.setTitle("TestFrame");
         this.getContentPane().setLayout(new BorderLayout());
         if (panel != null) {
            addContents(panel);
         }
         setSize(640, 480);
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         setLocation(
                 Math.max(0, screenSize.width / 2 - getWidth() / 2),
                 Math.max(0, screenSize.height / 2 - getHeight() / 2));

         setVisible(true);
         //org.pf.joi.Inspector.inspect(this);
      }

      public void addContents(JComponent j) {
         this.getContentPane().add(j, BorderLayout.CENTER);
//      this.setSize(
//            (int) j.getPreferredSize().getWidth() + 16,
//            (int) j.getPreferredSize().getHeight() + 28);
         pack();
      }

      void dumpEx(Throwable ex) {
         try {
            File dumpFile = new File(System.getProperty("user.home"), "error.log");
            PrintWriter wr = new PrintWriter(dumpFile);
            //wr.write(String.format("javaPrefs version: %s\n", version));
            ex.printStackTrace(wr);
            System.getProperties().list(wr);
            wr.close();
            JOptionPane.showMessageDialog(null,
                    "<html><b>Exeception Occurred</b>\n\n"
                    + ex.getMessage() + "/n"
                    + "\nThe debugging info is saved in\n" + dumpFile.getPath()
                    + "\n");
         } catch (IOException io) {
            // 
         }
         System.exit(1);
      }
      /**
       * Sets up java logger. By default logging is off, to turn it on, run JVM with: <br>
       * <code> -Dlogging=fine,/tmp/poll.log </code> substituting desired log level and optional
       * output file name. If not specified, log goes to standard output.
       */
      Logger logger = Logger.getLogger("global");

      public void log(Level level, String logMsg) {
         logger.log(level, logMsg);
      }

      private void setUpLogging() {
         String arg = System.getProperty("logging");
         String file = null;
         Level level = Level.FINE;  // default
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
         System.out.print("Logging set, level: " + level + ", ");
         if (file != null) {
            System.out.println("to file: " + file);
         } else {
            System.out.println("to Console");
         }
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
   }

   public class ThreadCheckingRepaintManager extends RepaintManager {

      private int tabCount = 0;
      private boolean checkIsShowing = false;

      public ThreadCheckingRepaintManager() {
         super();
      }

      public ThreadCheckingRepaintManager(boolean checkIsShowing) {
         super();
         this.checkIsShowing = checkIsShowing;
      }

      public synchronized void addInvalidComponent(JComponent jComponent) {
         checkThread(jComponent);
         super.addInvalidComponent(jComponent);
      }

      private void checkThread(JComponent c) {
         if (!SwingUtilities.isEventDispatchThread() && checkIsShowing(c)) {
            System.out.println("/n--------- Wrong Thread START >>>>>>>>>");
            System.out.println(getStracktraceAsString(new Exception()));
            dumpComponentTree(c);
            System.out.println("--------- Wrong Thread END <<<<<<<<<<</n");
         }
      }

      private String getStracktraceAsString(Exception e) {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         PrintStream printStream = new PrintStream(byteArrayOutputStream);
         e.printStackTrace(printStream);
         printStream.flush();
         return byteArrayOutputStream.toString();
      }

      private boolean checkIsShowing(JComponent c) {
         if (this.checkIsShowing == false) {
            return true;
         } else {
            return c.isShowing();
         }
      }

      public synchronized void addDirtyRegion(JComponent jComponent, int i, int i1, int i2, int i3) {
         checkThread(jComponent);
         super.addDirtyRegion(jComponent, i, i1, i2, i3);
      }

      private void dumpComponentTree(Component c) {
         System.out.println("----------Component Tree");
         resetTabCount();
         for (; c != null; c = c.getParent()) {
            printTabIndent();
            System.out.println(c);
            printTabIndent();
            System.out.println("Showing:" + c.isShowing() + " Visible: " + c.isVisible());
            incrementTabCount();
         }
      }

      private void resetTabCount() {
         this.tabCount = 0;
      }

      private void incrementTabCount() {
         this.tabCount++;
      }

      private void printTabIndent() {
         for (int i = 0; i < this.tabCount; i++) {
            System.out.print("\t");
         }
      }
   }

   static class CommandParsed implements ActionListener {

      Method m;       // The method to be invoked
      Object target;  // The object to invoke it on
      Object[] args;  // The arguments to pass to the method
      // An empty array; used for methods with no arguments at all.
      static final Object[] nullargs = new Object[]{};

      /**
       * This constructor creates a Command object for a no-arg method
       */
      public CommandParsed(Object target, Method m) {
         this(target, m, nullargs);
      }

      /**
       * This constructor creates a Command object for a method that takes the specified array of
       * arguments. Note that the parse() method provides another way to create a Command object
     *
       */
      public CommandParsed(Object target, Method m, Object[] args) {
         this.target = target;
         this.m = m;
         this.args = args;
      }

      /**
       * Invoke the Command by calling the method on its target, and passing the arguments. See also
       * actionPerformed() which does not throw the checked exceptions that this method does.
     *
       */
      public void invoke()
              throws IllegalAccessException, InvocationTargetException {
         m.invoke(target, args);  // Use reflection to invoke the method
      }

      /**
       * This method implements the ActionListener interface. It is like invoke() except that it
       * catches the exceptions thrown by that method and rethrows them as an unchecked
       * RuntimeException
     *
       */
      public void actionPerformed(ActionEvent e) {
         try {
            invoke();                           // Call the invoke method
         } catch (InvocationTargetException ex) {  // but handle the exceptions
            throw new RuntimeException("Command: "
                    + ex.getTargetException().toString());
         } catch (IllegalAccessException ex) {
            throw new RuntimeException("Command: " + ex.toString());
         }
      }

      /**
       * This static method creates a Command using the specified target object, and the specified
       * string. The string should contain method name followed by an optional parenthesized
       * comma-separated argument list and a semicolon. The arguments may be boolean, integer or
       * double literals, or double-quoted strings. The parser is lenient about missing commas,
       * semicolons and quotes, but throws an IOException if it cannot parse the string.
     *
       */
      public static CommandParsed parse(Object target, String text) throws IOException {
         String methodname;                 // The name of the method
         ArrayList args = new ArrayList();  // Hold arguments as we parse them.
         ArrayList types = new ArrayList(); // Hold argument types.

         // Convert the string into a character stream, and use the
         // StreamTokenizer class to convert it into a stream of tokens
         StreamTokenizer t = new StreamTokenizer(new StringReader(text));

         // The first token must be the method name
         int c = t.nextToken();  // read a token
         if (c != t.TT_WORD) // check the token type
         {
            throw new IOException("Missing method name for command");
         }
         methodname = t.sval;    // Remember the method name

         // Now we either need a semicolon or a open paren
         c = t.nextToken();
         if (c == '(') { // If we see an open paren, then parse an arg list
            for (;;) {                   // Loop 'till end of arglist
               c = t.nextToken();      // Read next token

               if (c == ')') {         // See if we're done parsing arguments.
                  c = t.nextToken();  // If so, parse an optional semicolon
                  if (c != ';') {
                     t.pushBack();
                  }
                  break;              // Now stop the loop.
               }

               // Otherwise, the token is an argument; figure out its type
               if (c == t.TT_WORD) {
                  // If the token is an identifier, parse boolean literals,
                  // and treat any other tokens as unquoted string literals.
                  if (t.sval.equals("true")) {       // Boolean literal
                     args.add(Boolean.TRUE);
                     types.add(boolean.class);
                  } else if (t.sval.equals("false")) { // Boolean literal
                     args.add(Boolean.FALSE);
                     types.add(boolean.class);
                  } else {                             // Assume its a string
                     args.add(t.sval);
                     types.add(String.class);
                  }
               } else if (c == '"') {         // If the token is a quoted string
                  args.add(t.sval);
                  types.add(String.class);
               } else if (c == t.TT_NUMBER) { // If the token is a number
                  int i = (int) t.nval;
                  if (i == t.nval) {           // Check if its an integer
                     // Note: this code treats a token like "2.0" as an int!
                     args.add(new Integer(i));
                     types.add(int.class);
                  } else {                       // Otherwise, its a double
                     args.add(new Double(t.nval));
                     types.add(double.class);
                  }
               } else {                        // Any other token is an error
                  throw new IOException("Unexpected token " + t.sval
                          + " in argument list of "
                          + methodname + "().");
               }

               // Next should be a comma, but we don't complain if its not
               c = t.nextToken();
               if (c != ',') {
                  t.pushBack();
               }
            }
         } else if (c != ';') { // if a method name is not followed by a paren
            t.pushBack();    // then allow a semi-colon but don't require it.
         }

         // We've parsed the argument list.
         // Next, convert the lists of argument values and types to arrays
         Object[] argValues = args.toArray();
         Class[] argtypes = (Class[]) types.toArray(new Class[argValues.length]);

         // At this point, we've got a method name, and arrays of argument
         // values and types.  Use reflection on the class of the target object
         // to find a method with the given name and argument types.  Throw
         // an exception if we can't find the named method.
         Method method;
         try {
            method = target.getClass().getMethod(methodname, argtypes);
         } catch (Exception e) {
            throw new IOException("No such method found, or wrong argument "
                    + "types: " + methodname);
         }

         // Finally, create and return a Command object, using the target object
         // passed to this method, the Method object we obtained above, and
         // the array of argument values we parsed from the string.
         return new CommandParsed(target, method, argValues);
      }
   }
}
