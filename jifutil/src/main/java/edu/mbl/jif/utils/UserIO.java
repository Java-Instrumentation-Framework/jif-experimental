/*
 * UserIO.java
 */
package edu.mbl.jif.utils;

import java.io.*;

import java.util.*;


/**This class is part of the
  <A HREF=http://www.mpi-inf.mpg.de/~suchanek/downloads/javatools target=_blank>
  Java Tools
  </A> by <A HREF=http://www.mpi-inf.mpg.de/~suchanek target=_blank>
          Fabian M. Suchanek</A>
  You may use this class if (1) it is not for commercial purposes,
  (2) you give credit to the author and (3) you use the class at your own risk.
  If you use the class for scientific purposes, please cite our paper
  "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents"
  (<A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.pdf target=_blank>pdf</A>,
  <A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.bib target=_blank>bib</A>,
  <A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.ppt target=_blank>ppt</A>
  ). If you would like to use the class for commercial purposes, please contact
  <A HREF=mailto:f.m.suchanek@web.de>Fabian M. Suchanek</A><P>

  This class provides convenience methods for Input/Output.
  Allows to do basic I/O with easy procedure calls
  -- nearly like in normal programming languages.
  Furthermore, the class provides basic set operations for EnumSets.<BR>
  Example:
   <PRE>
      D.p("This is an easy way to write a string");
      // And this is an easy way to read one:
      String s=D.r();

      // Here is a cool way to print something inline
      computeProduct(factor1,(Integer)D.p(factor2));

      enum T {a,b,c};
      EnumSet<T> i=D.intersection(EnumSet.of(T.a,T.b),EnumSet.of(T.b,T.c));
      EnumSet<T> u=D.union(EnumSet.of(T.a,T.b),EnumSet.of(T.b,T.c));
   </PRE>
 */
public class UserIO {
    /** Indentation margin. All methods indent their output by indent spaces */
    public static int indent = 0;

    /** Prints <indent> spaces */
    public static void i() {
        for (int i = 0; i < indent; i++)
            System.out.print(" ");
    }

    /** Prints a new line  */
    public static void p() {
        System.out.println("");
    }

    /** Prints some Objects */
    public static void p(Object... a) {
        i();
        if (a == null) {
            System.out.print("null-array");
            return;
        }
        for (Object o : a)
            System.out.print(o + " ");
        System.out.println("");
    }

    /** Prints an Object */
    public static Object p(Object o) {
        i();
        System.out.println(o);
        return (o);
    }

    /** Prints a String */
    public static String p(String o) {
        i();
        System.out.println(o);
        return (o);
    }

    /** Prints an array of integers*/
    public static int[] p(int[] a) {
        i();
        if (a == null) {
            System.out.print("null-array");
        } else {
            for (int i = 0; i < a.length; i++)
                System.out.print(a[i] + ", ");
        }
        System.out.println("");
        return (a);
    }

    /** Prints an array of doubles*/
    public static double[] p(double[] a) {
        i();
        if (a == null) {
            System.out.print("null-array");
        } else {
            for (int i = 0; i < a.length; i++)
                System.out.print(a[i] + ", ");
        }
        System.out.println("");
        return (a);
    }

    /** Reads a line from the keyboard */
    public static String r() {
        String s = "";
        i();
        try {
            s = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception whocares) {
        }
        return (s);
    }

    /** Reads a long from the keyboard */
    public static long readLong(String question) {
        System.out.print(question);
        return (Long.parseLong(UserIO.r()));
    }

    /** Waits for a number of milliseconds */
    public static void waitMS(long milliseconds) {
        long current = System.currentTimeMillis();
        while (System.currentTimeMillis() < (current + milliseconds)) {
            // wait
        }
    }

    /** Reads a double from the keyboard */
    public static double readDouble(String question) {
        System.out.print(question);
        return (Double.parseDouble(UserIO.r()));
    }

    /** Returns the intersection of two enumsets */
    public static <E extends Enum<E>> EnumSet<E> intersection(EnumSet<E> s1, EnumSet<E> s2) {
        // We have to clone, since retainAll modifies the set
        EnumSet<E> s = s1.clone();
        s.retainAll(s2);
        // I tried coding this for arbitrary sets, but it failed because
        // the interface Cloneable does not make sure that the clone-method
        // is visible (!)
        return (s);
    }

    /** Returns the union of two enumsets */
    public static <E extends Enum<E>> EnumSet<E> union(EnumSet<E> s1, EnumSet<E> s2) {
        EnumSet<E> s = s1.clone();
        s.addAll(s2);
        return (s);
    }

    /** Tells whether the intersection is non-empty */
    public static <E extends Enum<E>> boolean containsOneOf(EnumSet<E> s1, EnumSet<E> s2) {
        return (!intersection(s1, s2).isEmpty());
    }

    /** Exits with error code 0 */
    public static void exit() {
        System.exit(0);
    }

    /** Writes a line to a writer. Yes, this is possible */
    public static void writeln(Writer out, Object s) throws IOException {
        out.write(s.toString());
        out.write("\n");
    }

    /** Executes a command */
    public static void execute(String cmd, File folder)
        throws Exception {
        Process p = Runtime.getRuntime().exec(cmd, null, folder);
        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String s1;
        String s2 = null;
        while ((null != (s1 = bri.readLine())) || (null != (s2 = bre.readLine()))) {
            if (s1 != null) {
                System.out.println(s1);
            }
            if (s2 != null) {
                System.err.println(s2);
            }
        }
        p.waitFor();
    }
}
