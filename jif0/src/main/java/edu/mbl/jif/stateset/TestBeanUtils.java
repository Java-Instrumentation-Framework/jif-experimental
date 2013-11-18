/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.stateset;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author GBHxdscs
 */
public class TestBeanUtils {

  private double camera_exposureStream;

  public static void main(String[] args) {
    TestModelBean t = new TestModelBean();
    try {
      PropertyUtils.setProperty(t, "a", "New Text");
      PropertyUtils.setProperty(t, "text", "New Text");
      PropertyUtils.setProperty(t, "anInteger", new Integer(3));
    } catch (IllegalAccessException ex) {
      Logger.getLogger(TestBeanUtils.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvocationTargetException ex) {
      Logger.getLogger(TestBeanUtils.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchMethodException ex) {
      Logger.getLogger(TestBeanUtils.class.getName()).log(Level.SEVERE, null, ex);

    } catch (Exception ex){
      Logger.getLogger(TestBeanUtils.class.getName()).log(Level.SEVERE, null, ex);
    }

  }
}
