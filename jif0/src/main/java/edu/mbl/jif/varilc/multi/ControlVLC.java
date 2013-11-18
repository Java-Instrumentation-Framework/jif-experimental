package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.ControlDevice;
import java.text.DecimalFormat;

/**
 * VariLC Controller Model
 */

public class ControlVLC {

  ControlDevice[] device;
  int settings;

  public ControlVLC() {}

  public ControlVLC(ControlDevice[] device) {
   this.device = device;
  }

  public void transmitSettings() {
    int n = device.length;
    String toDevice = "";
    if(device.length > 0) {
       for (int i = 0; i < n; i++) {
          System.out.println("updating device: " + i);
          device[i].update();
       }
    }
  }
}
