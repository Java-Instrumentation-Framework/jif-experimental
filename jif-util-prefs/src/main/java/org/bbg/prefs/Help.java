/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
public class Help extends JFrame implements ActionListener, HyperlinkListener {

    private JButton closeBtn;
    private JEditorPane htmlPane;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JScrollPane scrollPane;
    
    public Help() {
    	super("Help");
        topPanel = new JPanel(new BorderLayout());
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        htmlPane.setContentType("text/html");
        scrollPane = new JScrollPane(htmlPane);
        bottomPanel = new JPanel();
        closeBtn = new JButton("Close");

        topPanel.add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.CENTER);
        bottomPanel.add(closeBtn);
        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        
        int w = 512, h = 512, x0 = 0, y0 = 0;
        String prefs = Main.prefs.get("help", null);
        if (prefs != null) {
        	try {
        		String[] wh = prefs.split(",");
        		x0 = Integer.parseInt(wh[0]);
        		y0 = Integer.parseInt(wh[1]);
        		w = Integer.parseInt(wh[2]);
        		h = Integer.parseInt(wh[3]);
        	} catch (Exception ex) {
        		// ignore
        	}
        }
        setSize(w, h);
        setLocation(x0, y0);
        try {
            htmlPane.setPage(getClass().getResource("help.html"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        closeBtn.addActionListener(this);
        htmlPane.addHyperlinkListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
            	savePrefs();
            }
        });
    }

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeBtn) {
			savePrefs(); dispose();
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
			return;
		try {
			htmlPane.setPage(e.getURL());
		} catch (IOException ex) {
			htmlPane.setText("<html><body>Cannot fetch the URL:<p><b>" +
					ex.getMessage() + "</b></body></html");
		}
	}
	
	protected void savePrefs() {
		Point pt = getLocationOnScreen();
        Main.prefs.put("help", String.format("%d,%d,%d,%d", pt.x, pt.y, 
        		getWidth(), getHeight()));
	}
        
}
