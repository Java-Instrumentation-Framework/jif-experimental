package edu.mbl.jif.script.jython;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.python.util.PythonInterpreter;


// Output from Jython scripts is redirected to this console window.

public class ScriptConsole
      extends JPanel
      //implements WindowListener,
      implements ActionListener, Runnable
{
   private JTextArea textArea;
   private JScrollPane textScroll;
   private Thread readerOut;
   private Thread readerErr;
   private boolean quit;
   private final PipedInputStream pinOut = new PipedInputStream();
   private final PipedInputStream pinErr = new PipedInputStream();
   Thread errorThrower;
   // just for testing (Throws an Exception at this Console)

   public ScriptConsole (PythonInterpreter interp, String title) {
      // create all components and add them
      setLayout(new BorderLayout());
      JButton button = new JButton("clear");
      button.setFont(new java.awt.Font("Dialog", 0, 10));
      textArea = new JTextArea();
      textArea.setEditable(false);
      textArea.setBackground(Color.lightGray);
      textScroll = new JScrollPane(textArea);
      add(textScroll, BorderLayout.CENTER);
      add(button, BorderLayout.SOUTH);
      //panelConsole.setBounds(100, 50, 400, 500);
      //panelConsole.addWindowListener(this);
      //button.addActionListener(this);
      setVisible(true);

      //
      // Redirect the out & err streams to pipes
      /** @todo System.setOut() too ??? */
      try {
         PipedOutputStream pOut = new PipedOutputStream(this.pinOut);
         interp.setOut(new PrintStream(pOut, true));
         //System.setOut(new PrintStream(pOut, true));
      }
      catch (java.io.IOException io) {
         textArea.append("Couldn't redirect STDOUT to this console\n"
                         + io.getMessage());
      }
      catch (SecurityException se) {
         textArea.append("Couldn't redirect STDOUT to this console\n"
                         + se.getMessage());
      }
      try {
         PipedOutputStream pOutErr = new PipedOutputStream(this.pinErr);
         interp.setErr(new PrintStream(pOutErr, true));
         //System.setErr(new PrintStream(pOutErr, true));
      }
      catch (java.io.IOException io) {
         textArea.append("Couldn't redirect STDERR to this console\n"
                         + io.getMessage());
      }
      catch (SecurityException se) {
         textArea.append("Couldn't redirect STDERR to this console\n"
                         + se.getMessage());
      }
      quit = false; // signals the Threads that they should exit
      readerOut = new Thread(this);
      readerOut.setDaemon(true);
      readerOut.start();
      //
      readerErr = new Thread(this);
      readerErr.setDaemon(true);
      readerErr.start();
   }


   public synchronized void windowClosed () {
      quit = true;
      this.notifyAll(); // stop all threads
      try {
         readerOut.join(1000);
         pinOut.close();
      }
      catch (Exception e) {}
      try {
         readerErr.join(1000);
         pinErr.close();
      }
      catch (Exception e) {}
      //System.exit(0);
   }


//  public synchronized void windowClosing(WindowEvent evt) {
//    panelConsole.setVisible(false);  // default behaviour of JFrame
//    panelConsole.dispose();
//  }

   public synchronized void actionPerformed (ActionEvent evt) {
      textArea.setText("");
   }


   public synchronized void showConsole (boolean showIt) {
      setVisible(showIt);
   }


   public synchronized void run () {
      try {
         while (Thread.currentThread() == readerOut) {
            try {
               this.wait(100);
            }
            catch (InterruptedException ie) {}
            if (pinOut.available() != 0) {
               String input = this.readLine(pinOut);
               textArea.append(input);
               SwingUtilities.invokeLater(new Runnable()
               {
                  public void run () {
                     textScroll.getVerticalScrollBar()
                           .setValue(textScroll.getVerticalScrollBar().
                                     getMaximum());
                  }
               });
            }
            if (quit) {
               return;
            }
         }
         while (Thread.currentThread() == readerErr) {
            try {
               this.wait(100);
            }
            catch (InterruptedException ie) {}
            if (pinErr.available() != 0) {
               String input = this.readLine(pinErr);
               textArea.append(input);
               SwingUtilities.invokeLater(new Runnable()
               {
                  public void run () {
                     textScroll.getVerticalScrollBar()
                           .setValue(textScroll.getVerticalScrollBar().
                                     getMaximum());
                  }
               });
            }
            if (quit) {
               return;
            }
         }
      }
      catch (Exception e) {
         textArea.append("\nConsole reports an Internal error.");
         textArea.append("The error is: " + e);
      }
   }


   public synchronized String readLine (PipedInputStream in) throws
         IOException {
      String input = "";
      do {
         int available = in.available();
         if (available == 0) {
            break;
         }
         byte[] b = new byte[available];
         in.read(b);
         input = input + new String(b, 0, b.length);
      }
      while (!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
      return input;
   }

}
