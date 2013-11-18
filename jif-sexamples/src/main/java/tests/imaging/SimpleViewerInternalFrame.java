package tests.imaging;

/*
* Created on Jun 14, 2005
* @author Rafael Santos (rafael.santos@lac.inpe.br)
*
* Part of the Java Advanced Imaging Stuff site
* (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
*
* STATUS: Complete.
*
* Redistribution and usage conditions must be done under the
* Creative Commons license:
* English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
* Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
* More information on design and applications are on the projects' page
* (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
*/
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import com.sun.media.jai.widget.DisplayJAI;

/**
* This class represents a simple internal frame which can display a DisplayJAI component.
*/
public class SimpleViewerInternalFrame extends JInternalFrame
 {
 /**
  * The constructor for this class, which will set its UI and some parameters on the
  * JInternalFrame.
  * @param name the image name.
  * @param scale the scale that will be used to display.
  * @param image the image to be shown.
  */
 public SimpleViewerInternalFrame(String name,float scale,RenderedImage image)
   {
   // Call the superclass constructor with the image URL name, telling also that
   // this internal frame will be resizable, closable, maximizable and iconifiable.
   super(name,true,true,true,true);
   // Scales the original image
   ParameterBlock pb = new ParameterBlock();
   pb.addSource(image);
   pb.add(scale);
   pb.add(scale);
   pb.add(0.0F);
   pb.add(0.0F);
   pb.add(new InterpolationNearest());
   // Creates a new, scaled image and uses it on the DisplayJAI component
   PlanarImage scaledImage = JAI.create("scale", pb);
   getContentPane().add(new JScrollPane(new DisplayJAI(scaledImage)));
   // Set the internal frame's dimension.
   pack();
   setVisible(true);
   }

 }
