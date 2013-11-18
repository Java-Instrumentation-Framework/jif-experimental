/*
 * SingleRowTabbedPaneExample4.java
 *
 * Created on December 14, 2006, 4:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui;
// Example from http://www.crionics.com/products/opensource/faq/swing_ex/SwingExamples.html
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class SingleRowTabbedPaneExample4 extends JPanel {                     
    public SingleRowTabbedPaneExample4() {
      setLayout(new BorderLayout());
          
      SingleRowTabbedPane tabbedPane = new SingleRowTabbedPane(
        SingleRowTabbedPane.FOUR_BUTTONS, SwingConstants.LEFT);
      tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
      
      String tabs[] = {"One", "Two", "Three", "Four", "Five",
                       "Six", "Seven","Eight","Nine", "Ten" };
      for (int i=0;i<tabs.length;i++) {
        tabbedPane.addTab(tabs[i], createPane(tabs[i]));
      }
      tabbedPane.setSelectedIndex(0);
      add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createPane(String s) {
      JPanel p = new JPanel();
      p.add(new JLabel(s));
      return p;
    }
    
    public static void main(String[] args) {
      JFrame frame = new JFrame("Four buttons Example");
      frame.addWindowListener( new WindowAdapter() {
        public void windowClosing( WindowEvent e ) {
          System.exit(0);
        }
      });
      frame.getContentPane().add( new SingleRowTabbedPaneExample4() );
      frame.setSize( 250, 100 );
      frame.setVisible(true);
    }
  }

class SingleRowTabbedPane extends JTabbedPane {

  public static final String ROTATE = "Rotate";

  public static final String PREVIOUS = "Previous";

  public static final String NEXT = "Next";

  public static final String FIRST = "First";

  public static final String LEFT_SHIFT = "Left";

  public static final String RIGHT_SHIFT = "Right";

  public static final String LAST = "Last";

  public static final int ONE_BUTTON = 1; //                  ROTATE ;

  public static final int TWO_BUTTONS = 2; //          PREVIOUS | NEXT ;

  public static final int FOUR_BUTTONS = 4; // FIRST | LEFT_SHIFT |
                        // RIGHT_SHIFT | LAST ;

  protected int buttonPlacement;

  protected int buttonCount;

  protected JButton[] tabPaneButtons;

  protected Dimension buttonSize;

  protected int visibleCount;

  protected int visibleStartIndex;

  private final int BUTTON_WIDTH = 16;

  private final int BUTTON_HEIGHT = 17;

  public SingleRowTabbedPane() {
    this(TWO_BUTTONS, RIGHT);
    //this(ONE_BUTTON, RIGHT);
    //this(FOUR_BUTTONS, LEFT);
  }

  public SingleRowTabbedPane(int buttonCount, int buttonPlacement) {
    setButtonPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    tabPaneButtons = createButtons(buttonCount);
    this.buttonPlacement = buttonPlacement;
    visibleStartIndex = 0;

    setUI(new SingleRowTabbedPaneUI());
  }

  public void setTabPlacement(int tabPlacement) {
    if (tabPlacement == LEFT || tabPlacement == RIGHT) {
      throw new IllegalArgumentException("not suported: LEFT and RIGHT");
    }
    super.setTabPlacement(tabPlacement);
  }

  public int getButtonPlacement() {
    return buttonPlacement;
  }

  public void setButtonPreferredSize(Dimension d) {
    if (d != null) {
      buttonSize = d;
    }
  }

  public Dimension getButtonPreferredSize() {
    return buttonSize;
  }

  public JButton[] getButtons() {
    return tabPaneButtons;
  }

  public int getButtonCount() {
    return buttonCount;
  }

  public void insertTab(String title, Icon icon, Component component,
      String tip, int index) {
    if (component instanceof TabbedPaneButton) {
      if (component != null) {
        component.setVisible(true);
        addImpl(component, null, -1);
      }
      return;
    }
    super.insertTab(title, icon, component, tip, index);
  }

  public boolean isVisibleTab(int index) {
    if ((visibleStartIndex <= index)
        && (index < visibleStartIndex + visibleCount)) {
      return true;
    } else {
      return false;
    }
  }

  public int getVisibleCount() {
    return visibleCount;
  }

  public void setVisibleCount(int visibleCount) {
    if (visibleCount < 0) {
      return;
    }
    this.visibleCount = visibleCount;
  }

  public int getVisibleStartIndex() {
    return visibleStartIndex;
  }

  public void setVisibleStartIndex(int visibleStartIndex) {
    if (visibleStartIndex < 0 || getTabCount() <= visibleStartIndex) {
      return;
    }
    this.visibleStartIndex = visibleStartIndex;
  }

  protected JButton[] createButtons(int buttonCount) {
    JButton[] tabPaneButtons = null;
    switch (buttonCount) {
    case ONE_BUTTON:
      this.buttonCount = buttonCount;
      tabPaneButtons = new JButton[buttonCount];
      tabPaneButtons[0] = new PrevOrNextButton(EAST);
      tabPaneButtons[0].setActionCommand(ROTATE);
      break;
    case TWO_BUTTONS:
      this.buttonCount = buttonCount;
      tabPaneButtons = new JButton[buttonCount];
      tabPaneButtons[0] = new PrevOrNextButton(WEST);
      tabPaneButtons[0].setActionCommand(PREVIOUS);
      tabPaneButtons[1] = new PrevOrNextButton(EAST);
      tabPaneButtons[1].setActionCommand(NEXT);
      break;
    case FOUR_BUTTONS:
      this.buttonCount = buttonCount;
      tabPaneButtons = new JButton[buttonCount];
      tabPaneButtons[0] = new FirstOrLastButton(WEST);
      tabPaneButtons[0].setActionCommand(FIRST);
      tabPaneButtons[1] = new PrevOrNextButton(WEST);
      tabPaneButtons[1].setActionCommand(LEFT_SHIFT);
      tabPaneButtons[2] = new PrevOrNextButton(EAST);
      tabPaneButtons[2].setActionCommand(RIGHT_SHIFT);
      tabPaneButtons[3] = new FirstOrLastButton(EAST);
      tabPaneButtons[3].setActionCommand(LAST);
      break;
    default:
    }
    return tabPaneButtons;
  }

  class PrevOrNextButton extends BasicArrowButton implements TabbedPaneButton {
    public PrevOrNextButton(int direction) {
      super(direction);
    }
  }

  class FirstOrLastButton extends StopArrowButton implements TabbedPaneButton {
    public FirstOrLastButton(int direction) {
      super(direction);
    }
  }

}

class SingleRowTabbedPaneUI extends MetalTabbedPaneUI {

  protected ActionListener[] buttonListeners;

  public void installUI(JComponent c) {
    this.tabPane = (JTabbedPane) c;
    c.setLayout(createLayoutManager());
    installDefaults();
    installComponents();
    installListeners();
    installKeyboardActions();

    runCount = 1;
    selectedRun = 0;
  }

  public void uninstallUI(JComponent c) {
    uninstallComponents();
    super.uninstallUI(c);
  }

  protected LayoutManager createLayoutManager() {
    return new SingleRowTabbedLayout(tabPane);
  }

  protected void installComponents() {
    JButton[] buttons = ((SingleRowTabbedPane) tabPane).getButtons();
    for (int i = 0; i < buttons.length; i++) {
      tabPane.add(buttons[i]);
    }
  }

  protected void uninstallComponents() {
    JButton[] buttons = ((SingleRowTabbedPane) tabPane).getButtons();
    for (int i = 0; i < buttons.length; i++) {
      tabPane.remove(buttons[i]);
    }
  }

  protected void installListeners() {
    super.installListeners();
    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    JButton[] buttons = stabPane.getButtons();
    int n = buttons.length;
    buttonListeners = new ActionListener[n];

    for (int i = 0; i < n; i++) {
      buttonListeners[i] = null;
      String str = buttons[i].getActionCommand();

      if (str.equals(SingleRowTabbedPane.ROTATE)) {
        buttonListeners[i] = new ShiftTabs() {
          protected int getStartIndex() {
            int index = sPane.getVisibleStartIndex()
                + sPane.getVisibleCount();
            return (index < sPane.getTabCount()) ? index : 0;
          }
        };
      } else if (str.equals(SingleRowTabbedPane.PREVIOUS)) {
        buttonListeners[i] = new ShiftTabs() {
          protected int getStartIndex() {
            return getStartIndex(sPane.getVisibleStartIndex() - 1);
          }
        };
      } else if (str.equals(SingleRowTabbedPane.NEXT)) {
        buttonListeners[i] = new ShiftTabs() {
          protected int getStartIndex() {
            return sPane.getVisibleStartIndex()
                + sPane.getVisibleCount();
          }
        };
      } else if (str.equals(SingleRowTabbedPane.FIRST)) {
        buttonListeners[i] = new ShiftTabs();
      } else if (str.equals(SingleRowTabbedPane.LEFT_SHIFT)) {
        buttonListeners[i] = new ShiftTabs() {
          protected int getStartIndex() {
            return sPane.getVisibleStartIndex() - 1;
          }
        };
      } else if (str.equals(SingleRowTabbedPane.RIGHT_SHIFT)) {
        buttonListeners[i] = new ShiftTabs() {
          protected int getStartIndex() {
            return sPane.getVisibleStartIndex() + 1;
          }
        };
      } else if (str.equals(SingleRowTabbedPane.LAST)) {
        buttonListeners[i] = new ShiftTabs() {
          protected int getStartIndex() {
            return getStartIndex(sPane.getTabCount() - 1);
          }
        };
      }
      buttons[i].addActionListener(buttonListeners[i]);
    }
  }

  protected void uninstallListeners() {
    super.uninstallListeners();
    JButton[] buttons = ((SingleRowTabbedPane) tabPane).getButtons();
    for (int i = 0; i < buttons.length; i++) {
      buttons[i].removeActionListener(buttonListeners[i]);
    }
  }

  public int tabForCoordinate(JTabbedPane pane, int x, int y) {
    int tabCount = tabPane.getTabCount();
    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    int visibleCount = stabPane.getVisibleCount();
    int visibleStartIndex = stabPane.getVisibleStartIndex();

    for (int i = 0, index = visibleStartIndex; i < visibleCount; i++, index++) {
      if (rects[index].contains(x, y)) {
        return index;
      }
    }
    return -1;
  }

  public void paint(Graphics g, JComponent c) {
    int selectedIndex = tabPane.getSelectedIndex();
    int tabPlacement = tabPane.getTabPlacement();
    int tabCount = tabPane.getTabCount();

    ensureCurrentLayout();

    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    int visibleCount = stabPane.getVisibleCount();
    int visibleStartIndex = stabPane.getVisibleStartIndex();

    Rectangle iconRect = new Rectangle(), textRect = new Rectangle();
    Rectangle clipRect = g.getClipBounds();
    Insets insets = tabPane.getInsets();

    tabRuns[0] = visibleStartIndex;

    for (int i = 0, index = visibleStartIndex; i < visibleCount; i++, index++) {
      if (rects[index].intersects(clipRect)) {
        paintTab(g, tabPlacement, rects, index, iconRect, textRect);
      }
    }
    if (stabPane.isVisibleTab(selectedIndex)) {
      if (rects[selectedIndex].intersects(clipRect)) {
        paintTab(g, tabPlacement, rects, selectedIndex, iconRect,
            textRect);
      }
    }

    paintContentBorder(g, tabPlacement, selectedIndex);
  }

  protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
      int selectedIndex, int x, int y, int w, int h) {
    g.setColor(selectHighlight);
    if (tabPlacement != TOP
        || selectedIndex < 0
        || (rects[selectedIndex].y + rects[selectedIndex].height + 1 < y)
        || !((SingleRowTabbedPane) tabPane).isVisibleTab(selectedIndex)) {
      g.drawLine(x, y, x + w - 2, y);
    } else {
      Rectangle selRect = rects[selectedIndex];
      g.drawLine(x, y, selRect.x + 1, y);
      if (selRect.x + selRect.width < x + w - 2) {
        g.drawLine(selRect.x + selRect.width, y, x + w - 2, y);
      } else {
        g.setColor(shadow);
        g.drawLine(x + w - 2, y, x + w - 2, y);
      }
    }
  }

  protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
      int selectedIndex, int x, int y, int w, int h) {
    g.setColor(darkShadow);
    if (tabPlacement != BOTTOM || selectedIndex < 0
        || (rects[selectedIndex].y - 1 > h)
        || !((SingleRowTabbedPane) tabPane).isVisibleTab(selectedIndex)) {
      g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
    } else {
      Rectangle selRect = rects[selectedIndex];
      g.drawLine(x, y + h - 1, selRect.x, y + h - 1);
      if (selRect.x + selRect.width < x + w - 2) {
        g.drawLine(selRect.x + selRect.width, y + h - 1, x + w - 1, y
            + h - 1);
      }
    }
  }

  protected Insets getTabAreaInsets(int tabPlacement) {
    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    Dimension d = stabPane.getButtonPreferredSize();
    int n = stabPane.getButtonCount();
    int buttonPlacement = stabPane.getButtonPlacement();

    Insets currentInsets = new Insets(0, 0, 0, 0);
    if (tabPlacement == TOP) {
      currentInsets.top = tabAreaInsets.top;
      currentInsets.bottom = tabAreaInsets.bottom;
    } else {
      currentInsets.top = tabAreaInsets.bottom;
      currentInsets.bottom = tabAreaInsets.top;
    }
    if (buttonPlacement == RIGHT) {
      currentInsets.left = tabAreaInsets.left;
      currentInsets.right = tabAreaInsets.right + n * d.width;
    } else {
      currentInsets.left = tabAreaInsets.left + n * d.width;
      currentInsets.right = tabAreaInsets.right;
    }
    return currentInsets;
  }

  protected int lastTabInRun(int tabCount, int run) {
    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    return stabPane.getVisibleStartIndex() + stabPane.getVisibleCount() - 1;
  }

  protected void ensureCurrentLayout() {
    SingleRowTabbedLayout layout = (SingleRowTabbedLayout) tabPane
        .getLayout();
    layout.calculateLayoutInfo();
    setButtonsEnabled();
  }

  protected void setButtonsEnabled() {
    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    int visibleCount = stabPane.getVisibleCount();
    int visibleStartIndex = stabPane.getVisibleStartIndex();
    JButton[] buttons = stabPane.getButtons();
    boolean lEnable = 0 < visibleStartIndex;
    boolean rEnable = visibleStartIndex + visibleCount < tabPane
        .getTabCount();
    for (int i = 0; i < buttons.length; i++) {
      boolean enable = false;
      String str = buttons[i].getActionCommand();
      if (str.equals(SingleRowTabbedPane.ROTATE)) {
        enable = lEnable || rEnable;
      } else if (str.equals(SingleRowTabbedPane.PREVIOUS)) {
        enable = lEnable;
      } else if (str.equals(SingleRowTabbedPane.NEXT)) {
        enable = rEnable;
      } else if (str.equals(SingleRowTabbedPane.FIRST)) {
        enable = lEnable;
      } else if (str.equals(SingleRowTabbedPane.LEFT_SHIFT)) {
        enable = lEnable;
      } else if (str.equals(SingleRowTabbedPane.RIGHT_SHIFT)) {
        enable = rEnable;
      } else if (str.equals(SingleRowTabbedPane.LAST)) {
        enable = rEnable;
      }
      buttons[i].setEnabled(enable);
    }
  }

  // 
  // Tab Navigation by Key
  // (Not yet done)
  //
  protected void ensureVisibleTabAt(int index) {
    SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
    int visibleCount = stabPane.getVisibleCount();
    int visibleStartIndex = stabPane.getVisibleStartIndex();
    int visibleEndIndex = visibleStartIndex + visibleCount - 1;

    if (visibleStartIndex < index && index < visibleEndIndex) {
      return;
    }
    int selectedIndex = tabPane.getSelectedIndex();
    boolean directionIsRight = (0 < index - selectedIndex) ? true : false;
    //if (directionIsRight) {
    if (index <= visibleStartIndex) {
      //System.out.println("dec");
      if (visibleStartIndex == 0)
        return;
      stabPane.setVisibleStartIndex(--visibleStartIndex);
      ((SingleRowTabbedLayout) tabPane.getLayout()).calculateLayoutInfo();
      int count = stabPane.getVisibleCount();
      int startIndex = stabPane.getVisibleStartIndex();
      if (startIndex <= index && index <= startIndex + count - 1) {
      } else {
        stabPane.setVisibleStartIndex(++visibleStartIndex);
      }
    }
    //} else {
    if (visibleEndIndex <= index) {
      //System.out.println("inc");
      if (visibleStartIndex == visibleCount + 1)
        return;
      stabPane.setVisibleStartIndex(++visibleStartIndex);
      ((SingleRowTabbedLayout) tabPane.getLayout()).calculateLayoutInfo();
      int count = stabPane.getVisibleCount();
      int startIndex = stabPane.getVisibleStartIndex();
      if (startIndex <= index && index <= startIndex + count - 1) {
      } else {
        stabPane.setVisibleStartIndex(--visibleStartIndex);
      }
    }
    //}

    int c = stabPane.getVisibleCount();
    int s = stabPane.getVisibleStartIndex();
  }

  protected void selectNextTab(int current) {
    for (int i = current + 1; i < tabPane.getTabCount(); i++) {
      if (tabPane.isEnabledAt(i)) {
        ensureVisibleTabAt(i);
        tabPane.setSelectedIndex(i);
        break;
      }
    }
  }

  protected void selectPreviousTab(int current) {
    for (int i = current - 1; 0 <= i; i--) {
      if (tabPane.isEnabledAt(i)) {
        ensureVisibleTabAt(i);
        tabPane.setSelectedIndex(i);
        break;
      }
    }
  }

  /*
   * not used protected int getPreviousTabIndex(int base) { int tabIndex =
   * base - 1; return (tabIndex < 0? 0: tabIndex); }
   * 
   * protected int getNextTabIndex(int base) { int tabIndex = base + 1; return
   * (tabPane.getTabCount() <= tabIndex? tabIndex-1: tabIndex); }
   */

  //
  // these methods exist for innerclass
  //
  void setMaxTabHeight(int maxTabHeight) {
    this.maxTabHeight = maxTabHeight;
  }

  int getMaxTabHeight() {
    return maxTabHeight;
  }

  Rectangle[] getRects() {
    return rects;
  }

  SingleRowTabbedPane getTabbedPane() {
    return (SingleRowTabbedPane) tabPane;
  }

  protected FontMetrics getFontMetrics() {
    Font font = tabPane.getFont();
    return Toolkit.getDefaultToolkit().getFontMetrics(font);
  }

  protected int calculateMaxTabHeight(int tabPlacement) {
    return super.calculateMaxTabHeight(tabPlacement);
  }

  protected int calculateTabWidth(int tabPlacement, int tabIndex,
      FontMetrics metrics) {
    return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
  }

  protected void assureRectsCreated(int tabCount) {
    super.assureRectsCreated(tabCount);
  }

  //
  // Layout
  //
  class SingleRowTabbedLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
    JTabbedPane tabPane;

    SingleRowTabbedLayout(JTabbedPane tabPane) {
      this.tabPane = tabPane;
    }

    public void layoutContainer(Container parent) {
      super.layoutContainer(parent);
      if (tabPane.getComponentCount() < 1) {
        return;
      }

      int tabPlacement = tabPane.getTabPlacement();
      int maxTabHeight = calculateMaxTabHeight(tabPlacement);
      Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
      Insets insets = tabPane.getInsets();
      Rectangle bounds = tabPane.getBounds();

      SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
      Dimension d = stabPane.getButtonPreferredSize();
      JButton[] buttons = stabPane.getButtons();
      int buttonPlacement = stabPane.getButtonPlacement();

      int x, y;
      if (tabPlacement == TOP) { // TOP
        y = bounds.y + insets.top + tabAreaInsets.top;
      } else { // BOTTOM
        y = bounds.y + bounds.height - insets.bottom
            - tabAreaInsets.bottom - maxTabHeight;
      }
      if (buttonPlacement == RIGHT) { // RIGHT
        x = bounds.x + bounds.width - insets.right;
        for (int i = buttons.length - 1; 0 <= i; i--) {
          x -= d.width;
          buttons[i].setBounds(x, y, d.width, d.height);
        }
      } else { // LEFT
        x = bounds.x + insets.left;
        for (int i = 0; i < buttons.length; i++) {
          buttons[i].setBounds(x, y, d.width, d.height);
          x += d.width;
        }
      }
    }

    public void calculateLayoutInfo() {
      int tabCount = tabPane.getTabCount();
      assureRectsCreated(tabCount);
      calculateTabWidths(tabPane.getTabPlacement(), tabCount);
      calculateTabRects(tabPane.getTabPlacement(), tabCount);
    }

    protected void calculateTabWidths(int tabPlacement, int tabCount) {
      if (tabCount == 0) {
        return;
      }
      FontMetrics metrics = getFontMetrics();
      int fontHeight = metrics.getHeight();
      int maxTabHeight = calculateMaxTabHeight(tabPlacement);
      setMaxTabHeight(maxTabHeight);
      Rectangle[] rects = getRects();
      for (int i = 0; i < tabCount; i++) {
        rects[i].width = calculateTabWidth(tabPlacement, i, metrics);
        rects[i].height = maxTabHeight;
      }
    }

    protected void calculateTabRects(int tabPlacement, int tabCount) {
      if (tabCount == 0) {
        return;
      }
      SingleRowTabbedPane stabPane = (SingleRowTabbedPane) tabPane;
      Dimension size = tabPane.getSize();
      Insets insets = tabPane.getInsets();
      Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
      int selectedIndex = tabPane.getSelectedIndex();

      int maxTabHeight = getMaxTabHeight();
      int x = insets.left + tabAreaInsets.left;
      int y;
      if (tabPlacement == TOP) {
        y = insets.top + tabAreaInsets.top;
      } else { // BOTTOM
        y = size.height - insets.bottom - tabAreaInsets.bottom
            - maxTabHeight;
      }

      int returnAt = size.width - (insets.right + tabAreaInsets.right);
      Rectangle[] rects = getRects();
      int visibleStartIndex = stabPane.getVisibleStartIndex();
      int visibleCount = 0;

      for (int i = visibleStartIndex; i < tabCount; i++) {
        Rectangle rect = rects[i];
        if (visibleStartIndex < i) {
          rect.x = rects[i - 1].x + rects[i - 1].width;
        } else {
          rect.x = x;
        }

        if (rect.x + rect.width > returnAt) {
          break;
        } else {
          visibleCount++;
          rect.y = y;
        }
      }
      stabPane.setVisibleCount(visibleCount);
      stabPane.setVisibleStartIndex(visibleStartIndex);
    }
  }

  //
  // Listener
  //
  protected class ShiftTabs implements ActionListener {
    SingleRowTabbedPane sPane;

    public void actionPerformed(ActionEvent e) {
      sPane = getTabbedPane();
      int index = getStartIndex();
      sPane.setVisibleStartIndex(index);
      sPane.repaint();
    }

    //public abstract int getStartIndex();
    protected int getStartIndex() {
      return 0; // first tab
    }

    protected int getStartIndex(int lastIndex) {
      Insets insets = sPane.getInsets();
      Insets tabAreaInsets = getTabAreaInsets(sPane.getTabPlacement());
      int width = sPane.getSize().width - (insets.left + insets.right)
          - (tabAreaInsets.left + tabAreaInsets.right);
      int index;
      Rectangle[] rects = getRects();
      for (index = lastIndex; 0 <= index; index--) {
        width -= rects[index].width;
        if (width < 0) {
          break;
        }
      }
      return ++index;
    }
  }

}

class StopArrowButton extends BasicArrowButton {

  public StopArrowButton(int direction) {
    super(direction);
  }

  public void paintTriangle(Graphics g, int x, int y, int size,
      int direction, boolean isEnabled) {
    super.paintTriangle(g, x, y, size, direction, isEnabled);
    Color c = g.getColor();
    if (isEnabled) {
      g.setColor(UIManager.getColor("controlDkShadow"));
    } else {
      g.setColor(UIManager.getColor("controlShadow"));
    }
    g.translate(x, y);
    size = Math.max(size, 2);
    int mid = size / 2;
    int h = size - 1;
    if (direction == WEST) {
      g.drawLine(-1, mid - h, -1, mid + h);
      if (!isEnabled) {
        g.setColor(UIManager.getColor("controlLtHighlight"));
        g.drawLine(0, mid - h + 1, 0, mid - 1);
        g.drawLine(0, mid + 2, 0, mid + h + 1);
      }
    } else { // EAST
      g.drawLine(size, mid - h, size, mid + h);
      if (!isEnabled) {
        g.setColor(UIManager.getColor("controlLtHighlight"));
        g.drawLine(size + 1, mid - h + 1, size + 1, mid + h + 1);
      }
    }
    g.setColor(c);
  }
}

interface TabbedPaneButton {

}
