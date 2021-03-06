/*
 * TestBindingFrame.java
 *
 * Created on January 21, 2008, 9:35 PM
 */
package tests.binding;

import javax.swing.UIManager;

/**
 *
 * @author  GBH
 */
public class TestBindingFrame
        extends javax.swing.JFrame {

    /** Creates new form TestBindingFrame */
    public TestBindingFrame() {
        initComponents();
        TestBean bean = new TestBean();
        TestBean2 bean2 = new TestBean2();
        //bean = bean.restore("testbean");
        //bean.setToAutoSave("testbean");
       
        testPanelBinding21.setBean(bean);
        testPanel21.setBean(bean2);
        
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testPanelBinding21 = new tests.binding.TestPanelBinding2();
        testPanel21 = new tests.binding.TestPanel2();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(testPanelBinding21, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(testPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(testPanelBinding21, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(testPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        try {
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
//                    new com.jgoodies.looks.plastic.theme.DesertBluer());
//            //new com.jgoodies.looks.plastic.theme.Silver());
//            //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
//            //new com.jgoodies.looks.plastic.theme.DesertBlue());
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
//            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
//        } catch (javax.swing.UnsupportedLookAndFeelException use) {
//            UIManager.getSystemLookAndFeelClassName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestBindingFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private tests.binding.TestPanel2 testPanel21;
    private tests.binding.TestPanelBinding2 testPanelBinding21;
    // End of variables declaration//GEN-END:variables
}
