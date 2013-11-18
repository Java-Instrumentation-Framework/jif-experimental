package edu.mbl.jif.utils;


import javax.swing.*;
import java.awt.*;


public class FrameForTest
      extends JFrame
{

   BorderLayout borderLayout1 = new BorderLayout();
   JComponent panel;

   public FrameForTest () {
      this(null);
   }


   public FrameForTest (JComponent _panel) {
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
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      this.setTitle("TestFrame");
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
            FrameForTest f = new FrameForTest();
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(300, 400));
            f.addContents(p);

         }
      });


   }
}


