package edu.mbl.jif.gui.test;


import edu.mbl.jif.utils.diag.edt.ThreadCheckingRepaintManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.*;

public class FrameForTest
        extends JFrame {

   BorderLayout borderLayout1 = new BorderLayout();
   JComponent panel;

   public FrameForTest() {
      this(null);
   }

   public FrameForTest(JComponent _panel) {
      this(_panel, false);
   }

   public FrameForTest(JComponent _panel, boolean enableExceptHandling) {
      super();
      panel = _panel;
      // Diagnostics...
      RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));
      setUpLogging();
      if (enableExceptHandling) {
         Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable ex) {
               dumpEx(ex);
            }
         });
      }
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

   static void dumpEx(Throwable ex) {
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
    * <code> -Dlogging=fine,/tmp/poll.log </code> substituting desired log level and optional output
    * file name. If not specified, log goes to standard output.
    */
   static Logger logger = Logger.getLogger("global");

   public void log(Level level, String logMsg) {
      logger.log(level, logMsg);
   }

   private static void setUpLogging() {
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

   // Test Main ========================================================
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            FrameForTest f = new FrameForTest();
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(300, 400));
            f.addContents(p);
            setUpLogging();
            logger.log(Level.FINE, "One fine logging message");
            //throw an UncaughtException
            // throw new IllegalArgumentException("foo");
         }
      });
   }
}
