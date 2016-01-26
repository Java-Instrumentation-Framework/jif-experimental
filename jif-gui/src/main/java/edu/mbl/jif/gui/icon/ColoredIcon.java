package edu.mbl.jif.gui.icon;

import edu.mbl.jif.utils.StaticSwingUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * MySwing: Advanced Swing Utilites Copyright (C) 2005 Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Santhosh Kumar T
 */
public class ColoredIcon implements Icon {
   
   private Icon delegate;
   
   public ColoredIcon(Icon delegate) {
      this(delegate, UIManager.getColor("textHighlight"), 0.5F);
   }
   
   public ColoredIcon(Icon delegate, Color color) {
      this(delegate, color, 0.5F);
   }
   
   public ColoredIcon(Icon delegate, Color color, float alpha) {
      this.delegate = delegate;
      createMask(color, alpha);
   }

   /*--------------------------------[ Mask ]--------------------------------*/
   private BufferedImage mask;
   
   private void createMask(Color color, float alpha) {
      mask = new BufferedImage(delegate.getIconWidth(), delegate.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D gbi = (Graphics2D) mask.getGraphics();
      delegate.paintIcon(new JLabel(), gbi, 0, 0);
      gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
      gbi.setColor(color);
      gbi.fillRect(0, 0, mask.getWidth() - 1, mask.getHeight() - 1);
   }

   /*----------------------------------[ Color Painted ]-----------------------------------*/
   private boolean colorPainted = true;
   
   public boolean isColorPainted() {
      return colorPainted;
   }
   
   public void setColorPainted(boolean colorPainted) {
      this.colorPainted = colorPainted;
   }

   /*-------------------------------------------------[ Icon Interface ]---------------------------------------------------*/
   public void paintIcon(Component c, Graphics g, int x, int y) {
      delegate.paintIcon(c, g, x, y);
      if (colorPainted) {
         g.drawImage(mask, x, y, c);
      }
   }
   
   public int getIconWidth() {
      return delegate.getIconWidth();
   }
   
   public int getIconHeight() {
      return delegate.getIconHeight();
   }
   
   public static void main(String[] args) {
      //Icon icon = IconUtils.getIcon("edu/mbl/jif/gui/movieNew24.gif");
      Icon icon = IconUtils.loadImageIcon("/movieNew24.gif", "edu/mbl/jif/gui");
      JToggleButton button = new JToggleButton("FlatButton", icon);
      button.setHorizontalTextPosition(JLabel.CENTER);
      button.setVerticalTextPosition(JLabel.BOTTOM);
      //button.setPressedIcon(new ColoredIcon(button.getIcon()));
      button.setPressedIcon(new ColoredIcon(button.getIcon(), Color.GREEN, 0.2f));
      button.setSelectedIcon(button.getPressedIcon());
      button.setContentAreaFilled(false);
      button.setBorder(null);
      JFrame frame = new StaticSwingUtils.QuickFrame("coloredIcon");
      Icon frameIcon = new LedIcon(Color.yellow,4,4);
      frame.setIconImage(IconUtils.iconToImage(frameIcon));
      frame.add(button);
      frame.pack();
      frame.setVisible(true);
   }
}