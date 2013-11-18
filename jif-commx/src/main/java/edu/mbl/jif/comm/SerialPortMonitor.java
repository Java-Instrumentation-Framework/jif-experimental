package edu.mbl.jif.comm;

import edu.mbl.jif.utils.convert.HexString;
import java.io.IOException;
import java.util.Stack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;



/**
 *   Serial Port IO Monitor panel
 */

public class SerialPortMonitor
      extends JPanel
{
   JScrollPane scrollPaneMsg = new JScrollPane();
   public JTextPane messageArea = new JTextPane();
   JPanel panelInput = new JPanel();
   JButton buttonSend = new JButton();
   JButton buttonClear = new JButton();
   JTextField commandField = new JTextField();
   boolean displayASCII = true;
   JButton buttonSend1 = new JButton();
   public JPanel panelButton = new JPanel();
   public JPanel panelControl = new JPanel();

   public char CHAR_LF = 0xA; // '\n' = LF = 10 = 0xA
   public char CHAR_CR = 0xD; // '\r' = CR = 13 = 0xD
   private char terminatorRecv = CHAR_LF;
   private char terminatorSend = CHAR_LF;
   public String history;
   public Stack historyStack = new Stack();
   BorderLayout borderLayout1 = new BorderLayout();
   FlowLayout flowLayout1 = new FlowLayout();

// Best to reuse attribute sets as much as possible.
   static SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();
   static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();
   static SimpleAttributeSet BLACK = new SimpleAttributeSet();
   static {
      StyleConstants.setForeground(ITALIC_GRAY, Color.blue);
      StyleConstants.setItalic(ITALIC_GRAY, true);
      StyleConstants.setFontFamily(ITALIC_GRAY, "Helvetica");
      StyleConstants.setFontSize(ITALIC_GRAY, 12);

      StyleConstants.setForeground(BOLD_BLACK, Color.black);
      StyleConstants.setBold(BOLD_BLACK, true);
      StyleConstants.setFontFamily(BOLD_BLACK, "Helvetica");
      StyleConstants.setFontSize(BOLD_BLACK, 12);

      StyleConstants.setForeground(BLACK, Color.black);
      StyleConstants.setFontFamily(BLACK, "Helvetica");
      StyleConstants.setFontSize(BLACK, 12);
   }


   SerialPortConnection port;

   public SerialPortMonitor (SerialPortConnection _port) {
      port = _port;
      port.setMonitor(this);
      try {
         jbInit();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   void jbInit () throws Exception {
      this.setLayout(borderLayout1);
      messageArea.setMargin(new Insets(5, 5, 5, 5));
      messageArea.setText("");
      messageArea.setEditable(false);
      ActionListener actionListenerenter = new ActionListener()
      {
         public void actionPerformed (ActionEvent actionEvent) {
            sendString();
         }
      };
      ActionListener actionListenerup = new ActionListener()
      {
         public void actionPerformed (ActionEvent actionEvent) {
            if (!historyStack.isEmpty()) {
               String lastOne = (String) historyStack.pop();
               commandField.setText(lastOne);
            }
         }
      };
      commandField.setPreferredSize(new Dimension(200, 21));
      KeyStroke enterkey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
      KeyStroke upkey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
      commandField.registerKeyboardAction(actionListenerenter, enterkey,
            JComponent.WHEN_IN_FOCUSED_WINDOW);
      commandField.registerKeyboardAction(actionListenerup, upkey,
            JComponent.WHEN_IN_FOCUSED_WINDOW);
      //
      buttonSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
      buttonSend.setMargin(new Insets(2, 6, 2, 6));
      buttonSend.setText("Send");
      buttonSend.addActionListener(
            new SerialPortMonitor_buttonSend_actionAdapter(this));
      buttonSend1.addActionListener(
            new SerialPortMonitor_buttonSend1_actionAdapter(this));
      buttonSend1.setText("Send char");
      buttonSend1.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
      buttonSend1.setMargin(new Insets(2, 6, 2, 6));
      buttonClear.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
      buttonClear.setMargin(new Insets(2, 4, 2, 4));
      buttonClear.setText("Clear Display");
      buttonClear.addActionListener(
            new SerialPortMonitor_buttonClear_actionAdapter(this));
      panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.Y_AXIS));
      panelControl.add(commandField);
      panelControl.add(panelButton);

      panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.X_AXIS));
      panelButton.setPreferredSize(new Dimension(30, 36));
      panelButton.add(buttonSend);
      panelButton.add(Box.createRigidArea(new Dimension(10, 0)));
      panelButton.add(buttonSend1);
      panelButton.add(Box.createRigidArea(new Dimension(10, 0)));
      panelButton.add(buttonClear);

      //
      scrollPaneMsg.getViewport().add(messageArea, null);
      this.add(panelControl, java.awt.BorderLayout.NORTH);
      this.add(scrollPaneMsg, java.awt.BorderLayout.CENTER);
      panelControl.add(panelInput);
      panelControl.add(panelButton);
      panelInput.add(commandField, null);
      this.setPreferredSize(new Dimension(500, 356));
   }


   public void sent (final String msg) {
      insertText(" " + msg.trim() + "\n", BLACK);
      // remove following line if you don't want the monitor
      // to clear the commandfield after hitting the send button
      commandField.setText("");
   }


   public void setDisplayASCII (boolean t) {
      displayASCII = t;
   }


   public void received (final String msg) {
      insertText(" " + msg.trim() + "\n", ITALIC_GRAY);
   }


   public void received (byte[] data, int offset, int len) {
      HexString hexs = new HexString();
      String s = "";
      if (displayASCII) {
         try {
            //String ascii = new String(data, 0, len, "ANSI_X3.4-1968");
            String ascii = new String(data, 0, len, "ASCII");
            s += ascii;
         }
         catch (IOException eio) {
            s += eio.toString();
         }
      } else {
         /** @todo add DisplayHEX option in the UI */
         //s += "\nhex: " + hexs.toHexString(data, 0, len);
      }
      received(s);
   }


   protected void insertText (final String text, final AttributeSet set) {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            try {
               messageArea.getDocument().insertString(
                     messageArea.getDocument().getLength(), text, set);
            }
            catch (BadLocationException e) {
               e.printStackTrace();
            }
            setEndSelection();
         }
      });
   }


   protected void setEndSelection () {
      messageArea.setSelectionStart(messageArea.getDocument().getLength());
      messageArea.setSelectionEnd(messageArea.getDocument().getLength());
   }


//---------------------------------------------------------------------------

   void buttonSend_actionPerformed (ActionEvent e) {
      sendString();
   }


   /**
    * sendString
    */
   public void sendString () {
      String ss = commandField.getText();
      history = commandField.getText();
      historyStack.push(history);
      try {
        // port.sendString(ss + terminatorSend);
         port.sendString(ss);

      }
      catch (IOException ex) {}
   }


   void buttonSend1_actionPerformed (ActionEvent e) {
      String ss = commandField.getText();
      char[] s = ss.toCharArray();
      try {
         port.sendChars(s);
      }
      catch (IOException ex) {}
   }


   void buttonClear_actionPerformed (ActionEvent e) {
      messageArea.setText("");
   }


//---------------------------------------------------------------------------
   public static void main (String[] args) {
//    SerialPortConnection port = new  SerialPortConnection();
//    SerialPortMonitor p = new SerialPortMonitor(port);
//    FrameTest ft = new FrameTest( (JPanel) p);
//    p.sent("test Send");
//    p.received("test Recv");
   }


   void buttonShowQ_actionPerformed (ActionEvent e) {
   //   org.pf.joi.Inspector.inspect(port.messageQueue.queue);
   }
}



//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
class SerialPortMonitor_buttonSend_actionAdapter
      implements java.awt.event.ActionListener
{
   SerialPortMonitor adaptee;

   SerialPortMonitor_buttonSend_actionAdapter (SerialPortMonitor adaptee) {
      this.adaptee = adaptee;
   }


   public void actionPerformed (ActionEvent e) {
      adaptee.buttonSend_actionPerformed(e);
   }
}



class SerialPortMonitor_buttonSend1_actionAdapter
      implements java.awt.event.ActionListener
{
   SerialPortMonitor adaptee;

   SerialPortMonitor_buttonSend1_actionAdapter (SerialPortMonitor adaptee) {
      this.adaptee = adaptee;
   }


   public void actionPerformed (ActionEvent e) {
      adaptee.buttonSend1_actionPerformed(e);
   }
}



class SerialPortMonitor_buttonClear_actionAdapter
      implements java.awt.event.ActionListener
{
   SerialPortMonitor adaptee;

   SerialPortMonitor_buttonClear_actionAdapter (SerialPortMonitor adaptee) {
      this.adaptee = adaptee;
   }


   public void actionPerformed (ActionEvent e) {
      adaptee.buttonClear_actionPerformed(e);
   }
}
