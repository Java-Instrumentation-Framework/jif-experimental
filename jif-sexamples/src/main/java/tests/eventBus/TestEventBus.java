/*
 * TestEventBus.java
 *
 * Created on February 25, 2009, 12:17 PM
 */
package tests.eventBus;

import org.bushe.swing.event.EventBus;

/**
 *
 * @author GBH
 */
public class TestEventBus extends javax.swing.JFrame {

	/**
	 * Creates new form TestEventBus
	 */
	public TestEventBus() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    statusBar1 = new tests.eventBus.StatusBar();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    statusBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    statusBar1.setText("statusBar1");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(87, 87, 87)
        .addComponent(statusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(67, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(202, 202, 202)
        .addComponent(statusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(75, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TestEventBus().setVisible(true);
			}

		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
		EventBus.publish(new StatusEvent("Looking up data..."));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
		EventBus.publish(new StatusEvent("Looking up data...22222"));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
		EventBus.publish(new StatusEvent("Looking up data...3333"));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
		EventBus.publish(new StatusEvent(""));
		

	}

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private tests.eventBus.StatusBar statusBar1;
  // End of variables declaration//GEN-END:variables
}
