package edu.mbl.jif.camera;

import edu.mbl.jif.fabric.Application;

import java.awt.BorderLayout;
import java.awt.Rectangle;

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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.binding.PresentationModel;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.LookAndFeelTweaks;

/**
 * PropertySheet. ExampleBeanPropSheet>
 * Usage:
 *      PropertySheetDisplay.showPropSheet(Model)
 *
 */
public class PropertySheetDisplay extends JPanel {

    PresentationModel exb;

    public PropertySheetDisplay(final PresentationModel exb) {
        this.exb = exb;
        setLayout(LookAndFeelTweaks.createVerticalPercentLayout());

        final JButton button = new JButton();
        button.setText("Change my properties!");
        //final CameraModelZ exb = new CameraModelZ();
        // >>>>> NOTE use of SimpleBeanInfo....
        BeanInfo beanInfo = new SimpleBeanInfo();
        try {
            beanInfo = Introspector.getBeanInfo(exb.getBean().getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        final PropertySheetPanel sheet = new PropertySheetPanel();
        sheet.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
        sheet.setToolBarVisible(true);
        sheet.setDescriptionVisible(false);
        sheet.setBeanInfo(beanInfo);

        final JPanel panel = new JPanel(LookAndFeelTweaks.createBorderLayout());
        panel.add("Center", sheet);
        // panel.add("East", button);

        // initialize the properties with the value from the object
        // one can use sheet.readFromObject(button)
        // but I encountered some issues with Java Web Start. The method
        // getLocationOnScreen on the button is throwing an exception, it
        // does not happen when not using Web Start. Load properties one
        // by one as follow will do the trick
        Property[] properties = sheet.getProperties();
        for (int i = 0, c = properties.length; i < c; i++) {
            try {
                properties[i].readFromObject(exb.getBean());
            } catch (Exception e) {
            }
        }

        // everytime a property change, update the button with it
        //      PropertyChangeListener listener = new PropertyChangeListener()
        //      {
        //         public void propertyChange (PropertyChangeEvent evt) {
        //            Property prop = (Property) evt.getSource();
        //            prop.writeToObject(exb.getBean());
        //            //button.repaint();
        //         }
        //      };
        //sheet.addPropertySheetChangeListener(listener);
        PropertyChangeListener listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String changedProp = evt.getPropertyName();
                if (Application.isDebug()) {
                    System.out.print("PropSheet Event: " + evt.getPropertyName());
                    System.out.println(" " + evt.getNewValue());
                    // PresentationModel pm = (PresentationModel)  evt.getSource();
                    Property[] properties = sheet.getProperties();
                    for (int i = 0, c = properties.length; i < c; i++) {
                        properties[i].isEditable();
                        //System.out.println(properties[i].getName());
                        if (changedProp.matches(properties[i].getName())) {
                            properties[i].setValue(evt.getNewValue());
//                        System.out.println("PropSheet: "
//                           + properties[i].getName() + " set to "
//                           + evt.getNewValue());
                        }
                    //                    try {
                    //                        properties[i].readFromObject(pm.getBean());
                    //                        //properties[i].setValue()
                    //                    } catch (Exception e) {
                    //                    }
                    }
                }
            //button.repaint();
            }

        };
        exb.addBeanPropertyChangeListener(listener);
        JTextArea message = new JTextArea();
        //message.setText(PropertySheetMain.RESOURCE.getString("Main.sheet2.message"));
        LookAndFeelTweaks.makeMultilineLabel(message);
        panel.add("North", message);
        add(panel, "*");
    }

    public void showPropSheet() {
        showPropSheet(new Rectangle(100, 100, 400, 600));
    }

    public void showPropSheet(Rectangle bounds) {
//      try {
//         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//      } catch (UnsupportedLookAndFeelException ex) {
//         ex.printStackTrace();
//      } catch (InstantiationException ex) {
//         ex.printStackTrace();
//      } catch (IllegalAccessException ex) {
//         ex.printStackTrace();
//      } catch (ClassNotFoundException ex) {
//         ex.printStackTrace();
//      }
      LookAndFeelTweaks.tweak();
        String name = exb.getBean().getClass().getName();
        JFrame frame = new JFrame(name.substring(name.length() - 20, name.length() - 1));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("Center", this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setBounds(bounds);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        CameraModelZ exb = new CameraModelZ();
        PropertySheetDisplay psd = new PropertySheetDisplay(new PresentationModel(exb));
        psd.showPropSheet();
    }

}
