package edu.mbl.jif.stateset;

/**
 * L2FProd.com Common Components 7.3 License.
 *
 * Copyright 2005-2007 L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * PropertySheetPage2. <br>
 *  
 */
public class PropertySheetPage extends JPanel {

    BeanInfo beanInfo;
    private Object obj;

    public PropertySheetPage(final Object obj)
      {
        this.obj = obj;
        setLayout(LookAndFeelTweaks.createVerticalPercentLayout());
        String classname = obj.getClass().getName();
        beanInfo = new SimpleBeanInfo();
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }


        final PropertySheetPanel sheet = new PropertySheetPanel();
        sheet.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
        sheet.setToolBarVisible(false);
        sheet.setDescriptionVisible(false);
        //sheet.setRestoreToggleStates(true);
        //new BeanBinder(obj, sheet);

        sheet.setBeanInfo(beanInfo);
           // org.pf.joi.Inspector.inspect(sheet);
        final JPanel panel = new JPanel(LookAndFeelTweaks.createBorderLayout());
        panel.add("Center", sheet);
        //panel.add("East", button);

        // initialize the properties with the value from the object
        // one can use sheet.readFromObject(button)
        // but I encountered some issues with Java Web Start. The method
        // getLocationOnScreen on the button is throwing an exception, it
        // does not happen when not using Web Start. Load properties one
        // by one as follow will do the trick
        Property[] properties = sheet.getProperties();
        for (int i = 0, c = properties.length; i < c; i++) {
            try {
                properties[i].readFromObject(obj);
            } catch (Exception e) {
            }
        }

        // everytime a property change, update the bean with it
        PropertyChangeListener listener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt)
              {
                Property prop = (Property) evt.getSource();
                prop.writeToObject(obj);
                System.out.println("Changed:" + prop);
              }

        };
        sheet.addPropertySheetChangeListener(listener);

        JTextArea message = new JTextArea();
        //message.setText(PropertySheetMain.RESOURCE.getString("Main.sheet2.message"));
        LookAndFeelTweaks.makeMultilineLabel(message);
        panel.add("North", message);

        add(panel, "*");
      }

}
