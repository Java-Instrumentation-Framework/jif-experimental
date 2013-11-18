package edu.mbl.jif.gui.internal;

import edu.mbl.jif.gui.panel.PanelEnclosed;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.BorderLayout;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;

/*
 JDesktopPane
 DesktopManager Interface
 */

public class IFrame extends JInternalFrame implements InternalFrameListener {

  BorderLayout borderLayout1 = new BorderLayout();
  private PanelEnclosed contents = null;
  private String title = null;

  public IFrame() {
    this(null);
  }


  public IFrame(PanelEnclosed _contents) {
    this(_contents, String.valueOf(System.currentTimeMillis()));
  }


  public IFrame(PanelEnclosed _contents, String _title) {
    this(_contents, _title, null);
  }


  public IFrame(PanelEnclosed _contents, String _title, ImageIcon icon) {
    super();
    this.setFrameIcon(icon);
    contents = _contents;
    title = _title;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  void jbInit() throws Exception {
    this.setClosable(true);
    this.setResizable(true);
    this.setMaximizable(true);
    this.setIconifiable(false);
    if (title != null) {
      super.setTitle(title);
    }
    else {
      setNoTitleBar();
    }
    this.getContentPane().setLayout(borderLayout1);
    if (contents != null) {
      addContents(contents);
    }
  }


//-----------------------------------------------------------
  public void addContents(PanelEnclosed _contents) {
    contents = _contents;
    this.getContentPane().add(contents, BorderLayout.CENTER);
//      this.setSize(
//           (int) contents.getPreferredSize().getWidth() + 16,
//           (int) contents.getPreferredSize().getHeight() + 28);

    //
//        Dimension deskSize = PSj.deskTopFrame.getSize();
//        if (h > deskSize.height) {
//           h = deskSize.height; }
//        if (w > deskSize.width) {
//           w = deskSize.width; }

//        //
//        PSj.deskTopFrame.addFrame(this);
//        setLocation(PSj.deskTopFrame.getNewFrameLocation());

    pack();
//        int w = (int) contents.getBounds().getWidth();
//        int h = (int) contents.getBounds().getHeight();
//        setSize(w,h);

    setVisible(true);
  }


//-----------------------------------------------------------
  public void setTitle(String _title) {
    title = _title;
    this.setTitle(title);
  }


//-----------------------------------------------------------
  public void setNoTitleBar() {
    ComponentUI frameUI = this.getUI();
    if (frameUI instanceof BasicInternalFrameUI) {
      ((BasicInternalFrameUI) frameUI).setNorthPane(null);
    }
  }


//-----------------------------------------------------------
  public void setTitleBar(String title, boolean close, boolean max, boolean min) {
  }


//-----------------------------------------------------------
  public void setBoundsMemory(String key) {
    if (key == null) {
      // no memory
    }
  }


//-----------------------------------------------------------
  public void setAsSelected() {
    try {
      setVisible(true);
      setSelected(true);
    }
    catch (java.beans.PropertyVetoException ve) {
    }
  }


//-----------------------------------------------------------
  public void close() {
    try {
      contents.closeParent();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    // aFrame.dispose()
    //frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
    //sends window the closing message
    /*
       public class Closer implements ActionListener {
         public Closer(Window window)
            {	m_window = window;
            }
            public void actionPerformed(ActionEvent ae)
            {	EventQueue eq = m_window.getToolkit().getSystemEventQueue();
     eq.postEvent(new WindowEvent(m_window, WindowEvent.WINDOW_CLOSING));
             }
            private Window m_window;
        }
      }
     */
  }


  ///////////////////////////////////////////////////////////////////////////
  // InternalFrameListener implementation
  //
  public void internalFrameActivated(InternalFrameEvent e) {}


  public void internalFrameDeactivated(InternalFrameEvent e) {}


  public void internalFrameClosing(InternalFrameEvent e) {
    if (contents != null) {
      try {
        contents.closeParent();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

    }
  }


  public void internalFrameClosed(InternalFrameEvent e) {}


  public void internalFrameOpened(InternalFrameEvent e) {}


  public void internalFrameIconified(InternalFrameEvent e) {}


  public void internalFrameDeiconified(InternalFrameEvent e) {}

}
