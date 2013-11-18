package edu.mbl.jif.stage;

import edu.mbl.jif.utils.JifUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mbl.jif.gui.swingthread.SwingWorker3;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.comm.SerialPortConnection;
import javax.swing.BoxLayout;


public class PanelStage
      extends JPanel
{
//   private Border border1;
//   private Insets inset0 = new Insets(0, 0, 0, 0);
//   private JButton button_Down = new JButton();
//   private JButton button_Down10 = new JButton();
//   private JButton button_MoveToBottom = new JButton();
//   private JButton button_MoveToTop = new JButton();
//   private JButton button_MoveToZero = new JButton();
//   private JButton button_PrePosTest = new JButton();
//   private JButton button_Preview = new JButton();
//   private JButton button_SetBottom = new JButton();
//   private JButton button_SetTop = new JButton();
//   private JButton button_SetZero = new JButton();
//   private JButton button_Up = new JButton();
//   private JButton button_Up10 = new JButton();
//   private JLabel field_Bottom = new JLabel();
//   private JLabel field_Top = new JLabel();
//   private JLabel label_Bottom = new JLabel();
//   private JLabel label_Ctrl1 = new JLabel();
//   private JLabel label_CurPos = new JLabel();
//   private JLabel label_Increment = new JLabel();
//   private JLabel label_Sections = new JLabel();
//   private JLabel label_Top = new JLabel();
//   private JLabel label_Zset = new JLabel();
//   private JPanel panelMovement = new JPanel();
//   private JPanel panel_CurrPos = new JPanel();
//   private JPanel panel_MoveButtons = new JPanel();
//   private JPanel panel_Sections = new JPanel();
//   private JPanel panel_Zset = new JPanel();
//   private JSpinner spinner_MoveIncr;
//   private JSpinner spinner_Sections;
//   private JSpinner spinner_Zincr;
//   private JToggleButton toggle_AnchorTop = new JToggleButton();
//   private SpinnerNumberModel model_MoveIncr;
//   private SpinnerNumberModel model_Sections;
//   private SpinnerNumberModel model_Zincr;
//   JSpinner.NumberEditor editor_MoveIncr;
//   JSpinner.NumberEditor editor_Sections;
//   JSpinner.NumberEditor editor_Zincr;
//   private boolean anchorBottom = true;
//   private boolean anchorTop = false;
//   private int moveIncrement = 100; // nanometers
//   private int zBottom = 0;
//   private int zIncrement = 0;
//   private int zSections = 1;
//   private int zTop = 0;
//
//   static NumberFormat formatter = new DecimalFormat("###0.0");
//   JButton buttonConnect = new JButton();
//   JButton buttonConfig = new JButton();
//
//
//
//   StageZController stage; //Construct the frame
//
//
//   public PanelStage () {
//     // this(new StageCtrlNikon(new SerialPortConnection()));
//
//   }
//
//   public PanelStage (StageZController stage) {
//      this.stage = stage;
//      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
//      try {
//         jbInit();
//      }
//      catch (Exception e) {
//         e.printStackTrace();
//      }
//
//   }
//   //Component initialization
//   private void jbInit () throws Exception {
//      setPreferredSize(new Dimension(301, 255));
//      this.setLayout(null);
//      border1 =
//            BorderFactory.createCompoundBorder(BorderFactory.
//            createEtchedBorder(Color.white, new Color(142, 142, 142)),
//            BorderFactory.createEmptyBorder(4, 4, 4, 4));
//      button_MoveToZero.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_MoveToZero_actionPerformed(e);
//         }
//      });
//      this.setToolTipText("");
//      button_SetTop.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            //button_SetTop_actionPerformed(e);
//         }
//      });
//      button_SetBottom.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_SetBottom_actionPerformed(e);
//         }
//      });
//      toggle_AnchorTop.setToolTipText("Anchor Top");
//      toggle_AnchorTop.setIcon(JifUtils.loadImageIcon("anchor18.gif", PanelStage.class));
//
//      toggle_AnchorTop.setBounds(new Rectangle(146, 11, 26, 27));
//
//      //
//      button_MoveToTop.setFont(new java.awt.Font("Dialog", 0, 11));
//      button_MoveToTop.setBounds(new Rectangle(105, 29, 32, 32));
//      button_MoveToTop.setMargin(inset0);
//      button_MoveToTop.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_MoveToTop_actionPerformed(e);
//         }
//      });
//      button_MoveToTop.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//            "goTop_32.gif", PanelStage.class));
//      button_MoveToTop.setToolTipText("Go to Top Position");
//
//      //
//      button_MoveToBottom.setFont(new java.awt.Font("Dialog", 0, 11));
//      button_MoveToBottom.setBounds(new Rectangle(106, 153, 36, 35));
//      button_MoveToBottom.setMargin(inset0);
//      button_MoveToBottom.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_MoveToBottom_actionPerformed(e);
//         }
//      });
//      button_MoveToBottom.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//            "goBottom_32.gif", PanelStage.class));
//      button_MoveToBottom.setToolTipText("Go to Bottom Position");
//
//      //
//      button_PrePosTest.setBounds(new Rectangle(128, 78, 44, 20));
//      button_PrePosTest.setFont(new java.awt.Font("Dialog", 0, 10));
//      button_PrePosTest.setMargin(inset0);
//      button_PrePosTest.setText("prePos");
//      button_PrePosTest.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_PrePosTest_actionPerformed(e);
//         }
//      });
//
//      //
//      button_Preview.setBackground(new Color(231, 204, 204));
//      button_Preview.setBounds(new Rectangle(12, 236, 59, 24));
//      button_Preview.setFont(new java.awt.Font("Dialog", 0, 11));
//      button_Preview.setToolTipText("Preview the Z-Series");
//      button_Preview.setMargin(new Insets(2, 4, 2, 4));
//      button_Preview.setText("Preview");
//      button_Preview.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_Preview_actionPerformed(e);
//         }
//      });
//
//      label_Top.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Top.setText("End");
//      label_Top.setBounds(new Rectangle(12, 33, 42, 19));
//      panelMovement.setBorder(BorderFactory.createLoweredBevelBorder());
//      panelMovement.setOpaque(false);
//      //panelMovement.setPreferredSize(new Dimension(1, 1));
//      panelMovement.setBounds(new Rectangle(10, 10, 174, 298));
//      panelMovement.setLayout(null);
//      panel_MoveButtons.setBorder(BorderFactory.createRaisedBevelBorder());
//      //
//
//      button_MoveToZero.setToolTipText("Move to Zero Position");
//      button_MoveToZero.
//            setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//                  "goZero_32.gif", PanelStage.class));
//      button_MoveToZero.setMargin(new Insets(4, 4, 4, 4));
//
//      //
//      button_SetTop.setToolTipText("Set the Top of the Z-scan range");
//      button_SetBottom.setToolTipText(
//            "Set the Starting (Bottom) of the Z-scan range");
//      button_Up10.setToolTipText("Move up increment x 10");
//      button_Up10.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//                  "up10_32.gif", PanelStage.class));
//      button_Up10.setMargin(new Insets(4, 4, 4, 4));
//      button_Down10.setToolTipText("Move Down increment x 10");
//      //
//
//      panel_MoveButtons.setBounds(new Rectangle(9, 28, 52, 245));
//      panel_MoveButtons.setLayout(new BoxLayout(panel_MoveButtons, BoxLayout.Y_AXIS));
//      panel_Zset.setBackground(new Color(204, 204, 157));
//      // Current Position
//      label_CurPos.setMaximumSize(new Dimension(40, 17));
//      label_CurPos.setMinimumSize(new Dimension(40, 17));
//      label_CurPos.setPreferredSize(new Dimension(40, 17));
//      panel_CurrPos.setBorder(BorderFactory.createLoweredBevelBorder());
//      panel_CurrPos.setOpaque(false);
//      panel_CurrPos.setBounds(new Rectangle(69, 35, 55, 29));
//      //
//      //
//      panel_Sections.setBorder(BorderFactory.createEtchedBorder());
//      panel_Sections.setOpaque(true);
//      panel_Sections.setBackground(new Color(223, 223, 200));
//      panel_Sections.setBounds(new Rectangle(7, 60, 125, 84));
//      panel_Sections.setLayout(null);
//      //
//      button_Up10.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//                  "up10_32.gif", PanelStage.class));
//      button_Up10.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_Up10_actionPerformed(e);
//         }
//      });
//      button_Up10.setMargin(new Insets(4, 4, 4, 4));
//
//
//      //
//
//      button_Down10.setMargin(new Insets(4, 4, 4, 4));
//      button_Down10.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//                  "dn10_32.gif", PanelStage.class));
//      button_Down10.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_Down10_actionPerformed(e);
//         }
//      });
//
//      //
//      button_Down.setMargin(new Insets(4, 4, 4, 4));
//      button_Down.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_Down_actionPerformed(e);
//         }
//      });
//      button_Down.setToolTipText("Move Down 1 increment");
//      button_Down.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//            "dn1_32.gif", PanelStage.class));
//
//      //
//      button_Up.setToolTipText("Move Up 1 increment");
//      button_Up.setIcon(edu.mbl.jif.utils.JifUtils.loadImageIcon(
//                  "up1_32.gif", PanelStage.class));
//      button_Up.setMargin(new Insets(4, 4, 4, 4));
//      button_Up.addActionListener(new java.awt.event.ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            button_Up_actionPerformed(e);
//         }
//      });
//
//      //
//      button_SetTop.setBackground(Color.lightGray);
//      button_SetTop.setBounds(new Rectangle(82, 11, 57, 27));
//      button_SetTop.setFont(new java.awt.Font("Dialog", 0, 11));
//      button_SetTop.setMargin(new Insets(2, 4, 2, 4));
//      button_SetTop.setText("Top");
//
//      //
//      label_CurPos.setFont(new java.awt.Font("Dialog", 0, 12));
//      label_CurPos.setToolTipText("");
//      label_CurPos.setHorizontalAlignment(SwingConstants.CENTER);
//      //label_Position.setText("000");
//      label_CurPos.setText("---");
//
//      //
//      button_SetBottom.setText("Set Start (bottom)");
//      button_SetBottom.setMargin(new Insets(2, 4, 2, 4));
//      button_SetBottom.setFont(new java.awt.Font("Dialog", 0, 11));
//      button_SetBottom.setBounds(new Rectangle(7, 201, 104, 24));
//      button_SetBottom.setBackground(new Color(192, 192, 239));
//
//      //
//      field_Top.setText("000");
//      field_Top.setHorizontalAlignment(SwingConstants.RIGHT);
//      field_Top.setBounds(new Rectangle(59, 32, 34, 21));
//      field_Bottom.setBounds(new Rectangle(61, 153, 34, 21));
//      field_Bottom.setHorizontalAlignment(SwingConstants.RIGHT);
//      field_Bottom.setText("000");
//      panel_Zset.setBorder(BorderFactory.createLoweredBevelBorder());
//      panel_Zset.setBounds(new Rectangle(201, 11, 151, 295));
//      panel_Zset.setLayout(null);
//      label_Increment.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
//      label_Increment.setHorizontalAlignment(SwingConstants.RIGHT);
//      label_Increment.setText("Increment:");
//      label_Increment.setBounds(new Rectangle( -1, 50, 60, 22));
//      label_Sections.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
//      label_Sections.setHorizontalAlignment(SwingConstants.RIGHT);
//      label_Sections.setText("Sections: ");
//      label_Sections.setBounds(new Rectangle( -1, 13, 57, 22)); // spinner_MoveIncr
//      model_MoveIncr = new SpinnerNumberModel((double) 1, 0.1, 50.0, 0.1);
//      spinner_MoveIncr = new JSpinner(model_MoveIncr);
//      editor_MoveIncr = new JSpinner.NumberEditor(spinner_MoveIncr, "0.0");
//      spinner_MoveIncr.setEditor(editor_MoveIncr);
//      spinner_MoveIncr.setFont(new java.awt.Font("Dialog", 0, 12));
//      spinner_MoveIncr.setBounds(new Rectangle(71, 77, 52, 24));
//      ChangeListener listener_MoveIncr = new ChangeListener()
//      {
//         public void stateChanged (ChangeEvent e) {
//            SpinnerModel source = (SpinnerModel) e.getSource();
//            //System.out.println("The value is: " + source.getValue());
//            String inStr = String.valueOf(source.getValue());
//            float value = 0f;
//            try {
//               value = Float.parseFloat(inStr);
//            }
//            catch (NumberFormatException nfe) {}
//            setMoveIncrement((int) (value * 1000));
//         }
//      };
//      model_MoveIncr.addChangeListener(listener_MoveIncr);
//
//      //
//      label_Bottom.setBounds(new Rectangle(10, 153, 41, 19));
//      label_Bottom.setText("Start");
//      label_Bottom.setFont(new java.awt.Font("Dialog", 0, 10));
//      label_Ctrl1.setFont(new java.awt.Font("Dialog", 1, 12));
//      label_Ctrl1.setHorizontalAlignment(SwingConstants.LEFT);
//      label_Ctrl1.setText("Focus/Stage Control");
//      label_Ctrl1.setBounds(new Rectangle(11, 5, 127, 18));
//      label_Zset.setFont(new java.awt.Font("Dialog", 1, 12));
//      label_Zset.setBounds(new Rectangle(12, 8, 122, 20));
//      label_Zset.setHorizontalAlignment(SwingConstants.LEFT);
//      label_Zset.setText("Z-Section Setup"); //----------------------------------
//      // spinner_Sections
//      zSections = 1; //Prefs.usr.getInt("seriesAcqZsections", 1);
//      if (zSections < 1) {
//         zSections = 1;
//      }
//      model_Sections = new SpinnerNumberModel(zSections, 1, 100, 1);
//      spinner_Sections = new JSpinner(model_Sections);
//      editor_Sections = new JSpinner.NumberEditor(spinner_Sections, "0");
//      spinner_Sections.setEditor(editor_Sections);
//      ChangeListener listener_Sections = new ChangeListener()
//      {
//         public void stateChanged (ChangeEvent e) {
//            SpinnerModel source = (SpinnerModel) e.getSource();
//            //System.out.println("zSections set to: " + source.getValue());
//            String inStr = String.valueOf(source.getValue());
//            try {
//               zSections = Integer.parseInt(inStr);
//            }
//            catch (NumberFormatException nfe) {}
//            adjustForChanges();
//         }
//      };
//      model_Sections.addChangeListener(listener_Sections);
//      //
//      // spinner_Zincr -----------------------------------------
//      zIncrement = 100; //Prefs.usr.getInt("seriesAcqZincrement", 1000);
//      double init = (double) (zIncrement) / 1000;
//      if (init < 0.1) {
//         init = 0.1;
//      }
//      model_Zincr = new SpinnerNumberModel(init, 0.1, 50.0, 0.1);
//      spinner_Zincr = new JSpinner(model_Zincr);
//      editor_Zincr = new JSpinner.NumberEditor(spinner_Zincr, "0.0");
//      spinner_Zincr.setEditor(editor_Zincr);
//      //model_Zincr.setStepSize(new Double(0.1));
//      ChangeListener listener_Zincr =
//            new ChangeListener()
//      {
//         public void stateChanged (ChangeEvent e) {
//            SpinnerModel source = (SpinnerModel) e.getSource();
//            // System.out.println("zIncrement set to: " + source.getValue());
//            String inStr = String.valueOf(source.getValue());
//            float value = 0;
//            try {
//               value = Float.parseFloat(inStr);
//            }
//            catch (NumberFormatException nfe) {}
//            zIncrement = (int) (value * 1000);
//            adjustForChanges();
//         }
//      };
//      model_Zincr.addChangeListener(listener_Zincr);
//      //
//      spinner_Sections.setBounds(new Rectangle(64, 10, 50, 29));
//      spinner_Zincr.setBounds(new Rectangle(65, 47, 52, 29));
//
//      //
//      button_SetZero.setBounds(new Rectangle(70, 113, 52, 28));
//      button_SetZero.setEnabled(false);
//      button_SetZero.setMargin(new Insets(2, 4, 2, 4));
//      button_SetZero.setText("zero");
//      button_SetZero.addActionListener(new ActionListener()
//      {
//         public void actionPerformed (ActionEvent actionEvent) {
//            button_SetZero_actionPerformed(actionEvent);
//         }
//      });
//
//      //
//      buttonConnect.setBounds(new Rectangle(70, 171, 59, 25));
//      buttonConnect.setEnabled(false);
//      buttonConnect.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
//      buttonConnect.setMargin(new Insets(2, 4, 2, 4));
//      buttonConnect.setText("Connect");
//
//      //
//      buttonConfig.setBounds(new Rectangle(69, 201, 59, 25));
//      buttonConfig.setFont(new java.awt.Font("Dialog", Font.PLAIN, 10));
//      buttonConfig.setMargin(new Insets(2, 4, 2, 4));
//      buttonConfig.setText("Config");
//      buttonConfig.addActionListener(new ActionListener()
//      {
//         public void actionPerformed (ActionEvent e) {
//            buttonConfig_actionPerformed(e);
//         }
//      }); //
//      panel_Sections.add(label_Sections, null);
//      panel_Sections.add(spinner_Sections, null);
//      panel_Sections.add(spinner_Zincr, null);
//      panel_Sections.add(label_Increment);
//      panel_Zset.add(button_MoveToTop, null);
//      panel_Zset.add(button_MoveToBottom, null);
//      panel_Zset.add(button_Preview, null);
//      panel_Zset.add(button_SetBottom, null);
//      panel_Zset.add(label_Zset, null);
//      panel_Zset.add(label_Top, null);
//      panel_Zset.add(field_Top, null);
//      panel_Zset.add(label_Bottom, null);
//      panel_Zset.add(field_Bottom, null);
//      panel_Zset.add(panel_Sections, null);
//      this.add(panel_Zset, null);
//
//      panel_MoveButtons.add(button_Up10, null);
//      panel_MoveButtons.add(button_Up, null);
//      panel_MoveButtons.add(button_MoveToZero, null);
//      panel_MoveButtons.add(button_Down, null);
//      panel_MoveButtons.add(button_Down10, null);
//      panelMovement.add(label_Ctrl1, null);
//      panelMovement.add(buttonConnect);
//      panelMovement.add(panel_CurrPos, null);
//      panel_CurrPos.add(label_CurPos, null);
//      panelMovement.add(buttonConfig);
//      panelMovement.add(spinner_MoveIncr);
//      panelMovement.add(button_SetZero);
//      panelMovement.add(panel_MoveButtons, null); //
//      button_Up.setEnabled(true);
//      button_Up10.setEnabled(true);
//      button_Down.setEnabled(true);
//      button_Down10.setEnabled(true);
//      button_MoveToZero.setEnabled(true);
//      button_MoveToTop.setEnabled(true);
//      button_MoveToBottom.setEnabled(true);
//      button_Preview.setEnabled(true);
//      enableLimitButtons(true);
//      enableZAcqButtons(true);
//      disenableMoveButtons();
//      this.add(panelMovement, null);
//      this.setBackground(new Color(223, 223, 200));
//      //initialize();
//      this.revalidate();
//   }
//
//
//   public void initializeValues () {
//      zBottom = 0; //Prefs.usr.getInt("seriesAcqZStart", 0);
//      adjustZTop();
//      synchFields();
//   }
//
//
//   public void initialize () {
//      if (stage != null) {
//         if (stage.isFunctional) {
//            initializeValues();
//            setMoveIncrement(1000); //Prefs.usr.getInt("stageMoveIncrement", 1000));
//            stage.setZeroPosition();
//            stage.setLowLimit( -500000);
//            stage.setHighLimit(200000);
//            //psj.PSjUtils.event("Stage/Focus Controller initialized");
//            synchFields();
//            button_Up.setEnabled(true);
//            button_Up10.setEnabled(true);
//            button_Down.setEnabled(true);
//            button_Down10.setEnabled(true);
//            button_MoveToZero.setEnabled(true);
//            button_MoveToTop.setEnabled(true);
//            button_MoveToBottom.setEnabled(true);
//            button_Preview.setEnabled(true);
//            enableLimitButtons(true);
//            enableZAcqButtons(true);
//            disenableMoveButtons();
//            this.add(panelMovement, null);
//            this.add(panel_Zset, null);
//         } else {
//            enableLimitButtons(false);
//            enableZAcqButtons(false);
//            button_Up.setEnabled(false);
//            button_Up10.setEnabled(false);
//            button_Down.setEnabled(false);
//            button_Down10.setEnabled(false);
//            button_MoveToZero.setEnabled(false);
//            button_MoveToTop.setEnabled(false);
//            button_MoveToBottom.setEnabled(false);
//            button_Preview.setEnabled(false);
//            button_SetZero.setEnabled(false);
//         }
//      }
//   }
//
//
////---------------------------------------------------------------------------
////
//   public int getMoveIncrement () {
//      return moveIncrement;
//   }
//
//
////---------------------------------------------------------------------------
//   // Stage Movement Buttons
//   //
//   void button_Up10_actionPerformed (ActionEvent e) {
//      moveUp(moveIncrement * 10);
//   }
//
//
//   void button_Up_actionPerformed (ActionEvent e) {
//      moveUp(moveIncrement);
//   }
//
//
//   public void moveUp (int n) {
//      stage.moveUp(n);
//      disenableMoveButtons();
//   }
//
//
//   void button_MoveToZero_actionPerformed (ActionEvent e) {
//      moveToZero();
//   }
//
//
//   public void moveToZero () {
//      stage.moveToAck(0);
//      disenableMoveButtons();
//   }
//
//
//   void button_Down_actionPerformed (ActionEvent e) {
//      moveDown(moveIncrement);
//   }
//
//
//   public void moveDown (int n) {
//      stage.moveDown(n);
//      disenableMoveButtons();
//   }
//
//
//   void button_Down10_actionPerformed (ActionEvent e) {
//      moveDown(moveIncrement * 10);
//   }
//
//
//   void setMoveIncrement (int moveIncrementnm) {
//      if (stage != null) {
//         moveIncrement = moveIncrementnm;
//         // System.out.println("setMoveIncrement: " + moveIncrement);
//         //Prefs.usr.putInt("stageMoveIncrement", moveIncrementnm);
//      }
//   }
//
//
//   void disenableMoveButtons () {
//      if (stage != null) {
//         label_CurPos.setText("-");
//         if (stage.areLimitsSet()) {
//            // update current position in panel
//            updatePosition();
//            int high = stage.getHighLimit();
//            int low = stage.getLowLimit();
//            int pos = 0;
//            try {
//               pos = stage.getCurrentPosition();
//            }
//            catch (Exception ex) {}
//            button_Up.setEnabled(pos < high);
//            button_Up10.setEnabled(pos <= (high - 10));
//            button_Down.setEnabled(pos > low);
//            button_Down10.setEnabled(pos >= (low + 10));
//            button_MoveToZero.setEnabled(pos != 0);
//         }
//      } else {
//         button_Up.setEnabled(false);
//         button_Up10.setEnabled(false);
//         button_Down.setEnabled(false);
//         button_Down10.setEnabled(false);
//         button_MoveToZero.setEnabled(false);
//      }
//   }
//
//
//   ////////////////////////////////////////////////////////////////////
//   //  Initialization & Limit buttons
//   //
//   void button_SetHigh_actionPerformed (ActionEvent e) {
//      try {
//         stage.setHighLimit(stage.getCurrentPosition());
//      }
//      catch (Exception ex) {}
//      disenableMoveButtons();
//      synchFields();
//   }
//
//
//   void button_SetLow_actionPerformed (ActionEvent e) {
//      try {
//         stage.setLowLimit(stage.getCurrentPosition());
//      }
//      catch (Exception ex) {}
//      disenableMoveButtons();
//      synchFields();
//   }
//
//
//   void enableLimitButtons (boolean t) {
//      //    button_SetHigh.setEnabled(true);
//      //    button_SetLow.setEnabled(true);
//      //    field_High.setEnabled(true);
//      //    field_Low.setEnabled(true);
//   }
//
//
//   //////////////////////////////////////////////////////////////////////////
//   //
//   void button_Initialize_actionPerformed (ActionEvent e) {
//      initialize();
//   }
//
//
//   void button_ClearCtrl_actionPerformed (ActionEvent e) {
//      stage.clearErrors();
//   }
//
//
//   void button_SetZero_actionPerformed (ActionEvent e) {
//      stage.setZeroPosition();
//      synchFields();
//      enableLimitButtons(true);
//      enableZAcqButtons(true);
//      //psj.PSjUtils.event("Focus position zeroed");
//      disenableMoveButtons();
//   }
//
//
//   ////////////////////////////////////////////////////////////////////
//   // Z-Scan parameters Set Buttons
//   //
//   public void enableZAcqButtons (boolean t) {
//      button_SetTop.setEnabled(t);
//      button_SetBottom.setEnabled(t);
//      //toggle_AnchorTop.setEnabled(t);
//      //toggle_AnchorBottom.setEnabled(t);
//      spinner_Sections.setEnabled(t);
//      spinner_Zincr.setEnabled(t);
//      field_Top.setEnabled(t);
//      field_Bottom.setEnabled(t);
//      disenableMoveButtons();
//   }
//
//
//   //  void button_SetTop_actionPerformed(ActionEvent e) {
//   //    zTop = stage.getPosition();
//   //    if (toggle_AnchorBottom.isSelected()) {
//   //      adjustZIncrement();
//   //    } else {
//   //      adjustZBottom();
//   //    }
//   //    synchFields();
//   //  }
//   //
//   void button_SetBottom_actionPerformed (ActionEvent e) {
//      stage.setZeroPosition();
//      try {
//         zBottom = stage.getCurrentPosition();
//      }
//      catch (Exception ex) {}
//      adjustZTop();
//      synchFields();
//      enableLimitButtons(true);
//      enableZAcqButtons(true);
//      //psj.PSjUtils.event("Focus position zeroed");
//   }
//
//
//   void button_MoveToTop_actionPerformed (ActionEvent e) {
//      stage.moveToAck(zTop);
//      disenableMoveButtons();
//   }
//
//
//   void button_MoveToBottom_actionPerformed (ActionEvent e) {
//      stage.moveToAck(zBottom);
//      disenableMoveButtons();
//   }
//
//
//   void adjustZTop () {
//      zTop = zBottom + (zSections * zIncrement);
//   }
//
//
//   void adjustZBottom () {
//      zBottom = zTop - (zSections * zIncrement);
//   }
//
//
//   void adjustZIncrement () {
//      float zIncrTemp = (float) (zTop - zBottom) / (float) zSections;
//      zIncrement = Math.round(zIncrTemp / 100) * 100; // round to .1 micron
//   }
//
//
//   void adjustZSections () {
//      zSections = Math.round((float) (zTop - zBottom) / zIncrement);
//   }
//
//
//   // Need to add some restrictions... like...
//   // if (toggle_AnchorTop.isSelected() && toggle_AnchorBottom.isSelected()) {
//   //   model_Sections.setMaximum(new Comparable(new Integer(5)) );
//   // }
//   void adjustForChanges () {
//      adjustZTop();
//      synchFields();
//   }
//
//
//   public void synchFields () {
//      updatePosition();
//      if (stage != null) {
//         //value_Zpi.setText(String.valueOf(stage.getZeroIndexPosition()));
//      }
////        Prefs.usr.putInt("seriesAcqZStart", zBottom);
////        Prefs.usr.putInt("seriesAcqZincrement", zIncrement);
////        Prefs.usr.putInt("seriesAcqZsections", zSections);
////        if (PSj.acqSeriesPanel != null) {
////           SwingUtilities.invokeLater(new Runnable()
////           {
////              public void run () {
////                 PSj.acqSeriesPanel.updateZscanFields();
////
////              }
////           });
////        }
//      field_Bottom.setText("-");
//      field_Top.setText("-");
//      repaint();
//      //Prefs.usr.getBoolean("seriesAcqZSeries",false);
//      //Prefs.usr.put("seriesAcqZSeries",true/false);
//      // dis/enable, actually...
//      //Prefs.usr.getInt("seriesAcqZStart",0);
//      //Prefs.usr.getInt("seriesAcqZincrement",0);
//      //Prefs.usr.getInt("seriesAcqZsections",0);
//      //
//      //Prefs.usr.put("seriesAcqZStart",);
//      //Prefs.usr.put("seriesAcqZincrement",);
//      //Prefs.usr.put("seriesAcqZsections",);
//      //    if (PSj.seriesProcPanel != null) {
//      //      PSj.seriesProcPanel.updateFields();
//      //    }
//   }
//
//
//   public boolean areZParemetersDefined () {
//      if ((stage != null) && stage.isFunctional) {
//         return true;
//      } else {
//         return false;
//      }
//   }
//
//
////---------------------------------------------------------------------------
//   void button_PrePosTest_actionPerformed (ActionEvent e) {}
//
//
//   void button_Preview_actionPerformed (ActionEvent e) {
//      System.out.println("Preview........................");
//      final SwingWorker3 worker =
//            new SwingWorker3()
//      {
//         public Object construct () {
//            /*
//             stage.moveToAck(Prefs.usr.getInt("seriesAcqZStart", 0)
//                                 - Prefs.usr.getInt("stageBacklash", 400));
//             stage.moveRelativeAck(Prefs.usr.getInt("stageBacklash", 400));
//             if (Camera.display.vPanel != null) {
//                 Camera.display.vPanel.showMessage(
//                         "Previewing Z-Scan: Start");
//             }
//             int i = 1;
//             do {
//                 stage.moveRelativeAck(Prefs.usr.getInt(
//                         "seriesAcqZincrement", 0));
//                 if (Camera.display.vPanel != null) {
//                     Camera.display.vPanel.showMessage("Previewing Z-Scan: " +
//                             i);
//                 }
//             edu.mbl.jif.utils.PSjUtils.waitFor((int) (Camera.exposure / 1000) + 300);
//                 i++;
//             }
//             while (i <= (Prefs.usr.getInt("seriesAcqZsections", 0)));
//             //
//             //stage.moveRelativeAck(Prefs.usr.getInt("seriesAcqZincrement", 0));
//             if (Camera.display.vPanel != null) {
//                 Camera.display.vPanel.clearMessage();
//             }
//             */
//            return null;
//         }
//
//
//         public void finished () {}
//      };
//      worker.start();
//   }
//
//
////---------------------------------------------------------------------------
//
//   void updatePosition () {
////    if (stage != null) {
////      (new SwingWorker() {
////        public Object construct() {
////          try {
////            label_CurPos.setText(formatter.format(
////                (float) stage.getCurrentPosition() /
////                stage.getNmPerIncrement() * 10));
////          } catch (Exception ex) {}
////          return null;
////        }
////
////        public void finished() {
////        }
////      }).start();
////    }
//   }
//
//
//   public void buttonConfig_actionPerformed (ActionEvent e) {
//      //new DialogStageConfig();
//   }
//
//
//   ////////////////////////////////////////////////////////////////////////////
//   // deBug...
//   //
//   void buttonShowState_actionPerformed (ActionEvent e) {
//      int pos = 0;
//      try {
//         pos = stage.getCurrentPosition();
//      }
//      catch (Exception ex) {}
//      System.out.println("stage.getCurrentPosition: " + pos);
//      try {
//         System.out.println("stage.position: " +
//                            stage.getCurrentPosition());
//      }
//      catch (Exception ex1) {}
//      System.out.println("stage.highLimit: " + stage.getHighLimit());
//      System.out.println("stage.lowLimit: " + stage.getLowLimit());
//      System.out.println("stage.nmPerIncrement: " +
//                         stage.getNmPerIncrement());
//      System.out.println("stage.moveIncrement: " + moveIncrement);
//      System.out.println("zTop, zBottom, zSections, zIncrement: " +
//                         zTop + ", " + zBottom + ", " + zSections + ", " +
//                         zIncrement);
//   }
//
//
//   public static void main (String[] args) {
//      FrameForTest f;
//      f = new FrameForTest(new PanelStage());
//      f.pack();
//
//   }
}
