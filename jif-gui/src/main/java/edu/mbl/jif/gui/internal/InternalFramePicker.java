package edu.mbl.jif.gui.internal;

import edu.mbl.jif.gui.swingthread.SwingWorker3;
import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import edu.mbl.jif.utils.*;

public class InternalFramePicker extends JInternalFrame {
    
  public InternalFramePicker( javax.swing.JDesktopPane desktop) {
    super("Show Frame", false, false, false, false);
    this.desktop = desktop;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private JPanel contentPane;
  private DefaultListModel listModel;
  private JList list;
  private JScrollPane jScrollPane1;
  private JButton button_Cancel = new JButton();
  private boolean cancelled = false;
  JInternalFrame[] allFrames;
  javax.swing.JDesktopPane desktop;

  //Component initialization
  private void jbInit() throws Exception {
    /** @todo  super.setFrameIcon(jif.utils.PSjUtils.loadImageIcon("framepicker16.gif")); */
    setSize(new Dimension(256, 243));
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(null);
    contentPane.setBackground(new Color(159, 206, 215));
    //
    listModel = new DefaultListModel();
    list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.addListSelectionListener(new MyListSelectionListener());
    getFrameList();
    jScrollPane1 = new JScrollPane(list);
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.
        HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane1.setOpaque(false);
    jScrollPane1.setBounds(new Rectangle(7, 8, 231, 170));
    //
    button_Cancel.setFont(new java.awt.Font("Dialog", 0, 10));
    contentPane.add(jScrollPane1, null);
    contentPane.add(button_Cancel, null);
    button_Cancel.setBounds(new Rectangle(89, 185, 68, 24));
    button_Cancel.setMargin(new Insets(2, 4, 2, 4));
    button_Cancel.setText("Cancel");
    button_Cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button_Cancel_actionPerformed(e);
      }
    });

    //
    //list.revalidate();
    jScrollPane1.revalidate();
    jScrollPane1.repaint();
    //jScrollPane1.updateUI();
    //list.updateUI();
    validate();
  }

  //////////////////////////////////////////////////////////////////////
  // getSeriesList
  //
  public boolean getFrameList() {
    listModel.removeAllElements();
    allFrames = desktop.getAllFrames();
    for (int i = 0; i < allFrames.length; i++) {
      listModel.addElement(allFrames[i].getTitle());
    }
    list.validate();
    list.updateUI();
    return true;
  }

  /////////////////////////////////////////////////////////////////////////
  // Cancel the process
  //
  void button_Cancel_actionPerformed(ActionEvent e) {
//    this.setVisible(false);
//    this.dispose();
    close();
  }

  public void close() {
    this.setVisible(false);
    this.dispose();
  }

  public void selectFrame(JInternalFrame _iFrame) {
    if (_iFrame != null) {
      final JInternalFrame iFrame = _iFrame;
      final InternalFramePicker ipick = this;
      final SwingWorker3 worker =
          new SwingWorker3() {
        public Object construct() {
          try {
            iFrame.setSelected(true);
          } catch (java.beans.PropertyVetoException pve) {}
          return null;
        }

        public void finished() {
          ipick.close();
        }
      };
      worker.start();
    }
  }

  ///////////////////////////////////////////////////////////////////
  // ListSelection
  //
  class MyListSelectionListener
      implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
        JList list = (JList) e.getSource();
        if (list.getSelectedIndex() > -1) {

          String frame = list.getSelectedValue()
              .toString();
          //
          System.out.println("Frame: " + frame);
          //
          JInternalFrame iFrame = null;
          for (int i = 0; i < allFrames.length; i++) {
            if (frame.equalsIgnoreCase(allFrames[i].getTitle())) {
              iFrame = (JInternalFrame) allFrames[i];
              break;
            }
          }
          selectFrame(iFrame);

        }
      }
    }
  }
}
