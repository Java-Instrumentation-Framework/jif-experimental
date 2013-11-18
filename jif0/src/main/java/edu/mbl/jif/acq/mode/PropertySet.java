/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.mode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;


/**
 *
 * @author GBH
 */
public class PropertySet extends Context {

  List<Property> props = new ArrayList();

  public void addProp(String name, Object clazz, String propName) {
    props.add(new Property(name, clazz, propName));
  }

  public void read() {
    try {
      for (Property p : props) {
        p.value = PropertyUtils.getProperty(p.clazz, p.propName);
      }
    } catch (IllegalAccessException ex) {
      Logger.getLogger(PropertySet.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvocationTargetException ex) {
      Logger.getLogger(PropertySet.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchMethodException ex) {
      Logger.getLogger(PropertySet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void write() {
    try {
      for (Property p : props) {
        PropertyUtils.setProperty(p.clazz, p.propName, p.value);
      }
    } catch (IllegalAccessException ex) {
      Logger.getLogger(PropertySet.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvocationTargetException ex) {
      Logger.getLogger(PropertySet.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchMethodException ex) {
      Logger.getLogger(PropertySet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void dump() {

    for (Property p : props) {
      System.out.println(p.name + " : " + p.value);
    }
  }


}
class Property {

  String name;
  Object clazz;
  String propName;
  Object value;

  public Property(String name, Object clazz, String propName) {
    this.name = name;
    this.clazz = clazz;
    this.propName = propName;
  }
}