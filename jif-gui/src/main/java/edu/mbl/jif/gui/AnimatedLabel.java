package edu.mbl.jif.gui;

import edu.mbl.jif.gui.test.FrameForTest;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Cursor;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JComponent;

class AnimatedLabel extends JLabel
    implements Runnable {
  protected Icon[] m_icons;
  protected int m_index = 0;
  protected boolean m_isRunning;
  String gifName;
  int numGifs;

  public AnimatedLabel() {
    this("Clock", 8);
  }

   AnimatedLabel(String gifName, int numGifs) {
    this.gifName = gifName;
    this.numGifs = numGifs;
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    m_icons = new Icon[numGifs];
    for (int k = 0; k < numGifs; k++) {
      m_icons[k] = new ImageIcon(".\\classes\\jfw\\icons\\" + gifName + k + ".gif");
    }
    setIcon(m_icons[0]);
    this.setPreferredSize(
        new Dimension(m_icons[0].getIconWidth(), m_icons[0].getIconHeight()));

    Thread tr = new Thread(this);
    tr.setPriority(Thread.MAX_PRIORITY);
    tr.start();
  }

  public void setRunning(boolean isRunning) {
    m_isRunning = isRunning;
  }

  public boolean getRunning() {
    return m_isRunning;
  }

  public void run() {
    while (true) {
      if (m_isRunning) {
        m_index++;
        if (m_index >= m_icons.length) {
          m_index = 0;
        }
        setIcon(m_icons[m_index]);
        Graphics g = getGraphics();
        m_icons[m_index].paintIcon(this, g, 0, 0);
      } else {
        if (m_index > 0) {
          m_index = 0;
          setIcon(m_icons[0]);
        }
      }
      try {
        Thread.sleep(333);
      } catch (Exception ex) {}
    }
  }

  public static void main(String[] args) {
    AnimatedLabel animClock;

    JPanel p = new JPanel();
    p.setPreferredSize(new Dimension(100, 100));
    animClock = new AnimatedLabel("Clock", 8);
    p.add((JComponent)animClock);
    FrameForTest ft = new FrameForTest(p);
    // start
    ft.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    animClock.setRunning(true);
    // done
//    animClock.setRunning(false);
//    ft.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


//----------------------------------------------------------------------
//  Another approach using an AnimatedGIF file as the source.
//
//    GifDecoder d = new GifDecoder();
//    System.out.println(
//        d.read(".\\classes\\jfw\\icons\\clockAnimation10.gif")
//        );
//    int n = d.getFrameCount();
//    ArrayList imgs = new ArrayList();
//    for (int i = 0; i < n; i++) {
//       BufferedImage frame = d.getFrame(i);  // frame i
//       imgs.add(frame);
//       int t = d.getDelay(i);  // display duration of frame in milliseconds
//       // do something with frame
//    }
//    new FrameImageDisplayTabbed(imgs);

  }

}
