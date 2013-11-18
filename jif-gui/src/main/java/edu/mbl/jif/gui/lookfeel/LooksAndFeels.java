package edu.mbl.jif.gui.lookfeel;


import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.ColorUIResource;
import java.awt.Font;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
//import de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;


public class LooksAndFeels
{
   public LooksAndFeels () {
   }


   // If L&F  changed After startup:
   //UIManager.setLookAndFeel(lnfName);
   //SwingUtilities.updateComponentTreeUI(frame);
   //frame.pack();
// --------------------------------------------------------------------
   
   
   static void setLookAndFeel (String laf) {
      try {
         UIManager.setLookAndFeel(laf);
      }
      catch (javax.swing.UnsupportedLookAndFeelException use) {
         setCrossPlatform();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public static void setCrossPlatform () {
      try {
         UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public static void setSystem () {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public static void setMetal () {
      setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      //MetalTheme theme = new psj.Utils.MoodyBlueTheme();
      //MetalTheme theme = new psj.Utils.WhiteSatinTheme();\
   }


   public static void setWindows () {
      setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
   }


   public static void setKunststoff () {
      setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
   }


   public static void setSynth () {
      try {
         SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
         try {
            UIManager.setLookAndFeel(lookAndFeel);
         }
         catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
         }
         //SynthLookAndFeel.setStyleFactory(new CustomStyleFactory());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

//
//   public static void setSynthetica() {
//      //setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel");
//      setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
//      //SyntheticaLookAndFeel.setWindowsDecorated(false);
//      de.javasoft.plaf.synthetica.SyntheticaLookAndFeel.setAntiAliasEnabled(true);
//      //SynthLookAndFeel.setStyleFactory(new CustomStyleFactory());
//   }
//   public static void setSyntheticaIce() {
//      setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel");
//      //SyntheticaLookAndFeel.setWindowsDecorated(false);
//      de.javasoft.plaf.synthetica.SyntheticaLookAndFeel.setAntiAliasEnabled(true);
//      //SynthLookAndFeel.setStyleFactory(new CustomStyleFactory());
//   }

   public static void setOyoaha () {
      try {
         //OyoahaLookAndFeel lookFeel = new OyoahaLookAndFeel();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public static void setLooks () {
      try {
         com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
               //new com.jgoodies.looks.plastic.theme.DesertBluer());
               new com.jgoodies.looks.plastic.theme.Silver());
         //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
         //new com.jgoodies.looks.plastic.theme.DesertBlue());
         com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
         setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public static void main (String args[]) {

      final JFrame frame = new JFrame("Change Look");

      ActionListener actionListener = new ActionListener()
      {
         public void actionPerformed (ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            String lafClassName = null;
            if (source instanceof JComboBox) {
               JComboBox comboBox = (JComboBox) source;
               lafClassName = (String) comboBox.getSelectedItem();
            } else if (source instanceof JButton) {
               lafClassName = actionEvent.getActionCommand();
            }
            if (lafClassName != null) {
               final String finalLafClassName = lafClassName;
                System.out.println(finalLafClassName);
               Runnable runnable = new Runnable()
               {
                  public void run () {
                     try {
                        UIManager.setLookAndFeel(finalLafClassName);
                        SwingUtilities.updateComponentTreeUI(frame);
                     }
                     catch (Exception exception) {
                        JOptionPane.showMessageDialog(frame, "Can't change look and feel",
                              "Invalid PLAF", JOptionPane.ERROR_MESSAGE);
                     }
                  }
               };
               SwingUtilities.invokeLater(runnable);
            }
         }
      };

      Object newSettings[] = {"Button.background", Color.pink, "Button.foreground",
            Color.magenta};
      UIDefaults defaults = UIManager.getDefaults();
      defaults.putDefaults(newSettings);

      UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();

      DefaultComboBoxModel model = new DefaultComboBoxModel();
      JComboBox comboBox = new JComboBox(model);

      JPanel panel = new JPanel();

      for (int i = 0, n = looks.length; i < n; i++) {
         JButton button = new JButton(looks[i].getName());
         model.addElement(looks[i].getClassName());
         button.setActionCommand(looks[i].getClassName());
         button.addActionListener(actionListener);
         panel.add(button);
      }

      comboBox.addActionListener(actionListener);

      Container contentPane = frame.getContentPane();
      contentPane.add(comboBox, BorderLayout.NORTH);
      contentPane.add(panel, BorderLayout.SOUTH);
      frame.setSize(350, 150);
      frame.setVisible(true);
   }

   // Nimbus
//        try {
//            UIManager.setLookAndFeel("org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeel");
//        } catch (javax.swing.UnsupportedLookAndFeelException use) {
//            UIManager.getSystemLookAndFeelClassName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

   //Nimrod
   /*       UIManager.setLookAndFeel( new com.nilo.plaf.nimrod.NimRODLookAndFeel());

// This will use de default colours. If you prefer to change them, you can write your own 
// class extending MetalTheme or you can use NimRODTheme and set the colours. Example:
NimRODTheme nt = new NimRODTheme();
nt.setPrimary1( new Color(10,10,10));
nt.setPrimary2( new Color(20,20,20));
nt.setPrimary3( new Color(30,30,30));

NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
NimRODLF.setCurrentTheme( nt);
UIManager.setLookAndFeel( NimRODLF);

*/
   
    }
