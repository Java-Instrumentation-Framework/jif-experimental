/*
 * StackTraceWindow.java
 *
 * Created on April 4, 2006, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.utils;

import java.awt.*;

import java.io.*;

import javax.swing.*;


public class StackTraceWindow extends JFrame implements Thread.UncaughtExceptionHandler {
   private JTextArea textArea;

   public StackTraceWindow(String title, final int width, final int height) {
      super(title);
      setSize(width, height);
      textArea = new JTextArea();
      JScrollPane pane = new JScrollPane(textArea);
      textArea.setEditable(false);
      getContentPane().add(pane);
   }

   public void uncaughtException(Thread t, Throwable e) {
      addStackInfo(e);
   }

   public void addStackInfo(final Throwable t) {
      EventQueue.invokeLater(new Runnable() {
            public void run() {
               // Bring window to foreground
               setVisible(true);
               toFront();
               // Convert stack dump to string
               StringWriter sw = new StringWriter();
               PrintWriter  out = new PrintWriter(sw);
               t.printStackTrace(out);
               // Add string to end of text area
               textArea.append(sw.toString());
            }
         });
   }

   public static void main(final String[] args) throws Exception {
      Thread.UncaughtExceptionHandler handler = new StackTraceWindow("Show Exception Stack", 400, 200);
      Thread.setDefaultUncaughtExceptionHandler(handler);
      new Thread() {
            public void run() {
               System.out.println(1 / 0);
            }
         }.start();
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Press Enter for next exception");
      br.readLine();
      new Thread() {
            public void run() {
               System.out.println(args[0]);
            }
         }.start();
      System.out.print("Press Enter to end");
      br.readLine();
      System.exit(0);
   }
}
