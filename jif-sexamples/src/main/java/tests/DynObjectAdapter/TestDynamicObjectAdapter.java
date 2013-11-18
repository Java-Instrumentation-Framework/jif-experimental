/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.DynObjectAdapter;
/**
 *
 * @author GBH
 */
public class TestDynamicObjectAdapter {
   // To implement this.

   public interface Implement {

      public void init();

      public void connect();

      public void disconnect();

      public boolean isConnected();
   }

   // An implementor that does implement.
   public static class Implements implements Implement {

      @Override
      public void init() {
      }

      @Override
      public void connect() {
      }

      @Override
      public void disconnect() {
      }

      @Override
      public boolean isConnected() {
         return false;
      }
   }

   // Extend the INSTANCE in this.
   public enum Extend {

      INSTANCE;
      // Hold my adapted version - thus still a singleton.
      public final Implement adaptedInstance;

      Extend() {
         // Use the constructor to adapt the instance.
         adaptedInstance = DynamicObjectAdapterFactory.adapt(this, Implement.class, new Implements());
      }
   }

   // Provides an INSTANCE that has been extended by an Implements to implement Implement.
   public static Implement getInstance() {
      return Extend.INSTANCE.adaptedInstance;
   }

   public void test() {
      System.out.println("Hello");
      Implement i = getInstance();

   }

   public static void main(String args[]) {
      new TestDynamicObjectAdapter().test();
   }
}