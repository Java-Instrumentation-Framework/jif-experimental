// PSj: DisplayFrame
// Display frame for Asynchronous Video Camera image (real-time) Stream
package edu.mbl.jif.camera;

import  java.awt.*;
import  java.awt.event.*;
import  java.awt.image.*;
import  java.awt.color.*;
import  java.util.*;
import  javax.swing.*;
import  javax.swing.event.*;


//_______________________________________________________________________
public class ReplayStreamFrame extends JFrame
    implements ComponentListener {
  private static int width, height;
  private static int iWidth, iHeight;
  static BufferedImage bImage;
  static Graphics2D buffGraphics;               //for drawing ROI rectangle
  static WritableRaster wr;
  static videoPanel vPanel;
  static int frames = 0;
  static float framesPerSec = 0;
  long xtime = System.currentTimeMillis();
  Rectangle box;
  RescaleOp rop;

  public ReplayStreamFrame (int _w, int _h) {
    setTitle(this.getClass().getName());
    // framesPerSec
    setResizable(true);
    this.width = _w;
    this.height = _h;

	// keep the image size (in pixels) separate from the frame size
	iWidth = width;
    iHeight = height;

    // position and size the display frame
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	if (height < 40) height = 40;
    if (height > screenSize.height) {
      height = screenSize.height;
    }
	if (width < 40) width = 40;
    if (width > screenSize.width) {
      width = screenSize.width;
    }
    setLocation((int)10, //((screenSize.width  - width - 40)),
				(int)((screenSize.height - height - 40)));
    setSize(width, height + 40);
    setVisible(true);

	// upon window closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing (WindowEvent e) {
		// stop the player
		//CameraInterface.;
        dispose();
      }
    });

	// in order to select ...
	addComponentListener(this);

	// Create the BufferedImage and the WritableRaster
    if (QCamJNI.wideDepth) { // for 16-bit
	  rop = new RescaleOp(16f, 0f, null);
      bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_USHORT_GRAY);
      buffGraphics = bImage.createGraphics();
      wr = bImage.getRaster();
      wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
    }
    else {
      bImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_BYTE_GRAY);
	  buffGraphics = bImage.createGraphics();
      wr = bImage.getRaster();
      wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);
    }

    vPanel = new videoPanel(width, height);
    getContentPane().add(vPanel);
    vPanel.repaint();
    this.repaint();
  }

  // -------------------------------------------------
  public void componentResized (ComponentEvent e) {
    // resize the frame and the videoPanel
    width = this.getWidth();
    height = this.getHeight();
    vPanel.setSize(width, height);
  }

  public void componentHidden (ComponentEvent e) {}

  public void componentMoved (ComponentEvent e) {}

  public void componentShown (ComponentEvent e) {}

  // -------------------------------------------------
  // This is the method that is called as a 'callback' from the C code
  // when the next frame in the stream is ready for display

  public synchronized void playFrame8 (byte[] frame) {
      wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels8);

    //    wr.setDataElements(0, 0, iWidth, iHeight, QCamJNI.pixels16);
    //	  rop.filter(wr,wr);

    vPanel.paint(vPanel.getGraphics());

    frames++;
    if (frames > 50) displayFPS();
  }


  public void displayFPS() {
	// Measure Frames Per Second & display in frame title
	framesPerSec = 1000f*(float)frames/(float)(System.currentTimeMillis() - xtime);
	xtime = System.currentTimeMillis();
	frames = 0;
	setTitle("Player: " + String.valueOf((int)framesPerSec) + " fps, " +
			 String.valueOf(iWidth) + "x" + String.valueOf(iHeight) +
			 "pixels, " + ((QCamJNI.wideDepth) ? "16-bit" : "8-bit"));
  }


  // videoPanel =============================================================
  class videoPanel extends JPanel {

    public videoPanel (int w, int h) {
      setSize(w, h);
      setResizable(true);
      setBackground(Color.white);
      setVisible(true);
    }

//	public void paintComponent(Graphics g) {
//	  super.paintComponent(g);
//	}

    public void update (Graphics g) {
      paint(g);
    }

    public synchronized void paint (Graphics g) {
      // use of width & height allows resizing of the display panel
      buffGraphics.setColor(Color.white);
      buffGraphics.drawRect(box.x, box.y, box.width - 1, box.height - 1);
      g.drawImage(bImage, 0, 0, width, height, Color.white, null);
    }
  }
}




