package edu.mbl.jif.camera.attic;

import edu.mbl.jif.camera.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;

public class ActiveValueLabel
    extends JLabel
    implements Observer {

  NumberFormat formatter = null;
  ActiveDouble doubleValue;
  ActiveInteger integerValue;
  public ActiveValueLabel() {}

  public ActiveValueLabel(ActiveInteger i) {
    integerValue = i;
  }

  //format = "#.##"
  public ActiveValueLabel(ActiveDouble d, String format) {
    doubleValue = d;
    formatter = new DecimalFormat(format);
  }

  public void update(Observable o, Object obj) {
    if (o instanceof ActiveDouble) {
      String s = formatter.format( ( (ActiveDouble) o).getValue());
      this.setText(s);
    } else
        if (o instanceof ActiveInteger) {
        String s = String.valueOf(( (ActiveInteger) o).getValue());
        this.setText(s);
      }
  }
}