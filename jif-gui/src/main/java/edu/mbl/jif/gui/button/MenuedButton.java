package edu.mbl.jif.gui.button;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
/*
 * From http://www.oocities.org/icewalker2g/
 */

public class MenuedButton extends JPanel implements ActionListener {

   public AbstractButton main;
   public JButton popper;
   public Vector<Object> filenames = new Vector<Object>();
   public Hashtable locations = new Hashtable();
   public JPopupMenu menu = new JPopupMenu();
   public ActionListener listener;
   private int tempPopArrowDir = DOWN;
   private PopupMenuListener padapter = new PopupMenuListener() {
      public void popupMenuCanceled(PopupMenuEvent e) {
      }

      public void popupMenuWillBecomeInvisible(PopupMenuEvent p) {
         if (getPopperButtonLocation() == RIGHT) {
            setPopperArrowDirection(tempPopArrowDir);
         }
      }

      public void popupMenuWillBecomeVisible(PopupMenuEvent p) {
      }
   };
   protected Color popperArrowColor = Color.black;
   protected Point popLocation = new Point(0, 0);
   protected int locX = 0, locY = 0;
   public static int BELOW = 1, ABOVE = 2, CUSTOM = 3;
   public static int DOWN = 4, FORWARD = 5;
   public static int BOTTOM = 7, RIGHT = 8;
   private int LOCATION = BELOW, ARROW_DIRECTION = DOWN, POPPER_LOCATION = RIGHT;
   private boolean shdPopup = false, popperIsVisible = true;
   private ActionListener pl = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         showMenu();
      }
   };

   public MenuedButton() {
      this((String) null, (Icon) null);
   }

   public MenuedButton(String text) {
      this(text, (Icon) null);
   }

   public MenuedButton(Icon icon) {
      this(null, icon);
   }

   public MenuedButton(String text, Icon icon) {
      createButtons(text, icon);
      createPopupMenu();
      //setPopupLocation(BELOW);
   }

   public MenuedButton(String text, Icon icon, JPopupMenu menu) {
      createButtons(text, icon);
      setPopupMenu(menu);
   }

   public MenuedButton(AbstractButton main) {
      //this(null, null);
      setMainButton(main);
   }

   public MenuedButton(Icon icon, JPopupMenu menu) {
      JButton iconBut = new JButton(icon);
      iconBut.setMargin(new Insets(0, 0, 0, 0));
      //iconBut.

      setMainButton(iconBut);
      setPopupMenu(menu);
      setShdPopupOnBtnClick(true);
      setPopupLocation(BELOW);
   }

   public MenuedButton(String butText, JPopupMenu menu) {
      setMainButton(new JButton(butText));
      setPopupMenu(menu);
      setShdPopupOnBtnClick(true);
      setPopupLocation(BELOW);
   }

   public MenuedButton(Action action, JPopupMenu menu) {
      setMainButton(new JButton(action));
      setPopupMenu(menu);
      setShdPopupOnBtnClick(true);
      setPopupLocation(BELOW);
   }

   public void createButtons(String text, Icon icon) {
      if (main == null) {
         main = new JButton(text, icon);
         main.setAction(null); // ---???
         if (text == null) {
            main.setMargin(new Insets(0, 0, 0, 0));
         }
      }

      //main.setFont( new Font("Verdana", Font.PLAIN, 11) );
      ImageIcon img = new ImageIcon("resources/images/popicon.gif");
      popper = new JButton( /*img*/) {
         public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int width = getWidth(), height = getHeight();
            int midWidth = width / 2, midHeight = height / 2;
            int[] xpoints = {midWidth - 4, midWidth, midWidth + 4};
            int[] ypoints = {midHeight - 1, midHeight + 3, midHeight - 1};
            if (ARROW_DIRECTION == FORWARD) {
               xpoints = new int[]{midWidth - 3, midWidth - 3, midWidth + 2};
               ypoints = new int[]{midHeight - 4, midHeight + 4, midHeight};
            }
            g2.setColor(getPopperArrowColor());
            g2.fill(new Polygon(xpoints, ypoints, 3));
         }
      };
      //popper.setPreferredSize( new Dimension(img.getImage().getWidth(this) + 10,
      popper.addActionListener(this);
      setBorder(null);
      //setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
      layoutComponents();
   }

   public void layoutComponents() {
      setLayout(new BorderLayout());
      add(main, BorderLayout.CENTER);
      if (popperIsVisible) {
         if (getPopperButtonLocation() == RIGHT) {
            popper.setPreferredSize(new Dimension(14, main.getHeight()));
            add(popper, BorderLayout.EAST);
         } else if (getPopperButtonLocation() == BOTTOM) {
            popper.setPreferredSize(new Dimension(main.getWidth(), 14));
            add(popper, BorderLayout.SOUTH);
            setPopperArrowDirection(DOWN);
            setPopupLocation(popper.getX(), popper.getY() + popper.getHeight() + 5);
         }
      }
      updateView(this);
   }

   public static void updateView(Component c) {
      c.validate();
      if (c.getParent() != null) {
         c.getParent().validate();
         updateView(c.getParent());
      }
      c.repaint();
   }

   public void createPopupMenu() {
      menu = new JPopupMenu();
      for (int i = filenames.size() - 2, j = 0; i >= 0; i--, j++) {
         menu.add((String) filenames.elementAt(i));
         JMenuItem mi = (JMenuItem) menu.getComponent(j);
         mi.setFont(new Font("Arial", Font.PLAIN, 11));
         mi.addActionListener(this);
      }
      menu.pack();
      //setPopupLocation(200, 200);
   }

   public void showMenu() {
      if (LOCATION == BELOW) {
         setPopupLocation(main.getX() - main.getWidth(), main.getY() + getHeight());
      } else if (LOCATION == ABOVE) {
         setPopupLocation(main.getX() - main.getWidth(), main.getY() - menu.getHeight());
      }
      if (getPopperButtonLocation() == RIGHT) {
         if (getPopperButtonLocation() == RIGHT) {
            tempPopArrowDir = getPopperArrowDirection();
         }
         setPopperArrowDirection(DOWN);
      }
      menu.show(popper, getPopupX(), getPopupY());
   }

   public void setMainButton(AbstractButton main) {
      this.main = main;
      removeAll();
      createButtons(null, null);
      validate();
      repaint();
   }

   public AbstractButton getMainButton() {
      return main;
   }

   public void setPopperButtonLocation(int location) {
      if (POPPER_LOCATION == location) {
         return;
      }
      this.POPPER_LOCATION = location;
      layoutComponents();
   }

   public void setPopperArrowColor(Color color) {
      popperArrowColor = color;
   }

   public Color getPopperArrowColor() {
      return popperArrowColor;
   }

   public int getPopperButtonLocation() {
      return POPPER_LOCATION;
   }

   public JButton getPopperButton() {
      return popper;
   }

   public void setPopperArrowDirection(int direction) {
      if (ARROW_DIRECTION == direction) {
         return;
      }
      this.ARROW_DIRECTION = direction;

      layoutComponents();
   }

   public int getPopperArrowDirection() {
      return ARROW_DIRECTION;
   }

   public void setPopperButtonVisible(boolean b) {
      popperIsVisible = b;
      //popper.setContentAreaFilled(b);

      //	if(isShowing()) {
      layoutComponents();
      //	}
   }

   public void setPopupMenu(JPopupMenu menu) {
      this.menu = menu;
      menu.addPopupMenuListener(padapter);
   }

   public void setPopupMenu(JPopupMenu menu, int position) {
      setPopupMenu(menu);
      setPopupLocation(position);
   }

   public void setPopupLocation(int x, int y) {
      locX = x;
      locY = y;
      LOCATION = CUSTOM;
   }

   public void setPopupLocation(Point location) {
      setPopupLocation((int) location.getX(), (int) location.getY());
   }

   public void setPopupLocation(int location) {
      LOCATION = location;

      if (location == -1) {
         throw new IllegalArgumentException("Invalid value: " + location);
      }
   }

   public void setShdPopupOnBtnClick(boolean shdPopup) {
      this.shdPopup = shdPopup;
      if (shdPopup) {
         main.addActionListener(pl);
      } else {
         main.removeActionListener(pl);
      }
   }

   public boolean shdPopupOnBtnClick() {
      return shdPopup;
   }

   public int getPopupX() {
      return locX;
   }

   public int getPopupY() {
      return locY;
   }

   public void addMenuItem(String text, String loc) {
      locations.put(text, loc);
      if (filenames.contains("Test Text")) {
         filenames.remove("Test Text");
         filenames.addElement(text);
      } else {
         filenames.addElement(text);
      }
      createPopupMenu();
   }

   public void setEnabled(boolean b) {
      main.setEnabled(b);
      popper.setEnabled(b);
   }

   public void setUseFlatUI(boolean b) {
      main.setContentAreaFilled(!b);
      main.setFocusPainted(!b);
      main.setBorderPainted(!b);
      main.setMargin(new Insets(1, 1, 1, 1));

      popper.setContentAreaFilled(!b);
      popper.setFocusPainted(!b);
      popper.setBorderPainted(!b);
      popper.setMargin(new Insets(1, 1, 1, 1));

      setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      setOpaque(false);

      MouseAdapter ma = new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            main.setContentAreaFilled(true);
            main.setBackground(new Color(216, 240, 254));
            //m.getMainButton().setForeground( Color.black );
            setBorder(new LineBorder(new Color(200, 200, 200), 1));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            popper.setBackground(new Color(242, 242, 242));
            popper.setContentAreaFilled(true);
            popper.setBorder(menu.getBorder());

         }

         public void mouseExited(MouseEvent e) {
            main.setContentAreaFilled(false);
            //	c.setForeground( Color.black );
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            setCursor(Cursor.getDefaultCursor());

            popper.setContentAreaFilled(false);
            popper.setBorder(null);

         }
      };

      main.addMouseListener(ma);
      popper.addMouseListener(ma);
   }

   public void addActionListener(ActionListener l) {
      main.addActionListener(l);
      listener = l;
   }

   public void displayPage(JEditorPane pane, String text) {
      try {
         File helpFile = new File((String) locations.get(text));
         String loc = "file:" + helpFile.getAbsolutePath();
         URL page = new URL(loc);
         pane.setPage(page);
      } catch (IOException ioe) {
         JOptionPane.showMessageDialog(null, "Help Topic Unavailable");
         pane.getParent().repaint();
      }
   }

   public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      if (source == popper) {
         showMenu();
      }
   }

   public static void main(String[] args) {
      final JPopupMenu menu = new JPopupMenu();
      menu.setLayout(new GridLayout(0, 3, 5, 5));

      final MenuedButton button = new MenuedButton("Icons", menu);
      button.setShdPopupOnBtnClick(false);

      for (int i = 0; i < 3; i++) {
         // replace "print.gif" with your own image
         final JLabel label = new JLabel(new ImageIcon("movieNew24.gif"));
         label.setText(" " + i + " ");
         // final JLabel label = new JLabel("    "+i + "    ");
         label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               button.getMainButton().setIcon(label.getIcon());
               button.getMainButton().setText(label.getText());
               menu.setVisible(false);
               System.out.println("clicked");
            }
         });
         menu.add(label);
      }

      JFrame frame = new JFrame("Button Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new JLabel("Click Arrow Button To Show Popup"),
              BorderLayout.NORTH);
      frame.getContentPane().add(button, BorderLayout.CENTER);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }
}