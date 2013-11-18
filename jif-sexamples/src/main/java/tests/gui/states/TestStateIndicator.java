/*
 * TestStateIndicator.java
 *
 * Created on May 8, 2006, 7:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.states;

import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 *
 * @author  GBH
 */
public class TestStateIndicator extends javax.swing.JFrame {
   
   /** Creates new form ActivityIndicator */
   public TestStateIndicator() {
      initComponents();
   }
   
                    
   private void initComponents() {
      jToggleButton1 = new javax.swing.JToggleButton();
      jPanel1 = new javax.swing.JPanel();
      jLabel1 = new StateIndicator();
      jLabel1.setState(0);
      jLabel1.setPreferredSize(new Dimension(32,32));

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      jToggleButton1.setText("jToggleButton1");
      jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            switchIt(evt);
         }
      });

      jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      jPanel1.setSize(100,100);
      jPanel1.add(jLabel1);
      jPanel1.add(jToggleButton1);   
      this.add(jPanel1, BorderLayout.CENTER);
      
//      org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
//      jPanel1.setLayout(jPanel1Layout);
//      jPanel1Layout.setHorizontalGroup(
//         jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//         .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
//            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//            .addContainerGap())
//      );
//      jPanel1Layout.setVerticalGroup(
//         jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//         .add(jPanel1Layout.createSequentialGroup()
//            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE)
//            .addContainerGap())
//      );
//
//      org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
//      getContentPane().setLayout(layout);
//      layout.setHorizontalGroup(
//         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//         .add(layout.createSequentialGroup()
//            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//               .add(layout.createSequentialGroup()
//                  .add(37, 37, 37)
//                  .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
//               .add(jToggleButton1))
//            .addContainerGap(295, Short.MAX_VALUE))
//      );
//      layout.setVerticalGroup(
//         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
//         .add(layout.createSequentialGroup()
//            .add(21, 21, 21)
//            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
//            .add(32, 32, 32)
//            .add(jToggleButton1)
//            .addContainerGap(201, Short.MAX_VALUE))
//      );

      pack();
   }// </editor-fold>  
   

   int s = 0;
   private void switchIt(java.awt.event.ActionEvent evt) {                          
// TODO add your handling code here:
      if(jToggleButton1.isSelected()){
             jLabel1.setState(s++);
      } else {
             jLabel1.setState(s++);
      }
   }                         
   
   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            
            TestStateIndicator t = new TestStateIndicator();
            t.setState(4);
            t.setVisible(true);

            
         }
      });
   }
   
   // Variables declaration - do not modify                     
   StateIndicator jLabel1;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JToggleButton jToggleButton1;
   // End of variables declaration                   
   
}
