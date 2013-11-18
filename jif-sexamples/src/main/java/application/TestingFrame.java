package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import edu.mbl.jif.gui.PanelValuePoint;

public class TestingFrame
    extends JFrame {
  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  JToolBar jToolBar = new JToolBar();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  ImageIcon image1;
  ImageIcon image2;
  ImageIcon image3;
  JLabel statusBar = new JLabel();
  JButton buttonGo = new JButton();
  JPanel panelResults = new JPanel();
  JPanel panelActionButtons = new JPanel();
  JScrollPane jScrollPane1;
  JTextArea jTextPane1 = new JTextArea();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel panelMain = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();

  /**Construct the frame*/
  public TestingFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**Component initialization*/
  private void jbInit() throws Exception {

    image1 = new ImageIcon(application.TestingFrame.class.getResource("icons/openFile.png"));
    image2 = new ImageIcon(application.TestingFrame.class.getResource("icons/closeFile.png"));
    image3 = new ImageIcon(application.TestingFrame.class.getResource("icons/help.png"));
    //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame1.class.getResource("[Your Icon]")));
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(null);
    this.setSize(new Dimension(616, 695));
    this.setTitle("Frame Title");
    statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
    statusBar.setText(" ");
    statusBar.setBounds(new Rectangle(8, 620, 572, 23));
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuFileExit_actionPerformed(e);
      }
    });
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuHelpAbout_actionPerformed(e);
      }
    });
    panelMain.setLayout(borderLayout2);
    buttonGo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonGo_actionPerformed(e);
      }
    });
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    //
    this.setJMenuBar(jMenuBar1);
    jButton1.setIcon(image1);
    jButton1.setToolTipText("Open File");
    jButton2.setIcon(image2);
    jButton2.setToolTipText("Close File");
    jButton3.setIcon(image3);
    jButton3.setToolTipText("Help");
    jToolBar.setBounds(new Rectangle(1, 4, 581, 33));
    jToolBar.add(jButton1);
    jToolBar.add(jButton2);
    jToolBar.add(jButton3);
    //
    panelMain.setBorder(BorderFactory.createEtchedBorder());
    panelMain.setBounds(new Rectangle(156, 44, 418, 431));
    addComponents();
    contentPane.add(panelMain, null);
    //
    panelActionButtons.setBorder(BorderFactory.createRaisedBevelBorder());
    panelActionButtons.setBounds(new Rectangle(8, 43, 139, 573));
    // Action Button
    buttonGo.setText("Go");

    panelActionButtons.add(buttonGo, null);
    //
    panelResults.setBorder(BorderFactory.createEtchedBorder());
    panelResults.setBounds(new Rectangle(153, 481, 422, 134));
    panelResults.setLayout(borderLayout1);
    jTextPane1.setText("jTextPane1");
    jScrollPane1 = new JScrollPane(jTextPane1);
    //jScrollPane1.getViewport().add(jTextPane1, null);
    panelResults.add(jScrollPane1, BorderLayout.CENTER);
    contentPane.add(panelResults, null);
    contentPane.add(panelActionButtons, null);
    contentPane.add(statusBar, null);
    contentPane.add(jToolBar, null);
  }

  /**File | Exit action performed*/
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  /**Help | About action performed*/
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
  }

  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

//----------------------------------------------------------------
//
  void addComponents() {
//    PanelValuePoint vpPanel = new PanelValuePoint();
//    panelMain.add(vpPanel, BorderLayout.SOUTH);
  }

  void buttonGo_actionPerformed(ActionEvent e) {
//
//        JFrame frm = new JFrame();
//        PanelValuePoint vpPanel = new PanelValuePoint(1);
//        frm.getContentPane().add(vpPanel, BorderLayout.SOUTH);
//        frm.setVisible(true);
//        vpPanel.test();


  }
}
