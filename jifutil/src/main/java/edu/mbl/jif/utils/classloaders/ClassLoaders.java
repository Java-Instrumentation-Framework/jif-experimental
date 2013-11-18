package edu.mbl.jif.utils.classloaders;

/**
 * Created on Apr 8, 2003
 * The ClassLoaders class is a utility class which contains utility methods
 * for dealing with classes of the type java.lang.ClassLoader.
 * @author John Evans (john@jpevans.com)
 */
public class ClassLoaders
{
   /**
    * Private Constructor to prevent instantiation of utility class.
    */
   private ClassLoaders () {
      super();
   }


   public static String convertFilenameToClassName (String filename) {
      String sub = substitute(filename, "/", ".");

      if (sub.endsWith(".class")) {
         sub = sub.substring(0, sub.length() - ".class".length());
      }

      return sub;
   }


   public static String convertClassNameToFilename (String className) {
      String sub = substitute(className, ".", "/");

      if (!sub.endsWith(".class")) {
         sub += ".class";
      }

      return sub;
   }


   public static String substitute (String orig, String find, String replace) {
      StringBuffer sb = new StringBuffer(orig.length());

      int last = 0;
      int start = orig.indexOf(find);

      while (start >= 0) {
         sb.append(orig.substring(last, start));
         sb.append(replace);

         last = start + find.length();
         start = orig.indexOf(find, last);
      }

      if (last < orig.length()) {
         sb.append(orig.substring(last));
      }

      return sb.toString();
   }

}
