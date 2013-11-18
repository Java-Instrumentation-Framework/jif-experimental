/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.binding;

/**
 *
 * @author GBH
 */
import java.beans.*;
   import java.util.*;
   
   public class IndexedChangeReporter {
   
     private PropertyChangeSupport changeSupport;
   
     private Map<Integer, String> names;
   
     private String title;
   
     public IndexedChangeReporter() {
       changeSupport = new PropertyChangeSupport(this);
       names = new HashMap<Integer, String>();
     }
   
     public void setTitle(String title) {
       String oldTitle = this.title;
       this.title = title;
       changeSupport.firePropertyChange("title", oldTitle, title);
     }
   
     public String getTitle() {
       return title;
     }
   
     public void setName(int index, String name) {
         String oldName = names.get(index);
         names.put(index, name);
         changeSupport.fireIndexedPropertyChange("name", index,
                   oldName, name);
     }
   
     public String getName(int index) {
       return names.get(index);
     }
   
     public void addPropertyChangeListener(
      PropertyChangeListener l) {
         changeSupport.addPropertyChangeListener(l);
     }

     public void removePropertyChangeListener(
      PropertyChangeListener l) {
         changeSupport.removePropertyChangeListener(l);
     }
   
     public static void main(String[] args) throws Exception {
         
       IndexedChangeReporter bean = new IndexedChangeReporter();
       
       PropertyChangeListener listener = 
         new PropertyChangeListener() {
           public void propertyChange(PropertyChangeEvent pce) {
             String name = pce.getPropertyName();
             if (pce instanceof IndexedPropertyChangeEvent) {
               IndexedPropertyChangeEvent ipce =
                 (IndexedPropertyChangeEvent) pce;
               int index = ipce.getIndex();
               System.out.print("Property: " + name +
                 "; index: " + index);
             } else {
               System.out.print("Property: " + name);
             }
             System.out.println("; value: " + pce.getNewValue());
           }
       };
       
       bean.addPropertyChangeListener(listener);
       
       bean.setName(1, "John");
       bean.setName(2, "Ed");
       bean.setName(3, "Mary");
       bean.setName(4, "Joan");
       bean.setTitle("Captain");
       System.out.println("Name at 3 is: " + bean.getName(3));
       System.out.println("Title is: " + bean.getTitle());
     }
   }