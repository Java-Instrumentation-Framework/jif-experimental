package application;


import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Credits extends JDialog {

  final static String CREDITS = "Credits:\n" +
      "\n";

  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel labelCredits = new JLabel();

  public Credits(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public Credits() {
    this(null, "", false);
  }
  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    labelCredits.setOpaque(false);
    labelCredits.setHorizontalAlignment(SwingConstants.CENTER);
    labelCredits.setText(CREDITS);
    getContentPane().add(panel1);
    panel1.add(labelCredits, BorderLayout.CENTER);
  }
}
