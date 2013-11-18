package edu.mbl.jif.gui.event;

/*
 * ETest.java
 */

/**
 * A simple GUI class to test out the features of EEL.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class EveryEventTest
      extends JFrame
{

   public EveryEventTest () {
      super("EEL Tester");
      setSize(750, 500);
      addWindowListener(new BasicWindowMonitor());

      EveryEventLogger eel = EveryEventLogger.getInstance();
      eel.addGui();

      addWindowListener(eel);


      JPanel buttonPane = new JPanel();
      buttonPane.addContainerListener(eel);
      buttonPane.addKeyListener(eel);
      JButton okButton = new JButton("Ok");
      JButton copyButton = new JButton(new ImageIcon("copy.gif"));
      JComboBox jcb = new JComboBox(new String[] {
            "Red", "Blue", "Green", "Yellow", "Cyan", "Magenta",
            "Black", "White"
      });
      jcb.setEditable(true);

      buttonPane.add(okButton);
      buttonPane.add(copyButton);
      buttonPane.add(jcb);

      okButton.addActionListener(eel);
      copyButton.addActionListener(eel);
      copyButton.addMouseListener(eel);
      jcb.addActionListener(eel);
      jcb.addItemListener(eel);

      getContentPane().add(buttonPane, BorderLayout.NORTH);

      eel.showDialog();
   }


   public static void main (String args[]) {
      EveryEventTest et = new EveryEventTest();
      et.setVisible(true);
   }
}
