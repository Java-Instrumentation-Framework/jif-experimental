package edu.mbl.jif.comm.serialtester;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import gnu.io.*;
import java.util.Enumeration;
import java.io.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.util.Properties;

/**
 * <p>Title: Serial Port Tester</p>
 * <p>Description: Verify Serial Port Operation Of Remote Hosts, GUI component</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter, contains modifed code from Sun Microsystems
 * @version 1.0
 */

public class SerialPortVerifyFrame
    extends JFrame
    implements SerialTestResultsDisplay {
  //IntlSwingSupport intlSwingSupport1 = new IntlSwingSupport();
  private JPanel contentPane;
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu jMenuFile = new JMenu();
  private JMenuItem jMenuFileExit = new JMenuItem();
  private JMenu jMenuHelp = new JMenu();
  private JMenuItem jMenuHelpAbout = new JMenuItem();
  private ImageIcon image1;
  private ImageIcon image2;
  private ImageIcon image3;
  private JPanel SerialTesterPanel = new JPanel();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();

  public static final String[] BAUDRATES = {
      "2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600",
      "115200"};
  public static final String[] STOPBITS = {
      "1", "2"};
  public static final String[] DATABITS = {
      "5", "6", "7", "8"};
  public static final String[] DELAY = {
      "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
  private SerialParameters _parameters = new SerialParameters();
  private boolean open = false;
  CommandComboBoxEditor comboEditor1 = new CommandComboBoxEditor();
  private JComboBox PortComboBox1 = new JComboBox();
  private JComboBox BaudComboBox1 = new JComboBox(BAUDRATES);
  private JComboBox DataBitsComboBox1 = new JComboBox(DATABITS);
  private JComboBox StopBitsComboBox1 = new JComboBox(STOPBITS);
  private JFileChooser jFileChooser1 = new JFileChooser();
  private JMenuItem jMenuFileOpen = new JMenuItem();
  private SerialConnection _serial;
  static final int TEST_STATE_START = 0;
  static final int TEST_STATE_ABORT = 1;
  private int _teststate = 0;
  private EchoTest _echoTester;
  private CommandTest _commandTester;
  private boolean _displayASCII;
  private boolean _commandASCII;
  private boolean _postpendCR;
  private boolean _postpendLF;
  private JTabbedPane jTabbedPane1 = new JTabbedPane();
  private JPanel EchoTestPanel = new JPanel();
  private JPanel CommadPanel = new JPanel();
  private JLabel jLabel13 = new JLabel();
  private JButton SendCommandButton = new JButton();
  private JLabel jLabel14 = new JLabel();
  private JCheckBox ResponseAsASCIICheckBox = new JCheckBox();
  private JComboBox CommandComboBox = new JComboBox();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JTextPane ResponseTextPane = new JTextPane();
  private JLabel OverRunErrorLabel = new JLabel();
  private JLabel FrameErrorLabel = new JLabel();
  private JLabel ParityErrorLabel = new JLabel();
  private JLabel BreakLabel = new JLabel();
  private JTextField BreakDurationTextField = new JTextField();
  private JLabel jLabel15 = new JLabel();
  private JLabel jLabel16 = new JLabel();
  private long _breakstarttime;
  static final int BREAK_START = 0;
  static final int BREAK_END = 1;
  private int _breakState;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GridBagLayout gridBagLayout4 = new GridBagLayout();
  private JPanel jPanel2 = new JPanel();
  private JButton TestButton = new JButton();
  private JTextField ResultsTextField = new JTextField();
  private JLabel jLabel9 = new JLabel();
  private JPanel jPanel3 = new JPanel();
  private JTextField BaudTextField = new JTextField();
  private JTextField BytesToSendTextField = new JTextField();
  private JLabel jLabel11 = new JLabel();
  private JLabel jLabel10 = new JLabel();
  private JTextField BytesSentTextField = new JTextField();
  private JLabel jLabel8 = new JLabel();
  private JLabel jLabel7 = new JLabel();
  private JLabel jLabel6 = new JLabel();
  private JTextField EstTimeTextField = new JTextField();
  private JTextField BytesReceivedTextField = new JTextField();
  private JPanel jPanel4 = new JPanel();
  private JPanel jPanel5 = new JPanel();
  private JPanel jPanel6 = new JPanel();
  private JTextField TestFileTextField = new JTextField();
  private JLabel jLabel5 = new JLabel();
  private JButton jButtonFileOpen1 = new JButton();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private GridBagLayout gridBagLayout7 = new GridBagLayout();
  private GridBagLayout gridBagLayout6 = new GridBagLayout();
  private GridBagLayout gridBagLayout8 = new GridBagLayout();
  private GridBagLayout gridBagLayout5 = new GridBagLayout();
  private GridBagLayout gridBagLayout9 = new GridBagLayout();
  JCheckBox CommandAsASCIICheckBox = new JCheckBox();
  JCheckBox checkBox_CR = new JCheckBox();
  JCheckBox checkBox_LF = new JCheckBox();

  //Construct the frame
  public SerialPortVerifyFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this,
                                    "Could not load comm driver:" + e.toString(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
    _serial = new SerialConnection(_parameters);
    _echoTester = new EchoTest(_serial, this);
    _commandTester = new CommandTest(_serial, this);
    _breakState = BREAK_START;
    _displayASCII = true;
    _commandASCII = true;
    LoadCommands();
  }

  //Component initialization
  private void jbInit() throws Exception {
    image1 = new ImageIcon(edu.mbl.jif.comm.serialtester.SerialPortVerifyFrame.class.getResource(
        "openFile.gif"));
    image2 = new ImageIcon(edu.mbl.jif.comm.serialtester.SerialPortVerifyFrame.class.getResource(
        "celogoicon32.gif"));
    image3 = new ImageIcon(edu.mbl.jif.comm.serialtester.SerialPortVerifyFrame.class.getResource(
        "help.gif"));
    //setIconImage(Toolkit.getDefaultToolkit().createImage(SerialPortVerifyFrame.class.getResource("[Your Icon]")));
    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(gridBagLayout1);
    this.setSize(new Dimension(494, 600));
    this.setTitle("Carter Engineering Serial Port Test Utility");
    this.setIconImage(image2.getImage());
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new
        SerialPortVerifyFrame_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new
        SerialPortVerifyFrame_jMenuHelpAbout_ActionAdapter(this));
    SerialTesterPanel.setLayout(gridBagLayout2);
    jLabel1.setText("Port");
    jLabel2.setText("Baud");
    jLabel3.setText("Data Bits");
    jLabel4.setText("Stop Bits");
    jMenuFileOpen.setText("Open");
    jMenuFileOpen.addActionListener(new
        SerialPortVerifyFrame_jMenuFileOpen_actionAdapter(this));
    EchoTestPanel.setLayout(gridBagLayout3);
    CommadPanel.setLayout(gridBagLayout4);
    jLabel13.setText("Command in Hex");
    //CommandTextField.setSelectionEnd(0);
    //CommandTextField.setSelectionStart(4);
    SendCommandButton.setFont(new java.awt.Font("Dialog", 0, 10));
    SendCommandButton.setMargin(new Insets(2, 4, 2, 4));
    SendCommandButton.setText("Send");
    SendCommandButton.addActionListener(new
        SerialPortVerifyFrame_SendCommandButton_actionAdapter(this));
    jLabel14.setText("Response");
    ResponseAsASCIICheckBox.setText("Display Response As ASCII");
    ResponseAsASCIICheckBox.setSelected(_displayASCII);
    ResponseAsASCIICheckBox.addItemListener(new
        SerialPortVerifyFrame_ResponseAsASCIICheckBox_itemAdapter(this));
    CommandComboBox.setToolTipText("Press RETURN to save command in list");
    CommandComboBox.setEditable(true);
    CommandComboBox.setEditor(comboEditor1);
    CommandComboBox.addActionListener(new
        SerialPortVerifyFrame_CommandComboBox_actionAdapter(this));
    ResponseTextPane.setBorder(BorderFactory.createLoweredBevelBorder());
    ResponseTextPane.setEditable(false);
    OverRunErrorLabel.setFont(new java.awt.Font("Dialog", 1, 12));
    OverRunErrorLabel.setText("OE");
    FrameErrorLabel.setFont(new java.awt.Font("Dialog", 1, 12));
    FrameErrorLabel.setText("FE");
    ParityErrorLabel.setFont(new java.awt.Font("Dialog", 1, 12));
    ParityErrorLabel.setText("PE");
    BreakLabel.setFont(new java.awt.Font("Dialog", 1, 12));
    BreakLabel.setText("BI");
    BreakDurationTextField.setText("0");
    jLabel15.setText("Errors");
    jLabel16.setText("Time");
    BaudComboBox1.addActionListener(new
        SerialPortVerifyFrame_BaudComboBox1_actionAdapter(this));
    DataBitsComboBox1.addActionListener(new
        SerialPortVerifyFrame_DataBitsComboBox1_actionAdapter(this));
    StopBitsComboBox1.addActionListener(new
        SerialPortVerifyFrame_StopBitsComboBox1_actionAdapter(this));
    jPanel2.setLayout(gridBagLayout7);
    TestButton.setEnabled(false);
    TestButton.setText("Start Test");
    TestButton.addActionListener(new
                                 SerialPortVerifyFrame_TestButton_actionAdapter(this));
    ResultsTextField.setMaximumSize(new Dimension(83, 20));
    ResultsTextField.setMinimumSize(new Dimension(83, 20));
    ResultsTextField.setOpaque(false);
    ResultsTextField.setPreferredSize(new Dimension(83, 20));
    ResultsTextField.setEditable(false);
    jLabel9.setText("Results");
    jPanel3.setLayout(gridBagLayout6);
    BaudTextField.setOpaque(false);
    BaudTextField.setEditable(false);
    BaudTextField.setText("0");
    BytesToSendTextField.setOpaque(false);
    BytesToSendTextField.setEditable(false);
    BytesToSendTextField.setText("0");
    jLabel11.setText("Measured Recv Baud");
    jLabel10.setText("Est. Time To Send(s)");
    BytesSentTextField.setOpaque(false);
    BytesSentTextField.setEditable(false);
    BytesSentTextField.setText("0");
    jLabel8.setText("Bytes Received");
    jLabel7.setText("Bytes Sent");
    jLabel6.setText("Bytes to Send");
    EstTimeTextField.setOpaque(false);
    EstTimeTextField.setEditable(false);
    EstTimeTextField.setText("0");
    BytesReceivedTextField.setDoubleBuffered(true);
    BytesReceivedTextField.setOpaque(false);
    BytesReceivedTextField.setEditable(false);
    BytesReceivedTextField.setText("0");
    jPanel4.setLayout(gridBagLayout5);
    jPanel5.setLayout(gridBagLayout9);
    jPanel6.setLayout(gridBagLayout8);
    TestFileTextField.setOpaque(false);
    TestFileTextField.setEditable(false);
    jLabel5.setText("Test File");
    jButtonFileOpen1.setToolTipText("Open File");
    jButtonFileOpen1.addActionListener(new
        SerialPortVerifyFrame_jButtonFileOpen1_actionAdapter(this));
    jButtonFileOpen1.setIcon(image1);
    jPanel4.setMaximumSize(new Dimension(2147483647, 62));
    jPanel5.setMaximumSize(new Dimension(65, 49));
    PortComboBox1.setNextFocusableComponent(BaudComboBox1);
    BaudComboBox1.setNextFocusableComponent(DataBitsComboBox1);
    DataBitsComboBox1.setNextFocusableComponent(StopBitsComboBox1);
    StopBitsComboBox1.setNextFocusableComponent(jTabbedPane1);
    CommandAsASCIICheckBox.addItemListener(new
        SerialPortVerifyFrame_CommandAsASCIICheckBox_itemAdapter(this));
    CommandAsASCIICheckBox.setSelected(true);
    CommandAsASCIICheckBox.setText("Use ASCII for Command");
    CommandAsASCIICheckBox.addItemListener(new
        SerialPortVerifyFrame_CommandAsASCIICheckBox_itemAdapter(this));
    checkBox_CR.setText("+CR");
    checkBox_CR.addItemListener(new
                                SerialPortVerifyFrame_checkBox_CR_itemAdapter(this));
    checkBox_LF.setText("+LF");
    checkBox_LF.addItemListener(new
                                SerialPortVerifyFrame_checkBox_LF_itemAdapter(this));
    jMenuFile.add(jMenuFileOpen);
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    this.setJMenuBar(jMenuBar1);
    SerialTesterPanel.add(jLabel3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(4, 8, 0, 0), 8, 0));
    SerialTesterPanel.add(PortComboBox1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 3, 0, 0), -30, 1));
    SerialTesterPanel.add(jLabel2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(4, 8, 0, 9), 57, 0));
    SerialTesterPanel.add(BaudComboBox1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 8, 0, 0), 26, 1));
    SerialTesterPanel.add(DataBitsComboBox1, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 8, 0, 9), 15, 1));
    SerialTesterPanel.add(jLabel4, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(4, 0, 0, 0), 8, 0));
    SerialTesterPanel.add(StopBitsComboBox1, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 0, 8), 15, 1));
    SerialTesterPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(4, 3, 0, 13), 59, 0));
    SerialTesterPanel.add(jTabbedPane1, new GridBagConstraints(0, 2, 8, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(9, 3, 17, 3), 3, 17));
    jPanel2.add(TestButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(10, 14, 0, 0), 0, 0));
    jPanel2.add(ResultsTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(17, 14, 30, 0), 0, 0));
    jPanel2.add(jLabel9, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(17, 18, 30, 0), 44,
                                                5));
    EchoTestPanel.add(jPanel3, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(0, 4, 0, 0), 0, 0));
    jPanel3.add(jLabel6, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 0, 0, 0), 46, 3));
    jPanel3.add(jLabel10, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.WEST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(13, 0, 0, 0), 5, 3));
    jPanel3.add(jLabel7, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(12, 0, 0, 0), 63, 3));
    jPanel3.add(jLabel8, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(13, 0, 0, 0), 37, 3));
    jPanel3.add(jLabel11, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.WEST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(12, 0, 0, 0), 4, 3));
    jPanel3.add(BaudTextField, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        new Insets(11, 0, 0, 11), 97, 1));
    jPanel3.add(BytesReceivedTextField,
                new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
                                       , GridBagConstraints.WEST,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(12, 0, 0, 11), 97, 1));
    jPanel3.add(BytesSentTextField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        new Insets(11, 0, 0, 11), 97, 1));
    jPanel3.add(EstTimeTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        new Insets(12, 0, 0, 11), 97, 1));
    jPanel3.add(BytesToSendTextField,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
                                       , GridBagConstraints.WEST,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(0, 0, 0, 11), 97, 1));
    EchoTestPanel.add(jPanel4, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
        new Insets(0, 0, 5, 0), 9, 0));
    jPanel5.add(jLabel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(5, 0, 0, 8), 5, 0));
    jPanel5.add(jButtonFileOpen1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 8), 1, 0));
    EchoTestPanel.add(jPanel2, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 2), 0, 0));
    jPanel4.add(jPanel6, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                                , GridBagConstraints.SOUTHWEST,
                                                GridBagConstraints.HORIZONTAL,
                                                new Insets(0, 0, 0, 1), 1, 0));
    jPanel6.add(TestFileTextField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        new Insets(21, 2, 0, 2), 0, 0));
    jPanel4.add(jPanel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(4, 3, 0, 0), 3, 0));
    jTabbedPane1.add(CommadPanel, "Command Test");
    contentPane.add(SerialTesterPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 2, 0, 2), 3, 0));
    CommadPanel.add(CommandComboBox,
                    new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(10, 0, 0, 15), 152, 4));
    CommadPanel.add(SendCommandButton,
                    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.CENTER,
                                           GridBagConstraints.NONE,
                                           new Insets(8, 7, 0, 3), 10, 3));
    CommadPanel.add(jLabel13, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(11, 5, 0, 0), 22, 7));
    CommadPanel.add(jLabel14, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        , GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        new Insets(8, 5, 0, 25), 35, 3));
    CommadPanel.add(jScrollPane1, new GridBagConstraints(1, 3, 4, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(2, 0, 19, 3), 336, 260));
    CommadPanel.add(CommandAsASCIICheckBox,
                    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.SOUTHWEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 17), 0, 0));
    CommadPanel.add(ResponseAsASCIICheckBox,
                    new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0
                                           , GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(6, 0, 1, 87), 13, 0));
    CommadPanel.add(checkBox_CR, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 17), 0, 0));
    CommadPanel.add(checkBox_LF, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));
    jTabbedPane1.add(EchoTestPanel, "Echo Test");
    jScrollPane1.getViewport().add(ResponseTextPane, null);
    SerialTesterPanel.add(OverRunErrorLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 11, 5));
    SerialTesterPanel.add(FrameErrorLabel, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 14, 5));
    SerialTesterPanel.add(ParityErrorLabel, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 12, 5));
    SerialTesterPanel.add(BreakLabel, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 37), 16, 5));
    SerialTesterPanel.add(BreakDurationTextField,
                new GridBagConstraints(7, 1, 1, 1, 1.0, 0.0
                                       , GridBagConstraints.WEST,
                                       GridBagConstraints.HORIZONTAL,
                                       new Insets(0, 23, 0, 3), 35, 1));
    SerialTesterPanel.add(jLabel15, new GridBagConstraints(4, 0, 3, 1, 0.0, 0.0
                                                 , GridBagConstraints.WEST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(4, 19, 0, 9), 23, 0));
    SerialTesterPanel.add(jLabel16, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.WEST,
                                                 GridBagConstraints.NONE,
                                                 new Insets(4, 23, 0, 3), 13, 0));

    BaudComboBox1.setSelectedItem(_parameters.getBaudRateString());
    DataBitsComboBox1.setSelectedItem(_parameters.getDatabitsString());
    StopBitsComboBox1.setSelectedItem(_parameters.getStopbitsString());
    try {
      listPortChoices();
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(this,
                                    "Could not load comm driver:" + e.toString(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
     Sets the elements for the portChoice from the ports available on the
     system. Uses an emuneration of comm ports returned by
     CommPortIdentifier.getPortIdentifiers(), then sets the current
     choice to a mathing element in the parameters object.
   */
  void listPortChoices() {
    CommPortIdentifier portId;

    Enumeration en = CommPortIdentifier.getPortIdentifiers();

    // iterate through the ports.
    while (en.hasMoreElements()) {
      portId = (CommPortIdentifier) en.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        PortComboBox1.addItem(portId.getName());
      }
    }
    if (_parameters.getPortName().length() != 0) {
      PortComboBox1.setSelectedItem(_parameters.getPortName());
    }
    else {
      PortComboBox1.setSelectedIndex(0); // select(_parameters.getPortName());
    }
  }

  /**
   * Displays the bytes sent in the GUI, called from the send thread.
   * @param bytesSent bytesSent
   */
  public void updateBytesSent(int bytesSent) {
    BytesSentTextField.setText(String.valueOf(bytesSent));
  }

  /**
   * Dispays the bytes received in the GUI, called from the receive thread.
   * @param bytesReceived Bytes Received
   */
  public void updateBytesReceived(int bytesReceived) {
    BytesReceivedTextField.setText(String.valueOf(bytesReceived));
  }

  /**
   * Displays pass or fail passed on a byte compare of the data sent to the
   * data received.  Called from the test thread.
   * @param updateResults A string either "Pass" or "Failure"
   */
  public void updateResults(String result) {
    ResultsTextField.setText(result);
  }

  /**
   * Called by the receive thread to signal error and break conditions.
   * @param eventType the type of receive event
   */
  public void updateEvent(int eventType) {

    switch (eventType) {
      case SerialPortEvent.BI:
        if (_breakState == BREAK_START) {
          _breakstarttime = System.currentTimeMillis();
          _breakState = BREAK_END;
          BreakLabel.setForeground(Color.red);
        }
        else {
          long endtime;
          _breakState = BREAK_START;
          endtime = System.currentTimeMillis() - _breakstarttime;
          BreakDurationTextField.setText(String.valueOf(endtime));
        }
        break;
      case SerialPortEvent.OE:
        OverRunErrorLabel.setForeground(Color.red);
        break;
      case SerialPortEvent.FE:
        FrameErrorLabel.setForeground(Color.red);
        break;
      case SerialPortEvent.PE:
        ParityErrorLabel.setForeground(Color.red);
        break;
    }
  }

  /**
   * Called by each echo test thread (send, receive, test) when it completes.
       * @param thread type of thread calling, SEND_DONE, RECEIVE_DONE, or RESULTS_DONE
   * @param bytespersec when thread is RESULTS_DONE bytespersec holds receive data rate.
   *
   **/
  public void threadDone(int thread, int bytespersec) {
    switch (thread) {
      case SEND_DONE:
        break;
      case RECEIVE_DONE:
        break;
      case RESULTS_DONE:

        Integer stopbit = Integer.valueOf(StopBitsComboBox1.getSelectedItem().
                                          toString());
        Integer databit = Integer.valueOf(DataBitsComboBox1.getSelectedItem().
                                          toString());
        int baud = bytespersec * (databit.intValue() + stopbit.intValue() + 1);
        BaudTextField.setText(String.valueOf(baud));

        TestButton.setText("Start Test");
        TestButton.setForeground(Color.black);
        _teststate = TEST_STATE_START;
        jButtonFileOpen1.setEnabled(true);
        jTabbedPane1.setEnabled(true);

    }
  }

  /**
   * Called by Command Test to update GUI of received data
   * @param receivedBytes data received from the Command Test.
   */
  public void updateResponse(String receivedBytes) {
    String current = ResponseTextPane.getText();
    ResponseTextPane.setText(current + receivedBytes);
  }

  public void updateResponse(byte[] data, int offset, int len) {
    String current = ResponseTextPane.getText();
    try {
      String ascii = new String(data, 0, len, "ANSI_X3.4-1968");
      if (_displayASCII) {
        current += ascii;
      }
    }
    catch (IOException eio) {
      current += eio.toString();
    }
    HexString hexs = new HexString();
    if (!_displayASCII) {
      current += hexs.toHexString(data, 0, len);
    }
    ResponseTextPane.setText(current);
  }

  /**
     Sets the parameters for the serial port object to the settings displayed in the GUI.
   */
  public void setParameters() {
    _parameters.setPortName(PortComboBox1.getSelectedItem().toString());
    _parameters.setBaudRate(BaudComboBox1.getSelectedItem().toString());
    //_parameters.setFlowControlIn(flowChoiceIn.getSelectedItem());
    //_parameters.setFlowControlOut(flowChoiceOut.getSelectedItem());
    _parameters.setDatabits(DataBitsComboBox1.getSelectedItem().toString());
    _parameters.setStopbits(StopBitsComboBox1.getSelectedItem().toString());
    //parameters.setParity(parityChoice.getSelectedItem());
  }

// Open named file; read text from file into jTextArea1; report to statusBar.
  void openFile(String fileName) {
    try {
      // Open a file of the given name.
      File file = new File(fileName);

      // Get the size of the opened file.
      int size = (int) file.length();

      // Create an input reader based on the file, so we can read its data.
      // FileReader handles international character encoding conversions.
      FileInputStream in = new FileInputStream(file);
      in.close();

      // Display the name of the opened directory+file in the statusBar.
      TestFileTextField.setText(fileName);
      BytesToSendTextField.setText(String.valueOf(size));
      BaudTextField.setText("");
      updateBytesSent(0);
      updateBytesReceived(0);
      updateResults("");
      zeroErrors();
      calcTimeToSend();
      TestButton.setEnabled(true);
    }
    catch (IOException e) {
      TestFileTextField.setText("Error opening " + fileName + " " + e.toString());
      TestButton.setEnabled(false);
    }
  }

//calculate the time to send
  private void calcTimeToSend() {
    Integer size = Integer.valueOf(BytesToSendTextField.getText());
    Integer baud = Integer.valueOf(BaudComboBox1.getSelectedItem().toString());
    Integer stopbit = Integer.valueOf(StopBitsComboBox1.getSelectedItem().
                                      toString());
    Integer databit = Integer.valueOf(DataBitsComboBox1.getSelectedItem().
                                      toString());
    int timetosend = ( (size.intValue() *
                        (1 + stopbit.intValue() + databit.intValue())) /
                      baud.intValue());
    EstTimeTextField.setText(String.valueOf(timetosend));
  }

  private void zeroErrors() {
    BreakDurationTextField.setText("0");
    BreakLabel.setForeground(Color.black);
    OverRunErrorLabel.setForeground(Color.black);
    FrameErrorLabel.setForeground(Color.black);
    ParityErrorLabel.setForeground(Color.black);
  }

  void fileOpen() {
    // Use the OPEN version of the dialog, test return for Approve/Cancel
    if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(this)) {
      // Call openFile to attempt to load the text from file into TextArea
      openFile(jFileChooser1.getSelectedFile().getPath());
    }
    this.repaint();
  }

  /**
   * Save the commands used in the Command Test to a file so that they may be reloaded
   * next time the program is run.
   */
  private void SaveCommands() {
    try {
      Properties commands = new Properties();
      int count = CommandComboBox.getItemCount();
      commands.setProperty("CommandCount", String.valueOf(count));
      for (int i = 0; i < count; i++) {
        String selection = (String) CommandComboBox.getItemAt(i);
        commands.setProperty("Command" + String.valueOf(i), selection);
      }
      FileOutputStream fos = new FileOutputStream("command.properties");
      commands.store(fos, "Serial Commands");
      fos.close();
    }
    catch (IOException e2) {
    }
  }

  /**
   * Load the commands from a file into the GUI.
   */
  private void LoadCommands() {
    try {
      Properties commands = new Properties();

      FileInputStream fis = new FileInputStream("command.properties");
      commands.load(fis);
      fis.close();
      int count = Integer.parseInt(commands.getProperty("CommandCount"));
      for (int i = 0; i < count; i++) {
        String selection = (String) CommandComboBox.getItemAt(i);
        CommandComboBox.addItem(commands.getProperty("Command" +
            String.valueOf(i)));
      }
    }
    catch (IOException e2) {
    }
  }

  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    SaveCommands();
    try {
      _parameters.Save();
    }
    catch (IOException ex) {

    }
    System.exit(0);
  }

  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    SerialPortVerifyFrame_AboutBox dlg = new SerialPortVerifyFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.setVisible(true);
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

  void jButtonFileOpen_actionPerformed(ActionEvent e) {
    fileOpen();
  }

  void jMenuFileOpen_actionPerformed(ActionEvent e) {
    fileOpen();
  }

  void jButtonFileOpen1_actionPerformed(ActionEvent e) {
    fileOpen();
  }

  void TestButton_actionPerformed(ActionEvent e) {
    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    switch (_teststate) {
      case TEST_STATE_START:
        TestButton.setText("Abort Test");
        TestButton.setForeground(Color.red);
        BytesSentTextField.setText(String.valueOf(0));
        BytesReceivedTextField.setText(String.valueOf(0));
        ResultsTextField.setText("");
        BaudTextField.setText("");

        setParameters();
        _teststate = TEST_STATE_ABORT;
        jButtonFileOpen1.setEnabled(false);
        jTabbedPane1.setEnabled(false);
        zeroErrors();

        try {
          FileInputStream fin = new FileInputStream(TestFileTextField.getText());
          _commandTester.CleanUp(); //clean up serial connection for other tests
          _echoTester.StartTest(fin);
        }
        catch (IOException eio) {
          JOptionPane.showMessageDialog(this,
                                        "Could not open file:" +
                                        TestFileTextField.getText(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
        }
        catch (SerialConnectionException ex) {
          //update results
          JOptionPane.showMessageDialog(this,
                                        "Unable to send data: " + ex.toString(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
        break;
      case TEST_STATE_ABORT:
        _echoTester.AbortTest();
        ResultsTextField.setText("Aborted");
        jButtonFileOpen1.setEnabled(true);
        jTabbedPane1.setEnabled(true);
        break;
    }
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

  class SerialPortVerifyFrame_jMenuFileExit_ActionAdapter
      implements ActionListener {
    private SerialPortVerifyFrame adaptee;

    SerialPortVerifyFrame_jMenuFileExit_ActionAdapter(SerialPortVerifyFrame
        adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.jMenuFileExit_actionPerformed(e);
    }
  }

  class SerialPortVerifyFrame_jMenuHelpAbout_ActionAdapter
      implements ActionListener {
    private SerialPortVerifyFrame adaptee;

    SerialPortVerifyFrame_jMenuHelpAbout_ActionAdapter(SerialPortVerifyFrame
        adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.jMenuHelpAbout_actionPerformed(e);
    }
  }

  class SerialPortVerifyFrame_jButtonFileOpen_actionAdapter
      implements java.awt.event.ActionListener {
    private SerialPortVerifyFrame adaptee;

    SerialPortVerifyFrame_jButtonFileOpen_actionAdapter(SerialPortVerifyFrame
        adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.jButtonFileOpen_actionPerformed(e);
    }
  }

  class SerialPortVerifyFrame_jMenuFileOpen_actionAdapter
      implements java.awt.event.ActionListener {
    private SerialPortVerifyFrame adaptee;

    SerialPortVerifyFrame_jMenuFileOpen_actionAdapter(SerialPortVerifyFrame
        adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.jMenuFileOpen_actionPerformed(e);
    }
  }

  class SerialPortVerifyFrame_jButtonFileOpen1_actionAdapter
      implements java.awt.event.ActionListener {
    private SerialPortVerifyFrame adaptee;

    SerialPortVerifyFrame_jButtonFileOpen1_actionAdapter(SerialPortVerifyFrame
        adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.jButtonFileOpen1_actionPerformed(e);
    }
  }

  class SerialPortVerifyFrame_TestButton_actionAdapter
      implements java.awt.event.ActionListener {
    private SerialPortVerifyFrame adaptee;

    SerialPortVerifyFrame_TestButton_actionAdapter(SerialPortVerifyFrame
        adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.TestButton_actionPerformed(e);
    }
  }

  /**
   *
   * <p>Title: CommandDocument</p>
   * <p>Description: Implents an editable field which only allows HEX values to be entered
   * in format [0xFF][0x11].</p>
   * <p>Copyright: Copyright (c) 2002</p>
   * <p>Company: Carter Engineering</p>
   * @author Greg Carter
   * @version 1.0
   */
  static class CommandDocument
      extends PlainDocument {
    CommandComboBoxEditor _ed = null;
    CommandDocument(CommandComboBoxEditor ed) {
      super();
      _ed = ed;
    }

    /**
     * Overrides the default PlainDocument insertString so that the format of the input
     * can be verified.
     */
    public void insertString(int offset, String string, AttributeSet attributes) throws
        BadLocationException {
      if (string == null) {
        return;
      }
      else {
        String newValue;
        int length = getLength();
        if (length == 0) {
          newValue = string;
        }
        else {
          String currentContent = getText(0, length);
          StringBuffer currentBuffer = new StringBuffer(currentContent);
          currentBuffer.insert(offset, string);
          newValue = currentBuffer.toString();
        }
        try {
          HexString hex = new HexString();
          String ns = hex.isValidHexString(newValue);
          super.remove(0, length);
          super.insertString(0, ns, attributes);
          if ( (_ed != null) && (ns.length() > 1)) {
            _ed.setCaretPosition(ns.length() - 1);
          }

        }
        catch (Exception exception) {
          Toolkit.getDefaultToolkit().beep();
        }
      }
    }

    public void remove(int offset, int length) throws BadLocationException {
      int currentLength = getLength();
      String currentContent = getText(0, currentLength);
      String before = currentContent.substring(0, offset);
      String after = currentContent.substring(length + offset, currentLength);
      String newValue = before + after;
      try {
        //checkInput(newValue);
        //currently we let the user have a 'bad formatted' value on removes
        super.remove(offset, length);
      }
      catch (Exception exception) {
        Toolkit.getDefaultToolkit().beep();
      }
    }
  }

  /**
   *
   * <p>Title: CommandComboBoxEditor</p>
   * <p>Description: Verify input is HEX format for Combobox edit field</p>
   * <p>Copyright: Copyright (c) 2002</p>
   * <p>Company: Carter Engineering</p>
   * @author Greg Carter
   * @version 1.0
   */
  static class CommandComboBoxEditor
      extends BasicComboBoxEditor {
    CommandDocument commandDocument1 = new CommandDocument(this);
    CommandComboBoxEditor() {
      editor.setDocument(commandDocument1);
    }

    public void setCaretPosition(int position) {
      editor.setCaretPosition(position);
    }
  }

  /**
   * Checks to see if the current item in th edit field of the combobox is in the combobox list
   * if it is not in the list it is added.
   * @param e
   */
  void CommandComboBox_actionPerformed(ActionEvent e) {
    JComboBox cb = (JComboBox) e.getSource();
    String newSelection = (String) cb.getSelectedItem();
    int x = cb.getSelectedIndex();
    boolean inList = false;
    for (int i = 0; i < cb.getItemCount(); i++) {
      String selection = (String) cb.getItemAt(i);
      if (selection.equalsIgnoreCase(newSelection)) {
        inList = true;
        break;
      }
    }
    if (!inList) {
      cb.addItem(newSelection);
    }
    //currentPattern = newSelection;
    //reformat();
  }

  void SendCommandButton_actionPerformed(ActionEvent e) {
    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    zeroErrors();
    setParameters();
    try {
      updateResponse("\n");
      _echoTester.CleanUp();
      if (_commandASCII) {
        String s = CommandComboBox.getSelectedItem().toString();
        char[] c = s.toCharArray();
        HexString hexs = new HexString();
        String current = hexs.toHexFormattedString("this");
        _commandTester.StartTest(CommandComboBox.getSelectedItem().toString());
      }
      else {
        _commandTester.StartTest(CommandComboBox.getSelectedItem().toString());
      }
    }
    catch (SerialConnectionException ex) {
      //
      Toolkit.getDefaultToolkit().beep();
      updateResponse("\nError sending command test: " + ex.toString());
    }
    finally {
      this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
  }

  void BaudComboBox1_actionPerformed(ActionEvent e) {
    calcTimeToSend();
  }

  void DataBitsComboBox1_actionPerformed(ActionEvent e) {
    calcTimeToSend();
  }

  void StopBitsComboBox1_actionPerformed(ActionEvent e) {
    calcTimeToSend();
  }

  void ResponseAsASCIICheckBox_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      _displayASCII = false;
    }
    else {
      _displayASCII = true;
    }
  }

  void CommandAsASCIICheckBox_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      _commandASCII = false;
    }
    else {
      _commandASCII = true;
    }
  }

  void checkBox_CR_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      _postpendCR = false;
    }
    else {
      _postpendCR = true;
    }
  }

  void checkBox_LF_itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      _postpendLF = false;
    }
    else {
      _postpendLF = true;
    }
  }

}

//----------------------------------------------------------------------------
class SerialPortVerifyFrame_CommandComboBox_actionAdapter
    implements java.awt.event.ActionListener {
  private SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_CommandComboBox_actionAdapter(SerialPortVerifyFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.CommandComboBox_actionPerformed(e);
  }
}

class SerialPortVerifyFrame_SendCommandButton_actionAdapter
    implements java.awt.event.ActionListener {
  private SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_SendCommandButton_actionAdapter(SerialPortVerifyFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.SendCommandButton_actionPerformed(e);
  }
}

class SerialPortVerifyFrame_BaudComboBox1_actionAdapter
    implements java.awt.event.ActionListener {
  private SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_BaudComboBox1_actionAdapter(SerialPortVerifyFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.BaudComboBox1_actionPerformed(e);
  }
}

class SerialPortVerifyFrame_DataBitsComboBox1_actionAdapter
    implements java.awt.event.ActionListener {
  private SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_DataBitsComboBox1_actionAdapter(SerialPortVerifyFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.DataBitsComboBox1_actionPerformed(e);
  }
}

class SerialPortVerifyFrame_StopBitsComboBox1_actionAdapter
    implements java.awt.event.ActionListener {
  private SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_StopBitsComboBox1_actionAdapter(SerialPortVerifyFrame
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.StopBitsComboBox1_actionPerformed(e);
  }
}

class SerialPortVerifyFrame_ResponseAsASCIICheckBox_itemAdapter
    implements java.awt.event.ItemListener {
  private SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_ResponseAsASCIICheckBox_itemAdapter(
      SerialPortVerifyFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.ResponseAsASCIICheckBox_itemStateChanged(e);
  }
}

class SerialPortVerifyFrame_CommandAsASCIICheckBox_itemAdapter
    implements java.awt.event.ItemListener {
  SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_CommandAsASCIICheckBox_itemAdapter(
      SerialPortVerifyFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.CommandAsASCIICheckBox_itemStateChanged(e);
  }
}

class SerialPortVerifyFrame_checkBox_CR_itemAdapter
    implements java.awt.event.ItemListener {
  SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_checkBox_CR_itemAdapter(SerialPortVerifyFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBox_CR_itemStateChanged(e);
  }
}

class SerialPortVerifyFrame_checkBox_LF_itemAdapter
    implements java.awt.event.ItemListener {
  SerialPortVerifyFrame adaptee;

  SerialPortVerifyFrame_checkBox_LF_itemAdapter(SerialPortVerifyFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBox_LF_itemStateChanged(e);
  }
}
