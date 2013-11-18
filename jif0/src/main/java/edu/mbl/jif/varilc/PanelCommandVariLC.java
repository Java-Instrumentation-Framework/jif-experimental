package edu.mbl.jif.varilc;

import edu.mbl.jif.varilc.camacq.VariLC_RT;
import edu.mbl.jif.utils.color.JifColor;
import edu.mbl.jif.gui.dialog.DialogBoxI;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import edu.mbl.jif.gui.*;



public class PanelCommandVariLC
      extends JPanel
{
   BorderLayout borderLayout1 = new BorderLayout();

   private JButton button_CheckStatus = new JButton();
   private JButton button_Exercise = new JButton();
   private JButton button_Initialize = new JButton();
   private JButton button_Reset = new JButton();
   private JButton button_Write = new JButton();
   private JScrollPane jScrollPane1 = new JScrollPane();
   private JLabel label_Mode = new JLabel();
   private JComboBox comboBox_Mode = new JComboBox();
   private JToggleButton toggle_Monitor = new JToggleButton();
   public JTextPane textPane = new JTextPane();
   private JTextArea text_ToSend = new JTextArea();
   FlowLayout flowLayout2 = new FlowLayout();

   VariLC_RT vlc;

   public PanelCommandVariLC () {
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {

      button_Initialize.setBounds(new Rectangle(14, 97, 99, 28));
      button_Initialize.setText("Re-Initialize");
      button_Initialize.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Initialize_actionPerformed(e);
         }
      });

      // Monitor
      toggle_Monitor.setMargin(new Insets(2, 4, 2, 4));
      toggle_Monitor.setText("Monitor I/O");
      toggle_Monitor.setBounds(new Rectangle(176, 13, 99, 28));
      toggle_Monitor.addChangeListener(new javax.swing.event.ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            toggle_Monitor_stateChanged(e);
         }
      });

      // Mode
      label_Mode.setText("Mode");
      label_Mode.setBounds(new Rectangle(18, 190, 41, 15));
      comboBox_Mode.addItem("Normal");
      comboBox_Mode.addItem("Brief");
      comboBox_Mode.addItem("Auto");
      comboBox_Mode.setToolTipText("Response Mode");
      comboBox_Mode.setBounds(new Rectangle(16, 208, 75, 24));
      comboBox_Mode.setVerifyInputWhenFocusTarget(false);
      comboBox_Mode.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            comboBox_Mode_actionPerformed(e);
         }
      });
      // Check
      button_CheckStatus.setBounds(new Rectangle(14, 13, 99, 28));
      button_CheckStatus.setMargin(new Insets(2, 4, 2, 4));
      button_CheckStatus.setText("Check Status");
      button_CheckStatus.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_CheckStatus_actionPerformed(e);
         }
      });
      button_Reset.setText("Reset");
      button_Reset.setMargin(new Insets(2, 4, 2, 4));
      button_Reset.setToolTipText("");
      button_Reset.setBounds(new Rectangle(14, 56, 99, 28));
      button_Reset.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Reset_actionPerformed(e);
         }
      });
      button_Exercise.setBounds(new Rectangle(14, 140, 99, 28));
      button_Exercise.setToolTipText("");
      button_Exercise.setMargin(new Insets(2, 4, 2, 4));
      button_Exercise.setText("Exercise LCs");
      button_Exercise.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Exercise_actionPerformed(e);
         }
      });

      text_ToSend.setBorder(BorderFactory.createLoweredBevelBorder());
      text_ToSend.setBounds(new Rectangle(175, 252, 148, 22));
      jScrollPane1.getViewport().add(textPane, null);
      jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      jScrollPane1.setBounds(new Rectangle(175, 52, 206, 191));
      button_Write.setBounds(new Rectangle(331, 251, 51, 25));
      button_Write.setMargin(new Insets(2, 4, 2, 4));
      button_Write.setText("Send");
      button_Write.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Write_actionPerformed(e);
         }
      });

      this.setLayout(null);
      this.setBackground(JifColor.yellow[2]);
      this.add(jScrollPane1, null);
      this.add(text_ToSend, null);
      this.add(button_Write, null);
      this.add(toggle_Monitor, null);
      this.add(button_Reset, null);
      this.add(button_Initialize, null);
      this.add(button_Exercise, null);
      this.add(label_Mode, null);
      this.add(comboBox_Mode, null);
      this.add(button_CheckStatus, null);
      this.validate();
   }


   //////////////////////////////////////////////////////////////////////
// On Command Tab --
//
   void toggle_Monitor_stateChanged (ChangeEvent e) {
//    if (PSj.variLC != null) {
      if (toggle_Monitor.isSelected()) {
//         // FrameApp frame = new FrameApp();(this);
      } else {
//        clearMonitor();
      }
//    }
   }


   void button_Write_actionPerformed (ActionEvent e) {
      String ss = text_ToSend.getText();
      vlc.sendCommandAndWait(ss, 250);
   }


   void button_WriteChar_actionPerformed (ActionEvent e) {
      String ss = text_ToSend.getText();
      vlc.sendChar(ss.toCharArray());
   }


   void button_Busy_actionPerformed (ActionEvent e) {
      vlc.busyCheck();
   }


   void button_StatusChar_actionPerformed (ActionEvent e) {
      vlc.statusCheck();
   }


// Mode
   void comboBox_Mode_actionPerformed (ActionEvent e) {
      if (comboBox_Mode.getSelectedItem() == "Normal") {
         vlc.setMode(0);
      }
      if (comboBox_Mode.getSelectedItem() == "Brief") {
         vlc.setMode(1);
      }
      if (comboBox_Mode.getSelectedItem() == "Auto") {
         vlc.setMode(2);
      }
      System.out.println("Mode set to: " + comboBox_Mode.getSelectedItem());
   }


//////////////////////////////////////////////////////////////////////
// VariLC initialization & settings
//
   void button_Initialize_actionPerformed (ActionEvent e) {
      vlc.initialize();
   }


   void button_Reset_actionPerformed (ActionEvent e) {
      vlc.reset();
   }


   void button_Exercise_actionPerformed (ActionEvent e) {
      vlc.exercise();
   }


   void button_CheckStatus_actionPerformed (ActionEvent e) {
      if (!vlc.statusCheck()) {
         DialogBoxI.boxError("VariLC Status", "Error!");
      }
   }


   void button_Responsive_actionPerformed (ActionEvent e) {
      boolean resp = vlc.checkIfResponsive();
      if (resp) {
         DialogBoxI.boxError("VariLC Status", "Is Responsive.");
      } else {
         DialogBoxI.boxError("VariLC Status", "Is NOT Responsive.");
      }
   }

}
