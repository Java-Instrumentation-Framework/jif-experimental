package edu.mbl.jif.script.jython;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.python.core.*;
import org.python.util.*;

/** @todo DELETE THIS */

public class JythonEvaluator
    extends JFrame {

  public PythonInterpreter interp;
  String startFile;
  JTextField textField;
  ScriptConsole sConsole;

  public JythonEvaluator(String startup) {
    this.startFile = startup;
    this.setTitle("Jython Interpreter");
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        sConsole.windowClosed();
      }
    });
    this.getContentPane().setLayout(new BorderLayout());
    PySystemState.initialize();
    //interp = new PythonInterpreter();
    interp = new org.python.util.InteractiveConsole();
    sConsole = new ScriptConsole(interp, "Jython Interpreter");
    sConsole.setPreferredSize(new Dimension(350, 300));
    sConsole.setVisible(true);
    this.getContentPane().add(sConsole, BorderLayout.CENTER);
    this.textField = new JTextField();
    this.textField.setPreferredSize(new Dimension(350, 20));
    this.getContentPane().add(this.textField, BorderLayout.SOUTH);
    this.textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          interp.exec("print \">>> " +
                                   textField.getText() +
                                   "\"");
          interp.exec(textField.getText());
        }
        catch (PyException pye) {
            Py.printException(pye);
        }
        textField.setText("");
      }
    });
    this.pack();
    setVisible(true);
  }
}
