/*
 * FrameForTesting.java
 *
 * Created on February 28, 2009, 1:08 PM
 */
package edu.mbl.jif.gui.test;

import edu.mbl.jif.gui.util.StaticSwingUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author GBH
 */
public class FrameForTesting extends javax.swing.JFrame {

   /**
    * Creates new form FrameForTesting
    */
   public FrameForTesting() {
      this(null);
   }

   public FrameForTesting(JComponent _panel) {
      super();
      initComponents();
      // Diagnostics...
      // RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));
      setUpLogging();
      Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
         public void uncaughtException(Thread t, Throwable ex) {
            dumpEx(ex);
         }
      });
      if (_panel != null) {
         addContents(_panel);
      }
   }

   public void addContents(JComponent j) {
      this.getContentPane().add(j, BorderLayout.CENTER);
//      this.setSize(
//            (int) j.getPreferredSize().getWidth() + 16,
//            (int) j.getPreferredSize().getHeight() + 28);
      pack();
   }

   static void dumpEx(final Throwable ex) {
      StaticSwingUtils.dispatchToEDT(new Runnable() {
         public void run() {
            try {
               File dumpFile = new File(System.getProperty("user.home"), "javaprefs.log");
               PrintWriter wr = new PrintWriter(dumpFile);
               //wr.write(String.format("javaPrefs version: %s\n", version));
               ex.printStackTrace(wr);
               System.getProperties().list(wr);
               wr.close();
               JOptionPane.showMessageDialog(null,
                       "<html><b>Oops! Didn't I tell you to expect bugs?</b>\n\n"
                       + "An exception occured: " + ex.getMessage()
                       + "\nThe debugging info is saved in\n" + dumpFile.getPath()
                       + "\nPlease send it to javaprefs@gmail.com");
            } catch (IOException io) {
               // 
            }
         }
      });
      //System.exit(1);
   }
   /**
    * Sets up java logger. By default logging is off, to turn it on, run JVM with: <br>
    * <code> -Dlogging=fine,/tmp/poll.log </code> substituting desired log level and optional output
    * file name. If not specified, log goes to standard output.
    */
   static Logger logger = Logger.getLogger("global");

   private static void setUpLogging() {
      String arg = System.getProperty("logging");
      String file = null;
      Level level = Level.FINE;
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

   @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 400, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 300, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {

      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
             JFrame.setDefaultLookAndFeelDecorated(true);
            try {
               //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel");
               //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
               //UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceMarinerLookAndFeel");
               UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel");
            } catch (ClassNotFoundException ex) {
               Logger.getLogger(FrameForTesting.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
               Logger.getLogger(FrameForTesting.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
               Logger.getLogger(FrameForTesting.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
               Logger.getLogger(FrameForTesting.class.getName()).log(Level.SEVERE, null, ex);
            }
//            try {
//               for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                  if ("Nimbus".equals(info.getName())) {
//                     UIManager.setLookAndFeel(info.getClassName());
//                     break;
//                  }
//               }
//            } catch (Exception e) {
//               // If Nimbus is not available, you can set the GUI to another look and feel.
//            }
//            try {
//               UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceNebulaLookAndFeel");
//            } catch (Exception ex) {
//               Logger.getLogger(FrameForTesting.class.getName()).log(Level.SEVERE, null, ex);
//            }
            FrameForTesting f = new FrameForTesting();
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(300, 400));
            f.addContents(p);
            f.setVisible(true);
            logger.log(Level.FINE, "One fine logging message");
            //throw an UncaughtException
            throw new IllegalArgumentException("foo");

         }
      });
   }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
