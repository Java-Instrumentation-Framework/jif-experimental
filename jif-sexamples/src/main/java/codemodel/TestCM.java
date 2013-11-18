/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package codemodel;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class TestCM {

   public void test() {
      try {
         JCodeModel cm = new JCodeModel();
         JDefinedClass dc = cm._class("foo.Bar");
         JMethod m = dc.method(0, int.class, "foo");
         m.body()._return(JExpr.lit(5));
         File file = new File("./target/classes");
         file.mkdirs();
         cm.build(file);
      } catch (JClassAlreadyExistsException ex) {
         Logger.getLogger(TestCM.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(TestCM.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   public static void main(String[] args) {
      new TestCM().test();
   }

}
