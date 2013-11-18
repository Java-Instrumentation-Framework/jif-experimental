package tests.gui;

import javax.swing.*;
import java.awt.*;
import edu.mbl.jif.utils.*;
import javax.swing.event.*;


public class WindowForTest
      extends JWindow
{

   BorderLayout borderLayout1 = new BorderLayout();
   JComponent panel;

   public WindowForTest () {
      this(null);
   }


   public WindowForTest (JComponent _panel) {
      super();
      panel = _panel;
      try {
         jbInit();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   void jbInit () throws Exception {
      this.getContentPane().setLayout(new BorderLayout());
      if (panel != null) {
         addContents(panel);
      }
      setVisible(true);
      //org.pf.joi.Inspector.inspect(this);
   }


   public void addContents (JComponent j) {
      this.getContentPane().add(j, BorderLayout.CENTER);
//      this.setSize(
//            (int) j.getPreferredSize().getWidth() + 16,
//            (int) j.getPreferredSize().getHeight() + 28);
      pack();
   }


   public static void main (String[] args) {
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run () {
            WindowForTest f = new WindowForTest();
            JPanel p = new JPanel();
            p.setBackground(Color.BLUE);
            p.setPreferredSize(new Dimension(300, 400));
            f.addContents(p);

         }
      });


   }
}
