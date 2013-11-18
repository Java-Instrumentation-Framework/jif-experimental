package edu.mbl.jif.gui.panel;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PanelEnclosedTest extends PanelEnclosed {
  BorderLayout borderLayout1 = new BorderLayout();
  JButton jButton1 = new JButton();
  public PanelEnclosedTest() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  void jbInit() throws Exception {
    jButton1.setText("Close");
    jButton1.addActionListener(new PanelEnclosedTest_jButton1_actionAdapter(this));
    this.setLayout(borderLayout1);
    this.add(jButton1, BorderLayout.CENTER);
  }


  void jButton1_actionPerformed(ActionEvent e) {
    closeParent();
  }
}


class PanelEnclosedTest_jButton1_actionAdapter implements java.awt.event.
    ActionListener {
  PanelEnclosedTest adaptee;
  PanelEnclosedTest_jButton1_actionAdapter(PanelEnclosedTest adaptee) {
    this.adaptee = adaptee;
  }


  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}
