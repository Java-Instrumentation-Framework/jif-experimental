/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binding;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author GBH
 */
public class TestBeanUtilsPropSet {

  public static void main(String[] args) {



    ExampleBindingBean beanA = new ExampleBindingBean();
    ExampleBindingBean beanB = new ExampleBindingBean();
    beanB.setText("I AM BEANb");
    beanA.setText("I AM BEANa");
        System.out.println(beanB.getText());
    try {
      PropertyUtils.setProperty(beanB, "text", "New Text");
      //PropertyUtils.setProperty( book1, "author", new Person( ) );
      //PropertyUtils.setProperty( book1, "author.name", "Ken Coar" );
    } catch (IllegalAccessException ex) {
    } catch (InvocationTargetException ex) {
    } catch (NoSuchMethodException ex) {
    }

    System.out.println(beanB.getText());

  }
}
