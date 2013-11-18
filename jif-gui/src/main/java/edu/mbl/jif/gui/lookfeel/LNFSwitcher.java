
package edu.mbl.jif.gui.lookfeel;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A Look-and-feel switcher.
 * 
 * @author Ian Darwin, http://www.darwinsys.com/
 * @version $Id: LNFSwitcher.java,v 1.7 2004/03/07 04:03:15 ian Exp $
 */
public class LNFSwitcher {
  /** The frame. */
  protected JFrame theFrame;

  /** Its content pane */
  protected Container cp;

  /** Start with the Java look-and-feel, if possible */
  final static String PREFERREDLOOKANDFEELNAME = "javax.swing.plaf.metal.MetalLookAndFeel";

  protected String curLF = PREFERREDLOOKANDFEELNAME;

  protected JRadioButton previousButton;

  /** Construct a program... */
  public LNFSwitcher() {
    super();
    theFrame = new JFrame("LNF Switcher");
    theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    cp = theFrame.getContentPane();
    cp.setLayout(new FlowLayout());

    ButtonGroup bg = new ButtonGroup();

    JRadioButton bJava = new JRadioButton("Java");
    bJava.addActionListener(new LNFSetter(
        "javax.swing.plaf.metal.MetalLookAndFeel", bJava));
    bg.add(bJava);
    cp.add(bJava);

    JRadioButton bMSW = new JRadioButton("MS-Windows");
    bMSW.addActionListener(new LNFSetter(
        "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", bMSW));
    bg.add(bMSW);
    cp.add(bMSW);

    JRadioButton bMotif = new JRadioButton("Motif");
    bMotif.addActionListener(new LNFSetter(
        "com.sun.java.swing.plaf.motif.MotifLookAndFeel", bMotif));
    bg.add(bMotif);
    cp.add(bMotif);

    JRadioButton bMac = new JRadioButton("Sun-MacOS");
    bMac.addActionListener(new LNFSetter(
        "com.sun.java.swing.plaf.mac.MacLookAndFeel", bMac));
    bg.add(bMac);
    cp.add(bMac);

    String defaultLookAndFeel = UIManager.getSystemLookAndFeelClassName();
    // System.out.println(defaultLookAndFeel);
    JRadioButton bDefault = new JRadioButton("Default");
    bDefault.addActionListener(new LNFSetter(defaultLookAndFeel, bDefault));
    bg.add(bDefault);
    cp.add(bDefault);

    (previousButton = bDefault).setSelected(true);

    theFrame.pack();
    theFrame.setVisible(true);
  }

  /* Class to set the Look and Feel on a frame */
  class LNFSetter implements ActionListener {
    String theLNFName;

    JRadioButton thisButton;

    /** Called to setup for button handling */
    LNFSetter(String lnfName, JRadioButton me) {
      theLNFName = lnfName;
      thisButton = me;
    }

    /** Called when the button actually gets pressed. */
    public void actionPerformed(ActionEvent e) {
      try {
        UIManager.setLookAndFeel(theLNFName);
        SwingUtilities.updateComponentTreeUI(theFrame);
        theFrame.pack();
      } catch (Exception evt) {
        JOptionPane.showMessageDialog(null,
            "setLookAndFeel didn't work: " + evt, "UI Failure",
            JOptionPane.INFORMATION_MESSAGE);
        previousButton.setSelected(true); // reset the GUI to agree
      }
      previousButton = thisButton;
    }
  }

  public static void main(String[] argv) {
    new LNFSwitcher();
  }
}