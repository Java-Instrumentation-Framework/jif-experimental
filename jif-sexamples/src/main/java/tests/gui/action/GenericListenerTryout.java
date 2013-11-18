package tests.gui.action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GenericListenerTryout {

	JPanel root = new JPanel(new BorderLayout());
	JLabel label = new JLabel(" ");

	
	public void button1Action(ActionEvent e) {
		label.setText("button1Action - normal listener class ");
	}
	public void signalAcqStarted() {
		label.setText("Acq Started.");
	}
	public void signalAcqFinished() {
		label.setText("Acq Finished.");
	}

	public void button2Action(ActionEvent e) {
		label.setText("button2Action - dynamically generated listener class");
	}

	public void rootMouseEntered(MouseEvent e) {
		label.setText(e.toString());
	}

	GenericListenerTryout() {
		JButton button1 = new JButton("Button1: Normal Listener");
		JButton button2 = new JButton("Button2: Dynamic Listener");
		/* This listener is of the conventional variety.  It causes         
		 * a new (inner) class to be generated which the compiler         
		 * Demo$1.class.         */ 
		ActionListener button1ActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button1Action(e);
			}

		};
		button1.addActionListener(button1ActionListener);
		/*         * This listener will be generated at run-time, i.e. at run-time         
		 * an ActionListener class will be code-generated and then         
		 * class-loaded.  Only one of these is actually created, even         
		 * if many calls to GenericListener.create(ActionListener.class ...)         
		 * are made.         *         */
		ActionListener button2ActionListener = (ActionListener) (GenericListener.create(
				ActionListener.class, "actionPerformed", this, "button2Action"));
		button2.addActionListener(button2ActionListener);
		
		JPanel buttons = new JPanel();
		buttons.add(button1);
		buttons.add(button2);    
		
		
		/* Here's another dynamically generated listener.  This one is         
		 * a little different because the listenerMethod argument actually         
		 * specifies one of many listener methods.  In the previous example         
		 * "actionPerformed" named the one and only ActionListener method.         
		 *         */ 
		MouseListener rootMouseListener = (MouseListener) (GenericListener.create(
				MouseListener.class, "mouseEntered", this, "rootMouseEntered"));
		root.addMouseListener(rootMouseListener);
		root.add(buttons, BorderLayout.CENTER);
		root.add(label, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		};
		JFrame f = new JFrame("Demo");
		f.addWindowListener(l);
		f.getContentPane().add(new GenericListenerTryout().root, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	}

}