/*
 * TestJavaBean.java
 *
 * Created on May 18, 2006, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package beans;

public class TestJavaBean extends JavaBean {
   private String foo;
   
   public void setFoo(String newFoo) {
      String old = getFoo();
      this.foo = newFoo;
      firePropertyChange("foo", old, getFoo());
   }
   
   public String getFoo() {
      return foo;
   }
}