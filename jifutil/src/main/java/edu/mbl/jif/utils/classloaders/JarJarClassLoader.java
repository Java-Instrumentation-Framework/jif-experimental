package edu.mbl.jif.utils.classloaders;

/**
 * Created on Apr 8, 2003
 */
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.io.BufferedInputStream;
//import javas.io.Streams;
//import javas.lang.ClassLoaders;


/**
 * @author john
 */
public class JarJarClassLoader
      extends ClassLoader
{
   /**
    * The list of jars we know about.
    */
   private final List internalJars = new LinkedList();

   /**
    * The Map of class Sets we know about.
    */
   private final Map classMaps = new HashMap();

   /**
    * Constructor for JarJarClassLoader.
    * @param parent
    */
   public JarJarClassLoader (ClassLoader parent) {
      super(parent);
   }


   /**
    * Constructor for JarJarClassLoader.
    */
   public JarJarClassLoader () {
      super();
   }


   public void addInternalJar (String jar) {
      internalJars.add(jar);
   }


   /**
    * @see java.lang.ClassLoader#findClass(String)
    */
   protected Class findClass (String name) throws ClassNotFoundException {
      Class theClass = null;

      if (null != name) {
         for (Iterator itr = internalJars.iterator(); itr.hasNext(); ) {
            String jar = (String) itr.next();

            String filename = ClassLoaders.convertClassNameToFilename(name);

            if (null != (theClass = defineClass(name, jar, filename))) {
               break;
            }
         }
      }

      if (null == theClass) {
         throw new ClassNotFoundException(name);
      }

      return theClass;
   }


   private Class defineClass (String className, String jar, String filename) {
      Class theClass = null;

      Map classMap = (Map) classMaps.get(jar);

      if (null == classMap) {
         classMap = createClassMapForJar(jar);

         if (null != classMap) {
            classMaps.put(jar, classMap);
         }
      }

      if (null != classMap) {
         byte[] classBytes = (byte[]) classMap.get(filename);

         if (null != classBytes) {
            classMap.remove(filename);

            if (classMap.size() <= 0) {
               removeClassMap(classMap);
            }

            theClass = defineClass(className, classBytes, 0, classBytes.length);
         }
      }

      return theClass;
   }


   private void removeClassMap (Map classMap) {
      // TODO: Implement me.
   }


   private Map createClassMapForJar (String jar) {
      Map map = new HashMap();

      try {
         InputStream is = getResourceAsStream(jar);

         if (null != is) {
            JarInputStream jis = new JarInputStream(is);

            JarEntry entry;

            while (null != (entry = jis.getNextJarEntry())) {
               String filename = entry.getName();

               // Technically a bug here if we have class files bigger than Integer.MAX_VALUE
               // but uh... yeah, right...

               int size = (int) entry.getSize();
               byte[] data;

               if (size <= 0) {
                  data = readByteArray(jis);
               } else {
                  data = readByteArray(jis, size);
               }

               if (null != data) {
                  map.put(filename, data);
               }
            }

            jis.close();
         }
      }
      catch (IOException suppressed) {}

      return map;
   }


   public static byte[] readByteArray (InputStream is) throws IOException {
      BufferedInputStream bis = new BufferedInputStream(is);

      int length = 0;

      bis.mark(Integer.MAX_VALUE);

      while ( -1 != bis.read()) {
         length++;
      }

      bis.reset();

      byte[] b = new byte[length];
      bis.read(b, 0, length);

      return b;
   }


   public static byte[] readByteArray (InputStream is,
         int contentLength) throws IOException {
      if (contentLength < 0) {
         return readByteArray(is);
      }

      byte[] b = new byte[contentLength];
      int totalBytesRead = 0;

      while (totalBytesRead < contentLength) {
         int numBytesRead = is.read(b, totalBytesRead, contentLength - totalBytesRead);

         if ( -1 == numBytesRead) {
            throw new IOException("EOF reached before content length (only read "
                  + totalBytesRead + " out of " + contentLength + ")");
         }

         totalBytesRead += numBytesRead;
      }

      return b;
   }


   public static void main (String[] args) throws Exception {
      System.err.println("JJCL Demo...");

      if (args.length != 2) {
         System.err.println(
               "usage: [java] com.jpevans.jjcl.JarJarClassLoader internal_jar classname");
         System.exit( -1);
      }

      String internalJar = args[0];
      String className = args[1];

      JarJarClassLoader jjcl = new JarJarClassLoader();

      jjcl.addInternalJar(internalJar);

      Class theClass = Class.forName(className, true, jjcl);

      Object theInstance = theClass.newInstance();

      Method theMainMethod = theClass.getMethod("main", new Class[] {String[].class});

      theMainMethod.invoke(theInstance, new Object[] {args});
   }
}
