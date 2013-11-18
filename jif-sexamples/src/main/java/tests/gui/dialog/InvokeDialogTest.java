/*
 * InvokeDialogTest.java
 *
 * Created on March 15, 2007, 11:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.dialog;
 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import javax.swing.JApplet;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;
import org.jdesktop.swingworker.SwingWorker;

 
 
 public class InvokeDialogTest extends JApplet {
 

     /*private JLabel jLabel;
 
     private SwingWorker<Object> worker;
 
     public InvokeDialogTest() {
         jLabel = new JLabel();
         jLabel.setPreferredSize(new Dimension(200, 100));
         jLabel.setHorizontalAlignment(JLabel.CENTER);
         getContentPane().add(jLabel, BorderLayout.CENTER);
     }
 */
     public synchronized void start() {
 /*        jLabel.setText("Running...");
         worker = new SwingWorker<Object>() {
             // Prompt the user every three seconds
             protected Object construct() throws InterruptedException {
                 while (true) {
                     Thread.sleep(3000);
                     int n = InvokeUtils.invokeConfirmDialog(
                             jLabel,
                             "Operation timed out. Try again?",
                             "Timeout",
                             JOptionPane.YES_NO_OPTION,
                             JOptionPane.WARNING_MESSAGE);
                     if (n != JOptionPane.YES_OPTION) {
                         break;
                     }
                 }
                 return null;
             }
             protected void finished() {
                 jLabel.setText("Stopped");
             }
         };
         worker.start();
  */
     }
 
     public synchronized void stop() {
        /* if (worker != null) {
             worker.cancel(true);
             worker = null;
         }
         */
     }
 }