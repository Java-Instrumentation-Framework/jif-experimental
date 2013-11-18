package edu.mbl.jif.gui.text;

import edu.mbl.jif.gui.test.FrameForTesting;
import edu.mbl.jif.utils.JifUtils;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JPanel {

  JMenuBar menuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileExit = new JMenuItem();
  JMenu menuHelp = new JMenu();
  JMenuItem menuHelpAbout = new JMenuItem();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  ImageIcon imageSave;
  ImageIcon imageCancel;
  JLabel statusBar = new JLabel();
  //
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea1 = new JTextArea();
  JFileChooser jFileChooser1 = new JFileChooser();
  String currFileName = null;  // Full path with filename. null means new/untitled.
  boolean dirty = false;
  Document document1;
  private JPanel panel_FileName = new JPanel();
  private JLabel label_FileName = new JLabel();

  //Construct the frame
  public TextEditor() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
      updateCaption();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    FrameForTesting f = new FrameForTesting();
    TextEditor te = new TextEditor();
    te.setPreferredSize(new Dimension(300, 300));
    f.addContents(te);
    f.setVisible(true);

  }
  //Component initialization

  private void jbInit() throws Exception {
    /**
     * @todo
     */
    //imageSave = JifUtils.loadImageIcon("save24.gif", TextEditor.class);
    jButton2.setIcon(imageSave);
    jButton2.setMargin(new Insets(0, 0, 0, 0));
    jButton2.addActionListener(new TextEditFrame_jButton2_actionAdapter(this));
    jButton2.setToolTipText("Save File");
    //
    //imageCancel = jif.utils.PSjUtils.loadImageIcon("save24.gif");
    jButton3.setIcon(imageCancel);
    jButton3.addActionListener(new TextEditFrame_jButton3_actionAdapter(this));
    jButton3.setToolTipText("Cancel & Exit");
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    statusBar.setAlignmentX((float) 0.5);
    statusBar.setMaximumSize(new Dimension(9999, 24));
    statusBar.setMinimumSize(new Dimension(0, 24));
    statusBar.setHorizontalAlignment(SwingConstants.LEFT);
    statusBar.setText(" ");
    document1 = jTextArea1.getDocument();
    jTextArea1.setLineWrap(true);
    jTextArea1.setWrapStyleWord(true);
    jTextArea1.setBackground(Color.white);
    jTextArea1.setMargin(new Insets(4, 4, 4, 4));
    label_FileName.setHorizontalAlignment(SwingConstants.LEFT);
    label_FileName.setText("-");
    document1.addDocumentListener(new TextEditFrame_document1_documentAdapter(
            this));
    panel_FileName.setMaximumSize(new Dimension(9999, 24));
    panel_FileName.setMinimumSize(new Dimension(0, 24));
    panel_FileName.add(label_FileName, null);
    panel_FileName.add(jButton2, null);
    jScrollPane1.getViewport()
            .add(jTextArea1, null);
    add(panel_FileName);
    add(jScrollPane1);
    //add(statusBar);
  }

  // Handle the File|Open menu or button, invoking okToAbandon and openFile
  // as needed.
  void fileOpen() {
    if (!okToAbandon()) {
      return;
    }

    // Use the OPEN version of the dialog, test return for Approve/Cancel
    if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(this)) {
      // Call openFile to attempt to load the text from file into TextArea
      openFile(jFileChooser1.getSelectedFile().getPath());
    }
    this.repaint();
  }

  // Open named file; read text from file into jTextArea1; report to statusBar.
  public boolean openFile(String fileName) {
    try {
      // Open a file of the given name.
      File file = new File(fileName);

      // Get the size of the opened file.
      int size = (int) file.length();

      // Set to zero a counter for counting the number of
      // characters that have been read from the file.
      int chars_read = 0;

      // Create an input reader based on the file, so we can read its data.
      // FileReader handles international character encoding conversions.
      FileReader in = new FileReader(file);

      // Create a character array of the size of the file,
      // to use as a data buffer, into which we will read
      // the text data.
      char[] data = new char[size];

      // Read all available characters into the buffer.
      while (in.ready()) {
        // Increment the count for each character read,
        // and accumulate them in the data buffer.
        chars_read += in.read(data, chars_read, size - chars_read);
      }
      in.close();
      // Create a temporary string containing the data,
      // and set the string into the JTextArea.
      jTextArea1.setText(new String(data, 0, chars_read));
      // Cache the currently opened filename for use at save time...
      this.currFileName = fileName;
      // ...and mark the edit session as being clean
      this.dirty = false;
      // Display the name of the opened directory+file in the statusBar.
      statusBar.setText("Opened " + fileName);
      updateCaption();
      return (true);
    } catch (IOException e) {
      statusBar.setText("Error opening " + fileName);
      return (false);
    }
  }

  // Save current file; handle not yet having a filename; report to statusBar.
  boolean saveFile() {
    // Handle the case where we don't have a file name yet.
    if (currFileName == null) {
      return saveAsFile();
    }
    try {
      // Open a file of the current name.
      File file = new File(currFileName);

      // Create an output writer that will write to that file.
      // FileWriter handles international characters encoding conversions.
      FileWriter out = new FileWriter(file);
      String text = jTextArea1.getText();
      out.write(text);
      out.close();
      this.dirty = false;
      // Display the name of the saved directory+file in the statusBar.
      /**
       * @todo
       */
      //jif.utils.PSjUtils.event("LogNotes saved: " + currFileName);
      statusBar.setText("Saved to " + currFileName);
      updateCaption();
      return true;
    } catch (IOException e) {
      statusBar.setText("Error saving " + currFileName);
    }
    return false;
  }

  // Save current file, asking user for new destination name.
  // Report to statuBar.
  boolean saveAsFile() {
    this.repaint();
    // Use the SAVE version of the dialog, test return for Approve/Cancel
    if (JFileChooser.APPROVE_OPTION == jFileChooser1.showSaveDialog(this)) {
      // Set the current file name to the user's selection,
      // then do a regular saveFile
      currFileName = jFileChooser1.getSelectedFile()
              .getPath();
      //repaints menu after item is selected
      this.repaint();
      return saveFile();
    } else {
      this.repaint();
      return false;
    }
  }

  // Check if file is dirty.
  // If so get user to make a "Save? yes/no/cancel" decision.
  boolean okToAbandon() {
    if (!dirty) {
      return true;
    }
    int value =
            JOptionPane.showConfirmDialog(this, "Save changes?", "Text Edit",
            JOptionPane.YES_NO_CANCEL_OPTION);
    switch (value) {
      case JOptionPane.YES_OPTION:  // yes, please save changes
        return saveFile();
      case JOptionPane.NO_OPTION:  // no, abandon edits
        // i.e. return true without saving
        return true;
      case JOptionPane.CANCEL_OPTION:
      default:  // cancel
        return false;
    }
  }

  // Update the caption of the application to show the filename and its dirty state.
  void updateCaption() {
    String caption;
    if (currFileName == null) {
      // synthesize the "Untitled" name if no name yet.
      caption = "Untitled";
    } else {
      caption = currFileName;
    }

    // add a "*" in the caption if the file is dirty.
    if (dirty) {
      caption = "* " + caption;
    }

    //caption = "Text Editor - " + caption;
    label_FileName.setText(caption);
  }

  //File | Exit action performed
  public void fileExit_actionPerformed(ActionEvent e) {
    if (okToAbandon()) {
      System.exit(0);
    }
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    //super.processWindowEvent(e);
    //if (e.getID() == WindowEvent.WINDOW_CLOSING) {
    //     fileExit_actionPerformed(null);
    //}
  }

  void jMenuItem6_actionPerformed(ActionEvent e) {
    // Handle the "Foreground Color" menu item
    Color color =
            JColorChooser.showDialog(this, "Foreground Color",
            jTextArea1.getForeground());
    if (color != null) {
      jTextArea1.setForeground(color);
    }

    //repaints menu after item is selected
    this.repaint();
  }

  void jMenuItem7_actionPerformed(ActionEvent e) {
    // Handle the "Background Color" menu item
    Color color =
            JColorChooser.showDialog(this, "Background Color",
            jTextArea1.getBackground());
    if (color != null) {
      jTextArea1.setBackground(color);
    }

    //repaints menu after item is selected
    this.repaint();
  }

  void jMenuItem1_actionPerformed(ActionEvent e) {
    // Handle the File|New menu item.
    if (okToAbandon()) {
      // clears the text of the TextArea
      jTextArea1.setText("");
      // clear the current filename and set the file as clean:
      currFileName = null;
      dirty = false;
      updateCaption();
    }
  }

  void jMenuItem2_actionPerformed(ActionEvent e) {
    //Handle the File|Open menu item.
    fileOpen();
  }

  void jMenuItem3_actionPerformed(ActionEvent e) {
    //Handle the File|Save menu item.
    saveFile();
  }

  void jMenuItem4_actionPerformed(ActionEvent e) {
    //Handle the File|Save As menu item.
    saveAsFile();
  }

  void jButton3_actionPerformed(ActionEvent e) {
    //Handle tool bar Open button
    // Cancel
    //fileOpen();
  }

  void jButton2_actionPerformed(ActionEvent e) {
    //Handle tool bar Save button
    saveFile();
  }

  void document1_changedUpdate(DocumentEvent e) {
    if (!dirty) {
      dirty = true;
      updateCaption();
    }
  }

  void document1_insertUpdate(DocumentEvent e) {
    if (!dirty) {
      dirty = true;
      updateCaption();
    }
  }

  void document1_removeUpdate(DocumentEvent e) {
    if (!dirty) {
      dirty = true;
      updateCaption();
    }
  }
}

///////////////////////////////////////////////////////////////////////////////
class TextEditFrame_menuFileExit_ActionAdapter
        implements ActionListener {

  TextEditor adaptee;

  TextEditFrame_menuFileExit_ActionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.fileExit_actionPerformed(e);
  }
}

class TextEditFrame_jMenuItem6_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jMenuItem6_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem6_actionPerformed(e);
  }
}

class TextEditFrame_jMenuItem7_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jMenuItem7_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem7_actionPerformed(e);
  }
}

class TextEditFrame_jMenuItem1_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jMenuItem1_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem1_actionPerformed(e);
  }
}

class TextEditFrame_jMenuItem2_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jMenuItem2_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem2_actionPerformed(e);
  }
}

class TextEditFrame_jMenuItem3_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jMenuItem3_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem3_actionPerformed(e);
  }
}

class TextEditFrame_jMenuItem4_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jMenuItem4_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem4_actionPerformed(e);
  }
}

class TextEditFrame_jButton3_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jButton3_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton3_actionPerformed(e);
  }
}

class TextEditFrame_jButton2_actionAdapter
        implements java.awt.event.ActionListener {

  TextEditor adaptee;

  TextEditFrame_jButton2_actionAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
  }
}

class TextEditFrame_document1_documentAdapter
        implements javax.swing.event.DocumentListener {

  TextEditor adaptee;

  TextEditFrame_document1_documentAdapter(TextEditor adaptee) {
    this.adaptee = adaptee;
  }

  public void changedUpdate(DocumentEvent e) {
    adaptee.document1_changedUpdate(e);
  }

  public void insertUpdate(DocumentEvent e) {
    adaptee.document1_insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) {
    adaptee.document1_removeUpdate(e);
  }
}
