package edu.mbl.jif.utils.log;

import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import java.io.OutputStream;


/**
   A handler for displaying log records in a window.

 Use:
   Handler windowHandler = new WindowHandler();
   windowHandler.setLevel(Level.ALL);
   Logger.getLogger("com.horstmann.corejava").addHandler(windowHandler);

 */
public class WindowHandler
      extends StreamHandler
{
   public WindowHandler () {
      frame = new JFrame();
      final JTextArea output = new JTextArea();
      output.setEditable(false);
      frame.setSize(200, 200);
      frame.add(new JScrollPane(output));
      frame.setFocusableWindowState(false);
      frame.setVisible(true);
      setOutputStream(new OutputStream()
      {
         public void write (int b) {} // not called


         public void write (byte[] b, int off, int len) {
            output.append(new String(b, off, len));
         }
      });
   }


   public void publish (LogRecord record) {
      if (!frame.isVisible()) {
         return;
      }
      super.publish(record);
      flush();
   }


   private JFrame frame;
}
