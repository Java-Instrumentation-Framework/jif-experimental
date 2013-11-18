package tests.test;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Iterator;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CacheLRUTest
{
   Map cache;

   // If the cache is to be used by multiple threads,
   // the cache must be wrapped with code to synchronize the methods
   // cache = (Map)Collections.synchronizedMap(cache);


   public CacheLRUTest () {

      //Implements a Least-Recently-Used (LRU) Cache

      // Create cache
      final int MAX_ENTRIES = 8;
      cache = new LinkedHashMap(MAX_ENTRIES + 1, .75F, true)
      { // This method is called just after a new entry has been added
         public boolean removeEldestEntry (Map.Entry eldest) {
            return size() > MAX_ENTRIES;
         }
      };
   }


   void add (Object key, Object thing) {
      // Add to cache
      cache.put(key, thing);
   }


   Object get (Object key) {
      // Get object
      Object o = cache.get(key);
      if (o == null && !cache.containsKey(key)) {

         // Object not in cache. If null is not a possible value in the cache,
         // the call to cache.contains(key) is not needed
      }
      return o;
   }


   void listContents () {
      System.out.println("Elements in cache:");
      Set set = cache.keySet();
      Iterator iter = set.iterator();
      int i = 1;
      while (iter.hasNext()) {
         System.out.println(" " + i + ") " + cache.get(iter.next()));
         i++;
      }

   }


   public static void main (String[] args) {
      CacheLRUTest cache = new CacheLRUTest();
      cache.add("009782", new Integer(9));
      cache.listContents ();
   }
}
