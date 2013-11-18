package edu.mbl.jif.gui.html;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import java.net.URL;


public class BrowserPanel
      extends JPanel
{
   BrowserPanel () {
      setLayout(new BorderLayout(5, 5));
      final JEditorPane jt = new JEditorPane();
      final JTextField input = new JTextField("http://java.sun.com");
      // make read-only
      jt.setEditable(false);
      // follow links
      jt.addHyperlinkListener(new HyperlinkListener()
      {
         public void hyperlinkUpdate (final HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
               SwingUtilities.invokeLater(new Runnable()
               {
                  public void run () {
                     // Save original
                     Document doc = jt.getDocument();
                     try {
                        URL url = e.getURL();
                        jt.setPage(url);
                        input.setText(url.toString());
                     }
                     catch (IOException io) {
                        JOptionPane.showMessageDialog(BrowserPanel.this, "Can't follow link",
                              "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        jt.setDocument(doc);
                     }
                  }
               });
            }
         }
      });
      JScrollPane pane = new JScrollPane();
      pane.setBorder(BorderFactory.createLoweredBevelBorder());
      pane.getViewport().add(jt);
      add(pane, BorderLayout.CENTER);

      input.addActionListener(new ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            try {
               jt.setPage(input.getText());
            }
            catch (IOException ex) {
               JOptionPane.showMessageDialog(BrowserPanel.this, "Invalid URL", "Invalid Input",
                     JOptionPane.ERROR_MESSAGE);
            }
         }
      });
      add(input, BorderLayout.SOUTH);
   }
}
