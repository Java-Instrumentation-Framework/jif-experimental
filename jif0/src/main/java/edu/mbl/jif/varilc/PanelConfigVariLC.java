package edu.mbl.jif.varilc;

import javax.swing.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PanelConfigVariLC
      extends JPanel
{
    /*
     *
   BorderLayout borderLayout1 = new BorderLayout();
   private JPanel tab_Config = new JPanel();
   private JRadioButton radioButton_LC13micron = new JRadioButton();
   private JRadioButton radioButton_LC7micron = new JRadioButton();
   private JRadioButton radioButton_Swing1 = new JRadioButton();
   private JRadioButton radioButton_Swing2 = new JRadioButton();
   private JRadioButton radioButton_Swing3 = new JRadioButton();
   private JRadioButton radioButton_Swing4 = new JRadioButton();
   private RadioListener radioListenerSwing;
   private JButton button_Version = new JButton();
   private ButtonGroup buttonGroup_LCType = new ButtonGroup();
   private ButtonGroup groupSwing = new ButtonGroup();
   private JLabel label_SwingSetting = new JLabel();
   private JLabel value_Swing = new JLabel();
   private JLabel label_VLCVersion = new JLabel();
   private JLabel label_Wavelength = new JLabel();
   private JLabel label_LCSettle = new JLabel();
   private JLabel label_LCType = new JLabel();
   private JLabel label_MaxRet = new JLabel();
   private JPanel panel_LCType = new JPanel();
   JLabel jLabel1 = new JLabel();
   SpinInteger spinAzimRefField;
   SpinInteger spinWavelength;
   SpinnerNumberModel modelAzimRef;
   SpinnerNumberModel modelWave;
   JSpinner spinnerSettle;
   private SpinnerNumberModel modelSettle;
   JPanel panelSwing = new JPanel();
   java.awt.Font font11 = new java.awt.Font("Dialog", Font.PLAIN, 11);

   VariLC_RT vlc;

   public PanelConfigVariLC () {
      try {
         new VariLC_RT(new SerialPortConnection());
      }
      catch (Exception ex) {
      }

   }


   public PanelConfigVariLC (VariLC_RT vlc) {
      this.vlc = vlc;
      try {
         jbInit();
      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
   }


   private void jbInit () throws Exception {
      this.setLayout(borderLayout1);
      tab_Config.setLayout(null);
      tab_Config.setBackground(JifColor.yellow[3]);
      // Wavelength
      modelWave = new SpinnerNumberModel((int) Prefs.usr.getFloat("wavelength", 540.0f),
            440, 1000, 10);
      spinWavelength = new SpinInteger("Wavelength: ", modelWave, "000", 3, 12);
      spinWavelength.setOpaque(false);
      spinWavelength.setBounds(new Rectangle(62, 126, 141, 33));
      modelWave.addChangeListener(new ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            vlc.setWavelength((float) modelWave.getNumber().intValue());
            //updateAcqParms();
         }
      });

      // AzimRef
      modelAzimRef = new SpinnerNumberModel(Prefs.usr.getInt("acq_azimuthRef", 0), 0, 179,
            1);
      spinAzimRefField = new SpinInteger("Azim Ref Angle:", modelAzimRef, "0", 2, 12);
      spinAzimRefField.setOpaque(false);
      spinAzimRefField.setBounds(new Rectangle(64, 156, 139, 29));
      modelAzimRef.addChangeListener(new ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            Prefs.usr.putInt("acq_azimuthRef", modelAzimRef.getNumber().intValue());
            //updateAcqParms
         }
      });
      button_Version.setBounds(new Rectangle(6, 347, 72, 23));

      button_Version.setFont(font11);

      //Settling Time
      label_LCSettle.setText("LC Settling Time (ms):");
      label_LCSettle.setBounds(new Rectangle(15, 77, 124, 22));
      label_LCSettle.setFont(font11);
      int settleVal = Prefs.usr.getInt("LC_SettleTime", 150);
      modelSettle = new SpinnerNumberModel(settleVal, 0, 1000, 10);
      spinnerSettle = new JSpinner(modelSettle);
      spinnerSettle.setBounds(new Rectangle(137, 76, 62, 25));
      spinnerSettle.setFont(font11);
      JSpinner.NumberEditor editorSettle = new JSpinner.NumberEditor(spinnerSettle, "0");
      spinnerSettle.setEditor(editorSettle);
      ChangeListener listenerSettle = new ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            SpinnerModel source = (SpinnerModel) e.getSource();

            // change the value of SettleTime property
            String settleStr = String.valueOf(source.getValue());
            int settleVal = 0;
            try {
               settleVal = Integer.parseInt(settleStr);
            }
            catch (NumberFormatException nfe) {}
            vlc.setSettlingTime(settleVal);
         }
      };
      modelSettle.addChangeListener(listenerSettle);

      // LC Type
      label_LCType.setFont(new java.awt.Font("Dialog", 1, 12));
      label_LCType.setText("VariLC Type:");
      label_LCType.setBounds(new Rectangle(11, 9, 85, 17));
      radioButton_LC7micron.setOpaque(false);
      radioButton_LC7micron.setText("Fast: 7 micron (min 30 msec.)");
      radioButton_LC7micron.setBounds(new Rectangle(7, 30, 205, 20));
      radioButton_LC7micron.addChangeListener(new javax.swing.event.ChangeListener()
      {
         public void stateChanged (ChangeEvent e) {
            radioButton_LC7micron_stateChanged(e);
         }
      });
      radioButton_LC13micron.setBounds(new Rectangle(7, 50, 206, 20));
      radioButton_LC13micron.setText("Standard: 13 micron (min 150 msec.)");
      radioButton_LC13micron.setOpaque(false);
      panelSwing.setAlignmentX((float) 0.1);
      radioButton_Swing2.setFont(font11);
      radioButton_Swing2.setHorizontalAlignment(SwingConstants.LEFT);
      radioButton_Swing1.setFont(font11);
      radioButton_Swing1.setHorizontalAlignment(SwingConstants.LEFT);
      label_SwingSetting.setBounds(new Rectangle(10, 8, 78, 16));
      label_VLCVersion.setBounds(new Rectangle(11, 372, 222, 25));
      radioButton_Swing3.setFont(new java.awt.Font("Dialog", Font.PLAIN, 11));
      radioButton_Swing4.setFont(new java.awt.Font("Dialog", Font.PLAIN, 11));
      panel_LCType.setPreferredSize(new Dimension(200, 400));

      buttonGroup_LCType.add(radioButton_LC7micron);
      buttonGroup_LCType.add(radioButton_LC13micron);

      String lcType = Prefs.usr.get("LC_Type", "7_micron");
      if (lcType.equalsIgnoreCase("7_micron")) {
         radioButton_LC7micron.setSelected(true);
         setLCType7();
      } else {
         radioButton_LC13micron.setSelected(true);
         setLCType13();
      }
      radioButton_LC7micron.setFont(font11);
      radioButton_LC13micron.setFont(font11);

      panel_LCType.setBorder(BorderFactory.createEtchedBorder());
      panel_LCType.setBackground(JifColor.yellow[3]);
      panel_LCType.setBounds(new Rectangle(3, 4, 231, 197));
      panel_LCType.setLayout(null);
      panel_LCType.add(radioButton_LC7micron, null);
      panel_LCType.add(radioButton_LC13micron, null);
      panel_LCType.add(label_LCType, null);
      panel_LCType.add(label_LCSettle, null);
      panel_LCType.add(jLabel1);
      panel_LCType.add(spinWavelength);
      panel_LCType.add(spinAzimRefField);
      panel_LCType.add(spinnerSettle, null);
      tab_Config.add(panel_LCType, null);

      // Version
      button_Version.setMargin(new Insets(2, 2, 2, 2));
      button_Version.setText("Get Version");
      button_Version.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed (ActionEvent e) {
            button_Version_actionPerformed(e);
         }
      });
      label_VLCVersion.setFont(new java.awt.Font("Dialog", 0, 11));
      label_VLCVersion.setText("VariLC:"); //
      jLabel1.setFont(new java.awt.Font("Dialog", Font.BOLD, 12));
      jLabel1.setText("System Settings");
      jLabel1.setBounds(new Rectangle(11, 107, 132, 18));
      tab_Config.add(label_VLCVersion);
      tab_Config.add(button_Version);

      // Swing setting
      panelSwing.setMinimumSize(new Dimension(50, 50));
      panelSwing.setPreferredSize(new Dimension(50, 50));
      panelSwing.setBounds(new Rectangle(4, 206, 231, 132));
      label_SwingSetting.setFont(font11);
      label_SwingSetting.setText("Swing Setting");
      radioListenerSwing = new RadioListener();
      radioButton_Swing4.setText("0.20 wave (retardances up to 273 nm)");
      radioButton_Swing4.setBounds(new Rectangle(11, 99, 212, 23));
      radioButton_Swing4.setOpaque(false);
      radioButton_Swing4.setActionCommand("swing4");
      radioButton_Swing4.addActionListener(radioListenerSwing);
      radioButton_Swing3.setText("0.10 wave (retardances up to 150 nm)");
      radioButton_Swing3.setBounds(new Rectangle(11, 74, 212, 23));
      radioButton_Swing3.setOpaque(false);
      radioButton_Swing3.setActionCommand("swing3");
      radioButton_Swing3.addActionListener(radioListenerSwing);
      radioButton_Swing2.setText("0.03 wave (retardances up to 50 nm)");
      radioButton_Swing2.setBounds(new Rectangle(11, 50, 206, 23));
      radioButton_Swing2.setOpaque(false);
      radioButton_Swing2.setActionCommand("swing2");
      radioButton_Swing2.addActionListener(radioListenerSwing);
      radioButton_Swing1.setOpaque(false);
      radioButton_Swing1.setText("0.02 wave (retardances up to 5 nm)");
      radioButton_Swing1.setBounds(new Rectangle(11, 27, 236, 23));
      radioButton_Swing1.setActionCommand("swing1");
      radioButton_Swing1.addActionListener(radioListenerSwing);

      groupSwing.add(radioButton_Swing4);
      groupSwing.add(radioButton_Swing3);
      groupSwing.add(radioButton_Swing2);
      groupSwing.add(radioButton_Swing1);
      selectSwingRadio();

      panelSwing.setBackground(JifColor.yellow[3]);
      panelSwing.add(label_SwingSetting, null);
      panelSwing.add(radioButton_Swing1, null);
      panelSwing.add(radioButton_Swing2, null);
      panelSwing.add(radioButton_Swing3, null);
      panelSwing.add(radioButton_Swing4, null);

      panelSwing.setBorder(BorderFactory.createEtchedBorder());
      panelSwing.setInputVerifier(null);
      panelSwing.setLayout(null);
      value_Swing.setFont(font11);
      value_Swing.setBounds(new Rectangle(15, 167, 113, 24));
      panelSwing.add(radioButton_Swing1, null);
      panelSwing.add(label_SwingSetting, null);
      panelSwing.add(radioButton_Swing4, null);
      panelSwing.add(radioButton_Swing3, null);
      tab_Config.add(panelSwing);
      this.add(tab_Config, BorderLayout.CENTER);

   }


   public void setVariLC (VariLC_RT vlc) {
      this.vlc = vlc;
   }


///////////////////////////////////////////////////////////////////////
// LC_Type Selection
//
   void radioButton_LC7micron_stateChanged (ChangeEvent e) {
      if (radioButton_LC7micron.isSelected()) {
         setLCType7();
      } else { // LC13micron
         setLCType13();
      }
   }


   void setLCType7 () {
      if (radioButton_Swing4.isSelected()) {
         radioButton_Swing3.setSelected(true);
         changeSwing(0.03f);
      }
      radioButton_Swing4.setEnabled(false);
      vlc.setType("7_micron");
      setSettleTimeMin(30);
   }


   void setLCType13 () {
      vlc.setType("13_micron");
      radioButton_Swing4.setEnabled(true);
      setSettleTimeMin(150);
   }


   void setSettleTimeMin (int minTime) {
      if (Prefs.usr.getInt("LC_SettleTime", 150) < minTime) {
         modelSettle.setValue(new Integer(minTime));
         vlc.setSettlingTime(minTime);
      }
      vlc.setSettlingTime(minTime + 10);
      modelSettle.setMinimum(new Integer(minTime));
   }


   void setSwingSetting (String set) {
      float setTo = 0.03f;
      int retCeiling = 0;
      if (set.equalsIgnoreCase("swing1")) {
         setTo = 0.02f;
         retCeiling = 3;
      }
      if (set.equalsIgnoreCase("swing2")) {
         setTo = 0.03f;
         retCeiling = 20;
      }
      if (set.equalsIgnoreCase("swing3")) {
         setTo = 0.10f;
         retCeiling = 100;
      }
      if (set.equalsIgnoreCase("swing4")) {
         setTo = 0.20f;
         retCeiling = 200;
      }
      changeSwing(setTo);
      // change the default Acq. Retardance Ceiling
      Prefs.usr.putFloat("acq_retCeiling", retCeiling);
      //PSj.acqPanel.setRetardanceCeiling(retCeiling);
   }


   void selectSwingRadio () {
      float LC_SwingVal = (float) Prefs.usr.getDouble("LC_Swing", 0.03);

      // ++ Check for LC_Type...
      // String lcType = Prefs.usr.get("LC_Type", "7_micron");
      vlc.setLCSwing(LC_SwingVal);
      value_Swing.setText("Swing:  " + String.valueOf(LC_SwingVal) + " nm.");
      ButtonModel model = null;
      if (LC_SwingVal == 0.02f) {
         model = radioButton_Swing1.getModel();
      }
      if (LC_SwingVal == 0.03f) {
         model = radioButton_Swing2.getModel();
      }
      if (LC_SwingVal == 0.10f) {
         model = radioButton_Swing3.getModel();
      }
      if (LC_SwingVal == 0.20f) {
         model = radioButton_Swing4.getModel();
      }
      if (model != null) {
         groupSwing.setSelected(model, true);
      }
   }


   void changeSwing (float setTo) {
      vlc.setLCSwing(setTo);
      value_Swing.setText("Swing:  " + String.valueOf(setTo) + " nm.");
      DialogBoxI.boxNotify("Change to Swing Setting",
            "Swing Setting changed to " + String.valueOf(setTo) + ".\n"
            + "You may wish to recalibrate or \n change VariLC settings.");
   }


   void button_Version_actionPerformed (ActionEvent e) {
      //getVersion();
      label_VLCVersion.setText("VariLC: " + vlc.getVersion());
      repaint();
   }


////////////////////////////////////////////////////////////////////////
// Swing setting radiobutton actions
//
   class RadioListener
         implements ActionListener
   {
      public void actionPerformed (ActionEvent e) {
         setSwingSetting((String) e.getActionCommand());
      }
   }



   public static void main (String[] args) {
      PanelConfigVariLC config = null;
      try {
         config = new PanelConfigVariLC(new VariLC_RT(new SerialPortConnection()));
         FrameForTest f = new FrameForTest(config); }
      catch (Exception ex) {
      }

   }
     */
}
