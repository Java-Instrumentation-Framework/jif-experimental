/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stateset.dynclass;

/**
 *
 * @author GBH
 */
import edu.mbl.jif.io.xml.ObjectStoreXML;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import edu.mbl.jif.stateset.dynclass.BeanCreator;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.File;

/**
 * Check basic operation of BeanCreator class.
 */
public class TestDynClassBean {

    private HashMap hm;
    private Object nonPropertyKey = new Object();
    // try the read and write methods on the descriptor
    private void checkProperties(Object bean,
        PropertyDescriptor desc) throws Exception
      {

        String name = desc.getName();
        // read the value
        Method readMethod = desc.getReadMethod();
        Object value = readMethod.invoke(bean, (Object[]) null);

        // write the value
        Method writeMethod = desc.getWriteMethod();
        Object newValue = "newValueOf" + name;
        writeMethod.invoke(bean, new Object[]{newValue});

        // read the value again
        value = readMethod.invoke(bean, (Object[]) null);

        System.out.println(name + ": \n    " + readMethod + "\n    " + writeMethod);
      }

    private void performTest(Object bean) throws Exception
      {
        BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] pd = bi.getPropertyDescriptors();
        for (int i = 0; i < pd.length; i++) {
            PropertyDescriptor desc = pd[i];
            if (hm.keySet().contains(desc.getName())) {
                checkProperties(bean, desc);

                // remove name - at end only non-property keys will remain
                hm.remove(desc.getName());
            }
        }
      }

    Object generatedBean;

    public void testGeneratedBean() throws Exception
      {
        hm = new HashMap();

        // the first two properties should be recognized by the
        // bean creator;  others will be ignored
        hm.put("goodProp1", new Integer(17));
        hm.put("aBoolean", true);
        hm.put("anotherBoolean", new Boolean(true));

        hm.put("URL", new Integer(96));
        hm.put("OddPropName", ""); // odd because initial capital is confusing
        hm.put(nonPropertyKey, "that key is not even a string");
        hm.put(this, this);

        try {
            ObjectStoreXML.write(hm, "hashmap");
        } catch (Throwable ex) {
            ex.printStackTrace();
            Logger.getLogger(TestDynClassBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        generatedBean = BeanCreator.createBeanFromMap(hm);
        performTest(generatedBean);
        // check that unexpected keys did not become properties
        HashSet expected = new HashSet();
        expected.addAll(Arrays.asList(new Object[]{
                nonPropertyKey,
                this
            }));
      }

    public void testCreateClass() throws Exception
      {
        // create a bean from a list of properties and 
        String[] props = {"goodProp1", "URL"};
        Class beanClass = BeanCreator.createClassForProperties(props);
        for (int i = 0; i < 2; i++) {
            hm = new HashMap();
            hm.put("goodProp1", new Integer(17));
            hm.put("URL", new Integer(96));
            Object bean = BeanCreator.createBean(beanClass, hm);
        }
      }

    public void testSavedPropMap()
      {
        SavePropMap prop = new SavePropMap();
        prop.setNameOfModel("Map1");
        prop.addProp("goodProp1", new Integer(17));
        prop.addProp("URL", new Integer(96));
        SavePropMap prop2 = new SavePropMap();
        prop2.setNameOfModel("Map2");
        prop2.addProp("goodProp2", new Integer(78));
        prop2.addProp("URL2", new Integer(2));
        propSet = new SavePropMap[]{prop, prop2};
        try {
            ObjectStoreXML.write(propSet, "propSet");
        } catch (Throwable ex) {
            ex.printStackTrace();
            Logger.getLogger(TestDynClassBean.class.getName()).log(Level.SEVERE, null, ex);
        }

      }

    public static void main(String[] args)
      {
        try {
            TestDynClassBean tdcb = new TestDynClassBean();
            tdcb.testCreateClass();
            tdcb.testGeneratedBean();
            tdcb.testSavedPropMap();
//            Object normalBean = new NormalBean();
//            BeanInfo normalBi = Introspector.getBeanInfo(normalBean.getClass());
//            BeanInfo generatedBi = Introspector.getBeanInfo(tdcb.generatedBean.getClass());
//            org.pf.joi.Inspector.inspect(normalBi);
//            org.pf.joi.Inspector.inspect(generatedBi);
        } catch (Exception ex) {
            Logger.getLogger(TestDynClassBean.class.getName()).log(Level.SEVERE, null, ex);
        }

      }

    SavePropMap[] propSet;

    public class SavePropMap {

        String nameOfModel;
        HashMap hm = new HashMap();

        public String getNameOfModel()
          {
            return nameOfModel;
          }

        public void setNameOfModel(String nameOfModel)
          {
            this.nameOfModel = nameOfModel;
          }

        public void addProp(String name, Object value)
          {
            hm.put(name, value);
          }

    }

    public static class NormalBean {

        private String name;

        public String getName()
          {
            return name;
          }

        public void setName(String name)
          {
            this.name = name;
          }

        private String text;

        public String getText()
          {
            return text;
          }

        public void setText(String text)
          {
            this.text = text;
          }

        private long time;

        public long getTime()
          {
            return time;
          }

        public void setTime(long time)
          {
            this.time = time;
          }

        public String getVersion()
          {
            return "1.0";
          }

        private boolean visible;

        public boolean isVisible()
          {
            return visible;
          }

        public void setVisible(boolean visible)
          {
            this.visible = visible;
          }

        private int id;

        public int getId()
          {
            return id;
          }

        public void setId(int id)
          {
            this.id = id;
          }

        private File path;

        public File getPath()
          {
            return path;
          }

        public void setPath(File path)
          {
            this.path = path;
          }

        private Color color = Color.blue;

        public Color getColor()
          {
            return color;
          }

        public void setColor(Color color)
          {
            this.color = color;
          }

        private double doubleValue = 121210.4343543;

        public void setADouble(double d)
          {
            this.doubleValue = d;
          }

        public double getADouble()
          {
            return doubleValue;
          }

        private String season;

        public void setSeason(String s)
          {
            season = s;
          }

        public String getSeason()
          {
            return season;
          }

        private String constrained;

        public String getConstrained()
          {
            return constrained;
          }

        public void setConstrained(String constrained) throws PropertyVetoException
          {
            if ("blah".equals(constrained)) {
                throw new PropertyVetoException("e",
                    new PropertyChangeEvent(this, "constrained", this.constrained,
                    constrained));
            }
            this.constrained = constrained;
          }

        public String toString()
          {
            return "[name=" + getName() + ",text=" + getText() + ",time=" + getTime() + ",version=" + getVersion() + ",visible=" + isVisible() + ",id=" + getId() + ",path=" + getPath() + ",aDouble=" + getADouble() +
                ",season=" + getSeason() + "]";
          }

    }
}
