package edu.mbl.jif.gui.help;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;


public class HelpBrowserFrame extends JFrame { // JInternalFrame {

    JPanel contentPane;
    JEditorPane editorPane;
    private JToolBar jToolBar1 = new JToolBar();
    private JButton button_Index = new JButton();
    private JButton button_Map = new JButton();

    final static String HELPDIR =  ".\\Help\\";  // include trailing path sep.
    final static String HELPINDEXFILE =  "index.htm";

    //private Browser   browser;
    
    //Construct the frame
    public HelpBrowserFrame() {
        super();
        super.setTitle("Help & Documentation");
        //super("Help/Documentation", false, true, true, false);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        contentPane = (JPanel) this.getContentPane();
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        JScrollPane pane = new JScrollPane();
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        button_Index.setText("Index");
        button_Index.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button_Index_actionPerformed(e);
            }
        });
        button_Map.setText("Map");
        button_Map.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button_Map_actionPerformed(e);
            }
        });
        pane.getViewport().add(editorPane);
        this.getContentPane().add(pane, BorderLayout.CENTER);
        // Add a hyperlink listener.
        final JEditorPane finalPane = editorPane;
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(final HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            // Save original
                            //Document doc = editorPane.getDocument();
                            try {
                                URL url = e.getURL();
                                editorPane.setPage(url);
                            } catch (IOException io) {
                            }
                        }
                    });
                }
            }
        });

        //	  editorPane.addHyperlinkListener(new HyperlinkListener() {
        //			public void hyperlinkUpdate(HyperlinkEvent ev) {
        //			   try {
        //				  if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        //					 finalPane.setPage(ev.getURL());
        //				  }
        //			   } catch (IOException ex) {
        //				  ex.printStackTrace(System.err);
        //			   }
        //			}
        //		 });
        editorPane.setPage((new File(HELPDIR+HELPINDEXFILE)).toURL());
        contentPane.add(pane);
        contentPane.add(jToolBar1, BorderLayout.NORTH);
        jToolBar1.add(button_Index, null);
        jToolBar1.add(button_Map, null);
    }

    public void gotoPage(String page) {
        try {
            File hFile = new File(HELPDIR + page + ".htm");
            editorPane.setPage(hFile.toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void button_Index_actionPerformed(ActionEvent e) {
        gotoPage("index");
    }

    void button_Map_actionPerformed(ActionEvent e) {
        gotoPage("helpmap");
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            //PSj.docsFrame = null;
            this.dispose();
        }
    }

}

