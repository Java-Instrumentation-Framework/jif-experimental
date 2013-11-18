package tests.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;


public class VetoableTest
      extends JFrame
{
   JDesktopPane desktopPane = new JDesktopPane();

   public VetoableTest () {
      Container contentPane = getContentPane();

      contentPane.add(desktopPane, BorderLayout.CENTER);
      desktopPane.setLayout(new FlowLayout());

      JInternalFrame jif = new JInternalFrame("A Window with a Vetoable Change Listener", false, true); // title,  resizable,  closable

      jif.setPreferredSize(new Dimension(400, 350));
      jif.show();
      jif.addVetoableChangeListener(new CloseListener());

      desktopPane.add(jif);
   }


   public static void main (String args[]) {
      GJApp.launch(new VetoableTest(), "Vetoing Internal Frame Closing", 300, 300, 550,
            400);
   }
}



class CloseListener
      implements VetoableChangeListener
{
   private VetoableTest applet;

   public void vetoableChange (PropertyChangeEvent e) throws PropertyVetoException {
      String name = e.getPropertyName();

      if (name.equals(JInternalFrame.IS_CLOSED_PROPERTY)) {
         Component internalFrame = (Component) e.getSource();
         Boolean oldValue = (Boolean) e.getOldValue(), newValue = (Boolean) e.getNewValue();

         if (oldValue == Boolean.FALSE && newValue == Boolean.TRUE) {
            int answer = JOptionPane.showConfirmDialog(internalFrame, "Save Changes?", // parentComponent,  message
                  "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION); // title

            if (answer == JOptionPane.CANCEL_OPTION) {
               throw new PropertyVetoException("close cancelled", e);
            }
         }
      }
   }
}



class GJApp
      extends WindowAdapter
{
   static private JPanel statusArea = new JPanel();
   static private JLabel status = new JLabel(" ");
   static private ResourceBundle resources;

   public static void launch (final JFrame f, String title, final int x, final int y,
         final int w, int h) {
      launch(f, title, x, y, w, h, null);
   }


   public static void launch (final JFrame f, String title, final int x, final int y,
         final int w, int h, String propertiesFilename) {
      f.setTitle(title);
      f.setBounds(x, y, w, h);
      f.setVisible(true);

      statusArea.setBorder(BorderFactory.createEtchedBorder());
      statusArea.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      statusArea.add(status);
      status.setHorizontalAlignment(JLabel.LEFT);

      f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      if (propertiesFilename != null) {
         resources = ResourceBundle.getBundle(propertiesFilename, Locale.getDefault());
      }

      f.addWindowListener(new WindowAdapter()
      {
         public void windowClosed (WindowEvent e) {
            System.exit(0);
         }
      });
   }


   static public JPanel getStatusArea () {
      return statusArea;
   }


   static public void showStatus (String s) {
      status.setText(s);
   }


   static Object getResource (String key) {
      if (resources != null) {
         return resources.getString(key);
      }
      return null;
   }
}
