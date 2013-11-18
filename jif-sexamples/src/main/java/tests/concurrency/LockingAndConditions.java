/*
 * LockingAndConditions.java
 * Created on May 5, 2006, 11:05 AM
 */
package tests.concurrency;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class LockingAndConditions {
   public LockingAndConditions() {
   }

   // Locking - requires final{}
   /*
   Lock lock = new ReentrantLock();
   ...
   lock.lock();
   try {
     // perform operations protected by lock
   }
   catch(Exception ex) {
     // restore invariants
   }
   finally {
     lock.unlock();
   }
    */

   //===========================================
   // ReadWriteLock Example
   class RWDictionary {
      private final Map<String, Object> m = new TreeMap<String, Object>();
      private final ReadWriteLock       rwl = new ReentrantReadWriteLock();
      private final Lock                r = rwl.readLock();
      private final Lock                w = rwl.writeLock();

      public Object get(String key) {
         r.lock();
         try {
            return m.get(key);
         } finally {
            r.unlock();
         }
      }

      public Object put(String key, Object value) {
         w.lock();
         try {
            return m.put(key, value);
         } finally {
            w.unlock();
         }
      }

      public void clear() {
         w.lock();
         try {
            m.clear();
         } finally {
            w.unlock();
         }
      }
   }

   //=============================================
   // Condition Example
   class BoundedBuffer {
      Lock      lock = new ReentrantLock();
      Condition notFull = lock.newCondition();
      Condition notEmpty = lock.newCondition();
      Object[]  items = new Object[100];
      int       putptr;
      int       takeptr;
      int       count;

      public void put(Object x) throws Exception {
         lock.lock();
         try {
            while (count == items.length)
               notFull.await();
            items[putptr] = x;
            if (++putptr == items.length) {
               putptr = 0;
            }
            ++count;
            notEmpty.signal();
         } finally {
            lock.unlock();
         }
      }

      public Object take() throws Exception {
         lock.lock();
         try {
            while (count == 0)
               notEmpty.await();
            Object x = items[takeptr];
            if (++takeptr == items.length) {
               takeptr = 0;
            }
            --count;
            notFull.signal();
            return x;
         } finally {
            lock.unlock();
         }
      }
   }

   class CubbyHole2 {
      private int       contents;
      private boolean   available = false;
      private Lock      aLock = new ReentrantLock();
      private Condition condVar = aLock.newCondition();

      public int get(int who) {
         aLock.lock();
         try {
            while (available == false) {
               try {
                  condVar.await();
               } catch (InterruptedException e) {
               }
            }
            available = false;
            System.out.format("Consumer %d got: %d%n", who, contents);
            condVar.signalAll();
         } finally {
            aLock.unlock();
         }
         return contents;
      }

      public void put(int who, int value) {
         aLock.lock();
         try {
            while (available == true) {
               try {
                  condVar.await();
               } catch (InterruptedException e) {
               }
            }
            contents     = value;
            available    = true;
            System.out.format("Producer %d put: %d%n", who, contents);
            condVar.signalAll();
         } finally {
            aLock.unlock();
         }
      }
   }
}
