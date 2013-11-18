package tests.gui.progress;

import edu.mbl.jif.gui.status.jx.JXStatusBar2;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.event.*;


public class JXStatusBarTest {

   public JXStatusBarTest() {
      super();
   }

   public static void main(String[] args) {
      JFrame frame = new JFrame();
      Container container = frame.getContentPane();
      container.setLayout(new BorderLayout());
      JPanel mainPane = new JPanel();
      final JXStatusBar2 statusBar = new JXStatusBar2();
//      statusBar.setText("Text");
//      statusBar.setLeadingMessage("Leading");
//      statusBar.setTrailingMessage("Trailing");

      mainPane.setPreferredSize(new Dimension(500, 200));
      JButton activateProgress = new JButton("Test");
      activateProgress.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ProgressGenerator generator = new ProgressGenerator();
//            generator.addMessageListener(statusBar);
//            generator.addProgressListener(statusBar);
            Thread thread = new Thread(generator);
            thread.start();
         }
      });
      
      mainPane.add(activateProgress);
      container.add(mainPane, BorderLayout.CENTER);
      container.add(statusBar, BorderLayout.SOUTH);

      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
   }
}
/*
patch for JXStatusBar...
setBorder(BorderFactory.createLoweredBevelBorder());
  
  	leadingLabel = (JLabel) add(new JLabel("", SwingConstants.LEADING));
+ 	leadingLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 6, 1, 6));
  	add(Box.createHorizontalGlue());
  
  	progressBar = (JProgressBar)add(new JProgressBar());
  	progressBar.setVisible(false);
  
  	trailingLabel = (JLabel) add(new JLabel("", SwingConstants.TRAILING));
+ 	trailingLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 6, 1, 6));
  
  	Font font = leadingLabel.getFont().deriveFont(Font.PLAIN);
  	leadingLabel.setFont(font);
 */