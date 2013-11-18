/*
 * From: http://aneesjavaee-osgi.blogspot.com/2008/10/how-to-bind-map-with-jgoodies-binding.html
 */
package tests.binding.veto;

import com.jgoodies.binding.adapter.Bindings;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Anees
 * 
*/
public class TestMapBinding {

   /**
    * @param args
    */
   public static void main(String[] args) {

      Map map = new HashMap();
      map.put("firstName", "Anees");

      MapValueModel mapValueModel = new MapValueModel(map, "firstName");

      JTextField firstNameField = new JTextField();
      Bindings.bind(firstNameField, mapValueModel);

      JPanel panel = new JPanel();
      panel.add(firstNameField);

      JFrame frame = new JFrame("Testing Map Bindings...");
      frame.getContentPane().add(panel);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.show();
      frame.pack();

   }
}
