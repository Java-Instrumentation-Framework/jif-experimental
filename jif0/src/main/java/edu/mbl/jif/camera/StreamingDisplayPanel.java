package edu.mbl.jif.camera;

import edu.mbl.jif.imaging.stream.StreamSource;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageProducer;
import javax.swing.JPanel;


public class StreamingDisplayPanel
      extends JPanel //?? implements ImageObserver
{
   Image img;
   ImageProducer source;
   StreamSource sourceStream;
   int w, h;
   int[] pixels;
   String pixValue = "";
   int updatePeriod = 10;
   int frames = 0;

   StreamingDisplayPanel (StreamSource sourceStream, int w, int h) {
      this.sourceStream = sourceStream;
      source = sourceStream.mis;
      this.h = h;
      this.w = w;
      img = createImage(source);
      pixels = new int[img.getWidth(this) * img.getHeight(this)];
      setSize(w, h);
   }

//   public void update (Graphics g) {
//      paint(g);
//   }

   public void paintComponent (Graphics g) {
      g.drawImage(img, 0, 0, getSize().width, getSize().height, this);
//      frames++;
//      if (frames % updatePeriod == 0) {
//         PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
//         try {
//            pg.grabPixels();
//         }
//         catch (InterruptedException e) {
//            System.err.println("interrupted waiting for pixels!");
//            return;
//         }
//         int v = pixels[0] & 0xFF;
//         pixValue = "> " + v;
//      }
//      g.setColor(Color.black);
//      g.drawString(pixValue, 20, 20);
   }
}
