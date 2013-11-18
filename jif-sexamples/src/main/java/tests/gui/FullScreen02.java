/*
 * FullScreen02.java
 *
 * Created on October 4, 2007, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui;


/*FullScreen02.java
Copyright 2006, R.G.Baldwin

The purpose of this program is to demonstrate the use of
the Full-Screen Exclusive Mode API for simulation and
animation.

This program is an update to the earlier program named
Animate03 as described in the earlier lesson number
Java1466.  This program has been modernized to
make use of the Swing Timer class as an animation timer,
and also to use Active Rendering (as opposed to Passive
Rendering) in Full-Screen Exclusive Mode.  It has also been
modernized to use generics.

The program animates colored worms on a background image
in Full-Screen Exclusive mode.  See the discussion of
Active Rendering versus Passive Rendering in The Java
Tutorial at:

http://java.sun.com/docs/books/tutorial/extra/fullscreen
index.html

Using Active Rendering, the program does not call the
repaint method to ask the operating system to call the
update method.  Rather, the program invokes a method named
activeRenderer, which draws the animated sprites on the
screen without concern for sharing the screen with other
applications.  (There are no other applications on the
screen in full-screen mode.)

The program places an undecorated non-resizable JFrame
object on the screen in Full-Screen Exclusive Mode.

A JButton appears in the North location of the JFrame.
Clicking the button causes the program to exit the full-
screen mode, restore the original graphics mode, and
terminate.

A JPanel appears in the center of the JFrame object.
The colored worms are animated as sprites on the JPanel.
A background image is drawn on the JPanel at the beginning
of each animation cycle.  This image erases the sprites
that were previously there creating a clean slate for
drawing the sprites in their new positions.  If the
background image is eliminated, the worms turn into long
tubes because the old sprite images are never erased.

The background image that is used fits a 1024 x 768
screen in Full-Screen Exclusive mode.  If the screen is
a different size, a different size background image is
needed.

Tested using J2SE5.0 under WinXP.  J2SE 1.4 or later is
required for the setFullScreenWindow method of the
GraphicsDevice object.  J2SE 5.0 or later is required
to support generics.
**********************************************************/
import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


class FullScreen02 extends JFrame implements ActionListener {
    private GraphicsDevice graphicsDevice;
    private DisplayMode origDisplayMode;
    private JButton exitButton = new JButton("Exit Full-Screen Mode  Copyright 2006, R.G.Baldwin");

    //Constructor
    public FullScreen02(GraphicsDevice graphicsDevice) {
        //Save a reference to the graphics device as an
        // instance variable so that it can be used later to
        // exit the full-screen mode.
        this.graphicsDevice = graphicsDevice;

        setTitle("This title will be hidden (undecorated)");

        //Get and save a reference to the original display
        // mode as an instance variable so that it can be
        // restored later.
        origDisplayMode = graphicsDevice.getDisplayMode();

        //Register an action listener on the exitButton.
        exitButton.addActionListener(this);

        //Place the exitButton at the top of the JFrame    
        getContentPane().add(exitButton, BorderLayout.NORTH);

        //Create an animation panel and place it in the center
        // of the JFrame.
        getContentPane().add(new AnimationPanel());

        if (graphicsDevice.isFullScreenSupported()) {
            // Enter full-screen mode witn an undecorated,
            // non-resizable JFrame object.
            setUndecorated(true);
            setResizable(false);
            graphicsDevice.setFullScreenWindow(this);
            validate();
        } else {
            System.out.println("Full-screen mode not supported");
        } //end else    
    } //end constructor
      //-----------------------------------------------------//

    public static void main(String[] args) {
        //Get a list of available graphics devices.
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = graphicsEnvironment.getScreenDevices();

        //Construct a full-screen object using the first
        // graphics device in the array.  Ignore the others, if
        // there are any.
        new FullScreen02(devices[0]);
    } //end main
      //-----------------------------------------------------//

    //The following method is invoked when the used clicks
    // the exitButton
    public void actionPerformed(ActionEvent evt) {
        //Restore the original display mode
        graphicsDevice.setDisplayMode(origDisplayMode);
        //Terminate the program
        System.exit(0);
    } //end actionPerformed
      //-----------------------------------------------------//
} //end class FullScreen02
//=======================================================//


//This is a convenience class that makes it a little
// easier to deal with the background image.  Note that
// much of the impetus for this class went away when the
// animation program named Animate 03 was converted to
// full-screen mode. Several convenience methods having to
// do with the size of the background image were eliminated
// at that time.
class BackgroundImage {
    private Image image;
    private Component component;

    public BackgroundImage(Component component, Image image) {
        this.component = component;
        this.image = image;
    } //end construtor

    public void drawBackgroundImage(Graphics g) {
        //Note that component is an ImageObserver in the
        // following statement.
        g.drawImage(image, 0, 0, component);
    } //end drawBackgroundImage()
} //end class BackgroundImage
//=======================================================//


//An object of this class manages a collection of animated
// sprites stored in an ArrayList object.  (A Vector
// object was used in the earlier version named Animate03.)
// Note that this class also underwent some modernization
// with respect to the use of generics as released in
// J2SE 5.0. 
class SpriteManager {
    private ArrayList<Sprite> theSprites = new ArrayList<Sprite>();
    private BackgroundImage backgroundImage;
    private int panelWidth;
    private int panelHeight;

    //-----------------------------------------------------//
    public SpriteManager(BackgroundImage backgroundImage, int panelWidth, int panelHeight) {
        this.backgroundImage = backgroundImage;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    } //end constructor
      //-----------------------------------------------------//

    public Point getEmptyPosition(Dimension spriteSize) {
        Rectangle trialSpaceOccupied = new Rectangle(0, 0, spriteSize.width, spriteSize.height);
        Random rand = new Random(System.currentTimeMillis());
        boolean empty = false;
        int numTries = 0;

        //Search for an empty position.  If an empty position
        // can't be found after 100 tries, give up and allow
        // two sprites to occupy the same initial location.
        while (!empty && (numTries++ < 100)) {
            // Get a trial position
            trialSpaceOccupied.x = Math.abs(rand.nextInt() % panelWidth);
            trialSpaceOccupied.y = Math.abs(rand.nextInt() % panelHeight);

            // Iterate through existing sprites, checking if
            // position is empty
            boolean collision = false;
            for (int cnt = 0; cnt < theSprites.size(); cnt++) {
                Rectangle testSpaceOccupied = theSprites.get(cnt).getSpaceOccupied();

                if (trialSpaceOccupied.intersects(testSpaceOccupied)) {
                    collision = true;
                } //end if
            } //end for loop
            empty = !collision;
        } //end while loop

        return new Point(trialSpaceOccupied.x, trialSpaceOccupied.y);
    } //end getEmptyPosition()
      //-----------------------------------------------------//

    //This method updates the state information for each of
    // the sprites.
    public void updateSprites() {
        Sprite sprite;

        //Iterate through sprite list
        for (int cnt = 0; cnt < theSprites.size(); cnt++) {
            sprite = theSprites.get(cnt);
            //Update a sprite's position
            sprite.updatePosition();

            //Test for collision. Positive 
            // result indicates a collision
            int hitIndex = testForCollision(sprite);
            if (hitIndex >= 0) {
                //A collision has occurred.  Make the two sprites
                // involved in the collision bounce off the other
                // sprite.
                bounceOffSprite(cnt, hitIndex);
            } //end if
        } //end for loop
    } //end updateSprites
      //-----------------------------------------------------//

    //This method tests for a collision between two sprites.
    private int testForCollision(Sprite testSprite) {
        //Check for collision with other sprites
        Sprite sprite;
        for (int cnt = 0; cnt < theSprites.size(); cnt++) {
            sprite = theSprites.get(cnt);
            if (sprite == testSprite) {
                //Don't check self
                continue;
            }

            //Invoke testCollision method of Sprite class to
            // perform the actual test.
            if (testSprite.testCollision(sprite)) {
                //Return index of colliding sprite
                return cnt;
            }
        } //end for loop

        return -1; //No collision detected
    } //end testForCollision()
      //-----------------------------------------------------//

    private void bounceOffSprite(int spriteAindex, int spriteBindex) {
        //Swap motion vectors for bounce algorithm
        Sprite spriteA = theSprites.get(spriteAindex);
        Sprite spriteB = theSprites.get(spriteBindex);
        Point swap = spriteA.getMotionVector();
        spriteA.setMotionVector(spriteB.getMotionVector());
        spriteB.setMotionVector(swap);
    } //end bounceOffSprite()
      //-----------------------------------------------------//

    //This method draws the sprites in their new locations
    // on the animation panel.
    public void drawScene(Graphics g) {
        //Draw the background and erase sprites from animation
        // panel.
        //Disable the following statement for an interesting
        // effect.  This will turn the short worms into tube
        // worms that increase in length forever.
        backgroundImage.drawBackgroundImage(g);

        //Iterate through sprites, drawing each sprite
        for (int cnt = 0; cnt < theSprites.size(); cnt++)
            theSprites.get(cnt).drawSpriteImage(g);
    } //end drawScene()
      //-----------------------------------------------------//

    public void addSprite(Sprite sprite) {
        //Add a new sprite to the ArrayList object.
        theSprites.add(sprite);
    } //end addSprite()
      //-----------------------------------------------------//
} //end class SpriteManager
//=======================================================//


//The class from which Sprite objects are instantiated.
class Sprite {
    private Component component;
    private Image[] image;
    private Rectangle spaceOccupied;
    private Point motionVector;
    private Rectangle bounds;
    private Random rand;
    private int frameNumber;

    //The following collection contains a reference to a
    // History object containing state information about
    // each spherical segment that makes up a worm.
    private ArrayList<History> tailData = new ArrayList<History>();
    private int wormLength;

    //-----------------------------------------------------//

    //Constructor
    public Sprite(Component component, Image[] image, Point position, Point motionVector) {
        //Seed a random number generator for this sprite with
        // the sprite position.
        rand = new Random(position.x);
        wormLength = Math.abs(rand.nextInt() % 20);
        this.component = component;
        this.image = image;
        this.spaceOccupied = new Rectangle(position.x, position.y, image[0].getWidth(component),
                image[0].getHeight(component));
        this.motionVector = motionVector;
        //Compute edges of usable graphics area.  Because we
        // are drawing on a JPanel, we don't need to worry
        // about insets for borders, banner, etc. This is much
        // simpler than in the earlier program named Animate03.
        bounds = new Rectangle(0, 0, component.getSize().width, component.getSize().height);
    } //end constructor
      //-----------------------------------------------------//

    public Rectangle getSpaceOccupied() {
        return spaceOccupied;
    } //end getSpaceOccupied()
      //-----------------------------------------------------//

    private void setSpaceOccupied(Point position) {
        spaceOccupied.setLocation(position.x, position.y);
    } //setSpaceOccupied()
      //-----------------------------------------------------//

    public Point getMotionVector() {
        return motionVector;
    } //end getMotionVector()
      //-----------------------------------------------------//

    public void setMotionVector(Point motionVector) {
        this.motionVector = motionVector;
    } //end setMotionVector()
      //-----------------------------------------------------//

    private void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    } //end setBounds()
      //-----------------------------------------------------//

    public void updatePosition() {
        Point position = new Point(spaceOccupied.x, spaceOccupied.y);

        //Insert random behavior.  During each update, a sprite
        // has about one chance in 10 of making a small random
        // change to its motionVector.  When a change occurs,
        // the motionVector coordinate values are forced to
        // fall between -7 and 7.
        if ((rand.nextInt() % 10) == 0) {
            Point randomOffset = new Point(rand.nextInt() % 3, rand.nextInt() % 3);
            motionVector.x += randomOffset.x;
            if (motionVector.x >= 7) {
                motionVector.x -= 7;
            }
            if (motionVector.x <= -7) {
                motionVector.x += 7;
            }
            motionVector.y += randomOffset.y;
            if (motionVector.y >= 7) {
                motionVector.y -= 7;
            }
            if (motionVector.y <= -7) {
                motionVector.y += 7;
            }
        } //end if

        //Make the move
        position.translate(motionVector.x, motionVector.y);

        //Bounce off the walls
        boolean bounceRequired = false;
        Point tempMotionVector = new Point(motionVector.x, motionVector.y);

        //Handle walls in x-dimension
        if (position.x < bounds.x) {
            bounceRequired = true;
            position.x = bounds.x;
            //reverse direction in x
            tempMotionVector.x = -tempMotionVector.x;
        } else if ((position.x + spaceOccupied.width) > (bounds.x + bounds.width)) {
            bounceRequired = true;
            position.x = (bounds.x + bounds.width) - spaceOccupied.width;
            //Reverse direction
            tempMotionVector.x = -tempMotionVector.x;
        } //end else if

        //Handle walls in y-dimension
        if (position.y < bounds.y) {
            bounceRequired = true;
            position.y = bounds.y;
            tempMotionVector.y = -tempMotionVector.y;
        } else if ((position.y + spaceOccupied.height) > (bounds.y + bounds.height)) {
            bounceRequired = true;
            position.y = (bounds.y + bounds.height) - spaceOccupied.height;
            tempMotionVector.y = -tempMotionVector.y;
        } //end else if

        //Save new motionVector
        if (bounceRequired) {
            setMotionVector(tempMotionVector);
        }

        //Update spaceOccupied
        setSpaceOccupied(position);
    } //end updatePosition()
      //-----------------------------------------------------//

    //This method draws each worm, one segment at a time.
    // A new segment is added for the head each time this
    // method is called.  When the worm has reached its
    // specified length, the oldest segment is discarded 
    // each time this method is called.
    public void drawSpriteImage(Graphics g) {
        frameNumber = Math.abs(rand.nextInt() % 6);
        //Add a new head to the worm.
        tailData.add(new History(image[frameNumber], spaceOccupied.x, spaceOccupied.y));

        Iterator<History> iterator = tailData.iterator();
        int cnt = 0;
        while (iterator.hasNext()) {
            //Draw each segment of the worm.
            History history = iterator.next();

            //Discard the first segment in the list if the worm
            // has met its length requirement.  This will keep
            // the length of the worm constant.
            if ((tailData.size() > wormLength) && (cnt == 0)) {
                iterator.remove();
            } //end if

            g.drawImage(history.image, history.x, history.y, component);
            cnt++;
        } //end while
    } //end drawSpriteImage()
      //-----------------------------------------------------//

    public boolean testCollision(Sprite testSprite) {
        // Check for collision with another sprite
        if (testSprite != this) {
            return spaceOccupied.intersects(testSprite.getSpaceOccupied());
        } //end if

        return false;
    } //end testCollision
} //end Sprite class
//=======================================================//


//An object of this class is used to contain state 
// information for each segment of each worm.
class History {
    Image image;
    int x;
    int y;

    public History(Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    } //end constructor
} //end class History
//=======================================================//


//The animation is played out by drawing images on an
// object of this class.  The object is placed in the 
// center of an undecorated non-resizable JFrame object
// that is displayed in Full-Screen Exclusive Mode.
//Most of the code in this class was applied to the JFrame
// object in the earlier program named Animate03, because
// in that case, the images were drawn directly on the
// JFrame object.
class AnimationPanel extends JPanel implements Runnable {
    private Image offScreenImage;
    private Image backGroundImage;
    private Image[] gifImages = new Image[6];
    private Graphics offScreenGraphicsCtx;
    private Thread animationThread;
    private MediaTracker mediaTracker;
    private SpriteManager spriteManager;

    //Animation display rate, 12fps
    private int animationDelay = 83;
    private Random rand = new Random(System.currentTimeMillis());
    private javax.swing.Timer animationTimer;

    //-----------------------------------------------------//
    public AnimationPanel() { //constructor
                              // Load and track the images
        mediaTracker = new MediaTracker(this);

        //Get and track the background image.  Note that this
        // background image is appropriate only for a screen
        // size of 1024x768.  For a different screen size, you
        // will need to use a different background image file.
        backGroundImage = Toolkit.getDefaultToolkit().getImage("background03.gif");

        mediaTracker.addImage(backGroundImage, 0);

        //Note that it should be possible to use the following
        // code to load a smaller version of the image and
        // have it scaled to the required size as it is loaded,
        // but this doesn't seem to work properly.  (I probably
        // just don't understand how it is supposed to work.)
        //    backGroundImage = Toolkit.getDefaultToolkit().
        //                            getImage("background02.gif");
        //    mediaTracker.addImage(backGroundImage,0,1024,768);

        //Get and track 6 images to use for sprites
        gifImages[0] = Toolkit.getDefaultToolkit().getImage("redball.gif");
        mediaTracker.addImage(gifImages[0], 0);
        gifImages[1] = Toolkit.getDefaultToolkit().getImage("greenball.gif");
        mediaTracker.addImage(gifImages[1], 0);
        gifImages[2] = Toolkit.getDefaultToolkit().getImage("blueball.gif");
        mediaTracker.addImage(gifImages[2], 0);
        gifImages[3] = Toolkit.getDefaultToolkit().getImage("yellowball.gif");
        mediaTracker.addImage(gifImages[3], 0);
        gifImages[4] = Toolkit.getDefaultToolkit().getImage("purpleball.gif");
        mediaTracker.addImage(gifImages[4], 0);
        gifImages[5] = Toolkit.getDefaultToolkit().getImage("orangeball.gif");
        mediaTracker.addImage(gifImages[5], 0);

        //Block and wait for all images to be loaded
        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            System.out.println(e);
        } //end catch

        //Instantiate an animation thread and start it running.
        animationThread = new Thread(this);
        animationThread.start();

        //Instantiate a Timer object and register an anonymous
        // ActionListener on it using the peculiar listener
        // registration process for a Timer object.  The 
        // Timer will be started in the run method of the
        // animationThread.
        animationTimer = new javax.swing.Timer(animationDelay,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Update the sprites
                        spriteManager.updateSprites();
                        //Update the animation panel on the screen
                        activeRenderer(getGraphics());
                    } //end actionPerformed
                } //end new ActionListener
            ); //end new javax.swing.Timer
    } //end constructor
      //-----------------------------------------------------//

    //This method is used to draw directly on the screen
    // using Active Rendering, without invoking the repaint
    // method.
    private void activeRenderer(Graphics g) {
        //Create the offscreen graphics context
        if (offScreenGraphicsCtx == null) {
            offScreenImage = createImage(getSize().width, getSize().height);
            offScreenGraphicsCtx = offScreenImage.getGraphics();
        } //end if

        // Draw the sprites offscreen
        spriteManager.drawScene(offScreenGraphicsCtx);

        // Draw the scene onto the screen
        if (offScreenImage != null) {
            g.drawImage(offScreenImage, 0, 0, this);
        } //end if
    } //end activeRenderer method
      //-----------------------------------------------------//

    public void run() {
        //Create and add sprites to the sprite manager

        //Delay until the width and the height of the
        // AnimationPanel stabilize in Full-Screen mode.  There
        // is probably a better way to do this, but I don't
        // know what it is.  In any event, this happens only
        // once while the program is getting started and
        // switching to Full-Screen Exclusive Mode.
        while (getWidth() == 0) {
            System.out.print(".");
        } //end while loop
        while (getHeight() == 0) {
            System.out.print(".");
        } //end while loop

        spriteManager = new SpriteManager(new BackgroundImage(this, backGroundImage), getWidth(),
                getHeight());
        //Create 200 sprites from 6 gif files.
        for (int cnt = 0; cnt < 200; cnt++) {
            Point position = spriteManager.getEmptyPosition(new Dimension(gifImages[0].getWidth(
                            this), gifImages[0].getHeight(this)));
            spriteManager.addSprite(makeSprite(position, cnt % 6));
        } //end for loop

        animationTimer.start(); //Start the animation timer
    } //end run method 
      //-----------------------------------------------------//

    private Sprite makeSprite(Point position, int imageIndex) {
        return new Sprite(this, gifImages, position,
            new Point(rand.nextInt() % 5, rand.nextInt() % 5));
    } //end makeSprite()
      //-----------------------------------------------------//
} //end AnimationPanel
