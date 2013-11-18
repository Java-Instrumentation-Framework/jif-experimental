package tests.gui.internal;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author GBH
 */

public class TestInternalFrameTitleBar {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JPanel p = new JPanel(new GridLayout());
                p.setPreferredSize(new Dimension(300,120));

                JDesktopPane dtp = new JDesktopPane();
                p.add(dtp);

                JInternalFrame jif = new JInternalFrame("JIF",
                    true, //resizable
                    false, //closable
                    true, //maximizable
                    true); //iconifiable
                jif.setVisible(true);
                jif.setSize(200,100);
                dtp.add(jif);
//						try {
//							jif.setClosed(true);
//						} catch (PropertyVetoException ex) {
//							ex.printStackTrace();
//						}

                JOptionPane.showMessageDialog(null, p);
            }
        });
    }	
}
