package tests.imaging;
/*
 * Created on Jun 14, 2005
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 *
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 *
 * STATUS: Complete, but could use some improvements.
 *
 * Redistribution and usage conditions must be done under the
 * Creative Commons license:
 * English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
 * Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
 * More information on design and applications are on the projects' page
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */

import edu.mbl.jif.imaging.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class is a simple application that allows the user to download images from the
 * Internet and see them in internal frames on the application. Images can be scaled.
 */
public class SimpleViewer extends JFrame implements ActionListener {
   // A JDesktopPane to hold the DisplayJAI components with the downloaded images.
   private JDesktopPane desktop;
   // A JTextField to hold the URL of the images.
   private JTextField url;
   // A JComboBox (and associated array) of scale values.
   private JComboBox scale;
   private String[] scales = {"0.01","0.05","0.1","0.2","0.5","1","2","5","10","20"};
   
   /**
    * The constructor for this class sets its user interface.
    */
   public SimpleViewer() {
      desktop = new JDesktopPane();
      // Create a controlPanel to hold the UI controls.
      JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      // The URL text field.
      controlPanel.add(new JLabel("Image URL:"));
      url = new JTextField(50);
      url.addActionListener(this);
      // Uncomment the line below to have a default string.
      url.setText("http://www.lac.inpe.br/~rafael.santos/Java/JAI/datasets/pyramids.jpg");
      controlPanel.add(url);
      // The scale combobox.
      controlPanel.add(new JLabel("Scale:"));
      scale = new JComboBox(scales);
      scale.setSelectedIndex(5); // must change if scales are changed.
      controlPanel.add(scale);
      // Add the control panel and desktop (JDesktopPane) to the application content pane.
      getContentPane().add(controlPanel,BorderLayout.NORTH);
      getContentPane().add(desktop,BorderLayout.CENTER);
      // Set the closing operation so the application is finished.
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800,600); // adjust the frame size using specific dimensions.
      setVisible(true); // show the frame.
   }
   
   /**
    * This method will be called when the user enters a URL and press ENTER.
    */
   public void actionPerformed(ActionEvent e) {
      // Attempt to open the image as a URL.
      BufferedImage image = null;
      boolean loaded = true;
      try {
         image = ImageIO.read(new URL(url.getText()));
      }
      // Is the URL malformed?
      catch (MalformedURLException e1) {
         JOptionPane.showMessageDialog(this,"Malformed URL: "+url.getText(),
               "Malformed URL",JOptionPane.ERROR_MESSAGE);
         loaded = false;
      }
      // Could we read the URL contents?
      catch (IOException e1) {
         JOptionPane.showMessageDialog(this,"IOException: "+e1.getMessage(),
               "IOException",JOptionPane.ERROR_MESSAGE);
         loaded = false;
      }
      // Ok, if the image was loaded let's check whether it is decodable and then
      // create an internal frame to display it.
      if (loaded) {
         // Maybe we could read it but not decode it with ImageIO.
         if (image == null) {
            JOptionPane.showMessageDialog(this,"Cannot decode "+url.getText(),
                  "Cannot decode image",JOptionPane.ERROR_MESSAGE);
         } else // Everything is OK, let's create the internal frame and add it to the desktop.
         {
            float useScale = Float.parseFloat(scales[scale.getSelectedIndex()]);
            SimpleViewerInternalFrame iFrame =
                  new SimpleViewerInternalFrame(url.getText(),useScale,image);
            
            desktop.add(iFrame);
         }
      }
   }
   
   /**
    * The application entry point.
    * @param args the command-line arguments, which will be ignored.
    */
   public static void main(String[] args) {
      // Creates an instance of this class.
      new SimpleViewer();
   }
   
}
