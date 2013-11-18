package edu.mbl.jif.varilc.multi;

import edu.mbl.jif.varilc.multi.Retarder;
import java.util.Vector;
import java.util.*;
import edu.mbl.jif.comm.SerialPortConnection;
import edu.mbl.jif.utils.convert.Type;
import java.text.DecimalFormat;


/**
 * A device to control some number of variable retarders
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
public class ControlDevice
{

   DecimalFormat dFmt = new DecimalFormat("0.000");

   String header = "";
   String footer = "";
   Vector retarders = new Vector();
   SerialPortConnection port; // port to which this device is attached

   public ControlDevice (SerialPortConnection port, String header, String footer) {
      this.port = port;
      this.header = header;
      this.footer = footer;

   }


   public void addRetarder (Retarder r) {
      retarders.add(r);
   }


//-------------------------------------------------------------------------
   public void update () {
      if (!port.isOpen()) {
         System.err.println("Port not open");
         return;
      }
      if (isInNeedOfUpdate()) {
         port.send(header);
         String s = "L ";
         // Build string with retarder settings
         Iterator iter = retarders.iterator();
         while (iter.hasNext()) {
            Object item = (Object) iter.next();
            Retarder ret = ((Retarder) item);
            s = s + " " + dFmt.format(ret.getValue());
            ret.confirmUpdated();
         }
         port.send(s);
         port.send(footer);
      }
   }


   public boolean isInNeedOfUpdate () {
      boolean toUpdate = false;
      Iterator iter = retarders.iterator();
      while (iter.hasNext()) {
         Object item = (Object) iter.next();
         Retarder ret = ((Retarder) item);
         if (ret.isToChange()) {
            toUpdate = true;
         }
      }
      return toUpdate;
   }
}
