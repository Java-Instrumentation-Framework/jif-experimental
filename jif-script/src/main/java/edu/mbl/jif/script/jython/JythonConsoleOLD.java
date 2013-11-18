package edu.mbl.jif.script.jython;

/*
 * Jython Console
 * Copyright (C) 2004 Benjamin Jung <bpjung@terreon.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.python.util.InteractiveConsole;


/**
 * This simple Jython Console offers the basic functionality that you would
 * expect of Python running in interactive mode.
 * @author Benjamin P. Jung
 * @version 1.0, 05/19/04
 */
public class JythonConsoleOLD extends JFrame {

    public final static Color BACKGROUND = Color.WHITE;
    public final static Color FOREGROUND = Color.BLACK;

    protected InteractiveConsole console;
    protected JPanel contentPanel;
    protected OutputPanel output;
    protected JTextField input;
    protected JButton enterButton;

    /**
     * Creates a new Jython Console.
     */
    public JythonConsoleOLD() {
        // Sets the title for this frame.
        setTitle("Jython Console");

        URL imgURL = JythonConsoleOLD.class.getResource("icon.png");
        if (imgURL != null) {
            setIconImage(new ImageIcon(imgURL).getImage());
        }

        // Sets the default close operation for this frame.
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ActionListener myActionListener = new ActionListener() {
            /* (non-Javadoc)
             * @see ActionListener#actionPerformed(ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                console.push(input.getText() + "\n");
                input.setText("");
                input.requestFocus();
            }
        };
        // Instantiates the actual content panel.
        contentPanel = new JPanel();
        // Sets the layout of the content panel.
        contentPanel.setLayout(new GridBagLayout());
        // Adds the content panel to the frame.
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        // Creates constraints for the grid bag layout.
        GridBagConstraints constraints = new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(4, 4, 2, 4),
                4, 4
        );
        // Instantiates the output panel.
        output = new OutputPanel();
        // Modifies the constraints to fit the output panel's needs.
        constraints.gridwidth = 2;
        // Adds the output panel onto the content panel.
        contentPanel.add(output, constraints);
        // Instantiates the input panel.
        input = new JTextField();
        // Sets the foreground color of the text pane.
        input.setForeground(FOREGROUND);
        // Sets the background color of the text pane.
        input.setBackground(BACKGROUND);
        // Sets the border of the text pane.
        input.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        // Modifies the constraints to fit the input panel's needs.
        input.addActionListener(myActionListener);
        constraints.gridy = 1; constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 1; constraints.insets = new Insets(2, 4, 4, 2);
        // Adds the input panel onto the content panel.
        contentPanel.add(input, constraints);
        // Instantiates the enter button.
        enterButton = new JButton("Enter");
        // Sets an mnemonic for the button.
        enterButton.setMnemonic(KeyEvent.VK_E);
        // Adds a new action listener to the button.
        enterButton.addActionListener(myActionListener);
        // Modifies the constraints to fit the button's needs.
        constraints.gridx = 1; constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0; constraints.insets = new Insets(4, 4, 4, 4);
        // Adds the "enter" buttons onto the content panel.
        contentPanel.add(enterButton, constraints);
        // Instantiates the Jython Interactive Console.
        console = new InteractiveConsole();
        // Instantiates the console writer.
        ConsoleWriter consoleWriter = new ConsoleWriter();
        // Sets stdout of the Jython interactive console.
        console.setOut(consoleWriter);
        // Sets stdedd of the Jython interactive console.
        console.setErr(consoleWriter);
        // Sets the initial size of the dialog.
        setSize(new Dimension(600, 400));
    }

    /**
     * @author Benjamin P. Jung
     * @version 1.0, 05/19/04
     * @since JythonConsole 1.0
     */
    private class ConsoleWriter extends Writer {
        /* (non-Javadoc)
         * @see java.io.Writer#close()
         */
        public void close() throws IOException { /* ... */ }
        /* (non-Javadoc)
         * @see java.io.Writer#flush()
         */
        public void flush() throws IOException { /* ... */ }
        /* (non-Javadoc)
         * @see java.io.Writer#write(char[], int, int)
         */
        public void write(char[] cbuf, int off, int len) throws IOException {
            output.append(new String(cbuf, off, len));

        }
    }

    /**
     * The <tt>OutputPanel</tt> is nothing more than an extended
     * <tt>JScrollPane</tt> that takes hold of a very ordinary
     * <tt>JTextPane</tt>.
     * @author	Benjamin P. Jung
     * @version	1.0, 05/19/04
     * @since	JythonConsole 1.0
     */
    private class OutputPanel extends JScrollPane {
        // The text pane for the output text to be displayed.
        private JTextPane outputText;
        /**
         * Creates a new <tt>OutputPanel</tt>
         */
        public OutputPanel() {
            outputText = new JTextPane();
            // Sets the output text pane to be non-editable.
            outputText.setEditable(false);
            // Sets the foreground color of the text pane.
            outputText.setForeground(FOREGROUND);
            // Sets the background color of the text pane.
            outputText.setBackground(BACKGROUND);
            // Sets the viewport view of the scroll pane.
            setViewportView(outputText);
            // Sets the border of the text pane.
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        /**
         * Clears the text pane.
         */
        public void clear() {
            outputText.setText("");
        }
        public void append(String string) {
            Document doc = outputText.getDocument();
            try {
                // Inserts the string.
                doc.insertString(doc.getLength(), string, null);
            } catch (BadLocationException e) { /* ... */ }
            // Sets the caret / scrolls to the bottom of the text pane.
            outputText.setCaretPosition(doc.getLength());

        }
        /**
         * @return	The <tt>Document</tt> of the output's <tt>JTextPane</tt>.
         */
        public Document getDocument() {
            return outputText.getDocument();
        }
    }

    /**
     * M A I N   L O O P
     * The main method here is mainly used for testing and demonstration
     * purposes. Usually you surely want to implement the Jython Console in
     * your project and not let it run stand-alone.
     * @param	args The command line arguments.
     */
    public final static void main (String args[]) {
        // Instantiate a new Jython Console and show it.
        new JythonConsoleOLD().setVisible(true);
    }
}
