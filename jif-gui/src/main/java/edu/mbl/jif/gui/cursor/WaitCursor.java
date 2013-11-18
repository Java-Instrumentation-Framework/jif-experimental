/*
 * WaitCursor.java
 *
 * Created on February 16, 2006, 10:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.gui.cursor;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
//import sun.security.krb5.internal.crypto.e;

public class WaitCursor {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Wait Cursor");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem launchLongRunningTask = new JMenuItem("Launch Long Running Task");

        launchLongRunningTask.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // At least show wait cursor
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    }
                });
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            // simulated long running operation
                            Thread.currentThread().sleep(10000);
                        } catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                        } finally {
                            // reset the cursor to default
                            frame.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                });
            }
        });

        fileMenu.add(launchLongRunningTask);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(400, 400, 200, 100);
        frame.setVisible(true);
    }
}
