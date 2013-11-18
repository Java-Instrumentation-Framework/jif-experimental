package edu.mbl.jif.camera;

import edu.mbl.jif.camacq.CamAcq;
import java.io.File;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import edu.mbl.jif.gui.*;

import java.awt.Rectangle;

import com.l2fprod.common.swing.JDirectoryChooser;
import edu.mbl.jif.imagej.IJMaker;
//import edu.mbl.jif.utils.classloaders.JarJarClassLoader;
/*
 * ((Unused))
 */

// +++ Note to put in parms string...
public class PanelDisplayController
      extends JPanel {
//
//   public final static int maxFramesToAverage = 254;
//   static boolean recording = false;
//   public JLabel value_FPS = new JLabel();
//   public JToggleButton toggleAutoExposure = new JToggleButton();
//   Border border1;
//   Border border10;
//   Border border11;
//   
//   Border border13;
//   Border border14;
//   Border border15;
//   Border border16;
//   Border border2;
//   Border border3;
//   Border border4;
//   Border border5;
//   Border border6;
//   Border border7;
//   Border border8;
//   Border border9;
//   ButtonGroup bGroup = new ButtonGroup();
//   ButtonGroup fnGroup = new ButtonGroup();
//   ButtonGroup resultGroup = new ButtonGroup();
//   
//   JButton buttonBurst = new JButton();
//   JButton buttonDirSet = new JButton();
//   JButton buttonEqualize = new JButton();
//   JButton buttonHistogram = new JButton();
//   JButton buttonLinearize = new JButton();
//   JButton buttonOpenCam = new JButton();
//   JButton buttonPlayVideo1 = new JButton();
//   
//   JButton buttonQ1 = new JButton();
//   JButton buttonQs = new JButton();
//   JButton buttonROI = new JButton();
//   
//   JButton buttonSeries = new JButton();
//   JButton buttonSetStream = new JButton();
//   JButton buttonSnap = new JButton();
//   JButton buttonStop = new JButton();
//   JButton buttonVideoRecPrefs = new JButton();
//   
//   JButton buttonZeroSerialCounter = new JButton();
//   JCheckBox checkDivide = new JCheckBox();
//   JCheckBox checkFlushFirst = new JCheckBox();
//   JCheckBox checkParms = new JCheckBox();
//   JCheckBox chkboxAutoSave = new JCheckBox();
//   JCheckBox chkboxPutInStack = new JCheckBox();
//   JCheckBox chkboxPutOnDesk = new JCheckBox();
//   JCheckBox chkboxSaveRawData = new JCheckBox();
//   JComboBox comboBox_Readout = new JComboBox();
//   JComboBox comboSettings = new JComboBox();
//   JLabel labelDirSave = new JLabel();
//   JLabel labelFileNaming = new JLabel();
//   JLabel labelFrames = new JLabel();
//   JLabel labelIntegrate = new JLabel();
//   JLabel labelInterval = new JLabel();
//   JLabel labelNumber = new JLabel();
//   JLabel labelProgress = new JLabel();
//   JLabel labelResult = new JLabel();
//   
//   JLabel labelSerialNum = new JLabel();
//   
//   //double interval = 1.0f;
//   //int numImages = 1;
//   JLabel labelSettings = new JLabel();
//   public JLabel label_Exp;
//   JLabel label_Readout = new JLabel();
//   public JLabel label_Value;
//   
//   JPanel panelCamSet = new JPanel();
//   JPanel panelCamStatus = new JPanel();
//   JPanel panelCommand = new JPanel();
//   JPanel panelExposGain = new JPanel();
//   JPanel panelFrameAvg = new JPanel();
//   JPanel panelNotification = new JPanel();
//   JPanel panelSave = new JPanel();
//   
//   JPanel panelSettings = new JPanel();
//   JPanel panelSnap = new JPanel();
//   JPanel panelVideoPlay = new JPanel();
//   JPanel tabAcq = new JPanel();
//   JPanel tabData = new JPanel();
//   JPanel tabSeries = new JPanel();
//   JPanel tabEtc = new JPanel();
//   JProgressBar progressBar = new JProgressBar();
//   JRadioButton radioResultByte = new JRadioButton();
//   JRadioButton radioResultShort = new JRadioButton();
//   JRadioButton radioSerial;
//   JRadioButton radioTimeStamp;
//   JSpinner spinAvg;
//   JSpinner spinImages;
//   JSpinner spinInterval;
//   private SpinnerExposure spinExpos;
//   private JSpinner spin_Gain;
//   private SpinnerExposure spinExposAcq;
//   private JSpinner spin_GainAcq;
//   private JSpinner spin_Offset;
//   JSpinner.NumberEditor edit_Offset;
//   JSpinner.NumberEditor editorI;
//   JSpinner.NumberEditor editorV;
//   JSpinner.NumberEditor editAvg;
//   JSpinner.NumberEditor edit_Gain;
//   JTabbedPane tabbedPaneMain = new JTabbedPane();
//   JTextField fieldPrefix = new JTextField();
//   JToggleButton toggleRecordVideo = new JToggleButton();
//   
//   boolean wasAutoExp = false;
//   private JCheckBox checkbox_Cooler = new JCheckBox();
//   private JComboBox comboBox_Binning = new JComboBox();
//   private JLabel label_Binning = new JLabel();
//   private JLabel label_Depth = new JLabel();
//   private JLabel label_FPS = new JLabel();
//   private JLabel label_Gain = new JLabel();
//   private JLabel label_Offset = new JLabel();
//   private JPanel tabCamera = new JPanel();
//   private JRadioButton radioButton_16bit = new JRadioButton();
//   private JRadioButton radioButton_8bit = new JRadioButton();
//   
//   JPanel jPanel2 = new JPanel();
//   
//   java.awt.event.ActionListener comboBinListener;
//   //
//   //  ActiveInteger aI = new ActiveInteger("aI", 4);
//   //  ActiveValueLabel testAVL = new ActiveValueLabel(aI);
//   
//   CamAcq camAcq;
//   
//   public PanelDisplayController(CamAcq camAcq) {
//      super();
//      this.camAcq = camAcq;
//      try {
//         jbInit();
//         // openDummyCamera();
//      } catch (Exception ex) {
//         ex.printStackTrace();
//      }
//   }
//   
//   
//   void jbInit() throws Exception {
//      border2 = new EtchedBorder(EtchedBorder.RAISED, new Color(171, 176, 159),
//            new Color(95, 134, 128));
//      border3 = new EtchedBorder(EtchedBorder.RAISED, new Color(97, 131, 176),
//            new Color(41, 56, 76));
//      border4 = new EtchedBorder(EtchedBorder.RAISED, new Color(217, 198, 132),
//            new Color(130, 127, 74));
//      border5 = new EtchedBorder(EtchedBorder.RAISED, new Color(34, 182, 171),
//            new Color(51, 120, 152));
//      border6 = new EtchedBorder(EtchedBorder.RAISED, new Color(199, 217, 217),
//            new Color(113, 155, 158));
//      border7 = new EtchedBorder(EtchedBorder.RAISED, new Color(142, 228, 199),
//            new Color(82, 125, 114));
//      border8 = new EtchedBorder(EtchedBorder.RAISED, new Color(80, 171, 154),
//            new Color(109, 129, 140));
//      border1 = BorderFactory.createEtchedBorder(new Color(194, 255, 255), new Color(95,
//            134, 128));
//      border9 = new EtchedBorder(EtchedBorder.RAISED, new Color(230, 228, 218),
//            new Color(156, 136, 118));
//      border10 = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white,
//            new Color(224, 255, 255), new Color(76, 90, 98), new Color(109, 129, 140));
//      border11 = BorderFactory.createLineBorder(new Color(56, 143, 171), 1);
//      //
//      border13 = new EtchedBorder(EtchedBorder.RAISED, new Color(182, 215, 198),
//            new Color(112, 151, 126));
//      border14 = new EtchedBorder(EtchedBorder.RAISED, new Color(230, 142, 158),
//            new Color(148, 145, 140));
//      border15 = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white,
//            Color.white, new Color(103, 101, 98), new Color(148, 145, 140));
//      border16 = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white,
//            new Color(231, 247, 255), new Color(79, 84, 94), new Color(113, 121, 135));
//      jPanel2.setBackground(new Color(186, 182, 106));
//      jPanel2.setBorder(BorderFactory.createEtchedBorder());
//      jPanel2.setBounds(new Rectangle(94, 95, 47, 46));
//      jPanel2.setLayout(null);
//      this.setLayout(null);
//      this.setBackground(Color.gray);
//      
//      label_Exp = new JLabel(" ");
//      label_Value = new JLabel(" ");
//      tabData.setLayout(null);
//      tabData.setBackground(new Color(110, 131, 156));
//      tabData.setMinimumSize(new Dimension(555, 40));
//      tabData.setPreferredSize(new Dimension(555, 90));
//      //
//      int numImgs = Prefs.usr.getInt("imagesInStack", 2);
//      
//      //if(numImgs < 2 || numImgs > 9999) numImgs = 2;
//      // Interval Spinner
//      double intvl = Prefs.usr.getDouble("interval", 1.0);
//      if (intvl < 0.01) {
//         intvl = 0.01;
//      }
//      spinInterval = new JSpinner(new SpinnerNumberModel(intvl, 0.01, 9999.0, 1));
//      spinImages = new JSpinner(new SpinnerNumberModel(numImgs, 1, 9999, 1));
//      editorI = new JSpinner.NumberEditor(spinImages, "0");
//      editorV = new JSpinner.NumberEditor(spinInterval, "0.0");
//      tabbedPaneMain.setBackground(new Color(250, 238, 135));
//      tabbedPaneMain.setFont(new java.awt.Font("Dialog", 0, 12));
//      tabbedPaneMain.setDoubleBuffered(false);
//      tabbedPaneMain.setBounds(new Rectangle(148, 27, 353, 126));
//      
//      buttonSnap.setText("");
//      buttonSnap.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonSnap_actionPerformed(e);
//         }
//      });
//      buttonSnap.setMargin(new Insets(1, 1, 1, 1));
//      buttonSnap.setBounds(new Rectangle(8, 7, 34, 35));
//      buttonSnap.setBorder(BorderFactory.createRaisedBevelBorder());
//      buttonSnap.setMaximumSize(new Dimension(32, 32));
//      buttonSnap.setMinimumSize(new Dimension(32, 32));
//      buttonSnap.setPreferredSize(new Dimension(32, 32));
//      buttonSnap.setToolTipText("Acquire an Image");
//      buttonSnap.setBorderPainted(true);
//      buttonSnap.setIcon(new ImageIcon(this.getClass().getResource("icons/acqone.gif")));
//      buttonSeries.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonSeries_actionPerformed(e);
//         }
//      });
//      buttonSeries.setMargin(new Insets(0, 0, 0, 0));
//      buttonSeries.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/acqseries.gif")));
//      buttonSeries.setToolTipText("Acquire Series");
//      buttonSeries.setPreferredSize(new Dimension(32, 32));
//      buttonSeries.setBounds(new Rectangle(47, 6, 35, 36));
//      buttonSeries.setBorder(BorderFactory.createRaisedBevelBorder());
//      buttonSeries.setMaximumSize(new Dimension(32, 32));
//      buttonSeries.setMinimumSize(new Dimension(32, 32));
//      makeTabAcq();
//      
//      buttonROI.setBounds(new Rectangle(251, 6, 38, 37));
//      buttonROI.setFont(new java.awt.Font("Dialog", 0, 10));
//      buttonROI.setBorder(BorderFactory.createRaisedBevelBorder());
//      buttonROI.setToolTipText("Set Camera ROI to selected ROI");
//      buttonROI.setHorizontalAlignment(SwingConstants.CENTER);
//      buttonROI.setIcon(new ImageIcon(this.getClass().getResource("icons/zoomroi32.gif")));
//      buttonROI.setMargin(new Insets(0, 0, 0, 0));
//      buttonROI.setText("");
//      buttonROI.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonROI_actionPerformed(e);
//         }
//      });
//      buttonOpenCam.setBounds(new Rectangle(9, 9, 35, 34));
//      buttonOpenCam.setFont(new java.awt.Font("Dialog", 0, 10));
//      buttonOpenCam.setBorder(BorderFactory.createRaisedBevelBorder());
//      buttonOpenCam.setToolTipText("Open Camera");
//      buttonOpenCam.setIcon(new ImageIcon(this.getClass().getResource("icons/camera1.gif")));
//      buttonOpenCam.setMargin(new Insets(0, 0, 0, 0));
//      buttonOpenCam.setText("");
//      buttonOpenCam.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonOpenCam_actionPerformed(e);
//         }
//      });
//      
//      //
//      label_Readout.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Readout.setRequestFocusEnabled(true);
//      label_Readout.setBounds(new Rectangle(6, 61, 47, 18));
//      label_Readout.setText("Readout:");
//      comboBox_Readout.setFont(new java.awt.Font("SansSerif", 0, 10));
//      comboBox_Readout.setBounds(new Rectangle(54, 60, 65, 22));
//      //comboBox_Readout.setSelectedIndex(0);
//      comboBox_Readout.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            comboBox_Readout_actionPerformed(e);
//         }
//      });
//      comboBox_Readout.setEnabled(false);
//      comboBox_Readout.addItem("20 MHz");
//      comboBox_Readout.addItem("10 MHz");
//      comboBox_Readout.addItem("5 MHz");
//      comboBox_Readout.addItem("2.5 MHz");
//      //
//      label_Gain.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Gain.setHorizontalAlignment(SwingConstants.RIGHT);
//      label_Offset.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Offset.setHorizontalAlignment(SwingConstants.RIGHT);
//      
//      toggleRecordVideo.setBorder(BorderFactory.createRaisedBevelBorder());
//      toggleRecordVideo.setMaximumSize(new Dimension(32, 32));
//      toggleRecordVideo.setMinimumSize(new Dimension(32, 32));
//      toggleRecordVideo.setPreferredSize(new Dimension(32, 32));
//      toggleRecordVideo.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/Movie24.gif")));
//      toggleRecordVideo.setSelectedIcon(new ImageIcon(this.getClass().getResource(
//            "icons/Movie24inv.gif")));
//      toggleRecordVideo.setMargin(new Insets(2, 4, 2, 4));
//      toggleRecordVideo.setText("");
//      toggleRecordVideo.setBounds(new Rectangle(7, 8, 34, 32));
//      toggleRecordVideo.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            toggleRecordVideo_actionPerformed(e);
//         }
//      });
//      checkbox_Cooler.setFont(new java.awt.Font("Dialog", 0, 10));
//      checkbox_Cooler.setHorizontalAlignment(SwingConstants.RIGHT);
//      checkbox_Cooler.setHorizontalTextPosition(SwingConstants.LEADING);
//      checkbox_Cooler.setSelected(Prefs.usr.getBoolean("coolerActive", true));
//      
//      label_FPS.setHorizontalAlignment(SwingConstants.CENTER);
//      panelSettings.setBackground(new Color(162, 173, 194));
//      panelSettings.setEnabled(true);
//      panelSettings.setVisible(false);
//      panelSettings.setBorder(BorderFactory.createEtchedBorder());
//      panelSettings.setBounds(new Rectangle(294, 5, 47, 43));
//      panelSettings.setLayout(null);
//      panelExposGain.setBackground(new Color(119, 184, 184));
//      panelExposGain.setBorder(border5);
//      panelExposGain.setDebugGraphicsOptions(0);
//      panelExposGain.setBounds(new Rectangle(115, 4, 130, 91));
//      panelExposGain.setLayout(null);
//      label_Depth.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Binning.setFont(new java.awt.Font("Dialog", 0, 10));
//      //
//      panelCamSet.setBackground(new Color(162, 222, 226));
//      panelCamSet.setBorder(border6);
//      panelCamSet.setBounds(new Rectangle(250, 54, 43, 39));
//      panelCamSet.setLayout(null);
//      //
//      panelCamStatus.setBackground(new Color(162, 173, 194));
//      panelCamStatus.setBorder(border16);
//      panelCamStatus.setBounds(new Rectangle(148, 1, 354, 23));
//      panelCamStatus.setLayout(null);
//      //
//      label_Exp.setHorizontalTextPosition(SwingConstants.LEFT);
//      label_Exp.setBounds(new Rectangle(5, 3, 127, 14));
//      label_Exp.setHorizontalAlignment(SwingConstants.LEFT);
//      label_Exp.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Value.setHorizontalTextPosition(SwingConstants.LEFT);
//      label_Value.setBounds(new Rectangle(166, 2, 161, 14));
//      label_Value.setFont(new java.awt.Font("Dialog", 0, 10));
//      //
//      toggleAutoExposure.setToolTipText("AutoExposure");
//      toggleAutoExposure.setContentAreaFilled(true);
//      toggleAutoExposure.setFocusPainted(true);
//      toggleAutoExposure.setMargin(new Insets(0, 0, 0, 0));
//      toggleAutoExposure.setMnemonic('0');
//      toggleAutoExposure.setText("");
//      toggleAutoExposure.setBounds(new Rectangle(119, 17, 15, 16));
//      toggleAutoExposure.setBackground(new Color(230, 164, 131));
//      toggleAutoExposure.setFont(new java.awt.Font("Dialog", 0, 10));
//      toggleAutoExposure.setMinimumSize(new Dimension(5, 5));
//      toggleAutoExposure.setOpaque(true);
//      toggleAutoExposure.setPreferredSize(new Dimension(5, 5));
//      toggleAutoExposure.setRequestFocusEnabled(true);
//      toggleAutoExposure.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            toggleAutoExposure_actionPerformed(e);
//         }
//      });
//      chkboxAutoSave.setFont(new java.awt.Font("Dialog", 0, 10));
//      chkboxAutoSave.setOpaque(false);
//      chkboxAutoSave.setHorizontalAlignment(SwingConstants.RIGHT);
//      chkboxAutoSave.setHorizontalTextPosition(SwingConstants.LEADING);
//      chkboxAutoSave.setSelected(Prefs.usr.getBoolean("autoSave", true));
//      chkboxAutoSave.setText("AutoSave");
//      chkboxAutoSave.setBounds(new Rectangle(231, 10, 78, 13));
//      chkboxAutoSave.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            chkboxAutoSave_actionPerformed(e);
//         }
//      });
//      buttonDirSet.setBounds(new Rectangle(8, 6, 28, 26));
//      buttonDirSet.setToolTipText("Set Directory");
//      buttonDirSet.setHorizontalTextPosition(SwingConstants.TRAILING);
//      buttonDirSet.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/directory.gif")));
//      buttonDirSet.setMargin(new Insets(0, 0, 0, 0));
//      buttonDirSet.setText("");
//      buttonDirSet.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonDirSet_actionPerformed(e);
//         }
//      });
//      chkboxPutOnDesk.setFont(new java.awt.Font("Dialog", 0, 10));
//      chkboxPutOnDesk.setOpaque(false);
//      chkboxPutOnDesk.setHorizontalAlignment(SwingConstants.CENTER);
//      chkboxPutOnDesk.setHorizontalTextPosition(SwingConstants.LEADING);
//      chkboxPutOnDesk.setSelected(Prefs.usr.getBoolean("putOnDesk", true));
//      chkboxPutOnDesk.setText("Put on Desktop");
//      chkboxPutOnDesk.setBounds(new Rectangle(213, 29, 96, 19));
//      chkboxPutOnDesk.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            chkboxPutOnDesk_actionPerformed(e);
//         }
//      });
//      
//      //
//      Action actionTimeStamp = new AbstractAction("TimeStamp") {
//         public void actionPerformed(ActionEvent evt) {
//            Prefs.usr.putBoolean("nameFileTimestamp", true);
//         }
//      };
//      Action actionSerial = new AbstractAction("Serial") {
//         public void actionPerformed(ActionEvent evt) {
//            Prefs.usr.putBoolean("nameFileTimestamp", false);
//         }
//      };
//      radioSerial = new JRadioButton(actionSerial);
//      radioTimeStamp = new JRadioButton(actionTimeStamp);
//      radioSerial.setFont(new java.awt.Font("Dialog", 0, 10));
//      radioSerial.setOpaque(false);
//      radioSerial.setToolTipText("");
//      radioSerial.setMargin(new Insets(0, 0, 0, 0));
//      radioSerial.setText("Serial w/ Prefix:");
//      radioSerial.setBounds(new Rectangle(7, 68, 97, 15));
//      fieldPrefix.setFont(new java.awt.Font("Dialog", 0, 10));
//      fieldPrefix.setToolTipText("Prefix for filenames");
//      fieldPrefix.setText(Prefs.usr.get("prefix", "Image"));
//      fieldPrefix.setBounds(new Rectangle(105, 65, 62, 20));
//      fieldPrefix.addFocusListener(new java.awt.event.FocusAdapter() {
//         public void focusLost(FocusEvent e) {
//            fieldPrefix_focusLost(e);
//         }
//      });
//      radioTimeStamp.setBounds(new Rectangle(5, 52, 88, 15));
//      radioTimeStamp.setText("Time stamp");
//      radioTimeStamp.setFont(new java.awt.Font("Dialog", 0, 10));
//      radioTimeStamp.setOpaque(false);
//      //
//      buttonZeroSerialCounter.setBounds(new Rectangle(207, 67, 27, 18));
//      buttonZeroSerialCounter.setFont(new java.awt.Font("Dialog", 0, 9));
//      buttonZeroSerialCounter.setBorder(BorderFactory.createRaisedBevelBorder());
//      buttonZeroSerialCounter.setToolTipText("Zero the filename counter");
//      buttonZeroSerialCounter.setMargin(new Insets(2, 2, 2, 2));
//      buttonZeroSerialCounter.setText("zero");
//      buttonZeroSerialCounter.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonZeroCounter_actionPerformed(e);
//         }
//      });
//      chkboxSaveRawData.setVisible(false);
//      chkboxSaveRawData.setHorizontalAlignment(SwingConstants.RIGHT);
//      chkboxSaveRawData.setHorizontalTextPosition(SwingConstants.LEADING);
//      
//      labelSerialNum.setFont(new java.awt.Font("Dialog", 0, 9));
//      labelSerialNum.setBorder(border15);
//      labelSerialNum.setToolTipText("Current serial# for filename");
//      labelSerialNum.setHorizontalAlignment(SwingConstants.CENTER);
//      labelSerialNum.setText(String.valueOf(Prefs.usr.getInt("fileCounter", 1)));
//      labelSerialNum.setBounds(new Rectangle(175, 66, 26, 19));
//      
//      buttonLinearize.setMargin(new Insets(0, 0, 0, 0));
//      buttonLinearize.setText("");
//      buttonLinearize.setBounds(new Rectangle(9, 53, 32, 32));
//      buttonLinearize.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/linear.gif")));
//      buttonLinearize.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonLinearize_actionPerformed(e);
//         }
//      });
//      buttonHistogram.setBounds(new Rectangle(78, 57, 61, 24));
//      buttonHistogram.setEnabled(false);
//      buttonHistogram.setFont(new java.awt.Font("Dialog", 0, 10));
//      buttonHistogram.setMargin(new Insets(2, 4, 2, 4));
//      buttonHistogram.setText("Histogram");
//      buttonEqualize.setBounds(new Rectangle(41, 53, 32, 32));
//      buttonEqualize.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/equalize.gif")));
//      buttonEqualize.setMargin(new Insets(0, 0, 0, 0));
//      buttonEqualize.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonEqualize_actionPerformed(e);
//         }
//      });
//      tabSeries.setLayout(null);
//      tabSeries.setBackground(new Color(123, 189, 153));
//      labelFileNaming.setFont(new java.awt.Font("Dialog", 0, 10));
//      labelFileNaming.setText("File Naming:");
//      labelFileNaming.setBounds(new Rectangle(8, 36, 85, 15));
//      panelCommand.setBackground(Color.lightGray);
//      panelCommand.setBorder(null);
//      panelCommand.setBounds(new Rectangle(0, 1, 147, 150));
//      panelCommand.setLayout(null);
//      
//      checkFlushFirst.setEnabled(false);
//      checkFlushFirst.setFont(new java.awt.Font("Dialog", 0, 10));
//      checkFlushFirst.setOpaque(false);
//      checkFlushFirst.setText("Flush 1st");
//      checkFlushFirst.setBounds(new Rectangle(263, 30, 71, 13));
//      checkFlushFirst.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent actionEvent) {
//            checkFlushFirst_actionPerformed(actionEvent);
//         }
//      });
//      labelInterval.setBounds(new Rectangle(115, 14, 82, 16));
//      labelInterval.setVerticalTextPosition(SwingConstants.BOTTOM);
//      labelInterval.setToolTipText("");
//      labelInterval.setHorizontalAlignment(SwingConstants.RIGHT);
//      labelInterval.setIcon(null);
//      labelInterval.setText("Time interval:");
//      labelInterval.setFont(new java.awt.Font("Dialog", 0, 12));
//      spinInterval.setBorder(BorderFactory.createEtchedBorder());
//      spinInterval.setFont(new java.awt.Font("Dialog", 0, 10));
//      spinInterval.setToolTipText("Time between series images, seconds");
//      spinInterval.setBounds(new Rectangle(202, 8, 54, 28));
//      spinInterval.setEditor(editorV);
//      spinInterval.addChangeListener(new ChangeListener() {
//         public void stateChanged(ChangeEvent se) {
//            System.out.println(((JSpinner) se.getSource()).getValue());
//            double interval = ((SpinnerNumberModel) ((JSpinner) se.getSource()).getModel()).
//                  getNumber().doubleValue();
//            Prefs.usr.putDouble("interval", interval);
//            camAcq.setupSeries(Prefs.usr.getInt("imagesInStack", 2),
//                  interval);
//         }
//      });
//      chkboxPutInStack.setEnabled(false);
//      chkboxPutInStack.setFont(new java.awt.Font("Dialog", 0, 10));
//      chkboxPutInStack.setOpaque(false);
//      chkboxPutInStack.setHorizontalAlignment(SwingConstants.LEFT);
//      chkboxPutInStack.setHorizontalTextPosition(SwingConstants.TRAILING);
//      chkboxPutInStack.setSelected(true);
//      chkboxPutInStack.setText("Put in Stack");
//      chkboxPutInStack.setBounds(new Rectangle(263, 8, 85, 18));
//      chkboxPutInStack.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            chkboxPutInStack_actionPerformed(e);
//         }
//      });
//      spinImages.setBorder(BorderFactory.createEtchedBorder());
//      spinImages.setFont(new java.awt.Font("Dialog", 0, 10));
//      spinImages.setToolTipText("Number of Images to acquire in series");
//      spinImages.setBounds(new Rectangle(60, 8, 54, 28));
//      spinImages.setEditor(editorI);
//      spinImages.addChangeListener(new ChangeListener() {
//         public void stateChanged(ChangeEvent se) {
//            System.out.println(((JSpinner) se.getSource()).getValue());
//            int n = ((SpinnerNumberModel) ((JSpinner) se.getSource()).getModel()).
//                  getNumber().intValue();
//            Prefs.usr.putInt("imagesInStack", n);
//            camAcq.setupSeries(n, Prefs.usr.getDouble("interval", 1.0));
//         }
//      });
//      labelNumber.setFont(new java.awt.Font("Dialog", 0, 12));
//      labelNumber.setHorizontalAlignment(SwingConstants.RIGHT);
//      labelNumber.setText("Number:");
//      labelNumber.setBounds(new Rectangle(2, 13, 55, 17));
//      buttonStop.setBounds(new Rectangle(302, 75, 37, 17));
//      panelVideoPlay.setLayout(null);
//      panelVideoPlay.setBounds(new Rectangle(8, 48, 97, 45));
//      panelVideoPlay.setBorder(border4);
//      panelVideoPlay.setBackground(new Color(186, 182, 106));
//      buttonVideoRecPrefs.setBounds(new Rectangle(55, 7, 32, 32));
//      buttonVideoRecPrefs.setEnabled(false);
//      buttonVideoRecPrefs.setToolTipText("Video Recording Preferences");
//      buttonVideoRecPrefs.setHorizontalTextPosition(SwingConstants.TRAILING);
//      buttonVideoRecPrefs.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/MoviePrefs.gif")));
//      buttonVideoRecPrefs.setMargin(new Insets(0, 0, 0, 0));
//      buttonVideoRecPrefs.setText("");
//      buttonPlayVideo1.setBounds(new Rectangle(9, 7, 34, 33));
//      buttonPlayVideo1.setEnabled(false);
//      buttonPlayVideo1.setToolTipText("Play Video Recording");
//      buttonPlayVideo1.setVerifyInputWhenFocusTarget(true);
//      buttonPlayVideo1.setIcon(new ImageIcon(this.getClass().getResource(
//            "icons/OpenSeries.gif")));
//      buttonPlayVideo1.setMargin(new Insets(0, 0, 0, 0));
//      buttonPlayVideo1.setText("");
//      buttonPlayVideo1.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonPlayVideo1_actionPerformed(e);
//         }
//      });
//      panelSave.setBorder(null);
//      
//      labelDirSave.setBorder(BorderFactory.createLoweredBevelBorder());
//      buttonSetZero.setBounds(new Rectangle(297, 65, 47, 21));
//      buttonSetZero.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
//      buttonSetZero.setVisible(false);
//      buttonSetZero.setMargin(new Insets(2, 2, 2, 2));
//      buttonSetZero.setText("SetZero");
//      buttonSetZero.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonSetZero_actionPerformed(e);
//         }
//      });
//      ButtonPrefs.setBounds(new Rectangle(10, 38, 86, 22));
//      ButtonPrefs.setMargin(new Insets(2, 2, 2, 2));
//      ButtonPrefs.setText("Preferences");
//      ButtonPrefs.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            ButtonPrefs_actionPerformed(e);
//         }
//      });
//      buttonListCameraParms.setBounds(new Rectangle(10, 9, 86, 22));
//      buttonListCameraParms.setToolTipText("");
//      buttonListCameraParms.setText("Camera");
//      buttonListCameraParms.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonListCameraParms_actionPerformed(e);
//         }
//      });
//      buttonConsole.setBounds(new Rectangle(251, 11, 87, 24));
//      buttonConsole.setText("Console");
//      buttonConsole.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonConsole_actionPerformed(e);
//         }
//      });
//      buttonCamAcqParms.setBounds(new Rectangle(251, 39, 86, 23));
//      buttonCamAcqParms.setFont(new java.awt.Font("Bitstream Vera Serif", Font.PLAIN, 9));
//      buttonCamAcqParms.setMargin(new Insets(2, 4, 2, 4));
//      buttonCamAcqParms.setText("CamAcq.parms");
//      buttonCamAcqParms.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent actionEvent) {
//            buttonCamAcqParms_actionPerformed(actionEvent);
//         }
//      });
//      buttonMemMon.setBounds(new Rectangle(251, 66, 87, 25));
//      buttonMemMon.setFont(new java.awt.Font("Bitstream Vera Serif", Font.PLAIN, 10));
//      buttonMemMon.setToolTipText("Memory Monitor");
//      buttonMemMon.setText("MemMon");
//      buttonMemMon.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent actionEvent) {
//            buttonMemMon_actionPerformed(actionEvent);
//         }
//      });
//      buttonTest1.setBounds(new Rectangle(134, 9, 69, 26));
//      buttonTest1.setText("Test 1");
//      buttonTest1.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent actionEvent) {
//            buttonTest1_actionPerformed(actionEvent);
//         }
//      });
//      fnGroup.add(radioSerial);
//      fnGroup.add(radioTimeStamp);
//      radioTimeStamp.setSelected(Prefs.usr.getBoolean("nameFileTimestamp", true));
//      radioSerial.setSelected(!radioTimeStamp.isSelected());
//      //
//      //
//      panelSave.setLayout(null);
//      panelSave.setBounds(new Rectangle(0, 1, 348, 104));
//      panelSave.setBackground(new Color(161, 174, 169));
//      panelSnap.setBackground(new Color(78, 162, 170));
//      panelSnap.setBorder(BorderFactory.createEtchedBorder());
//      panelSnap.setBounds(new Rectangle(8, 95, 88, 46));
//      panelSnap.setLayout(null);
//      labelSettings.setFont(new java.awt.Font("Dialog", 1, 14));
//      labelSettings.setToolTipText("");
//      labelSettings.setText("#");
//      labelSettings.setVerticalTextPosition(SwingConstants.BOTTOM);
//      labelSettings.setBounds(new Rectangle(12, 9, 15, 16));
//      labelSettings.setFont(new java.awt.Font("Dialog", 0, 10));
//      labelSettings.setText("Preset");
//      labelSettings.setBounds(new Rectangle(8, 0, 46, 18));
//      buttonSetStream.setBounds(new Rectangle(438, 30, 62, 21));
//      buttonSetStream.setFont(new java.awt.Font("Dialog", 0, 10));
//      buttonSetStream.setToolTipText("");
//      buttonSetStream.setMargin(new Insets(2, 1, 2, 1));
//      buttonSetStream.setText("SetStream");
//      buttonSetStream.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonSetStream_actionPerformed(e);
//         }
//      });
//      
//      buttonBurst.setMargin(new Insets(0, 0, 0, 0));
//      buttonBurst.setBounds(new Rectangle(56, 11, 41, 37));
//      buttonBurst.setFont(new java.awt.Font("Dialog", 0, 10));
//      buttonBurst.setBorder(border10);
//      buttonBurst.setToolTipText("Acquire Burst Series");
//      buttonBurst.setIcon(new ImageIcon(this.getClass().getResource("icons/burstAcq.gif")));
//      buttonBurst.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            buttonBurst_actionPerformed(e);
//         }
//      });
//      labelDirSave.setFont(new java.awt.Font("Dialog", 0, 12));
//      labelDirSave.setForeground(new Color(108, 58, 64));
//      labelDirSave.setText(Prefs.usr.get("directory.imageData", "\\"));
//      labelDirSave.setBounds(new Rectangle(42, 9, 176, 21));
//      chkboxSaveRawData.setFont(new java.awt.Font("Dialog", 0, 10));
//      chkboxSaveRawData.setOpaque(false);
//      chkboxSaveRawData.setText("Save Raw");
//      chkboxSaveRawData.setSelected(Prefs.usr.getBoolean("saveRawData", false));
//      chkboxSaveRawData.setBounds(new Rectangle(233, 52, 76, 15));
//      chkboxSaveRawData.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            chkboxSaveRawData_actionPerformed(e);
//         }
//      });
//      chkboxSaveRawData.setEnabled(false);
//      panelNotification.setBounds(new Rectangle( -2, 153, 503, 20));
//      panelNotification.setLayout(null);
//      progressBar.setBounds(new Rectangle(315, 3, 180, 14));
//      labelProgress.setFont(new java.awt.Font("Dialog", 0, 10));
//      labelProgress.setText(".");
//      labelProgress.setBounds(new Rectangle(7, 2, 307, 15));
//      comboSettings.setEnabled(false);
//      
//      //testAVL.setBounds(new Rectangle(396, 8, 37, 19));
//      
//      panelCamStatus.add(label_Exp, null);
//      panelCamStatus.add(label_Value, null);
//      panelSnap.add(buttonSnap, null);
//      panelSnap.add(buttonSeries, null);
//      panelCommand.add(jPanel2, null);
//      jPanel2.add(toggleRecordVideo, null);
//      panelCommand.add(panelSnap, null);
//      
//      panelCommand.add(buttonOpenCam, null);
//      spinExpos = camAcq.spinExpos;
//      panelCommand.add(spinExpos, null);
//      panelCommand.add(toggleAutoExposure, null);
//      panelCommand.add(buttonHistogram, null);
//      panelCommand.add(buttonLinearize, null);
//      panelCommand.add(buttonEqualize, null);
//      panelNotification.add(labelProgress, null);
//      panelNotification.add(progressBar, null);
//      this.add(panelCamStatus, null);
//      // panelSeriesAcq.add(buttonBurst, null);
//      // panelOther.add(buttonQ1, null);
//      // panelOther.add(buttonSetStream, null);
//      // panelOther.add(buttonQs, null);
//      //
//      
//      label_FPS.setFont(new java.awt.Font("Dialog", 1, 10));
//      label_FPS.setBounds(new Rectangle(3, 4, 37, 17));
//      value_FPS.setFont(new java.awt.Font("Dialog", 1, 10));
//      value_FPS.setHorizontalAlignment(SwingConstants.CENTER);
//      value_FPS.setBounds(new Rectangle(4, 19, 34, 14));
//      label_Binning.setToolTipText("");
//      label_Depth.setBounds(new Rectangle(7, 32, 32, 24));
//      radioButton_8bit.setOpaque(false);
//      radioButton_8bit.setBounds(new Rectangle(51, 31, 35, 28));
//      radioButton_16bit.setOpaque(false);
//      radioButton_16bit.setBounds(new Rectangle(84, 31, 44, 28));
//      tabCamera.setBackground(new Color(101, 146, 166));
//      tabCamera.setBorder(null);
//      tabCamera.setOpaque(true);
//      // --- Binning
//      label_Binning.setText("Binning:");
//      label_Binning.setBounds(new Rectangle(7, 9, 42, 18));
//      
//      comboBox_Binning.setToolTipText("Binning");
//      comboBox_Binning.setVerifyInputWhenFocusTarget(false);
//      comboBox_Binning.setBounds(new Rectangle(54, 8, 65, 22));
//      comboBox_Binning.addItem("1 x 1");
//      comboBox_Binning.addItem("2 x 2");
//      comboBox_Binning.addItem("4 x 4");
//      //comboBox_Binning.addItem("8 x 8");
//      int binIndex = 0;
//      switch (Prefs.usr.getInt("camera.binning", 2)) {
//         case 1: // 1 x 1
//            binIndex = 0;
//            break;
//         case 2: // 2 x 2
//            binIndex = 1;
//            break;
//         case 4: // 4 x 4
//            binIndex = 2;
//      }
//      comboBox_Binning.setSelectedIndex(binIndex);
//      comboBinListener = new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            comboBox_Binning_actionPerformed(e);
//         }
//      };
//      comboBox_Binning.addActionListener(comboBinListener);
//      
//      // --- Depth
//      radioButton_8bit.setSelected(true);
//      radioButton_8bit.setText("8");
//      radioButton_8bit.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            radioButton_8bit_actionPerformed(e);
//         }
//      });
//      radioButton_16bit.setText("12");
//      radioButton_16bit.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            radioButton_16bit_actionPerformed(e);
//         }
//      });
//      label_Depth.setText("Bits:");
//      // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//      radioButton_16bit.setEnabled(true);
//      radioButton_8bit.setEnabled(true);
//      comboSettings.setFont(new java.awt.Font("Dialog", 0, 10));
//      comboSettings.setBounds(new Rectangle(4, 18, 40, 21));
//      bGroup.add(radioButton_16bit);
//      bGroup.add(radioButton_8bit);
//      resultGroup.add(radioResultByte);
//      resultGroup.add(radioResultShort);
//      tabCamera.setBounds(new Rectangle(1, 1, 376, 339));
//      tabCamera.setLayout(null);
//      label_Gain.setText("Gain:");
//      label_Gain.setBounds(new Rectangle(8, 10, 33, 18));
//      label_Offset.setText("Offset:");
//      label_Offset.setBounds(new Rectangle(8, 41, 34, 18));
//      label_FPS.setText("FPS:");
//      value_FPS.setText("00.0");
//      // Spinner for Exposure ---------------------------------------------
//      spinExpos.setBorder(border1);
//      spinExpos.setBounds(new Rectangle(54, 13, 60, 25));
//      // Spinner for Gain ---------------------------------------------
//      spin_Gain = new JSpinner(camAcq.model_Gain);
//      edit_Gain = new JSpinner.NumberEditor(spin_Gain, "0.0");
//      spin_Gain.setEditor(edit_Gain);
//      spin_Gain.setBorder(BorderFactory.createEtchedBorder());
//      spin_Gain.setBounds(new Rectangle(47, 7, 60, 25));
//      ChangeListener listener_Gain = new ChangeListener() {
//         public void stateChanged(ChangeEvent e) {
//            SpinnerModel source = (SpinnerModel) e.getSource();
//            String inStr = String.valueOf(source.getValue());
//            float value = 1.0f;
//            try {
//               value = Float.parseFloat(inStr);
//            } catch (NumberFormatException nfe) {}
//            try {
//               //camAcq.setExposureStream(value);
//               camAcq.setGainSteam(value);
//               camAcq.setGainAcq(value);
//            } catch (Exception ex) {
//               System.out.println("Error in : " + ex);
//            }
//            //?? camAcq.displayResume();
//         }
//      };
//      camAcq.model_Gain.addChangeListener(listener_Gain);
//      spin_Gain.setEnabled(true);
//      
//      // Spinner for Offset ---------------------------------------------
//      spin_Offset = new JSpinner(camAcq.model_Offset);
//      edit_Offset = new JSpinner.NumberEditor(spin_Offset, "0");
//      spin_Offset.setEditor(edit_Offset);
//      spin_Offset.setBorder(BorderFactory.createEtchedBorder());
//      spin_Offset.setBounds(new Rectangle(47, 38, 60, 25));
//      ChangeListener listener_Offset = new ChangeListener() {
//         public void stateChanged(ChangeEvent e) {
//            SpinnerModel source = (SpinnerModel) e.getSource();
//            String inStr = String.valueOf(source.getValue());
//            int value = 0;
//            try {
//               value = Integer.parseInt(inStr);
//            } catch (NumberFormatException nfe) {}
//            // Change the setting
//            try {
//               camAcq.setOffset(value);
//            } catch (Exception ex) {
//               System.out.println("Error in : " + ex);
//            }
//            //?? camAcq.displayResume();
//         }
//      };
//      camAcq.model_Offset.addChangeListener(listener_Offset);
//      // --- Cooler
//      checkbox_Cooler.setBackground(SystemColor.control);
//      checkbox_Cooler.setToolTipText("");
//      checkbox_Cooler.setMargin(new Insets(2, 5, 2, 2));
//      checkbox_Cooler.setText("Cooler");
//      checkbox_Cooler.setOpaque(false);
//      checkbox_Cooler.setBounds(new Rectangle(26, 69, 84, 18));
//      checkbox_Cooler.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            checkbox_Cooler_actionPerformed(e);
//         }
//      });
//      checkbox_Cooler.setEnabled(true);
//      //
//      panelSave.add(radioSerial, null);
//      panelSave.add(buttonDirSet, null);
//      panelSave.add(radioTimeStamp, null);
//      panelSave.add(chkboxAutoSave, null);
//      panelSave.add(fieldPrefix, null);
//      panelSave.add(labelSerialNum, null);
//      panelSave.add(buttonZeroSerialCounter, null);
//      panelSave.add(labelDirSave, null);
//      panelSave.add(chkboxPutOnDesk, null);
//      panelSave.add(chkboxSaveRawData, null);
//      panelSave.add(labelFileNaming, null);
//      
//      panelExposGain.add(comboBox_Readout, null);
//      panelExposGain.add(label_Readout, null);
//      panelExposGain.add(comboBox_Binning, null);
//      panelExposGain.add(label_Binning, null);
//      panelExposGain.add(label_Depth, null);
//      panelExposGain.add(radioButton_8bit, null);
//      panelExposGain.add(radioButton_16bit, null);
//      tabCamera.add(panelSettings, null);
//      
//      panelCamSet.add(value_FPS, null);
//      panelCamSet.add(label_FPS, null);
//      tabCamera.add(buttonROI, null);
//      tabCamera.add(buttonSetZero);
//      tabCamera.add(checkbox_Cooler, null);
//      tabCamera.add(panelCamSet, null);
//      tabCamera.add(panelExposGain, null);
//      
//      tabCamera.add(label_Gain, null);
//      tabCamera.add(spin_Gain, null);
//      tabCamera.add(spin_Offset, null);
//      tabCamera.add(label_Offset, null);
//      
//      panelSettings.add(labelSettings, null);
//      panelSettings.add(comboSettings, null);
//      //
//      panelVideoPlay.add(buttonVideoRecPrefs, null);
//      panelVideoPlay.add(buttonPlayVideo1, null);
//      tabSeries.add(buttonStop, null);
//      tabSeries.add(labelNumber, null);
//      tabSeries.add(spinInterval, null);
//      tabSeries.add(spinImages, null);
//      tabSeries.add(labelInterval, null);
//      tabSeries.add(chkboxPutInStack, null);
//      tabSeries.add(checkFlushFirst, null);
//      tabData.add(panelSave, null);
//      tabSeries.add(panelVideoPlay, null);
//      //
//      tabEtc.add(buttonListCameraParms);
//      tabEtc.add(ButtonPrefs);
//      tabEtc.add(buttonConsole);
//      tabEtc.add(buttonCamAcqParms);
//      tabEtc.add(buttonMemMon);
//      tabEtc.add(buttonTest1);
//      tabEtc.setLayout(null);
//      tabEtc.setBackground(new Color(123, 100, 123));
//      //
//      tabbedPaneMain.add(tabCamera, "Camera");
//      tabbedPaneMain.add(tabAcq, "Image");
//      tabbedPaneMain.add(tabSeries, "Series");
//      tabbedPaneMain.add(tabData, "Data");
//      tabbedPaneMain.add(tabEtc, "etc");
//      tabbedPaneMain.setBackgroundAt(0, tabCamera.getBackground());
//      tabbedPaneMain.setBackgroundAt(1, tabAcq.getBackground());
//      tabbedPaneMain.setBackgroundAt(2, tabSeries.getBackground());
//      tabbedPaneMain.setBackgroundAt(3, tabData.getBackground());
//      tabbedPaneMain.setBackgroundAt(4, tabEtc.getBackground());
//      //
//      tabbedPaneMain.setSelectedComponent(tabCamera);
//      tabbedPaneMain.setMinimumSize(new Dimension(600, 40));
//      tabbedPaneMain.setPreferredSize(new Dimension(600, 90));
//      //
//      this.add(panelCommand, null);
//      this.add(tabbedPaneMain, null);
//      this.add(panelNotification, null);
//      validate();
//      updateUI();
//      //synchValues();
//      enableFunctions(true);
//      if (Globals.deBug) {
//         enableFunctions(true);
//      }
//   }
//   
//   
//   //------------------------------------------------------------------------------
//   // Tab: Acquistion
//   void makeTabAcq() {
//      tabAcq.setLayout(null);
//      tabAcq.setBackground(new Color(64, 107, 109));
//      tabAcq.add(panelFrameAvg, null);
//      
//      panelFrameAvg.setBackground(new Color(117, 160, 162));
//      panelFrameAvg.setBorder(null);
//      panelFrameAvg.setOpaque(true);
//      panelFrameAvg.setBounds(new Rectangle(1, 1, 345, 102));
//      panelFrameAvg.setLayout(null);
//      
//      int frmAvg = Prefs.usr.getInt("framesToAvg", 1);
//      spinAvg = new JSpinner(new SpinnerNumberModel(frmAvg, 1, maxFramesToAverage, 1));
//      spinAvg.setBorder(BorderFactory.createEtchedBorder());
//      spinAvg.setFont(new java.awt.Font("Dialog", 0, 10));
//      spinAvg.setBounds(new Rectangle(62, 9, 50, 27));
//      editAvg = new JSpinner.NumberEditor(spinAvg, "0");
//      spinAvg.setEditor(editAvg);
//      spinAvg.addChangeListener(new ChangeListener() {
//         public void stateChanged(ChangeEvent se) {
//            System.out.println(((JSpinner) se.getSource()).getValue());
//            int a = ((SpinnerNumberModel) ((JSpinner) se.getSource()).getModel()).
//                  getNumber().intValue();
//            //Prefs.usr.putInt("framesToAvg", a);
//            camAcq.setFramesToAverage(a);
//            synchValues();
//         }
//      });
//      
//      checkParms.setMinimumSize(new Dimension(59, 22));
//      checkParms.setFont(new java.awt.Font("Dialog", 0, 10));
//      checkParms.setOpaque(false);
//      checkParms.setToolTipText("Show acq. parameters in resulting image(s)");
//      checkParms.setText("Show Parameters");
//      checkParms.setBounds(new Rectangle(146, 6, 121, 22));
//      checkParms.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            checkParms_actionPerformed(e);
//         }
//      });
//      checkParms.setSelected(Prefs.usr.getBoolean("parmsInFilename", false));
//      
//      radioResultByte.setFont(new java.awt.Font("Dialog", 0, 10));
//      radioResultByte.setOpaque(false);
//      radioResultByte.setText("byte");
//      radioResultByte.setBounds(new Rectangle(195, 41, 46, 16));
//      radioResultByte.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            radioResultByte_actionPerformed(e);
//         }
//      });
//      
//      radioResultShort.setBounds(new Rectangle(240, 41, 48, 16));
//      radioResultShort.setText("short");
//      radioResultShort.setOpaque(false);
//      radioResultShort.setFont(new java.awt.Font("Dialog", 0, 10));
//      if (Prefs.usr.getInt("resultAvg", 8) == (int) 8) {
//         radioResultByte.setSelected(true);
//      } else {
//         radioResultShort.setSelected(true);
//      }
//      
//      radioResultShort.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            radioResultShort_actionPerformed(e);
//         }
//      });
//      
//      labelResult.setFont(new java.awt.Font("Dialog", 0, 10));
//      labelResult.setToolTipText("Resulting Image Depth");
//      labelResult.setHorizontalAlignment(SwingConstants.RIGHT);
//      labelResult.setText("Result type:");
//      labelResult.setBounds(new Rectangle(130, 41, 61, 16));
////
//      
//      labelFrames.setFont(new java.awt.Font("Dialog", 0, 10));
//      labelFrames.setToolTipText("Frames to Average");
//      labelFrames.setHorizontalAlignment(SwingConstants.RIGHT);
//      labelFrames.setHorizontalTextPosition(SwingConstants.RIGHT);
//      labelFrames.setText("Frames to");
//      labelFrames.setBounds(new Rectangle(1, 6, 53, 18));
//      
//      labelIntegrate.setBounds(new Rectangle(7, 19, 48, 18));
//      labelIntegrate.setText("Integrate:");
//      labelIntegrate.setHorizontalTextPosition(SwingConstants.RIGHT);
//      labelIntegrate.setHorizontalAlignment(SwingConstants.RIGHT);
//      labelIntegrate.setToolTipText("Frames to Average");
//      labelIntegrate.setFont(new java.awt.Font("Dialog", 0, 10));
//      
//      checkDivide.setFont(new java.awt.Font("Dialog", 0, 10));
//      checkDivide.setOpaque(false);
//      checkDivide.setToolTipText("Divide for Average");
//      checkDivide.setText("Divide (average)");
//      checkDivide.setBounds(new Rectangle(8, 43, 107, 14));
//      checkDivide.setSelected(false);
//      checkDivide.addActionListener(new java.awt.event.ActionListener() {
//         public void actionPerformed(ActionEvent e) {
//            checkDivide_actionPerformed(e);
//         }
//      });
//      panelFrameAvg.add(labelFrames, null);
//      panelFrameAvg.add(labelIntegrate, null);
//      panelFrameAvg.add(spinAvg, null);
//      panelFrameAvg.add(checkDivide, null);
//      panelFrameAvg.add(checkParms, null);
//      panelFrameAvg.add(labelResult, null);
//      panelFrameAvg.add(radioResultByte, null);
//      panelFrameAvg.add(radioResultShort, null);
//   }
//   
//   
//   void setupDependencyHandlers() {
//      new ComponentDependencyHandler(radioButton_16bit) {
//         public void dependencyNotification() {
//            
//         }
//      };
//   }
//   
//   
//   //------------------------------------------------------------------------
//   void enableFunctions(final boolean t) {
//      Utils.dispatchToEDT(new Runnable() {
//         public void run() {
//            buttonSeries.setEnabled(t);
//            buttonROI.setEnabled(t);
//            buttonSnap.setEnabled(t);
//            comboBox_Binning.setEnabled(t);
//            checkbox_Cooler.setEnabled(t);
//            comboBox_Readout.setEnabled(t);
//            toggleRecordVideo.setEnabled(t);
//            buttonEqualize.setEnabled(t);
//            buttonLinearize.setEnabled(t);
//            spin_Gain.setEnabled(t);
//            spin_Offset.setEnabled(t);
//            spinExpos.setEnabled(t);
//            radioButton_16bit.setEnabled(t);
//            radioButton_8bit.setEnabled(t);
//         }
//      });
//      
//   }
//   
//   
//   //--------------------------------------------------------------------------
//   public void setProgress(String status) {
//      labelProgress.setText(status);
//   }
//   
//   
//   //------------------------------------------------------------------------
//   public void synchValues() {
//      
//      //System.out.println("isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
//      
//      if (true /*cam.isOpen*/) {
//         
////         Utils.dispatchToEDT(new Runnable() {
////            public void run() {
//               spinExpos.setEnabled(true);
//               spin_Gain.setEnabled(true);
//               spin_Offset.setEnabled(true);
//               comboBox_Binning.setEnabled(true);
//               // cam.updateValues();
//               if (camAcq.getExposure() > 0) {
//                  spinExpos.forceTo(camAcq.getExposure() / 1000);
//               }
//               if (camAcq.getGain() >= 0) {
//                  camAcq.model_Gain.setValue(new Double(camAcq.getGain()));
//               }
//               if (camAcq.getOffset() >= 0) {
//                  camAcq.model_Offset.setValue(new Integer((int) camAcq.getOffset()));
//               } else {
//                  camAcq.model_Offset.setValue(0);
//               }
//               comboBox_Binning.removeActionListener(comboBinListener);
//               int binIndex = 0;
//               switch (Prefs.usr.getInt("camera.binning", 2)) {
//                  case 1: // 1 x 1
//                     binIndex = 0;
//                     break;
//                  case 2: // 2 x 2
//                     binIndex = 1;
//                     break;
//                  case 4: // 4 x 4
//                     binIndex = 2;
//               }
//               comboBox_Binning.setSelectedIndex(binIndex);
//               comboBox_Binning.addActionListener(comboBinListener);
//               
//               checkbox_Cooler.setSelected(Prefs.usr.getBoolean("coolerActive", true));
//               
//               int numImgs = Prefs.usr.getInt("imagesInStack", 2);
//               spinImages.getModel().setValue(new Integer(numImgs));
//               
//               double intvl = Prefs.usr.getDouble("interval", 1.0);
//               spinInterval.getModel().setValue(new Double(intvl));
//               
//               chkboxAutoSave.setSelected(Prefs.usr.getBoolean("autoSave", true));
//               chkboxPutOnDesk.setSelected(Prefs.usr.getBoolean("putOnDesk", true));
//               
//               fieldPrefix.setText(Prefs.usr.get("prefix", "Image"));
//               labelSerialNum.setText(String.valueOf(Prefs.usr.getInt("fileCounter", 1)));
//               radioTimeStamp.setSelected(Prefs.usr.getBoolean("nameFileTimestamp", true));
//               radioSerial.setSelected(!radioTimeStamp.isSelected());
//               labelDirSave.setText(Prefs.usr.get("directory.imageData", "\\"));
//               chkboxSaveRawData.setSelected(Prefs.usr.getBoolean("saveRawData", false));
//               
//               if (camAcq.getDepth()==16) {
//                  radioButton_16bit.setSelected(true);
//                  radioResultShort.setSelected(true);
//                  radioResultByte.setSelected(false);
//                  radioResultByte.setEnabled(false);
//                  if (camAcq.getFramesToAverage() > 1) {
//                     checkDivide.setEnabled(true);
//                  } else {
//                     checkDivide.setEnabled(false);
//                  }
//                  Prefs.usr.putInt("resultAvg", 16);
//               } else { // is 8 bit acq
//                  radioButton_8bit.setSelected(true);
//                  radioResultByte.setSelected(true);
//                  radioResultShort.setEnabled(false);
//                  if (radioResultByte.isSelected()) {
//                     setDivide(true);
//                     checkDivide.setSelected(true);
//                     checkDivide.setEnabled(false);
//                  } else { // result is short
//                     if (camAcq.getFramesToAverage() > 1) {
//                        checkDivide.setEnabled(true);
//                     } else {
//                        checkDivide.setEnabled(false);
//                     }
//                  }
//                  
//               }
////            }
////         });
//      } else {
//         spinExpos.setEnabled(false);
//         spin_Gain.setEnabled(false);
//         spin_Offset.setEnabled(false);
//         comboBox_Binning.setEnabled(false);
//      }
//   }
//   
//   
//   //-------------------------------------------------------------------------
//   boolean isCameraOn = false;
//   JButton buttonSetZero = new JButton();
//   
//   void buttonOpenCam_actionPerformed(ActionEvent e) {
//      Utils.dispatchToEDT(new Runnable() {
//         public void run() {
//            camAcq.displayOpen();
//         }
//      });
//      // @todo display does not close or null out.
//      
////      if (camAcq.displayIsOn()) {
////         camAcq.displayClose();
////         enableFunctions(false);
////      } else {
////         camAcq.displayOpen();
////         synchValues();
////         enableFunctions(true);
////      }
//   }
//   
//   
//   // --- Cooler control
//   void checkbox_Cooler_actionPerformed(ActionEvent e) {
//      camAcq.setCoolerActive(checkbox_Cooler.isSelected());
//      Prefs.usr.putBoolean("camera.coolerActive", checkbox_Cooler.isSelected());
//   }
//   
//   
//   public void buttonSetZero_actionPerformed(ActionEvent e) {
//      /** @todo Button set zero intensity */
//   }
//   
//   
//   // Binning
//   void comboBox_Binning_actionPerformed(ActionEvent e) {
//      final int selected = comboBox_Binning.getSelectedIndex();
//      final SwingWorker worker = new SwingWorker() {
//         public Object construct() {
//            //if (cam.isOpen) {
//            camAcq.displayClose();
//            switch (selected) {
//               case 0: // 1 x 1
//                  camAcq.setBinning(1);
//                  Prefs.usr.putFloat("displayScale", 0.5f);
//                  break;
//               case 1: // 2 x 2
//                  camAcq.setBinning(2);
//                  Prefs.usr.putFloat("displayScale", 1.0f);
//                  break;
//               case 2: // 4 x 4
//                  camAcq.setBinning(4);
//                  Prefs.usr.putFloat("displayScale", 1.0f);
//                  break;
//               case 3: // 8 x 8
//                  camAcq.setBinning(8);
//                  break;
//            }
//            //}
//            return null;
//         }
//         
//         
//         public void finished() {
//            //camAcq.waitFor(500);
//            // update the exposure spinner, because expos. is changed with binning
//            //cam.updateValues();
//            synchValues();
//            camAcq.displayOpen();
//         }
//      }; worker.start();
//   }
//   
//   
//   // Depth ===============================================================
//   void radioButton_8bit_actionPerformed(ActionEvent e) {
//      camAcq.displaySuspend();
//      camAcq.setDepth(8);
//      synchValues();
//      camAcq.displayResume();
//   }
//   
//   
//   void radioButton_16bit_actionPerformed(ActionEvent e) {
//      camAcq.displaySuspend();
//      camAcq.setDepth(16);
//      synchValues();
//      camAcq.displayResume();
//   }
//   
//   
////   void radioButton_8bit_actionPerformed (ActionEvent e) {
////      boolean is8bit = radioButton_8bit.isSelected();
////      System.out.println("8bit statechange:"
////                         + ((JRadioButton) e.getSource()).getText());
////      try {
////         cam.setFormat(2);
////      }
////      catch (Exception ex) {
////         System.out.println("Error in : " + ex);
////      }
////      cam.openDisplayFrame();
////      cam.setDisplayOn();
////      synchValues();
////   }
////
////
////   void radioButton_16bit_actionPerformed (ActionEvent e) {
////      // NOTE: Readout is switched to 10M if depth = 12 (in C-code)
////      boolean is16bit = radioButton_16bit.isSelected();
////      System.out.println("16bit statechange:"
////                         + ((JRadioButton) e.getSource()).getText());
////      try {
////         cam.setFormat(3);
////      }
////      catch (Exception ex) {
////         System.out.println("Error in : " + ex);
////      }
////      cam.openDisplayFrame();
////      cam.setDisplayOn();
////      synchValues();
////   }
//   
//   
//   // Resulting Averaged Image depth ---------------------------------------
//   void radioResultByte_actionPerformed(ActionEvent e) {
//      Prefs.usr.putInt("resultAvg", 8);
//      synchValues();
//   }
//   
//   
//   void radioResultShort_actionPerformed(ActionEvent e) {
//      Prefs.usr.putInt("resultAvg", 16);
//      synchValues();
//   }
//   
//   
//   // Set the Readout Speed ================================================
//   void comboBox_Readout_actionPerformed(ActionEvent e) {
//      int selected = comboBox_Readout.getSelectedIndex();
//      System.out.println("Readout ComboboxSelected: " + comboBox_Readout.getSelectedIndex()
//      + "   " + ((JComboBox) e.getSource()).getSelectedItem());
//      //if (cam.isOpen) {
//      switch (selected) {
//         // ReadoutSpeed: 20M = 0, 10M = 1, 5M = 2, 2M5 = 3, _last = 4
//         case 0: // 20M
//            camAcq.setSpeed(0);
//            break;
//         case 1: // 10M
//            camAcq.setSpeed(1);
//            break;
//         case 2: // 5M
//            camAcq.setSpeed(2);
//            break;
//         case 3: // 2M5
//            camAcq.setSpeed(3);
//            break;
//            //}
//            //camAcq.displayOpen();
//      }
//   }
//   
//   
//   void chkboxAutoSave_actionPerformed(ActionEvent e) {
//      Prefs.usr.putBoolean("autoSave", chkboxAutoSave.isSelected());
//   }
//   
//   
//   void chkboxPutInStack_actionPerformed(ActionEvent e) {
//      Prefs.usr.putBoolean("putInStack", chkboxPutInStack.isSelected());
//   }
//   
//   
//   void checkParms_actionPerformed(ActionEvent e) {
//      Prefs.usr.putBoolean("parmsInFilename", checkParms.isSelected());
//   }
//   
//   
//   void chkboxPutOnDesk_actionPerformed(ActionEvent e) {
//      Prefs.usr.putBoolean("putOnDesk", chkboxPutOnDesk.isSelected());
//   }
//   
//   
//   void chkboxSaveRawData_actionPerformed(ActionEvent e) {
//      Prefs.usr.putBoolean("saveRawData", chkboxSaveRawData.isSelected());
//   }
//   
//   
//   void fieldPrefix_focusLost(FocusEvent e) {
//      Prefs.usr.put("prefix", fieldPrefix.getText());
//   }
//   
//   
//   ///////////////////////////////////////////////////////////////////////////
//   // Display list of camera info and parameters.
//   //
//   public void buttonListCameraParms_actionPerformed(ActionEvent e) {
//      //if (cam.isOpen) {
////         TextWindow tf = new TextWindow("Camera Parameters Listing");
////         QCamJNI.getInfo();
////         QCamJNI.getParameters();
////         tf.set(QCamJNI.showCameraInfo() + "\n");
////         tf.append(QCamJNI.showCameraState());
////         tf.setBounds(80, 80, 450, 600);
////         tf.setVisible(true);
//      //} else {}
//   }
//   
//   
//   // AutoExposure -----------------------------------------------------------
//   void toggleAutoExposure_actionPerformed(ActionEvent e) {
//      if (toggleAutoExposure.isSelected()) {
//         spinExpos.setEnabled(false);
//         toggleAutoExposure.setBackground(Color.yellow);
//         toggleAutoExposure.repaint();
//      } else {
//         spinExpos.setEnabled(true);
//         toggleAutoExposure.setBackground(Color.black);
//      }
//   }
//   
//   
//   //----------------------------------------------------------
//   void buttonROI_actionPerformed(ActionEvent e) {
//      if (camAcq.isROISelected()) {
//         try {
//            camAcq.setCameraROI(camAcq.getSelectedROI());
//         } catch (Exception ex) {
//            System.out.println("Error in : " + ex);
//         }
//      }
//   }
//   
//   
//   //----------------------------------------------------------------------
//   void buttonDirSet_actionPerformed(ActionEvent e) {
//      String dataDir = selectDirectory(this, Prefs.usr.get("directory.imageData", "\\"),
//            "Select Image Data directory", "Image Data dir. set to:");
//      if (dataDir != null) {
//         CamAcq.getInstance().setImageDirectory(dataDir);
//         this.labelDirSave.setText(dataDir);
//         
//      }
//   }
//   
//   
//   public void setLabelSerialNum() {
//      labelSerialNum.setText(String.valueOf(Prefs.usr.getInt("fileCounter", 1)));
//   }
//   
//   
//   void buttonZeroCounter_actionPerformed(ActionEvent e) {
//      CamAcq.getInstance().setFileCounter(0);
//   }
//   
//   
//   void checkDivide_actionPerformed(ActionEvent e) {
//      //setDivide(checkDivide.isSelected());
//      Prefs.usr.putBoolean("divide", checkDivide.isSelected());
//   }
//   
//   
//   void setDivide(boolean t) {
//      checkDivide.setSelected(t);
//      Prefs.usr.putBoolean("divide", t);
//   }
//   
//   
//   //========================================================================
//   void buttonSnap_actionPerformed(ActionEvent e) {
//      Utils.dispatchToEDT(new Runnable() {
//         public void run() {
//            Utils.status("Acquiring one image...");
//            buttonSnap.setEnabled(false);
//            CamAcq.getInstance().captureToImagePlus();
//            returnFromAcq();
//         }
//      });
//      
////      final SwingWorker worker = new SwingWorker() {
////         public Object construct() {
////            buttonSnap.setEnabled(false);
////            CamAcq.getInstance().captureToImagePlus();
////            return null;
////         }
////
////
////         public void finished() {
////            returnFromAcq();
//////            ij.ImagePlus ip = ij.IJ.getImage();
//////            if (cam.display != null) {
//////               if (cam.display.vPanel != null) {
//////                  cam.display.vPanel.clearMessage();
//////                  cam.setDisplayOn();
//////               }
//////            }
//////            CamAcq.getInstance().status("");
//////            buttonSnap.setEnabled(true);
//////            ij.WindowManager.setCurrentWindow(ip.getWindow());
//////            ip.getWindow().toFront();
////         }
////      }; worker.start();
//   }
//   
//   
//   // ----------------------------------------------------------------------
//   // Acquisition, Before and after...
//   //
//   void prepareForAcq() {
//      wasAutoExp = toggleAutoExposure.isSelected();
//      toggleAutoExposure.setSelected(false);
//      enableFunctions(false);
//   }
//   
//   
//   void returnFromAcq() {
//      
//      ij.ImagePlus ip = ij.IJ.getImage();
//      Utils.status("");
//      camAcq.displayResume();
//      buttonStop.setVisible(false);
//      enableFunctions(true);
//      toggleAutoExposure.setSelected(wasAutoExp);
//      if (ij.IJ.getInstance() != null) {
//         if (ip != null) {
//            ij.WindowManager.setCurrentWindow(ip.getWindow());
//            ip.getWindow().toFront();
//         }
//      }
//   }
//   
//   
//   //========================================================================
//   // Series Acq.
//   void buttonSeries_actionPerformed(ActionEvent e) {
//      Utils.dispatchToEDT(new Runnable() {
//         public void run() {
//            prepareForAcq();
//            Utils.status("Acquiring series...");
//            buttonStop.setVisible(true);
//            CamAcq.getInstance().captureSeriesToStack();
//            System.out.println("Capture done.");
//            returnFromAcq();
//         }
//      });
//   }
//   
//   public void checkFlushFirst_actionPerformed(ActionEvent actionEvent) {
//      
//   }
//   
//   
//   /*  void buttonSeries_actionPerformed(ActionEvent e) {
//       final boolean wasAutoExp = toggleAutoExposure.isSelected();
//       toggleAutoExposure.setSelected(false);
//       enableFunctions(false);
//       final SwingWorker worker = new SwingWorker() {
//         public Object construct() {
//           buttonStop.setVisible(true);
//           CamAcq.getInstance().captureSeriesToStack();
//           return null;
//         }
//         public void finished() {
//           System.out.println("Capture done.");
//           CameraInterface.display.vPanel.clearMessage();
//           CameraInterface.setDisplayOn();
//           buttonStop.setVisible(false);
//           enableFunctions(true);
//           toggleAutoExposure.setSelected(wasAutoExp);
//         }
//       };
//       worker.start();
//     }
//    */
//   
//   //========================================================================
//   void buttonBurst_actionPerformed(ActionEvent e) {
//      //    final boolean wasAutoExp = toggleAutoExposure.isSelected();
//      //    toggleAutoExposure.setSelected(false);
//      //    enableFunctions(false);
//      //    final SwingWorker worker = new SwingWorker() {
//      //      public Object construct() {
//      //        buttonStop.setVisible(true);
//      //        CamAcq.getInstance().captureSeriesFast();
//      //        return null;
//      //      }
//      //
//      //      public void finished() {
//      //        System.out.println("Capture done.");
//      //        CameraInterface.display.vPanel.clearMessage();
//      //        CameraInterface.setDisplayOn();
//      //        buttonStop.setVisible(false);
//      //        enableFunctions(true);
//      //        toggleAutoExposure.setSelected(wasAutoExp);
//      //      }
//      //    };
//      //    worker.start();
//   }
//   
//   
//   //========================================================================
//   // Video Record and playback
//   //
//   
//   VideoFileTiff vf = null;
//   JButton ButtonPrefs = new JButton();
//   JButton buttonListCameraParms = new JButton();
//   JButton buttonConsole = new JButton();
//   JButton buttonCamAcqParms = new JButton();
//   JButton buttonMemMon = new JButton();
//   JButton buttonTest1 = new JButton();
//   
//   void toggleRecordVideo_actionPerformed(ActionEvent e) {
//      if (toggleRecordVideo.isSelected()) {
//         startRecording();
//         Utils.status("Recording video...");
//      } else {
//         Utils.status("Video recording stopped.");
//         stopRecording();
//      }
//   }
//   
//   
//   public void startRecording() {
////      if (cam.display != null) {
////         String tempFile = CamAcq.getInstance().getImageDirectory() + "\\" + "VID_"
////               + CamAcq.getInstance().timeStamp() + ".tif";
////         vf = new VideoFileTiff(tempFile, (int) cam.width, (int) cam.height, 8);
////         if (vf != null) {
////            cam.display.vPanel.setVideoFile(vf);
////         }
////      }
//   }
//   
//   
//   public void stopRecording() {
////      cam.display.vPanel.setVideoFile(null);
////      vf.close();
////      vf = null;
//   }
//   
//   
//   void buttonPlayVideo1_actionPerformed(ActionEvent e) {
////      // first, stop camera display
////      cam.setTriggerSoft();
////      /** @todo add FileChooser */
////      String videoFilename = "testVid.dat";
////      File fn = new File(videoFilename);
////      //new VideoPlayer(fn);
//////      playFile.getWidth();
//////      playFile.getHeight();
//////      playFile.getBitDepth();
//   }
//   
//   
//   //======================================================================
//   // Display Controls
//   //
//   void buttonLinearize_actionPerformed(ActionEvent e) {
//      camAcq.displaySetMode(0);
//      buttonEqualize.setEnabled(true);
//      buttonLinearize.setEnabled(false);
//   }
//   
//   
//   void buttonEqualize_actionPerformed(ActionEvent e) {
//      camAcq.displaySetMode(0);
//      buttonEqualize.setEnabled(false);
//      buttonLinearize.setEnabled(true);
//   }
//   
//   
//   //------------------------------------------------------------------------
//   // for testing ---------------
//   void openDummyCamera() {
////      final SwingWorker worker = new SwingWorker() {
////         public Object construct() {
////            cam.initializeDummyCamera();
////            QCamJNI.pixels8 = new byte[(int) (cam.width * cam.height)];
////            int w = (int) cam.width;
////            int h = (int) cam.height;
////            int o = 10;
////            int c = 0;
////            int z = w / o;
////            for (int i = 0; i < h; i++) {
////               c = 0;
////               for (int j = 0; j < w; j++) {
////                  QCamJNI.pixels8[(i * w) + j] = (byte) c;
////                  if ((j % z) == 0) {
////                     c = c + z;
////                  }
////               }
////            }
////            if (cam.isOpen) {
////               FrameJCameraDisplay display = new FrameJCameraDisplay(1.0f);
////               cam.display = display;
////               display.setVisible(true);
////            }
////            return null;
////         }
////
////
////         public void finished() {}
////      }; worker.start();
//   }
//   
//   
//   // TEST ========================================================================
//   //
//   void buttonSetStream_actionPerformed(ActionEvent e) {
//      //cam.setStreaming();
//   }
//   
//   
//   void buttonThread_actionPerformed(ActionEvent e) {
//      Utils.showThreads();
//   }
//   
//   
//   void buttonSetOne_actionPerformed(ActionEvent e) {
//      //Prefs.setOne();
//   }
//   
//   
//   //   void spinInterval_stateChanged (ChangeEvent e) {}
////
////
////   void spinImages_stateChanged (ChangeEvent e) {}
//   
//   
//   public static String selectDirectory(Component parent, String selectedFile,
//         String message, String confirmMsg) {
//      JDirectoryChooser chooser = null;
////      try {
////         JarJarClassLoader jjcl = new JarJarClassLoader();
////         jjcl.addInternalJar("l2fprod-common-all.jar");
////         Class theClass = Class.forName("com.l2fprod.common.swing.JDirectoryChooser", true,
////               jjcl);
////         chooser = (JDirectoryChooser) theClass.newInstance();
////      }
////      catch (Exception ex) {
////      }
////      if (chooser != null) {
//      chooser = new JDirectoryChooser();
//      if (selectedFile != null) {
//         chooser.setSelectedFile(new File(selectedFile));
//      }
//      JTextArea accessory = new JTextArea(message);
//      accessory.setLineWrap(true);
//      accessory.setWrapStyleWord(true);
//      accessory.setEditable(false);
//      accessory.setOpaque(false);
//      accessory.setFont(UIManager.getFont("Tree.font"));
//      chooser.setAccessory(accessory);
//      chooser.setMultiSelectionEnabled(false); // <<<<<<<<
//      int choice = chooser.showOpenDialog(parent);
//      if (choice == JDirectoryChooser.APPROVE_OPTION) {
//         String filenames = "";
//         File[] selectedFiles = chooser.getSelectedFiles();
//         for (int i = 0, c = selectedFiles.length; i < c; i++) {
//            filenames += selectedFiles[i];
//         }
//         JOptionPane.showMessageDialog(parent, confirmMsg + filenames);
//         return filenames;
//      } else {
//         JOptionPane.showMessageDialog(parent, "Cancelled");
//         return null;
//      }
////      }
////      return null;
//   }
//   
//   
//   public void buttonConsole_actionPerformed(ActionEvent e) {
//      ConsoleJava jConsole = new ConsoleJava();
//   }
//   
//   
//   public void ButtonPrefs_actionPerformed(ActionEvent e) {
//      Prefs.frameDisplayPrefs();
//   }
//   
//   
//   public void buttonCamAcqParms_actionPerformed(ActionEvent actionEvent) {
//      CamAcq.getInstance().listAllParameters();
//   }
//   
//   
//   public void buttonMemMon_actionPerformed(ActionEvent actionEvent) {
//      new edu.mbl.jif.utils.diag.MemoryMonitor().openInJFrame();
//   }
//   
//   
//   public void buttonTest1_actionPerformed(ActionEvent actionEvent) {
//      //new edu.mbl.jif.ijplugins.CaptureStack_().main(null);
//      IJMaker.openImageJ();
//   }
   
}
