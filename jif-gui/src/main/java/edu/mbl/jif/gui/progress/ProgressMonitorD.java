package edu.mbl.jif.gui.progress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ProgressMonitorD
      extends JDialog implements ActionListener
{
   public static final int PLAIN_TYPE = 0,
   CANCEL_TYPE = 1;
   JLabel messageLabel,
   noteLabel;
   JProgressBar theBar;
   JButton cancelButton;
   JPanel aPanel;
   int counter = 0;
   int min;
   int max;
   float percent = 0f;
   volatile boolean cancelled = false;


   public ProgressMonitorD (Frame owner, int min, int max, String title,
                            String text, String note, int type) {
      super(owner, title, false);
      this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      this.getContentPane().setLayout(new FlowLayout());
      this.setResizable(false);
      this.min = min;
      this.max = max;
      messageLabel = new JLabel(text);
      messageLabel.setVisible(true);
      noteLabel = new JLabel(note);
      noteLabel.setVisible(true);
      theBar = new JProgressBar(min, max);
      theBar.setVisible(true);
      this.getContentPane().add(messageLabel);
      this.getContentPane().add(noteLabel);
      this.getContentPane().add(theBar);

      if (type == CANCEL_TYPE) {
         aPanel = new JPanel(new FlowLayout());
         cancelButton = new JButton("Cancel");
         cancelButton.setVisible(true);
         cancelButton.addActionListener(this);
         aPanel.add(cancelButton);
         this.getContentPane().add(aPanel);
         this.setSize(300, 150);
      } else {
         this.setSize(300, 100);
      }
      if (owner != null) {
         this.setLocation((owner.getWidth() - this.getWidth()) / 2,
                          (owner.getHeight() - this.getHeight()) / 2);
      }
      this.setVisible(true);
      this.paintComponents(this.getGraphics());
   }


   public void setProgress (int newValue) {
      if (!cancelled) {
         theBar.setValue(newValue);
         float f = round((float) newValue / max * 100F, 1);
         this.setNote(f + "% completed");
         if (f > percent) {
            percent = f;
            this.paintComponents(this.getGraphics());
         }
      }
   }


   public boolean isCancelled () {
      return cancelled; }


   public void destroy () {
      this.dispose(); }


   public void setNote (String newNote) {
      if (!cancelled) {
         noteLabel.setText(newNote);
      }
   }


   public void setMessage (String newMessage) {
      if (!cancelled) {
         messageLabel.setText(newMessage);
      }
   }


   public void actionPerformed (ActionEvent e) {
      this.setMessage("Operation cancelled by user.");
      this.setNote("Please wait.....");
      this.setTitle("Cancelling.....");
      this.paintComponents(this.getGraphics());
      _cancel();
   }


   private void _cancel () {
      cancelled = true;
//    this.dispose();
   }


   private float round (float x, int decimalPlaces) {
      return (float) ((Math.round(x * Math.pow(10, decimalPlaces)))
      / Math.pow(10, decimalPlaces));
   }


   public static void main (String[] args) {
      ProgressMonitorD dpm = 
            new ProgressMonitorD(null, 0, 100, "Doin It", "Whoopee", "Note", 1);


   }

}
