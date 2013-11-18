package tests.gui.frame;

import tests.gui.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class StayOnTopFrame {
	 JFrame f2;
   public StayOnTopFrame() {
      JFrame f = new JFrame();
      f.getContentPane().add(BorderLayout.CENTER, new JButton("hello"));
      f.setBounds(100,100,300, 100);
			JToolBar toolBar = new JToolBar();
			f.setVisible(true);
      f.setAlwaysOnTop(true);

      f2 = new JFrame();
      f2.getContentPane().add(BorderLayout.CENTER, new JButton("hello2"));
      f2.setBounds(100,160,300, 300);
      f2.setVisible(true);
			
			// Now make f2 become focused if f is selected...
		  f.addWindowFocusListener(new WindowAdapter() {
			 @Override
			 public void windowGainedFocus(WindowEvent e) {
				 super.windowGainedFocus(e);
				 f2.transferFocus();
			 }
		 });

      //new EscapeDialog(f);
   }

   public static void main(String[] args) {
      new StayOnTopFrame();
   }
}


class EscapeDialog extends JDialog {
   public EscapeDialog(JFrame owner) {
      super(owner);
      getContentPane().add(BorderLayout.CENTER, new JButton("helloDialog"));
      setModal(false);
      setSize(50, 45);
      setVisible(true);
   }
}
