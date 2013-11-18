/*
 * MaskInputSample.java
 *
 * Created on June 18, 2007, 3:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.FormattedInput;
/*
 *
CHARACTER    DESCRIPTION

#	Matches numeric character (Character.isDigit())
H	Matches hexadecimal number (0-9 + a-f + A-F)
A	Matches alphanumeric character (Character.is LetterOrDigit())
?	Matches alphabetic character (Character.isLetter())
U	Matches uppercase letter, maps lowercase to uppercase
L	Matches lowercase letter, maps uppercase to lowercase
*	Wildcard, match any character
'	Escape character to have literal strings/separators in input field.
 *
 */


import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class MaskInputSample {
  public static void main(String args[]) {
    Runnable runner = new Runnable() {
      public void run() {
        JFrame frame = new JFrame("Mask Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label;
        JFormattedTextField input;
        JPanel panel;
        MaskFormatter formatter;

        BoxLayout layout =
          new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(layout);

        try {
          label = new JLabel("SSN");
          formatter = new MaskFormatter("###'-##'-####");
          input = new JFormattedTextField(formatter);
          input.setValue("123-45-6789");
          input.setColumns(20);
          panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
          panel.add(label);
          panel.add(input);
          frame.add(panel);
        } catch (ParseException e) {
          System.err.println("Unable to add SSN");
        }

        try {
          label = new JLabel("US Phone");
          formatter = new MaskFormatter("'(###')' ###'-####");
          input = new JFormattedTextField(formatter);
          input.setColumns(20);
          panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
          panel.add(label);
          panel.add(input);
          frame.add(panel);
        } catch (ParseException e) {
          System.err.println("Unable to add Phone");
        }

        frame.pack();
        frame.setVisible(true);
      }
    };
    EventQueue.invokeLater(runner);
  }
}
