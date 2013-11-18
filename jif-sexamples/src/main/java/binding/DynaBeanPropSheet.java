package binding;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.LookAndFeelTweaks;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.SimpleBeanInfo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;

/**
 * PropertySheet. ExampleBeanPropSheet>
 *
 */
public class DynaBeanPropSheet
        extends JPanel {

   public DynaBeanPropSheet() {
      setLayout(LookAndFeelTweaks.createVerticalPercentLayout());

      final JButton button = new JButton();
      button.setText("Change my properties!");

      final ExampleBindingBean exampleBean = new ExampleBindingBean();
      BeanInfo beanInfo = new SimpleBeanInfo();
      DynaBean dynabean = new WrapDynaBean(exampleBean);

      try {
         beanInfo = Introspector.getBeanInfo(dynabean.getDynaClass().getClass());
      } catch (IntrospectionException e) {
         e.printStackTrace();
      }

      final PropertySheetPanel sheet = new PropertySheetPanel();
      sheet.setMode(PropertySheet.VIEW_AS_FLAT_LIST);

      sheet.setToolBarVisible(false);
      sheet.setDescriptionVisible(false);
      sheet.setBeanInfo(beanInfo);

      final JPanel panel = new JPanel(LookAndFeelTweaks.createBorderLayout());
      panel.add("Center", sheet);
      panel.add("East", button);

      // initialize the properties with the value from the object
      // one can use sheet.readFromObject(button)
      // but I encountered some issues with Java Web Start. The method
      // getLocationOnScreen on the button is throwing an exception, it
      // does not happen when not using Web Start. Load properties one
      // by one as follow will do the trick
      Property[] properties = sheet.getProperties();
      for (int i = 0, c = properties.length; i < c; i++) {
         try {
            properties[i].readFromObject(exampleBean);
         } catch (Exception e) {
         }
      }
      // everytime a property change, update the button with it
      PropertyChangeListener listener = new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent evt) {
            Property prop = (Property) evt.getSource();
            prop.writeToObject(exampleBean);
            //button.repaint();
         }
      };
      sheet.addPropertySheetChangeListener(listener);
      JTextArea message = new JTextArea();
      //message.setText(PropertySheetMain.RESOURCE.getString("Main.sheet2.message"));
      LookAndFeelTweaks.makeMultilineLabel(message);
      panel.add("North", message);
      add(panel, "*");
   }

   public static void main(String[] args) {
      JFrame frame = new JFrame();
      frame.add(new DynaBeanPropSheet());
      frame.pack();
      frame.setVisible(true);
   }

}
