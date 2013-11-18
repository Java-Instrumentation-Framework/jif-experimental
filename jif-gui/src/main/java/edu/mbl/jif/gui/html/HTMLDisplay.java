package edu.mbl.jif.gui.html;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.net.URL;
import java.io.*;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HTMLDisplay
      extends JScrollPane implements HyperlinkListener
{
   // for displaying the HTML help pages, forms the display area of the app
   private JEditorPane edit;

//******************************************************************************************
// Function Name : HTMLDisplay() => the default constructor
// Parameter : None
//
// Creates and adds a JEditorPane, adds HyperlinkListener to it, and displays the
// default help page in it.
//
//******************************************************************************************

     public HTMLDisplay () {
        edit = new JEditorPane();
        edit.setEditable(false);
        edit.addHyperlinkListener(this);

        getViewport().add(edit);

//     File file = new File("");
//     showURL("file:" + file.getAbsolutePath());
     }


//******************************************************************************************
// Function Name : hyperlinkUpdate(HyperlinkEvent e)
// Parameter : HyperlinkEvent e
// Returns : None
//
// Retrieves and displays a new URL whenever a  link in the HTML page is clicked. Method of
// HyperlinkListener.
//
//******************************************************************************************

     public void hyperlinkUpdate (HyperlinkEvent e) {
        if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
           // when a link is cliked page display is differed slightly to ensure
           // proper evet dispatch and paint
           Cursor c = edit.getCursor();
           Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
           edit.setCursor(waitCursor);
           SwingUtilities.invokeLater(new URLLoader(e.getURL(), c));
        }
     }


//******************************************************************************************
// Function Name : showURL(String url)
// Parameter : String url
// Returns : None
//
// Displays the URL passed to it in the JEditorPane, in case of error, shows error message box
//
//******************************************************************************************

     public void showURL (String url) {
        Document doc = edit.getDocument();
        try {
           edit.setPage(url);
        }
        catch (IOException e) {
           edit.setDocument(doc);
           System.out.println(e);
           JOptionPane.showMessageDialog(this, "Couldn't open page : " + url);
           getToolkit().beep();
        }
     }


//******************************************************************************************
// Inner Class : URLLoader
//
// The inner class which loads the URL in its run method. Implements runnable.
// Used with SwingUtilities.invokeLater() method
//
//******************************************************************************************

     class URLLoader
           implements Runnable
     {
        private URL url;
        private Cursor cursor;

        URLLoader (URL u, Cursor c) {
           this.url = u;
           this.cursor = c;
        }


        public void run () {
           //after PAINT is done
           if (null == url) {
              edit.setCursor(cursor);
           } else {
              showURL(url.toString());
              url = null;
              // to show normal cursor
              SwingUtilities.invokeLater(this);
           }
        }
     }
		 public static void main(String[] args) {
		
    JFrame f = new JFrame();
    HTMLDisplay hd = new HTMLDisplay();
    f.getContentPane().add(hd);
		try {
			URL url = new URL("http://www.mbl.edu");
			hd.showURL(url.toString());
		} catch (MalformedURLException ex) {
			Logger.getLogger(HTMLDisplay.class.getName()).log(Level.SEVERE, null, ex);
		}    
    f.setSize(new Dimension(680, 650));
    f.setVisible(true);
  
	}
}
