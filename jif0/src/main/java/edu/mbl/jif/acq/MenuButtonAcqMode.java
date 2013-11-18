package edu.mbl.jif.acq;

import edu.mbl.jif.gui.button.MenuedButton;
import edu.mbl.jif.gui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.border.*;

// NEW -- Under dev....
public class MenuButtonAcqMode extends MenuedButton {

  public ColoredIcon selectedColor;
  public JButton back;

  public MenuButtonAcqMode() {
    this(Color.black);
  }

  public MenuButtonAcqMode(Color color) {
    this(color, color.toString());
  }

  public MenuButtonAcqMode(Color color, String toolTip) {
    back = new JButton(null, new ColoredIcon(Color.black));
    back.setPreferredSize(new Dimension(22, 22));
    setMainButton(back);
    setPopupMenu(new ColorPanel(back));
    setPopupLocation(MenuedButton.BELOW);
    setSelectedColor(color);
    setToolTipText(toolTip);
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.add(new MenuButtonAcqMode());
    f.setVisible(true);
  }

  public Color getSelectedColor() {
    return ((ColoredIcon) getMainButton().getIcon()).getCurrentColor();
  }

  public void setSelectedColor(Color color) {
    selectedColor = new ColoredIcon(color);
    back.setIcon(selectedColor);
  }
  JColorChooser cChooser;

  class ColorChooserAction extends AbstractAction {

    JButton target;

    ColorChooserAction(JButton target) {
      super(null, target.getIcon());
      this.target = target;
    }

    public void actionPerformed(ActionEvent e) {
      if (cChooser == null) {
        cChooser = new JColorChooser();
      }
      Color color = cChooser.showDialog(target, "Available Colors",
              ((ColoredIcon) target.getIcon()).getCurrentColor());
      target.setIcon(new ColoredIcon(color));
      target.doClick();
    }
  }

  class ColoredIcon implements Icon {

    Color color;
    int width = 20, height = 20;

    public ColoredIcon(Color c) {
      this.color = c;
    }

    public ColoredIcon(Color c, int width, int height) {
      this(c);
      setIconWidth(width);
      setIconHeight(height);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
      Color oldColor = g.getColor();
      g.setColor(color);
      g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
      g.setColor(oldColor);
    }

    public int getIconWidth() {
      return width;
    }

    public int getIconHeight() {
      return height;
    }

    public Color getCurrentColor() {
      return color;
    }

    public void setIconWidth(int width) {
      this.width = width;
    }

    public void setIconHeight(int height) {
      this.height = height;
    }
  }

  class ColorPanel extends JPopupMenu {

    Color[] color;
    String[] colorString = {"255 0 0", "150 150 0", "255 0 255", "0 255 255", "0 0 255",
      "255 125 125", "230 230 50", "200 100 200", "100 200 200", "100 100 200",
      "125 75 75", "255 255 0", "100 150 100", "197 228 255", "75 75 150",
      "125 75 125", "255 255 204", "75 100 75", "75 50 100", "40 40 85",
      "0 0 0", "50 50 50", "125 125 125", "200 200 200", "255 255 255"};
    Component target;
    JButton moreColors;

    public ColorPanel(Component invoker) {
      super();
      setTarget(invoker);
      setLayout(new BorderLayout());
      JPanel topLabel = new JPanel();
      topLabel.setOpaque(true);
      topLabel.setBackground(Color.darkGray);
      //topLabel.setPreferredSize( new Dimension(getWidth() - 10, 15) );
      add(topLabel, BorderLayout.NORTH);
      createColorArray();
      createColorPanel();

    }

    public void createColorArray() {
      color = new Color[colorString.length];
      for (int i = 0; i < colorString.length; i++) {
        StringTokenizer st = new StringTokenizer(colorString[i], " ");
        Color c = new Color(rgb(st.nextToken()), rgb(st.nextToken()), rgb(st.nextToken()));
        color[i] = c;
      }
    }
    JLabel oldLabel = new JLabel();

    public void createColorPanel() {
      JPanel colorPanel = new JPanel(new GridLayout(0, 1));
      int curRow = 0;
      for (int i = 0; i < colorString.length / 5; i++) {
        JPanel row = new JPanel(new GridLayout(1, 0, 2, 1));
        row.setBorder(new EmptyBorder(2, 2, 2, 2));

        for (int j = curRow; j < curRow + 5; j++) {
          final JLabel colorLabel = new JLabel(null, new ColoredIcon(color[j], 14, 14), JLabel.CENTER);
          colorLabel.setOpaque(true);
          final Border emb = BorderFactory.createEmptyBorder(2, 1, 2, 1);
          final Border lnb = BorderFactory.createLineBorder(Color.black);
          final Border cmb = BorderFactory.createCompoundBorder(lnb, emb);
          colorLabel.setBorder(emb);
          colorLabel.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
              JButton btn = (JButton) getTarget();
              Color selColor = ((ColoredIcon) colorLabel.getIcon()).getCurrentColor();
              btn.setIcon(new ColoredIcon(selColor));
              setVisible(false);
              btn.doClick();
              oldLabel.setBackground(null);
              colorLabel.setBackground(new Color(150, 150, 200));
              colorLabel.setBorder(emb);
              oldLabel = colorLabel;
            }

            public void mouseEntered(MouseEvent e) {
              colorLabel.setBorder(cmb);
              colorLabel.setBackground(new Color(150, 150, 200));
            }

            public void mouseExited(MouseEvent e) {
              colorLabel.setBorder(emb);
              colorLabel.setBackground(null);
            }
          });
          row.add(colorLabel);
        }
        colorPanel.add(row);
        curRow += row.getComponentCount();
      //System.out.println(curRow);
      }

      add(colorPanel, BorderLayout.CENTER);

      // More Colors Button
      moreColors = new JButton(new ColorChooserAction((JButton) target));
      moreColors.setText("More Colors...");
      moreColors.setIcon(null);
      moreColors.setFont(new Font("Verdana", Font.PLAIN, 10));
      //
      JPanel c = new JPanel(new FlowLayout(FlowLayout.CENTER));
      c.add(moreColors);
      add(c, BorderLayout.SOUTH);
    }

    int rgb(String rgb) {
      return Integer.parseInt(rgb);
    }

    void setTarget(Component target) {
      this.target = target;
    }

    Component getTarget() {
      return target;
    }
  }
}