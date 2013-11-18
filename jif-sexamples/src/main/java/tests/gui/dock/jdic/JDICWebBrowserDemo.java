package tests.gui.dock.jdic;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.DockingPreferences;
import com.vlsolutions.swing.docking.RelativeDockablePosition;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.jdic.browser.WebBrowser;
import org.jdesktop.jdic.browser.WebBrowserEvent;
import org.jdesktop.jdic.browser.WebBrowserListener;

/** The JDIC WebBrowser demo 
 * 
 * Source code copyright 2005 VLSolutions. 
 *<p>
 * Licensing : You can freely use / modify / include it in any application, as long as you keep in mind the
 * following code is an example, provided without any guarantees (the png files cannot be used outside this 
 * sample application, due to copyright restrictions).
 *
 *<p> 
 * Installing and running the demo : This demo is composed of a single .java file 3 xml files (workspaces) 
 *  and 7 png file (icons) that should be located in the same package (dockingtutorial.jdic).
 *<p>
 * The workaround for the JDIC Browser has been successfully tested with the 20050930 release of the 
 * JDIC API, and certainly doesn't work with prior releases.
 *<p>
 * The workaround is available starting with VLDocking 2.0.2, son don't even think about trying
 * it with prior releases...
 *<p>
 * for support and more explanations on that topic, please contact us at support_at_vlsolutions.com
 * or use our online forum at www.vlsolutions.com/en/forum
 *
 * @author Lilian Chamontin, VLSolutions
 */
public class JDICWebBrowserDemo extends JFrame {
  
  /** The web browser component, without dispose on remove */
  WebBrowser browser = new WebBrowser(false);       
  
  /** our dockables */
  HistoryPanel historyPanel = new HistoryPanel();
  RssReaderPanel rssPanel = new RssReaderPanel();
  WebBrowserPanel webBrowserPanel = new WebBrowserPanel(browser);
  ConsolePanel consolePanel = new ConsolePanel(); 
  
  /** the docking desktop */
  DockingDesktop desk = new DockingDesktop();
  

  // byte array used to save a workspace (custom layout of dockables)
  byte [] savedWorkpace;
  
  // action used to save the current workspace
  Action saveWorkspaceAction = new AbstractAction("Save Workspace"){ 
    public void actionPerformed(ActionEvent e){ saveWorkspace(); }
  };
  
  // some sample worspaces
  String sampleWorkspace1 = readWorkspace("ws1.xml");
  String sampleWorkspace2 = readWorkspace("ws2.xml");
  String sampleWorkspace3 = readWorkspace("ws3.xml");
  
  // action used to reload a workspace
  Action loadWorkspaceAction = new AbstractAction("Reload Workspace"){
    public void actionPerformed(ActionEvent e){reloadWorkspace(); }
  };
  Action workspace1Action = new AbstractAction("Select Predefined Workspace 1"){
    public void actionPerformed(ActionEvent e){loadWorkspace(sampleWorkspace1);}
  };
  Action workspace2Action = new AbstractAction("Select Predefined Workspace 2"){
    public void actionPerformed(ActionEvent e){loadWorkspace(sampleWorkspace2); }
  };
  Action workspace3Action = new AbstractAction("Revert to initial Workspace"){
    public void actionPerformed(ActionEvent e){loadWorkspace(sampleWorkspace3); }
  };
  Action aboutAction = new AbstractAction("About"){
    public void actionPerformed(ActionEvent e){ about();}
  };
  Action exitAction = new AbstractAction("Exit"){
    public void actionPerformed(ActionEvent e){ exit();}
  };
  
  
  String aboutMessage = "<html><body><h1>The JDIC WebBrowser Demo</h1>"
      +"<p> a VLDocking 2.0 sample application by VLSolutions"
      + "<p> This application demonstrates the use of the <b>JDIC Browser</b> with VLDocking, an also"
      + " some workspace management (try the <b>Actions</b> Menu )."
      + "<p> Libraries used by this application are : "
      + "<ul><li>The JDIC Browser version 20050930</li>"
      + "<li>VLDockking, starting from version 2.0.2</li>"
      +" </ul>"
      +"</body></html>";
  
  
  
  
  public JDICWebBrowserDemo() {
    
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("VLDocking 2.0 / JDIC Browser Demo - VLSolutions");
    try {
      setIconImage(new ImageIcon(getClass().getResource("vlsolutions_white-16.png")).getImage());
    } catch (Exception ignore){      
    }
    
    installMenu();

    // Docking Layout 
    getContentPane().add(desk, BorderLayout.CENTER);
    desk.addDockable(webBrowserPanel);
    desk.split(webBrowserPanel, historyPanel, DockingConstants.SPLIT_LEFT);
    desk.split(historyPanel, rssPanel, DockingConstants.SPLIT_BOTTOM);
    desk.addHiddenDockable(consolePanel, RelativeDockablePosition.BOTTOM);
    
    pack();
    setLocationRelativeTo(null); // center on screen
    setVisible(true);
    addWindowListener(new WindowAdapter(){
      public void windowOpened(WindowEvent e){
        desk.resetToPreferredSize();
        desk.setDockableWidth(historyPanel, 0.2);
        webBrowserPanel.urlTextField.requestFocus();        
      }
      public void windowClosed(WindowEvent e){
        browser.dispose(); // release resources
      }
    });
    
    browser.addWebBrowserListener(new BrowserDispatcher());
  }
  
  private void about(){
    JOptionPane.showMessageDialog(this, aboutMessage);
  }
 
  private void exit(){
    int reply = JOptionPane.showConfirmDialog(this, "Exit Application ?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
    if (reply == JOptionPane.YES_OPTION){
      System.exit(0);
    }
  }
  
  private void installMenu(){
    JMenuBar mb = new JMenuBar();
    setJMenuBar(mb);
    JMenu file=  new JMenu("File");
    JMenu help=  new JMenu("Help");
    JMenu actions = new JMenu("Actions");
    mb.add(file);
    mb.add(actions);
    mb.add(help);

    aboutAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("stock_about-16.png")));
    exitAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("stock_close-16.png")));

    
    file.add(new JMenuItem(exitAction));
    help.add(new JMenuItem(aboutAction));
    
    // cannot reload before a workspace is saved
    loadWorkspaceAction.setEnabled(false);
    
    // add sale/reload menus
    actions.add(saveWorkspaceAction);
    actions.add(loadWorkspaceAction);
    actions.addSeparator();
    actions.add(workspace1Action);
    actions.add(workspace2Action);
    actions.add(workspace3Action);

  }
  
  // ============================================================================
  // Workspace management 
  
  private void saveWorkspace() {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      desk.writeXML(out);
      out.close();
      savedWorkpace = out.toByteArray();
      String ws = new String(savedWorkpace);
      JDialog dlg = new JDialog(this, "Your workspace", true);
      String message = "<html><body><h3>Your workspace has been saved in memory</h3>"
          + "<p> You can reload it at any time with the <b>Reload Workspace</b> menu."
          + "<p> And here is what it looks like : "
          +"</body></html>";
      dlg.getContentPane().add(new JLabel(message), BorderLayout.NORTH);
      dlg.getContentPane().add(new JScrollPane(new JTextArea(ws)), BorderLayout.CENTER);
      dlg.pack();
      dlg.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      dlg.setLocationRelativeTo(this);
      dlg.setVisible(true);
      loadWorkspaceAction.setEnabled(true);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  
  /** Reloads a saved workspace  */
  private void reloadWorkspace() {
    try {
      ByteArrayInputStream in = new ByteArrayInputStream(savedWorkpace);
      desk.readXML(in);
      in.close();
    } catch (Exception ex) {
      // catch all exceptions, including those of the SAXParser
      ex.printStackTrace();
    }
  }
  /** Reloads a saved workspace  */
  private void loadWorkspace(String ws) {
    try {
      ByteArrayInputStream in = new ByteArrayInputStream(ws.getBytes());
      desk.readXML(in);
      in.close();
    } catch (Exception ex) {
      // catch all exceptions, including those of the SAXParser
      ex.printStackTrace();
    }
  }

  /** reads a workspace as a resource */
  private String readWorkspace(String name){
    StringBuffer sb = new StringBuffer();
    try {
      BufferedReader bin = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(name)));
      String line;
      while ((line = bin.readLine()) != null){
        sb.append(line);
      }
      bin.close();
    } catch (IOException ioe){
      ioe.printStackTrace();
    } catch (Exception e){
      e.printStackTrace();
    }
    return sb.toString();    
  }

  
 // ================================================================================= 
 // Launcher
 // ================================================================================= 
  public static void main(String[] args){
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) {
    }
    DockingPreferences.initHeavyWeightUsage();
    DockingPreferences.setSingleHeavyWeightComponent(true);
    
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        new JDICWebBrowserDemo();
      }
    });    

    try {
      Class.forName("com.vlsolutions.stats.UserStats").newInstance();
      // unintrusive stats tools (sends os.name/version and java version to the web site to gather 
      // statistics on our user base), used when application launched by Java Web Start
    } catch (Throwable ignore){      
    }
  }
  
 // ================================================================================= 
 // The Dockables  
 // ================================================================================= 
  
  /** This panel shows history navigation */
  class HistoryPanel extends JPanel implements Dockable {
    DockKey key = new DockKey("history", "History", "Navigation history", 
        new ImageIcon(getClass().getResource("history16.png")));
    DefaultTableModel model = new DefaultTableModel(new String[]{"URL"}, 0){
      public boolean isCellEditable (int row, int col){
        return false;
      }
    };
    
    JTable table = new JTable();
    
    public HistoryPanel(){
      table.setModel(model);
      setPreferredSize(new Dimension(300,300));
      setLayout(new BorderLayout());
      add(new JScrollPane(table));
      table.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
          if (e.getClickCount() == 2){
            String url = (String)table.getValueAt(table.getSelectedRow(), 0);
            webBrowserPanel.setURL(url);
          }
        }
      });
      
      key.setResizeWeight(0);
      //key.setFloatEnabled(true);
    }
    
    public void addHistoryURL(String url){
      for (int i=0; i < model.getRowCount(); i++){
        if (model.getValueAt(i,0).equals(url)){
          model.removeRow(i);
          break;
        }
      }
      model.addRow(new Object[]{url});
    }
    
    public DockKey getDockKey() {
      return key;
    }

    public Component getComponent() {
      return this;
    }
    
  }
  
  /** This is the web browser dockable */
  class WebBrowserPanel extends JPanel implements Dockable, ActionListener {
    DockKey key = new DockKey("browser", "Browser", "Web Browser powered by JDIC",
        new ImageIcon(getClass().getResource("stock_internet-16.png")));
    JTextField urlTextField = new JTextField("http://www.vlsolutions.com/en/documentation/docking/tutorial/tutorial7.php");
    
    public WebBrowserPanel(WebBrowser browser){
      
      setLayout(new GridBagLayout());      
      GridBagConstraints gc = new GridBagConstraints(0, 0, 1, 1, 0, 0, 
          GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3,3,2,2), 0,0);
      add(new JLabel("URL"), gc);     
      gc.gridx++;
      gc.fill = GridBagConstraints.HORIZONTAL;
      urlTextField.addActionListener(this);
      add(urlTextField, gc);
      gc.gridx = 0;
      gc.gridy++;
      gc.gridwidth = 2;
      gc.weightx = gc.weighty = 1;
      gc.fill = GridBagConstraints.BOTH;
      // workaround another JDIC Browser bug : we must put it alone in its container, otherwise
      // there are layout problems appearing (wrong vertical position...)
      JPanel browserPanel = new JPanel(new BorderLayout());
      browserPanel.add(browser);
      add(browserPanel, gc);      
      
      setPreferredSize(new Dimension(800,600));
      key.setResizeWeight(1);
      key.setCloseEnabled(false);
      key.setFloatEnabled(true); //2006/01/24
      
      reloadURL(urlTextField.getText());      
    }
    
    public void setTitle(String title){
      key.setName(title);
    }
    
    
    public DockKey getDockKey() {
      return key;
    }

    public Component getComponent() {
      return this;
    }
    
    public void setURL(String url){
      urlTextField.setText(url);
      reloadURL(url);
    }
    
    public void reloadURL(String text){
      try {
        browser.setURL(new URL(text));
      } catch (Exception e){
        e.printStackTrace();
      }
    }

    /** event triggered by the url text field*/
    public void actionPerformed(ActionEvent event) {
      JTextField tf=  (JTextField)event.getSource();
      reloadURL(tf.getText());
    }
  }
  
  
  /** This panel simulates an RSS feed reader */
  class RssReaderPanel extends JPanel implements Dockable {
    DockKey key = new DockKey("rss", "RSS Feeds", "Latest feeds", 
        new ImageIcon(getClass().getResource("rss16.png")));
    DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "News"}, 0){
      public boolean isCellEditable (int row, int col){
        return false;
      }
    };
    
    JTable table = new JTable();
    
    String [][] feeds = {
      {"Today", "VLDocking 2.0 supports the JDIC Browser !"},
      {"Today", "Simulated RSS entry 1"},    
      {"Yesterday", "Simulated RSS entry 2"},    
      {"Yesterday", "Simulated RSS entry 3"},          
    };
    
    public RssReaderPanel(){
      for (int i=0; i < feeds.length; i++){
        model.addRow(feeds[i]);
      }
      table.setModel(model);
      table.getColumnModel().getColumn(0).setPreferredWidth(60);
      table.getColumnModel().getColumn(0).setMaxWidth(60);
      //table.getColumnModel().getColumn(0).setPreferredWidth(40);
      setPreferredSize(new Dimension(300,300));
      key.setResizeWeight(0);
      key.setFloatEnabled(true);

      setLayout(new BorderLayout());
      add(new JScrollPane(table));
      table.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
          if (e.getClickCount() == 2){
            String url = (String)table.getValueAt(table.getSelectedRow(), 0);
            webBrowserPanel.setURL(url);
          }
        }
      });
    }
      
    public DockKey getDockKey() {
      return key;
    }

    public Component getComponent() {
      return this;
    }
    
  }
  

  
  /** A simple console panel */
  class ConsolePanel extends JPanel implements Dockable {
    DockKey key = new DockKey("log", "Console",  "Console for logging error messages",
        new ImageIcon(getClass().getResource("console16.png")));
    
    JTextArea jta = new JTextArea(10, 80);
    
    PrintStream out = new PrintStream(new LogStream(), true);
    
    public ConsolePanel(){  
      super(new BorderLayout());
      out.println("Console logging initialized "+ new Date());
      jta.setEditable(false);
      JScrollPane jsp = new JScrollPane(jta);
      jsp.setPreferredSize(new Dimension(400,100));
      add(jsp, BorderLayout.CENTER);
      
      key.setAutoHideBorder(DockingConstants.HIDE_BOTTOM);
    }
    
    public DockKey getDockKey() {
      return key;
    }
    
    public Component getComponent() {
      return this;
    }
    
    
    class LogStream extends OutputStream {
      StringBuffer temp = new StringBuffer(512);
      boolean append;
      
      public synchronized void write(int b) throws IOException {
        temp.append( (char) b);
        append = true;
      }
      
      public synchronized void flush() throws IOException {
        if (append) {
          jta.append(temp.toString());
          append = false;
        }
        temp = new StringBuffer(512);
      }
    }
  }
  
  

  /** A simple class that dispatches browser events to the appropriate components 
   *<p> 
   * This is not very OO, but we must keep the demo as simple as possible...
   */
  class BrowserDispatcher implements WebBrowserListener { 
    public void titleChange(WebBrowserEvent event) {
      final String title = event.getData();
      SwingUtilities.invokeLater(new Runnable(){
        public void run(){
          webBrowserPanel.setTitle(title);
        }
      });
      
    }

    public void statusTextChange(WebBrowserEvent event) {
    }

    public void downloadStarted(WebBrowserEvent event) {
    }

    public void downloadProgress(WebBrowserEvent event) {
    }

    public void downloadError(WebBrowserEvent event) {
    }

    public void downloadCompleted(WebBrowserEvent event) {
      URL url = browser.getURL();
      if (url != null){
        historyPanel.addHistoryURL(url.toExternalForm());
      }
    }

    public void documentCompleted(WebBrowserEvent event) {      
    }


        public void windowClose(WebBrowserEvent arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
  }
  
  
  
}
