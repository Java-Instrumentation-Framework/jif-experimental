package tests.gui.states;

/*
 // lock in the size so it won't jitter when states change.
Dimension size = new Dimension ( 90, 30 );
setSize( size );
setMinimumSize( size );
setPreferredSize ( size );
setMaximumSize ( size );
 */


import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * State indicator. Displays different icon depending on internal
 * state 0..n.
 * Can create images it needs with:
 * URL url = XXXX.class.getResource( "images/ball.gif" );
 * Image ball = Toolkit.getDefaultToolkit().getImage( url );
 *
 * @author Roedy Green
 * @version 1.1
 * @since 2003-05-24
 */
public class StateIndicator extends JLabel {
   
   /**
    * constructor
    *
    * @param images array of images to display to represent the
    * various
    * states 0..n. Normally they would be the same size,
    * but
    * not necessarily.
    *
    * @param descriptions array of descriptions of the various states,
    * what they mean.
    */
//   public StateIndicator() {
//      Image[] imagesX = new Image[8];
//         imagesX[0] = getImage("white.gif");
//         imagesX[1] = getImage("gray.gif");
//         imagesX[2] = getImage("black.gif");
//         imagesX[3] = getImage("blue.gif");
//         imagesX[4] = getImage("green.gif");
//         imagesX[5] = getImage("red.gif");
//         imagesX[6] = getImage("purple.gif");
//         imagesX[7] = getImage("yellow.gif");
//         
//      String[] descriptionsX = {"1","1", "1", "1", "1", "1", "1", "1"};
//      org.pf.joi.Inspector.inspect(imagesX);
//      new StateIndicator(imagesX, descriptionsX);
//   }
   
   public StateIndicator() { // Image[] _images, String[] descriptions ) {
      
         images[0] = getImage("white.gif");
         images[1] = getImage("gray.gif");
         images[2] = getImage("black.gif");
         images[3] = getImage("blue.gif");
         images[4] = getImage("green.gif");
         images[5] = getImage("red.gif");
         images[6] = getImage("purple.gif");
         images[7] = getImage("yellow.gif");
      //this.images = _images;
      //this.descriptions = descriptions;
      icon = new ImageIcon();
      this.setIcon( icon );
      setState( 0 );
   }
   /**
    * Use one Icon, change image in it.
    */
   private ImageIcon icon;
   /**
    * Images corresponding to states 0..n
    */
   private Image[] images = new Image[8];
   /**
    * descriptions corresponding to states 0..n
    */
   private String[] descriptions  = {"1","2", "3", "4", "5", "6", "7", "8"};
   
   /**
    * current state 0..n, initially undefined to force an image load.
    */
   private int state = -1;
   
   Image getImage(String file) {
      Image img = null;
      try {
         InputStream in = getClass().getResourceAsStream(file);
         img = ImageIO.read(in);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      return img;
   }
   /**
    * get the current state
    *
    * @return current state
    */
   public int getState() {
      return state;
   }
   
   /**
    * set the state.
    *
    * @param state new state 0..n. Must have matching image and
    * description.
    */
   public void setState( int _state ) {
//      if ( _state < 0 || _state > images.length ) {
//         return;
//         //throw new IllegalArgumentException( "StateIndicator.setState out of bounds " + state );
//      }
         this.state = _state;
         icon.setImage( images [ state ] );
         icon.setDescription( descriptions[ state ] );
         this.setText( descriptions [ state ] );
         this.invalidate();
         this.repaint();
      
   }
} // end StateIndicator