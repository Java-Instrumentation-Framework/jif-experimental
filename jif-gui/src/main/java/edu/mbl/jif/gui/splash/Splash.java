package edu.mbl.jif.gui.splash;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 *	Implements a basic splash screen for the Swing Application Framework.
 *	The splash screen will show for a fixed amount of time unles the user
 *	clicks the mouse on the window.
 *
 *	This code is based on code published by the Java Developer's Journal
 *  <http://www.sys-con.com/java/index2.html<
 *
 *	@version	1.0
 */

public class Splash extends JWindow
    implements KeyListener, MouseListener, ActionListener {

    /*
     *  JSplash constructs a splash screen (JWindow).
     *		parent is the parent frame for the window
     *		filename is the JPEG/GIF file to show as the splash
     *		timeout is time in milliseconds to display the splash
     */
    public Splash(JFrame parent, String filename, int timeout) {
        super(parent);

        // Note, this code does no error checking
        ImageIcon image = new ImageIcon(".\\icons\\psjsplash.gif");
        // The splash will be centered on the screen
        int w = image.getIconWidth() + 5;
        int h = image.getIconHeight() + 5;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - w) / 2;
        int y = (screen.height - h) / 2;
        setSize(w, h);
        setLocation(x, y);

        getContentPane().setLayout(new BorderLayout());
        JLabel picture = new JLabel(image);
        getContentPane().add("Center", picture);
        picture.setBorder(new BevelBorder(BevelBorder.RAISED));

        // Listen for key strokes
        addKeyListener(this);
        // Listen for mouse events from here and parent
        addMouseListener(this);
        parent.addMouseListener(this);
        // Timeout after a while
        Timer timer = new Timer(0, this);
        timer.setRepeats(false);
        timer.setInitialDelay(timeout);
        timer.start();
    }

    // This method is called in order to block the application until
    // the splash screen times out or is dismissed.
    // This is a bit of a kludge, actually, and the actual affect
    // varies based on platform.
    public void block() {
        while(isVisible()) {}
    }

    // Dismiss the window on a key press, ignore rest.
    public void keyTyped(KeyEvent event) {}
    public void keyReleased(KeyEvent event) {}
    public void keyPressed(KeyEvent event) {
        setVisible(false);
        dispose();
    }

    // Dismiss the window on a mouse click, ignore rest.
    public void mousePressed(MouseEvent event) {}
    public void mouseReleased(MouseEvent event) {}
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
    public void mouseClicked(MouseEvent event) {
        setVisible(false);
        dispose();
    }

    // Dismiss the window on a timeout
    public void actionPerformed(ActionEvent event) {
        setVisible(false);
        dispose();
    }
}

/*  ANOTHER APPROACH perhaps 1.6...
SplashScreen splash = SplashScreen.getSplashScreen();
Graphics2D g = splash.createGraphics();
//We are initializing our app
for (ApplicationInitStates s: app.getInitStates()) {
	String msg = ... //Determine states
	g.drawString(msg, 10, 50);
	splash.update();
	initApp(s);
}
g.dispose();
splash.close();
//=====================================================
public class MySplashScreen extends JWindow {
public MySplashScreen() {
//Place image in the center
URL url = new URL(""); // URL to the image
add(new JLabel(new ImageIcon(url)),
BorderLayout.CENTER);
//Place a progress bar at the bottom
progressBar =
new JProgressBar(JProgressBar.HORIZONTAL);
progressBar.setStringPainted(true);
add(progressBar, BorderLayout.SOUTH);
pack();
//Set the location of the splashscreen
...
}
}
(new MySplashScreen()).setVisible(true);
*/