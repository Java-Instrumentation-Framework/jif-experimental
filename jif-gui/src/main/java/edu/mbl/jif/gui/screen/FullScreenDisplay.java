package edu.mbl.jif.gui.screen;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


/*
 *  window.setExtendedState(Frame.MAXIMIZED_BOTH); //maximise window
    window.setUndecorated(true); //remove decorations e.g. x in top right
    // And to make the window always on top use(To stop people using other running programs);
    window.setAlwaysOnTop(true);
 */

public class FullScreenDisplay
      extends JFrame implements KeyListener, MouseListener
{
   //
   private static Color[] COLORS =
         new Color[] {
         Color.red,
         Color.blue,
         Color.green,
         Color.white,
         Color.black,
         Color.yellow,
         Color.gray,
         Color.cyan,
         Color.pink,
         Color.lightGray,
         Color.magenta,
         Color.orange,
         Color.darkGray
   };
   private static DisplayMode[] BEST_DISPLAY_MODES =
         new DisplayMode[] {
         new DisplayMode(1280, 1024, 32, 0)
   };
   private static GraphicsDevice device;
   private static Image img;

   //    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(gui);
   //
   public FullScreenDisplay () {
      super("");
      //    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      // Suspend anything that may interfere... eg. Camera.setDisplayOff();
      getContentPane().setBackground(Color.black);
      setBackground(Color.black);
      addMouseListener(this);
      addKeyListener(this);
      GraphicsEnvironment env =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
      device = env.getDefaultScreenDevice();
      try {
         GraphicsConfiguration gc = device.getDefaultConfiguration();
         setUndecorated(true);
         setIgnoreRepaint(false);
         setResizable(false);
         device.setFullScreenWindow(this);
         //device.setDisplayMode(new DisplayMode(1280, 1024, 32, 75));
      }
      catch (Exception e) {
         e.printStackTrace();
         device.setFullScreenWindow(null);
         // Resume anything suspended...eg.
         //if (Camera.isOpen) {
         //   Camera.openDisplayFrame();
         //}
      }
      finally {}
   }


   public void display (Image _img) {
      img = _img;
      Graphics g = this.getGraphics();
      g.drawImage(img, 0, 0,
                  (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                  (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
                  0, 0,
                  img.getWidth(null), img.getHeight(null), null);
      g.dispose();
   }


   public void update () {
      if (img != null) {
         Graphics g = this.getGraphics();
         paint(g);
         g.dispose();
      }
   }


   public void paint (Graphics g) {
      int scrnW = (int) Toolkit.getDefaultToolkit()
                  .getScreenSize()
                  .getWidth();
      int scrnH =
            (int) Toolkit.getDefaultToolkit()
            .getScreenSize()
            .getHeight();
      int imgW = img.getWidth(null);
      int imgH = img.getHeight(null);
      int x, y, w, h;

      if (true) {
      //if (Prefs.usr.getBoolean("fullScreenZoom", true)) {

         x = 0;
         y = 0;
         w = scrnW;
         h = scrnH;
      } else {
         x = (scrnW - imgW) / 2;
         y = (scrnH - imgH) / 2;
         w = ((x + imgW) < scrnW) ? x + imgW : scrnW;
         h = ((y + imgH) < scrnH) ? y + imgH : scrnH;
      }
//      int x = 0;
//      int y = 0;
//      int w = 0;
//      int h = 0;
//      if ((imgH / imgW) >= 1) {
//         w = scrnW;
//         x = 0;
//         h = (scrnW / imgW) * imgH;
//         y = (scrnH - h) / 2;
//      } else {
//         h = scrnH;
//         y = 0;
//         w = (scrnH / imgH) * imgW;
//         x = (scrnW - w) / 2;
//      }
      if (x < 0) {
         x = 0;
      }
      if (y < 0) {
         y = 0;
      }
      g.drawImage(img, x, y, w, h, 0, 0, imgW, imgH, null);
      //      g.drawImage(img,
      //         0, 0, scrnW, scrnH,
      //         0, 0, imgW, imgH,
      //         null);
      //g.drawString("Press key to exit full screen view.", 100,100);
   }


   public void exit () {
      device.setFullScreenWindow(null);
      this.setVisible(false);
      //psj.PSj.deskTopFrame.repaint();
      //Camera.setDisplayOn();
      img = null;
      device = null;
      this.dispose();
   }


   private static DisplayMode getBestDisplayMode (GraphicsDevice device) {
      for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
         DisplayMode[] modes = device.getDisplayModes();
         for (int i = 0; i < modes.length; i++) {
            if ((modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth())
                && (modes[i].getHeight() == BEST_DISPLAY_MODES[x]
                    .getHeight())
                && (modes[i].getBitDepth() == BEST_DISPLAY_MODES[x]
                    .getBitDepth())) {
               return BEST_DISPLAY_MODES[x];
            }
         }
      }
      return null;
   }


   public static void chooseBestDisplayMode (GraphicsDevice device) {
      DisplayMode best = getBestDisplayMode(device);
      if (best != null) {
         device.setDisplayMode(best);
      }
   }


   public static void main (String[] args) {
   JFrame f = new JFrame();
   f.setUndecorated(true);
      GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
            setFullScreenWindow(f);

   }


   //////////////////////////////////////////////////////////////////////
   // Key Events
   public void keyTyped (KeyEvent e) {
      exit();
   }


   public void keyPressed (KeyEvent e) {}


   public void keyReleased (KeyEvent e) {}


   //////////////////////////////////////////////////////////////////////
   // Mouse Events
   public void mousePressed (MouseEvent e) {}


   public void mouseReleased (MouseEvent e) {}


   public void mouseEntered (MouseEvent e) {}


   public void mouseExited (MouseEvent e) {}


   public void mouseClicked (MouseEvent e) {
      exit();
   }
}
