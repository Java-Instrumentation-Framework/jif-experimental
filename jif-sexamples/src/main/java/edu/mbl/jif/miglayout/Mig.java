package edu.mbl.jif.miglayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author GBH
 */
public class Mig {

   private static boolean buttonOpaque = true;
   private static boolean contentAreaFilled = true;
   private static final boolean OPAQUE = false;

   public static void setup() {
      try {
         System.setProperty("apple.laf.useScreenMenuBar", "true");
         System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MiGLayout Swing Demo");
      } catch (Throwable ex) {
      }
      String laf = UIManager.getSystemLookAndFeelClassName();
      if (laf.endsWith("WindowsLookAndFeel")) {
         buttonOpaque = false;
      }
      if (laf.endsWith("AquaLookAndFeel")) {
         contentAreaFilled = false;
      }

   }
   // **********************************************************
   // * Helper Methods
   // **********************************************************

   public static JLabel createLabel(String text) {
      return createLabel(text, SwingConstants.LEADING);
   }

   public static JLabel createLabel(String text, int align) {
      final JLabel b = new JLabel(text, align);
      return b;
   }

   public JComboBox createCombo(String[] items) {
      JComboBox combo = new JComboBox(items);
      if (PlatformDefaults.getCurrentPlatform() == PlatformDefaults.MAC_OSX) {
         combo.setOpaque(false);
      }
      return combo;
   }

   public static JTextField createTextField(int cols) {
      return createTextField("", cols);
   }

   public static JTextField createTextField(String text) {
      return createTextField(text, 0);
   }

   public static JTextField createTextField(String text, int cols) {
      final JTextField b = new JTextField(text, cols);
      return b;
   }
   private static final Font BUTT_FONT = new Font("monospaced", Font.PLAIN, 12);

   public static JButton createButton() {
      return createButton("");
   }

   public static JButton createButton(String text) {
      return createButton(text, false);
   }

   public static JButton createButton(String text, boolean bold) {
      JButton b = new JButton(text) {
         public void addNotify() {
            super.addNotify();
            if (getText().length() == 0) {
               String lText = (String) ((MigLayout) getParent().getLayout()).getComponentConstraints(this);
               setText(lText != null && lText.length() > 0 ? lText : "<Empty>");
            }

         }
      };
      if (bold) {
         b.setFont(b.getFont().deriveFont(Font.BOLD));
      }
      b.setOpaque(buttonOpaque); // Or window's buttons will have strange border
      b.setContentAreaFilled(contentAreaFilled);
      return b;
   }

   public static JToggleButton createToggleButton(String text) {
      JToggleButton b = new JToggleButton(text);
//		configureActiveComponet(b);
      b.setOpaque(buttonOpaque); // Or window's buttons will have strange border
      return b;
   }

   public static JCheckBox createCheck(String text) {
      JCheckBox b = new JCheckBox(text);
      b.setOpaque(OPAQUE); // Or window's checkboxes will have strange border
      return b;
   }

   public static JPanel createTabPanel(LayoutManager lm) {
      JPanel panel = new JPanel(lm);
      panel.setOpaque(OPAQUE);
      return panel;
   }

   public static JComponent createPanel() {
      return createPanel("");
   }

   public static JComponent createPanel(String s) {
      JLabel panel = new JLabel(s, SwingConstants.CENTER) {
         public void addNotify() {
            super.addNotify();
            if (getText().length() == 0) {
               String lText = (String) ((MigLayout) getParent().getLayout()).getComponentConstraints(this);
               setText(lText != null && lText.length() > 0 ? lText : "<Empty>");
            }
         }
      };
      panel.setBorder(new EtchedBorder());
      panel.setOpaque(true);
      return panel;
   }

   public static JTextArea createTextArea(String text, int rows, int cols) {
      JTextArea ta = new JTextArea(text, rows, cols);
      ta.setBorder(UIManager.getBorder("TextField.border"));
      ta.setFont(UIManager.getFont("TextField.font"));
      ta.setWrapStyleWord(true);
      ta.setLineWrap(true);
      return ta;
   }

   public static JScrollPane createTextAreaScroll(String text, int rows, int cols, boolean hasVerScroll) {
      JTextArea ta = new JTextArea(text, rows, cols);
      ta.setFont(UIManager.getFont("TextField.font"));
      ta.setWrapStyleWord(true);
      ta.setLineWrap(true);
      JScrollPane scroll = new JScrollPane(
              ta,
              hasVerScroll ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      return scroll;
   }
   static final Color LABEL_COLOR = new Color(0, 70, 213);

   public static void addSeparator(JPanel panel, String text) {
      JLabel l = createLabel(text);
      l.setForeground(LABEL_COLOR);
      panel.add(l, "gapbottom 1, span, split 2, aligny center");
      panel.add(new JSeparator());
   }
}
