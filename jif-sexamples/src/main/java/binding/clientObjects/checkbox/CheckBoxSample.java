/*
 * Copyright 2007 Yegor Jbanov
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package binding.clientObjects.checkbox;

import java.awt.FlowLayout;

import javax.swing.*;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.spicesoft.clientobjects.BeanEnhancer;
import com.spicesoft.clientobjects.BeanRegistry;

/**
 * This examples shows how two GUI elements can be bound to a POJO object
 * that does not support property change listeners.
 * 
 * @author Yegor Jbanov
 */
public class CheckBoxSample {

  public static void main(String[] args) {
    BeanRegistry.registerType(MyPOJO.class);
    
    MyPOJO pojo = new MyPOJO();
    MyPOJO bean = BeanEnhancer.addPropertyChangeSupport(pojo);

    PropertyAdapter adapter = new PropertyAdapter(bean, "booleanValue", true);
    
    JCheckBox box1 = BasicComponentFactory.createCheckBox(adapter, "Boolean Value 1");
    JCheckBox box2 = BasicComponentFactory.createCheckBox(adapter, "Boolean Value 2");

    JFrame frame = new JFrame();
    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(box1);
    frame.getContentPane().add(box2);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
