package edu.mbl.jif.gui.internal;


/* -*- mode: JDE; c-basic-offset: 2; indent-tabs-mode: nil -*-
 *
 * $Id: DetachableRootPane.java,v 1.6 2005/06/20 22:40:48 ljnelson Exp $
 *
 * Copyright (c) 2003, 2005 Laird Jarrett Nelson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * The original copy of this license is available at
 * http://www.opensource.org/license/mit-license.html.
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * A {@link JRootPane} that is capable of being detached from a {@link
 * JInternalFrame} into a self-created {@link JFrame}.  The visual effect is one
 * of "popping" the {@link JInternalFrame} "off" of its enclosing {@link
 * JDesktopPane} and onto the user's desktop.  The {@link JFrame} it detaches
 * "into" as a part of this process, when closed, appears to "sink" back into
 * the {@link JDesktopPane} as a {@link JInternalFrame}.  The whole effect is
 * considerably less complicated than this description might lead you to
 * believe.
 *
 * @author     <a href="mailto:ljnelson@gmail.com">Laird Nelson</a>
 * @version    $Revision: 1.6 $ $Date: 2005/06/20 22:40:48 $
 * @since      May 27, 2005
 */
public class DetachableRootPane extends JRootPane {

  /**
   * The name of a {@linkplain JComponent#getClientProperty(Object) client
   * property} that indicates that the {@link JInternalFrame} this {@link
   * DetachableRootPane} is "leaving" was resizable.
   */
  private static final String INTERNAL_FRAME_RESIZABLE = "internalFrameResizable";
  /**
   * The name of a {@linkplain JComponent#getClientProperty(Object) client
   * property} that indicates that the {@link JInternalFrame} this {@link
   * DetachableRootPane} is "leaving" was closable.
   */
  private static final String INTERNAL_FRAME_CLOSABLE = "internalFrameClosable";
  /**
   * The name of a {@linkplain JComponent#getClientProperty(Object) client
   * property} that indicates that the {@link JInternalFrame} this {@link
   * DetachableRootPane} is "leaving" was maximizable.
   */
  private static final String INTERNAL_FRAME_MAXIMIZABLE = "internalFrameMaximizable";
  /**
   * The name of a {@linkplain JComponent#getClientProperty(Object) client
   * property} that indicates that the {@link JInternalFrame} this {@link
   * DetachableRootPane} is "leaving" was iconifiable.
   */
  private static final String INTERNAL_FRAME_ICONIFIABLE = "internalFrameIconifiable";
  /**
   * A {@link PropertyChangeListener} that monitors a {@link JInternalFrame} for
   * changes in its structural resizability and sets the {@link
   * #parentIsResizable} field appropriately.  This field is assigned a
   * non-<code>null</code> value in the {@link #addNotify()} method, and is
   * reset to <code>null</code> in most cases by the {@link #removeNotify()}
   * method.
   */
  private PropertyChangeListener pcl;
  /**
   * Indicates whether the "outgoing" parent of this {@link
   * DetachableRootPane}&mdash;usually a {@link JInternalFrame}&mdash;was
   * structurally resizable.
   */
  private boolean parentIsResizable;

  /**
   * Creates a {@link DetachableRootPane}.  Typically this will be called in an
   * overridden {@link JInternalFrame#createRootPane()} method.  As a side
   * effect, installs an {@link AbstractAction} named "<code>detach</code>" in
   * the {@linkplain JComponent#getActionMap() associated
   * <code>ActionMap</code>} and associates it with the keystroke
   * "<code>F2</code>" (in the default {@link InputMap}).
   */
  public DetachableRootPane() {
    super();
    final ActionMap actionMap = this.getActionMap();
    assert actionMap != null;
    actionMap.put("detach", new AbstractAction("Detach") {

      public final void actionPerformed(final ActionEvent event) {
        detach();
      }
      });
    final InputMap inputMap = this.getInputMap();
    assert inputMap != null;
    inputMap.put(KeyStroke.getKeyStroke("F2"), "detach");
  }

  /**
   * Calls the {@linkplain Component#addNotify() superclass implementation} and
   * then ensures that if the parent this {@link DetachableRootPane} was just
   * added to is either a {@link JInternalFrame} or a {@link JFrame} its
   * resizability is monitored via a {@link PropertyChangeListener}.
   *
   * @see        #removeNotify()
   */
  public void addNotify() {
    super.addNotify();

    final Container parent = this.getParent();

    boolean addPropertyChangeListener = true;

    if (parent instanceof JInternalFrame) {
      this.parentIsResizable = ((JInternalFrame) parent).isResizable();
    } else if (parent instanceof JFrame) {
      this.parentIsResizable = ((JFrame) parent).isResizable();
    } else {
      addPropertyChangeListener = false;
    }

    if (addPropertyChangeListener) {
      this.pcl = new PropertyChangeListener() {

        public final void propertyChange(final PropertyChangeEvent event) {

          // Monitor the "resizable" property of the parent for changes.
          if (event != null && "resizable".equals(event.getPropertyName())) {
            final Boolean value = (Boolean) event.getNewValue();
            parentIsResizable = value != null && value.booleanValue();
          }

        }
        };
      parent.addPropertyChangeListener("resizable", this.pcl);
    }
  }

  /**
   * Ensures that the {@link PropertyChangeListener} that might have been
   * installed on the {@linkplain Component#getParent() parent of this
   * <code>DetachableRootPane</code>} is removed.
   */
  public void removeNotify() {
    if (this.pcl != null) {
      final Container parent = this.getParent();
      assert parent != null;
      parent.removePropertyChangeListener("resizable", this.pcl);
    }
    super.removeNotify();
  }

  /**
   * Detaches this {@link DetachableRootPane} from its current parent
   * frame&mdash;provided that parent is either a {@link JInternalFrame} or a
   * {@link JFrame}&mdash;and installs it in a new frame of the appropriate
   * type.
   *
   * <p>If the current parent of this {@link DetachableRootPane} is a
   * non-iconified {@link JInternalFrame}, then the following visual behavior
   * occurs:
   * <ul>
   *
   * <li>The {@link DetachableRootPane} {@linkplain
   * JComponent#putClientProperty(Object, Object) sets a client property} named
   * "<code>desktopPane</code>" on itself, set to the value of the {@link
   * JInternalFrame}'s associated {@link JDesktopPane}</li>
   *
   * <li>The {@link JInternalFrame} is closed and disposed</li>
   *
   * <li>A new {@link JFrame} with the same title appears in its place,
   * retaining its size, but not its maximized status</li>
   *
   * <li>If that {@link JFrame} is closed, then a new {@link JInternalFrame} is
   * created directly under it, where possible, on the {@link JDesktopPane}
   * previously installed as a {@linkplain JComponent#getClientProperty(Object)
   * client property}</li>
   *
   * </ul></p>
   *
   * <p>If the current parent of this {@link DetachableRootPane} is a
   * non-iconified {@link JFrame}, then the following visual behavior occurs:
   * <ul>
   * <li>The {@link JFrame} is closed and disposed</li>
   *
   * <li>If the "<tt>desktopPane</tt>" {@linkplain
   * JComponent#getClientProperty(Object) client property} is set and is equal
   * to a non-<code>null</code> {@link JDesktopPane} instance, then a new {@link
   * JInternalFrame} is created, added to that {@link JDesktopPane} in the
   * location that the outgoing {@link JFrame} last had (see {@link
   * SwingUtilities#convertPointFromScreen(Point, Component)}); otherwise
   * nothing further happens</li>
   * </ul>
   */
  public void detach() {

    final Component parent = this.getParent();
    if (parent instanceof JInternalFrame) {
      // Leaving JInternalFrame; going into JFrame

      final JInternalFrame internalFrame = (JInternalFrame) parent;

      final Rectangle bounds = internalFrame.getBounds();
      assert bounds != null;

      final Point point = internalFrame.getLocationOnScreen();
      assert point != null;

      bounds.x = point.x;
      bounds.y = point.y;

      final JDesktopPane pane = internalFrame.getDesktopPane();
      if (pane != null) {
        pane.remove(internalFrame);
        this.putClientProperty("desktop", pane);
      }

      // Store the structural state of the "outgoing" JInternalFrame.  This is
      // so that we can create another JInternalFrame, if necessary, that will
      // look and behave like the one that we are currently detaching from.
      this.putClientProperty(INTERNAL_FRAME_RESIZABLE, new Boolean(this.parentIsResizable));
      this.putClientProperty(INTERNAL_FRAME_CLOSABLE, Boolean.valueOf(internalFrame.isClosable()));
      this.putClientProperty(INTERNAL_FRAME_MAXIMIZABLE, Boolean.valueOf(internalFrame.isMaximizable()));
      this.putClientProperty(INTERNAL_FRAME_ICONIFIABLE, Boolean.valueOf(internalFrame.isIconifiable()));

      internalFrame.setVisible(false);

      // Create a new JFrame that has us as its root pane, and that calls this
      // method (detach()) when closed.
      final JFrame frame = new JFrame(internalFrame.getTitle()) {

        protected final void frameInit() {
          super.frameInit();

          // Ensure that however the window is closed, it actually causes this
          // detach() method to be fired instead.
          this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
          this.addWindowListener(new WindowAdapter() {

            public final void windowClosing(final WindowEvent event) {
              detach();
            }
          });
        }

        protected final JRootPane createRootPane() {
          // Ensure that this DetachableRootPane instance is the root pane for
          // any JFrames it creates.
          return DetachableRootPane.this;
        }
        };

      frame.setBounds(bounds);
      frame.setVisible(true);

    } else if (parent instanceof JFrame) {
      // Leaving JFrame; going into JInternalFrame

      final JFrame frame = (JFrame) parent;
      final String title = frame.getTitle();
      final Rectangle bounds = frame.getBounds();
      final JDesktopPane pane = (JDesktopPane) this.getClientProperty("desktop");

      frame.dispose();

      if (pane != null) {

        final JInternalFrame internalFrame =
                new JInternalFrame(title,
                ((Boolean) this.getClientProperty(INTERNAL_FRAME_RESIZABLE)).booleanValue(),
                ((Boolean) this.getClientProperty(INTERNAL_FRAME_CLOSABLE)).booleanValue(),
                ((Boolean) this.getClientProperty(INTERNAL_FRAME_MAXIMIZABLE)).booleanValue(),
                ((Boolean) this.getClientProperty(INTERNAL_FRAME_ICONIFIABLE)).booleanValue()) {

                  protected final JRootPane createRootPane() {
                    return DetachableRootPane.this;
                  }
                };

        pane.add(internalFrame);

        final Point point = new Point(bounds.x, bounds.y);
        SwingUtilities.convertPointFromScreen(point, pane);
        bounds.x = point.x;
        bounds.y = point.y;
        internalFrame.setBounds(bounds);

        internalFrame.setVisible(true);

      }
    }
  }

  /**
   * Convenience method that creates a {@link JFrame} with a {@link
   * DetachableRootPane} instance as {@linkplain JFrame#getRootPane() its root
   * pane}.  This method never returns <code>null</code>.
   * 
   * @return     a new {@link JFrame}; never <code>null</code>
   */
  public static JFrame createJFrame() {
    return new JFrame() {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Convenience method that creates a {@link JFrame} with a {@link
   * DetachableRootPane} instance as {@linkplain JFrame#getRootPane() its root
   * pane}.  This method never returns <code>null</code>.
   * 
   * @param      configuration
   *               the supplied {@link GraphicsConfiguration}; supplied to the
   *               new {@link JFrame}'s {@linkplain
   *               JFrame#JFrame(GraphicsConfiguration) constructor}; may be
   *               <code>null</code>
   * @return     a new {@link JFrame}; never <code>null</code>
   */
  public static JFrame createJFrame(final GraphicsConfiguration configuration) {
    return new JFrame(configuration) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Convenience method that creates a {@link JFrame} with a {@link
   * DetachableRootPane} instance as {@linkplain JFrame#getRootPane() its root
   * pane}.  This method never returns <code>null</code>.
   * 
   * @param      title
   *               the supplied {@linkplain JFrame#getTitle() title}; supplied
   *               to the new {@link JFrame}'s {@linkplain JFrame#JFrame(String)
   *               constructor}; may be <code>null</code>
   * @return     a new {@link JFrame}; never <code>null</code>
   */
  public static JFrame createJFrame(final String title) {
    return new JFrame(title) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Convenience method that creates a {@link JFrame} with a {@link
   * DetachableRootPane} instance as {@linkplain JFrame#getRootPane() its root
   * pane}.  This method never returns <code>null</code>.
   * 
   * @param      title
   *               the supplied {@linkplain JFrame#getTitle() title}; supplied
   *               to the new {@link JFrame}'s {@linkplain JFrame#JFrame(String,
   *               GraphicsConfiguration) constructor}; may be <code>null</code>
   * @param      configuration
   *               the supplied {@link GraphicsConfiguration}; supplied to the
   *               new {@link JFrame}'s {@linkplain JFrame#JFrame(String,
   *               GraphicsConfiguration) constructor}; may be <code>null</code>
   * @return     a new {@link JFrame}; never <code>null</code>
   */
  public static JFrame createJFrame(final String title, final GraphicsConfiguration configuration) {
    return new JFrame(title, configuration) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Creates a new {@link JInternalFrame} with a {@link DetachableRootPane}
   * instance as {@linkplain JFrame#getRootPane() its root pane}.  This method
   * never returns <code>null</code>.
   *
   * @return     a new {@link JInternalFrame}; never <code>null</code>
   */
  public static JInternalFrame createJInternalFrame() {
    return new JInternalFrame() {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Creates a new {@link JInternalFrame} with a {@link DetachableRootPane}
   * instance as {@linkplain JFrame#getRootPane() its root pane}.  This method
   * never returns <code>null</code>.
   *
   * @param      title
   *               the supplied {@linkplain JInternalFrame#getTitle() title};
   *               supplied to the new {@link JInternalFrame}'s {@linkplain
   *               JInternalFrame#JInternalFrame(String) constructor}; may be
   *               <code>null</code>
   * @return     a new {@link JInternalFrame}; never <code>null</code>
   */
  public static JInternalFrame createJInternalFrame(final String title) {
    return new JInternalFrame(title) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Creates a new {@link JInternalFrame} with a {@link DetachableRootPane}
   * instance as {@linkplain JFrame#getRootPane() its root pane}.  This method
   * never returns <code>null</code>.
   *
   * @param      title
   *               the supplied {@linkplain JInternalFrame#getTitle() title};
   *               supplied to the new {@link JInternalFrame}'s {@linkplain
   *               JInternalFrame#JInternalFrame(String) constructor}; may be
   *               <code>null</code>
   * @param      resizable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isResizable() structurally
   *               resizable}
   * @return     a new {@link JInternalFrame}; never <code>null</code>
   */
  public static JInternalFrame createJInternalFrame(final String title, final boolean resizable) {
    return new JInternalFrame(title, resizable) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Creates a new {@link JInternalFrame} with a {@link DetachableRootPane}
   * instance as {@linkplain JFrame#getRootPane() its root pane}.  This method
   * never returns <code>null</code>.
   *
   * @param      title
   *               the supplied {@linkplain JInternalFrame#getTitle() title};
   *               supplied to the new {@link JInternalFrame}'s {@linkplain
   *               JInternalFrame#JInternalFrame(String) constructor}; may be
   *               <code>null</code>
   * @param      resizable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isResizable() structurally
   *               resizable}
   * @param      closable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isClosable() closable}
   * @return     a new {@link JInternalFrame}; never <code>null</code>
   */
  public static JInternalFrame createJInternalFrame(final String title, final boolean resizable, final boolean closable) {
    return new JInternalFrame(title, resizable, closable) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Creates a new {@link JInternalFrame} with a {@link DetachableRootPane}
   * instance as {@linkplain JFrame#getRootPane() its root pane}.  This method
   * never returns <code>null</code>.
   *
   * @param      title
   *               the supplied {@linkplain JInternalFrame#getTitle() title};
   *               supplied to the new {@link JInternalFrame}'s {@linkplain
   *               JInternalFrame#JInternalFrame(String) constructor}; may be
   *               <code>null</code>
   * @param      resizable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isResizable() structurally
   *               resizable}
   * @param      closable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isClosable() closable}
   * @param      maximizable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isMaximizable() maximizable}
   * @return     a new {@link JInternalFrame}; never <code>null</code>
   */
  public static JInternalFrame createJInternalFrame(final String title, final boolean resizable, final boolean closable, final boolean maximizable) {
    return new JInternalFrame(title, resizable, closable, maximizable) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  /**
   * Creates a new {@link JInternalFrame} with a {@link DetachableRootPane}
   * instance as {@linkplain JFrame#getRootPane() its root pane}.  This method
   * never returns <code>null</code>.
   *
   * @param      title
   *               the supplied {@linkplain JInternalFrame#getTitle() title};
   *               supplied to the new {@link JInternalFrame}'s {@linkplain
   *               JInternalFrame#JInternalFrame(String) constructor}; may be
   *               <code>null</code>
   * @param      resizable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isResizable() structurally
   *               resizable}
   * @param      closable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isClosable() closable}
   * @param      maximizable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isMaximizable() maximizable}
   * @param      iconifiable
   *               whether or not the new {@link JInternalFrame} is supposed to
   *               be {@linkplain JInternalFrame#isIconifiable() iconifiable}
   * @return     a new {@link JInternalFrame}; never <code>null</code>
   */
  public static JInternalFrame createJInternalFrame(final String title, final boolean resizable, final boolean closable, final boolean maximizable, final boolean iconifiable) {
    return new JInternalFrame(title, resizable, closable, maximizable, iconifiable) {

      protected final JRootPane createRootPane() {
        return new DetachableRootPane();
      }
      };
  }

  public static void main(String[] args) {
    String title = (args.length == 0 ? "Desktop Sample" : args[0]);
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //final JInternalFrame frame = DetachableRootPane.createJInternalFrame("Frame 1");
    JDesktopPane desktop = new JDesktopPane();
    JInternalFrame internalFrames[] = {
      DetachableRootPane.createJInternalFrame("Can Do All", true, true, true, true),
      DetachableRootPane.createJInternalFrame("Not Resizable", false, true, true, true),
      DetachableRootPane.createJInternalFrame("Not Closable", true, false, true, true),
      DetachableRootPane.createJInternalFrame("Not Maximizable", true, true, false, true),
      DetachableRootPane.createJInternalFrame("Not Iconifiable", true, true, true, false)
    };

    for (int i = 0,  n = internalFrames.length; i < n; i++) {
      // Add to desktop
      desktop.add(internalFrames[i]);

      // Position and size
      internalFrames[i].setBounds(i * 25, i * 25, 200, 100);


      JLabel label = new JLabel(internalFrames[i].getTitle(),
              JLabel.CENTER);
      Container content = internalFrames[i].getContentPane();
      content.add(label, BorderLayout.CENTER);

      // Make visible
      internalFrames[i].setVisible(true);
    }

    JInternalFrame palette = new JInternalFrame("Palette", true, false,
            true, false);
    palette.setBounds(350, 150, 100, 100);
    palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
    desktop.add(palette, JDesktopPane.PALETTE_LAYER);
    palette.setVisible(true);

    desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    Container content = frame.getContentPane();
    content.add(desktop, BorderLayout.CENTER);
    frame.setSize(500, 300);
    frame.setVisible(true);
  }
}
