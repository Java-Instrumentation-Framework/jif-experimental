
package edu.mbl.jif.camera.display;

import edu.mbl.jif.gui.imaging.GraphicOverlay;
import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import java.awt.Color;

/**
 *
 * @author GBH
 */
public class OverlayMessage implements GraphicOverlay{

  String msg;
  //GraphicOverlay overlay = new GraphicOverlay() {

  @Override
    public void drawGraphicOverlay(ZoomGraphics zg) {

      zg.setFont(new java.awt.Font("SansSerif", 0, 36));
      //zg.setStroke(new BasicStroke(1.0f));
      // zg.drawRect(100, 100, 100, 100);
//      double x = 0; 
//      double y = 0; 
//      double width = 0;
//      double height =0;
//      zg.getZoomedSpace(x, y, width, height);
      zg.setColor(Color.black);
      zg.drawString(msg, 12, 52);
      zg.setColor(Color.red);
      zg.drawString(msg, 10, 50);

    }
  //};

  public void setMessage(String msg) {
    this.msg = msg;
  }
}
