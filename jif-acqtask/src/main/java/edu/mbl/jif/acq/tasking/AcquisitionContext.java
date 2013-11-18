package edu.mbl.jif.acq.tasking;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Holds the context info for an acquisition
 *
 * @author GBH
 */
public class AcquisitionContext {

   private StepMonitor monitor;
   private String name;
   private final ConcurrentMap<String, Object> map;

   AcquisitionContext(String name) {
      this.name = name;
      map = new ConcurrentHashMap<String, Object>();
   }

   public String getName() {
      return name;
   }

   public ConcurrentMap<String, Object> getMap() {
      return map;
   }

   public Object get(String s) {
      Object value = map.get(s);
      return value;
   }

   public void put(String s, Object obj) {
      map.put(s, obj);
   }

   public void replace(String s, Object obj) {
      map.replace(s, obj);
   }

   public void remove(String s) {
      map.remove(s);
   }

   public StepMonitor getMonitor() {
      return monitor;
   }

   public void setMonitor(StepMonitor monitor) {
      this.monitor = monitor;
   }
}
