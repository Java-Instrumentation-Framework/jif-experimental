package edu.mbl.jif.gui.screen;

import java.awt.Image;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.AWTException;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ScreenCapture
{
  public ScreenCapture () {
  }
  public static Image captureScreen() {
    BufferedImage bufferedImage = null;
    try {
      Robot robot = new Robot();
      Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().
                                            getScreenSize());
       bufferedImage = robot.createScreenCapture(captureSize);
    } catch (AWTException e) {
      System.err.println("Someone call a doctor!");
    }
    return bufferedImage;
  }
}
