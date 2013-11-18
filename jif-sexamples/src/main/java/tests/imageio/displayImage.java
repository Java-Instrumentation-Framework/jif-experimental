package tests.imageio;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.IIOException;
import javax.imageio.stream.ImageInputStream;


/**
 * displayImage.java -- displays an image or a series of images contained
 * at the URL provided on the command line. using javax.imageio.ImageIO
 */
public class displayImage
      extends JFrame
{
   private BufferedImage bi;
   private Insets insets;
   private ImageReader reader;
   private ImageInputStream iis;
   private URL url;
   private int imageIndex = 0;

   public displayImage (String inputURL) {

      try {
         url = new URL(inputURL);
      }
      catch (MalformedURLException mue) {
         System.err.print("MalformedURLException: ");
         System.err.println(mue.getMessage());
         System.exit(1);
      }

      try {
         iis = ImageIO.createImageInputStream(url.openStream());
      }
      catch (IOException ie) {
         System.err.println("IOException:  " + ie.getMessage());
         System.exit(1);
      }

      /*
       * get ImageReaders which can decode the given ImageInputStream
       */
      Iterator readers = ImageIO.getImageReaders(iis);

      /* if there is a set of appropriate ImageReaders, then take
       * the first one
       */
      if (readers.hasNext()) {
         reader = (ImageReader) readers.next();
         reader.setInput(iis, true);
      }
      if (reader == null) {
         System.err.print("No Available ImageReader can ");
         System.err.println("decode: " + url);
         System.exit(1);
      }

      addNotify();
      insets = getInsets();
      setVisible(true);
      showImages();
   }


   /**
    * This method iteratively displays all images in the given
    * ImageInputStream
    */
   private void showImages () {
      boolean stillReading = true;
      imageIndex = 0;

      /*
       * read and display all images
       */
      while (stillReading) {
         try {
            bi = reader.read(imageIndex);
            setSize(bi.getWidth() + insets.left + insets.right,
                  bi.getHeight() + insets.top + insets.bottom);
            imageIndex++;
            repaint();
         }
         catch (IOException ie) {
            System.err.println("IIOException " + ie.getMessage());
            System.exit(1);
         }
         catch (IndexOutOfBoundsException iobe) {
            // all of the images have been read
            stillReading = false;
         }
      }
   }


   /**
    * simple image paint routine which double buffers display
    */
   public void paint (Graphics g) {
      Image buffer;
      Graphics g2d;

      if (bi != null) {
         buffer = createImage(bi.getWidth(), bi.getHeight());
         g2d = buffer.getGraphics();

         /*
          * first clear viewing area
          * then draw image on buffered image
          * then draw buffered image on JFrame
          */
         g2d.clearRect(0, 0, bi.getWidth(), bi.getHeight());
         g2d.drawImage(bi, 0, 0, null);
         g.drawImage(buffer, insets.left, insets.top, null);
      }
   }


   public static void main (String[] args) {
      if (args.length == 0) {
//         new displayImage("file:" + edu.mbl.jif.Constants.testDataPath +
//               "images/PSCollagenDark.gif");
      } else {
         new displayImage(args[0]);
      }
   }
}
