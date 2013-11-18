package edu.mbl.jif.gui.sound;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AuditoryCues extends JFrame {

  public AuditoryCues() {
    super("Auditory Popups");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    UIManager.put("AuditoryCues.playList",
      UIManager.get("AuditoryCues.defaultCueList"));
    UIManager.put("OptionPane.questionSound",
      "sounds/OptionPaneError.wav");

    JPanel contentPane = (JPanel)this.getContentPane();
    JPanel center = new JPanel();
    ButtonGroup buttonGroup = new ButtonGroup();

    JRadioButton defaultAudio = new JRadioButton("Default", true);
    center.add(defaultAudio);
    buttonGroup.add(defaultAudio);
    defaultAudio.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        UIManager.put("AuditoryCues.playList",
          UIManager.get("AuditoryCues.defaultCueList"));
      }
    });

    JRadioButton offAudio = new JRadioButton("Off", false);
    center.add(offAudio);
    buttonGroup.add(offAudio);
    offAudio.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        UIManager.put("AuditoryCues.playList",
          UIManager.get("AuditoryCues.noAuditoryCues"));
      }
    });

    JRadioButton onAudio = new JRadioButton("On", false);
    center.add(onAudio);
    buttonGroup.add(onAudio);
    onAudio.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        UIManager.put("AuditoryCues.playList",
          UIManager.get("AuditoryCues.allAuditoryCues"));
      }
    });

    contentPane.add(center,  BorderLayout.CENTER);

    JButton confirmButton = new JButton("Confirmation Dialog");
    contentPane.add(confirmButton, BorderLayout.SOUTH);
    confirmButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(AuditoryCues.this,
          "Confirm?");
        if (result == JOptionPane.YES_OPTION) {
          JOptionPane.showMessageDialog(AuditoryCues.this, "Confirmed");
        } else {
          JOptionPane.showMessageDialog(AuditoryCues.this, "Rejected");
        }
      }
    });

    JButton messageButton = new JButton("Message Dialog");
    contentPane.add(messageButton, BorderLayout.NORTH);
    messageButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(AuditoryCues.this, "The Message");
      }
    });
    this.pack();
    show();
  }
  public static void main(String args[]) {
    new AuditoryCues();
  }
}
