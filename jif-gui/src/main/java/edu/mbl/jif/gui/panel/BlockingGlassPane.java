package edu.mbl.jif.gui.panel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * A Panel that can be blocked.
 * set an instance of this class as the glassPane
 * of your JFrame and call block(true|false) as needed.
 */
public class BlockingGlassPane extends JPanel {
  private int blockCount = 0;
  private BlockMouse blockMouse = new BlockMouse();
  private BlockKeys blockKeys = new BlockKeys();

  public BlockingGlassPane() {
    setVisible(false);
    setOpaque(false);
    addMouseListener(blockMouse);
  }

  /**
   * Start or end blocking.
   * @param block   should blocking be started or ended
   */
  public void block(boolean block) {
    if (block) {
      if (blockCount == 0) {
        setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        FocusManager.getCurrentManager()
            .addKeyEventDispatcher(blockKeys);
      }
      blockCount++;
    }
    else {
      blockCount--;
      if (blockCount == 0) {
        FocusManager.getCurrentManager()
            .removeKeyEventDispatcher(blockKeys);
        setCursor(Cursor.getDefaultCursor());
        setVisible(false);
      }
    }
  }

  /**
   * Test if this glasspane is blocked.
   *
   * @return    <code>true</code> if currently blocked
   */
  public boolean isBlocked() {
    return blockCount > 0;
  }

  /**
   * The key dispatcher to block the keys.
   */
  private class BlockKeys
      implements KeyEventDispatcher {
    public boolean dispatchKeyEvent(KeyEvent ev) {
      Component source = ev.getComponent();
      if ( (source != null)
          && SwingUtilities.isDescendingFrom(source, getParent())) {
        Toolkit.getDefaultToolkit()
            .beep();
        ev.consume();
        return true;
      }
      return false;
      //May want to add:
      // in BlockKeys.dispatchKeyEvent(...),
      // to avoid too may beeps (on press and release)
      // and to quiet Shift, Caps lock and Ctrl.
      //if (ev.getID() == KeyEvent.KEY_TYPED)
      //{
      //Toolkit.getDefaultToolkit().beep();
      //}
    }
  }

  /**
   * The mouse listener used to block the mouse.
   */
  private class BlockMouse extends MouseAdapter {
    public void mouseClicked(MouseEvent ev) {
      Toolkit.getDefaultToolkit()
          .beep();
    }
  }
}
