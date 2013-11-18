package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.PanelAdjustPath;
import edu.mbl.jif.varilc.multi.PathVLC;
import edu.mbl.jif.varilc.multi.Retarder;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.text.DecimalFormat;

import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.varilc.camacq.VariLC_RT;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PanelPathVLC
      extends JPanel
{

   Box box;
   JLabel labelPath = new JLabel();
   JButton buttonAdjust = new JButton();
   Box boxButtons;
   Box boxLabels;
   JToggleButton[] button_Element;
   ButtonGroup buttonGroup1 = new ButtonGroup();
   JLabel[] labelRet;
   DecimalFormat fmt = new DecimalFormat("0.000");

   //
   PathVLC path;

   JPanel panelMeasured = new JPanel();
   JLabel labelMeasured = new JLabel();


   public PanelPathVLC () {
      this(new PathVLC());
   }

   public PanelPathVLC (PathVLC _path) {

      path = _path;

      try {
         jbInit();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   void jbInit () throws Exception {
      box = Box.createVerticalBox();
      boxButtons = Box.createVerticalBox();
      boxLabels = Box.createVerticalBox();

      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      // this.setBackground(PSj.COLOR_VARILC[4]);
      this.setAlignmentX((float) 0.5);
      this.setPreferredSize(new Dimension(44, 272));
      //
      labelPath.setFont(new java.awt.Font("DialogInput", 1, 14));
      labelPath.setOpaque(false);
      labelPath.setPreferredSize(new Dimension(20, 24));
      labelPath.setHorizontalAlignment(SwingConstants.CENTER);
      labelPath.setHorizontalTextPosition(SwingConstants.CENTER);
      labelPath.setText(path.getName());
      labelPath.setAlignmentX(Component.CENTER_ALIGNMENT);
      //
      buttonAdjust.setFont(new java.awt.Font("Arial", 0, 10));
      buttonAdjust.setMargin(new Insets(2, 4, 2, 4));
      buttonAdjust.addActionListener(new PanelPathVLC_buttonAdjust_actionAdapter(this));
      buttonAdjust.setAlignmentX(Component.CENTER_ALIGNMENT);
      buttonAdjust.setMaximumSize(new Dimension(32, 32));
      buttonAdjust.setMinimumSize(new Dimension(32, 32));
      buttonAdjust.setPreferredSize(new Dimension(32, 32));
      buttonAdjust.setIcon(new ImageIcon(this.getClass().getResource("icons/knob.gif")));

      // Add settings buttons
      button_Element = new JToggleButton[path.numSettings];
      for (int i = 0; i < path.numSettings; i++) {
         button_Element[i] = new JToggleButton();
         //button_Element[i].setBorder(BorderFactory.createRaisedBevelBorder());
         button_Element[i].setFont(new java.awt.Font("Dialog", 0, 11));
         button_Element[i].setMaximumSize(new Dimension(32, 32));
         button_Element[i].setMinimumSize(new Dimension(32, 32));
         button_Element[i].setPreferredSize(new Dimension(32, 32));
         button_Element[i].setMargin(new Insets(2, 4, 2, 4));
         button_Element[i].setIcon(VariLC_RT.pStackIcon[i + 2]);
         button_Element[i].setSelectedIcon(VariLC_RT.pStackIconSelected[i + 2]);
         final int s = i;
         button_Element[i].addActionListener(new java.awt.event.ActionListener()
         {
            public void actionPerformed (ActionEvent e) {
               path.switchToSetting(s);
               updateValues();
            }
         });
         boxButtons.add(button_Element[i]);
         buttonGroup1.add(button_Element[i]);
      }
      // Add value labels
      Font fontSettings = new Font("Dialog", 0, 10);

      boxButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
      labelRet = new JLabel[path.getNumRetarders()];
      for (int i = 0; i < path.getNumRetarders(); i++) {
         labelRet[i] = new JLabel(String.valueOf(i));
         labelRet[i].setAlignmentX((float) 0.5);
         labelRet[i].setMaximumSize(new Dimension(32, 18));
         labelRet[i].setMinimumSize(new Dimension(32, 18));
         labelRet[i].setOpaque(false);
         labelRet[i].setPreferredSize(new Dimension(32, 18));
         labelRet[i].setText("0.000");
         labelRet[i].setFont(fontSettings);
         // type of retarder
         switch (path.getRetarders()[i].type) {
            case Retarder.CELL_0_DEG:
               labelRet[i].setForeground(Color.black);
               break;
            case Retarder.CELL_45_DEG:
               labelRet[i].setForeground(Color.black);
               break;
            case Retarder.ATTENUATOR:
               labelRet[i].setForeground(Color.gray);
               break;
         }
         labelRet[i].setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
         boxLabels.add(labelRet[i], null);
      }

      panelMeasured.setLayout(new BorderLayout());
      panelMeasured.setBorder(BorderFactory.createEtchedBorder());
      panelMeasured.setPreferredSize(new Dimension(36, 22));
      labelMeasured.setText("255");
      labelMeasured.setBounds(new Rectangle(174, 125, 21, 15));
      //labelMeasured.setAlignmentX( (float) 0.5);
      //labelMeasured.setHorizontalTextPosition(JLabel.CENTER);
      labelMeasured.setHorizontalAlignment(JLabel.CENTER);
      box.add(labelPath);
      panelMeasured.add(labelMeasured, BorderLayout.CENTER);
      box.add(panelMeasured);
      box.add(boxButtons, null);
      box.add(boxLabels, null);
      box.add(buttonAdjust, null);
      box.add(Box.createHorizontalStrut(40));
      this.add(box, BorderLayout.CENTER);
      //this.setOpaque(false);
      this.validate();
      updateValues();
   }


   public void updateValues () {
      for (int i = 0; i < path.getNumRetarders(); i++) {
         labelRet[i].setText(fmt.format(path.setting[i][path.getSetTo()]));
      }
      //buttonGroup1.getSelection().getSelectedObjects()
   }


   public void setMeasured (int n) {
      labelMeasured.setText(String.valueOf(n));
      if (n > 254) {
         labelMeasured.setForeground(Color.red);
      } else {
         labelMeasured.setForeground(Color.black);
      }
   }


   public void buttonAdjust_actionPerformed (ActionEvent e) {
      PanelAdjustPath p = new PanelAdjustPath(path);
      FrameForTest f = new FrameForTest(p);
			f.setTitle("VariLC-Adjust Path: " + p.getName());
   }
}



class PanelPathVLC_buttonAdjust_actionAdapter
      implements ActionListener
{
   private PanelPathVLC adaptee;
   PanelPathVLC_buttonAdjust_actionAdapter (PanelPathVLC adaptee) {
      this.adaptee = adaptee; }


   public void actionPerformed (ActionEvent e) {
      adaptee.buttonAdjust_actionPerformed(e); }}
